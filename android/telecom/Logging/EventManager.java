package android.telecom.Logging;

import android.telecom.Log;
import android.text.TextUtils;
import android.util.Pair;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class EventManager
{
  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
  @VisibleForTesting
  public static final int DEFAULT_EVENTS_TO_CACHE = 10;
  public static final String TAG = "Logging.Events";
  private static final Object mSync = new Object();
  private final Map<Loggable, EventRecord> mCallEventRecordMap = new HashMap();
  private List<EventListener> mEventListeners = new ArrayList();
  private LinkedBlockingQueue<EventRecord> mEventRecords = new LinkedBlockingQueue(10);
  private SessionManager.ISessionIdQueryHandler mSessionIdHandler;
  private final Map<String, List<TimedEventPair>> requestResponsePairs = new HashMap();
  
  public EventManager(SessionManager.ISessionIdQueryHandler paramISessionIdQueryHandler)
  {
    mSessionIdHandler = paramISessionIdQueryHandler;
  }
  
  private void addEventRecord(EventRecord paramEventRecord)
  {
    ??? = paramEventRecord.getRecordEntry();
    Object localObject2;
    if (mEventRecords.remainingCapacity() == 0)
    {
      localObject2 = (EventRecord)mEventRecords.poll();
      if (localObject2 != null) {
        mCallEventRecordMap.remove(((EventRecord)localObject2).getRecordEntry());
      }
    }
    mEventRecords.add(paramEventRecord);
    mCallEventRecordMap.put(???, paramEventRecord);
    synchronized (mSync)
    {
      localObject2 = mEventListeners.iterator();
      while (((Iterator)localObject2).hasNext()) {
        ((EventListener)((Iterator)localObject2).next()).eventRecordAdded(paramEventRecord);
      }
      return;
    }
  }
  
  public void addRequestResponsePair(TimedEventPair paramTimedEventPair)
  {
    if (requestResponsePairs.containsKey(mRequest))
    {
      ((List)requestResponsePairs.get(mRequest)).add(paramTimedEventPair);
    }
    else
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramTimedEventPair);
      requestResponsePairs.put(mRequest, localArrayList);
    }
  }
  
  public void changeEventCacheSize(int paramInt)
  {
    LinkedBlockingQueue localLinkedBlockingQueue = mEventRecords;
    mEventRecords = new LinkedBlockingQueue(paramInt);
    mCallEventRecordMap.clear();
    localLinkedBlockingQueue.forEach(new _..Lambda.EventManager.uddp6XAJ90VBwdTiuzBdSYelntQ(this));
  }
  
  public void dumpEvents(IndentingPrintWriter paramIndentingPrintWriter)
  {
    paramIndentingPrintWriter.println("Historical Events:");
    paramIndentingPrintWriter.increaseIndent();
    Iterator localIterator = mEventRecords.iterator();
    while (localIterator.hasNext()) {
      ((EventRecord)localIterator.next()).dump(paramIndentingPrintWriter);
    }
    paramIndentingPrintWriter.decreaseIndent();
  }
  
  public void dumpEventsTimeline(IndentingPrintWriter paramIndentingPrintWriter)
  {
    paramIndentingPrintWriter.println("Historical Events (sorted by time):");
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator1 = mEventRecords.iterator();
    Object localObject1;
    while (localIterator1.hasNext())
    {
      localObject1 = (EventRecord)localIterator1.next();
      Iterator localIterator2 = ((EventRecord)localObject1).getEvents().iterator();
      while (localIterator2.hasNext())
      {
        localObject2 = (Event)localIterator2.next();
        localArrayList.add(new Pair(((EventRecord)localObject1).getRecordEntry(), localObject2));
      }
    }
    localArrayList.sort(Comparator.comparingLong(_..Lambda.EventManager.weOtitr8e1cZeiy1aDSqzNoKaY8.INSTANCE));
    paramIndentingPrintWriter.increaseIndent();
    Object localObject2 = localArrayList.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject1 = (Pair)((Iterator)localObject2).next();
      paramIndentingPrintWriter.print(second).timestampString);
      paramIndentingPrintWriter.print(",");
      paramIndentingPrintWriter.print(((Loggable)first).getId());
      paramIndentingPrintWriter.print(",");
      paramIndentingPrintWriter.print(second).eventId);
      paramIndentingPrintWriter.print(",");
      paramIndentingPrintWriter.println(second).data);
    }
    paramIndentingPrintWriter.decreaseIndent();
  }
  
  public void event(Loggable paramLoggable, String paramString, Object paramObject)
  {
    String str = mSessionIdHandler.getSessionId();
    if (paramLoggable == null)
    {
      Log.i("Logging.Events", "Non-call EVENT: %s, %s", new Object[] { paramString, paramObject });
      return;
    }
    synchronized (mEventRecords)
    {
      if (!mCallEventRecordMap.containsKey(paramLoggable))
      {
        EventRecord localEventRecord = new android/telecom/Logging/EventManager$EventRecord;
        localEventRecord.<init>(this, paramLoggable);
        addEventRecord(localEventRecord);
      }
      ((EventRecord)mCallEventRecordMap.get(paramLoggable)).addEvent(paramString, str, paramObject);
      return;
    }
  }
  
  public void event(Loggable paramLoggable, String paramString1, String paramString2, Object... paramVarArgs)
  {
    if (paramVarArgs != null) {
      try
      {
        if (paramVarArgs.length != 0)
        {
          String str = String.format(Locale.US, paramString2, paramVarArgs);
          paramString2 = str;
        }
      }
      catch (IllegalFormatException localIllegalFormatException)
      {
        Log.e(this, localIllegalFormatException, "IllegalFormatException: formatString='%s' numArgs=%d", new Object[] { paramString2, Integer.valueOf(paramVarArgs.length) });
        paramVarArgs = new StringBuilder();
        paramVarArgs.append(paramString2);
        paramVarArgs.append(" (An error occurred while formatting the message.)");
        paramString2 = paramVarArgs.toString();
      }
    }
    event(paramLoggable, paramString1, paramString2);
  }
  
  @VisibleForTesting
  public Map<Loggable, EventRecord> getCallEventRecordMap()
  {
    return mCallEventRecordMap;
  }
  
  @VisibleForTesting
  public LinkedBlockingQueue<EventRecord> getEventRecords()
  {
    return mEventRecords;
  }
  
  public void registerEventListener(EventListener paramEventListener)
  {
    if (paramEventListener != null) {
      synchronized (mSync)
      {
        mEventListeners.add(paramEventListener);
      }
    }
  }
  
  public static class Event
  {
    public Object data;
    public String eventId;
    public String sessionId;
    public long time;
    public final String timestampString;
    
    public Event(String paramString1, String paramString2, long paramLong, Object paramObject)
    {
      eventId = paramString1;
      sessionId = paramString2;
      time = paramLong;
      timestampString = ZonedDateTime.ofInstant(Instant.ofEpochMilli(paramLong), ZoneId.systemDefault()).format(EventManager.DATE_TIME_FORMATTER);
      data = paramObject;
    }
  }
  
  public static abstract interface EventListener
  {
    public abstract void eventRecordAdded(EventManager.EventRecord paramEventRecord);
  }
  
  public class EventRecord
  {
    private final List<EventManager.Event> mEvents = new LinkedList();
    private final EventManager.Loggable mRecordEntry;
    
    public EventRecord(EventManager.Loggable paramLoggable)
    {
      mRecordEntry = paramLoggable;
    }
    
    public void addEvent(String paramString1, String paramString2, Object paramObject)
    {
      mEvents.add(new EventManager.Event(paramString1, paramString2, System.currentTimeMillis(), paramObject));
      Log.i("Event", "RecordEntry %s: %s, %s", new Object[] { mRecordEntry.getId(), paramString1, paramObject });
    }
    
    public void dump(IndentingPrintWriter paramIndentingPrintWriter)
    {
      paramIndentingPrintWriter.print(mRecordEntry.getDescription());
      paramIndentingPrintWriter.increaseIndent();
      Iterator localIterator = mEvents.iterator();
      Object localObject1;
      while (localIterator.hasNext())
      {
        localObject1 = (EventManager.Event)localIterator.next();
        paramIndentingPrintWriter.print(timestampString);
        paramIndentingPrintWriter.print(" - ");
        paramIndentingPrintWriter.print(eventId);
        if (data != null)
        {
          paramIndentingPrintWriter.print(" (");
          localObject2 = data;
          localObject3 = localObject2;
          if ((localObject2 instanceof EventManager.Loggable))
          {
            EventRecord localEventRecord = (EventRecord)mCallEventRecordMap.get(localObject2);
            localObject3 = localObject2;
            if (localEventRecord != null)
            {
              localObject3 = new StringBuilder();
              ((StringBuilder)localObject3).append("RecordEntry ");
              ((StringBuilder)localObject3).append(mRecordEntry.getId());
              localObject3 = ((StringBuilder)localObject3).toString();
            }
          }
          paramIndentingPrintWriter.print(localObject3);
          paramIndentingPrintWriter.print(")");
        }
        if (!TextUtils.isEmpty(sessionId))
        {
          paramIndentingPrintWriter.print(":");
          paramIndentingPrintWriter.print(sessionId);
        }
        paramIndentingPrintWriter.println();
      }
      paramIndentingPrintWriter.println("Timings (average for this call, milliseconds):");
      paramIndentingPrintWriter.increaseIndent();
      Object localObject3 = EventTiming.averageTimings(extractEventTimings());
      Object localObject2 = new ArrayList(((Map)localObject3).keySet());
      Collections.sort((List)localObject2);
      localObject2 = ((List)localObject2).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (String)((Iterator)localObject2).next();
        paramIndentingPrintWriter.printf("%s: %.2f\n", new Object[] { localObject1, ((Map)localObject3).get(localObject1) });
      }
      paramIndentingPrintWriter.decreaseIndent();
      paramIndentingPrintWriter.decreaseIndent();
    }
    
    public List<EventTiming> extractEventTimings()
    {
      if (mEvents == null) {
        return Collections.emptyList();
      }
      LinkedList localLinkedList = new LinkedList();
      HashMap localHashMap = new HashMap();
      Object localObject2;
      for (Object localObject1 = mEvents.iterator(); ((Iterator)localObject1).hasNext(); localObject1 = localObject2)
      {
        EventManager.Event localEvent = (EventManager.Event)((Iterator)localObject1).next();
        localObject2 = localObject1;
        if (requestResponsePairs.containsKey(eventId))
        {
          Iterator localIterator = ((List)requestResponsePairs.get(eventId)).iterator();
          for (;;)
          {
            localObject2 = localObject1;
            if (!localIterator.hasNext()) {
              break;
            }
            localObject2 = (EventManager.TimedEventPair)localIterator.next();
            localHashMap.put(mResponse, new PendingResponse(eventId, time, mTimeoutMillis, mName));
          }
        }
        localObject1 = (PendingResponse)localHashMap.remove(eventId);
        if (localObject1 != null)
        {
          long l = time - requestEventTimeMillis;
          if (l < timeoutMillis) {
            localLinkedList.add(new EventTiming(name, l));
          }
        }
      }
      return localLinkedList;
    }
    
    public List<EventManager.Event> getEvents()
    {
      return mEvents;
    }
    
    public EventManager.Loggable getRecordEntry()
    {
      return mRecordEntry;
    }
    
    public class EventTiming
      extends TimedEvent<String>
    {
      public String name;
      public long time;
      
      public EventTiming(String paramString, long paramLong)
      {
        name = paramString;
        time = paramLong;
      }
      
      public String getKey()
      {
        return name;
      }
      
      public long getTime()
      {
        return time;
      }
    }
    
    private class PendingResponse
    {
      String name;
      String requestEventId;
      long requestEventTimeMillis;
      long timeoutMillis;
      
      public PendingResponse(String paramString1, long paramLong1, long paramLong2, String paramString2)
      {
        requestEventId = paramString1;
        requestEventTimeMillis = paramLong1;
        timeoutMillis = paramLong2;
        name = paramString2;
      }
    }
  }
  
  public static abstract interface Loggable
  {
    public abstract String getDescription();
    
    public abstract String getId();
  }
  
  public static class TimedEventPair
  {
    private static final long DEFAULT_TIMEOUT = 3000L;
    String mName;
    String mRequest;
    String mResponse;
    long mTimeoutMillis = 3000L;
    
    public TimedEventPair(String paramString1, String paramString2, String paramString3)
    {
      mRequest = paramString1;
      mResponse = paramString2;
      mName = paramString3;
    }
    
    public TimedEventPair(String paramString1, String paramString2, String paramString3, long paramLong)
    {
      mRequest = paramString1;
      mResponse = paramString2;
      mName = paramString3;
      mTimeoutMillis = paramLong;
    }
  }
}
