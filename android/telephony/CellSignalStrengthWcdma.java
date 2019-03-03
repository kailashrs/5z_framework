package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class CellSignalStrengthWcdma
  extends CellSignalStrength
  implements Parcelable
{
  public static final Parcelable.Creator<CellSignalStrengthWcdma> CREATOR = new Parcelable.Creator()
  {
    public CellSignalStrengthWcdma createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CellSignalStrengthWcdma(paramAnonymousParcel, null);
    }
    
    public CellSignalStrengthWcdma[] newArray(int paramAnonymousInt)
    {
      return new CellSignalStrengthWcdma[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String LOG_TAG = "CellSignalStrengthWcdma";
  private static final int WCDMA_SIGNAL_STRENGTH_GOOD = 8;
  private static final int WCDMA_SIGNAL_STRENGTH_GREAT = 12;
  private static final int WCDMA_SIGNAL_STRENGTH_MODERATE = 5;
  private int mBitErrorRate;
  private int mSignalStrength;
  
  public CellSignalStrengthWcdma()
  {
    setDefaultValues();
  }
  
  public CellSignalStrengthWcdma(int paramInt1, int paramInt2)
  {
    mSignalStrength = paramInt1;
    mBitErrorRate = paramInt2;
  }
  
  private CellSignalStrengthWcdma(Parcel paramParcel)
  {
    mSignalStrength = paramParcel.readInt();
    mBitErrorRate = paramParcel.readInt();
  }
  
  public CellSignalStrengthWcdma(CellSignalStrengthWcdma paramCellSignalStrengthWcdma)
  {
    copyFrom(paramCellSignalStrengthWcdma);
  }
  
  private static void log(String paramString)
  {
    Rlog.w("CellSignalStrengthWcdma", paramString);
  }
  
  public CellSignalStrengthWcdma copy()
  {
    return new CellSignalStrengthWcdma(this);
  }
  
  protected void copyFrom(CellSignalStrengthWcdma paramCellSignalStrengthWcdma)
  {
    mSignalStrength = mSignalStrength;
    mBitErrorRate = mBitErrorRate;
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
      CellSignalStrengthWcdma localCellSignalStrengthWcdma = (CellSignalStrengthWcdma)paramObject;
      if (paramObject == null) {
        return false;
      }
      boolean bool2 = bool1;
      if (mSignalStrength == mSignalStrength)
      {
        bool2 = bool1;
        if (mBitErrorRate == mBitErrorRate) {
          bool2 = true;
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
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mSignalStrength), Integer.valueOf(mBitErrorRate) });
  }
  
  public void setDefaultValues()
  {
    mSignalStrength = Integer.MAX_VALUE;
    mBitErrorRate = Integer.MAX_VALUE;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CellSignalStrengthWcdma: ss=");
    localStringBuilder.append(mSignalStrength);
    localStringBuilder.append(" ber=");
    localStringBuilder.append(mBitErrorRate);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSignalStrength);
    paramParcel.writeInt(mBitErrorRate);
  }
}
