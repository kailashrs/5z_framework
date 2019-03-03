package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class CellInfoLte
  extends CellInfo
  implements Parcelable
{
  public static final Parcelable.Creator<CellInfoLte> CREATOR = new Parcelable.Creator()
  {
    public CellInfoLte createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel.readInt();
      return CellInfoLte.createFromParcelBody(paramAnonymousParcel);
    }
    
    public CellInfoLte[] newArray(int paramAnonymousInt)
    {
      return new CellInfoLte[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String LOG_TAG = "CellInfoLte";
  private CellIdentityLte mCellIdentityLte;
  private CellSignalStrengthLte mCellSignalStrengthLte;
  
  public CellInfoLte()
  {
    mCellIdentityLte = new CellIdentityLte();
    mCellSignalStrengthLte = new CellSignalStrengthLte();
  }
  
  private CellInfoLte(Parcel paramParcel)
  {
    super(paramParcel);
    mCellIdentityLte = ((CellIdentityLte)CellIdentityLte.CREATOR.createFromParcel(paramParcel));
    mCellSignalStrengthLte = ((CellSignalStrengthLte)CellSignalStrengthLte.CREATOR.createFromParcel(paramParcel));
  }
  
  public CellInfoLte(CellInfoLte paramCellInfoLte)
  {
    super(paramCellInfoLte);
    mCellIdentityLte = mCellIdentityLte.copy();
    mCellSignalStrengthLte = mCellSignalStrengthLte.copy();
  }
  
  protected static CellInfoLte createFromParcelBody(Parcel paramParcel)
  {
    return new CellInfoLte(paramParcel);
  }
  
  private static void log(String paramString)
  {
    Rlog.w("CellInfoLte", paramString);
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
      paramObject = (CellInfoLte)paramObject;
      bool1 = bool2;
      if (mCellIdentityLte.equals(mCellIdentityLte))
      {
        boolean bool3 = mCellSignalStrengthLte.equals(mCellSignalStrengthLte);
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
  
  public CellIdentityLte getCellIdentity()
  {
    return mCellIdentityLte;
  }
  
  public CellSignalStrengthLte getCellSignalStrength()
  {
    return mCellSignalStrengthLte;
  }
  
  public int hashCode()
  {
    return super.hashCode() + mCellIdentityLte.hashCode() + mCellSignalStrengthLte.hashCode();
  }
  
  public void setCellIdentity(CellIdentityLte paramCellIdentityLte)
  {
    mCellIdentityLte = paramCellIdentityLte;
  }
  
  public void setCellSignalStrength(CellSignalStrengthLte paramCellSignalStrengthLte)
  {
    mCellSignalStrengthLte = paramCellSignalStrengthLte;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("CellInfoLte:{");
    localStringBuffer.append(super.toString());
    localStringBuffer.append(" ");
    localStringBuffer.append(mCellIdentityLte);
    localStringBuffer.append(" ");
    localStringBuffer.append(mCellSignalStrengthLte);
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt, 3);
    mCellIdentityLte.writeToParcel(paramParcel, paramInt);
    mCellSignalStrengthLte.writeToParcel(paramParcel, paramInt);
  }
}
