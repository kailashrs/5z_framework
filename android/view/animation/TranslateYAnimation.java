package android.view.animation;

import android.graphics.Matrix;

public class TranslateYAnimation
  extends TranslateAnimation
{
  float[] mTmpValues = new float[9];
  
  public TranslateYAnimation(float paramFloat1, float paramFloat2)
  {
    super(0.0F, 0.0F, paramFloat1, paramFloat2);
  }
  
  public TranslateYAnimation(int paramInt1, float paramFloat1, int paramInt2, float paramFloat2)
  {
    super(0, 0.0F, 0, 0.0F, paramInt1, paramFloat1, paramInt2, paramFloat2);
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    paramTransformation.getMatrix().getValues(mTmpValues);
    float f1 = mFromYDelta;
    float f2 = mToYDelta;
    float f3 = mFromYDelta;
    paramTransformation.getMatrix().setTranslate(mTmpValues[2], f1 + (f2 - f3) * paramFloat);
  }
}
