package android.animation;

import android.content.res.ConstantState;
import java.util.ArrayList;

public abstract class Animator
  implements Cloneable
{
  public static final long DURATION_INFINITE = -1L;
  int mChangingConfigurations = 0;
  private AnimatorConstantState mConstantState;
  ArrayList<AnimatorListener> mListeners = null;
  ArrayList<AnimatorPauseListener> mPauseListeners = null;
  boolean mPaused = false;
  
  public Animator() {}
  
  public void addListener(AnimatorListener paramAnimatorListener)
  {
    if (mListeners == null) {
      mListeners = new ArrayList();
    }
    mListeners.add(paramAnimatorListener);
  }
  
  public void addPauseListener(AnimatorPauseListener paramAnimatorPauseListener)
  {
    if (mPauseListeners == null) {
      mPauseListeners = new ArrayList();
    }
    mPauseListeners.add(paramAnimatorPauseListener);
  }
  
  void animateBasedOnPlayTime(long paramLong1, long paramLong2, boolean paramBoolean) {}
  
  public void appendChangingConfigurations(int paramInt)
  {
    mChangingConfigurations |= paramInt;
  }
  
  public boolean canReverse()
  {
    return false;
  }
  
  public void cancel() {}
  
  public Animator clone()
  {
    try
    {
      Animator localAnimator = (Animator)super.clone();
      ArrayList localArrayList;
      if (mListeners != null)
      {
        localArrayList = new java/util/ArrayList;
        localArrayList.<init>(mListeners);
        mListeners = localArrayList;
      }
      if (mPauseListeners != null)
      {
        localArrayList = new java/util/ArrayList;
        localArrayList.<init>(mPauseListeners);
        mPauseListeners = localArrayList;
      }
      return localAnimator;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      throw new AssertionError();
    }
  }
  
  public ConstantState<Animator> createConstantState()
  {
    return new AnimatorConstantState(this);
  }
  
  public void end() {}
  
  public int getChangingConfigurations()
  {
    return mChangingConfigurations;
  }
  
  public abstract long getDuration();
  
  public TimeInterpolator getInterpolator()
  {
    return null;
  }
  
  public ArrayList<AnimatorListener> getListeners()
  {
    return mListeners;
  }
  
  public abstract long getStartDelay();
  
  public long getTotalDuration()
  {
    long l = getDuration();
    if (l == -1L) {
      return -1L;
    }
    return getStartDelay() + l;
  }
  
  boolean isInitialized()
  {
    return true;
  }
  
  public boolean isPaused()
  {
    return mPaused;
  }
  
  public abstract boolean isRunning();
  
  public boolean isStarted()
  {
    return isRunning();
  }
  
  public void pause()
  {
    if ((isStarted()) && (!mPaused))
    {
      mPaused = true;
      if (mPauseListeners != null)
      {
        ArrayList localArrayList = (ArrayList)mPauseListeners.clone();
        int i = localArrayList.size();
        for (int j = 0; j < i; j++) {
          ((AnimatorPauseListener)localArrayList.get(j)).onAnimationPause(this);
        }
      }
    }
  }
  
  boolean pulseAnimationFrame(long paramLong)
  {
    return false;
  }
  
  public void removeAllListeners()
  {
    if (mListeners != null)
    {
      mListeners.clear();
      mListeners = null;
    }
    if (mPauseListeners != null)
    {
      mPauseListeners.clear();
      mPauseListeners = null;
    }
  }
  
  public void removeListener(AnimatorListener paramAnimatorListener)
  {
    if (mListeners == null) {
      return;
    }
    mListeners.remove(paramAnimatorListener);
    if (mListeners.size() == 0) {
      mListeners = null;
    }
  }
  
  public void removePauseListener(AnimatorPauseListener paramAnimatorPauseListener)
  {
    if (mPauseListeners == null) {
      return;
    }
    mPauseListeners.remove(paramAnimatorPauseListener);
    if (mPauseListeners.size() == 0) {
      mPauseListeners = null;
    }
  }
  
  public void resume()
  {
    if (mPaused)
    {
      int i = 0;
      mPaused = false;
      if (mPauseListeners != null)
      {
        ArrayList localArrayList = (ArrayList)mPauseListeners.clone();
        int j = localArrayList.size();
        while (i < j)
        {
          ((AnimatorPauseListener)localArrayList.get(i)).onAnimationResume(this);
          i++;
        }
      }
    }
  }
  
  public void reverse()
  {
    throw new IllegalStateException("Reverse is not supported");
  }
  
  public void setAllowRunningAsynchronously(boolean paramBoolean) {}
  
  public void setChangingConfigurations(int paramInt)
  {
    mChangingConfigurations = paramInt;
  }
  
  public abstract Animator setDuration(long paramLong);
  
  public abstract void setInterpolator(TimeInterpolator paramTimeInterpolator);
  
  public abstract void setStartDelay(long paramLong);
  
  public void setTarget(Object paramObject) {}
  
  public void setupEndValues() {}
  
  public void setupStartValues() {}
  
  void skipToEndValue(boolean paramBoolean) {}
  
  public void start() {}
  
  void startWithoutPulsing(boolean paramBoolean)
  {
    if (paramBoolean) {
      reverse();
    } else {
      start();
    }
  }
  
  private static class AnimatorConstantState
    extends ConstantState<Animator>
  {
    final Animator mAnimator;
    int mChangingConf;
    
    public AnimatorConstantState(Animator paramAnimator)
    {
      mAnimator = paramAnimator;
      Animator.access$002(mAnimator, this);
      mChangingConf = mAnimator.getChangingConfigurations();
    }
    
    public int getChangingConfigurations()
    {
      return mChangingConf;
    }
    
    public Animator newInstance()
    {
      Animator localAnimator = mAnimator.clone();
      Animator.access$002(localAnimator, this);
      return localAnimator;
    }
  }
  
  public static abstract interface AnimatorListener
  {
    public abstract void onAnimationCancel(Animator paramAnimator);
    
    public abstract void onAnimationEnd(Animator paramAnimator);
    
    public void onAnimationEnd(Animator paramAnimator, boolean paramBoolean)
    {
      onAnimationEnd(paramAnimator);
    }
    
    public abstract void onAnimationRepeat(Animator paramAnimator);
    
    public abstract void onAnimationStart(Animator paramAnimator);
    
    public void onAnimationStart(Animator paramAnimator, boolean paramBoolean)
    {
      onAnimationStart(paramAnimator);
    }
  }
  
  public static abstract interface AnimatorPauseListener
  {
    public abstract void onAnimationPause(Animator paramAnimator);
    
    public abstract void onAnimationResume(Animator paramAnimator);
  }
}
