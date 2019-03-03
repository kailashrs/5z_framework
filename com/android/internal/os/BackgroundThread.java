package com.android.internal.os;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public final class BackgroundThread
  extends HandlerThread
{
  private static final long SLOW_DELIVERY_THRESHOLD_MS = 30000L;
  private static final long SLOW_DISPATCH_THRESHOLD_MS = 10000L;
  private static Handler sHandler;
  private static BackgroundThread sInstance;
  
  private BackgroundThread()
  {
    super("android.bg", 10);
  }
  
  private static void ensureThreadLocked()
  {
    if (sInstance == null)
    {
      sInstance = new BackgroundThread();
      sInstance.start();
      Looper localLooper = sInstance.getLooper();
      localLooper.setTraceTag(524288L);
      localLooper.setSlowLogThresholdMs(10000L, 30000L);
      sHandler = new Handler(sInstance.getLooper());
    }
  }
  
  public static BackgroundThread get()
  {
    try
    {
      ensureThreadLocked();
      BackgroundThread localBackgroundThread = sInstance;
      return localBackgroundThread;
    }
    finally {}
  }
  
  public static Handler getHandler()
  {
    try
    {
      ensureThreadLocked();
      Handler localHandler = sHandler;
      return localHandler;
    }
    finally {}
  }
}
