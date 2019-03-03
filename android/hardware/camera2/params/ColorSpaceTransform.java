package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import android.util.Rational;
import com.android.internal.util.Preconditions;
import java.util.Arrays;

public final class ColorSpaceTransform
{
  private static final int COLUMNS = 3;
  private static final int COUNT = 9;
  private static final int COUNT_INT = 18;
  private static final int OFFSET_DENOMINATOR = 1;
  private static final int OFFSET_NUMERATOR = 0;
  private static final int RATIONAL_SIZE = 2;
  private static final int ROWS = 3;
  private final int[] mElements;
  
  public ColorSpaceTransform(int[] paramArrayOfInt)
  {
    Preconditions.checkNotNull(paramArrayOfInt, "elements must not be null");
    if (paramArrayOfInt.length == 18)
    {
      for (int i = 0; i < paramArrayOfInt.length; i++)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("element ");
        localStringBuilder.append(i);
        localStringBuilder.append(" must not be null");
        Preconditions.checkNotNull(paramArrayOfInt, localStringBuilder.toString());
      }
      mElements = Arrays.copyOf(paramArrayOfInt, paramArrayOfInt.length);
      return;
    }
    throw new IllegalArgumentException("elements must be 18 length");
  }
  
  public ColorSpaceTransform(Rational[] paramArrayOfRational)
  {
    Preconditions.checkNotNull(paramArrayOfRational, "elements must not be null");
    if (paramArrayOfRational.length == 9)
    {
      mElements = new int[18];
      for (int i = 0; i < paramArrayOfRational.length; i++)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("element[");
        localStringBuilder.append(i);
        localStringBuilder.append("] must not be null");
        Preconditions.checkNotNull(paramArrayOfRational, localStringBuilder.toString());
        mElements[(i * 2 + 0)] = paramArrayOfRational[i].getNumerator();
        mElements[(i * 2 + 1)] = paramArrayOfRational[i].getDenominator();
      }
      return;
    }
    throw new IllegalArgumentException("elements must be 9 length");
  }
  
  private String toShortString()
  {
    StringBuilder localStringBuilder = new StringBuilder("(");
    int i = 0;
    int j = 0;
    while (i < 3)
    {
      localStringBuilder.append("[");
      int k = 0;
      while (k < 3)
      {
        int m = mElements[(j + 0)];
        int n = mElements[(j + 1)];
        localStringBuilder.append(m);
        localStringBuilder.append("/");
        localStringBuilder.append(n);
        if (k < 2) {
          localStringBuilder.append(", ");
        }
        k++;
        j += 2;
      }
      localStringBuilder.append("]");
      if (i < 2) {
        localStringBuilder.append(", ");
      }
      i++;
    }
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void copyElements(int[] paramArrayOfInt, int paramInt)
  {
    Preconditions.checkArgumentNonnegative(paramInt, "offset must not be negative");
    Preconditions.checkNotNull(paramArrayOfInt, "destination must not be null");
    if (paramArrayOfInt.length - paramInt >= 18)
    {
      for (int i = 0; i < 18; i++) {
        paramArrayOfInt[(i + paramInt)] = mElements[i];
      }
      return;
    }
    throw new ArrayIndexOutOfBoundsException("destination too small to fit elements");
  }
  
  public void copyElements(Rational[] paramArrayOfRational, int paramInt)
  {
    Preconditions.checkArgumentNonnegative(paramInt, "offset must not be negative");
    Preconditions.checkNotNull(paramArrayOfRational, "destination must not be null");
    if (paramArrayOfRational.length - paramInt >= 9)
    {
      int i = 0;
      for (int j = 0; i < 9; j += 2)
      {
        paramArrayOfRational[(i + paramInt)] = new Rational(mElements[(j + 0)], mElements[(j + 1)]);
        i++;
      }
      return;
    }
    throw new ArrayIndexOutOfBoundsException("destination too small to fit elements");
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof ColorSpaceTransform))
    {
      paramObject = (ColorSpaceTransform)paramObject;
      int i = 0;
      for (int j = 0; i < 9; j += 2)
      {
        int k = mElements[(j + 0)];
        int m = mElements[(j + 1)];
        int n = mElements[(j + 0)];
        int i1 = mElements[(j + 1)];
        if (!new Rational(k, m).equals(new Rational(n, i1))) {
          return false;
        }
        i++;
      }
      return true;
    }
    return false;
  }
  
  public Rational getElement(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < 3))
    {
      if ((paramInt2 >= 0) && (paramInt2 < 3)) {
        return new Rational(mElements[((paramInt2 * 3 + paramInt1) * 2 + 0)], mElements[((paramInt2 * 3 + paramInt1) * 2 + 1)]);
      }
      throw new IllegalArgumentException("row out of range");
    }
    throw new IllegalArgumentException("column out of range");
  }
  
  public int hashCode()
  {
    return HashCodeHelpers.hashCode(mElements);
  }
  
  public String toString()
  {
    return String.format("ColorSpaceTransform%s", new Object[] { toShortString() });
  }
}
