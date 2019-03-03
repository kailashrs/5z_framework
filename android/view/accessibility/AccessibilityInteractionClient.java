package android.view.accessibility;

import android.accessibilityservice.IAccessibilityServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public final class AccessibilityInteractionClient
  extends IAccessibilityInteractionConnectionCallback.Stub
{
  private static final boolean CHECK_INTEGRITY = true;
  private static final boolean DEBUG = false;
  private static final String LOG_TAG = "AccessibilityInteractionClient";
  public static final int NO_ID = -1;
  private static final long TIMEOUT_INTERACTION_MILLIS = 5000L;
  private static AccessibilityCache sAccessibilityCache = new AccessibilityCache(new AccessibilityCache.AccessibilityNodeRefresher());
  private static final LongSparseArray<AccessibilityInteractionClient> sClients;
  private static final SparseArray<IAccessibilityServiceConnection> sConnectionCache;
  private static final Object sStaticLock = new Object();
  private AccessibilityNodeInfo mFindAccessibilityNodeInfoResult;
  private List<AccessibilityNodeInfo> mFindAccessibilityNodeInfosResult;
  private final Object mInstanceLock = new Object();
  private volatile int mInteractionId = -1;
  private final AtomicInteger mInteractionIdCounter = new AtomicInteger();
  private boolean mPerformAccessibilityActionResult;
  private Message mSameThreadMessage;
  
  static
  {
    sClients = new LongSparseArray();
    sConnectionCache = new SparseArray();
  }
  
  private AccessibilityInteractionClient() {}
  
  public static void addConnection(int paramInt, IAccessibilityServiceConnection paramIAccessibilityServiceConnection)
  {
    synchronized (sConnectionCache)
    {
      sConnectionCache.put(paramInt, paramIAccessibilityServiceConnection);
      return;
    }
  }
  
  private void checkFindAccessibilityNodeInfoResultIntegrity(List<AccessibilityNodeInfo> paramList)
  {
    if (paramList.size() == 0) {
      return;
    }
    Object localObject1 = (AccessibilityNodeInfo)paramList.get(0);
    int i = paramList.size();
    int j = 1;
    int k;
    while (j < i)
    {
      for (k = j;; k++)
      {
        localObject2 = localObject1;
        if (k >= i) {
          break;
        }
        localObject2 = (AccessibilityNodeInfo)paramList.get(k);
        if (((AccessibilityNodeInfo)localObject1).getParentNodeId() == ((AccessibilityNodeInfo)localObject2).getSourceNodeId()) {
          break;
        }
      }
      j++;
      localObject1 = localObject2;
    }
    if (localObject1 == null) {
      Log.e("AccessibilityInteractionClient", "No root.");
    }
    Object localObject2 = new HashSet();
    LinkedList localLinkedList = new LinkedList();
    localLinkedList.add(localObject1);
    while (!localLinkedList.isEmpty())
    {
      localObject1 = (AccessibilityNodeInfo)localLinkedList.poll();
      if (!((HashSet)localObject2).add(localObject1))
      {
        Log.e("AccessibilityInteractionClient", "Duplicate node.");
        return;
      }
      int m = ((AccessibilityNodeInfo)localObject1).getChildCount();
      for (j = 0; j < m; j++)
      {
        long l = ((AccessibilityNodeInfo)localObject1).getChildId(j);
        for (k = 0; k < i; k++)
        {
          AccessibilityNodeInfo localAccessibilityNodeInfo = (AccessibilityNodeInfo)paramList.get(k);
          if (localAccessibilityNodeInfo.getSourceNodeId() == l) {
            localLinkedList.add(localAccessibilityNodeInfo);
          }
        }
      }
    }
    j = paramList.size() - ((HashSet)localObject2).size();
    if (j > 0)
    {
      paramList = new StringBuilder();
      paramList.append(j);
      paramList.append(" Disconnected nodes.");
      Log.e("AccessibilityInteractionClient", paramList.toString());
    }
  }
  
  private void clearResultLocked()
  {
    mInteractionId = -1;
    mFindAccessibilityNodeInfoResult = null;
    mFindAccessibilityNodeInfosResult = null;
    mPerformAccessibilityActionResult = false;
  }
  
  private void finalizeAndCacheAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo, int paramInt, boolean paramBoolean, String[] paramArrayOfString)
  {
    if (paramAccessibilityNodeInfo != null)
    {
      paramAccessibilityNodeInfo.setConnectionId(paramInt);
      if (!ArrayUtils.isEmpty(paramArrayOfString))
      {
        CharSequence localCharSequence = paramAccessibilityNodeInfo.getPackageName();
        if ((localCharSequence == null) || (!ArrayUtils.contains(paramArrayOfString, localCharSequence.toString()))) {
          paramAccessibilityNodeInfo.setPackageName(paramArrayOfString[0]);
        }
      }
      paramAccessibilityNodeInfo.setSealed(true);
      if (!paramBoolean) {
        sAccessibilityCache.add(paramAccessibilityNodeInfo);
      }
    }
  }
  
  private void finalizeAndCacheAccessibilityNodeInfos(List<AccessibilityNodeInfo> paramList, int paramInt, boolean paramBoolean, String[] paramArrayOfString)
  {
    if (paramList != null)
    {
      int i = paramList.size();
      for (int j = 0; j < i; j++) {
        finalizeAndCacheAccessibilityNodeInfo((AccessibilityNodeInfo)paramList.get(j), paramInt, paramBoolean, paramArrayOfString);
      }
    }
  }
  
  public static IAccessibilityServiceConnection getConnection(int paramInt)
  {
    synchronized (sConnectionCache)
    {
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = (IAccessibilityServiceConnection)sConnectionCache.get(paramInt);
      return localIAccessibilityServiceConnection;
    }
  }
  
  private AccessibilityNodeInfo getFindAccessibilityNodeInfoResultAndClear(int paramInt)
  {
    synchronized (mInstanceLock)
    {
      AccessibilityNodeInfo localAccessibilityNodeInfo;
      if (waitForResultTimedLocked(paramInt)) {
        localAccessibilityNodeInfo = mFindAccessibilityNodeInfoResult;
      } else {
        localAccessibilityNodeInfo = null;
      }
      clearResultLocked();
      return localAccessibilityNodeInfo;
    }
  }
  
  private List<AccessibilityNodeInfo> getFindAccessibilityNodeInfosResultAndClear(int paramInt)
  {
    synchronized (mInstanceLock)
    {
      List localList;
      if (waitForResultTimedLocked(paramInt)) {
        localList = mFindAccessibilityNodeInfosResult;
      } else {
        localList = Collections.emptyList();
      }
      clearResultLocked();
      if (Build.IS_DEBUGGABLE) {
        checkFindAccessibilityNodeInfoResultIntegrity(localList);
      }
      return localList;
    }
  }
  
  public static AccessibilityInteractionClient getInstance()
  {
    return getInstanceForThread(Thread.currentThread().getId());
  }
  
  public static AccessibilityInteractionClient getInstanceForThread(long paramLong)
  {
    synchronized (sStaticLock)
    {
      AccessibilityInteractionClient localAccessibilityInteractionClient1 = (AccessibilityInteractionClient)sClients.get(paramLong);
      AccessibilityInteractionClient localAccessibilityInteractionClient2 = localAccessibilityInteractionClient1;
      if (localAccessibilityInteractionClient1 == null)
      {
        localAccessibilityInteractionClient2 = new android/view/accessibility/AccessibilityInteractionClient;
        localAccessibilityInteractionClient2.<init>();
        sClients.put(paramLong, localAccessibilityInteractionClient2);
      }
      return localAccessibilityInteractionClient2;
    }
  }
  
  private boolean getPerformAccessibilityActionResultAndClear(int paramInt)
  {
    synchronized (mInstanceLock)
    {
      boolean bool;
      if (waitForResultTimedLocked(paramInt)) {
        bool = mPerformAccessibilityActionResult;
      } else {
        bool = false;
      }
      clearResultLocked();
      return bool;
    }
  }
  
  private Message getSameProcessMessageAndClear()
  {
    synchronized (mInstanceLock)
    {
      Message localMessage = mSameThreadMessage;
      mSameThreadMessage = null;
      return localMessage;
    }
  }
  
  private static String idToString(int paramInt, long paramLong)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramInt);
    localStringBuilder.append("/");
    localStringBuilder.append(AccessibilityNodeInfo.idToString(paramLong));
    return localStringBuilder.toString();
  }
  
  public static void removeConnection(int paramInt)
  {
    synchronized (sConnectionCache)
    {
      sConnectionCache.remove(paramInt);
      return;
    }
  }
  
  @VisibleForTesting
  public static void setCache(AccessibilityCache paramAccessibilityCache)
  {
    sAccessibilityCache = paramAccessibilityCache;
  }
  
  private boolean waitForResultTimedLocked(int paramInt)
  {
    long l1 = SystemClock.uptimeMillis();
    for (;;)
    {
      try
      {
        Message localMessage = getSameProcessMessageAndClear();
        if (localMessage != null) {
          localMessage.getTarget().handleMessage(localMessage);
        }
        if (mInteractionId == paramInt) {
          return true;
        }
        if (mInteractionId > paramInt) {
          return false;
        }
        long l2 = 5000L - (SystemClock.uptimeMillis() - l1);
        if (l2 <= 0L) {
          return false;
        }
        mInstanceLock.wait(l2);
      }
      catch (InterruptedException localInterruptedException) {}
    }
  }
  
  public void clearCache()
  {
    sAccessibilityCache.clear();
  }
  
  public AccessibilityNodeInfo findAccessibilityNodeInfoByAccessibilityId(int paramInt1, int paramInt2, long paramLong, boolean paramBoolean, int paramInt3, Bundle paramBundle)
  {
    if (((paramInt3 & 0x2) != 0) && ((paramInt3 & 0x1) == 0)) {
      throw new IllegalArgumentException("FLAG_PREFETCH_SIBLINGS requires FLAG_PREFETCH_PREDECESSORS");
    }
    try
    {
      Object localObject1 = getConnection(paramInt1);
      if (localObject1 != null)
      {
        if (!paramBoolean) {
          try
          {
            Object localObject2 = sAccessibilityCache;
            localObject2 = ((AccessibilityCache)localObject2).getNode(paramInt2, paramLong);
            if (localObject2 != null) {
              return localObject2;
            }
          }
          catch (RemoteException paramBundle) {}
        }
        int i = mInteractionIdCounter.getAndIncrement();
        long l1 = Binder.clearCallingIdentity();
        try
        {
          long l2 = Thread.currentThread().getId();
          try
          {
            localObject1 = ((IAccessibilityServiceConnection)localObject1).findAccessibilityNodeInfoByAccessibilityId(paramInt2, paramLong, i, this, paramInt3, l2, paramBundle);
            Binder.restoreCallingIdentity(l1);
            if (localObject1 != null)
            {
              paramBundle = getFindAccessibilityNodeInfosResultAndClear(i);
              paramBundle = finally;
            }
          }
          finally
          {
            try
            {
              finalizeAndCacheAccessibilityNodeInfos(paramBundle, paramInt1, paramBoolean, (String[])localObject1);
              if ((paramBundle == null) || (paramBundle.isEmpty())) {
                break label222;
              }
              for (paramInt1 = 1; paramInt1 < paramBundle.size(); paramInt1++) {
                ((AccessibilityNodeInfo)paramBundle.get(paramInt1)).recycle();
              }
              return (AccessibilityNodeInfo)paramBundle.get(0);
            }
            catch (RemoteException paramBundle) {}
            paramBundle = finally;
          }
        }
        finally
        {
          Binder.restoreCallingIdentity(l1);
          throw paramBundle;
        }
      }
      label222:
      return null;
    }
    catch (RemoteException paramBundle)
    {
      Log.e("AccessibilityInteractionClient", "Error while calling remote findAccessibilityNodeInfoByAccessibilityId", paramBundle);
    }
  }
  
  /* Error */
  public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(int paramInt1, int paramInt2, long paramLong, String paramString)
  {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic 324	android/view/accessibility/AccessibilityInteractionClient:getConnection	(I)Landroid/accessibilityservice/IAccessibilityServiceConnection;
    //   4: astore 6
    //   6: aload 6
    //   8: ifnull +90 -> 98
    //   11: aload_0
    //   12: getfield 78	android/view/accessibility/AccessibilityInteractionClient:mInteractionIdCounter	Ljava/util/concurrent/atomic/AtomicInteger;
    //   15: invokevirtual 331	java/util/concurrent/atomic/AtomicInteger:getAndIncrement	()I
    //   18: istore 7
    //   20: invokestatic 336	android/os/Binder:clearCallingIdentity	()J
    //   23: lstore 8
    //   25: aload 6
    //   27: iload_2
    //   28: lload_3
    //   29: aload 5
    //   31: iload 7
    //   33: aload_0
    //   34: invokestatic 248	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   37: invokevirtual 251	java/lang/Thread:getId	()J
    //   40: invokeinterface 360 9 0
    //   45: astore 5
    //   47: lload 8
    //   49: invokestatic 342	android/os/Binder:restoreCallingIdentity	(J)V
    //   52: aload 5
    //   54: ifnull +44 -> 98
    //   57: aload_0
    //   58: iload 7
    //   60: invokespecial 344	android/view/accessibility/AccessibilityInteractionClient:getFindAccessibilityNodeInfosResultAndClear	(I)Ljava/util/List;
    //   63: astore 6
    //   65: aload 6
    //   67: ifnull +31 -> 98
    //   70: aload_0
    //   71: aload 6
    //   73: iload_1
    //   74: iconst_0
    //   75: aload 5
    //   77: invokespecial 346	android/view/accessibility/AccessibilityInteractionClient:finalizeAndCacheAccessibilityNodeInfos	(Ljava/util/List;IZ[Ljava/lang/String;)V
    //   80: aload 6
    //   82: areturn
    //   83: astore 5
    //   85: lload 8
    //   87: invokestatic 342	android/os/Binder:restoreCallingIdentity	(J)V
    //   90: aload 5
    //   92: athrow
    //   93: astore 5
    //   95: goto +8 -> 103
    //   98: goto +16 -> 114
    //   101: astore 5
    //   103: ldc 13
    //   105: ldc_w 362
    //   108: aload 5
    //   110: invokestatic 365	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   113: pop
    //   114: invokestatic 232	java/util/Collections:emptyList	()Ljava/util/List;
    //   117: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	118	0	this	AccessibilityInteractionClient
    //   0	118	1	paramInt1	int
    //   0	118	2	paramInt2	int
    //   0	118	3	paramLong	long
    //   0	118	5	paramString	String
    //   4	77	6	localObject	Object
    //   18	41	7	i	int
    //   23	63	8	l	long
    // Exception table:
    //   from	to	target	type
    //   25	47	83	finally
    //   70	80	93	android/os/RemoteException
    //   85	93	93	android/os/RemoteException
    //   0	6	101	android/os/RemoteException
    //   11	25	101	android/os/RemoteException
    //   47	52	101	android/os/RemoteException
    //   57	65	101	android/os/RemoteException
  }
  
  /* Error */
  public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId(int paramInt1, int paramInt2, long paramLong, String paramString)
  {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic 324	android/view/accessibility/AccessibilityInteractionClient:getConnection	(I)Landroid/accessibilityservice/IAccessibilityServiceConnection;
    //   4: astore 6
    //   6: aload 6
    //   8: ifnull +90 -> 98
    //   11: aload_0
    //   12: getfield 78	android/view/accessibility/AccessibilityInteractionClient:mInteractionIdCounter	Ljava/util/concurrent/atomic/AtomicInteger;
    //   15: invokevirtual 331	java/util/concurrent/atomic/AtomicInteger:getAndIncrement	()I
    //   18: istore 7
    //   20: invokestatic 336	android/os/Binder:clearCallingIdentity	()J
    //   23: lstore 8
    //   25: aload 6
    //   27: iload_2
    //   28: lload_3
    //   29: aload 5
    //   31: iload 7
    //   33: aload_0
    //   34: invokestatic 248	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   37: invokevirtual 251	java/lang/Thread:getId	()J
    //   40: invokeinterface 369 9 0
    //   45: astore 5
    //   47: lload 8
    //   49: invokestatic 342	android/os/Binder:restoreCallingIdentity	(J)V
    //   52: aload 5
    //   54: ifnull +44 -> 98
    //   57: aload_0
    //   58: iload 7
    //   60: invokespecial 344	android/view/accessibility/AccessibilityInteractionClient:getFindAccessibilityNodeInfosResultAndClear	(I)Ljava/util/List;
    //   63: astore 6
    //   65: aload 6
    //   67: ifnull +31 -> 98
    //   70: aload_0
    //   71: aload 6
    //   73: iload_1
    //   74: iconst_0
    //   75: aload 5
    //   77: invokespecial 346	android/view/accessibility/AccessibilityInteractionClient:finalizeAndCacheAccessibilityNodeInfos	(Ljava/util/List;IZ[Ljava/lang/String;)V
    //   80: aload 6
    //   82: areturn
    //   83: astore 5
    //   85: lload 8
    //   87: invokestatic 342	android/os/Binder:restoreCallingIdentity	(J)V
    //   90: aload 5
    //   92: athrow
    //   93: astore 5
    //   95: goto +8 -> 103
    //   98: goto +16 -> 114
    //   101: astore 5
    //   103: ldc 13
    //   105: ldc_w 371
    //   108: aload 5
    //   110: invokestatic 365	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   113: pop
    //   114: invokestatic 232	java/util/Collections:emptyList	()Ljava/util/List;
    //   117: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	118	0	this	AccessibilityInteractionClient
    //   0	118	1	paramInt1	int
    //   0	118	2	paramInt2	int
    //   0	118	3	paramLong	long
    //   0	118	5	paramString	String
    //   4	77	6	localObject	Object
    //   18	41	7	i	int
    //   23	63	8	l	long
    // Exception table:
    //   from	to	target	type
    //   25	47	83	finally
    //   70	80	93	android/os/RemoteException
    //   85	93	93	android/os/RemoteException
    //   0	6	101	android/os/RemoteException
    //   11	25	101	android/os/RemoteException
    //   47	52	101	android/os/RemoteException
    //   57	65	101	android/os/RemoteException
  }
  
  public AccessibilityNodeInfo findFocus(int paramInt1, int paramInt2, long paramLong, int paramInt3)
  {
    try
    {
      Object localObject1 = getConnection(paramInt1);
      if (localObject1 != null)
      {
        int i = mInteractionIdCounter.getAndIncrement();
        long l = Binder.clearCallingIdentity();
        try
        {
          arrayOfString = ((IAccessibilityServiceConnection)localObject1).findFocus(paramInt2, paramLong, paramInt3, i, this, Thread.currentThread().getId());
          Binder.restoreCallingIdentity(l);
          if (arrayOfString != null) {
            localObject1 = getFindAccessibilityNodeInfoResultAndClear(i);
          }
        }
        finally
        {
          try
          {
            String[] arrayOfString;
            finalizeAndCacheAccessibilityNodeInfo((AccessibilityNodeInfo)localObject1, paramInt1, false, arrayOfString);
            return localObject1;
          }
          catch (RemoteException localRemoteException1) {}
          localObject2 = finally;
          Binder.restoreCallingIdentity(l);
        }
      }
      return null;
    }
    catch (RemoteException localRemoteException2)
    {
      Log.w("AccessibilityInteractionClient", "Error while calling remote findFocus", localRemoteException2);
    }
  }
  
  public AccessibilityNodeInfo focusSearch(int paramInt1, int paramInt2, long paramLong, int paramInt3)
  {
    try
    {
      Object localObject1 = getConnection(paramInt1);
      if (localObject1 != null)
      {
        int i = mInteractionIdCounter.getAndIncrement();
        long l = Binder.clearCallingIdentity();
        try
        {
          localObject1 = ((IAccessibilityServiceConnection)localObject1).focusSearch(paramInt2, paramLong, paramInt3, i, this, Thread.currentThread().getId());
          Binder.restoreCallingIdentity(l);
          if (localObject1 != null) {
            localAccessibilityNodeInfo = getFindAccessibilityNodeInfoResultAndClear(i);
          }
        }
        finally
        {
          try
          {
            AccessibilityNodeInfo localAccessibilityNodeInfo;
            finalizeAndCacheAccessibilityNodeInfo(localAccessibilityNodeInfo, paramInt1, false, (String[])localObject1);
            return localAccessibilityNodeInfo;
          }
          catch (RemoteException localRemoteException1) {}
          localObject2 = finally;
          Binder.restoreCallingIdentity(l);
        }
      }
      return null;
    }
    catch (RemoteException localRemoteException2)
    {
      Log.w("AccessibilityInteractionClient", "Error while calling remote accessibilityFocusSearch", localRemoteException2);
    }
  }
  
  public AccessibilityNodeInfo getRootInActiveWindow(int paramInt)
  {
    return findAccessibilityNodeInfoByAccessibilityId(paramInt, Integer.MAX_VALUE, AccessibilityNodeInfo.ROOT_NODE_ID, false, 4, null);
  }
  
  public AccessibilityWindowInfo getWindow(int paramInt1, int paramInt2)
  {
    try
    {
      Object localObject1 = getConnection(paramInt1);
      if (localObject1 != null)
      {
        AccessibilityWindowInfo localAccessibilityWindowInfo = sAccessibilityCache.getWindow(paramInt2);
        if (localAccessibilityWindowInfo != null) {
          return localAccessibilityWindowInfo;
        }
        long l = Binder.clearCallingIdentity();
        try
        {
          localObject1 = ((IAccessibilityServiceConnection)localObject1).getWindow(paramInt2);
          Binder.restoreCallingIdentity(l);
          if (localObject1 != null)
          {
            sAccessibilityCache.addWindow((AccessibilityWindowInfo)localObject1);
            return localObject1;
          }
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AccessibilityInteractionClient", "Error while calling remote getWindow", localRemoteException);
    }
    return null;
  }
  
  public List<AccessibilityWindowInfo> getWindows(int paramInt)
  {
    try
    {
      Object localObject1 = getConnection(paramInt);
      if (localObject1 != null)
      {
        List localList = sAccessibilityCache.getWindows();
        if (localList != null) {
          return localList;
        }
        long l = Binder.clearCallingIdentity();
        try
        {
          localObject1 = ((IAccessibilityServiceConnection)localObject1).getWindows();
          Binder.restoreCallingIdentity(l);
          if (localObject1 != null)
          {
            sAccessibilityCache.setWindows((List)localObject1);
            return localObject1;
          }
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("AccessibilityInteractionClient", "Error while calling remote getWindows", localRemoteException);
    }
    return Collections.emptyList();
  }
  
  public void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    sAccessibilityCache.onAccessibilityEvent(paramAccessibilityEvent);
  }
  
  public boolean performAccessibilityAction(int paramInt1, int paramInt2, long paramLong, int paramInt3, Bundle paramBundle)
  {
    try
    {
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = getConnection(paramInt1);
      if (localIAccessibilityServiceConnection != null)
      {
        paramInt1 = mInteractionIdCounter.getAndIncrement();
        long l = Binder.clearCallingIdentity();
        try
        {
          boolean bool = localIAccessibilityServiceConnection.performAccessibilityAction(paramInt2, paramLong, paramInt3, paramBundle, paramInt1, this, Thread.currentThread().getId());
          Binder.restoreCallingIdentity(l);
          if (bool) {
            return getPerformAccessibilityActionResultAndClear(paramInt1);
          }
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
      }
    }
    catch (RemoteException paramBundle)
    {
      Log.w("AccessibilityInteractionClient", "Error while calling remote performAccessibilityAction", paramBundle);
    }
    return false;
  }
  
  public void setFindAccessibilityNodeInfoResult(AccessibilityNodeInfo paramAccessibilityNodeInfo, int paramInt)
  {
    synchronized (mInstanceLock)
    {
      if (paramInt > mInteractionId)
      {
        mFindAccessibilityNodeInfoResult = paramAccessibilityNodeInfo;
        mInteractionId = paramInt;
      }
      mInstanceLock.notifyAll();
      return;
    }
  }
  
  public void setFindAccessibilityNodeInfosResult(List<AccessibilityNodeInfo> paramList, int paramInt)
  {
    synchronized (mInstanceLock)
    {
      if (paramInt > mInteractionId)
      {
        if (paramList != null)
        {
          int i;
          if (Binder.getCallingPid() != Process.myPid()) {
            i = 1;
          } else {
            i = 0;
          }
          if (i == 0)
          {
            ArrayList localArrayList = new java/util/ArrayList;
            localArrayList.<init>(paramList);
            mFindAccessibilityNodeInfosResult = localArrayList;
          }
          else
          {
            mFindAccessibilityNodeInfosResult = paramList;
          }
        }
        else
        {
          mFindAccessibilityNodeInfosResult = Collections.emptyList();
        }
        mInteractionId = paramInt;
      }
      mInstanceLock.notifyAll();
      return;
    }
  }
  
  public void setPerformAccessibilityActionResult(boolean paramBoolean, int paramInt)
  {
    synchronized (mInstanceLock)
    {
      if (paramInt > mInteractionId)
      {
        mPerformAccessibilityActionResult = paramBoolean;
        mInteractionId = paramInt;
      }
      mInstanceLock.notifyAll();
      return;
    }
  }
  
  public void setSameThreadMessage(Message paramMessage)
  {
    synchronized (mInstanceLock)
    {
      mSameThreadMessage = paramMessage;
      mInstanceLock.notifyAll();
      return;
    }
  }
}
