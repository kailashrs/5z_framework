package android.location;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.util.Printer;
import android.util.TimeUtils;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class Location
  implements Parcelable
{
  public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator()
  {
    public Location createFromParcel(Parcel paramAnonymousParcel)
    {
      Location localLocation = new Location(paramAnonymousParcel.readString());
      Location.access$802(localLocation, paramAnonymousParcel.readLong());
      Location.access$902(localLocation, paramAnonymousParcel.readLong());
      Location.access$1002(localLocation, paramAnonymousParcel.readByte());
      Location.access$1102(localLocation, paramAnonymousParcel.readDouble());
      Location.access$1202(localLocation, paramAnonymousParcel.readDouble());
      Location.access$1302(localLocation, paramAnonymousParcel.readDouble());
      Location.access$1402(localLocation, paramAnonymousParcel.readFloat());
      Location.access$1502(localLocation, paramAnonymousParcel.readFloat());
      Location.access$1602(localLocation, paramAnonymousParcel.readFloat());
      Location.access$1702(localLocation, paramAnonymousParcel.readFloat());
      Location.access$1802(localLocation, paramAnonymousParcel.readFloat());
      Location.access$1902(localLocation, paramAnonymousParcel.readFloat());
      Location.access$2002(localLocation, Bundle.setDefusable(paramAnonymousParcel.readBundle(), true));
      return localLocation;
    }
    
    public Location[] newArray(int paramAnonymousInt)
    {
      return new Location[paramAnonymousInt];
    }
  };
  public static final String EXTRA_COARSE_LOCATION = "coarseLocation";
  public static final String EXTRA_NO_GPS_LOCATION = "noGPSLocation";
  public static final int FORMAT_DEGREES = 0;
  public static final int FORMAT_MINUTES = 1;
  public static final int FORMAT_SECONDS = 2;
  private static final int HAS_ALTITUDE_MASK = 1;
  private static final int HAS_BEARING_ACCURACY_MASK = 128;
  private static final int HAS_BEARING_MASK = 4;
  private static final int HAS_HORIZONTAL_ACCURACY_MASK = 8;
  private static final int HAS_MOCK_PROVIDER_MASK = 16;
  private static final int HAS_SPEED_ACCURACY_MASK = 64;
  private static final int HAS_SPEED_MASK = 2;
  private static final int HAS_VERTICAL_ACCURACY_MASK = 32;
  private static ThreadLocal<BearingDistanceCache> sBearingDistanceCache = new ThreadLocal()
  {
    protected Location.BearingDistanceCache initialValue()
    {
      return new Location.BearingDistanceCache(null);
    }
  };
  private double mAltitude = 0.0D;
  private float mBearing = 0.0F;
  private float mBearingAccuracyDegrees = 0.0F;
  private long mElapsedRealtimeNanos = 0L;
  private Bundle mExtras = null;
  private byte mFieldsMask = (byte)0;
  private float mHorizontalAccuracyMeters = 0.0F;
  private double mLatitude = 0.0D;
  private double mLongitude = 0.0D;
  private String mProvider;
  private float mSpeed = 0.0F;
  private float mSpeedAccuracyMetersPerSecond = 0.0F;
  private long mTime = 0L;
  private float mVerticalAccuracyMeters = 0.0F;
  
  public Location(Location paramLocation)
  {
    set(paramLocation);
  }
  
  public Location(String paramString)
  {
    mProvider = paramString;
  }
  
  private static void computeDistanceAndBearing(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, BearingDistanceCache paramBearingDistanceCache)
  {
    double d1 = paramDouble1 * 0.017453292519943295D;
    double d2 = paramDouble3 * 0.017453292519943295D;
    double d3 = paramDouble2 * 0.017453292519943295D;
    double d4 = 0.017453292519943295D * paramDouble4;
    double d5 = (6378137.0D - 6356752.3142D) / 6378137.0D;
    double d6 = (6378137.0D * 6378137.0D - 6356752.3142D * 6356752.3142D) / (6356752.3142D * 6356752.3142D);
    double d7 = d4 - d3;
    paramDouble1 = 0.0D;
    double d8 = Math.atan((1.0D - d5) * Math.tan(d1));
    double d9 = Math.atan((1.0D - d5) * Math.tan(d2));
    double d10 = Math.cos(d8);
    double d11 = Math.cos(d9);
    double d12 = Math.sin(d8);
    double d13 = Math.sin(d9);
    double d14 = d10 * d11;
    double d15 = d12 * d13;
    paramDouble2 = 0.0D;
    paramDouble3 = 0.0D;
    double d16 = 0.0D;
    double d17 = 0.0D;
    paramDouble4 = 0.0D;
    double d18 = 0.0D;
    int i = 0;
    double d19 = d7;
    for (d17 = d16; i < 20; d17 = d16)
    {
      paramDouble4 = Math.cos(d19);
      d18 = Math.sin(d19);
      paramDouble2 = d11 * d18;
      paramDouble1 = d10 * d13 - d12 * d11 * paramDouble4;
      double d20 = Math.sqrt(paramDouble2 * paramDouble2 + paramDouble1 * paramDouble1);
      double d21 = d15 + d14 * paramDouble4;
      paramDouble2 = Math.atan2(d20, d21);
      d16 = 0.0D;
      if (d20 == 0.0D) {
        d17 = 0.0D;
      } else {
        d17 = d14 * d18 / d20;
      }
      paramDouble3 = 1.0D - d17 * d17;
      if (paramDouble3 != 0.0D) {
        d16 = d21 - 2.0D * d15 / paramDouble3;
      }
      double d22 = paramDouble3 * d6;
      paramDouble1 = 1.0D + d22 / 16384.0D * (4096.0D + (-768.0D + (320.0D - 175.0D * d22) * d22) * d22);
      double d23 = d22 / 1024.0D * (256.0D + (-128.0D + (74.0D - 47.0D * d22) * d22) * d22);
      d22 = d5 / 16.0D * paramDouble3 * (4.0D + (4.0D - 3.0D * paramDouble3) * d5);
      paramDouble3 = d16 * d16;
      paramDouble3 = d23 * d20 * (d16 + d23 / 4.0D * ((-1.0D + 2.0D * paramDouble3) * d21 - d23 / 6.0D * d16 * (-3.0D + 4.0D * d20 * d20) * (-3.0D + 4.0D * paramDouble3)));
      d16 = d7 + (1.0D - d22) * d5 * d17 * (paramDouble2 + d22 * d20 * (d16 + d22 * d21 * (-1.0D + 2.0D * d16 * d16)));
      if (Math.abs((d16 - d19) / d16) < 1.0E-12D) {
        break;
      }
      i++;
      d17 = d21;
      d19 = d16;
      d16 = d20;
    }
    BearingDistanceCache.access$102(paramBearingDistanceCache, (float)(6356752.3142D * paramDouble1 * (paramDouble2 - paramDouble3)));
    BearingDistanceCache.access$202(paramBearingDistanceCache, (float)((float)Math.atan2(d11 * d18, d10 * d13 - d12 * d11 * paramDouble4) * 57.29577951308232D));
    BearingDistanceCache.access$302(paramBearingDistanceCache, (float)((float)Math.atan2(d10 * d18, -d12 * d11 + d10 * d13 * paramDouble4) * 57.29577951308232D));
    BearingDistanceCache.access$402(paramBearingDistanceCache, d1);
    BearingDistanceCache.access$502(paramBearingDistanceCache, d2);
    BearingDistanceCache.access$602(paramBearingDistanceCache, d3);
    BearingDistanceCache.access$702(paramBearingDistanceCache, d4);
  }
  
  public static double convert(String paramString)
  {
    Object localObject1 = paramString;
    if (localObject1 != null)
    {
      int i = 0;
      paramString = (String)localObject1;
      if (((String)localObject1).charAt(0) == '-')
      {
        paramString = ((String)localObject1).substring(1);
        i = 1;
      }
      localObject1 = new StringTokenizer(paramString, ":");
      int j = ((StringTokenizer)localObject1).countTokens();
      if (j >= 1)
      {
        try
        {
          String str = ((StringTokenizer)localObject1).nextToken();
          double d1;
          if (j == 1) {
            try
            {
              d1 = Double.parseDouble(str);
              if (i != 0) {
                d1 = -d1;
              }
              return d1;
            }
            catch (NumberFormatException localNumberFormatException1) {}
          }
          Object localObject2 = localNumberFormatException1.nextToken();
          int k = Integer.parseInt(str);
          double d2 = 0.0D;
          j = 0;
          boolean bool = localNumberFormatException1.hasMoreTokens();
          if (bool)
          {
            d1 = Integer.parseInt((String)localObject2);
            d2 = Double.parseDouble(localNumberFormatException1.nextToken());
            j = 1;
          }
          else
          {
            d1 = Double.parseDouble((String)localObject2);
          }
          int m;
          if ((i != 0) && (k == 180) && (d1 == 0.0D) && (d2 == 0.0D)) {
            m = 1;
          } else {
            m = 0;
          }
          if ((k >= 0.0D) && ((k <= 179) || (m != 0))) {
            if ((d1 >= 0.0D) && (d1 < 60.0D) && ((j == 0) || (d1 <= 59.0D))) {
              if ((d2 >= 0.0D) && (d2 < 60.0D))
              {
                d1 = (k * 3600.0D + 60.0D * d1 + d2) / 3600.0D;
                if (i != 0) {
                  d1 = -d1;
                }
                return d1;
              }
            }
          }
          try
          {
            localIllegalArgumentException = new java/lang/IllegalArgumentException;
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append("coordinate=");
            ((StringBuilder)localObject2).append(paramString);
            localIllegalArgumentException.<init>(((StringBuilder)localObject2).toString());
            throw localIllegalArgumentException;
          }
          catch (NumberFormatException localNumberFormatException2) {}
          IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("coordinate=");
          ((StringBuilder)localObject2).append(paramString);
          localIllegalArgumentException.<init>(((StringBuilder)localObject2).toString());
          throw localIllegalArgumentException;
          localIllegalArgumentException = new java/lang/IllegalArgumentException;
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("coordinate=");
          ((StringBuilder)localObject2).append(paramString);
          localIllegalArgumentException.<init>(((StringBuilder)localObject2).toString());
          throw localIllegalArgumentException;
        }
        catch (NumberFormatException localNumberFormatException3) {}
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("coordinate=");
        localStringBuilder.append(paramString);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("coordinate=");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    throw new NullPointerException("coordinate");
  }
  
  public static String convert(double paramDouble, int paramInt)
  {
    if ((paramDouble >= -180.0D) && (paramDouble <= 180.0D) && (!Double.isNaN(paramDouble)))
    {
      if ((paramInt != 0) && (paramInt != 1) && (paramInt != 2))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("outputType=");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      localStringBuilder = new StringBuilder();
      double d = paramDouble;
      if (paramDouble < 0.0D)
      {
        localStringBuilder.append('-');
        d = -paramDouble;
      }
      DecimalFormat localDecimalFormat = new DecimalFormat("###.#####");
      if (paramInt != 1)
      {
        paramDouble = d;
        if (paramInt != 2) {}
      }
      else
      {
        int i = (int)Math.floor(d);
        localStringBuilder.append(i);
        localStringBuilder.append(':');
        d = (d - i) * 60.0D;
        paramDouble = d;
        if (paramInt == 2)
        {
          paramInt = (int)Math.floor(d);
          localStringBuilder.append(paramInt);
          localStringBuilder.append(':');
          paramDouble = (d - paramInt) * 60.0D;
        }
      }
      localStringBuilder.append(localDecimalFormat.format(paramDouble));
      return localStringBuilder.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("coordinate=");
    localStringBuilder.append(paramDouble);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static void distanceBetween(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, float[] paramArrayOfFloat)
  {
    if ((paramArrayOfFloat != null) && (paramArrayOfFloat.length >= 1))
    {
      BearingDistanceCache localBearingDistanceCache = (BearingDistanceCache)sBearingDistanceCache.get();
      computeDistanceAndBearing(paramDouble1, paramDouble2, paramDouble3, paramDouble4, localBearingDistanceCache);
      paramArrayOfFloat[0] = mDistance;
      if (paramArrayOfFloat.length > 1)
      {
        paramArrayOfFloat[1] = mInitialBearing;
        if (paramArrayOfFloat.length > 2) {
          paramArrayOfFloat[2] = mFinalBearing;
        }
      }
      return;
    }
    throw new IllegalArgumentException("results is null or has length < 1");
  }
  
  public float bearingTo(Location paramLocation)
  {
    BearingDistanceCache localBearingDistanceCache = (BearingDistanceCache)sBearingDistanceCache.get();
    if ((mLatitude != mLat1) || (mLongitude != mLon1) || (mLatitude != mLat2) || (mLongitude != mLon2)) {
      computeDistanceAndBearing(mLatitude, mLongitude, mLatitude, mLongitude, localBearingDistanceCache);
    }
    return mInitialBearing;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public float distanceTo(Location paramLocation)
  {
    BearingDistanceCache localBearingDistanceCache = (BearingDistanceCache)sBearingDistanceCache.get();
    if ((mLatitude != mLat1) || (mLongitude != mLon1) || (mLatitude != mLat2) || (mLongitude != mLon2)) {
      computeDistanceAndBearing(mLatitude, mLongitude, mLatitude, mLongitude, localBearingDistanceCache);
    }
    return mDistance;
  }
  
  public void dump(Printer paramPrinter, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(toString());
    paramPrinter.println(localStringBuilder.toString());
  }
  
  public float getAccuracy()
  {
    return mHorizontalAccuracyMeters;
  }
  
  public double getAltitude()
  {
    return mAltitude;
  }
  
  public float getBearing()
  {
    return mBearing;
  }
  
  public float getBearingAccuracyDegrees()
  {
    return mBearingAccuracyDegrees;
  }
  
  public long getElapsedRealtimeNanos()
  {
    return mElapsedRealtimeNanos;
  }
  
  public Location getExtraLocation(String paramString)
  {
    if (mExtras != null)
    {
      paramString = mExtras.getParcelable(paramString);
      if ((paramString instanceof Location)) {
        return (Location)paramString;
      }
    }
    return null;
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public double getLatitude()
  {
    return mLatitude;
  }
  
  public double getLongitude()
  {
    return mLongitude;
  }
  
  public String getProvider()
  {
    return mProvider;
  }
  
  public float getSpeed()
  {
    return mSpeed;
  }
  
  public float getSpeedAccuracyMetersPerSecond()
  {
    return mSpeedAccuracyMetersPerSecond;
  }
  
  public long getTime()
  {
    return mTime;
  }
  
  public float getVerticalAccuracyMeters()
  {
    return mVerticalAccuracyMeters;
  }
  
  public boolean hasAccuracy()
  {
    boolean bool;
    if ((mFieldsMask & 0x8) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasAltitude()
  {
    int i = mFieldsMask;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasBearing()
  {
    boolean bool;
    if ((mFieldsMask & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasBearingAccuracy()
  {
    boolean bool;
    if ((mFieldsMask & 0x80) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasSpeed()
  {
    boolean bool;
    if ((mFieldsMask & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasSpeedAccuracy()
  {
    boolean bool;
    if ((mFieldsMask & 0x40) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasVerticalAccuracy()
  {
    boolean bool;
    if ((mFieldsMask & 0x20) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @SystemApi
  public boolean isComplete()
  {
    if (mProvider == null) {
      return false;
    }
    if (!hasAccuracy()) {
      return false;
    }
    if (mTime == 0L) {
      return false;
    }
    return mElapsedRealtimeNanos != 0L;
  }
  
  public boolean isFromMockProvider()
  {
    boolean bool;
    if ((mFieldsMask & 0x10) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @SystemApi
  public void makeComplete()
  {
    if (mProvider == null) {
      mProvider = "?";
    }
    if (!hasAccuracy())
    {
      mFieldsMask = ((byte)(byte)(mFieldsMask | 0x8));
      mHorizontalAccuracyMeters = 100.0F;
    }
    if (mTime == 0L) {
      mTime = System.currentTimeMillis();
    }
    if (mElapsedRealtimeNanos == 0L) {
      mElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
    }
  }
  
  @Deprecated
  public void removeAccuracy()
  {
    mHorizontalAccuracyMeters = 0.0F;
    mFieldsMask = ((byte)(byte)(mFieldsMask & 0xFFFFFFF7));
  }
  
  @Deprecated
  public void removeAltitude()
  {
    mAltitude = 0.0D;
    mFieldsMask = ((byte)(byte)(mFieldsMask & 0xFFFFFFFE));
  }
  
  @Deprecated
  public void removeBearing()
  {
    mBearing = 0.0F;
    mFieldsMask = ((byte)(byte)(mFieldsMask & 0xFFFFFFFB));
  }
  
  @Deprecated
  public void removeBearingAccuracy()
  {
    mBearingAccuracyDegrees = 0.0F;
    mFieldsMask = ((byte)(byte)(mFieldsMask & 0xFF7F));
  }
  
  @Deprecated
  public void removeSpeed()
  {
    mSpeed = 0.0F;
    mFieldsMask = ((byte)(byte)(mFieldsMask & 0xFFFFFFFD));
  }
  
  @Deprecated
  public void removeSpeedAccuracy()
  {
    mSpeedAccuracyMetersPerSecond = 0.0F;
    mFieldsMask = ((byte)(byte)(mFieldsMask & 0xFFFFFFBF));
  }
  
  @Deprecated
  public void removeVerticalAccuracy()
  {
    mVerticalAccuracyMeters = 0.0F;
    mFieldsMask = ((byte)(byte)(mFieldsMask & 0xFFFFFFDF));
  }
  
  public void reset()
  {
    mProvider = null;
    mTime = 0L;
    mElapsedRealtimeNanos = 0L;
    mFieldsMask = ((byte)0);
    mLatitude = 0.0D;
    mLongitude = 0.0D;
    mAltitude = 0.0D;
    mSpeed = 0.0F;
    mBearing = 0.0F;
    mHorizontalAccuracyMeters = 0.0F;
    mVerticalAccuracyMeters = 0.0F;
    mSpeedAccuracyMetersPerSecond = 0.0F;
    mBearingAccuracyDegrees = 0.0F;
    mExtras = null;
  }
  
  public void set(Location paramLocation)
  {
    mProvider = mProvider;
    mTime = mTime;
    mElapsedRealtimeNanos = mElapsedRealtimeNanos;
    mFieldsMask = ((byte)mFieldsMask);
    mLatitude = mLatitude;
    mLongitude = mLongitude;
    mAltitude = mAltitude;
    mSpeed = mSpeed;
    mBearing = mBearing;
    mHorizontalAccuracyMeters = mHorizontalAccuracyMeters;
    mVerticalAccuracyMeters = mVerticalAccuracyMeters;
    mSpeedAccuracyMetersPerSecond = mSpeedAccuracyMetersPerSecond;
    mBearingAccuracyDegrees = mBearingAccuracyDegrees;
    if (mExtras == null) {
      paramLocation = null;
    } else {
      paramLocation = new Bundle(mExtras);
    }
    mExtras = paramLocation;
  }
  
  public void setAccuracy(float paramFloat)
  {
    mHorizontalAccuracyMeters = paramFloat;
    mFieldsMask = ((byte)(byte)(mFieldsMask | 0x8));
  }
  
  public void setAltitude(double paramDouble)
  {
    mAltitude = paramDouble;
    mFieldsMask = ((byte)(byte)(mFieldsMask | 0x1));
  }
  
  public void setBearing(float paramFloat)
  {
    float f;
    for (;;)
    {
      f = paramFloat;
      if (paramFloat >= 0.0F) {
        break;
      }
      paramFloat += 360.0F;
    }
    while (f >= 360.0F) {
      f -= 360.0F;
    }
    mBearing = f;
    mFieldsMask = ((byte)(byte)(mFieldsMask | 0x4));
  }
  
  public void setBearingAccuracyDegrees(float paramFloat)
  {
    mBearingAccuracyDegrees = paramFloat;
    mFieldsMask = ((byte)(byte)(mFieldsMask | 0x80));
  }
  
  public void setElapsedRealtimeNanos(long paramLong)
  {
    mElapsedRealtimeNanos = paramLong;
  }
  
  public void setExtraLocation(String paramString, Location paramLocation)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putParcelable(paramString, paramLocation);
  }
  
  public void setExtras(Bundle paramBundle)
  {
    if (paramBundle == null) {
      paramBundle = null;
    } else {
      paramBundle = new Bundle(paramBundle);
    }
    mExtras = paramBundle;
  }
  
  @SystemApi
  public void setIsFromMockProvider(boolean paramBoolean)
  {
    if (paramBoolean) {
      mFieldsMask = ((byte)(byte)(mFieldsMask | 0x10));
    } else {
      mFieldsMask = ((byte)(byte)(mFieldsMask & 0xFFFFFFEF));
    }
  }
  
  public void setLatitude(double paramDouble)
  {
    mLatitude = paramDouble;
  }
  
  public void setLongitude(double paramDouble)
  {
    mLongitude = paramDouble;
  }
  
  public void setProvider(String paramString)
  {
    mProvider = paramString;
  }
  
  public void setSpeed(float paramFloat)
  {
    mSpeed = paramFloat;
    mFieldsMask = ((byte)(byte)(mFieldsMask | 0x2));
  }
  
  public void setSpeedAccuracyMetersPerSecond(float paramFloat)
  {
    mSpeedAccuracyMetersPerSecond = paramFloat;
    mFieldsMask = ((byte)(byte)(mFieldsMask | 0x40));
  }
  
  public void setTime(long paramLong)
  {
    mTime = paramLong;
  }
  
  public void setVerticalAccuracyMeters(float paramFloat)
  {
    mVerticalAccuracyMeters = paramFloat;
    mFieldsMask = ((byte)(byte)(mFieldsMask | 0x20));
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Location[");
    localStringBuilder.append(mProvider);
    localStringBuilder.append(String.format(" %.6f,%.6f", new Object[] { Double.valueOf(mLatitude), Double.valueOf(mLongitude) }));
    if (hasAccuracy()) {
      localStringBuilder.append(String.format(" hAcc=%.0f", new Object[] { Float.valueOf(mHorizontalAccuracyMeters) }));
    } else {
      localStringBuilder.append(" hAcc=???");
    }
    if (mTime == 0L) {
      localStringBuilder.append(" t=?!?");
    }
    if (mElapsedRealtimeNanos == 0L)
    {
      localStringBuilder.append(" et=?!?");
    }
    else
    {
      localStringBuilder.append(" et=");
      TimeUtils.formatDuration(mElapsedRealtimeNanos / 1000000L, localStringBuilder);
    }
    if (hasAltitude())
    {
      localStringBuilder.append(" alt=");
      localStringBuilder.append(mAltitude);
    }
    if (hasSpeed())
    {
      localStringBuilder.append(" vel=");
      localStringBuilder.append(mSpeed);
    }
    if (hasBearing())
    {
      localStringBuilder.append(" bear=");
      localStringBuilder.append(mBearing);
    }
    if (hasVerticalAccuracy()) {
      localStringBuilder.append(String.format(" vAcc=%.0f", new Object[] { Float.valueOf(mVerticalAccuracyMeters) }));
    } else {
      localStringBuilder.append(" vAcc=???");
    }
    if (hasSpeedAccuracy()) {
      localStringBuilder.append(String.format(" sAcc=%.0f", new Object[] { Float.valueOf(mSpeedAccuracyMetersPerSecond) }));
    } else {
      localStringBuilder.append(" sAcc=???");
    }
    if (hasBearingAccuracy()) {
      localStringBuilder.append(String.format(" bAcc=%.0f", new Object[] { Float.valueOf(mBearingAccuracyDegrees) }));
    } else {
      localStringBuilder.append(" bAcc=???");
    }
    if (isFromMockProvider()) {
      localStringBuilder.append(" mock");
    }
    if (mExtras != null)
    {
      localStringBuilder.append(" {");
      localStringBuilder.append(mExtras);
      localStringBuilder.append('}');
    }
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mProvider);
    paramParcel.writeLong(mTime);
    paramParcel.writeLong(mElapsedRealtimeNanos);
    paramParcel.writeByte(mFieldsMask);
    paramParcel.writeDouble(mLatitude);
    paramParcel.writeDouble(mLongitude);
    paramParcel.writeDouble(mAltitude);
    paramParcel.writeFloat(mSpeed);
    paramParcel.writeFloat(mBearing);
    paramParcel.writeFloat(mHorizontalAccuracyMeters);
    paramParcel.writeFloat(mVerticalAccuracyMeters);
    paramParcel.writeFloat(mSpeedAccuracyMetersPerSecond);
    paramParcel.writeFloat(mBearingAccuracyDegrees);
    paramParcel.writeBundle(mExtras);
  }
  
  private static class BearingDistanceCache
  {
    private float mDistance = 0.0F;
    private float mFinalBearing = 0.0F;
    private float mInitialBearing = 0.0F;
    private double mLat1 = 0.0D;
    private double mLat2 = 0.0D;
    private double mLon1 = 0.0D;
    private double mLon2 = 0.0D;
    
    private BearingDistanceCache() {}
  }
}
