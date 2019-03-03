package android.telecom;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.telecom.Logging.EventManager;
import android.telecom.Logging.EventManager.EventListener;
import android.telecom.Logging.EventManager.Loggable;
import android.telecom.Logging.EventManager.TimedEventPair;
import android.telecom.Logging.Session;
import android.telecom.Logging.Session.Info;
import android.telecom.Logging.SessionManager;
import android.telecom.Logging.SessionManager.ISessionListener;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import java.util.IllegalFormatException;
import java.util.Locale;

public class Log
{
  public static boolean DEBUG = false;
  public static boolean ERROR = false;
  private static final int EVENTS_TO_CACHE = 10;
  private static final int EVENTS_TO_CACHE_DEBUG = 20;
  private static final long EXTENDED_LOGGING_DURATION_MILLIS = 1800000L;
  private static final boolean FORCE_LOGGING = false;
  public static boolean INFO;
  @VisibleForTesting
  public static String TAG = "TelecomFramework";
  private static final boolean USER_BUILD;
  public static boolean VERBOSE;
  public static boolean WARN;
  private static EventManager sEventManager;
  private static boolean sIsUserExtendedLoggingEnabled = false;
  private static SessionManager sSessionManager;
  private static final Object sSingletonSync;
  private static long sUserExtendedLoggingStopTime = 0L;
  
  static
  {
    DEBUG = isLoggable(3);
    INFO = isLoggable(4);
    VERBOSE = isLoggable(2);
    WARN = isLoggable(5);
    ERROR = isLoggable(6);
    USER_BUILD = Build.IS_USER;
    sSingletonSync = new Object();
  }
  
  private Log() {}
  
  public static void addEvent(EventManager.Loggable paramLoggable, String paramString)
  {
    getEventManager().event(paramLoggable, paramString, null);
  }
  
  public static void addEvent(EventManager.Loggable paramLoggable, String paramString, Object paramObject)
  {
    getEventManager().event(paramLoggable, paramString, paramObject);
  }
  
  public static void addEvent(EventManager.Loggable paramLoggable, String paramString1, String paramString2, Object... paramVarArgs)
  {
    getEventManager().event(paramLoggable, paramString1, paramString2, paramVarArgs);
  }
  
  public static void addRequestResponsePair(EventManager.TimedEventPair paramTimedEventPair)
  {
    getEventManager().addRequestResponsePair(paramTimedEventPair);
  }
  
  private static String buildMessage(String paramString1, String paramString2, Object... paramVarArgs)
  {
    String str = getSessionId();
    Object localObject;
    if (TextUtils.isEmpty(str))
    {
      localObject = "";
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(": ");
      ((StringBuilder)localObject).append(str);
      localObject = ((StringBuilder)localObject).toString();
    }
    if (paramVarArgs != null) {
      try
      {
        if (paramVarArgs.length != 0)
        {
          str = String.format(Locale.US, paramString2, paramVarArgs);
          paramString2 = str;
        }
      }
      catch (IllegalFormatException localIllegalFormatException)
      {
        e(TAG, localIllegalFormatException, "Log: IllegalFormatException: formatString='%s' numArgs=%d", new Object[] { paramString2, Integer.valueOf(paramVarArgs.length) });
        paramVarArgs = new StringBuilder();
        paramVarArgs.append(paramString2);
        paramVarArgs.append(" (An error occurred while formatting the message.)");
        paramString2 = paramVarArgs.toString();
      }
    }
    return String.format(Locale.US, "%s: %s%s", new Object[] { paramString1, paramString2, localObject });
  }
  
  public static void cancelSubsession(Session paramSession)
  {
    getSessionManager().cancelSubsession(paramSession);
  }
  
  public static void continueSession(Session paramSession, String paramString)
  {
    getSessionManager().continueSession(paramSession, paramString);
  }
  
  public static Session createSubsession()
  {
    return getSessionManager().createSubsession();
  }
  
  public static void d(Object paramObject, String paramString, Object... paramVarArgs)
  {
    if (sIsUserExtendedLoggingEnabled)
    {
      maybeDisableLogging();
      Slog.i(TAG, buildMessage(getPrefixFromObject(paramObject), paramString, paramVarArgs));
    }
    else if (DEBUG)
    {
      Slog.d(TAG, buildMessage(getPrefixFromObject(paramObject), paramString, paramVarArgs));
    }
  }
  
  public static void d(String paramString1, String paramString2, Object... paramVarArgs)
  {
    if (sIsUserExtendedLoggingEnabled)
    {
      maybeDisableLogging();
      Slog.i(TAG, buildMessage(paramString1, paramString2, paramVarArgs));
    }
    else if (DEBUG)
    {
      Slog.d(TAG, buildMessage(paramString1, paramString2, paramVarArgs));
    }
  }
  
  public static void dumpEvents(IndentingPrintWriter paramIndentingPrintWriter)
  {
    synchronized (sSingletonSync)
    {
      if (sEventManager != null) {
        getEventManager().dumpEvents(paramIndentingPrintWriter);
      } else {
        paramIndentingPrintWriter.println("No Historical Events Logged.");
      }
      return;
    }
  }
  
  public static void dumpEventsTimeline(IndentingPrintWriter paramIndentingPrintWriter)
  {
    synchronized (sSingletonSync)
    {
      if (sEventManager != null) {
        getEventManager().dumpEventsTimeline(paramIndentingPrintWriter);
      } else {
        paramIndentingPrintWriter.println("No Historical Events Logged.");
      }
      return;
    }
  }
  
  public static void e(Object paramObject, Throwable paramThrowable, String paramString, Object... paramVarArgs)
  {
    if (ERROR) {
      Slog.e(TAG, buildMessage(getPrefixFromObject(paramObject), paramString, paramVarArgs), paramThrowable);
    }
  }
  
  public static void e(String paramString1, Throwable paramThrowable, String paramString2, Object... paramVarArgs)
  {
    if (ERROR) {
      Slog.e(TAG, buildMessage(paramString1, paramString2, paramVarArgs), paramThrowable);
    }
  }
  
  public static void endSession()
  {
    getSessionManager().endSession();
  }
  
  private static EventManager getEventManager()
  {
    if (sEventManager == null) {
      synchronized (sSingletonSync)
      {
        if (sEventManager == null)
        {
          EventManager localEventManager = new android/telecom/Logging/EventManager;
          localEventManager.<init>(_..Lambda.qa4s1Fm2YuohEunaJUJcmJXDXG0.INSTANCE);
          sEventManager = localEventManager;
          localEventManager = sEventManager;
          return localEventManager;
        }
      }
    }
    return sEventManager;
  }
  
  public static Session.Info getExternalSession()
  {
    return getSessionManager().getExternalSession();
  }
  
  private static String getPrefixFromObject(Object paramObject)
  {
    if (paramObject == null) {
      paramObject = "<null>";
    } else {
      paramObject = paramObject.getClass().getSimpleName();
    }
    return paramObject;
  }
  
  public static String getSessionId()
  {
    synchronized (sSingletonSync)
    {
      if (sSessionManager != null)
      {
        String str = getSessionManager().getSessionId();
        return str;
      }
      return "";
    }
  }
  
  @VisibleForTesting
  public static SessionManager getSessionManager()
  {
    if (sSessionManager == null) {
      synchronized (sSingletonSync)
      {
        if (sSessionManager == null)
        {
          SessionManager localSessionManager = new android/telecom/Logging/SessionManager;
          localSessionManager.<init>();
          sSessionManager = localSessionManager;
          localSessionManager = sSessionManager;
          return localSessionManager;
        }
      }
    }
    return sSessionManager;
  }
  
  public static void i(Object paramObject, String paramString, Object... paramVarArgs)
  {
    if (INFO) {
      Slog.i(TAG, buildMessage(getPrefixFromObject(paramObject), paramString, paramVarArgs));
    }
  }
  
  public static void i(String paramString1, String paramString2, Object... paramVarArgs)
  {
    if (INFO) {
      Slog.i(TAG, buildMessage(paramString1, paramString2, paramVarArgs));
    }
  }
  
  public static boolean isLoggable(int paramInt)
  {
    return android.util.Log.isLoggable(TAG, paramInt);
  }
  
  private static void maybeDisableLogging()
  {
    if (!sIsUserExtendedLoggingEnabled) {
      return;
    }
    if (sUserExtendedLoggingStopTime < System.currentTimeMillis())
    {
      sUserExtendedLoggingStopTime = 0L;
      sIsUserExtendedLoggingEnabled = false;
    }
  }
  
  public static String pii(Object paramObject)
  {
    if ((paramObject != null) && (!VERBOSE)) {
      return "***";
    }
    return String.valueOf(paramObject);
  }
  
  public static String piiHandle(Object paramObject)
  {
    if ((paramObject != null) && (!VERBOSE))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      if ((paramObject instanceof Uri))
      {
        Object localObject = (Uri)paramObject;
        String str = ((Uri)localObject).getScheme();
        if (!TextUtils.isEmpty(str))
        {
          localStringBuilder.append(str);
          localStringBuilder.append(":");
        }
        localObject = ((Uri)localObject).getSchemeSpecificPart();
        boolean bool = "tel".equals(str);
        int i = 0;
        int j = 0;
        char c;
        if (bool) {
          while (j < ((String)localObject).length())
          {
            c = ((String)localObject).charAt(j);
            if (PhoneNumberUtils.isDialable(c)) {
              paramObject = "*";
            } else {
              paramObject = Character.valueOf(c);
            }
            localStringBuilder.append(paramObject);
            j++;
          }
        }
        if ("sip".equals(str)) {
          for (j = i; j < ((String)localObject).length(); j++)
          {
            i = ((String)localObject).charAt(j);
            c = i;
            if (i != 64)
            {
              c = i;
              if (i != 46)
              {
                i = 42;
                c = i;
              }
            }
            localStringBuilder.append(c);
          }
        }
        localStringBuilder.append(pii(paramObject));
      }
      return localStringBuilder.toString();
    }
    return String.valueOf(paramObject);
  }
  
  public static void registerEventListener(EventManager.EventListener paramEventListener)
  {
    getEventManager().registerEventListener(paramEventListener);
  }
  
  public static void registerSessionListener(SessionManager.ISessionListener paramISessionListener)
  {
    getSessionManager().registerSessionListener(paramISessionListener);
  }
  
  public static void setIsExtendedLoggingEnabled(boolean paramBoolean)
  {
    if (sIsUserExtendedLoggingEnabled == paramBoolean) {
      return;
    }
    if (sEventManager != null)
    {
      EventManager localEventManager = sEventManager;
      int i;
      if (paramBoolean) {
        i = 20;
      } else {
        i = 10;
      }
      localEventManager.changeEventCacheSize(i);
    }
    sIsUserExtendedLoggingEnabled = paramBoolean;
    if (sIsUserExtendedLoggingEnabled) {
      sUserExtendedLoggingStopTime = System.currentTimeMillis() + 1800000L;
    } else {
      sUserExtendedLoggingStopTime = 0L;
    }
  }
  
  public static void setSessionContext(Context paramContext)
  {
    getSessionManager().setContext(paramContext);
  }
  
  public static void setTag(String paramString)
  {
    TAG = paramString;
    DEBUG = isLoggable(3);
    INFO = isLoggable(4);
    VERBOSE = isLoggable(2);
    WARN = isLoggable(5);
    ERROR = isLoggable(6);
  }
  
  public static void startSession(Session.Info paramInfo, String paramString)
  {
    getSessionManager().startSession(paramInfo, paramString, null);
  }
  
  public static void startSession(Session.Info paramInfo, String paramString1, String paramString2)
  {
    getSessionManager().startSession(paramInfo, paramString1, paramString2);
  }
  
  public static void startSession(String paramString)
  {
    getSessionManager().startSession(paramString, null);
  }
  
  public static void startSession(String paramString1, String paramString2)
  {
    getSessionManager().startSession(paramString1, paramString2);
  }
  
  public static void v(Object paramObject, String paramString, Object... paramVarArgs)
  {
    if (sIsUserExtendedLoggingEnabled)
    {
      maybeDisableLogging();
      Slog.i(TAG, buildMessage(getPrefixFromObject(paramObject), paramString, paramVarArgs));
    }
    else if (VERBOSE)
    {
      Slog.v(TAG, buildMessage(getPrefixFromObject(paramObject), paramString, paramVarArgs));
    }
  }
  
  public static void v(String paramString1, String paramString2, Object... paramVarArgs)
  {
    if (sIsUserExtendedLoggingEnabled)
    {
      maybeDisableLogging();
      Slog.i(TAG, buildMessage(paramString1, paramString2, paramVarArgs));
    }
    else if (VERBOSE)
    {
      Slog.v(TAG, buildMessage(paramString1, paramString2, paramVarArgs));
    }
  }
  
  public static void w(Object paramObject, String paramString, Object... paramVarArgs)
  {
    if (WARN) {
      Slog.w(TAG, buildMessage(getPrefixFromObject(paramObject), paramString, paramVarArgs));
    }
  }
  
  public static void w(String paramString1, String paramString2, Object... paramVarArgs)
  {
    if (WARN) {
      Slog.w(TAG, buildMessage(paramString1, paramString2, paramVarArgs));
    }
  }
  
  public static void wtf(Object paramObject, String paramString, Object... paramVarArgs)
  {
    paramObject = buildMessage(getPrefixFromObject(paramObject), paramString, paramVarArgs);
    Slog.wtf(TAG, paramObject, new IllegalStateException(paramObject));
  }
  
  public static void wtf(Object paramObject, Throwable paramThrowable, String paramString, Object... paramVarArgs)
  {
    Slog.wtf(TAG, buildMessage(getPrefixFromObject(paramObject), paramString, paramVarArgs), paramThrowable);
  }
  
  public static void wtf(String paramString1, String paramString2, Object... paramVarArgs)
  {
    paramString1 = buildMessage(paramString1, paramString2, paramVarArgs);
    Slog.wtf(TAG, paramString1, new IllegalStateException(paramString1));
  }
  
  public static void wtf(String paramString1, Throwable paramThrowable, String paramString2, Object... paramVarArgs)
  {
    Slog.wtf(TAG, buildMessage(paramString1, paramString2, paramVarArgs), paramThrowable);
  }
}
