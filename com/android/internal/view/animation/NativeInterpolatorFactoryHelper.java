package com.android.internal.view.animation;

public final class NativeInterpolatorFactoryHelper
{
  private NativeInterpolatorFactoryHelper() {}
  
  public static native long createAccelerateDecelerateInterpolator();
  
  public static native long createAccelerateInterpolator(float paramFloat);
  
  public static native long createAnticipateInterpolator(float paramFloat);
  
  public static native long createAnticipateOvershootInterpolator(float paramFloat);
  
  public static native long createBounceInterpolator();
  
  public static native long createCycleInterpolator(float paramFloat);
  
  public static native long createDecelerateInterpolator(float paramFloat);
  
  public static native long createLinearInterpolator();
  
  public static native long createLutInterpolator(float[] paramArrayOfFloat);
  
  public static native long createOvershootInterpolator(float paramFloat);
  
  public static native long createPathInterpolator(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2);
}
