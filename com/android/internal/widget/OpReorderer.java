package com.android.internal.widget;

import java.util.List;

class OpReorderer
{
  final Callback mCallback;
  
  OpReorderer(Callback paramCallback)
  {
    mCallback = paramCallback;
  }
  
  private int getLastMoveOutOfOrder(List<AdapterHelper.UpdateOp> paramList)
  {
    int i = 0;
    int j = paramList.size() - 1;
    while (j >= 0)
    {
      int k;
      if (getcmd == 8)
      {
        k = i;
        if (i != 0) {
          return j;
        }
      }
      else
      {
        k = 1;
      }
      j--;
      i = k;
    }
    return -1;
  }
  
  private void swapMoveAdd(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2)
  {
    int i = 0;
    if (itemCount < positionStart) {
      i = 0 - 1;
    }
    int j = i;
    if (positionStart < positionStart) {
      j = i + 1;
    }
    if (positionStart <= positionStart) {
      positionStart += itemCount;
    }
    if (positionStart <= itemCount) {
      itemCount += itemCount;
    }
    positionStart += j;
    paramList.set(paramInt1, paramUpdateOp2);
    paramList.set(paramInt2, paramUpdateOp1);
  }
  
  private void swapMoveOp(List<AdapterHelper.UpdateOp> paramList, int paramInt1, int paramInt2)
  {
    AdapterHelper.UpdateOp localUpdateOp1 = (AdapterHelper.UpdateOp)paramList.get(paramInt1);
    AdapterHelper.UpdateOp localUpdateOp2 = (AdapterHelper.UpdateOp)paramList.get(paramInt2);
    int i = cmd;
    if (i != 4) {
      switch (i)
      {
      default: 
        break;
      case 2: 
        swapMoveRemove(paramList, paramInt1, localUpdateOp1, paramInt2, localUpdateOp2);
        break;
      case 1: 
        swapMoveAdd(paramList, paramInt1, localUpdateOp1, paramInt2, localUpdateOp2);
        break;
      }
    } else {
      swapMoveUpdate(paramList, paramInt1, localUpdateOp1, paramInt2, localUpdateOp2);
    }
  }
  
  void reorderOps(List<AdapterHelper.UpdateOp> paramList)
  {
    for (;;)
    {
      int i = getLastMoveOutOfOrder(paramList);
      if (i == -1) {
        break;
      }
      swapMoveOp(paramList, i, i + 1);
    }
  }
  
  void swapMoveRemove(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2)
  {
    AdapterHelper.UpdateOp localUpdateOp = null;
    int i = 0;
    int j;
    int k;
    int m;
    if (positionStart < itemCount)
    {
      j = 0;
      k = i;
      m = j;
      if (positionStart == positionStart)
      {
        k = i;
        m = j;
        if (itemCount == itemCount - positionStart)
        {
          k = 1;
          m = j;
        }
      }
    }
    else
    {
      j = 1;
      k = i;
      m = j;
      if (positionStart == itemCount + 1)
      {
        k = i;
        m = j;
        if (itemCount == positionStart - itemCount)
        {
          k = 1;
          m = j;
        }
      }
    }
    if (itemCount < positionStart)
    {
      positionStart -= 1;
    }
    else if (itemCount < positionStart + itemCount)
    {
      itemCount -= 1;
      cmd = 2;
      itemCount = 1;
      if (itemCount == 0)
      {
        paramList.remove(paramInt2);
        mCallback.recycleUpdateOp(paramUpdateOp2);
      }
      return;
    }
    if (positionStart <= positionStart)
    {
      positionStart += 1;
    }
    else if (positionStart < positionStart + itemCount)
    {
      j = positionStart;
      i = itemCount;
      int n = positionStart;
      localUpdateOp = mCallback.obtainUpdateOp(2, positionStart + 1, j + i - n, null);
      itemCount = (positionStart - positionStart);
    }
    if (k != 0)
    {
      paramList.set(paramInt1, paramUpdateOp2);
      paramList.remove(paramInt2);
      mCallback.recycleUpdateOp(paramUpdateOp1);
      return;
    }
    if (m != 0)
    {
      if (localUpdateOp != null)
      {
        if (positionStart > positionStart) {
          positionStart -= itemCount;
        }
        if (itemCount > positionStart) {
          itemCount -= itemCount;
        }
      }
      if (positionStart > positionStart) {
        positionStart -= itemCount;
      }
      if (itemCount > positionStart) {
        itemCount -= itemCount;
      }
    }
    else
    {
      if (localUpdateOp != null)
      {
        if (positionStart >= positionStart) {
          positionStart -= itemCount;
        }
        if (itemCount >= positionStart) {
          itemCount -= itemCount;
        }
      }
      if (positionStart >= positionStart) {
        positionStart -= itemCount;
      }
      if (itemCount >= positionStart) {
        itemCount -= itemCount;
      }
    }
    paramList.set(paramInt1, paramUpdateOp2);
    if (positionStart != itemCount) {
      paramList.set(paramInt2, paramUpdateOp1);
    } else {
      paramList.remove(paramInt2);
    }
    if (localUpdateOp != null) {
      paramList.add(paramInt1, localUpdateOp);
    }
  }
  
  void swapMoveUpdate(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2)
  {
    AdapterHelper.UpdateOp localUpdateOp1 = null;
    AdapterHelper.UpdateOp localUpdateOp2 = null;
    if (itemCount < positionStart)
    {
      positionStart -= 1;
    }
    else if (itemCount < positionStart + itemCount)
    {
      itemCount -= 1;
      localUpdateOp1 = mCallback.obtainUpdateOp(4, positionStart, 1, payload);
    }
    if (positionStart <= positionStart)
    {
      positionStart += 1;
    }
    else if (positionStart < positionStart + itemCount)
    {
      int i = positionStart + itemCount - positionStart;
      localUpdateOp2 = mCallback.obtainUpdateOp(4, positionStart + 1, i, payload);
      itemCount -= i;
    }
    paramList.set(paramInt2, paramUpdateOp1);
    if (itemCount > 0)
    {
      paramList.set(paramInt1, paramUpdateOp2);
    }
    else
    {
      paramList.remove(paramInt1);
      mCallback.recycleUpdateOp(paramUpdateOp2);
    }
    if (localUpdateOp1 != null) {
      paramList.add(paramInt1, localUpdateOp1);
    }
    if (localUpdateOp2 != null) {
      paramList.add(paramInt1, localUpdateOp2);
    }
  }
  
  static abstract interface Callback
  {
    public abstract AdapterHelper.UpdateOp obtainUpdateOp(int paramInt1, int paramInt2, int paramInt3, Object paramObject);
    
    public abstract void recycleUpdateOp(AdapterHelper.UpdateOp paramUpdateOp);
  }
}
