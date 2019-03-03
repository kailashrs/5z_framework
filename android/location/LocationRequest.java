package android.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.os.WorkSource;
import android.util.TimeUtils;

@SystemApi
public final class LocationRequest
  implements Parcelable
{
  public static final int ACCURACY_BLOCK = 102;
  public static final int ACCURACY_CITY = 104;
  public static final int ACCURACY_FINE = 100;
  public static final Parcelable.Creator<LocationRequest> CREATOR = new Parcelable.Creator()
  {
    public LocationRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      LocationRequest localLocationRequest = new LocationRequest();
      localLocationRequest.setQuality(paramAnonymousParcel.readInt());
      localLocationRequest.setFastestInterval(paramAnonymousParcel.readLong());
      localLocationRequest.setInterval(paramAnonymousParcel.readLong());
      localLocationRequest.setExpireAt(paramAnonymousParcel.readLong());
      localLocationRequest.setNumUpdates(paramAnonymousParcel.readInt());
      localLocationRequest.setSmallestDisplacement(paramAnonymousParcel.readFloat());
      int i = paramAnonymousParcel.readInt();
      boolean bool1 = false;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      localLocationRequest.setHideFromAppOps(bool2);
      boolean bool2 = bool1;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      }
      localLocationRequest.setLowPowerMode(bool2);
      String str = paramAnonymousParcel.readString();
      if (str != null) {
        localLocationRequest.setProvider(str);
      }
      paramAnonymousParcel = (WorkSource)paramAnonymousParcel.readParcelable(null);
      if (paramAnonymousParcel != null) {
        localLocationRequest.setWorkSource(paramAnonymousParcel);
      }
      return localLocationRequest;
    }
    
    public LocationRequest[] newArray(int paramAnonymousInt)
    {
      return new LocationRequest[paramAnonymousInt];
    }
  };
  private static final double FASTEST_INTERVAL_FACTOR = 6.0D;
  public static final int POWER_HIGH = 203;
  public static final int POWER_LOW = 201;
  public static final int POWER_NONE = 200;
  private long mExpireAt = Long.MAX_VALUE;
  private boolean mExplicitFastestInterval = false;
  private long mFastestInterval = (mInterval / 6.0D);
  private boolean mHideFromAppOps = false;
  private long mInterval = 3600000L;
  private boolean mLowPowerMode = false;
  private int mNumUpdates = Integer.MAX_VALUE;
  private String mProvider = "fused";
  private int mQuality = 201;
  private float mSmallestDisplacement = 0.0F;
  private WorkSource mWorkSource = null;
  
  public LocationRequest() {}
  
  public LocationRequest(LocationRequest paramLocationRequest)
  {
    mQuality = mQuality;
    mInterval = mInterval;
    mFastestInterval = mFastestInterval;
    mExplicitFastestInterval = mExplicitFastestInterval;
    mExpireAt = mExpireAt;
    mNumUpdates = mNumUpdates;
    mSmallestDisplacement = mSmallestDisplacement;
    mProvider = mProvider;
    mWorkSource = mWorkSource;
    mHideFromAppOps = mHideFromAppOps;
    mLowPowerMode = mLowPowerMode;
  }
  
  private static void checkDisplacement(float paramFloat)
  {
    if (paramFloat >= 0.0F) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid displacement: ");
    localStringBuilder.append(paramFloat);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static void checkInterval(long paramLong)
  {
    if (paramLong >= 0L) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid interval: ");
    localStringBuilder.append(paramLong);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static void checkProvider(String paramString)
  {
    if (paramString != null) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid provider: ");
    localStringBuilder.append(paramString);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static void checkQuality(int paramInt)
  {
    if ((paramInt != 100) && (paramInt != 102) && (paramInt != 104) && (paramInt != 203)) {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("invalid quality: ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
    }
  }
  
  public static LocationRequest create()
  {
    return new LocationRequest();
  }
  
  @SystemApi
  public static LocationRequest createFromDeprecatedCriteria(Criteria paramCriteria, long paramLong, float paramFloat, boolean paramBoolean)
  {
    long l = paramLong;
    if (paramLong < 0L) {
      l = 0L;
    }
    float f = paramFloat;
    if (paramFloat < 0.0F) {
      f = 0.0F;
    }
    switch (paramCriteria.getAccuracy())
    {
    default: 
      if (paramCriteria.getPowerRequirement() != 3) {
        i = 201;
      }
      break;
    case 2: 
      i = 102;
      break;
    case 1: 
      i = 100;
      break;
    }
    int i = 203;
    paramCriteria = new LocationRequest().setQuality(i).setInterval(l).setFastestInterval(l).setSmallestDisplacement(f);
    if (paramBoolean) {
      paramCriteria.setNumUpdates(1);
    }
    return paramCriteria;
  }
  
  @SystemApi
  public static LocationRequest createFromDeprecatedProvider(String paramString, long paramLong, float paramFloat, boolean paramBoolean)
  {
    long l = paramLong;
    if (paramLong < 0L) {
      l = 0L;
    }
    float f = paramFloat;
    if (paramFloat < 0.0F) {
      f = 0.0F;
    }
    int i;
    if ("passive".equals(paramString)) {
      i = 200;
    }
    for (;;)
    {
      break;
      if ("gps".equals(paramString)) {
        i = 100;
      } else {
        i = 201;
      }
    }
    paramString = new LocationRequest().setProvider(paramString).setQuality(i).setInterval(l).setFastestInterval(l).setSmallestDisplacement(f);
    if (paramBoolean) {
      paramString.setNumUpdates(1);
    }
    return paramString;
  }
  
  public static String qualityToString(int paramInt)
  {
    if (paramInt != 100)
    {
      if (paramInt != 102)
      {
        if (paramInt != 104)
        {
          if (paramInt != 203)
          {
            switch (paramInt)
            {
            default: 
              return "???";
            case 201: 
              return "POWER_LOW";
            }
            return "POWER_NONE";
          }
          return "POWER_HIGH";
        }
        return "ACCURACY_CITY";
      }
      return "ACCURACY_BLOCK";
    }
    return "ACCURACY_FINE";
  }
  
  public void decrementNumUpdates()
  {
    if (mNumUpdates != Integer.MAX_VALUE) {
      mNumUpdates -= 1;
    }
    if (mNumUpdates < 0) {
      mNumUpdates = 0;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getExpireAt()
  {
    return mExpireAt;
  }
  
  public long getFastestInterval()
  {
    return mFastestInterval;
  }
  
  @SystemApi
  public boolean getHideFromAppOps()
  {
    return mHideFromAppOps;
  }
  
  public long getInterval()
  {
    return mInterval;
  }
  
  public int getNumUpdates()
  {
    return mNumUpdates;
  }
  
  @SystemApi
  public String getProvider()
  {
    return mProvider;
  }
  
  public int getQuality()
  {
    return mQuality;
  }
  
  @SystemApi
  public float getSmallestDisplacement()
  {
    return mSmallestDisplacement;
  }
  
  @SystemApi
  public WorkSource getWorkSource()
  {
    return mWorkSource;
  }
  
  @SystemApi
  public boolean isLowPowerMode()
  {
    return mLowPowerMode;
  }
  
  public LocationRequest setExpireAt(long paramLong)
  {
    mExpireAt = paramLong;
    if (mExpireAt < 0L) {
      mExpireAt = 0L;
    }
    return this;
  }
  
  public LocationRequest setExpireIn(long paramLong)
  {
    long l = SystemClock.elapsedRealtime();
    if (paramLong > Long.MAX_VALUE - l) {
      mExpireAt = Long.MAX_VALUE;
    } else {
      mExpireAt = (paramLong + l);
    }
    if (mExpireAt < 0L) {
      mExpireAt = 0L;
    }
    return this;
  }
  
  public LocationRequest setFastestInterval(long paramLong)
  {
    checkInterval(paramLong);
    mExplicitFastestInterval = true;
    mFastestInterval = paramLong;
    return this;
  }
  
  @SystemApi
  public void setHideFromAppOps(boolean paramBoolean)
  {
    mHideFromAppOps = paramBoolean;
  }
  
  public LocationRequest setInterval(long paramLong)
  {
    checkInterval(paramLong);
    mInterval = paramLong;
    if (!mExplicitFastestInterval) {
      mFastestInterval = ((mInterval / 6.0D));
    }
    return this;
  }
  
  @SystemApi
  public LocationRequest setLowPowerMode(boolean paramBoolean)
  {
    mLowPowerMode = paramBoolean;
    return this;
  }
  
  public LocationRequest setNumUpdates(int paramInt)
  {
    if (paramInt > 0)
    {
      mNumUpdates = paramInt;
      return this;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid numUpdates: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  @SystemApi
  public LocationRequest setProvider(String paramString)
  {
    checkProvider(paramString);
    mProvider = paramString;
    return this;
  }
  
  public LocationRequest setQuality(int paramInt)
  {
    checkQuality(paramInt);
    mQuality = paramInt;
    return this;
  }
  
  @SystemApi
  public LocationRequest setSmallestDisplacement(float paramFloat)
  {
    checkDisplacement(paramFloat);
    mSmallestDisplacement = paramFloat;
    return this;
  }
  
  @SystemApi
  public void setWorkSource(WorkSource paramWorkSource)
  {
    mWorkSource = paramWorkSource;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Request[");
    localStringBuilder.append(qualityToString(mQuality));
    if (mProvider != null)
    {
      localStringBuilder.append(' ');
      localStringBuilder.append(mProvider);
    }
    if (mQuality != 200)
    {
      localStringBuilder.append(" requested=");
      TimeUtils.formatDuration(mInterval, localStringBuilder);
    }
    localStringBuilder.append(" fastest=");
    TimeUtils.formatDuration(mFastestInterval, localStringBuilder);
    if (mExpireAt != Long.MAX_VALUE)
    {
      long l1 = mExpireAt;
      long l2 = SystemClock.elapsedRealtime();
      localStringBuilder.append(" expireIn=");
      TimeUtils.formatDuration(l1 - l2, localStringBuilder);
    }
    if (mNumUpdates != Integer.MAX_VALUE)
    {
      localStringBuilder.append(" num=");
      localStringBuilder.append(mNumUpdates);
    }
    localStringBuilder.append(" lowPowerMode=");
    localStringBuilder.append(mLowPowerMode);
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mQuality);
    paramParcel.writeLong(mFastestInterval);
    paramParcel.writeLong(mInterval);
    paramParcel.writeLong(mExpireAt);
    paramParcel.writeInt(mNumUpdates);
    paramParcel.writeFloat(mSmallestDisplacement);
    paramParcel.writeInt(mHideFromAppOps);
    paramParcel.writeInt(mLowPowerMode);
    paramParcel.writeString(mProvider);
    paramParcel.writeParcelable(mWorkSource, 0);
  }
}
