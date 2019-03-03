package android.telephony;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.Objects;

public final class CellIdentityLte
  extends CellIdentity
{
  public static final Parcelable.Creator<CellIdentityLte> CREATOR = new Parcelable.Creator()
  {
    public CellIdentityLte createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel.readInt();
      return CellIdentityLte.createFromParcelBody(paramAnonymousParcel);
    }
    
    public CellIdentityLte[] newArray(int paramAnonymousInt)
    {
      return new CellIdentityLte[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String TAG = CellIdentityLte.class.getSimpleName();
  private final int mBandwidth;
  private final int mCi;
  private final int mEarfcn;
  private final int mPci;
  private final int mTac;
  
  public CellIdentityLte()
  {
    super(TAG, 3, null, null, null, null);
    mCi = Integer.MAX_VALUE;
    mPci = Integer.MAX_VALUE;
    mTac = Integer.MAX_VALUE;
    mEarfcn = Integer.MAX_VALUE;
    mBandwidth = Integer.MAX_VALUE;
  }
  
  public CellIdentityLte(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this(paramInt3, paramInt4, paramInt5, Integer.MAX_VALUE, Integer.MAX_VALUE, String.valueOf(paramInt1), String.valueOf(paramInt2), null, null);
  }
  
  public CellIdentityLte(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    this(paramInt3, paramInt4, paramInt5, paramInt6, Integer.MAX_VALUE, String.valueOf(paramInt1), String.valueOf(paramInt2), null, null);
  }
  
  public CellIdentityLte(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    super(TAG, 3, paramString1, paramString2, paramString3, paramString4);
    mCi = paramInt1;
    mPci = paramInt2;
    mTac = paramInt3;
    mEarfcn = paramInt4;
    mBandwidth = paramInt5;
  }
  
  private CellIdentityLte(Parcel paramParcel)
  {
    super(TAG, 3, paramParcel);
    mCi = paramParcel.readInt();
    mPci = paramParcel.readInt();
    mTac = paramParcel.readInt();
    mEarfcn = paramParcel.readInt();
    mBandwidth = paramParcel.readInt();
  }
  
  private CellIdentityLte(CellIdentityLte paramCellIdentityLte)
  {
    this(mCi, mPci, mTac, mEarfcn, mBandwidth, mMccStr, mMncStr, mAlphaLong, mAlphaShort);
  }
  
  protected static CellIdentityLte createFromParcelBody(Parcel paramParcel)
  {
    return new CellIdentityLte(paramParcel);
  }
  
  CellIdentityLte copy()
  {
    return new CellIdentityLte(this);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof CellIdentityLte)) {
      return false;
    }
    CellIdentityLte localCellIdentityLte = (CellIdentityLte)paramObject;
    if ((mCi != mCi) || (mPci != mPci) || (mTac != mTac) || (mEarfcn != mEarfcn) || (mBandwidth != mBandwidth) || (!TextUtils.equals(mMccStr, mMccStr)) || (!TextUtils.equals(mMncStr, mMncStr)) || (!super.equals(paramObject))) {
      bool = false;
    }
    return bool;
  }
  
  public int getBandwidth()
  {
    return mBandwidth;
  }
  
  public int getChannelNumber()
  {
    return mEarfcn;
  }
  
  public int getCi()
  {
    return mCi;
  }
  
  public int getEarfcn()
  {
    return mEarfcn;
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
  
  public int getPci()
  {
    return mPci;
  }
  
  public int getTac()
  {
    return mTac;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mCi), Integer.valueOf(mPci), Integer.valueOf(mTac), Integer.valueOf(super.hashCode()) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(TAG);
    localStringBuilder.append(":{ mCi=");
    localStringBuilder.append(mCi);
    localStringBuilder.append(" mPci=");
    localStringBuilder.append(mPci);
    localStringBuilder.append(" mTac=");
    localStringBuilder.append(mTac);
    localStringBuilder.append(" mEarfcn=");
    localStringBuilder.append(mEarfcn);
    localStringBuilder.append(" mBandwidth=");
    localStringBuilder.append(mBandwidth);
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
    super.writeToParcel(paramParcel, 3);
    paramParcel.writeInt(mCi);
    paramParcel.writeInt(mPci);
    paramParcel.writeInt(mTac);
    paramParcel.writeInt(mEarfcn);
    paramParcel.writeInt(mBandwidth);
  }
}
