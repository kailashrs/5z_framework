package android.animation;

import android.os.SystemClock;
import android.util.ArrayMap;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import java.util.ArrayList;

public class AnimationHandler
{
  public static final ThreadLocal<AnimationHandler> sAnimatorHandler = new ThreadLocal();
  private final ArrayList<AnimationFrameCallback> mAnimationCallbacks = new ArrayList();
  private final ArrayList<AnimationFrameCallback> mCommitCallbacks = new ArrayList();
  private final ArrayMap<AnimationFrameCallback, Long> mDelayedCallbackStartTime = new ArrayMap();
  private final Choreographer.FrameCallback mFrameCallback = new Choreographer.FrameCallback()
  {
    public void doFrame(long paramAnonymousLong)
    {
      AnimationHandler.this.doAnimationFrame(AnimationHandler.access$000(AnimationHandler.this).getFrameTime());
      if (mAnimationCallbacks.size() > 0) {
        AnimationHandler.this.getProvider().postFrameCallback(this);
      }
    }
  };
  private boolean mListDirty = false;
  private AnimationFrameCallbackProvider mProvider;
  
  public AnimationHandler() {}
  
  private void cleanUpList()
  {
    if (mListDirty)
    {
      for (int i = mAnimationCallbacks.size() - 1; i >= 0; i--) {
        if (mAnimationCallbacks.get(i) == null) {
          mAnimationCallbacks.remove(i);
        }
      }
      mListDirty = false;
    }
  }
  
  private void commitAnimationFrame(AnimationFrameCallback paramAnimationFrameCallback, long paramLong)
  {
    if ((!mDelayedCallbackStartTime.containsKey(paramAnimationFrameCallback)) && (mCommitCallbacks.contains(paramAnimationFrameCallback)))
    {
      paramAnimationFrameCallback.commitAnimationFrame(paramLong);
      mCommitCallbacks.remove(paramAnimationFrameCallback);
    }
  }
  
  private void doAnimationFrame(long paramLong)
  {
    long l = SystemClock.uptimeMillis();
    int i = mAnimationCallbacks.size();
    for (int j = 0; j < i; j++)
    {
      final AnimationFrameCallback localAnimationFrameCallback = (AnimationFrameCallback)mAnimationCallbacks.get(j);
      if ((localAnimationFrameCallback != null) && (isCallbackDue(localAnimationFrameCallback, l)))
      {
        localAnimationFrameCallback.doAnimationFrame(paramLong);
        if (mCommitCallbacks.contains(localAnimationFrameCallback)) {
          getProvider().postCommitCallback(new Runnable()
          {
            public void run()
            {
              AnimationHandler.this.commitAnimationFrame(localAnimationFrameCallback, AnimationHandler.access$000(AnimationHandler.this).getFrameTime());
            }
          });
        }
      }
    }
    cleanUpList();
  }
  
  public static int getAnimationCount()
  {
    AnimationHandler localAnimationHandler = (AnimationHandler)sAnimatorHandler.get();
    if (localAnimationHandler == null) {
      return 0;
    }
    return localAnimationHandler.getCallbackSize();
  }
  
  private int getCallbackSize()
  {
    int i = 0;
    int j = mAnimationCallbacks.size() - 1;
    while (j >= 0)
    {
      int k = i;
      if (mAnimationCallbacks.get(j) != null) {
        k = i + 1;
      }
      j--;
      i = k;
    }
    return i;
  }
  
  public static long getFrameDelay()
  {
    return getInstance().getProvider().getFrameDelay();
  }
  
  public static AnimationHandler getInstance()
  {
    if (sAnimatorHandler.get() == null) {
      sAnimatorHandler.set(new AnimationHandler());
    }
    return (AnimationHandler)sAnimatorHandler.get();
  }
  
  private AnimationFrameCallbackProvider getProvider()
  {
    if (mProvider == null) {
      mProvider = new MyFrameCallbackProvider(null);
    }
    return mProvider;
  }
  
  private boolean isCallbackDue(AnimationFrameCallback paramAnimationFrameCallback, long paramLong)
  {
    Long localLong = (Long)mDelayedCallbackStartTime.get(paramAnimationFrameCallback);
    if (localLong == null) {
      return true;
    }
    if (localLong.longValue() < paramLong)
    {
      mDelayedCallbackStartTime.remove(paramAnimationFrameCallback);
      return true;
    }
    return false;
  }
  
  public static void setFrameDelay(long paramLong)
  {
    getInstance().getProvider().setFrameDelay(paramLong);
  }
  
  public void addAnimationFrameCallback(AnimationFrameCallback paramAnimationFrameCallback, long paramLong)
  {
    if (mAnimationCallbacks.size() == 0) {
      getProvider().postFrameCallback(mFrameCallback);
    }
    if (!mAnimationCallbacks.contains(paramAnimationFrameCallback)) {
      mAnimationCallbacks.add(paramAnimationFrameCallback);
    }
    if (paramLong > 0L) {
      mDelayedCallbackStartTime.put(paramAnimationFrameCallback, Long.valueOf(SystemClock.uptimeMillis() + paramLong));
    }
  }
  
  public void addOneShotCommitCallback(AnimationFrameCallback paramAnimationFrameCallback)
  {
    if (!mCommitCallbacks.contains(paramAnimationFrameCallback)) {
      mCommitCallbacks.add(paramAnimationFrameCallback);
    }
  }
  
  void autoCancelBasedOn(ObjectAnimator paramObjectAnimator)
  {
    for (int i = mAnimationCallbacks.size() - 1; i >= 0; i--)
    {
      AnimationFrameCallback localAnimationFrameCallback = (AnimationFrameCallback)mAnimationCallbacks.get(i);
      if ((localAnimationFrameCallback != null) && (paramObjectAnimator.shouldAutoCancel(localAnimationFrameCallback))) {
        ((Animator)mAnimationCallbacks.get(i)).cancel();
      }
    }
  }
  
  public void removeCallback(AnimationFrameCallback paramAnimationFrameCallback)
  {
    mCommitCallbacks.remove(paramAnimationFrameCallback);
    mDelayedCallbackStartTime.remove(paramAnimationFrameCallback);
    int i = mAnimationCallbacks.indexOf(paramAnimationFrameCallback);
    if (i >= 0)
    {
      mAnimationCallbacks.set(i, null);
      mListDirty = true;
    }
  }
  
  public void setProvider(AnimationFrameCallbackProvider paramAnimationFrameCallbackProvider)
  {
    if (paramAnimationFrameCallbackProvider == null) {
      mProvider = new MyFrameCallbackProvider(null);
    } else {
      mProvider = paramAnimationFrameCallbackProvider;
    }
  }
  
  static abstract interface AnimationFrameCallback
  {
    public abstract void commitAnimationFrame(long paramLong);
    
    public abstract boolean doAnimationFrame(long paramLong);
  }
  
  public static abstract interface AnimationFrameCallbackProvider
  {
    public abstract long getFrameDelay();
    
    public abstract long getFrameTime();
    
    public abstract void postCommitCallback(Runnable paramRunnable);
    
    public abstract void postFrameCallback(Choreographer.FrameCallback paramFrameCallback);
    
    public abstract void setFrameDelay(long paramLong);
  }
  
  private class MyFrameCallbackProvider
    implements AnimationHandler.AnimationFrameCallbackProvider
  {
    final Choreographer mChoreographer = Choreographer.getInstance();
    
    private MyFrameCallbackProvider() {}
    
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
}
