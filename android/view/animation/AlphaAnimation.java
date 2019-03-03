package android.view.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.android.internal.R.styleable;

public class AlphaAnimation
  extends Animation
{
  private float mFromAlpha;
  private float mToAlpha;
  
  public AlphaAnimation(float paramFloat1, float paramFloat2)
  {
    mFromAlpha = paramFloat1;
    mToAlpha = paramFloat2;
  }
  
  public AlphaAnimation(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AlphaAnimation);
    mFromAlpha = paramContext.getFloat(0, 1.0F);
    mToAlpha = paramContext.getFloat(1, 1.0F);
    paramContext.recycle();
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    float f = mFromAlpha;
    paramTransformation.setAlpha((mToAlpha - f) * paramFloat + f);
  }
  
  public boolean hasAlpha()
  {
    return true;
  }
  
  public boolean willChangeBounds()
  {
    return false;
  }
  
  public boolean willChangeTransformationMatrix()
  {
    return false;
  }
}
