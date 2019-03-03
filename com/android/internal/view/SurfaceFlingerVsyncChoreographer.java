package com.android.internal.view;

import android.os.Handler;
import android.os.Message;
import android.view.Choreographer;
import android.view.Display;

public class SurfaceFlingerVsyncChoreographer
{
  private static final long ONE_MS_IN_NS = 1000000L;
  private static final long ONE_S_IN_NS = 1000000000L;
  private final Choreographer mChoreographer;
  private final Handler mHandler;
  private long mSurfaceFlingerOffsetMs;
  
  public SurfaceFlingerVsyncChoreographer(Handler paramHandler, Display paramDisplay, Choreographer paramChoreographer)
  {
    mHandler = paramHandler;
    mChoreographer = paramChoreographer;
    mSurfaceFlingerOffsetMs = calculateAppSurfaceFlingerVsyncOffsetMs(paramDisplay);
  }
  
  private long calculateAppSurfaceFlingerVsyncOffsetMs(Display paramDisplay)
  {
    return Math.max(0L, ((1.0E9F / paramDisplay.getRefreshRate()) - (paramDisplay.getPresentationDeadlineNanos() - 1000000L) - paramDisplay.getAppVsyncOffsetNanos()) / 1000000L);
  }
  
  private long calculateDelay()
  {
    long l1 = System.nanoTime();
    long l2 = mChoreographer.getLastFrameTimeNanos();
    return mSurfaceFlingerOffsetMs - (l1 - l2) / 1000000L;
  }
  
  public long getSurfaceFlingerOffsetMs()
  {
    return mSurfaceFlingerOffsetMs;
  }
  
  public void scheduleAtSfVsync(Handler paramHandler, Message paramMessage)
  {
    long l = calculateDelay();
    if (l <= 0L)
    {
      paramHandler.handleMessage(paramMessage);
    }
    else
    {
      paramMessage.setAsynchronous(true);
      paramHandler.sendMessageDelayed(paramMessage, l);
    }
  }
  
  public void scheduleAtSfVsync(Runnable paramRunnable)
  {
    long l = calculateDelay();
    if (l <= 0L) {
      paramRunnable.run();
    } else {
      mHandler.postDelayed(paramRunnable, l);
    }
  }
}
