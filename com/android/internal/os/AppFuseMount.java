package com.android.internal.os;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

public class AppFuseMount
  implements Parcelable
{
  public static final Parcelable.Creator<AppFuseMount> CREATOR = new Parcelable.Creator()
  {
    public AppFuseMount createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AppFuseMount(paramAnonymousParcel.readInt(), (ParcelFileDescriptor)paramAnonymousParcel.readParcelable(null));
    }
    
    public AppFuseMount[] newArray(int paramAnonymousInt)
    {
      return new AppFuseMount[paramAnonymousInt];
    }
  };
  public final ParcelFileDescriptor fd;
  public final int mountPointId;
  
  public AppFuseMount(int paramInt, ParcelFileDescriptor paramParcelFileDescriptor)
  {
    Preconditions.checkNotNull(paramParcelFileDescriptor);
    mountPointId = paramInt;
    fd = paramParcelFileDescriptor;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mountPointId);
    paramParcel.writeParcelable(fd, paramInt);
  }
}
