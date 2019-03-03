package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class CellInfoWcdma
  extends CellInfo
  implements Parcelable
{
  public static final Parcelable.Creator<CellInfoWcdma> CREATOR = new Parcelable.Creator()
  {
    public CellInfoWcdma createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel.readInt();
      return CellInfoWcdma.createFromParcelBody(paramAnonymousParcel);
    }
    
    public CellInfoWcdma[] newArray(int paramAnonymousInt)
    {
      return new CellInfoWcdma[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String LOG_TAG = "CellInfoWcdma";
  private CellIdentityWcdma mCellIdentityWcdma;
  private CellSignalStrengthWcdma mCellSignalStrengthWcdma;
  
  public CellInfoWcdma()
  {
    mCellIdentityWcdma = new CellIdentityWcdma();
    mCellSignalStrengthWcdma = new CellSignalStrengthWcdma();
  }
  
  private CellInfoWcdma(Parcel paramParcel)
  {
    super(paramParcel);
    mCellIdentityWcdma = ((CellIdentityWcdma)CellIdentityWcdma.CREATOR.createFromParcel(paramParcel));
    mCellSignalStrengthWcdma = ((CellSignalStrengthWcdma)CellSignalStrengthWcdma.CREATOR.createFromParcel(paramParcel));
  }
  
  public CellInfoWcdma(CellInfoWcdma paramCellInfoWcdma)
  {
    super(paramCellInfoWcdma);
    mCellIdentityWcdma = mCellIdentityWcdma.copy();
    mCellSignalStrengthWcdma = mCellSignalStrengthWcdma.copy();
  }
  
  protected static CellInfoWcdma createFromParcelBody(Parcel paramParcel)
  {
    return new CellInfoWcdma(paramParcel);
  }
  
  private static void log(String paramString)
  {
    Rlog.w("CellInfoWcdma", paramString);
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
      paramObject = (CellInfoWcdma)paramObject;
      bool1 = bool2;
      if (mCellIdentityWcdma.equals(mCellIdentityWcdma))
      {
        boolean bool3 = mCellSignalStrengthWcdma.equals(mCellSignalStrengthWcdma);
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
  
  public CellIdentityWcdma getCellIdentity()
  {
    return mCellIdentityWcdma;
  }
  
  public CellSignalStrengthWcdma getCellSignalStrength()
  {
    return mCellSignalStrengthWcdma;
  }
  
  public int hashCode()
  {
    return super.hashCode() + mCellIdentityWcdma.hashCode() + mCellSignalStrengthWcdma.hashCode();
  }
  
  public void setCellIdentity(CellIdentityWcdma paramCellIdentityWcdma)
  {
    mCellIdentityWcdma = paramCellIdentityWcdma;
  }
  
  public void setCellSignalStrength(CellSignalStrengthWcdma paramCellSignalStrengthWcdma)
  {
    mCellSignalStrengthWcdma = paramCellSignalStrengthWcdma;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("CellInfoWcdma:{");
    localStringBuffer.append(super.toString());
    localStringBuffer.append(" ");
    localStringBuffer.append(mCellIdentityWcdma);
    localStringBuffer.append(" ");
    localStringBuffer.append(mCellSignalStrengthWcdma);
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt, 4);
    mCellIdentityWcdma.writeToParcel(paramParcel, paramInt);
    mCellSignalStrengthWcdma.writeToParcel(paramParcel, paramInt);
  }
}
