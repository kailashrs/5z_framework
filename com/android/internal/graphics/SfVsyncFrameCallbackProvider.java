package com.android.internal.graphics;

import android.animation.AnimationHandler.AnimationFrameCallbackProvider;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;

public final class SfVsyncFrameCallbackProvider
  implements AnimationHandler.AnimationFrameCallbackProvider
{
  private final Choreographer mChoreographer;
  
  public SfVsyncFrameCallbackProvider()
  {
    mChoreographer = Choreographer.getSfInstance();
  }
  
  public SfVsyncFrameCallbackProvider(Choreographer paramChoreographer)
  {
    mChoreographer = paramChoreographer;
  }
  
  public long getFrameDelay()
  {
    return Choreographer.getFrameDelay();
  }
  
  public long getFrameTime()
  {
    return mChoreographer.getFrameTime();
  }
  
  public void postCommitCallback(Runnable paramRunnable)
  {
    mChoreographer.postCallback(3, paramRunnable, null);
  }
  
  public void postFrameCallback(Choreographer.FrameCallback paramFrameCallback)
  {
    mChoreographer.postFrameCallback(paramFrameCallback);
  }
  
  public void setFrameDelay(long paramLong)
  {
    Choreographer.setFrameDelay(paramLong);
  }
}
