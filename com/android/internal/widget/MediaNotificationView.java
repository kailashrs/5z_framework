package com.android.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews.RemoteView;

@RemoteViews.RemoteView
public class MediaNotificationView
  extends FrameLayout
{
  private View mActions;
  private View mHeader;
  private int mImagePushIn;
  private View mMainColumn;
  private final int mNotificationContentImageMarginEnd;
  private final int mNotificationContentMarginEnd;
  private ImageView mRightIcon;
  
  public MediaNotificationView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public MediaNotificationView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public MediaNotificationView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public MediaNotificationView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    mNotificationContentMarginEnd = paramContext.getResources().getDimensionPixelSize(17105317);
    mNotificationContentImageMarginEnd = paramContext.getResources().getDimensionPixelSize(17105315);
  }
  
  private void resetHeaderIndention()
  {
    if (mHeader.getPaddingEnd() != mNotificationContentMarginEnd) {
      mHeader.setPaddingRelative(mHeader.getPaddingStart(), mHeader.getPaddingTop(), mNotificationContentMarginEnd, mHeader.getPaddingBottom());
    }
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)mHeader.getLayoutParams();
    localMarginLayoutParams.setMarginEnd(0);
    if (localMarginLayoutParams.getMarginEnd() != 0)
    {
      localMarginLayoutParams.setMarginEnd(0);
      mHeader.setLayoutParams(localMarginLayoutParams);
    }
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    mRightIcon = ((ImageView)findViewById(16909299));
    mActions = findViewById(16909112);
    mHeader = findViewById(16909177);
    mMainColumn = findViewById(16909178);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (mImagePushIn > 0) {
      mRightIcon.layout(mRightIcon.getLeft() + mImagePushIn, mRightIcon.getTop(), mRightIcon.getRight() + mImagePushIn, mRightIcon.getBottom());
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i;
    if (mRightIcon.getVisibility() != 8) {
      i = 1;
    } else {
      i = 0;
    }
    if (i == 0) {
      resetHeaderIndention();
    }
    super.onMeasure(paramInt1, paramInt2);
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = 0;
    int m = 0;
    mImagePushIn = 0;
    int n = k;
    if (i != 0)
    {
      n = k;
      if (j != 0)
      {
        n = View.MeasureSpec.getSize(paramInt1);
        i = mActions.getMeasuredWidth();
        ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)mRightIcon.getLayoutParams();
        j = localMarginLayoutParams.getMarginEnd();
        i = n - i - j;
        k = getMeasuredHeight();
        if (i > k)
        {
          n = k;
        }
        else
        {
          n = i;
          if (i < k)
          {
            n = Math.max(0, i);
            mImagePushIn = (k - n);
          }
        }
        if (width == k)
        {
          i = m;
          if (height == k) {}
        }
        else
        {
          width = k;
          height = k;
          mRightIcon.setLayoutParams(localMarginLayoutParams);
          i = 1;
        }
        localMarginLayoutParams = (ViewGroup.MarginLayoutParams)mMainColumn.getLayoutParams();
        m = n + j + mNotificationContentMarginEnd;
        if (m != localMarginLayoutParams.getMarginEnd())
        {
          localMarginLayoutParams.setMarginEnd(m);
          mMainColumn.setLayoutParams(localMarginLayoutParams);
          i = 1;
        }
        m = n + j;
        localMarginLayoutParams = (ViewGroup.MarginLayoutParams)mHeader.getLayoutParams();
        n = i;
        if (localMarginLayoutParams.getMarginEnd() != m)
        {
          localMarginLayoutParams.setMarginEnd(m);
          mHeader.setLayoutParams(localMarginLayoutParams);
          n = 1;
        }
        if (mHeader.getPaddingEnd() != mNotificationContentImageMarginEnd)
        {
          mHeader.setPaddingRelative(mHeader.getPaddingStart(), mHeader.getPaddingTop(), mNotificationContentImageMarginEnd, mHeader.getPaddingBottom());
          n = 1;
        }
      }
    }
    if (n != 0) {
      super.onMeasure(paramInt1, paramInt2);
    }
  }
}
