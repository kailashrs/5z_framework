package android.telephony;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class CellIdentityCdma
  extends CellIdentity
{
  public static final Parcelable.Creator<CellIdentityCdma> CREATOR = new Parcelable.Creator()
  {
    public CellIdentityCdma createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel.readInt();
      return CellIdentityCdma.createFromParcelBody(paramAnonymousParcel);
    }
    
    public CellIdentityCdma[] newArray(int paramAnonymousInt)
    {
      return new CellIdentityCdma[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  private static final String TAG = CellIdentityCdma.class.getSimpleName();
  private final int mBasestationId;
  private final int mLatitude;
  private final int mLongitude;
  private final int mNetworkId;
  private final int mSystemId;
  
  public CellIdentityCdma()
  {
    super(TAG, 2, null, null, null, null);
    mNetworkId = Integer.MAX_VALUE;
    mSystemId = Integer.MAX_VALUE;
    mBasestationId = Integer.MAX_VALUE;
    mLongitude = Integer.MAX_VALUE;
    mLatitude = Integer.MAX_VALUE;
  }
  
  public CellIdentityCdma(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, null, null);
  }
  
  public CellIdentityCdma(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString1, String paramString2)
  {
    super(TAG, 2, null, null, paramString1, paramString2);
    mNetworkId = paramInt1;
    mSystemId = paramInt2;
    mBasestationId = paramInt3;
    if (!isNullIsland(paramInt5, paramInt4))
    {
      mLongitude = paramInt4;
      mLatitude = paramInt5;
    }
    else
    {
      mLatitude = Integer.MAX_VALUE;
      mLongitude = Integer.MAX_VALUE;
    }
  }
  
  private CellIdentityCdma(Parcel paramParcel)
  {
    super(TAG, 2, paramParcel);
    mNetworkId = paramParcel.readInt();
    mSystemId = paramParcel.readInt();
    mBasestationId = paramParcel.readInt();
    mLongitude = paramParcel.readInt();
    mLatitude = paramParcel.readInt();
  }
  
  private CellIdentityCdma(CellIdentityCdma paramCellIdentityCdma)
  {
    this(mNetworkId, mSystemId, mBasestationId, mLongitude, mLatitude, mAlphaLong, mAlphaShort);
  }
  
  protected static CellIdentityCdma createFromParcelBody(Parcel paramParcel)
  {
    return new CellIdentityCdma(paramParcel);
  }
  
  private boolean isNullIsland(int paramInt1, int paramInt2)
  {
    paramInt1 = Math.abs(paramInt1);
    boolean bool = true;
    if ((paramInt1 > 1) || (Math.abs(paramInt2) > 1)) {
      bool = false;
    }
    return bool;
  }
  
  CellIdentityCdma copy()
  {
    return new CellIdentityCdma(this);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof CellIdentityCdma)) {
      return false;
    }
    CellIdentityCdma localCellIdentityCdma = (CellIdentityCdma)paramObject;
    if ((mNetworkId != mNetworkId) || (mSystemId != mSystemId) || (mBasestationId != mBasestationId) || (mLatitude != mLatitude) || (mLongitude != mLongitude) || (!super.equals(paramObject))) {
      bool = false;
    }
    return bool;
  }
  
  public int getBasestationId()
  {
    return mBasestationId;
  }
  
  public int getLatitude()
  {
    return mLatitude;
  }
  
  public int getLongitude()
  {
    return mLongitude;
  }
  
  public int getNetworkId()
  {
    return mNetworkId;
  }
  
  public int getSystemId()
  {
    return mSystemId;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mNetworkId), Integer.valueOf(mSystemId), Integer.valueOf(mBasestationId), Integer.valueOf(mLatitude), Integer.valueOf(mLongitude), Integer.valueOf(super.hashCode()) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(TAG);
    localStringBuilder.append(":{ mNetworkId=");
    localStringBuilder.append(mNetworkId);
    localStringBuilder.append(" mSystemId=");
    localStringBuilder.append(mSystemId);
    localStringBuilder.append(" mBasestationId=");
    localStringBuilder.append(mBasestationId);
    localStringBuilder.append(" mLongitude=");
    localStringBuilder.append(mLongitude);
    localStringBuilder.append(" mLatitude=");
    localStringBuilder.append(mLatitude);
    localStringBuilder.append(" mAlphaLong=");
    localStringBuilder.append(mAlphaLong);
    localStringBuilder.append(" mAlphaShort=");
    localStringBuilder.append(mAlphaShort);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, 2);
    paramParcel.writeInt(mNetworkId);
    paramParcel.writeInt(mSystemId);
    paramParcel.writeInt(mBasestationId);
    paramParcel.writeInt(mLongitude);
    paramParcel.writeInt(mLatitude);
  }
}
