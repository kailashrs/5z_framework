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
public class AnticipateInterpolator
  extends BaseInterpolator
  implements NativeInterpolatorFactory
{
  private final float mTension;
  
  public AnticipateInterpolator()
  {
    mTension = 2.0F;
  }
  
  public AnticipateInterpolator(float paramFloat)
  {
    mTension = paramFloat;
  }
  
  public AnticipateInterpolator(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext.getResources(), paramContext.getTheme(), paramAttributeSet);
  }
  
  public AnticipateInterpolator(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet)
  {
    if (paramTheme != null) {
      paramResources = paramTheme.obtainStyledAttributes(paramAttributeSet, R.styleable.AnticipateInterpolator, 0, 0);
    } else {
      paramResources = paramResources.obtainAttributes(paramAttributeSet, R.styleable.AnticipateInterpolator);
    }
    mTension = paramResources.getFloat(0, 2.0F);
    setChangingConfiguration(paramResources.getChangingConfigurations());
    paramResources.recycle();
  }
  
  public long createNativeInterpolator()
  {
    return NativeInterpolatorFactoryHelper.createAnticipateInterpolator(mTension);
  }
  
  public float getInterpolation(float paramFloat)
  {
    return paramFloat * paramFloat * ((mTension + 1.0F) * paramFloat - mTension);
  }
}
