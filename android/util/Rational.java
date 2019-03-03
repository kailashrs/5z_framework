package android.util;

import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;

public final class Rational
  extends Number
  implements Comparable<Rational>
{
  public static final Rational NEGATIVE_INFINITY = new Rational(-1, 0);
  public static final Rational NaN = new Rational(0, 0);
  public static final Rational POSITIVE_INFINITY = new Rational(1, 0);
  public static final Rational ZERO = new Rational(0, 1);
  private static final long serialVersionUID = 1L;
  private final int mDenominator;
  private final int mNumerator;
  
  public Rational(int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = paramInt2;
    if (paramInt2 < 0)
    {
      i = -paramInt1;
      j = -paramInt2;
    }
    if ((j == 0) && (i > 0))
    {
      mNumerator = 1;
      mDenominator = 0;
    }
    else if ((j == 0) && (i < 0))
    {
      mNumerator = -1;
      mDenominator = 0;
    }
    else if ((j == 0) && (i == 0))
    {
      mNumerator = 0;
      mDenominator = 0;
    }
    else if (i == 0)
    {
      mNumerator = 0;
      mDenominator = 1;
    }
    else
    {
      paramInt1 = gcd(i, j);
      mNumerator = (i / paramInt1);
      mDenominator = (j / paramInt1);
    }
  }
  
  private boolean equals(Rational paramRational)
  {
    boolean bool;
    if ((mNumerator == mNumerator) && (mDenominator == mDenominator)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static int gcd(int paramInt1, int paramInt2)
  {
    int i = paramInt2;
    paramInt2 = paramInt1;
    for (paramInt1 = i; paramInt1 != 0; paramInt1 = i)
    {
      i = paramInt2 % paramInt1;
      paramInt2 = paramInt1;
    }
    return Math.abs(paramInt2);
  }
  
  private static NumberFormatException invalidRational(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid Rational: \"");
    localStringBuilder.append(paramString);
    localStringBuilder.append("\"");
    throw new NumberFormatException(localStringBuilder.toString());
  }
  
  private boolean isNegInf()
  {
    boolean bool;
    if ((mDenominator == 0) && (mNumerator < 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isPosInf()
  {
    boolean bool;
    if ((mDenominator == 0) && (mNumerator > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static Rational parseRational(String paramString)
    throws NumberFormatException
  {
    Preconditions.checkNotNull(paramString, "string must not be null");
    if (paramString.equals("NaN")) {
      return NaN;
    }
    if (paramString.equals("Infinity")) {
      return POSITIVE_INFINITY;
    }
    if (paramString.equals("-Infinity")) {
      return NEGATIVE_INFINITY;
    }
    int i = paramString.indexOf(':');
    int j = i;
    if (i < 0) {
      j = paramString.indexOf('/');
    }
    if (j >= 0) {
      try
      {
        Rational localRational = new Rational(Integer.parseInt(paramString.substring(0, j)), Integer.parseInt(paramString.substring(j + 1)));
        return localRational;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw invalidRational(paramString);
      }
    }
    throw invalidRational(paramString);
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    if (mNumerator == 0)
    {
      if ((mDenominator != 1) && (mDenominator != 0)) {
        throw new InvalidObjectException("Rational must be deserialized from a reduced form for zero values");
      }
      return;
    }
    if (mDenominator == 0)
    {
      if ((mNumerator != 1) && (mNumerator != -1)) {
        throw new InvalidObjectException("Rational must be deserialized from a reduced form for infinity values");
      }
      return;
    }
    if (gcd(mNumerator, mDenominator) <= 1) {
      return;
    }
    throw new InvalidObjectException("Rational must be deserialized from a reduced form for finite values");
  }
  
  public int compareTo(Rational paramRational)
  {
    Preconditions.checkNotNull(paramRational, "another must not be null");
    if (equals(paramRational)) {
      return 0;
    }
    if (isNaN()) {
      return 1;
    }
    if (paramRational.isNaN()) {
      return -1;
    }
    if ((!isPosInf()) && (!paramRational.isNegInf()))
    {
      if ((!isNegInf()) && (!paramRational.isPosInf()))
      {
        long l1 = mNumerator * mDenominator;
        long l2 = mNumerator * mDenominator;
        if (l1 < l2) {
          return -1;
        }
        if (l1 > l2) {
          return 1;
        }
        return 0;
      }
      return -1;
    }
    return 1;
  }
  
  public double doubleValue()
  {
    return mNumerator / mDenominator;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (((paramObject instanceof Rational)) && (equals((Rational)paramObject))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public float floatValue()
  {
    return mNumerator / mDenominator;
  }
  
  public int getDenominator()
  {
    return mDenominator;
  }
  
  public int getNumerator()
  {
    return mNumerator;
  }
  
  public int hashCode()
  {
    int i = mNumerator;
    int j = mNumerator;
    return mDenominator ^ (i << 16 | j >>> 16);
  }
  
  public int intValue()
  {
    if (isPosInf()) {
      return Integer.MAX_VALUE;
    }
    if (isNegInf()) {
      return Integer.MIN_VALUE;
    }
    if (isNaN()) {
      return 0;
    }
    return mNumerator / mDenominator;
  }
  
  public boolean isFinite()
  {
    boolean bool;
    if (mDenominator != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isInfinite()
  {
    boolean bool;
    if ((mNumerator != 0) && (mDenominator == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isNaN()
  {
    boolean bool;
    if ((mDenominator == 0) && (mNumerator == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isZero()
  {
    boolean bool;
    if ((isFinite()) && (mNumerator == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public long longValue()
  {
    if (isPosInf()) {
      return Long.MAX_VALUE;
    }
    if (isNegInf()) {
      return Long.MIN_VALUE;
    }
    if (isNaN()) {
      return 0L;
    }
    return mNumerator / mDenominator;
  }
  
  public short shortValue()
  {
    return (short)intValue();
  }
  
  public float toFloat()
  {
    return floatValue();
  }
  
  public String toString()
  {
    if (isNaN()) {
      return "NaN";
    }
    if (isPosInf()) {
      return "Infinity";
    }
    if (isNegInf()) {
      return "-Infinity";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mNumerator);
    localStringBuilder.append("/");
    localStringBuilder.append(mDenominator);
    return localStringBuilder.toString();
  }
}
