package android.graphics;

public class LinearGradient
  extends Shader
{
  private static final int TYPE_COLORS_AND_POSITIONS = 1;
  private static final int TYPE_COLOR_START_AND_COLOR_END = 2;
  private int mColor0;
  private int mColor1;
  private int[] mColors;
  private float[] mPositions;
  private Shader.TileMode mTileMode;
  private int mType;
  private float mX0;
  private float mX1;
  private float mY0;
  private float mY1;
  
  public LinearGradient(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2, Shader.TileMode paramTileMode)
  {
    mType = 2;
    mX0 = paramFloat1;
    mY0 = paramFloat2;
    mX1 = paramFloat3;
    mY1 = paramFloat4;
    mColor0 = paramInt1;
    mColor1 = paramInt2;
    mColors = null;
    mPositions = null;
    mTileMode = paramTileMode;
  }
  
  public LinearGradient(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int[] paramArrayOfInt, float[] paramArrayOfFloat, Shader.TileMode paramTileMode)
  {
    if (paramArrayOfInt.length >= 2)
    {
      if ((paramArrayOfFloat != null) && (paramArrayOfInt.length != paramArrayOfFloat.length)) {
        throw new IllegalArgumentException("color and position arrays must be of equal length");
      }
      mType = 1;
      mX0 = paramFloat1;
      mY0 = paramFloat2;
      mX1 = paramFloat3;
      mY1 = paramFloat4;
      mColors = ((int[])paramArrayOfInt.clone());
      if (paramArrayOfFloat != null) {
        paramArrayOfInt = (float[])paramArrayOfFloat.clone();
      } else {
        paramArrayOfInt = null;
      }
      mPositions = paramArrayOfInt;
      mTileMode = paramTileMode;
      return;
    }
    throw new IllegalArgumentException("needs >= 2 number of colors");
  }
  
  private native long nativeCreate1(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int[] paramArrayOfInt, float[] paramArrayOfFloat, int paramInt);
  
  private native long nativeCreate2(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2, int paramInt3);
  
  protected Shader copy()
  {
    Object localObject;
    if (mType == 1)
    {
      float f1 = mX0;
      float f2 = mY0;
      float f3 = mX1;
      float f4 = mY1;
      int[] arrayOfInt = (int[])mColors.clone();
      if (mPositions != null) {}
      for (localObject = (float[])mPositions.clone();; localObject = null) {
        break;
      }
      localObject = new LinearGradient(f1, f2, f3, f4, arrayOfInt, (float[])localObject, mTileMode);
    }
    else
    {
      localObject = new LinearGradient(mX0, mY0, mX1, mY1, mColor0, mColor1, mTileMode);
    }
    copyLocalMatrix((Shader)localObject);
    return localObject;
  }
  
  long createNativeInstance(long paramLong)
  {
    if (mType == 1) {
      return nativeCreate1(paramLong, mX0, mY0, mX1, mY1, mColors, mPositions, mTileMode.nativeInt);
    }
    return nativeCreate2(paramLong, mX0, mY0, mX1, mY1, mColor0, mColor1, mTileMode.nativeInt);
  }
}
