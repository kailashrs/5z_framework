package android.net.wifi.p2p.nsd;

import android.net.wifi.p2p.WifiP2pDevice;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WifiP2pUpnpServiceResponse
  extends WifiP2pServiceResponse
{
  private List<String> mUniqueServiceNames;
  private int mVersion;
  
  protected WifiP2pUpnpServiceResponse(int paramInt1, int paramInt2, WifiP2pDevice paramWifiP2pDevice, byte[] paramArrayOfByte)
  {
    super(2, paramInt1, paramInt2, paramWifiP2pDevice, paramArrayOfByte);
    if (parse()) {
      return;
    }
    throw new IllegalArgumentException("Malformed upnp service response");
  }
  
  static WifiP2pUpnpServiceResponse newInstance(int paramInt1, int paramInt2, WifiP2pDevice paramWifiP2pDevice, byte[] paramArrayOfByte)
  {
    if (paramInt1 != 0) {
      return new WifiP2pUpnpServiceResponse(paramInt1, paramInt2, paramWifiP2pDevice, null);
    }
    try
    {
      paramWifiP2pDevice = new WifiP2pUpnpServiceResponse(paramInt1, paramInt2, paramWifiP2pDevice, paramArrayOfByte);
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
    int i = mData.length;
    int j = 0;
    if (i < 1) {
      return false;
    }
    mVersion = (mData[0] & 0xFF);
    String[] arrayOfString = new String(mData, 1, mData.length - 1).split(",");
    mUniqueServiceNames = new ArrayList();
    i = arrayOfString.length;
    while (j < i)
    {
      String str = arrayOfString[j];
      mUniqueServiceNames.add(str);
      j++;
    }
    return true;
  }
  
  public List<String> getUniqueServiceNames()
  {
    return mUniqueServiceNames;
  }
  
  public int getVersion()
  {
    return mVersion;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("serviceType:UPnP(");
    localStringBuffer.append(mServiceType);
    localStringBuffer.append(")");
    localStringBuffer.append(" status:");
    localStringBuffer.append(WifiP2pServiceResponse.Status.toString(mStatus));
    localStringBuffer.append(" srcAddr:");
    localStringBuffer.append(mDevice.deviceAddress);
    localStringBuffer.append(" version:");
    localStringBuffer.append(String.format("%02x", new Object[] { Integer.valueOf(mVersion) }));
    if (mUniqueServiceNames != null)
    {
      Iterator localIterator = mUniqueServiceNames.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localStringBuffer.append(" usn:");
        localStringBuffer.append(str);
      }
    }
    return localStringBuffer.toString();
  }
}
