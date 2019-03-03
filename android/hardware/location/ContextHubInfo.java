package android.hardware.location;

import android.annotation.SystemApi;
import android.hardware.contexthub.V1_0.ContextHub;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

@SystemApi
public class ContextHubInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ContextHubInfo> CREATOR = new Parcelable.Creator()
  {
    public ContextHubInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ContextHubInfo(paramAnonymousParcel, null);
    }
    
    public ContextHubInfo[] newArray(int paramAnonymousInt)
    {
      return new ContextHubInfo[paramAnonymousInt];
    }
  };
  private byte mChreApiMajorVersion;
  private byte mChreApiMinorVersion;
  private short mChrePatchVersion;
  private long mChrePlatformId;
  private int mId;
  private int mMaxPacketLengthBytes;
  private MemoryRegion[] mMemoryRegions;
  private String mName;
  private float mPeakMips;
  private float mPeakPowerDrawMw;
  private int mPlatformVersion;
  private float mSleepPowerDrawMw;
  private float mStoppedPowerDrawMw;
  private int[] mSupportedSensors;
  private String mToolchain;
  private int mToolchainVersion;
  private String mVendor;
  
  public ContextHubInfo() {}
  
  public ContextHubInfo(ContextHub paramContextHub)
  {
    mId = hubId;
    mName = name;
    mVendor = vendor;
    mToolchain = toolchain;
    mPlatformVersion = platformVersion;
    mToolchainVersion = toolchainVersion;
    mPeakMips = peakMips;
    mStoppedPowerDrawMw = stoppedPowerDrawMw;
    mSleepPowerDrawMw = sleepPowerDrawMw;
    mPeakPowerDrawMw = peakPowerDrawMw;
    mMaxPacketLengthBytes = maxSupportedMsgLen;
    mChrePlatformId = chrePlatformId;
    mChreApiMajorVersion = ((byte)chreApiMajorVersion);
    mChreApiMinorVersion = ((byte)chreApiMinorVersion);
    mChrePatchVersion = ((short)chrePatchVersion);
    mSupportedSensors = new int[0];
    mMemoryRegions = new MemoryRegion[0];
  }
  
  private ContextHubInfo(Parcel paramParcel)
  {
    mId = paramParcel.readInt();
    mName = paramParcel.readString();
    mVendor = paramParcel.readString();
    mToolchain = paramParcel.readString();
    mPlatformVersion = paramParcel.readInt();
    mToolchainVersion = paramParcel.readInt();
    mPeakMips = paramParcel.readFloat();
    mStoppedPowerDrawMw = paramParcel.readFloat();
    mSleepPowerDrawMw = paramParcel.readFloat();
    mPeakPowerDrawMw = paramParcel.readFloat();
    mMaxPacketLengthBytes = paramParcel.readInt();
    mChrePlatformId = paramParcel.readLong();
    mChreApiMajorVersion = paramParcel.readByte();
    mChreApiMinorVersion = paramParcel.readByte();
    mChrePatchVersion = ((short)(short)paramParcel.readInt());
    mSupportedSensors = new int[paramParcel.readInt()];
    paramParcel.readIntArray(mSupportedSensors);
    mMemoryRegions = ((MemoryRegion[])paramParcel.createTypedArray(MemoryRegion.CREATOR));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte getChreApiMajorVersion()
  {
    return mChreApiMajorVersion;
  }
  
  public byte getChreApiMinorVersion()
  {
    return mChreApiMinorVersion;
  }
  
  public short getChrePatchVersion()
  {
    return mChrePatchVersion;
  }
  
  public long getChrePlatformId()
  {
    return mChrePlatformId;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public int getMaxPacketLengthBytes()
  {
    return mMaxPacketLengthBytes;
  }
  
  public MemoryRegion[] getMemoryRegions()
  {
    return (MemoryRegion[])Arrays.copyOf(mMemoryRegions, mMemoryRegions.length);
  }
  
  public String getName()
  {
    return mName;
  }
  
  public float getPeakMips()
  {
    return mPeakMips;
  }
  
  public float getPeakPowerDrawMw()
  {
    return mPeakPowerDrawMw;
  }
  
  public int getPlatformVersion()
  {
    return mPlatformVersion;
  }
  
  public float getSleepPowerDrawMw()
  {
    return mSleepPowerDrawMw;
  }
  
  public int getStaticSwVersion()
  {
    return mChreApiMajorVersion << 24 | mChreApiMinorVersion << 16 | mChrePatchVersion;
  }
  
  public float getStoppedPowerDrawMw()
  {
    return mStoppedPowerDrawMw;
  }
  
  public int[] getSupportedSensors()
  {
    return Arrays.copyOf(mSupportedSensors, mSupportedSensors.length);
  }
  
  public String getToolchain()
  {
    return mToolchain;
  }
  
  public int getToolchainVersion()
  {
    return mToolchainVersion;
  }
  
  public String getVendor()
  {
    return mVendor;
  }
  
  public String toString()
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("");
    ((StringBuilder)localObject1).append("ID/handle : ");
    ((StringBuilder)localObject1).append(mId);
    localObject1 = ((StringBuilder)localObject1).toString();
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(", Name : ");
    ((StringBuilder)localObject2).append(mName);
    localObject1 = ((StringBuilder)localObject2).toString();
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append("\n\tVendor : ");
    ((StringBuilder)localObject2).append(mVendor);
    localObject2 = ((StringBuilder)localObject2).toString();
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append(", Toolchain : ");
    ((StringBuilder)localObject1).append(mToolchain);
    localObject1 = ((StringBuilder)localObject1).toString();
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(", Toolchain version: 0x");
    ((StringBuilder)localObject2).append(Integer.toHexString(mToolchainVersion));
    localObject2 = ((StringBuilder)localObject2).toString();
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append("\n\tPlatformVersion : 0x");
    ((StringBuilder)localObject1).append(Integer.toHexString(mPlatformVersion));
    localObject1 = ((StringBuilder)localObject1).toString();
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(", SwVersion : ");
    ((StringBuilder)localObject2).append(mChreApiMajorVersion);
    ((StringBuilder)localObject2).append(".");
    ((StringBuilder)localObject2).append(mChreApiMinorVersion);
    ((StringBuilder)localObject2).append(".");
    ((StringBuilder)localObject2).append(mChrePatchVersion);
    localObject2 = ((StringBuilder)localObject2).toString();
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append(", CHRE platform ID: 0x");
    ((StringBuilder)localObject1).append(Long.toHexString(mChrePlatformId));
    localObject2 = ((StringBuilder)localObject1).toString();
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append("\n\tPeakMips : ");
    ((StringBuilder)localObject1).append(mPeakMips);
    localObject2 = ((StringBuilder)localObject1).toString();
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append(", StoppedPowerDraw : ");
    ((StringBuilder)localObject1).append(mStoppedPowerDrawMw);
    ((StringBuilder)localObject1).append(" mW");
    localObject1 = ((StringBuilder)localObject1).toString();
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(", PeakPowerDraw : ");
    ((StringBuilder)localObject2).append(mPeakPowerDrawMw);
    ((StringBuilder)localObject2).append(" mW");
    localObject2 = ((StringBuilder)localObject2).toString();
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append(", MaxPacketLength : ");
    ((StringBuilder)localObject1).append(mMaxPacketLengthBytes);
    ((StringBuilder)localObject1).append(" Bytes");
    return ((StringBuilder)localObject1).toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mId);
    paramParcel.writeString(mName);
    paramParcel.writeString(mVendor);
    paramParcel.writeString(mToolchain);
    paramParcel.writeInt(mPlatformVersion);
    paramParcel.writeInt(mToolchainVersion);
    paramParcel.writeFloat(mPeakMips);
    paramParcel.writeFloat(mStoppedPowerDrawMw);
    paramParcel.writeFloat(mSleepPowerDrawMw);
    paramParcel.writeFloat(mPeakPowerDrawMw);
    paramParcel.writeInt(mMaxPacketLengthBytes);
    paramParcel.writeLong(mChrePlatformId);
    paramParcel.writeByte(mChreApiMajorVersion);
    paramParcel.writeByte(mChreApiMinorVersion);
    paramParcel.writeInt(mChrePatchVersion);
    paramParcel.writeInt(mSupportedSensors.length);
    paramParcel.writeIntArray(mSupportedSensors);
    paramParcel.writeTypedArray(mMemoryRegions, paramInt);
  }
}
