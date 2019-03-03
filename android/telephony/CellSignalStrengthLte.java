package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class CellSignalStrengthLte
  extends CellSignalStrength
  implements Parcelable
{
  public static final Parcelable.Creator<CellSignalStrengthLte> CREATOR = new Parcelable.Creator()
  {
    public CellSignalStrengthLte createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CellSignalStrengthLte(paramAnonymousParcel, null);
    }
    
    public CellSignalStrengthLte[] newArray(int paramAnonymousInt)
    {
      return new CellSignalStrengthLte[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String LOG_TAG = "CellSignalStrengthLte";
  private int mCqi;
  private int mRsrp;
  private int mRsrq;
  private int mRssnr;
  private int mSignalStrength;
  private int mTimingAdvance;
  
  public CellSignalStrengthLte()
  {
    setDefaultValues();
  }
  
  public CellSignalStrengthLte(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    mSignalStrength = paramInt1;
    mRsrp = paramInt2;
    mRsrq = paramInt3;
    mRssnr = paramInt4;
    mCqi = paramInt5;
    mTimingAdvance = paramInt6;
  }
  
  private CellSignalStrengthLte(Parcel paramParcel)
  {
    mSignalStrength = paramParcel.readInt();
    mRsrp = paramParcel.readInt();
    if (mRsrp != Integer.MAX_VALUE) {
      mRsrp *= -1;
    }
    mRsrq = paramParcel.readInt();
    if (mRsrq != Integer.MAX_VALUE) {
      mRsrq *= -1;
    }
    mRssnr = paramParcel.readInt();
    mCqi = paramParcel.readInt();
    mTimingAdvance = paramParcel.readInt();
  }
  
  public CellSignalStrengthLte(CellSignalStrengthLte paramCellSignalStrengthLte)
  {
    copyFrom(paramCellSignalStrengthLte);
  }
  
  private static void log(String paramString)
  {
    Rlog.w("CellSignalStrengthLte", paramString);
  }
  
  public CellSignalStrengthLte copy()
  {
    return new CellSignalStrengthLte(this);
  }
  
  protected void copyFrom(CellSignalStrengthLte paramCellSignalStrengthLte)
  {
    mSignalStrength = mSignalStrength;
    mRsrp = mRsrp;
    mRsrq = mRsrq;
    mRssnr = mRssnr;
    mCqi = mCqi;
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
      CellSignalStrengthLte localCellSignalStrengthLte = (CellSignalStrengthLte)paramObject;
      if (paramObject == null) {
        return false;
      }
      boolean bool2 = bool1;
      if (mSignalStrength == mSignalStrength)
      {
        bool2 = bool1;
        if (mRsrp == mRsrp)
        {
          bool2 = bool1;
          if (mRsrq == mRsrq)
          {
            bool2 = bool1;
            if (mRssnr == mRssnr)
            {
              bool2 = bool1;
              if (mCqi == mCqi)
              {
                bool2 = bool1;
                if (mTimingAdvance == mTimingAdvance) {
                  bool2 = true;
                }
              }
            }
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
    int i = getDbm();
    if (i == Integer.MAX_VALUE) {
      i = 99;
    } else if (i <= 65396) {
      i = 0;
    } else if (i >= -43) {
      i = 97;
    } else {
      i += 140;
    }
    return i;
  }
  
  public int getCqi()
  {
    return mCqi;
  }
  
  public int getDbm()
  {
    return mRsrp;
  }
  
  public int getLevel()
  {
    int i;
    if (mRsrp == Integer.MAX_VALUE) {
      i = 0;
    } else if (mRsrp >= -95) {
      i = 4;
    } else if (mRsrp >= -105) {
      i = 3;
    } else if (mRsrp >= -115) {
      i = 2;
    } else {
      i = 1;
    }
    int j;
    if (mRssnr == Integer.MAX_VALUE) {
      j = 0;
    } else if (mRssnr >= 45) {
      j = 4;
    } else if (mRssnr >= 10) {
      j = 3;
    } else if (mRssnr >= -30) {
      j = 2;
    } else {
      j = 1;
    }
    if (mRsrp == Integer.MAX_VALUE) {
      i = j;
    }
    for (;;)
    {
      break;
      if (mRssnr != Integer.MAX_VALUE) {
        if (j < i) {
          i = j;
        }
      }
    }
    return i;
  }
  
  public int getRsrp()
  {
    return mRsrp;
  }
  
  public int getRsrq()
  {
    return mRsrq;
  }
  
  public int getRssnr()
  {
    return mRssnr;
  }
  
  public int getTimingAdvance()
  {
    return mTimingAdvance;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mSignalStrength), Integer.valueOf(mRsrp), Integer.valueOf(mRsrq), Integer.valueOf(mRssnr), Integer.valueOf(mCqi), Integer.valueOf(mTimingAdvance) });
  }
  
  public void setDefaultValues()
  {
    mSignalStrength = Integer.MAX_VALUE;
    mRsrp = Integer.MAX_VALUE;
    mRsrq = Integer.MAX_VALUE;
    mRssnr = Integer.MAX_VALUE;
    mCqi = Integer.MAX_VALUE;
    mTimingAdvance = Integer.MAX_VALUE;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CellSignalStrengthLte: ss=");
    localStringBuilder.append(mSignalStrength);
    localStringBuilder.append(" rsrp=");
    localStringBuilder.append(mRsrp);
    localStringBuilder.append(" rsrq=");
    localStringBuilder.append(mRsrq);
    localStringBuilder.append(" rssnr=");
    localStringBuilder.append(mRssnr);
    localStringBuilder.append(" cqi=");
    localStringBuilder.append(mCqi);
    localStringBuilder.append(" ta=");
    localStringBuilder.append(mTimingAdvance);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSignalStrength);
    int i = mRsrp;
    paramInt = mRsrp;
    int j = 1;
    if (paramInt != Integer.MAX_VALUE) {
      paramInt = -1;
    } else {
      paramInt = 1;
    }
    paramParcel.writeInt(i * paramInt);
    i = mRsrq;
    paramInt = j;
    if (mRsrq != Integer.MAX_VALUE) {
      paramInt = -1;
    }
    paramParcel.writeInt(i * paramInt);
    paramParcel.writeInt(mRssnr);
    paramParcel.writeInt(mCqi);
    paramParcel.writeInt(mTimingAdvance);
  }
}
