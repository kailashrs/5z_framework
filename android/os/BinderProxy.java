package android.os;

import android.util.Log;
import android.util.SparseIntArray;
import com.android.internal.os.BinderInternal;
import java.io.FileDescriptor;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import libcore.util.NativeAllocationRegistry;

final class BinderProxy
  implements IBinder
{
  private static final int NATIVE_ALLOCATION_SIZE = 1000;
  private static ProxyMap sProxyMap = new ProxyMap(null);
  private final long mNativeData;
  volatile boolean mWarnOnBlocking = Binder.sWarnOnBlocking;
  
  private BinderProxy(long paramLong)
  {
    mNativeData = paramLong;
  }
  
  private static void dumpProxyDebugInfo()
  {
    if (Build.IS_DEBUGGABLE) {
      sProxyMap.dumpProxyInterfaceCounts();
    }
  }
  
  private static BinderProxy getInstance(long paramLong1, long paramLong2)
  {
    try
    {
      BinderProxy localBinderProxy = sProxyMap.get(paramLong2);
      if (localBinderProxy != null) {
        return localBinderProxy;
      }
      localBinderProxy = new BinderProxy(paramLong1);
      NoImagePreloadHolder.sRegistry.registerNativeAllocation(localBinderProxy, paramLong1);
      sProxyMap.set(paramLong2, localBinderProxy);
      return localBinderProxy;
    }
    catch (Throwable localThrowable)
    {
      NativeAllocationRegistry.applyFreeFunction(NoImagePreloadHolder.sNativeFinalizer, paramLong1);
      throw localThrowable;
    }
  }
  
  private static native long getNativeFinalizer();
  
  private static final void sendDeathNotice(IBinder.DeathRecipient paramDeathRecipient)
  {
    try
    {
      paramDeathRecipient.binderDied();
    }
    catch (RuntimeException paramDeathRecipient)
    {
      Log.w("BinderNative", "Uncaught exception from death notification", paramDeathRecipient);
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, String[] paramArrayOfString)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    localParcel1.writeFileDescriptor(paramFileDescriptor);
    localParcel1.writeStringArray(paramArrayOfString);
    try
    {
      transact(1598311760, localParcel1, localParcel2, 0);
      localParcel2.readException();
      return;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public void dumpAsync(FileDescriptor paramFileDescriptor, String[] paramArrayOfString)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    localParcel1.writeFileDescriptor(paramFileDescriptor);
    localParcel1.writeStringArray(paramArrayOfString);
    try
    {
      transact(1598311760, localParcel1, localParcel2, 1);
      return;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public native String getInterfaceDescriptor()
    throws RemoteException;
  
  public native boolean isBinderAlive();
  
  public native void linkToDeath(IBinder.DeathRecipient paramDeathRecipient, int paramInt)
    throws RemoteException;
  
  public native boolean pingBinder();
  
  public IInterface queryLocalInterface(String paramString)
  {
    return null;
  }
  
  public void shellCommand(FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2, FileDescriptor paramFileDescriptor3, String[] paramArrayOfString, ShellCallback paramShellCallback, ResultReceiver paramResultReceiver)
    throws RemoteException
  {
    Parcel localParcel1 = Parcel.obtain();
    Parcel localParcel2 = Parcel.obtain();
    localParcel1.writeFileDescriptor(paramFileDescriptor1);
    localParcel1.writeFileDescriptor(paramFileDescriptor2);
    localParcel1.writeFileDescriptor(paramFileDescriptor3);
    localParcel1.writeStringArray(paramArrayOfString);
    ShellCallback.writeToParcel(paramShellCallback, localParcel1);
    paramResultReceiver.writeToParcel(localParcel1, 0);
    try
    {
      transact(1598246212, localParcel1, localParcel2, 0);
      localParcel2.readException();
      return;
    }
    finally
    {
      localParcel1.recycle();
      localParcel2.recycle();
    }
  }
  
  public boolean transact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
    throws RemoteException
  {
    Binder.checkParcel(this, paramInt1, paramParcel1, "Unreasonably large binder buffer");
    if ((mWarnOnBlocking) && ((paramInt2 & 0x1) == 0))
    {
      mWarnOnBlocking = false;
      Log.w("Binder", "Outgoing transactions from this process must be FLAG_ONEWAY", new Throwable());
    }
    boolean bool1 = Binder.isTracingEnabled();
    if (bool1)
    {
      Object localObject = new Throwable();
      Binder.getTransactionTracker().addTrace((Throwable)localObject);
      StackTraceElement localStackTraceElement = localObject.getStackTrace()[1];
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(localStackTraceElement.getClassName());
      ((StringBuilder)localObject).append(".");
      ((StringBuilder)localObject).append(localStackTraceElement.getMethodName());
      Trace.traceBegin(1L, ((StringBuilder)localObject).toString());
    }
    try
    {
      boolean bool2 = transactNative(paramInt1, paramParcel1, paramParcel2, paramInt2);
      return bool2;
    }
    finally
    {
      if (bool1) {
        Trace.traceEnd(1L);
      }
    }
  }
  
  public native boolean transactNative(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
    throws RemoteException;
  
  public native boolean unlinkToDeath(IBinder.DeathRecipient paramDeathRecipient, int paramInt);
  
  private static class NoImagePreloadHolder
  {
    public static final long sNativeFinalizer = ;
    public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(BinderProxy.class.getClassLoader(), sNativeFinalizer, 1000L);
    
    private NoImagePreloadHolder() {}
  }
  
  private static final class ProxyMap
  {
    private static final int CRASH_AT_SIZE = 20000;
    private static final int LOG_MAIN_INDEX_SIZE = 8;
    private static final int MAIN_INDEX_MASK = 255;
    private static final int MAIN_INDEX_SIZE = 256;
    private static final int WARN_INCREMENT = 10;
    private final Long[][] mMainIndexKeys = new Long['Ā'][];
    private final ArrayList<WeakReference<BinderProxy>>[] mMainIndexValues = new ArrayList['Ā'];
    private int mRandom;
    private int mWarnBucketSize = 20;
    
    private ProxyMap() {}
    
    private void dumpPerUidProxyCounts()
    {
      SparseIntArray localSparseIntArray = BinderInternal.nGetBinderProxyPerUidCounts();
      if (localSparseIntArray.size() == 0) {
        return;
      }
      Log.d("Binder", "Per Uid Binder Proxy Counts:");
      for (int i = 0; i < localSparseIntArray.size(); i++)
      {
        int j = localSparseIntArray.keyAt(i);
        int k = localSparseIntArray.valueAt(i);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("UID : ");
        localStringBuilder.append(j);
        localStringBuilder.append("  count = ");
        localStringBuilder.append(k);
        Log.d("Binder", localStringBuilder.toString());
      }
    }
    
    private void dumpProxyInterfaceCounts()
    {
      Object localObject1 = new HashMap();
      ArrayList[] arrayOfArrayList = mMainIndexValues;
      int i = arrayOfArrayList.length;
      int j = 0;
      for (int k = 0; k < i; k++)
      {
        Object localObject2 = arrayOfArrayList[k];
        if (localObject2 != null)
        {
          Iterator localIterator = ((ArrayList)localObject2).iterator();
          while (localIterator.hasNext())
          {
            localObject2 = (BinderProxy)((WeakReference)localIterator.next()).get();
            if (localObject2 == null) {
              localObject2 = "<cleared weak-ref>";
            } else {
              try
              {
                localObject2 = ((BinderProxy)localObject2).getInterfaceDescriptor();
              }
              catch (Throwable localThrowable)
              {
                localObject3 = "<exception during getDescriptor>";
              }
            }
            Integer localInteger = (Integer)((Map)localObject1).get(localObject3);
            if (localInteger == null) {
              ((Map)localObject1).put(localObject3, Integer.valueOf(1));
            } else {
              ((Map)localObject1).put(localObject3, Integer.valueOf(localInteger.intValue() + 1));
            }
          }
        }
      }
      Object localObject3 = (Map.Entry[])((Map)localObject1).entrySet().toArray(new Map.Entry[((Map)localObject1).size()]);
      Arrays.sort((Object[])localObject3, _..Lambda.BinderProxy.ProxyMap.huB_NMtOmTDIIYkL7mXm_Otlfnw.INSTANCE);
      Log.v("Binder", "BinderProxy descriptor histogram (top ten):");
      i = Math.min(10, localObject3.length);
      for (k = j; k < i; k++)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append(" #");
        ((StringBuilder)localObject1).append(k + 1);
        ((StringBuilder)localObject1).append(": ");
        ((StringBuilder)localObject1).append((String)localObject3[k].getKey());
        ((StringBuilder)localObject1).append(" x");
        ((StringBuilder)localObject1).append(localObject3[k].getValue());
        Log.v("Binder", ((StringBuilder)localObject1).toString());
      }
    }
    
    private static int hash(long paramLong)
    {
      return (int)(paramLong >> 2 ^ paramLong >> 10) & 0xFF;
    }
    
    private void remove(int paramInt1, int paramInt2)
    {
      Long[] arrayOfLong = mMainIndexKeys[paramInt1];
      ArrayList localArrayList = mMainIndexValues[paramInt1];
      paramInt1 = localArrayList.size();
      if (paramInt2 != paramInt1 - 1)
      {
        arrayOfLong[paramInt2] = arrayOfLong[(paramInt1 - 1)];
        localArrayList.set(paramInt2, (WeakReference)localArrayList.get(paramInt1 - 1));
      }
      localArrayList.remove(paramInt1 - 1);
    }
    
    private int size()
    {
      int i = 0;
      ArrayList[] arrayOfArrayList = mMainIndexValues;
      int j = arrayOfArrayList.length;
      int k = 0;
      while (k < j)
      {
        ArrayList localArrayList = arrayOfArrayList[k];
        int m = i;
        if (localArrayList != null) {
          m = i + localArrayList.size();
        }
        k++;
        i = m;
      }
      return i;
    }
    
    private int unclearedSize()
    {
      int i = 0;
      ArrayList[] arrayOfArrayList = mMainIndexValues;
      int j = arrayOfArrayList.length;
      int k = 0;
      while (k < j)
      {
        Object localObject = arrayOfArrayList[k];
        int m = i;
        if (localObject != null)
        {
          localObject = ((ArrayList)localObject).iterator();
          for (;;)
          {
            m = i;
            if (!((Iterator)localObject).hasNext()) {
              break;
            }
            m = i;
            if (((WeakReference)((Iterator)localObject).next()).get() != null) {
              m = i + 1;
            }
            i = m;
          }
        }
        k++;
        i = m;
      }
      return i;
    }
    
    BinderProxy get(long paramLong)
    {
      int i = hash(paramLong);
      Object localObject = mMainIndexKeys[i];
      if (localObject == null) {
        return null;
      }
      ArrayList localArrayList = mMainIndexValues[i];
      int j = localArrayList.size();
      for (int k = 0; k < j; k++) {
        if (paramLong == localObject[k].longValue())
        {
          localObject = (BinderProxy)((WeakReference)localArrayList.get(k)).get();
          if (localObject != null) {
            return localObject;
          }
          remove(i, k);
          return null;
        }
      }
      return null;
    }
    
    void set(long paramLong, BinderProxy paramBinderProxy)
    {
      int i = hash(paramLong);
      Object localObject1 = mMainIndexValues[i];
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject1 = mMainIndexValues;
        localObject2 = new ArrayList();
        localObject1[i] = localObject2;
        mMainIndexKeys[i] = new Long[1];
      }
      int j = ((ArrayList)localObject2).size();
      paramBinderProxy = new WeakReference(paramBinderProxy);
      for (int k = 0; k < j; k++) {
        if (((WeakReference)((ArrayList)localObject2).get(k)).get() == null)
        {
          ((ArrayList)localObject2).set(k, paramBinderProxy);
          mMainIndexKeys[i][k] = Long.valueOf(paramLong);
          if (k < j - 1)
          {
            int m = mRandom + 1;
            mRandom = m;
            j = Math.floorMod(m, j - (k + 1));
            if (((WeakReference)((ArrayList)localObject2).get(k + 1 + j)).get() == null) {
              remove(i, k + 1 + j);
            }
          }
          return;
        }
      }
      ((ArrayList)localObject2).add(j, paramBinderProxy);
      localObject2 = mMainIndexKeys[i];
      if (localObject2.length == j)
      {
        paramBinderProxy = new Long[j / 2 + j + 2];
        System.arraycopy(localObject2, 0, paramBinderProxy, 0, j);
        paramBinderProxy[j] = Long.valueOf(paramLong);
        mMainIndexKeys[i] = paramBinderProxy;
      }
      else
      {
        localObject2[j] = Long.valueOf(paramLong);
      }
      if (j >= mWarnBucketSize)
      {
        k = size();
        paramBinderProxy = new StringBuilder();
        paramBinderProxy.append("BinderProxy map growth! bucket size = ");
        paramBinderProxy.append(j);
        paramBinderProxy.append(" total = ");
        paramBinderProxy.append(k);
        Log.v("Binder", paramBinderProxy.toString());
        mWarnBucketSize += 10;
        if ((Build.IS_DEBUGGABLE) && (k >= 20000))
        {
          i = unclearedSize();
          if (i < 20000)
          {
            if (k > 3 * i / 2)
            {
              paramBinderProxy = new StringBuilder();
              paramBinderProxy.append("BinderProxy map has many cleared entries: ");
              paramBinderProxy.append(k - i);
              paramBinderProxy.append(" of ");
              paramBinderProxy.append(k);
              paramBinderProxy.append(" are cleared");
              Log.v("Binder", paramBinderProxy.toString());
            }
          }
          else
          {
            dumpProxyInterfaceCounts();
            dumpPerUidProxyCounts();
            Runtime.getRuntime().gc();
            paramBinderProxy = new StringBuilder();
            paramBinderProxy.append("Binder ProxyMap has too many entries: ");
            paramBinderProxy.append(k);
            paramBinderProxy.append(" (total), ");
            paramBinderProxy.append(i);
            paramBinderProxy.append(" (uncleared), ");
            paramBinderProxy.append(unclearedSize());
            paramBinderProxy.append(" (uncleared after GC). BinderProxy leak?");
            throw new AssertionError(paramBinderProxy.toString());
          }
        }
      }
    }
  }
}
