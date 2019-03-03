package android.telephony;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.Objects;

public final class CellIdentityGsm
  extends CellIdentity
{
  public static final Parcelable.Creator<CellIdentityGsm> CREATOR = new Parcelable.Creator()
  {
    public CellIdentityGsm createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel.readInt();
      return CellIdentityGsm.createFromParcelBody(paramAnonymousParcel);
    }
    
    public CellIdentityGsm[] newArray(int paramAnonymousInt)
    {
      return new CellIdentityGsm[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String TAG = CellIdentityGsm.class.getSimpleName();
  private final int mArfcn;
  private final int mBsic;
  private final int mCid;
  private final int mLac;
  
  public CellIdentityGsm()
  {
    super(TAG, 1, null, null, null, null);
    mLac = Integer.MAX_VALUE;
    mCid = Integer.MAX_VALUE;
    mArfcn = Integer.MAX_VALUE;
    mBsic = Integer.MAX_VALUE;
  }
  
  public CellIdentityGsm(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this(paramInt3, paramInt4, Integer.MAX_VALUE, Integer.MAX_VALUE, String.valueOf(paramInt1), String.valueOf(paramInt2), null, null);
  }
  
  public CellIdentityGsm(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    this(paramInt3, paramInt4, paramInt5, paramInt6, String.valueOf(paramInt1), String.valueOf(paramInt2), null, null);
  }
  
  public CellIdentityGsm(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    super(TAG, 1, paramString1, paramString2, paramString3, paramString4);
    mLac = paramInt1;
    mCid = paramInt2;
    mArfcn = paramInt3;
    if (paramInt4 == 255) {
      paramInt4 = Integer.MAX_VALUE;
    }
    mBsic = paramInt4;
  }
  
  private CellIdentityGsm(Parcel paramParcel)
  {
    super(TAG, 1, paramParcel);
    mLac = paramParcel.readInt();
    mCid = paramParcel.readInt();
    mArfcn = paramParcel.readInt();
    mBsic = paramParcel.readInt();
  }
  
  private CellIdentityGsm(CellIdentityGsm paramCellIdentityGsm)
  {
    this(mLac, mCid, mArfcn, mBsic, mMccStr, mMncStr, mAlphaLong, mAlphaShort);
  }
  
  protected static CellIdentityGsm createFromParcelBody(Parcel paramParcel)
  {
    return new CellIdentityGsm(paramParcel);
  }
  
  CellIdentityGsm copy()
  {
    return new CellIdentityGsm(this);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof CellIdentityGsm)) {
      return false;
    }
    CellIdentityGsm localCellIdentityGsm = (CellIdentityGsm)paramObject;
    if ((mLac != mLac) || (mCid != mCid) || (mArfcn != mArfcn) || (mBsic != mBsic) || (!TextUtils.equals(mMccStr, mMccStr)) || (!TextUtils.equals(mMncStr, mMncStr)) || (!super.equals(paramObject))) {
      bool = false;
    }
    return bool;
  }
  
  public int getArfcn()
  {
    return mArfcn;
  }
  
  public int getBsic()
  {
    return mBsic;
  }
  
  public int getChannelNumber()
  {
    return mArfcn;
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
  
  @Deprecated
  public int getPsc()
  {
    return Integer.MAX_VALUE;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mLac), Integer.valueOf(mCid), Integer.valueOf(super.hashCode()) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(TAG);
    localStringBuilder.append(":{ mLac=");
    localStringBuilder.append(mLac);
    localStringBuilder.append(" mCid=");
    localStringBuilder.append(mCid);
    localStringBuilder.append(" mArfcn=");
    localStringBuilder.append(mArfcn);
    localStringBuilder.append(" mBsic=");
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(mBsic));
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
    super.writeToParcel(paramParcel, 1);
    paramParcel.writeInt(mLac);
    paramParcel.writeInt(mCid);
    paramParcel.writeInt(mArfcn);
    paramParcel.writeInt(mBsic);
  }
}
