package android.content;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class UriPermission
  implements Parcelable
{
  public static final Parcelable.Creator<UriPermission> CREATOR = new Parcelable.Creator()
  {
    public UriPermission createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UriPermission(paramAnonymousParcel);
    }
    
    public UriPermission[] newArray(int paramAnonymousInt)
    {
      return new UriPermission[paramAnonymousInt];
    }
  };
  public static final long INVALID_TIME = Long.MIN_VALUE;
  private final int mModeFlags;
  private final long mPersistedTime;
  private final Uri mUri;
  
  public UriPermission(Uri paramUri, int paramInt, long paramLong)
  {
    mUri = paramUri;
    mModeFlags = paramInt;
    mPersistedTime = paramLong;
  }
  
  public UriPermission(Parcel paramParcel)
  {
    mUri = ((Uri)paramParcel.readParcelable(null));
    mModeFlags = paramParcel.readInt();
    mPersistedTime = paramParcel.readLong();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getPersistedTime()
  {
    return mPersistedTime;
  }
  
  public Uri getUri()
  {
    return mUri;
  }
  
  public boolean isReadPermission()
  {
    int i = mModeFlags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isWritePermission()
  {
    boolean bool;
    if ((mModeFlags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UriPermission {uri=");
    localStringBuilder.append(mUri);
    localStringBuilder.append(", modeFlags=");
    localStringBuilder.append(mModeFlags);
    localStringBuilder.append(", persistedTime=");
    localStringBuilder.append(mPersistedTime);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mUri, paramInt);
    paramParcel.writeInt(mModeFlags);
    paramParcel.writeLong(mPersistedTime);
  }
}
