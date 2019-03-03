package android.view.animation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.android.internal.R.styleable;
import com.android.internal.view.animation.HasNativeInterpolator;
import com.android.internal.view.animation.NativeInterpolatorFactory;
import com.android.internal.view.animation.NativeInterpolatorFactoryHelper;

@HasNativeInterpolator
public class AccelerateInterpolator
  extends BaseInterpolator
  implements NativeInterpolatorFactory
{
  private final double mDoubleFactor;
  private final float mFactor;
  
  public AccelerateInterpolator()
  {
    mFactor = 1.0F;
    mDoubleFactor = 2.0D;
  }
  
  public AccelerateInterpolator(float paramFloat)
  {
    mFactor = paramFloat;
    mDoubleFactor = (2.0F * mFactor);
  }
  
  public AccelerateInterpolator(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext.getResources(), paramContext.getTheme(), paramAttributeSet);
  }
  
  public AccelerateInterpolator(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet)
  {
    if (paramTheme != null) {
      paramResources = paramTheme.obtainStyledAttributes(paramAttributeSet, R.styleable.AccelerateInterpolator, 0, 0);
    } else {
      paramResources = paramResources.obtainAttributes(paramAttributeSet, R.styleable.AccelerateInterpolator);
    }
    mFactor = paramResources.getFloat(0, 1.0F);
    mDoubleFactor = (2.0F * mFactor);
    setChangingConfiguration(paramResources.getChangingConfigurations());
    paramResources.recycle();
  }
  
  public long createNativeInterpolator()
  {
    return NativeInterpolatorFactoryHelper.createAccelerateInterpolator(mFactor);
  }
  
  public float getInterpolation(float paramFloat)
  {
    if (mFactor == 1.0F) {
      return paramFloat * paramFloat;
    }
    return (float)Math.pow(paramFloat, mDoubleFactor);
  }
}
