package com.android.internal.util;

public final class GrowingArrayUtils
{
  private GrowingArrayUtils() {}
  
  public static float[] append(float[] paramArrayOfFloat, int paramInt, float paramFloat)
  {
    float[] arrayOfFloat = paramArrayOfFloat;
    if (paramInt + 1 > paramArrayOfFloat.length)
    {
      arrayOfFloat = ArrayUtils.newUnpaddedFloatArray(growSize(paramInt));
      System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, paramInt);
    }
    arrayOfFloat[paramInt] = paramFloat;
    return arrayOfFloat;
  }
  
  public static int[] append(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = paramArrayOfInt;
    if (paramInt1 + 1 > paramArrayOfInt.length)
    {
      arrayOfInt = ArrayUtils.newUnpaddedIntArray(growSize(paramInt1));
      System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, paramInt1);
    }
    arrayOfInt[paramInt1] = paramInt2;
    return arrayOfInt;
  }
  
  public static long[] append(long[] paramArrayOfLong, int paramInt, long paramLong)
  {
    long[] arrayOfLong = paramArrayOfLong;
    if (paramInt + 1 > paramArrayOfLong.length)
    {
      arrayOfLong = ArrayUtils.newUnpaddedLongArray(growSize(paramInt));
      System.arraycopy(paramArrayOfLong, 0, arrayOfLong, 0, paramInt);
    }
    arrayOfLong[paramInt] = paramLong;
    return arrayOfLong;
  }
  
  public static <T> T[] append(T[] paramArrayOfT, int paramInt, T paramT)
  {
    Object localObject = paramArrayOfT;
    if (paramInt + 1 > paramArrayOfT.length)
    {
      localObject = ArrayUtils.newUnpaddedArray(paramArrayOfT.getClass().getComponentType(), growSize(paramInt));
      System.arraycopy(paramArrayOfT, 0, localObject, 0, paramInt);
    }
    localObject[paramInt] = paramT;
    return localObject;
  }
  
  public static boolean[] append(boolean[] paramArrayOfBoolean, int paramInt, boolean paramBoolean)
  {
    boolean[] arrayOfBoolean = paramArrayOfBoolean;
    if (paramInt + 1 > paramArrayOfBoolean.length)
    {
      arrayOfBoolean = ArrayUtils.newUnpaddedBooleanArray(growSize(paramInt));
      System.arraycopy(paramArrayOfBoolean, 0, arrayOfBoolean, 0, paramInt);
    }
    arrayOfBoolean[paramInt] = paramBoolean;
    return arrayOfBoolean;
  }
  
  public static int growSize(int paramInt)
  {
    if (paramInt <= 4) {
      paramInt = 8;
    } else {
      paramInt *= 2;
    }
    return paramInt;
  }
  
  public static int[] insert(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 + 1 <= paramArrayOfInt.length)
    {
      System.arraycopy(paramArrayOfInt, paramInt2, paramArrayOfInt, paramInt2 + 1, paramInt1 - paramInt2);
      paramArrayOfInt[paramInt2] = paramInt3;
      return paramArrayOfInt;
    }
    int[] arrayOfInt = ArrayUtils.newUnpaddedIntArray(growSize(paramInt1));
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, paramInt2);
    arrayOfInt[paramInt2] = paramInt3;
    System.arraycopy(paramArrayOfInt, paramInt2, arrayOfInt, paramInt2 + 1, paramArrayOfInt.length - paramInt2);
    return arrayOfInt;
  }
  
  public static long[] insert(long[] paramArrayOfLong, int paramInt1, int paramInt2, long paramLong)
  {
    if (paramInt1 + 1 <= paramArrayOfLong.length)
    {
      System.arraycopy(paramArrayOfLong, paramInt2, paramArrayOfLong, paramInt2 + 1, paramInt1 - paramInt2);
      paramArrayOfLong[paramInt2] = paramLong;
      return paramArrayOfLong;
    }
    long[] arrayOfLong = ArrayUtils.newUnpaddedLongArray(growSize(paramInt1));
    System.arraycopy(paramArrayOfLong, 0, arrayOfLong, 0, paramInt2);
    arrayOfLong[paramInt2] = paramLong;
    System.arraycopy(paramArrayOfLong, paramInt2, arrayOfLong, paramInt2 + 1, paramArrayOfLong.length - paramInt2);
    return arrayOfLong;
  }
  
  public static <T> T[] insert(T[] paramArrayOfT, int paramInt1, int paramInt2, T paramT)
  {
    if (paramInt1 + 1 <= paramArrayOfT.length)
    {
      System.arraycopy(paramArrayOfT, paramInt2, paramArrayOfT, paramInt2 + 1, paramInt1 - paramInt2);
      paramArrayOfT[paramInt2] = paramT;
      return paramArrayOfT;
    }
    Object[] arrayOfObject = ArrayUtils.newUnpaddedArray(paramArrayOfT.getClass().getComponentType(), growSize(paramInt1));
    System.arraycopy(paramArrayOfT, 0, arrayOfObject, 0, paramInt2);
    arrayOfObject[paramInt2] = paramT;
    System.arraycopy(paramArrayOfT, paramInt2, arrayOfObject, paramInt2 + 1, paramArrayOfT.length - paramInt2);
    return arrayOfObject;
  }
  
  public static boolean[] insert(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramInt1 + 1 <= paramArrayOfBoolean.length)
    {
      System.arraycopy(paramArrayOfBoolean, paramInt2, paramArrayOfBoolean, paramInt2 + 1, paramInt1 - paramInt2);
      paramArrayOfBoolean[paramInt2] = paramBoolean;
      return paramArrayOfBoolean;
    }
    boolean[] arrayOfBoolean = ArrayUtils.newUnpaddedBooleanArray(growSize(paramInt1));
    System.arraycopy(paramArrayOfBoolean, 0, arrayOfBoolean, 0, paramInt2);
    arrayOfBoolean[paramInt2] = paramBoolean;
    System.arraycopy(paramArrayOfBoolean, paramInt2, arrayOfBoolean, paramInt2 + 1, paramArrayOfBoolean.length - paramInt2);
    return arrayOfBoolean;
  }
}