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
public class DecelerateInterpolator
  extends BaseInterpolator
  implements NativeInterpolatorFactory
{
  private float mFactor = 1.0F;
  
  public DecelerateInterpolator() {}
  
  public DecelerateInterpolator(float paramFloat)
  {
    mFactor = paramFloat;
  }
  
  public DecelerateInterpolator(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext.getResources(), paramContext.getTheme(), paramAttributeSet);
  }
  
  public DecelerateInterpolator(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet)
  {
    if (paramTheme != null) {
      paramResources = paramTheme.obtainStyledAttributes(paramAttributeSet, R.styleable.DecelerateInterpolator, 0, 0);
    } else {
      paramResources = paramResources.obtainAttributes(paramAttributeSet, R.styleable.DecelerateInterpolator);
    }
    mFactor = paramResources.getFloat(0, 1.0F);
    setChangingConfiguration(paramResources.getChangingConfigurations());
    paramResources.recycle();
  }
  
  public long createNativeInterpolator()
  {
    return NativeInterpolatorFactoryHelper.createDecelerateInterpolator(mFactor);
  }
  
  public float getInterpolation(float paramFloat)
  {
    if (mFactor == 1.0F) {
      paramFloat = 1.0F - (1.0F - paramFloat) * (1.0F - paramFloat);
    } else {
      paramFloat = (float)(1.0D - Math.pow(1.0F - paramFloat, 2.0F * mFactor));
    }
    return paramFloat;
  }
}
