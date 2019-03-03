package com.android.internal.view.animation;

import android.animation.TimeInterpolator;
import android.view.Choreographer;

@HasNativeInterpolator
public class FallbackLUTInterpolator
  implements NativeInterpolatorFactory, TimeInterpolator
{
  private static final int MAX_SAMPLE_POINTS = 300;
  private final float[] mLut;
  private TimeInterpolator mSourceInterpolator;
  
  public FallbackLUTInterpolator(TimeInterpolator paramTimeInterpolator, long paramLong)
  {
    mSourceInterpolator = paramTimeInterpolator;
    mLut = createLUT(paramTimeInterpolator, paramLong);
  }
  
  private static float[] createLUT(TimeInterpolator paramTimeInterpolator, long paramLong)
  {
    int i = (int)(Choreographer.getInstance().getFrameIntervalNanos() / 1000000L);
    int j = Math.min(Math.max(2, (int)Math.ceil(paramLong / i)), 300);
    float[] arrayOfFloat = new float[j];
    float f = j - 1;
    for (i = 0; i < j; i++) {
      arrayOfFloat[i] = paramTimeInterpolator.getInterpolation(i / f);
    }
    return arrayOfFloat;
  }
  
  public static long createNativeInterpolator(TimeInterpolator paramTimeInterpolator, long paramLong)
  {
    return NativeInterpolatorFactoryHelper.createLutInterpolator(createLUT(paramTimeInterpolator, paramLong));
  }
  
  public long createNativeInterpolator()
  {
    return NativeInterpolatorFactoryHelper.createLutInterpolator(mLut);
  }
  
  public float getInterpolation(float paramFloat)
  {
    return mSourceInterpolator.getInterpolation(paramFloat);
  }
}
