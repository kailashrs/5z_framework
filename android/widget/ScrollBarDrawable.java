package android.widget;

import android.graphics.Canvas;
import android.graphics.Canvas.EdgeType;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import com.android.internal.widget.ScrollBarUtils;

public class ScrollBarDrawable
  extends Drawable
  implements Drawable.Callback
{
  private int mAlpha = 255;
  private boolean mAlwaysDrawHorizontalTrack;
  private boolean mAlwaysDrawVerticalTrack;
  private boolean mBoundsChanged;
  private ColorFilter mColorFilter;
  private int mExtent;
  private boolean mHasSetAlpha;
  private boolean mHasSetColorFilter;
  private Drawable mHorizontalThumb;
  private Drawable mHorizontalTrack;
  private boolean mMutated;
  private int mOffset;
  private int mRange;
  private boolean mRangeChanged;
  private boolean mVertical;
  private Drawable mVerticalThumb;
  private Drawable mVerticalTrack;
  
  public ScrollBarDrawable() {}
  
  private void drawThumb(Canvas paramCanvas, Rect paramRect, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i;
    if ((!mRangeChanged) && (!mBoundsChanged)) {
      i = 0;
    } else {
      i = 1;
    }
    Drawable localDrawable;
    if (paramBoolean)
    {
      if (mVerticalThumb != null)
      {
        localDrawable = mVerticalThumb;
        if (i != 0) {
          localDrawable.setBounds(left, top + paramInt1, right, top + paramInt1 + paramInt2);
        }
        localDrawable.draw(paramCanvas);
      }
    }
    else if (mHorizontalThumb != null)
    {
      localDrawable = mHorizontalThumb;
      if (i != 0) {
        localDrawable.setBounds(left + paramInt1, top, left + paramInt1 + paramInt2, bottom);
      }
      localDrawable.draw(paramCanvas);
    }
  }
  
  private void drawTrack(Canvas paramCanvas, Rect paramRect, boolean paramBoolean)
  {
    Drawable localDrawable;
    if (paramBoolean) {
      localDrawable = mVerticalTrack;
    } else {
      localDrawable = mHorizontalTrack;
    }
    if (localDrawable != null)
    {
      if (mBoundsChanged) {
        localDrawable.setBounds(paramRect);
      }
      localDrawable.draw(paramCanvas);
    }
  }
  
  private void propagateCurrentState(Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      if (mMutated) {
        paramDrawable.mutate();
      }
      paramDrawable.setState(getState());
      paramDrawable.setCallback(this);
      if (mHasSetAlpha) {
        paramDrawable.setAlpha(mAlpha);
      }
      if (mHasSetColorFilter) {
        paramDrawable.setColorFilter(mColorFilter);
      }
    }
  }
  
  public void draw(Canvas paramCanvas)
  {
    boolean bool1 = mVertical;
    int i = mExtent;
    int j = mRange;
    boolean bool2 = true;
    int k = 1;
    if ((i > 0) && (j > i)) {}
    for (;;)
    {
      break;
      if (bool1) {
        bool2 = mAlwaysDrawVerticalTrack;
      } else {
        bool2 = mAlwaysDrawHorizontalTrack;
      }
      k = 0;
    }
    Rect localRect = getBounds();
    if (paramCanvas.quickReject(left, top, right, bottom, Canvas.EdgeType.AA)) {
      return;
    }
    if (bool2) {
      drawTrack(paramCanvas, localRect, bool1);
    }
    if (k != 0)
    {
      if (bool1) {
        k = localRect.height();
      } else {
        k = localRect.width();
      }
      if (bool1) {
        m = localRect.width();
      } else {
        m = localRect.height();
      }
      int m = ScrollBarUtils.getThumbLength(k, m, i, j);
      drawThumb(paramCanvas, localRect, ScrollBarUtils.getThumbOffset(k, m, i, j, mOffset), m, bool1);
    }
  }
  
  public int getAlpha()
  {
    return mAlpha;
  }
  
  public boolean getAlwaysDrawHorizontalTrack()
  {
    return mAlwaysDrawHorizontalTrack;
  }
  
  public boolean getAlwaysDrawVerticalTrack()
  {
    return mAlwaysDrawVerticalTrack;
  }
  
  public ColorFilter getColorFilter()
  {
    return mColorFilter;
  }
  
  public int getOpacity()
  {
    return -3;
  }
  
  public int getSize(boolean paramBoolean)
  {
    int i = 0;
    int j = 0;
    if (paramBoolean)
    {
      if (mVerticalTrack != null) {
        j = mVerticalTrack.getIntrinsicWidth();
      } else if (mVerticalThumb != null) {
        j = mVerticalThumb.getIntrinsicWidth();
      }
      return j;
    }
    if (mHorizontalTrack != null)
    {
      j = mHorizontalTrack.getIntrinsicHeight();
    }
    else
    {
      j = i;
      if (mHorizontalThumb != null) {
        j = mHorizontalThumb.getIntrinsicHeight();
      }
    }
    return j;
  }
  
  public void invalidateDrawable(Drawable paramDrawable)
  {
    invalidateSelf();
  }
  
  public boolean isStateful()
  {
    boolean bool;
    if (((mVerticalTrack != null) && (mVerticalTrack.isStateful())) || ((mVerticalThumb != null) && (mVerticalThumb.isStateful())) || ((mHorizontalTrack != null) && (mHorizontalTrack.isStateful())) || ((mHorizontalThumb != null) && (mHorizontalThumb.isStateful())) || (super.isStateful())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public ScrollBarDrawable mutate()
  {
    if ((!mMutated) && (super.mutate() == this))
    {
      if (mVerticalTrack != null) {
        mVerticalTrack.mutate();
      }
      if (mVerticalThumb != null) {
        mVerticalThumb.mutate();
      }
      if (mHorizontalTrack != null) {
        mHorizontalTrack.mutate();
      }
      if (mHorizontalThumb != null) {
        mHorizontalThumb.mutate();
      }
      mMutated = true;
    }
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    super.onBoundsChange(paramRect);
    mBoundsChanged = true;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    boolean bool1 = super.onStateChange(paramArrayOfInt);
    boolean bool2 = bool1;
    if (mVerticalTrack != null) {
      bool2 = bool1 | mVerticalTrack.setState(paramArrayOfInt);
    }
    bool1 = bool2;
    if (mVerticalThumb != null) {
      bool1 = bool2 | mVerticalThumb.setState(paramArrayOfInt);
    }
    bool2 = bool1;
    if (mHorizontalTrack != null) {
      bool2 = bool1 | mHorizontalTrack.setState(paramArrayOfInt);
    }
    bool1 = bool2;
    if (mHorizontalThumb != null) {
      bool1 = bool2 | mHorizontalThumb.setState(paramArrayOfInt);
    }
    return bool1;
  }
  
  public void scheduleDrawable(Drawable paramDrawable, Runnable paramRunnable, long paramLong)
  {
    scheduleSelf(paramRunnable, paramLong);
  }
  
  public void setAlpha(int paramInt)
  {
    mAlpha = paramInt;
    mHasSetAlpha = true;
    if (mVerticalTrack != null) {
      mVerticalTrack.setAlpha(paramInt);
    }
    if (mVerticalThumb != null) {
      mVerticalThumb.setAlpha(paramInt);
    }
    if (mHorizontalTrack != null) {
      mHorizontalTrack.setAlpha(paramInt);
    }
    if (mHorizontalThumb != null) {
      mHorizontalThumb.setAlpha(paramInt);
    }
  }
  
  public void setAlwaysDrawHorizontalTrack(boolean paramBoolean)
  {
    mAlwaysDrawHorizontalTrack = paramBoolean;
  }
  
  public void setAlwaysDrawVerticalTrack(boolean paramBoolean)
  {
    mAlwaysDrawVerticalTrack = paramBoolean;
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    mColorFilter = paramColorFilter;
    mHasSetColorFilter = true;
    if (mVerticalTrack != null) {
      mVerticalTrack.setColorFilter(paramColorFilter);
    }
    if (mVerticalThumb != null) {
      mVerticalThumb.setColorFilter(paramColorFilter);
    }
    if (mHorizontalTrack != null) {
      mHorizontalTrack.setColorFilter(paramColorFilter);
    }
    if (mHorizontalThumb != null) {
      mHorizontalThumb.setColorFilter(paramColorFilter);
    }
  }
  
  public void setHorizontalThumbDrawable(Drawable paramDrawable)
  {
    if (mHorizontalThumb != null) {
      mHorizontalThumb.setCallback(null);
    }
    propagateCurrentState(paramDrawable);
    mHorizontalThumb = paramDrawable;
  }
  
  public void setHorizontalTrackDrawable(Drawable paramDrawable)
  {
    if (mHorizontalTrack != null) {
      mHorizontalTrack.setCallback(null);
    }
    propagateCurrentState(paramDrawable);
    mHorizontalTrack = paramDrawable;
  }
  
  public void setParameters(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    if (mVertical != paramBoolean)
    {
      mVertical = paramBoolean;
      mBoundsChanged = true;
    }
    if ((mRange != paramInt1) || (mOffset != paramInt2) || (mExtent != paramInt3))
    {
      mRange = paramInt1;
      mOffset = paramInt2;
      mExtent = paramInt3;
      mRangeChanged = true;
    }
  }
  
  public void setVerticalThumbDrawable(Drawable paramDrawable)
  {
    if (mVerticalThumb != null) {
      mVerticalThumb.setCallback(null);
    }
    propagateCurrentState(paramDrawable);
    mVerticalThumb = paramDrawable;
  }
  
  public void setVerticalTrackDrawable(Drawable paramDrawable)
  {
    if (mVerticalTrack != null) {
      mVerticalTrack.setCallback(null);
    }
    propagateCurrentState(paramDrawable);
    mVerticalTrack = paramDrawable;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ScrollBarDrawable: range=");
    localStringBuilder.append(mRange);
    localStringBuilder.append(" offset=");
    localStringBuilder.append(mOffset);
    localStringBuilder.append(" extent=");
    localStringBuilder.append(mExtent);
    String str;
    if (mVertical) {
      str = " V";
    } else {
      str = " H";
    }
    localStringBuilder.append(str);
    return localStringBuilder.toString();
  }
  
  public void unscheduleDrawable(Drawable paramDrawable, Runnable paramRunnable)
  {
    unscheduleSelf(paramRunnable);
  }
}
