package android.util;

import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import libcore.util.EmptyArray;

public class SparseLongArray
  implements Cloneable
{
  private int[] mKeys;
  private int mSize;
  private long[] mValues;
  
  public SparseLongArray()
  {
    this(10);
  }
  
  public SparseLongArray(int paramInt)
  {
    if (paramInt == 0)
    {
      mKeys = EmptyArray.INT;
      mValues = EmptyArray.LONG;
    }
    else
    {
      mValues = ArrayUtils.newUnpaddedLongArray(paramInt);
      mKeys = new int[mValues.length];
    }
    mSize = 0;
  }
  
  public void append(int paramInt, long paramLong)
  {
    if ((mSize != 0) && (paramInt <= mKeys[(mSize - 1)]))
    {
      put(paramInt, paramLong);
      return;
    }
    mKeys = GrowingArrayUtils.append(mKeys, mSize, paramInt);
    mValues = GrowingArrayUtils.append(mValues, mSize, paramLong);
    mSize += 1;
  }
  
  public void clear()
  {
    mSize = 0;
  }
  
  public SparseLongArray clone()
  {
    Object localObject = null;
    try
    {
      SparseLongArray localSparseLongArray = (SparseLongArray)super.clone();
      localObject = localSparseLongArray;
      mKeys = ((int[])mKeys.clone());
      localObject = localSparseLongArray;
      mValues = ((long[])mValues.clone());
      localObject = localSparseLongArray;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {}
    return localObject;
  }
  
  public void delete(int paramInt)
  {
    paramInt = ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
    if (paramInt >= 0) {
      removeAt(paramInt);
    }
  }
  
  public long get(int paramInt)
  {
    return get(paramInt, 0L);
  }
  
  public long get(int paramInt, long paramLong)
  {
    paramInt = ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
    if (paramInt < 0) {
      return paramLong;
    }
    return mValues[paramInt];
  }
  
  public int indexOfKey(int paramInt)
  {
    return ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
  }
  
  public int indexOfValue(long paramLong)
  {
    for (int i = 0; i < mSize; i++) {
      if (mValues[i] == paramLong) {
        return i;
      }
    }
    return -1;
  }
  
  public int keyAt(int paramInt)
  {
    return mKeys[paramInt];
  }
  
  public void put(int paramInt, long paramLong)
  {
    int i = ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
    if (i >= 0)
    {
      mValues[i] = paramLong;
    }
    else
    {
      i = i;
      mKeys = GrowingArrayUtils.insert(mKeys, mSize, i, paramInt);
      mValues = GrowingArrayUtils.insert(mValues, mSize, i, paramLong);
      mSize += 1;
    }
  }
  
  public void removeAt(int paramInt)
  {
    System.arraycopy(mKeys, paramInt + 1, mKeys, paramInt, mSize - (paramInt + 1));
    System.arraycopy(mValues, paramInt + 1, mValues, paramInt, mSize - (paramInt + 1));
    mSize -= 1;
  }
  
  public void removeAtRange(int paramInt1, int paramInt2)
  {
    paramInt2 = Math.min(paramInt2, mSize - paramInt1);
    System.arraycopy(mKeys, paramInt1 + paramInt2, mKeys, paramInt1, mSize - (paramInt1 + paramInt2));
    System.arraycopy(mValues, paramInt1 + paramInt2, mValues, paramInt1, mSize - (paramInt1 + paramInt2));
    mSize -= paramInt2;
  }
  
  public int size()
  {
    return mSize;
  }
  
  public String toString()
  {
    if (size() <= 0) {
      return "{}";
    }
    StringBuilder localStringBuilder = new StringBuilder(mSize * 28);
    localStringBuilder.append('{');
    for (int i = 0; i < mSize; i++)
    {
      if (i > 0) {
        localStringBuilder.append(", ");
      }
      localStringBuilder.append(keyAt(i));
      localStringBuilder.append('=');
      localStringBuilder.append(valueAt(i));
    }
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public long valueAt(int paramInt)
  {
    return mValues[paramInt];
  }
}
