package android.app;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ServiceStartArgs
  implements Parcelable
{
  public static final Parcelable.Creator<ServiceStartArgs> CREATOR = new Parcelable.Creator()
  {
    public ServiceStartArgs createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ServiceStartArgs(paramAnonymousParcel);
    }
    
    public ServiceStartArgs[] newArray(int paramAnonymousInt)
    {
      return new ServiceStartArgs[paramAnonymousInt];
    }
  };
  public final Intent args;
  public final int flags;
  public final int startId;
  public final boolean taskRemoved;
  
  public ServiceStartArgs(Parcel paramParcel)
  {
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    taskRemoved = bool;
    startId = paramParcel.readInt();
    flags = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      args = ((Intent)Intent.CREATOR.createFromParcel(paramParcel));
    } else {
      args = null;
    }
  }
  
  public ServiceStartArgs(boolean paramBoolean, int paramInt1, int paramInt2, Intent paramIntent)
  {
    taskRemoved = paramBoolean;
    startId = paramInt1;
    flags = paramInt2;
    args = paramIntent;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ServiceStartArgs{taskRemoved=");
    localStringBuilder.append(taskRemoved);
    localStringBuilder.append(", startId=");
    localStringBuilder.append(startId);
    localStringBuilder.append(", flags=0x");
    localStringBuilder.append(Integer.toHexString(flags));
    localStringBuilder.append(", args=");
    localStringBuilder.append(args);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(taskRemoved);
    paramParcel.writeInt(startId);
    paramParcel.writeInt(paramInt);
    if (args != null)
    {
      paramParcel.writeInt(1);
      args.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
}
