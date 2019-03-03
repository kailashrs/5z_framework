package com.android.internal.transition;

import android.animation.TimeInterpolator;
import android.view.animation.PathInterpolator;

class TransitionConstants
{
  static final TimeInterpolator FAST_OUT_SLOW_IN = new PathInterpolator(0.4F, 0.0F, 0.2F, 1.0F);
  static final TimeInterpolator LINEAR_OUT_SLOW_IN = new PathInterpolator(0.0F, 0.0F, 0.2F, 1.0F);
  
  TransitionConstants() {}
}
