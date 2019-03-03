package com.android.internal.logging;

import android.util.EventLog;

public class EventLogTags
{
  public static final int COMMIT_SYS_CONFIG_FILE = 525000;
  public static final int SYSUI_ACTION = 524288;
  public static final int SYSUI_COUNT = 524290;
  public static final int SYSUI_HISTOGRAM = 524291;
  public static final int SYSUI_LATENCY = 36070;
  public static final int SYSUI_MULTI_ACTION = 524292;
  public static final int SYSUI_VIEW_VISIBILITY = 524287;
  
  private EventLogTags() {}
  
  public static void writeCommitSysConfigFile(String paramString, long paramLong)
  {
    EventLog.writeEvent(525000, new Object[] { paramString, Long.valueOf(paramLong) });
  }
  
  public static void writeSysuiAction(int paramInt, String paramString)
  {
    EventLog.writeEvent(524288, new Object[] { Integer.valueOf(paramInt), paramString });
  }
  
  public static void writeSysuiCount(String paramString, int paramInt)
  {
    EventLog.writeEvent(524290, new Object[] { paramString, Integer.valueOf(paramInt) });
  }
  
  public static void writeSysuiHistogram(String paramString, int paramInt)
  {
    EventLog.writeEvent(524291, new Object[] { paramString, Integer.valueOf(paramInt) });
  }
  
  public static void writeSysuiLatency(int paramInt1, int paramInt2)
  {
    EventLog.writeEvent(36070, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
  }
  
  public static void writeSysuiMultiAction(Object[] paramArrayOfObject)
  {
    EventLog.writeEvent(524292, paramArrayOfObject);
  }
  
  public static void writeSysuiViewVisibility(int paramInt1, int paramInt2)
  {
    EventLog.writeEvent(524287, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
  }
}
