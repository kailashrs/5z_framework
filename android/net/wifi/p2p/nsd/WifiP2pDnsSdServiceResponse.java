package android.net.wifi.p2p.nsd;

import android.net.wifi.p2p.WifiP2pDevice;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WifiP2pDnsSdServiceResponse
  extends WifiP2pServiceResponse
{
  private static final Map<Integer, String> sVmpack = new HashMap();
  private String mDnsQueryName;
  private int mDnsType;
  private String mInstanceName;
  private final HashMap<String, String> mTxtRecord = new HashMap();
  private int mVersion;
  
  static
  {
    sVmpack.put(Integer.valueOf(12), "_tcp.local.");
    sVmpack.put(Integer.valueOf(17), "local.");
    sVmpack.put(Integer.valueOf(28), "_udp.local.");
  }
  
  protected WifiP2pDnsSdServiceResponse(int paramInt1, int paramInt2, WifiP2pDevice paramWifiP2pDevice, byte[] paramArrayOfByte)
  {
    super(1, paramInt1, paramInt2, paramWifiP2pDevice, paramArrayOfByte);
    if (parse()) {
      return;
    }
    throw new IllegalArgumentException("Malformed bonjour service response");
  }
  
  static WifiP2pDnsSdServiceResponse newInstance(int paramInt1, int paramInt2, WifiP2pDevice paramWifiP2pDevice, byte[] paramArrayOfByte)
  {
    if (paramInt1 != 0) {
      return new WifiP2pDnsSdServiceResponse(paramInt1, paramInt2, paramWifiP2pDevice, null);
    }
    try
    {
      paramWifiP2pDevice = new WifiP2pDnsSdServiceResponse(paramInt1, paramInt2, paramWifiP2pDevice, paramArrayOfByte);
      return paramWifiP2pDevice;
    }
    catch (IllegalArgumentException paramWifiP2pDevice)
    {
      paramWifiP2pDevice.printStackTrace();
    }
    return null;
  }
  
  private boolean parse()
  {
    if (mData == null) {
      return true;
    }
    Object localObject = new DataInputStream(new ByteArrayInputStream(mData));
    mDnsQueryName = readDnsName((DataInputStream)localObject);
    if (mDnsQueryName == null) {
      return false;
    }
    try
    {
      mDnsType = ((DataInputStream)localObject).readUnsignedShort();
      mVersion = ((DataInputStream)localObject).readUnsignedByte();
      if (mDnsType == 12)
      {
        localObject = readDnsName((DataInputStream)localObject);
        if (localObject == null) {
          return false;
        }
        if (((String)localObject).length() <= mDnsQueryName.length()) {
          return false;
        }
        mInstanceName = ((String)localObject).substring(0, ((String)localObject).length() - mDnsQueryName.length() - 1);
        return true;
      }
      if (mDnsType == 16) {
        return readTxtData((DataInputStream)localObject);
      }
      return false;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return false;
  }
  
  private String readDnsName(DataInputStream paramDataInputStream)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    HashMap localHashMap = new HashMap(sVmpack);
    if (mDnsQueryName != null) {
      localHashMap.put(Integer.valueOf(39), mDnsQueryName);
    }
    try
    {
      for (;;)
      {
        int i = paramDataInputStream.readUnsignedByte();
        if (i == 0) {
          return localStringBuffer.toString();
        }
        if (i == 192)
        {
          paramDataInputStream = (String)localHashMap.get(Integer.valueOf(paramDataInputStream.readUnsignedByte()));
          if (paramDataInputStream == null) {
            return null;
          }
          localStringBuffer.append(paramDataInputStream);
          return localStringBuffer.toString();
        }
        byte[] arrayOfByte = new byte[i];
        paramDataInputStream.readFully(arrayOfByte);
        String str = new java/lang/String;
        str.<init>(arrayOfByte);
        localStringBuffer.append(str);
        localStringBuffer.append(".");
      }
      return null;
    }
    catch (IOException paramDataInputStream)
    {
      paramDataInputStream.printStackTrace();
    }
  }
  
  private boolean readTxtData(DataInputStream paramDataInputStream)
  {
    try
    {
      while (paramDataInputStream.available() > 0)
      {
        int i = paramDataInputStream.readUnsignedByte();
        if (i == 0) {
          break;
        }
        byte[] arrayOfByte = new byte[i];
        paramDataInputStream.readFully(arrayOfByte);
        Object localObject = new java/lang/String;
        ((String)localObject).<init>(arrayOfByte);
        localObject = ((String)localObject).split("=");
        if (localObject.length != 2) {
          return false;
        }
        mTxtRecord.put(localObject[0], localObject[1]);
      }
      return true;
    }
    catch (IOException paramDataInputStream)
    {
      paramDataInputStream.printStackTrace();
    }
    return false;
  }
  
  public String getDnsQueryName()
  {
    return mDnsQueryName;
  }
  
  public int getDnsType()
  {
    return mDnsType;
  }
  
  public String getInstanceName()
  {
    return mInstanceName;
  }
  
  public Map<String, String> getTxtRecord()
  {
    return mTxtRecord;
  }
  
  public int getVersion()
  {
    return mVersion;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("serviceType:DnsSd(");
    localStringBuffer.append(mServiceType);
    localStringBuffer.append(")");
    localStringBuffer.append(" status:");
    localStringBuffer.append(WifiP2pServiceResponse.Status.toString(mStatus));
    localStringBuffer.append(" srcAddr:");
    localStringBuffer.append(mDevice.deviceAddress);
    localStringBuffer.append(" version:");
    localStringBuffer.append(String.format("%02x", new Object[] { Integer.valueOf(mVersion) }));
    localStringBuffer.append(" dnsName:");
    localStringBuffer.append(mDnsQueryName);
    localStringBuffer.append(" TxtRecord:");
    Iterator localIterator = mTxtRecord.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      localStringBuffer.append(" key:");
      localStringBuffer.append(str);
      localStringBuffer.append(" value:");
      localStringBuffer.append((String)mTxtRecord.get(str));
    }
    if (mInstanceName != null)
    {
      localStringBuffer.append(" InsName:");
      localStringBuffer.append(mInstanceName);
    }
    return localStringBuffer.toString();
  }
}
