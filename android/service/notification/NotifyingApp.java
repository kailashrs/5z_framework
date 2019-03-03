package android.service.notification;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class NotifyingApp
  implements Parcelable, Comparable<NotifyingApp>
{
  public static final Parcelable.Creator<NotifyingApp> CREATOR = new Parcelable.Creator()
  {
    public NotifyingApp createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NotifyingApp(paramAnonymousParcel);
    }
    
    public NotifyingApp[] newArray(int paramAnonymousInt)
    {
      return new NotifyingApp[paramAnonymousInt];
    }
  };
  private long mLastNotified;
  private String mPkg;
  private int mUid;
  
  public NotifyingApp() {}
  
  protected NotifyingApp(Parcel paramParcel)
  {
    mUid = paramParcel.readInt();
    mPkg = paramParcel.readString();
    mLastNotified = paramParcel.readLong();
  }
  
  public int compareTo(NotifyingApp paramNotifyingApp)
  {
    if (getLastNotified() == paramNotifyingApp.getLastNotified())
    {
      if (getUid() == paramNotifyingApp.getUid()) {
        return getPackage().compareTo(paramNotifyingApp.getPackage());
      }
      return Integer.compare(getUid(), paramNotifyingApp.getUid());
    }
    return -Long.compare(getLastNotified(), paramNotifyingApp.getLastNotified());
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
      paramObject = (NotifyingApp)paramObject;
      if ((getUid() != paramObject.getUid()) || (getLastNotified() != paramObject.getLastNotified()) || (!Objects.equals(mPkg, mPkg))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public long getLastNotified()
  {
    return mLastNotified;
  }
  
  public String getPackage()
  {
    return mPkg;
  }
  
  public int getUid()
  {
    return mUid;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(getUid()), mPkg, Long.valueOf(getLastNotified()) });
  }
  
  public NotifyingApp setLastNotified(long paramLong)
  {
    mLastNotified = paramLong;
    return this;
  }
  
  public NotifyingApp setPackage(String paramString)
  {
    mPkg = paramString;
    return this;
  }
  
  public NotifyingApp setUid(int paramInt)
  {
    mUid = paramInt;
    return this;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NotifyingApp{mUid=");
    localStringBuilder.append(mUid);
    localStringBuilder.append(", mPkg='");
    localStringBuilder.append(mPkg);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mLastNotified=");
    localStringBuilder.append(mLastNotified);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mUid);
    paramParcel.writeString(mPkg);
    paramParcel.writeLong(mLastNotified);
  }
}
