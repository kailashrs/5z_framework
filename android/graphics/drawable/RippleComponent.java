package android.graphics.drawable;

import android.graphics.Rect;

abstract class RippleComponent
{
  protected final Rect mBounds;
  protected float mDensityScale;
  private boolean mHasMaxRadius;
  protected final RippleDrawable mOwner;
  protected float mTargetRadius;
  
  public RippleComponent(RippleDrawable paramRippleDrawable, Rect paramRect)
  {
    mOwner = paramRippleDrawable;
    mBounds = paramRect;
  }
  
  private static float getTargetRadius(Rect paramRect)
  {
    float f1 = paramRect.width() / 2.0F;
    float f2 = paramRect.height() / 2.0F;
    return (float)Math.sqrt(f1 * f1 + f2 * f2);
  }
  
  public void getBounds(Rect paramRect)
  {
    int i = (int)Math.ceil(mTargetRadius);
    paramRect.set(-i, -i, i, i);
  }
  
  protected final void invalidateSelf()
  {
    mOwner.invalidateSelf(false);
  }
  
  public void onBoundsChange()
  {
    if (!mHasMaxRadius)
    {
      mTargetRadius = getTargetRadius(mBounds);
      onTargetRadiusChanged(mTargetRadius);
    }
  }
  
  protected final void onHotspotBoundsChanged()
  {
    if (!mHasMaxRadius)
    {
      mTargetRadius = getTargetRadius(mBounds);
      onTargetRadiusChanged(mTargetRadius);
    }
  }
  
  protected void onTargetRadiusChanged(float paramFloat) {}
  
  public final void setup(float paramFloat, int paramInt)
  {
    if (paramFloat >= 0.0F)
    {
      mHasMaxRadius = true;
      mTargetRadius = paramFloat;
    }
    else
    {
      mTargetRadius = getTargetRadius(mBounds);
    }
    mDensityScale = (paramInt * 0.00625F);
    onTargetRadiusChanged(mTargetRadius);
  }
}
