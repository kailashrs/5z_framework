package android.os;

import android.util.EventLog;

public class EventLogTags
{
  public static final int SERVICE_MANAGER_SLOW = 230001;
  public static final int SERVICE_MANAGER_STATS = 230000;
  
  private EventLogTags() {}
  
  public static void writeServiceManagerSlow(int paramInt, String paramString)
  {
    EventLog.writeEvent(230001, new Object[] { Integer.valueOf(paramInt), paramString });
  }
  
  public static void writeServiceManagerStats(int paramInt1, int paramInt2, int paramInt3)
  {
    EventLog.writeEvent(230000, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
  }
}
