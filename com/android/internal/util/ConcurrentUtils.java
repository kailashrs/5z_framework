package com.android.internal.util;

import android.os.Process;
import android.util.Slog;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentUtils
{
  private ConcurrentUtils() {}
  
  public static ExecutorService newFixedThreadPool(int paramInt1, String paramString, final int paramInt2)
  {
    Executors.newFixedThreadPool(paramInt1, new ThreadFactory()
    {
      private final AtomicInteger threadNum = new AtomicInteger(0);
      
      public Thread newThread(final Runnable paramAnonymousRunnable)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(ConcurrentUtils.this);
        localStringBuilder.append(threadNum.incrementAndGet());
        new Thread(localStringBuilder.toString())
        {
          public void run()
          {
            Process.setThreadPriority(val$linuxThreadPriority);
            paramAnonymousRunnable.run();
          }
        };
      }
    });
  }
  
  public static void waitForCountDownNoInterrupt(CountDownLatch paramCountDownLatch, long paramLong, String paramString)
  {
    try
    {
      if (paramCountDownLatch.await(paramLong, TimeUnit.MILLISECONDS)) {
        return;
      }
      paramCountDownLatch = new java/lang/IllegalStateException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append(paramString);
      localStringBuilder.append(" timed out.");
      paramCountDownLatch.<init>(localStringBuilder.toString());
      throw paramCountDownLatch;
    }
    catch (InterruptedException paramCountDownLatch)
    {
      Thread.currentThread().interrupt();
      paramCountDownLatch = new StringBuilder();
      paramCountDownLatch.append(paramString);
      paramCountDownLatch.append(" interrupted.");
      throw new IllegalStateException(paramCountDownLatch.toString());
    }
  }
  
  public static <T> T waitForFutureNoInterrupt(Future<T> paramFuture, String paramString)
  {
    try
    {
      paramFuture = paramFuture.get();
      return paramFuture;
    }
    catch (ExecutionException paramFuture)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(" failed");
      throw new RuntimeException(localStringBuilder.toString(), paramFuture);
    }
    catch (InterruptedException paramFuture)
    {
      Thread.currentThread().interrupt();
      paramFuture = new StringBuilder();
      paramFuture.append(paramString);
      paramFuture.append(" interrupted");
      throw new IllegalStateException(paramFuture.toString());
    }
  }
  
  public static void wtfIfLockHeld(String paramString, Object paramObject)
  {
    if (Thread.holdsLock(paramObject)) {
      Slog.wtf(paramString, "Lock mustn't be held");
    }
  }
  
  public static void wtfIfLockNotHeld(String paramString, Object paramObject)
  {
    if (!Thread.holdsLock(paramObject)) {
      Slog.wtf(paramString, "Lock must be held");
    }
  }
}
