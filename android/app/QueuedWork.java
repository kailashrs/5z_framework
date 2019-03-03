package android.app;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ExponentiallyBucketedHistogram;
import java.util.Iterator;
import java.util.LinkedList;

public class QueuedWork
{
  private static final boolean DEBUG = false;
  private static final long DELAY = 100L;
  private static final String LOG_TAG = QueuedWork.class.getSimpleName();
  private static final long MAX_WAIT_TIME_MILLIS = 512L;
  private static int mNumWaits = 0;
  @GuardedBy("sLock")
  private static final ExponentiallyBucketedHistogram mWaitTimes;
  @GuardedBy("sLock")
  private static boolean sCanDelay;
  @GuardedBy("sLock")
  private static final LinkedList<Runnable> sFinishers;
  @GuardedBy("sLock")
  private static Handler sHandler;
  private static final Object sLock = new Object();
  private static Object sProcessingWork = new Object();
  @GuardedBy("sLock")
  private static final LinkedList<Runnable> sWork;
  
  static
  {
    sFinishers = new LinkedList();
    sHandler = null;
    sWork = new LinkedList();
    sCanDelay = true;
    mWaitTimes = new ExponentiallyBucketedHistogram(16);
  }
  
  public QueuedWork() {}
  
  public static void addFinisher(Runnable paramRunnable)
  {
    synchronized (sLock)
    {
      sFinishers.add(paramRunnable);
      return;
    }
  }
  
  private static Handler getHandler()
  {
    synchronized (sLock)
    {
      if (sHandler == null)
      {
        localObject2 = new android/os/HandlerThread;
        ((HandlerThread)localObject2).<init>("queued-work-looper", -2);
        ((HandlerThread)localObject2).start();
        QueuedWorkHandler localQueuedWorkHandler = new android/app/QueuedWork$QueuedWorkHandler;
        localQueuedWorkHandler.<init>(((HandlerThread)localObject2).getLooper());
        sHandler = localQueuedWorkHandler;
      }
      Object localObject2 = sHandler;
      return localObject2;
    }
  }
  
  public static boolean hasPendingWork()
  {
    synchronized (sLock)
    {
      boolean bool = sWork.isEmpty();
      return bool ^ true;
    }
  }
  
  private static void processPendingWork()
  {
    synchronized (sProcessingWork)
    {
      synchronized (sLock)
      {
        LinkedList localLinkedList = (LinkedList)sWork.clone();
        sWork.clear();
        getHandler().removeMessages(1);
        if (localLinkedList.size() > 0)
        {
          ??? = localLinkedList.iterator();
          while (((Iterator)???).hasNext()) {
            ((Runnable)((Iterator)???).next()).run();
          }
        }
        return;
      }
    }
  }
  
  public static void queue(Runnable paramRunnable, boolean paramBoolean)
  {
    Handler localHandler = getHandler();
    synchronized (sLock)
    {
      sWork.add(paramRunnable);
      if ((paramBoolean) && (sCanDelay)) {
        localHandler.sendEmptyMessageDelayed(1, 100L);
      } else {
        localHandler.sendEmptyMessage(1);
      }
      return;
    }
  }
  
  public static void removeFinisher(Runnable paramRunnable)
  {
    synchronized (sLock)
    {
      sFinishers.remove(paramRunnable);
      return;
    }
  }
  
  public static void waitToFinish()
  {
    long l = System.currentTimeMillis();
    Object localObject1 = getHandler();
    synchronized (sLock)
    {
      if (((Handler)localObject1).hasMessages(1)) {
        ((Handler)localObject1).removeMessages(1);
      }
      sCanDelay = false;
      ??? = StrictMode.allowThreadDiskWrites();
      try
      {
        processPendingWork();
        StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)???);
        try
        {
          synchronized (sLock)
          {
            localObject1 = (Runnable)sFinishers.poll();
            if (localObject1 == null)
            {
              sCanDelay = true;
              synchronized (sLock)
              {
                l = System.currentTimeMillis() - l;
                if ((l > 0L) || (0 != 0))
                {
                  mWaitTimes.add(Long.valueOf(l).intValue());
                  mNumWaits += 1;
                  if ((mNumWaits % 1024 == 0) || (l > 512L)) {
                    mWaitTimes.log(LOG_TAG, "waited: ");
                  }
                }
                return;
              }
            }
            localObject2.run();
          }
          localObject4 = finally;
        }
        finally
        {
          sCanDelay = true;
        }
        localObject5 = finally;
      }
      finally {}
    }
  }
  
  private static class QueuedWorkHandler
    extends Handler
  {
    static final int MSG_RUN = 1;
    
    QueuedWorkHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (what == 1) {
        QueuedWork.access$000();
      }
    }
  }
}
