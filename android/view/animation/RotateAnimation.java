package android.view.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.util.AttributeSet;
import com.android.internal.R.styleable;

public class RotateAnimation
  extends Animation
{
  private float mFromDegrees;
  private float mPivotX;
  private int mPivotXType = 0;
  private float mPivotXValue = 0.0F;
  private float mPivotY;
  private int mPivotYType = 0;
  private float mPivotYValue = 0.0F;
  private float mToDegrees;
  
  public RotateAnimation(float paramFloat1, float paramFloat2)
  {
    mFromDegrees = paramFloat1;
    mToDegrees = paramFloat2;
    mPivotX = 0.0F;
    mPivotY = 0.0F;
  }
  
  public RotateAnimation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    mFromDegrees = paramFloat1;
    mToDegrees = paramFloat2;
    mPivotXType = 0;
    mPivotYType = 0;
    mPivotXValue = paramFloat3;
    mPivotYValue = paramFloat4;
    initializePivotPoint();
  }
  
  public RotateAnimation(float paramFloat1, float paramFloat2, int paramInt1, float paramFloat3, int paramInt2, float paramFloat4)
  {
    mFromDegrees = paramFloat1;
    mToDegrees = paramFloat2;
    mPivotXValue = paramFloat3;
    mPivotXType = paramInt1;
    mPivotYValue = paramFloat4;
    mPivotYType = paramInt2;
    initializePivotPoint();
  }
  
  public RotateAnimation(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RotateAnimation);
    mFromDegrees = paramContext.getFloat(0, 0.0F);
    mToDegrees = paramContext.getFloat(1, 0.0F);
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(2));
    mPivotXType = type;
    mPivotXValue = value;
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(3));
    mPivotYType = type;
    mPivotYValue = value;
    paramContext.recycle();
    initializePivotPoint();
  }
  
  private void initializePivotPoint()
  {
    if (mPivotXType == 0) {
      mPivotX = mPivotXValue;
    }
    if (mPivotYType == 0) {
      mPivotY = mPivotYValue;
    }
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    paramFloat = mFromDegrees + (mToDegrees - mFromDegrees) * paramFloat;
    float f = getScaleFactor();
    if ((mPivotX == 0.0F) && (mPivotY == 0.0F)) {
      paramTransformation.getMatrix().setRotate(paramFloat);
    } else {
      paramTransformation.getMatrix().setRotate(paramFloat, mPivotX * f, mPivotY * f);
    }
  }
  
  public void initialize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.initialize(paramInt1, paramInt2, paramInt3, paramInt4);
    mPivotX = resolveSize(mPivotXType, mPivotXValue, paramInt1, paramInt3);
    mPivotY = resolveSize(mPivotYType, mPivotYValue, paramInt2, paramInt4);
  }
}
