package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SmsCbLocation
  implements Parcelable
{
  public static final Parcelable.Creator<SmsCbLocation> CREATOR = new Parcelable.Creator()
  {
    public SmsCbLocation createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SmsCbLocation(paramAnonymousParcel);
    }
    
    public SmsCbLocation[] newArray(int paramAnonymousInt)
    {
      return new SmsCbLocation[paramAnonymousInt];
    }
  };
  private final int mCid;
  private final int mLac;
  private final String mPlmn;
  
  public SmsCbLocation()
  {
    mPlmn = "";
    mLac = -1;
    mCid = -1;
  }
  
  public SmsCbLocation(Parcel paramParcel)
  {
    mPlmn = paramParcel.readString();
    mLac = paramParcel.readInt();
    mCid = paramParcel.readInt();
  }
  
  public SmsCbLocation(String paramString)
  {
    mPlmn = paramString;
    mLac = -1;
    mCid = -1;
  }
  
  public SmsCbLocation(String paramString, int paramInt1, int paramInt2)
  {
    mPlmn = paramString;
    mLac = paramInt1;
    mCid = paramInt2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    if ((paramObject != null) && ((paramObject instanceof SmsCbLocation)))
    {
      paramObject = (SmsCbLocation)paramObject;
      if ((!mPlmn.equals(mPlmn)) || (mLac != mLac) || (mCid != mCid)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int getCid()
  {
    return mCid;
  }
  
  public int getLac()
  {
    return mLac;
  }
  
  public String getPlmn()
  {
    return mPlmn;
  }
  
  public int hashCode()
  {
    return (mPlmn.hashCode() * 31 + mLac) * 31 + mCid;
  }
  
  public boolean isInLocationArea(SmsCbLocation paramSmsCbLocation)
  {
    if ((mCid != -1) && (mCid != mCid)) {
      return false;
    }
    if ((mLac != -1) && (mLac != mLac)) {
      return false;
    }
    return mPlmn.equals(mPlmn);
  }
  
  public boolean isInLocationArea(String paramString, int paramInt1, int paramInt2)
  {
    if (!mPlmn.equals(paramString)) {
      return false;
    }
    if ((mLac != -1) && (mLac != paramInt1)) {
      return false;
    }
    return (mCid == -1) || (mCid == paramInt2);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('[');
    localStringBuilder.append(mPlmn);
    localStringBuilder.append(',');
    localStringBuilder.append(mLac);
    localStringBuilder.append(',');
    localStringBuilder.append(mCid);
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPlmn);
    paramParcel.writeInt(mLac);
    paramParcel.writeInt(mCid);
  }
}
