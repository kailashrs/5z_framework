package com.android.internal.widget;

import android.util.Pools.Pool;
import android.util.Pools.SimplePool;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AdapterHelper
  implements OpReorderer.Callback
{
  private static final boolean DEBUG = false;
  static final int POSITION_TYPE_INVISIBLE = 0;
  static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
  private static final String TAG = "AHT";
  final Callback mCallback;
  final boolean mDisableRecycler;
  private int mExistingUpdateTypes = 0;
  Runnable mOnItemProcessedCallback;
  final OpReorderer mOpReorderer;
  final ArrayList<UpdateOp> mPendingUpdates = new ArrayList();
  final ArrayList<UpdateOp> mPostponedList = new ArrayList();
  private Pools.Pool<UpdateOp> mUpdateOpPool = new Pools.SimplePool(30);
  
  AdapterHelper(Callback paramCallback)
  {
    this(paramCallback, false);
  }
  
  AdapterHelper(Callback paramCallback, boolean paramBoolean)
  {
    mCallback = paramCallback;
    mDisableRecycler = paramBoolean;
    mOpReorderer = new OpReorderer(this);
  }
  
  private void applyAdd(UpdateOp paramUpdateOp)
  {
    postponeAndUpdateViewHolders(paramUpdateOp);
  }
  
  private void applyMove(UpdateOp paramUpdateOp)
  {
    postponeAndUpdateViewHolders(paramUpdateOp);
  }
  
  private void applyRemove(UpdateOp paramUpdateOp)
  {
    int i = positionStart;
    int j = 0;
    int k = positionStart + itemCount;
    int m = -1;
    int i2;
    for (int n = positionStart; n < k; n = i2)
    {
      int i1 = 0;
      i2 = 0;
      if ((mCallback.findViewHolder(n) == null) && (!canFindInPreLayout(n)))
      {
        if (m == 1)
        {
          postponeAndUpdateViewHolders(obtainUpdateOp(2, i, j, null));
          i2 = 1;
        }
        i1 = 0;
      }
      else
      {
        if (m == 0)
        {
          dispatchAndUpdateViewHolders(obtainUpdateOp(2, i, j, null));
          i1 = 1;
        }
        m = 1;
        i2 = i1;
        i1 = m;
      }
      if (i2 != 0)
      {
        i2 = n - j;
        k -= j;
        n = 1;
      }
      else
      {
        j++;
        i2 = n;
        n = j;
      }
      i2++;
      j = n;
      m = i1;
    }
    UpdateOp localUpdateOp = paramUpdateOp;
    if (j != itemCount)
    {
      recycleUpdateOp(paramUpdateOp);
      localUpdateOp = obtainUpdateOp(2, i, j, null);
    }
    if (m == 0) {
      dispatchAndUpdateViewHolders(localUpdateOp);
    } else {
      postponeAndUpdateViewHolders(localUpdateOp);
    }
  }
  
  private void applyUpdate(UpdateOp paramUpdateOp)
  {
    int i = positionStart;
    int j = 0;
    int k = positionStart;
    int m = itemCount;
    int n = -1;
    int i1 = positionStart;
    while (i1 < k + m)
    {
      int i2;
      int i3;
      if ((mCallback.findViewHolder(i1) == null) && (!canFindInPreLayout(i1)))
      {
        i2 = i;
        i3 = j;
        if (n == 1)
        {
          postponeAndUpdateViewHolders(obtainUpdateOp(4, i, j, payload));
          i3 = 0;
          i2 = i1;
        }
        j = 0;
        i = i2;
        i2 = i3;
        i3 = j;
      }
      else
      {
        int i4 = i;
        i2 = j;
        if (n == 0)
        {
          dispatchAndUpdateViewHolders(obtainUpdateOp(4, i, j, payload));
          i2 = 0;
          i4 = i1;
        }
        i3 = 1;
        i = i4;
      }
      j = i2 + 1;
      i1++;
      n = i3;
    }
    Object localObject = paramUpdateOp;
    if (j != itemCount)
    {
      localObject = payload;
      recycleUpdateOp(paramUpdateOp);
      localObject = obtainUpdateOp(4, i, j, localObject);
    }
    if (n == 0) {
      dispatchAndUpdateViewHolders((UpdateOp)localObject);
    } else {
      postponeAndUpdateViewHolders((UpdateOp)localObject);
    }
  }
  
  private boolean canFindInPreLayout(int paramInt)
  {
    int i = mPostponedList.size();
    for (int j = 0; j < i; j++)
    {
      UpdateOp localUpdateOp = (UpdateOp)mPostponedList.get(j);
      if (cmd == 8)
      {
        if (findPositionOffset(itemCount, j + 1) == paramInt) {
          return true;
        }
      }
      else if (cmd == 1)
      {
        int k = positionStart;
        int m = itemCount;
        for (int n = positionStart; n < k + m; n++) {
          if (findPositionOffset(n, j + 1) == paramInt) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  private void dispatchAndUpdateViewHolders(UpdateOp paramUpdateOp)
  {
    if ((cmd != 1) && (cmd != 8))
    {
      int i = updatePositionWithPostponed(positionStart, cmd);
      int j = 1;
      int k = positionStart;
      int m = cmd;
      int n;
      if (m != 2)
      {
        if (m == 4)
        {
          n = 1;
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("op should be remove or update.");
          ((StringBuilder)localObject).append(paramUpdateOp);
          throw new IllegalArgumentException(((StringBuilder)localObject).toString());
        }
      }
      else {
        n = 0;
      }
      int i1 = 1;
      while (i1 < itemCount)
      {
        int i2 = updatePositionWithPostponed(positionStart + n * i1, cmd);
        int i3 = 0;
        int i4 = cmd;
        int i5 = 0;
        m = 0;
        if (i4 != 2)
        {
          if (i4 != 4) {
            m = i3;
          } else if (i2 == i + 1) {
            m = 1;
          }
        }
        else
        {
          m = i5;
          if (i2 == i) {
            m = 1;
          }
        }
        if (m != 0)
        {
          m = j + 1;
        }
        else
        {
          localObject = obtainUpdateOp(cmd, i, j, payload);
          dispatchFirstPassAndUpdateViewHolders((UpdateOp)localObject, k);
          recycleUpdateOp((UpdateOp)localObject);
          m = k;
          if (cmd == 4) {
            m = k + j;
          }
          i = i2;
          j = 1;
          k = m;
          m = j;
        }
        i1++;
        j = m;
      }
      Object localObject = payload;
      recycleUpdateOp(paramUpdateOp);
      if (j > 0)
      {
        paramUpdateOp = obtainUpdateOp(cmd, i, j, localObject);
        dispatchFirstPassAndUpdateViewHolders(paramUpdateOp, k);
        recycleUpdateOp(paramUpdateOp);
      }
      return;
    }
    throw new IllegalArgumentException("should not dispatch add or move for pre layout");
  }
  
  private void postponeAndUpdateViewHolders(UpdateOp paramUpdateOp)
  {
    mPostponedList.add(paramUpdateOp);
    int i = cmd;
    if (i != 4)
    {
      if (i != 8) {
        switch (i)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown update op type for ");
          localStringBuilder.append(paramUpdateOp);
          throw new IllegalArgumentException(localStringBuilder.toString());
        case 2: 
          mCallback.offsetPositionsForRemovingLaidOutOrNewView(positionStart, itemCount);
          break;
        case 1: 
          mCallback.offsetPositionsForAdd(positionStart, itemCount);
          break;
        }
      } else {
        mCallback.offsetPositionsForMove(positionStart, itemCount);
      }
    }
    else {
      mCallback.markViewHoldersUpdated(positionStart, itemCount, payload);
    }
  }
  
  private int updatePositionWithPostponed(int paramInt1, int paramInt2)
  {
    int i = mPostponedList.size() - 1;
    UpdateOp localUpdateOp;
    for (int j = paramInt1; i >= 0; j = paramInt1)
    {
      localUpdateOp = (UpdateOp)mPostponedList.get(i);
      if (cmd == 8)
      {
        int k;
        if (positionStart < itemCount)
        {
          paramInt1 = positionStart;
          k = itemCount;
        }
        else
        {
          paramInt1 = itemCount;
          k = positionStart;
        }
        if ((j >= paramInt1) && (j <= k))
        {
          if (paramInt1 == positionStart)
          {
            if (paramInt2 == 1) {
              itemCount += 1;
            } else if (paramInt2 == 2) {
              itemCount -= 1;
            }
            paramInt1 = j + 1;
          }
          else
          {
            if (paramInt2 == 1) {
              positionStart += 1;
            } else if (paramInt2 == 2) {
              positionStart -= 1;
            }
            paramInt1 = j - 1;
          }
        }
        else
        {
          paramInt1 = j;
          if (j < positionStart) {
            if (paramInt2 == 1)
            {
              positionStart += 1;
              itemCount += 1;
              paramInt1 = j;
            }
            else
            {
              paramInt1 = j;
              if (paramInt2 == 2)
              {
                positionStart -= 1;
                itemCount -= 1;
                paramInt1 = j;
              }
            }
          }
        }
      }
      else if (positionStart <= j)
      {
        if (cmd == 1)
        {
          paramInt1 = j - itemCount;
        }
        else
        {
          paramInt1 = j;
          if (cmd == 2) {
            paramInt1 = j + itemCount;
          }
        }
      }
      else if (paramInt2 == 1)
      {
        positionStart += 1;
        paramInt1 = j;
      }
      else
      {
        paramInt1 = j;
        if (paramInt2 == 2)
        {
          positionStart -= 1;
          paramInt1 = j;
        }
      }
      i--;
    }
    for (paramInt1 = mPostponedList.size() - 1; paramInt1 >= 0; paramInt1--)
    {
      localUpdateOp = (UpdateOp)mPostponedList.get(paramInt1);
      if (cmd == 8)
      {
        if ((itemCount == positionStart) || (itemCount < 0))
        {
          mPostponedList.remove(paramInt1);
          recycleUpdateOp(localUpdateOp);
        }
      }
      else if (itemCount <= 0)
      {
        mPostponedList.remove(paramInt1);
        recycleUpdateOp(localUpdateOp);
      }
    }
    return j;
  }
  
  AdapterHelper addUpdateOp(UpdateOp... paramVarArgs)
  {
    Collections.addAll(mPendingUpdates, paramVarArgs);
    return this;
  }
  
  public int applyPendingUpdatesToPosition(int paramInt)
  {
    int i = mPendingUpdates.size();
    int j = 0;
    for (int k = paramInt; j < i; k = paramInt)
    {
      UpdateOp localUpdateOp = (UpdateOp)mPendingUpdates.get(j);
      paramInt = cmd;
      if (paramInt != 8)
      {
        switch (paramInt)
        {
        default: 
          paramInt = k;
          break;
        case 2: 
          paramInt = k;
          if (positionStart > k) {
            break;
          }
          if (positionStart + itemCount > k) {
            return -1;
          }
          paramInt = k - itemCount;
          break;
        case 1: 
          paramInt = k;
          if (positionStart > k) {
            break;
          }
          paramInt = k + itemCount;
          break;
        }
      }
      else if (positionStart == k)
      {
        paramInt = itemCount;
      }
      else
      {
        int m = k;
        if (positionStart < k) {
          m = k - 1;
        }
        paramInt = m;
        if (itemCount <= m) {
          paramInt = m + 1;
        }
      }
      j++;
    }
    return k;
  }
  
  void consumePostponedUpdates()
  {
    int i = mPostponedList.size();
    for (int j = 0; j < i; j++) {
      mCallback.onDispatchSecondPass((UpdateOp)mPostponedList.get(j));
    }
    recycleUpdateOpsAndClearList(mPostponedList);
    mExistingUpdateTypes = 0;
  }
  
  void consumeUpdatesInOnePass()
  {
    consumePostponedUpdates();
    int i = mPendingUpdates.size();
    for (int j = 0; j < i; j++)
    {
      UpdateOp localUpdateOp = (UpdateOp)mPendingUpdates.get(j);
      int k = cmd;
      if (k != 4)
      {
        if (k != 8)
        {
          switch (k)
          {
          default: 
            break;
          case 2: 
            mCallback.onDispatchSecondPass(localUpdateOp);
            mCallback.offsetPositionsForRemovingInvisible(positionStart, itemCount);
            break;
          case 1: 
            mCallback.onDispatchSecondPass(localUpdateOp);
            mCallback.offsetPositionsForAdd(positionStart, itemCount);
            break;
          }
        }
        else
        {
          mCallback.onDispatchSecondPass(localUpdateOp);
          mCallback.offsetPositionsForMove(positionStart, itemCount);
        }
      }
      else
      {
        mCallback.onDispatchSecondPass(localUpdateOp);
        mCallback.markViewHoldersUpdated(positionStart, itemCount, payload);
      }
      if (mOnItemProcessedCallback != null) {
        mOnItemProcessedCallback.run();
      }
    }
    recycleUpdateOpsAndClearList(mPendingUpdates);
    mExistingUpdateTypes = 0;
  }
  
  void dispatchFirstPassAndUpdateViewHolders(UpdateOp paramUpdateOp, int paramInt)
  {
    mCallback.onDispatchFirstPass(paramUpdateOp);
    int i = cmd;
    if (i != 2)
    {
      if (i == 4) {
        mCallback.markViewHoldersUpdated(paramInt, itemCount, payload);
      } else {
        throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
      }
    }
    else {
      mCallback.offsetPositionsForRemovingInvisible(paramInt, itemCount);
    }
  }
  
  int findPositionOffset(int paramInt)
  {
    return findPositionOffset(paramInt, 0);
  }
  
  int findPositionOffset(int paramInt1, int paramInt2)
  {
    int i = mPostponedList.size();
    int j = paramInt2;
    for (paramInt2 = paramInt1; j < i; paramInt2 = paramInt1)
    {
      UpdateOp localUpdateOp = (UpdateOp)mPostponedList.get(j);
      if (cmd == 8)
      {
        if (positionStart == paramInt2)
        {
          paramInt1 = itemCount;
        }
        else
        {
          int k = paramInt2;
          if (positionStart < paramInt2) {
            k = paramInt2 - 1;
          }
          paramInt1 = k;
          if (itemCount <= k) {
            paramInt1 = k + 1;
          }
        }
      }
      else
      {
        paramInt1 = paramInt2;
        if (positionStart <= paramInt2) {
          if (cmd == 2)
          {
            if (paramInt2 < positionStart + itemCount) {
              return -1;
            }
            paramInt1 = paramInt2 - itemCount;
          }
          else
          {
            paramInt1 = paramInt2;
            if (cmd == 1) {
              paramInt1 = paramInt2 + itemCount;
            }
          }
        }
      }
      j++;
    }
    return paramInt2;
  }
  
  boolean hasAnyUpdateTypes(int paramInt)
  {
    boolean bool;
    if ((mExistingUpdateTypes & paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean hasPendingUpdates()
  {
    boolean bool;
    if (mPendingUpdates.size() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean hasUpdates()
  {
    boolean bool;
    if ((!mPostponedList.isEmpty()) && (!mPendingUpdates.isEmpty())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public UpdateOp obtainUpdateOp(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    UpdateOp localUpdateOp = (UpdateOp)mUpdateOpPool.acquire();
    if (localUpdateOp == null)
    {
      paramObject = new UpdateOp(paramInt1, paramInt2, paramInt3, paramObject);
    }
    else
    {
      cmd = paramInt1;
      positionStart = paramInt2;
      itemCount = paramInt3;
      payload = paramObject;
      paramObject = localUpdateOp;
    }
    return paramObject;
  }
  
  boolean onItemRangeChanged(int paramInt1, int paramInt2, Object paramObject)
  {
    boolean bool = false;
    if (paramInt2 < 1) {
      return false;
    }
    mPendingUpdates.add(obtainUpdateOp(4, paramInt1, paramInt2, paramObject));
    mExistingUpdateTypes |= 0x4;
    if (mPendingUpdates.size() == 1) {
      bool = true;
    }
    return bool;
  }
  
  boolean onItemRangeInserted(int paramInt1, int paramInt2)
  {
    boolean bool = false;
    if (paramInt2 < 1) {
      return false;
    }
    mPendingUpdates.add(obtainUpdateOp(1, paramInt1, paramInt2, null));
    mExistingUpdateTypes |= 0x1;
    if (mPendingUpdates.size() == 1) {
      bool = true;
    }
    return bool;
  }
  
  boolean onItemRangeMoved(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool = false;
    if (paramInt1 == paramInt2) {
      return false;
    }
    if (paramInt3 == 1)
    {
      mPendingUpdates.add(obtainUpdateOp(8, paramInt1, paramInt2, null));
      mExistingUpdateTypes |= 0x8;
      if (mPendingUpdates.size() == 1) {
        bool = true;
      }
      return bool;
    }
    throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
  }
  
  boolean onItemRangeRemoved(int paramInt1, int paramInt2)
  {
    boolean bool = false;
    if (paramInt2 < 1) {
      return false;
    }
    mPendingUpdates.add(obtainUpdateOp(2, paramInt1, paramInt2, null));
    mExistingUpdateTypes |= 0x2;
    if (mPendingUpdates.size() == 1) {
      bool = true;
    }
    return bool;
  }
  
  void preProcess()
  {
    mOpReorderer.reorderOps(mPendingUpdates);
    int i = mPendingUpdates.size();
    for (int j = 0; j < i; j++)
    {
      UpdateOp localUpdateOp = (UpdateOp)mPendingUpdates.get(j);
      int k = cmd;
      if (k != 4)
      {
        if (k != 8) {
          switch (k)
          {
          default: 
            break;
          case 2: 
            applyRemove(localUpdateOp);
            break;
          case 1: 
            applyAdd(localUpdateOp);
            break;
          }
        } else {
          applyMove(localUpdateOp);
        }
      }
      else {
        applyUpdate(localUpdateOp);
      }
      if (mOnItemProcessedCallback != null) {
        mOnItemProcessedCallback.run();
      }
    }
    mPendingUpdates.clear();
  }
  
  public void recycleUpdateOp(UpdateOp paramUpdateOp)
  {
    if (!mDisableRecycler)
    {
      payload = null;
      mUpdateOpPool.release(paramUpdateOp);
    }
  }
  
  void recycleUpdateOpsAndClearList(List<UpdateOp> paramList)
  {
    int i = paramList.size();
    for (int j = 0; j < i; j++) {
      recycleUpdateOp((UpdateOp)paramList.get(j));
    }
    paramList.clear();
  }
  
  void reset()
  {
    recycleUpdateOpsAndClearList(mPendingUpdates);
    recycleUpdateOpsAndClearList(mPostponedList);
    mExistingUpdateTypes = 0;
  }
  
  static abstract interface Callback
  {
    public abstract RecyclerView.ViewHolder findViewHolder(int paramInt);
    
    public abstract void markViewHoldersUpdated(int paramInt1, int paramInt2, Object paramObject);
    
    public abstract void offsetPositionsForAdd(int paramInt1, int paramInt2);
    
    public abstract void offsetPositionsForMove(int paramInt1, int paramInt2);
    
    public abstract void offsetPositionsForRemovingInvisible(int paramInt1, int paramInt2);
    
    public abstract void offsetPositionsForRemovingLaidOutOrNewView(int paramInt1, int paramInt2);
    
    public abstract void onDispatchFirstPass(AdapterHelper.UpdateOp paramUpdateOp);
    
    public abstract void onDispatchSecondPass(AdapterHelper.UpdateOp paramUpdateOp);
  }
  
  static class UpdateOp
  {
    static final int ADD = 1;
    static final int MOVE = 8;
    static final int POOL_SIZE = 30;
    static final int REMOVE = 2;
    static final int UPDATE = 4;
    int cmd;
    int itemCount;
    Object payload;
    int positionStart;
    
    UpdateOp(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
    {
      cmd = paramInt1;
      positionStart = paramInt2;
      itemCount = paramInt3;
      payload = paramObject;
    }
    
    String cmdToString()
    {
      int i = cmd;
      if (i != 4)
      {
        if (i != 8)
        {
          switch (i)
          {
          default: 
            return "??";
          case 2: 
            return "rm";
          }
          return "add";
        }
        return "mv";
      }
      return "up";
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()))
      {
        paramObject = (UpdateOp)paramObject;
        if (cmd != cmd) {
          return false;
        }
        if ((cmd == 8) && (Math.abs(itemCount - positionStart) == 1) && (itemCount == positionStart) && (positionStart == itemCount)) {
          return true;
        }
        if (itemCount != itemCount) {
          return false;
        }
        if (positionStart != positionStart) {
          return false;
        }
        if (payload != null)
        {
          if (!payload.equals(payload)) {
            return false;
          }
        }
        else if (payload != null) {
          return false;
        }
        return true;
      }
      return false;
    }
    
    public int hashCode()
    {
      return 31 * (31 * cmd + positionStart) + itemCount;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append("[");
      localStringBuilder.append(cmdToString());
      localStringBuilder.append(",s:");
      localStringBuilder.append(positionStart);
      localStringBuilder.append("c:");
      localStringBuilder.append(itemCount);
      localStringBuilder.append(",p:");
      localStringBuilder.append(payload);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
  }
}
