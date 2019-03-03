package com.android.internal.view.menu;

import android.content.Context;
import android.graphics.Rect;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow.OnDismissListener;

public abstract class MenuPopup
  implements ShowableListMenu, MenuPresenter, AdapterView.OnItemClickListener
{
  private Rect mEpicenterBounds;
  
  public MenuPopup() {}
  
  protected static int measureIndividualMenuWidth(ListAdapter paramListAdapter, ViewGroup paramViewGroup, Context paramContext, int paramInt)
  {
    int i = 0;
    Object localObject1 = null;
    int j = 0;
    int k = 0;
    int m = View.MeasureSpec.makeMeasureSpec(0, 0);
    int n = View.MeasureSpec.makeMeasureSpec(0, 0);
    int i1 = paramListAdapter.getCount();
    Object localObject2 = paramViewGroup;
    paramViewGroup = (ViewGroup)localObject1;
    while (k < i1)
    {
      int i2 = paramListAdapter.getItemViewType(k);
      int i3 = j;
      if (i2 != j)
      {
        i3 = i2;
        paramViewGroup = null;
      }
      localObject1 = localObject2;
      if (localObject2 == null) {
        localObject1 = new FrameLayout(paramContext);
      }
      paramViewGroup = paramListAdapter.getView(k, paramViewGroup, (ViewGroup)localObject1);
      paramViewGroup.measure(m, n);
      i2 = paramViewGroup.getMeasuredWidth();
      if (i2 >= paramInt) {
        return paramInt;
      }
      j = i;
      if (i2 > i) {
        j = i2;
      }
      k++;
      i = j;
      j = i3;
      localObject2 = localObject1;
    }
    return i;
  }
  
  protected static boolean shouldPreserveIconSpacing(MenuBuilder paramMenuBuilder)
  {
    boolean bool1 = false;
    int i = paramMenuBuilder.size();
    boolean bool2;
    for (int j = 0;; j++)
    {
      bool2 = bool1;
      if (j >= i) {
        break;
      }
      MenuItem localMenuItem = paramMenuBuilder.getItem(j);
      if ((localMenuItem.isVisible()) && (localMenuItem.getIcon() != null))
      {
        bool2 = true;
        break;
      }
    }
    return bool2;
  }
  
  protected static MenuAdapter toMenuAdapter(ListAdapter paramListAdapter)
  {
    if ((paramListAdapter instanceof HeaderViewListAdapter)) {
      return (MenuAdapter)((HeaderViewListAdapter)paramListAdapter).getWrappedAdapter();
    }
    return (MenuAdapter)paramListAdapter;
  }
  
  public abstract void addMenu(MenuBuilder paramMenuBuilder);
  
  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    return false;
  }
  
  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    return false;
  }
  
  public Rect getEpicenterBounds()
  {
    return mEpicenterBounds;
  }
  
  public int getId()
  {
    return 0;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup)
  {
    throw new UnsupportedOperationException("MenuPopups manage their own views");
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder) {}
  
  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = (ListAdapter)paramAdapterView.getAdapter();
    toMenuAdaptermAdapterMenu.performItemAction((MenuItem)paramAdapterView.getItem(paramInt), 0);
  }
  
  public abstract void setAnchorView(View paramView);
  
  public void setEpicenterBounds(Rect paramRect)
  {
    mEpicenterBounds = paramRect;
  }
  
  public abstract void setForceShowIcon(boolean paramBoolean);
  
  public abstract void setGravity(int paramInt);
  
  public abstract void setHorizontalOffset(int paramInt);
  
  public abstract void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener);
  
  public abstract void setShowTitle(boolean paramBoolean);
  
  public abstract void setVerticalOffset(int paramInt);
}
