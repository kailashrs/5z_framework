package android.view.animation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.TypedValue;
import com.android.internal.R.styleable;

public class ScaleAnimation
  extends Animation
{
  private float mFromX;
  private int mFromXData = 0;
  private int mFromXType = 0;
  private float mFromY;
  private int mFromYData = 0;
  private int mFromYType = 0;
  private float mPivotX;
  private int mPivotXType = 0;
  private float mPivotXValue = 0.0F;
  private float mPivotY;
  private int mPivotYType = 0;
  private float mPivotYValue = 0.0F;
  private final Resources mResources;
  private float mToX;
  private int mToXData = 0;
  private int mToXType = 0;
  private float mToY;
  private int mToYData = 0;
  private int mToYType = 0;
  
  public ScaleAnimation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    mResources = null;
    mFromX = paramFloat1;
    mToX = paramFloat2;
    mFromY = paramFloat3;
    mToY = paramFloat4;
    mPivotX = 0.0F;
    mPivotY = 0.0F;
  }
  
  public ScaleAnimation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    mResources = null;
    mFromX = paramFloat1;
    mToX = paramFloat2;
    mFromY = paramFloat3;
    mToY = paramFloat4;
    mPivotXType = 0;
    mPivotYType = 0;
    mPivotXValue = paramFloat5;
    mPivotYValue = paramFloat6;
    initializePivotPoint();
  }
  
  public ScaleAnimation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, float paramFloat5, int paramInt2, float paramFloat6)
  {
    mResources = null;
    mFromX = paramFloat1;
    mToX = paramFloat2;
    mFromY = paramFloat3;
    mToY = paramFloat4;
    mPivotXValue = paramFloat5;
    mPivotXType = paramInt1;
    mPivotYValue = paramFloat6;
    mPivotYType = paramInt2;
    initializePivotPoint();
  }
  
  public ScaleAnimation(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    mResources = paramContext.getResources();
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ScaleAnimation);
    paramAttributeSet = paramContext.peekValue(2);
    mFromX = 0.0F;
    if (paramAttributeSet != null) {
      if (type == 4)
      {
        mFromX = paramAttributeSet.getFloat();
      }
      else
      {
        mFromXType = type;
        mFromXData = data;
      }
    }
    paramAttributeSet = paramContext.peekValue(3);
    mToX = 0.0F;
    if (paramAttributeSet != null) {
      if (type == 4)
      {
        mToX = paramAttributeSet.getFloat();
      }
      else
      {
        mToXType = type;
        mToXData = data;
      }
    }
    paramAttributeSet = paramContext.peekValue(4);
    mFromY = 0.0F;
    if (paramAttributeSet != null) {
      if (type == 4)
      {
        mFromY = paramAttributeSet.getFloat();
      }
      else
      {
        mFromYType = type;
        mFromYData = data;
      }
    }
    paramAttributeSet = paramContext.peekValue(5);
    mToY = 0.0F;
    if (paramAttributeSet != null) {
      if (type == 4)
      {
        mToY = paramAttributeSet.getFloat();
      }
      else
      {
        mToYType = type;
        mToYData = data;
      }
    }
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(0));
    mPivotXType = type;
    mPivotXValue = value;
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(1));
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
    float f1 = 1.0F;
    float f2 = 1.0F;
    float f3 = getScaleFactor();
    if ((mFromX != 1.0F) || (mToX != 1.0F)) {
      f1 = mFromX + (mToX - mFromX) * paramFloat;
    }
    if ((mFromY != 1.0F) || (mToY != 1.0F)) {
      f2 = mFromY + (mToY - mFromY) * paramFloat;
    }
    if ((mPivotX == 0.0F) && (mPivotY == 0.0F)) {
      paramTransformation.getMatrix().setScale(f1, f2);
    } else {
      paramTransformation.getMatrix().setScale(f1, f2, mPivotX * f3, mPivotY * f3);
    }
  }
  
  public void initialize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.initialize(paramInt1, paramInt2, paramInt3, paramInt4);
    mFromX = resolveScale(mFromX, mFromXType, mFromXData, paramInt1, paramInt3);
    mToX = resolveScale(mToX, mToXType, mToXData, paramInt1, paramInt3);
    mFromY = resolveScale(mFromY, mFromYType, mFromYData, paramInt2, paramInt4);
    mToY = resolveScale(mToY, mToYType, mToYData, paramInt2, paramInt4);
    mPivotX = resolveSize(mPivotXType, mPivotXValue, paramInt1, paramInt3);
    mPivotY = resolveSize(mPivotYType, mPivotYValue, paramInt2, paramInt4);
  }
  
  float resolveScale(float paramFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramInt1 == 6) {}
    for (paramFloat = TypedValue.complexToFraction(paramInt2, paramInt3, paramInt4);; paramFloat = TypedValue.complexToDimension(paramInt2, mResources.getDisplayMetrics()))
    {
      break;
      if (paramInt1 != 5) {
        return paramFloat;
      }
    }
    if (paramInt3 == 0) {
      return 1.0F;
    }
    return paramFloat / paramInt3;
    return paramFloat;
  }
}
