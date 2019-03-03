package android.util;

import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import libcore.util.EmptyArray;

public class LongSparseArray<E>
  implements Cloneable
{
  private static final Object DELETED = new Object();
  private boolean mGarbage = false;
  private long[] mKeys;
  private int mSize;
  private Object[] mValues;
  
  public LongSparseArray()
  {
    this(10);
  }
  
  public LongSparseArray(int paramInt)
  {
    if (paramInt == 0)
    {
      mKeys = EmptyArray.LONG;
      mValues = EmptyArray.OBJECT;
    }
    else
    {
      mKeys = ArrayUtils.newUnpaddedLongArray(paramInt);
      mValues = ArrayUtils.newUnpaddedObjectArray(paramInt);
    }
    mSize = 0;
  }
  
  private void gc()
  {
    int i = mSize;
    long[] arrayOfLong = mKeys;
    Object[] arrayOfObject = mValues;
    int j = 0;
    int k = 0;
    while (k < i)
    {
      Object localObject = arrayOfObject[k];
      int m = j;
      if (localObject != DELETED)
      {
        if (k != j)
        {
          arrayOfLong[j] = arrayOfLong[k];
          arrayOfObject[j] = localObject;
          arrayOfObject[k] = null;
        }
        m = j + 1;
      }
      k++;
      j = m;
    }
    mGarbage = false;
    mSize = j;
  }
  
  public void append(long paramLong, E paramE)
  {
    if ((mSize != 0) && (paramLong <= mKeys[(mSize - 1)]))
    {
      put(paramLong, paramE);
      return;
    }
    if ((mGarbage) && (mSize >= mKeys.length)) {
      gc();
    }
    mKeys = GrowingArrayUtils.append(mKeys, mSize, paramLong);
    mValues = GrowingArrayUtils.append(mValues, mSize, paramE);
    mSize += 1;
  }
  
  public void clear()
  {
    int i = mSize;
    Object[] arrayOfObject = mValues;
    for (int j = 0; j < i; j++) {
      arrayOfObject[j] = null;
    }
    mSize = 0;
    mGarbage = false;
  }
  
  public LongSparseArray<E> clone()
  {
    Object localObject = null;
    try
    {
      LongSparseArray localLongSparseArray = (LongSparseArray)super.clone();
      localObject = localLongSparseArray;
      mKeys = ((long[])mKeys.clone());
      localObject = localLongSparseArray;
      mValues = ((Object[])mValues.clone());
      localObject = localLongSparseArray;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {}
    return localObject;
  }
  
  public void delete(long paramLong)
  {
    int i = ContainerHelpers.binarySearch(mKeys, mSize, paramLong);
    if ((i >= 0) && (mValues[i] != DELETED))
    {
      mValues[i] = DELETED;
      mGarbage = true;
    }
  }
  
  public E get(long paramLong)
  {
    return get(paramLong, null);
  }
  
  public E get(long paramLong, E paramE)
  {
    int i = ContainerHelpers.binarySearch(mKeys, mSize, paramLong);
    if ((i >= 0) && (mValues[i] != DELETED)) {
      return mValues[i];
    }
    return paramE;
  }
  
  public int indexOfKey(long paramLong)
  {
    if (mGarbage) {
      gc();
    }
    return ContainerHelpers.binarySearch(mKeys, mSize, paramLong);
  }
  
  public int indexOfValue(E paramE)
  {
    if (mGarbage) {
      gc();
    }
    for (int i = 0; i < mSize; i++) {
      if (mValues[i] == paramE) {
        return i;
      }
    }
    return -1;
  }
  
  public int indexOfValueByValue(E paramE)
  {
    if (mGarbage) {
      gc();
    }
    for (int i = 0; i < mSize; i++) {
      if (paramE == null)
      {
        if (mValues[i] == null) {
          return i;
        }
      }
      else if (paramE.equals(mValues[i])) {
        return i;
      }
    }
    return -1;
  }
  
  public long keyAt(int paramInt)
  {
    if (mGarbage) {
      gc();
    }
    return mKeys[paramInt];
  }
  
  public void put(long paramLong, E paramE)
  {
    int i = ContainerHelpers.binarySearch(mKeys, mSize, paramLong);
    if (i >= 0)
    {
      mValues[i] = paramE;
    }
    else
    {
      int j = i;
      if ((j < mSize) && (mValues[j] == DELETED))
      {
        mKeys[j] = paramLong;
        mValues[j] = paramE;
        return;
      }
      i = j;
      if (mGarbage)
      {
        i = j;
        if (mSize >= mKeys.length)
        {
          gc();
          i = ContainerHelpers.binarySearch(mKeys, mSize, paramLong);
        }
      }
      mKeys = GrowingArrayUtils.insert(mKeys, mSize, i, paramLong);
      mValues = GrowingArrayUtils.insert(mValues, mSize, i, paramE);
      mSize += 1;
    }
  }
  
  public void remove(long paramLong)
  {
    delete(paramLong);
  }
  
  public void removeAt(int paramInt)
  {
    if (mValues[paramInt] != DELETED)
    {
      mValues[paramInt] = DELETED;
      mGarbage = true;
    }
  }
  
  public void setValueAt(int paramInt, E paramE)
  {
    if (mGarbage) {
      gc();
    }
    mValues[paramInt] = paramE;
  }
  
  public int size()
  {
    if (mGarbage) {
      gc();
    }
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
      Object localObject = valueAt(i);
      if (localObject != this) {
        localStringBuilder.append(localObject);
      } else {
        localStringBuilder.append("(this Map)");
      }
    }
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public E valueAt(int paramInt)
  {
    if (mGarbage) {
      gc();
    }
    return mValues[paramInt];
  }
}
