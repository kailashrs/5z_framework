package android.animation;

import android.view.animation.AnimationUtils;

public class TimeAnimator
  extends ValueAnimator
{
  private TimeListener mListener;
  private long mPreviousTime = -1L;
  
  public TimeAnimator() {}
  
  boolean animateBasedOnTime(long paramLong)
  {
    if (mListener != null)
    {
      long l1 = mStartTime;
      long l2;
      if (mPreviousTime < 0L) {
        l2 = 0L;
      } else {
        l2 = paramLong - mPreviousTime;
      }
      mPreviousTime = paramLong;
      mListener.onTimeUpdate(this, paramLong - l1, l2);
    }
    return false;
  }
  
  void animateValue(float paramFloat) {}
  
  void initAnimation() {}
  
  public void setCurrentPlayTime(long paramLong)
  {
    long l = AnimationUtils.currentAnimationTimeMillis();
    mStartTime = Math.max(mStartTime, l - paramLong);
    mStartTimeCommitted = true;
    animateBasedOnTime(l);
  }
  
  public void setTimeListener(TimeListener paramTimeListener)
  {
    mListener = paramTimeListener;
  }
  
  public void start()
  {
    mPreviousTime = -1L;
    super.start();
  }
  
  public static abstract interface TimeListener
  {
    public abstract void onTimeUpdate(TimeAnimator paramTimeAnimator, long paramLong1, long paramLong2);
  }
}
