package android.app;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListActivity
  extends Activity
{
  protected ListAdapter mAdapter;
  private boolean mFinishedStart = false;
  private Handler mHandler = new Handler();
  protected ListView mList;
  private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      onListItemClick((ListView)paramAnonymousAdapterView, paramAnonymousView, paramAnonymousInt, paramAnonymousLong);
    }
  };
  private Runnable mRequestFocus = new Runnable()
  {
    public void run()
    {
      mList.focusableViewAvailable(mList);
    }
  };
  
  public ListActivity() {}
  
  private void ensureList()
  {
    if (mList != null) {
      return;
    }
    setContentView(17367197);
  }
  
  public ListAdapter getListAdapter()
  {
    return mAdapter;
  }
  
  public ListView getListView()
  {
    ensureList();
    return mList;
  }
  
  public long getSelectedItemId()
  {
    return mList.getSelectedItemId();
  }
  
  public int getSelectedItemPosition()
  {
    return mList.getSelectedItemPosition();
  }
  
  public void onContentChanged()
  {
    super.onContentChanged();
    View localView = findViewById(16908292);
    mList = ((ListView)findViewById(16908298));
    if (mList != null)
    {
      if (localView != null) {
        mList.setEmptyView(localView);
      }
      mList.setOnItemClickListener(mOnClickListener);
      if (mFinishedStart) {
        setListAdapter(mAdapter);
      }
      mHandler.post(mRequestFocus);
      mFinishedStart = true;
      return;
    }
    throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
  }
  
  protected void onDestroy()
  {
    mHandler.removeCallbacks(mRequestFocus);
    super.onDestroy();
  }
  
  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong) {}
  
  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    ensureList();
    super.onRestoreInstanceState(paramBundle);
  }
  
  public void setListAdapter(ListAdapter paramListAdapter)
  {
    try
    {
      ensureList();
      mAdapter = paramListAdapter;
      mList.setAdapter(paramListAdapter);
      return;
    }
    finally {}
  }
  
  public void setSelection(int paramInt)
  {
    mList.setSelection(paramInt);
  }
}
