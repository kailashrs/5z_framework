package android.content.pm.permission;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class RuntimePermissionPresentationInfo
  implements Parcelable
{
  public static final Parcelable.Creator<RuntimePermissionPresentationInfo> CREATOR = new Parcelable.Creator()
  {
    public RuntimePermissionPresentationInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RuntimePermissionPresentationInfo(paramAnonymousParcel, null);
    }
    
    public RuntimePermissionPresentationInfo[] newArray(int paramAnonymousInt)
    {
      return new RuntimePermissionPresentationInfo[paramAnonymousInt];
    }
  };
  private static final int FLAG_GRANTED = 1;
  private static final int FLAG_STANDARD = 2;
  private final int mFlags;
  private final CharSequence mLabel;
  
  private RuntimePermissionPresentationInfo(Parcel paramParcel)
  {
    mLabel = paramParcel.readCharSequence();
    mFlags = paramParcel.readInt();
  }
  
  public RuntimePermissionPresentationInfo(CharSequence paramCharSequence, boolean paramBoolean1, boolean paramBoolean2)
  {
    mLabel = paramCharSequence;
    int i = 0;
    if (paramBoolean1) {
      i = 0x0 | 0x1;
    }
    int j = i;
    if (paramBoolean2) {
      j = i | 0x2;
    }
    mFlags = j;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CharSequence getLabel()
  {
    return mLabel;
  }
  
  public boolean isGranted()
  {
    int i = mFlags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isStandard()
  {
    boolean bool;
    if ((mFlags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeCharSequence(mLabel);
    paramParcel.writeInt(mFlags);
  }
}
