package android.hardware.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
@Deprecated
public class NanoAppFilter
  implements Parcelable
{
  public static final int APP_ANY = -1;
  public static final Parcelable.Creator<NanoAppFilter> CREATOR = new Parcelable.Creator()
  {
    public NanoAppFilter createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NanoAppFilter(paramAnonymousParcel, null);
    }
    
    public NanoAppFilter[] newArray(int paramAnonymousInt)
    {
      return new NanoAppFilter[paramAnonymousInt];
    }
  };
  public static final int FLAGS_VERSION_ANY = -1;
  public static final int FLAGS_VERSION_GREAT_THAN = 2;
  public static final int FLAGS_VERSION_LESS_THAN = 4;
  public static final int FLAGS_VERSION_STRICTLY_EQUAL = 8;
  public static final int HUB_ANY = -1;
  private static final String TAG = "NanoAppFilter";
  public static final int VENDOR_ANY = -1;
  private long mAppId;
  private long mAppIdVendorMask;
  private int mAppVersion;
  private int mContextHubId = -1;
  private int mVersionRestrictionMask;
  
  public NanoAppFilter(long paramLong1, int paramInt1, int paramInt2, long paramLong2)
  {
    mAppId = paramLong1;
    mAppVersion = paramInt1;
    mVersionRestrictionMask = paramInt2;
    mAppIdVendorMask = paramLong2;
  }
  
  private NanoAppFilter(Parcel paramParcel)
  {
    mAppId = paramParcel.readLong();
    mAppVersion = paramParcel.readInt();
    mVersionRestrictionMask = paramParcel.readInt();
    mAppIdVendorMask = paramParcel.readLong();
  }
  
  private boolean versionsMatch(int paramInt1, int paramInt2, int paramInt3)
  {
    return true;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean testMatch(NanoAppInstanceInfo paramNanoAppInstanceInfo)
  {
    boolean bool;
    if (((mContextHubId == -1) || (paramNanoAppInstanceInfo.getContexthubId() == mContextHubId)) && ((mAppId == -1L) || (paramNanoAppInstanceInfo.getAppId() == mAppId)) && (versionsMatch(mVersionRestrictionMask, mAppVersion, paramNanoAppInstanceInfo.getAppVersion()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("nanoAppId: 0x");
    localStringBuilder.append(Long.toHexString(mAppId));
    localStringBuilder.append(", nanoAppVersion: 0x");
    localStringBuilder.append(Integer.toHexString(mAppVersion));
    localStringBuilder.append(", versionMask: ");
    localStringBuilder.append(mVersionRestrictionMask);
    localStringBuilder.append(", vendorMask: ");
    localStringBuilder.append(mAppIdVendorMask);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mAppId);
    paramParcel.writeInt(mAppVersion);
    paramParcel.writeInt(mVersionRestrictionMask);
    paramParcel.writeLong(mAppIdVendorMask);
  }
}
