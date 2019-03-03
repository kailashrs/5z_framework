package android.util;

import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
import libcore.util.EmptyArray;

public class IntArray
  implements Cloneable
{
  private static final int MIN_CAPACITY_INCREMENT = 12;
  private int mSize;
  private int[] mValues;
  
  public IntArray()
  {
    this(10);
  }
  
  public IntArray(int paramInt)
  {
    if (paramInt == 0) {
      mValues = EmptyArray.INT;
    } else {
      mValues = ArrayUtils.newUnpaddedIntArray(paramInt);
    }
    mSize = 0;
  }
  
  private IntArray(int[] paramArrayOfInt, int paramInt)
  {
    mValues = paramArrayOfInt;
    mSize = Preconditions.checkArgumentInRange(paramInt, 0, paramArrayOfInt.length, "size");
  }
  
  private void checkBounds(int paramInt)
  {
    if ((paramInt >= 0) && (mSize > paramInt)) {
      return;
    }
    throw new ArrayIndexOutOfBoundsException(mSize, paramInt);
  }
  
  private void ensureCapacity(int paramInt)
  {
    int i = mSize;
    int j = i + paramInt;
    if (j >= mValues.length)
    {
      if (i < 6) {
        paramInt = 12;
      } else {
        paramInt = i >> 1;
      }
      paramInt += i;
      if (paramInt <= j) {
        paramInt = j;
      }
      int[] arrayOfInt = ArrayUtils.newUnpaddedIntArray(paramInt);
      System.arraycopy(mValues, 0, arrayOfInt, 0, i);
      mValues = arrayOfInt;
    }
  }
  
  public static IntArray fromArray(int[] paramArrayOfInt, int paramInt)
  {
    return wrap(Arrays.copyOf(paramArrayOfInt, paramInt));
  }
  
  public static IntArray wrap(int[] paramArrayOfInt)
  {
    return new IntArray(paramArrayOfInt, paramArrayOfInt.length);
  }
  
  public void add(int paramInt)
  {
    add(mSize, paramInt);
  }
  
  public void add(int paramInt1, int paramInt2)
  {
    ensureCapacity(1);
    int i = mSize - paramInt1;
    mSize += 1;
    checkBounds(paramInt1);
    if (i != 0) {
      System.arraycopy(mValues, paramInt1, mValues, paramInt1 + 1, i);
    }
    mValues[paramInt1] = paramInt2;
  }
  
  public void addAll(IntArray paramIntArray)
  {
    int i = mSize;
    ensureCapacity(i);
    System.arraycopy(mValues, 0, mValues, mSize, i);
    mSize += i;
  }
  
  public int binarySearch(int paramInt)
  {
    return ContainerHelpers.binarySearch(mValues, mSize, paramInt);
  }
  
  public void clear()
  {
    mSize = 0;
  }
  
  public IntArray clone()
    throws CloneNotSupportedException
  {
    IntArray localIntArray = (IntArray)super.clone();
    mValues = ((int[])mValues.clone());
    return localIntArray;
  }
  
  public int get(int paramInt)
  {
    checkBounds(paramInt);
    return mValues[paramInt];
  }
  
  public int indexOf(int paramInt)
  {
    int i = mSize;
    for (int j = 0; j < i; j++) {
      if (mValues[j] == paramInt) {
        return j;
      }
    }
    return -1;
  }
  
  public void remove(int paramInt)
  {
    checkBounds(paramInt);
    System.arraycopy(mValues, paramInt + 1, mValues, paramInt, mSize - paramInt - 1);
    mSize -= 1;
  }
  
  public void resize(int paramInt)
  {
    Preconditions.checkArgumentNonnegative(paramInt);
    if (paramInt <= mValues.length) {
      Arrays.fill(mValues, paramInt, mValues.length, 0);
    } else {
      ensureCapacity(paramInt - mSize);
    }
    mSize = paramInt;
  }
  
  public void set(int paramInt1, int paramInt2)
  {
    checkBounds(paramInt1);
    mValues[paramInt1] = paramInt2;
  }
  
  public int size()
  {
    return mSize;
  }
  
  public int[] toArray()
  {
    return Arrays.copyOf(mValues, mSize);
  }
}
