package android.service.notification;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public final class NotificationStats
  implements Parcelable
{
  public static final Parcelable.Creator<NotificationStats> CREATOR = new Parcelable.Creator()
  {
    public NotificationStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NotificationStats(paramAnonymousParcel);
    }
    
    public NotificationStats[] newArray(int paramAnonymousInt)
    {
      return new NotificationStats[paramAnonymousInt];
    }
  };
  public static final int DISMISSAL_AOD = 2;
  public static final int DISMISSAL_NOT_DISMISSED = -1;
  public static final int DISMISSAL_OTHER = 0;
  public static final int DISMISSAL_PEEK = 1;
  public static final int DISMISSAL_SHADE = 3;
  private boolean mDirectReplied;
  private int mDismissalSurface = -1;
  private boolean mExpanded;
  private boolean mInteracted;
  private boolean mSeen;
  private boolean mSnoozed;
  private boolean mViewedSettings;
  
  public NotificationStats() {}
  
  protected NotificationStats(Parcel paramParcel)
  {
    int i = paramParcel.readByte();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mSeen = bool2;
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mExpanded = bool2;
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mDirectReplied = bool2;
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mSnoozed = bool2;
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mViewedSettings = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    }
    mInteracted = bool2;
    mDismissalSurface = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (NotificationStats)paramObject;
      if (mSeen != mSeen) {
        return false;
      }
      if (mExpanded != mExpanded) {
        return false;
      }
      if (mDirectReplied != mDirectReplied) {
        return false;
      }
      if (mSnoozed != mSnoozed) {
        return false;
      }
      if (mViewedSettings != mViewedSettings) {
        return false;
      }
      if (mInteracted != mInteracted) {
        return false;
      }
      if (mDismissalSurface != mDismissalSurface) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int getDismissalSurface()
  {
    return mDismissalSurface;
  }
  
  public boolean hasDirectReplied()
  {
    return mDirectReplied;
  }
  
  public boolean hasExpanded()
  {
    return mExpanded;
  }
  
  public boolean hasInteracted()
  {
    return mInteracted;
  }
  
  public boolean hasSeen()
  {
    return mSeen;
  }
  
  public boolean hasSnoozed()
  {
    return mSnoozed;
  }
  
  public boolean hasViewedSettings()
  {
    return mViewedSettings;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * (31 * (true * mSeen + mExpanded) + mDirectReplied) + mSnoozed) + mViewedSettings) + mInteracted) + mDismissalSurface;
  }
  
  public void setDirectReplied()
  {
    mDirectReplied = true;
    mInteracted = true;
  }
  
  public void setDismissalSurface(int paramInt)
  {
    mDismissalSurface = paramInt;
  }
  
  public void setExpanded()
  {
    mExpanded = true;
    mInteracted = true;
  }
  
  public void setSeen()
  {
    mSeen = true;
  }
  
  public void setSnoozed()
  {
    mSnoozed = true;
    mInteracted = true;
  }
  
  public void setViewedSettings()
  {
    mViewedSettings = true;
    mInteracted = true;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("NotificationStats{");
    localStringBuilder.append("mSeen=");
    localStringBuilder.append(mSeen);
    localStringBuilder.append(", mExpanded=");
    localStringBuilder.append(mExpanded);
    localStringBuilder.append(", mDirectReplied=");
    localStringBuilder.append(mDirectReplied);
    localStringBuilder.append(", mSnoozed=");
    localStringBuilder.append(mSnoozed);
    localStringBuilder.append(", mViewedSettings=");
    localStringBuilder.append(mViewedSettings);
    localStringBuilder.append(", mInteracted=");
    localStringBuilder.append(mInteracted);
    localStringBuilder.append(", mDismissalSurface=");
    localStringBuilder.append(mDismissalSurface);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByte((byte)mSeen);
    paramParcel.writeByte((byte)mExpanded);
    paramParcel.writeByte((byte)mDirectReplied);
    paramParcel.writeByte((byte)mSnoozed);
    paramParcel.writeByte((byte)mViewedSettings);
    paramParcel.writeByte((byte)mInteracted);
    paramParcel.writeInt(mDismissalSurface);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DismissalSurface {}
}
