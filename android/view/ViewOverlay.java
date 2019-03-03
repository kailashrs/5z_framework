package android.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.Iterator;

public class ViewOverlay
{
  OverlayViewGroup mOverlayViewGroup;
  
  ViewOverlay(Context paramContext, View paramView)
  {
    mOverlayViewGroup = new OverlayViewGroup(paramContext, paramView);
  }
  
  public void add(Drawable paramDrawable)
  {
    mOverlayViewGroup.add(paramDrawable);
  }
  
  public void clear()
  {
    mOverlayViewGroup.clear();
  }
  
  ViewGroup getOverlayView()
  {
    return mOverlayViewGroup;
  }
  
  boolean isEmpty()
  {
    return mOverlayViewGroup.isEmpty();
  }
  
  public void remove(Drawable paramDrawable)
  {
    mOverlayViewGroup.remove(paramDrawable);
  }
  
  static class OverlayViewGroup
    extends ViewGroup
  {
    ArrayList<Drawable> mDrawables = null;
    final View mHostView;
    
    OverlayViewGroup(Context paramContext, View paramView)
    {
      super();
      mHostView = paramView;
      mAttachInfo = mHostView.mAttachInfo;
      mRight = paramView.getWidth();
      mBottom = paramView.getHeight();
      mRenderNode.setLeftTopRightBottom(0, 0, mRight, mBottom);
    }
    
    public void add(Drawable paramDrawable)
    {
      if (paramDrawable != null)
      {
        if (mDrawables == null) {
          mDrawables = new ArrayList();
        }
        if (!mDrawables.contains(paramDrawable))
        {
          mDrawables.add(paramDrawable);
          invalidate(paramDrawable.getBounds());
          paramDrawable.setCallback(this);
        }
        return;
      }
      throw new IllegalArgumentException("drawable must be non-null");
    }
    
    public void add(View paramView)
    {
      if (paramView != null)
      {
        if ((paramView.getParent() instanceof ViewGroup))
        {
          ViewGroup localViewGroup = (ViewGroup)paramView.getParent();
          if ((localViewGroup != mHostView) && (localViewGroup.getParent() != null) && (mAttachInfo != null))
          {
            int[] arrayOfInt1 = new int[2];
            int[] arrayOfInt2 = new int[2];
            localViewGroup.getLocationOnScreen(arrayOfInt1);
            mHostView.getLocationOnScreen(arrayOfInt2);
            paramView.offsetLeftAndRight(arrayOfInt1[0] - arrayOfInt2[0]);
            paramView.offsetTopAndBottom(arrayOfInt1[1] - arrayOfInt2[1]);
          }
          localViewGroup.removeView(paramView);
          if (localViewGroup.getLayoutTransition() != null) {
            localViewGroup.getLayoutTransition().cancel(3);
          }
          if (paramView.getParent() != null) {
            mParent = null;
          }
        }
        super.addView(paramView);
        return;
      }
      throw new IllegalArgumentException("view must be non-null");
    }
    
    public void clear()
    {
      removeAllViews();
      if (mDrawables != null)
      {
        Iterator localIterator = mDrawables.iterator();
        while (localIterator.hasNext()) {
          ((Drawable)localIterator.next()).setCallback(null);
        }
        mDrawables.clear();
      }
    }
    
    protected void dispatchDraw(Canvas paramCanvas)
    {
      paramCanvas.insertReorderBarrier();
      super.dispatchDraw(paramCanvas);
      paramCanvas.insertInorderBarrier();
      ArrayList localArrayList = mDrawables;
      int i = 0;
      int j;
      if (localArrayList == null) {
        j = 0;
      } else {
        j = mDrawables.size();
      }
      while (i < j)
      {
        ((Drawable)mDrawables.get(i)).draw(paramCanvas);
        i++;
      }
    }
    
    public void invalidate()
    {
      super.invalidate();
      if (mHostView != null) {
        mHostView.invalidate();
      }
    }
    
    public void invalidate(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super.invalidate(paramInt1, paramInt2, paramInt3, paramInt4);
      if (mHostView != null) {
        mHostView.invalidate(paramInt1, paramInt2, paramInt3, paramInt4);
      }
    }
    
    public void invalidate(Rect paramRect)
    {
      super.invalidate(paramRect);
      if (mHostView != null) {
        mHostView.invalidate(paramRect);
      }
    }
    
    public void invalidate(boolean paramBoolean)
    {
      super.invalidate(paramBoolean);
      if (mHostView != null) {
        mHostView.invalidate(paramBoolean);
      }
    }
    
    public ViewParent invalidateChildInParent(int[] paramArrayOfInt, Rect paramRect)
    {
      if (mHostView != null)
      {
        paramRect.offset(paramArrayOfInt[0], paramArrayOfInt[1]);
        if ((mHostView instanceof ViewGroup))
        {
          paramArrayOfInt[0] = 0;
          paramArrayOfInt[1] = 0;
          super.invalidateChildInParent(paramArrayOfInt, paramRect);
          return ((ViewGroup)mHostView).invalidateChildInParent(paramArrayOfInt, paramRect);
        }
        invalidate(paramRect);
      }
      return null;
    }
    
    public void invalidateDrawable(Drawable paramDrawable)
    {
      invalidate(paramDrawable.getBounds());
    }
    
    protected void invalidateParentCaches()
    {
      super.invalidateParentCaches();
      if (mHostView != null) {
        mHostView.invalidateParentCaches();
      }
    }
    
    protected void invalidateParentIfNeeded()
    {
      super.invalidateParentIfNeeded();
      if (mHostView != null) {
        mHostView.invalidateParentIfNeeded();
      }
    }
    
    void invalidateViewProperty(boolean paramBoolean1, boolean paramBoolean2)
    {
      super.invalidateViewProperty(paramBoolean1, paramBoolean2);
      if (mHostView != null) {
        mHostView.invalidateViewProperty(paramBoolean1, paramBoolean2);
      }
    }
    
    boolean isEmpty()
    {
      return (getChildCount() == 0) && ((mDrawables == null) || (mDrawables.size() == 0));
    }
    
    public void onDescendantInvalidated(View paramView1, View paramView2)
    {
      if (mHostView != null) {
        if ((mHostView instanceof ViewGroup))
        {
          ((ViewGroup)mHostView).onDescendantInvalidated(mHostView, paramView2);
          super.onDescendantInvalidated(paramView1, paramView2);
        }
        else
        {
          invalidate();
        }
      }
    }
    
    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
    
    public void remove(Drawable paramDrawable)
    {
      if (paramDrawable != null)
      {
        if (mDrawables != null)
        {
          mDrawables.remove(paramDrawable);
          invalidate(paramDrawable.getBounds());
          paramDrawable.setCallback(null);
        }
        return;
      }
      throw new IllegalArgumentException("drawable must be non-null");
    }
    
    public void remove(View paramView)
    {
      if (paramView != null)
      {
        super.removeView(paramView);
        return;
      }
      throw new IllegalArgumentException("view must be non-null");
    }
    
    protected boolean verifyDrawable(Drawable paramDrawable)
    {
      boolean bool;
      if ((!super.verifyDrawable(paramDrawable)) && ((mDrawables == null) || (!mDrawables.contains(paramDrawable)))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
  }
}
