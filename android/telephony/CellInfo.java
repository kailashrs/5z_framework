package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class CellInfo
  implements Parcelable
{
  public static final int CONNECTION_NONE = 0;
  public static final int CONNECTION_PRIMARY_SERVING = 1;
  public static final int CONNECTION_SECONDARY_SERVING = 2;
  public static final int CONNECTION_UNKNOWN = Integer.MAX_VALUE;
  public static final Parcelable.Creator<CellInfo> CREATOR = new Parcelable.Creator()
  {
    public CellInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      switch (paramAnonymousParcel.readInt())
      {
      default: 
        throw new RuntimeException("Bad CellInfo Parcel");
      case 4: 
        return CellInfoWcdma.createFromParcelBody(paramAnonymousParcel);
      case 3: 
        return CellInfoLte.createFromParcelBody(paramAnonymousParcel);
      case 2: 
        return CellInfoCdma.createFromParcelBody(paramAnonymousParcel);
      }
      return CellInfoGsm.createFromParcelBody(paramAnonymousParcel);
    }
    
    public CellInfo[] newArray(int paramAnonymousInt)
    {
      return new CellInfo[paramAnonymousInt];
    }
  };
  public static final int TIMESTAMP_TYPE_ANTENNA = 1;
  public static final int TIMESTAMP_TYPE_JAVA_RIL = 4;
  public static final int TIMESTAMP_TYPE_MODEM = 2;
  public static final int TIMESTAMP_TYPE_OEM_RIL = 3;
  public static final int TIMESTAMP_TYPE_UNKNOWN = 0;
  protected static final int TYPE_CDMA = 2;
  protected static final int TYPE_GSM = 1;
  protected static final int TYPE_LTE = 3;
  protected static final int TYPE_WCDMA = 4;
  private int mCellConnectionStatus;
  private boolean mRegistered;
  private long mTimeStamp;
  private int mTimeStampType;
  
  protected CellInfo()
  {
    mCellConnectionStatus = 0;
    mRegistered = false;
    mTimeStampType = 0;
    mTimeStamp = Long.MAX_VALUE;
  }
  
  protected CellInfo(Parcel paramParcel)
  {
    boolean bool = false;
    mCellConnectionStatus = 0;
    if (paramParcel.readInt() == 1) {
      bool = true;
    }
    mRegistered = bool;
    mTimeStampType = paramParcel.readInt();
    mTimeStamp = paramParcel.readLong();
    mCellConnectionStatus = paramParcel.readInt();
  }
  
  protected CellInfo(CellInfo paramCellInfo)
  {
    mCellConnectionStatus = 0;
    mRegistered = mRegistered;
    mTimeStampType = mTimeStampType;
    mTimeStamp = mTimeStamp;
    mCellConnectionStatus = mCellConnectionStatus;
  }
  
  private static String timeStampTypeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "unknown";
    case 4: 
      return "java_ril";
    case 3: 
      return "oem_ril";
    case 2: 
      return "modem";
    }
    return "antenna";
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    try
    {
      paramObject = (CellInfo)paramObject;
      boolean bool2 = bool1;
      if (mRegistered == mRegistered)
      {
        bool2 = bool1;
        if (mTimeStamp == mTimeStamp)
        {
          bool2 = bool1;
          if (mTimeStampType == mTimeStampType)
          {
            int i = mCellConnectionStatus;
            int j = mCellConnectionStatus;
            bool2 = bool1;
            if (i == j) {
              bool2 = true;
            }
          }
        }
      }
      return bool2;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public int getCellConnectionStatus()
  {
    return mCellConnectionStatus;
  }
  
  public long getTimeStamp()
  {
    return mTimeStamp;
  }
  
  public int getTimeStampType()
  {
    return mTimeStampType;
  }
  
  public int hashCode()
  {
    return (mRegistered ^ true) * true + (int)(mTimeStamp / 1000L) * 31 + mTimeStampType * 31 + mCellConnectionStatus * 31;
  }
  
  public boolean isRegistered()
  {
    return mRegistered;
  }
  
  public void setCellConnectionStatus(int paramInt)
  {
    mCellConnectionStatus = paramInt;
  }
  
  public void setRegistered(boolean paramBoolean)
  {
    mRegistered = paramBoolean;
  }
  
  public void setTimeStamp(long paramLong)
  {
    mTimeStamp = paramLong;
  }
  
  public void setTimeStampType(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 4)) {
      mTimeStampType = paramInt;
    } else {
      mTimeStampType = 0;
    }
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("mRegistered=");
    if (mRegistered) {
      str = "YES";
    } else {
      str = "NO";
    }
    localStringBuffer.append(str);
    String str = timeStampTypeToString(mTimeStampType);
    localStringBuffer.append(" mTimeStampType=");
    localStringBuffer.append(str);
    localStringBuffer.append(" mTimeStamp=");
    localStringBuffer.append(mTimeStamp);
    localStringBuffer.append("ns");
    localStringBuffer.append(" mCellConnectionStatus=");
    localStringBuffer.append(mCellConnectionStatus);
    return localStringBuffer.toString();
  }
  
  public abstract void writeToParcel(Parcel paramParcel, int paramInt);
  
  protected void writeToParcel(Parcel paramParcel, int paramInt1, int paramInt2)
  {
    paramParcel.writeInt(paramInt2);
    paramParcel.writeInt(mRegistered);
    paramParcel.writeInt(mTimeStampType);
    paramParcel.writeLong(mTimeStamp);
    paramParcel.writeInt(mCellConnectionStatus);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CellConnectionStatus {}
}
