package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class CellInfoGsm
  extends CellInfo
  implements Parcelable
{
  public static final Parcelable.Creator<CellInfoGsm> CREATOR = new Parcelable.Creator()
  {
    public CellInfoGsm createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel.readInt();
      return CellInfoGsm.createFromParcelBody(paramAnonymousParcel);
    }
    
    public CellInfoGsm[] newArray(int paramAnonymousInt)
    {
      return new CellInfoGsm[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String LOG_TAG = "CellInfoGsm";
  private CellIdentityGsm mCellIdentityGsm;
  private CellSignalStrengthGsm mCellSignalStrengthGsm;
  
  public CellInfoGsm()
  {
    mCellIdentityGsm = new CellIdentityGsm();
    mCellSignalStrengthGsm = new CellSignalStrengthGsm();
  }
  
  private CellInfoGsm(Parcel paramParcel)
  {
    super(paramParcel);
    mCellIdentityGsm = ((CellIdentityGsm)CellIdentityGsm.CREATOR.createFromParcel(paramParcel));
    mCellSignalStrengthGsm = ((CellSignalStrengthGsm)CellSignalStrengthGsm.CREATOR.createFromParcel(paramParcel));
  }
  
  public CellInfoGsm(CellInfoGsm paramCellInfoGsm)
  {
    super(paramCellInfoGsm);
    mCellIdentityGsm = mCellIdentityGsm.copy();
    mCellSignalStrengthGsm = mCellSignalStrengthGsm.copy();
  }
  
  protected static CellInfoGsm createFromParcelBody(Parcel paramParcel)
  {
    return new CellInfoGsm(paramParcel);
  }
  
  private static void log(String paramString)
  {
    Rlog.w("CellInfoGsm", paramString);
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
      paramObject = (CellInfoGsm)paramObject;
      bool1 = bool2;
      if (mCellIdentityGsm.equals(mCellIdentityGsm))
      {
        boolean bool3 = mCellSignalStrengthGsm.equals(mCellSignalStrengthGsm);
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
  
  public CellIdentityGsm getCellIdentity()
  {
    return mCellIdentityGsm;
  }
  
  public CellSignalStrengthGsm getCellSignalStrength()
  {
    return mCellSignalStrengthGsm;
  }
  
  public int hashCode()
  {
    return super.hashCode() + mCellIdentityGsm.hashCode() + mCellSignalStrengthGsm.hashCode();
  }
  
  public void setCellIdentity(CellIdentityGsm paramCellIdentityGsm)
  {
    mCellIdentityGsm = paramCellIdentityGsm;
  }
  
  public void setCellSignalStrength(CellSignalStrengthGsm paramCellSignalStrengthGsm)
  {
    mCellSignalStrengthGsm = paramCellSignalStrengthGsm;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("CellInfoGsm:{");
    localStringBuffer.append(super.toString());
    localStringBuffer.append(" ");
    localStringBuffer.append(mCellIdentityGsm);
    localStringBuffer.append(" ");
    localStringBuffer.append(mCellSignalStrengthGsm);
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt, 1);
    mCellIdentityGsm.writeToParcel(paramParcel, paramInt);
    mCellSignalStrengthGsm.writeToParcel(paramParcel, paramInt);
  }
}
