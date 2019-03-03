package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

public abstract class CellIdentity
  implements Parcelable
{
  public static final Parcelable.Creator<CellIdentity> CREATOR = new Parcelable.Creator()
  {
    public CellIdentity createFromParcel(Parcel paramAnonymousParcel)
    {
      switch (paramAnonymousParcel.readInt())
      {
      default: 
        throw new IllegalArgumentException("Bad Cell identity Parcel");
      case 5: 
        return CellIdentityTdscdma.createFromParcelBody(paramAnonymousParcel);
      case 4: 
        return CellIdentityWcdma.createFromParcelBody(paramAnonymousParcel);
      case 3: 
        return CellIdentityLte.createFromParcelBody(paramAnonymousParcel);
      case 2: 
        return CellIdentityCdma.createFromParcelBody(paramAnonymousParcel);
      }
      return CellIdentityGsm.createFromParcelBody(paramAnonymousParcel);
    }
    
    public CellIdentity[] newArray(int paramAnonymousInt)
    {
      return new CellIdentity[paramAnonymousInt];
    }
  };
  public static final int INVALID_CHANNEL_NUMBER = -1;
  public static final int TYPE_CDMA = 2;
  public static final int TYPE_GSM = 1;
  public static final int TYPE_LTE = 3;
  public static final int TYPE_TDSCDMA = 5;
  public static final int TYPE_UNKNOWN = 0;
  public static final int TYPE_WCDMA = 4;
  protected final String mAlphaLong;
  protected final String mAlphaShort;
  protected final String mMccStr;
  protected final String mMncStr;
  protected final String mTag;
  protected final int mType;
  
  protected CellIdentity(String paramString, int paramInt, Parcel paramParcel)
  {
    this(paramString, paramInt, paramParcel.readString(), paramParcel.readString(), paramParcel.readString(), paramParcel.readString());
  }
  
  protected CellIdentity(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    mTag = paramString1;
    mType = paramInt;
    if ((paramString2 != null) && (!paramString2.matches("^[0-9]{3}$")))
    {
      if ((!paramString2.isEmpty()) && (!paramString2.equals(String.valueOf(Integer.MAX_VALUE))))
      {
        mMccStr = null;
        paramString1 = new StringBuilder();
        paramString1.append("invalid MCC format: ");
        paramString1.append(paramString2);
        log(paramString1.toString());
      }
      else
      {
        mMccStr = null;
      }
    }
    else {
      mMccStr = paramString2;
    }
    if ((paramString3 != null) && (!paramString3.matches("^[0-9]{2,3}$")))
    {
      if ((!paramString3.isEmpty()) && (!paramString3.equals(String.valueOf(Integer.MAX_VALUE))))
      {
        mMncStr = null;
        paramString1 = new StringBuilder();
        paramString1.append("invalid MNC format: ");
        paramString1.append(paramString3);
        log(paramString1.toString());
      }
      else
      {
        mMncStr = null;
      }
    }
    else {
      mMncStr = paramString3;
    }
    mAlphaLong = paramString4;
    mAlphaShort = paramString5;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof CellIdentity;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (CellIdentity)paramObject;
    bool1 = bool2;
    if (TextUtils.equals(mAlphaLong, mAlphaLong))
    {
      bool1 = bool2;
      if (TextUtils.equals(mAlphaShort, mAlphaShort)) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  public int getChannelNumber()
  {
    return -1;
  }
  
  public CharSequence getOperatorAlphaLong()
  {
    return mAlphaLong;
  }
  
  public CharSequence getOperatorAlphaShort()
  {
    return mAlphaShort;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mAlphaLong, mAlphaShort, mMccStr, mMncStr, Integer.valueOf(mType) });
  }
  
  protected void log(String paramString)
  {
    Rlog.w(mTag, paramString);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(paramInt);
    paramParcel.writeString(mMccStr);
    paramParcel.writeString(mMncStr);
    paramParcel.writeString(mAlphaLong);
    paramParcel.writeString(mAlphaShort);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Type {}
}
