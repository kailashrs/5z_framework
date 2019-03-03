package android.text.format;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings.System;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import libcore.icu.ICU;
import libcore.icu.LocaleData;

public class DateFormat
{
  @Deprecated
  public static final char AM_PM = 'a';
  @Deprecated
  public static final char CAPITAL_AM_PM = 'A';
  @Deprecated
  public static final char DATE = 'd';
  @Deprecated
  public static final char DAY = 'E';
  @Deprecated
  public static final char HOUR = 'h';
  @Deprecated
  public static final char HOUR_OF_DAY = 'k';
  @Deprecated
  public static final char MINUTE = 'm';
  @Deprecated
  public static final char MONTH = 'M';
  @Deprecated
  public static final char QUOTE = '\'';
  @Deprecated
  public static final char SECONDS = 's';
  @Deprecated
  public static final char STANDALONE_MONTH = 'L';
  @Deprecated
  public static final char TIME_ZONE = 'z';
  @Deprecated
  public static final char YEAR = 'y';
  private static boolean sIs24Hour;
  private static Locale sIs24HourLocale;
  private static final Object sLocaleLock = new Object();
  
  public DateFormat() {}
  
  public static int appendQuotedText(SpannableStringBuilder paramSpannableStringBuilder, int paramInt)
  {
    int i = paramSpannableStringBuilder.length();
    if ((paramInt + 1 < i) && (paramSpannableStringBuilder.charAt(paramInt + 1) == '\''))
    {
      paramSpannableStringBuilder.delete(paramInt, paramInt + 1);
      return 1;
    }
    int j = 0;
    paramSpannableStringBuilder.delete(paramInt, paramInt + 1);
    i--;
    while (paramInt < i) {
      if (paramSpannableStringBuilder.charAt(paramInt) == '\'')
      {
        if ((paramInt + 1 < i) && (paramSpannableStringBuilder.charAt(paramInt + 1) == '\''))
        {
          paramSpannableStringBuilder.delete(paramInt, paramInt + 1);
          i--;
          j++;
          paramInt++;
        }
        else
        {
          paramSpannableStringBuilder.delete(paramInt, paramInt + 1);
          break;
        }
      }
      else
      {
        paramInt++;
        j++;
      }
    }
    return j;
  }
  
  public static CharSequence format(CharSequence paramCharSequence, long paramLong)
  {
    return format(paramCharSequence, new Date(paramLong));
  }
  
  public static CharSequence format(CharSequence paramCharSequence, Calendar paramCalendar)
  {
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramCharSequence);
    LocaleData localLocaleData = LocaleData.get(Locale.getDefault());
    int i = paramCharSequence.length();
    int j = 0;
    while (j < i)
    {
      int k = 1;
      int m = localSpannableStringBuilder.charAt(j);
      int n;
      if (m == 39)
      {
        n = appendQuotedText(localSpannableStringBuilder, j);
        i = localSpannableStringBuilder.length();
      }
      else
      {
        while ((j + k < i) && (localSpannableStringBuilder.charAt(j + k) == m)) {
          k++;
        }
        String str;
        switch (m)
        {
        default: 
          str = null;
          break;
        case 122: 
          str = getTimeZoneString(paramCalendar, k);
          break;
        case 121: 
          str = getYearString(paramCalendar.get(1), k);
          break;
        case 115: 
          str = zeroPad(paramCalendar.get(13), k);
          break;
        case 109: 
          str = zeroPad(paramCalendar.get(12), k);
          break;
        case 100: 
          str = zeroPad(paramCalendar.get(5), k);
          break;
        case 76: 
        case 77: 
          str = getMonthString(localLocaleData, paramCalendar.get(2), k, m);
          break;
        case 75: 
        case 104: 
          int i1 = paramCalendar.get(10);
          n = i1;
          if (m == 104)
          {
            n = i1;
            if (i1 == 0) {
              n = 12;
            }
          }
          str = zeroPad(n, k);
          break;
        case 72: 
        case 107: 
          str = zeroPad(paramCalendar.get(11), k);
          break;
        case 69: 
        case 99: 
          str = getDayOfWeekString(localLocaleData, paramCalendar.get(7), k, m);
          break;
        case 65: 
        case 97: 
          str = amPm[(paramCalendar.get(9) - 0)];
        }
        n = k;
        if (str != null)
        {
          localSpannableStringBuilder.replace(j, j + k, str);
          n = str.length();
          i = localSpannableStringBuilder.length();
        }
      }
      j += n;
    }
    if ((paramCharSequence instanceof Spanned)) {
      return new SpannedString(localSpannableStringBuilder);
    }
    return localSpannableStringBuilder.toString();
  }
  
  public static CharSequence format(CharSequence paramCharSequence, Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(paramDate);
    return format(paramCharSequence, localGregorianCalendar);
  }
  
  private static String formatZoneOffset(int paramInt1, int paramInt2)
  {
    paramInt1 /= 1000;
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramInt1 < 0)
    {
      localStringBuilder.insert(0, "-");
      paramInt1 = -paramInt1;
    }
    else
    {
      localStringBuilder.insert(0, "+");
    }
    paramInt2 = paramInt1 / 3600;
    paramInt1 = paramInt1 % 3600 / 60;
    localStringBuilder.append(zeroPad(paramInt2, 2));
    localStringBuilder.append(zeroPad(paramInt1, 2));
    return localStringBuilder.toString();
  }
  
  public static String getBestDateTimePattern(Locale paramLocale, String paramString)
  {
    return ICU.getBestDateTimePattern(paramString, paramLocale);
  }
  
  public static java.text.DateFormat getDateFormat(Context paramContext)
  {
    return java.text.DateFormat.getDateInstance(3, getResourcesgetConfigurationlocale);
  }
  
  public static char[] getDateFormatOrder(Context paramContext)
  {
    return ICU.getDateFormatOrder(getDateFormatString(paramContext));
  }
  
  private static String getDateFormatString(Context paramContext)
  {
    paramContext = java.text.DateFormat.getDateInstance(3, getResourcesgetConfigurationlocale);
    if ((paramContext instanceof SimpleDateFormat)) {
      return ((SimpleDateFormat)paramContext).toPattern();
    }
    throw new AssertionError("!(df instanceof SimpleDateFormat)");
  }
  
  private static String getDayOfWeekString(LocaleData paramLocaleData, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 == 99) {
      paramInt3 = 1;
    } else {
      paramInt3 = 0;
    }
    if (paramInt2 == 5)
    {
      if (paramInt3 != 0) {
        paramLocaleData = tinyStandAloneWeekdayNames[paramInt1];
      } else {
        paramLocaleData = tinyWeekdayNames[paramInt1];
      }
      return paramLocaleData;
    }
    if (paramInt2 == 4)
    {
      if (paramInt3 != 0) {
        paramLocaleData = longStandAloneWeekdayNames[paramInt1];
      } else {
        paramLocaleData = longWeekdayNames[paramInt1];
      }
      return paramLocaleData;
    }
    if (paramInt3 != 0) {
      paramLocaleData = shortStandAloneWeekdayNames[paramInt1];
    } else {
      paramLocaleData = shortWeekdayNames[paramInt1];
    }
    return paramLocaleData;
  }
  
  public static java.text.DateFormat getLongDateFormat(Context paramContext)
  {
    return java.text.DateFormat.getDateInstance(1, getResourcesgetConfigurationlocale);
  }
  
  public static java.text.DateFormat getMediumDateFormat(Context paramContext)
  {
    return java.text.DateFormat.getDateInstance(2, getResourcesgetConfigurationlocale);
  }
  
  private static String getMonthString(LocaleData paramLocaleData, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 == 76) {
      paramInt3 = 1;
    } else {
      paramInt3 = 0;
    }
    if (paramInt2 == 5)
    {
      if (paramInt3 != 0) {
        paramLocaleData = tinyStandAloneMonthNames[paramInt1];
      } else {
        paramLocaleData = tinyMonthNames[paramInt1];
      }
      return paramLocaleData;
    }
    if (paramInt2 == 4)
    {
      if (paramInt3 != 0) {
        paramLocaleData = longStandAloneMonthNames[paramInt1];
      } else {
        paramLocaleData = longMonthNames[paramInt1];
      }
      return paramLocaleData;
    }
    if (paramInt2 == 3)
    {
      if (paramInt3 != 0) {
        paramLocaleData = shortStandAloneMonthNames[paramInt1];
      } else {
        paramLocaleData = shortMonthNames[paramInt1];
      }
      return paramLocaleData;
    }
    return zeroPad(paramInt1 + 1, paramInt2);
  }
  
  public static java.text.DateFormat getTimeFormat(Context paramContext)
  {
    Locale localLocale = getResourcesgetConfigurationlocale;
    return new SimpleDateFormat(getTimeFormatString(paramContext), localLocale);
  }
  
  public static String getTimeFormatString(Context paramContext)
  {
    return getTimeFormatString(paramContext, paramContext.getUserId());
  }
  
  public static String getTimeFormatString(Context paramContext, int paramInt)
  {
    LocaleData localLocaleData = LocaleData.get(getResourcesgetConfigurationlocale);
    if (is24HourFormat(paramContext, paramInt)) {
      paramContext = timeFormat_Hm;
    } else {
      paramContext = timeFormat_hm;
    }
    return paramContext;
  }
  
  private static String getTimeZoneString(Calendar paramCalendar, int paramInt)
  {
    TimeZone localTimeZone = paramCalendar.getTimeZone();
    if (paramInt < 2) {
      return formatZoneOffset(paramCalendar.get(16) + paramCalendar.get(15), paramInt);
    }
    boolean bool;
    if (paramCalendar.get(16) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return localTimeZone.getDisplayName(bool, 0);
  }
  
  private static String getYearString(int paramInt1, int paramInt2)
  {
    String str;
    if (paramInt2 <= 2) {
      str = zeroPad(paramInt1 % 100, 2);
    } else {
      str = String.format(Locale.getDefault(), "%d", new Object[] { Integer.valueOf(paramInt1) });
    }
    return str;
  }
  
  public static boolean hasDesignator(CharSequence paramCharSequence, char paramChar)
  {
    if (paramCharSequence == null) {
      return false;
    }
    int i = paramCharSequence.length();
    int j = 0;
    int k = 0;
    while (k < i)
    {
      char c = paramCharSequence.charAt(k);
      int m = 1;
      if (c == '\'')
      {
        if (j == 0) {
          j = m;
        } else {
          j = 0;
        }
        m = j;
      }
      else
      {
        m = j;
        if (j == 0)
        {
          m = j;
          if (c == paramChar) {
            return true;
          }
        }
      }
      k++;
      j = m;
    }
    return false;
  }
  
  public static boolean hasSeconds(CharSequence paramCharSequence)
  {
    return hasDesignator(paramCharSequence, 's');
  }
  
  public static boolean is24HourFormat(Context paramContext)
  {
    return is24HourFormat(paramContext, paramContext.getUserId());
  }
  
  public static boolean is24HourFormat(Context paramContext, int paramInt)
  {
    String str = Settings.System.getStringForUser(paramContext.getContentResolver(), "time_12_24", paramInt);
    if (str != null) {
      return str.equals("24");
    }
    return is24HourLocale(getResourcesgetConfigurationlocale);
  }
  
  public static boolean is24HourLocale(Locale paramLocale)
  {
    synchronized (sLocaleLock)
    {
      boolean bool;
      if ((sIs24HourLocale != null) && (sIs24HourLocale.equals(paramLocale)))
      {
        bool = sIs24Hour;
        return bool;
      }
      ??? = java.text.DateFormat.getTimeInstance(1, paramLocale);
      if ((??? instanceof SimpleDateFormat)) {
        bool = hasDesignator(((SimpleDateFormat)???).toPattern(), 'H');
      } else {
        bool = false;
      }
      synchronized (sLocaleLock)
      {
        sIs24HourLocale = paramLocale;
        sIs24Hour = bool;
        return bool;
      }
    }
  }
  
  private static String zeroPad(int paramInt1, int paramInt2)
  {
    Locale localLocale = Locale.getDefault();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("%0");
    localStringBuilder.append(paramInt2);
    localStringBuilder.append("d");
    return String.format(localLocale, localStringBuilder.toString(), new Object[] { Integer.valueOf(paramInt1) });
  }
}
