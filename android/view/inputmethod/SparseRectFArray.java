package android.view.inputmethod;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class SparseRectFArray
  implements Parcelable
{
  public static final Parcelable.Creator<SparseRectFArray> CREATOR = new Parcelable.Creator()
  {
    public SparseRectFArray createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SparseRectFArray(paramAnonymousParcel);
    }
    
    public SparseRectFArray[] newArray(int paramAnonymousInt)
    {
      return new SparseRectFArray[paramAnonymousInt];
    }
  };
  private final float[] mCoordinates;
  private final int[] mFlagsArray;
  private final int[] mKeys;
  
  public SparseRectFArray(Parcel paramParcel)
  {
    mKeys = paramParcel.createIntArray();
    mCoordinates = paramParcel.createFloatArray();
    mFlagsArray = paramParcel.createIntArray();
  }
  
  private SparseRectFArray(SparseRectFArrayBuilder paramSparseRectFArrayBuilder)
  {
    if (mCount == 0)
    {
      mKeys = null;
      mCoordinates = null;
      mFlagsArray = null;
    }
    else
    {
      mKeys = new int[mCount];
      mCoordinates = new float[mCount * 4];
      mFlagsArray = new int[mCount];
      System.arraycopy(mKeys, 0, mKeys, 0, mCount);
      System.arraycopy(mCoordinates, 0, mCoordinates, 0, mCount * 4);
      System.arraycopy(mFlagsArray, 0, mFlagsArray, 0, mCount);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof SparseRectFArray)) {
      return false;
    }
    paramObject = (SparseRectFArray)paramObject;
    if ((Arrays.equals(mKeys, mKeys)) && (Arrays.equals(mCoordinates, mCoordinates)) && (Arrays.equals(mFlagsArray, mFlagsArray))) {
      bool = true;
    }
    return bool;
  }
  
  public RectF get(int paramInt)
  {
    if (mKeys == null) {
      return null;
    }
    if (paramInt < 0) {
      return null;
    }
    paramInt = Arrays.binarySearch(mKeys, paramInt);
    if (paramInt < 0) {
      return null;
    }
    paramInt *= 4;
    return new RectF(mCoordinates[paramInt], mCoordinates[(paramInt + 1)], mCoordinates[(paramInt + 2)], mCoordinates[(paramInt + 3)]);
  }
  
  public int getFlags(int paramInt1, int paramInt2)
  {
    if (mKeys == null) {
      return paramInt2;
    }
    if (paramInt1 < 0) {
      return paramInt2;
    }
    paramInt1 = Arrays.binarySearch(mKeys, paramInt1);
    if (paramInt1 < 0) {
      return paramInt2;
    }
    return mFlagsArray[paramInt1];
  }
  
  public int hashCode()
  {
    if ((mKeys != null) && (mKeys.length != 0))
    {
      int i = mKeys.length;
      for (int j = 0; j < 4; j++) {
        i = (int)(i * 31 + mCoordinates[j]);
      }
      return i * 31 + mFlagsArray[0];
    }
    return 0;
  }
  
  public String toString()
  {
    if ((mKeys != null) && (mCoordinates != null) && (mFlagsArray != null))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SparseRectFArray{");
      for (int i = 0; i < mKeys.length; i++)
      {
        if (i != 0) {
          localStringBuilder.append(", ");
        }
        int j = i * 4;
        localStringBuilder.append(mKeys[i]);
        localStringBuilder.append(":[");
        localStringBuilder.append(mCoordinates[(j + 0)]);
        localStringBuilder.append(",");
        localStringBuilder.append(mCoordinates[(j + 1)]);
        localStringBuilder.append("],[");
        localStringBuilder.append(mCoordinates[(j + 2)]);
        localStringBuilder.append(",");
        localStringBuilder.append(mCoordinates[(j + 3)]);
        localStringBuilder.append("]:flagsArray=");
        localStringBuilder.append(mFlagsArray[i]);
      }
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    return "SparseRectFArray{}";
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeIntArray(mKeys);
    paramParcel.writeFloatArray(mCoordinates);
    paramParcel.writeIntArray(mFlagsArray);
  }
  
  public static final class SparseRectFArrayBuilder
  {
    private static int INITIAL_SIZE = 16;
    private float[] mCoordinates = null;
    private int mCount = 0;
    private int[] mFlagsArray = null;
    private int[] mKeys = null;
    
    public SparseRectFArrayBuilder() {}
    
    private void checkIndex(int paramInt)
    {
      if (mCount == 0) {
        return;
      }
      if (mKeys[(mCount - 1)] < paramInt) {
        return;
      }
      throw new IllegalArgumentException("key must be greater than all existing keys.");
    }
    
    private void ensureBufferSize()
    {
      if (mKeys == null) {
        mKeys = new int[INITIAL_SIZE];
      }
      if (mCoordinates == null) {
        mCoordinates = new float[INITIAL_SIZE * 4];
      }
      if (mFlagsArray == null) {
        mFlagsArray = new int[INITIAL_SIZE];
      }
      int i = mCount + 1;
      Object localObject;
      if (mKeys.length <= i)
      {
        localObject = new int[i * 2];
        System.arraycopy(mKeys, 0, localObject, 0, mCount);
        mKeys = ((int[])localObject);
      }
      int j = (mCount + 1) * 4;
      if (mCoordinates.length <= j)
      {
        localObject = new float[j * 2];
        System.arraycopy(mCoordinates, 0, localObject, 0, mCount * 4);
        mCoordinates = ((float[])localObject);
      }
      if (mFlagsArray.length <= i)
      {
        localObject = new int[i * 2];
        System.arraycopy(mFlagsArray, 0, localObject, 0, mCount);
        mFlagsArray = ((int[])localObject);
      }
    }
    
    public SparseRectFArrayBuilder append(int paramInt1, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt2)
    {
      checkIndex(paramInt1);
      ensureBufferSize();
      int i = mCount * 4;
      mCoordinates[(i + 0)] = paramFloat1;
      mCoordinates[(i + 1)] = paramFloat2;
      mCoordinates[(i + 2)] = paramFloat3;
      mCoordinates[(i + 3)] = paramFloat4;
      i = mCount;
      mFlagsArray[i] = paramInt2;
      mKeys[mCount] = paramInt1;
      mCount += 1;
      return this;
    }
    
    public SparseRectFArray build()
    {
      return new SparseRectFArray(this, null);
    }
    
    public boolean isEmpty()
    {
      boolean bool;
      if (mCount <= 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void reset()
    {
      if (mCount == 0)
      {
        mKeys = null;
        mCoordinates = null;
        mFlagsArray = null;
      }
      mCount = 0;
    }
  }
}
