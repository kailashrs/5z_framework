package android.animation;

import android.os.Looper;
import android.os.Trace;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ValueAnimator
  extends Animator
  implements AnimationHandler.AnimationFrameCallback
{
  private static final boolean DEBUG = false;
  public static final int INFINITE = -1;
  public static final int RESTART = 1;
  public static final int REVERSE = 2;
  private static final String TAG = "ValueAnimator";
  private static final TimeInterpolator sDefaultInterpolator = new AccelerateDecelerateInterpolator();
  private static float sDurationScale = 1.0F;
  private boolean mAnimationEndRequested = false;
  private float mCurrentFraction = 0.0F;
  private long mDuration = 300L;
  private float mDurationScale = -1.0F;
  private long mFirstFrameTime = -1L;
  boolean mInitialized = false;
  private TimeInterpolator mInterpolator = sDefaultInterpolator;
  private long mLastFrameTime = -1L;
  private float mOverallFraction = 0.0F;
  private long mPauseTime;
  private int mRepeatCount = 0;
  private int mRepeatMode = 1;
  private boolean mResumed = false;
  private boolean mReversing;
  private boolean mRunning = false;
  float mSeekFraction = -1.0F;
  private boolean mSelfPulse = true;
  private long mStartDelay = 0L;
  private boolean mStartListenersCalled = false;
  long mStartTime = -1L;
  boolean mStartTimeCommitted;
  private boolean mStarted = false;
  private boolean mSuppressSelfPulseRequested = false;
  ArrayList<AnimatorUpdateListener> mUpdateListeners = null;
  PropertyValuesHolder[] mValues;
  HashMap<String, PropertyValuesHolder> mValuesMap;
  
  public ValueAnimator() {}
  
  private void addAnimationCallback(long paramLong)
  {
    if (!mSelfPulse) {
      return;
    }
    getAnimationHandler().addAnimationFrameCallback(this, paramLong);
  }
  
  private void addOneShotCommitCallback()
  {
    if (!mSelfPulse) {
      return;
    }
    getAnimationHandler().addOneShotCommitCallback(this);
  }
  
  public static boolean areAnimatorsEnabled()
  {
    boolean bool;
    if (sDurationScale != 0.0F) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private float clampFraction(float paramFloat)
  {
    float f;
    if (paramFloat < 0.0F)
    {
      f = 0.0F;
    }
    else
    {
      f = paramFloat;
      if (mRepeatCount != -1) {
        f = Math.min(paramFloat, mRepeatCount + 1);
      }
    }
    return f;
  }
  
  private void endAnimation()
  {
    if (mAnimationEndRequested) {
      return;
    }
    removeAnimationCallback();
    int i = 1;
    mAnimationEndRequested = true;
    mPaused = false;
    if (((!mStarted) && (!mRunning)) || (mListeners == null)) {
      i = 0;
    }
    if ((i != 0) && (!mRunning)) {
      notifyStartListeners();
    }
    mRunning = false;
    mStarted = false;
    mStartListenersCalled = false;
    mLastFrameTime = -1L;
    mFirstFrameTime = -1L;
    mStartTime = -1L;
    if ((i != 0) && (mListeners != null))
    {
      ArrayList localArrayList = (ArrayList)mListeners.clone();
      int j = localArrayList.size();
      for (i = 0; i < j; i++) {
        ((Animator.AnimatorListener)localArrayList.get(i)).onAnimationEnd(this, mReversing);
      }
    }
    mReversing = false;
    if (Trace.isTagEnabled(8L)) {
      Trace.asyncTraceEnd(8L, getNameForTrace(), System.identityHashCode(this));
    }
  }
  
  public static int getCurrentAnimationsCount()
  {
    return AnimationHandler.getAnimationCount();
  }
  
  private int getCurrentIteration(float paramFloat)
  {
    paramFloat = clampFraction(paramFloat);
    double d1 = Math.floor(paramFloat);
    double d2 = d1;
    if (paramFloat == d1)
    {
      d2 = d1;
      if (paramFloat > 0.0F) {
        d2 = d1 - 1.0D;
      }
    }
    return (int)d2;
  }
  
  private float getCurrentIterationFraction(float paramFloat, boolean paramBoolean)
  {
    paramFloat = clampFraction(paramFloat);
    int i = getCurrentIteration(paramFloat);
    paramFloat -= i;
    if (shouldPlayBackward(i, paramBoolean)) {
      paramFloat = 1.0F - paramFloat;
    }
    return paramFloat;
  }
  
  public static float getDurationScale()
  {
    return sDurationScale;
  }
  
  public static long getFrameDelay()
  {
    AnimationHandler.getInstance();
    return AnimationHandler.getFrameDelay();
  }
  
  private long getScaledDuration()
  {
    return ((float)mDuration * resolveDurationScale());
  }
  
  private boolean isPulsingInternal()
  {
    boolean bool;
    if (mLastFrameTime >= 0L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void notifyStartListeners()
  {
    if ((mListeners != null) && (!mStartListenersCalled))
    {
      ArrayList localArrayList = (ArrayList)mListeners.clone();
      int i = localArrayList.size();
      for (int j = 0; j < i; j++) {
        ((Animator.AnimatorListener)localArrayList.get(j)).onAnimationStart(this, mReversing);
      }
    }
    mStartListenersCalled = true;
  }
  
  public static ValueAnimator ofArgb(int... paramVarArgs)
  {
    ValueAnimator localValueAnimator = new ValueAnimator();
    localValueAnimator.setIntValues(paramVarArgs);
    localValueAnimator.setEvaluator(ArgbEvaluator.getInstance());
    return localValueAnimator;
  }
  
  public static ValueAnimator ofFloat(float... paramVarArgs)
  {
    ValueAnimator localValueAnimator = new ValueAnimator();
    localValueAnimator.setFloatValues(paramVarArgs);
    return localValueAnimator;
  }
  
  public static ValueAnimator ofInt(int... paramVarArgs)
  {
    ValueAnimator localValueAnimator = new ValueAnimator();
    localValueAnimator.setIntValues(paramVarArgs);
    return localValueAnimator;
  }
  
  public static ValueAnimator ofObject(TypeEvaluator paramTypeEvaluator, Object... paramVarArgs)
  {
    ValueAnimator localValueAnimator = new ValueAnimator();
    localValueAnimator.setObjectValues(paramVarArgs);
    localValueAnimator.setEvaluator(paramTypeEvaluator);
    return localValueAnimator;
  }
  
  public static ValueAnimator ofPropertyValuesHolder(PropertyValuesHolder... paramVarArgs)
  {
    ValueAnimator localValueAnimator = new ValueAnimator();
    localValueAnimator.setValues(paramVarArgs);
    return localValueAnimator;
  }
  
  private void removeAnimationCallback()
  {
    if (!mSelfPulse) {
      return;
    }
    getAnimationHandler().removeCallback(this);
  }
  
  private float resolveDurationScale()
  {
    float f;
    if (mDurationScale >= 0.0F) {
      f = mDurationScale;
    } else {
      f = sDurationScale;
    }
    return f;
  }
  
  public static void setDurationScale(float paramFloat)
  {
    sDurationScale = paramFloat;
  }
  
  public static void setFrameDelay(long paramLong)
  {
    AnimationHandler.getInstance();
    AnimationHandler.setFrameDelay(paramLong);
  }
  
  private boolean shouldPlayBackward(int paramInt, boolean paramBoolean)
  {
    if ((paramInt > 0) && (mRepeatMode == 2) && ((paramInt < mRepeatCount + 1) || (mRepeatCount == -1)))
    {
      boolean bool1 = false;
      boolean bool2 = false;
      if (paramBoolean)
      {
        paramBoolean = bool2;
        if (paramInt % 2 == 0) {
          paramBoolean = true;
        }
        return paramBoolean;
      }
      paramBoolean = bool1;
      if (paramInt % 2 != 0) {
        paramBoolean = true;
      }
      return paramBoolean;
    }
    return paramBoolean;
  }
  
  private void start(boolean paramBoolean)
  {
    if (Looper.myLooper() != null)
    {
      mReversing = paramBoolean;
      mSelfPulse = (mSuppressSelfPulseRequested ^ true);
      if ((paramBoolean) && (mSeekFraction != -1.0F) && (mSeekFraction != 0.0F)) {
        if (mRepeatCount == -1) {
          mSeekFraction = (1.0F - (float)(mSeekFraction - Math.floor(mSeekFraction)));
        } else {
          mSeekFraction = (mRepeatCount + 1 - mSeekFraction);
        }
      }
      mStarted = true;
      mPaused = false;
      mRunning = false;
      mAnimationEndRequested = false;
      mLastFrameTime = -1L;
      mFirstFrameTime = -1L;
      mStartTime = -1L;
      addAnimationCallback(0L);
      if ((mStartDelay == 0L) || (mSeekFraction >= 0.0F) || (mReversing))
      {
        startAnimation();
        if (mSeekFraction == -1.0F) {
          setCurrentPlayTime(0L);
        } else {
          setCurrentFraction(mSeekFraction);
        }
      }
      return;
    }
    throw new AndroidRuntimeException("Animators may only be run on Looper threads");
  }
  
  private void startAnimation()
  {
    if (Trace.isTagEnabled(8L)) {
      Trace.asyncTraceBegin(8L, getNameForTrace(), System.identityHashCode(this));
    }
    mAnimationEndRequested = false;
    initAnimation();
    mRunning = true;
    if (mSeekFraction >= 0.0F) {
      mOverallFraction = mSeekFraction;
    } else {
      mOverallFraction = 0.0F;
    }
    if (mListeners != null) {
      notifyStartListeners();
    }
  }
  
  public void addUpdateListener(AnimatorUpdateListener paramAnimatorUpdateListener)
  {
    if (mUpdateListeners == null) {
      mUpdateListeners = new ArrayList();
    }
    mUpdateListeners.add(paramAnimatorUpdateListener);
  }
  
  void animateBasedOnPlayTime(long paramLong1, long paramLong2, boolean paramBoolean)
  {
    if ((paramLong1 >= 0L) && (paramLong2 >= 0L))
    {
      initAnimation();
      if (mRepeatCount > 0)
      {
        int i = (int)(paramLong1 / mDuration);
        int j = (int)(paramLong2 / mDuration);
        if ((Math.min(i, mRepeatCount) != Math.min(j, mRepeatCount)) && (mListeners != null))
        {
          i = mListeners.size();
          for (j = 0; j < i; j++) {
            ((Animator.AnimatorListener)mListeners.get(j)).onAnimationRepeat(this);
          }
        }
      }
      if ((mRepeatCount != -1) && (paramLong1 >= (mRepeatCount + 1) * mDuration)) {
        skipToEndValue(paramBoolean);
      } else {
        animateValue(getCurrentIterationFraction((float)paramLong1 / (float)mDuration, paramBoolean));
      }
      return;
    }
    throw new UnsupportedOperationException("Error: Play time should never be negative.");
  }
  
  boolean animateBasedOnTime(long paramLong)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (mRunning)
    {
      long l = getScaledDuration();
      float f1;
      if (l > 0L) {
        f1 = (float)(paramLong - mStartTime) / (float)l;
      } else {
        f1 = 1.0F;
      }
      float f2 = mOverallFraction;
      int i = (int)f1;
      int j = (int)f2;
      int k = 0;
      int m = 1;
      if (i > j) {
        j = 1;
      } else {
        j = 0;
      }
      if ((f1 < mRepeatCount + 1) || (mRepeatCount == -1)) {
        m = 0;
      }
      if (l == 0L)
      {
        bool1 = true;
      }
      else if ((j != 0) && (m == 0))
      {
        bool1 = bool2;
        if (mListeners != null)
        {
          m = mListeners.size();
          for (j = k; j < m; j++) {
            ((Animator.AnimatorListener)mListeners.get(j)).onAnimationRepeat(this);
          }
          bool1 = bool2;
        }
      }
      else
      {
        bool1 = bool2;
        if (m != 0) {
          bool1 = true;
        }
      }
      mOverallFraction = clampFraction(f1);
      animateValue(getCurrentIterationFraction(mOverallFraction, mReversing));
    }
    return bool1;
  }
  
  void animateValue(float paramFloat)
  {
    paramFloat = mInterpolator.getInterpolation(paramFloat);
    mCurrentFraction = paramFloat;
    int i = mValues.length;
    int j = 0;
    for (int k = 0; k < i; k++) {
      mValues[k].calculateValue(paramFloat);
    }
    if (mUpdateListeners != null)
    {
      i = mUpdateListeners.size();
      for (k = j; k < i; k++) {
        ((AnimatorUpdateListener)mUpdateListeners.get(k)).onAnimationUpdate(this);
      }
    }
  }
  
  public boolean canReverse()
  {
    return true;
  }
  
  public void cancel()
  {
    if (Looper.myLooper() != null)
    {
      if (mAnimationEndRequested) {
        return;
      }
      if (((mStarted) || (mRunning)) && (mListeners != null))
      {
        if (!mRunning) {
          notifyStartListeners();
        }
        Iterator localIterator = ((ArrayList)mListeners.clone()).iterator();
        while (localIterator.hasNext()) {
          ((Animator.AnimatorListener)localIterator.next()).onAnimationCancel(this);
        }
      }
      endAnimation();
      return;
    }
    throw new AndroidRuntimeException("Animators may only be run on Looper threads");
  }
  
  public ValueAnimator clone()
  {
    ValueAnimator localValueAnimator = (ValueAnimator)super.clone();
    if (mUpdateListeners != null) {
      mUpdateListeners = new ArrayList(mUpdateListeners);
    }
    mSeekFraction = -1.0F;
    int i = 0;
    mReversing = false;
    mInitialized = false;
    mStarted = false;
    mRunning = false;
    mPaused = false;
    mResumed = false;
    mStartListenersCalled = false;
    mStartTime = -1L;
    mStartTimeCommitted = false;
    mAnimationEndRequested = false;
    mPauseTime = -1L;
    mLastFrameTime = -1L;
    mFirstFrameTime = -1L;
    mOverallFraction = 0.0F;
    mCurrentFraction = 0.0F;
    mSelfPulse = true;
    mSuppressSelfPulseRequested = false;
    PropertyValuesHolder[] arrayOfPropertyValuesHolder = mValues;
    if (arrayOfPropertyValuesHolder != null)
    {
      int j = arrayOfPropertyValuesHolder.length;
      mValues = new PropertyValuesHolder[j];
      mValuesMap = new HashMap(j);
      while (i < j)
      {
        PropertyValuesHolder localPropertyValuesHolder = arrayOfPropertyValuesHolder[i].clone();
        mValues[i] = localPropertyValuesHolder;
        mValuesMap.put(localPropertyValuesHolder.getPropertyName(), localPropertyValuesHolder);
        i++;
      }
    }
    return localValueAnimator;
  }
  
  public void commitAnimationFrame(long paramLong)
  {
    if (!mStartTimeCommitted)
    {
      mStartTimeCommitted = true;
      paramLong -= mLastFrameTime;
      if (paramLong > 0L) {
        mStartTime += paramLong;
      }
    }
  }
  
  public final boolean doAnimationFrame(long paramLong)
  {
    if (mStartTime < 0L)
    {
      long l;
      if (mReversing) {
        l = paramLong;
      } else {
        l = ((float)mStartDelay * resolveDurationScale()) + paramLong;
      }
      mStartTime = l;
    }
    if (mPaused)
    {
      mPauseTime = paramLong;
      removeAnimationCallback();
      return false;
    }
    if (mResumed)
    {
      mResumed = false;
      if (mPauseTime > 0L) {
        mStartTime += paramLong - mPauseTime;
      }
    }
    if (!mRunning)
    {
      if ((mStartTime > paramLong) && (mSeekFraction == -1.0F)) {
        return false;
      }
      mRunning = true;
      startAnimation();
    }
    if (mLastFrameTime < 0L)
    {
      if (mSeekFraction >= 0.0F)
      {
        mStartTime = (paramLong - ((float)getScaledDuration() * mSeekFraction));
        mSeekFraction = -1.0F;
      }
      mStartTimeCommitted = false;
    }
    mLastFrameTime = paramLong;
    boolean bool = animateBasedOnTime(Math.max(paramLong, mStartTime));
    if (bool) {
      endAnimation();
    }
    return bool;
  }
  
  public void end()
  {
    if (Looper.myLooper() != null)
    {
      if (!mRunning)
      {
        startAnimation();
        mStarted = true;
      }
      else if (!mInitialized)
      {
        initAnimation();
      }
      float f;
      if (shouldPlayBackward(mRepeatCount, mReversing)) {
        f = 0.0F;
      } else {
        f = 1.0F;
      }
      animateValue(f);
      endAnimation();
      return;
    }
    throw new AndroidRuntimeException("Animators may only be run on Looper threads");
  }
  
  public float getAnimatedFraction()
  {
    return mCurrentFraction;
  }
  
  public Object getAnimatedValue()
  {
    if ((mValues != null) && (mValues.length > 0)) {
      return mValues[0].getAnimatedValue();
    }
    return null;
  }
  
  public Object getAnimatedValue(String paramString)
  {
    paramString = (PropertyValuesHolder)mValuesMap.get(paramString);
    if (paramString != null) {
      return paramString.getAnimatedValue();
    }
    return null;
  }
  
  public AnimationHandler getAnimationHandler()
  {
    return AnimationHandler.getInstance();
  }
  
  public long getCurrentPlayTime()
  {
    if ((mInitialized) && ((mStarted) || (mSeekFraction >= 0.0F)))
    {
      if (mSeekFraction >= 0.0F) {
        return ((float)mDuration * mSeekFraction);
      }
      float f1 = resolveDurationScale();
      float f2 = f1;
      if (f1 == 0.0F) {
        f2 = 1.0F;
      }
      return ((float)(AnimationUtils.currentAnimationTimeMillis() - mStartTime) / f2);
    }
    return 0L;
  }
  
  public long getDuration()
  {
    return mDuration;
  }
  
  public TimeInterpolator getInterpolator()
  {
    return mInterpolator;
  }
  
  String getNameForTrace()
  {
    return "animator";
  }
  
  public int getRepeatCount()
  {
    return mRepeatCount;
  }
  
  public int getRepeatMode()
  {
    return mRepeatMode;
  }
  
  public long getStartDelay()
  {
    return mStartDelay;
  }
  
  public long getTotalDuration()
  {
    if (mRepeatCount == -1) {
      return -1L;
    }
    return mStartDelay + mDuration * (mRepeatCount + 1);
  }
  
  public PropertyValuesHolder[] getValues()
  {
    return mValues;
  }
  
  void initAnimation()
  {
    if (!mInitialized)
    {
      int i = mValues.length;
      for (int j = 0; j < i; j++) {
        mValues[j].init();
      }
      mInitialized = true;
    }
  }
  
  boolean isInitialized()
  {
    return mInitialized;
  }
  
  public boolean isRunning()
  {
    return mRunning;
  }
  
  public boolean isStarted()
  {
    return mStarted;
  }
  
  public void overrideDurationScale(float paramFloat)
  {
    mDurationScale = paramFloat;
  }
  
  public void pause()
  {
    boolean bool = mPaused;
    super.pause();
    if ((!bool) && (mPaused))
    {
      mPauseTime = -1L;
      mResumed = false;
    }
  }
  
  boolean pulseAnimationFrame(long paramLong)
  {
    if (mSelfPulse) {
      return false;
    }
    return doAnimationFrame(paramLong);
  }
  
  public void removeAllUpdateListeners()
  {
    if (mUpdateListeners == null) {
      return;
    }
    mUpdateListeners.clear();
    mUpdateListeners = null;
  }
  
  public void removeUpdateListener(AnimatorUpdateListener paramAnimatorUpdateListener)
  {
    if (mUpdateListeners == null) {
      return;
    }
    mUpdateListeners.remove(paramAnimatorUpdateListener);
    if (mUpdateListeners.size() == 0) {
      mUpdateListeners = null;
    }
  }
  
  public void resume()
  {
    if (Looper.myLooper() != null)
    {
      if ((mPaused) && (!mResumed))
      {
        mResumed = true;
        if (mPauseTime > 0L) {
          addAnimationCallback(0L);
        }
      }
      super.resume();
      return;
    }
    throw new AndroidRuntimeException("Animators may only be resumed from the same thread that the animator was started on");
  }
  
  public void reverse()
  {
    if (isPulsingInternal())
    {
      long l1 = AnimationUtils.currentAnimationTimeMillis();
      long l2 = mStartTime;
      mStartTime = (l1 - (getScaledDuration() - (l1 - l2)));
      mStartTimeCommitted = true;
      mReversing ^= true;
    }
    else if (mStarted)
    {
      mReversing ^= true;
      end();
    }
    else
    {
      start(true);
    }
  }
  
  public void setAllowRunningAsynchronously(boolean paramBoolean) {}
  
  public void setCurrentFraction(float paramFloat)
  {
    initAnimation();
    paramFloat = clampFraction(paramFloat);
    mStartTimeCommitted = true;
    if (isPulsingInternal())
    {
      long l = ((float)getScaledDuration() * paramFloat);
      mStartTime = (AnimationUtils.currentAnimationTimeMillis() - l);
    }
    else
    {
      mSeekFraction = paramFloat;
    }
    mOverallFraction = paramFloat;
    animateValue(getCurrentIterationFraction(paramFloat, mReversing));
  }
  
  public void setCurrentPlayTime(long paramLong)
  {
    float f;
    if (mDuration > 0L) {
      f = (float)paramLong / (float)mDuration;
    } else {
      f = 1.0F;
    }
    setCurrentFraction(f);
  }
  
  public ValueAnimator setDuration(long paramLong)
  {
    if (paramLong >= 0L)
    {
      mDuration = paramLong;
      return this;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Animators cannot have negative duration: ");
    localStringBuilder.append(paramLong);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setEvaluator(TypeEvaluator paramTypeEvaluator)
  {
    if ((paramTypeEvaluator != null) && (mValues != null) && (mValues.length > 0)) {
      mValues[0].setEvaluator(paramTypeEvaluator);
    }
  }
  
  public void setFloatValues(float... paramVarArgs)
  {
    if ((paramVarArgs != null) && (paramVarArgs.length != 0))
    {
      if ((mValues != null) && (mValues.length != 0)) {
        mValues[0].setFloatValues(paramVarArgs);
      } else {
        setValues(new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("", paramVarArgs) });
      }
      mInitialized = false;
      return;
    }
  }
  
  public void setIntValues(int... paramVarArgs)
  {
    if ((paramVarArgs != null) && (paramVarArgs.length != 0))
    {
      if ((mValues != null) && (mValues.length != 0)) {
        mValues[0].setIntValues(paramVarArgs);
      } else {
        setValues(new PropertyValuesHolder[] { PropertyValuesHolder.ofInt("", paramVarArgs) });
      }
      mInitialized = false;
      return;
    }
  }
  
  public void setInterpolator(TimeInterpolator paramTimeInterpolator)
  {
    if (paramTimeInterpolator != null) {
      mInterpolator = paramTimeInterpolator;
    } else {
      mInterpolator = new LinearInterpolator();
    }
  }
  
  public void setObjectValues(Object... paramVarArgs)
  {
    if ((paramVarArgs != null) && (paramVarArgs.length != 0))
    {
      if ((mValues != null) && (mValues.length != 0)) {
        mValues[0].setObjectValues(paramVarArgs);
      } else {
        setValues(new PropertyValuesHolder[] { PropertyValuesHolder.ofObject("", null, paramVarArgs) });
      }
      mInitialized = false;
      return;
    }
  }
  
  public void setRepeatCount(int paramInt)
  {
    mRepeatCount = paramInt;
  }
  
  public void setRepeatMode(int paramInt)
  {
    mRepeatMode = paramInt;
  }
  
  public void setStartDelay(long paramLong)
  {
    long l = paramLong;
    if (paramLong < 0L)
    {
      Log.w("ValueAnimator", "Start delay should always be non-negative");
      l = 0L;
    }
    mStartDelay = l;
  }
  
  public void setValues(PropertyValuesHolder... paramVarArgs)
  {
    int i = paramVarArgs.length;
    mValues = paramVarArgs;
    mValuesMap = new HashMap(i);
    for (int j = 0; j < i; j++)
    {
      PropertyValuesHolder localPropertyValuesHolder = paramVarArgs[j];
      mValuesMap.put(localPropertyValuesHolder.getPropertyName(), localPropertyValuesHolder);
    }
    mInitialized = false;
  }
  
  void skipToEndValue(boolean paramBoolean)
  {
    initAnimation();
    float f1;
    if (paramBoolean) {
      f1 = 0.0F;
    } else {
      f1 = 1.0F;
    }
    float f2 = f1;
    if (mRepeatCount % 2 == 1)
    {
      f2 = f1;
      if (mRepeatMode == 2) {
        f2 = 0.0F;
      }
    }
    animateValue(f2);
  }
  
  public void start()
  {
    start(false);
  }
  
  void startWithoutPulsing(boolean paramBoolean)
  {
    mSuppressSelfPulseRequested = true;
    if (paramBoolean) {
      reverse();
    } else {
      start();
    }
    mSuppressSelfPulseRequested = false;
  }
  
  public String toString()
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("ValueAnimator@");
    ((StringBuilder)localObject1).append(Integer.toHexString(hashCode()));
    localObject1 = ((StringBuilder)localObject1).toString();
    Object localObject2 = localObject1;
    if (mValues != null) {
      for (int i = 0;; i++)
      {
        localObject2 = localObject1;
        if (i >= mValues.length) {
          break;
        }
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append((String)localObject1);
        ((StringBuilder)localObject2).append("\n    ");
        ((StringBuilder)localObject2).append(mValues[i].toString());
        localObject1 = ((StringBuilder)localObject2).toString();
      }
    }
    return localObject2;
  }
  
  public static abstract interface AnimatorUpdateListener
  {
    public abstract void onAnimationUpdate(ValueAnimator paramValueAnimator);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RepeatMode {}
}
