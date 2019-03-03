package com.android.internal.logging;

import android.util.Log;
import com.android.internal.util.FastPrintWriter;
import dalvik.system.DalvikLogHandler;
import dalvik.system.DalvikLogging;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AndroidHandler
  extends Handler
  implements DalvikLogHandler
{
  private static final Formatter THE_FORMATTER = new Formatter()
  {
    public String format(LogRecord paramAnonymousLogRecord)
    {
      Throwable localThrowable = paramAnonymousLogRecord.getThrown();
      if (localThrowable != null)
      {
        StringWriter localStringWriter = new StringWriter();
        FastPrintWriter localFastPrintWriter = new FastPrintWriter(localStringWriter, false, 256);
        localStringWriter.write(paramAnonymousLogRecord.getMessage());
        localStringWriter.write("\n");
        localThrowable.printStackTrace(localFastPrintWriter);
        localFastPrintWriter.flush();
        return localStringWriter.toString();
      }
      return paramAnonymousLogRecord.getMessage();
    }
  };
  
  public AndroidHandler()
  {
    setFormatter(THE_FORMATTER);
  }
  
  static int getAndroidLevel(Level paramLevel)
  {
    int i = paramLevel.intValue();
    if (i >= 1000) {
      return 6;
    }
    if (i >= 900) {
      return 5;
    }
    if (i >= 800) {
      return 4;
    }
    return 3;
  }
  
  public void close() {}
  
  public void flush() {}
  
  public void publish(LogRecord paramLogRecord)
  {
    int i = getAndroidLevel(paramLogRecord.getLevel());
    String str = DalvikLogging.loggerNameToTag(paramLogRecord.getLoggerName());
    if (!Log.isLoggable(str, i)) {
      return;
    }
    try
    {
      Log.println(i, str, getFormatter().format(paramLogRecord));
    }
    catch (RuntimeException paramLogRecord)
    {
      Log.e("AndroidHandler", "Error logging message.", paramLogRecord);
    }
  }
  
  public void publish(Logger paramLogger, String paramString1, Level paramLevel, String paramString2)
  {
    int i = getAndroidLevel(paramLevel);
    if (!Log.isLoggable(paramString1, i)) {
      return;
    }
    try
    {
      Log.println(i, paramString1, paramString2);
    }
    catch (RuntimeException paramLogger)
    {
      Log.e("AndroidHandler", "Error logging message.", paramLogger);
    }
  }
}
