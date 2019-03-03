package com.android.internal.widget;

import android.os.Trace;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

final class GapWorker
  implements Runnable
{
  static final ThreadLocal<GapWorker> sGapWorker = new ThreadLocal();
  static Comparator<Task> sTaskComparator = new Comparator()
  {
    public int compare(GapWorker.Task paramAnonymousTask1, GapWorker.Task paramAnonymousTask2)
    {
      RecyclerView localRecyclerView = view;
      int i = 1;
      int j = 1;
      if (localRecyclerView == null) {
        k = 1;
      } else {
        k = 0;
      }
      int m;
      if (view == null) {
        m = 1;
      } else {
        m = 0;
      }
      if (k != m)
      {
        if (view == null) {
          k = j;
        } else {
          k = -1;
        }
        return k;
      }
      if (immediate != immediate)
      {
        k = i;
        if (immediate) {
          k = -1;
        }
        return k;
      }
      int k = viewVelocity - viewVelocity;
      if (k != 0) {
        return k;
      }
      k = distanceToItem - distanceToItem;
      if (k != 0) {
        return k;
      }
      return 0;
    }
  };
  long mFrameIntervalNs;
  long mPostTimeNs;
  ArrayList<RecyclerView> mRecyclerViews = new ArrayList();
  private ArrayList<Task> mTasks = new ArrayList();
  
  GapWorker() {}
  
  private void buildTaskList()
  {
    int i = mRecyclerViews.size();
    int j = 0;
    Object localObject;
    for (int k = 0; k < i; k++)
    {
      localObject = (RecyclerView)mRecyclerViews.get(k);
      mPrefetchRegistry.collectPrefetchPositionsFromView((RecyclerView)localObject, false);
      j += mPrefetchRegistry.mCount;
    }
    mTasks.ensureCapacity(j);
    k = 0;
    for (j = 0; j < i; j++)
    {
      RecyclerView localRecyclerView = (RecyclerView)mRecyclerViews.get(j);
      LayoutPrefetchRegistryImpl localLayoutPrefetchRegistryImpl = mPrefetchRegistry;
      int m = Math.abs(mPrefetchDx) + Math.abs(mPrefetchDy);
      for (int n = 0; n < mCount * 2; n += 2)
      {
        if (k >= mTasks.size())
        {
          localObject = new Task();
          mTasks.add(localObject);
        }
        else
        {
          localObject = (Task)mTasks.get(k);
        }
        int i1 = mPrefetchArray[(n + 1)];
        boolean bool;
        if (i1 <= m) {
          bool = true;
        } else {
          bool = false;
        }
        immediate = bool;
        viewVelocity = m;
        distanceToItem = i1;
        view = localRecyclerView;
        position = mPrefetchArray[n];
        k++;
      }
    }
    Collections.sort(mTasks, sTaskComparator);
  }
  
  private void flushTaskWithDeadline(Task paramTask, long paramLong)
  {
    long l;
    if (immediate) {
      l = Long.MAX_VALUE;
    } else {
      l = paramLong;
    }
    paramTask = prefetchPositionWithDeadline(view, position, l);
    if ((paramTask != null) && (mNestedRecyclerView != null)) {
      prefetchInnerRecyclerViewWithDeadline((RecyclerView)mNestedRecyclerView.get(), paramLong);
    }
  }
  
  private void flushTasksWithDeadline(long paramLong)
  {
    for (int i = 0; i < mTasks.size(); i++)
    {
      Task localTask = (Task)mTasks.get(i);
      if (view == null) {
        break;
      }
      flushTaskWithDeadline(localTask, paramLong);
      localTask.clear();
    }
  }
  
  static boolean isPrefetchPositionAttached(RecyclerView paramRecyclerView, int paramInt)
  {
    int i = mChildHelper.getUnfilteredChildCount();
    for (int j = 0; j < i; j++)
    {
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(j));
      if ((mPosition == paramInt) && (!localViewHolder.isInvalid())) {
        return true;
      }
    }
    return false;
  }
  
  private void prefetchInnerRecyclerViewWithDeadline(RecyclerView paramRecyclerView, long paramLong)
  {
    if (paramRecyclerView == null) {
      return;
    }
    if ((mDataSetHasChangedAfterLayout) && (mChildHelper.getUnfilteredChildCount() != 0)) {
      paramRecyclerView.removeAndRecycleViews();
    }
    LayoutPrefetchRegistryImpl localLayoutPrefetchRegistryImpl = mPrefetchRegistry;
    localLayoutPrefetchRegistryImpl.collectPrefetchPositionsFromView(paramRecyclerView, true);
    if (mCount != 0) {
      try
      {
        Trace.beginSection("RV Nested Prefetch");
        mState.prepareForNestedPrefetch(mAdapter);
        for (int i = 0; i < mCount * 2; i += 2) {
          prefetchPositionWithDeadline(paramRecyclerView, mPrefetchArray[i], paramLong);
        }
      }
      finally
      {
        Trace.endSection();
      }
    }
  }
  
  private RecyclerView.ViewHolder prefetchPositionWithDeadline(RecyclerView paramRecyclerView, int paramInt, long paramLong)
  {
    if (isPrefetchPositionAttached(paramRecyclerView, paramInt)) {
      return null;
    }
    paramRecyclerView = mRecycler;
    RecyclerView.ViewHolder localViewHolder = paramRecyclerView.tryGetViewHolderForPositionByDeadline(paramInt, false, paramLong);
    if (localViewHolder != null) {
      if (localViewHolder.isBound()) {
        paramRecyclerView.recycleView(itemView);
      } else {
        paramRecyclerView.addViewHolderToRecycledViewPool(localViewHolder, false);
      }
    }
    return localViewHolder;
  }
  
  public void add(RecyclerView paramRecyclerView)
  {
    mRecyclerViews.add(paramRecyclerView);
  }
  
  void postFromTraversal(RecyclerView paramRecyclerView, int paramInt1, int paramInt2)
  {
    if ((paramRecyclerView.isAttachedToWindow()) && (mPostTimeNs == 0L))
    {
      mPostTimeNs = paramRecyclerView.getNanoTime();
      paramRecyclerView.post(this);
    }
    mPrefetchRegistry.setPrefetchVector(paramInt1, paramInt2);
  }
  
  void prefetch(long paramLong)
  {
    buildTaskList();
    flushTasksWithDeadline(paramLong);
  }
  
  public void remove(RecyclerView paramRecyclerView)
  {
    mRecyclerViews.remove(paramRecyclerView);
  }
  
  public void run()
  {
    try
    {
      Trace.beginSection("RV Prefetch");
      boolean bool = mRecyclerViews.isEmpty();
      if (bool) {
        return;
      }
      long l = TimeUnit.MILLISECONDS.toNanos(((RecyclerView)mRecyclerViews.get(0)).getDrawingTime());
      if (l == 0L) {
        return;
      }
      prefetch(mFrameIntervalNs + l);
      return;
    }
    finally
    {
      mPostTimeNs = 0L;
      Trace.endSection();
    }
  }
  
  static class LayoutPrefetchRegistryImpl
    implements RecyclerView.LayoutManager.LayoutPrefetchRegistry
  {
    int mCount;
    int[] mPrefetchArray;
    int mPrefetchDx;
    int mPrefetchDy;
    
    LayoutPrefetchRegistryImpl() {}
    
    public void addPosition(int paramInt1, int paramInt2)
    {
      if (paramInt2 >= 0)
      {
        int i = mCount * 2;
        if (mPrefetchArray == null)
        {
          mPrefetchArray = new int[4];
          Arrays.fill(mPrefetchArray, -1);
        }
        else if (i >= mPrefetchArray.length)
        {
          int[] arrayOfInt = mPrefetchArray;
          mPrefetchArray = new int[i * 2];
          System.arraycopy(arrayOfInt, 0, mPrefetchArray, 0, arrayOfInt.length);
        }
        mPrefetchArray[i] = paramInt1;
        mPrefetchArray[(i + 1)] = paramInt2;
        mCount += 1;
        return;
      }
      throw new IllegalArgumentException("Pixel distance must be non-negative");
    }
    
    void clearPrefetchPositions()
    {
      if (mPrefetchArray != null) {
        Arrays.fill(mPrefetchArray, -1);
      }
    }
    
    void collectPrefetchPositionsFromView(RecyclerView paramRecyclerView, boolean paramBoolean)
    {
      mCount = 0;
      if (mPrefetchArray != null) {
        Arrays.fill(mPrefetchArray, -1);
      }
      RecyclerView.LayoutManager localLayoutManager = mLayout;
      if ((mAdapter != null) && (localLayoutManager != null) && (localLayoutManager.isItemPrefetchEnabled()))
      {
        if (paramBoolean)
        {
          if (!mAdapterHelper.hasPendingUpdates()) {
            localLayoutManager.collectInitialPrefetchPositions(mAdapter.getItemCount(), this);
          }
        }
        else if (!paramRecyclerView.hasPendingAdapterUpdates()) {
          localLayoutManager.collectAdjacentPrefetchPositions(mPrefetchDx, mPrefetchDy, mState, this);
        }
        if (mCount > mPrefetchMaxCountObserved)
        {
          mPrefetchMaxCountObserved = mCount;
          mPrefetchMaxObservedInInitialPrefetch = paramBoolean;
          mRecycler.updateViewCacheSize();
        }
      }
    }
    
    boolean lastPrefetchIncludedPosition(int paramInt)
    {
      if (mPrefetchArray != null)
      {
        int i = mCount;
        for (int j = 0; j < i * 2; j += 2) {
          if (mPrefetchArray[j] == paramInt) {
            return true;
          }
        }
      }
      return false;
    }
    
    void setPrefetchVector(int paramInt1, int paramInt2)
    {
      mPrefetchDx = paramInt1;
      mPrefetchDy = paramInt2;
    }
  }
  
  static class Task
  {
    public int distanceToItem;
    public boolean immediate;
    public int position;
    public RecyclerView view;
    public int viewVelocity;
    
    Task() {}
    
    public void clear()
    {
      immediate = false;
      viewVelocity = 0;
      distanceToItem = 0;
      view = null;
      position = 0;
    }
  }
}
