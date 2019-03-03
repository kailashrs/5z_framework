package android.bluetooth.le;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class PeriodicAdvertisingReport
  implements Parcelable
{
  public static final Parcelable.Creator<PeriodicAdvertisingReport> CREATOR = new Parcelable.Creator()
  {
    public PeriodicAdvertisingReport createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PeriodicAdvertisingReport(paramAnonymousParcel, null);
    }
    
    public PeriodicAdvertisingReport[] newArray(int paramAnonymousInt)
    {
      return new PeriodicAdvertisingReport[paramAnonymousInt];
    }
  };
  public static final int DATA_COMPLETE = 0;
  public static final int DATA_INCOMPLETE_TRUNCATED = 2;
  private ScanRecord mData;
  private int mDataStatus;
  private int mRssi;
  private int mSyncHandle;
  private long mTimestampNanos;
  private int mTxPower;
  
  public PeriodicAdvertisingReport(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ScanRecord paramScanRecord)
  {
    mSyncHandle = paramInt1;
    mTxPower = paramInt2;
    mRssi = paramInt3;
    mDataStatus = paramInt4;
    mData = paramScanRecord;
  }
  
  private PeriodicAdvertisingReport(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    mSyncHandle = paramParcel.readInt();
    mTxPower = paramParcel.readInt();
    mRssi = paramParcel.readInt();
    mDataStatus = paramParcel.readInt();
    if (paramParcel.readInt() == 1) {
      mData = ScanRecord.parseFromBytes(paramParcel.createByteArray());
    }
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
      paramObject = (PeriodicAdvertisingReport)paramObject;
      if ((mSyncHandle != mSyncHandle) || (mTxPower != mTxPower) || (mRssi != mRssi) || (mDataStatus != mDataStatus) || (!Objects.equals(mData, mData)) || (mTimestampNanos != mTimestampNanos)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public ScanRecord getData()
  {
    return mData;
  }
  
  public int getDataStatus()
  {
    return mDataStatus;
  }
  
  public int getRssi()
  {
    return mRssi;
  }
  
  public int getSyncHandle()
  {
    return mSyncHandle;
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
    return Objects.hash(new Object[] { Integer.valueOf(mSyncHandle), Integer.valueOf(mTxPower), Integer.valueOf(mRssi), Integer.valueOf(mDataStatus), mData, Long.valueOf(mTimestampNanos) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PeriodicAdvertisingReport{syncHandle=");
    localStringBuilder.append(mSyncHandle);
    localStringBuilder.append(", txPower=");
    localStringBuilder.append(mTxPower);
    localStringBuilder.append(", rssi=");
    localStringBuilder.append(mRssi);
    localStringBuilder.append(", dataStatus=");
    localStringBuilder.append(mDataStatus);
    localStringBuilder.append(", data=");
    localStringBuilder.append(Objects.toString(mData));
    localStringBuilder.append(", timestampNanos=");
    localStringBuilder.append(mTimestampNanos);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSyncHandle);
    paramParcel.writeInt(mTxPower);
    paramParcel.writeInt(mRssi);
    paramParcel.writeInt(mDataStatus);
    if (mData != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeByteArray(mData.getBytes());
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
}
