package com.android.internal.midi;

import java.util.SortedMap;
import java.util.TreeMap;

public class EventScheduler
{
  private static final long NANOS_PER_MILLI = 1000000L;
  private boolean mClosed;
  private volatile SortedMap<Long, FastEventQueue> mEventBuffer = new TreeMap();
  private FastEventQueue mEventPool = null;
  private final Object mLock = new Object();
  private int mMaxPoolSize = 200;
  
  public EventScheduler() {}
  
  private SchedulableEvent removeNextEventLocked(long paramLong)
  {
    FastEventQueue localFastEventQueue = (FastEventQueue)mEventBuffer.get(Long.valueOf(paramLong));
    if (localFastEventQueue.size() == 1) {
      mEventBuffer.remove(Long.valueOf(paramLong));
    }
    return localFastEventQueue.remove();
  }
  
  public void add(SchedulableEvent paramSchedulableEvent)
  {
    synchronized (mLock)
    {
      FastEventQueue localFastEventQueue = (FastEventQueue)mEventBuffer.get(Long.valueOf(paramSchedulableEvent.getTimestamp()));
      if (localFastEventQueue == null)
      {
        long l;
        if (mEventBuffer.isEmpty()) {
          l = Long.MAX_VALUE;
        } else {
          l = ((Long)mEventBuffer.firstKey()).longValue();
        }
        localFastEventQueue = new com/android/internal/midi/EventScheduler$FastEventQueue;
        localFastEventQueue.<init>(this, paramSchedulableEvent);
        mEventBuffer.put(Long.valueOf(paramSchedulableEvent.getTimestamp()), localFastEventQueue);
        if (paramSchedulableEvent.getTimestamp() < l) {
          mLock.notify();
        }
      }
      else
      {
        localFastEventQueue.add(paramSchedulableEvent);
      }
      return;
    }
  }
  
  public void addEventToPool(SchedulableEvent paramSchedulableEvent)
  {
    if (mEventPool == null) {
      mEventPool = new FastEventQueue(paramSchedulableEvent);
    } else if (mEventPool.size() < mMaxPoolSize) {
      mEventPool.add(paramSchedulableEvent);
    }
  }
  
  public void close()
  {
    synchronized (mLock)
    {
      mClosed = true;
      mLock.notify();
      return;
    }
  }
  
  protected void flush()
  {
    mEventBuffer = new TreeMap();
  }
  
  public SchedulableEvent getNextEvent(long paramLong)
  {
    Object localObject1 = null;
    Object localObject2 = mLock;
    Object localObject3 = localObject1;
    try
    {
      if (!mEventBuffer.isEmpty())
      {
        long l = ((Long)mEventBuffer.firstKey()).longValue();
        localObject3 = localObject1;
        if (l <= paramLong) {
          localObject3 = removeNextEventLocked(l);
        }
      }
      return localObject3;
    }
    finally {}
  }
  
  public SchedulableEvent removeEventfromPool()
  {
    Object localObject1 = null;
    Object localObject2 = localObject1;
    if (mEventPool != null)
    {
      localObject2 = localObject1;
      if (mEventPool.size() > 1) {
        localObject2 = mEventPool.remove();
      }
    }
    return localObject2;
  }
  
  public SchedulableEvent waitNextEvent()
    throws InterruptedException
  {
    Object localObject1 = null;
    Object localObject2 = mLock;
    for (;;)
    {
      Object localObject3 = localObject1;
      try
      {
        if (!mClosed)
        {
          long l1 = 2147483647L;
          if (!mEventBuffer.isEmpty())
          {
            long l2 = System.nanoTime();
            l1 = ((Long)mEventBuffer.firstKey()).longValue();
            if (l1 <= l2)
            {
              localObject3 = removeNextEventLocked(l1);
              break label120;
            }
            l2 = 1L + (l1 - l2) / 1000000L;
            l1 = l2;
            if (l2 > 2147483647L) {
              l1 = 2147483647L;
            }
          }
          mLock.wait((int)l1);
          continue;
        }
        label120:
        return localObject3;
      }
      finally {}
    }
  }
  
  private class FastEventQueue
  {
    volatile long mEventsAdded;
    volatile long mEventsRemoved;
    volatile EventScheduler.SchedulableEvent mFirst;
    volatile EventScheduler.SchedulableEvent mLast;
    
    FastEventQueue(EventScheduler.SchedulableEvent paramSchedulableEvent)
    {
      mFirst = paramSchedulableEvent;
      mLast = mFirst;
      mEventsAdded = 1L;
      mEventsRemoved = 0L;
    }
    
    public void add(EventScheduler.SchedulableEvent paramSchedulableEvent)
    {
      EventScheduler.SchedulableEvent.access$002(paramSchedulableEvent, null);
      EventScheduler.SchedulableEvent.access$002(mLast, paramSchedulableEvent);
      mLast = paramSchedulableEvent;
      mEventsAdded += 1L;
    }
    
    public EventScheduler.SchedulableEvent remove()
    {
      mEventsRemoved += 1L;
      EventScheduler.SchedulableEvent localSchedulableEvent = mFirst;
      mFirst = EventScheduler.SchedulableEvent.access$000(localSchedulableEvent);
      EventScheduler.SchedulableEvent.access$002(localSchedulableEvent, null);
      return localSchedulableEvent;
    }
    
    int size()
    {
      return (int)(mEventsAdded - mEventsRemoved);
    }
  }
  
  public static class SchedulableEvent
  {
    private volatile SchedulableEvent mNext = null;
    private long mTimestamp;
    
    public SchedulableEvent(long paramLong)
    {
      mTimestamp = paramLong;
    }
    
    public long getTimestamp()
    {
      return mTimestamp;
    }
    
    public void setTimestamp(long paramLong)
    {
      mTimestamp = paramLong;
    }
  }
}
