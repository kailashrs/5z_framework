package android.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public abstract class CursorTreeAdapter
  extends BaseExpandableListAdapter
  implements Filterable, CursorFilter.CursorFilterClient
{
  private boolean mAutoRequery;
  SparseArray<MyCursorHelper> mChildrenCursorHelpers;
  private Context mContext;
  CursorFilter mCursorFilter;
  FilterQueryProvider mFilterQueryProvider;
  MyCursorHelper mGroupCursorHelper;
  private Handler mHandler;
  
  public CursorTreeAdapter(Cursor paramCursor, Context paramContext)
  {
    init(paramCursor, paramContext, true);
  }
  
  public CursorTreeAdapter(Cursor paramCursor, Context paramContext, boolean paramBoolean)
  {
    init(paramCursor, paramContext, paramBoolean);
  }
  
  private void init(Cursor paramCursor, Context paramContext, boolean paramBoolean)
  {
    mContext = paramContext;
    mHandler = new Handler();
    mAutoRequery = paramBoolean;
    mGroupCursorHelper = new MyCursorHelper(paramCursor);
    mChildrenCursorHelpers = new SparseArray();
  }
  
  private void releaseCursorHelpers()
  {
    try
    {
      for (int i = mChildrenCursorHelpers.size() - 1; i >= 0; i--) {
        ((MyCursorHelper)mChildrenCursorHelpers.valueAt(i)).deactivate();
      }
      mChildrenCursorHelpers.clear();
      return;
    }
    finally {}
  }
  
  protected abstract void bindChildView(View paramView, Context paramContext, Cursor paramCursor, boolean paramBoolean);
  
  protected abstract void bindGroupView(View paramView, Context paramContext, Cursor paramCursor, boolean paramBoolean);
  
  public void changeCursor(Cursor paramCursor)
  {
    mGroupCursorHelper.changeCursor(paramCursor, true);
  }
  
  public String convertToString(Cursor paramCursor)
  {
    if (paramCursor == null) {
      paramCursor = "";
    } else {
      paramCursor = paramCursor.toString();
    }
    return paramCursor;
  }
  
  void deactivateChildrenCursorHelper(int paramInt)
  {
    try
    {
      MyCursorHelper localMyCursorHelper = getChildrenCursorHelper(paramInt, true);
      mChildrenCursorHelpers.remove(paramInt);
      localMyCursorHelper.deactivate();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public Cursor getChild(int paramInt1, int paramInt2)
  {
    return getChildrenCursorHelper(paramInt1, true).moveTo(paramInt2);
  }
  
  public long getChildId(int paramInt1, int paramInt2)
  {
    return getChildrenCursorHelper(paramInt1, true).getId(paramInt2);
  }
  
  public View getChildView(int paramInt1, int paramInt2, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
  {
    Cursor localCursor = getChildrenCursorHelper(paramInt1, true).moveTo(paramInt2);
    if (localCursor != null)
    {
      if (paramView == null) {
        paramView = newChildView(mContext, localCursor, paramBoolean, paramViewGroup);
      }
      bindChildView(paramView, mContext, localCursor, paramBoolean);
      return paramView;
    }
    throw new IllegalStateException("this should only be called when the cursor is valid");
  }
  
  public int getChildrenCount(int paramInt)
  {
    MyCursorHelper localMyCursorHelper = getChildrenCursorHelper(paramInt, true);
    if ((mGroupCursorHelper.isValid()) && (localMyCursorHelper != null)) {
      paramInt = localMyCursorHelper.getCount();
    } else {
      paramInt = 0;
    }
    return paramInt;
  }
  
  protected abstract Cursor getChildrenCursor(Cursor paramCursor);
  
  MyCursorHelper getChildrenCursorHelper(int paramInt, boolean paramBoolean)
  {
    try
    {
      Object localObject1 = (MyCursorHelper)mChildrenCursorHelpers.get(paramInt);
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = mGroupCursorHelper.moveTo(paramInt);
        if (localObject2 == null) {
          return null;
        }
        localObject1 = getChildrenCursor(mGroupCursorHelper.getCursor());
        localObject2 = new android/widget/CursorTreeAdapter$MyCursorHelper;
        ((MyCursorHelper)localObject2).<init>(this, (Cursor)localObject1);
        mChildrenCursorHelpers.put(paramInt, localObject2);
      }
      return localObject2;
    }
    finally {}
  }
  
  public Cursor getCursor()
  {
    return mGroupCursorHelper.getCursor();
  }
  
  public Filter getFilter()
  {
    if (mCursorFilter == null) {
      mCursorFilter = new CursorFilter(this);
    }
    return mCursorFilter;
  }
  
  public FilterQueryProvider getFilterQueryProvider()
  {
    return mFilterQueryProvider;
  }
  
  public Cursor getGroup(int paramInt)
  {
    return mGroupCursorHelper.moveTo(paramInt);
  }
  
  public int getGroupCount()
  {
    return mGroupCursorHelper.getCount();
  }
  
  public long getGroupId(int paramInt)
  {
    return mGroupCursorHelper.getId(paramInt);
  }
  
  public View getGroupView(int paramInt, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
  {
    Cursor localCursor = mGroupCursorHelper.moveTo(paramInt);
    if (localCursor != null)
    {
      if (paramView == null) {
        paramView = newGroupView(mContext, localCursor, paramBoolean, paramViewGroup);
      }
      bindGroupView(paramView, mContext, localCursor, paramBoolean);
      return paramView;
    }
    throw new IllegalStateException("this should only be called when the cursor is valid");
  }
  
  public boolean hasStableIds()
  {
    return true;
  }
  
  public boolean isChildSelectable(int paramInt1, int paramInt2)
  {
    return true;
  }
  
  protected abstract View newChildView(Context paramContext, Cursor paramCursor, boolean paramBoolean, ViewGroup paramViewGroup);
  
  protected abstract View newGroupView(Context paramContext, Cursor paramCursor, boolean paramBoolean, ViewGroup paramViewGroup);
  
  public void notifyDataSetChanged()
  {
    notifyDataSetChanged(true);
  }
  
  public void notifyDataSetChanged(boolean paramBoolean)
  {
    if (paramBoolean) {
      releaseCursorHelpers();
    }
    super.notifyDataSetChanged();
  }
  
  public void notifyDataSetInvalidated()
  {
    releaseCursorHelpers();
    super.notifyDataSetInvalidated();
  }
  
  public void onGroupCollapsed(int paramInt)
  {
    deactivateChildrenCursorHelper(paramInt);
  }
  
  public Cursor runQueryOnBackgroundThread(CharSequence paramCharSequence)
  {
    if (mFilterQueryProvider != null) {
      return mFilterQueryProvider.runQuery(paramCharSequence);
    }
    return mGroupCursorHelper.getCursor();
  }
  
  public void setChildrenCursor(int paramInt, Cursor paramCursor)
  {
    getChildrenCursorHelper(paramInt, false).changeCursor(paramCursor, false);
  }
  
  public void setFilterQueryProvider(FilterQueryProvider paramFilterQueryProvider)
  {
    mFilterQueryProvider = paramFilterQueryProvider;
  }
  
  public void setGroupCursor(Cursor paramCursor)
  {
    mGroupCursorHelper.changeCursor(paramCursor, false);
  }
  
  class MyCursorHelper
  {
    private MyContentObserver mContentObserver;
    private Cursor mCursor;
    private MyDataSetObserver mDataSetObserver;
    private boolean mDataValid;
    private int mRowIDColumn;
    
    MyCursorHelper(Cursor paramCursor)
    {
      boolean bool;
      if (paramCursor != null) {
        bool = true;
      } else {
        bool = false;
      }
      mCursor = paramCursor;
      mDataValid = bool;
      int i;
      if (bool) {
        i = paramCursor.getColumnIndex("_id");
      } else {
        i = -1;
      }
      mRowIDColumn = i;
      mContentObserver = new MyContentObserver();
      mDataSetObserver = new MyDataSetObserver(null);
      if (bool)
      {
        paramCursor.registerContentObserver(mContentObserver);
        paramCursor.registerDataSetObserver(mDataSetObserver);
      }
    }
    
    void changeCursor(Cursor paramCursor, boolean paramBoolean)
    {
      if (paramCursor == mCursor) {
        return;
      }
      deactivate();
      mCursor = paramCursor;
      if (paramCursor != null)
      {
        paramCursor.registerContentObserver(mContentObserver);
        paramCursor.registerDataSetObserver(mDataSetObserver);
        mRowIDColumn = paramCursor.getColumnIndex("_id");
        mDataValid = true;
        notifyDataSetChanged(paramBoolean);
      }
      else
      {
        mRowIDColumn = -1;
        mDataValid = false;
        notifyDataSetInvalidated();
      }
    }
    
    void deactivate()
    {
      if (mCursor == null) {
        return;
      }
      mCursor.unregisterContentObserver(mContentObserver);
      mCursor.unregisterDataSetObserver(mDataSetObserver);
      mCursor.close();
      mCursor = null;
    }
    
    int getCount()
    {
      if ((mDataValid) && (mCursor != null)) {
        return mCursor.getCount();
      }
      return 0;
    }
    
    Cursor getCursor()
    {
      return mCursor;
    }
    
    long getId(int paramInt)
    {
      if ((mDataValid) && (mCursor != null))
      {
        if (mCursor.moveToPosition(paramInt)) {
          return mCursor.getLong(mRowIDColumn);
        }
        return 0L;
      }
      return 0L;
    }
    
    boolean isValid()
    {
      boolean bool;
      if ((mDataValid) && (mCursor != null)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    Cursor moveTo(int paramInt)
    {
      if ((mDataValid) && (mCursor != null) && (mCursor.moveToPosition(paramInt))) {
        return mCursor;
      }
      return null;
    }
    
    private class MyContentObserver
      extends ContentObserver
    {
      public MyContentObserver()
      {
        super();
      }
      
      public boolean deliverSelfNotifications()
      {
        return true;
      }
      
      public void onChange(boolean paramBoolean)
      {
        if ((mAutoRequery) && (mCursor != null) && (!mCursor.isClosed())) {
          CursorTreeAdapter.MyCursorHelper.access$402(CursorTreeAdapter.MyCursorHelper.this, mCursor.requery());
        }
      }
    }
    
    private class MyDataSetObserver
      extends DataSetObserver
    {
      private MyDataSetObserver() {}
      
      public void onChanged()
      {
        CursorTreeAdapter.MyCursorHelper.access$402(CursorTreeAdapter.MyCursorHelper.this, true);
        notifyDataSetChanged();
      }
      
      public void onInvalidated()
      {
        CursorTreeAdapter.MyCursorHelper.access$402(CursorTreeAdapter.MyCursorHelper.this, false);
        notifyDataSetInvalidated();
      }
    }
  }
}
