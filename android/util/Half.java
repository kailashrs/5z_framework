package android.util;

import sun.misc.FloatingDecimal;

public final class Half
  extends Number
  implements Comparable<Half>
{
  public static final short EPSILON = 5120;
  private static final int FP16_COMBINED = 32767;
  private static final int FP16_EXPONENT_BIAS = 15;
  private static final int FP16_EXPONENT_MASK = 31;
  private static final int FP16_EXPONENT_MAX = 31744;
  private static final int FP16_EXPONENT_SHIFT = 10;
  private static final int FP16_SIGNIFICAND_MASK = 1023;
  private static final int FP16_SIGN_MASK = 32768;
  private static final int FP16_SIGN_SHIFT = 15;
  private static final float FP32_DENORMAL_FLOAT = Float.intBitsToFloat(1056964608);
  private static final int FP32_DENORMAL_MAGIC = 1056964608;
  private static final int FP32_EXPONENT_BIAS = 127;
  private static final int FP32_EXPONENT_MASK = 255;
  private static final int FP32_EXPONENT_SHIFT = 23;
  private static final int FP32_SIGNIFICAND_MASK = 8388607;
  private static final int FP32_SIGN_SHIFT = 31;
  public static final short LOWEST_VALUE = -1025;
  public static final int MAX_EXPONENT = 15;
  public static final short MAX_VALUE = 31743;
  public static final int MIN_EXPONENT = -14;
  public static final short MIN_NORMAL = 1024;
  public static final short MIN_VALUE = 1;
  public static final short NEGATIVE_INFINITY = -1024;
  public static final short NEGATIVE_ZERO = -32768;
  public static final short NaN = 32256;
  public static final short POSITIVE_INFINITY = 31744;
  public static final short POSITIVE_ZERO = 0;
  public static final int SIZE = 16;
  private final short mValue;
  
  public Half(double paramDouble)
  {
    mValue = toHalf((float)paramDouble);
  }
  
  public Half(float paramFloat)
  {
    mValue = toHalf(paramFloat);
  }
  
  public Half(String paramString)
    throws NumberFormatException
  {
    mValue = toHalf(Float.parseFloat(paramString));
  }
  
  public Half(short paramShort)
  {
    mValue = ((short)paramShort);
  }
  
  public static short abs(short paramShort)
  {
    return (short)(paramShort & 0x7FFF);
  }
  
  public static short ceil(short paramShort)
  {
    short s1 = 0xFFFF & paramShort;
    int i = s1 & 0x7FFF;
    short s2 = s1;
    paramShort = 1;
    if (i < 15360)
    {
      if (i == 0) {
        paramShort = 0;
      }
      paramShort = s2 & 0x8000 | 0x3C00 & -(paramShort & s1 >> 15);
    }
    else
    {
      paramShort = s2;
      if (i < 25600)
      {
        paramShort = (1 << 25 - (i >> 10)) - 1;
        paramShort = s2 + (paramShort & (s1 >> 15) - 1) & paramShort;
      }
    }
    return (short)paramShort;
  }
  
  public static int compare(short paramShort1, short paramShort2)
  {
    boolean bool = less(paramShort1, paramShort2);
    int i = -1;
    if (bool) {
      return -1;
    }
    if (greater(paramShort1, paramShort2)) {
      return 1;
    }
    int j = 32256;
    int k;
    if ((paramShort1 & 0x7FFF) > 31744) {
      k = 32256;
    } else {
      k = paramShort1;
    }
    if ((paramShort2 & 0x7FFF) <= 31744) {
      j = paramShort2;
    }
    if (k == j) {
      k = 0;
    } else if (k < j) {
      k = i;
    } else {
      k = 1;
    }
    return k;
  }
  
  public static short copySign(short paramShort1, short paramShort2)
  {
    return (short)(0x8000 & paramShort2 | paramShort1 & 0x7FFF);
  }
  
  public static boolean equals(short paramShort1, short paramShort2)
  {
    boolean bool = false;
    if ((paramShort1 & 0x7FFF) > 31744) {
      return false;
    }
    if ((paramShort2 & 0x7FFF) > 31744) {
      return false;
    }
    if ((paramShort1 != paramShort2) && (((paramShort1 | paramShort2) & 0x7FFF) != 0)) {
      return bool;
    }
    bool = true;
    return bool;
  }
  
  public static short floor(short paramShort)
  {
    short s1 = 65535;
    short s2 = paramShort & 0xFFFF;
    int i = s2 & 0x7FFF;
    short s3 = s2;
    if (i < 15360)
    {
      if (s2 > 32768) {
        paramShort = s1;
      } else {
        paramShort = 0;
      }
      paramShort = s3 & 0x8000 | paramShort & 0x3C00;
    }
    else
    {
      paramShort = s3;
      if (i < 25600)
      {
        paramShort = (1 << 25 - (i >> 10)) - 1;
        paramShort = s3 + (-(s2 >> 15) & paramShort) & paramShort;
      }
    }
    return (short)paramShort;
  }
  
  public static int getExponent(short paramShort)
  {
    return (paramShort >>> 10 & 0x1F) - 15;
  }
  
  public static int getSign(short paramShort)
  {
    if ((0x8000 & paramShort) == 0) {
      paramShort = 1;
    } else {
      paramShort = -1;
    }
    return paramShort;
  }
  
  public static int getSignificand(short paramShort)
  {
    return paramShort & 0x3FF;
  }
  
  public static boolean greater(short paramShort1, short paramShort2)
  {
    boolean bool = false;
    if ((paramShort1 & 0x7FFF) > 31744) {
      return false;
    }
    if ((paramShort2 & 0x7FFF) > 31744) {
      return false;
    }
    if ((paramShort1 & 0x8000) != 0) {
      paramShort1 = 32768 - (paramShort1 & 0xFFFF);
    } else {
      paramShort1 &= 0xFFFF;
    }
    if ((paramShort2 & 0x8000) != 0) {
      paramShort2 = 32768 - (0xFFFF & paramShort2);
    } else {
      paramShort2 &= 0xFFFF;
    }
    if (paramShort1 > paramShort2) {
      bool = true;
    }
    return bool;
  }
  
  public static boolean greaterEquals(short paramShort1, short paramShort2)
  {
    boolean bool = false;
    if ((paramShort1 & 0x7FFF) > 31744) {
      return false;
    }
    if ((paramShort2 & 0x7FFF) > 31744) {
      return false;
    }
    if ((paramShort1 & 0x8000) != 0) {
      paramShort1 = 32768 - (paramShort1 & 0xFFFF);
    } else {
      paramShort1 &= 0xFFFF;
    }
    if ((paramShort2 & 0x8000) != 0) {
      paramShort2 = 32768 - (0xFFFF & paramShort2);
    } else {
      paramShort2 &= 0xFFFF;
    }
    if (paramShort1 >= paramShort2) {
      bool = true;
    }
    return bool;
  }
  
  public static int halfToIntBits(short paramShort)
  {
    if ((paramShort & 0x7FFF) > 31744) {
      paramShort = 32256;
    } else {
      paramShort = 0xFFFF & paramShort;
    }
    return paramShort;
  }
  
  public static int halfToRawIntBits(short paramShort)
  {
    return 0xFFFF & paramShort;
  }
  
  public static short halfToShortBits(short paramShort)
  {
    short s;
    if ((paramShort & 0x7FFF) > 31744)
    {
      paramShort = 32256;
      s = paramShort;
    }
    else
    {
      s = paramShort;
    }
    return s;
  }
  
  public static int hashCode(short paramShort)
  {
    return halfToIntBits(paramShort);
  }
  
  public static short intBitsToHalf(int paramInt)
  {
    return (short)(0xFFFF & paramInt);
  }
  
  public static boolean isInfinite(short paramShort)
  {
    boolean bool;
    if ((paramShort & 0x7FFF) == 31744) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isNaN(short paramShort)
  {
    boolean bool;
    if ((paramShort & 0x7FFF) > 31744) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isNormalized(short paramShort)
  {
    boolean bool;
    if (((paramShort & 0x7C00) != 0) && ((paramShort & 0x7C00) != 31744)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean less(short paramShort1, short paramShort2)
  {
    boolean bool = false;
    if ((paramShort1 & 0x7FFF) > 31744) {
      return false;
    }
    if ((paramShort2 & 0x7FFF) > 31744) {
      return false;
    }
    if ((paramShort1 & 0x8000) != 0) {
      paramShort1 = 32768 - (paramShort1 & 0xFFFF);
    } else {
      paramShort1 &= 0xFFFF;
    }
    if ((paramShort2 & 0x8000) != 0) {
      paramShort2 = 32768 - (0xFFFF & paramShort2);
    } else {
      paramShort2 &= 0xFFFF;
    }
    if (paramShort1 < paramShort2) {
      bool = true;
    }
    return bool;
  }
  
  public static boolean lessEquals(short paramShort1, short paramShort2)
  {
    boolean bool = false;
    if ((paramShort1 & 0x7FFF) > 31744) {
      return false;
    }
    if ((paramShort2 & 0x7FFF) > 31744) {
      return false;
    }
    if ((paramShort1 & 0x8000) != 0) {
      paramShort1 = 32768 - (paramShort1 & 0xFFFF);
    } else {
      paramShort1 &= 0xFFFF;
    }
    if ((paramShort2 & 0x8000) != 0) {
      paramShort2 = 32768 - (0xFFFF & paramShort2);
    } else {
      paramShort2 &= 0xFFFF;
    }
    if (paramShort1 <= paramShort2) {
      bool = true;
    }
    return bool;
  }
  
  public static short max(short paramShort1, short paramShort2)
  {
    if ((paramShort1 & 0x7FFF) > 31744) {
      return 32256;
    }
    if ((paramShort2 & 0x7FFF) > 31744) {
      return 32256;
    }
    short s;
    if (((paramShort1 & 0x7FFF) == 0) && ((paramShort2 & 0x7FFF) == 0))
    {
      if ((paramShort1 & 0x8000) != 0) {
        s = paramShort2;
      } else {
        s = paramShort1;
      }
      return s;
    }
    int i;
    if ((paramShort1 & 0x8000) != 0) {
      i = 32768 - (paramShort1 & 0xFFFF);
    } else {
      i = paramShort1 & 0xFFFF;
    }
    int j;
    if ((paramShort2 & 0x8000) != 0) {
      j = 32768 - (0xFFFF & paramShort2);
    } else {
      j = paramShort2 & 0xFFFF;
    }
    if (i > j) {
      s = paramShort1;
    } else {
      s = paramShort2;
    }
    return s;
  }
  
  public static short min(short paramShort1, short paramShort2)
  {
    if ((paramShort1 & 0x7FFF) > 31744) {
      return 32256;
    }
    if ((paramShort2 & 0x7FFF) > 31744) {
      return 32256;
    }
    short s;
    if (((paramShort1 & 0x7FFF) == 0) && ((paramShort2 & 0x7FFF) == 0))
    {
      if ((paramShort1 & 0x8000) != 0) {
        s = paramShort1;
      } else {
        s = paramShort2;
      }
      return s;
    }
    int i;
    if ((paramShort1 & 0x8000) != 0) {
      i = 32768 - (paramShort1 & 0xFFFF);
    } else {
      i = paramShort1 & 0xFFFF;
    }
    int j;
    if ((paramShort2 & 0x8000) != 0) {
      j = 32768 - (0xFFFF & paramShort2);
    } else {
      j = paramShort2 & 0xFFFF;
    }
    if (i < j) {
      s = paramShort1;
    } else {
      s = paramShort2;
    }
    return s;
  }
  
  public static short parseHalf(String paramString)
    throws NumberFormatException
  {
    return toHalf(FloatingDecimal.parseFloat(paramString));
  }
  
  public static short round(short paramShort)
  {
    short s1 = 65535;
    short s2 = paramShort & 0xFFFF;
    int i = s2 & 0x7FFF;
    if (i < 15360)
    {
      if (i >= 14336) {
        paramShort = s1;
      } else {
        paramShort = 0;
      }
      paramShort = s2 & 0x8000 | paramShort & 0x3C00;
    }
    else
    {
      paramShort = s2;
      if (i < 25600)
      {
        paramShort = 25 - (i >> 10);
        paramShort = s2 + (1 << paramShort - 1) & (1 << paramShort) - 1;
      }
    }
    return (short)paramShort;
  }
  
  public static float toFloat(short paramShort)
  {
    paramShort = 0xFFFF & paramShort;
    int i = 0x8000 & paramShort;
    int j = paramShort >>> 10 & 0x1F;
    int k = paramShort & 0x3FF;
    paramShort = 0;
    int m = 0;
    if (j == 0)
    {
      if (k != 0)
      {
        float f = Float.intBitsToFloat(1056964608 + k) - FP32_DENORMAL_FLOAT;
        if (i != 0) {
          f = -f;
        }
        return f;
      }
    }
    else
    {
      m = k << 13;
      if (j == 31) {
        paramShort = 255;
      } else {
        paramShort = j - 15 + 127;
      }
    }
    return Float.intBitsToFloat(i << 16 | paramShort << 23 | m);
  }
  
  public static short toHalf(float paramFloat)
  {
    int i = Float.floatToRawIntBits(paramFloat);
    int j = i >>> 31;
    int k = i >>> 23 & 0xFF;
    int m = 0x7FFFFF & i;
    i = 0;
    int n = 0;
    if (k == 255)
    {
      n = 31;
      if (m != 0) {
        k = 512;
      } else {
        k = 0;
      }
      i = k;
      k = n;
    }
    else
    {
      k = k - 127 + 15;
      if (k >= 31)
      {
        k = 49;
        i = n;
      }
      else if (k <= 0)
      {
        if (k < -10)
        {
          k = i;
          i = n;
        }
        else
        {
          n = (0x800000 | m) >> 1 - k;
          k = n;
          if ((n & 0x1000) != 0) {
            k = n + 8192;
          }
          n = k >> 13;
          k = i;
          i = n;
        }
      }
      else
      {
        n = k;
        int i1 = m >> 13;
        k = n;
        i = i1;
        if ((m & 0x1000) != 0) {
          return (short)(j << 15 | (n << 10 | i1) + 1);
        }
      }
    }
    return (short)(j << 15 | k << 10 | i);
  }
  
  public static String toHexString(short paramShort)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0xFFFF & paramShort;
    paramShort = i >>> 15;
    int j = i >>> 10 & 0x1F;
    i &= 0x3FF;
    if (j == 31)
    {
      if (i == 0)
      {
        if (paramShort != 0) {
          localStringBuilder.append('-');
        }
        localStringBuilder.append("Infinity");
      }
      else
      {
        localStringBuilder.append("NaN");
      }
    }
    else
    {
      if (paramShort == 1) {
        localStringBuilder.append('-');
      }
      if (j == 0)
      {
        if (i == 0)
        {
          localStringBuilder.append("0x0.0p0");
        }
        else
        {
          localStringBuilder.append("0x0.");
          localStringBuilder.append(Integer.toHexString(i).replaceFirst("0{2,}$", ""));
          localStringBuilder.append("p-14");
        }
      }
      else
      {
        localStringBuilder.append("0x1.");
        localStringBuilder.append(Integer.toHexString(i).replaceFirst("0{2,}$", ""));
        localStringBuilder.append('p');
        localStringBuilder.append(Integer.toString(j - 15));
      }
    }
    return localStringBuilder.toString();
  }
  
  public static String toString(short paramShort)
  {
    return Float.toString(toFloat(paramShort));
  }
  
  public static short trunc(short paramShort)
  {
    short s = 0xFFFF & paramShort;
    int i = s & 0x7FFF;
    if (i < 15360)
    {
      paramShort = s & 0x8000;
    }
    else
    {
      paramShort = s;
      if (i < 25600) {
        paramShort = s & (1 << 25 - (i >> 10)) - 1;
      }
    }
    return (short)paramShort;
  }
  
  public static Half valueOf(float paramFloat)
  {
    return new Half(paramFloat);
  }
  
  public static Half valueOf(String paramString)
  {
    return new Half(paramString);
  }
  
  public static Half valueOf(short paramShort)
  {
    return new Half(paramShort);
  }
  
  public byte byteValue()
  {
    return (byte)(int)toFloat(mValue);
  }
  
  public int compareTo(Half paramHalf)
  {
    return compare(mValue, mValue);
  }
  
  public double doubleValue()
  {
    return toFloat(mValue);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (((paramObject instanceof Half)) && (halfToIntBits(mValue) == halfToIntBits(mValue))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public float floatValue()
  {
    return toFloat(mValue);
  }
  
  public short halfValue()
  {
    return mValue;
  }
  
  public int hashCode()
  {
    return hashCode(mValue);
  }
  
  public int intValue()
  {
    return (int)toFloat(mValue);
  }
  
  public boolean isNaN()
  {
    return isNaN(mValue);
  }
  
  public long longValue()
  {
    return toFloat(mValue);
  }
  
  public short shortValue()
  {
    return (short)(int)toFloat(mValue);
  }
  
  public String toString()
  {
    return toString(mValue);
  }
}
