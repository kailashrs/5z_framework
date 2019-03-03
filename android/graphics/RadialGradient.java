package android.graphics;

public class RadialGradient
  extends Shader
{
  private static final int TYPE_COLORS_AND_POSITIONS = 1;
  private static final int TYPE_COLOR_CENTER_AND_COLOR_EDGE = 2;
  private int mCenterColor;
  private int[] mColors;
  private int mEdgeColor;
  private float[] mPositions;
  private float mRadius;
  private Shader.TileMode mTileMode;
  private int mType;
  private float mX;
  private float mY;
  
  public RadialGradient(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2, Shader.TileMode paramTileMode)
  {
    if (paramFloat3 > 0.0F)
    {
      mType = 2;
      mX = paramFloat1;
      mY = paramFloat2;
      mRadius = paramFloat3;
      mCenterColor = paramInt1;
      mEdgeColor = paramInt2;
      mTileMode = paramTileMode;
      return;
    }
    throw new IllegalArgumentException("radius must be > 0");
  }
  
  public RadialGradient(float paramFloat1, float paramFloat2, float paramFloat3, int[] paramArrayOfInt, float[] paramArrayOfFloat, Shader.TileMode paramTileMode)
  {
    if (paramFloat3 > 0.0F)
    {
      if (paramArrayOfInt.length >= 2)
      {
        if ((paramArrayOfFloat != null) && (paramArrayOfInt.length != paramArrayOfFloat.length)) {
          throw new IllegalArgumentException("color and position arrays must be of equal length");
        }
        mType = 1;
        mX = paramFloat1;
        mY = paramFloat2;
        mRadius = paramFloat3;
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
    throw new IllegalArgumentException("radius must be > 0");
  }
  
  private static native long nativeCreate1(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, int[] paramArrayOfInt, float[] paramArrayOfFloat, int paramInt);
  
  private static native long nativeCreate2(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2, int paramInt3);
  
  protected Shader copy()
  {
    Object localObject;
    if (mType == 1)
    {
      float f1 = mX;
      float f2 = mY;
      float f3 = mRadius;
      int[] arrayOfInt = (int[])mColors.clone();
      if (mPositions != null) {}
      for (localObject = (float[])mPositions.clone();; localObject = null) {
        break;
      }
      localObject = new RadialGradient(f1, f2, f3, arrayOfInt, (float[])localObject, mTileMode);
    }
    else
    {
      localObject = new RadialGradient(mX, mY, mRadius, mCenterColor, mEdgeColor, mTileMode);
    }
    copyLocalMatrix((Shader)localObject);
    return localObject;
  }
  
  long createNativeInstance(long paramLong)
  {
    if (mType == 1) {
      return nativeCreate1(paramLong, mX, mY, mRadius, mColors, mPositions, mTileMode.nativeInt);
    }
    return nativeCreate2(paramLong, mX, mY, mRadius, mCenterColor, mEdgeColor, mTileMode.nativeInt);
  }
}
