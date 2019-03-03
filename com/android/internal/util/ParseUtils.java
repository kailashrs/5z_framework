package com.android.internal.util;

public final class ParseUtils
{
  private ParseUtils() {}
  
  public static boolean parseBoolean(String paramString, boolean paramBoolean)
  {
    boolean bool1 = "true".equals(paramString);
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if ("false".equals(paramString)) {
      return false;
    }
    if (parseInt(paramString, paramBoolean) == 0) {
      bool2 = false;
    }
    return bool2;
  }
  
  public static double parseDouble(String paramString, double paramDouble)
  {
    if (paramString == null) {
      return paramDouble;
    }
    try
    {
      double d = Double.parseDouble(paramString);
      return d;
    }
    catch (NumberFormatException paramString) {}
    return paramDouble;
  }
  
  public static float parseFloat(String paramString, float paramFloat)
  {
    if (paramString == null) {
      return paramFloat;
    }
    try
    {
      float f = Float.parseFloat(paramString);
      return f;
    }
    catch (NumberFormatException paramString) {}
    return paramFloat;
  }
  
  public static int parseInt(String paramString, int paramInt)
  {
    return parseIntWithBase(paramString, 10, paramInt);
  }
  
  public static int parseIntWithBase(String paramString, int paramInt1, int paramInt2)
  {
    if (paramString == null) {
      return paramInt2;
    }
    try
    {
      paramInt1 = Integer.parseInt(paramString, paramInt1);
      return paramInt1;
    }
    catch (NumberFormatException paramString) {}
    return paramInt2;
  }
  
  public static long parseLong(String paramString, long paramLong)
  {
    return parseLongWithBase(paramString, 10, paramLong);
  }
  
  public static long parseLongWithBase(String paramString, int paramInt, long paramLong)
  {
    if (paramString == null) {
      return paramLong;
    }
    try
    {
      long l = Long.parseLong(paramString, paramInt);
      return l;
    }
    catch (NumberFormatException paramString) {}
    return paramLong;
  }
}
