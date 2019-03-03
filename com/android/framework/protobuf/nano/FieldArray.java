package com.android.framework.protobuf.nano;

public final class FieldArray
  implements Cloneable
{
  private static final FieldData DELETED = new FieldData();
  private FieldData[] mData;
  private int[] mFieldNumbers;
  private boolean mGarbage = false;
  private int mSize;
  
  FieldArray()
  {
    this(10);
  }
  
  FieldArray(int paramInt)
  {
    paramInt = idealIntArraySize(paramInt);
    mFieldNumbers = new int[paramInt];
    mData = new FieldData[paramInt];
    mSize = 0;
  }
  
  private boolean arrayEquals(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
  {
    for (int i = 0; i < paramInt; i++) {
      if (paramArrayOfInt1[i] != paramArrayOfInt2[i]) {
        return false;
      }
    }
    return true;
  }
  
  private boolean arrayEquals(FieldData[] paramArrayOfFieldData1, FieldData[] paramArrayOfFieldData2, int paramInt)
  {
    for (int i = 0; i < paramInt; i++) {
      if (!paramArrayOfFieldData1[i].equals(paramArrayOfFieldData2[i])) {
        return false;
      }
    }
    return true;
  }
  
  private int binarySearch(int paramInt)
  {
    int i = 0;
    int j = mSize - 1;
    while (i <= j)
    {
      int k = i + j >>> 1;
      int m = mFieldNumbers[k];
      if (m < paramInt)
      {
        i = k + 1;
      }
      else
      {
        if (m <= paramInt) {
          break label58;
        }
        j = k - 1;
      }
      continue;
      label58:
      return k;
    }
    return i;
  }
  
  private void gc()
  {
    int i = mSize;
    int[] arrayOfInt = mFieldNumbers;
    FieldData[] arrayOfFieldData = mData;
    int j = 0;
    int k = 0;
    while (k < i)
    {
      FieldData localFieldData = arrayOfFieldData[k];
      int m = j;
      if (localFieldData != DELETED)
      {
        if (k != j)
        {
          arrayOfInt[j] = arrayOfInt[k];
          arrayOfFieldData[j] = localFieldData;
          arrayOfFieldData[k] = null;
        }
        m = j + 1;
      }
      k++;
      j = m;
    }
    mGarbage = false;
    mSize = j;
  }
  
  private int idealByteArraySize(int paramInt)
  {
    for (int i = 4; i < 32; i++) {
      if (paramInt <= (1 << i) - 12) {
        return (1 << i) - 12;
      }
    }
    return paramInt;
  }
  
  private int idealIntArraySize(int paramInt)
  {
    return idealByteArraySize(paramInt * 4) / 4;
  }
  
  public final FieldArray clone()
  {
    int i = size();
    FieldArray localFieldArray = new FieldArray(i);
    int[] arrayOfInt1 = mFieldNumbers;
    int[] arrayOfInt2 = mFieldNumbers;
    int j = 0;
    System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, i);
    while (j < i)
    {
      if (mData[j] != null) {
        mData[j] = mData[j].clone();
      }
      j++;
    }
    mSize = i;
    return localFieldArray;
  }
  
  FieldData dataAt(int paramInt)
  {
    if (mGarbage) {
      gc();
    }
    return mData[paramInt];
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof FieldArray)) {
      return false;
    }
    paramObject = (FieldArray)paramObject;
    if (size() != paramObject.size()) {
      return false;
    }
    if ((!arrayEquals(mFieldNumbers, mFieldNumbers, mSize)) || (!arrayEquals(mData, mData, mSize))) {
      bool = false;
    }
    return bool;
  }
  
  FieldData get(int paramInt)
  {
    paramInt = binarySearch(paramInt);
    if ((paramInt >= 0) && (mData[paramInt] != DELETED)) {
      return mData[paramInt];
    }
    return null;
  }
  
  public int hashCode()
  {
    if (mGarbage) {
      gc();
    }
    int i = 17;
    for (int j = 0; j < mSize; j++)
    {
      int k = mFieldNumbers[j];
      i = mData[j].hashCode() + 31 * (31 * i + k);
    }
    return i;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if (size() == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void put(int paramInt, FieldData paramFieldData)
  {
    int i = binarySearch(paramInt);
    if (i >= 0)
    {
      mData[i] = paramFieldData;
    }
    else
    {
      int j = i;
      if ((j < mSize) && (mData[j] == DELETED))
      {
        mFieldNumbers[j] = paramInt;
        mData[j] = paramFieldData;
        return;
      }
      i = j;
      if (mGarbage)
      {
        i = j;
        if (mSize >= mFieldNumbers.length)
        {
          gc();
          i = binarySearch(paramInt);
        }
      }
      if (mSize >= mFieldNumbers.length)
      {
        j = idealIntArraySize(mSize + 1);
        int[] arrayOfInt = new int[j];
        FieldData[] arrayOfFieldData = new FieldData[j];
        System.arraycopy(mFieldNumbers, 0, arrayOfInt, 0, mFieldNumbers.length);
        System.arraycopy(mData, 0, arrayOfFieldData, 0, mData.length);
        mFieldNumbers = arrayOfInt;
        mData = arrayOfFieldData;
      }
      if (mSize - i != 0)
      {
        System.arraycopy(mFieldNumbers, i, mFieldNumbers, i + 1, mSize - i);
        System.arraycopy(mData, i, mData, i + 1, mSize - i);
      }
      mFieldNumbers[i] = paramInt;
      mData[i] = paramFieldData;
      mSize += 1;
    }
  }
  
  void remove(int paramInt)
  {
    paramInt = binarySearch(paramInt);
    if ((paramInt >= 0) && (mData[paramInt] != DELETED))
    {
      mData[paramInt] = DELETED;
      mGarbage = true;
    }
  }
  
  int size()
  {
    if (mGarbage) {
      gc();
    }
    return mSize;
  }
}
