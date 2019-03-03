package com.android.internal.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window.Callback;
import android.widget.ActionMenuPresenter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toolbar;
import android.widget.Toolbar.LayoutParams;
import com.android.internal.R.styleable;
import com.android.internal.view.menu.ActionMenuItem;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuBuilder.Callback;
import com.android.internal.view.menu.MenuPresenter.Callback;

public class ToolbarWidgetWrapper
  implements DecorToolbar
{
  private static final int AFFECTS_LOGO_MASK = 3;
  private static final long DEFAULT_FADE_DURATION_MS = 200L;
  private static final String TAG = "ToolbarWidgetWrapper";
  private ActionMenuPresenter mActionMenuPresenter;
  private View mCustomView;
  private int mDefaultNavigationContentDescription = 0;
  private Drawable mDefaultNavigationIcon;
  private int mDisplayOpts;
  private CharSequence mHomeDescription;
  private Drawable mIcon;
  private Drawable mLogo;
  private boolean mMenuPrepared;
  private Drawable mNavIcon;
  private int mNavigationMode = 0;
  private Spinner mSpinner;
  private CharSequence mSubtitle;
  private View mTabView;
  private CharSequence mTitle;
  private boolean mTitleSet;
  private Toolbar mToolbar;
  private Window.Callback mWindowCallback;
  
  public ToolbarWidgetWrapper(Toolbar paramToolbar, boolean paramBoolean)
  {
    this(paramToolbar, paramBoolean, 17039432);
  }
  
  public ToolbarWidgetWrapper(Toolbar paramToolbar, boolean paramBoolean, int paramInt)
  {
    mToolbar = paramToolbar;
    mTitle = paramToolbar.getTitle();
    mSubtitle = paramToolbar.getSubtitle();
    boolean bool;
    if (mTitle != null) {
      bool = true;
    } else {
      bool = false;
    }
    mTitleSet = bool;
    mNavIcon = mToolbar.getNavigationIcon();
    paramToolbar = paramToolbar.getContext().obtainStyledAttributes(null, R.styleable.ActionBar, 16843470, 0);
    mDefaultNavigationIcon = paramToolbar.getDrawable(13);
    if (paramBoolean)
    {
      Object localObject = paramToolbar.getText(5);
      if (!TextUtils.isEmpty((CharSequence)localObject)) {
        setTitle((CharSequence)localObject);
      }
      localObject = paramToolbar.getText(9);
      if (!TextUtils.isEmpty((CharSequence)localObject)) {
        setSubtitle((CharSequence)localObject);
      }
      localObject = paramToolbar.getDrawable(6);
      if (localObject != null) {
        setLogo((Drawable)localObject);
      }
      localObject = paramToolbar.getDrawable(0);
      if (localObject != null) {
        setIcon((Drawable)localObject);
      }
      if ((mNavIcon == null) && (mDefaultNavigationIcon != null)) {
        setNavigationIcon(mDefaultNavigationIcon);
      }
      setDisplayOptions(paramToolbar.getInt(8, 0));
      int i = paramToolbar.getResourceId(10, 0);
      if (i != 0)
      {
        setCustomView(LayoutInflater.from(mToolbar.getContext()).inflate(i, mToolbar, false));
        setDisplayOptions(mDisplayOpts | 0x10);
      }
      i = paramToolbar.getLayoutDimension(4, 0);
      if (i > 0)
      {
        localObject = mToolbar.getLayoutParams();
        height = i;
        mToolbar.setLayoutParams((ViewGroup.LayoutParams)localObject);
      }
      i = paramToolbar.getDimensionPixelOffset(22, -1);
      int j = paramToolbar.getDimensionPixelOffset(23, -1);
      if ((i >= 0) || (j >= 0)) {
        mToolbar.setContentInsetsRelative(Math.max(i, 0), Math.max(j, 0));
      }
      i = paramToolbar.getResourceId(11, 0);
      if (i != 0) {
        mToolbar.setTitleTextAppearance(mToolbar.getContext(), i);
      }
      i = paramToolbar.getResourceId(12, 0);
      if (i != 0) {
        mToolbar.setSubtitleTextAppearance(mToolbar.getContext(), i);
      }
      i = paramToolbar.getResourceId(26, 0);
      if (i != 0) {
        mToolbar.setPopupTheme(i);
      }
    }
    else
    {
      mDisplayOpts = detectDisplayOptions();
    }
    paramToolbar.recycle();
    setDefaultNavigationContentDescription(paramInt);
    mHomeDescription = mToolbar.getNavigationContentDescription();
    mToolbar.setNavigationOnClickListener(new View.OnClickListener()
    {
      final ActionMenuItem mNavItem = new ActionMenuItem(mToolbar.getContext(), 0, 16908332, 0, 0, mTitle);
      
      public void onClick(View paramAnonymousView)
      {
        if ((mWindowCallback != null) && (mMenuPrepared)) {
          mWindowCallback.onMenuItemSelected(0, mNavItem);
        }
      }
    });
  }
  
  private int detectDisplayOptions()
  {
    int i = 11;
    if (mToolbar.getNavigationIcon() != null)
    {
      i = 0xB | 0x4;
      mDefaultNavigationIcon = mToolbar.getNavigationIcon();
    }
    return i;
  }
  
  private void ensureSpinner()
  {
    if (mSpinner == null)
    {
      mSpinner = new Spinner(getContext(), null, 16843479);
      Toolbar.LayoutParams localLayoutParams = new Toolbar.LayoutParams(-2, -2, 8388627);
      mSpinner.setLayoutParams(localLayoutParams);
    }
  }
  
  private void setTitleInt(CharSequence paramCharSequence)
  {
    mTitle = paramCharSequence;
    if ((mDisplayOpts & 0x8) != 0) {
      mToolbar.setTitle(paramCharSequence);
    }
  }
  
  private void updateHomeAccessibility()
  {
    if ((mDisplayOpts & 0x4) != 0) {
      if (TextUtils.isEmpty(mHomeDescription)) {
        mToolbar.setNavigationContentDescription(mDefaultNavigationContentDescription);
      } else {
        mToolbar.setNavigationContentDescription(mHomeDescription);
      }
    }
  }
  
  private void updateNavigationIcon()
  {
    if ((mDisplayOpts & 0x4) != 0)
    {
      Toolbar localToolbar = mToolbar;
      Drawable localDrawable;
      if (mNavIcon != null) {
        localDrawable = mNavIcon;
      } else {
        localDrawable = mDefaultNavigationIcon;
      }
      localToolbar.setNavigationIcon(localDrawable);
    }
    else
    {
      mToolbar.setNavigationIcon(null);
    }
  }
  
  private void updateToolbarLogo()
  {
    Drawable localDrawable = null;
    if ((mDisplayOpts & 0x2) != 0) {
      if ((mDisplayOpts & 0x1) != 0)
      {
        if (mLogo != null) {
          localDrawable = mLogo;
        } else {
          localDrawable = mIcon;
        }
      }
      else {
        localDrawable = mIcon;
      }
    }
    mToolbar.setLogo(localDrawable);
  }
  
  public void animateToVisibility(int paramInt)
  {
    Animator localAnimator = setupAnimatorToVisibility(paramInt, 200L);
    if (localAnimator != null) {
      localAnimator.start();
    }
  }
  
  public boolean canShowOverflowMenu()
  {
    return mToolbar.canShowOverflowMenu();
  }
  
  public boolean canSplit()
  {
    return false;
  }
  
  public void collapseActionView()
  {
    mToolbar.collapseActionView();
  }
  
  public void dismissPopupMenus()
  {
    mToolbar.dismissPopupMenus();
  }
  
  public Context getContext()
  {
    return mToolbar.getContext();
  }
  
  public View getCustomView()
  {
    return mCustomView;
  }
  
  public int getDisplayOptions()
  {
    return mDisplayOpts;
  }
  
  public int getDropdownItemCount()
  {
    int i;
    if (mSpinner != null) {
      i = mSpinner.getCount();
    } else {
      i = 0;
    }
    return i;
  }
  
  public int getDropdownSelectedPosition()
  {
    int i;
    if (mSpinner != null) {
      i = mSpinner.getSelectedItemPosition();
    } else {
      i = 0;
    }
    return i;
  }
  
  public int getHeight()
  {
    return mToolbar.getHeight();
  }
  
  public Menu getMenu()
  {
    return mToolbar.getMenu();
  }
  
  public int getNavigationMode()
  {
    return mNavigationMode;
  }
  
  public CharSequence getSubtitle()
  {
    return mToolbar.getSubtitle();
  }
  
  public CharSequence getTitle()
  {
    return mToolbar.getTitle();
  }
  
  public ViewGroup getViewGroup()
  {
    return mToolbar;
  }
  
  public int getVisibility()
  {
    return mToolbar.getVisibility();
  }
  
  public boolean hasEmbeddedTabs()
  {
    boolean bool;
    if (mTabView != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasExpandedActionView()
  {
    return mToolbar.hasExpandedActionView();
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
  
  public boolean hideOverflowMenu()
  {
    return mToolbar.hideOverflowMenu();
  }
  
  public void initIndeterminateProgress()
  {
    Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
  }
  
  public void initProgress()
  {
    Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
  }
  
  public boolean isOverflowMenuShowPending()
  {
    return mToolbar.isOverflowMenuShowPending();
  }
  
  public boolean isOverflowMenuShowing()
  {
    return mToolbar.isOverflowMenuShowing();
  }
  
  public boolean isSplit()
  {
    return false;
  }
  
  public boolean isTitleTruncated()
  {
    return mToolbar.isTitleTruncated();
  }
  
  public void restoreHierarchyState(SparseArray<Parcelable> paramSparseArray)
  {
    mToolbar.restoreHierarchyState(paramSparseArray);
  }
  
  public void saveHierarchyState(SparseArray<Parcelable> paramSparseArray)
  {
    mToolbar.saveHierarchyState(paramSparseArray);
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    mToolbar.setBackgroundDrawable(paramDrawable);
  }
  
  public void setCollapsible(boolean paramBoolean)
  {
    mToolbar.setCollapsible(paramBoolean);
  }
  
  public void setCustomView(View paramView)
  {
    if ((mCustomView != null) && ((mDisplayOpts & 0x10) != 0)) {
      mToolbar.removeView(mCustomView);
    }
    mCustomView = paramView;
    if ((paramView != null) && ((mDisplayOpts & 0x10) != 0)) {
      mToolbar.addView(mCustomView);
    }
  }
  
  public void setDefaultNavigationContentDescription(int paramInt)
  {
    if (paramInt == mDefaultNavigationContentDescription) {
      return;
    }
    mDefaultNavigationContentDescription = paramInt;
    if (TextUtils.isEmpty(mToolbar.getNavigationContentDescription())) {
      setNavigationContentDescription(mDefaultNavigationContentDescription);
    }
  }
  
  public void setDefaultNavigationIcon(Drawable paramDrawable)
  {
    if (mDefaultNavigationIcon != paramDrawable)
    {
      mDefaultNavigationIcon = paramDrawable;
      updateNavigationIcon();
    }
  }
  
  public void setDisplayOptions(int paramInt)
  {
    int i = mDisplayOpts ^ paramInt;
    mDisplayOpts = paramInt;
    if (i != 0)
    {
      if ((i & 0x4) != 0)
      {
        if ((paramInt & 0x4) != 0) {
          updateHomeAccessibility();
        }
        updateNavigationIcon();
      }
      if ((i & 0x3) != 0) {
        updateToolbarLogo();
      }
      if ((i & 0x8) != 0) {
        if ((paramInt & 0x8) != 0)
        {
          mToolbar.setTitle(mTitle);
          mToolbar.setSubtitle(mSubtitle);
        }
        else
        {
          mToolbar.setTitle(null);
          mToolbar.setSubtitle(null);
        }
      }
      if (((i & 0x10) != 0) && (mCustomView != null)) {
        if ((paramInt & 0x10) != 0) {
          mToolbar.addView(mCustomView);
        } else {
          mToolbar.removeView(mCustomView);
        }
      }
    }
  }
  
  public void setDropdownParams(SpinnerAdapter paramSpinnerAdapter, AdapterView.OnItemSelectedListener paramOnItemSelectedListener)
  {
    ensureSpinner();
    mSpinner.setAdapter(paramSpinnerAdapter);
    mSpinner.setOnItemSelectedListener(paramOnItemSelectedListener);
  }
  
  public void setDropdownSelectedPosition(int paramInt)
  {
    if (mSpinner != null)
    {
      mSpinner.setSelection(paramInt);
      return;
    }
    throw new IllegalStateException("Can't set dropdown selected position without an adapter");
  }
  
  public void setEmbeddedTabView(ScrollingTabContainerView paramScrollingTabContainerView)
  {
    if ((mTabView != null) && (mTabView.getParent() == mToolbar)) {
      mToolbar.removeView(mTabView);
    }
    mTabView = paramScrollingTabContainerView;
    if ((paramScrollingTabContainerView != null) && (mNavigationMode == 2))
    {
      mToolbar.addView(mTabView, 0);
      Toolbar.LayoutParams localLayoutParams = (Toolbar.LayoutParams)mTabView.getLayoutParams();
      width = -2;
      height = -2;
      gravity = 8388691;
      paramScrollingTabContainerView.setAllowCollapse(true);
    }
  }
  
  public void setHomeButtonEnabled(boolean paramBoolean) {}
  
  public void setIcon(int paramInt)
  {
    Drawable localDrawable;
    if (paramInt != 0) {
      localDrawable = getContext().getDrawable(paramInt);
    } else {
      localDrawable = null;
    }
    setIcon(localDrawable);
  }
  
  public void setIcon(Drawable paramDrawable)
  {
    mIcon = paramDrawable;
    updateToolbarLogo();
  }
  
  public void setLogo(int paramInt)
  {
    Drawable localDrawable;
    if (paramInt != 0) {
      localDrawable = getContext().getDrawable(paramInt);
    } else {
      localDrawable = null;
    }
    setLogo(localDrawable);
  }
  
  public void setLogo(Drawable paramDrawable)
  {
    mLogo = paramDrawable;
    updateToolbarLogo();
  }
  
  public void setMenu(Menu paramMenu, MenuPresenter.Callback paramCallback)
  {
    if (mActionMenuPresenter == null)
    {
      mActionMenuPresenter = new ActionMenuPresenter(mToolbar.getContext());
      mActionMenuPresenter.setId(16908705);
    }
    mActionMenuPresenter.setCallback(paramCallback);
    mToolbar.setMenu((MenuBuilder)paramMenu, mActionMenuPresenter);
  }
  
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1)
  {
    mToolbar.setMenuCallbacks(paramCallback, paramCallback1);
  }
  
  public void setMenuPrepared()
  {
    mMenuPrepared = true;
  }
  
  public void setNavigationContentDescription(int paramInt)
  {
    String str;
    if (paramInt == 0) {
      str = null;
    } else {
      str = getContext().getString(paramInt);
    }
    setNavigationContentDescription(str);
  }
  
  public void setNavigationContentDescription(CharSequence paramCharSequence)
  {
    mHomeDescription = paramCharSequence;
    updateHomeAccessibility();
  }
  
  public void setNavigationIcon(int paramInt)
  {
    Drawable localDrawable;
    if (paramInt != 0) {
      localDrawable = mToolbar.getContext().getDrawable(paramInt);
    } else {
      localDrawable = null;
    }
    setNavigationIcon(localDrawable);
  }
  
  public void setNavigationIcon(Drawable paramDrawable)
  {
    mNavIcon = paramDrawable;
    updateNavigationIcon();
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
        if ((mTabView != null) && (mTabView.getParent() == mToolbar)) {
          mToolbar.removeView(mTabView);
        }
        break;
      case 1: 
        if ((mSpinner != null) && (mSpinner.getParent() == mToolbar)) {
          mToolbar.removeView(mSpinner);
        }
        break;
      }
      mNavigationMode = paramInt;
      Object localObject;
      switch (paramInt)
      {
      default: 
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Invalid navigation mode ");
        ((StringBuilder)localObject).append(paramInt);
        throw new IllegalArgumentException(((StringBuilder)localObject).toString());
      case 2: 
        if (mTabView != null)
        {
          mToolbar.addView(mTabView, 0);
          localObject = (Toolbar.LayoutParams)mTabView.getLayoutParams();
          width = -2;
          height = -2;
          gravity = 8388691;
        }
        break;
      case 1: 
        ensureSpinner();
        mToolbar.addView(mSpinner, 0);
        break;
      }
    }
  }
  
  public void setSplitToolbar(boolean paramBoolean)
  {
    if (!paramBoolean) {
      return;
    }
    throw new UnsupportedOperationException("Cannot split an android.widget.Toolbar");
  }
  
  public void setSplitView(ViewGroup paramViewGroup) {}
  
  public void setSplitWhenNarrow(boolean paramBoolean) {}
  
  public void setSubtitle(CharSequence paramCharSequence)
  {
    mSubtitle = paramCharSequence;
    if ((mDisplayOpts & 0x8) != 0) {
      mToolbar.setSubtitle(paramCharSequence);
    }
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    mTitleSet = true;
    setTitleInt(paramCharSequence);
  }
  
  public void setVisibility(int paramInt)
  {
    mToolbar.setVisibility(paramInt);
  }
  
  public void setWindowCallback(Window.Callback paramCallback)
  {
    mWindowCallback = paramCallback;
  }
  
  public void setWindowTitle(CharSequence paramCharSequence)
  {
    if (!mTitleSet) {
      setTitleInt(paramCharSequence);
    }
  }
  
  public Animator setupAnimatorToVisibility(int paramInt, long paramLong)
  {
    ObjectAnimator localObjectAnimator;
    if (paramInt == 8)
    {
      localObjectAnimator = ObjectAnimator.ofFloat(mToolbar, View.ALPHA, new float[] { 1.0F, 0.0F });
      localObjectAnimator.setDuration(paramLong);
      localObjectAnimator.addListener(new AnimatorListenerAdapter()
      {
        private boolean mCanceled = false;
        
        public void onAnimationCancel(Animator paramAnonymousAnimator)
        {
          mCanceled = true;
        }
        
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          if (!mCanceled) {
            mToolbar.setVisibility(8);
          }
        }
      });
      return localObjectAnimator;
    }
    if (paramInt == 0)
    {
      localObjectAnimator = ObjectAnimator.ofFloat(mToolbar, View.ALPHA, new float[] { 0.0F, 1.0F });
      localObjectAnimator.setDuration(paramLong);
      localObjectAnimator.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationStart(Animator paramAnonymousAnimator)
        {
          mToolbar.setVisibility(0);
        }
      });
      return localObjectAnimator;
    }
    return null;
  }
  
  public boolean showOverflowMenu()
  {
    return mToolbar.showOverflowMenu();
  }
}
