package android.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ViewPropertyAnimator
{
  static final int ALPHA = 2048;
  static final int NONE = 0;
  static final int ROTATION = 32;
  static final int ROTATION_X = 64;
  static final int ROTATION_Y = 128;
  static final int SCALE_X = 8;
  static final int SCALE_Y = 16;
  private static final int TRANSFORM_MASK = 2047;
  static final int TRANSLATION_X = 1;
  static final int TRANSLATION_Y = 2;
  static final int TRANSLATION_Z = 4;
  static final int X = 256;
  static final int Y = 512;
  static final int Z = 1024;
  private Runnable mAnimationStarter = new Runnable()
  {
    public void run()
    {
      ViewPropertyAnimator.this.startAnimation();
    }
  };
  private HashMap<Animator, Runnable> mAnimatorCleanupMap;
  private AnimatorEventListener mAnimatorEventListener = new AnimatorEventListener(null);
  private HashMap<Animator, PropertyBundle> mAnimatorMap = new HashMap();
  private HashMap<Animator, Runnable> mAnimatorOnEndMap;
  private HashMap<Animator, Runnable> mAnimatorOnStartMap;
  private HashMap<Animator, Runnable> mAnimatorSetupMap;
  private long mDuration;
  private boolean mDurationSet = false;
  private TimeInterpolator mInterpolator;
  private boolean mInterpolatorSet = false;
  private Animator.AnimatorListener mListener = null;
  ArrayList<NameValuesHolder> mPendingAnimations = new ArrayList();
  private Runnable mPendingCleanupAction;
  private Runnable mPendingOnEndAction;
  private Runnable mPendingOnStartAction;
  private Runnable mPendingSetupAction;
  private ViewPropertyAnimatorRT mRTBackend;
  private long mStartDelay = 0L;
  private boolean mStartDelaySet = false;
  private ValueAnimator mTempValueAnimator;
  private ValueAnimator.AnimatorUpdateListener mUpdateListener = null;
  final View mView;
  
  ViewPropertyAnimator(View paramView)
  {
    mView = paramView;
    paramView.ensureTransformationInfo();
  }
  
  private void animateProperty(int paramInt, float paramFloat)
  {
    float f = getValue(paramInt);
    animatePropertyBy(paramInt, f, paramFloat - f);
  }
  
  private void animatePropertyBy(int paramInt, float paramFloat)
  {
    animatePropertyBy(paramInt, getValue(paramInt), paramFloat);
  }
  
  private void animatePropertyBy(int paramInt, float paramFloat1, float paramFloat2)
  {
    if (mAnimatorMap.size() > 0)
    {
      Object localObject1 = null;
      Iterator localIterator = mAnimatorMap.keySet().iterator();
      for (;;)
      {
        localObject2 = localObject1;
        if (!localIterator.hasNext()) {
          break;
        }
        localObject2 = (Animator)localIterator.next();
        PropertyBundle localPropertyBundle = (PropertyBundle)mAnimatorMap.get(localObject2);
        if ((localPropertyBundle.cancel(paramInt)) && (mPropertyMask == 0)) {
          break;
        }
      }
      if (localObject2 != null) {
        ((Animator)localObject2).cancel();
      }
    }
    Object localObject2 = new NameValuesHolder(paramInt, paramFloat1, paramFloat2);
    mPendingAnimations.add(localObject2);
    mView.removeCallbacks(mAnimationStarter);
    mView.postOnAnimation(mAnimationStarter);
  }
  
  private float getValue(int paramInt)
  {
    RenderNode localRenderNode = mView.mRenderNode;
    switch (paramInt)
    {
    default: 
      return 0.0F;
    case 2048: 
      return mView.mTransformationInfo.mAlpha;
    case 1024: 
      return localRenderNode.getElevation() + localRenderNode.getTranslationZ();
    case 512: 
      return mView.mTop + localRenderNode.getTranslationY();
    case 256: 
      return mView.mLeft + localRenderNode.getTranslationX();
    case 128: 
      return localRenderNode.getRotationY();
    case 64: 
      return localRenderNode.getRotationX();
    case 32: 
      return localRenderNode.getRotation();
    case 16: 
      return localRenderNode.getScaleY();
    case 8: 
      return localRenderNode.getScaleX();
    case 4: 
      return localRenderNode.getTranslationZ();
    case 2: 
      return localRenderNode.getTranslationY();
    }
    return localRenderNode.getTranslationX();
  }
  
  private void setValue(int paramInt, float paramFloat)
  {
    View.TransformationInfo localTransformationInfo = mView.mTransformationInfo;
    RenderNode localRenderNode = mView.mRenderNode;
    switch (paramInt)
    {
    default: 
      break;
    case 2048: 
      mAlpha = paramFloat;
      localRenderNode.setAlpha(paramFloat);
      break;
    case 1024: 
      localRenderNode.setTranslationZ(paramFloat - localRenderNode.getElevation());
      break;
    case 512: 
      localRenderNode.setTranslationY(paramFloat - mView.mTop);
      break;
    case 256: 
      localRenderNode.setTranslationX(paramFloat - mView.mLeft);
      break;
    case 128: 
      localRenderNode.setRotationY(paramFloat);
      break;
    case 64: 
      localRenderNode.setRotationX(paramFloat);
      break;
    case 32: 
      localRenderNode.setRotation(paramFloat);
      break;
    case 16: 
      localRenderNode.setScaleY(paramFloat);
      break;
    case 8: 
      localRenderNode.setScaleX(paramFloat);
      break;
    case 4: 
      localRenderNode.setTranslationZ(paramFloat);
      break;
    case 2: 
      localRenderNode.setTranslationY(paramFloat);
      break;
    case 1: 
      localRenderNode.setTranslationX(paramFloat);
    }
  }
  
  private void startAnimation()
  {
    if ((mRTBackend != null) && (mRTBackend.startAnimation(this))) {
      return;
    }
    mView.setHasTransientState(true);
    int i = 0;
    ValueAnimator localValueAnimator = ValueAnimator.ofFloat(new float[] { 1.0F });
    ArrayList localArrayList = (ArrayList)mPendingAnimations.clone();
    mPendingAnimations.clear();
    int j = 0;
    int k = localArrayList.size();
    while (i < k)
    {
      j |= getmNameConstant;
      i++;
    }
    mAnimatorMap.put(localValueAnimator, new PropertyBundle(j, localArrayList));
    if (mPendingSetupAction != null)
    {
      mAnimatorSetupMap.put(localValueAnimator, mPendingSetupAction);
      mPendingSetupAction = null;
    }
    if (mPendingCleanupAction != null)
    {
      mAnimatorCleanupMap.put(localValueAnimator, mPendingCleanupAction);
      mPendingCleanupAction = null;
    }
    if (mPendingOnStartAction != null)
    {
      mAnimatorOnStartMap.put(localValueAnimator, mPendingOnStartAction);
      mPendingOnStartAction = null;
    }
    if (mPendingOnEndAction != null)
    {
      mAnimatorOnEndMap.put(localValueAnimator, mPendingOnEndAction);
      mPendingOnEndAction = null;
    }
    localValueAnimator.addUpdateListener(mAnimatorEventListener);
    localValueAnimator.addListener(mAnimatorEventListener);
    if (mStartDelaySet) {
      localValueAnimator.setStartDelay(mStartDelay);
    }
    if (mDurationSet) {
      localValueAnimator.setDuration(mDuration);
    }
    if (mInterpolatorSet) {
      localValueAnimator.setInterpolator(mInterpolator);
    }
    localValueAnimator.start();
  }
  
  public ViewPropertyAnimator alpha(float paramFloat)
  {
    animateProperty(2048, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator alphaBy(float paramFloat)
  {
    animatePropertyBy(2048, paramFloat);
    return this;
  }
  
  public void cancel()
  {
    if (mAnimatorMap.size() > 0)
    {
      Iterator localIterator = ((HashMap)mAnimatorMap.clone()).keySet().iterator();
      while (localIterator.hasNext()) {
        ((Animator)localIterator.next()).cancel();
      }
    }
    mPendingAnimations.clear();
    mPendingSetupAction = null;
    mPendingCleanupAction = null;
    mPendingOnStartAction = null;
    mPendingOnEndAction = null;
    mView.removeCallbacks(mAnimationStarter);
    if (mRTBackend != null) {
      mRTBackend.cancelAll();
    }
  }
  
  public long getDuration()
  {
    if (mDurationSet) {
      return mDuration;
    }
    if (mTempValueAnimator == null) {
      mTempValueAnimator = new ValueAnimator();
    }
    return mTempValueAnimator.getDuration();
  }
  
  public TimeInterpolator getInterpolator()
  {
    if (mInterpolatorSet) {
      return mInterpolator;
    }
    if (mTempValueAnimator == null) {
      mTempValueAnimator = new ValueAnimator();
    }
    return mTempValueAnimator.getInterpolator();
  }
  
  Animator.AnimatorListener getListener()
  {
    return mListener;
  }
  
  public long getStartDelay()
  {
    if (mStartDelaySet) {
      return mStartDelay;
    }
    return 0L;
  }
  
  ValueAnimator.AnimatorUpdateListener getUpdateListener()
  {
    return mUpdateListener;
  }
  
  boolean hasActions()
  {
    boolean bool;
    if ((mPendingSetupAction == null) && (mPendingCleanupAction == null) && (mPendingOnStartAction == null) && (mPendingOnEndAction == null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public ViewPropertyAnimator rotation(float paramFloat)
  {
    animateProperty(32, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator rotationBy(float paramFloat)
  {
    animatePropertyBy(32, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator rotationX(float paramFloat)
  {
    animateProperty(64, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator rotationXBy(float paramFloat)
  {
    animatePropertyBy(64, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator rotationY(float paramFloat)
  {
    animateProperty(128, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator rotationYBy(float paramFloat)
  {
    animatePropertyBy(128, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator scaleX(float paramFloat)
  {
    animateProperty(8, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator scaleXBy(float paramFloat)
  {
    animatePropertyBy(8, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator scaleY(float paramFloat)
  {
    animateProperty(16, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator scaleYBy(float paramFloat)
  {
    animatePropertyBy(16, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator setDuration(long paramLong)
  {
    if (paramLong >= 0L)
    {
      mDurationSet = true;
      mDuration = paramLong;
      return this;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Animators cannot have negative duration: ");
    localStringBuilder.append(paramLong);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public ViewPropertyAnimator setInterpolator(TimeInterpolator paramTimeInterpolator)
  {
    mInterpolatorSet = true;
    mInterpolator = paramTimeInterpolator;
    return this;
  }
  
  public ViewPropertyAnimator setListener(Animator.AnimatorListener paramAnimatorListener)
  {
    mListener = paramAnimatorListener;
    return this;
  }
  
  public ViewPropertyAnimator setStartDelay(long paramLong)
  {
    if (paramLong >= 0L)
    {
      mStartDelaySet = true;
      mStartDelay = paramLong;
      return this;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Animators cannot have negative start delay: ");
    localStringBuilder.append(paramLong);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public ViewPropertyAnimator setUpdateListener(ValueAnimator.AnimatorUpdateListener paramAnimatorUpdateListener)
  {
    mUpdateListener = paramAnimatorUpdateListener;
    return this;
  }
  
  public void start()
  {
    mView.removeCallbacks(mAnimationStarter);
    startAnimation();
  }
  
  public ViewPropertyAnimator translationX(float paramFloat)
  {
    animateProperty(1, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator translationXBy(float paramFloat)
  {
    animatePropertyBy(1, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator translationY(float paramFloat)
  {
    animateProperty(2, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator translationYBy(float paramFloat)
  {
    animatePropertyBy(2, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator translationZ(float paramFloat)
  {
    animateProperty(4, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator translationZBy(float paramFloat)
  {
    animatePropertyBy(4, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator withEndAction(Runnable paramRunnable)
  {
    mPendingOnEndAction = paramRunnable;
    if ((paramRunnable != null) && (mAnimatorOnEndMap == null)) {
      mAnimatorOnEndMap = new HashMap();
    }
    return this;
  }
  
  public ViewPropertyAnimator withLayer()
  {
    mPendingSetupAction = new Runnable()
    {
      public void run()
      {
        mView.setLayerType(2, null);
        if (mView.isAttachedToWindow()) {
          mView.buildLayer();
        }
      }
    };
    mPendingCleanupAction = new Runnable()
    {
      public void run()
      {
        mView.setLayerType(val$currentLayerType, null);
      }
    };
    if (mAnimatorSetupMap == null) {
      mAnimatorSetupMap = new HashMap();
    }
    if (mAnimatorCleanupMap == null) {
      mAnimatorCleanupMap = new HashMap();
    }
    return this;
  }
  
  public ViewPropertyAnimator withStartAction(Runnable paramRunnable)
  {
    mPendingOnStartAction = paramRunnable;
    if ((paramRunnable != null) && (mAnimatorOnStartMap == null)) {
      mAnimatorOnStartMap = new HashMap();
    }
    return this;
  }
  
  public ViewPropertyAnimator x(float paramFloat)
  {
    animateProperty(256, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator xBy(float paramFloat)
  {
    animatePropertyBy(256, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator y(float paramFloat)
  {
    animateProperty(512, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator yBy(float paramFloat)
  {
    animatePropertyBy(512, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator z(float paramFloat)
  {
    animateProperty(1024, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator zBy(float paramFloat)
  {
    animatePropertyBy(1024, paramFloat);
    return this;
  }
  
  private class AnimatorEventListener
    implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener
  {
    private AnimatorEventListener() {}
    
    public void onAnimationCancel(Animator paramAnimator)
    {
      if (mListener != null) {
        mListener.onAnimationCancel(paramAnimator);
      }
      if (mAnimatorOnEndMap != null) {
        mAnimatorOnEndMap.remove(paramAnimator);
      }
    }
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      mView.setHasTransientState(false);
      Runnable localRunnable;
      if (mAnimatorCleanupMap != null)
      {
        localRunnable = (Runnable)mAnimatorCleanupMap.get(paramAnimator);
        if (localRunnable != null) {
          localRunnable.run();
        }
        mAnimatorCleanupMap.remove(paramAnimator);
      }
      if (mListener != null) {
        mListener.onAnimationEnd(paramAnimator);
      }
      if (mAnimatorOnEndMap != null)
      {
        localRunnable = (Runnable)mAnimatorOnEndMap.get(paramAnimator);
        if (localRunnable != null) {
          localRunnable.run();
        }
        mAnimatorOnEndMap.remove(paramAnimator);
      }
      mAnimatorMap.remove(paramAnimator);
    }
    
    public void onAnimationRepeat(Animator paramAnimator)
    {
      if (mListener != null) {
        mListener.onAnimationRepeat(paramAnimator);
      }
    }
    
    public void onAnimationStart(Animator paramAnimator)
    {
      Runnable localRunnable;
      if (mAnimatorSetupMap != null)
      {
        localRunnable = (Runnable)mAnimatorSetupMap.get(paramAnimator);
        if (localRunnable != null) {
          localRunnable.run();
        }
        mAnimatorSetupMap.remove(paramAnimator);
      }
      if (mAnimatorOnStartMap != null)
      {
        localRunnable = (Runnable)mAnimatorOnStartMap.get(paramAnimator);
        if (localRunnable != null) {
          localRunnable.run();
        }
        mAnimatorOnStartMap.remove(paramAnimator);
      }
      if (mListener != null) {
        mListener.onAnimationStart(paramAnimator);
      }
    }
    
    public void onAnimationUpdate(ValueAnimator paramValueAnimator)
    {
      Object localObject = (ViewPropertyAnimator.PropertyBundle)mAnimatorMap.get(paramValueAnimator);
      if (localObject == null) {
        return;
      }
      boolean bool1 = mView.isHardwareAccelerated();
      boolean bool2 = false;
      if (!bool1) {
        mView.invalidateParentCaches();
      }
      float f1 = paramValueAnimator.getAnimatedFraction();
      int i = mPropertyMask;
      if ((i & 0x7FF) != 0) {
        mView.invalidateViewProperty(bool1, false);
      }
      localObject = mNameValuesHolder;
      if (localObject != null)
      {
        int j = ((ArrayList)localObject).size();
        bool2 = false;
        for (int k = 0; k < j; k++)
        {
          ViewPropertyAnimator.NameValuesHolder localNameValuesHolder = (ViewPropertyAnimator.NameValuesHolder)((ArrayList)localObject).get(k);
          float f2 = mFromValue + mDeltaValue * f1;
          if (mNameConstant == 2048) {
            bool2 = mView.setAlphaNoInvalidation(f2);
          } else {
            ViewPropertyAnimator.this.setValue(mNameConstant, f2);
          }
        }
      }
      if (((i & 0x7FF) != 0) && (!bool1))
      {
        localObject = mView;
        mPrivateFlags |= 0x20;
      }
      if (bool2) {
        mView.invalidate(true);
      } else {
        mView.invalidateViewProperty(false, false);
      }
      if (mUpdateListener != null) {
        mUpdateListener.onAnimationUpdate(paramValueAnimator);
      }
    }
  }
  
  static class NameValuesHolder
  {
    float mDeltaValue;
    float mFromValue;
    int mNameConstant;
    
    NameValuesHolder(int paramInt, float paramFloat1, float paramFloat2)
    {
      mNameConstant = paramInt;
      mFromValue = paramFloat1;
      mDeltaValue = paramFloat2;
    }
  }
  
  private static class PropertyBundle
  {
    ArrayList<ViewPropertyAnimator.NameValuesHolder> mNameValuesHolder;
    int mPropertyMask;
    
    PropertyBundle(int paramInt, ArrayList<ViewPropertyAnimator.NameValuesHolder> paramArrayList)
    {
      mPropertyMask = paramInt;
      mNameValuesHolder = paramArrayList;
    }
    
    boolean cancel(int paramInt)
    {
      if (((mPropertyMask & paramInt) != 0) && (mNameValuesHolder != null))
      {
        int i = mNameValuesHolder.size();
        for (int j = 0; j < i; j++) {
          if (mNameValuesHolder.get(j)).mNameConstant == paramInt)
          {
            mNameValuesHolder.remove(j);
            mPropertyMask &= paramInt;
            return true;
          }
        }
      }
      return false;
    }
  }
}
