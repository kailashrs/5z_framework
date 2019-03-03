package com.android.internal.os;

import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.EventLog;
import android.util.SparseIntArray;
import com.android.internal.util.Preconditions;
import dalvik.system.VMRuntime;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class BinderInternal
{
  private static final String TAG = "BinderInternal";
  static final BinderProxyLimitListenerDelegate sBinderProxyLimitListenerDelegate = new BinderProxyLimitListenerDelegate(null);
  static WeakReference<GcWatcher> sGcWatcher = new WeakReference(new GcWatcher());
  static ArrayList<Runnable> sGcWatchers = new ArrayList();
  static long sLastGcTime;
  static Runnable[] sTmpWatchers = new Runnable[1];
  
  public BinderInternal() {}
  
  public static void addGcWatcher(Runnable paramRunnable)
  {
    synchronized (sGcWatchers)
    {
      sGcWatchers.add(paramRunnable);
      return;
    }
  }
  
  public static void binderProxyLimitCallbackFromNative(int paramInt)
  {
    sBinderProxyLimitListenerDelegate.notifyClient(paramInt);
  }
  
  public static void clearBinderProxyCountCallback()
  {
    sBinderProxyLimitListenerDelegate.setListener(null, null);
  }
  
  public static final native void disableBackgroundScheduling(boolean paramBoolean);
  
  static void forceBinderGc()
  {
    forceGc("Binder");
  }
  
  public static void forceGc(String paramString)
  {
    EventLog.writeEvent(2741, paramString);
    VMRuntime.getRuntime().requestConcurrentGC();
  }
  
  public static final native IBinder getContextObject();
  
  public static long getLastGcTime()
  {
    return sLastGcTime;
  }
  
  static final native void handleGc();
  
  public static final native void joinThreadPool();
  
  public static final native int nGetBinderProxyCount(int paramInt);
  
  public static final native SparseIntArray nGetBinderProxyPerUidCounts();
  
  public static final native void nSetBinderProxyCountEnabled(boolean paramBoolean);
  
  public static final native void nSetBinderProxyCountWatermarks(int paramInt1, int paramInt2);
  
  public static void setBinderProxyCountCallback(BinderProxyLimitListener paramBinderProxyLimitListener, Handler paramHandler)
  {
    Preconditions.checkNotNull(paramHandler, "Must provide NonNull Handler to setBinderProxyCountCallback when setting BinderProxyLimitListener");
    sBinderProxyLimitListenerDelegate.setListener(paramBinderProxyLimitListener, paramHandler);
  }
  
  public static final native void setMaxThreads(int paramInt);
  
  public static abstract interface BinderProxyLimitListener
  {
    public abstract void onLimitReached(int paramInt);
  }
  
  private static class BinderProxyLimitListenerDelegate
  {
    private BinderInternal.BinderProxyLimitListener mBinderProxyLimitListener;
    private Handler mHandler;
    
    private BinderProxyLimitListenerDelegate() {}
    
    void notifyClient(int paramInt)
    {
      try
      {
        if (mBinderProxyLimitListener != null)
        {
          Handler localHandler = mHandler;
          Runnable local1 = new com/android/internal/os/BinderInternal$BinderProxyLimitListenerDelegate$1;
          local1.<init>(this, paramInt);
          localHandler.post(local1);
        }
        return;
      }
      finally {}
    }
    
    void setListener(BinderInternal.BinderProxyLimitListener paramBinderProxyLimitListener, Handler paramHandler)
    {
      try
      {
        mBinderProxyLimitListener = paramBinderProxyLimitListener;
        mHandler = paramHandler;
        return;
      }
      finally {}
    }
  }
  
  static final class GcWatcher
  {
    GcWatcher() {}
    
    protected void finalize()
      throws Throwable
    {
      BinderInternal.handleGc();
      BinderInternal.sLastGcTime = SystemClock.uptimeMillis();
      synchronized (BinderInternal.sGcWatchers)
      {
        BinderInternal.sTmpWatchers = (Runnable[])BinderInternal.sGcWatchers.toArray(BinderInternal.sTmpWatchers);
        for (int i = 0; i < BinderInternal.sTmpWatchers.length; i++) {
          if (BinderInternal.sTmpWatchers[i] != null) {
            BinderInternal.sTmpWatchers[i].run();
          }
        }
        BinderInternal.sGcWatcher = new WeakReference(new GcWatcher());
        return;
      }
    }
  }
}
