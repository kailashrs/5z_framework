package com.android.internal.widget;

import android.util.ArrayMap;
import android.util.LongSparseArray;
import android.util.Pools.Pool;
import android.util.Pools.SimplePool;
import com.android.internal.annotations.VisibleForTesting;

class ViewInfoStore
{
  private static final boolean DEBUG = false;
  @VisibleForTesting
  final ArrayMap<RecyclerView.ViewHolder, InfoRecord> mLayoutHolderMap = new ArrayMap();
  @VisibleForTesting
  final LongSparseArray<RecyclerView.ViewHolder> mOldChangedHolders = new LongSparseArray();
  
  ViewInfoStore() {}
  
  private RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(RecyclerView.ViewHolder paramViewHolder, int paramInt)
  {
    int i = mLayoutHolderMap.indexOfKey(paramViewHolder);
    if (i < 0) {
      return null;
    }
    InfoRecord localInfoRecord = (InfoRecord)mLayoutHolderMap.valueAt(i);
    if ((localInfoRecord != null) && ((flags & paramInt) != 0))
    {
      flags &= paramInt;
      if (paramInt == 4) {}
      for (paramViewHolder = preInfo;; paramViewHolder = postInfo)
      {
        break;
        if (paramInt != 8) {
          break label111;
        }
      }
      if ((flags & 0xC) == 0)
      {
        mLayoutHolderMap.removeAt(i);
        InfoRecord.recycle(localInfoRecord);
      }
      return paramViewHolder;
      label111:
      throw new IllegalArgumentException("Must provide flag PRE or POST");
    }
    return null;
  }
  
  void addToAppearedInPreLayoutHolders(RecyclerView.ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo)
  {
    InfoRecord localInfoRecord1 = (InfoRecord)mLayoutHolderMap.get(paramViewHolder);
    InfoRecord localInfoRecord2 = localInfoRecord1;
    if (localInfoRecord1 == null)
    {
      localInfoRecord2 = InfoRecord.obtain();
      mLayoutHolderMap.put(paramViewHolder, localInfoRecord2);
    }
    flags |= 0x2;
    preInfo = paramItemHolderInfo;
  }
  
  void addToDisappearedInLayout(RecyclerView.ViewHolder paramViewHolder)
  {
    InfoRecord localInfoRecord1 = (InfoRecord)mLayoutHolderMap.get(paramViewHolder);
    InfoRecord localInfoRecord2 = localInfoRecord1;
    if (localInfoRecord1 == null)
    {
      localInfoRecord2 = InfoRecord.obtain();
      mLayoutHolderMap.put(paramViewHolder, localInfoRecord2);
    }
    flags |= 0x1;
  }
  
  void addToOldChangeHolders(long paramLong, RecyclerView.ViewHolder paramViewHolder)
  {
    mOldChangedHolders.put(paramLong, paramViewHolder);
  }
  
  void addToPostLayout(RecyclerView.ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo)
  {
    InfoRecord localInfoRecord1 = (InfoRecord)mLayoutHolderMap.get(paramViewHolder);
    InfoRecord localInfoRecord2 = localInfoRecord1;
    if (localInfoRecord1 == null)
    {
      localInfoRecord2 = InfoRecord.obtain();
      mLayoutHolderMap.put(paramViewHolder, localInfoRecord2);
    }
    postInfo = paramItemHolderInfo;
    flags |= 0x8;
  }
  
  void addToPreLayout(RecyclerView.ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo)
  {
    InfoRecord localInfoRecord1 = (InfoRecord)mLayoutHolderMap.get(paramViewHolder);
    InfoRecord localInfoRecord2 = localInfoRecord1;
    if (localInfoRecord1 == null)
    {
      localInfoRecord2 = InfoRecord.obtain();
      mLayoutHolderMap.put(paramViewHolder, localInfoRecord2);
    }
    preInfo = paramItemHolderInfo;
    flags |= 0x4;
  }
  
  void clear()
  {
    mLayoutHolderMap.clear();
    mOldChangedHolders.clear();
  }
  
  RecyclerView.ViewHolder getFromOldChangeHolders(long paramLong)
  {
    return (RecyclerView.ViewHolder)mOldChangedHolders.get(paramLong);
  }
  
  boolean isDisappearing(RecyclerView.ViewHolder paramViewHolder)
  {
    paramViewHolder = (InfoRecord)mLayoutHolderMap.get(paramViewHolder);
    boolean bool = true;
    if ((paramViewHolder == null) || ((flags & 0x1) == 0)) {
      bool = false;
    }
    return bool;
  }
  
  boolean isInPreLayout(RecyclerView.ViewHolder paramViewHolder)
  {
    paramViewHolder = (InfoRecord)mLayoutHolderMap.get(paramViewHolder);
    boolean bool;
    if ((paramViewHolder != null) && ((flags & 0x4) != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void onDetach() {}
  
  public void onViewDetached(RecyclerView.ViewHolder paramViewHolder)
  {
    removeFromDisappearedInLayout(paramViewHolder);
  }
  
  RecyclerView.ItemAnimator.ItemHolderInfo popFromPostLayout(RecyclerView.ViewHolder paramViewHolder)
  {
    return popFromLayoutStep(paramViewHolder, 8);
  }
  
  RecyclerView.ItemAnimator.ItemHolderInfo popFromPreLayout(RecyclerView.ViewHolder paramViewHolder)
  {
    return popFromLayoutStep(paramViewHolder, 4);
  }
  
  void process(ProcessCallback paramProcessCallback)
  {
    for (int i = mLayoutHolderMap.size() - 1; i >= 0; i--)
    {
      RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)mLayoutHolderMap.keyAt(i);
      InfoRecord localInfoRecord = (InfoRecord)mLayoutHolderMap.removeAt(i);
      if ((flags & 0x3) == 3) {
        paramProcessCallback.unused(localViewHolder);
      } else if ((flags & 0x1) != 0)
      {
        if (preInfo == null) {
          paramProcessCallback.unused(localViewHolder);
        } else {
          paramProcessCallback.processDisappeared(localViewHolder, preInfo, postInfo);
        }
      }
      else if ((flags & 0xE) == 14) {
        paramProcessCallback.processAppeared(localViewHolder, preInfo, postInfo);
      } else if ((flags & 0xC) == 12) {
        paramProcessCallback.processPersistent(localViewHolder, preInfo, postInfo);
      } else if ((flags & 0x4) != 0) {
        paramProcessCallback.processDisappeared(localViewHolder, preInfo, null);
      } else if ((flags & 0x8) != 0) {
        paramProcessCallback.processAppeared(localViewHolder, preInfo, postInfo);
      } else {
        int j = flags;
      }
      InfoRecord.recycle(localInfoRecord);
    }
  }
  
  void removeFromDisappearedInLayout(RecyclerView.ViewHolder paramViewHolder)
  {
    paramViewHolder = (InfoRecord)mLayoutHolderMap.get(paramViewHolder);
    if (paramViewHolder == null) {
      return;
    }
    flags &= 0xFFFFFFFE;
  }
  
  void removeViewHolder(RecyclerView.ViewHolder paramViewHolder)
  {
    for (int i = mOldChangedHolders.size() - 1; i >= 0; i--) {
      if (paramViewHolder == mOldChangedHolders.valueAt(i))
      {
        mOldChangedHolders.removeAt(i);
        break;
      }
    }
    paramViewHolder = (InfoRecord)mLayoutHolderMap.remove(paramViewHolder);
    if (paramViewHolder != null) {
      InfoRecord.recycle(paramViewHolder);
    }
  }
  
  static class InfoRecord
  {
    static final int FLAG_APPEAR = 2;
    static final int FLAG_APPEAR_AND_DISAPPEAR = 3;
    static final int FLAG_APPEAR_PRE_AND_POST = 14;
    static final int FLAG_DISAPPEARED = 1;
    static final int FLAG_POST = 8;
    static final int FLAG_PRE = 4;
    static final int FLAG_PRE_AND_POST = 12;
    static Pools.Pool<InfoRecord> sPool = new Pools.SimplePool(20);
    int flags;
    RecyclerView.ItemAnimator.ItemHolderInfo postInfo;
    RecyclerView.ItemAnimator.ItemHolderInfo preInfo;
    
    private InfoRecord() {}
    
    static void drainCache()
    {
      while (sPool.acquire() != null) {}
    }
    
    static InfoRecord obtain()
    {
      InfoRecord localInfoRecord = (InfoRecord)sPool.acquire();
      if (localInfoRecord == null) {
        localInfoRecord = new InfoRecord();
      }
      return localInfoRecord;
    }
    
    static void recycle(InfoRecord paramInfoRecord)
    {
      flags = 0;
      preInfo = null;
      postInfo = null;
      sPool.release(paramInfoRecord);
    }
  }
  
  static abstract interface ProcessCallback
  {
    public abstract void processAppeared(RecyclerView.ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo2);
    
    public abstract void processDisappeared(RecyclerView.ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo2);
    
    public abstract void processPersistent(RecyclerView.ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo2);
    
    public abstract void unused(RecyclerView.ViewHolder paramViewHolder);
  }
}
