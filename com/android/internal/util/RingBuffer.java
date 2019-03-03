package com.android.internal.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public class RingBuffer<T>
{
  private final T[] mBuffer;
  private long mCursor = 0L;
  
  public RingBuffer(Class<T> paramClass, int paramInt)
  {
    Preconditions.checkArgumentPositive(paramInt, "A RingBuffer cannot have 0 capacity");
    mBuffer = ((Object[])Array.newInstance(paramClass, paramInt));
  }
  
  private int indexOf(long paramLong)
  {
    return (int)Math.abs(paramLong % mBuffer.length);
  }
  
  public void append(T paramT)
  {
    Object[] arrayOfObject = mBuffer;
    long l = mCursor;
    mCursor = (1L + l);
    arrayOfObject[indexOf(l)] = paramT;
  }
  
  public void clear()
  {
    for (int i = 0; i < size(); i++) {
      mBuffer[i] = null;
    }
    mCursor = 0L;
  }
  
  protected T createNewItem()
  {
    try
    {
      Object localObject = mBuffer.getClass().getComponentType().newInstance();
      return localObject;
    }
    catch (IllegalAccessException|InstantiationException localIllegalAccessException) {}
    return null;
  }
  
  public T getNextSlot()
  {
    long l = mCursor;
    mCursor = (1L + l);
    int i = indexOf(l);
    if (mBuffer[i] == null) {
      mBuffer[i] = createNewItem();
    }
    return mBuffer[i];
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
  
  public int size()
  {
    return (int)Math.min(mBuffer.length, mCursor);
  }
  
  public T[] toArray()
  {
    Object[] arrayOfObject = Arrays.copyOf(mBuffer, size(), mBuffer.getClass());
    long l = mCursor - 1L;
    int i = arrayOfObject.length - 1;
    while (i >= 0)
    {
      arrayOfObject[i] = mBuffer[indexOf(l)];
      i--;
      l -= 1L;
    }
    return arrayOfObject;
  }
}
