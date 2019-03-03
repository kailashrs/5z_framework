package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.SystemProperties;
import android.util.BoostFramework;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class OverScroller
{
  private static final int DEFAULT_DURATION = 250;
  private static final int FLING_MODE = 1;
  private static boolean SCROLL_BOOST_SS_ENABLE = false;
  private static final int SCROLL_MODE = 0;
  private final boolean mFlywheel;
  private Interpolator mInterpolator;
  private int mMode;
  private final SplineOverScroller mScrollerX;
  private final SplineOverScroller mScrollerY;
  
  public OverScroller(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public OverScroller(Context paramContext, Interpolator paramInterpolator)
  {
    this(paramContext, paramInterpolator, true);
  }
  
  @Deprecated
  public OverScroller(Context paramContext, Interpolator paramInterpolator, float paramFloat1, float paramFloat2)
  {
    this(paramContext, paramInterpolator, true);
  }
  
  @Deprecated
  public OverScroller(Context paramContext, Interpolator paramInterpolator, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    this(paramContext, paramInterpolator, paramBoolean);
  }
  
  public OverScroller(Context paramContext, Interpolator paramInterpolator, boolean paramBoolean)
  {
    if (paramInterpolator == null) {
      mInterpolator = new Scroller.ViscousFluidInterpolator();
    } else {
      mInterpolator = paramInterpolator;
    }
    mFlywheel = paramBoolean;
    mScrollerX = new SplineOverScroller(paramContext);
    mScrollerY = new SplineOverScroller(paramContext);
    SCROLL_BOOST_SS_ENABLE = SystemProperties.getBoolean("vendor.perf.gestureflingboost.enable", false);
  }
  
  public void abortAnimation()
  {
    mScrollerX.finish();
    mScrollerY.finish();
  }
  
  public boolean computeScrollOffset()
  {
    if (isFinished()) {
      return false;
    }
    switch (mMode)
    {
    default: 
      break;
    case 1: 
      if ((!mScrollerX.mFinished) && (!mScrollerX.update()) && (!mScrollerX.continueWhenFinished())) {
        mScrollerX.finish();
      }
      if ((!mScrollerY.mFinished) && (!mScrollerY.update()) && (!mScrollerY.continueWhenFinished())) {
        mScrollerY.finish();
      }
      break;
    case 0: 
      long l = AnimationUtils.currentAnimationTimeMillis() - mScrollerX.mStartTime;
      int i = mScrollerX.mDuration;
      if (l < i)
      {
        float f = mInterpolator.getInterpolation((float)l / i);
        mScrollerX.updateScroll(f);
        mScrollerY.updateScroll(f);
      }
      else
      {
        abortAnimation();
      }
      break;
    }
    return true;
  }
  
  @Deprecated
  public void extendDuration(int paramInt)
  {
    mScrollerX.extendDuration(paramInt);
    mScrollerY.extendDuration(paramInt);
  }
  
  public void fling(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    fling(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, 0, 0);
  }
  
  public void fling(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10)
  {
    int i = paramInt3;
    int j = paramInt4;
    if (mFlywheel)
    {
      i = paramInt3;
      j = paramInt4;
      if (!isFinished())
      {
        float f1 = mScrollerX.mCurrVelocity;
        float f2 = mScrollerY.mCurrVelocity;
        i = paramInt3;
        j = paramInt4;
        if (Math.signum(paramInt3) == Math.signum(f1))
        {
          i = paramInt3;
          j = paramInt4;
          if (Math.signum(paramInt4) == Math.signum(f2))
          {
            i = (int)(paramInt3 + f1);
            j = (int)(paramInt4 + f2);
          }
        }
      }
    }
    mMode = 1;
    mScrollerX.fling(paramInt1, i, paramInt5, paramInt6, paramInt9);
    mScrollerY.fling(paramInt2, j, paramInt7, paramInt8, paramInt10);
  }
  
  public final void forceFinished(boolean paramBoolean)
  {
    SplineOverScroller.access$002(mScrollerX, SplineOverScroller.access$002(mScrollerY, paramBoolean));
  }
  
  public float getCurrVelocity()
  {
    return (float)Math.hypot(mScrollerX.mCurrVelocity, mScrollerY.mCurrVelocity);
  }
  
  public final int getCurrX()
  {
    return mScrollerX.mCurrentPosition;
  }
  
  public final int getCurrY()
  {
    return mScrollerY.mCurrentPosition;
  }
  
  @Deprecated
  public final int getDuration()
  {
    return Math.max(mScrollerX.mDuration, mScrollerY.mDuration);
  }
  
  public final int getFinalX()
  {
    return mScrollerX.mFinal;
  }
  
  public final int getFinalY()
  {
    return mScrollerY.mFinal;
  }
  
  public final int getStartX()
  {
    return mScrollerX.mStart;
  }
  
  public final int getStartY()
  {
    return mScrollerY.mStart;
  }
  
  public final boolean isFinished()
  {
    boolean bool;
    if ((mScrollerX.mFinished) && (mScrollerY.mFinished)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isOverScrolled()
  {
    boolean bool;
    if (((!mScrollerX.mFinished) && (mScrollerX.mState != 0)) || ((!mScrollerY.mFinished) && (mScrollerY.mState != 0))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isScrollingInDirection(float paramFloat1, float paramFloat2)
  {
    int i = mScrollerX.mFinal;
    int j = mScrollerX.mStart;
    int k = mScrollerY.mFinal;
    int m = mScrollerY.mStart;
    boolean bool;
    if ((!isFinished()) && (Math.signum(paramFloat1) == Math.signum(i - j)) && (Math.signum(paramFloat2) == Math.signum(k - m))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void notifyHorizontalEdgeReached(int paramInt1, int paramInt2, int paramInt3)
  {
    mScrollerX.notifyEdgeReached(paramInt1, paramInt2, paramInt3);
  }
  
  public void notifyVerticalEdgeReached(int paramInt1, int paramInt2, int paramInt3)
  {
    mScrollerY.notifyEdgeReached(paramInt1, paramInt2, paramInt3);
  }
  
  @Deprecated
  public void setFinalX(int paramInt)
  {
    mScrollerX.setFinalPosition(paramInt);
  }
  
  @Deprecated
  public void setFinalY(int paramInt)
  {
    mScrollerY.setFinalPosition(paramInt);
  }
  
  public final void setFriction(float paramFloat)
  {
    mScrollerX.setFriction(paramFloat);
    mScrollerY.setFriction(paramFloat);
  }
  
  void setInterpolator(Interpolator paramInterpolator)
  {
    if (paramInterpolator == null) {
      mInterpolator = new Scroller.ViscousFluidInterpolator();
    } else {
      mInterpolator = paramInterpolator;
    }
  }
  
  public boolean springBack(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    boolean bool1 = true;
    mMode = 1;
    boolean bool2 = mScrollerX.springback(paramInt1, paramInt3, paramInt4);
    boolean bool3 = mScrollerY.springback(paramInt2, paramInt5, paramInt6);
    boolean bool4 = bool1;
    if (!bool2) {
      if (bool3) {
        bool4 = bool1;
      } else {
        bool4 = false;
      }
    }
    return bool4;
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    startScroll(paramInt1, paramInt2, paramInt3, paramInt4, 250);
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    mMode = 0;
    mScrollerX.startScroll(paramInt1, paramInt3, paramInt5);
    mScrollerY.startScroll(paramInt2, paramInt4, paramInt5);
  }
  
  public int timePassed()
  {
    return (int)(AnimationUtils.currentAnimationTimeMillis() - Math.min(mScrollerX.mStartTime, mScrollerY.mStartTime));
  }
  
  static class SplineOverScroller
  {
    private static final int BALLISTIC = 2;
    private static final int CUBIC = 1;
    private static float DECELERATION_RATE = (float)(Math.log(0.78D) / Math.log(0.9D));
    private static final float END_TENSION = 1.0F;
    private static final float GRAVITY = 2000.0F;
    private static final float INFLEXION = 0.35F;
    private static final int NB_SAMPLES = 100;
    private static final float P1 = 0.175F;
    private static final float P2 = 0.35000002F;
    private static final int SPLINE = 0;
    private static final float[] SPLINE_POSITION = new float[101];
    private static final float[] SPLINE_TIME = new float[101];
    private static final float START_TENSION = 0.5F;
    private Context mContext;
    private float mCurrVelocity;
    private int mCurrentPosition;
    private float mDeceleration;
    private int mDuration;
    private int mFinal;
    private boolean mFinished;
    private float mFlingFriction = ViewConfiguration.getScrollFriction();
    private boolean mIsPerfLockAcquired = false;
    private int mOver;
    private BoostFramework mPerf = null;
    private float mPhysicalCoeff;
    private int mSplineDistance;
    private int mSplineDuration;
    private int mStart;
    private long mStartTime;
    private int mState = 0;
    private int mVelocity;
    
    static
    {
      float f1 = 0.0F;
      float f2 = 0.0F;
      int i = 0;
      if (i < 100)
      {
        float f3 = i / 100.0F;
        float f4 = 1.0F;
        for (;;)
        {
          float f5 = (f4 - f1) / 2.0F + f1;
          float f6 = 3.0F * f5 * (1.0F - f5);
          float f7 = ((1.0F - f5) * 0.175F + f5 * 0.35000002F) * f6 + f5 * f5 * f5;
          if (Math.abs(f7 - f3) < 1.0E-5D)
          {
            SPLINE_POSITION[i] = (((1.0F - f5) * 0.5F + f5) * f6 + f5 * f5 * f5);
            f4 = 1.0F;
            f5 = (f4 - f2) / 2.0F + f2;
            f7 = 3.0F * f5 * (1.0F - f5);
            f6 = ((1.0F - f5) * 0.5F + f5) * f7 + f5 * f5 * f5;
            if (Math.abs(f6 - f3) < 1.0E-5D)
            {
              SPLINE_TIME[i] = (((1.0F - f5) * 0.175F + 0.35000002F * f5) * f7 + f5 * f5 * f5);
              i++;
              break;
            }
            if (f6 > f3) {
              f4 = f5;
            }
            for (;;)
            {
              break;
              f2 = f5;
            }
          }
          if (f7 > f3) {
            f4 = f5;
          } else {
            f1 = f5;
          }
        }
      }
      float[] arrayOfFloat = SPLINE_POSITION;
      SPLINE_TIME[100] = 1.0F;
      arrayOfFloat[100] = 1.0F;
    }
    
    SplineOverScroller(Context paramContext)
    {
      mContext = paramContext;
      mFinished = true;
      mPhysicalCoeff = (386.0878F * (getResourcesgetDisplayMetricsdensity * 160.0F) * 0.84F);
      if ((!OverScroller.SCROLL_BOOST_SS_ENABLE) && (mPerf == null)) {
        mPerf = new BoostFramework(paramContext);
      }
    }
    
    private void adjustDuration(int paramInt1, int paramInt2, int paramInt3)
    {
      float f1 = Math.abs((paramInt3 - paramInt1) / (paramInt2 - paramInt1));
      paramInt1 = (int)(100.0F * f1);
      if (paramInt1 < 100)
      {
        float f2 = paramInt1 / 100.0F;
        float f3 = (paramInt1 + 1) / 100.0F;
        float f4 = SPLINE_TIME[paramInt1];
        float f5 = SPLINE_TIME[(paramInt1 + 1)];
        f2 = (f1 - f2) / (f3 - f2);
        mDuration = ((int)(mDuration * (f2 * (f5 - f4) + f4)));
      }
    }
    
    private void fitOnBounceCurve(int paramInt1, int paramInt2, int paramInt3)
    {
      float f1 = -paramInt3 / mDeceleration;
      float f2 = (float)Math.sqrt(2.0D * (paramInt3 * paramInt3 / 2.0F / Math.abs(mDeceleration) + Math.abs(paramInt2 - paramInt1)) / Math.abs(mDeceleration));
      mStartTime -= (int)(1000.0F * (f2 - f1));
      mStart = paramInt2;
      mCurrentPosition = paramInt2;
      mVelocity = ((int)(-mDeceleration * f2));
    }
    
    private static float getDeceleration(int paramInt)
    {
      float f;
      if (paramInt > 0) {
        f = -2000.0F;
      } else {
        f = 2000.0F;
      }
      return f;
    }
    
    private double getSplineDeceleration(int paramInt)
    {
      return Math.log(0.35F * Math.abs(paramInt) / (mFlingFriction * mPhysicalCoeff));
    }
    
    private double getSplineFlingDistance(int paramInt)
    {
      double d1 = getSplineDeceleration(paramInt);
      double d2 = DECELERATION_RATE;
      return mFlingFriction * mPhysicalCoeff * Math.exp(DECELERATION_RATE / (d2 - 1.0D) * d1);
    }
    
    private int getSplineFlingDuration(int paramInt)
    {
      return (int)(1000.0D * Math.exp(getSplineDeceleration(paramInt) / (DECELERATION_RATE - 1.0D)));
    }
    
    private void onEdgeReached()
    {
      float f1 = mVelocity * mVelocity;
      float f2 = f1 / (Math.abs(mDeceleration) * 2.0F);
      float f3 = Math.signum(mVelocity);
      float f4 = f2;
      if (f2 > mOver)
      {
        mDeceleration = (-f3 * f1 / (2.0F * mOver));
        f4 = mOver;
      }
      mOver = ((int)f4);
      mState = 2;
      int i = mStart;
      if (mVelocity <= 0) {
        f4 = -f4;
      }
      mFinal = (i + (int)f4);
      mDuration = (-(int)(1000.0F * mVelocity / mDeceleration));
    }
    
    private void startAfterEdge(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int i = 1;
      if ((paramInt1 > paramInt2) && (paramInt1 < paramInt3))
      {
        Log.e("OverScroller", "startAfterEdge called from a valid position");
        mFinished = true;
        return;
      }
      int j;
      if (paramInt1 > paramInt3) {
        j = 1;
      } else {
        j = 0;
      }
      int k;
      if (j != 0) {
        k = paramInt3;
      } else {
        k = paramInt2;
      }
      int m = paramInt1 - k;
      if (m * paramInt4 < 0) {
        i = 0;
      }
      if (i != 0)
      {
        startBounceAfterEdge(paramInt1, k, paramInt4);
      }
      else if (getSplineFlingDistance(paramInt4) > Math.abs(m))
      {
        if (j == 0) {
          paramInt2 = paramInt1;
        }
        if (j != 0) {
          paramInt3 = paramInt1;
        }
        fling(paramInt1, paramInt4, paramInt2, paramInt3, mOver);
      }
      else
      {
        startSpringback(paramInt1, k, paramInt4);
      }
    }
    
    private void startBounceAfterEdge(int paramInt1, int paramInt2, int paramInt3)
    {
      int i;
      if (paramInt3 == 0) {
        i = paramInt1 - paramInt2;
      } else {
        i = paramInt3;
      }
      mDeceleration = getDeceleration(i);
      fitOnBounceCurve(paramInt1, paramInt2, paramInt3);
      onEdgeReached();
    }
    
    private void startSpringback(int paramInt1, int paramInt2, int paramInt3)
    {
      mFinished = false;
      mState = 1;
      mStart = paramInt1;
      mCurrentPosition = paramInt1;
      mFinal = paramInt2;
      paramInt1 -= paramInt2;
      mDeceleration = getDeceleration(paramInt1);
      mVelocity = (-paramInt1);
      mOver = Math.abs(paramInt1);
      mDuration = ((int)(1000.0D * Math.sqrt(-2.0D * paramInt1 / mDeceleration)));
    }
    
    boolean continueWhenFinished()
    {
      switch (mState)
      {
      default: 
        break;
      case 2: 
        mStartTime += mDuration;
        startSpringback(mFinal, mStart, 0);
        break;
      case 1: 
        return false;
      case 0: 
        if (mDuration < mSplineDuration)
        {
          int i = mFinal;
          mStart = i;
          mCurrentPosition = i;
          mVelocity = ((int)mCurrVelocity);
          mDeceleration = getDeceleration(mVelocity);
          mStartTime += mDuration;
          onEdgeReached();
        }
        else
        {
          return false;
        }
        break;
      }
      update();
      return true;
    }
    
    void extendDuration(int paramInt)
    {
      mDuration = ((int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime) + paramInt);
      mFinished = false;
    }
    
    void finish()
    {
      if ((!OverScroller.SCROLL_BOOST_SS_ENABLE) && (mIsPerfLockAcquired) && (mPerf != null))
      {
        mPerf.perfLockRelease();
        mIsPerfLockAcquired = false;
      }
      mCurrentPosition = mFinal;
      mFinished = true;
    }
    
    void fling(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      mOver = paramInt5;
      mFinished = false;
      mVelocity = paramInt2;
      mCurrVelocity = paramInt2;
      mSplineDuration = 0;
      mDuration = 0;
      mStartTime = AnimationUtils.currentAnimationTimeMillis();
      mStart = paramInt1;
      mCurrentPosition = paramInt1;
      if ((!OverScroller.SCROLL_BOOST_SS_ENABLE) && (mIsPerfLockAcquired) && (mPerf != null))
      {
        mPerf.perfLockRelease();
        mIsPerfLockAcquired = false;
      }
      if ((paramInt1 <= paramInt4) && (paramInt1 >= paramInt3))
      {
        mState = 0;
        double d = 0.0D;
        if (paramInt2 != 0)
        {
          paramInt5 = getSplineFlingDuration(paramInt2);
          mSplineDuration = paramInt5;
          mDuration = paramInt5;
          d = getSplineFlingDistance(paramInt2);
        }
        mSplineDistance = ((int)(Math.signum(paramInt2) * d));
        mFinal = (mSplineDistance + paramInt1);
        if (mFinal < paramInt3)
        {
          adjustDuration(mStart, mFinal, paramInt3);
          mFinal = paramInt3;
        }
        if (mFinal > paramInt4)
        {
          adjustDuration(mStart, mFinal, paramInt4);
          mFinal = paramInt4;
        }
        return;
      }
      startAfterEdge(paramInt1, paramInt3, paramInt4, paramInt2);
    }
    
    void notifyEdgeReached(int paramInt1, int paramInt2, int paramInt3)
    {
      if (mState == 0)
      {
        mOver = paramInt3;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        startAfterEdge(paramInt1, paramInt2, paramInt2, (int)mCurrVelocity);
      }
    }
    
    void setFinalPosition(int paramInt)
    {
      mFinal = paramInt;
      mFinished = false;
    }
    
    void setFriction(float paramFloat)
    {
      mFlingFriction = paramFloat;
    }
    
    boolean springback(int paramInt1, int paramInt2, int paramInt3)
    {
      mFinished = true;
      mFinal = paramInt1;
      mStart = paramInt1;
      mCurrentPosition = paramInt1;
      mVelocity = 0;
      mStartTime = AnimationUtils.currentAnimationTimeMillis();
      mDuration = 0;
      if (paramInt1 < paramInt2) {
        startSpringback(paramInt1, paramInt2, 0);
      } else if (paramInt1 > paramInt3) {
        startSpringback(paramInt1, paramInt3, 0);
      }
      return true ^ mFinished;
    }
    
    void startScroll(int paramInt1, int paramInt2, int paramInt3)
    {
      mFinished = false;
      mStart = paramInt1;
      mCurrentPosition = paramInt1;
      mFinal = (paramInt1 + paramInt2);
      mStartTime = AnimationUtils.currentAnimationTimeMillis();
      mDuration = paramInt3;
      mDeceleration = 0.0F;
      mVelocity = 0;
    }
    
    boolean update()
    {
      long l = AnimationUtils.currentAnimationTimeMillis() - mStartTime;
      boolean bool = false;
      if (l == 0L)
      {
        if (mDuration > 0) {
          bool = true;
        }
        return bool;
      }
      if (l > mDuration) {
        return false;
      }
      if ((!OverScroller.SCROLL_BOOST_SS_ENABLE) && (mPerf != null) && (!mIsPerfLockAcquired))
      {
        String str = mContext.getPackageName();
        mIsPerfLockAcquired = true;
        mPerf.perfHint(4224, str, mDuration, 1);
      }
      double d = 0.0D;
      float f1;
      float f2;
      float f3;
      switch (mState)
      {
      default: 
        break;
      case 2: 
        f1 = (float)l / 1000.0F;
        mCurrVelocity = (mVelocity + mDeceleration * f1);
        d = mVelocity * f1 + mDeceleration * f1 * f1 / 2.0F;
        break;
      case 1: 
        f2 = (float)l / mDuration;
        f1 = f2 * f2;
        f3 = Math.signum(mVelocity);
        d = mOver * f3 * (3.0F * f1 - 2.0F * f2 * f1);
        mCurrVelocity = (mOver * f3 * 6.0F * (-f2 + f1));
        break;
      case 0: 
        f2 = (float)l / mSplineDuration;
        int i = (int)(100.0F * f2);
        f1 = 1.0F;
        f3 = 0.0F;
        if (i < 100)
        {
          f1 = i / 100.0F;
          f3 = (i + 1) / 100.0F;
          float f4 = SPLINE_POSITION[i];
          f3 = (SPLINE_POSITION[(i + 1)] - f4) / (f3 - f1);
          f1 = f4 + (f2 - f1) * f3;
        }
        d = mSplineDistance * f1;
        mCurrVelocity = (mSplineDistance * f3 / mSplineDuration * 1000.0F);
      }
      mCurrentPosition = (mStart + (int)Math.round(d));
      return true;
    }
    
    void updateScroll(float paramFloat)
    {
      mCurrentPosition = (mStart + Math.round((mFinal - mStart) * paramFloat));
    }
  }
}
