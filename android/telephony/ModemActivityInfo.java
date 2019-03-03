package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public class ModemActivityInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ModemActivityInfo> CREATOR = new Parcelable.Creator()
  {
    public ModemActivityInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      long l = paramAnonymousParcel.readLong();
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      int[] arrayOfInt = new int[5];
      for (int k = 0; k < 5; k++) {
        arrayOfInt[k] = paramAnonymousParcel.readInt();
      }
      return new ModemActivityInfo(l, i, j, arrayOfInt, paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public ModemActivityInfo[] newArray(int paramAnonymousInt)
    {
      return new ModemActivityInfo[paramAnonymousInt];
    }
  };
  public static final int TX_POWER_LEVELS = 5;
  private int mEnergyUsed;
  private int mIdleTimeMs;
  private int mRxTimeMs;
  private int mSleepTimeMs;
  private long mTimestamp;
  private int[] mTxTimeMs = new int[5];
  
  public ModemActivityInfo(long paramLong, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4)
  {
    mTimestamp = paramLong;
    mSleepTimeMs = paramInt1;
    mIdleTimeMs = paramInt2;
    if (paramArrayOfInt != null) {
      System.arraycopy(paramArrayOfInt, 0, mTxTimeMs, 0, Math.min(paramArrayOfInt.length, 5));
    }
    mRxTimeMs = paramInt3;
    mEnergyUsed = paramInt4;
  }
  
  private boolean isEmpty()
  {
    int[] arrayOfInt = getTxTimeMillis();
    int i = arrayOfInt.length;
    boolean bool = false;
    for (int j = 0; j < i; j++) {
      if (arrayOfInt[j] != 0) {
        return false;
      }
    }
    if ((getIdleTimeMillis() == 0) && (getSleepTimeMillis() == 0) && (getRxTimeMillis() == 0) && (getEnergyUsed() == 0)) {
      bool = true;
    }
    return bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getEnergyUsed()
  {
    return mEnergyUsed;
  }
  
  public int getIdleTimeMillis()
  {
    return mIdleTimeMs;
  }
  
  public int getRxTimeMillis()
  {
    return mRxTimeMs;
  }
  
  public int getSleepTimeMillis()
  {
    return mSleepTimeMs;
  }
  
  public long getTimestamp()
  {
    return mTimestamp;
  }
  
  public int[] getTxTimeMillis()
  {
    return mTxTimeMs;
  }
  
  public boolean isValid()
  {
    int[] arrayOfInt = getTxTimeMillis();
    int i = arrayOfInt.length;
    boolean bool = false;
    for (int j = 0; j < i; j++) {
      if (arrayOfInt[j] < 0) {
        return false;
      }
    }
    if ((getIdleTimeMillis() >= 0) && (getSleepTimeMillis() >= 0) && (getRxTimeMillis() >= 0) && (getEnergyUsed() >= 0) && (!isEmpty())) {
      bool = true;
    }
    return bool;
  }
  
  public void setEnergyUsed(int paramInt)
  {
    mEnergyUsed = paramInt;
  }
  
  public void setIdleTimeMillis(int paramInt)
  {
    mIdleTimeMs = paramInt;
  }
  
  public void setRxTimeMillis(int paramInt)
  {
    mRxTimeMs = paramInt;
  }
  
  public void setSleepTimeMillis(int paramInt)
  {
    mSleepTimeMs = paramInt;
  }
  
  public void setTimestamp(long paramLong)
  {
    mTimestamp = paramLong;
  }
  
  public void setTxTimeMillis(int[] paramArrayOfInt)
  {
    mTxTimeMs = paramArrayOfInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ModemActivityInfo{ mTimestamp=");
    localStringBuilder.append(mTimestamp);
    localStringBuilder.append(" mSleepTimeMs=");
    localStringBuilder.append(mSleepTimeMs);
    localStringBuilder.append(" mIdleTimeMs=");
    localStringBuilder.append(mIdleTimeMs);
    localStringBuilder.append(" mTxTimeMs[]=");
    localStringBuilder.append(Arrays.toString(mTxTimeMs));
    localStringBuilder.append(" mRxTimeMs=");
    localStringBuilder.append(mRxTimeMs);
    localStringBuilder.append(" mEnergyUsed=");
    localStringBuilder.append(mEnergyUsed);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mTimestamp);
    paramParcel.writeInt(mSleepTimeMs);
    paramParcel.writeInt(mIdleTimeMs);
    for (paramInt = 0; paramInt < 5; paramInt++) {
      paramParcel.writeInt(mTxTimeMs[paramInt]);
    }
    paramParcel.writeInt(mRxTimeMs);
    paramParcel.writeInt(mEnergyUsed);
  }
}
