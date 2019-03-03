package android.text;

import com.android.internal.util.ArrayUtils;
import libcore.util.EmptyArray;

public final class AutoGrowArray
{
  private static final int MAX_CAPACITY_TO_BE_KEPT = 10000;
  private static final int MIN_CAPACITY_INCREMENT = 12;
  
  public AutoGrowArray() {}
  
  private static int computeNewCapacity(int paramInt1, int paramInt2)
  {
    int i;
    if (paramInt1 < 6) {
      i = 12;
    } else {
      i = paramInt1 >> 1;
    }
    paramInt1 = i + paramInt1;
    if (paramInt1 <= paramInt2) {
      paramInt1 = paramInt2;
    }
    return paramInt1;
  }
  
  public static class ByteArray
  {
    private int mSize;
    private byte[] mValues;
    
    public ByteArray()
    {
      this(10);
    }
    
    public ByteArray(int paramInt)
    {
      if (paramInt == 0) {
        mValues = EmptyArray.BYTE;
      } else {
        mValues = ArrayUtils.newUnpaddedByteArray(paramInt);
      }
      mSize = 0;
    }
    
    private void ensureCapacity(int paramInt)
    {
      paramInt = mSize + paramInt;
      if (paramInt >= mValues.length)
      {
        byte[] arrayOfByte = ArrayUtils.newUnpaddedByteArray(AutoGrowArray.computeNewCapacity(mSize, paramInt));
        System.arraycopy(mValues, 0, arrayOfByte, 0, mSize);
        mValues = arrayOfByte;
      }
    }
    
    public void append(byte paramByte)
    {
      ensureCapacity(1);
      byte[] arrayOfByte = mValues;
      int i = mSize;
      mSize = (i + 1);
      arrayOfByte[i] = ((byte)paramByte);
    }
    
    public void clear()
    {
      mSize = 0;
    }
    
    public void clearWithReleasingLargeArray()
    {
      clear();
      if (mValues.length > 10000) {
        mValues = EmptyArray.BYTE;
      }
    }
    
    public byte get(int paramInt)
    {
      return mValues[paramInt];
    }
    
    public byte[] getRawArray()
    {
      return mValues;
    }
    
    public void resize(int paramInt)
    {
      if (paramInt > mValues.length) {
        ensureCapacity(paramInt - mSize);
      }
      mSize = paramInt;
    }
    
    public void set(int paramInt, byte paramByte)
    {
      mValues[paramInt] = ((byte)paramByte);
    }
    
    public int size()
    {
      return mSize;
    }
  }
  
  public static class FloatArray
  {
    private int mSize;
    private float[] mValues;
    
    public FloatArray()
    {
      this(10);
    }
    
    public FloatArray(int paramInt)
    {
      if (paramInt == 0) {
        mValues = EmptyArray.FLOAT;
      } else {
        mValues = ArrayUtils.newUnpaddedFloatArray(paramInt);
      }
      mSize = 0;
    }
    
    private void ensureCapacity(int paramInt)
    {
      paramInt = mSize + paramInt;
      if (paramInt >= mValues.length)
      {
        float[] arrayOfFloat = ArrayUtils.newUnpaddedFloatArray(AutoGrowArray.computeNewCapacity(mSize, paramInt));
        System.arraycopy(mValues, 0, arrayOfFloat, 0, mSize);
        mValues = arrayOfFloat;
      }
    }
    
    public void append(float paramFloat)
    {
      ensureCapacity(1);
      float[] arrayOfFloat = mValues;
      int i = mSize;
      mSize = (i + 1);
      arrayOfFloat[i] = paramFloat;
    }
    
    public void clear()
    {
      mSize = 0;
    }
    
    public void clearWithReleasingLargeArray()
    {
      clear();
      if (mValues.length > 10000) {
        mValues = EmptyArray.FLOAT;
      }
    }
    
    public float get(int paramInt)
    {
      return mValues[paramInt];
    }
    
    public float[] getRawArray()
    {
      return mValues;
    }
    
    public void resize(int paramInt)
    {
      if (paramInt > mValues.length) {
        ensureCapacity(paramInt - mSize);
      }
      mSize = paramInt;
    }
    
    public void set(int paramInt, float paramFloat)
    {
      mValues[paramInt] = paramFloat;
    }
    
    public int size()
    {
      return mSize;
    }
  }
  
  public static class IntArray
  {
    private int mSize;
    private int[] mValues;
    
    public IntArray()
    {
      this(10);
    }
    
    public IntArray(int paramInt)
    {
      if (paramInt == 0) {
        mValues = EmptyArray.INT;
      } else {
        mValues = ArrayUtils.newUnpaddedIntArray(paramInt);
      }
      mSize = 0;
    }
    
    private void ensureCapacity(int paramInt)
    {
      paramInt = mSize + paramInt;
      if (paramInt >= mValues.length)
      {
        int[] arrayOfInt = ArrayUtils.newUnpaddedIntArray(AutoGrowArray.computeNewCapacity(mSize, paramInt));
        System.arraycopy(mValues, 0, arrayOfInt, 0, mSize);
        mValues = arrayOfInt;
      }
    }
    
    public void append(int paramInt)
    {
      ensureCapacity(1);
      int[] arrayOfInt = mValues;
      int i = mSize;
      mSize = (i + 1);
      arrayOfInt[i] = paramInt;
    }
    
    public void clear()
    {
      mSize = 0;
    }
    
    public void clearWithReleasingLargeArray()
    {
      clear();
      if (mValues.length > 10000) {
        mValues = EmptyArray.INT;
      }
    }
    
    public int get(int paramInt)
    {
      return mValues[paramInt];
    }
    
    public int[] getRawArray()
    {
      return mValues;
    }
    
    public void resize(int paramInt)
    {
      if (paramInt > mValues.length) {
        ensureCapacity(paramInt - mSize);
      }
      mSize = paramInt;
    }
    
    public void set(int paramInt1, int paramInt2)
    {
      mValues[paramInt1] = paramInt2;
    }
    
    public int size()
    {
      return mSize;
    }
  }
}
