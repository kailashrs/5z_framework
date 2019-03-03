package android.content.pm;

import android.annotation.SystemApi;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class InstantAppInfo
  implements Parcelable
{
  public static final Parcelable.Creator<InstantAppInfo> CREATOR = new Parcelable.Creator()
  {
    public InstantAppInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new InstantAppInfo(paramAnonymousParcel, null);
    }
    
    public InstantAppInfo[] newArray(int paramAnonymousInt)
    {
      return new InstantAppInfo[0];
    }
  };
  private final ApplicationInfo mApplicationInfo;
  private final String[] mGrantedPermissions;
  private final CharSequence mLabelText;
  private final String mPackageName;
  private final String[] mRequestedPermissions;
  
  public InstantAppInfo(ApplicationInfo paramApplicationInfo, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    mApplicationInfo = paramApplicationInfo;
    mPackageName = null;
    mLabelText = null;
    mRequestedPermissions = paramArrayOfString1;
    mGrantedPermissions = paramArrayOfString2;
  }
  
  private InstantAppInfo(Parcel paramParcel)
  {
    mPackageName = paramParcel.readString();
    mLabelText = paramParcel.readCharSequence();
    mRequestedPermissions = paramParcel.readStringArray();
    mGrantedPermissions = paramParcel.createStringArray();
    mApplicationInfo = ((ApplicationInfo)paramParcel.readParcelable(null));
  }
  
  public InstantAppInfo(String paramString, CharSequence paramCharSequence, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    mApplicationInfo = null;
    mPackageName = paramString;
    mLabelText = paramCharSequence;
    mRequestedPermissions = paramArrayOfString1;
    mGrantedPermissions = paramArrayOfString2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ApplicationInfo getApplicationInfo()
  {
    return mApplicationInfo;
  }
  
  public String[] getGrantedPermissions()
  {
    return mGrantedPermissions;
  }
  
  public String getPackageName()
  {
    if (mApplicationInfo != null) {
      return mApplicationInfo.packageName;
    }
    return mPackageName;
  }
  
  public String[] getRequestedPermissions()
  {
    return mRequestedPermissions;
  }
  
  public Drawable loadIcon(PackageManager paramPackageManager)
  {
    if (mApplicationInfo != null) {
      return mApplicationInfo.loadIcon(paramPackageManager);
    }
    return paramPackageManager.getInstantAppIcon(mPackageName);
  }
  
  public CharSequence loadLabel(PackageManager paramPackageManager)
  {
    if (mApplicationInfo != null) {
      return mApplicationInfo.loadLabel(paramPackageManager);
    }
    return mLabelText;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPackageName);
    paramParcel.writeCharSequence(mLabelText);
    paramParcel.writeStringArray(mRequestedPermissions);
    paramParcel.writeStringArray(mGrantedPermissions);
    paramParcel.writeParcelable(mApplicationInfo, paramInt);
  }
}
