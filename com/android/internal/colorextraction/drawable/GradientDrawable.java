package com.android.internal.colorextraction.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.animation.DecelerateInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor.GradientColors;

public class GradientDrawable
  extends Drawable
{
  private static final float CENTRALIZED_CIRCLE_1 = -2.0F;
  private static final long COLOR_ANIMATION_DURATION = 2000L;
  private static final int GRADIENT_RADIUS = 480;
  private static final String TAG = "GradientDrawable";
  private int mAlpha = 255;
  private ValueAnimator mColorAnimation;
  private float mDensity;
  private int mMainColor;
  private int mMainColorTo;
  private final Paint mPaint;
  private int mSecondaryColor;
  private int mSecondaryColorTo;
  private final Splat mSplat;
  private final Rect mWindowBounds;
  
  public GradientDrawable(Context paramContext)
  {
    mDensity = getResourcesgetDisplayMetricsdensity;
    mSplat = new Splat(0.5F, 1.0F, 480.0F, -2.0F);
    mWindowBounds = new Rect();
    mPaint = new Paint();
    mPaint.setStyle(Paint.Style.FILL);
  }
  
  private void buildPaints()
  {
    Object localObject = mWindowBounds;
    if (((Rect)localObject).width() == 0) {
      return;
    }
    float f1 = ((Rect)localObject).width();
    float f2 = ((Rect)localObject).height();
    localObject = new RadialGradient(mSplat.x * f1, mSplat.y * f2, mSplat.radius * mDensity, mSecondaryColor, mMainColor, Shader.TileMode.CLAMP);
    mPaint.setShader((Shader)localObject);
  }
  
  public void draw(Canvas paramCanvas)
  {
    Rect localRect = mWindowBounds;
    if (localRect.width() != 0)
    {
      float f1 = localRect.width();
      float f2 = localRect.height();
      float f3 = mSplat.x * f1;
      float f4 = mSplat.y * f2;
      f2 = Math.max(f1, f2);
      paramCanvas.drawRect(f3 - f2, f4 - f2, f3 + f2, f4 + f2, mPaint);
      return;
    }
    throw new IllegalStateException("You need to call setScreenSize before drawing.");
  }
  
  public int getAlpha()
  {
    return mAlpha;
  }
  
  public ColorFilter getColorFilter()
  {
    return mPaint.getColorFilter();
  }
  
  @VisibleForTesting
  public int getMainColor()
  {
    return mMainColor;
  }
  
  public int getOpacity()
  {
    return -3;
  }
  
  @VisibleForTesting
  public int getSecondaryColor()
  {
    return mSecondaryColor;
  }
  
  public void setAlpha(int paramInt)
  {
    if (paramInt != mAlpha)
    {
      mAlpha = paramInt;
      mPaint.setAlpha(mAlpha);
      invalidateSelf();
    }
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    mPaint.setColorFilter(paramColorFilter);
  }
  
  public void setColors(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if ((paramInt1 == mMainColorTo) && (paramInt2 == mSecondaryColorTo)) {
      return;
    }
    if ((mColorAnimation != null) && (mColorAnimation.isRunning())) {
      mColorAnimation.cancel();
    }
    mMainColorTo = paramInt1;
    mSecondaryColorTo = paramInt1;
    if (paramBoolean)
    {
      int i = mMainColor;
      int j = mSecondaryColor;
      ValueAnimator localValueAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
      localValueAnimator.setDuration(2000L);
      localValueAnimator.addUpdateListener(new _..Lambda.GradientDrawable.lMoQsZzfSN2bVHgYiK0hm0tzCVE(this, i, paramInt1, j, paramInt2));
      localValueAnimator.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator, boolean paramAnonymousBoolean)
        {
          if (mColorAnimation == paramAnonymousAnimator) {
            GradientDrawable.access$002(GradientDrawable.this, null);
          }
        }
      });
      localValueAnimator.setInterpolator(new DecelerateInterpolator());
      localValueAnimator.start();
      mColorAnimation = localValueAnimator;
    }
    else
    {
      mMainColor = paramInt1;
      mSecondaryColor = paramInt2;
      buildPaints();
      invalidateSelf();
    }
  }
  
  public void setColors(ColorExtractor.GradientColors paramGradientColors)
  {
    setColors(paramGradientColors.getMainColor(), paramGradientColors.getSecondaryColor(), true);
  }
  
  public void setColors(ColorExtractor.GradientColors paramGradientColors, boolean paramBoolean)
  {
    setColors(paramGradientColors.getMainColor(), paramGradientColors.getSecondaryColor(), paramBoolean);
  }
  
  public void setScreenSize(int paramInt1, int paramInt2)
  {
    mWindowBounds.set(0, 0, paramInt1, paramInt2);
    setBounds(0, 0, paramInt1, paramInt2);
    buildPaints();
  }
  
  public void setXfermode(Xfermode paramXfermode)
  {
    mPaint.setXfermode(paramXfermode);
    invalidateSelf();
  }
  
  static final class Splat
  {
    final float colorIndex;
    final float radius;
    final float x;
    final float y;
    
    Splat(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
    {
      x = paramFloat1;
      y = paramFloat2;
      radius = paramFloat3;
      colorIndex = paramFloat4;
    }
  }
}
