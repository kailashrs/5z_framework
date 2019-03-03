package android.util;

import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import libcore.util.EmptyArray;

public class LongSparseLongArray
  implements Cloneable
{
  private long[] mKeys;
  private int mSize;
  private long[] mValues;
  
  public LongSparseLongArray()
  {
    this(10);
  }
  
  public LongSparseLongArray(int paramInt)
  {
    if (paramInt == 0)
    {
      mKeys = EmptyArray.LONG;
      mValues = EmptyArray.LONG;
    }
    else
    {
      mKeys = ArrayUtils.newUnpaddedLongArray(paramInt);
      mValues = new long[mKeys.length];
    }
    mSize = 0;
  }
  
  public void append(long paramLong1, long paramLong2)
  {
    if ((mSize != 0) && (paramLong1 <= mKeys[(mSize - 1)]))
    {
      put(paramLong1, paramLong2);
      return;
    }
    mKeys = GrowingArrayUtils.append(mKeys, mSize, paramLong1);
    mValues = GrowingArrayUtils.append(mValues, mSize, paramLong2);
    mSize += 1;
  }
  
  public void clear()
  {
    mSize = 0;
  }
  
  public LongSparseLongArray clone()
  {
    Object localObject = null;
    try
    {
      LongSparseLongArray localLongSparseLongArray = (LongSparseLongArray)super.clone();
      localObject = localLongSparseLongArray;
      mKeys = ((long[])mKeys.clone());
      localObject = localLongSparseLongArray;
      mValues = ((long[])mValues.clone());
      localObject = localLongSparseLongArray;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {}
    return localObject;
  }
  
  public void delete(long paramLong)
  {
    int i = ContainerHelpers.binarySearch(mKeys, mSize, paramLong);
    if (i >= 0) {
      removeAt(i);
    }
  }
  
  public long get(long paramLong)
  {
    return get(paramLong, 0L);
  }
  
  public long get(long paramLong1, long paramLong2)
  {
    int i = ContainerHelpers.binarySearch(mKeys, mSize, paramLong1);
    if (i < 0) {
      return paramLong2;
    }
    return mValues[i];
  }
  
  public int indexOfKey(long paramLong)
  {
    return ContainerHelpers.binarySearch(mKeys, mSize, paramLong);
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
  
  public long keyAt(int paramInt)
  {
    return mKeys[paramInt];
  }
  
  public void put(long paramLong1, long paramLong2)
  {
    int i = ContainerHelpers.binarySearch(mKeys, mSize, paramLong1);
    if (i >= 0)
    {
      mValues[i] = paramLong2;
    }
    else
    {
      i = i;
      mKeys = GrowingArrayUtils.insert(mKeys, mSize, i, paramLong1);
      mValues = GrowingArrayUtils.insert(mValues, mSize, i, paramLong2);
      mSize += 1;
    }
  }
  
  public void removeAt(int paramInt)
  {
    System.arraycopy(mKeys, paramInt + 1, mKeys, paramInt, mSize - (paramInt + 1));
    System.arraycopy(mValues, paramInt + 1, mValues, paramInt, mSize - (paramInt + 1));
    mSize -= 1;
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
