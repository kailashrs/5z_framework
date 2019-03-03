package android.hardware.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import libcore.util.EmptyArray;

@SystemApi
@Deprecated
public class NanoAppInstanceInfo
  implements Parcelable
{
  public static final Parcelable.Creator<NanoAppInstanceInfo> CREATOR = new Parcelable.Creator()
  {
    public NanoAppInstanceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NanoAppInstanceInfo(paramAnonymousParcel, null);
    }
    
    public NanoAppInstanceInfo[] newArray(int paramAnonymousInt)
    {
      return new NanoAppInstanceInfo[paramAnonymousInt];
    }
  };
  private long mAppId;
  private int mAppVersion;
  private int mContexthubId;
  private int mHandle;
  private String mName = "Unknown";
  private int mNeededExecMemBytes = 0;
  private int mNeededReadMemBytes = 0;
  private int[] mNeededSensors = EmptyArray.INT;
  private int mNeededWriteMemBytes = 0;
  private int[] mOutputEvents = EmptyArray.INT;
  private String mPublisher = "Unknown";
  
  public NanoAppInstanceInfo() {}
  
  public NanoAppInstanceInfo(int paramInt1, long paramLong, int paramInt2, int paramInt3)
  {
    mHandle = paramInt1;
    mAppId = paramLong;
    mAppVersion = paramInt2;
    mContexthubId = paramInt3;
  }
  
  private NanoAppInstanceInfo(Parcel paramParcel)
  {
    mPublisher = paramParcel.readString();
    mName = paramParcel.readString();
    mHandle = paramParcel.readInt();
    mAppId = paramParcel.readLong();
    mAppVersion = paramParcel.readInt();
    mContexthubId = paramParcel.readInt();
    mNeededReadMemBytes = paramParcel.readInt();
    mNeededWriteMemBytes = paramParcel.readInt();
    mNeededExecMemBytes = paramParcel.readInt();
    mNeededSensors = new int[paramParcel.readInt()];
    paramParcel.readIntArray(mNeededSensors);
    mOutputEvents = new int[paramParcel.readInt()];
    paramParcel.readIntArray(mOutputEvents);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getAppId()
  {
    return mAppId;
  }
  
  public int getAppVersion()
  {
    return mAppVersion;
  }
  
  public int getContexthubId()
  {
    return mContexthubId;
  }
  
  public int getHandle()
  {
    return mHandle;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getNeededExecMemBytes()
  {
    return mNeededExecMemBytes;
  }
  
  public int getNeededReadMemBytes()
  {
    return mNeededReadMemBytes;
  }
  
  public int[] getNeededSensors()
  {
    return mNeededSensors;
  }
  
  public int getNeededWriteMemBytes()
  {
    return mNeededWriteMemBytes;
  }
  
  public int[] getOutputEvents()
  {
    return mOutputEvents;
  }
  
  public String getPublisher()
  {
    return mPublisher;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("handle : ");
    localStringBuilder.append(mHandle);
    String str = localStringBuilder.toString();
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(str);
    localStringBuilder.append(", Id : 0x");
    localStringBuilder.append(Long.toHexString(mAppId));
    str = localStringBuilder.toString();
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(str);
    localStringBuilder.append(", Version : 0x");
    localStringBuilder.append(Integer.toHexString(mAppVersion));
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPublisher);
    paramParcel.writeString(mName);
    paramParcel.writeInt(mHandle);
    paramParcel.writeLong(mAppId);
    paramParcel.writeInt(mAppVersion);
    paramParcel.writeInt(mContexthubId);
    paramParcel.writeInt(mNeededReadMemBytes);
    paramParcel.writeInt(mNeededWriteMemBytes);
    paramParcel.writeInt(mNeededExecMemBytes);
    paramParcel.writeInt(mNeededSensors.length);
    paramParcel.writeIntArray(mNeededSensors);
    paramParcel.writeInt(mOutputEvents.length);
    paramParcel.writeIntArray(mOutputEvents);
  }
}
