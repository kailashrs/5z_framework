package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.BoostFramework;
import android.util.DisplayMetrics;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class Scroller
{
  private static float DECELERATION_RATE = (float)(Math.log(0.78D) / Math.log(0.9D));
  private static final int DEFAULT_DURATION = 250;
  private static final float END_TENSION = 1.0F;
  private static final int FLING_MODE = 1;
  private static final float INFLEXION = 0.35F;
  private static final int NB_SAMPLES = 100;
  private static final float P1 = 0.175F;
  private static final float P2 = 0.35000002F;
  private static final int SCROLL_MODE = 0;
  private static final float[] SPLINE_POSITION = new float[101];
  private static final float[] SPLINE_TIME = new float[101];
  private static final float START_TENSION = 0.5F;
  private Context mContext;
  private float mCurrVelocity;
  private int mCurrX;
  private int mCurrY;
  private float mDeceleration;
  private float mDeltaX;
  private float mDeltaY;
  private int mDistance;
  private int mDuration;
  private float mDurationReciprocal;
  private int mFinalX;
  private int mFinalY;
  private boolean mFinished = true;
  private float mFlingFriction = ViewConfiguration.getScrollFriction();
  private boolean mFlywheel;
  private final Interpolator mInterpolator;
  private int mMaxX;
  private int mMaxY;
  private int mMinX;
  private int mMinY;
  private int mMode;
  private BoostFramework mPerf = null;
  private float mPhysicalCoeff;
  private final float mPpi;
  private long mStartTime;
  private int mStartX;
  private int mStartY;
  private float mVelocity;
  
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
  
  public Scroller(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Scroller(Context paramContext, Interpolator paramInterpolator)
  {
    this(paramContext, paramInterpolator, bool);
  }
  
  public Scroller(Context paramContext, Interpolator paramInterpolator, boolean paramBoolean)
  {
    mContext = paramContext;
    if (paramInterpolator == null) {
      mInterpolator = new ViscousFluidInterpolator();
    } else {
      mInterpolator = paramInterpolator;
    }
    mPpi = (getResourcesgetDisplayMetricsdensity * 160.0F);
    mDeceleration = computeDeceleration(ViewConfiguration.getScrollFriction());
    mFlywheel = paramBoolean;
    mPhysicalCoeff = computeDeceleration(0.84F);
    if (mPerf == null) {
      mPerf = new BoostFramework(paramContext);
    }
  }
  
  private float computeDeceleration(float paramFloat)
  {
    return 386.0878F * mPpi * paramFloat;
  }
  
  private double getSplineDeceleration(float paramFloat)
  {
    return Math.log(0.35F * Math.abs(paramFloat) / (mFlingFriction * mPhysicalCoeff));
  }
  
  private double getSplineFlingDistance(float paramFloat)
  {
    double d1 = getSplineDeceleration(paramFloat);
    double d2 = DECELERATION_RATE;
    return mFlingFriction * mPhysicalCoeff * Math.exp(DECELERATION_RATE / (d2 - 1.0D) * d1);
  }
  
  private int getSplineFlingDuration(float paramFloat)
  {
    return (int)(1000.0D * Math.exp(getSplineDeceleration(paramFloat) / (DECELERATION_RATE - 1.0D)));
  }
  
  public void abortAnimation()
  {
    mCurrX = mFinalX;
    mCurrY = mFinalY;
    mFinished = true;
  }
  
  public boolean computeScrollOffset()
  {
    if (mFinished) {
      return false;
    }
    int i = (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
    if (i < mDuration)
    {
      float f2;
      switch (mMode)
      {
      default: 
        break;
      case 1: 
        float f1 = i / mDuration;
        i = (int)(100.0F * f1);
        f2 = 1.0F;
        float f3 = 0.0F;
        if (i < 100)
        {
          f2 = i / 100.0F;
          f3 = (i + 1) / 100.0F;
          float f4 = SPLINE_POSITION[i];
          f3 = (SPLINE_POSITION[(i + 1)] - f4) / (f3 - f2);
          f2 = f4 + (f1 - f2) * f3;
        }
        mCurrVelocity = (mDistance * f3 / mDuration * 1000.0F);
        mCurrX = (mStartX + Math.round((mFinalX - mStartX) * f2));
        mCurrX = Math.min(mCurrX, mMaxX);
        mCurrX = Math.max(mCurrX, mMinX);
        mCurrY = (mStartY + Math.round((mFinalY - mStartY) * f2));
        mCurrY = Math.min(mCurrY, mMaxY);
        mCurrY = Math.max(mCurrY, mMinY);
        if ((mCurrX == mFinalX) && (mCurrY == mFinalY)) {
          mFinished = true;
        }
        break;
      case 0: 
        f2 = mInterpolator.getInterpolation(i * mDurationReciprocal);
        mCurrX = (mStartX + Math.round(mDeltaX * f2));
        mCurrY = (mStartY + Math.round(mDeltaY * f2));
      }
    }
    else
    {
      mCurrX = mFinalX;
      mCurrY = mFinalY;
      mFinished = true;
    }
    return true;
  }
  
  public void extendDuration(int paramInt)
  {
    mDuration = (timePassed() + paramInt);
    mDurationReciprocal = (1.0F / mDuration);
    mFinished = false;
  }
  
  public void fling(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    int i = paramInt4;
    int j = paramInt3;
    paramInt4 = i;
    float f1;
    if (mFlywheel)
    {
      j = paramInt3;
      paramInt4 = i;
      if (!mFinished)
      {
        f1 = getCurrVelocity();
        float f2 = mFinalX - mStartX;
        f3 = mFinalY - mStartY;
        f4 = (float)Math.hypot(f2, f3);
        f2 /= f4;
        f3 /= f4;
        f4 = f2 * f1;
        f1 = f3 * f1;
        j = paramInt3;
        paramInt4 = i;
        if (Math.signum(paramInt3) == Math.signum(f4))
        {
          j = paramInt3;
          paramInt4 = i;
          if (Math.signum(i) == Math.signum(f1))
          {
            j = (int)(paramInt3 + f4);
            paramInt4 = (int)(i + f1);
          }
        }
      }
    }
    mMode = 1;
    mFinished = false;
    float f4 = (float)Math.hypot(j, paramInt4);
    mVelocity = f4;
    mDuration = getSplineFlingDuration(f4);
    mStartTime = AnimationUtils.currentAnimationTimeMillis();
    mStartX = paramInt1;
    mStartY = paramInt2;
    float f3 = 1.0F;
    if (f4 == 0.0F) {
      f1 = 1.0F;
    } else {
      f1 = j / f4;
    }
    if (f4 != 0.0F) {
      f3 = paramInt4 / f4;
    }
    double d = getSplineFlingDistance(f4);
    mDistance = ((int)(Math.signum(f4) * d));
    mMinX = paramInt5;
    mMaxX = paramInt6;
    mMinY = paramInt7;
    mMaxY = paramInt8;
    mFinalX = ((int)Math.round(f1 * d) + paramInt1);
    mFinalX = Math.min(mFinalX, mMaxX);
    mFinalX = Math.max(mFinalX, mMinX);
    mFinalY = ((int)Math.round(f3 * d) + paramInt2);
    mFinalY = Math.min(mFinalY, mMaxY);
    mFinalY = Math.max(mFinalY, mMinY);
  }
  
  public final void forceFinished(boolean paramBoolean)
  {
    mFinished = paramBoolean;
  }
  
  public float getCurrVelocity()
  {
    float f;
    if (mMode == 1) {
      f = mCurrVelocity;
    } else {
      f = mVelocity - mDeceleration * timePassed() / 2000.0F;
    }
    return f;
  }
  
  public final int getCurrX()
  {
    return mCurrX;
  }
  
  public final int getCurrY()
  {
    return mCurrY;
  }
  
  public final int getDuration()
  {
    return mDuration;
  }
  
  public final int getFinalX()
  {
    return mFinalX;
  }
  
  public final int getFinalY()
  {
    return mFinalY;
  }
  
  public final int getStartX()
  {
    return mStartX;
  }
  
  public final int getStartY()
  {
    return mStartY;
  }
  
  public final boolean isFinished()
  {
    return mFinished;
  }
  
  public boolean isScrollingInDirection(float paramFloat1, float paramFloat2)
  {
    boolean bool;
    if ((!mFinished) && (Math.signum(paramFloat1) == Math.signum(mFinalX - mStartX)) && (Math.signum(paramFloat2) == Math.signum(mFinalY - mStartY))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setFinalX(int paramInt)
  {
    mFinalX = paramInt;
    mDeltaX = (mFinalX - mStartX);
    mFinished = false;
  }
  
  public void setFinalY(int paramInt)
  {
    mFinalY = paramInt;
    mDeltaY = (mFinalY - mStartY);
    mFinished = false;
  }
  
  public final void setFriction(float paramFloat)
  {
    mDeceleration = computeDeceleration(paramFloat);
    mFlingFriction = paramFloat;
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    startScroll(paramInt1, paramInt2, paramInt3, paramInt4, 250);
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    mMode = 0;
    mFinished = false;
    mDuration = paramInt5;
    mStartTime = AnimationUtils.currentAnimationTimeMillis();
    mStartX = paramInt1;
    mStartY = paramInt2;
    mFinalX = (paramInt1 + paramInt3);
    mFinalY = (paramInt2 + paramInt4);
    mDeltaX = paramInt3;
    mDeltaY = paramInt4;
    mDurationReciprocal = (1.0F / mDuration);
    if ((mPerf != null) && (paramInt5 != 0))
    {
      String str = mContext.getPackageName();
      mPerf.perfHint(4224, str, mDuration, 2);
    }
  }
  
  public int timePassed()
  {
    return (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
  }
  
  static class ViscousFluidInterpolator
    implements Interpolator
  {
    private static final float VISCOUS_FLUID_NORMALIZE = 1.0F / viscousFluid(1.0F);
    private static final float VISCOUS_FLUID_OFFSET = 1.0F - VISCOUS_FLUID_NORMALIZE * viscousFluid(1.0F);
    private static final float VISCOUS_FLUID_SCALE = 8.0F;
    
    ViscousFluidInterpolator() {}
    
    private static float viscousFluid(float paramFloat)
    {
      paramFloat *= 8.0F;
      if (paramFloat < 1.0F) {
        paramFloat -= 1.0F - (float)Math.exp(-paramFloat);
      } else {
        paramFloat = 0.36787945F + (1.0F - 0.36787945F) * (1.0F - (float)Math.exp(1.0F - paramFloat));
      }
      return paramFloat;
    }
    
    public float getInterpolation(float paramFloat)
    {
      paramFloat = VISCOUS_FLUID_NORMALIZE * viscousFluid(paramFloat);
      if (paramFloat > 0.0F) {
        return VISCOUS_FLUID_OFFSET + paramFloat;
      }
      return paramFloat;
    }
  }
}
