package com.android.internal.telephony;

import android.telephony.Rlog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import java.util.Calendar;
import java.util.TimeZone;

@VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
public final class NitzData
{
  private static final String LOG_TAG = "SST";
  private static final int MAX_NITZ_YEAR = 2037;
  private static final int MS_PER_QUARTER_HOUR = 900000;
  private final long mCurrentTimeMillis;
  private final Integer mDstOffset;
  private final TimeZone mEmulatorHostTimeZone;
  private final String mOriginalString;
  private final int mZoneOffset;
  
  private NitzData(String paramString, int paramInt, Integer paramInteger, long paramLong, TimeZone paramTimeZone)
  {
    if (paramString != null)
    {
      mOriginalString = paramString;
      mZoneOffset = paramInt;
      mDstOffset = paramInteger;
      mCurrentTimeMillis = paramLong;
      mEmulatorHostTimeZone = paramTimeZone;
      return;
    }
    throw new NullPointerException("originalString==null");
  }
  
  public static NitzData createForTests(int paramInt, Integer paramInteger, long paramLong, TimeZone paramTimeZone)
  {
    return new NitzData("Test data", paramInt, paramInteger, paramLong, paramTimeZone);
  }
  
  public static NitzData parse(String paramString)
  {
    try
    {
      Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
      localCalendar.clear();
      int i = 0;
      localCalendar.set(16, 0);
      String[] arrayOfString = paramString.split("[/:,+-]");
      int j = 2000 + Integer.parseInt(arrayOfString[0]);
      if (j > 2037)
      {
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("NITZ year: ");
        ((StringBuilder)localObject1).append(j);
        ((StringBuilder)localObject1).append(" exceeds limit, skip NITZ time update");
        Rlog.e("SST", ((StringBuilder)localObject1).toString());
        return null;
      }
      int k = 1;
      localCalendar.set(1, j);
      localCalendar.set(2, Integer.parseInt(arrayOfString[1]) - 1);
      localCalendar.set(5, Integer.parseInt(arrayOfString[2]));
      localCalendar.set(10, Integer.parseInt(arrayOfString[3]));
      localCalendar.set(12, Integer.parseInt(arrayOfString[4]));
      localCalendar.set(13, Integer.parseInt(arrayOfString[5]));
      if (paramString.indexOf('-') == -1) {
        i = 1;
      }
      j = Integer.parseInt(arrayOfString[6]);
      if (i != 0) {
        i = k;
      } else {
        i = -1;
      }
      if (arrayOfString.length >= 8) {
        localObject2 = Integer.valueOf(Integer.parseInt(arrayOfString[7]));
      } else {
        localObject2 = null;
      }
      localObject1 = null;
      if (localObject2 != null) {
        localObject1 = Integer.valueOf(((Integer)localObject2).intValue() * 900000);
      }
      Object localObject2 = null;
      if (arrayOfString.length >= 9) {
        localObject2 = TimeZone.getTimeZone(arrayOfString[8].replace('!', '/'));
      }
      localObject1 = new NitzData(paramString, i * j * 900000, (Integer)localObject1, localCalendar.getTimeInMillis(), (TimeZone)localObject2);
      return localObject1;
    }
    catch (RuntimeException localRuntimeException)
    {
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("NITZ: Parsing NITZ time ");
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append(" ex=");
      ((StringBuilder)localObject1).append(localRuntimeException);
      Rlog.e("SST", ((StringBuilder)localObject1).toString());
    }
    return null;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (NitzData)paramObject;
      if (mZoneOffset != mZoneOffset) {
        return false;
      }
      if (mCurrentTimeMillis != mCurrentTimeMillis) {
        return false;
      }
      if (!mOriginalString.equals(mOriginalString)) {
        return false;
      }
      if (mDstOffset != null ? !mDstOffset.equals(mDstOffset) : mDstOffset != null) {
        return false;
      }
      if (mEmulatorHostTimeZone != null) {
        bool = mEmulatorHostTimeZone.equals(mEmulatorHostTimeZone);
      } else if (mEmulatorHostTimeZone != null) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public long getCurrentTimeInMillis()
  {
    return mCurrentTimeMillis;
  }
  
  public Integer getDstAdjustmentMillis()
  {
    return mDstOffset;
  }
  
  public TimeZone getEmulatorHostTimeZone()
  {
    return mEmulatorHostTimeZone;
  }
  
  public int getLocalOffsetMillis()
  {
    return mZoneOffset;
  }
  
  public int hashCode()
  {
    int i = mOriginalString.hashCode();
    int j = mZoneOffset;
    Integer localInteger = mDstOffset;
    int k = 0;
    int m;
    if (localInteger != null) {
      m = mDstOffset.hashCode();
    } else {
      m = 0;
    }
    int n = (int)(mCurrentTimeMillis ^ mCurrentTimeMillis >>> 32);
    if (mEmulatorHostTimeZone != null) {
      k = mEmulatorHostTimeZone.hashCode();
    }
    return 31 * (31 * (31 * (31 * i + j) + m) + n) + k;
  }
  
  public boolean isDst()
  {
    boolean bool;
    if ((mDstOffset != null) && (mDstOffset.intValue() != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NitzData{mOriginalString=");
    localStringBuilder.append(mOriginalString);
    localStringBuilder.append(", mZoneOffset=");
    localStringBuilder.append(mZoneOffset);
    localStringBuilder.append(", mDstOffset=");
    localStringBuilder.append(mDstOffset);
    localStringBuilder.append(", mCurrentTimeMillis=");
    localStringBuilder.append(mCurrentTimeMillis);
    localStringBuilder.append(", mEmulatorHostTimeZone=");
    localStringBuilder.append(mEmulatorHostTimeZone);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
}
