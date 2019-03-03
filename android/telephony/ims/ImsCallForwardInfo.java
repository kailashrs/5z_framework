package android.telephony.ims;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class ImsCallForwardInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ImsCallForwardInfo> CREATOR = new Parcelable.Creator()
  {
    public ImsCallForwardInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ImsCallForwardInfo(paramAnonymousParcel);
    }
    
    public ImsCallForwardInfo[] newArray(int paramAnonymousInt)
    {
      return new ImsCallForwardInfo[paramAnonymousInt];
    }
  };
  public int mCondition;
  public String mNumber;
  public int mServiceClass;
  public int mStatus;
  public int mTimeSeconds;
  public int mToA;
  
  public ImsCallForwardInfo() {}
  
  public ImsCallForwardInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString, int paramInt5)
  {
    mCondition = paramInt1;
    mStatus = paramInt2;
    mToA = paramInt3;
    mServiceClass = paramInt4;
    mNumber = paramString;
    mTimeSeconds = paramInt5;
  }
  
  public ImsCallForwardInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    mCondition = paramParcel.readInt();
    mStatus = paramParcel.readInt();
    mToA = paramParcel.readInt();
    mNumber = paramParcel.readString();
    mTimeSeconds = paramParcel.readInt();
    mServiceClass = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getCondition()
  {
    return mCondition;
  }
  
  public String getNumber()
  {
    return mNumber;
  }
  
  public int getServiceClass()
  {
    return mServiceClass;
  }
  
  public int getStatus()
  {
    return mStatus;
  }
  
  public int getTimeSeconds()
  {
    return mTimeSeconds;
  }
  
  public int getToA()
  {
    return mToA;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.toString());
    localStringBuilder.append(", Condition: ");
    localStringBuilder.append(mCondition);
    localStringBuilder.append(", Status: ");
    String str;
    if (mStatus == 0) {
      str = "disabled";
    } else {
      str = "enabled";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(", ToA: ");
    localStringBuilder.append(mToA);
    localStringBuilder.append(", Service Class: ");
    localStringBuilder.append(mServiceClass);
    localStringBuilder.append(", Number=");
    localStringBuilder.append(mNumber);
    localStringBuilder.append(", Time (seconds): ");
    localStringBuilder.append(mTimeSeconds);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mCondition);
    paramParcel.writeInt(mStatus);
    paramParcel.writeInt(mToA);
    paramParcel.writeString(mNumber);
    paramParcel.writeInt(mTimeSeconds);
    paramParcel.writeInt(mServiceClass);
  }
}
