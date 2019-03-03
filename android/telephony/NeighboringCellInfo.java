package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class NeighboringCellInfo
  implements Parcelable
{
  public static final Parcelable.Creator<NeighboringCellInfo> CREATOR = new Parcelable.Creator()
  {
    public NeighboringCellInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NeighboringCellInfo(paramAnonymousParcel);
    }
    
    public NeighboringCellInfo[] newArray(int paramAnonymousInt)
    {
      return new NeighboringCellInfo[paramAnonymousInt];
    }
  };
  public static final int UNKNOWN_CID = -1;
  public static final int UNKNOWN_RSSI = 99;
  private int mCid;
  private int mLac;
  private int mNetworkType;
  private int mPsc;
  private int mRssi;
  
  @Deprecated
  public NeighboringCellInfo()
  {
    mRssi = 99;
    mLac = -1;
    mCid = -1;
    mPsc = -1;
    mNetworkType = 0;
  }
  
  @Deprecated
  public NeighboringCellInfo(int paramInt1, int paramInt2)
  {
    mRssi = paramInt1;
    mCid = paramInt2;
  }
  
  public NeighboringCellInfo(int paramInt1, String paramString, int paramInt2)
  {
    mRssi = paramInt1;
    mNetworkType = 0;
    mPsc = -1;
    mLac = -1;
    mCid = -1;
    int i = paramString.length();
    if (i > 8) {
      return;
    }
    Object localObject = paramString;
    if (i < 8)
    {
      for (paramInt1 = 0; paramInt1 < 8 - i; paramInt1++)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("0");
        ((StringBuilder)localObject).append(paramString);
        paramString = ((StringBuilder)localObject).toString();
      }
      localObject = paramString;
    }
    switch (paramInt2)
    {
    default: 
      switch (paramInt2)
      {
      }
      break;
    }
    try
    {
      mNetworkType = paramInt2;
      mPsc = Integer.parseInt((String)localObject, 16);
      return;
      mNetworkType = paramInt2;
      if (!((String)localObject).equalsIgnoreCase("FFFFFFFF"))
      {
        mCid = Integer.parseInt(((String)localObject).substring(4), 16);
        mLac = Integer.parseInt(((String)localObject).substring(0, 4), 16);
      }
    }
    catch (NumberFormatException paramString)
    {
      mPsc = -1;
      mLac = -1;
      mCid = -1;
      mNetworkType = 0;
    }
  }
  
  public NeighboringCellInfo(Parcel paramParcel)
  {
    mRssi = paramParcel.readInt();
    mLac = paramParcel.readInt();
    mCid = paramParcel.readInt();
    mPsc = paramParcel.readInt();
    mNetworkType = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getCid()
  {
    return mCid;
  }
  
  public int getLac()
  {
    return mLac;
  }
  
  public int getNetworkType()
  {
    return mNetworkType;
  }
  
  public int getPsc()
  {
    return mPsc;
  }
  
  public int getRssi()
  {
    return mRssi;
  }
  
  @Deprecated
  public void setCid(int paramInt)
  {
    mCid = paramInt;
  }
  
  @Deprecated
  public void setRssi(int paramInt)
  {
    mRssi = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    Object localObject;
    if (mPsc != -1)
    {
      localStringBuilder.append(Integer.toHexString(mPsc));
      localStringBuilder.append("@");
      if (mRssi == 99) {
        localObject = "-";
      } else {
        localObject = Integer.valueOf(mRssi);
      }
      localStringBuilder.append(localObject);
    }
    else if ((mLac != -1) && (mCid != -1))
    {
      localStringBuilder.append(Integer.toHexString(mLac));
      localStringBuilder.append(Integer.toHexString(mCid));
      localStringBuilder.append("@");
      if (mRssi == 99) {
        localObject = "-";
      } else {
        localObject = Integer.valueOf(mRssi);
      }
      localStringBuilder.append(localObject);
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRssi);
    paramParcel.writeInt(mLac);
    paramParcel.writeInt(mCid);
    paramParcel.writeInt(mPsc);
    paramParcel.writeInt(mNetworkType);
  }
}
