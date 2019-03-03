package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.PatternMatcher;

public class PathPermission
  extends PatternMatcher
{
  public static final Parcelable.Creator<PathPermission> CREATOR = new Parcelable.Creator()
  {
    public PathPermission createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PathPermission(paramAnonymousParcel);
    }
    
    public PathPermission[] newArray(int paramAnonymousInt)
    {
      return new PathPermission[paramAnonymousInt];
    }
  };
  private final String mReadPermission;
  private final String mWritePermission;
  
  public PathPermission(Parcel paramParcel)
  {
    super(paramParcel);
    mReadPermission = paramParcel.readString();
    mWritePermission = paramParcel.readString();
  }
  
  public PathPermission(String paramString1, int paramInt, String paramString2, String paramString3)
  {
    super(paramString1, paramInt);
    mReadPermission = paramString2;
    mWritePermission = paramString3;
  }
  
  public String getReadPermission()
  {
    return mReadPermission;
  }
  
  public String getWritePermission()
  {
    return mWritePermission;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(mReadPermission);
    paramParcel.writeString(mWritePermission);
  }
}
