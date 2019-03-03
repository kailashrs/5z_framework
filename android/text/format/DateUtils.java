package android.text.format;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.icu.text.MeasureFormat;
import android.icu.text.MeasureFormat.FormatWidth;
import android.icu.util.Measure;
import android.icu.util.MeasureUnit;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import libcore.icu.DateIntervalFormat;
import libcore.icu.LocaleData;
import libcore.icu.RelativeDateTimeFormatter;

public class DateUtils
{
  @Deprecated
  public static final String ABBREV_MONTH_FORMAT = "%b";
  public static final String ABBREV_WEEKDAY_FORMAT = "%a";
  public static final long DAY_IN_MILLIS = 86400000L;
  @Deprecated
  public static final int FORMAT_12HOUR = 64;
  @Deprecated
  public static final int FORMAT_24HOUR = 128;
  public static final int FORMAT_ABBREV_ALL = 524288;
  public static final int FORMAT_ABBREV_MONTH = 65536;
  public static final int FORMAT_ABBREV_RELATIVE = 262144;
  public static final int FORMAT_ABBREV_TIME = 16384;
  public static final int FORMAT_ABBREV_WEEKDAY = 32768;
  @Deprecated
  public static final int FORMAT_CAP_AMPM = 256;
  @Deprecated
  public static final int FORMAT_CAP_MIDNIGHT = 4096;
  @Deprecated
  public static final int FORMAT_CAP_NOON = 1024;
  @Deprecated
  public static final int FORMAT_CAP_NOON_MIDNIGHT = 5120;
  public static final int FORMAT_NO_MIDNIGHT = 2048;
  public static final int FORMAT_NO_MONTH_DAY = 32;
  public static final int FORMAT_NO_NOON = 512;
  @Deprecated
  public static final int FORMAT_NO_NOON_MIDNIGHT = 2560;
  public static final int FORMAT_NO_YEAR = 8;
  public static final int FORMAT_NUMERIC_DATE = 131072;
  public static final int FORMAT_SHOW_DATE = 16;
  public static final int FORMAT_SHOW_TIME = 1;
  public static final int FORMAT_SHOW_WEEKDAY = 2;
  public static final int FORMAT_SHOW_YEAR = 4;
  @Deprecated
  public static final int FORMAT_UTC = 8192;
  public static final long HOUR_IN_MILLIS = 3600000L;
  @Deprecated
  public static final String HOUR_MINUTE_24 = "%H:%M";
  @Deprecated
  public static final int LENGTH_LONG = 10;
  @Deprecated
  public static final int LENGTH_MEDIUM = 20;
  @Deprecated
  public static final int LENGTH_SHORT = 30;
  @Deprecated
  public static final int LENGTH_SHORTER = 40;
  @Deprecated
  public static final int LENGTH_SHORTEST = 50;
  public static final long MINUTE_IN_MILLIS = 60000L;
  public static final String MONTH_DAY_FORMAT = "%-d";
  public static final String MONTH_FORMAT = "%B";
  public static final String NUMERIC_MONTH_FORMAT = "%m";
  public static final long SECOND_IN_MILLIS = 1000L;
  public static final String WEEKDAY_FORMAT = "%A";
  public static final long WEEK_IN_MILLIS = 604800000L;
  public static final String YEAR_FORMAT = "%Y";
  public static final String YEAR_FORMAT_TWO_DIGITS = "%g";
  public static final long YEAR_IN_MILLIS = 31449600000L;
  private static String sElapsedFormatHMMSS;
  private static String sElapsedFormatMMSS;
  private static Configuration sLastConfig;
  private static final Object sLock = new Object();
  private static Time sNowTime;
  private static Time sThenTime;
  @Deprecated
  public static final int[] sameMonthTable = null;
  @Deprecated
  public static final int[] sameYearTable = null;
  
  public DateUtils() {}
  
  public static String formatDateRange(Context paramContext, long paramLong1, long paramLong2, int paramInt)
  {
    return formatDateRange(paramContext, new Formatter(new StringBuilder(50), Locale.getDefault()), paramLong1, paramLong2, paramInt).toString();
  }
  
  public static Formatter formatDateRange(Context paramContext, Formatter paramFormatter, long paramLong1, long paramLong2, int paramInt)
  {
    return formatDateRange(paramContext, paramFormatter, paramLong1, paramLong2, paramInt, null);
  }
  
  public static Formatter formatDateRange(Context paramContext, Formatter paramFormatter, long paramLong1, long paramLong2, int paramInt, String paramString)
  {
    int i = paramInt;
    if ((paramInt & 0xC1) == 1)
    {
      if (DateFormat.is24HourFormat(paramContext)) {
        i = 128;
      } else {
        i = 64;
      }
      i = paramInt | i;
    }
    paramContext = DateIntervalFormat.formatDateRange(paramLong1, paramLong2, i, paramString);
    try
    {
      paramFormatter.out().append(paramContext);
      return paramFormatter;
    }
    catch (IOException paramContext)
    {
      throw new AssertionError(paramContext);
    }
  }
  
  public static String formatDateTime(Context paramContext, long paramLong, int paramInt)
  {
    return formatDateRange(paramContext, paramLong, paramLong, paramInt);
  }
  
  public static CharSequence formatDuration(long paramLong)
  {
    return formatDuration(paramLong, 10);
  }
  
  public static CharSequence formatDuration(long paramLong, int paramInt)
  {
    if (paramInt != 10)
    {
      if ((paramInt != 20) && (paramInt != 30) && (paramInt != 40))
      {
        if (paramInt != 50) {
          localObject = MeasureFormat.FormatWidth.WIDE;
        } else {
          localObject = MeasureFormat.FormatWidth.NARROW;
        }
      }
      else {
        localObject = MeasureFormat.FormatWidth.SHORT;
      }
    }
    else {
      localObject = MeasureFormat.FormatWidth.WIDE;
    }
    Object localObject = MeasureFormat.getInstance(Locale.getDefault(), (MeasureFormat.FormatWidth)localObject);
    if (paramLong >= 3600000L) {
      return ((MeasureFormat)localObject).format(new Measure(Integer.valueOf((int)((1800000L + paramLong) / 3600000L)), MeasureUnit.HOUR));
    }
    if (paramLong >= 60000L) {
      return ((MeasureFormat)localObject).format(new Measure(Integer.valueOf((int)((30000L + paramLong) / 60000L)), MeasureUnit.MINUTE));
    }
    return ((MeasureFormat)localObject).format(new Measure(Integer.valueOf((int)((500L + paramLong) / 1000L)), MeasureUnit.SECOND));
  }
  
  public static String formatElapsedTime(long paramLong)
  {
    return formatElapsedTime(null, paramLong);
  }
  
  public static String formatElapsedTime(StringBuilder paramStringBuilder, long paramLong)
  {
    long l1 = 0L;
    long l2 = 0L;
    if (paramLong >= 3600L)
    {
      l1 = paramLong / 3600L;
      paramLong -= 3600L * l1;
    }
    long l3 = paramLong;
    if (paramLong >= 60L)
    {
      l2 = paramLong / 60L;
      l3 = paramLong - 60L * l2;
    }
    if (paramStringBuilder == null) {
      paramStringBuilder = new StringBuilder(8);
    } else {
      paramStringBuilder.setLength(0);
    }
    paramStringBuilder = new Formatter(paramStringBuilder, Locale.getDefault());
    initFormatStrings();
    if (l1 > 0L) {
      return paramStringBuilder.format(sElapsedFormatHMMSS, new Object[] { Long.valueOf(l1), Long.valueOf(l2), Long.valueOf(l3) }).toString();
    }
    return paramStringBuilder.format(sElapsedFormatMMSS, new Object[] { Long.valueOf(l2), Long.valueOf(l3) }).toString();
  }
  
  public static final CharSequence formatSameDayTime(long paramLong1, long paramLong2, int paramInt1, int paramInt2)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTimeInMillis(paramLong1);
    Date localDate = localGregorianCalendar.getTime();
    Object localObject = new GregorianCalendar();
    ((Calendar)localObject).setTimeInMillis(paramLong2);
    if ((localGregorianCalendar.get(1) == ((Calendar)localObject).get(1)) && (localGregorianCalendar.get(2) == ((Calendar)localObject).get(2)) && (localGregorianCalendar.get(5) == ((Calendar)localObject).get(5))) {
      localObject = java.text.DateFormat.getTimeInstance(paramInt2);
    } else {
      localObject = java.text.DateFormat.getDateInstance(paramInt1);
    }
    return ((java.text.DateFormat)localObject).format(localDate);
  }
  
  @Deprecated
  public static String getAMPMString(int paramInt)
  {
    return getgetDefaultamPm[(paramInt + 0)];
  }
  
  @Deprecated
  public static String getDayOfWeekString(int paramInt1, int paramInt2)
  {
    Object localObject = LocaleData.get(Locale.getDefault());
    if (paramInt2 != 10)
    {
      if (paramInt2 != 20)
      {
        if (paramInt2 != 30)
        {
          if (paramInt2 != 40)
          {
            if (paramInt2 != 50) {
              localObject = shortWeekdayNames;
            } else {
              localObject = tinyWeekdayNames;
            }
          }
          else {
            localObject = shortWeekdayNames;
          }
        }
        else {
          localObject = shortWeekdayNames;
        }
      }
      else {
        localObject = shortWeekdayNames;
      }
    }
    else {
      localObject = longWeekdayNames;
    }
    return localObject[paramInt1];
  }
  
  @Deprecated
  public static String getMonthString(int paramInt1, int paramInt2)
  {
    Object localObject = LocaleData.get(Locale.getDefault());
    if (paramInt2 != 10)
    {
      if (paramInt2 != 20)
      {
        if (paramInt2 != 30)
        {
          if (paramInt2 != 40)
          {
            if (paramInt2 != 50) {
              localObject = shortMonthNames;
            } else {
              localObject = tinyMonthNames;
            }
          }
          else {
            localObject = shortMonthNames;
          }
        }
        else {
          localObject = shortMonthNames;
        }
      }
      else {
        localObject = shortMonthNames;
      }
    }
    else {
      localObject = longMonthNames;
    }
    return localObject[paramInt1];
  }
  
  public static CharSequence getRelativeDateTimeString(Context paramContext, long paramLong1, long paramLong2, long paramLong3, int paramInt)
  {
    int i = paramInt;
    if ((paramInt & 0xC1) == 1)
    {
      if (DateFormat.is24HourFormat(paramContext)) {
        i = 128;
      } else {
        i = 64;
      }
      i = paramInt | i;
    }
    return RelativeDateTimeFormatter.getRelativeDateTimeString(Locale.getDefault(), TimeZone.getDefault(), paramLong1, System.currentTimeMillis(), paramLong2, paramLong3, i);
  }
  
  public static CharSequence getRelativeTimeSpanString(long paramLong)
  {
    return getRelativeTimeSpanString(paramLong, System.currentTimeMillis(), 60000L);
  }
  
  public static CharSequence getRelativeTimeSpanString(long paramLong1, long paramLong2, long paramLong3)
  {
    return getRelativeTimeSpanString(paramLong1, paramLong2, paramLong3, 65556);
  }
  
  public static CharSequence getRelativeTimeSpanString(long paramLong1, long paramLong2, long paramLong3, int paramInt)
  {
    return RelativeDateTimeFormatter.getRelativeTimeSpanString(Locale.getDefault(), TimeZone.getDefault(), paramLong1, paramLong2, paramLong3, paramInt);
  }
  
  public static CharSequence getRelativeTimeSpanString(Context paramContext, long paramLong)
  {
    return getRelativeTimeSpanString(paramContext, paramLong, false);
  }
  
  public static CharSequence getRelativeTimeSpanString(Context paramContext, long paramLong, boolean paramBoolean)
  {
    long l1 = System.currentTimeMillis();
    long l2 = Math.abs(l1 - paramLong);
    try
    {
      Object localObject1;
      if (sNowTime == null)
      {
        localObject1 = new android/text/format/Time;
        ((Time)localObject1).<init>();
        sNowTime = (Time)localObject1;
      }
      if (sThenTime == null)
      {
        localObject1 = new android/text/format/Time;
        ((Time)localObject1).<init>();
        sThenTime = (Time)localObject1;
      }
      sNowTime.set(l1);
      sThenTime.set(paramLong);
      int i;
      if ((l2 < 86400000L) && (sNowTimeweekDay == sThenTimeweekDay))
      {
        localObject1 = formatDateRange(paramContext, paramLong, paramLong, 1);
        i = 17040859;
      }
      else if (sNowTimeyear != sThenTimeyear)
      {
        localObject1 = formatDateRange(paramContext, paramLong, paramLong, 131092);
        i = 17040858;
      }
      else
      {
        localObject1 = formatDateRange(paramContext, paramLong, paramLong, 65552);
        i = 17040858;
      }
      Object localObject2 = localObject1;
      if (paramBoolean) {
        localObject2 = paramContext.getResources().getString(i, new Object[] { localObject1 });
      }
      return localObject2;
    }
    finally {}
  }
  
  private static void initFormatStrings()
  {
    synchronized (sLock)
    {
      initFormatStringsLocked();
      return;
    }
  }
  
  private static void initFormatStringsLocked()
  {
    Resources localResources = Resources.getSystem();
    Configuration localConfiguration = localResources.getConfiguration();
    if ((sLastConfig == null) || (!sLastConfig.equals(localConfiguration)))
    {
      sLastConfig = localConfiguration;
      sElapsedFormatMMSS = localResources.getString(17039909);
      sElapsedFormatHMMSS = localResources.getString(17039908);
    }
  }
  
  public static boolean isToday(long paramLong)
  {
    Time localTime = new Time();
    localTime.set(paramLong);
    int i = year;
    int j = month;
    int k = monthDay;
    localTime.set(System.currentTimeMillis());
    boolean bool;
    if ((i == year) && (j == month) && (k == monthDay)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
