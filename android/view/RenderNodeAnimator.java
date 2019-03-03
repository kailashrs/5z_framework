package android.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.CanvasProperty;
import android.graphics.Paint;
import android.util.SparseIntArray;
import com.android.internal.util.VirtualRefBasePtr;
import com.android.internal.view.animation.FallbackLUTInterpolator;
import com.android.internal.view.animation.HasNativeInterpolator;
import com.android.internal.view.animation.NativeInterpolatorFactory;
import java.util.ArrayList;

public class RenderNodeAnimator
  extends Animator
{
  public static final int ALPHA = 11;
  public static final int LAST_VALUE = 11;
  public static final int PAINT_ALPHA = 1;
  public static final int PAINT_STROKE_WIDTH = 0;
  public static final int ROTATION = 5;
  public static final int ROTATION_X = 6;
  public static final int ROTATION_Y = 7;
  public static final int SCALE_X = 3;
  public static final int SCALE_Y = 4;
  private static final int STATE_DELAYED = 1;
  private static final int STATE_FINISHED = 3;
  private static final int STATE_PREPARE = 0;
  private static final int STATE_RUNNING = 2;
  public static final int TRANSLATION_X = 0;
  public static final int TRANSLATION_Y = 1;
  public static final int TRANSLATION_Z = 2;
  public static final int X = 8;
  public static final int Y = 9;
  public static final int Z = 10;
  private static ThreadLocal<DelayedAnimationHelper> sAnimationHelper = new ThreadLocal();
  private static final SparseIntArray sViewPropertyAnimatorMap = new SparseIntArray(15) {};
  private float mFinalValue;
  private TimeInterpolator mInterpolator;
  private VirtualRefBasePtr mNativePtr;
  private int mRenderProperty = -1;
  private long mStartDelay = 0L;
  private long mStartTime;
  private int mState = 0;
  private RenderNode mTarget;
  private final boolean mUiThreadHandlesDelay;
  private long mUnscaledDuration = 300L;
  private long mUnscaledStartDelay = 0L;
  private View mViewTarget;
  
  public RenderNodeAnimator(int paramInt, float paramFloat)
  {
    mRenderProperty = paramInt;
    mFinalValue = paramFloat;
    mUiThreadHandlesDelay = true;
    init(nCreateAnimator(paramInt, paramFloat));
  }
  
  public RenderNodeAnimator(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    init(nCreateRevealAnimator(paramInt1, paramInt2, paramFloat1, paramFloat2));
    mUiThreadHandlesDelay = true;
  }
  
  public RenderNodeAnimator(CanvasProperty<Float> paramCanvasProperty, float paramFloat)
  {
    init(nCreateCanvasPropertyFloatAnimator(paramCanvasProperty.getNativeContainer(), paramFloat));
    mUiThreadHandlesDelay = false;
  }
  
  public RenderNodeAnimator(CanvasProperty<Paint> paramCanvasProperty, int paramInt, float paramFloat)
  {
    init(nCreateCanvasPropertyPaintAnimator(paramCanvasProperty.getNativeContainer(), paramInt, paramFloat));
    mUiThreadHandlesDelay = false;
  }
  
  private void applyInterpolator()
  {
    if ((mInterpolator != null) && (mNativePtr != null))
    {
      long l;
      if (isNativeInterpolator(mInterpolator))
      {
        l = ((NativeInterpolatorFactory)mInterpolator).createNativeInterpolator();
      }
      else
      {
        l = nGetDuration(mNativePtr.get());
        l = FallbackLUTInterpolator.createNativeInterpolator(mInterpolator, l);
      }
      nSetInterpolator(mNativePtr.get(), l);
      return;
    }
  }
  
  private static void callOnFinished(RenderNodeAnimator paramRenderNodeAnimator)
  {
    paramRenderNodeAnimator.onFinished();
  }
  
  private void checkMutable()
  {
    if (mState == 0)
    {
      if (mNativePtr != null) {
        return;
      }
      throw new IllegalStateException("Animator's target has been destroyed (trying to modify an animation after activity destroy?)");
    }
    throw new IllegalStateException("Animator has already started, cannot change it now!");
  }
  
  private ArrayList<Animator.AnimatorListener> cloneListeners()
  {
    ArrayList localArrayList1 = getListeners();
    ArrayList localArrayList2 = localArrayList1;
    if (localArrayList1 != null) {
      localArrayList2 = (ArrayList)localArrayList1.clone();
    }
    return localArrayList2;
  }
  
  private void doStart()
  {
    if (mRenderProperty == 11)
    {
      mViewTarget.ensureTransformationInfo();
      mViewTarget.mTransformationInfo.mAlpha = mFinalValue;
    }
    moveToRunningState();
    if (mViewTarget != null) {
      mViewTarget.invalidateViewProperty(true, false);
    }
  }
  
  private static DelayedAnimationHelper getHelper()
  {
    DelayedAnimationHelper localDelayedAnimationHelper1 = (DelayedAnimationHelper)sAnimationHelper.get();
    DelayedAnimationHelper localDelayedAnimationHelper2 = localDelayedAnimationHelper1;
    if (localDelayedAnimationHelper1 == null)
    {
      localDelayedAnimationHelper2 = new DelayedAnimationHelper();
      sAnimationHelper.set(localDelayedAnimationHelper2);
    }
    return localDelayedAnimationHelper2;
  }
  
  private void init(long paramLong)
  {
    mNativePtr = new VirtualRefBasePtr(paramLong);
  }
  
  static boolean isNativeInterpolator(TimeInterpolator paramTimeInterpolator)
  {
    return paramTimeInterpolator.getClass().isAnnotationPresent(HasNativeInterpolator.class);
  }
  
  public static int mapViewPropertyToRenderProperty(int paramInt)
  {
    return sViewPropertyAnimatorMap.get(paramInt);
  }
  
  private void moveToRunningState()
  {
    mState = 2;
    if (mNativePtr != null) {
      nStart(mNativePtr.get());
    }
    notifyStartListeners();
  }
  
  private static native long nCreateAnimator(int paramInt, float paramFloat);
  
  private static native long nCreateCanvasPropertyFloatAnimator(long paramLong, float paramFloat);
  
  private static native long nCreateCanvasPropertyPaintAnimator(long paramLong, int paramInt, float paramFloat);
  
  private static native long nCreateRevealAnimator(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2);
  
  private static native void nEnd(long paramLong);
  
  private static native long nGetDuration(long paramLong);
  
  private static native void nSetAllowRunningAsync(long paramLong, boolean paramBoolean);
  
  private static native void nSetDuration(long paramLong1, long paramLong2);
  
  private static native void nSetInterpolator(long paramLong1, long paramLong2);
  
  private static native void nSetListener(long paramLong, RenderNodeAnimator paramRenderNodeAnimator);
  
  private static native void nSetStartDelay(long paramLong1, long paramLong2);
  
  private static native void nSetStartValue(long paramLong, float paramFloat);
  
  private static native void nStart(long paramLong);
  
  private void notifyStartListeners()
  {
    ArrayList localArrayList = cloneListeners();
    int i = 0;
    int j;
    if (localArrayList == null) {
      j = 0;
    } else {
      j = localArrayList.size();
    }
    while (i < j)
    {
      ((Animator.AnimatorListener)localArrayList.get(i)).onAnimationStart(this);
      i++;
    }
  }
  
  private boolean processDelayed(long paramLong)
  {
    if (mStartTime == 0L)
    {
      mStartTime = paramLong;
    }
    else if (paramLong - mStartTime >= mStartDelay)
    {
      doStart();
      return true;
    }
    return false;
  }
  
  private void releaseNativePtr()
  {
    if (mNativePtr != null)
    {
      mNativePtr.release();
      mNativePtr = null;
    }
  }
  
  private void setTarget(RenderNode paramRenderNode)
  {
    checkMutable();
    if (mTarget == null)
    {
      nSetListener(mNativePtr.get(), this);
      mTarget = paramRenderNode;
      mTarget.addAnimator(this);
      return;
    }
    throw new IllegalStateException("Target already set!");
  }
  
  public void cancel()
  {
    if ((mState != 0) && (mState != 3))
    {
      if (mState == 1)
      {
        getHelper().removeDelayedAnimation(this);
        moveToRunningState();
      }
      ArrayList localArrayList = cloneListeners();
      int i = 0;
      int j;
      if (localArrayList == null) {
        j = 0;
      } else {
        j = localArrayList.size();
      }
      while (i < j)
      {
        ((Animator.AnimatorListener)localArrayList.get(i)).onAnimationCancel(this);
        i++;
      }
      end();
    }
  }
  
  public Animator clone()
  {
    throw new IllegalStateException("Cannot clone this animator");
  }
  
  public void end()
  {
    if (mState != 3)
    {
      if (mState < 2)
      {
        getHelper().removeDelayedAnimation(this);
        doStart();
      }
      if (mNativePtr != null)
      {
        nEnd(mNativePtr.get());
        if (mViewTarget != null) {
          mViewTarget.invalidateViewProperty(true, false);
        }
      }
      else
      {
        onFinished();
      }
    }
  }
  
  public long getDuration()
  {
    return mUnscaledDuration;
  }
  
  public TimeInterpolator getInterpolator()
  {
    return mInterpolator;
  }
  
  long getNativeAnimator()
  {
    return mNativePtr.get();
  }
  
  public long getStartDelay()
  {
    return mUnscaledStartDelay;
  }
  
  public long getTotalDuration()
  {
    return mUnscaledDuration + mUnscaledStartDelay;
  }
  
  public boolean isRunning()
  {
    int i = mState;
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != 1) {
      if (mState == 2) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  public boolean isStarted()
  {
    boolean bool;
    if (mState != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void onFinished()
  {
    if (mState == 0)
    {
      releaseNativePtr();
      return;
    }
    if (mState == 1)
    {
      getHelper().removeDelayedAnimation(this);
      notifyStartListeners();
    }
    mState = 3;
    ArrayList localArrayList = cloneListeners();
    int i = 0;
    int j;
    if (localArrayList == null) {
      j = 0;
    } else {
      j = localArrayList.size();
    }
    while (i < j)
    {
      ((Animator.AnimatorListener)localArrayList.get(i)).onAnimationEnd(this);
      i++;
    }
    releaseNativePtr();
  }
  
  public void pause()
  {
    throw new UnsupportedOperationException();
  }
  
  public void resume()
  {
    throw new UnsupportedOperationException();
  }
  
  public void setAllowRunningAsynchronously(boolean paramBoolean)
  {
    checkMutable();
    nSetAllowRunningAsync(mNativePtr.get(), paramBoolean);
  }
  
  public RenderNodeAnimator setDuration(long paramLong)
  {
    checkMutable();
    if (paramLong >= 0L)
    {
      mUnscaledDuration = paramLong;
      nSetDuration(mNativePtr.get(), ((float)paramLong * ValueAnimator.getDurationScale()));
      return this;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("duration must be positive; ");
    localStringBuilder.append(paramLong);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setInterpolator(TimeInterpolator paramTimeInterpolator)
  {
    checkMutable();
    mInterpolator = paramTimeInterpolator;
  }
  
  public void setStartDelay(long paramLong)
  {
    checkMutable();
    if (paramLong >= 0L)
    {
      mUnscaledStartDelay = paramLong;
      mStartDelay = ((ValueAnimator.getDurationScale() * (float)paramLong));
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("startDelay must be positive; ");
    localStringBuilder.append(paramLong);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setStartValue(float paramFloat)
  {
    checkMutable();
    nSetStartValue(mNativePtr.get(), paramFloat);
  }
  
  public void setTarget(DisplayListCanvas paramDisplayListCanvas)
  {
    setTarget(mNode);
  }
  
  public void setTarget(View paramView)
  {
    mViewTarget = paramView;
    setTarget(mViewTarget.mRenderNode);
  }
  
  public void start()
  {
    if (mTarget != null)
    {
      if (mState == 0)
      {
        mState = 1;
        applyInterpolator();
        if (mNativePtr == null)
        {
          cancel();
        }
        else if ((mStartDelay > 0L) && (mUiThreadHandlesDelay))
        {
          getHelper().addDelayedAnimation(this);
        }
        else
        {
          nSetStartDelay(mNativePtr.get(), mStartDelay);
          doStart();
        }
        return;
      }
      throw new IllegalStateException("Already started!");
    }
    throw new IllegalStateException("Missing target!");
  }
  
  private static class DelayedAnimationHelper
    implements Runnable
  {
    private boolean mCallbackScheduled;
    private final Choreographer mChoreographer = Choreographer.getInstance();
    private ArrayList<RenderNodeAnimator> mDelayedAnims = new ArrayList();
    
    public DelayedAnimationHelper() {}
    
    private void scheduleCallback()
    {
      if (!mCallbackScheduled)
      {
        mCallbackScheduled = true;
        mChoreographer.postCallback(1, this, null);
      }
    }
    
    public void addDelayedAnimation(RenderNodeAnimator paramRenderNodeAnimator)
    {
      mDelayedAnims.add(paramRenderNodeAnimator);
      scheduleCallback();
    }
    
    public void removeDelayedAnimation(RenderNodeAnimator paramRenderNodeAnimator)
    {
      mDelayedAnims.remove(paramRenderNodeAnimator);
    }
    
    public void run()
    {
      long l = mChoreographer.getFrameTime();
      int i = 0;
      mCallbackScheduled = false;
      int k;
      for (int j = 0; i < mDelayedAnims.size(); j = k)
      {
        RenderNodeAnimator localRenderNodeAnimator = (RenderNodeAnimator)mDelayedAnims.get(i);
        k = j;
        if (!localRenderNodeAnimator.processDelayed(l))
        {
          if (j != i) {
            mDelayedAnims.set(j, localRenderNodeAnimator);
          }
          k = j + 1;
        }
        i++;
      }
      while (mDelayedAnims.size() > j) {
        mDelayedAnims.remove(mDelayedAnims.size() - 1);
      }
      if (mDelayedAnims.size() > 0) {
        scheduleCallback();
      }
    }
  }
}
