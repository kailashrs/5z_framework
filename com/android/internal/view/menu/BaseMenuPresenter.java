package com.android.internal.view.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public abstract class BaseMenuPresenter
  implements MenuPresenter
{
  private MenuPresenter.Callback mCallback;
  protected Context mContext;
  private int mId;
  protected LayoutInflater mInflater;
  private int mItemLayoutRes;
  protected MenuBuilder mMenu;
  private int mMenuLayoutRes;
  protected MenuView mMenuView;
  protected Context mSystemContext;
  protected LayoutInflater mSystemInflater;
  
  public BaseMenuPresenter(Context paramContext, int paramInt1, int paramInt2)
  {
    mSystemContext = paramContext;
    mSystemInflater = LayoutInflater.from(paramContext);
    mMenuLayoutRes = paramInt1;
    mItemLayoutRes = paramInt2;
  }
  
  protected void addItemView(View paramView, int paramInt)
  {
    ViewGroup localViewGroup = (ViewGroup)paramView.getParent();
    if (localViewGroup != null) {
      localViewGroup.removeView(paramView);
    }
    ((ViewGroup)mMenuView).addView(paramView, paramInt);
  }
  
  public abstract void bindItemView(MenuItemImpl paramMenuItemImpl, MenuView.ItemView paramItemView);
  
  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    return false;
  }
  
  public MenuView.ItemView createItemView(ViewGroup paramViewGroup)
  {
    return (MenuView.ItemView)mSystemInflater.inflate(mItemLayoutRes, paramViewGroup, false);
  }
  
  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    return false;
  }
  
  protected boolean filterLeftoverView(ViewGroup paramViewGroup, int paramInt)
  {
    paramViewGroup.removeViewAt(paramInt);
    return true;
  }
  
  public boolean flagActionItems()
  {
    return false;
  }
  
  public MenuPresenter.Callback getCallback()
  {
    return mCallback;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public View getItemView(MenuItemImpl paramMenuItemImpl, View paramView, ViewGroup paramViewGroup)
  {
    if ((paramView instanceof MenuView.ItemView)) {
      paramView = (MenuView.ItemView)paramView;
    } else {
      paramView = createItemView(paramViewGroup);
    }
    bindItemView(paramMenuItemImpl, paramView);
    return (View)paramView;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup)
  {
    if (mMenuView == null)
    {
      mMenuView = ((MenuView)mSystemInflater.inflate(mMenuLayoutRes, paramViewGroup, false));
      mMenuView.initialize(mMenu);
      updateMenuView(true);
    }
    return mMenuView;
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder)
  {
    mContext = paramContext;
    mInflater = LayoutInflater.from(mContext);
    mMenu = paramMenuBuilder;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    if (mCallback != null) {
      mCallback.onCloseMenu(paramMenuBuilder, paramBoolean);
    }
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
  {
    if (mCallback != null) {
      return mCallback.onOpenSubMenu(paramSubMenuBuilder);
    }
    return false;
  }
  
  public void setCallback(MenuPresenter.Callback paramCallback)
  {
    mCallback = paramCallback;
  }
  
  public void setId(int paramInt)
  {
    mId = paramInt;
  }
  
  public boolean shouldIncludeItem(int paramInt, MenuItemImpl paramMenuItemImpl)
  {
    return true;
  }
  
  public void updateMenuView(boolean paramBoolean)
  {
    ViewGroup localViewGroup = (ViewGroup)mMenuView;
    if (localViewGroup == null) {
      return;
    }
    int i = 0;
    if (mMenu != null)
    {
      mMenu.flagActionItems();
      ArrayList localArrayList = mMenu.getVisibleItems();
      int j = localArrayList.size();
      i = 0;
      int k = 0;
      while (k < j)
      {
        MenuItemImpl localMenuItemImpl1 = (MenuItemImpl)localArrayList.get(k);
        int m = i;
        if (shouldIncludeItem(i, localMenuItemImpl1))
        {
          View localView1 = localViewGroup.getChildAt(i);
          MenuItemImpl localMenuItemImpl2;
          if ((localView1 instanceof MenuView.ItemView)) {
            localMenuItemImpl2 = ((MenuView.ItemView)localView1).getItemData();
          } else {
            localMenuItemImpl2 = null;
          }
          View localView2 = getItemView(localMenuItemImpl1, localView1, localViewGroup);
          if (localMenuItemImpl1 != localMenuItemImpl2)
          {
            localView2.setPressed(false);
            localView2.jumpDrawablesToCurrentState();
          }
          if (localView2 != localView1) {
            addItemView(localView2, i);
          }
          m = i + 1;
        }
        k++;
        i = m;
      }
    }
    while (i < localViewGroup.getChildCount()) {
      if (!filterLeftoverView(localViewGroup, i)) {
        i++;
      }
    }
  }
}
