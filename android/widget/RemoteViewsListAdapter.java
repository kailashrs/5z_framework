package android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;

public class RemoteViewsListAdapter
  extends BaseAdapter
{
  private Context mContext;
  private ArrayList<RemoteViews> mRemoteViewsList;
  private int mViewTypeCount;
  private ArrayList<Integer> mViewTypes = new ArrayList();
  
  public RemoteViewsListAdapter(Context paramContext, ArrayList<RemoteViews> paramArrayList, int paramInt)
  {
    mContext = paramContext;
    mRemoteViewsList = paramArrayList;
    mViewTypeCount = paramInt;
    init();
  }
  
  private void init()
  {
    if (mRemoteViewsList == null) {
      return;
    }
    mViewTypes.clear();
    Iterator localIterator = mRemoteViewsList.iterator();
    while (localIterator.hasNext())
    {
      RemoteViews localRemoteViews = (RemoteViews)localIterator.next();
      if (!mViewTypes.contains(Integer.valueOf(localRemoteViews.getLayoutId()))) {
        mViewTypes.add(Integer.valueOf(localRemoteViews.getLayoutId()));
      }
    }
    if ((mViewTypes.size() <= mViewTypeCount) && (mViewTypeCount >= 1)) {
      return;
    }
    throw new RuntimeException("Invalid view type count -- view type count must be >= 1and must be as large as the total number of distinct view types");
  }
  
  public int getCount()
  {
    if (mRemoteViewsList != null) {
      return mRemoteViewsList.size();
    }
    return 0;
  }
  
  public Object getItem(int paramInt)
  {
    return null;
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public int getItemViewType(int paramInt)
  {
    if (paramInt < getCount())
    {
      paramInt = ((RemoteViews)mRemoteViewsList.get(paramInt)).getLayoutId();
      return mViewTypes.indexOf(Integer.valueOf(paramInt));
    }
    return 0;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramInt < getCount())
    {
      RemoteViews localRemoteViews = (RemoteViews)mRemoteViewsList.get(paramInt);
      localRemoteViews.setIsWidgetCollectionChild(true);
      if ((paramView != null) && (localRemoteViews != null) && (paramView.getId() == localRemoteViews.getLayoutId())) {
        localRemoteViews.reapply(mContext, paramView);
      } else {
        paramView = localRemoteViews.apply(mContext, paramViewGroup);
      }
      return paramView;
    }
    return null;
  }
  
  public int getViewTypeCount()
  {
    return mViewTypeCount;
  }
  
  public boolean hasStableIds()
  {
    return false;
  }
  
  public void setViewsList(ArrayList<RemoteViews> paramArrayList)
  {
    mRemoteViewsList = paramArrayList;
    init();
    notifyDataSetChanged();
  }
}
