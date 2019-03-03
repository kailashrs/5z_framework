package android.telephony;

import android.net.LinkProperties;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PreciseDataConnectionState
  implements Parcelable
{
  public static final Parcelable.Creator<PreciseDataConnectionState> CREATOR = new Parcelable.Creator()
  {
    public PreciseDataConnectionState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PreciseDataConnectionState(paramAnonymousParcel, null);
    }
    
    public PreciseDataConnectionState[] newArray(int paramAnonymousInt)
    {
      return new PreciseDataConnectionState[paramAnonymousInt];
    }
  };
  private String mAPN = "";
  private String mAPNType = "";
  private String mFailCause = "";
  private LinkProperties mLinkProperties = null;
  private int mNetworkType = 0;
  private String mReason = "";
  private int mState = -1;
  
  public PreciseDataConnectionState() {}
  
  public PreciseDataConnectionState(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, LinkProperties paramLinkProperties, String paramString4)
  {
    mState = paramInt1;
    mNetworkType = paramInt2;
    mAPNType = paramString1;
    mAPN = paramString2;
    mReason = paramString3;
    mLinkProperties = paramLinkProperties;
    mFailCause = paramString4;
  }
  
  private PreciseDataConnectionState(Parcel paramParcel)
  {
    mState = paramParcel.readInt();
    mNetworkType = paramParcel.readInt();
    mAPNType = paramParcel.readString();
    mAPN = paramParcel.readString();
    mReason = paramParcel.readString();
    mLinkProperties = ((LinkProperties)paramParcel.readParcelable(null));
    mFailCause = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (PreciseDataConnectionState)paramObject;
    if (mAPN == null)
    {
      if (mAPN != null) {
        return false;
      }
    }
    else if (!mAPN.equals(mAPN)) {
      return false;
    }
    if (mAPNType == null)
    {
      if (mAPNType != null) {
        return false;
      }
    }
    else if (!mAPNType.equals(mAPNType)) {
      return false;
    }
    if (mFailCause == null)
    {
      if (mFailCause != null) {
        return false;
      }
    }
    else if (!mFailCause.equals(mFailCause)) {
      return false;
    }
    if (mLinkProperties == null)
    {
      if (mLinkProperties != null) {
        return false;
      }
    }
    else if (!mLinkProperties.equals(mLinkProperties)) {
      return false;
    }
    if (mNetworkType != mNetworkType) {
      return false;
    }
    if (mReason == null)
    {
      if (mReason != null) {
        return false;
      }
    }
    else if (!mReason.equals(mReason)) {
      return false;
    }
    return mState == mState;
  }
  
  public String getDataConnectionAPN()
  {
    return mAPN;
  }
  
  public String getDataConnectionAPNType()
  {
    return mAPNType;
  }
  
  public String getDataConnectionChangeReason()
  {
    return mReason;
  }
  
  public String getDataConnectionFailCause()
  {
    return mFailCause;
  }
  
  public LinkProperties getDataConnectionLinkProperties()
  {
    return mLinkProperties;
  }
  
  public int getDataConnectionNetworkType()
  {
    return mNetworkType;
  }
  
  public int getDataConnectionState()
  {
    return mState;
  }
  
  public int hashCode()
  {
    int i = mState;
    int j = mNetworkType;
    String str = mAPNType;
    int k = 0;
    int m;
    if (str == null) {
      m = 0;
    } else {
      m = mAPNType.hashCode();
    }
    int n;
    if (mAPN == null) {
      n = 0;
    } else {
      n = mAPN.hashCode();
    }
    int i1;
    if (mReason == null) {
      i1 = 0;
    } else {
      i1 = mReason.hashCode();
    }
    int i2;
    if (mLinkProperties == null) {
      i2 = 0;
    } else {
      i2 = mLinkProperties.hashCode();
    }
    if (mFailCause != null) {
      k = mFailCause.hashCode();
    }
    return 31 * (31 * (31 * (31 * (31 * (31 * (31 * 1 + i) + j) + m) + n) + i1) + i2) + k;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append("Data Connection state: ");
    localStringBuilder2.append(mState);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", Network type: ");
    localStringBuilder2.append(mNetworkType);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", APN type: ");
    localStringBuilder2.append(mAPNType);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", APN: ");
    localStringBuilder2.append(mAPN);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", Change reason: ");
    localStringBuilder2.append(mReason);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", Link properties: ");
    localStringBuilder2.append(mLinkProperties);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", Fail cause: ");
    localStringBuilder2.append(mFailCause);
    localStringBuilder1.append(localStringBuilder2.toString());
    return localStringBuilder1.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mState);
    paramParcel.writeInt(mNetworkType);
    paramParcel.writeString(mAPNType);
    paramParcel.writeString(mAPN);
    paramParcel.writeString(mReason);
    paramParcel.writeParcelable(mLinkProperties, paramInt);
    paramParcel.writeString(mFailCause);
  }
}
