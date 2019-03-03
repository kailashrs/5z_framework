package android.net.wifi.aware;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import libcore.util.HexEncoding;

public final class SubscribeConfig
  implements Parcelable
{
  public static final Parcelable.Creator<SubscribeConfig> CREATOR = new Parcelable.Creator()
  {
    public SubscribeConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      byte[] arrayOfByte1 = paramAnonymousParcel.createByteArray();
      byte[] arrayOfByte2 = paramAnonymousParcel.createByteArray();
      byte[] arrayOfByte3 = paramAnonymousParcel.createByteArray();
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      boolean bool1;
      if (paramAnonymousParcel.readInt() != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      int k = paramAnonymousParcel.readInt();
      boolean bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      int m = paramAnonymousParcel.readInt();
      boolean bool3;
      if (paramAnonymousParcel.readInt() != 0) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      return new SubscribeConfig(arrayOfByte1, arrayOfByte2, arrayOfByte3, i, j, bool1, bool2, k, bool3, m);
    }
    
    public SubscribeConfig[] newArray(int paramAnonymousInt)
    {
      return new SubscribeConfig[paramAnonymousInt];
    }
  };
  public static final int SUBSCRIBE_TYPE_ACTIVE = 1;
  public static final int SUBSCRIBE_TYPE_PASSIVE = 0;
  public final boolean mEnableTerminateNotification;
  public final byte[] mMatchFilter;
  public final int mMaxDistanceMm;
  public final boolean mMaxDistanceMmSet;
  public final int mMinDistanceMm;
  public final boolean mMinDistanceMmSet;
  public final byte[] mServiceName;
  public final byte[] mServiceSpecificInfo;
  public final int mSubscribeType;
  public final int mTtlSec;
  
  public SubscribeConfig(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, int paramInt3, boolean paramBoolean3, int paramInt4)
  {
    mServiceName = paramArrayOfByte1;
    mServiceSpecificInfo = paramArrayOfByte2;
    mMatchFilter = paramArrayOfByte3;
    mSubscribeType = paramInt1;
    mTtlSec = paramInt2;
    mEnableTerminateNotification = paramBoolean1;
    mMinDistanceMm = paramInt3;
    mMinDistanceMmSet = paramBoolean2;
    mMaxDistanceMm = paramInt4;
    mMaxDistanceMmSet = paramBoolean3;
  }
  
  public void assertValid(Characteristics paramCharacteristics, boolean paramBoolean)
    throws IllegalArgumentException
  {
    WifiAwareUtils.validateServiceName(mServiceName);
    if (TlvBufferUtils.isValid(mMatchFilter, 0, 1))
    {
      if ((mSubscribeType >= 0) && (mSubscribeType <= 1))
      {
        if (mTtlSec >= 0)
        {
          if (paramCharacteristics != null)
          {
            int i = paramCharacteristics.getMaxServiceNameLength();
            if ((i != 0) && (mServiceName.length > i)) {
              throw new IllegalArgumentException("Service name longer than supported by device characteristics");
            }
            i = paramCharacteristics.getMaxServiceSpecificInfoLength();
            if ((i != 0) && (mServiceSpecificInfo != null) && (mServiceSpecificInfo.length > i)) {
              throw new IllegalArgumentException("Service specific info longer than supported by device characteristics");
            }
            i = paramCharacteristics.getMaxMatchFilterLength();
            if ((i != 0) && (mMatchFilter != null) && (mMatchFilter.length > i)) {
              throw new IllegalArgumentException("Match filter longer than supported by device characteristics");
            }
          }
          if ((mMinDistanceMmSet) && (mMinDistanceMm < 0)) {
            throw new IllegalArgumentException("Minimum distance must be non-negative");
          }
          if ((mMaxDistanceMmSet) && (mMaxDistanceMm < 0)) {
            throw new IllegalArgumentException("Maximum distance must be non-negative");
          }
          if ((mMinDistanceMmSet) && (mMaxDistanceMmSet) && (mMaxDistanceMm <= mMinDistanceMm)) {
            throw new IllegalArgumentException("Maximum distance must be greater than minimum distance");
          }
          if ((!paramBoolean) && ((mMinDistanceMmSet) || (mMaxDistanceMmSet))) {
            throw new IllegalArgumentException("Ranging is not supported");
          }
          return;
        }
        throw new IllegalArgumentException("Invalid ttlSec - must be non-negative");
      }
      paramCharacteristics = new StringBuilder();
      paramCharacteristics.append("Invalid subscribeType - ");
      paramCharacteristics.append(mSubscribeType);
      throw new IllegalArgumentException(paramCharacteristics.toString());
    }
    throw new IllegalArgumentException("Invalid matchFilter configuration - LV fields do not match up to length");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof SubscribeConfig)) {
      return false;
    }
    paramObject = (SubscribeConfig)paramObject;
    if ((Arrays.equals(mServiceName, mServiceName)) && (Arrays.equals(mServiceSpecificInfo, mServiceSpecificInfo)) && (Arrays.equals(mMatchFilter, mMatchFilter)) && (mSubscribeType == mSubscribeType) && (mTtlSec == mTtlSec) && (mEnableTerminateNotification == mEnableTerminateNotification) && (mMinDistanceMmSet == mMinDistanceMmSet) && (mMaxDistanceMmSet == mMaxDistanceMmSet))
    {
      if ((mMinDistanceMmSet) && (mMinDistanceMm != mMinDistanceMm)) {
        return false;
      }
      return (!mMaxDistanceMmSet) || (mMaxDistanceMm == mMaxDistanceMm);
    }
    return false;
  }
  
  public int hashCode()
  {
    int i = Objects.hash(new Object[] { mServiceName, mServiceSpecificInfo, mMatchFilter, Integer.valueOf(mSubscribeType), Integer.valueOf(mTtlSec), Boolean.valueOf(mEnableTerminateNotification), Boolean.valueOf(mMinDistanceMmSet), Boolean.valueOf(mMaxDistanceMmSet) });
    int j = i;
    if (mMinDistanceMmSet) {
      j = Objects.hash(new Object[] { Integer.valueOf(i), Integer.valueOf(mMinDistanceMm) });
    }
    i = j;
    if (mMaxDistanceMmSet) {
      i = Objects.hash(new Object[] { Integer.valueOf(j), Integer.valueOf(mMaxDistanceMm) });
    }
    return i;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SubscribeConfig [mServiceName='");
    if (mServiceName == null) {
      localObject = "<null>";
    } else {
      localObject = String.valueOf(HexEncoding.encode(mServiceName));
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(", mServiceName.length=");
    Object localObject = mServiceName;
    int i = 0;
    int j;
    if (localObject == null) {
      j = 0;
    } else {
      j = mServiceName.length;
    }
    localStringBuilder.append(j);
    localStringBuilder.append(", mServiceSpecificInfo='");
    if (mServiceSpecificInfo == null) {
      localObject = "<null>";
    } else {
      localObject = String.valueOf(HexEncoding.encode(mServiceSpecificInfo));
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(", mServiceSpecificInfo.length=");
    if (mServiceSpecificInfo == null) {
      j = 0;
    } else {
      j = mServiceSpecificInfo.length;
    }
    localStringBuilder.append(j);
    localStringBuilder.append(", mMatchFilter=");
    localStringBuilder.append(new TlvBufferUtils.TlvIterable(0, 1, mMatchFilter).toString());
    localStringBuilder.append(", mMatchFilter.length=");
    if (mMatchFilter == null) {
      j = i;
    } else {
      j = mMatchFilter.length;
    }
    localStringBuilder.append(j);
    localStringBuilder.append(", mSubscribeType=");
    localStringBuilder.append(mSubscribeType);
    localStringBuilder.append(", mTtlSec=");
    localStringBuilder.append(mTtlSec);
    localStringBuilder.append(", mEnableTerminateNotification=");
    localStringBuilder.append(mEnableTerminateNotification);
    localStringBuilder.append(", mMinDistanceMm=");
    localStringBuilder.append(mMinDistanceMm);
    localStringBuilder.append(", mMinDistanceMmSet=");
    localStringBuilder.append(mMinDistanceMmSet);
    localStringBuilder.append(", mMaxDistanceMm=");
    localStringBuilder.append(mMaxDistanceMm);
    localStringBuilder.append(", mMaxDistanceMmSet=");
    localStringBuilder.append(mMaxDistanceMmSet);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByteArray(mServiceName);
    paramParcel.writeByteArray(mServiceSpecificInfo);
    paramParcel.writeByteArray(mMatchFilter);
    paramParcel.writeInt(mSubscribeType);
    paramParcel.writeInt(mTtlSec);
    paramParcel.writeInt(mEnableTerminateNotification);
    paramParcel.writeInt(mMinDistanceMm);
    paramParcel.writeInt(mMinDistanceMmSet);
    paramParcel.writeInt(mMaxDistanceMm);
    paramParcel.writeInt(mMaxDistanceMmSet);
  }
  
  public static final class Builder
  {
    private boolean mEnableTerminateNotification = true;
    private byte[] mMatchFilter;
    private int mMaxDistanceMm;
    private boolean mMaxDistanceMmSet = false;
    private int mMinDistanceMm;
    private boolean mMinDistanceMmSet = false;
    private byte[] mServiceName;
    private byte[] mServiceSpecificInfo;
    private int mSubscribeType = 0;
    private int mTtlSec = 0;
    
    public Builder() {}
    
    public SubscribeConfig build()
    {
      return new SubscribeConfig(mServiceName, mServiceSpecificInfo, mMatchFilter, mSubscribeType, mTtlSec, mEnableTerminateNotification, mMinDistanceMmSet, mMinDistanceMm, mMaxDistanceMmSet, mMaxDistanceMm);
    }
    
    public Builder setMatchFilter(List<byte[]> paramList)
    {
      mMatchFilter = new TlvBufferUtils.TlvConstructor(0, 1).allocateAndPut(paramList).getArray();
      return this;
    }
    
    public Builder setMaxDistanceMm(int paramInt)
    {
      mMaxDistanceMm = paramInt;
      mMaxDistanceMmSet = true;
      return this;
    }
    
    public Builder setMinDistanceMm(int paramInt)
    {
      mMinDistanceMm = paramInt;
      mMinDistanceMmSet = true;
      return this;
    }
    
    public Builder setServiceName(String paramString)
    {
      if (paramString != null)
      {
        mServiceName = paramString.getBytes(StandardCharsets.UTF_8);
        return this;
      }
      throw new IllegalArgumentException("Invalid service name - must be non-null");
    }
    
    public Builder setServiceSpecificInfo(byte[] paramArrayOfByte)
    {
      mServiceSpecificInfo = paramArrayOfByte;
      return this;
    }
    
    public Builder setSubscribeType(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt <= 1))
      {
        mSubscribeType = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid subscribeType - ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Builder setTerminateNotificationEnabled(boolean paramBoolean)
    {
      mEnableTerminateNotification = paramBoolean;
      return this;
    }
    
    public Builder setTtlSec(int paramInt)
    {
      if (paramInt >= 0)
      {
        mTtlSec = paramInt;
        return this;
      }
      throw new IllegalArgumentException("Invalid ttlSec - must be non-negative");
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SubscribeTypes {}
}
