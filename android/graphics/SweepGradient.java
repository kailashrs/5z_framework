package android.graphics;

public class SweepGradient
  extends Shader
{
  private static final int TYPE_COLORS_AND_POSITIONS = 1;
  private static final int TYPE_COLOR_START_AND_COLOR_END = 2;
  private int mColor0;
  private int mColor1;
  private int[] mColors;
  private float mCx;
  private float mCy;
  private float[] mPositions;
  private int mType;
  
  public SweepGradient(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    mType = 2;
    mCx = paramFloat1;
    mCy = paramFloat2;
    mColor0 = paramInt1;
    mColor1 = paramInt2;
    mColors = null;
    mPositions = null;
  }
  
  public SweepGradient(float paramFloat1, float paramFloat2, int[] paramArrayOfInt, float[] paramArrayOfFloat)
  {
    if (paramArrayOfInt.length >= 2)
    {
      if ((paramArrayOfFloat != null) && (paramArrayOfInt.length != paramArrayOfFloat.length)) {
        throw new IllegalArgumentException("color and position arrays must be of equal length");
      }
      mType = 1;
      mCx = paramFloat1;
      mCy = paramFloat2;
      mColors = ((int[])paramArrayOfInt.clone());
      if (paramArrayOfFloat != null) {
        paramArrayOfInt = (float[])paramArrayOfFloat.clone();
      } else {
        paramArrayOfInt = null;
      }
      mPositions = paramArrayOfInt;
      return;
    }
    throw new IllegalArgumentException("needs >= 2 number of colors");
  }
  
  private static native long nativeCreate1(long paramLong, float paramFloat1, float paramFloat2, int[] paramArrayOfInt, float[] paramArrayOfFloat);
  
  private static native long nativeCreate2(long paramLong, float paramFloat1, float paramFloat2, int paramInt1, int paramInt2);
  
  protected Shader copy()
  {
    Object localObject;
    if (mType == 1)
    {
      float f1 = mCx;
      float f2 = mCy;
      int[] arrayOfInt = (int[])mColors.clone();
      if (mPositions != null) {
        localObject = (float[])mPositions.clone();
      } else {
        localObject = null;
      }
      localObject = new SweepGradient(f1, f2, arrayOfInt, (float[])localObject);
    }
    else
    {
      localObject = new SweepGradient(mCx, mCy, mColor0, mColor1);
    }
    copyLocalMatrix((Shader)localObject);
    return localObject;
  }
  
  long createNativeInstance(long paramLong)
  {
    if (mType == 1) {
      return nativeCreate1(paramLong, mCx, mCy, mColors, mPositions);
    }
    return nativeCreate2(paramLong, mCx, mCy, mColor0, mColor1);
  }
}
