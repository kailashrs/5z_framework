package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import com.android.internal.util.Preconditions;
import java.util.Arrays;

public final class ReprocessFormatsMap
{
  private final int[] mEntry;
  private final int mInputCount;
  
  public ReprocessFormatsMap(int[] paramArrayOfInt)
  {
    Preconditions.checkNotNull(paramArrayOfInt, "entry must not be null");
    int i = paramArrayOfInt.length;
    int j = 0;
    int k = 0;
    while (k < paramArrayOfInt.length)
    {
      int m = StreamConfigurationMap.checkArgumentFormatInternal(paramArrayOfInt[k]);
      i--;
      k++;
      if (i >= 1)
      {
        int n = paramArrayOfInt[k];
        int i1 = i - 1;
        int i2 = k + 1;
        for (k = 0; k < n; k++) {
          StreamConfigurationMap.checkArgumentFormatInternal(paramArrayOfInt[(i2 + k)]);
        }
        k = i2;
        i = i1;
        if (n > 0) {
          if (i1 >= n)
          {
            k = i2 + n;
            i = i1 - n;
          }
          else
          {
            throw new IllegalArgumentException(String.format("Input %x had too few output formats listed (actual: %d, expected: %d)", new Object[] { Integer.valueOf(m), Integer.valueOf(i1), Integer.valueOf(n) }));
          }
        }
        j++;
      }
      else
      {
        throw new IllegalArgumentException(String.format("Input %x had no output format length listed", new Object[] { Integer.valueOf(m) }));
      }
    }
    mEntry = paramArrayOfInt;
    mInputCount = j;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof ReprocessFormatsMap))
    {
      paramObject = (ReprocessFormatsMap)paramObject;
      return Arrays.equals(mEntry, mEntry);
    }
    return false;
  }
  
  public int[] getInputs()
  {
    int[] arrayOfInt = new int[mInputCount];
    int i = mEntry.length;
    int j = 0;
    int k = 0;
    while (j < mEntry.length)
    {
      int m = mEntry[j];
      int n = i - 1;
      i = j + 1;
      if (n >= 1)
      {
        int i1 = mEntry[i];
        n--;
        int i2 = i + 1;
        j = i2;
        i = n;
        if (i1 > 0) {
          if (n >= i1)
          {
            j = i2 + i1;
            i = n - i1;
          }
          else
          {
            throw new AssertionError(String.format("Input %x had too few output formats listed (actual: %d, expected: %d)", new Object[] { Integer.valueOf(m), Integer.valueOf(n), Integer.valueOf(i1) }));
          }
        }
        arrayOfInt[k] = m;
        k++;
      }
      else
      {
        throw new AssertionError(String.format("Input %x had no output format length listed", new Object[] { Integer.valueOf(m) }));
      }
    }
    return StreamConfigurationMap.imageFormatToPublic(arrayOfInt);
  }
  
  public int[] getOutputs(int paramInt)
  {
    int i = mEntry.length;
    int j = 0;
    int k = 0;
    while (k < mEntry.length)
    {
      int m = mEntry[k];
      int n = i - 1;
      k++;
      if (n >= 1)
      {
        i = mEntry[k];
        n--;
        k++;
        if ((i > 0) && (n < i)) {
          throw new AssertionError(String.format("Input %x had too few output formats listed (actual: %d, expected: %d)", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(n), Integer.valueOf(i) }));
        }
        if (m == paramInt)
        {
          int[] arrayOfInt = new int[i];
          for (paramInt = j; paramInt < i; paramInt++) {
            arrayOfInt[paramInt] = mEntry[(k + paramInt)];
          }
          return StreamConfigurationMap.imageFormatToPublic(arrayOfInt);
        }
        k += i;
        i = n - i;
      }
      else
      {
        throw new AssertionError(String.format("Input %x had no output format length listed", new Object[] { Integer.valueOf(paramInt) }));
      }
    }
    throw new IllegalArgumentException(String.format("Input format %x was not one in #getInputs", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public int hashCode()
  {
    return HashCodeHelpers.hashCode(mEntry);
  }
}
