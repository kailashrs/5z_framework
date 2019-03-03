package com.android.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ActionMenuPresenter;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.R.styleable;
import com.android.internal.view.menu.MenuBuilder;

public class ActionBarContextView
  extends AbsActionBarView
{
  private static final String TAG = "ActionBarContextView";
  private View mClose;
  private int mCloseItemLayout;
  private View mCustomView;
  private Drawable mSplitBackground;
  private CharSequence mSubtitle;
  private int mSubtitleStyleRes;
  private TextView mSubtitleView;
  private CharSequence mTitle;
  private LinearLayout mTitleLayout;
  private boolean mTitleOptional;
  private int mTitleStyleRes;
  private TextView mTitleView;
  
  public ActionBarContextView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843668);
  }
  
  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ActionMode, paramInt1, paramInt2);
    setBackground(paramContext.getDrawable(0));
    mTitleStyleRes = paramContext.getResourceId(2, 0);
    mSubtitleStyleRes = paramContext.getResourceId(3, 0);
    mContentHeight = paramContext.getLayoutDimension(1, 0);
    mSplitBackground = paramContext.getDrawable(4);
    mCloseItemLayout = paramContext.getResourceId(5, 17367073);
    paramContext.recycle();
  }
  
  private void initTitle()
  {
    if (mTitleLayout == null)
    {
      LayoutInflater.from(getContext()).inflate(17367068, this);
      mTitleLayout = ((LinearLayout)getChildAt(getChildCount() - 1));
      mTitleView = ((TextView)mTitleLayout.findViewById(16908701));
      mSubtitleView = ((TextView)mTitleLayout.findViewById(16908700));
      if (mTitleStyleRes != 0) {
        mTitleView.setTextAppearance(mTitleStyleRes);
      }
      if (mSubtitleStyleRes != 0) {
        mSubtitleView.setTextAppearance(mSubtitleStyleRes);
      }
    }
    mTitleView.setText(mTitle);
    mSubtitleView.setText(mSubtitle);
    boolean bool1 = TextUtils.isEmpty(mTitle);
    boolean bool2 = TextUtils.isEmpty(mSubtitle) ^ true;
    Object localObject = mSubtitleView;
    int i = 8;
    int j;
    if (bool2) {
      j = 0;
    } else {
      j = 8;
    }
    ((TextView)localObject).setVisibility(j);
    localObject = mTitleLayout;
    if ((!(bool1 ^ true)) && (!bool2)) {
      j = i;
    } else {
      j = 0;
    }
    ((LinearLayout)localObject).setVisibility(j);
    if (mTitleLayout.getParent() == null) {
      addView(mTitleLayout);
    }
  }
  
  public void closeMode()
  {
    if (mClose == null)
    {
      killMode();
      return;
    }
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new ViewGroup.MarginLayoutParams(-1, -2);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new ViewGroup.MarginLayoutParams(getContext(), paramAttributeSet);
  }
  
  public CharSequence getSubtitle()
  {
    return mSubtitle;
  }
  
  public CharSequence getTitle()
  {
    return mTitle;
  }
  
  public boolean hideOverflowMenu()
  {
    if (mActionMenuPresenter != null) {
      return mActionMenuPresenter.hideOverflowMenu();
    }
    return false;
  }
  
  public void initForMode(final ActionMode paramActionMode)
  {
    if (mClose == null)
    {
      mClose = LayoutInflater.from(mContext).inflate(mCloseItemLayout, this, false);
      addView(mClose);
    }
    else if (mClose.getParent() == null)
    {
      addView(mClose);
    }
    mClose.findViewById(16908708).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramActionMode.finish();
      }
    });
    paramActionMode = (MenuBuilder)paramActionMode.getMenu();
    if (mActionMenuPresenter != null) {
      mActionMenuPresenter.dismissPopupMenus();
    }
    mActionMenuPresenter = new ActionMenuPresenter(mContext);
    mActionMenuPresenter.setReserveOverflow(true);
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-2, -1);
    if (!mSplitActionBar)
    {
      paramActionMode.addMenuPresenter(mActionMenuPresenter, mPopupContext);
      mMenuView = ((ActionMenuView)mActionMenuPresenter.getMenuView(this));
      mMenuView.setBackground(null);
      addView(mMenuView, localLayoutParams);
    }
    else
    {
      mActionMenuPresenter.setWidthLimit(getContextgetResourcesgetDisplayMetricswidthPixels, true);
      mActionMenuPresenter.setItemLimit(Integer.MAX_VALUE);
      width = -1;
      height = mContentHeight;
      paramActionMode.addMenuPresenter(mActionMenuPresenter, mPopupContext);
      mMenuView = ((ActionMenuView)mActionMenuPresenter.getMenuView(this));
      mMenuView.setBackgroundDrawable(mSplitBackground);
      mSplitView.addView(mMenuView, localLayoutParams);
    }
  }
  
  public boolean isOverflowMenuShowing()
  {
    if (mActionMenuPresenter != null) {
      return mActionMenuPresenter.isOverflowMenuShowing();
    }
    return false;
  }
  
  public boolean isTitleOptional()
  {
    return mTitleOptional;
  }
  
  public void killMode()
  {
    removeAllViews();
    if (mSplitView != null) {
      mSplitView.removeView(mMenuView);
    }
    mCustomView = null;
    mMenuView = null;
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (mActionMenuPresenter != null)
    {
      mActionMenuPresenter.hideOverflowMenu();
      mActionMenuPresenter.hideSubMenus();
    }
  }
  
  public void onInitializeAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    if (paramAccessibilityEvent.getEventType() == 32)
    {
      paramAccessibilityEvent.setSource(this);
      paramAccessibilityEvent.setClassName(getClass().getName());
      paramAccessibilityEvent.setPackageName(getContext().getPackageName());
      paramAccessibilityEvent.setContentDescription(mTitle);
    }
    else
    {
      super.onInitializeAccessibilityEventInternal(paramAccessibilityEvent);
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramBoolean = isLayoutRtl();
    int i;
    if (paramBoolean) {
      i = paramInt3 - paramInt1 - getPaddingRight();
    } else {
      i = getPaddingLeft();
    }
    int j = getPaddingTop();
    int k = paramInt4 - paramInt2 - getPaddingTop() - getPaddingBottom();
    paramInt2 = i;
    if (mClose != null)
    {
      paramInt2 = i;
      if (mClose.getVisibility() != 8)
      {
        ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)mClose.getLayoutParams();
        if (paramBoolean) {
          paramInt2 = rightMargin;
        } else {
          paramInt2 = leftMargin;
        }
        if (paramBoolean) {
          paramInt4 = leftMargin;
        } else {
          paramInt4 = rightMargin;
        }
        paramInt2 = next(i, paramInt2, paramBoolean);
        paramInt2 = next(positionChild(mClose, paramInt2, j, k, paramBoolean) + paramInt2, paramInt4, paramBoolean);
      }
    }
    paramInt4 = paramInt2;
    if (mTitleLayout != null)
    {
      paramInt4 = paramInt2;
      if (mCustomView == null)
      {
        paramInt4 = paramInt2;
        if (mTitleLayout.getVisibility() != 8) {
          paramInt4 = paramInt2 + positionChild(mTitleLayout, paramInt2, j, k, paramBoolean);
        }
      }
    }
    if (mCustomView != null) {
      positionChild(mCustomView, paramInt4, j, k, paramBoolean);
    }
    if (paramBoolean) {
      paramInt1 = getPaddingLeft();
    } else {
      paramInt1 = paramInt3 - paramInt1 - getPaddingRight();
    }
    if (mMenuView != null) {
      positionChild(mMenuView, paramInt1, j, k, paramBoolean ^ true);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824)
    {
      if (View.MeasureSpec.getMode(paramInt2) != 0)
      {
        int i = View.MeasureSpec.getSize(paramInt1);
        int j;
        if (mContentHeight > 0) {
          j = mContentHeight;
        } else {
          j = View.MeasureSpec.getSize(paramInt2);
        }
        int k = getPaddingTop() + getPaddingBottom();
        paramInt1 = i - getPaddingLeft() - getPaddingRight();
        int m = j - k;
        int n = View.MeasureSpec.makeMeasureSpec(m, Integer.MIN_VALUE);
        paramInt2 = paramInt1;
        if (mClose != null)
        {
          paramInt1 = measureChildView(mClose, paramInt1, n, 0);
          localObject = (ViewGroup.MarginLayoutParams)mClose.getLayoutParams();
          paramInt2 = paramInt1 - (leftMargin + rightMargin);
        }
        paramInt1 = paramInt2;
        if (mMenuView != null)
        {
          paramInt1 = paramInt2;
          if (mMenuView.getParent() == this) {
            paramInt1 = measureChildView(mMenuView, paramInt2, n, 0);
          }
        }
        paramInt2 = paramInt1;
        if (mTitleLayout != null)
        {
          paramInt2 = paramInt1;
          if (mCustomView == null) {
            if (mTitleOptional)
            {
              paramInt2 = View.MeasureSpec.makeSafeMeasureSpec(i, 0);
              mTitleLayout.measure(paramInt2, n);
              int i1 = mTitleLayout.getMeasuredWidth();
              if (i1 <= paramInt1) {
                n = 1;
              } else {
                n = 0;
              }
              paramInt2 = paramInt1;
              if (n != 0) {
                paramInt2 = paramInt1 - i1;
              }
              localObject = mTitleLayout;
              if (n != 0) {
                paramInt1 = 0;
              } else {
                paramInt1 = 8;
              }
              ((LinearLayout)localObject).setVisibility(paramInt1);
            }
            else
            {
              paramInt2 = measureChildView(mTitleLayout, paramInt1, n, 0);
            }
          }
        }
        if (mCustomView != null)
        {
          localObject = mCustomView.getLayoutParams();
          if (width != -2) {
            paramInt1 = 1073741824;
          } else {
            paramInt1 = Integer.MIN_VALUE;
          }
          if (width >= 0) {
            paramInt2 = Math.min(width, paramInt2);
          }
          if (height != -2) {
            n = 1073741824;
          } else {
            n = Integer.MIN_VALUE;
          }
          if (height >= 0) {
            m = Math.min(height, m);
          }
          mCustomView.measure(View.MeasureSpec.makeMeasureSpec(paramInt2, paramInt1), View.MeasureSpec.makeMeasureSpec(m, n));
        }
        if (mContentHeight <= 0)
        {
          j = 0;
          m = getChildCount();
          paramInt1 = 0;
          while (paramInt1 < m)
          {
            n = getChildAt(paramInt1).getMeasuredHeight() + k;
            paramInt2 = j;
            if (n > j) {
              paramInt2 = n;
            }
            paramInt1++;
            j = paramInt2;
          }
          setMeasuredDimension(i, j);
        }
        else
        {
          setMeasuredDimension(i, j);
        }
        return;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(getClass().getSimpleName());
      ((StringBuilder)localObject).append(" can only be used with android:layout_height=\"wrap_content\"");
      throw new IllegalStateException(((StringBuilder)localObject).toString());
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(getClass().getSimpleName());
    ((StringBuilder)localObject).append(" can only be used with android:layout_width=\"match_parent\" (or fill_parent)");
    throw new IllegalStateException(((StringBuilder)localObject).toString());
  }
  
  public void setContentHeight(int paramInt)
  {
    mContentHeight = paramInt;
  }
  
  public void setCustomView(View paramView)
  {
    if (mCustomView != null) {
      removeView(mCustomView);
    }
    mCustomView = paramView;
    if ((paramView != null) && (mTitleLayout != null))
    {
      removeView(mTitleLayout);
      mTitleLayout = null;
    }
    if (paramView != null) {
      addView(paramView);
    }
    requestLayout();
  }
  
  public void setSplitToolbar(boolean paramBoolean)
  {
    if (mSplitActionBar != paramBoolean)
    {
      if (mActionMenuPresenter != null)
      {
        ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-2, -1);
        ViewGroup localViewGroup;
        if (!paramBoolean)
        {
          mMenuView = ((ActionMenuView)mActionMenuPresenter.getMenuView(this));
          mMenuView.setBackground(null);
          localViewGroup = (ViewGroup)mMenuView.getParent();
          if (localViewGroup != null) {
            localViewGroup.removeView(mMenuView);
          }
          addView(mMenuView, localLayoutParams);
        }
        else
        {
          mActionMenuPresenter.setWidthLimit(getContextgetResourcesgetDisplayMetricswidthPixels, true);
          mActionMenuPresenter.setItemLimit(Integer.MAX_VALUE);
          width = -1;
          height = mContentHeight;
          mMenuView = ((ActionMenuView)mActionMenuPresenter.getMenuView(this));
          mMenuView.setBackground(mSplitBackground);
          localViewGroup = (ViewGroup)mMenuView.getParent();
          if (localViewGroup != null) {
            localViewGroup.removeView(mMenuView);
          }
          mSplitView.addView(mMenuView, localLayoutParams);
        }
      }
      super.setSplitToolbar(paramBoolean);
    }
  }
  
  public void setSubtitle(CharSequence paramCharSequence)
  {
    mSubtitle = paramCharSequence;
    initTitle();
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    mTitle = paramCharSequence;
    initTitle();
  }
  
  public void setTitleOptional(boolean paramBoolean)
  {
    if (paramBoolean != mTitleOptional) {
      requestLayout();
    }
    mTitleOptional = paramBoolean;
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return false;
  }
  
  public boolean showOverflowMenu()
  {
    if (mActionMenuPresenter != null) {
      return mActionMenuPresenter.showOverflowMenu();
    }
    return false;
  }
}
