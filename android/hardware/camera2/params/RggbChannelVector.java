package android.hardware.camera2.params;

import com.android.internal.util.Preconditions;

public final class RggbChannelVector
{
  public static final int BLUE = 3;
  public static final int COUNT = 4;
  public static final int GREEN_EVEN = 1;
  public static final int GREEN_ODD = 2;
  public static final int RED = 0;
  private final float mBlue;
  private final float mGreenEven;
  private final float mGreenOdd;
  private final float mRed;
  
  public RggbChannelVector(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    mRed = Preconditions.checkArgumentFinite(paramFloat1, "red");
    mGreenEven = Preconditions.checkArgumentFinite(paramFloat2, "greenEven");
    mGreenOdd = Preconditions.checkArgumentFinite(paramFloat3, "greenOdd");
    mBlue = Preconditions.checkArgumentFinite(paramFloat4, "blue");
  }
  
  private String toShortString()
  {
    return String.format("{R:%f, G_even:%f, G_odd:%f, B:%f}", new Object[] { Float.valueOf(mRed), Float.valueOf(mGreenEven), Float.valueOf(mGreenOdd), Float.valueOf(mBlue) });
  }
  
  public void copyTo(float[] paramArrayOfFloat, int paramInt)
  {
    Preconditions.checkNotNull(paramArrayOfFloat, "destination must not be null");
    if (paramArrayOfFloat.length - paramInt >= 4)
    {
      paramArrayOfFloat[(paramInt + 0)] = mRed;
      paramArrayOfFloat[(paramInt + 1)] = mGreenEven;
      paramArrayOfFloat[(paramInt + 2)] = mGreenOdd;
      paramArrayOfFloat[(paramInt + 3)] = mBlue;
      return;
    }
    throw new ArrayIndexOutOfBoundsException("destination too small to fit elements");
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof RggbChannelVector))
    {
      paramObject = (RggbChannelVector)paramObject;
      boolean bool2 = bool1;
      if (mRed == mRed)
      {
        bool2 = bool1;
        if (mGreenEven == mGreenEven)
        {
          bool2 = bool1;
          if (mGreenOdd == mGreenOdd)
          {
            bool2 = bool1;
            if (mBlue == mBlue) {
              bool2 = true;
            }
          }
        }
      }
      return bool2;
    }
    return false;
  }
  
  public float getBlue()
  {
    return mBlue;
  }
  
  public float getComponent(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < 4))
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unhandled case ");
        localStringBuilder.append(paramInt);
        throw new AssertionError(localStringBuilder.toString());
      case 3: 
        return mBlue;
      case 2: 
        return mGreenOdd;
      case 1: 
        return mGreenEven;
      }
      return mRed;
    }
    throw new IllegalArgumentException("Color channel out of range");
  }
  
  public float getGreenEven()
  {
    return mGreenEven;
  }
  
  public float getGreenOdd()
  {
    return mGreenOdd;
  }
  
  public final float getRed()
  {
    return mRed;
  }
  
  public int hashCode()
  {
    return Float.floatToIntBits(mRed) ^ Float.floatToIntBits(mGreenEven) ^ Float.floatToIntBits(mGreenOdd) ^ Float.floatToIntBits(mBlue);
  }
  
  public String toString()
  {
    return String.format("RggbChannelVector%s", new Object[] { toShortString() });
  }
}
