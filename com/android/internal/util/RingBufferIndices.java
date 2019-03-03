package com.android.internal.util;

public class RingBufferIndices
{
  private final int mCapacity;
  private int mSize;
  private int mStart;
  
  public RingBufferIndices(int paramInt)
  {
    mCapacity = paramInt;
  }
  
  public int add()
  {
    if (mSize < mCapacity)
    {
      i = mSize;
      mSize += 1;
      return i;
    }
    int i = mStart;
    mStart += 1;
    if (mStart == mCapacity) {
      mStart = 0;
    }
    return i;
  }
  
  public void clear()
  {
    mStart = 0;
    mSize = 0;
  }
  
  public int indexOf(int paramInt)
  {
    int i = mStart + paramInt;
    paramInt = i;
    if (i >= mCapacity) {
      paramInt = i - mCapacity;
    }
    return paramInt;
  }
  
  public int size()
  {
    return mSize;
  }
}
