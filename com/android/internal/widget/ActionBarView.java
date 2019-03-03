package com.android.internal.widget;

import android.animation.LayoutTransition;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.CollapsibleActionView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window.Callback;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ActionMenuPresenter;
import android.widget.ActionMenuView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.android.internal.R.styleable;
import com.android.internal.view.menu.ActionMenuItem;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuBuilder.Callback;
import com.android.internal.view.menu.MenuItemImpl;
import com.android.internal.view.menu.MenuPresenter;
import com.android.internal.view.menu.MenuPresenter.Callback;
import com.android.internal.view.menu.MenuView;
import com.android.internal.view.menu.SubMenuBuilder;
import java.util.List;

public class ActionBarView
  extends AbsActionBarView
  implements DecorToolbar
{
  private static final int DEFAULT_CUSTOM_GRAVITY = 8388627;
  public static final int DISPLAY_DEFAULT = 0;
  private static final int DISPLAY_RELAYOUT_MASK = 63;
  private static final String TAG = "ActionBarView";
  private ActionBarContextView mContextView;
  private View mCustomNavView;
  private int mDefaultUpDescription = 17039432;
  private int mDisplayOptions = -1;
  View mExpandedActionView;
  private final View.OnClickListener mExpandedActionViewUpListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      paramAnonymousView = mExpandedMenuPresenter.mCurrentExpandedItem;
      if (paramAnonymousView != null) {
        paramAnonymousView.collapseActionView();
      }
    }
  };
  private HomeView mExpandedHomeLayout;
  private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
  private CharSequence mHomeDescription;
  private int mHomeDescriptionRes;
  private HomeView mHomeLayout;
  private Drawable mIcon;
  private boolean mIncludeTabs;
  private final int mIndeterminateProgressStyle;
  private ProgressBar mIndeterminateProgressView;
  private boolean mIsCollapsible;
  private int mItemPadding;
  private LinearLayout mListNavLayout;
  private Drawable mLogo;
  private ActionMenuItem mLogoNavItem;
  private boolean mMenuPrepared;
  private AdapterView.OnItemSelectedListener mNavItemSelectedListener;
  private int mNavigationMode;
  private MenuBuilder mOptionsMenu;
  private int mProgressBarPadding;
  private final int mProgressStyle;
  private ProgressBar mProgressView;
  private Spinner mSpinner;
  private SpinnerAdapter mSpinnerAdapter;
  private CharSequence mSubtitle;
  private final int mSubtitleStyleRes;
  private TextView mSubtitleView;
  private ScrollingTabContainerView mTabScrollView;
  private Runnable mTabSelector;
  private CharSequence mTitle;
  private LinearLayout mTitleLayout;
  private final int mTitleStyleRes;
  private TextView mTitleView;
  private final View.OnClickListener mUpClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (mMenuPrepared) {
        mWindowCallback.onMenuItemSelected(0, mLogoNavItem);
      }
    }
  };
  private ViewGroup mUpGoerFive;
  private boolean mUserTitle;
  private boolean mWasHomeEnabled;
  Window.Callback mWindowCallback;
  
  public ActionBarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setBackgroundResource(0);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ActionBar, 16843470, 0);
    mNavigationMode = paramAttributeSet.getInt(7, 0);
    mTitle = paramAttributeSet.getText(5);
    mSubtitle = paramAttributeSet.getText(9);
    mLogo = paramAttributeSet.getDrawable(6);
    mIcon = paramAttributeSet.getDrawable(0);
    LayoutInflater localLayoutInflater = LayoutInflater.from(paramContext);
    int i = paramAttributeSet.getResourceId(16, 17367066);
    mUpGoerFive = ((ViewGroup)localLayoutInflater.inflate(17367069, this, false));
    mHomeLayout = ((HomeView)localLayoutInflater.inflate(i, mUpGoerFive, false));
    mExpandedHomeLayout = ((HomeView)localLayoutInflater.inflate(i, mUpGoerFive, false));
    mExpandedHomeLayout.setShowUp(true);
    mExpandedHomeLayout.setOnClickListener(mExpandedActionViewUpListener);
    mExpandedHomeLayout.setContentDescription(getResources().getText(mDefaultUpDescription));
    Drawable localDrawable = mUpGoerFive.getBackground();
    if (localDrawable != null) {
      mExpandedHomeLayout.setBackground(localDrawable.getConstantState().newDrawable());
    }
    mExpandedHomeLayout.setEnabled(true);
    mExpandedHomeLayout.setFocusable(true);
    mTitleStyleRes = paramAttributeSet.getResourceId(11, 0);
    mSubtitleStyleRes = paramAttributeSet.getResourceId(12, 0);
    mProgressStyle = paramAttributeSet.getResourceId(1, 0);
    mIndeterminateProgressStyle = paramAttributeSet.getResourceId(14, 0);
    mProgressBarPadding = paramAttributeSet.getDimensionPixelOffset(15, 0);
    mItemPadding = paramAttributeSet.getDimensionPixelOffset(17, 0);
    setDisplayOptions(paramAttributeSet.getInt(8, 0));
    i = paramAttributeSet.getResourceId(10, 0);
    if (i != 0)
    {
      mCustomNavView = localLayoutInflater.inflate(i, this, false);
      mNavigationMode = 0;
      setDisplayOptions(0x10 | mDisplayOptions);
    }
    mContentHeight = paramAttributeSet.getLayoutDimension(4, 0);
    paramAttributeSet.recycle();
    mLogoNavItem = new ActionMenuItem(paramContext, 0, 16908332, 0, 0, mTitle);
    mUpGoerFive.setOnClickListener(mUpClickListener);
    mUpGoerFive.setClickable(true);
    mUpGoerFive.setFocusable(true);
    if (getImportantForAccessibility() == 0) {
      setImportantForAccessibility(1);
    }
  }
  
  private CharSequence buildHomeContentDescription()
  {
    Object localObject;
    if (mHomeDescription != null) {
      localObject = mHomeDescription;
    }
    for (;;)
    {
      break;
      if ((mDisplayOptions & 0x4) != 0) {
        localObject = mContext.getResources().getText(mDefaultUpDescription);
      } else {
        localObject = mContext.getResources().getText(17039429);
      }
    }
    CharSequence localCharSequence1 = getTitle();
    CharSequence localCharSequence2 = getSubtitle();
    if (!TextUtils.isEmpty(localCharSequence1))
    {
      if (!TextUtils.isEmpty(localCharSequence2)) {
        localObject = getResources().getString(17039431, new Object[] { localCharSequence1, localCharSequence2, localObject });
      } else {
        localObject = getResources().getString(17039430, new Object[] { localCharSequence1, localObject });
      }
      return localObject;
    }
    return localObject;
  }
  
  private void configPresenters(MenuBuilder paramMenuBuilder)
  {
    if (paramMenuBuilder != null)
    {
      paramMenuBuilder.addMenuPresenter(mActionMenuPresenter, mPopupContext);
      paramMenuBuilder.addMenuPresenter(mExpandedMenuPresenter, mPopupContext);
    }
    else
    {
      mActionMenuPresenter.initForMenu(mPopupContext, null);
      mExpandedMenuPresenter.initForMenu(mPopupContext, null);
      mActionMenuPresenter.updateMenuView(true);
      mExpandedMenuPresenter.updateMenuView(true);
    }
  }
  
  private void initTitle()
  {
    if (mTitleLayout == null)
    {
      mTitleLayout = ((LinearLayout)LayoutInflater.from(getContext()).inflate(17367068, this, false));
      mTitleView = ((TextView)mTitleLayout.findViewById(16908701));
      mSubtitleView = ((TextView)mTitleLayout.findViewById(16908700));
      if (mTitleStyleRes != 0) {
        mTitleView.setTextAppearance(mTitleStyleRes);
      }
      if (mTitle != null) {
        mTitleView.setText(mTitle);
      }
      if (mSubtitleStyleRes != 0) {
        mSubtitleView.setTextAppearance(mSubtitleStyleRes);
      }
      if (mSubtitle != null)
      {
        mSubtitleView.setText(mSubtitle);
        mSubtitleView.setVisibility(0);
      }
    }
    mUpGoerFive.addView(mTitleLayout);
    if ((mExpandedActionView == null) && ((!TextUtils.isEmpty(mTitle)) || (!TextUtils.isEmpty(mSubtitle)))) {
      mTitleLayout.setVisibility(0);
    } else {
      mTitleLayout.setVisibility(8);
    }
  }
  
  private void setHomeButtonEnabled(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean2) {
      mWasHomeEnabled = paramBoolean1;
    }
    if (mExpandedActionView != null) {
      return;
    }
    mUpGoerFive.setEnabled(paramBoolean1);
    mUpGoerFive.setFocusable(paramBoolean1);
    updateHomeAccessibility(paramBoolean1);
  }
  
  private void setTitleImpl(CharSequence paramCharSequence)
  {
    mTitle = paramCharSequence;
    if (mTitleView != null)
    {
      mTitleView.setText(paramCharSequence);
      Object localObject = mExpandedActionView;
      int i = 0;
      int j;
      if ((localObject == null) && ((mDisplayOptions & 0x8) != 0) && ((!TextUtils.isEmpty(mTitle)) || (!TextUtils.isEmpty(mSubtitle)))) {
        j = 1;
      } else {
        j = 0;
      }
      localObject = mTitleLayout;
      if (j != 0) {
        j = i;
      } else {
        j = 8;
      }
      ((LinearLayout)localObject).setVisibility(j);
    }
    if (mLogoNavItem != null) {
      mLogoNavItem.setTitle(paramCharSequence);
    }
    updateHomeAccessibility(mUpGoerFive.isEnabled());
  }
  
  private void updateHomeAccessibility(boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      mUpGoerFive.setContentDescription(null);
      mUpGoerFive.setImportantForAccessibility(2);
    }
    else
    {
      mUpGoerFive.setImportantForAccessibility(0);
      mUpGoerFive.setContentDescription(buildHomeContentDescription());
    }
  }
  
  public boolean canSplit()
  {
    return true;
  }
  
  public void collapseActionView()
  {
    MenuItemImpl localMenuItemImpl;
    if (mExpandedMenuPresenter == null) {
      localMenuItemImpl = null;
    } else {
      localMenuItemImpl = mExpandedMenuPresenter.mCurrentExpandedItem;
    }
    if (localMenuItemImpl != null) {
      localMenuItemImpl.collapseActionView();
    }
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new ActionBar.LayoutParams(8388627);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new ActionBar.LayoutParams(getContext(), paramAttributeSet);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    ViewGroup.LayoutParams localLayoutParams = paramLayoutParams;
    if (paramLayoutParams == null) {
      localLayoutParams = generateDefaultLayoutParams();
    }
    return localLayoutParams;
  }
  
  public View getCustomView()
  {
    return mCustomNavView;
  }
  
  public int getDisplayOptions()
  {
    return mDisplayOptions;
  }
  
  public int getDropdownItemCount()
  {
    int i;
    if (mSpinnerAdapter != null) {
      i = mSpinnerAdapter.getCount();
    } else {
      i = 0;
    }
    return i;
  }
  
  public int getDropdownSelectedPosition()
  {
    return mSpinner.getSelectedItemPosition();
  }
  
  public Menu getMenu()
  {
    return mOptionsMenu;
  }
  
  public int getNavigationMode()
  {
    return mNavigationMode;
  }
  
  public CharSequence getSubtitle()
  {
    return mSubtitle;
  }
  
  public CharSequence getTitle()
  {
    return mTitle;
  }
  
  public ViewGroup getViewGroup()
  {
    return this;
  }
  
  public boolean hasEmbeddedTabs()
  {
    return mIncludeTabs;
  }
  
  public boolean hasExpandedActionView()
  {
    boolean bool;
    if ((mExpandedMenuPresenter != null) && (mExpandedMenuPresenter.mCurrentExpandedItem != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasIcon()
  {
    boolean bool;
    if (mIcon != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasLogo()
  {
    boolean bool;
    if (mLogo != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void initIndeterminateProgress()
  {
    mIndeterminateProgressView = new ProgressBar(mContext, null, 0, mIndeterminateProgressStyle);
    mIndeterminateProgressView.setId(16909265);
    mIndeterminateProgressView.setVisibility(8);
    addView(mIndeterminateProgressView);
  }
  
  public void initProgress()
  {
    mProgressView = new ProgressBar(mContext, null, 0, mProgressStyle);
    mProgressView.setId(16909266);
    mProgressView.setMax(10000);
    mProgressView.setVisibility(8);
    addView(mProgressView);
  }
  
  public boolean isSplit()
  {
    return mSplitActionBar;
  }
  
  public boolean isTitleTruncated()
  {
    if (mTitleView == null) {
      return false;
    }
    Layout localLayout = mTitleView.getLayout();
    if (localLayout == null) {
      return false;
    }
    int i = localLayout.getLineCount();
    for (int j = 0; j < i; j++) {
      if (localLayout.getEllipsisCount(j) > 0) {
        return true;
      }
    }
    return false;
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    mTitleView = null;
    mSubtitleView = null;
    if ((mTitleLayout != null) && (mTitleLayout.getParent() == mUpGoerFive)) {
      mUpGoerFive.removeView(mTitleLayout);
    }
    mTitleLayout = null;
    if ((mDisplayOptions & 0x8) != 0) {
      initTitle();
    }
    if (mHomeDescriptionRes != 0) {
      setNavigationContentDescription(mHomeDescriptionRes);
    }
    if ((mTabScrollView != null) && (mIncludeTabs))
    {
      paramConfiguration = mTabScrollView.getLayoutParams();
      if (paramConfiguration != null)
      {
        width = -2;
        height = -1;
      }
      mTabScrollView.setAllowCollapse(true);
    }
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    removeCallbacks(mTabSelector);
    if (mActionMenuPresenter != null)
    {
      mActionMenuPresenter.hideOverflowMenu();
      mActionMenuPresenter.hideSubMenus();
    }
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    mUpGoerFive.addView(mHomeLayout, 0);
    addView(mUpGoerFive);
    if ((mCustomNavView != null) && ((mDisplayOptions & 0x10) != 0))
    {
      ViewParent localViewParent = mCustomNavView.getParent();
      if (localViewParent != this)
      {
        if ((localViewParent instanceof ViewGroup)) {
          ((ViewGroup)localViewParent).removeView(mCustomNavView);
        }
        addView(mCustomNavView);
      }
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt4 - paramInt2 - getPaddingTop() - getPaddingBottom();
    if (i <= 0) {
      return;
    }
    paramBoolean = isLayoutRtl();
    int j;
    if (paramBoolean) {
      j = 1;
    } else {
      j = -1;
    }
    if (paramBoolean) {
      paramInt2 = getPaddingLeft();
    } else {
      paramInt2 = paramInt3 - paramInt1 - getPaddingRight();
    }
    paramInt4 = paramInt2;
    if (paramBoolean) {
      paramInt2 = paramInt3 - paramInt1 - getPaddingRight();
    } else {
      paramInt2 = getPaddingLeft();
    }
    int k = getPaddingTop();
    Object localObject1;
    if (mExpandedActionView != null) {
      localObject1 = mExpandedHomeLayout;
    } else {
      localObject1 = mHomeLayout;
    }
    if ((mTitleLayout != null) && (mTitleLayout.getVisibility() != 8) && ((mDisplayOptions & 0x8) != 0)) {
      paramInt3 = 1;
    } else {
      paramInt3 = 0;
    }
    int m = 0;
    paramInt1 = m;
    if (((HomeView)localObject1).getParent() == mUpGoerFive) {
      if (((HomeView)localObject1).getVisibility() != 8)
      {
        paramInt1 = ((HomeView)localObject1).getStartOffset();
      }
      else
      {
        paramInt1 = m;
        if (paramInt3 != 0) {
          paramInt1 = ((HomeView)localObject1).getUpWidth();
        }
      }
    }
    paramInt2 = next(paramInt2 + positionChild(mUpGoerFive, next(paramInt2, paramInt1, paramBoolean), k, i, paramBoolean), paramInt1, paramBoolean);
    paramInt1 = paramInt2;
    if (mExpandedActionView == null) {
      switch (mNavigationMode)
      {
      default: 
        paramInt1 = paramInt2;
        break;
      case 2: 
        paramInt1 = paramInt2;
        if (mTabScrollView != null)
        {
          paramInt1 = paramInt2;
          if (paramInt3 != 0) {
            paramInt1 = next(paramInt2, mItemPadding, paramBoolean);
          }
          paramInt1 = next(paramInt1 + positionChild(mTabScrollView, paramInt1, k, i, paramBoolean), mItemPadding, paramBoolean);
        }
        break;
      case 1: 
        paramInt1 = paramInt2;
        if (mListNavLayout != null)
        {
          paramInt1 = paramInt2;
          if (paramInt3 != 0) {
            paramInt1 = next(paramInt2, mItemPadding, paramBoolean);
          }
          paramInt1 = next(paramInt1 + positionChild(mListNavLayout, paramInt1, k, i, paramBoolean), mItemPadding, paramBoolean);
        }
        break;
      case 0: 
        paramInt1 = paramInt2;
      }
    }
    if ((mMenuView != null) && (mMenuView.getParent() == this))
    {
      positionChild(mMenuView, paramInt4, k, i, paramBoolean ^ true);
      paramInt3 = paramInt4 + mMenuView.getMeasuredWidth() * j;
    }
    else
    {
      paramInt3 = paramInt4;
    }
    paramInt2 = paramInt3;
    if (mIndeterminateProgressView != null)
    {
      paramInt2 = paramInt3;
      if (mIndeterminateProgressView.getVisibility() != 8)
      {
        positionChild(mIndeterminateProgressView, paramInt3, k, i, paramBoolean ^ true);
        paramInt2 = paramInt3 + mIndeterminateProgressView.getMeasuredWidth() * j;
      }
    }
    Object localObject2 = null;
    if (mExpandedActionView != null)
    {
      localObject1 = mExpandedActionView;
    }
    else
    {
      localObject1 = localObject2;
      if ((mDisplayOptions & 0x10) != 0)
      {
        localObject1 = localObject2;
        if (mCustomNavView != null) {
          localObject1 = mCustomNavView;
        }
      }
    }
    if (localObject1 != null)
    {
      k = getLayoutDirection();
      localObject2 = ((View)localObject1).getLayoutParams();
      if ((localObject2 instanceof ActionBar.LayoutParams)) {
        localObject2 = (ActionBar.LayoutParams)localObject2;
      } else {
        localObject2 = null;
      }
      if (localObject2 != null) {
        paramInt4 = gravity;
      } else {
        paramInt4 = 8388627;
      }
      int n = ((View)localObject1).getMeasuredWidth();
      m = 0;
      if (localObject2 != null)
      {
        paramInt3 = next(paramInt1, ((ActionBar.LayoutParams)localObject2).getMarginStart(), paramBoolean);
        paramInt2 += ((ActionBar.LayoutParams)localObject2).getMarginEnd() * j;
        m = topMargin;
        j = bottomMargin;
      }
      else
      {
        j = 0;
        paramInt3 = paramInt1;
      }
      paramInt1 = paramInt4 & 0x800007;
      if (paramInt1 == 1)
      {
        i = mRight;
        i = (i - mLeft - n) / 2;
        if (paramBoolean)
        {
          if (i + n > paramInt3) {
            paramInt1 = 5;
          } else if (i < paramInt2) {
            paramInt1 = 3;
          }
        }
        else if (i < paramInt3) {
          paramInt1 = 3;
        } else if (i + n > paramInt2) {
          paramInt1 = 5;
        }
      }
      else if (paramInt4 == 0)
      {
        paramInt1 = 8388611;
      }
      i = 0;
      paramInt1 = Gravity.getAbsoluteGravity(paramInt1, k);
      if (paramInt1 != 1)
      {
        if (paramInt1 != 3)
        {
          if (paramInt1 != 5) {
            paramInt1 = i;
          } else if (paramBoolean) {
            paramInt1 = paramInt3 - n;
          } else {
            paramInt1 = paramInt2 - n;
          }
        }
        else if (paramBoolean) {
          paramInt1 = paramInt2;
        } else {
          paramInt1 = paramInt3;
        }
      }
      else {
        paramInt1 = (mRight - mLeft - n) / 2;
      }
      paramInt2 = paramInt4 & 0x70;
      if (paramInt4 == 0) {
        paramInt2 = 16;
      }
      paramInt4 = 0;
      if (paramInt2 != 16)
      {
        if (paramInt2 != 48) {
          if (paramInt2 != 80) {
            paramInt2 = paramInt4;
          }
        }
        for (;;)
        {
          break;
          paramInt2 = getHeight() - getPaddingBottom() - ((View)localObject1).getMeasuredHeight() - j;
          continue;
          paramInt2 = getPaddingTop() + m;
        }
      }
      paramInt2 = getPaddingTop();
      paramInt2 = (mBottom - mTop - getPaddingBottom() - paramInt2 - ((View)localObject1).getMeasuredHeight()) / 2;
      paramInt4 = ((View)localObject1).getMeasuredWidth();
      ((View)localObject1).layout(paramInt1, paramInt2, paramInt1 + paramInt4, ((View)localObject1).getMeasuredHeight() + paramInt2);
      next(paramInt3, paramInt4, paramBoolean);
    }
    if (mProgressView != null)
    {
      mProgressView.bringToFront();
      paramInt1 = mProgressView.getMeasuredHeight() / 2;
      mProgressView.layout(mProgressBarPadding, -paramInt1, mProgressBarPadding + mProgressView.getMeasuredWidth(), paramInt1);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    int j;
    int k;
    int m;
    int n;
    if (mIsCollapsible)
    {
      j = 0;
      k = 0;
      while (k < i)
      {
        localObject1 = getChildAt(k);
        m = j;
        if (((View)localObject1).getVisibility() != 8) {
          if (localObject1 == mMenuView)
          {
            m = j;
            if (mMenuView.getChildCount() == 0) {}
          }
          else
          {
            m = j;
            if (localObject1 != mUpGoerFive) {
              m = j + 1;
            }
          }
        }
        k++;
        j = m;
      }
      n = mUpGoerFive.getChildCount();
      k = 0;
      for (m = j; k < n; m = j)
      {
        j = m;
        if (mUpGoerFive.getChildAt(k).getVisibility() != 8) {
          j = m + 1;
        }
        k++;
      }
      if (m == 0)
      {
        setMeasuredDimension(0, 0);
        return;
      }
    }
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824)
    {
      if (View.MeasureSpec.getMode(paramInt2) == Integer.MIN_VALUE)
      {
        int i1 = View.MeasureSpec.getSize(paramInt1);
        if (mContentHeight >= 0) {
          n = mContentHeight;
        } else {
          n = View.MeasureSpec.getSize(paramInt2);
        }
        int i2 = getPaddingTop() + getPaddingBottom();
        paramInt2 = getPaddingLeft();
        paramInt1 = getPaddingRight();
        int i3 = n - i2;
        int i4 = View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE);
        int i5 = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
        paramInt2 = i1 - paramInt2 - paramInt1;
        j = paramInt2 / 2;
        m = j;
        int i6;
        if ((mTitleLayout != null) && (mTitleLayout.getVisibility() != 8) && ((mDisplayOptions & 0x8) != 0)) {
          i6 = 1;
        } else {
          i6 = 0;
        }
        if (mExpandedActionView != null) {
          localObject1 = mExpandedHomeLayout;
        } else {
          localObject1 = mHomeLayout;
        }
        Object localObject2 = ((HomeView)localObject1).getLayoutParams();
        if (width < 0) {
          paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt2, Integer.MIN_VALUE);
        } else {
          paramInt1 = View.MeasureSpec.makeMeasureSpec(width, 1073741824);
        }
        ((HomeView)localObject1).measure(paramInt1, i5);
        paramInt1 = ((HomeView)localObject1).getVisibility();
        int i7 = 0;
        if (((paramInt1 != 8) && (((HomeView)localObject1).getParent() == mUpGoerFive)) || (i6 != 0))
        {
          i7 = ((HomeView)localObject1).getMeasuredWidth();
          paramInt1 = ((HomeView)localObject1).getStartOffset() + i7;
          paramInt2 = Math.max(0, paramInt2 - paramInt1);
          j = Math.max(0, paramInt2 - paramInt1);
        }
        k = paramInt2;
        paramInt1 = m;
        if (mMenuView != null)
        {
          k = paramInt2;
          paramInt1 = m;
          if (mMenuView.getParent() == this)
          {
            k = measureChildView(mMenuView, paramInt2, i5, 0);
            paramInt1 = Math.max(0, m - mMenuView.getMeasuredWidth());
          }
        }
        m = k;
        i5 = paramInt1;
        if (mIndeterminateProgressView != null)
        {
          m = k;
          i5 = paramInt1;
          if (mIndeterminateProgressView.getVisibility() != 8)
          {
            m = measureChildView(mIndeterminateProgressView, k, i4, 0);
            i5 = Math.max(0, paramInt1 - mIndeterminateProgressView.getMeasuredWidth());
          }
        }
        if (mExpandedActionView == null) {
          switch (mNavigationMode)
          {
          default: 
            break;
          case 2: 
            if (mTabScrollView != null)
            {
              if (i6 != 0) {
                paramInt1 = mItemPadding * 2;
              } else {
                paramInt1 = mItemPadding;
              }
              paramInt2 = Math.max(0, m - paramInt1);
              m = Math.max(0, j - paramInt1);
              mTabScrollView.measure(View.MeasureSpec.makeMeasureSpec(paramInt2, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
              j = mTabScrollView.getMeasuredWidth();
              paramInt1 = Math.max(0, paramInt2 - j);
              paramInt2 = Math.max(0, m - j);
            }
            break;
          case 1: 
            paramInt1 = m;
            paramInt2 = j;
            if (mListNavLayout == null) {
              break label825;
            }
            if (i6 != 0) {
              paramInt1 = mItemPadding * 2;
            } else {
              paramInt1 = mItemPadding;
            }
            paramInt2 = Math.max(0, m - paramInt1);
            j = Math.max(0, j - paramInt1);
            mListNavLayout.measure(View.MeasureSpec.makeMeasureSpec(paramInt2, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
            m = mListNavLayout.getMeasuredWidth();
            paramInt1 = Math.max(0, paramInt2 - m);
            paramInt2 = Math.max(0, j - m);
            break;
          }
        }
        paramInt2 = j;
        paramInt1 = m;
        label825:
        localObject2 = null;
        if (mExpandedActionView != null)
        {
          localObject1 = mExpandedActionView;
        }
        else
        {
          localObject1 = localObject2;
          if ((mDisplayOptions & 0x10) != 0)
          {
            localObject1 = localObject2;
            if (mCustomNavView != null) {
              localObject1 = mCustomNavView;
            }
          }
        }
        if (localObject1 != null)
        {
          ViewGroup.LayoutParams localLayoutParams = generateLayoutParams(((View)localObject1).getLayoutParams());
          if ((localLayoutParams instanceof ActionBar.LayoutParams)) {
            localObject2 = (ActionBar.LayoutParams)localLayoutParams;
          } else {
            localObject2 = null;
          }
          k = 0;
          if (localObject2 != null)
          {
            m = leftMargin;
            j = rightMargin;
            k = topMargin + bottomMargin;
            m = j + m;
          }
          else
          {
            m = 0;
          }
          if (mContentHeight <= 0) {
            j = Integer.MIN_VALUE;
          } else if (height != -2) {
            j = 1073741824;
          } else {
            j = Integer.MIN_VALUE;
          }
          if (height >= 0) {
            i6 = Math.min(height, i3);
          } else {
            i6 = i3;
          }
          i4 = Math.max(0, i6 - k);
          if (width != -2) {
            k = 1073741824;
          } else {
            k = Integer.MIN_VALUE;
          }
          if (width >= 0) {
            i6 = Math.min(width, paramInt1);
          } else {
            i6 = paramInt1;
          }
          i3 = Math.max(0, i6 - m);
          if (localObject2 != null) {
            i6 = gravity;
          } else {
            i6 = 8388627;
          }
          if ((i6 & 0x7) == 1)
          {
            i6 = i3;
            if (width == -1) {
              i6 = Math.min(paramInt2, i5) * 2;
            }
          }
          else
          {
            i6 = i3;
          }
          ((View)localObject1).measure(View.MeasureSpec.makeMeasureSpec(i6, k), View.MeasureSpec.makeMeasureSpec(i4, j));
          paramInt1 -= m + ((View)localObject1).getMeasuredWidth();
        }
        localObject1 = mUpGoerFive;
        m = View.MeasureSpec.makeMeasureSpec(mContentHeight, 1073741824);
        j = 0;
        measureChildView((View)localObject1, paramInt1 + i7, m, 0);
        if (mTitleLayout != null) {
          Math.max(0, paramInt2 - mTitleLayout.getMeasuredWidth());
        }
        if (mContentHeight <= 0)
        {
          paramInt2 = 0;
          paramInt1 = j;
          while (paramInt1 < i)
          {
            m = getChildAt(paramInt1).getMeasuredHeight() + i2;
            j = paramInt2;
            if (m > paramInt2) {
              j = m;
            }
            paramInt1++;
            paramInt2 = j;
          }
          setMeasuredDimension(i1, paramInt2);
        }
        else
        {
          setMeasuredDimension(i1, n);
        }
        if (mContextView != null) {
          mContextView.setContentHeight(getMeasuredHeight());
        }
        if ((mProgressView != null) && (mProgressView.getVisibility() != 8)) {
          mProgressView.measure(View.MeasureSpec.makeMeasureSpec(i1 - mProgressBarPadding * 2, 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Integer.MIN_VALUE));
        }
        return;
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(getClass().getSimpleName());
      ((StringBuilder)localObject1).append(" can only be used with android:layout_height=\"wrap_content\"");
      throw new IllegalStateException(((StringBuilder)localObject1).toString());
    }
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(getClass().getSimpleName());
    ((StringBuilder)localObject1).append(" can only be used with android:layout_width=\"match_parent\" (or fill_parent)");
    throw new IllegalStateException(((StringBuilder)localObject1).toString());
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    if ((expandedMenuItemId != 0) && (mExpandedMenuPresenter != null) && (mOptionsMenu != null))
    {
      MenuItem localMenuItem = mOptionsMenu.findItem(expandedMenuItemId);
      if (localMenuItem != null) {
        localMenuItem.expandActionView();
      }
    }
    if (isOverflowOpen) {
      postShowOverflowMenu();
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    if ((mExpandedMenuPresenter != null) && (mExpandedMenuPresenter.mCurrentExpandedItem != null)) {
      expandedMenuItemId = mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
    }
    isOverflowOpen = isOverflowMenuShowing();
    return localSavedState;
  }
  
  public void setCollapsible(boolean paramBoolean)
  {
    mIsCollapsible = paramBoolean;
  }
  
  public void setContextView(ActionBarContextView paramActionBarContextView)
  {
    mContextView = paramActionBarContextView;
  }
  
  public void setCustomView(View paramView)
  {
    int i;
    if ((mDisplayOptions & 0x10) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    if ((mCustomNavView != null) && (i != 0)) {
      removeView(mCustomNavView);
    }
    mCustomNavView = paramView;
    if ((mCustomNavView != null) && (i != 0)) {
      addView(mCustomNavView);
    }
  }
  
  public void setDefaultNavigationContentDescription(int paramInt)
  {
    if (mDefaultUpDescription == paramInt) {
      return;
    }
    mDefaultUpDescription = paramInt;
    updateHomeAccessibility(mUpGoerFive.isEnabled());
  }
  
  public void setDefaultNavigationIcon(Drawable paramDrawable)
  {
    mHomeLayout.setDefaultUpIndicator(paramDrawable);
  }
  
  public void setDisplayOptions(int paramInt)
  {
    int i = mDisplayOptions;
    int j = -1;
    if (i != -1) {
      j = paramInt ^ mDisplayOptions;
    }
    mDisplayOptions = paramInt;
    if ((j & 0x3F) != 0)
    {
      boolean bool;
      if ((j & 0x4) != 0)
      {
        if ((paramInt & 0x4) != 0) {
          bool = true;
        } else {
          bool = false;
        }
        mHomeLayout.setShowUp(bool);
        if (bool) {
          setHomeButtonEnabled(true);
        }
      }
      if ((j & 0x1) != 0)
      {
        if ((mLogo != null) && ((paramInt & 0x1) != 0)) {
          i = 1;
        } else {
          i = 0;
        }
        HomeView localHomeView = mHomeLayout;
        Drawable localDrawable;
        if (i != 0) {
          localDrawable = mLogo;
        } else {
          localDrawable = mIcon;
        }
        localHomeView.setIcon(localDrawable);
      }
      if ((j & 0x8) != 0) {
        if ((paramInt & 0x8) != 0) {
          initTitle();
        } else {
          mUpGoerFive.removeView(mTitleLayout);
        }
      }
      if ((paramInt & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      if ((mDisplayOptions & 0x4) != 0) {
        i = 1;
      } else {
        i = 0;
      }
      if ((!bool) && (i != 0)) {
        i = 1;
      } else {
        i = 0;
      }
      mHomeLayout.setShowIcon(bool);
      if (((bool) || (i != 0)) && (mExpandedActionView == null)) {
        i = 0;
      } else {
        i = 8;
      }
      mHomeLayout.setVisibility(i);
      if (((j & 0x10) != 0) && (mCustomNavView != null)) {
        if ((paramInt & 0x10) != 0) {
          addView(mCustomNavView);
        } else {
          removeView(mCustomNavView);
        }
      }
      if ((mTitleLayout != null) && ((j & 0x20) != 0)) {
        if ((paramInt & 0x20) != 0)
        {
          mTitleView.setSingleLine(false);
          mTitleView.setMaxLines(2);
        }
        else
        {
          mTitleView.setMaxLines(1);
          mTitleView.setSingleLine(true);
        }
      }
      requestLayout();
    }
    else
    {
      invalidate();
    }
    updateHomeAccessibility(mUpGoerFive.isEnabled());
  }
  
  public void setDropdownParams(SpinnerAdapter paramSpinnerAdapter, AdapterView.OnItemSelectedListener paramOnItemSelectedListener)
  {
    mSpinnerAdapter = paramSpinnerAdapter;
    mNavItemSelectedListener = paramOnItemSelectedListener;
    if (mSpinner != null)
    {
      mSpinner.setAdapter(paramSpinnerAdapter);
      mSpinner.setOnItemSelectedListener(paramOnItemSelectedListener);
    }
  }
  
  public void setDropdownSelectedPosition(int paramInt)
  {
    mSpinner.setSelection(paramInt);
  }
  
  public void setEmbeddedTabView(ScrollingTabContainerView paramScrollingTabContainerView)
  {
    if (mTabScrollView != null) {
      removeView(mTabScrollView);
    }
    mTabScrollView = paramScrollingTabContainerView;
    boolean bool;
    if (paramScrollingTabContainerView != null) {
      bool = true;
    } else {
      bool = false;
    }
    mIncludeTabs = bool;
    if ((mIncludeTabs) && (mNavigationMode == 2))
    {
      addView(mTabScrollView);
      ViewGroup.LayoutParams localLayoutParams = mTabScrollView.getLayoutParams();
      width = -2;
      height = -1;
      paramScrollingTabContainerView.setAllowCollapse(true);
    }
  }
  
  public void setHomeButtonEnabled(boolean paramBoolean)
  {
    setHomeButtonEnabled(paramBoolean, true);
  }
  
  public void setIcon(int paramInt)
  {
    Drawable localDrawable;
    if (paramInt != 0) {
      localDrawable = mContext.getDrawable(paramInt);
    } else {
      localDrawable = null;
    }
    setIcon(localDrawable);
  }
  
  public void setIcon(Drawable paramDrawable)
  {
    mIcon = paramDrawable;
    if ((paramDrawable != null) && (((mDisplayOptions & 0x1) == 0) || (mLogo == null))) {
      mHomeLayout.setIcon(paramDrawable);
    }
    if (mExpandedActionView != null) {
      mExpandedHomeLayout.setIcon(mIcon.getConstantState().newDrawable(getResources()));
    }
  }
  
  public void setLogo(int paramInt)
  {
    Drawable localDrawable;
    if (paramInt != 0) {
      localDrawable = mContext.getDrawable(paramInt);
    } else {
      localDrawable = null;
    }
    setLogo(localDrawable);
  }
  
  public void setLogo(Drawable paramDrawable)
  {
    mLogo = paramDrawable;
    if ((paramDrawable != null) && ((mDisplayOptions & 0x1) != 0)) {
      mHomeLayout.setIcon(paramDrawable);
    }
  }
  
  public void setMenu(Menu paramMenu, MenuPresenter.Callback paramCallback)
  {
    if (paramMenu == mOptionsMenu) {
      return;
    }
    if (mOptionsMenu != null)
    {
      mOptionsMenu.removeMenuPresenter(mActionMenuPresenter);
      mOptionsMenu.removeMenuPresenter(mExpandedMenuPresenter);
    }
    paramMenu = (MenuBuilder)paramMenu;
    mOptionsMenu = paramMenu;
    ViewGroup localViewGroup;
    if (mMenuView != null)
    {
      localViewGroup = (ViewGroup)mMenuView.getParent();
      if (localViewGroup != null) {
        localViewGroup.removeView(mMenuView);
      }
    }
    if (mActionMenuPresenter == null)
    {
      mActionMenuPresenter = new ActionMenuPresenter(mContext);
      mActionMenuPresenter.setCallback(paramCallback);
      mActionMenuPresenter.setId(16908705);
      mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(null);
    }
    paramCallback = new ViewGroup.LayoutParams(-2, -1);
    if (!mSplitActionBar)
    {
      mActionMenuPresenter.setExpandedActionViewsExclusive(getResources().getBoolean(17956866));
      configPresenters(paramMenu);
      paramMenu = (ActionMenuView)mActionMenuPresenter.getMenuView(this);
      localViewGroup = (ViewGroup)paramMenu.getParent();
      if ((localViewGroup != null) && (localViewGroup != this)) {
        localViewGroup.removeView(paramMenu);
      }
      addView(paramMenu, paramCallback);
    }
    else
    {
      mActionMenuPresenter.setExpandedActionViewsExclusive(false);
      mActionMenuPresenter.setWidthLimit(getContextgetResourcesgetDisplayMetricswidthPixels, true);
      mActionMenuPresenter.setItemLimit(Integer.MAX_VALUE);
      width = -1;
      height = -2;
      configPresenters(paramMenu);
      paramMenu = (ActionMenuView)mActionMenuPresenter.getMenuView(this);
      if (mSplitView != null)
      {
        localViewGroup = (ViewGroup)paramMenu.getParent();
        if ((localViewGroup != null) && (localViewGroup != mSplitView)) {
          localViewGroup.removeView(paramMenu);
        }
        paramMenu.setVisibility(getAnimatedVisibility());
        mSplitView.addView(paramMenu, paramCallback);
      }
      else
      {
        paramMenu.setLayoutParams(paramCallback);
      }
    }
    mMenuView = paramMenu;
  }
  
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1)
  {
    if (mActionMenuPresenter != null) {
      mActionMenuPresenter.setCallback(paramCallback);
    }
    if (mOptionsMenu != null) {
      mOptionsMenu.setCallback(paramCallback1);
    }
  }
  
  public void setMenuPrepared()
  {
    mMenuPrepared = true;
  }
  
  public void setNavigationContentDescription(int paramInt)
  {
    mHomeDescriptionRes = paramInt;
    CharSequence localCharSequence;
    if (paramInt != 0) {
      localCharSequence = getResources().getText(paramInt);
    } else {
      localCharSequence = null;
    }
    mHomeDescription = localCharSequence;
    updateHomeAccessibility(mUpGoerFive.isEnabled());
  }
  
  public void setNavigationContentDescription(CharSequence paramCharSequence)
  {
    mHomeDescription = paramCharSequence;
    updateHomeAccessibility(mUpGoerFive.isEnabled());
  }
  
  public void setNavigationIcon(int paramInt)
  {
    mHomeLayout.setUpIndicator(paramInt);
  }
  
  public void setNavigationIcon(Drawable paramDrawable)
  {
    mHomeLayout.setUpIndicator(paramDrawable);
  }
  
  public void setNavigationMode(int paramInt)
  {
    int i = mNavigationMode;
    if (paramInt != i)
    {
      switch (i)
      {
      default: 
        break;
      case 2: 
        if ((mTabScrollView != null) && (mIncludeTabs)) {
          removeView(mTabScrollView);
        }
        break;
      case 1: 
        if (mListNavLayout != null) {
          removeView(mListNavLayout);
        }
        break;
      }
      switch (paramInt)
      {
      default: 
        break;
      case 2: 
        if ((mTabScrollView != null) && (mIncludeTabs)) {
          addView(mTabScrollView);
        }
        break;
      case 1: 
        if (mSpinner == null)
        {
          mSpinner = new Spinner(mContext, null, 16843479);
          mSpinner.setId(16908699);
          mListNavLayout = new LinearLayout(mContext, null, 16843508);
          LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -1);
          gravity = 17;
          mListNavLayout.addView(mSpinner, localLayoutParams);
        }
        if (mSpinner.getAdapter() != mSpinnerAdapter) {
          mSpinner.setAdapter(mSpinnerAdapter);
        }
        mSpinner.setOnItemSelectedListener(mNavItemSelectedListener);
        addView(mListNavLayout);
      }
      mNavigationMode = paramInt;
      requestLayout();
    }
  }
  
  public void setSplitToolbar(boolean paramBoolean)
  {
    if (mSplitActionBar != paramBoolean)
    {
      ViewGroup localViewGroup;
      if (mMenuView != null)
      {
        localViewGroup = (ViewGroup)mMenuView.getParent();
        if (localViewGroup != null) {
          localViewGroup.removeView(mMenuView);
        }
        if (paramBoolean)
        {
          if (mSplitView != null) {
            mSplitView.addView(mMenuView);
          }
          mMenuView.getLayoutParams().width = -1;
        }
        else
        {
          addView(mMenuView);
          mMenuView.getLayoutParams().width = -2;
        }
        mMenuView.requestLayout();
      }
      if (mSplitView != null)
      {
        localViewGroup = mSplitView;
        int i;
        if (paramBoolean) {
          i = 0;
        } else {
          i = 8;
        }
        localViewGroup.setVisibility(i);
      }
      if (mActionMenuPresenter != null) {
        if (!paramBoolean)
        {
          mActionMenuPresenter.setExpandedActionViewsExclusive(getResources().getBoolean(17956866));
        }
        else
        {
          mActionMenuPresenter.setExpandedActionViewsExclusive(false);
          mActionMenuPresenter.setWidthLimit(getContextgetResourcesgetDisplayMetricswidthPixels, true);
          mActionMenuPresenter.setItemLimit(Integer.MAX_VALUE);
        }
      }
      super.setSplitToolbar(paramBoolean);
    }
  }
  
  public void setSubtitle(CharSequence paramCharSequence)
  {
    mSubtitle = paramCharSequence;
    if (mSubtitleView != null)
    {
      mSubtitleView.setText(paramCharSequence);
      TextView localTextView = mSubtitleView;
      int i = 8;
      int j;
      if (paramCharSequence != null) {
        j = 0;
      } else {
        j = 8;
      }
      localTextView.setVisibility(j);
      if ((mExpandedActionView == null) && ((mDisplayOptions & 0x8) != 0) && ((!TextUtils.isEmpty(mTitle)) || (!TextUtils.isEmpty(mSubtitle)))) {
        j = 1;
      } else {
        j = 0;
      }
      paramCharSequence = mTitleLayout;
      if (j != 0) {
        i = 0;
      }
      paramCharSequence.setVisibility(i);
    }
    updateHomeAccessibility(mUpGoerFive.isEnabled());
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    mUserTitle = true;
    setTitleImpl(paramCharSequence);
  }
  
  public void setWindowCallback(Window.Callback paramCallback)
  {
    mWindowCallback = paramCallback;
  }
  
  public void setWindowTitle(CharSequence paramCharSequence)
  {
    if (!mUserTitle) {
      setTitleImpl(paramCharSequence);
    }
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return false;
  }
  
  private class ExpandedActionViewMenuPresenter
    implements MenuPresenter
  {
    MenuItemImpl mCurrentExpandedItem;
    MenuBuilder mMenu;
    
    private ExpandedActionViewMenuPresenter() {}
    
    public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
    {
      if ((mExpandedActionView instanceof CollapsibleActionView)) {
        ((CollapsibleActionView)mExpandedActionView).onActionViewCollapsed();
      }
      removeView(mExpandedActionView);
      mUpGoerFive.removeView(mExpandedHomeLayout);
      mExpandedActionView = null;
      if ((mDisplayOptions & 0x2) != 0) {
        mHomeLayout.setVisibility(0);
      }
      if ((mDisplayOptions & 0x8) != 0) {
        if (mTitleLayout == null) {
          ActionBarView.this.initTitle();
        } else {
          mTitleLayout.setVisibility(0);
        }
      }
      if (mTabScrollView != null) {
        mTabScrollView.setVisibility(0);
      }
      if (mSpinner != null) {
        mSpinner.setVisibility(0);
      }
      if (mCustomNavView != null) {
        mCustomNavView.setVisibility(0);
      }
      mExpandedHomeLayout.setIcon(null);
      mCurrentExpandedItem = null;
      setHomeButtonEnabled(mWasHomeEnabled);
      requestLayout();
      paramMenuItemImpl.setActionViewExpanded(false);
      return true;
    }
    
    public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
    {
      mExpandedActionView = paramMenuItemImpl.getActionView();
      mExpandedHomeLayout.setIcon(mIcon.getConstantState().newDrawable(getResources()));
      mCurrentExpandedItem = paramMenuItemImpl;
      if (mExpandedActionView.getParent() != ActionBarView.this) {
        addView(mExpandedActionView);
      }
      if (mExpandedHomeLayout.getParent() != mUpGoerFive) {
        mUpGoerFive.addView(mExpandedHomeLayout);
      }
      mHomeLayout.setVisibility(8);
      if (mTitleLayout != null) {
        mTitleLayout.setVisibility(8);
      }
      if (mTabScrollView != null) {
        mTabScrollView.setVisibility(8);
      }
      if (mSpinner != null) {
        mSpinner.setVisibility(8);
      }
      if (mCustomNavView != null) {
        mCustomNavView.setVisibility(8);
      }
      ActionBarView.this.setHomeButtonEnabled(false, false);
      requestLayout();
      paramMenuItemImpl.setActionViewExpanded(true);
      if ((mExpandedActionView instanceof CollapsibleActionView)) {
        ((CollapsibleActionView)mExpandedActionView).onActionViewExpanded();
      }
      return true;
    }
    
    public boolean flagActionItems()
    {
      return false;
    }
    
    public int getId()
    {
      return 0;
    }
    
    public MenuView getMenuView(ViewGroup paramViewGroup)
    {
      return null;
    }
    
    public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder)
    {
      if ((mMenu != null) && (mCurrentExpandedItem != null)) {
        mMenu.collapseItemActionView(mCurrentExpandedItem);
      }
      mMenu = paramMenuBuilder;
    }
    
    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {}
    
    public void onRestoreInstanceState(Parcelable paramParcelable) {}
    
    public Parcelable onSaveInstanceState()
    {
      return null;
    }
    
    public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
    {
      return false;
    }
    
    public void setCallback(MenuPresenter.Callback paramCallback) {}
    
    public void updateMenuView(boolean paramBoolean)
    {
      if (mCurrentExpandedItem != null)
      {
        int i = 0;
        int j = i;
        if (mMenu != null)
        {
          int k = mMenu.size();
          for (int m = 0;; m++)
          {
            j = i;
            if (m >= k) {
              break;
            }
            if (mMenu.getItem(m) == mCurrentExpandedItem)
            {
              j = 1;
              break;
            }
          }
        }
        if (j == 0) {
          collapseItemActionView(mMenu, mCurrentExpandedItem);
        }
      }
    }
  }
  
  private static class HomeView
    extends FrameLayout
  {
    private static final long DEFAULT_TRANSITION_DURATION = 150L;
    private Drawable mDefaultUpIndicator;
    private ImageView mIconView;
    private int mStartOffset;
    private Drawable mUpIndicator;
    private int mUpIndicatorRes;
    private ImageView mUpView;
    private int mUpWidth;
    
    public HomeView(Context paramContext)
    {
      this(paramContext, null);
    }
    
    public HomeView(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramContext = getLayoutTransition();
      if (paramContext != null) {
        paramContext.setDuration(150L);
      }
    }
    
    private void updateUpIndicator()
    {
      if (mUpIndicator != null) {
        mUpView.setImageDrawable(mUpIndicator);
      } else if (mUpIndicatorRes != 0) {
        mUpView.setImageDrawable(getContext().getDrawable(mUpIndicatorRes));
      } else {
        mUpView.setImageDrawable(mDefaultUpIndicator);
      }
    }
    
    public boolean dispatchHoverEvent(MotionEvent paramMotionEvent)
    {
      return onHoverEvent(paramMotionEvent);
    }
    
    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
    {
      onPopulateAccessibilityEvent(paramAccessibilityEvent);
      return true;
    }
    
    public int getStartOffset()
    {
      int i;
      if (mUpView.getVisibility() == 8) {
        i = mStartOffset;
      } else {
        i = 0;
      }
      return i;
    }
    
    public int getUpWidth()
    {
      return mUpWidth;
    }
    
    protected void onConfigurationChanged(Configuration paramConfiguration)
    {
      super.onConfigurationChanged(paramConfiguration);
      if (mUpIndicatorRes != 0) {
        updateUpIndicator();
      }
    }
    
    protected void onFinishInflate()
    {
      mUpView = ((ImageView)findViewById(16909537));
      mIconView = ((ImageView)findViewById(16908332));
      mDefaultUpIndicator = mUpView.getDrawable();
    }
    
    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int i = (paramInt4 - paramInt2) / 2;
      paramBoolean = isLayoutRtl();
      int j = getWidth();
      paramInt4 = 0;
      if (mUpView.getVisibility() != 8)
      {
        localLayoutParams = (FrameLayout.LayoutParams)mUpView.getLayoutParams();
        int k = mUpView.getMeasuredHeight();
        paramInt4 = mUpView.getMeasuredWidth();
        m = leftMargin + paramInt4 + rightMargin;
        int n = i - k / 2;
        if (paramBoolean)
        {
          paramInt2 = j;
          i1 = paramInt2 - paramInt4;
          int i2 = paramInt3 - m;
          paramInt4 = paramInt2;
          paramInt3 = i1;
          paramInt2 = i2;
        }
        else
        {
          i1 = 0;
          paramInt1 += m;
          paramInt2 = paramInt3;
          paramInt3 = i1;
        }
        mUpView.layout(paramInt3, n, paramInt4, n + k);
        paramInt3 = m;
      }
      else
      {
        paramInt2 = paramInt3;
        paramInt3 = paramInt4;
      }
      FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)mIconView.getLayoutParams();
      paramInt4 = mIconView.getMeasuredHeight();
      int i1 = mIconView.getMeasuredWidth();
      paramInt1 = (paramInt2 - paramInt1) / 2;
      int m = Math.max(topMargin, i - paramInt4 / 2);
      paramInt1 = Math.max(localLayoutParams.getMarginStart(), paramInt1 - i1 / 2);
      if (paramBoolean)
      {
        paramInt2 = j - paramInt3 - paramInt1;
        paramInt1 = paramInt2 - i1;
      }
      for (;;)
      {
        break;
        paramInt1 = paramInt3 + paramInt1;
        paramInt2 = paramInt1 + i1;
      }
      mIconView.layout(paramInt1, m, paramInt2, m + paramInt4);
    }
    
    protected void onMeasure(int paramInt1, int paramInt2)
    {
      measureChildWithMargins(mUpView, paramInt1, 0, paramInt2, 0);
      FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)mUpView.getLayoutParams();
      int i = leftMargin + rightMargin;
      mUpWidth = mUpView.getMeasuredWidth();
      mStartOffset = (mUpWidth + i);
      if (mUpView.getVisibility() == 8) {
        j = 0;
      } else {
        j = mStartOffset;
      }
      int k = topMargin + mUpView.getMeasuredHeight() + bottomMargin;
      int m;
      int n;
      if (mIconView.getVisibility() != 8)
      {
        measureChildWithMargins(mIconView, paramInt1, j, paramInt2, 0);
        localLayoutParams = (FrameLayout.LayoutParams)mIconView.getLayoutParams();
        m = j + (leftMargin + mIconView.getMeasuredWidth() + rightMargin);
        n = Math.max(k, topMargin + mIconView.getMeasuredHeight() + bottomMargin);
      }
      else
      {
        m = j;
        n = k;
        if (i < 0)
        {
          m = j - i;
          n = k;
        }
      }
      i = View.MeasureSpec.getMode(paramInt1);
      k = View.MeasureSpec.getMode(paramInt2);
      int j = View.MeasureSpec.getSize(paramInt1);
      paramInt1 = View.MeasureSpec.getSize(paramInt2);
      if (i != Integer.MIN_VALUE)
      {
        if (i == 1073741824) {
          m = j;
        }
      }
      else {
        m = Math.min(m, j);
      }
      if (k != Integer.MIN_VALUE)
      {
        if (k == 1073741824) {
          n = paramInt1;
        }
      }
      else {
        n = Math.min(n, paramInt1);
      }
      setMeasuredDimension(m, n);
    }
    
    public void onPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
    {
      super.onPopulateAccessibilityEventInternal(paramAccessibilityEvent);
      CharSequence localCharSequence = getContentDescription();
      if (!TextUtils.isEmpty(localCharSequence)) {
        paramAccessibilityEvent.getText().add(localCharSequence);
      }
    }
    
    public void setDefaultUpIndicator(Drawable paramDrawable)
    {
      mDefaultUpIndicator = paramDrawable;
      updateUpIndicator();
    }
    
    public void setIcon(Drawable paramDrawable)
    {
      mIconView.setImageDrawable(paramDrawable);
    }
    
    public void setShowIcon(boolean paramBoolean)
    {
      ImageView localImageView = mIconView;
      int i;
      if (paramBoolean) {
        i = 0;
      } else {
        i = 8;
      }
      localImageView.setVisibility(i);
    }
    
    public void setShowUp(boolean paramBoolean)
    {
      ImageView localImageView = mUpView;
      int i;
      if (paramBoolean) {
        i = 0;
      } else {
        i = 8;
      }
      localImageView.setVisibility(i);
    }
    
    public void setUpIndicator(int paramInt)
    {
      mUpIndicatorRes = paramInt;
      mUpIndicator = null;
      updateUpIndicator();
    }
    
    public void setUpIndicator(Drawable paramDrawable)
    {
      mUpIndicator = paramDrawable;
      mUpIndicatorRes = 0;
      updateUpIndicator();
    }
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public ActionBarView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActionBarView.SavedState(paramAnonymousParcel, null);
      }
      
      public ActionBarView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ActionBarView.SavedState[paramAnonymousInt];
      }
    };
    int expandedMenuItemId;
    boolean isOverflowOpen;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      expandedMenuItemId = paramParcel.readInt();
      boolean bool;
      if (paramParcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      isOverflowOpen = bool;
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(expandedMenuItemId);
      paramParcel.writeInt(isOverflowOpen);
    }
  }
}
