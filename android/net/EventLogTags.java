package android.net;

import android.util.EventLog;

public class EventLogTags
{
  public static final int NTP_FAILURE = 50081;
  public static final int NTP_SUCCESS = 50080;
  
  private EventLogTags() {}
  
  public static void writeNtpFailure(String paramString1, String paramString2)
  {
    EventLog.writeEvent(50081, new Object[] { paramString1, paramString2 });
  }
  
  public static void writeNtpSuccess(String paramString, long paramLong1, long paramLong2)
  {
    EventLog.writeEvent(50080, new Object[] { paramString, Long.valueOf(paramLong1), Long.valueOf(paramLong2) });
  }
}
