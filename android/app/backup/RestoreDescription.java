package android.app.backup;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public class RestoreDescription
  implements Parcelable
{
  public static final Parcelable.Creator<RestoreDescription> CREATOR = new Parcelable.Creator()
  {
    public RestoreDescription createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel = new RestoreDescription(paramAnonymousParcel, null);
      if ("NO_MORE_PACKAGES".equals(mPackageName)) {
        paramAnonymousParcel = RestoreDescription.NO_MORE_PACKAGES;
      }
      return paramAnonymousParcel;
    }
    
    public RestoreDescription[] newArray(int paramAnonymousInt)
    {
      return new RestoreDescription[paramAnonymousInt];
    }
  };
  public static final RestoreDescription NO_MORE_PACKAGES = new RestoreDescription("NO_MORE_PACKAGES", 0);
  private static final String NO_MORE_PACKAGES_SENTINEL = "NO_MORE_PACKAGES";
  public static final int TYPE_FULL_STREAM = 2;
  public static final int TYPE_KEY_VALUE = 1;
  private final int mDataType;
  private final String mPackageName;
  
  private RestoreDescription(Parcel paramParcel)
  {
    mPackageName = paramParcel.readString();
    mDataType = paramParcel.readInt();
  }
  
  public RestoreDescription(String paramString, int paramInt)
  {
    mPackageName = paramString;
    mDataType = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getDataType()
  {
    return mDataType;
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RestoreDescription{");
    localStringBuilder.append(mPackageName);
    localStringBuilder.append(" : ");
    String str;
    if (mDataType == 1) {
      str = "KEY_VALUE";
    } else {
      str = "STREAM";
    }
    localStringBuilder.append(str);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPackageName);
    paramParcel.writeInt(mDataType);
  }
}
