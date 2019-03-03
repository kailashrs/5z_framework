package com.android.internal.view.menu;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class IconMenuPresenter
  extends BaseMenuPresenter
{
  private static final String OPEN_SUBMENU_KEY = "android:menu:icon:submenu";
  private static final String VIEWS_TAG = "android:menu:icon";
  private int mMaxItems = -1;
  private IconMenuItemView mMoreView;
  MenuDialogHelper mOpenSubMenu;
  int mOpenSubMenuId;
  SubMenuPresenterCallback mSubMenuPresenterCallback = new SubMenuPresenterCallback();
  
  public IconMenuPresenter(Context paramContext)
  {
    super(new ContextThemeWrapper(paramContext, 16975064), 17367184, 17367183);
  }
  
  protected void addItemView(View paramView, int paramInt)
  {
    IconMenuItemView localIconMenuItemView = (IconMenuItemView)paramView;
    IconMenuView localIconMenuView = (IconMenuView)mMenuView;
    localIconMenuItemView.setIconMenuView(localIconMenuView);
    localIconMenuItemView.setItemInvoker(localIconMenuView);
    localIconMenuItemView.setBackgroundDrawable(localIconMenuView.getItemBackgroundDrawable());
    super.addItemView(paramView, paramInt);
  }
  
  public void bindItemView(MenuItemImpl paramMenuItemImpl, MenuView.ItemView paramItemView)
  {
    paramItemView = (IconMenuItemView)paramItemView;
    paramItemView.setItemData(paramMenuItemImpl);
    paramItemView.initialize(paramMenuItemImpl.getTitleForItemView(paramItemView), paramMenuItemImpl.getIcon());
    int i;
    if (paramMenuItemImpl.isVisible()) {
      i = 0;
    } else {
      i = 8;
    }
    paramItemView.setVisibility(i);
    paramItemView.setEnabled(paramItemView.isEnabled());
    paramItemView.setLayoutParams(paramItemView.getTextAppropriateLayoutParams());
  }
  
  protected boolean filterLeftoverView(ViewGroup paramViewGroup, int paramInt)
  {
    if (paramViewGroup.getChildAt(paramInt) != mMoreView) {
      return super.filterLeftoverView(paramViewGroup, paramInt);
    }
    return false;
  }
  
  public int getNumActualItemsShown()
  {
    return ((IconMenuView)mMenuView).getNumActualItemsShown();
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder)
  {
    super.initForMenu(paramContext, paramMenuBuilder);
    mMaxItems = -1;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    restoreHierarchyState((Bundle)paramParcelable);
  }
  
  public Parcelable onSaveInstanceState()
  {
    if (mMenuView == null) {
      return null;
    }
    Bundle localBundle = new Bundle();
    saveHierarchyState(localBundle);
    if (mOpenSubMenuId > 0) {
      localBundle.putInt("android:menu:icon:submenu", mOpenSubMenuId);
    }
    return localBundle;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
  {
    if (!paramSubMenuBuilder.hasVisibleItems()) {
      return false;
    }
    MenuDialogHelper localMenuDialogHelper = new MenuDialogHelper(paramSubMenuBuilder);
    localMenuDialogHelper.setPresenterCallback(mSubMenuPresenterCallback);
    localMenuDialogHelper.show(null);
    mOpenSubMenu = localMenuDialogHelper;
    mOpenSubMenuId = paramSubMenuBuilder.getItem().getItemId();
    super.onSubMenuSelected(paramSubMenuBuilder);
    return true;
  }
  
  public void restoreHierarchyState(Bundle paramBundle)
  {
    SparseArray localSparseArray = paramBundle.getSparseParcelableArray("android:menu:icon");
    if (localSparseArray != null) {
      ((View)mMenuView).restoreHierarchyState(localSparseArray);
    }
    int i = paramBundle.getInt("android:menu:icon:submenu", 0);
    if ((i > 0) && (mMenu != null))
    {
      paramBundle = mMenu.findItem(i);
      if (paramBundle != null) {
        onSubMenuSelected((SubMenuBuilder)paramBundle.getSubMenu());
      }
    }
  }
  
  public void saveHierarchyState(Bundle paramBundle)
  {
    SparseArray localSparseArray = new SparseArray();
    if (mMenuView != null) {
      ((View)mMenuView).saveHierarchyState(localSparseArray);
    }
    paramBundle.putSparseParcelableArray("android:menu:icon", localSparseArray);
  }
  
  public boolean shouldIncludeItem(int paramInt, MenuItemImpl paramMenuItemImpl)
  {
    int i = mMenu.getNonActionItems().size();
    int j = mMaxItems;
    boolean bool1 = false;
    if (((i == j) && (paramInt < mMaxItems)) || (paramInt < mMaxItems - 1)) {
      paramInt = 1;
    } else {
      paramInt = 0;
    }
    boolean bool2 = bool1;
    if (paramInt != 0)
    {
      bool2 = bool1;
      if (!paramMenuItemImpl.isActionButton()) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public void updateMenuView(boolean paramBoolean)
  {
    IconMenuView localIconMenuView = (IconMenuView)mMenuView;
    if (mMaxItems < 0) {
      mMaxItems = localIconMenuView.getMaxItems();
    }
    ArrayList localArrayList = mMenu.getNonActionItems();
    int i;
    if (localArrayList.size() > mMaxItems) {
      i = 1;
    } else {
      i = 0;
    }
    super.updateMenuView(paramBoolean);
    if ((i != 0) && ((mMoreView == null) || (mMoreView.getParent() != localIconMenuView)))
    {
      if (mMoreView == null)
      {
        mMoreView = localIconMenuView.createMoreItemView();
        mMoreView.setBackgroundDrawable(localIconMenuView.getItemBackgroundDrawable());
      }
      localIconMenuView.addView(mMoreView);
    }
    else if ((i == 0) && (mMoreView != null))
    {
      localIconMenuView.removeView(mMoreView);
    }
    if (i != 0) {
      i = mMaxItems - 1;
    } else {
      i = localArrayList.size();
    }
    localIconMenuView.setNumActualItemsShown(i);
  }
  
  class SubMenuPresenterCallback
    implements MenuPresenter.Callback
  {
    SubMenuPresenterCallback() {}
    
    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
    {
      mOpenSubMenuId = 0;
      if (mOpenSubMenu != null)
      {
        mOpenSubMenu.dismiss();
        mOpenSubMenu = null;
      }
    }
    
    public boolean onOpenSubMenu(MenuBuilder paramMenuBuilder)
    {
      if (paramMenuBuilder != null) {
        mOpenSubMenuId = ((SubMenuBuilder)paramMenuBuilder).getItem().getItemId();
      }
      return false;
    }
  }
}
