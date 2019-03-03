package android.util;

import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import libcore.util.EmptyArray;

public class SparseArray<E>
  implements Cloneable
{
  private static final Object DELETED = new Object();
  private boolean mGarbage = false;
  private int[] mKeys;
  private int mSize;
  private Object[] mValues;
  
  public SparseArray()
  {
    this(10);
  }
  
  public SparseArray(int paramInt)
  {
    if (paramInt == 0)
    {
      mKeys = EmptyArray.INT;
      mValues = EmptyArray.OBJECT;
    }
    else
    {
      mValues = ArrayUtils.newUnpaddedObjectArray(paramInt);
      mKeys = new int[mValues.length];
    }
    mSize = 0;
  }
  
  private void gc()
  {
    int i = mSize;
    int[] arrayOfInt = mKeys;
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
          arrayOfInt[j] = arrayOfInt[k];
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
  
  public void append(int paramInt, E paramE)
  {
    if ((mSize != 0) && (paramInt <= mKeys[(mSize - 1)]))
    {
      put(paramInt, paramE);
      return;
    }
    if ((mGarbage) && (mSize >= mKeys.length)) {
      gc();
    }
    mKeys = GrowingArrayUtils.append(mKeys, mSize, paramInt);
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
  
  public SparseArray<E> clone()
  {
    Object localObject = null;
    try
    {
      SparseArray localSparseArray = (SparseArray)super.clone();
      localObject = localSparseArray;
      mKeys = ((int[])mKeys.clone());
      localObject = localSparseArray;
      mValues = ((Object[])mValues.clone());
      localObject = localSparseArray;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {}
    return localObject;
  }
  
  public void delete(int paramInt)
  {
    paramInt = ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
    if ((paramInt >= 0) && (mValues[paramInt] != DELETED))
    {
      mValues[paramInt] = DELETED;
      mGarbage = true;
    }
  }
  
  public E get(int paramInt)
  {
    return get(paramInt, null);
  }
  
  public E get(int paramInt, E paramE)
  {
    paramInt = ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
    if ((paramInt >= 0) && (mValues[paramInt] != DELETED)) {
      return mValues[paramInt];
    }
    return paramE;
  }
  
  public int indexOfKey(int paramInt)
  {
    if (mGarbage) {
      gc();
    }
    return ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
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
  
  public int keyAt(int paramInt)
  {
    if (mGarbage) {
      gc();
    }
    return mKeys[paramInt];
  }
  
  public void put(int paramInt, E paramE)
  {
    int i = ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
    if (i >= 0)
    {
      mValues[i] = paramE;
    }
    else
    {
      int j = i;
      if ((j < mSize) && (mValues[j] == DELETED))
      {
        mKeys[j] = paramInt;
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
          i = ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
        }
      }
      mKeys = GrowingArrayUtils.insert(mKeys, mSize, i, paramInt);
      mValues = GrowingArrayUtils.insert(mValues, mSize, i, paramE);
      mSize += 1;
    }
  }
  
  public void remove(int paramInt)
  {
    delete(paramInt);
  }
  
  public void removeAt(int paramInt)
  {
    if (mValues[paramInt] != DELETED)
    {
      mValues[paramInt] = DELETED;
      mGarbage = true;
    }
  }
  
  public void removeAtRange(int paramInt1, int paramInt2)
  {
    paramInt2 = Math.min(mSize, paramInt1 + paramInt2);
    while (paramInt1 < paramInt2)
    {
      removeAt(paramInt1);
      paramInt1++;
    }
  }
  
  public E removeReturnOld(int paramInt)
  {
    paramInt = ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
    if ((paramInt >= 0) && (mValues[paramInt] != DELETED))
    {
      Object localObject = mValues[paramInt];
      mValues[paramInt] = DELETED;
      mGarbage = true;
      return localObject;
    }
    return null;
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
