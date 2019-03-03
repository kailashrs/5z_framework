package android.telephony.euicc;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Objects;

@SystemApi
public final class EuiccNotification
  implements Parcelable
{
  public static final int ALL_EVENTS = 15;
  public static final Parcelable.Creator<EuiccNotification> CREATOR = new Parcelable.Creator()
  {
    public EuiccNotification createFromParcel(Parcel paramAnonymousParcel)
    {
      return new EuiccNotification(paramAnonymousParcel, null);
    }
    
    public EuiccNotification[] newArray(int paramAnonymousInt)
    {
      return new EuiccNotification[paramAnonymousInt];
    }
  };
  public static final int EVENT_DELETE = 8;
  public static final int EVENT_DISABLE = 4;
  public static final int EVENT_ENABLE = 2;
  public static final int EVENT_INSTALL = 1;
  private final byte[] mData;
  private final int mEvent;
  private final int mSeq;
  private final String mTargetAddr;
  
  public EuiccNotification(int paramInt1, String paramString, int paramInt2, byte[] paramArrayOfByte)
  {
    mSeq = paramInt1;
    mTargetAddr = paramString;
    mEvent = paramInt2;
    mData = paramArrayOfByte;
  }
  
  private EuiccNotification(Parcel paramParcel)
  {
    mSeq = paramParcel.readInt();
    mTargetAddr = paramParcel.readString();
    mEvent = paramParcel.readInt();
    mData = paramParcel.createByteArray();
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
      paramObject = (EuiccNotification)paramObject;
      if ((mSeq != mSeq) || (!Objects.equals(mTargetAddr, mTargetAddr)) || (mEvent != mEvent) || (!Arrays.equals(mData, mData))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public byte[] getData()
  {
    return mData;
  }
  
  public int getEvent()
  {
    return mEvent;
  }
  
  public int getSeq()
  {
    return mSeq;
  }
  
  public String getTargetAddr()
  {
    return mTargetAddr;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * 1 + mSeq) + Objects.hashCode(mTargetAddr)) + mEvent) + Arrays.hashCode(mData);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("EuiccNotification (seq=");
    localStringBuilder.append(mSeq);
    localStringBuilder.append(", targetAddr=");
    localStringBuilder.append(mTargetAddr);
    localStringBuilder.append(", event=");
    localStringBuilder.append(mEvent);
    localStringBuilder.append(", data=");
    Object localObject;
    if (mData == null)
    {
      localObject = "null";
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("byte[");
      ((StringBuilder)localObject).append(mData.length);
      ((StringBuilder)localObject).append("]");
      localObject = ((StringBuilder)localObject).toString();
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSeq);
    paramParcel.writeString(mTargetAddr);
    paramParcel.writeInt(mEvent);
    paramParcel.writeByteArray(mData);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Event {}
}
