package android.view.textclassifier;

import android.util.Slog;

final class Log
{
  private static final boolean ENABLE_FULL_LOGGING = false;
  
  private Log() {}
  
  public static void d(String paramString1, String paramString2)
  {
    Slog.d(paramString1, paramString2);
  }
  
  public static void e(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (paramThrowable != null) {
      paramThrowable = paramThrowable.getClass().getSimpleName();
    } else {
      paramThrowable = "??";
    }
    Slog.d(paramString1, String.format("%s (%s)", new Object[] { paramString2, paramThrowable }));
  }
  
  public static void w(String paramString1, String paramString2)
  {
    Slog.w(paramString1, paramString2);
  }
}
