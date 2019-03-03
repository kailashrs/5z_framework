package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class BluetoothActivityEnergyInfo
  implements Parcelable
{
  public static final int BT_STACK_STATE_INVALID = 0;
  public static final int BT_STACK_STATE_STATE_ACTIVE = 1;
  public static final int BT_STACK_STATE_STATE_IDLE = 3;
  public static final int BT_STACK_STATE_STATE_SCANNING = 2;
  public static final Parcelable.Creator<BluetoothActivityEnergyInfo> CREATOR = new Parcelable.Creator()
  {
    public BluetoothActivityEnergyInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothActivityEnergyInfo(paramAnonymousParcel);
    }
    
    public BluetoothActivityEnergyInfo[] newArray(int paramAnonymousInt)
    {
      return new BluetoothActivityEnergyInfo[paramAnonymousInt];
    }
  };
  private int mBluetoothStackState;
  private long mControllerEnergyUsed;
  private long mControllerIdleTimeMs;
  private long mControllerRxTimeMs;
  private long mControllerTxTimeMs;
  private final long mTimestamp;
  private UidTraffic[] mUidTraffic;
  
  public BluetoothActivityEnergyInfo(long paramLong1, int paramInt, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
  {
    mTimestamp = paramLong1;
    mBluetoothStackState = paramInt;
    mControllerTxTimeMs = paramLong2;
    mControllerRxTimeMs = paramLong3;
    mControllerIdleTimeMs = paramLong4;
    mControllerEnergyUsed = paramLong5;
  }
  
  BluetoothActivityEnergyInfo(Parcel paramParcel)
  {
    mTimestamp = paramParcel.readLong();
    mBluetoothStackState = paramParcel.readInt();
    mControllerTxTimeMs = paramParcel.readLong();
    mControllerRxTimeMs = paramParcel.readLong();
    mControllerIdleTimeMs = paramParcel.readLong();
    mControllerEnergyUsed = paramParcel.readLong();
    mUidTraffic = ((UidTraffic[])paramParcel.createTypedArray(UidTraffic.CREATOR));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getBluetoothStackState()
  {
    return mBluetoothStackState;
  }
  
  public long getControllerEnergyUsed()
  {
    return mControllerEnergyUsed;
  }
  
  public long getControllerIdleTimeMillis()
  {
    return mControllerIdleTimeMs;
  }
  
  public long getControllerRxTimeMillis()
  {
    return mControllerRxTimeMs;
  }
  
  public long getControllerTxTimeMillis()
  {
    return mControllerTxTimeMs;
  }
  
  public long getTimeStamp()
  {
    return mTimestamp;
  }
  
  public UidTraffic[] getUidTraffic()
  {
    return mUidTraffic;
  }
  
  public boolean isValid()
  {
    boolean bool;
    if ((mControllerTxTimeMs >= 0L) && (mControllerRxTimeMs >= 0L) && (mControllerIdleTimeMs >= 0L)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setUidTraffic(UidTraffic[] paramArrayOfUidTraffic)
  {
    mUidTraffic = paramArrayOfUidTraffic;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("BluetoothActivityEnergyInfo{ mTimestamp=");
    localStringBuilder.append(mTimestamp);
    localStringBuilder.append(" mBluetoothStackState=");
    localStringBuilder.append(mBluetoothStackState);
    localStringBuilder.append(" mControllerTxTimeMs=");
    localStringBuilder.append(mControllerTxTimeMs);
    localStringBuilder.append(" mControllerRxTimeMs=");
    localStringBuilder.append(mControllerRxTimeMs);
    localStringBuilder.append(" mControllerIdleTimeMs=");
    localStringBuilder.append(mControllerIdleTimeMs);
    localStringBuilder.append(" mControllerEnergyUsed=");
    localStringBuilder.append(mControllerEnergyUsed);
    localStringBuilder.append(" mUidTraffic=");
    localStringBuilder.append(Arrays.toString(mUidTraffic));
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mTimestamp);
    paramParcel.writeInt(mBluetoothStackState);
    paramParcel.writeLong(mControllerTxTimeMs);
    paramParcel.writeLong(mControllerRxTimeMs);
    paramParcel.writeLong(mControllerIdleTimeMs);
    paramParcel.writeLong(mControllerEnergyUsed);
    paramParcel.writeTypedArray(mUidTraffic, paramInt);
  }
}
