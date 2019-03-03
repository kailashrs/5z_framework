package com.android.internal.telephony;

import android.text.TextUtils;
import java.util.Date;
import libcore.util.CountryTimeZones;
import libcore.util.CountryTimeZones.OffsetResult;
import libcore.util.TimeZoneFinder;

public class TimeZoneLookupHelper
{
  private static final int MS_PER_HOUR = 3600000;
  private CountryTimeZones mLastCountryTimeZones;
  
  public TimeZoneLookupHelper() {}
  
  private CountryTimeZones getCountryTimeZones(String paramString)
  {
    try
    {
      if ((mLastCountryTimeZones != null) && (mLastCountryTimeZones.isForCountryCode(paramString)))
      {
        paramString = mLastCountryTimeZones;
        return paramString;
      }
      paramString = TimeZoneFinder.getInstance().lookupCountryTimeZones(paramString);
      if (paramString != null) {
        mLastCountryTimeZones = paramString;
      }
      return paramString;
    }
    finally {}
  }
  
  static java.util.TimeZone guessZoneByNitzStatic(NitzData paramNitzData)
  {
    paramNitzData = lookupByNitzStatic(paramNitzData);
    if (paramNitzData != null) {
      paramNitzData = java.util.TimeZone.getTimeZone(zoneId);
    } else {
      paramNitzData = null;
    }
    return paramNitzData;
  }
  
  private static OffsetResult lookupByInstantOffsetDst(long paramLong, int paramInt, boolean paramBoolean)
  {
    int i = paramInt;
    int j = i;
    if (paramBoolean) {
      j = i - 3600000;
    }
    String[] arrayOfString = java.util.TimeZone.getAvailableIDs(j);
    Object localObject1 = null;
    Date localDate = new Date(paramLong);
    boolean bool1 = true;
    i = arrayOfString.length;
    j = 0;
    boolean bool2;
    for (;;)
    {
      bool2 = bool1;
      if (j >= i) {
        break;
      }
      java.util.TimeZone localTimeZone = java.util.TimeZone.getTimeZone(arrayOfString[j]);
      Object localObject2 = localObject1;
      if (localTimeZone.getOffset(paramLong) == paramInt)
      {
        localObject2 = localObject1;
        if (localTimeZone.inDaylightTime(localDate) == paramBoolean) {
          if (localObject1 == null)
          {
            localObject2 = localTimeZone;
          }
          else
          {
            bool2 = false;
            break;
          }
        }
      }
      j++;
      localObject1 = localObject2;
    }
    if (localObject1 == null) {
      return null;
    }
    return new OffsetResult(localObject1.getID(), bool2);
  }
  
  private static OffsetResult lookupByNitzStatic(NitzData paramNitzData)
  {
    int i = paramNitzData.getLocalOffsetMillis();
    boolean bool = paramNitzData.isDst();
    long l = paramNitzData.getCurrentTimeInMillis();
    OffsetResult localOffsetResult = lookupByInstantOffsetDst(l, i, bool);
    paramNitzData = localOffsetResult;
    if (localOffsetResult == null) {
      paramNitzData = lookupByInstantOffsetDst(l, i, bool ^ true);
    }
    return paramNitzData;
  }
  
  public boolean countryUsesUtc(String paramString, long paramLong)
  {
    boolean bool1 = TextUtils.isEmpty(paramString);
    boolean bool2 = false;
    if (bool1) {
      return false;
    }
    paramString = getCountryTimeZones(paramString);
    bool1 = bool2;
    if (paramString != null)
    {
      bool1 = bool2;
      if (paramString.hasUtcZone(paramLong)) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  public CountryResult lookupByCountry(String paramString, long paramLong)
  {
    paramString = getCountryTimeZones(paramString);
    if (paramString == null) {
      return null;
    }
    if (paramString.getDefaultTimeZoneId() == null) {
      return null;
    }
    return new CountryResult(paramString.getDefaultTimeZoneId(), paramString.isDefaultOkForCountryTimeZoneDetection(paramLong), paramLong);
  }
  
  public OffsetResult lookupByNitz(NitzData paramNitzData)
  {
    return lookupByNitzStatic(paramNitzData);
  }
  
  public OffsetResult lookupByNitzCountry(NitzData paramNitzData, String paramString)
  {
    paramString = getCountryTimeZones(paramString);
    if (paramString == null) {
      return null;
    }
    android.icu.util.TimeZone localTimeZone = android.icu.util.TimeZone.getDefault();
    paramNitzData = paramString.lookupByOffsetWithBias(paramNitzData.getLocalOffsetMillis(), paramNitzData.isDst(), paramNitzData.getCurrentTimeInMillis(), localTimeZone);
    if (paramNitzData == null) {
      return null;
    }
    return new OffsetResult(mTimeZone.getID(), mOneMatch);
  }
  
  public static final class CountryResult
  {
    public final boolean allZonesHaveSameOffset;
    public final long whenMillis;
    public final String zoneId;
    
    public CountryResult(String paramString, boolean paramBoolean, long paramLong)
    {
      zoneId = paramString;
      allZonesHaveSameOffset = paramBoolean;
      whenMillis = paramLong;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()))
      {
        paramObject = (CountryResult)paramObject;
        if (allZonesHaveSameOffset != allZonesHaveSameOffset) {
          return false;
        }
        if (whenMillis != whenMillis) {
          return false;
        }
        return zoneId.equals(zoneId);
      }
      return false;
    }
    
    public int hashCode()
    {
      return 31 * (31 * zoneId.hashCode() + allZonesHaveSameOffset) + (int)(whenMillis ^ whenMillis >>> 32);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CountryResult{zoneId='");
      localStringBuilder.append(zoneId);
      localStringBuilder.append('\'');
      localStringBuilder.append(", allZonesHaveSameOffset=");
      localStringBuilder.append(allZonesHaveSameOffset);
      localStringBuilder.append(", whenMillis=");
      localStringBuilder.append(whenMillis);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
  
  public static final class OffsetResult
  {
    public final boolean isOnlyMatch;
    public final String zoneId;
    
    public OffsetResult(String paramString, boolean paramBoolean)
    {
      zoneId = paramString;
      isOnlyMatch = paramBoolean;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()))
      {
        paramObject = (OffsetResult)paramObject;
        if (isOnlyMatch != isOnlyMatch) {
          return false;
        }
        return zoneId.equals(zoneId);
      }
      return false;
    }
    
    public int hashCode()
    {
      return 31 * zoneId.hashCode() + isOnlyMatch;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Result{zoneId='");
      localStringBuilder.append(zoneId);
      localStringBuilder.append('\'');
      localStringBuilder.append(", isOnlyMatch=");
      localStringBuilder.append(isOnlyMatch);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
}
