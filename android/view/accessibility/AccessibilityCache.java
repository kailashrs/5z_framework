package android.view.accessibility;

import android.os.Build;
import android.util.ArraySet;
import android.util.Log;
import android.util.LongArray;
import android.util.LongSparseArray;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityCache
{
  public static final int CACHE_CRITICAL_EVENTS_MASK = 4307005;
  private static final boolean CHECK_INTEGRITY = Build.IS_ENG;
  private static final boolean DEBUG = false;
  private static final String LOG_TAG = "AccessibilityCache";
  private long mAccessibilityFocus = 2147483647L;
  private final AccessibilityNodeRefresher mAccessibilityNodeRefresher;
  private long mInputFocus = 2147483647L;
  private boolean mIsAllWindowsCached;
  private final Object mLock = new Object();
  private final SparseArray<LongSparseArray<AccessibilityNodeInfo>> mNodeCache = new SparseArray();
  private final SparseArray<AccessibilityWindowInfo> mTempWindowArray = new SparseArray();
  private final SparseArray<AccessibilityWindowInfo> mWindowCache = new SparseArray();
  
  public AccessibilityCache(AccessibilityNodeRefresher paramAccessibilityNodeRefresher)
  {
    mAccessibilityNodeRefresher = paramAccessibilityNodeRefresher;
  }
  
  private void clearNodesForWindowLocked(int paramInt)
  {
    LongSparseArray localLongSparseArray = (LongSparseArray)mNodeCache.get(paramInt);
    if (localLongSparseArray == null) {
      return;
    }
    for (int i = localLongSparseArray.size() - 1; i >= 0; i--)
    {
      AccessibilityNodeInfo localAccessibilityNodeInfo = (AccessibilityNodeInfo)localLongSparseArray.valueAt(i);
      localLongSparseArray.removeAt(i);
      localAccessibilityNodeInfo.recycle();
    }
    mNodeCache.remove(paramInt);
  }
  
  private void clearSubTreeLocked(int paramInt, long paramLong)
  {
    LongSparseArray localLongSparseArray = (LongSparseArray)mNodeCache.get(paramInt);
    if (localLongSparseArray != null) {
      clearSubTreeRecursiveLocked(localLongSparseArray, paramLong);
    }
  }
  
  private void clearSubTreeRecursiveLocked(LongSparseArray<AccessibilityNodeInfo> paramLongSparseArray, long paramLong)
  {
    AccessibilityNodeInfo localAccessibilityNodeInfo = (AccessibilityNodeInfo)paramLongSparseArray.get(paramLong);
    if (localAccessibilityNodeInfo == null) {
      return;
    }
    paramLongSparseArray.remove(paramLong);
    int i = localAccessibilityNodeInfo.getChildCount();
    for (int j = 0; j < i; j++) {
      clearSubTreeRecursiveLocked(paramLongSparseArray, localAccessibilityNodeInfo.getChildId(j));
    }
    localAccessibilityNodeInfo.recycle();
  }
  
  private void clearWindowCache()
  {
    for (int i = mWindowCache.size() - 1; i >= 0; i--)
    {
      ((AccessibilityWindowInfo)mWindowCache.valueAt(i)).recycle();
      mWindowCache.removeAt(i);
    }
    mIsAllWindowsCached = false;
  }
  
  private void refreshCachedNodeLocked(int paramInt, long paramLong)
  {
    Object localObject = (LongSparseArray)mNodeCache.get(paramInt);
    if (localObject == null) {
      return;
    }
    localObject = (AccessibilityNodeInfo)((LongSparseArray)localObject).get(paramLong);
    if (localObject == null) {
      return;
    }
    if (mAccessibilityNodeRefresher.refreshNode((AccessibilityNodeInfo)localObject, true)) {
      return;
    }
    clearSubTreeLocked(paramInt, paramLong);
  }
  
  public void add(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    synchronized (mLock)
    {
      int i = paramAccessibilityNodeInfo.getWindowId();
      Object localObject2 = (LongSparseArray)mNodeCache.get(i);
      Object localObject3 = localObject2;
      if (localObject2 == null)
      {
        localObject3 = new android/util/LongSparseArray;
        ((LongSparseArray)localObject3).<init>();
        mNodeCache.put(i, localObject3);
      }
      long l1 = paramAccessibilityNodeInfo.getSourceNodeId();
      localObject2 = (AccessibilityNodeInfo)((LongSparseArray)localObject3).get(l1);
      if (localObject2 != null)
      {
        LongArray localLongArray = paramAccessibilityNodeInfo.getChildNodeIds();
        int j = ((AccessibilityNodeInfo)localObject2).getChildCount();
        for (int k = 0; k < j; k++)
        {
          if (((LongSparseArray)localObject3).get(l1) == null)
          {
            clearNodesForWindowLocked(i);
            return;
          }
          l2 = ((AccessibilityNodeInfo)localObject2).getChildId(k);
          if ((localLongArray == null) || (localLongArray.indexOf(l2) < 0)) {
            clearSubTreeLocked(i, l2);
          }
        }
        long l2 = ((AccessibilityNodeInfo)localObject2).getParentNodeId();
        if (paramAccessibilityNodeInfo.getParentNodeId() != l2) {
          clearSubTreeLocked(i, l2);
        } else {
          ((AccessibilityNodeInfo)localObject2).recycle();
        }
      }
      paramAccessibilityNodeInfo = AccessibilityNodeInfo.obtain(paramAccessibilityNodeInfo);
      ((LongSparseArray)localObject3).put(l1, paramAccessibilityNodeInfo);
      if (paramAccessibilityNodeInfo.isAccessibilityFocused()) {
        mAccessibilityFocus = l1;
      }
      if (paramAccessibilityNodeInfo.isFocused()) {
        mInputFocus = l1;
      }
      return;
    }
  }
  
  public void addWindow(AccessibilityWindowInfo paramAccessibilityWindowInfo)
  {
    synchronized (mLock)
    {
      int i = paramAccessibilityWindowInfo.getId();
      AccessibilityWindowInfo localAccessibilityWindowInfo = (AccessibilityWindowInfo)mWindowCache.get(i);
      if (localAccessibilityWindowInfo != null) {
        localAccessibilityWindowInfo.recycle();
      }
      mWindowCache.put(i, AccessibilityWindowInfo.obtain(paramAccessibilityWindowInfo));
      return;
    }
  }
  
  public void checkIntegrity()
  {
    Object localObject1 = this;
    synchronized (mLock)
    {
      if ((mWindowCache.size() <= 0) && (mNodeCache.size() == 0)) {
        return;
      }
      int i = mWindowCache.size();
      Object localObject3 = null;
      Object localObject5 = null;
      int j = 0;
      while (j < i)
      {
        localObject6 = (AccessibilityWindowInfo)mWindowCache.valueAt(j);
        localObject7 = localObject3;
        if (((AccessibilityWindowInfo)localObject6).isActive()) {
          if (localObject3 != null)
          {
            localObject7 = new java/lang/StringBuilder;
            ((StringBuilder)localObject7).<init>();
            ((StringBuilder)localObject7).append("Duplicate active window:");
            ((StringBuilder)localObject7).append(localObject6);
            Log.e("AccessibilityCache", ((StringBuilder)localObject7).toString());
            localObject7 = localObject3;
          }
          else
          {
            localObject7 = localObject6;
          }
        }
        localObject3 = localObject5;
        if (((AccessibilityWindowInfo)localObject6).isFocused()) {
          if (localObject5 != null)
          {
            localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append("Duplicate focused window:");
            ((StringBuilder)localObject3).append(localObject6);
            Log.e("AccessibilityCache", ((StringBuilder)localObject3).toString());
            localObject3 = localObject5;
          }
          else
          {
            localObject3 = localObject6;
          }
        }
        j++;
        localObject5 = localObject3;
        localObject3 = localObject7;
      }
      j = mNodeCache.size();
      Object localObject6 = null;
      Object localObject7 = null;
      int k = 0;
      for (;;)
      {
        localObject1 = this;
        if (k >= j) {
          break;
        }
        LongSparseArray localLongSparseArray = (LongSparseArray)mNodeCache.valueAt(k);
        if (localLongSparseArray.size() <= 0)
        {
          localObject1 = localObject6;
          localObject6 = localObject5;
        }
        else
        {
          ArraySet localArraySet = new android/util/ArraySet;
          localArraySet.<init>();
          int m = mNodeCache.keyAt(k);
          int n = localLongSparseArray.size();
          int i1 = 0;
          while (i1 < n)
          {
            AccessibilityNodeInfo localAccessibilityNodeInfo1 = (AccessibilityNodeInfo)localLongSparseArray.valueAt(i1);
            if (!localArraySet.add(localAccessibilityNodeInfo1))
            {
              localObject1 = new java/lang/StringBuilder;
              ((StringBuilder)localObject1).<init>();
              ((StringBuilder)localObject1).append("Duplicate node: ");
              ((StringBuilder)localObject1).append(localAccessibilityNodeInfo1);
              ((StringBuilder)localObject1).append(" in window:");
              ((StringBuilder)localObject1).append(m);
              Log.e("AccessibilityCache", ((StringBuilder)localObject1).toString());
              localObject1 = localObject6;
            }
            else
            {
              localObject1 = localObject7;
              if (localAccessibilityNodeInfo1.isAccessibilityFocused()) {
                if (localObject7 != null)
                {
                  localObject1 = new java/lang/StringBuilder;
                  ((StringBuilder)localObject1).<init>();
                  ((StringBuilder)localObject1).append("Duplicate accessibility focus:");
                  ((StringBuilder)localObject1).append(localAccessibilityNodeInfo1);
                  ((StringBuilder)localObject1).append(" in window:");
                  ((StringBuilder)localObject1).append(m);
                  Log.e("AccessibilityCache", ((StringBuilder)localObject1).toString());
                  localObject1 = localObject7;
                }
                else
                {
                  localObject1 = localAccessibilityNodeInfo1;
                }
              }
              localObject7 = localObject6;
              if (localAccessibilityNodeInfo1.isFocused()) {
                if (localObject6 != null)
                {
                  localObject7 = new java/lang/StringBuilder;
                  ((StringBuilder)localObject7).<init>();
                  ((StringBuilder)localObject7).append("Duplicate input focus: ");
                  ((StringBuilder)localObject7).append(localAccessibilityNodeInfo1);
                  ((StringBuilder)localObject7).append(" in window:");
                  ((StringBuilder)localObject7).append(m);
                  Log.e("AccessibilityCache", ((StringBuilder)localObject7).toString());
                  localObject7 = localObject6;
                }
                else
                {
                  localObject7 = localAccessibilityNodeInfo1;
                }
              }
              AccessibilityNodeInfo localAccessibilityNodeInfo2 = (AccessibilityNodeInfo)localLongSparseArray.get(localAccessibilityNodeInfo1.getParentNodeId());
              if (localAccessibilityNodeInfo2 != null)
              {
                i2 = 0;
                i3 = localAccessibilityNodeInfo2.getChildCount();
                for (int i4 = 0; i4 < i3; i4++) {
                  if ((AccessibilityNodeInfo)localLongSparseArray.get(localAccessibilityNodeInfo2.getChildId(i4)) == localAccessibilityNodeInfo1)
                  {
                    i2 = 1;
                    break;
                  }
                }
                if (i2 == 0)
                {
                  localObject6 = new java/lang/StringBuilder;
                  ((StringBuilder)localObject6).<init>();
                  ((StringBuilder)localObject6).append("Invalid parent-child relation between parent: ");
                  ((StringBuilder)localObject6).append(localAccessibilityNodeInfo2);
                  ((StringBuilder)localObject6).append(" and child: ");
                  ((StringBuilder)localObject6).append(localAccessibilityNodeInfo1);
                  Log.e("AccessibilityCache", ((StringBuilder)localObject6).toString());
                }
              }
              int i2 = localAccessibilityNodeInfo1.getChildCount();
              int i3 = 0;
              localObject6 = localObject1;
              while (i3 < i2)
              {
                localObject1 = (AccessibilityNodeInfo)localLongSparseArray.get(localAccessibilityNodeInfo1.getChildId(i3));
                if (localObject1 != null) {
                  if ((AccessibilityNodeInfo)localLongSparseArray.get(((AccessibilityNodeInfo)localObject1).getParentNodeId()) != localAccessibilityNodeInfo1)
                  {
                    localObject1 = new java/lang/StringBuilder;
                    ((StringBuilder)localObject1).<init>();
                    ((StringBuilder)localObject1).append("Invalid child-parent relation between child: ");
                    ((StringBuilder)localObject1).append(localAccessibilityNodeInfo1);
                    ((StringBuilder)localObject1).append(" and parent: ");
                    ((StringBuilder)localObject1).append(localAccessibilityNodeInfo2);
                    Log.e("AccessibilityCache", ((StringBuilder)localObject1).toString());
                  }
                  else {}
                }
                i3++;
              }
              localObject1 = localObject7;
              localObject7 = localObject6;
            }
            i1++;
            localObject6 = localObject1;
          }
          localObject1 = localObject6;
          localObject6 = localObject5;
        }
        k++;
        localObject5 = localObject6;
        localObject6 = localObject1;
      }
      return;
    }
  }
  
  public void clear()
  {
    synchronized (mLock)
    {
      clearWindowCache();
      int i = mNodeCache.size();
      for (int j = 0; j < i; j++) {
        clearNodesForWindowLocked(mNodeCache.keyAt(j));
      }
      mAccessibilityFocus = 2147483647L;
      mInputFocus = 2147483647L;
      return;
    }
  }
  
  public AccessibilityNodeInfo getNode(int paramInt, long paramLong)
  {
    synchronized (mLock)
    {
      Object localObject2 = (LongSparseArray)mNodeCache.get(paramInt);
      if (localObject2 == null) {
        return null;
      }
      AccessibilityNodeInfo localAccessibilityNodeInfo = (AccessibilityNodeInfo)((LongSparseArray)localObject2).get(paramLong);
      localObject2 = localAccessibilityNodeInfo;
      if (localAccessibilityNodeInfo != null) {
        localObject2 = AccessibilityNodeInfo.obtain(localAccessibilityNodeInfo);
      }
      return localObject2;
    }
  }
  
  public AccessibilityWindowInfo getWindow(int paramInt)
  {
    synchronized (mLock)
    {
      AccessibilityWindowInfo localAccessibilityWindowInfo = (AccessibilityWindowInfo)mWindowCache.get(paramInt);
      if (localAccessibilityWindowInfo != null)
      {
        localAccessibilityWindowInfo = AccessibilityWindowInfo.obtain(localAccessibilityWindowInfo);
        return localAccessibilityWindowInfo;
      }
      return null;
    }
  }
  
  public List<AccessibilityWindowInfo> getWindows()
  {
    synchronized (mLock)
    {
      if (!mIsAllWindowsCached) {
        return null;
      }
      int i = mWindowCache.size();
      if (i > 0)
      {
        SparseArray localSparseArray = mTempWindowArray;
        localSparseArray.clear();
        for (int j = 0; j < i; j++)
        {
          localObject3 = (AccessibilityWindowInfo)mWindowCache.valueAt(j);
          localSparseArray.put(((AccessibilityWindowInfo)localObject3).getLayer(), localObject3);
        }
        j = localSparseArray.size();
        Object localObject3 = new java/util/ArrayList;
        ((ArrayList)localObject3).<init>(j);
        j--;
        while (j >= 0)
        {
          ((List)localObject3).add(AccessibilityWindowInfo.obtain((AccessibilityWindowInfo)localSparseArray.valueAt(j)));
          localSparseArray.removeAt(j);
          j--;
        }
        return localObject3;
      }
      return null;
    }
  }
  
  public void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    synchronized (mLock)
    {
      switch (paramAccessibilityEvent.getEventType())
      {
      default: 
        break;
      case 65536: 
        if (mAccessibilityFocus == paramAccessibilityEvent.getSourceNodeId())
        {
          refreshCachedNodeLocked(paramAccessibilityEvent.getWindowId(), mAccessibilityFocus);
          mAccessibilityFocus = 2147483647L;
        }
        break;
      case 32768: 
        if (mAccessibilityFocus != 2147483647L) {
          refreshCachedNodeLocked(paramAccessibilityEvent.getWindowId(), mAccessibilityFocus);
        }
        mAccessibilityFocus = paramAccessibilityEvent.getSourceNodeId();
        refreshCachedNodeLocked(paramAccessibilityEvent.getWindowId(), mAccessibilityFocus);
        break;
      case 4096: 
        clearSubTreeLocked(paramAccessibilityEvent.getWindowId(), paramAccessibilityEvent.getSourceNodeId());
        break;
      case 2048: 
        synchronized (mLock)
        {
          int i = paramAccessibilityEvent.getWindowId();
          long l = paramAccessibilityEvent.getSourceNodeId();
          if ((paramAccessibilityEvent.getContentChangeTypes() & 0x1) != 0) {
            clearSubTreeLocked(i, l);
          } else {
            refreshCachedNodeLocked(i, l);
          }
        }
      case 32: 
      case 4194304: 
        clear();
        break;
      case 8: 
        if (mInputFocus != 2147483647L) {
          refreshCachedNodeLocked(paramAccessibilityEvent.getWindowId(), mInputFocus);
        }
        mInputFocus = paramAccessibilityEvent.getSourceNodeId();
        refreshCachedNodeLocked(paramAccessibilityEvent.getWindowId(), mInputFocus);
        break;
      case 1: 
      case 4: 
      case 16: 
      case 8192: 
        refreshCachedNodeLocked(paramAccessibilityEvent.getWindowId(), paramAccessibilityEvent.getSourceNodeId());
      }
      if (CHECK_INTEGRITY) {
        checkIntegrity();
      }
      return;
    }
  }
  
  public void setWindows(List<AccessibilityWindowInfo> paramList)
  {
    synchronized (mLock)
    {
      clearWindowCache();
      if (paramList == null) {
        return;
      }
      int i = paramList.size();
      for (int j = 0; j < i; j++) {
        addWindow((AccessibilityWindowInfo)paramList.get(j));
      }
      mIsAllWindowsCached = true;
      return;
    }
  }
  
  public static class AccessibilityNodeRefresher
  {
    public AccessibilityNodeRefresher() {}
    
    public boolean refreshNode(AccessibilityNodeInfo paramAccessibilityNodeInfo, boolean paramBoolean)
    {
      return paramAccessibilityNodeInfo.refresh(null, paramBoolean);
    }
  }
}
