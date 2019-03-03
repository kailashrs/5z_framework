package android.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.LayerDrawable;

public final class LauncherIcons
{
  private static final int AMBIENT_SHADOW_ALPHA = 30;
  private static final float ICON_SIZE_BLUR_FACTOR = 0.010416667F;
  private static final float ICON_SIZE_KEY_SHADOW_DELTA_FACTOR = 0.020833334F;
  private static final int KEY_SHADOW_ALPHA = 61;
  private final int mIconSize;
  private final Resources mRes;
  private final SparseArray<Bitmap> mShadowCache = new SparseArray();
  
  public LauncherIcons(Context paramContext)
  {
    mRes = paramContext.getResources();
    mIconSize = mRes.getDimensionPixelSize(17104896);
  }
  
  private Bitmap getShadowBitmap(AdaptiveIconDrawable arg1)
  {
    int i = Math.max(mIconSize, ???.getIntrinsicHeight());
    synchronized (mShadowCache)
    {
      Object localObject3 = (Bitmap)mShadowCache.get(i);
      if (localObject3 != null) {
        return localObject3;
      }
      ???.setBounds(0, 0, i, i);
      float f1 = 0.010416667F * i;
      float f2 = 0.020833334F * i;
      int j = (int)(i + 2.0F * f1 + f2);
      ??? = Bitmap.createBitmap(j, j, Bitmap.Config.ARGB_8888);
      localObject3 = new Canvas((Bitmap)???);
      ((Canvas)localObject3).translate(f2 / 2.0F + f1, f1);
      Paint localPaint = new Paint(1);
      localPaint.setColor(0);
      localPaint.setShadowLayer(f1, 0.0F, 0.0F, 503316480);
      ((Canvas)localObject3).drawPath(???.getIconMask(), localPaint);
      ((Canvas)localObject3).translate(0.0F, f2);
      localPaint.setShadowLayer(f1, 0.0F, 0.0F, 1023410176);
      ((Canvas)localObject3).drawPath(???.getIconMask(), localPaint);
      ((Canvas)localObject3).setBitmap(null);
      synchronized (mShadowCache)
      {
        mShadowCache.put(i, ???);
        return ???;
      }
    }
  }
  
  public Drawable getBadgeDrawable(int paramInt1, int paramInt2)
  {
    return getBadgedDrawable(null, paramInt1, paramInt2);
  }
  
  public Drawable getBadgedDrawable(Drawable paramDrawable, int paramInt1, int paramInt2)
  {
    Object localObject = Resources.getSystem();
    Drawable localDrawable1 = ((Resources)localObject).getDrawable(17302603);
    Drawable localDrawable2 = ((Resources)localObject).getDrawable(17302602).getConstantState().newDrawable().mutate();
    localObject = ((Resources)localObject).getDrawable(paramInt1);
    ((Drawable)localObject).setTint(paramInt2);
    if (paramDrawable == null)
    {
      paramDrawable = new Drawable[3];
      paramDrawable[0] = localDrawable1;
      paramDrawable[1] = localDrawable2;
      paramDrawable[2] = localObject;
    }
    else
    {
      paramDrawable = new Drawable[] { paramDrawable, localDrawable1, localDrawable2, localObject };
    }
    return new LayerDrawable(paramDrawable);
  }
  
  public Drawable getBadgedDrawable(Drawable paramDrawable, int paramInt1, int paramInt2, int paramInt3)
  {
    Object localObject = Resources.getSystem();
    Drawable localDrawable1 = ((Resources)localObject).getDrawable(17302603);
    localDrawable1.setTint(paramInt3);
    Drawable localDrawable2 = ((Resources)localObject).getDrawable(17302602).getConstantState().newDrawable().mutate();
    localDrawable2.setTint(paramInt2);
    localObject = ((Resources)localObject).getDrawable(paramInt1);
    if (paramDrawable == null)
    {
      paramDrawable = new Drawable[3];
      paramDrawable[0] = localDrawable1;
      paramDrawable[1] = localDrawable2;
      paramDrawable[2] = localObject;
    }
    else
    {
      paramDrawable = new Drawable[] { paramDrawable, localDrawable1, localDrawable2, localObject };
    }
    return new LayerDrawable(paramDrawable);
  }
  
  public Drawable wrapIconDrawableWithShadow(Drawable paramDrawable)
  {
    if (!(paramDrawable instanceof AdaptiveIconDrawable)) {
      return paramDrawable;
    }
    return new ShadowDrawable(getShadowBitmap((AdaptiveIconDrawable)paramDrawable), paramDrawable);
  }
  
  private static class ShadowDrawable
    extends DrawableWrapper
  {
    final MyConstantState mState;
    
    public ShadowDrawable(Bitmap paramBitmap, Drawable paramDrawable)
    {
      super();
      mState = new MyConstantState(paramBitmap, paramDrawable.getConstantState());
    }
    
    ShadowDrawable(MyConstantState paramMyConstantState)
    {
      super();
      mState = paramMyConstantState;
    }
    
    public void draw(Canvas paramCanvas)
    {
      Rect localRect = getBounds();
      paramCanvas.drawBitmap(mState.mShadow, null, localRect, mState.mPaint);
      paramCanvas.save();
      paramCanvas.translate(localRect.width() * 0.9599999F * 0.020833334F, localRect.height() * 0.9599999F * 0.010416667F);
      paramCanvas.scale(0.9599999F, 0.9599999F);
      super.draw(paramCanvas);
      paramCanvas.restore();
    }
    
    public Drawable.ConstantState getConstantState()
    {
      return mState;
    }
    
    private static class MyConstantState
      extends Drawable.ConstantState
    {
      final Drawable.ConstantState mChildState;
      final Paint mPaint = new Paint(2);
      final Bitmap mShadow;
      
      MyConstantState(Bitmap paramBitmap, Drawable.ConstantState paramConstantState)
      {
        mShadow = paramBitmap;
        mChildState = paramConstantState;
      }
      
      public int getChangingConfigurations()
      {
        return mChildState.getChangingConfigurations();
      }
      
      public Drawable newDrawable()
      {
        return new LauncherIcons.ShadowDrawable(this);
      }
    }
  }
}
