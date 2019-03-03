package com.android.internal.util;

public class FastMath
{
  public FastMath() {}
  
  public static int round(float paramFloat)
  {
    return (int)(8388608L + (1.6777216E7F * paramFloat) >> 24);
  }
}
