package android.view.animation;

import android.content.Context;
import android.util.AttributeSet;
import com.android.internal.view.animation.HasNativeInterpolator;
import com.android.internal.view.animation.NativeInterpolatorFactory;
import com.android.internal.view.animation.NativeInterpolatorFactoryHelper;

@HasNativeInterpolator
public class AccelerateDecelerateInterpolator
  extends BaseInterpolator
  implements NativeInterpolatorFactory
{
  public AccelerateDecelerateInterpolator() {}
  
  public AccelerateDecelerateInterpolator(Context paramContext, AttributeSet paramAttributeSet) {}
  
  public long createNativeInterpolator()
  {
    return NativeInterpolatorFactoryHelper.createAccelerateDecelerateInterpolator();
  }
  
  public float getInterpolation(float paramFloat)
  {
    return (float)(Math.cos((1.0F + paramFloat) * 3.141592653589793D) / 2.0D) + 0.5F;
  }
}
