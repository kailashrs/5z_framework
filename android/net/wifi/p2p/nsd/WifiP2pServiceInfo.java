package android.net.wifi.p2p.nsd;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class WifiP2pServiceInfo
  implements Parcelable
{
  public static final Parcelable.Creator<WifiP2pServiceInfo> CREATOR = new Parcelable.Creator()
  {
    public WifiP2pServiceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      ArrayList localArrayList = new ArrayList();
      paramAnonymousParcel.readStringList(localArrayList);
      return new WifiP2pServiceInfo(localArrayList);
    }
    
    public WifiP2pServiceInfo[] newArray(int paramAnonymousInt)
    {
      return new WifiP2pServiceInfo[paramAnonymousInt];
    }
  };
  public static final int SERVICE_TYPE_ALL = 0;
  public static final int SERVICE_TYPE_BONJOUR = 1;
  public static final int SERVICE_TYPE_UPNP = 2;
  public static final int SERVICE_TYPE_VENDOR_SPECIFIC = 255;
  public static final int SERVICE_TYPE_WS_DISCOVERY = 3;
  private List<String> mQueryList;
  
  protected WifiP2pServiceInfo(List<String> paramList)
  {
    if (paramList != null)
    {
      mQueryList = paramList;
      return;
    }
    throw new IllegalArgumentException("query list cannot be null");
  }
  
  static String bin2HexStr(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramArrayOfByte.length;
    int j = 0;
    while (j < i)
    {
      int k = paramArrayOfByte[j];
      try
      {
        String str = Integer.toHexString(k & 0xFF);
        if (str.length() == 1) {
          localStringBuffer.append('0');
        }
        localStringBuffer.append(str);
        j++;
      }
      catch (Exception paramArrayOfByte)
      {
        paramArrayOfByte.printStackTrace();
        return null;
      }
    }
    return localStringBuffer.toString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof WifiP2pServiceInfo)) {
      return false;
    }
    paramObject = (WifiP2pServiceInfo)paramObject;
    return mQueryList.equals(mQueryList);
  }
  
  public List<String> getSupplicantQueryList()
  {
    return mQueryList;
  }
  
  public int hashCode()
  {
    int i;
    if (mQueryList == null) {
      i = 0;
    } else {
      i = mQueryList.hashCode();
    }
    return 31 * 17 + i;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStringList(mQueryList);
  }
}
