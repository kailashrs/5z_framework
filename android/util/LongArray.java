package android.util;

import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
import libcore.util.EmptyArray;

public class LongArray
  implements Cloneable
{
  private static final int MIN_CAPACITY_INCREMENT = 12;
  private int mSize;
  private long[] mValues;
  
  public LongArray()
  {
    this(10);
  }
  
  public LongArray(int paramInt)
  {
    if (paramInt == 0) {
      mValues = EmptyArray.LONG;
    } else {
      mValues = ArrayUtils.newUnpaddedLongArray(paramInt);
    }
    mSize = 0;
  }
  
  private LongArray(long[] paramArrayOfLong, int paramInt)
  {
    mValues = paramArrayOfLong;
    mSize = Preconditions.checkArgumentInRange(paramInt, 0, paramArrayOfLong.length, "size");
  }
  
  private void checkBounds(int paramInt)
  {
    if ((paramInt >= 0) && (mSize > paramInt)) {
      return;
    }
    throw new ArrayIndexOutOfBoundsException(mSize, paramInt);
  }
  
  public static boolean elementsEqual(LongArray paramLongArray1, LongArray paramLongArray2)
  {
    boolean bool = true;
    if ((paramLongArray1 != null) && (paramLongArray2 != null))
    {
      if (mSize != mSize) {
        return false;
      }
      for (int i = 0; i < mSize; i++) {
        if (paramLongArray1.get(i) != paramLongArray2.get(i)) {
          return false;
        }
      }
      return true;
    }
    if (paramLongArray1 != paramLongArray2) {
      bool = false;
    }
    return bool;
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
      long[] arrayOfLong = ArrayUtils.newUnpaddedLongArray(paramInt);
      System.arraycopy(mValues, 0, arrayOfLong, 0, i);
      mValues = arrayOfLong;
    }
  }
  
  public static LongArray fromArray(long[] paramArrayOfLong, int paramInt)
  {
    return wrap(Arrays.copyOf(paramArrayOfLong, paramInt));
  }
  
  public static LongArray wrap(long[] paramArrayOfLong)
  {
    return new LongArray(paramArrayOfLong, paramArrayOfLong.length);
  }
  
  public void add(int paramInt, long paramLong)
  {
    ensureCapacity(1);
    int i = mSize - paramInt;
    mSize += 1;
    checkBounds(paramInt);
    if (i != 0) {
      System.arraycopy(mValues, paramInt, mValues, paramInt + 1, i);
    }
    mValues[paramInt] = paramLong;
  }
  
  public void add(long paramLong)
  {
    add(mSize, paramLong);
  }
  
  public void addAll(LongArray paramLongArray)
  {
    int i = mSize;
    ensureCapacity(i);
    System.arraycopy(mValues, 0, mValues, mSize, i);
    mSize += i;
  }
  
  public void clear()
  {
    mSize = 0;
  }
  
  public LongArray clone()
  {
    Object localObject = null;
    try
    {
      LongArray localLongArray = (LongArray)super.clone();
      localObject = localLongArray;
      mValues = ((long[])mValues.clone());
      localObject = localLongArray;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {}
    return localObject;
  }
  
  public long get(int paramInt)
  {
    checkBounds(paramInt);
    return mValues[paramInt];
  }
  
  public int indexOf(long paramLong)
  {
    int i = mSize;
    for (int j = 0; j < i; j++) {
      if (mValues[j] == paramLong) {
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
      Arrays.fill(mValues, paramInt, mValues.length, 0L);
    } else {
      ensureCapacity(paramInt - mSize);
    }
    mSize = paramInt;
  }
  
  public void set(int paramInt, long paramLong)
  {
    checkBounds(paramInt);
    mValues[paramInt] = paramLong;
  }
  
  public int size()
  {
    return mSize;
  }
  
  public long[] toArray()
  {
    return Arrays.copyOf(mValues, mSize);
  }
}
