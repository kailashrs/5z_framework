package android.text.format;

import android.content.res.Resources;
import java.nio.CharBuffer;
import java.util.Formatter;
import java.util.Locale;
import libcore.icu.LocaleData;
import libcore.util.ZoneInfo;
import libcore.util.ZoneInfo.WallTime;

class TimeFormatter
{
  private static final int DAYSPERLYEAR = 366;
  private static final int DAYSPERNYEAR = 365;
  private static final int DAYSPERWEEK = 7;
  private static final int FORCE_LOWER_CASE = -1;
  private static final int HOURSPERDAY = 24;
  private static final int MINSPERHOUR = 60;
  private static final int MONSPERYEAR = 12;
  private static final int SECSPERMIN = 60;
  private static String sDateOnlyFormat;
  private static String sDateTimeFormat;
  private static Locale sLocale;
  private static LocaleData sLocaleData;
  private static String sTimeOnlyFormat;
  private final String dateOnlyFormat;
  private final String dateTimeFormat;
  private final LocaleData localeData;
  private Formatter numberFormatter;
  private StringBuilder outputBuilder;
  private final String timeOnlyFormat;
  
  public TimeFormatter()
  {
    try
    {
      Object localObject1 = Locale.getDefault();
      if ((sLocale == null) || (!((Locale)localObject1).equals(sLocale)))
      {
        sLocale = (Locale)localObject1;
        sLocaleData = LocaleData.get((Locale)localObject1);
        localObject1 = Resources.getSystem();
        sTimeOnlyFormat = ((Resources)localObject1).getString(17041110);
        sDateOnlyFormat = ((Resources)localObject1).getString(17040454);
        sDateTimeFormat = ((Resources)localObject1).getString(17039842);
      }
      dateTimeFormat = sDateTimeFormat;
      timeOnlyFormat = sTimeOnlyFormat;
      dateOnlyFormat = sDateOnlyFormat;
      localeData = sLocaleData;
      return;
    }
    finally {}
  }
  
  private static boolean brokenIsLower(char paramChar)
  {
    boolean bool;
    if ((paramChar >= 'a') && (paramChar <= 'z')) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean brokenIsUpper(char paramChar)
  {
    boolean bool;
    if ((paramChar >= 'A') && (paramChar <= 'Z')) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static char brokenToLower(char paramChar)
  {
    if ((paramChar >= 'A') && (paramChar <= 'Z')) {
      return (char)(paramChar - 'A' + 97);
    }
    return paramChar;
  }
  
  private static char brokenToUpper(char paramChar)
  {
    if ((paramChar >= 'a') && (paramChar <= 'z')) {
      return (char)(paramChar - 'a' + 65);
    }
    return paramChar;
  }
  
  private void formatInternal(String paramString, ZoneInfo.WallTime paramWallTime, ZoneInfo paramZoneInfo)
  {
    paramString = CharBuffer.wrap(paramString);
    while (paramString.remaining() > 0)
    {
      boolean bool = true;
      if (paramString.get(paramString.position()) == '%') {
        bool = handleToken(paramString, paramWallTime, paramZoneInfo);
      }
      if (bool) {
        outputBuilder.append(paramString.get(paramString.position()));
      }
      paramString.position(paramString.position() + 1);
    }
  }
  
  private static String getFormat(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (paramInt != 45)
    {
      if (paramInt != 48)
      {
        if (paramInt != 95) {
          return paramString1;
        }
        return paramString2;
      }
      return paramString4;
    }
    return paramString3;
  }
  
  private boolean handleToken(CharBuffer paramCharBuffer, ZoneInfo.WallTime paramWallTime, ZoneInfo paramZoneInfo)
  {
    int i = 0;
    for (;;)
    {
      int j = paramCharBuffer.remaining();
      boolean bool = true;
      if (j <= 1) {
        break;
      }
      paramCharBuffer.position(paramCharBuffer.position() + 1);
      int k = paramCharBuffer.get(paramCharBuffer.position());
      int m = 7;
      j = 12;
      switch (k)
      {
      default: 
        switch (k)
        {
        default: 
          switch (k)
          {
          default: 
            switch (k)
            {
            default: 
              switch (k)
              {
              default: 
                switch (k)
                {
                default: 
                  switch (k)
                  {
                  default: 
                    switch (k)
                    {
                    default: 
                      switch (k)
                      {
                      default: 
                        return true;
                      case 112: 
                        if (paramWallTime.getHour() >= 12) {
                          paramCharBuffer = localeData.amPm[1];
                        } else {
                          paramCharBuffer = localeData.amPm[0];
                        }
                        modifyAndAppend(paramCharBuffer, i);
                        return false;
                      case 77: 
                        numberFormatter.format(getFormat(i, "%02d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf(paramWallTime.getMinute()) });
                        return false;
                      case 43: 
                        formatInternal("%a %b %e %H:%M:%S %Z %Y", paramWallTime, paramZoneInfo);
                        return false;
                      }
                    case 122: 
                      if (paramWallTime.getIsDst() < 0) {
                        return false;
                      }
                      j = paramWallTime.getGmtOffset();
                      int n;
                      if (j < 0)
                      {
                        m = 45;
                        j = -j;
                        n = m;
                      }
                      else
                      {
                        m = 43;
                        n = m;
                      }
                      outputBuilder.append(n);
                      m = j / 60;
                      j = m / 60;
                      numberFormatter.format(getFormat(i, "%04d", "%4d", "%d", "%04d"), new Object[] { Integer.valueOf(j * 100 + m % 60) });
                      return false;
                    case 121: 
                      outputYear(paramWallTime.getYear(), false, true, i);
                      return false;
                    case 120: 
                      formatInternal(dateOnlyFormat, paramWallTime, paramZoneInfo);
                      return false;
                    case 119: 
                      numberFormatter.format("%d", new Object[] { Integer.valueOf(paramWallTime.getWeekDay()) });
                      return false;
                    case 118: 
                      formatInternal("%e-%b-%Y", paramWallTime, paramZoneInfo);
                      return false;
                    case 117: 
                      if (paramWallTime.getWeekDay() == 0) {
                        j = m;
                      } else {
                        j = paramWallTime.getWeekDay();
                      }
                      numberFormatter.format("%d", new Object[] { Integer.valueOf(j) });
                      return false;
                    case 116: 
                      outputBuilder.append('\t');
                      return false;
                    case 115: 
                      j = paramWallTime.mktime(paramZoneInfo);
                      outputBuilder.append(Integer.toString(j));
                      return false;
                    }
                    formatInternal("%I:%M:%S %p", paramWallTime, paramZoneInfo);
                    return false;
                  case 110: 
                    outputBuilder.append('\n');
                    return false;
                  case 109: 
                    numberFormatter.format(getFormat(i, "%02d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf(paramWallTime.getMonth() + 1) });
                    return false;
                  case 108: 
                    if (paramWallTime.getHour() % 12 != 0) {
                      j = paramWallTime.getHour() % 12;
                    }
                    numberFormatter.format(getFormat(i, "%2d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf(j) });
                    return false;
                  case 107: 
                    numberFormatter.format(getFormat(i, "%2d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf(paramWallTime.getHour()) });
                    return false;
                  }
                  j = paramWallTime.getYearDay();
                  numberFormatter.format(getFormat(i, "%03d", "%3d", "%d", "%03d"), new Object[] { Integer.valueOf(j + 1) });
                  return false;
                }
              case 101: 
                numberFormatter.format(getFormat(i, "%2d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf(paramWallTime.getMonthDay()) });
                return false;
              case 100: 
                numberFormatter.format(getFormat(i, "%02d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf(paramWallTime.getMonthDay()) });
                return false;
              case 99: 
                formatInternal(dateTimeFormat, paramWallTime, paramZoneInfo);
                return false;
              case 98: 
                if ((paramWallTime.getMonth() >= 0) && (paramWallTime.getMonth() < 12)) {
                  paramCharBuffer = localeData.shortMonthNames[paramWallTime.getMonth()];
                } else {
                  paramCharBuffer = "?";
                }
                modifyAndAppend(paramCharBuffer, i);
                return false;
              }
              if ((paramWallTime.getWeekDay() >= 0) && (paramWallTime.getWeekDay() < 7)) {
                paramCharBuffer = localeData.shortWeekdayNames[(paramWallTime.getWeekDay() + 1)];
              } else {
                paramCharBuffer = "?";
              }
              modifyAndAppend(paramCharBuffer, i);
              return false;
            }
            i = k;
            break;
          case 90: 
            if (paramWallTime.getIsDst() < 0) {
              return false;
            }
            if (paramWallTime.getIsDst() == 0) {
              bool = false;
            }
            modifyAndAppend(paramZoneInfo.getDisplayName(bool, 0), i);
            return false;
          case 89: 
            outputYear(paramWallTime.getYear(), true, true, i);
            return false;
          case 88: 
            formatInternal(timeOnlyFormat, paramWallTime, paramZoneInfo);
            return false;
          case 87: 
            m = paramWallTime.getYearDay();
            if (paramWallTime.getWeekDay() != 0) {
              j = paramWallTime.getWeekDay() - 1;
            } else {
              j = 6;
            }
            j = (m + 7 - j) / 7;
            numberFormatter.format(getFormat(i, "%02d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf(j) });
            return false;
          case 85: 
            numberFormatter.format(getFormat(i, "%02d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf((paramWallTime.getYearDay() + 7 - paramWallTime.getWeekDay()) / 7) });
            return false;
          case 84: 
            formatInternal("%H:%M:%S", paramWallTime, paramZoneInfo);
            return false;
          case 83: 
            numberFormatter.format(getFormat(i, "%02d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf(paramWallTime.getSecond()) });
            return false;
          case 82: 
            formatInternal("%H:%M", paramWallTime, paramZoneInfo);
            return false;
          }
          break;
        case 80: 
          if (paramWallTime.getHour() >= 12) {
            paramCharBuffer = localeData.amPm[1];
          } else {
            paramCharBuffer = localeData.amPm[0];
          }
          modifyAndAppend(paramCharBuffer, -1);
          return false;
        }
        break;
      case 73: 
        if (paramWallTime.getHour() % 12 != 0) {
          j = paramWallTime.getHour() % 12;
        }
        numberFormatter.format(getFormat(i, "%02d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf(j) });
        return false;
      case 72: 
        numberFormatter.format(getFormat(i, "%02d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf(paramWallTime.getHour()) });
        return false;
      case 71: 
        j = paramWallTime.getYear();
        m = paramWallTime.getYearDay();
        int i1 = paramWallTime.getWeekDay();
        for (;;)
        {
          if (isLeap(j)) {
            i2 = 366;
          } else {
            i2 = 365;
          }
          int i3 = (m + 11 - i1) % 7 - 3;
          int i4 = i3 - i2 % 7;
          int i5 = i4;
          if (i4 < -3) {
            i5 = i4 + 7;
          }
          if (m >= i5 + i2)
          {
            j++;
            m = 1;
          }
          else
          {
            if (m < i3) {
              break label1716;
            }
            m = 1 + (m - i3) / 7;
          }
          if (k == 86)
          {
            paramWallTime = numberFormatter;
            paramCharBuffer = getFormat(i, "%02d", "%2d", "%d", "%02d");
            bool = false;
            paramWallTime.format(paramCharBuffer, new Object[] { Integer.valueOf(m) });
          }
          else
          {
            bool = false;
            if (k == 103) {
              outputYear(j, bool, true, i);
            } else {
              outputYear(j, true, true, i);
            }
          }
          return bool;
          int i2 = j - 1;
          if (isLeap(i2)) {
            j = 366;
          } else {
            j = 365;
          }
          m += j;
          j = i2;
        }
      case 70: 
        label1716:
        formatInternal("%Y-%m-%d", paramWallTime, paramZoneInfo);
        return false;
      }
    }
    formatInternal("%m/%d/%y", paramWallTime, paramZoneInfo);
    return false;
    outputYear(paramWallTime.getYear(), true, false, i);
    return false;
    if (i == 45)
    {
      if ((paramWallTime.getMonth() >= 0) && (paramWallTime.getMonth() < 12)) {
        paramCharBuffer = localeData.longStandAloneMonthNames[paramWallTime.getMonth()];
      } else {
        paramCharBuffer = "?";
      }
      modifyAndAppend(paramCharBuffer, i);
    }
    else
    {
      if ((paramWallTime.getMonth() >= 0) && (paramWallTime.getMonth() < 12)) {
        paramCharBuffer = localeData.longMonthNames[paramWallTime.getMonth()];
      } else {
        paramCharBuffer = "?";
      }
      modifyAndAppend(paramCharBuffer, i);
    }
    return false;
    if ((paramWallTime.getWeekDay() >= 0) && (paramWallTime.getWeekDay() < 7)) {
      paramCharBuffer = localeData.longWeekdayNames[(paramWallTime.getWeekDay() + 1)];
    } else {
      paramCharBuffer = "?";
    }
    modifyAndAppend(paramCharBuffer, i);
    return false;
    return true;
  }
  
  private static boolean isLeap(int paramInt)
  {
    boolean bool;
    if ((paramInt % 4 == 0) && ((paramInt % 100 != 0) || (paramInt % 400 == 0))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private String localizeDigits(String paramString)
  {
    int i = paramString.length();
    int j = localeData.zeroDigit;
    StringBuilder localStringBuilder = new StringBuilder(i);
    for (int k = 0; k < i; k++)
    {
      int m = paramString.charAt(k);
      int n = m;
      if (m >= 48)
      {
        n = m;
        if (m <= 57)
        {
          m = (char)(m + (j - 48));
          n = m;
        }
      }
      localStringBuilder.append(n);
    }
    return localStringBuilder.toString();
  }
  
  private void modifyAndAppend(CharSequence paramCharSequence, int paramInt)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    if (paramInt != -1)
    {
      if (paramInt != 35)
      {
        if (paramInt != 94) {
          outputBuilder.append(paramCharSequence);
        } else {
          for (paramInt = k; paramInt < paramCharSequence.length(); paramInt++) {
            outputBuilder.append(brokenToUpper(paramCharSequence.charAt(paramInt)));
          }
        }
      }
      else {
        for (paramInt = i; paramInt < paramCharSequence.length(); paramInt++)
        {
          char c1 = paramCharSequence.charAt(paramInt);
          char c2;
          if (brokenIsUpper(c1))
          {
            k = brokenToLower(c1);
            c2 = k;
          }
          else
          {
            c2 = c1;
            if (brokenIsLower(c1))
            {
              k = brokenToUpper(c1);
              c2 = k;
            }
          }
          outputBuilder.append(c2);
        }
      }
    }
    else {
      for (paramInt = j; paramInt < paramCharSequence.length(); paramInt++) {
        outputBuilder.append(brokenToLower(paramCharSequence.charAt(paramInt)));
      }
    }
  }
  
  private void outputYear(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
  {
    int i = paramInt1 % 100;
    int j = paramInt1 / 100 + i / 100;
    int k = i % 100;
    if ((k < 0) && (j > 0))
    {
      paramInt1 = k + 100;
      i = j - 1;
    }
    else
    {
      paramInt1 = k;
      i = j;
      if (j < 0)
      {
        paramInt1 = k;
        i = j;
        if (k > 0)
        {
          paramInt1 = k - 100;
          i = j + 1;
        }
      }
    }
    if (paramBoolean1) {
      if ((i == 0) && (paramInt1 < 0)) {
        outputBuilder.append("-0");
      } else {
        numberFormatter.format(getFormat(paramInt2, "%02d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf(i) });
      }
    }
    if (paramBoolean2)
    {
      if (paramInt1 < 0) {
        paramInt1 = -paramInt1;
      }
      numberFormatter.format(getFormat(paramInt2, "%02d", "%2d", "%d", "%02d"), new Object[] { Integer.valueOf(paramInt1) });
    }
  }
  
  public String format(String paramString, ZoneInfo.WallTime paramWallTime, ZoneInfo paramZoneInfo)
  {
    try
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      outputBuilder = localStringBuilder;
      Formatter localFormatter = new java/util/Formatter;
      localFormatter.<init>(localStringBuilder, Locale.US);
      numberFormatter = localFormatter;
      formatInternal(paramString, paramWallTime, paramZoneInfo);
      paramWallTime = localStringBuilder.toString();
      paramString = paramWallTime;
      if (localeData.zeroDigit != '0') {
        paramString = localizeDigits(paramWallTime);
      }
      return paramString;
    }
    finally
    {
      outputBuilder = null;
      numberFormatter = null;
    }
  }
}
