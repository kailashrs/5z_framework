package com.android.internal.telephony.metrics;

import android.os.SystemClock;
import com.android.internal.telephony.nano.TelephonyProto.SmsSession.Event;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

public class InProgressSmsSession
{
  private static final int MAX_EVENTS = 20;
  public final Deque<TelephonyProto.SmsSession.Event> events;
  private boolean mEventsDropped = false;
  private long mLastElapsedTimeMs;
  private AtomicInteger mNumExpectedResponses = new AtomicInteger(0);
  public final int phoneId;
  public final long startElapsedTimeMs;
  public final int startSystemTimeMin;
  
  public InProgressSmsSession(int paramInt)
  {
    phoneId = paramInt;
    events = new ArrayDeque();
    startSystemTimeMin = TelephonyMetrics.roundSessionStart(System.currentTimeMillis());
    startElapsedTimeMs = SystemClock.elapsedRealtime();
    mLastElapsedTimeMs = startElapsedTimeMs;
  }
  
  public void addEvent(long paramLong, SmsSessionEventBuilder paramSmsSessionEventBuilder)
  {
    try
    {
      if (events.size() >= 20)
      {
        events.removeFirst();
        mEventsDropped = true;
      }
      paramSmsSessionEventBuilder.setDelay(TelephonyMetrics.toPrivacyFuzzedTimeInterval(mLastElapsedTimeMs, paramLong));
      events.add(paramSmsSessionEventBuilder.build());
      mLastElapsedTimeMs = paramLong;
      return;
    }
    finally {}
  }
  
  public void addEvent(SmsSessionEventBuilder paramSmsSessionEventBuilder)
  {
    addEvent(SystemClock.elapsedRealtime(), paramSmsSessionEventBuilder);
  }
  
  public void decreaseExpectedResponse()
  {
    mNumExpectedResponses.decrementAndGet();
  }
  
  public int getNumExpectedResponses()
  {
    return mNumExpectedResponses.get();
  }
  
  public void increaseExpectedResponse()
  {
    mNumExpectedResponses.incrementAndGet();
  }
  
  public boolean isEventsDropped()
  {
    return mEventsDropped;
  }
}
