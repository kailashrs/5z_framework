package android.util;

public final class Slog
{
  private Slog() {}
  
  public static int d(String paramString1, String paramString2)
  {
    return Log.println_native(3, 3, paramString1, paramString2);
  }
  
  public static int d(String paramString1, String paramString2, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString2);
    localStringBuilder.append('\n');
    localStringBuilder.append(Log.getStackTraceString(paramThrowable));
    return Log.println_native(3, 3, paramString1, localStringBuilder.toString());
  }
  
  public static int e(String paramString1, String paramString2)
  {
    return Log.println_native(3, 6, paramString1, paramString2);
  }
  
  public static int e(String paramString1, String paramString2, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString2);
    localStringBuilder.append('\n');
    localStringBuilder.append(Log.getStackTraceString(paramThrowable));
    return Log.println_native(3, 6, paramString1, localStringBuilder.toString());
  }
  
  public static int i(String paramString1, String paramString2)
  {
    return Log.println_native(3, 4, paramString1, paramString2);
  }
  
  public static int i(String paramString1, String paramString2, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString2);
    localStringBuilder.append('\n');
    localStringBuilder.append(Log.getStackTraceString(paramThrowable));
    return Log.println_native(3, 4, paramString1, localStringBuilder.toString());
  }
  
  public static int println(int paramInt, String paramString1, String paramString2)
  {
    return Log.println_native(3, paramInt, paramString1, paramString2);
  }
  
  public static int v(String paramString1, String paramString2)
  {
    return Log.println_native(3, 2, paramString1, paramString2);
  }
  
  public static int v(String paramString1, String paramString2, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString2);
    localStringBuilder.append('\n');
    localStringBuilder.append(Log.getStackTraceString(paramThrowable));
    return Log.println_native(3, 2, paramString1, localStringBuilder.toString());
  }
  
  public static int w(String paramString1, String paramString2)
  {
    return Log.println_native(3, 5, paramString1, paramString2);
  }
  
  public static int w(String paramString1, String paramString2, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString2);
    localStringBuilder.append('\n');
    localStringBuilder.append(Log.getStackTraceString(paramThrowable));
    return Log.println_native(3, 5, paramString1, localStringBuilder.toString());
  }
  
  public static int w(String paramString, Throwable paramThrowable)
  {
    return Log.println_native(3, 5, paramString, Log.getStackTraceString(paramThrowable));
  }
  
  public static int wtf(String paramString1, String paramString2)
  {
    return Log.wtf(3, paramString1, paramString2, null, false, true);
  }
  
  public static int wtf(String paramString1, String paramString2, Throwable paramThrowable)
  {
    return Log.wtf(3, paramString1, paramString2, paramThrowable, false, true);
  }
  
  public static int wtf(String paramString, Throwable paramThrowable)
  {
    return Log.wtf(3, paramString, paramThrowable.getMessage(), paramThrowable, false, true);
  }
  
  public static void wtfQuiet(String paramString1, String paramString2)
  {
    Log.wtfQuiet(3, paramString1, paramString2, true);
  }
  
  public static int wtfStack(String paramString1, String paramString2)
  {
    return Log.wtf(3, paramString1, paramString2, null, true, true);
  }
}
