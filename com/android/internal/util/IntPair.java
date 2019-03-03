package com.android.internal.util;

public class IntPair
{
  private IntPair() {}
  
  public static int first(long paramLong)
  {
    return (int)(paramLong >> 32);
  }
  
  public static long of(int paramInt1, int paramInt2)
  {
    return paramInt1 << 32 | paramInt2 & 0xFFFFFFFF;
  }
  
  public static int second(long paramLong)
  {
    return (int)paramLong;
  }
}
