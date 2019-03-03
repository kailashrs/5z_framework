package android.speech.tts;

import android.util.EventLog;

public class EventLogTags
{
  public static final int TTS_SPEAK_FAILURE = 76002;
  public static final int TTS_SPEAK_SUCCESS = 76001;
  public static final int TTS_V2_SPEAK_FAILURE = 76004;
  public static final int TTS_V2_SPEAK_SUCCESS = 76003;
  
  private EventLogTags() {}
  
  public static void writeTtsSpeakFailure(String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2, int paramInt4, int paramInt5)
  {
    EventLog.writeEvent(76002, new Object[] { paramString1, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), paramString2, Integer.valueOf(paramInt4), Integer.valueOf(paramInt5) });
  }
  
  public static void writeTtsSpeakSuccess(String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2, int paramInt4, int paramInt5, long paramLong1, long paramLong2, long paramLong3)
  {
    EventLog.writeEvent(76001, new Object[] { paramString1, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), paramString2, Integer.valueOf(paramInt4), Integer.valueOf(paramInt5), Long.valueOf(paramLong1), Long.valueOf(paramLong2), Long.valueOf(paramLong3) });
  }
  
  public static void writeTtsV2SpeakFailure(String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2, int paramInt4)
  {
    EventLog.writeEvent(76004, new Object[] { paramString1, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), paramString2, Integer.valueOf(paramInt4) });
  }
  
  public static void writeTtsV2SpeakSuccess(String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2, long paramLong1, long paramLong2, long paramLong3)
  {
    EventLog.writeEvent(76003, new Object[] { paramString1, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), paramString2, Long.valueOf(paramLong1), Long.valueOf(paramLong2), Long.valueOf(paramLong3) });
  }
}
