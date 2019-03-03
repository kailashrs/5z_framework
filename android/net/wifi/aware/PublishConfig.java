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

public final class PublishConfig
  implements Parcelable
{
  public static final Parcelable.Creator<PublishConfig> CREATOR = new Parcelable.Creator()
  {
    public PublishConfig createFromParcel(Parcel paramAnonymousParcel)
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
      boolean bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      return new PublishConfig(arrayOfByte1, arrayOfByte2, arrayOfByte3, i, j, bool1, bool2);
    }
    
    public PublishConfig[] newArray(int paramAnonymousInt)
    {
      return new PublishConfig[paramAnonymousInt];
    }
  };
  public static final int PUBLISH_TYPE_SOLICITED = 1;
  public static final int PUBLISH_TYPE_UNSOLICITED = 0;
  public final boolean mEnableRanging;
  public final boolean mEnableTerminateNotification;
  public final byte[] mMatchFilter;
  public final int mPublishType;
  public final byte[] mServiceName;
  public final byte[] mServiceSpecificInfo;
  public final int mTtlSec;
  
  public PublishConfig(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    mServiceName = paramArrayOfByte1;
    mServiceSpecificInfo = paramArrayOfByte2;
    mMatchFilter = paramArrayOfByte3;
    mPublishType = paramInt1;
    mTtlSec = paramInt2;
    mEnableTerminateNotification = paramBoolean1;
    mEnableRanging = paramBoolean2;
  }
  
  public void assertValid(Characteristics paramCharacteristics, boolean paramBoolean)
    throws IllegalArgumentException
  {
    WifiAwareUtils.validateServiceName(mServiceName);
    if (TlvBufferUtils.isValid(mMatchFilter, 0, 1))
    {
      if ((mPublishType >= 0) && (mPublishType <= 1))
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
          if ((!paramBoolean) && (mEnableRanging)) {
            throw new IllegalArgumentException("Ranging is not supported");
          }
          return;
        }
        throw new IllegalArgumentException("Invalid ttlSec - must be non-negative");
      }
      paramCharacteristics = new StringBuilder();
      paramCharacteristics.append("Invalid publishType - ");
      paramCharacteristics.append(mPublishType);
      throw new IllegalArgumentException(paramCharacteristics.toString());
    }
    throw new IllegalArgumentException("Invalid txFilter configuration - LV fields do not match up to length");
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
    if (!(paramObject instanceof PublishConfig)) {
      return false;
    }
    paramObject = (PublishConfig)paramObject;
    if ((!Arrays.equals(mServiceName, mServiceName)) || (!Arrays.equals(mServiceSpecificInfo, mServiceSpecificInfo)) || (!Arrays.equals(mMatchFilter, mMatchFilter)) || (mPublishType != mPublishType) || (mTtlSec != mTtlSec) || (mEnableTerminateNotification != mEnableTerminateNotification) || (mEnableRanging != mEnableRanging)) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mServiceName, mServiceSpecificInfo, mMatchFilter, Integer.valueOf(mPublishType), Integer.valueOf(mTtlSec), Boolean.valueOf(mEnableTerminateNotification), Boolean.valueOf(mEnableRanging) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PublishConfig [mServiceName='");
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
    localStringBuilder.append(", mPublishType=");
    localStringBuilder.append(mPublishType);
    localStringBuilder.append(", mTtlSec=");
    localStringBuilder.append(mTtlSec);
    localStringBuilder.append(", mEnableTerminateNotification=");
    localStringBuilder.append(mEnableTerminateNotification);
    localStringBuilder.append(", mEnableRanging=");
    localStringBuilder.append(mEnableRanging);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByteArray(mServiceName);
    paramParcel.writeByteArray(mServiceSpecificInfo);
    paramParcel.writeByteArray(mMatchFilter);
    paramParcel.writeInt(mPublishType);
    paramParcel.writeInt(mTtlSec);
    paramParcel.writeInt(mEnableTerminateNotification);
    paramParcel.writeInt(mEnableRanging);
  }
  
  public static final class Builder
  {
    private boolean mEnableRanging = false;
    private boolean mEnableTerminateNotification = true;
    private byte[] mMatchFilter;
    private int mPublishType = 0;
    private byte[] mServiceName;
    private byte[] mServiceSpecificInfo;
    private int mTtlSec = 0;
    
    public Builder() {}
    
    public PublishConfig build()
    {
      return new PublishConfig(mServiceName, mServiceSpecificInfo, mMatchFilter, mPublishType, mTtlSec, mEnableTerminateNotification, mEnableRanging);
    }
    
    public Builder setMatchFilter(List<byte[]> paramList)
    {
      mMatchFilter = new TlvBufferUtils.TlvConstructor(0, 1).allocateAndPut(paramList).getArray();
      return this;
    }
    
    public Builder setPublishType(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt <= 1))
      {
        mPublishType = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid publishType - ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Builder setRangingEnabled(boolean paramBoolean)
    {
      mEnableRanging = paramBoolean;
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
  public static @interface PublishTypes {}
}
