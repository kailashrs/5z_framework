package android.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class VolumePolicy
  implements Parcelable
{
  public static final int A11Y_MODE_INDEPENDENT_A11Y_VOLUME = 1;
  public static final int A11Y_MODE_MEDIA_A11Y_VOLUME = 0;
  public static final Parcelable.Creator<VolumePolicy> CREATOR = new Parcelable.Creator()
  {
    public VolumePolicy createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      boolean bool1 = false;
      boolean bool2;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      boolean bool3;
      if (paramAnonymousParcel.readInt() != 0) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      if (paramAnonymousParcel.readInt() != 0) {
        bool1 = true;
      }
      return new VolumePolicy(bool2, bool3, bool1, paramAnonymousParcel.readInt());
    }
    
    public VolumePolicy[] newArray(int paramAnonymousInt)
    {
      return new VolumePolicy[paramAnonymousInt];
    }
  };
  public static final VolumePolicy DEFAULT = new VolumePolicy(false, false, false, 400);
  public final boolean doNotDisturbWhenSilent;
  public final int vibrateToSilentDebounce;
  public final boolean volumeDownToEnterSilent;
  public final boolean volumeUpToExitSilent;
  
  public VolumePolicy(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt)
  {
    volumeDownToEnterSilent = paramBoolean1;
    volumeUpToExitSilent = paramBoolean2;
    doNotDisturbWhenSilent = paramBoolean3;
    vibrateToSilentDebounce = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof VolumePolicy)) {
      return false;
    }
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    paramObject = (VolumePolicy)paramObject;
    if ((volumeDownToEnterSilent != volumeDownToEnterSilent) || (volumeUpToExitSilent != volumeUpToExitSilent) || (doNotDisturbWhenSilent != doNotDisturbWhenSilent) || (vibrateToSilentDebounce != vibrateToSilentDebounce)) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Boolean.valueOf(volumeDownToEnterSilent), Boolean.valueOf(volumeUpToExitSilent), Boolean.valueOf(doNotDisturbWhenSilent), Integer.valueOf(vibrateToSilentDebounce) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("VolumePolicy[volumeDownToEnterSilent=");
    localStringBuilder.append(volumeDownToEnterSilent);
    localStringBuilder.append(",volumeUpToExitSilent=");
    localStringBuilder.append(volumeUpToExitSilent);
    localStringBuilder.append(",doNotDisturbWhenSilent=");
    localStringBuilder.append(doNotDisturbWhenSilent);
    localStringBuilder.append(",vibrateToSilentDebounce=");
    localStringBuilder.append(vibrateToSilentDebounce);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(volumeDownToEnterSilent);
    paramParcel.writeInt(volumeUpToExitSilent);
    paramParcel.writeInt(doNotDisturbWhenSilent);
    paramParcel.writeInt(vibrateToSilentDebounce);
  }
}
