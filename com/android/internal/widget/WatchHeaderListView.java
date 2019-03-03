package com.android.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ListView.FixedViewInfo;
import java.util.ArrayList;
import java.util.function.Predicate;

public class WatchHeaderListView
  extends ListView
{
  private View mTopPanel;
  
  public WatchHeaderListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public WatchHeaderListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public WatchHeaderListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  private void wrapAdapterIfNecessary()
  {
    ListAdapter localListAdapter = getAdapter();
    if ((localListAdapter != null) && (mTopPanel != null))
    {
      if (!(localListAdapter instanceof WatchHeaderListAdapter)) {
        wrapHeaderListAdapterInternal();
      }
      ((WatchHeaderListAdapter)getAdapter()).setTopPanel(mTopPanel);
      dispatchDataSetObserverOnChangedInternal();
    }
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (mTopPanel == null)
    {
      setTopPanel(paramView);
      return;
    }
    throw new IllegalStateException("WatchHeaderListView can host only one header");
  }
  
  protected <T extends View> T findViewByPredicateTraversal(Predicate<View> paramPredicate, View paramView)
  {
    View localView = super.findViewByPredicateTraversal(paramPredicate, paramView);
    if ((localView == null) && (mTopPanel != null) && (mTopPanel != paramView) && (!mTopPanel.isRootNamespace())) {
      return mTopPanel.findViewByPredicate(paramPredicate);
    }
    return localView;
  }
  
  protected View findViewTraversal(int paramInt)
  {
    View localView = super.findViewTraversal(paramInt);
    if ((localView == null) && (mTopPanel != null) && (!mTopPanel.isRootNamespace())) {
      return mTopPanel.findViewById(paramInt);
    }
    return localView;
  }
  
  protected View findViewWithTagTraversal(Object paramObject)
  {
    View localView = super.findViewWithTagTraversal(paramObject);
    if ((localView == null) && (mTopPanel != null) && (!mTopPanel.isRootNamespace())) {
      return mTopPanel.findViewWithTag(paramObject);
    }
    return localView;
  }
  
  public int getHeaderViewsCount()
  {
    int i;
    if (mTopPanel == null)
    {
      i = super.getHeaderViewsCount();
    }
    else
    {
      int j = super.getHeaderViewsCount();
      if (mTopPanel.getVisibility() == 8) {
        i = 0;
      } else {
        i = 1;
      }
      i = j + i;
    }
    return i;
  }
  
  public void setAdapter(ListAdapter paramListAdapter)
  {
    super.setAdapter(paramListAdapter);
    wrapAdapterIfNecessary();
  }
  
  public void setTopPanel(View paramView)
  {
    mTopPanel = paramView;
    wrapAdapterIfNecessary();
  }
  
  protected HeaderViewListAdapter wrapHeaderListAdapterInternal(ArrayList<ListView.FixedViewInfo> paramArrayList1, ArrayList<ListView.FixedViewInfo> paramArrayList2, ListAdapter paramListAdapter)
  {
    return new WatchHeaderListAdapter(paramArrayList1, paramArrayList2, paramListAdapter);
  }
  
  private static class WatchHeaderListAdapter
    extends HeaderViewListAdapter
  {
    private View mTopPanel;
    
    public WatchHeaderListAdapter(ArrayList<ListView.FixedViewInfo> paramArrayList1, ArrayList<ListView.FixedViewInfo> paramArrayList2, ListAdapter paramListAdapter)
    {
      super(paramArrayList2, paramListAdapter);
    }
    
    private int getTopPanelCount()
    {
      int i;
      if ((mTopPanel != null) && (mTopPanel.getVisibility() != 8)) {
        i = 1;
      } else {
        i = 0;
      }
      return i;
    }
    
    public boolean areAllItemsEnabled()
    {
      boolean bool;
      if ((getTopPanelCount() == 0) && (super.areAllItemsEnabled())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int getCount()
    {
      return super.getCount() + getTopPanelCount();
    }
    
    public Object getItem(int paramInt)
    {
      int i = getTopPanelCount();
      Object localObject;
      if (paramInt < i) {
        localObject = null;
      } else {
        localObject = super.getItem(paramInt - i);
      }
      return localObject;
    }
    
    public long getItemId(int paramInt)
    {
      int i = getHeadersCount() + getTopPanelCount();
      if ((getWrappedAdapter() != null) && (paramInt >= i))
      {
        paramInt -= i;
        if (paramInt < getWrappedAdapter().getCount()) {
          return getWrappedAdapter().getItemId(paramInt);
        }
      }
      return -1L;
    }
    
    public int getItemViewType(int paramInt)
    {
      int i = getHeadersCount() + getTopPanelCount();
      if ((getWrappedAdapter() != null) && (paramInt >= i))
      {
        paramInt -= i;
        if (paramInt < getWrappedAdapter().getCount()) {
          return getWrappedAdapter().getItemViewType(paramInt);
        }
      }
      return -2;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      int i = getTopPanelCount();
      if (paramInt < i) {
        paramView = mTopPanel;
      } else {
        paramView = super.getView(paramInt - i, paramView, paramViewGroup);
      }
      return paramView;
    }
    
    public boolean isEnabled(int paramInt)
    {
      int i = getTopPanelCount();
      boolean bool;
      if (paramInt < i) {
        bool = false;
      } else {
        bool = super.isEnabled(paramInt - i);
      }
      return bool;
    }
    
    public void setTopPanel(View paramView)
    {
      mTopPanel = paramView;
    }
  }
}
