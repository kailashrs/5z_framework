package android.hardware.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public class MemoryRegion
  implements Parcelable
{
  public static final Parcelable.Creator<MemoryRegion> CREATOR = new Parcelable.Creator()
  {
    public MemoryRegion createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MemoryRegion(paramAnonymousParcel);
    }
    
    public MemoryRegion[] newArray(int paramAnonymousInt)
    {
      return new MemoryRegion[paramAnonymousInt];
    }
  };
  private boolean mIsExecutable;
  private boolean mIsReadable;
  private boolean mIsWritable;
  private int mSizeBytes;
  private int mSizeBytesFree;
  
  public MemoryRegion(Parcel paramParcel)
  {
    mSizeBytes = paramParcel.readInt();
    mSizeBytesFree = paramParcel.readInt();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsReadable = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsWritable = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    mIsExecutable = bool2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getCapacityBytes()
  {
    return mSizeBytes;
  }
  
  public int getFreeCapacityBytes()
  {
    return mSizeBytesFree;
  }
  
  public boolean isExecutable()
  {
    return mIsExecutable;
  }
  
  public boolean isReadable()
  {
    return mIsReadable;
  }
  
  public boolean isWritable()
  {
    return mIsWritable;
  }
  
  public String toString()
  {
    Object localObject;
    if (isReadable())
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("");
      ((StringBuilder)localObject).append("r");
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("");
      ((StringBuilder)localObject).append("-");
      localObject = ((StringBuilder)localObject).toString();
    }
    if (isWritable())
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append((String)localObject);
      localStringBuilder.append("w");
      localObject = localStringBuilder.toString();
    }
    else
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append((String)localObject);
      localStringBuilder.append("-");
      localObject = localStringBuilder.toString();
    }
    if (isExecutable())
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append((String)localObject);
      localStringBuilder.append("x");
      localObject = localStringBuilder.toString();
    }
    else
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append((String)localObject);
      localStringBuilder.append("-");
      localObject = localStringBuilder.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ ");
    localStringBuilder.append(mSizeBytesFree);
    localStringBuilder.append("/ ");
    localStringBuilder.append(mSizeBytes);
    localStringBuilder.append(" ] : ");
    localStringBuilder.append((String)localObject);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSizeBytes);
    paramParcel.writeInt(mSizeBytesFree);
    paramParcel.writeInt(mIsReadable);
    paramParcel.writeInt(mIsWritable);
    paramParcel.writeInt(mIsExecutable);
  }
}
