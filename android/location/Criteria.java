package android.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Criteria
  implements Parcelable
{
  public static final int ACCURACY_COARSE = 2;
  public static final int ACCURACY_FINE = 1;
  public static final int ACCURACY_HIGH = 3;
  public static final int ACCURACY_LOW = 1;
  public static final int ACCURACY_MEDIUM = 2;
  public static final Parcelable.Creator<Criteria> CREATOR = new Parcelable.Creator()
  {
    public Criteria createFromParcel(Parcel paramAnonymousParcel)
    {
      Criteria localCriteria = new Criteria();
      Criteria.access$002(localCriteria, paramAnonymousParcel.readInt());
      Criteria.access$102(localCriteria, paramAnonymousParcel.readInt());
      Criteria.access$202(localCriteria, paramAnonymousParcel.readInt());
      Criteria.access$302(localCriteria, paramAnonymousParcel.readInt());
      Criteria.access$402(localCriteria, paramAnonymousParcel.readInt());
      int i = paramAnonymousParcel.readInt();
      boolean bool1 = false;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      Criteria.access$502(localCriteria, bool2);
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      Criteria.access$602(localCriteria, bool2);
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      Criteria.access$702(localCriteria, bool2);
      boolean bool2 = bool1;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      }
      Criteria.access$802(localCriteria, bool2);
      return localCriteria;
    }
    
    public Criteria[] newArray(int paramAnonymousInt)
    {
      return new Criteria[paramAnonymousInt];
    }
  };
  public static final int NO_REQUIREMENT = 0;
  public static final int POWER_HIGH = 3;
  public static final int POWER_LOW = 1;
  public static final int POWER_MEDIUM = 2;
  private boolean mAltitudeRequired = false;
  private int mBearingAccuracy = 0;
  private boolean mBearingRequired = false;
  private boolean mCostAllowed = false;
  private int mHorizontalAccuracy = 0;
  private int mPowerRequirement = 0;
  private int mSpeedAccuracy = 0;
  private boolean mSpeedRequired = false;
  private int mVerticalAccuracy = 0;
  
  public Criteria() {}
  
  public Criteria(Criteria paramCriteria)
  {
    mHorizontalAccuracy = mHorizontalAccuracy;
    mVerticalAccuracy = mVerticalAccuracy;
    mSpeedAccuracy = mSpeedAccuracy;
    mBearingAccuracy = mBearingAccuracy;
    mPowerRequirement = mPowerRequirement;
    mAltitudeRequired = mAltitudeRequired;
    mBearingRequired = mBearingRequired;
    mSpeedRequired = mSpeedRequired;
    mCostAllowed = mCostAllowed;
  }
  
  private static String accuracyToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "???";
    case 3: 
      return "HIGH";
    case 2: 
      return "MEDIUM";
    case 1: 
      return "LOW";
    }
    return "---";
  }
  
  private static String powerToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "???";
    case 3: 
      return "HIGH";
    case 2: 
      return "MEDIUM";
    case 1: 
      return "LOW";
    }
    return "NO_REQ";
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAccuracy()
  {
    if (mHorizontalAccuracy >= 3) {
      return 1;
    }
    return 2;
  }
  
  public int getBearingAccuracy()
  {
    return mBearingAccuracy;
  }
  
  public int getHorizontalAccuracy()
  {
    return mHorizontalAccuracy;
  }
  
  public int getPowerRequirement()
  {
    return mPowerRequirement;
  }
  
  public int getSpeedAccuracy()
  {
    return mSpeedAccuracy;
  }
  
  public int getVerticalAccuracy()
  {
    return mVerticalAccuracy;
  }
  
  public boolean isAltitudeRequired()
  {
    return mAltitudeRequired;
  }
  
  public boolean isBearingRequired()
  {
    return mBearingRequired;
  }
  
  public boolean isCostAllowed()
  {
    return mCostAllowed;
  }
  
  public boolean isSpeedRequired()
  {
    return mSpeedRequired;
  }
  
  public void setAccuracy(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 2))
    {
      if (paramInt == 1) {
        mHorizontalAccuracy = 3;
      } else {
        mHorizontalAccuracy = 1;
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("accuracy=");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setAltitudeRequired(boolean paramBoolean)
  {
    mAltitudeRequired = paramBoolean;
  }
  
  public void setBearingAccuracy(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 3))
    {
      mBearingAccuracy = paramInt;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("accuracy=");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setBearingRequired(boolean paramBoolean)
  {
    mBearingRequired = paramBoolean;
  }
  
  public void setCostAllowed(boolean paramBoolean)
  {
    mCostAllowed = paramBoolean;
  }
  
  public void setHorizontalAccuracy(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 3))
    {
      mHorizontalAccuracy = paramInt;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("accuracy=");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setPowerRequirement(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 3))
    {
      mPowerRequirement = paramInt;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("level=");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setSpeedAccuracy(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 3))
    {
      mSpeedAccuracy = paramInt;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("accuracy=");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setSpeedRequired(boolean paramBoolean)
  {
    mSpeedRequired = paramBoolean;
  }
  
  public void setVerticalAccuracy(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 3))
    {
      mVerticalAccuracy = paramInt;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("accuracy=");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Criteria[power=");
    localStringBuilder.append(powerToString(mPowerRequirement));
    localStringBuilder.append(" acc=");
    localStringBuilder.append(accuracyToString(mHorizontalAccuracy));
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mHorizontalAccuracy);
    paramParcel.writeInt(mVerticalAccuracy);
    paramParcel.writeInt(mSpeedAccuracy);
    paramParcel.writeInt(mBearingAccuracy);
    paramParcel.writeInt(mPowerRequirement);
    paramParcel.writeInt(mAltitudeRequired);
    paramParcel.writeInt(mBearingRequired);
    paramParcel.writeInt(mSpeedRequired);
    paramParcel.writeInt(mCostAllowed);
  }
}
