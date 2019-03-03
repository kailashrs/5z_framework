package android.telephony.ims;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class ImsSsInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ImsSsInfo> CREATOR = new Parcelable.Creator()
  {
    public ImsSsInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ImsSsInfo(paramAnonymousParcel, null);
    }
    
    public ImsSsInfo[] newArray(int paramAnonymousInt)
    {
      return new ImsSsInfo[paramAnonymousInt];
    }
  };
  public static final int DISABLED = 0;
  public static final int ENABLED = 1;
  public static final int NOT_REGISTERED = -1;
  public static final int SERVICE_NOT_PROVISIONED = 0;
  public static final int SERVICE_PROVISIONED = 1;
  public static final int SERVICE_PROVISIONING_UNKNOWN = -1;
  public String mIcbNum;
  public int mProvisionStatus = -1;
  public int mStatus;
  
  public ImsSsInfo() {}
  
  public ImsSsInfo(int paramInt, String paramString)
  {
    mStatus = paramInt;
    mIcbNum = paramString;
  }
  
  private ImsSsInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  private static String provisionStatusToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "Service provisioning unknown";
    case 1: 
      return "Service provisioned";
    }
    return "Service not provisioned";
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    mStatus = paramParcel.readInt();
    mIcbNum = paramParcel.readString();
    mProvisionStatus = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getIcbNum()
  {
    return mIcbNum;
  }
  
  public int getProvisionStatus()
  {
    return mProvisionStatus;
  }
  
  public int getStatus()
  {
    return mStatus;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.toString());
    localStringBuilder.append(", Status: ");
    String str;
    if (mStatus == 0) {
      str = "disabled";
    } else {
      str = "enabled";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(", ProvisionStatus: ");
    localStringBuilder.append(provisionStatusToString(mProvisionStatus));
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mStatus);
    paramParcel.writeString(mIcbNum);
    paramParcel.writeInt(mProvisionStatus);
  }
}
