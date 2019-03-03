package android.app;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

@Deprecated
public class ListFragment
  extends Fragment
{
  ListAdapter mAdapter;
  CharSequence mEmptyText;
  View mEmptyView;
  private final Handler mHandler = new Handler();
  ListView mList;
  View mListContainer;
  boolean mListShown;
  private final AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      onListItemClick((ListView)paramAnonymousAdapterView, paramAnonymousView, paramAnonymousInt, paramAnonymousLong);
    }
  };
  View mProgressContainer;
  private final Runnable mRequestFocus = new Runnable()
  {
    public void run()
    {
      mList.focusableViewAvailable(mList);
    }
  };
  TextView mStandardEmptyView;
  
  public ListFragment() {}
  
  private void ensureList()
  {
    if (mList != null) {
      return;
    }
    Object localObject = getView();
    if (localObject != null)
    {
      if ((localObject instanceof ListView))
      {
        mList = ((ListView)localObject);
      }
      else
      {
        mStandardEmptyView = ((TextView)((View)localObject).findViewById(16909056));
        if (mStandardEmptyView == null) {
          mEmptyView = ((View)localObject).findViewById(16908292);
        } else {
          mStandardEmptyView.setVisibility(8);
        }
        mProgressContainer = ((View)localObject).findViewById(16909264);
        mListContainer = ((View)localObject).findViewById(16909089);
        localObject = ((View)localObject).findViewById(16908298);
        if (!(localObject instanceof ListView)) {
          break label253;
        }
        mList = ((ListView)localObject);
        if (mList == null) {
          break label243;
        }
        if (mEmptyView != null)
        {
          mList.setEmptyView(mEmptyView);
        }
        else if (mEmptyText != null)
        {
          mStandardEmptyView.setText(mEmptyText);
          mList.setEmptyView(mStandardEmptyView);
        }
      }
      mListShown = true;
      mList.setOnItemClickListener(mOnClickListener);
      if (mAdapter != null)
      {
        localObject = mAdapter;
        mAdapter = null;
        setListAdapter((ListAdapter)localObject);
      }
      else if (mProgressContainer != null)
      {
        setListShown(false, false);
      }
      mHandler.post(mRequestFocus);
      return;
      label243:
      throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
      label253:
      throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView class");
    }
    throw new IllegalStateException("Content view not yet created");
  }
  
  private void setListShown(boolean paramBoolean1, boolean paramBoolean2)
  {
    ensureList();
    if (mProgressContainer != null)
    {
      if (mListShown == paramBoolean1) {
        return;
      }
      mListShown = paramBoolean1;
      if (paramBoolean1)
      {
        if (paramBoolean2)
        {
          mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getContext(), 17432577));
          mListContainer.startAnimation(AnimationUtils.loadAnimation(getContext(), 17432576));
        }
        else
        {
          mProgressContainer.clearAnimation();
          mListContainer.clearAnimation();
        }
        mProgressContainer.setVisibility(8);
        mListContainer.setVisibility(0);
      }
      else
      {
        if (paramBoolean2)
        {
          mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getContext(), 17432576));
          mListContainer.startAnimation(AnimationUtils.loadAnimation(getContext(), 17432577));
        }
        else
        {
          mProgressContainer.clearAnimation();
          mListContainer.clearAnimation();
        }
        mProgressContainer.setVisibility(0);
        mListContainer.setVisibility(8);
      }
      return;
    }
    throw new IllegalStateException("Can't be used with a custom content view");
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
    ensureList();
    return mList.getSelectedItemId();
  }
  
  public int getSelectedItemPosition()
  {
    ensureList();
    return mList.getSelectedItemPosition();
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(17367060, paramViewGroup, false);
  }
  
  public void onDestroyView()
  {
    mHandler.removeCallbacks(mRequestFocus);
    mList = null;
    mListShown = false;
    mListContainer = null;
    mProgressContainer = null;
    mEmptyView = null;
    mStandardEmptyView = null;
    super.onDestroyView();
  }
  
  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong) {}
  
  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    ensureList();
  }
  
  public void setEmptyText(CharSequence paramCharSequence)
  {
    ensureList();
    if (mStandardEmptyView != null)
    {
      mStandardEmptyView.setText(paramCharSequence);
      if (mEmptyText == null) {
        mList.setEmptyView(mStandardEmptyView);
      }
      mEmptyText = paramCharSequence;
      return;
    }
    throw new IllegalStateException("Can't be used with a custom content view");
  }
  
  public void setListAdapter(ListAdapter paramListAdapter)
  {
    ListAdapter localListAdapter = mAdapter;
    boolean bool = false;
    int i;
    if (localListAdapter != null) {
      i = 1;
    } else {
      i = 0;
    }
    mAdapter = paramListAdapter;
    if (mList != null)
    {
      mList.setAdapter(paramListAdapter);
      if ((!mListShown) && (i == 0))
      {
        if (getView().getWindowToken() != null) {
          bool = true;
        }
        setListShown(true, bool);
      }
    }
  }
  
  public void setListShown(boolean paramBoolean)
  {
    setListShown(paramBoolean, true);
  }
  
  public void setListShownNoAnimation(boolean paramBoolean)
  {
    setListShown(paramBoolean, false);
  }
  
  public void setSelection(int paramInt)
  {
    ensureList();
    mList.setSelection(paramInt);
  }
}
