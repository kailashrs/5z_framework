package android.view.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.util.AttributeSet;
import com.android.internal.R.styleable;

public class TranslateAnimation
  extends Animation
{
  protected float mFromXDelta;
  private int mFromXType = 0;
  protected float mFromXValue = 0.0F;
  protected float mFromYDelta;
  private int mFromYType = 0;
  protected float mFromYValue = 0.0F;
  protected float mToXDelta;
  private int mToXType = 0;
  protected float mToXValue = 0.0F;
  protected float mToYDelta;
  private int mToYType = 0;
  protected float mToYValue = 0.0F;
  
  public TranslateAnimation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    mFromXValue = paramFloat1;
    mToXValue = paramFloat2;
    mFromYValue = paramFloat3;
    mToYValue = paramFloat4;
    mFromXType = 0;
    mToXType = 0;
    mFromYType = 0;
    mToYType = 0;
  }
  
  public TranslateAnimation(int paramInt1, float paramFloat1, int paramInt2, float paramFloat2, int paramInt3, float paramFloat3, int paramInt4, float paramFloat4)
  {
    mFromXValue = paramFloat1;
    mToXValue = paramFloat2;
    mFromYValue = paramFloat3;
    mToYValue = paramFloat4;
    mFromXType = paramInt1;
    mToXType = paramInt2;
    mFromYType = paramInt3;
    mToYType = paramInt4;
  }
  
  public TranslateAnimation(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TranslateAnimation);
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(0));
    mFromXType = type;
    mFromXValue = value;
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(1));
    mToXType = type;
    mToXValue = value;
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(2));
    mFromYType = type;
    mFromYValue = value;
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(3));
    mToYType = type;
    mToYValue = value;
    paramContext.recycle();
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    float f1 = mFromXDelta;
    float f2 = mFromYDelta;
    if (mFromXDelta != mToXDelta) {
      f1 = mFromXDelta + (mToXDelta - mFromXDelta) * paramFloat;
    }
    if (mFromYDelta != mToYDelta) {
      f2 = mFromYDelta + (mToYDelta - mFromYDelta) * paramFloat;
    }
    paramTransformation.getMatrix().setTranslate(f1, f2);
  }
  
  public void initialize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.initialize(paramInt1, paramInt2, paramInt3, paramInt4);
    mFromXDelta = resolveSize(mFromXType, mFromXValue, paramInt1, paramInt3);
    mToXDelta = resolveSize(mToXType, mToXValue, paramInt1, paramInt3);
    mFromYDelta = resolveSize(mFromYType, mFromYValue, paramInt2, paramInt4);
    mToYDelta = resolveSize(mToYType, mToYValue, paramInt2, paramInt4);
  }
}
