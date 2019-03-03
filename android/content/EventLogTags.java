package android.content;

import android.util.EventLog;

public class EventLogTags
{
  public static final int BINDER_SAMPLE = 52004;
  public static final int CONTENT_QUERY_SAMPLE = 52002;
  public static final int CONTENT_UPDATE_SAMPLE = 52003;
  
  private EventLogTags() {}
  
  public static void writeBinderSample(String paramString1, int paramInt1, int paramInt2, String paramString2, int paramInt3)
  {
    EventLog.writeEvent(52004, new Object[] { paramString1, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramString2, Integer.valueOf(paramInt3) });
  }
  
  public static void writeContentQuerySample(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, String paramString5, int paramInt2)
  {
    EventLog.writeEvent(52002, new Object[] { paramString1, paramString2, paramString3, paramString4, Integer.valueOf(paramInt1), paramString5, Integer.valueOf(paramInt2) });
  }
  
  public static void writeContentUpdateSample(String paramString1, String paramString2, String paramString3, int paramInt1, String paramString4, int paramInt2)
  {
    EventLog.writeEvent(52003, new Object[] { paramString1, paramString2, paramString3, Integer.valueOf(paramInt1), paramString4, Integer.valueOf(paramInt2) });
  }
}
