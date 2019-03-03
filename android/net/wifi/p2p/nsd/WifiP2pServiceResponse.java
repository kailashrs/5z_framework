package android.net.wifi.p2p.nsd;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WifiP2pServiceResponse
  implements Parcelable
{
  public static final Parcelable.Creator<WifiP2pServiceResponse> CREATOR = new Parcelable.Creator()
  {
    public WifiP2pServiceResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      int k = paramAnonymousParcel.readInt();
      WifiP2pDevice localWifiP2pDevice = (WifiP2pDevice)paramAnonymousParcel.readParcelable(null);
      int m = paramAnonymousParcel.readInt();
      byte[] arrayOfByte = null;
      if (m > 0)
      {
        arrayOfByte = new byte[m];
        paramAnonymousParcel.readByteArray(arrayOfByte);
      }
      if (i == 1) {
        return WifiP2pDnsSdServiceResponse.newInstance(j, k, localWifiP2pDevice, arrayOfByte);
      }
      if (i == 2) {
        return WifiP2pUpnpServiceResponse.newInstance(j, k, localWifiP2pDevice, arrayOfByte);
      }
      return new WifiP2pServiceResponse(i, j, k, localWifiP2pDevice, arrayOfByte);
    }
    
    public WifiP2pServiceResponse[] newArray(int paramAnonymousInt)
    {
      return new WifiP2pServiceResponse[paramAnonymousInt];
    }
  };
  private static int MAX_BUF_SIZE = 1024;
  protected byte[] mData;
  protected WifiP2pDevice mDevice;
  protected int mServiceType;
  protected int mStatus;
  protected int mTransId;
  
  protected WifiP2pServiceResponse(int paramInt1, int paramInt2, int paramInt3, WifiP2pDevice paramWifiP2pDevice, byte[] paramArrayOfByte)
  {
    mServiceType = paramInt1;
    mStatus = paramInt2;
    mTransId = paramInt3;
    mDevice = paramWifiP2pDevice;
    mData = paramArrayOfByte;
  }
  
  private boolean equals(Object paramObject1, Object paramObject2)
  {
    if ((paramObject1 == null) && (paramObject2 == null)) {
      return true;
    }
    if (paramObject1 != null) {
      return paramObject1.equals(paramObject2);
    }
    return false;
  }
  
  private static byte[] hexStr2Bin(String paramString)
  {
    int i = paramString.length() / 2;
    byte[] arrayOfByte = new byte[paramString.length() / 2];
    int j = 0;
    while (j < i) {
      try
      {
        arrayOfByte[j] = ((byte)(byte)Integer.parseInt(paramString.substring(j * 2, j * 2 + 2), 16));
        j++;
      }
      catch (Exception paramString)
      {
        paramString.printStackTrace();
        return null;
      }
    }
    return arrayOfByte;
  }
  
  public static List<WifiP2pServiceResponse> newInstance(String paramString, byte[] paramArrayOfByte)
  {
    ArrayList localArrayList = new ArrayList();
    WifiP2pDevice localWifiP2pDevice = new WifiP2pDevice();
    deviceAddress = paramString;
    if (paramArrayOfByte == null) {
      return null;
    }
    paramArrayOfByte = new DataInputStream(new ByteArrayInputStream(paramArrayOfByte));
    try
    {
      while (paramArrayOfByte.available() > 0)
      {
        int i = paramArrayOfByte.readUnsignedByte() + (paramArrayOfByte.readUnsignedByte() << 8) - 3;
        int j = paramArrayOfByte.readUnsignedByte();
        int k = paramArrayOfByte.readUnsignedByte();
        int m = paramArrayOfByte.readUnsignedByte();
        if (i < 0) {
          return null;
        }
        if (i == 0)
        {
          if (m == 0)
          {
            paramString = new android/net/wifi/p2p/nsd/WifiP2pServiceResponse;
            paramString.<init>(j, m, k, localWifiP2pDevice, null);
            localArrayList.add(paramString);
          }
        }
        else if (i > MAX_BUF_SIZE)
        {
          paramArrayOfByte.skip(i);
        }
        else
        {
          paramString = new byte[i];
          paramArrayOfByte.readFully(paramString);
          if (j == 1) {
            paramString = WifiP2pDnsSdServiceResponse.newInstance(m, k, localWifiP2pDevice, paramString);
          }
          for (;;)
          {
            break;
            if (j == 2) {
              paramString = WifiP2pUpnpServiceResponse.newInstance(m, k, localWifiP2pDevice, paramString);
            } else {
              paramString = new WifiP2pServiceResponse(j, m, k, localWifiP2pDevice, paramString);
            }
          }
          if ((paramString != null) && (paramString.getStatus() == 0)) {
            localArrayList.add(paramString);
          }
        }
      }
      return localArrayList;
    }
    catch (IOException paramString)
    {
      paramString.printStackTrace();
      if (localArrayList.size() > 0) {
        return localArrayList;
      }
    }
    return null;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof WifiP2pServiceResponse)) {
      return false;
    }
    paramObject = (WifiP2pServiceResponse)paramObject;
    if ((mServiceType != mServiceType) || (mStatus != mStatus) || (!equals(mDevice.deviceAddress, mDevice.deviceAddress)) || (!Arrays.equals(mData, mData))) {
      bool = false;
    }
    return bool;
  }
  
  public byte[] getRawData()
  {
    return mData;
  }
  
  public int getServiceType()
  {
    return mServiceType;
  }
  
  public WifiP2pDevice getSrcDevice()
  {
    return mDevice;
  }
  
  public int getStatus()
  {
    return mStatus;
  }
  
  public int getTransactionId()
  {
    return mTransId;
  }
  
  public int hashCode()
  {
    int i = mServiceType;
    int j = mStatus;
    int k = mTransId;
    String str = mDevice.deviceAddress;
    int m = 0;
    int n;
    if (str == null) {
      n = 0;
    } else {
      n = mDevice.deviceAddress.hashCode();
    }
    if (mData != null) {
      m = Arrays.hashCode(mData);
    }
    return 31 * (31 * (31 * (31 * (31 * 17 + i) + j) + k) + n) + m;
  }
  
  public void setSrcDevice(WifiP2pDevice paramWifiP2pDevice)
  {
    if (paramWifiP2pDevice == null) {
      return;
    }
    mDevice = paramWifiP2pDevice;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("serviceType:");
    localStringBuffer.append(mServiceType);
    localStringBuffer.append(" status:");
    localStringBuffer.append(Status.toString(mStatus));
    localStringBuffer.append(" srcAddr:");
    localStringBuffer.append(mDevice.deviceAddress);
    localStringBuffer.append(" data:");
    localStringBuffer.append(Arrays.toString(mData));
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mServiceType);
    paramParcel.writeInt(mStatus);
    paramParcel.writeInt(mTransId);
    paramParcel.writeParcelable(mDevice, paramInt);
    if ((mData != null) && (mData.length != 0))
    {
      paramParcel.writeInt(mData.length);
      paramParcel.writeByteArray(mData);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
  
  public static class Status
  {
    public static final int BAD_REQUEST = 3;
    public static final int REQUESTED_INFORMATION_NOT_AVAILABLE = 2;
    public static final int SERVICE_PROTOCOL_NOT_AVAILABLE = 1;
    public static final int SUCCESS = 0;
    
    private Status() {}
    
    public static String toString(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return "UNKNOWN";
      case 3: 
        return "BAD_REQUEST";
      case 2: 
        return "REQUESTED_INFORMATION_NOT_AVAILABLE";
      case 1: 
        return "SERVICE_PROTOCOL_NOT_AVAILABLE";
      }
      return "SUCCESS";
    }
  }
}
