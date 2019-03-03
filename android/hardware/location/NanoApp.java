package android.hardware.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

@SystemApi
@Deprecated
public class NanoApp
  implements Parcelable
{
  public static final Parcelable.Creator<NanoApp> CREATOR = new Parcelable.Creator()
  {
    public NanoApp createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NanoApp(paramAnonymousParcel, null);
    }
    
    public NanoApp[] newArray(int paramAnonymousInt)
    {
      return new NanoApp[paramAnonymousInt];
    }
  };
  private final String TAG = "NanoApp";
  private final String UNKNOWN = "Unknown";
  private byte[] mAppBinary;
  private long mAppId;
  private boolean mAppIdSet;
  private int mAppVersion;
  private String mName;
  private int mNeededExecMemBytes;
  private int mNeededReadMemBytes;
  private int[] mNeededSensors;
  private int mNeededWriteMemBytes;
  private int[] mOutputEvents;
  private String mPublisher;
  
  public NanoApp()
  {
    this(0L, null);
    mAppIdSet = false;
  }
  
  @Deprecated
  public NanoApp(int paramInt, byte[] paramArrayOfByte)
  {
    Log.w("NanoApp", "NanoApp(int, byte[]) is deprecated, please use NanoApp(long, byte[]) instead.");
  }
  
  public NanoApp(long paramLong, byte[] paramArrayOfByte)
  {
    mPublisher = "Unknown";
    mName = "Unknown";
    mAppId = paramLong;
    mAppIdSet = true;
    mAppVersion = 0;
    mNeededReadMemBytes = 0;
    mNeededWriteMemBytes = 0;
    mNeededExecMemBytes = 0;
    mNeededSensors = new int[0];
    mOutputEvents = new int[0];
    mAppBinary = paramArrayOfByte;
  }
  
  private NanoApp(Parcel paramParcel)
  {
    mPublisher = paramParcel.readString();
    mName = paramParcel.readString();
    mAppId = paramParcel.readLong();
    mAppVersion = paramParcel.readInt();
    mNeededReadMemBytes = paramParcel.readInt();
    mNeededWriteMemBytes = paramParcel.readInt();
    mNeededExecMemBytes = paramParcel.readInt();
    mNeededSensors = new int[paramParcel.readInt()];
    paramParcel.readIntArray(mNeededSensors);
    mOutputEvents = new int[paramParcel.readInt()];
    paramParcel.readIntArray(mOutputEvents);
    mAppBinary = new byte[paramParcel.readInt()];
    paramParcel.readByteArray(mAppBinary);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getAppBinary()
  {
    return mAppBinary;
  }
  
  public long getAppId()
  {
    return mAppId;
  }
  
  public int getAppVersion()
  {
    return mAppVersion;
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
  
  public void setAppBinary(byte[] paramArrayOfByte)
  {
    mAppBinary = paramArrayOfByte;
  }
  
  public void setAppId(long paramLong)
  {
    mAppId = paramLong;
    mAppIdSet = true;
  }
  
  public void setAppVersion(int paramInt)
  {
    mAppVersion = paramInt;
  }
  
  public void setName(String paramString)
  {
    mName = paramString;
  }
  
  public void setNeededExecMemBytes(int paramInt)
  {
    mNeededExecMemBytes = paramInt;
  }
  
  public void setNeededReadMemBytes(int paramInt)
  {
    mNeededReadMemBytes = paramInt;
  }
  
  public void setNeededSensors(int[] paramArrayOfInt)
  {
    mNeededSensors = paramArrayOfInt;
  }
  
  public void setNeededWriteMemBytes(int paramInt)
  {
    mNeededWriteMemBytes = paramInt;
  }
  
  public void setOutputEvents(int[] paramArrayOfInt)
  {
    mOutputEvents = paramArrayOfInt;
  }
  
  public void setPublisher(String paramString)
  {
    mPublisher = paramString;
  }
  
  public String toString()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Id : ");
    ((StringBuilder)localObject).append(mAppId);
    localObject = ((StringBuilder)localObject).toString();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(", Version : ");
    localStringBuilder.append(mAppVersion);
    localObject = localStringBuilder.toString();
    localStringBuilder = new StringBuilder();
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(", Name : ");
    localStringBuilder.append(mName);
    localObject = localStringBuilder.toString();
    localStringBuilder = new StringBuilder();
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(", Publisher : ");
    localStringBuilder.append(mPublisher);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mAppBinary != null)
    {
      if (mAppIdSet)
      {
        paramParcel.writeString(mPublisher);
        paramParcel.writeString(mName);
        paramParcel.writeLong(mAppId);
        paramParcel.writeInt(mAppVersion);
        paramParcel.writeInt(mNeededReadMemBytes);
        paramParcel.writeInt(mNeededWriteMemBytes);
        paramParcel.writeInt(mNeededExecMemBytes);
        paramParcel.writeInt(mNeededSensors.length);
        paramParcel.writeIntArray(mNeededSensors);
        paramParcel.writeInt(mOutputEvents.length);
        paramParcel.writeIntArray(mOutputEvents);
        paramParcel.writeInt(mAppBinary.length);
        paramParcel.writeByteArray(mAppBinary);
        return;
      }
      paramParcel = new StringBuilder();
      paramParcel.append("Must set AppId for nanoapp ");
      paramParcel.append(mName);
      throw new IllegalStateException(paramParcel.toString());
    }
    paramParcel = new StringBuilder();
    paramParcel.append("Must set non-null AppBinary for nanoapp ");
    paramParcel.append(mName);
    throw new IllegalStateException(paramParcel.toString());
  }
}
