package android.text.format;

import android.util.TimeFormatException;
import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;
import libcore.util.ZoneInfo;
import libcore.util.ZoneInfo.WallTime;
import libcore.util.ZoneInfoDB;
import libcore.util.ZoneInfoDB.TzData;

@Deprecated
public class Time
{
  private static final int[] DAYS_PER_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
  public static final int EPOCH_JULIAN_DAY = 2440588;
  public static final int FRIDAY = 5;
  public static final int HOUR = 3;
  public static final int MINUTE = 2;
  public static final int MONDAY = 1;
  public static final int MONDAY_BEFORE_JULIAN_EPOCH = 2440585;
  public static final int MONTH = 5;
  public static final int MONTH_DAY = 4;
  public static final int SATURDAY = 6;
  public static final int SECOND = 1;
  public static final int SUNDAY = 0;
  public static final int THURSDAY = 4;
  public static final String TIMEZONE_UTC = "UTC";
  public static final int TUESDAY = 2;
  public static final int WEDNESDAY = 3;
  public static final int WEEK_DAY = 7;
  public static final int WEEK_NUM = 9;
  public static final int YEAR = 6;
  public static final int YEAR_DAY = 8;
  private static final String Y_M_D = "%Y-%m-%d";
  private static final String Y_M_D_T_H_M_S_000 = "%Y-%m-%dT%H:%M:%S.000";
  private static final String Y_M_D_T_H_M_S_000_Z = "%Y-%m-%dT%H:%M:%S.000Z";
  private static final int[] sThursdayOffset = { -3, 3, 2, 1, 0, -1, -2 };
  public boolean allDay;
  private TimeCalculator calculator;
  public long gmtoff;
  public int hour;
  public int isDst;
  public int minute;
  public int month;
  public int monthDay;
  public int second;
  public String timezone;
  public int weekDay;
  public int year;
  public int yearDay;
  
  public Time()
  {
    initialize(TimeZone.getDefault().getID());
  }
  
  public Time(Time paramTime)
  {
    initialize(timezone);
    set(paramTime);
  }
  
  public Time(String paramString)
  {
    if (paramString != null)
    {
      initialize(paramString);
      return;
    }
    throw new NullPointerException("timezoneId is null!");
  }
  
  private void checkChar(String paramString, int paramInt, char paramChar)
  {
    char c = paramString.charAt(paramInt);
    if (c == paramChar) {
      return;
    }
    throw new TimeFormatException(String.format("Unexpected character 0x%02d at pos=%d.  Expected 0x%02d ('%c').", new Object[] { Integer.valueOf(c), Integer.valueOf(paramInt), Integer.valueOf(paramChar), Character.valueOf(paramChar) }));
  }
  
  public static int compare(Time paramTime1, Time paramTime2)
  {
    if (paramTime1 != null)
    {
      if (paramTime2 != null)
      {
        calculator.copyFieldsFromTime(paramTime1);
        calculator.copyFieldsFromTime(paramTime2);
        return TimeCalculator.compare(calculator, calculator);
      }
      throw new NullPointerException("b == null");
    }
    throw new NullPointerException("a == null");
  }
  
  private static int getChar(String paramString, int paramInt1, int paramInt2)
  {
    char c = paramString.charAt(paramInt1);
    if (Character.isDigit(c)) {
      return Character.getNumericValue(c) * paramInt2;
    }
    paramString = new StringBuilder();
    paramString.append("Parse error at pos=");
    paramString.append(paramInt1);
    throw new TimeFormatException(paramString.toString());
  }
  
  public static String getCurrentTimezone()
  {
    return TimeZone.getDefault().getID();
  }
  
  public static int getJulianDay(long paramLong1, long paramLong2)
  {
    return (int)((paramLong1 + 1000L * paramLong2) / 86400000L) + 2440588;
  }
  
  public static int getJulianMondayFromWeeksSinceEpoch(int paramInt)
  {
    return 2440585 + paramInt * 7;
  }
  
  public static int getWeeksSinceEpochFromJulianDay(int paramInt1, int paramInt2)
  {
    int i = 4 - paramInt2;
    paramInt2 = i;
    if (i < 0) {
      paramInt2 = i + 7;
    }
    return (paramInt1 - (2440588 - paramInt2)) / 7;
  }
  
  private void initialize(String paramString)
  {
    timezone = paramString;
    year = 1970;
    monthDay = 1;
    isDst = -1;
    calculator = new TimeCalculator(paramString);
  }
  
  public static boolean isEpoch(Time paramTime)
  {
    boolean bool = true;
    if (getJulianDay(paramTime.toMillis(true), 0L) != 2440588) {
      bool = false;
    }
    return bool;
  }
  
  private boolean parse3339Internal(String paramString)
  {
    int i = paramString.length();
    if (i >= 10)
    {
      boolean bool1 = false;
      boolean bool2 = false;
      year = (getChar(paramString, 0, 1000) + getChar(paramString, 1, 100) + getChar(paramString, 2, 10) + getChar(paramString, 3, 1));
      checkChar(paramString, 4, '-');
      month = (getChar(paramString, 5, 10) + getChar(paramString, 6, 1) - 1);
      checkChar(paramString, 7, '-');
      monthDay = (getChar(paramString, 8, 10) + getChar(paramString, 9, 1));
      if (i >= 19)
      {
        checkChar(paramString, 10, 'T');
        allDay = false;
        int j = getChar(paramString, 11, 10) + getChar(paramString, 12, 1);
        checkChar(paramString, 13, ':');
        int k = getChar(paramString, 14, 10) + getChar(paramString, 15, 1);
        checkChar(paramString, 16, ':');
        second = (getChar(paramString, 17, 10) + getChar(paramString, 18, 1));
        int m = 19;
        int n = m;
        if (19 < i)
        {
          n = m;
          if (paramString.charAt(19) == '.')
          {
            n = m;
            do
            {
              m = n + 1;
              n = m;
              if (m >= i) {
                break;
              }
              n = m;
            } while (Character.isDigit(paramString.charAt(m)));
            n = m;
          }
        }
        int i1 = 0;
        m = j;
        int i2 = k;
        if (i > n)
        {
          m = paramString.charAt(n);
          if (m != 43)
          {
            if (m != 45)
            {
              if (m == 90) {
                m = 0;
              } else {
                throw new TimeFormatException(String.format("Unexpected character 0x%02d at position %d.  Expected + or -", new Object[] { Integer.valueOf(m), Integer.valueOf(n) }));
              }
            }
            else {
              m = 1;
            }
          }
          else {
            m = -1;
          }
          int i3 = m;
          bool1 = true;
          bool2 = bool1;
          m = j;
          i2 = k;
          i1 = i3;
          if (i3 != 0) {
            if (i >= n + 6)
            {
              m = j + (getChar(paramString, n + 1, 10) + getChar(paramString, n + 2, 1)) * i3;
              i2 = k + (getChar(paramString, n + 4, 10) + getChar(paramString, n + 5, 1)) * i3;
              bool2 = bool1;
              i1 = i3;
            }
            else
            {
              throw new TimeFormatException(String.format("Unexpected length; should be %d characters", new Object[] { Integer.valueOf(n + 6) }));
            }
          }
        }
        hour = m;
        minute = i2;
        if (i1 != 0) {
          normalize(false);
        }
      }
      else
      {
        allDay = true;
        hour = 0;
        minute = 0;
        second = 0;
        bool2 = bool1;
      }
      weekDay = 0;
      yearDay = 0;
      isDst = -1;
      gmtoff = 0L;
      return bool2;
    }
    throw new TimeFormatException("String too short --- expected at least 10 characters.");
  }
  
  private boolean parseInternal(String paramString)
  {
    int i = paramString.length();
    if (i >= 8)
    {
      boolean bool = false;
      year = (getChar(paramString, 0, 1000) + getChar(paramString, 1, 100) + getChar(paramString, 2, 10) + getChar(paramString, 3, 1));
      month = (getChar(paramString, 4, 10) + getChar(paramString, 5, 1) - 1);
      monthDay = (getChar(paramString, 6, 10) + getChar(paramString, 7, 1));
      if (i > 8)
      {
        if (i >= 15)
        {
          checkChar(paramString, 8, 'T');
          allDay = false;
          hour = (getChar(paramString, 9, 10) + getChar(paramString, 10, 1));
          minute = (getChar(paramString, 11, 10) + getChar(paramString, 12, 1));
          int j = getChar(paramString, 13, 10);
          second = (getChar(paramString, 14, 1) + j);
          if (i > 15)
          {
            checkChar(paramString, 15, 'Z');
            bool = true;
          }
        }
        else
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("String is too short: \"");
          localStringBuilder.append(paramString);
          localStringBuilder.append("\" If there are more than 8 characters there must be at least 15.");
          throw new TimeFormatException(localStringBuilder.toString());
        }
      }
      else
      {
        allDay = true;
        hour = 0;
        minute = 0;
        second = 0;
      }
      weekDay = 0;
      yearDay = 0;
      isDst = -1;
      gmtoff = 0L;
      return bool;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("String is too short: \"");
    localStringBuilder.append(paramString);
    localStringBuilder.append("\" Expected at least 8 characters.");
    throw new TimeFormatException(localStringBuilder.toString());
  }
  
  public boolean after(Time paramTime)
  {
    boolean bool;
    if (compare(this, paramTime) > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean before(Time paramTime)
  {
    boolean bool;
    if (compare(this, paramTime) < 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void clear(String paramString)
  {
    if (paramString != null)
    {
      timezone = paramString;
      allDay = false;
      second = 0;
      minute = 0;
      hour = 0;
      monthDay = 0;
      month = 0;
      year = 0;
      weekDay = 0;
      yearDay = 0;
      gmtoff = 0L;
      isDst = -1;
      return;
    }
    throw new NullPointerException("timezone is null!");
  }
  
  public String format(String paramString)
  {
    calculator.copyFieldsFromTime(this);
    return calculator.format(paramString);
  }
  
  public String format2445()
  {
    calculator.copyFieldsFromTime(this);
    return calculator.format2445(allDay ^ true);
  }
  
  public String format3339(boolean paramBoolean)
  {
    if (paramBoolean) {
      return format("%Y-%m-%d");
    }
    if ("UTC".equals(timezone)) {
      return format("%Y-%m-%dT%H:%M:%S.000Z");
    }
    String str1 = format("%Y-%m-%dT%H:%M:%S.000");
    String str2;
    if (gmtoff < 0L) {
      str2 = "-";
    } else {
      str2 = "+";
    }
    int i = (int)Math.abs(gmtoff);
    int j = i % 3600 / 60;
    i /= 3600;
    return String.format(Locale.US, "%s%s%02d:%02d", new Object[] { str1, str2, Integer.valueOf(i), Integer.valueOf(j) });
  }
  
  public int getActualMaximum(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bad field=");
      localStringBuilder.append(paramInt);
      throw new RuntimeException(localStringBuilder.toString());
    case 9: 
      throw new RuntimeException("WEEK_NUM not implemented");
    case 8: 
      paramInt = year;
      if ((paramInt % 4 == 0) && ((paramInt % 100 != 0) || (paramInt % 400 == 0))) {
        paramInt = 365;
      } else {
        paramInt = 364;
      }
      return paramInt;
    case 7: 
      return 6;
    case 6: 
      return 2037;
    case 5: 
      return 11;
    case 4: 
      paramInt = DAYS_PER_MONTH[month];
      int i = 28;
      if (paramInt != 28) {
        return paramInt;
      }
      int j = year;
      paramInt = i;
      if (j % 4 == 0) {
        if (j % 100 == 0)
        {
          paramInt = i;
          if (j % 400 != 0) {}
        }
        else
        {
          paramInt = 29;
        }
      }
      return paramInt;
    case 3: 
      return 23;
    case 2: 
      return 59;
    }
    return 59;
  }
  
  public int getWeekNumber()
  {
    int i = yearDay + sThursdayOffset[weekDay];
    if ((i >= 0) && (i <= 364)) {
      return i / 7 + 1;
    }
    Time localTime = new Time(this);
    monthDay += sThursdayOffset[weekDay];
    localTime.normalize(true);
    return yearDay / 7 + 1;
  }
  
  public long normalize(boolean paramBoolean)
  {
    calculator.copyFieldsFromTime(this);
    long l = calculator.toMillis(paramBoolean);
    calculator.copyFieldsToTime(this);
    return l;
  }
  
  public boolean parse(String paramString)
  {
    if (paramString != null)
    {
      if (parseInternal(paramString))
      {
        timezone = "UTC";
        return true;
      }
      return false;
    }
    throw new NullPointerException("time string is null");
  }
  
  public boolean parse3339(String paramString)
  {
    if (paramString != null)
    {
      if (parse3339Internal(paramString))
      {
        timezone = "UTC";
        return true;
      }
      return false;
    }
    throw new NullPointerException("time string is null");
  }
  
  public void set(int paramInt1, int paramInt2, int paramInt3)
  {
    allDay = true;
    second = 0;
    minute = 0;
    hour = 0;
    monthDay = paramInt1;
    month = paramInt2;
    year = paramInt3;
    weekDay = 0;
    yearDay = 0;
    isDst = -1;
    gmtoff = 0L;
  }
  
  public void set(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    allDay = false;
    second = paramInt1;
    minute = paramInt2;
    hour = paramInt3;
    monthDay = paramInt4;
    month = paramInt5;
    year = paramInt6;
    weekDay = 0;
    yearDay = 0;
    isDst = -1;
    gmtoff = 0L;
  }
  
  public void set(long paramLong)
  {
    allDay = false;
    calculator.timezone = timezone;
    calculator.setTimeInMillis(paramLong);
    calculator.copyFieldsToTime(this);
  }
  
  public void set(Time paramTime)
  {
    timezone = timezone;
    allDay = allDay;
    second = second;
    minute = minute;
    hour = hour;
    monthDay = monthDay;
    month = month;
    year = year;
    weekDay = weekDay;
    yearDay = yearDay;
    isDst = isDst;
    gmtoff = gmtoff;
  }
  
  public long setJulianDay(int paramInt)
  {
    long l = (paramInt - 2440588) * 86400000L;
    set(l);
    int i = getJulianDay(l, gmtoff);
    monthDay += paramInt - i;
    hour = 0;
    minute = 0;
    second = 0;
    return normalize(true);
  }
  
  public void setToNow()
  {
    set(System.currentTimeMillis());
  }
  
  public void switchTimezone(String paramString)
  {
    calculator.copyFieldsFromTime(this);
    calculator.switchTimeZone(paramString);
    calculator.copyFieldsToTime(this);
    timezone = paramString;
  }
  
  public long toMillis(boolean paramBoolean)
  {
    calculator.copyFieldsFromTime(this);
    return calculator.toMillis(paramBoolean);
  }
  
  public String toString()
  {
    TimeCalculator localTimeCalculator = new TimeCalculator(timezone);
    localTimeCalculator.copyFieldsFromTime(this);
    return localTimeCalculator.toStringInternal();
  }
  
  private static class TimeCalculator
  {
    public String timezone;
    public final ZoneInfo.WallTime wallTime;
    private ZoneInfo zoneInfo;
    
    public TimeCalculator(String paramString)
    {
      zoneInfo = lookupZoneInfo(paramString);
      wallTime = new ZoneInfo.WallTime();
    }
    
    public static int compare(TimeCalculator paramTimeCalculator1, TimeCalculator paramTimeCalculator2)
    {
      boolean bool = timezone.equals(timezone);
      int i = 0;
      if (bool)
      {
        i = wallTime.getYear() - wallTime.getYear();
        if (i != 0) {
          return i;
        }
        i = wallTime.getMonth() - wallTime.getMonth();
        if (i != 0) {
          return i;
        }
        i = wallTime.getMonthDay() - wallTime.getMonthDay();
        if (i != 0) {
          return i;
        }
        i = wallTime.getHour() - wallTime.getHour();
        if (i != 0) {
          return i;
        }
        i = wallTime.getMinute() - wallTime.getMinute();
        if (i != 0) {
          return i;
        }
        i = wallTime.getSecond() - wallTime.getSecond();
        if (i != 0) {
          return i;
        }
        return 0;
      }
      long l = paramTimeCalculator1.toMillis(false) - paramTimeCalculator2.toMillis(false);
      if (l < 0L) {
        i = -1;
      } else if (l > 0L) {
        i = 1;
      }
      return i;
    }
    
    private static ZoneInfo lookupZoneInfo(String paramString)
    {
      try
      {
        localObject1 = ZoneInfoDB.getInstance().makeTimeZone(paramString);
        Object localObject2 = localObject1;
        if (localObject1 == null) {
          localObject2 = ZoneInfoDB.getInstance().makeTimeZone("GMT");
        }
        if (localObject2 != null) {
          return localObject2;
        }
        localObject2 = new java/lang/AssertionError;
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("GMT not found: \"");
        ((StringBuilder)localObject1).append(paramString);
        ((StringBuilder)localObject1).append("\"");
        ((AssertionError)localObject2).<init>(((StringBuilder)localObject1).toString());
        throw ((Throwable)localObject2);
      }
      catch (IOException localIOException)
      {
        Object localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Error loading timezone: \"");
        ((StringBuilder)localObject1).append(paramString);
        ((StringBuilder)localObject1).append("\"");
        throw new AssertionError(((StringBuilder)localObject1).toString(), localIOException);
      }
    }
    
    private char toChar(int paramInt)
    {
      int i;
      if ((paramInt >= 0) && (paramInt <= 9))
      {
        paramInt = (char)(paramInt + 48);
        i = paramInt;
      }
      else
      {
        paramInt = 32;
        i = paramInt;
      }
      return i;
    }
    
    private void updateZoneInfoFromTimeZone()
    {
      if (!zoneInfo.getID().equals(timezone)) {
        zoneInfo = lookupZoneInfo(timezone);
      }
    }
    
    public void copyFieldsFromTime(Time paramTime)
    {
      wallTime.setSecond(second);
      wallTime.setMinute(minute);
      wallTime.setHour(hour);
      wallTime.setMonthDay(monthDay);
      wallTime.setMonth(month);
      wallTime.setYear(year);
      wallTime.setWeekDay(weekDay);
      wallTime.setYearDay(yearDay);
      wallTime.setIsDst(isDst);
      wallTime.setGmtOffset((int)gmtoff);
      if ((allDay) && ((second != 0) || (minute != 0) || (hour != 0))) {
        throw new IllegalArgumentException("allDay is true but sec, min, hour are not 0.");
      }
      timezone = timezone;
      updateZoneInfoFromTimeZone();
    }
    
    public void copyFieldsToTime(Time paramTime)
    {
      second = wallTime.getSecond();
      minute = wallTime.getMinute();
      hour = wallTime.getHour();
      monthDay = wallTime.getMonthDay();
      month = wallTime.getMonth();
      year = wallTime.getYear();
      weekDay = wallTime.getWeekDay();
      yearDay = wallTime.getYearDay();
      isDst = wallTime.getIsDst();
      gmtoff = wallTime.getGmtOffset();
    }
    
    public String format(String paramString)
    {
      String str = paramString;
      if (paramString == null) {
        str = "%c";
      }
      return new TimeFormatter().format(str, wallTime, zoneInfo);
    }
    
    public String format2445(boolean paramBoolean)
    {
      if (paramBoolean) {
        i = 16;
      } else {
        i = 8;
      }
      char[] arrayOfChar = new char[i];
      int i = wallTime.getYear();
      arrayOfChar[0] = toChar(i / 1000);
      i %= 1000;
      arrayOfChar[1] = toChar(i / 100);
      i %= 100;
      arrayOfChar[2] = toChar(i / 10);
      arrayOfChar[3] = toChar(i % 10);
      i = wallTime.getMonth() + 1;
      arrayOfChar[4] = toChar(i / 10);
      arrayOfChar[5] = toChar(i % 10);
      i = wallTime.getMonthDay();
      arrayOfChar[6] = toChar(i / 10);
      arrayOfChar[7] = toChar(i % 10);
      if (!paramBoolean) {
        return new String(arrayOfChar, 0, 8);
      }
      arrayOfChar[8] = ((char)84);
      i = wallTime.getHour();
      arrayOfChar[9] = toChar(i / 10);
      arrayOfChar[10] = toChar(i % 10);
      i = wallTime.getMinute();
      arrayOfChar[11] = toChar(i / 10);
      arrayOfChar[12] = toChar(i % 10);
      i = wallTime.getSecond();
      arrayOfChar[13] = toChar(i / 10);
      arrayOfChar[14] = toChar(i % 10);
      if ("UTC".equals(timezone))
      {
        arrayOfChar[15] = ((char)90);
        return new String(arrayOfChar, 0, 16);
      }
      return new String(arrayOfChar, 0, 15);
    }
    
    public void setTimeInMillis(long paramLong)
    {
      int i = (int)(paramLong / 1000L);
      updateZoneInfoFromTimeZone();
      wallTime.localtime(i, zoneInfo);
    }
    
    public void switchTimeZone(String paramString)
    {
      int i = wallTime.mktime(zoneInfo);
      timezone = paramString;
      updateZoneInfoFromTimeZone();
      wallTime.localtime(i, zoneInfo);
    }
    
    public long toMillis(boolean paramBoolean)
    {
      if (paramBoolean) {
        wallTime.setIsDst(-1);
      }
      int i = wallTime.mktime(zoneInfo);
      if (i == -1) {
        return -1L;
      }
      return i * 1000L;
    }
    
    public String toStringInternal()
    {
      return String.format("%04d%02d%02dT%02d%02d%02d%s(%d,%d,%d,%d,%d)", new Object[] { Integer.valueOf(wallTime.getYear()), Integer.valueOf(wallTime.getMonth() + 1), Integer.valueOf(wallTime.getMonthDay()), Integer.valueOf(wallTime.getHour()), Integer.valueOf(wallTime.getMinute()), Integer.valueOf(wallTime.getSecond()), timezone, Integer.valueOf(wallTime.getWeekDay()), Integer.valueOf(wallTime.getYearDay()), Integer.valueOf(wallTime.getGmtOffset()), Integer.valueOf(wallTime.getIsDst()), Long.valueOf(toMillis(false) / 1000L) });
    }
  }
}
