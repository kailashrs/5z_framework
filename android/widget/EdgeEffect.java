package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.android.internal.R.styleable;

public class EdgeEffect
{
  private static final double ANGLE = 0.5235987755982988D;
  private static final float COS = (float)Math.cos(0.5235987755982988D);
  private static final float EPSILON = 0.001F;
  private static final float GLOW_ALPHA_START = 0.09F;
  private static final float MAX_ALPHA = 0.15F;
  private static final float MAX_GLOW_SCALE = 2.0F;
  private static final int MAX_VELOCITY = 10000;
  private static final int MIN_VELOCITY = 100;
  private static final int PULL_DECAY_TIME = 2000;
  private static final float PULL_DISTANCE_ALPHA_GLOW_FACTOR = 0.8F;
  private static final float PULL_GLOW_BEGIN = 0.0F;
  private static final int PULL_TIME = 167;
  private static final float RADIUS_FACTOR = 0.6F;
  private static final int RECEDE_TIME = 600;
  private static final float SIN = (float)Math.sin(0.5235987755982988D);
  private static final int STATE_ABSORB = 2;
  private static final int STATE_IDLE = 0;
  private static final int STATE_PULL = 1;
  private static final int STATE_PULL_DECAY = 4;
  private static final int STATE_RECEDE = 3;
  private static final String TAG = "EdgeEffect";
  private static final int VELOCITY_GLOW_FACTOR = 6;
  private float mBaseGlowScale;
  private final Rect mBounds = new Rect();
  private float mDisplacement = 0.5F;
  private float mDuration;
  private float mGlowAlpha;
  private float mGlowAlphaFinish;
  private float mGlowAlphaStart;
  private float mGlowScaleY;
  private float mGlowScaleYFinish;
  private float mGlowScaleYStart;
  private final Interpolator mInterpolator;
  private final Paint mPaint = new Paint();
  private float mPullDistance;
  private float mRadius;
  private long mStartTime;
  private int mState = 0;
  private float mTargetDisplacement = 0.5F;
  
  public EdgeEffect(Context paramContext)
  {
    mPaint.setAntiAlias(true);
    paramContext = paramContext.obtainStyledAttributes(R.styleable.EdgeEffect);
    int i = paramContext.getColor(0, -10066330);
    paramContext.recycle();
    mPaint.setColor(0xFFFFFF & i | 0x33000000);
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
    mInterpolator = new DecelerateInterpolator();
  }
  
  private void update()
  {
    float f1 = Math.min((float)(AnimationUtils.currentAnimationTimeMillis() - mStartTime) / mDuration, 1.0F);
    float f2 = mInterpolator.getInterpolation(f1);
    mGlowAlpha = (mGlowAlphaStart + (mGlowAlphaFinish - mGlowAlphaStart) * f2);
    mGlowScaleY = (mGlowScaleYStart + (mGlowScaleYFinish - mGlowScaleYStart) * f2);
    mDisplacement = ((mDisplacement + mTargetDisplacement) / 2.0F);
    if (f1 >= 0.999F) {
      switch (mState)
      {
      default: 
        break;
      case 4: 
        mState = 3;
        break;
      case 3: 
        mState = 0;
        break;
      case 2: 
        mState = 3;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mDuration = 600.0F;
        mGlowAlphaStart = mGlowAlpha;
        mGlowScaleYStart = mGlowScaleY;
        mGlowAlphaFinish = 0.0F;
        mGlowScaleYFinish = 0.0F;
        break;
      case 1: 
        mState = 4;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mDuration = 2000.0F;
        mGlowAlphaStart = mGlowAlpha;
        mGlowScaleYStart = mGlowScaleY;
        mGlowAlphaFinish = 0.0F;
        mGlowScaleYFinish = 0.0F;
      }
    }
  }
  
  public boolean draw(Canvas paramCanvas)
  {
    update();
    int i = paramCanvas.save();
    float f1 = mBounds.centerX();
    float f2 = mBounds.height();
    float f3 = mRadius;
    paramCanvas.scale(1.0F, Math.min(mGlowScaleY, 1.0F) * mBaseGlowScale, f1, 0.0F);
    float f4 = Math.max(0.0F, Math.min(mDisplacement, 1.0F));
    f4 = mBounds.width() * (f4 - 0.5F) / 2.0F;
    paramCanvas.clipRect(mBounds);
    paramCanvas.translate(f4, 0.0F);
    mPaint.setAlpha((int)(255.0F * mGlowAlpha));
    paramCanvas.drawCircle(f1, f2 - f3, mRadius, mPaint);
    paramCanvas.restoreToCount(i);
    int j = 0;
    int k = mState;
    boolean bool = false;
    i = j;
    if (k == 3)
    {
      i = j;
      if (mGlowScaleY == 0.0F)
      {
        mState = 0;
        i = 1;
      }
    }
    if ((mState == 0) && (i == 0)) {
      break label200;
    }
    bool = true;
    label200:
    return bool;
  }
  
  public void finish()
  {
    mState = 0;
  }
  
  public int getColor()
  {
    return mPaint.getColor();
  }
  
  public int getMaxHeight()
  {
    return (int)(mBounds.height() * 2.0F + 0.5F);
  }
  
  public boolean isFinished()
  {
    boolean bool;
    if (mState == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void onAbsorb(int paramInt)
  {
    mState = 2;
    paramInt = Math.min(Math.max(100, Math.abs(paramInt)), 10000);
    mStartTime = AnimationUtils.currentAnimationTimeMillis();
    mDuration = (paramInt * 0.02F + 0.15F);
    mGlowAlphaStart = 0.09F;
    mGlowScaleYStart = Math.max(mGlowScaleY, 0.0F);
    mGlowScaleYFinish = Math.min(0.025F + paramInt / 100 * paramInt * 1.5E-4F / 2.0F, 1.0F);
    mGlowAlphaFinish = Math.max(mGlowAlphaStart, Math.min(paramInt * 6 * 1.0E-5F, 0.15F));
    mTargetDisplacement = 0.5F;
  }
  
  public void onPull(float paramFloat)
  {
    onPull(paramFloat, 0.5F);
  }
  
  public void onPull(float paramFloat1, float paramFloat2)
  {
    long l = AnimationUtils.currentAnimationTimeMillis();
    mTargetDisplacement = paramFloat2;
    if ((mState == 4) && ((float)(l - mStartTime) < mDuration)) {
      return;
    }
    if (mState != 1) {
      mGlowScaleY = Math.max(0.0F, mGlowScaleY);
    }
    mState = 1;
    mStartTime = l;
    mDuration = 167.0F;
    mPullDistance += paramFloat1;
    paramFloat1 = Math.abs(paramFloat1);
    paramFloat1 = Math.min(0.15F, mGlowAlpha + 0.8F * paramFloat1);
    mGlowAlphaStart = paramFloat1;
    mGlowAlpha = paramFloat1;
    if (mPullDistance == 0.0F)
    {
      mGlowScaleYStart = 0.0F;
      mGlowScaleY = 0.0F;
    }
    else
    {
      paramFloat1 = (float)(Math.max(0.0D, 1.0D - 1.0D / Math.sqrt(Math.abs(mPullDistance) * mBounds.height()) - 0.3D) / 0.7D);
      mGlowScaleYStart = paramFloat1;
      mGlowScaleY = paramFloat1;
    }
    mGlowAlphaFinish = mGlowAlpha;
    mGlowScaleYFinish = mGlowScaleY;
  }
  
  public void onRelease()
  {
    mPullDistance = 0.0F;
    if ((mState != 1) && (mState != 4)) {
      return;
    }
    mState = 3;
    mGlowAlphaStart = mGlowAlpha;
    mGlowScaleYStart = mGlowScaleY;
    mGlowAlphaFinish = 0.0F;
    mGlowScaleYFinish = 0.0F;
    mStartTime = AnimationUtils.currentAnimationTimeMillis();
    mDuration = 600.0F;
  }
  
  public void setColor(int paramInt)
  {
    mPaint.setColor(paramInt);
  }
  
  public void setSize(int paramInt1, int paramInt2)
  {
    float f1 = paramInt1 * 0.6F / SIN;
    float f2 = f1 - COS * f1;
    float f3 = paramInt2 * 0.6F / SIN;
    float f4 = COS;
    mRadius = f1;
    f1 = 1.0F;
    if (f2 > 0.0F) {
      f1 = Math.min((f3 - f4 * f3) / f2, 1.0F);
    }
    mBaseGlowScale = f1;
    mBounds.set(mBounds.left, mBounds.top, paramInt1, (int)Math.min(paramInt2, f2));
  }
}
