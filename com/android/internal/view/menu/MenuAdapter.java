package com.android.internal.view.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;

public class MenuAdapter
  extends BaseAdapter
{
  MenuBuilder mAdapterMenu;
  private int mExpandedIndex = -1;
  private boolean mForceShowIcon;
  private final LayoutInflater mInflater;
  private final int mItemLayoutRes;
  private final boolean mOverflowOnly;
  
  public MenuAdapter(MenuBuilder paramMenuBuilder, LayoutInflater paramLayoutInflater, boolean paramBoolean, int paramInt)
  {
    mOverflowOnly = paramBoolean;
    mInflater = paramLayoutInflater;
    mAdapterMenu = paramMenuBuilder;
    mItemLayoutRes = paramInt;
    findExpandedIndex();
  }
  
  void findExpandedIndex()
  {
    MenuItemImpl localMenuItemImpl = mAdapterMenu.getExpandedItem();
    if (localMenuItemImpl != null)
    {
      ArrayList localArrayList = mAdapterMenu.getNonActionItems();
      int i = localArrayList.size();
      for (int j = 0; j < i; j++) {
        if ((MenuItemImpl)localArrayList.get(j) == localMenuItemImpl)
        {
          mExpandedIndex = j;
          return;
        }
      }
    }
    mExpandedIndex = -1;
  }
  
  public MenuBuilder getAdapterMenu()
  {
    return mAdapterMenu;
  }
  
  public int getCount()
  {
    ArrayList localArrayList;
    if (mOverflowOnly) {
      localArrayList = mAdapterMenu.getNonActionItems();
    } else {
      localArrayList = mAdapterMenu.getVisibleItems();
    }
    if (mExpandedIndex < 0) {
      return localArrayList.size();
    }
    return localArrayList.size() - 1;
  }
  
  public boolean getForceShowIcon()
  {
    return mForceShowIcon;
  }
  
  public MenuItemImpl getItem(int paramInt)
  {
    ArrayList localArrayList;
    if (mOverflowOnly) {
      localArrayList = mAdapterMenu.getNonActionItems();
    } else {
      localArrayList = mAdapterMenu.getVisibleItems();
    }
    int i = paramInt;
    if (mExpandedIndex >= 0)
    {
      i = paramInt;
      if (paramInt >= mExpandedIndex) {
        i = paramInt + 1;
      }
    }
    return (MenuItemImpl)localArrayList.get(i);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramView;
    if (paramView == null) {
      localView = mInflater.inflate(mItemLayoutRes, paramViewGroup, false);
    }
    int i = getItem(paramInt).getGroupId();
    int j;
    if (paramInt - 1 >= 0) {
      j = getItem(paramInt - 1).getGroupId();
    } else {
      j = i;
    }
    paramView = (ListMenuItemView)localView;
    boolean bool;
    if ((mAdapterMenu.isGroupDividerEnabled()) && (i != j)) {
      bool = true;
    } else {
      bool = false;
    }
    paramView.setGroupDividerEnabled(bool);
    paramView = (MenuView.ItemView)localView;
    if (mForceShowIcon) {
      ((ListMenuItemView)localView).setForceShowIcon(true);
    }
    paramView.initialize(getItem(paramInt), 0);
    return localView;
  }
  
  public void notifyDataSetChanged()
  {
    findExpandedIndex();
    super.notifyDataSetChanged();
  }
  
  public void setForceShowIcon(boolean paramBoolean)
  {
    mForceShowIcon = paramBoolean;
  }
}
