package com.android.internal.logging;

import android.content.Context;
import android.metrics.LogMaker;
import android.os.Build;

public class MetricsLogger
{
  public static final int ASUS_COVER = 300;
  public static final int LOGTAG = 524292;
  public static final int VIEW_UNKNOWN = 0;
  private static MetricsLogger sMetricsLogger;
  
  public MetricsLogger() {}
  
  @Deprecated
  public static void action(Context paramContext, int paramInt)
  {
    getLogger().action(paramInt);
  }
  
  @Deprecated
  public static void action(Context paramContext, int paramInt1, int paramInt2)
  {
    getLogger().action(paramInt1, paramInt2);
  }
  
  @Deprecated
  public static void action(Context paramContext, int paramInt, String paramString)
  {
    getLogger().action(paramInt, paramString);
  }
  
  @Deprecated
  public static void action(Context paramContext, int paramInt, boolean paramBoolean)
  {
    getLogger().action(paramInt, paramBoolean);
  }
  
  @Deprecated
  public static void action(LogMaker paramLogMaker)
  {
    getLogger().write(paramLogMaker);
  }
  
  @Deprecated
  public static void count(Context paramContext, String paramString, int paramInt)
  {
    getLogger().count(paramString, paramInt);
  }
  
  private static MetricsLogger getLogger()
  {
    if (sMetricsLogger == null) {
      sMetricsLogger = new MetricsLogger();
    }
    return sMetricsLogger;
  }
  
  @Deprecated
  public static void hidden(Context paramContext, int paramInt)
    throws IllegalArgumentException
  {
    getLogger().hidden(paramInt);
  }
  
  @Deprecated
  public static void histogram(Context paramContext, String paramString, int paramInt)
  {
    getLogger().histogram(paramString, paramInt);
  }
  
  @Deprecated
  public static void visibility(Context paramContext, int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    boolean bool;
    if (paramInt2 == 0) {
      bool = true;
    } else {
      bool = false;
    }
    visibility(paramContext, paramInt1, bool);
  }
  
  @Deprecated
  public static void visibility(Context paramContext, int paramInt, boolean paramBoolean)
    throws IllegalArgumentException
  {
    getLogger().visibility(paramInt, paramBoolean);
  }
  
  @Deprecated
  public static void visible(Context paramContext, int paramInt)
    throws IllegalArgumentException
  {
    getLogger().visible(paramInt);
  }
  
  public void action(int paramInt)
  {
    EventLogTags.writeSysuiAction(paramInt, "");
    saveLog(new LogMaker(paramInt).setType(4).serialize());
  }
  
  public void action(int paramInt1, int paramInt2)
  {
    EventLogTags.writeSysuiAction(paramInt1, Integer.toString(paramInt2));
    saveLog(new LogMaker(paramInt1).setType(4).setSubtype(paramInt2).serialize());
  }
  
  public void action(int paramInt, String paramString)
  {
    if ((Build.IS_DEBUGGABLE) && (paramInt == 0)) {
      throw new IllegalArgumentException("Must define metric category");
    }
    EventLogTags.writeSysuiAction(paramInt, paramString);
    saveLog(new LogMaker(paramInt).setType(4).setPackageName(paramString).serialize());
  }
  
  public void action(int paramInt, boolean paramBoolean)
  {
    EventLogTags.writeSysuiAction(paramInt, Boolean.toString(paramBoolean));
    saveLog(new LogMaker(paramInt).setType(4).setSubtype(paramBoolean).serialize());
  }
  
  public void count(String paramString, int paramInt)
  {
    EventLogTags.writeSysuiCount(paramString, paramInt);
    saveLog(new LogMaker(803).setCounterName(paramString).setCounterValue(paramInt).serialize());
  }
  
  public void hidden(int paramInt)
    throws IllegalArgumentException
  {
    if ((Build.IS_DEBUGGABLE) && (paramInt == 0)) {
      throw new IllegalArgumentException("Must define metric category");
    }
    EventLogTags.writeSysuiViewVisibility(paramInt, 0);
    saveLog(new LogMaker(paramInt).setType(2).serialize());
  }
  
  public void histogram(String paramString, int paramInt)
  {
    EventLogTags.writeSysuiHistogram(paramString, paramInt);
    saveLog(new LogMaker(804).setCounterName(paramString).setCounterBucket(paramInt).setCounterValue(1).serialize());
  }
  
  protected void saveLog(Object[] paramArrayOfObject)
  {
    EventLogTags.writeSysuiMultiAction(paramArrayOfObject);
  }
  
  public void visibility(int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    boolean bool;
    if (paramInt2 == 0) {
      bool = true;
    } else {
      bool = false;
    }
    visibility(paramInt1, bool);
  }
  
  public void visibility(int paramInt, boolean paramBoolean)
    throws IllegalArgumentException
  {
    if (paramBoolean) {
      visible(paramInt);
    } else {
      hidden(paramInt);
    }
  }
  
  public void visible(int paramInt)
    throws IllegalArgumentException
  {
    if ((Build.IS_DEBUGGABLE) && (paramInt == 0)) {
      throw new IllegalArgumentException("Must define metric category");
    }
    EventLogTags.writeSysuiViewVisibility(paramInt, 100);
    saveLog(new LogMaker(paramInt).setType(1).serialize());
  }
  
  public void write(LogMaker paramLogMaker)
  {
    if (paramLogMaker.getType() == 0) {
      paramLogMaker.setType(4);
    }
    saveLog(paramLogMaker.serialize());
  }
}
