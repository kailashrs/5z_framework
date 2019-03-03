package android.net.wifi.p2p.nsd;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Locale;

public class WifiP2pServiceRequest
  implements Parcelable
{
  public static final Parcelable.Creator<WifiP2pServiceRequest> CREATOR = new Parcelable.Creator()
  {
    public WifiP2pServiceRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WifiP2pServiceRequest(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readString(), null);
    }
    
    public WifiP2pServiceRequest[] newArray(int paramAnonymousInt)
    {
      return new WifiP2pServiceRequest[paramAnonymousInt];
    }
  };
  private int mLength;
  private int mProtocolType;
  private String mQuery;
  private int mTransId;
  
  private WifiP2pServiceRequest(int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
    mProtocolType = paramInt1;
    mLength = paramInt2;
    mTransId = paramInt3;
    mQuery = paramString;
  }
  
  protected WifiP2pServiceRequest(int paramInt, String paramString)
  {
    validateQuery(paramString);
    mProtocolType = paramInt;
    mQuery = paramString;
    if (paramString != null) {
      mLength = (paramString.length() / 2 + 2);
    } else {
      mLength = 2;
    }
  }
  
  public static WifiP2pServiceRequest newInstance(int paramInt)
  {
    return new WifiP2pServiceRequest(paramInt, null);
  }
  
  public static WifiP2pServiceRequest newInstance(int paramInt, String paramString)
  {
    return new WifiP2pServiceRequest(paramInt, paramString);
  }
  
  private void validateQuery(String paramString)
  {
    if (paramString == null) {
      return;
    }
    if (paramString.length() % 2 != 1)
    {
      if (paramString.length() / 2 <= 65535)
      {
        paramString = paramString.toLowerCase(Locale.ROOT);
        localObject = paramString.toCharArray();
        int i = localObject.length;
        int j = 0;
        while (j < i)
        {
          int k = localObject[j];
          if (((k >= 48) && (k <= 57)) || ((k >= 97) && (k <= 102)))
          {
            j++;
          }
          else
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("query should be hex string. query=");
            ((StringBuilder)localObject).append(paramString);
            throw new IllegalArgumentException(((StringBuilder)localObject).toString());
          }
        }
        return;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("query size is too large. len=");
      ((StringBuilder)localObject).append(paramString.length());
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("query size is invalid. query=");
    ((StringBuilder)localObject).append(paramString);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
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
    if (!(paramObject instanceof WifiP2pServiceRequest)) {
      return false;
    }
    paramObject = (WifiP2pServiceRequest)paramObject;
    if ((mProtocolType == mProtocolType) && (mLength == mLength))
    {
      if ((mQuery == null) && (mQuery == null)) {
        return true;
      }
      if (mQuery != null) {
        return mQuery.equals(mQuery);
      }
      return false;
    }
    return false;
  }
  
  public String getSupplicantQuery()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(String.format(Locale.US, "%02x", new Object[] { Integer.valueOf(mLength & 0xFF) }));
    localStringBuffer.append(String.format(Locale.US, "%02x", new Object[] { Integer.valueOf(mLength >> 8 & 0xFF) }));
    localStringBuffer.append(String.format(Locale.US, "%02x", new Object[] { Integer.valueOf(mProtocolType) }));
    localStringBuffer.append(String.format(Locale.US, "%02x", new Object[] { Integer.valueOf(mTransId) }));
    if (mQuery != null) {
      localStringBuffer.append(mQuery);
    }
    return localStringBuffer.toString();
  }
  
  public int getTransactionId()
  {
    return mTransId;
  }
  
  public int hashCode()
  {
    int i = mProtocolType;
    int j = mLength;
    int k;
    if (mQuery == null) {
      k = 0;
    } else {
      k = mQuery.hashCode();
    }
    return 31 * (31 * (31 * 17 + i) + j) + k;
  }
  
  public void setTransactionId(int paramInt)
  {
    mTransId = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mProtocolType);
    paramParcel.writeInt(mLength);
    paramParcel.writeInt(mTransId);
    paramParcel.writeString(mQuery);
  }
}
