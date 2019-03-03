package com.android.internal.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.animation.DecelerateInterpolator;
import java.util.ArrayList;
import java.util.Iterator;

public class DrawableHolder
  implements Animator.AnimatorListener
{
  private static final boolean DBG = false;
  public static final DecelerateInterpolator EASE_OUT_INTERPOLATOR = new DecelerateInterpolator();
  private static final String TAG = "DrawableHolder";
  private float mAlpha = 1.0F;
  private ArrayList<ObjectAnimator> mAnimators = new ArrayList();
  private BitmapDrawable mDrawable;
  private ArrayList<ObjectAnimator> mNeedToStart = new ArrayList();
  private float mScaleX = 1.0F;
  private float mScaleY = 1.0F;
  private float mX = 0.0F;
  private float mY = 0.0F;
  
  public DrawableHolder(BitmapDrawable paramBitmapDrawable)
  {
    this(paramBitmapDrawable, 0.0F, 0.0F);
  }
  
  public DrawableHolder(BitmapDrawable paramBitmapDrawable, float paramFloat1, float paramFloat2)
  {
    mDrawable = paramBitmapDrawable;
    mX = paramFloat1;
    mY = paramFloat2;
    mDrawable.getPaint().setAntiAlias(true);
    mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
  }
  
  private DrawableHolder addAnimation(ObjectAnimator paramObjectAnimator, boolean paramBoolean)
  {
    if (paramObjectAnimator != null) {
      mAnimators.add(paramObjectAnimator);
    }
    mNeedToStart.add(paramObjectAnimator);
    return this;
  }
  
  public ObjectAnimator addAnimTo(long paramLong1, long paramLong2, String paramString, float paramFloat, boolean paramBoolean)
  {
    if (paramBoolean) {
      removeAnimationFor(paramString);
    }
    paramString = ObjectAnimator.ofFloat(this, paramString, new float[] { paramFloat });
    paramString.setDuration(paramLong1);
    paramString.setStartDelay(paramLong2);
    paramString.setInterpolator(EASE_OUT_INTERPOLATOR);
    addAnimation(paramString, paramBoolean);
    return paramString;
  }
  
  public void clearAnimations()
  {
    Iterator localIterator = mAnimators.iterator();
    while (localIterator.hasNext()) {
      ((ObjectAnimator)localIterator.next()).cancel();
    }
    mAnimators.clear();
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (mAlpha <= 0.00390625F) {
      return;
    }
    paramCanvas.save(1);
    paramCanvas.translate(mX, mY);
    paramCanvas.scale(mScaleX, mScaleY);
    paramCanvas.translate(getWidth() * -0.5F, -0.5F * getHeight());
    mDrawable.setAlpha(Math.round(mAlpha * 255.0F));
    mDrawable.draw(paramCanvas);
    paramCanvas.restore();
  }
  
  public float getAlpha()
  {
    return mAlpha;
  }
  
  public BitmapDrawable getDrawable()
  {
    return mDrawable;
  }
  
  public int getHeight()
  {
    return mDrawable.getIntrinsicHeight();
  }
  
  public float getScaleX()
  {
    return mScaleX;
  }
  
  public float getScaleY()
  {
    return mScaleY;
  }
  
  public int getWidth()
  {
    return mDrawable.getIntrinsicWidth();
  }
  
  public float getX()
  {
    return mX;
  }
  
  public float getY()
  {
    return mY;
  }
  
  public void onAnimationCancel(Animator paramAnimator) {}
  
  public void onAnimationEnd(Animator paramAnimator)
  {
    mAnimators.remove(paramAnimator);
  }
  
  public void onAnimationRepeat(Animator paramAnimator) {}
  
  public void onAnimationStart(Animator paramAnimator) {}
  
  public void removeAnimationFor(String paramString)
  {
    Iterator localIterator = ((ArrayList)mAnimators.clone()).iterator();
    while (localIterator.hasNext())
    {
      ObjectAnimator localObjectAnimator = (ObjectAnimator)localIterator.next();
      if (paramString.equals(localObjectAnimator.getPropertyName())) {
        localObjectAnimator.cancel();
      }
    }
  }
  
  public void setAlpha(float paramFloat)
  {
    mAlpha = paramFloat;
  }
  
  public void setScaleX(float paramFloat)
  {
    mScaleX = paramFloat;
  }
  
  public void setScaleY(float paramFloat)
  {
    mScaleY = paramFloat;
  }
  
  public void setX(float paramFloat)
  {
    mX = paramFloat;
  }
  
  public void setY(float paramFloat)
  {
    mY = paramFloat;
  }
  
  public void startAnimations(ValueAnimator.AnimatorUpdateListener paramAnimatorUpdateListener)
  {
    for (int i = 0; i < mNeedToStart.size(); i++)
    {
      ObjectAnimator localObjectAnimator = (ObjectAnimator)mNeedToStart.get(i);
      localObjectAnimator.addUpdateListener(paramAnimatorUpdateListener);
      localObjectAnimator.addListener(this);
      localObjectAnimator.start();
    }
    mNeedToStart.clear();
  }
}
