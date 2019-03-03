package android.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.SystemClock;

public class TransitionDrawable
  extends LayerDrawable
  implements Drawable.Callback
{
  private static final int TRANSITION_NONE = 2;
  private static final int TRANSITION_RUNNING = 1;
  private static final int TRANSITION_STARTING = 0;
  private int mAlpha = 0;
  private boolean mCrossFade;
  private int mDuration;
  private int mFrom;
  private int mOriginalDuration;
  private boolean mReverse;
  private long mStartTimeMillis;
  private int mTo;
  private int mTransitionState = 2;
  
  TransitionDrawable()
  {
    this(new TransitionState(null, null, null), (Resources)null);
  }
  
  private TransitionDrawable(TransitionState paramTransitionState, Resources paramResources)
  {
    super(paramTransitionState, paramResources);
  }
  
  private TransitionDrawable(TransitionState paramTransitionState, Drawable[] paramArrayOfDrawable)
  {
    super(paramArrayOfDrawable, paramTransitionState);
  }
  
  public TransitionDrawable(Drawable[] paramArrayOfDrawable)
  {
    this(new TransitionState(null, null, null), paramArrayOfDrawable);
  }
  
  LayerDrawable.LayerState createConstantState(LayerDrawable.LayerState paramLayerState, Resources paramResources)
  {
    return new TransitionState((TransitionState)paramLayerState, this, paramResources);
  }
  
  public void draw(Canvas paramCanvas)
  {
    int i = 1;
    switch (mTransitionState)
    {
    default: 
      break;
    case 1: 
      if (mStartTimeMillis >= 0L)
      {
        float f = (float)(SystemClock.uptimeMillis() - mStartTimeMillis) / mDuration;
        if (f >= 1.0F) {
          i = 1;
        } else {
          i = 0;
        }
        f = Math.min(f, 1.0F);
        mAlpha = ((int)(mFrom + (mTo - mFrom) * f));
      }
      break;
    case 0: 
      mStartTimeMillis = SystemClock.uptimeMillis();
      i = 0;
      mTransitionState = 1;
    }
    int j = mAlpha;
    boolean bool = mCrossFade;
    LayerDrawable.ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    if (i != 0)
    {
      if ((!bool) || (j == 0)) {
        0mDrawable.draw(paramCanvas);
      }
      if (j == 255) {
        1mDrawable.draw(paramCanvas);
      }
      return;
    }
    Drawable localDrawable = 0mDrawable;
    if (bool) {
      localDrawable.setAlpha(255 - j);
    }
    localDrawable.draw(paramCanvas);
    if (bool) {
      localDrawable.setAlpha(255);
    }
    if (j > 0)
    {
      localDrawable = 1mDrawable;
      localDrawable.setAlpha(j);
      localDrawable.draw(paramCanvas);
      localDrawable.setAlpha(255);
    }
    if (i == 0) {
      invalidateSelf();
    }
  }
  
  public boolean isCrossFadeEnabled()
  {
    return mCrossFade;
  }
  
  public void resetTransition()
  {
    mAlpha = 0;
    mTransitionState = 2;
    invalidateSelf();
  }
  
  public void reverseTransition(int paramInt)
  {
    long l1 = SystemClock.uptimeMillis();
    long l2 = mStartTimeMillis;
    long l3 = mDuration;
    int i = 255;
    if (l1 - l2 > l3)
    {
      if (mTo == 0)
      {
        mFrom = 0;
        mTo = 255;
        mAlpha = 0;
        mReverse = false;
      }
      else
      {
        mFrom = 255;
        mTo = 0;
        mAlpha = 255;
        mReverse = true;
      }
      mOriginalDuration = paramInt;
      mDuration = paramInt;
      mTransitionState = 0;
      invalidateSelf();
      return;
    }
    mReverse ^= true;
    mFrom = mAlpha;
    paramInt = i;
    if (mReverse) {
      paramInt = 0;
    }
    mTo = paramInt;
    if (mReverse) {
      l3 = l1 - mStartTimeMillis;
    } else {
      l3 = mOriginalDuration - (l1 - mStartTimeMillis);
    }
    mDuration = ((int)l3);
    mTransitionState = 0;
  }
  
  public void setCrossFadeEnabled(boolean paramBoolean)
  {
    mCrossFade = paramBoolean;
  }
  
  public void showSecondLayer()
  {
    mAlpha = 255;
    mReverse = false;
    mTransitionState = 2;
    invalidateSelf();
  }
  
  public void startTransition(int paramInt)
  {
    mFrom = 0;
    mTo = 255;
    mAlpha = 0;
    mOriginalDuration = paramInt;
    mDuration = paramInt;
    mReverse = false;
    mTransitionState = 0;
    invalidateSelf();
  }
  
  static class TransitionState
    extends LayerDrawable.LayerState
  {
    TransitionState(TransitionState paramTransitionState, TransitionDrawable paramTransitionDrawable, Resources paramResources)
    {
      super(paramTransitionDrawable, paramResources);
    }
    
    public int getChangingConfigurations()
    {
      return mChangingConfigurations;
    }
    
    public Drawable newDrawable()
    {
      return new TransitionDrawable(this, (Resources)null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new TransitionDrawable(this, paramResources, null);
    }
  }
}
