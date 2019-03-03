package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class UidTraffic
  implements Cloneable, Parcelable
{
  public static final Parcelable.Creator<UidTraffic> CREATOR = new Parcelable.Creator()
  {
    public UidTraffic createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UidTraffic(paramAnonymousParcel);
    }
    
    public UidTraffic[] newArray(int paramAnonymousInt)
    {
      return new UidTraffic[paramAnonymousInt];
    }
  };
  private final int mAppUid;
  private long mRxBytes;
  private long mTxBytes;
  
  public UidTraffic(int paramInt)
  {
    mAppUid = paramInt;
  }
  
  public UidTraffic(int paramInt, long paramLong1, long paramLong2)
  {
    mAppUid = paramInt;
    mRxBytes = paramLong1;
    mTxBytes = paramLong2;
  }
  
  UidTraffic(Parcel paramParcel)
  {
    mAppUid = paramParcel.readInt();
    mRxBytes = paramParcel.readLong();
    mTxBytes = paramParcel.readLong();
  }
  
  public void addRxBytes(long paramLong)
  {
    mRxBytes += paramLong;
  }
  
  public void addTxBytes(long paramLong)
  {
    mTxBytes += paramLong;
  }
  
  public UidTraffic clone()
  {
    return new UidTraffic(mAppUid, mRxBytes, mTxBytes);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getRxBytes()
  {
    return mRxBytes;
  }
  
  public long getTxBytes()
  {
    return mTxBytes;
  }
  
  public int getUid()
  {
    return mAppUid;
  }
  
  public void setRxBytes(long paramLong)
  {
    mRxBytes = paramLong;
  }
  
  public void setTxBytes(long paramLong)
  {
    mTxBytes = paramLong;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UidTraffic{mAppUid=");
    localStringBuilder.append(mAppUid);
    localStringBuilder.append(", mRxBytes=");
    localStringBuilder.append(mRxBytes);
    localStringBuilder.append(", mTxBytes=");
    localStringBuilder.append(mTxBytes);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mAppUid);
    paramParcel.writeLong(mRxBytes);
    paramParcel.writeLong(mTxBytes);
  }
}
