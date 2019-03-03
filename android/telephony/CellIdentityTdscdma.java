package android.telephony;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.Objects;

public final class CellIdentityTdscdma
  extends CellIdentity
{
  public static final Parcelable.Creator<CellIdentityTdscdma> CREATOR = new Parcelable.Creator()
  {
    public CellIdentityTdscdma createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel.readInt();
      return CellIdentityTdscdma.createFromParcelBody(paramAnonymousParcel);
    }
    
    public CellIdentityTdscdma[] newArray(int paramAnonymousInt)
    {
      return new CellIdentityTdscdma[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String TAG = CellIdentityTdscdma.class.getSimpleName();
  private final int mCid;
  private final int mCpid;
  private final int mLac;
  
  public CellIdentityTdscdma()
  {
    super(TAG, 5, null, null, null, null);
    mLac = Integer.MAX_VALUE;
    mCid = Integer.MAX_VALUE;
    mCpid = Integer.MAX_VALUE;
  }
  
  public CellIdentityTdscdma(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this(String.valueOf(paramInt1), String.valueOf(paramInt2), paramInt3, paramInt4, paramInt5, null, null);
  }
  
  private CellIdentityTdscdma(Parcel paramParcel)
  {
    super(TAG, 5, paramParcel);
    mLac = paramParcel.readInt();
    mCid = paramParcel.readInt();
    mCpid = paramParcel.readInt();
  }
  
  private CellIdentityTdscdma(CellIdentityTdscdma paramCellIdentityTdscdma)
  {
    this(mMccStr, mMncStr, mLac, mCid, mCpid, mAlphaLong, mAlphaShort);
  }
  
  public CellIdentityTdscdma(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
  {
    super(TAG, 5, paramString1, paramString2, null, null);
    mLac = paramInt1;
    mCid = paramInt2;
    mCpid = paramInt3;
  }
  
  public CellIdentityTdscdma(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3, String paramString4)
  {
    super(TAG, 5, paramString1, paramString2, paramString3, paramString4);
    mLac = paramInt1;
    mCid = paramInt2;
    mCpid = paramInt3;
  }
  
  protected static CellIdentityTdscdma createFromParcelBody(Parcel paramParcel)
  {
    return new CellIdentityTdscdma(paramParcel);
  }
  
  CellIdentityTdscdma copy()
  {
    return new CellIdentityTdscdma(this);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof CellIdentityTdscdma)) {
      return false;
    }
    CellIdentityTdscdma localCellIdentityTdscdma = (CellIdentityTdscdma)paramObject;
    if ((!TextUtils.equals(mMccStr, mMccStr)) || (!TextUtils.equals(mMncStr, mMncStr)) || (mLac != mLac) || (mCid != mCid) || (mCpid != mCpid) || (!super.equals(paramObject))) {
      bool = false;
    }
    return bool;
  }
  
  public int getCid()
  {
    return mCid;
  }
  
  public int getCpid()
  {
    return mCpid;
  }
  
  public int getLac()
  {
    return mLac;
  }
  
  public String getMccString()
  {
    return mMccStr;
  }
  
  public String getMncString()
  {
    return mMncStr;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mLac), Integer.valueOf(mCid), Integer.valueOf(mCpid), Integer.valueOf(super.hashCode()) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(TAG);
    localStringBuilder.append(":{ mMcc=");
    localStringBuilder.append(mMccStr);
    localStringBuilder.append(" mMnc=");
    localStringBuilder.append(mMncStr);
    localStringBuilder.append(" mLac=");
    localStringBuilder.append(mLac);
    localStringBuilder.append(" mCid=");
    localStringBuilder.append(mCid);
    localStringBuilder.append(" mCpid=");
    localStringBuilder.append(mCpid);
    localStringBuilder.append(" mAlphaLong=");
    localStringBuilder.append(mAlphaLong);
    localStringBuilder.append(" mAlphaShort=");
    localStringBuilder.append(mAlphaShort);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, 5);
    paramParcel.writeInt(mLac);
    paramParcel.writeInt(mCid);
    paramParcel.writeInt(mCpid);
  }
}
