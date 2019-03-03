package android.util;

import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import libcore.util.EmptyArray;

public class SparseBooleanArray
  implements Cloneable
{
  private int[] mKeys;
  private int mSize;
  private boolean[] mValues;
  
  public SparseBooleanArray()
  {
    this(10);
  }
  
  public SparseBooleanArray(int paramInt)
  {
    if (paramInt == 0)
    {
      mKeys = EmptyArray.INT;
      mValues = EmptyArray.BOOLEAN;
    }
    else
    {
      mKeys = ArrayUtils.newUnpaddedIntArray(paramInt);
      mValues = new boolean[mKeys.length];
    }
    mSize = 0;
  }
  
  public void append(int paramInt, boolean paramBoolean)
  {
    if ((mSize != 0) && (paramInt <= mKeys[(mSize - 1)]))
    {
      put(paramInt, paramBoolean);
      return;
    }
    mKeys = GrowingArrayUtils.append(mKeys, mSize, paramInt);
    mValues = GrowingArrayUtils.append(mValues, mSize, paramBoolean);
    mSize += 1;
  }
  
  public void clear()
  {
    mSize = 0;
  }
  
  public SparseBooleanArray clone()
  {
    Object localObject = null;
    try
    {
      SparseBooleanArray localSparseBooleanArray = (SparseBooleanArray)super.clone();
      localObject = localSparseBooleanArray;
      mKeys = ((int[])mKeys.clone());
      localObject = localSparseBooleanArray;
      mValues = ((boolean[])mValues.clone());
      localObject = localSparseBooleanArray;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {}
    return localObject;
  }
  
  public void delete(int paramInt)
  {
    paramInt = ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
    if (paramInt >= 0)
    {
      System.arraycopy(mKeys, paramInt + 1, mKeys, paramInt, mSize - (paramInt + 1));
      System.arraycopy(mValues, paramInt + 1, mValues, paramInt, mSize - (paramInt + 1));
      mSize -= 1;
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof SparseBooleanArray)) {
      return false;
    }
    paramObject = (SparseBooleanArray)paramObject;
    if (mSize != mSize) {
      return false;
    }
    for (int i = 0; i < mSize; i++)
    {
      if (mKeys[i] != mKeys[i]) {
        return false;
      }
      if (mValues[i] != mValues[i]) {
        return false;
      }
    }
    return true;
  }
  
  public boolean get(int paramInt)
  {
    return get(paramInt, false);
  }
  
  public boolean get(int paramInt, boolean paramBoolean)
  {
    paramInt = ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
    if (paramInt < 0) {
      return paramBoolean;
    }
    return mValues[paramInt];
  }
  
  public int hashCode()
  {
    int i = mSize;
    for (int j = 0; j < mSize; j++) {
      i = 31 * i + mKeys[j] | mValues[j];
    }
    return i;
  }
  
  public int indexOfKey(int paramInt)
  {
    return ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
  }
  
  public int indexOfValue(boolean paramBoolean)
  {
    for (int i = 0; i < mSize; i++) {
      if (mValues[i] == paramBoolean) {
        return i;
      }
    }
    return -1;
  }
  
  public int keyAt(int paramInt)
  {
    return mKeys[paramInt];
  }
  
  public void put(int paramInt, boolean paramBoolean)
  {
    int i = ContainerHelpers.binarySearch(mKeys, mSize, paramInt);
    if (i >= 0)
    {
      mValues[i] = paramBoolean;
    }
    else
    {
      i = i;
      mKeys = GrowingArrayUtils.insert(mKeys, mSize, i, paramInt);
      mValues = GrowingArrayUtils.insert(mValues, mSize, i, paramBoolean);
      mSize += 1;
    }
  }
  
  public void removeAt(int paramInt)
  {
    System.arraycopy(mKeys, paramInt + 1, mKeys, paramInt, mSize - (paramInt + 1));
    System.arraycopy(mValues, paramInt + 1, mValues, paramInt, mSize - (paramInt + 1));
    mSize -= 1;
  }
  
  public void setKeyAt(int paramInt1, int paramInt2)
  {
    mKeys[paramInt1] = paramInt2;
  }
  
  public void setValueAt(int paramInt, boolean paramBoolean)
  {
    mValues[paramInt] = paramBoolean;
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
  
  public boolean valueAt(int paramInt)
  {
    return mValues[paramInt];
  }
}
