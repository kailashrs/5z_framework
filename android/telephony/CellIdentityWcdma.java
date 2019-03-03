package android.telephony;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.Objects;

public final class CellIdentityWcdma
  extends CellIdentity
{
  public static final Parcelable.Creator<CellIdentityWcdma> CREATOR = new Parcelable.Creator()
  {
    public CellIdentityWcdma createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel.readInt();
      return CellIdentityWcdma.createFromParcelBody(paramAnonymousParcel);
    }
    
    public CellIdentityWcdma[] newArray(int paramAnonymousInt)
    {
      return new CellIdentityWcdma[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String TAG = CellIdentityWcdma.class.getSimpleName();
  private final int mCid;
  private final int mLac;
  private final int mPsc;
  private final int mUarfcn;
  
  public CellIdentityWcdma()
  {
    super(TAG, 5, null, null, null, null);
    mLac = Integer.MAX_VALUE;
    mCid = Integer.MAX_VALUE;
    mPsc = Integer.MAX_VALUE;
    mUarfcn = Integer.MAX_VALUE;
  }
  
  public CellIdentityWcdma(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this(paramInt3, paramInt4, paramInt5, Integer.MAX_VALUE, String.valueOf(paramInt1), String.valueOf(paramInt2), null, null);
  }
  
  public CellIdentityWcdma(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    this(paramInt3, paramInt4, paramInt5, paramInt6, String.valueOf(paramInt1), String.valueOf(paramInt2), null, null);
  }
  
  public CellIdentityWcdma(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    super(TAG, 4, paramString1, paramString2, paramString3, paramString4);
    mLac = paramInt1;
    mCid = paramInt2;
    mPsc = paramInt3;
    mUarfcn = paramInt4;
  }
  
  private CellIdentityWcdma(Parcel paramParcel)
  {
    super(TAG, 4, paramParcel);
    mLac = paramParcel.readInt();
    mCid = paramParcel.readInt();
    mPsc = paramParcel.readInt();
    mUarfcn = paramParcel.readInt();
  }
  
  private CellIdentityWcdma(CellIdentityWcdma paramCellIdentityWcdma)
  {
    this(mLac, mCid, mPsc, mUarfcn, mMccStr, mMncStr, mAlphaLong, mAlphaShort);
  }
  
  protected static CellIdentityWcdma createFromParcelBody(Parcel paramParcel)
  {
    return new CellIdentityWcdma(paramParcel);
  }
  
  CellIdentityWcdma copy()
  {
    return new CellIdentityWcdma(this);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof CellIdentityWcdma)) {
      return false;
    }
    CellIdentityWcdma localCellIdentityWcdma = (CellIdentityWcdma)paramObject;
    if ((mLac != mLac) || (mCid != mCid) || (mPsc != mPsc) || (mUarfcn != mUarfcn) || (!TextUtils.equals(mMccStr, mMccStr)) || (!TextUtils.equals(mMncStr, mMncStr)) || (!super.equals(paramObject))) {
      bool = false;
    }
    return bool;
  }
  
  public int getChannelNumber()
  {
    return mUarfcn;
  }
  
  public int getCid()
  {
    return mCid;
  }
  
  public int getLac()
  {
    return mLac;
  }
  
  @Deprecated
  public int getMcc()
  {
    int i;
    if (mMccStr != null) {
      i = Integer.valueOf(mMccStr).intValue();
    } else {
      i = Integer.MAX_VALUE;
    }
    return i;
  }
  
  public String getMccString()
  {
    return mMccStr;
  }
  
  @Deprecated
  public int getMnc()
  {
    int i;
    if (mMncStr != null) {
      i = Integer.valueOf(mMncStr).intValue();
    } else {
      i = Integer.MAX_VALUE;
    }
    return i;
  }
  
  public String getMncString()
  {
    return mMncStr;
  }
  
  public String getMobileNetworkOperator()
  {
    Object localObject;
    if ((mMccStr != null) && (mMncStr != null))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(mMccStr);
      ((StringBuilder)localObject).append(mMncStr);
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = null;
    }
    return localObject;
  }
  
  public int getPsc()
  {
    return mPsc;
  }
  
  public int getUarfcn()
  {
    return mUarfcn;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mLac), Integer.valueOf(mCid), Integer.valueOf(mPsc), Integer.valueOf(super.hashCode()) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(TAG);
    localStringBuilder.append(":{ mLac=");
    localStringBuilder.append(mLac);
    localStringBuilder.append(" mCid=");
    localStringBuilder.append(mCid);
    localStringBuilder.append(" mPsc=");
    localStringBuilder.append(mPsc);
    localStringBuilder.append(" mUarfcn=");
    localStringBuilder.append(mUarfcn);
    localStringBuilder.append(" mMcc=");
    localStringBuilder.append(mMccStr);
    localStringBuilder.append(" mMnc=");
    localStringBuilder.append(mMncStr);
    localStringBuilder.append(" mAlphaLong=");
    localStringBuilder.append(mAlphaLong);
    localStringBuilder.append(" mAlphaShort=");
    localStringBuilder.append(mAlphaShort);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, 4);
    paramParcel.writeInt(mLac);
    paramParcel.writeInt(mCid);
    paramParcel.writeInt(mPsc);
    paramParcel.writeInt(mUarfcn);
  }
}
