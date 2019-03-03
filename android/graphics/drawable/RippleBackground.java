package android.graphics.drawable;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.FloatProperty;
import android.view.animation.LinearInterpolator;

class RippleBackground
  extends RippleComponent
{
  private static final TimeInterpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
  private static final BackgroundProperty OPACITY = new BackgroundProperty("opacity")
  {
    public Float get(RippleBackground paramAnonymousRippleBackground)
    {
      return Float.valueOf(mOpacity);
    }
    
    public void setValue(RippleBackground paramAnonymousRippleBackground, float paramAnonymousFloat)
    {
      RippleBackground.access$002(paramAnonymousRippleBackground, paramAnonymousFloat);
      paramAnonymousRippleBackground.invalidateSelf();
    }
  };
  private static final int OPACITY_DURATION = 80;
  private ObjectAnimator mAnimator;
  private boolean mFocused = false;
  private boolean mHovered = false;
  private boolean mIsBounded;
  private float mOpacity = 0.0F;
  
  public RippleBackground(RippleDrawable paramRippleDrawable, Rect paramRect, boolean paramBoolean)
  {
    super(paramRippleDrawable, paramRect);
    mIsBounded = paramBoolean;
  }
  
  private void onStateChanged()
  {
    float f;
    if (mFocused) {
      f = 0.6F;
    } else if (mHovered) {
      f = 0.2F;
    } else {
      f = 0.0F;
    }
    if (mAnimator != null)
    {
      mAnimator.cancel();
      mAnimator = null;
    }
    mAnimator = ObjectAnimator.ofFloat(this, OPACITY, new float[] { f });
    mAnimator.setDuration(80L);
    mAnimator.setInterpolator(LINEAR_INTERPOLATOR);
    mAnimator.start();
  }
  
  public void draw(Canvas paramCanvas, Paint paramPaint)
  {
    int i = paramPaint.getAlpha();
    int j = Math.min((int)(i * mOpacity + 0.5F), 255);
    if (j > 0)
    {
      paramPaint.setAlpha(j);
      paramCanvas.drawCircle(0.0F, 0.0F, mTargetRadius, paramPaint);
      paramPaint.setAlpha(i);
    }
  }
  
  public boolean isVisible()
  {
    boolean bool;
    if (mOpacity > 0.0F) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void jumpToFinal()
  {
    if (mAnimator != null)
    {
      mAnimator.end();
      mAnimator = null;
    }
  }
  
  public void setState(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    boolean bool1 = mFocused;
    boolean bool2 = false;
    boolean bool3 = paramBoolean1;
    if (!bool1)
    {
      if ((paramBoolean1) && (!paramBoolean3)) {
        paramBoolean1 = true;
      } else {
        paramBoolean1 = false;
      }
      bool3 = paramBoolean1;
    }
    paramBoolean1 = paramBoolean2;
    if (!mHovered)
    {
      paramBoolean1 = bool2;
      if (paramBoolean2)
      {
        paramBoolean1 = bool2;
        if (!paramBoolean3) {
          paramBoolean1 = true;
        }
      }
    }
    if ((mHovered != paramBoolean1) || (mFocused != bool3))
    {
      mHovered = paramBoolean1;
      mFocused = bool3;
      onStateChanged();
    }
  }
  
  private static abstract class BackgroundProperty
    extends FloatProperty<RippleBackground>
  {
    public BackgroundProperty(String paramString)
    {
      super();
    }
  }
}
