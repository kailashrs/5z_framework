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
public class CycleInterpolator
  extends BaseInterpolator
  implements NativeInterpolatorFactory
{
  private float mCycles;
  
  public CycleInterpolator(float paramFloat)
  {
    mCycles = paramFloat;
  }
  
  public CycleInterpolator(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext.getResources(), paramContext.getTheme(), paramAttributeSet);
  }
  
  public CycleInterpolator(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet)
  {
    if (paramTheme != null) {
      paramResources = paramTheme.obtainStyledAttributes(paramAttributeSet, R.styleable.CycleInterpolator, 0, 0);
    } else {
      paramResources = paramResources.obtainAttributes(paramAttributeSet, R.styleable.CycleInterpolator);
    }
    mCycles = paramResources.getFloat(0, 1.0F);
    setChangingConfiguration(paramResources.getChangingConfigurations());
    paramResources.recycle();
  }
  
  public long createNativeInterpolator()
  {
    return NativeInterpolatorFactoryHelper.createCycleInterpolator(mCycles);
  }
  
  public float getInterpolation(float paramFloat)
  {
    return (float)Math.sin(2.0F * mCycles * 3.141592653589793D * paramFloat);
  }
}
