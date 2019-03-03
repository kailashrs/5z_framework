package android.metrics;

import android.annotation.SystemApi;
import android.util.EventLog;
import android.util.EventLog.Event;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

@SystemApi
public class MetricsReader
{
  private int[] LOGTAGS = { 524292 };
  private int mCheckpointTag = -1;
  private Queue<LogMaker> mPendingQueue = new LinkedList();
  private LogReader mReader = new LogReader();
  private Queue<LogMaker> mSeenQueue = new LinkedList();
  
  public MetricsReader() {}
  
  public void checkpoint()
  {
    mCheckpointTag = ((int)(System.currentTimeMillis() % 2147483647L));
    mReader.writeCheckpoint(mCheckpointTag);
    mPendingQueue.clear();
    mSeenQueue.clear();
  }
  
  public boolean hasNext()
  {
    return mPendingQueue.isEmpty() ^ true;
  }
  
  public LogMaker next()
  {
    LogMaker localLogMaker = (LogMaker)mPendingQueue.poll();
    if (localLogMaker != null) {
      mSeenQueue.offer(localLogMaker);
    }
    return localLogMaker;
  }
  
  public void read(long paramLong)
  {
    Object localObject1 = new ArrayList();
    try
    {
      mReader.readEvents(LOGTAGS, paramLong, (Collection)localObject1);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    mPendingQueue.clear();
    mSeenQueue.clear();
    localObject1 = ((ArrayList)localObject1).iterator();
    while (((Iterator)localObject1).hasNext())
    {
      Event localEvent = (Event)((Iterator)localObject1).next();
      paramLong = localEvent.getTimeMillis();
      Object localObject3 = localEvent.getData();
      if ((localObject3 instanceof Object[]))
      {
        localObject2 = (Object[])localObject3;
      }
      else
      {
        localObject2 = new Object[1];
        localObject2[0] = localObject3;
      }
      Object localObject2 = new LogMaker((Object[])localObject2).setTimestamp(paramLong).setUid(localEvent.getUid()).setProcessId(localEvent.getProcessId());
      if (((LogMaker)localObject2).getCategory() == 920)
      {
        if (((LogMaker)localObject2).getSubtype() == mCheckpointTag) {
          mPendingQueue.clear();
        }
      }
      else {
        mPendingQueue.offer(localObject2);
      }
    }
  }
  
  public void reset()
  {
    mSeenQueue.addAll(mPendingQueue);
    mPendingQueue.clear();
    mCheckpointTag = -1;
    Queue localQueue = mPendingQueue;
    mPendingQueue = mSeenQueue;
    mSeenQueue = localQueue;
  }
  
  @VisibleForTesting
  public void setLogReader(LogReader paramLogReader)
  {
    mReader = paramLogReader;
  }
  
  @VisibleForTesting
  public static class Event
  {
    Object mData;
    int mPid;
    long mTimeMillis;
    int mUid;
    
    public Event(long paramLong, int paramInt1, int paramInt2, Object paramObject)
    {
      mTimeMillis = paramLong;
      mPid = paramInt1;
      mUid = paramInt2;
      mData = paramObject;
    }
    
    Event(EventLog.Event paramEvent)
    {
      mTimeMillis = TimeUnit.MILLISECONDS.convert(paramEvent.getTimeNanos(), TimeUnit.NANOSECONDS);
      mPid = paramEvent.getProcessId();
      mUid = paramEvent.getUid();
      mData = paramEvent.getData();
    }
    
    public Object getData()
    {
      return mData;
    }
    
    public int getProcessId()
    {
      return mPid;
    }
    
    public long getTimeMillis()
    {
      return mTimeMillis;
    }
    
    public int getUid()
    {
      return mUid;
    }
    
    public void setData(Object paramObject)
    {
      mData = paramObject;
    }
  }
  
  @VisibleForTesting
  public static class LogReader
  {
    public LogReader() {}
    
    public void readEvents(int[] paramArrayOfInt, long paramLong, Collection<MetricsReader.Event> paramCollection)
      throws IOException
    {
      ArrayList localArrayList = new ArrayList();
      EventLog.readEventsOnWrapping(paramArrayOfInt, TimeUnit.NANOSECONDS.convert(paramLong, TimeUnit.MILLISECONDS), localArrayList);
      paramArrayOfInt = localArrayList.iterator();
      while (paramArrayOfInt.hasNext()) {
        paramCollection.add(new MetricsReader.Event((EventLog.Event)paramArrayOfInt.next()));
      }
    }
    
    public void writeCheckpoint(int paramInt)
    {
      new MetricsLogger().action(920, paramInt);
    }
  }
}
