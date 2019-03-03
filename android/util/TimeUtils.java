package android.util;

import android.os.SystemClock;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import libcore.util.TimeZoneFinder;
import libcore.util.ZoneInfoDB;
import libcore.util.ZoneInfoDB.TzData;

public class TimeUtils
{
  public static final int HUNDRED_DAY_FIELD_LEN = 19;
  public static final long NANOS_PER_MS = 1000000L;
  private static final int SECONDS_PER_DAY = 86400;
  private static final int SECONDS_PER_HOUR = 3600;
  private static final int SECONDS_PER_MINUTE = 60;
  private static char[] sFormatStr = new char[29];
  private static final Object sFormatSync;
  private static SimpleDateFormat sLoggingFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static char[] sTmpFormatStr = new char[29];
  
  static
  {
    sFormatSync = new Object();
  }
  
  public TimeUtils() {}
  
  private static int accumField(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    int i = 0;
    if (paramInt1 > 999)
    {
      paramInt3 = i;
      while (paramInt1 != 0)
      {
        paramInt3++;
        paramInt1 /= 10;
      }
      return paramInt3 + paramInt2;
    }
    if ((paramInt1 <= 99) && ((!paramBoolean) || (paramInt3 < 3)))
    {
      if ((paramInt1 <= 9) && ((!paramBoolean) || (paramInt3 < 2)))
      {
        if ((!paramBoolean) && (paramInt1 <= 0)) {
          return 0;
        }
        return 1 + paramInt2;
      }
      return 2 + paramInt2;
    }
    return 3 + paramInt2;
  }
  
  public static String formatDuration(long paramLong)
  {
    synchronized (sFormatSync)
    {
      int i = formatDurationLocked(paramLong, 0);
      String str = new java/lang/String;
      str.<init>(sFormatStr, 0, i);
      return str;
    }
  }
  
  public static void formatDuration(long paramLong1, long paramLong2, PrintWriter paramPrintWriter)
  {
    if (paramLong1 == 0L)
    {
      paramPrintWriter.print("--");
      return;
    }
    formatDuration(paramLong1 - paramLong2, paramPrintWriter, 0);
  }
  
  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter)
  {
    formatDuration(paramLong, paramPrintWriter, 0);
  }
  
  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter, int paramInt)
  {
    synchronized (sFormatSync)
    {
      paramInt = formatDurationLocked(paramLong, paramInt);
      String str = new java/lang/String;
      str.<init>(sFormatStr, 0, paramInt);
      paramPrintWriter.print(str);
      return;
    }
  }
  
  public static void formatDuration(long paramLong, StringBuilder paramStringBuilder)
  {
    synchronized (sFormatSync)
    {
      int i = formatDurationLocked(paramLong, 0);
      paramStringBuilder.append(sFormatStr, 0, i);
      return;
    }
  }
  
  public static void formatDuration(long paramLong, StringBuilder paramStringBuilder, int paramInt)
  {
    synchronized (sFormatSync)
    {
      paramInt = formatDurationLocked(paramLong, paramInt);
      paramStringBuilder.append(sFormatStr, 0, paramInt);
      return;
    }
  }
  
  private static int formatDurationLocked(long paramLong, int paramInt)
  {
    if (sFormatStr.length < paramInt) {
      sFormatStr = new char[paramInt];
    }
    char[] arrayOfChar = sFormatStr;
    if (paramLong == 0L)
    {
      for (i = 0; i < paramInt - 1; i++) {
        arrayOfChar[i] = ((char)32);
      }
      arrayOfChar[i] = ((char)48);
      return i + 1;
    }
    if (paramLong > 0L) {
      j = 43;
    }
    for (;;)
    {
      break;
      j = 45;
      paramLong = -paramLong;
    }
    int k = (int)(paramLong % 1000L);
    int m = (int)Math.floor(paramLong / 1000L);
    int n = 0;
    int i = m;
    if (m >= 86400)
    {
      n = m / 86400;
      i = m - 86400 * n;
    }
    if (i >= 3600)
    {
      i1 = i / 3600;
      i -= i1 * 3600;
    }
    else
    {
      i1 = 0;
    }
    int i2;
    if (i >= 60)
    {
      i2 = i / 60;
      m = i - i2 * 60;
    }
    else
    {
      i2 = 0;
      m = i;
    }
    int i3 = 0;
    int i4 = 0;
    int i5 = 3;
    boolean bool1 = false;
    if (paramInt != 0)
    {
      i = accumField(n, 1, false, 0);
      if (i > 0) {
        bool1 = true;
      }
      i += accumField(i1, 1, bool1, 2);
      if (i > 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      i += accumField(i2, 1, bool1, 2);
      if (i > 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      i6 = i + accumField(m, 1, bool1, 2);
      if (i6 > 0) {
        i = 3;
      } else {
        i = 0;
      }
      i6 += accumField(k, 2, true, i) + 1;
      i = i4;
      for (;;)
      {
        i3 = i;
        if (i6 >= paramInt) {
          break;
        }
        arrayOfChar[i] = ((char)32);
        i++;
        i6++;
      }
    }
    arrayOfChar[i3] = ((char)j);
    int j = i3 + 1;
    if (paramInt != 0) {
      i = 1;
    } else {
      i = 0;
    }
    bool1 = true;
    paramInt = 2;
    int i6 = printFieldLocked(arrayOfChar, n, 'd', j, false, 0);
    boolean bool2;
    if (i6 != j) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    if (i != 0) {
      n = paramInt;
    } else {
      n = 0;
    }
    int i1 = printFieldLocked(arrayOfChar, i1, 'h', i6, bool2, n);
    if (i1 != j) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    if (i != 0) {
      n = paramInt;
    } else {
      n = 0;
    }
    n = printFieldLocked(arrayOfChar, i2, 'm', i1, bool2, n);
    if (n == j) {
      bool1 = false;
    }
    if (i == 0) {
      paramInt = 0;
    }
    n = printFieldLocked(arrayOfChar, m, 's', n, bool1, paramInt);
    if ((i != 0) && (n != j)) {
      paramInt = i5;
    } else {
      paramInt = 0;
    }
    paramInt = printFieldLocked(arrayOfChar, k, 'm', n, true, paramInt);
    arrayOfChar[paramInt] = ((char)115);
    return paramInt + 1;
  }
  
  public static String formatForLogging(long paramLong)
  {
    if (paramLong <= 0L) {
      return "unknown";
    }
    return sLoggingFormat.format(new Date(paramLong));
  }
  
  public static String formatUptime(long paramLong)
  {
    long l = paramLong - SystemClock.uptimeMillis();
    if (l > 0L)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramLong);
      localStringBuilder.append(" (in ");
      localStringBuilder.append(l);
      localStringBuilder.append(" ms)");
      return localStringBuilder.toString();
    }
    if (l < 0L)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramLong);
      localStringBuilder.append(" (");
      localStringBuilder.append(-l);
      localStringBuilder.append(" ms ago)");
      return localStringBuilder.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramLong);
    localStringBuilder.append(" (now)");
    return localStringBuilder.toString();
  }
  
  private static android.icu.util.TimeZone getIcuTimeZone(int paramInt, boolean paramBoolean, long paramLong, String paramString)
  {
    if (paramString == null) {
      return null;
    }
    android.icu.util.TimeZone localTimeZone = android.icu.util.TimeZone.getDefault();
    return TimeZoneFinder.getInstance().lookupTimeZoneByCountryAndOffset(paramString, paramInt, paramBoolean, paramLong, localTimeZone);
  }
  
  public static java.util.TimeZone getTimeZone(int paramInt, boolean paramBoolean, long paramLong, String paramString)
  {
    paramString = getIcuTimeZone(paramInt, paramBoolean, paramLong, paramString);
    if (paramString != null) {
      paramString = java.util.TimeZone.getTimeZone(paramString.getID());
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  public static String getTimeZoneDatabaseVersion()
  {
    return ZoneInfoDB.getInstance().getVersion();
  }
  
  public static String logTimeOfDay(long paramLong)
  {
    Calendar localCalendar = Calendar.getInstance();
    if (paramLong >= 0L)
    {
      localCalendar.setTimeInMillis(paramLong);
      return String.format("%tm-%td %tH:%tM:%tS.%tL", new Object[] { localCalendar, localCalendar, localCalendar, localCalendar, localCalendar, localCalendar });
    }
    return Long.toString(paramLong);
  }
  
  private static int printFieldLocked(char[] paramArrayOfChar, int paramInt1, char paramChar, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    int i;
    if (!paramBoolean)
    {
      i = paramInt2;
      if (paramInt1 <= 0) {}
    }
    else
    {
      if (paramInt1 > 999)
      {
        paramInt3 = 0;
        while ((paramInt1 != 0) && (paramInt3 < sTmpFormatStr.length))
        {
          sTmpFormatStr[paramInt3] = ((char)(char)(paramInt1 % 10 + 48));
          paramInt3++;
          paramInt1 /= 10;
        }
        for (paramInt1 = paramInt3 - 1; paramInt1 >= 0; paramInt1--)
        {
          paramArrayOfChar[paramInt2] = ((char)sTmpFormatStr[paramInt1]);
          paramInt2++;
        }
      }
      else
      {
        int j;
        if ((!paramBoolean) || (paramInt3 < 3))
        {
          i = paramInt1;
          j = paramInt2;
          if (paramInt1 <= 99) {}
        }
        else
        {
          i = paramInt1 / 100;
          paramArrayOfChar[paramInt2] = ((char)(char)(i + 48));
          j = paramInt2 + 1;
          i = paramInt1 - i * 100;
        }
        if (((!paramBoolean) || (paramInt3 < 2)) && (i <= 9))
        {
          paramInt3 = i;
          paramInt1 = j;
          if (paramInt2 == j) {}
        }
        else
        {
          paramInt2 = i / 10;
          paramArrayOfChar[j] = ((char)(char)(paramInt2 + 48));
          paramInt1 = j + 1;
          paramInt3 = i - paramInt2 * 10;
        }
        paramArrayOfChar[paramInt1] = ((char)(char)(paramInt3 + 48));
        paramInt2 = paramInt1 + 1;
      }
      paramArrayOfChar[paramInt2] = ((char)paramChar);
      i = paramInt2 + 1;
    }
    return i;
  }
}
