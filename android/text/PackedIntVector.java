package android.text;

import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;

@VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
public class PackedIntVector
{
  private final int mColumns;
  private int mRowGapLength;
  private int mRowGapStart;
  private int mRows;
  private int[] mValueGap;
  private int[] mValues;
  
  public PackedIntVector(int paramInt)
  {
    mColumns = paramInt;
    mRows = 0;
    mRowGapStart = 0;
    mRowGapLength = mRows;
    mValues = null;
    mValueGap = new int[2 * paramInt];
  }
  
  private final void growBuffer()
  {
    int i = mColumns;
    int[] arrayOfInt1 = ArrayUtils.newUnpaddedIntArray(GrowingArrayUtils.growSize(size()) * i);
    int j = arrayOfInt1.length / i;
    int[] arrayOfInt2 = mValueGap;
    int k = mRowGapStart;
    int m = mRows - (mRowGapLength + k);
    int[] arrayOfInt3 = mValues;
    int n = 0;
    if (arrayOfInt3 != null)
    {
      System.arraycopy(mValues, 0, arrayOfInt1, 0, i * k);
      System.arraycopy(mValues, (mRows - m) * i, arrayOfInt1, (j - m) * i, m * i);
    }
    while (n < i)
    {
      if (arrayOfInt2[n] >= k)
      {
        arrayOfInt2[n] += j - mRows;
        if (arrayOfInt2[n] < k) {
          arrayOfInt2[n] = k;
        }
      }
      n++;
    }
    mRowGapLength += j - mRows;
    mRows = j;
    mValues = arrayOfInt1;
  }
  
  private final void moveRowGapTo(int paramInt)
  {
    if (paramInt == mRowGapStart) {
      return;
    }
    int i;
    int j;
    int k;
    int m;
    int[] arrayOfInt1;
    int[] arrayOfInt2;
    int n;
    int i1;
    int i3;
    int i4;
    int i5;
    if (paramInt > mRowGapStart)
    {
      i = mRowGapLength;
      j = mRowGapStart;
      k = mRowGapLength;
      m = mColumns;
      arrayOfInt1 = mValueGap;
      arrayOfInt2 = mValues;
      n = mRowGapStart + mRowGapLength;
      for (i1 = n; i1 < n + (i + paramInt - (j + k)); i1++)
      {
        int i2 = i1 - n + mRowGapStart;
        for (i3 = 0; i3 < m; i3++)
        {
          i4 = arrayOfInt2[(i1 * m + i3)];
          i5 = i4;
          if (i1 >= arrayOfInt1[i3]) {
            i5 = i4 + arrayOfInt1[(i3 + m)];
          }
          i4 = i5;
          if (i2 >= arrayOfInt1[i3]) {
            i4 = i5 - arrayOfInt1[(i3 + m)];
          }
          arrayOfInt2[(i2 * m + i3)] = i4;
        }
      }
    }
    else
    {
      n = mRowGapStart - paramInt;
      i = mColumns;
      arrayOfInt2 = mValueGap;
      arrayOfInt1 = mValues;
      k = mRowGapStart;
      j = mRowGapLength;
      for (i1 = paramInt + n - 1; i1 >= paramInt; i1--)
      {
        m = i1 - paramInt + (k + j) - n;
        for (i3 = 0; i3 < i; i3++)
        {
          i4 = arrayOfInt1[(i1 * i + i3)];
          i5 = i4;
          if (i1 >= arrayOfInt2[i3]) {
            i5 = i4 + arrayOfInt2[(i3 + i)];
          }
          i4 = i5;
          if (m >= arrayOfInt2[i3]) {
            i4 = i5 - arrayOfInt2[(i3 + i)];
          }
          arrayOfInt1[(m * i + i3)] = i4;
        }
      }
    }
    mRowGapStart = paramInt;
  }
  
  private final void moveValueGapTo(int paramInt1, int paramInt2)
  {
    int[] arrayOfInt1 = mValueGap;
    int[] arrayOfInt2 = mValues;
    int i = mColumns;
    if (paramInt2 == arrayOfInt1[paramInt1]) {
      return;
    }
    int k;
    if (paramInt2 > arrayOfInt1[paramInt1]) {
      for (j = arrayOfInt1[paramInt1]; j < paramInt2; j++)
      {
        k = j * i + paramInt1;
        arrayOfInt2[k] += arrayOfInt1[(paramInt1 + i)];
      }
    }
    for (int j = paramInt2; j < arrayOfInt1[paramInt1]; j++)
    {
      k = j * i + paramInt1;
      arrayOfInt2[k] -= arrayOfInt1[(paramInt1 + i)];
    }
    arrayOfInt1[paramInt1] = paramInt2;
  }
  
  private void setValueInternal(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt1;
    if (paramInt1 >= mRowGapStart) {
      i = paramInt1 + mRowGapLength;
    }
    int[] arrayOfInt = mValueGap;
    paramInt1 = paramInt3;
    if (i >= arrayOfInt[paramInt2]) {
      paramInt1 = paramInt3 - arrayOfInt[(mColumns + paramInt2)];
    }
    mValues[(mColumns * i + paramInt2)] = paramInt1;
  }
  
  public void adjustValuesBelow(int paramInt1, int paramInt2, int paramInt3)
  {
    if (((paramInt1 | paramInt2) >= 0) && (paramInt1 <= size()) && (paramInt2 < width()))
    {
      int i = paramInt1;
      if (paramInt1 >= mRowGapStart) {
        i = paramInt1 + mRowGapLength;
      }
      moveValueGapTo(paramInt2, i);
      localObject = mValueGap;
      paramInt1 = mColumns + paramInt2;
      localObject[paramInt1] += paramInt3;
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(", ");
    ((StringBuilder)localObject).append(paramInt2);
    throw new IndexOutOfBoundsException(((StringBuilder)localObject).toString());
  }
  
  public void deleteAt(int paramInt1, int paramInt2)
  {
    if (((paramInt1 | paramInt2) >= 0) && (paramInt1 + paramInt2 <= size()))
    {
      moveRowGapTo(paramInt1 + paramInt2);
      mRowGapStart -= paramInt2;
      mRowGapLength += paramInt2;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(", ");
    localStringBuilder.append(paramInt2);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public int getValue(int paramInt1, int paramInt2)
  {
    int i = mColumns;
    if (((paramInt1 | paramInt2) >= 0) && (paramInt1 < size()) && (paramInt2 < i))
    {
      int j = paramInt1;
      if (paramInt1 >= mRowGapStart) {
        j = paramInt1 + mRowGapLength;
      }
      int k = mValues[(j * i + paramInt2)];
      localObject = mValueGap;
      paramInt1 = k;
      if (j >= localObject[paramInt2]) {
        paramInt1 = k + localObject[(paramInt2 + i)];
      }
      return paramInt1;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(", ");
    ((StringBuilder)localObject).append(paramInt2);
    throw new IndexOutOfBoundsException(((StringBuilder)localObject).toString());
  }
  
  public void insertAt(int paramInt, int[] paramArrayOfInt)
  {
    if ((paramInt >= 0) && (paramInt <= size()))
    {
      if ((paramArrayOfInt != null) && (paramArrayOfInt.length < width()))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("value count ");
        localStringBuilder.append(paramArrayOfInt.length);
        throw new IndexOutOfBoundsException(localStringBuilder.toString());
      }
      moveRowGapTo(paramInt);
      if (mRowGapLength == 0) {
        growBuffer();
      }
      mRowGapStart += 1;
      mRowGapLength -= 1;
      if (paramArrayOfInt == null) {
        for (i = mColumns - 1; i >= 0; i--) {
          setValueInternal(paramInt, i, 0);
        }
      }
      for (int i = mColumns - 1; i >= 0; i--) {
        setValueInternal(paramInt, i, paramArrayOfInt[i]);
      }
      return;
    }
    paramArrayOfInt = new StringBuilder();
    paramArrayOfInt.append("row ");
    paramArrayOfInt.append(paramInt);
    throw new IndexOutOfBoundsException(paramArrayOfInt.toString());
  }
  
  public void setValue(int paramInt1, int paramInt2, int paramInt3)
  {
    if (((paramInt1 | paramInt2) >= 0) && (paramInt1 < size()) && (paramInt2 < mColumns))
    {
      int i = paramInt1;
      if (paramInt1 >= mRowGapStart) {
        i = paramInt1 + mRowGapLength;
      }
      localObject = mValueGap;
      paramInt1 = paramInt3;
      if (i >= localObject[paramInt2]) {
        paramInt1 = paramInt3 - localObject[(mColumns + paramInt2)];
      }
      mValues[(mColumns * i + paramInt2)] = paramInt1;
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(", ");
    ((StringBuilder)localObject).append(paramInt2);
    throw new IndexOutOfBoundsException(((StringBuilder)localObject).toString());
  }
  
  public int size()
  {
    return mRows - mRowGapLength;
  }
  
  public int width()
  {
    return mColumns;
  }
}
