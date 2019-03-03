package android.widget;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

public abstract class BaseExpandableListAdapter
  implements ExpandableListAdapter, HeterogeneousExpandableList
{
  private final DataSetObservable mDataSetObservable = new DataSetObservable();
  
  public BaseExpandableListAdapter() {}
  
  public boolean areAllItemsEnabled()
  {
    return true;
  }
  
  public int getChildType(int paramInt1, int paramInt2)
  {
    return 0;
  }
  
  public int getChildTypeCount()
  {
    return 1;
  }
  
  public long getCombinedChildId(long paramLong1, long paramLong2)
  {
    return (0x7FFFFFFF & paramLong1) << 32 | 0x8000000000000000 | 0xFFFFFFFFFFFFFFFF & paramLong2;
  }
  
  public long getCombinedGroupId(long paramLong)
  {
    return (0x7FFFFFFF & paramLong) << 32;
  }
  
  public int getGroupType(int paramInt)
  {
    return 0;
  }
  
  public int getGroupTypeCount()
  {
    return 1;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if (getGroupCount() == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void notifyDataSetChanged()
  {
    mDataSetObservable.notifyChanged();
  }
  
  public void notifyDataSetInvalidated()
  {
    mDataSetObservable.notifyInvalidated();
  }
  
  public void onGroupCollapsed(int paramInt) {}
  
  public void onGroupExpanded(int paramInt) {}
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    mDataSetObservable.registerObserver(paramDataSetObserver);
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    mDataSetObservable.unregisterObserver(paramDataSetObserver);
  }
}
