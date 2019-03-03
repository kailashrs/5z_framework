package android.view;

import android.animation.TimeInterpolator;
import com.android.internal.view.animation.FallbackLUTInterpolator;
import com.android.internal.view.animation.NativeInterpolatorFactory;
import com.android.internal.view.animation.NativeInterpolatorFactoryHelper;

public class RenderNodeAnimatorSetHelper
{
  public RenderNodeAnimatorSetHelper() {}
  
  public static long createNativeInterpolator(TimeInterpolator paramTimeInterpolator, long paramLong)
  {
    if (paramTimeInterpolator == null) {
      return NativeInterpolatorFactoryHelper.createLinearInterpolator();
    }
    if (RenderNodeAnimator.isNativeInterpolator(paramTimeInterpolator)) {
      return ((NativeInterpolatorFactory)paramTimeInterpolator).createNativeInterpolator();
    }
    return FallbackLUTInterpolator.createNativeInterpolator(paramTimeInterpolator, paramLong);
  }
  
  public static RenderNode getTarget(DisplayListCanvas paramDisplayListCanvas)
  {
    return mNode;
  }
}
