package android.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.CanvasProperty;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.FloatProperty;
import android.util.MathUtils;
import android.view.DisplayListCanvas;
import android.view.RenderNodeAnimator;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import java.util.ArrayList;

class RippleForeground
  extends RippleComponent
{
  private static final TimeInterpolator DECELERATE_INTERPOLATOR;
  private static final TimeInterpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
  private static final FloatProperty<RippleForeground> OPACITY = new FloatProperty("opacity")
  {
    public Float get(RippleForeground paramAnonymousRippleForeground)
    {
      return Float.valueOf(mOpacity);
    }
    
    public void setValue(RippleForeground paramAnonymousRippleForeground, float paramAnonymousFloat)
    {
      RippleForeground.access$902(paramAnonymousRippleForeground, paramAnonymousFloat);
      paramAnonymousRippleForeground.onAnimationPropertyChanged();
    }
  };
  private static final int OPACITY_ENTER_DURATION = 75;
  private static final int OPACITY_EXIT_DURATION = 150;
  private static final int OPACITY_HOLD_DURATION = 225;
  private static final int RIPPLE_ENTER_DURATION = 225;
  private static final int RIPPLE_ORIGIN_DURATION = 225;
  private static final FloatProperty<RippleForeground> TWEEN_ORIGIN;
  private static final FloatProperty<RippleForeground> TWEEN_RADIUS;
  private final AnimatorListenerAdapter mAnimationListener = new AnimatorListenerAdapter()
  {
    public void onAnimationEnd(Animator paramAnonymousAnimator)
    {
      RippleForeground.access$002(RippleForeground.this, true);
      RippleForeground.this.pruneHwFinished();
      RippleForeground.this.pruneSwFinished();
      if (mRunningHwAnimators.isEmpty()) {
        RippleForeground.this.clearHwProps();
      }
    }
  };
  private float mClampedStartingX;
  private float mClampedStartingY;
  private long mEnterStartedAtMillis;
  private final boolean mForceSoftware;
  private boolean mHasFinishedExit;
  private float mOpacity = 0.0F;
  private ArrayList<RenderNodeAnimator> mPendingHwAnimators = new ArrayList();
  private CanvasProperty<Paint> mPropPaint;
  private CanvasProperty<Float> mPropRadius;
  private CanvasProperty<Float> mPropX;
  private CanvasProperty<Float> mPropY;
  private ArrayList<RenderNodeAnimator> mRunningHwAnimators = new ArrayList();
  private ArrayList<Animator> mRunningSwAnimators = new ArrayList();
  private float mStartRadius = 0.0F;
  private float mStartingX;
  private float mStartingY;
  private float mTargetX = 0.0F;
  private float mTargetY = 0.0F;
  private float mTweenRadius = 0.0F;
  private float mTweenX = 0.0F;
  private float mTweenY = 0.0F;
  private boolean mUsingProperties;
  
  static
  {
    DECELERATE_INTERPOLATOR = new PathInterpolator(0.4F, 0.0F, 0.2F, 1.0F);
    TWEEN_RADIUS = new FloatProperty("tweenRadius")
    {
      public Float get(RippleForeground paramAnonymousRippleForeground)
      {
        return Float.valueOf(mTweenRadius);
      }
      
      public void setValue(RippleForeground paramAnonymousRippleForeground, float paramAnonymousFloat)
      {
        RippleForeground.access$502(paramAnonymousRippleForeground, paramAnonymousFloat);
        paramAnonymousRippleForeground.onAnimationPropertyChanged();
      }
    };
    TWEEN_ORIGIN = new FloatProperty("tweenOrigin")
    {
      public Float get(RippleForeground paramAnonymousRippleForeground)
      {
        return Float.valueOf(mTweenX);
      }
      
      public void setValue(RippleForeground paramAnonymousRippleForeground, float paramAnonymousFloat)
      {
        RippleForeground.access$702(paramAnonymousRippleForeground, paramAnonymousFloat);
        RippleForeground.access$802(paramAnonymousRippleForeground, paramAnonymousFloat);
        paramAnonymousRippleForeground.onAnimationPropertyChanged();
      }
    };
  }
  
  public RippleForeground(RippleDrawable paramRippleDrawable, Rect paramRect, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    super(paramRippleDrawable, paramRect);
    mForceSoftware = paramBoolean;
    mStartingX = paramFloat1;
    mStartingY = paramFloat2;
    mStartRadius = (Math.max(paramRect.width(), paramRect.height()) * 0.3F);
    clampStartingPosition();
  }
  
  private void clampStartingPosition()
  {
    float f1 = mBounds.exactCenterX();
    float f2 = mBounds.exactCenterY();
    float f3 = mStartingX - f1;
    float f4 = mStartingY - f2;
    float f5 = mTargetRadius - mStartRadius;
    if (f3 * f3 + f4 * f4 > f5 * f5)
    {
      double d = Math.atan2(f4, f3);
      mClampedStartingX = ((float)(Math.cos(d) * f5) + f1);
      mClampedStartingY = ((float)(Math.sin(d) * f5) + f2);
    }
    else
    {
      mClampedStartingX = mStartingX;
      mClampedStartingY = mStartingY;
    }
  }
  
  private void clearHwProps()
  {
    mPropPaint = null;
    mPropRadius = null;
    mPropX = null;
    mPropY = null;
    mUsingProperties = false;
  }
  
  private long computeFadeOutDelay()
  {
    long l = AnimationUtils.currentAnimationTimeMillis() - mEnterStartedAtMillis;
    if ((l > 0L) && (l < 225L)) {
      return 225L - l;
    }
    return 0L;
  }
  
  private void drawHardware(DisplayListCanvas paramDisplayListCanvas, Paint paramPaint)
  {
    startPending(paramDisplayListCanvas);
    pruneHwFinished();
    if (mPropPaint != null)
    {
      mUsingProperties = true;
      paramDisplayListCanvas.drawCircle(mPropX, mPropY, mPropRadius, mPropPaint);
    }
    else
    {
      mUsingProperties = false;
      drawSoftware(paramDisplayListCanvas, paramPaint);
    }
  }
  
  private void drawSoftware(Canvas paramCanvas, Paint paramPaint)
  {
    int i = paramPaint.getAlpha();
    int j = (int)(i * mOpacity + 0.5F);
    float f1 = getCurrentRadius();
    if ((j > 0) && (f1 > 0.0F))
    {
      float f2 = getCurrentX();
      float f3 = getCurrentY();
      paramPaint.setAlpha(j);
      paramCanvas.drawCircle(f2, f3, f1, paramPaint);
      paramPaint.setAlpha(i);
    }
  }
  
  private float getCurrentRadius()
  {
    return MathUtils.lerp(mStartRadius, mTargetRadius, mTweenRadius);
  }
  
  private float getCurrentX()
  {
    return MathUtils.lerp(mClampedStartingX - mBounds.exactCenterX(), mTargetX, mTweenX);
  }
  
  private float getCurrentY()
  {
    return MathUtils.lerp(mClampedStartingY - mBounds.exactCenterY(), mTargetY, mTweenY);
  }
  
  private void onAnimationPropertyChanged()
  {
    if (!mUsingProperties) {
      invalidateSelf();
    }
  }
  
  private void pruneHwFinished()
  {
    if (!mRunningHwAnimators.isEmpty()) {
      for (int i = mRunningHwAnimators.size() - 1; i >= 0; i--) {
        if (!((RenderNodeAnimator)mRunningHwAnimators.get(i)).isRunning()) {
          mRunningHwAnimators.remove(i);
        }
      }
    }
  }
  
  private void pruneSwFinished()
  {
    if (!mRunningSwAnimators.isEmpty()) {
      for (int i = mRunningSwAnimators.size() - 1; i >= 0; i--) {
        if (!((Animator)mRunningSwAnimators.get(i)).isRunning()) {
          mRunningSwAnimators.remove(i);
        }
      }
    }
  }
  
  private void startHardwareEnter()
  {
    if (mForceSoftware) {
      return;
    }
    mPropX = CanvasProperty.createFloat(getCurrentX());
    mPropY = CanvasProperty.createFloat(getCurrentY());
    mPropRadius = CanvasProperty.createFloat(getCurrentRadius());
    Object localObject = mOwner.getRipplePaint();
    mPropPaint = CanvasProperty.createPaint((Paint)localObject);
    RenderNodeAnimator localRenderNodeAnimator = new RenderNodeAnimator(mPropRadius, mTargetRadius);
    localRenderNodeAnimator.setDuration(225L);
    localRenderNodeAnimator.setInterpolator(DECELERATE_INTERPOLATOR);
    mPendingHwAnimators.add(localRenderNodeAnimator);
    localRenderNodeAnimator = new RenderNodeAnimator(mPropX, mTargetX);
    localRenderNodeAnimator.setDuration(225L);
    localRenderNodeAnimator.setInterpolator(DECELERATE_INTERPOLATOR);
    mPendingHwAnimators.add(localRenderNodeAnimator);
    localRenderNodeAnimator = new RenderNodeAnimator(mPropY, mTargetY);
    localRenderNodeAnimator.setDuration(225L);
    localRenderNodeAnimator.setInterpolator(DECELERATE_INTERPOLATOR);
    mPendingHwAnimators.add(localRenderNodeAnimator);
    localObject = new RenderNodeAnimator(mPropPaint, 1, ((Paint)localObject).getAlpha());
    ((RenderNodeAnimator)localObject).setDuration(75L);
    ((RenderNodeAnimator)localObject).setInterpolator(LINEAR_INTERPOLATOR);
    ((RenderNodeAnimator)localObject).setStartValue(0.0F);
    mPendingHwAnimators.add(localObject);
    invalidateSelf();
  }
  
  private void startHardwareExit()
  {
    if ((!mForceSoftware) && (mPropPaint != null))
    {
      RenderNodeAnimator localRenderNodeAnimator = new RenderNodeAnimator(mPropPaint, 1, 0.0F);
      localRenderNodeAnimator.setDuration(150L);
      localRenderNodeAnimator.setInterpolator(LINEAR_INTERPOLATOR);
      localRenderNodeAnimator.addListener(mAnimationListener);
      localRenderNodeAnimator.setStartDelay(computeFadeOutDelay());
      localRenderNodeAnimator.setStartValue(mOwner.getRipplePaint().getAlpha());
      mPendingHwAnimators.add(localRenderNodeAnimator);
      invalidateSelf();
      return;
    }
  }
  
  private void startPending(DisplayListCanvas paramDisplayListCanvas)
  {
    if (!mPendingHwAnimators.isEmpty())
    {
      for (int i = 0; i < mPendingHwAnimators.size(); i++)
      {
        RenderNodeAnimator localRenderNodeAnimator = (RenderNodeAnimator)mPendingHwAnimators.get(i);
        localRenderNodeAnimator.setTarget(paramDisplayListCanvas);
        localRenderNodeAnimator.start();
        mRunningHwAnimators.add(localRenderNodeAnimator);
      }
      mPendingHwAnimators.clear();
    }
  }
  
  private void startSoftwareEnter()
  {
    for (int i = 0; i < mRunningSwAnimators.size(); i++) {
      ((Animator)mRunningSwAnimators.get(i)).cancel();
    }
    mRunningSwAnimators.clear();
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(this, TWEEN_RADIUS, new float[] { 1.0F });
    localObjectAnimator.setDuration(225L);
    localObjectAnimator.setInterpolator(DECELERATE_INTERPOLATOR);
    localObjectAnimator.start();
    mRunningSwAnimators.add(localObjectAnimator);
    localObjectAnimator = ObjectAnimator.ofFloat(this, TWEEN_ORIGIN, new float[] { 1.0F });
    localObjectAnimator.setDuration(225L);
    localObjectAnimator.setInterpolator(DECELERATE_INTERPOLATOR);
    localObjectAnimator.start();
    mRunningSwAnimators.add(localObjectAnimator);
    localObjectAnimator = ObjectAnimator.ofFloat(this, OPACITY, new float[] { 1.0F });
    localObjectAnimator.setDuration(75L);
    localObjectAnimator.setInterpolator(LINEAR_INTERPOLATOR);
    localObjectAnimator.start();
    mRunningSwAnimators.add(localObjectAnimator);
  }
  
  private void startSoftwareExit()
  {
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(this, OPACITY, new float[] { 0.0F });
    localObjectAnimator.setDuration(150L);
    localObjectAnimator.setInterpolator(LINEAR_INTERPOLATOR);
    localObjectAnimator.addListener(mAnimationListener);
    localObjectAnimator.setStartDelay(computeFadeOutDelay());
    localObjectAnimator.start();
    mRunningSwAnimators.add(localObjectAnimator);
  }
  
  private void switchToUiThreadAnimation()
  {
    for (int i = 0; i < mRunningHwAnimators.size(); i++)
    {
      Animator localAnimator = (Animator)mRunningHwAnimators.get(i);
      localAnimator.removeListener(mAnimationListener);
      localAnimator.end();
    }
    mRunningHwAnimators.clear();
    clearHwProps();
    invalidateSelf();
  }
  
  public void draw(Canvas paramCanvas, Paint paramPaint)
  {
    int i;
    if ((!mForceSoftware) && ((paramCanvas instanceof DisplayListCanvas))) {
      i = 1;
    } else {
      i = 0;
    }
    pruneSwFinished();
    if (i != 0) {
      drawHardware((DisplayListCanvas)paramCanvas, paramPaint);
    } else {
      drawSoftware(paramCanvas, paramPaint);
    }
  }
  
  public void end()
  {
    int i = 0;
    for (int j = 0; j < mRunningSwAnimators.size(); j++) {
      ((Animator)mRunningSwAnimators.get(j)).end();
    }
    mRunningSwAnimators.clear();
    for (j = i; j < mRunningHwAnimators.size(); j++) {
      ((RenderNodeAnimator)mRunningHwAnimators.get(j)).end();
    }
    mRunningHwAnimators.clear();
  }
  
  public final void enter()
  {
    mEnterStartedAtMillis = AnimationUtils.currentAnimationTimeMillis();
    startSoftwareEnter();
    startHardwareEnter();
  }
  
  public final void exit()
  {
    startSoftwareExit();
    startHardwareExit();
  }
  
  public void getBounds(Rect paramRect)
  {
    int i = (int)mTargetX;
    int j = (int)mTargetY;
    int k = (int)mTargetRadius + 1;
    paramRect.set(i - k, j - k, i + k, j + k);
  }
  
  public boolean hasFinishedExit()
  {
    return mHasFinishedExit;
  }
  
  public void move(float paramFloat1, float paramFloat2)
  {
    mStartingX = paramFloat1;
    mStartingY = paramFloat2;
    clampStartingPosition();
  }
  
  protected void onTargetRadiusChanged(float paramFloat)
  {
    clampStartingPosition();
    switchToUiThreadAnimation();
  }
}
