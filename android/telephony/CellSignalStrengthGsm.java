package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class CellSignalStrengthGsm
  extends CellSignalStrength
  implements Parcelable
{
  public static final Parcelable.Creator<CellSignalStrengthGsm> CREATOR = new Parcelable.Creator()
  {
    public CellSignalStrengthGsm createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CellSignalStrengthGsm(paramAnonymousParcel, null);
    }
    
    public CellSignalStrengthGsm[] newArray(int paramAnonymousInt)
    {
      return new CellSignalStrengthGsm[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final int GSM_SIGNAL_STRENGTH_GOOD = 8;
  private static final int GSM_SIGNAL_STRENGTH_GREAT = 12;
  private static final int GSM_SIGNAL_STRENGTH_MODERATE = 5;
  private static final String LOG_TAG = "CellSignalStrengthGsm";
  private int mBitErrorRate;
  private int mSignalStrength;
  private int mTimingAdvance;
  
  public CellSignalStrengthGsm()
  {
    setDefaultValues();
  }
  
  public CellSignalStrengthGsm(int paramInt1, int paramInt2)
  {
    this(paramInt1, paramInt2, Integer.MAX_VALUE);
  }
  
  public CellSignalStrengthGsm(int paramInt1, int paramInt2, int paramInt3)
  {
    mSignalStrength = paramInt1;
    mBitErrorRate = paramInt2;
    mTimingAdvance = paramInt3;
  }
  
  private CellSignalStrengthGsm(Parcel paramParcel)
  {
    mSignalStrength = paramParcel.readInt();
    mBitErrorRate = paramParcel.readInt();
    mTimingAdvance = paramParcel.readInt();
  }
  
  public CellSignalStrengthGsm(CellSignalStrengthGsm paramCellSignalStrengthGsm)
  {
    copyFrom(paramCellSignalStrengthGsm);
  }
  
  private static void log(String paramString)
  {
    Rlog.w("CellSignalStrengthGsm", paramString);
  }
  
  public CellSignalStrengthGsm copy()
  {
    return new CellSignalStrengthGsm(this);
  }
  
  protected void copyFrom(CellSignalStrengthGsm paramCellSignalStrengthGsm)
  {
    mSignalStrength = mSignalStrength;
    mBitErrorRate = mBitErrorRate;
    mTimingAdvance = mTimingAdvance;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    try
    {
      CellSignalStrengthGsm localCellSignalStrengthGsm = (CellSignalStrengthGsm)paramObject;
      if (paramObject == null) {
        return false;
      }
      boolean bool2 = bool1;
      if (mSignalStrength == mSignalStrength)
      {
        bool2 = bool1;
        if (mBitErrorRate == mBitErrorRate)
        {
          bool2 = bool1;
          if (mTimingAdvance == mTimingAdvance) {
            bool2 = true;
          }
        }
      }
      return bool2;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public int getAsuLevel()
  {
    return mSignalStrength;
  }
  
  public int getDbm()
  {
    int i = mSignalStrength;
    int j = Integer.MAX_VALUE;
    if (i == 99) {
      i = Integer.MAX_VALUE;
    }
    if (i != Integer.MAX_VALUE) {
      i = -113 + 2 * i;
    } else {
      i = j;
    }
    return i;
  }
  
  public int getLevel()
  {
    int i = mSignalStrength;
    if ((i > 2) && (i != 99))
    {
      if (i >= 12) {
        i = 4;
      } else if (i >= 8) {
        i = 3;
      } else if (i >= 5) {
        i = 2;
      } else {
        i = 1;
      }
    }
    else {
      i = 0;
    }
    return i;
  }
  
  public int getTimingAdvance()
  {
    return mTimingAdvance;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mSignalStrength), Integer.valueOf(mBitErrorRate), Integer.valueOf(mTimingAdvance) });
  }
  
  public void setDefaultValues()
  {
    mSignalStrength = Integer.MAX_VALUE;
    mBitErrorRate = Integer.MAX_VALUE;
    mTimingAdvance = Integer.MAX_VALUE;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CellSignalStrengthGsm: ss=");
    localStringBuilder.append(mSignalStrength);
    localStringBuilder.append(" ber=");
    localStringBuilder.append(mBitErrorRate);
    localStringBuilder.append(" mTa=");
    localStringBuilder.append(mTimingAdvance);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSignalStrength);
    paramParcel.writeInt(mBitErrorRate);
    paramParcel.writeInt(mTimingAdvance);
  }
}
