package android.bluetooth.le;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class ScanResult
  implements Parcelable
{
  public static final Parcelable.Creator<ScanResult> CREATOR = new Parcelable.Creator()
  {
    public ScanResult createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ScanResult(paramAnonymousParcel, null);
    }
    
    public ScanResult[] newArray(int paramAnonymousInt)
    {
      return new ScanResult[paramAnonymousInt];
    }
  };
  public static final int DATA_COMPLETE = 0;
  public static final int DATA_TRUNCATED = 2;
  private static final int ET_CONNECTABLE_MASK = 1;
  private static final int ET_LEGACY_MASK = 16;
  public static final int PERIODIC_INTERVAL_NOT_PRESENT = 0;
  public static final int PHY_UNUSED = 0;
  public static final int SID_NOT_PRESENT = 255;
  public static final int TX_POWER_NOT_PRESENT = 127;
  private int mAdvertisingSid;
  private BluetoothDevice mDevice;
  private int mEventType;
  private int mPeriodicAdvertisingInterval;
  private int mPrimaryPhy;
  private int mRssi;
  private ScanRecord mScanRecord;
  private int mSecondaryPhy;
  private long mTimestampNanos;
  private int mTxPower;
  
  public ScanResult(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ScanRecord paramScanRecord, long paramLong)
  {
    mDevice = paramBluetoothDevice;
    mEventType = paramInt1;
    mPrimaryPhy = paramInt2;
    mSecondaryPhy = paramInt3;
    mAdvertisingSid = paramInt4;
    mTxPower = paramInt5;
    mRssi = paramInt6;
    mPeriodicAdvertisingInterval = paramInt7;
    mScanRecord = paramScanRecord;
    mTimestampNanos = paramLong;
  }
  
  @Deprecated
  public ScanResult(BluetoothDevice paramBluetoothDevice, ScanRecord paramScanRecord, int paramInt, long paramLong)
  {
    mDevice = paramBluetoothDevice;
    mScanRecord = paramScanRecord;
    mRssi = paramInt;
    mTimestampNanos = paramLong;
    mEventType = 17;
    mPrimaryPhy = 1;
    mSecondaryPhy = 0;
    mAdvertisingSid = 255;
    mTxPower = 127;
    mPeriodicAdvertisingInterval = 0;
  }
  
  private ScanResult(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    if (paramParcel.readInt() == 1) {
      mDevice = ((BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel));
    }
    if (paramParcel.readInt() == 1) {
      mScanRecord = ScanRecord.parseFromBytes(paramParcel.createByteArray());
    }
    mRssi = paramParcel.readInt();
    mTimestampNanos = paramParcel.readLong();
    mEventType = paramParcel.readInt();
    mPrimaryPhy = paramParcel.readInt();
    mSecondaryPhy = paramParcel.readInt();
    mAdvertisingSid = paramParcel.readInt();
    mTxPower = paramParcel.readInt();
    mPeriodicAdvertisingInterval = paramParcel.readInt();
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
      paramObject = (ScanResult)paramObject;
      if ((!Objects.equals(mDevice, mDevice)) || (mRssi != mRssi) || (!Objects.equals(mScanRecord, mScanRecord)) || (mTimestampNanos != mTimestampNanos) || (mEventType != mEventType) || (mPrimaryPhy != mPrimaryPhy) || (mSecondaryPhy != mSecondaryPhy) || (mAdvertisingSid != mAdvertisingSid) || (mTxPower != mTxPower) || (mPeriodicAdvertisingInterval != mPeriodicAdvertisingInterval)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int getAdvertisingSid()
  {
    return mAdvertisingSid;
  }
  
  public int getDataStatus()
  {
    return mEventType >> 5 & 0x3;
  }
  
  public BluetoothDevice getDevice()
  {
    return mDevice;
  }
  
  public int getPeriodicAdvertisingInterval()
  {
    return mPeriodicAdvertisingInterval;
  }
  
  public int getPrimaryPhy()
  {
    return mPrimaryPhy;
  }
  
  public int getRssi()
  {
    return mRssi;
  }
  
  public ScanRecord getScanRecord()
  {
    return mScanRecord;
  }
  
  public int getSecondaryPhy()
  {
    return mSecondaryPhy;
  }
  
  public long getTimestampNanos()
  {
    return mTimestampNanos;
  }
  
  public int getTxPower()
  {
    return mTxPower;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mDevice, Integer.valueOf(mRssi), mScanRecord, Long.valueOf(mTimestampNanos), Integer.valueOf(mEventType), Integer.valueOf(mPrimaryPhy), Integer.valueOf(mSecondaryPhy), Integer.valueOf(mAdvertisingSid), Integer.valueOf(mTxPower), Integer.valueOf(mPeriodicAdvertisingInterval) });
  }
  
  public boolean isConnectable()
  {
    int i = mEventType;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isLegacy()
  {
    boolean bool;
    if ((mEventType & 0x10) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ScanResult{device=");
    localStringBuilder.append(mDevice);
    localStringBuilder.append(", scanRecord=");
    localStringBuilder.append(Objects.toString(mScanRecord));
    localStringBuilder.append(", rssi=");
    localStringBuilder.append(mRssi);
    localStringBuilder.append(", timestampNanos=");
    localStringBuilder.append(mTimestampNanos);
    localStringBuilder.append(", eventType=");
    localStringBuilder.append(mEventType);
    localStringBuilder.append(", primaryPhy=");
    localStringBuilder.append(mPrimaryPhy);
    localStringBuilder.append(", secondaryPhy=");
    localStringBuilder.append(mSecondaryPhy);
    localStringBuilder.append(", advertisingSid=");
    localStringBuilder.append(mAdvertisingSid);
    localStringBuilder.append(", txPower=");
    localStringBuilder.append(mTxPower);
    localStringBuilder.append(", periodicAdvertisingInterval=");
    localStringBuilder.append(mPeriodicAdvertisingInterval);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mDevice != null)
    {
      paramParcel.writeInt(1);
      mDevice.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mScanRecord != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeByteArray(mScanRecord.getBytes());
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(mRssi);
    paramParcel.writeLong(mTimestampNanos);
    paramParcel.writeInt(mEventType);
    paramParcel.writeInt(mPrimaryPhy);
    paramParcel.writeInt(mSecondaryPhy);
    paramParcel.writeInt(mAdvertisingSid);
    paramParcel.writeInt(mTxPower);
    paramParcel.writeInt(mPeriodicAdvertisingInterval);
  }
}
