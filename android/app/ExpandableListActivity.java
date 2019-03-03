package android.app;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class ExpandableListActivity
  extends Activity
  implements View.OnCreateContextMenuListener, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupCollapseListener, ExpandableListView.OnGroupExpandListener
{
  ExpandableListAdapter mAdapter;
  boolean mFinishedStart = false;
  ExpandableListView mList;
  
  public ExpandableListActivity() {}
  
  private void ensureList()
  {
    if (mList != null) {
      return;
    }
    setContentView(17367041);
  }
  
  public ExpandableListAdapter getExpandableListAdapter()
  {
    return mAdapter;
  }
  
  public ExpandableListView getExpandableListView()
  {
    ensureList();
    return mList;
  }
  
  public long getSelectedId()
  {
    return mList.getSelectedId();
  }
  
  public long getSelectedPosition()
  {
    return mList.getSelectedPosition();
  }
  
  public boolean onChildClick(ExpandableListView paramExpandableListView, View paramView, int paramInt1, int paramInt2, long paramLong)
  {
    return false;
  }
  
  public void onContentChanged()
  {
    super.onContentChanged();
    View localView = findViewById(16908292);
    mList = ((ExpandableListView)findViewById(16908298));
    if (mList != null)
    {
      if (localView != null) {
        mList.setEmptyView(localView);
      }
      mList.setOnChildClickListener(this);
      mList.setOnGroupExpandListener(this);
      mList.setOnGroupCollapseListener(this);
      if (mFinishedStart) {
        setListAdapter(mAdapter);
      }
      mFinishedStart = true;
      return;
    }
    throw new RuntimeException("Your content must have a ExpandableListView whose id attribute is 'android.R.id.list'");
  }
  
  public void onCreateContextMenu(ContextMenu paramContextMenu, View paramView, ContextMenu.ContextMenuInfo paramContextMenuInfo) {}
  
  public void onGroupCollapse(int paramInt) {}
  
  public void onGroupExpand(int paramInt) {}
  
  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    ensureList();
    super.onRestoreInstanceState(paramBundle);
  }
  
  public void setListAdapter(ExpandableListAdapter paramExpandableListAdapter)
  {
    try
    {
      ensureList();
      mAdapter = paramExpandableListAdapter;
      mList.setAdapter(paramExpandableListAdapter);
      return;
    }
    finally {}
  }
  
  public boolean setSelectedChild(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    return mList.setSelectedChild(paramInt1, paramInt2, paramBoolean);
  }
  
  public void setSelectedGroup(int paramInt)
  {
    mList.setSelectedGroup(paramInt);
  }
}
