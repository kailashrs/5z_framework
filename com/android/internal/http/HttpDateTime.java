package com.android.internal.http;

import android.text.format.Time;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HttpDateTime
{
  private static final Pattern HTTP_DATE_ANSIC_PATTERN = Pattern.compile("[ ]([A-Za-z]{3,9})[ ]+([0-9]{1,2})[ ]([0-9]{1,2}:[0-9][0-9]:[0-9][0-9])[ ]([0-9]{2,4})");
  private static final String HTTP_DATE_ANSIC_REGEXP = "[ ]([A-Za-z]{3,9})[ ]+([0-9]{1,2})[ ]([0-9]{1,2}:[0-9][0-9]:[0-9][0-9])[ ]([0-9]{2,4})";
  private static final Pattern HTTP_DATE_RFC_PATTERN = Pattern.compile("([0-9]{1,2})[- ]([A-Za-z]{3,9})[- ]([0-9]{2,4})[ ]([0-9]{1,2}:[0-9][0-9]:[0-9][0-9])");
  private static final String HTTP_DATE_RFC_REGEXP = "([0-9]{1,2})[- ]([A-Za-z]{3,9})[- ]([0-9]{2,4})[ ]([0-9]{1,2}:[0-9][0-9]:[0-9][0-9])";
  
  public HttpDateTime() {}
  
  private static int getDate(String paramString)
  {
    if (paramString.length() == 2) {
      return (paramString.charAt(0) - '0') * 10 + (paramString.charAt(1) - '0');
    }
    return paramString.charAt(0) - '0';
  }
  
  private static int getMonth(String paramString)
  {
    int i = Character.toLowerCase(paramString.charAt(0)) + Character.toLowerCase(paramString.charAt(1)) + Character.toLowerCase(paramString.charAt(2)) - 291;
    if (i != 22)
    {
      if (i != 26)
      {
        if (i != 29)
        {
          if (i != 32)
          {
            if (i != 40)
            {
              if (i != 42)
              {
                if (i != 48)
                {
                  switch (i)
                  {
                  default: 
                    switch (i)
                    {
                    default: 
                      throw new IllegalArgumentException();
                    case 37: 
                      return 8;
                    case 36: 
                      return 4;
                    }
                    return 9;
                  case 10: 
                    return 1;
                  }
                  return 11;
                }
                return 10;
              }
              return 5;
            }
            return 6;
          }
          return 3;
        }
        return 2;
      }
      return 7;
    }
    return 0;
  }
  
  private static TimeOfDay getTime(String paramString)
  {
    int i = 0 + 1;
    int j = paramString.charAt(0) - '0';
    int k = j;
    int m = i;
    if (paramString.charAt(i) != ':')
    {
      k = j * 10 + (paramString.charAt(i) - '0');
      m = i + 1;
    }
    m++;
    j = m + 1;
    i = paramString.charAt(m);
    m = paramString.charAt(j);
    int n = j + 1 + 1;
    j = n + 1;
    return new TimeOfDay(k, (i - 48) * 10 + (m - 48), (paramString.charAt(n) - '0') * 10 + (paramString.charAt(j) - '0'));
  }
  
  private static int getYear(String paramString)
  {
    if (paramString.length() == 2)
    {
      int i = (paramString.charAt(0) - '0') * 10 + (paramString.charAt(1) - '0');
      if (i >= 70) {
        return i + 1900;
      }
      return i + 2000;
    }
    if (paramString.length() == 3) {
      return (paramString.charAt(0) - '0') * 100 + (paramString.charAt(1) - '0') * 10 + (paramString.charAt(2) - '0') + 1900;
    }
    if (paramString.length() == 4) {
      return (paramString.charAt(0) - '0') * 1000 + (paramString.charAt(1) - '0') * 100 + (paramString.charAt(2) - '0') * 10 + (paramString.charAt(3) - '0');
    }
    return 1970;
  }
  
  public static long parse(String paramString)
    throws IllegalArgumentException
  {
    Object localObject = HTTP_DATE_RFC_PATTERN.matcher(paramString);
    int k;
    if (((Matcher)localObject).find())
    {
      i = getDate(((Matcher)localObject).group(1));
      j = getMonth(((Matcher)localObject).group(2));
      k = getYear(((Matcher)localObject).group(3));
      paramString = getTime(((Matcher)localObject).group(4));
    }
    else
    {
      localObject = HTTP_DATE_ANSIC_PATTERN.matcher(paramString);
      if (!((Matcher)localObject).find()) {
        break label168;
      }
      j = getMonth(((Matcher)localObject).group(1));
      i = getDate(((Matcher)localObject).group(2));
      paramString = getTime(((Matcher)localObject).group(3));
      k = getYear(((Matcher)localObject).group(4));
    }
    int m = i;
    int i = j;
    int j = k;
    if (k >= 2038)
    {
      j = 2038;
      i = 0;
      m = 1;
    }
    localObject = new Time("UTC");
    ((Time)localObject).set(second, minute, hour, m, i, j);
    return ((Time)localObject).toMillis(false);
    label168:
    throw new IllegalArgumentException();
  }
  
  private static class TimeOfDay
  {
    int hour;
    int minute;
    int second;
    
    TimeOfDay(int paramInt1, int paramInt2, int paramInt3)
    {
      hour = paramInt1;
      minute = paramInt2;
      second = paramInt3;
    }
  }
}
