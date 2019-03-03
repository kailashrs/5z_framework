package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class CellSignalStrengthCdma
  extends CellSignalStrength
  implements Parcelable
{
  public static final Parcelable.Creator<CellSignalStrengthCdma> CREATOR = new Parcelable.Creator()
  {
    public CellSignalStrengthCdma createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CellSignalStrengthCdma(paramAnonymousParcel, null);
    }
    
    public CellSignalStrengthCdma[] newArray(int paramAnonymousInt)
    {
      return new CellSignalStrengthCdma[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String LOG_TAG = "CellSignalStrengthCdma";
  private int mCdmaDbm;
  private int mCdmaEcio;
  private int mEvdoDbm;
  private int mEvdoEcio;
  private int mEvdoSnr;
  
  public CellSignalStrengthCdma()
  {
    setDefaultValues();
  }
  
  public CellSignalStrengthCdma(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i = Integer.MAX_VALUE;
    if ((paramInt1 > 0) && (paramInt1 < 120)) {
      paramInt1 = -paramInt1;
    } else {
      paramInt1 = Integer.MAX_VALUE;
    }
    mCdmaDbm = paramInt1;
    if ((paramInt2 > 0) && (paramInt2 < 160)) {
      paramInt1 = -paramInt2;
    } else {
      paramInt1 = Integer.MAX_VALUE;
    }
    mCdmaEcio = paramInt1;
    if ((paramInt3 > 0) && (paramInt3 < 120)) {
      paramInt1 = -paramInt3;
    } else {
      paramInt1 = Integer.MAX_VALUE;
    }
    mEvdoDbm = paramInt1;
    if ((paramInt4 > 0) && (paramInt4 < 160)) {
      paramInt1 = -paramInt4;
    } else {
      paramInt1 = Integer.MAX_VALUE;
    }
    mEvdoEcio = paramInt1;
    paramInt1 = i;
    if (paramInt5 > 0)
    {
      paramInt1 = i;
      if (paramInt5 <= 8) {
        paramInt1 = paramInt5;
      }
    }
    mEvdoSnr = paramInt1;
  }
  
  private CellSignalStrengthCdma(Parcel paramParcel)
  {
    mCdmaDbm = paramParcel.readInt();
    mCdmaEcio = paramParcel.readInt();
    mEvdoDbm = paramParcel.readInt();
    mEvdoEcio = paramParcel.readInt();
    mEvdoSnr = paramParcel.readInt();
  }
  
  public CellSignalStrengthCdma(CellSignalStrengthCdma paramCellSignalStrengthCdma)
  {
    copyFrom(paramCellSignalStrengthCdma);
  }
  
  private static void log(String paramString)
  {
    Rlog.w("CellSignalStrengthCdma", paramString);
  }
  
  public CellSignalStrengthCdma copy()
  {
    return new CellSignalStrengthCdma(this);
  }
  
  protected void copyFrom(CellSignalStrengthCdma paramCellSignalStrengthCdma)
  {
    mCdmaDbm = mCdmaDbm;
    mCdmaEcio = mCdmaEcio;
    mEvdoDbm = mEvdoDbm;
    mEvdoEcio = mEvdoEcio;
    mEvdoSnr = mEvdoSnr;
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
      CellSignalStrengthCdma localCellSignalStrengthCdma = (CellSignalStrengthCdma)paramObject;
      if (paramObject == null) {
        return false;
      }
      boolean bool2 = bool1;
      if (mCdmaDbm == mCdmaDbm)
      {
        bool2 = bool1;
        if (mCdmaEcio == mCdmaEcio)
        {
          bool2 = bool1;
          if (mEvdoDbm == mEvdoDbm)
          {
            bool2 = bool1;
            if (mEvdoEcio == mEvdoEcio)
            {
              bool2 = bool1;
              if (mEvdoSnr == mEvdoSnr) {
                bool2 = true;
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
    int i = getCdmaDbm();
    int j = getCdmaEcio();
    int k = 99;
    if (i == Integer.MAX_VALUE) {
      i = 99;
    }
    for (;;)
    {
      m = i;
      break label89;
      if (i >= -75)
      {
        i = 16;
      }
      else if (i >= -82)
      {
        i = 8;
      }
      else if (i >= -90)
      {
        i = 4;
      }
      else if (i >= -95)
      {
        i = 2;
      }
      else
      {
        if (i < -100) {
          break;
        }
        i = 1;
      }
    }
    int m = 99;
    label89:
    if (j == Integer.MAX_VALUE) {
      i = 99;
    }
    for (;;)
    {
      break;
      if (j >= -90) {
        i = 16;
      } else if (j >= -100) {
        i = 8;
      } else if (j >= -115) {
        i = 4;
      } else if (j >= 65406) {
        i = 2;
      } else if (j >= 65386) {
        i = 1;
      } else {
        i = k;
      }
    }
    if (m < i) {
      i = m;
    }
    return i;
  }
  
  public int getCdmaDbm()
  {
    return mCdmaDbm;
  }
  
  public int getCdmaEcio()
  {
    return mCdmaEcio;
  }
  
  public int getCdmaLevel()
  {
    int i = getCdmaDbm();
    int j = getCdmaEcio();
    int k = 0;
    if (i == Integer.MAX_VALUE) {
      i = 0;
    }
    for (;;)
    {
      m = i;
      break label73;
      if (i >= -75)
      {
        i = 4;
      }
      else if (i >= -85)
      {
        i = 3;
      }
      else if (i >= -95)
      {
        i = 2;
      }
      else
      {
        if (i < -100) {
          break;
        }
        i = 1;
      }
    }
    int m = 0;
    label73:
    if (j == Integer.MAX_VALUE) {
      i = 0;
    }
    for (;;)
    {
      break;
      if (j >= -90) {
        i = 4;
      } else if (j >= -110) {
        i = 3;
      } else if (j >= 65406) {
        i = 2;
      } else if (j >= 65386) {
        i = 1;
      } else {
        i = k;
      }
    }
    if (m < i) {
      i = m;
    }
    return i;
  }
  
  public int getDbm()
  {
    int i = getCdmaDbm();
    int j = getEvdoDbm();
    if (i >= j) {
      i = j;
    }
    return i;
  }
  
  public int getEvdoDbm()
  {
    return mEvdoDbm;
  }
  
  public int getEvdoEcio()
  {
    return mEvdoEcio;
  }
  
  public int getEvdoLevel()
  {
    int i = getEvdoDbm();
    int j = getEvdoSnr();
    int k = 0;
    if (i == Integer.MAX_VALUE) {
      i = 0;
    }
    for (;;)
    {
      m = i;
      break label73;
      if (i >= -65)
      {
        i = 4;
      }
      else if (i >= -75)
      {
        i = 3;
      }
      else if (i >= -90)
      {
        i = 2;
      }
      else
      {
        if (i < -105) {
          break;
        }
        i = 1;
      }
    }
    int m = 0;
    label73:
    if (j == Integer.MAX_VALUE) {
      i = 0;
    }
    for (;;)
    {
      break;
      if (j >= 7) {
        i = 4;
      } else if (j >= 5) {
        i = 3;
      } else if (j >= 3) {
        i = 2;
      } else if (j >= 1) {
        i = 1;
      } else {
        i = k;
      }
    }
    if (m < i) {
      i = m;
    }
    return i;
  }
  
  public int getEvdoSnr()
  {
    return mEvdoSnr;
  }
  
  public int getLevel()
  {
    int i = getCdmaLevel();
    int j = getEvdoLevel();
    if (j == 0) {
      i = getCdmaLevel();
    }
    for (;;)
    {
      break;
      if (i == 0) {
        i = getEvdoLevel();
      } else if (i >= j) {
        i = j;
      }
    }
    return i;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mCdmaDbm), Integer.valueOf(mCdmaEcio), Integer.valueOf(mEvdoDbm), Integer.valueOf(mEvdoEcio), Integer.valueOf(mEvdoSnr) });
  }
  
  public void setCdmaDbm(int paramInt)
  {
    mCdmaDbm = paramInt;
  }
  
  public void setCdmaEcio(int paramInt)
  {
    mCdmaEcio = paramInt;
  }
  
  public void setDefaultValues()
  {
    mCdmaDbm = Integer.MAX_VALUE;
    mCdmaEcio = Integer.MAX_VALUE;
    mEvdoDbm = Integer.MAX_VALUE;
    mEvdoEcio = Integer.MAX_VALUE;
    mEvdoSnr = Integer.MAX_VALUE;
  }
  
  public void setEvdoDbm(int paramInt)
  {
    mEvdoDbm = paramInt;
  }
  
  public void setEvdoEcio(int paramInt)
  {
    mEvdoEcio = paramInt;
  }
  
  public void setEvdoSnr(int paramInt)
  {
    mEvdoSnr = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CellSignalStrengthCdma: cdmaDbm=");
    localStringBuilder.append(mCdmaDbm);
    localStringBuilder.append(" cdmaEcio=");
    localStringBuilder.append(mCdmaEcio);
    localStringBuilder.append(" evdoDbm=");
    localStringBuilder.append(mEvdoDbm);
    localStringBuilder.append(" evdoEcio=");
    localStringBuilder.append(mEvdoEcio);
    localStringBuilder.append(" evdoSnr=");
    localStringBuilder.append(mEvdoSnr);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mCdmaDbm);
    paramParcel.writeInt(mCdmaEcio);
    paramParcel.writeInt(mEvdoDbm);
    paramParcel.writeInt(mEvdoEcio);
    paramParcel.writeInt(mEvdoSnr);
  }
}
