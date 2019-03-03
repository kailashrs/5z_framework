package android.widget;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;

public class HeaderViewListAdapter
  implements WrapperListAdapter, Filterable
{
  static final ArrayList<ListView.FixedViewInfo> EMPTY_INFO_LIST = new ArrayList();
  private final ListAdapter mAdapter;
  boolean mAreAllFixedViewsSelectable;
  ArrayList<ListView.FixedViewInfo> mFooterViewInfos;
  ArrayList<ListView.FixedViewInfo> mHeaderViewInfos;
  private final boolean mIsFilterable;
  
  public HeaderViewListAdapter(ArrayList<ListView.FixedViewInfo> paramArrayList1, ArrayList<ListView.FixedViewInfo> paramArrayList2, ListAdapter paramListAdapter)
  {
    mAdapter = paramListAdapter;
    mIsFilterable = (paramListAdapter instanceof Filterable);
    if (paramArrayList1 == null) {
      mHeaderViewInfos = EMPTY_INFO_LIST;
    } else {
      mHeaderViewInfos = paramArrayList1;
    }
    if (paramArrayList2 == null) {
      mFooterViewInfos = EMPTY_INFO_LIST;
    } else {
      mFooterViewInfos = paramArrayList2;
    }
    boolean bool;
    if ((areAllListInfosSelectable(mHeaderViewInfos)) && (areAllListInfosSelectable(mFooterViewInfos))) {
      bool = true;
    } else {
      bool = false;
    }
    mAreAllFixedViewsSelectable = bool;
  }
  
  private boolean areAllListInfosSelectable(ArrayList<ListView.FixedViewInfo> paramArrayList)
  {
    if (paramArrayList != null)
    {
      paramArrayList = paramArrayList.iterator();
      while (paramArrayList.hasNext()) {
        if (!nextisSelectable) {
          return false;
        }
      }
    }
    return true;
  }
  
  public boolean areAllItemsEnabled()
  {
    ListAdapter localListAdapter = mAdapter;
    boolean bool = true;
    if (localListAdapter != null)
    {
      if ((!mAreAllFixedViewsSelectable) || (!mAdapter.areAllItemsEnabled())) {
        bool = false;
      }
      return bool;
    }
    return true;
  }
  
  public int getCount()
  {
    if (mAdapter != null) {
      return getFootersCount() + getHeadersCount() + mAdapter.getCount();
    }
    return getFootersCount() + getHeadersCount();
  }
  
  public Filter getFilter()
  {
    if (mIsFilterable) {
      return ((Filterable)mAdapter).getFilter();
    }
    return null;
  }
  
  public int getFootersCount()
  {
    return mFooterViewInfos.size();
  }
  
  public int getHeadersCount()
  {
    return mHeaderViewInfos.size();
  }
  
  public Object getItem(int paramInt)
  {
    int i = getHeadersCount();
    if (paramInt < i) {
      return mHeaderViewInfos.get(paramInt)).data;
    }
    int j = paramInt - i;
    paramInt = 0;
    if (mAdapter != null)
    {
      i = mAdapter.getCount();
      paramInt = i;
      if (j < i) {
        return mAdapter.getItem(j);
      }
    }
    return mFooterViewInfos.get(j - paramInt)).data;
  }
  
  public long getItemId(int paramInt)
  {
    int i = getHeadersCount();
    if ((mAdapter != null) && (paramInt >= i))
    {
      paramInt -= i;
      if (paramInt < mAdapter.getCount()) {
        return mAdapter.getItemId(paramInt);
      }
    }
    return -1L;
  }
  
  public int getItemViewType(int paramInt)
  {
    int i = getHeadersCount();
    if ((mAdapter != null) && (paramInt >= i))
    {
      paramInt -= i;
      if (paramInt < mAdapter.getCount()) {
        return mAdapter.getItemViewType(paramInt);
      }
    }
    return -2;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    int i = getHeadersCount();
    if (paramInt < i) {
      return mHeaderViewInfos.get(paramInt)).view;
    }
    int j = paramInt - i;
    paramInt = 0;
    if (mAdapter != null)
    {
      i = mAdapter.getCount();
      paramInt = i;
      if (j < i) {
        return mAdapter.getView(j, paramView, paramViewGroup);
      }
    }
    return mFooterViewInfos.get(j - paramInt)).view;
  }
  
  public int getViewTypeCount()
  {
    if (mAdapter != null) {
      return mAdapter.getViewTypeCount();
    }
    return 1;
  }
  
  public ListAdapter getWrappedAdapter()
  {
    return mAdapter;
  }
  
  public boolean hasStableIds()
  {
    if (mAdapter != null) {
      return mAdapter.hasStableIds();
    }
    return false;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if ((mAdapter != null) && (!mAdapter.isEmpty())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isEnabled(int paramInt)
  {
    int i = getHeadersCount();
    if (paramInt < i) {
      return mHeaderViewInfos.get(paramInt)).isSelectable;
    }
    int j = paramInt - i;
    paramInt = 0;
    if (mAdapter != null)
    {
      i = mAdapter.getCount();
      paramInt = i;
      if (j < i) {
        return mAdapter.isEnabled(j);
      }
    }
    return mFooterViewInfos.get(j - paramInt)).isSelectable;
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    if (mAdapter != null) {
      mAdapter.registerDataSetObserver(paramDataSetObserver);
    }
  }
  
  public boolean removeFooter(View paramView)
  {
    boolean bool1 = false;
    for (int i = 0; i < mFooterViewInfos.size(); i++) {
      if (mFooterViewInfos.get(i)).view == paramView)
      {
        mFooterViewInfos.remove(i);
        boolean bool2 = bool1;
        if (areAllListInfosSelectable(mHeaderViewInfos))
        {
          bool2 = bool1;
          if (areAllListInfosSelectable(mFooterViewInfos)) {
            bool2 = true;
          }
        }
        mAreAllFixedViewsSelectable = bool2;
        return true;
      }
    }
    return false;
  }
  
  public boolean removeHeader(View paramView)
  {
    boolean bool1 = false;
    for (int i = 0; i < mHeaderViewInfos.size(); i++) {
      if (mHeaderViewInfos.get(i)).view == paramView)
      {
        mHeaderViewInfos.remove(i);
        boolean bool2 = bool1;
        if (areAllListInfosSelectable(mHeaderViewInfos))
        {
          bool2 = bool1;
          if (areAllListInfosSelectable(mFooterViewInfos)) {
            bool2 = true;
          }
        }
        mAreAllFixedViewsSelectable = bool2;
        return true;
      }
    }
    return false;
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    if (mAdapter != null) {
      mAdapter.unregisterDataSetObserver(paramDataSetObserver);
    }
  }
}
