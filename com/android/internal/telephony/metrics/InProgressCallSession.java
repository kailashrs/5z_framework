package com.android.internal.telephony.metrics;

import android.os.SystemClock;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyCallSession.Event;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class InProgressCallSession
{
  private static final int MAX_EVENTS = 300;
  public final Deque<TelephonyProto.TelephonyCallSession.Event> events;
  private boolean mEventsDropped = false;
  private long mLastElapsedTimeMs;
  private int mLastKnownPhoneState;
  public final int phoneId;
  public final long startElapsedTimeMs;
  public final int startSystemTimeMin;
  
  public InProgressCallSession(int paramInt)
  {
    phoneId = paramInt;
    events = new ArrayDeque();
    startSystemTimeMin = TelephonyMetrics.roundSessionStart(System.currentTimeMillis());
    startElapsedTimeMs = SystemClock.elapsedRealtime();
    mLastElapsedTimeMs = startElapsedTimeMs;
  }
  
  public void addEvent(long paramLong, CallSessionEventBuilder paramCallSessionEventBuilder)
  {
    try
    {
      if (events.size() >= 300)
      {
        events.removeFirst();
        mEventsDropped = true;
      }
      paramCallSessionEventBuilder.setDelay(TelephonyMetrics.toPrivacyFuzzedTimeInterval(mLastElapsedTimeMs, paramLong));
      events.add(paramCallSessionEventBuilder.build());
      mLastElapsedTimeMs = paramLong;
      return;
    }
    finally {}
  }
  
  public void addEvent(CallSessionEventBuilder paramCallSessionEventBuilder)
  {
    addEvent(SystemClock.elapsedRealtime(), paramCallSessionEventBuilder);
  }
  
  public boolean containsCsCalls()
  {
    Iterator localIterator = events.iterator();
    while (localIterator.hasNext()) {
      if (nexttype == 10) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isEventsDropped()
  {
    return mEventsDropped;
  }
  
  public boolean isPhoneIdle()
  {
    int i = mLastKnownPhoneState;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public void setLastKnownPhoneState(int paramInt)
  {
    mLastKnownPhoneState = paramInt;
  }
}
