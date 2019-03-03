package android.hardware.radio;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@SystemApi
public final class Announcement
  implements Parcelable
{
  public static final Parcelable.Creator<Announcement> CREATOR = new Parcelable.Creator()
  {
    public Announcement createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Announcement(paramAnonymousParcel, null);
    }
    
    public Announcement[] newArray(int paramAnonymousInt)
    {
      return new Announcement[paramAnonymousInt];
    }
  };
  public static final int TYPE_EMERGENCY = 1;
  public static final int TYPE_EVENT = 6;
  public static final int TYPE_MISC = 8;
  public static final int TYPE_NEWS = 5;
  public static final int TYPE_SPORT = 7;
  public static final int TYPE_TRAFFIC = 3;
  public static final int TYPE_WARNING = 2;
  public static final int TYPE_WEATHER = 4;
  private final ProgramSelector mSelector;
  private final int mType;
  private final Map<String, String> mVendorInfo;
  
  public Announcement(ProgramSelector paramProgramSelector, int paramInt, Map<String, String> paramMap)
  {
    mSelector = ((ProgramSelector)Objects.requireNonNull(paramProgramSelector));
    mType = ((Integer)Objects.requireNonNull(Integer.valueOf(paramInt))).intValue();
    mVendorInfo = ((Map)Objects.requireNonNull(paramMap));
  }
  
  private Announcement(Parcel paramParcel)
  {
    mSelector = ((ProgramSelector)paramParcel.readTypedObject(ProgramSelector.CREATOR));
    mType = paramParcel.readInt();
    mVendorInfo = Utils.readStringMap(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ProgramSelector getSelector()
  {
    return mSelector;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public Map<String, String> getVendorInfo()
  {
    return mVendorInfo;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedObject(mSelector, 0);
    paramParcel.writeInt(mType);
    Utils.writeStringMap(paramParcel, mVendorInfo);
  }
  
  public static abstract interface OnListUpdatedListener
  {
    public abstract void onListUpdated(Collection<Announcement> paramCollection);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Type {}
}
