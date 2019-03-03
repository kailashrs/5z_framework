package android.util;

import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.util.Arrays;
import libcore.util.EmptyArray;

public class SparseIntArray
  implements Cloneable
{
  private int[] mKeys;
  private int mSize;
  private int[] mValues;
  
  public SparseIntArray()
  {
    this(10);
  }
  
  public SparseIntArray(int paramInt)
  {
    if (paramInt == 0)
    {
      mKeys = EmptyArray.INT;
      mValues = EmptyArray.INT;
    }
    else
    {
      mKeys = ArrayUtils.newUnpaddedIntArray(paramInt);
      mValues = new int[mKeys.length];
    }
    mSize = 0;
  }
  
  public void append(int paramInt1, int paramInt2)
  {
    if ((mSize != 0) && (paramInt1 <= mKeys[(mSize - 1)]))
    {
      put(paramInt1, paramInt2);
      return;
    }
    mKeys = GrowingArrayUtils.append(mKeys, mSize, paramInt1);
    mValues = GrowingArrayUtils.append(mValues, mSize, paramInt2);
    mSize += 1;
  }
  
  public void clear()
  {
    mSize = 0;
  }
  
  public SparseIntArray clone()
  {
    Object localObject = null;
    try
    {
      SparseIntArray localSparseIntArray = (SparseIntArray)super.clone();
      localObject = localSparseIntArray;
      mKeys = ((int[])mKeys.clone());
      localObject = localSparseIntArray;
      mValues = ((int[])mValues.clone());
      localObject = localSparseIntArray;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {}
    return localObject;
  }
  
  public int[] copyKeys()
  {
    if (size() == 0) {
      return null;
    }
    return Arrays.copyOf(mKeys, size());
  }
  
  public void delete(int paramInt)
  {
    paramInt = ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
    if (paramInt >= 0) {
      removeAt(paramInt);
    }
  }
  
  public int get(int paramInt)
  {
    return get(paramInt, 0);
  }
  
  public int get(int paramInt1, int paramInt2)
  {
    paramInt1 = ContainerHelpers.binarySearch(mKeys, mSize, paramInt1);
    if (paramInt1 < 0) {
      return paramInt2;
    }
    return mValues[paramInt1];
  }
  
  public int indexOfKey(int paramInt)
  {
    return ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
  }
  
  public int indexOfValue(int paramInt)
  {
    for (int i = 0; i < mSize; i++) {
      if (mValues[i] == paramInt) {
        return i;
      }
    }
    return -1;
  }
  
  public int keyAt(int paramInt)
  {
    return mKeys[paramInt];
  }
  
  public void put(int paramInt1, int paramInt2)
  {
    int i = ContainerHelpers.binarySearch(mKeys, mSize, paramInt1);
    if (i >= 0)
    {
      mValues[i] = paramInt2;
    }
    else
    {
      i = i;
      mKeys = GrowingArrayUtils.insert(mKeys, mSize, i, paramInt1);
      mValues = GrowingArrayUtils.insert(mValues, mSize, i, paramInt2);
      mSize += 1;
    }
  }
  
  public void removeAt(int paramInt)
  {
    System.arraycopy(mKeys, paramInt + 1, mKeys, paramInt, mSize - (paramInt + 1));
    System.arraycopy(mValues, paramInt + 1, mValues, paramInt, mSize - (paramInt + 1));
    mSize -= 1;
  }
  
  public void setValueAt(int paramInt1, int paramInt2)
  {
    mValues[paramInt1] = paramInt2;
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
  
  public int valueAt(int paramInt)
  {
    return mValues[paramInt];
  }
}
