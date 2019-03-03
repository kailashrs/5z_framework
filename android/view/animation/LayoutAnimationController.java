package android.view.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.android.internal.R.styleable;
import java.util.Random;

public class LayoutAnimationController
{
  public static final int ORDER_NORMAL = 0;
  public static final int ORDER_RANDOM = 2;
  public static final int ORDER_REVERSE = 1;
  protected Animation mAnimation;
  private float mDelay;
  private long mDuration;
  protected Interpolator mInterpolator;
  private long mMaxDelay;
  private int mOrder;
  protected Random mRandomizer;
  
  public LayoutAnimationController(Context paramContext, AttributeSet paramAttributeSet)
  {
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.LayoutAnimation);
    mDelay = parseValuepeekValue1value;
    mOrder = paramAttributeSet.getInt(3, 0);
    int i = paramAttributeSet.getResourceId(2, 0);
    if (i > 0) {
      setAnimation(paramContext, i);
    }
    i = paramAttributeSet.getResourceId(0, 0);
    if (i > 0) {
      setInterpolator(paramContext, i);
    }
    paramAttributeSet.recycle();
  }
  
  public LayoutAnimationController(Animation paramAnimation)
  {
    this(paramAnimation, 0.5F);
  }
  
  public LayoutAnimationController(Animation paramAnimation, float paramFloat)
  {
    mDelay = paramFloat;
    setAnimation(paramAnimation);
  }
  
  public Animation getAnimation()
  {
    return mAnimation;
  }
  
  public final Animation getAnimationForView(View paramView)
  {
    long l = getDelayForView(paramView) + mAnimation.getStartOffset();
    mMaxDelay = Math.max(mMaxDelay, l);
    try
    {
      paramView = mAnimation.clone();
      paramView.setStartOffset(l);
      return paramView;
    }
    catch (CloneNotSupportedException paramView) {}
    return null;
  }
  
  public float getDelay()
  {
    return mDelay;
  }
  
  protected long getDelayForView(View paramView)
  {
    paramView = getLayoutParamslayoutAnimationParameters;
    if (paramView == null) {
      return 0L;
    }
    float f1 = mDelay * (float)mAnimation.getDuration();
    long l = (getTransformedIndex(paramView) * f1);
    f1 = count * f1;
    if (mInterpolator == null) {
      mInterpolator = new LinearInterpolator();
    }
    float f2 = (float)l / f1;
    return (mInterpolator.getInterpolation(f2) * f1);
  }
  
  public Interpolator getInterpolator()
  {
    return mInterpolator;
  }
  
  public int getOrder()
  {
    return mOrder;
  }
  
  protected int getTransformedIndex(AnimationParameters paramAnimationParameters)
  {
    switch (getOrder())
    {
    default: 
      return index;
    case 2: 
      if (mRandomizer == null) {
        mRandomizer = new Random();
      }
      return (int)(count * mRandomizer.nextFloat());
    }
    return count - 1 - index;
  }
  
  public boolean isDone()
  {
    boolean bool;
    if (AnimationUtils.currentAnimationTimeMillis() > mAnimation.getStartTime() + mMaxDelay + mDuration) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setAnimation(Context paramContext, int paramInt)
  {
    setAnimation(AnimationUtils.loadAnimation(paramContext, paramInt));
  }
  
  public void setAnimation(Animation paramAnimation)
  {
    mAnimation = paramAnimation;
    mAnimation.setFillBefore(true);
  }
  
  public void setDelay(float paramFloat)
  {
    mDelay = paramFloat;
  }
  
  public void setInterpolator(Context paramContext, int paramInt)
  {
    setInterpolator(AnimationUtils.loadInterpolator(paramContext, paramInt));
  }
  
  public void setInterpolator(Interpolator paramInterpolator)
  {
    mInterpolator = paramInterpolator;
  }
  
  public void setOrder(int paramInt)
  {
    mOrder = paramInt;
  }
  
  public void start()
  {
    mDuration = mAnimation.getDuration();
    mMaxDelay = Long.MIN_VALUE;
    mAnimation.setStartTime(-1L);
  }
  
  public boolean willOverlap()
  {
    boolean bool;
    if (mDelay < 1.0F) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static class AnimationParameters
  {
    public int count;
    public int index;
    
    public AnimationParameters() {}
  }
}
