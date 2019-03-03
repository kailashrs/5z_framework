package android.util;

import android.os.DeadSystemException;
import com.android.internal.os.RuntimeInit;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.LineBreakBufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.UnknownHostException;

public final class Log
{
  public static final int ASSERT = 7;
  public static final int DEBUG = 3;
  public static final int ERROR = 6;
  public static final int INFO = 4;
  public static final int LOG_ID_CRASH = 4;
  public static final int LOG_ID_EVENTS = 2;
  public static final int LOG_ID_MAIN = 0;
  public static final int LOG_ID_RADIO = 1;
  public static final int LOG_ID_SYSTEM = 3;
  public static final int VERBOSE = 2;
  public static final int WARN = 5;
  private static TerribleFailureHandler sWtfHandler = new TerribleFailureHandler()
  {
    public void onTerribleFailure(String paramAnonymousString, Log.TerribleFailure paramAnonymousTerribleFailure, boolean paramAnonymousBoolean)
    {
      RuntimeInit.wtf(paramAnonymousString, paramAnonymousTerribleFailure, paramAnonymousBoolean);
    }
  };
  
  private Log() {}
  
  public static int d(String paramString1, String paramString2)
  {
    return println_native(0, 3, paramString1, paramString2);
  }
  
  public static int d(String paramString1, String paramString2, Throwable paramThrowable)
  {
    return printlns(0, 3, paramString1, paramString2, paramThrowable);
  }
  
  public static int e(String paramString1, String paramString2)
  {
    return println_native(0, 6, paramString1, paramString2);
  }
  
  public static int e(String paramString1, String paramString2, Throwable paramThrowable)
  {
    return printlns(0, 6, paramString1, paramString2, paramThrowable);
  }
  
  public static String getStackTraceString(Throwable paramThrowable)
  {
    if (paramThrowable == null) {
      return "";
    }
    for (Object localObject = paramThrowable; localObject != null; localObject = ((Throwable)localObject).getCause()) {
      if ((localObject instanceof UnknownHostException)) {
        return "";
      }
    }
    localObject = new StringWriter();
    FastPrintWriter localFastPrintWriter = new FastPrintWriter((Writer)localObject, false, 256);
    paramThrowable.printStackTrace(localFastPrintWriter);
    localFastPrintWriter.flush();
    return ((StringWriter)localObject).toString();
  }
  
  public static int i(String paramString1, String paramString2)
  {
    return println_native(0, 4, paramString1, paramString2);
  }
  
  public static int i(String paramString1, String paramString2, Throwable paramThrowable)
  {
    return printlns(0, 4, paramString1, paramString2, paramThrowable);
  }
  
  public static native boolean isLoggable(String paramString, int paramInt);
  
  private static native int logger_entry_max_payload_native();
  
  public static int println(int paramInt, String paramString1, String paramString2)
  {
    return println_native(0, paramInt, paramString1, paramString2);
  }
  
  public static native int println_native(int paramInt1, int paramInt2, String paramString1, String paramString2);
  
  public static int printlns(int paramInt1, int paramInt2, String paramString1, String paramString2, Throwable paramThrowable)
  {
    ImmediateLogWriter localImmediateLogWriter = new ImmediateLogWriter(paramInt1, paramInt2, paramString1);
    paramInt2 = PreloadHolder.LOGGER_ENTRY_MAX_PAYLOAD;
    if (paramString1 != null) {
      paramInt1 = paramString1.length();
    } else {
      paramInt1 = 0;
    }
    LineBreakBufferedWriter localLineBreakBufferedWriter = new LineBreakBufferedWriter(localImmediateLogWriter, Math.max(paramInt2 - 2 - paramInt1 - 32, 100));
    localLineBreakBufferedWriter.println(paramString2);
    if (paramThrowable != null)
    {
      for (paramString1 = paramThrowable; (paramString1 != null) && (!(paramString1 instanceof UnknownHostException)); paramString1 = paramString1.getCause()) {
        if ((paramString1 instanceof DeadSystemException))
        {
          localLineBreakBufferedWriter.println("DeadSystemException: The system died; earlier logs will point to the root cause");
          break;
        }
      }
      if (paramString1 == null) {
        paramThrowable.printStackTrace(localLineBreakBufferedWriter);
      }
    }
    localLineBreakBufferedWriter.flush();
    return localImmediateLogWriter.getWritten();
  }
  
  public static TerribleFailureHandler setWtfHandler(TerribleFailureHandler paramTerribleFailureHandler)
  {
    if (paramTerribleFailureHandler != null)
    {
      TerribleFailureHandler localTerribleFailureHandler = sWtfHandler;
      sWtfHandler = paramTerribleFailureHandler;
      return localTerribleFailureHandler;
    }
    throw new NullPointerException("handler == null");
  }
  
  public static int v(String paramString1, String paramString2)
  {
    return println_native(0, 2, paramString1, paramString2);
  }
  
  public static int v(String paramString1, String paramString2, Throwable paramThrowable)
  {
    return printlns(0, 2, paramString1, paramString2, paramThrowable);
  }
  
  public static int w(String paramString1, String paramString2)
  {
    return println_native(0, 5, paramString1, paramString2);
  }
  
  public static int w(String paramString1, String paramString2, Throwable paramThrowable)
  {
    return printlns(0, 5, paramString1, paramString2, paramThrowable);
  }
  
  public static int w(String paramString, Throwable paramThrowable)
  {
    return printlns(0, 5, paramString, "", paramThrowable);
  }
  
  static int wtf(int paramInt, String paramString1, String paramString2, Throwable paramThrowable, boolean paramBoolean1, boolean paramBoolean2)
  {
    TerribleFailure localTerribleFailure = new TerribleFailure(paramString2, paramThrowable);
    if (paramBoolean1) {
      paramThrowable = localTerribleFailure;
    }
    paramInt = printlns(paramInt, 6, paramString1, paramString2, paramThrowable);
    sWtfHandler.onTerribleFailure(paramString1, localTerribleFailure, paramBoolean2);
    return paramInt;
  }
  
  public static int wtf(String paramString1, String paramString2)
  {
    return wtf(0, paramString1, paramString2, null, false, false);
  }
  
  public static int wtf(String paramString1, String paramString2, Throwable paramThrowable)
  {
    return wtf(0, paramString1, paramString2, paramThrowable, false, false);
  }
  
  public static int wtf(String paramString, Throwable paramThrowable)
  {
    return wtf(0, paramString, paramThrowable.getMessage(), paramThrowable, false, false);
  }
  
  static void wtfQuiet(int paramInt, String paramString1, String paramString2, boolean paramBoolean)
  {
    paramString2 = new TerribleFailure(paramString2, null);
    sWtfHandler.onTerribleFailure(paramString1, paramString2, paramBoolean);
  }
  
  public static int wtfStack(String paramString1, String paramString2)
  {
    return wtf(0, paramString1, paramString2, null, true, false);
  }
  
  private static class ImmediateLogWriter
    extends Writer
  {
    private int bufID;
    private int priority;
    private String tag;
    private int written = 0;
    
    public ImmediateLogWriter(int paramInt1, int paramInt2, String paramString)
    {
      bufID = paramInt1;
      priority = paramInt2;
      tag = paramString;
    }
    
    public void close() {}
    
    public void flush() {}
    
    public int getWritten()
    {
      return written;
    }
    
    public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    {
      written += Log.println_native(bufID, priority, tag, new String(paramArrayOfChar, paramInt1, paramInt2));
    }
  }
  
  static class PreloadHolder
  {
    public static final int LOGGER_ENTRY_MAX_PAYLOAD = ;
    
    PreloadHolder() {}
  }
  
  public static class TerribleFailure
    extends Exception
  {
    TerribleFailure(String paramString, Throwable paramThrowable)
    {
      super(paramThrowable);
    }
  }
  
  public static abstract interface TerribleFailureHandler
  {
    public abstract void onTerribleFailure(String paramString, Log.TerribleFailure paramTerribleFailure, boolean paramBoolean);
  }
}
