package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class CellInfoCdma
  extends CellInfo
  implements Parcelable
{
  public static final Parcelable.Creator<CellInfoCdma> CREATOR = new Parcelable.Creator()
  {
    public CellInfoCdma createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel.readInt();
      return CellInfoCdma.createFromParcelBody(paramAnonymousParcel);
    }
    
    public CellInfoCdma[] newArray(int paramAnonymousInt)
    {
      return new CellInfoCdma[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String LOG_TAG = "CellInfoCdma";
  private CellIdentityCdma mCellIdentityCdma;
  private CellSignalStrengthCdma mCellSignalStrengthCdma;
  
  public CellInfoCdma()
  {
    mCellIdentityCdma = new CellIdentityCdma();
    mCellSignalStrengthCdma = new CellSignalStrengthCdma();
  }
  
  private CellInfoCdma(Parcel paramParcel)
  {
    super(paramParcel);
    mCellIdentityCdma = ((CellIdentityCdma)CellIdentityCdma.CREATOR.createFromParcel(paramParcel));
    mCellSignalStrengthCdma = ((CellSignalStrengthCdma)CellSignalStrengthCdma.CREATOR.createFromParcel(paramParcel));
  }
  
  public CellInfoCdma(CellInfoCdma paramCellInfoCdma)
  {
    super(paramCellInfoCdma);
    mCellIdentityCdma = mCellIdentityCdma.copy();
    mCellSignalStrengthCdma = mCellSignalStrengthCdma.copy();
  }
  
  protected static CellInfoCdma createFromParcelBody(Parcel paramParcel)
  {
    return new CellInfoCdma(paramParcel);
  }
  
  private static void log(String paramString)
  {
    Rlog.w("CellInfoCdma", paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = super.equals(paramObject);
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    try
    {
      paramObject = (CellInfoCdma)paramObject;
      bool1 = bool2;
      if (mCellIdentityCdma.equals(mCellIdentityCdma))
      {
        boolean bool3 = mCellSignalStrengthCdma.equals(mCellSignalStrengthCdma);
        bool1 = bool2;
        if (bool3) {
          bool1 = true;
        }
      }
      return bool1;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public CellIdentityCdma getCellIdentity()
  {
    return mCellIdentityCdma;
  }
  
  public CellSignalStrengthCdma getCellSignalStrength()
  {
    return mCellSignalStrengthCdma;
  }
  
  public int hashCode()
  {
    return super.hashCode() + mCellIdentityCdma.hashCode() + mCellSignalStrengthCdma.hashCode();
  }
  
  public void setCellIdentity(CellIdentityCdma paramCellIdentityCdma)
  {
    mCellIdentityCdma = paramCellIdentityCdma;
  }
  
  public void setCellSignalStrength(CellSignalStrengthCdma paramCellSignalStrengthCdma)
  {
    mCellSignalStrengthCdma = paramCellSignalStrengthCdma;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("CellInfoCdma:{");
    localStringBuffer.append(super.toString());
    localStringBuffer.append(" ");
    localStringBuffer.append(mCellIdentityCdma);
    localStringBuffer.append(" ");
    localStringBuffer.append(mCellSignalStrengthCdma);
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt, 2);
    mCellIdentityCdma.writeToParcel(paramParcel, paramInt);
    mCellSignalStrengthCdma.writeToParcel(paramParcel, paramInt);
  }
}
