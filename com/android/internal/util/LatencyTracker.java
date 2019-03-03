package com.android.internal.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.EventLog;
import android.util.Log;
import android.util.SparseLongArray;

public class LatencyTracker
{
  public static final int ACTION_CHECK_CREDENTIAL = 3;
  public static final int ACTION_CHECK_CREDENTIAL_UNLOCKED = 4;
  public static final int ACTION_EXPAND_PANEL = 0;
  public static final int ACTION_FINGERPRINT_WAKE_AND_UNLOCK = 2;
  private static final String ACTION_RELOAD_PROPERTY = "com.android.systemui.RELOAD_LATENCY_TRACKER_PROPERTY";
  public static final int ACTION_ROTATE_SCREEN = 6;
  public static final int ACTION_TOGGLE_RECENTS = 1;
  public static final int ACTION_TURN_ON_SCREEN = 5;
  private static final String[] NAMES = { "expand panel", "toggle recents", "fingerprint wake-and-unlock", "check credential", "check credential unlocked", "turn on screen", "rotate the screen" };
  private static final String TAG = "LatencyTracker";
  private static LatencyTracker sLatencyTracker;
  private boolean mEnabled;
  private final SparseLongArray mStartRtc = new SparseLongArray();
  
  private LatencyTracker(Context paramContext)
  {
    paramContext.registerReceiver(new BroadcastReceiver()new IntentFilter
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        LatencyTracker.this.reloadProperty();
      }
    }, new IntentFilter("com.android.systemui.RELOAD_LATENCY_TRACKER_PROPERTY"));
    reloadProperty();
  }
  
  public static LatencyTracker getInstance(Context paramContext)
  {
    if (sLatencyTracker == null) {
      sLatencyTracker = new LatencyTracker(paramContext);
    }
    return sLatencyTracker;
  }
  
  public static boolean isEnabled(Context paramContext)
  {
    boolean bool;
    if ((Build.IS_DEBUGGABLE) && (getInstancemEnabled)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static void logAction(int paramInt1, int paramInt2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("action=");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(" latency=");
    localStringBuilder.append(paramInt2);
    Log.i("LatencyTracker", localStringBuilder.toString());
    EventLog.writeEvent(36070, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
  }
  
  private void reloadProperty()
  {
    mEnabled = SystemProperties.getBoolean("debug.systemui.latency_tracking", false);
  }
  
  public void onActionEnd(int paramInt)
  {
    if (!mEnabled) {
      return;
    }
    long l1 = SystemClock.elapsedRealtime();
    long l2 = mStartRtc.get(paramInt, -1L);
    if (l2 == -1L) {
      return;
    }
    mStartRtc.delete(paramInt);
    Trace.asyncTraceEnd(4096L, NAMES[paramInt], 0);
    logAction(paramInt, (int)(l1 - l2));
  }
  
  public void onActionStart(int paramInt)
  {
    if (!mEnabled) {
      return;
    }
    Trace.asyncTraceBegin(4096L, NAMES[paramInt], 0);
    mStartRtc.put(paramInt, SystemClock.elapsedRealtime());
  }
}
