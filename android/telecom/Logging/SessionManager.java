package android.telecom.Logging;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.telecom.Log;
import android.util.Base64;
import com.android.internal.annotations.VisibleForTesting;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager
{
  private static final long DEFAULT_SESSION_TIMEOUT_MS = 30000L;
  private static final String LOGGING_TAG = "Logging";
  private static final long SESSION_ID_ROLLOVER_THRESHOLD = 262144L;
  private static final String TIMEOUTS_PREFIX = "telecom.";
  @VisibleForTesting
  public Runnable mCleanStaleSessions = new _..Lambda.SessionManager.VyH2gT1EjIvzDy_C9JfTT60CISM(this);
  private Context mContext;
  @VisibleForTesting
  public ICurrentThreadId mCurrentThreadId = _..Lambda.L5F_SL2jOCUETYvgdB36aGwY50E.INSTANCE;
  private Handler mSessionCleanupHandler = new Handler(Looper.getMainLooper());
  private ISessionCleanupTimeoutMs mSessionCleanupTimeoutMs = new _..Lambda.SessionManager.hhtZwTEbvO_fLNlAvB6Do9_2gW4(this);
  private List<ISessionListener> mSessionListeners = new ArrayList();
  @VisibleForTesting
  public ConcurrentHashMap<Integer, Session> mSessionMapper = new ConcurrentHashMap(100);
  private int sCodeEntryCounter = 0;
  
  public SessionManager() {}
  
  private Session createSubsession(boolean paramBoolean)
  {
    try
    {
      int i = getCallingThreadId();
      Object localObject1 = (Session)mSessionMapper.get(Integer.valueOf(i));
      if (localObject1 == null)
      {
        Log.d("Logging", "Log.createSubsession was called with no session active.", new Object[0]);
        return null;
      }
      Session localSession = new android/telecom/Logging/Session;
      localSession.<init>(((Session)localObject1).getNextChildId(), ((Session)localObject1).getShortMethodName(), System.currentTimeMillis(), paramBoolean, null);
      ((Session)localObject1).addChild(localSession);
      localSession.setParentSession((Session)localObject1);
      if (!paramBoolean)
      {
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("CREATE_SUBSESSION ");
        ((StringBuilder)localObject1).append(localSession.toString());
        Log.v("Logging", ((StringBuilder)localObject1).toString(), new Object[0]);
      }
      else
      {
        Log.v("Logging", "CREATE_SUBSESSION (Invisible subsession)", new Object[0]);
      }
      return localSession;
    }
    finally {}
  }
  
  private void endParentSessions(Session paramSession)
  {
    if ((paramSession.isSessionCompleted()) && (paramSession.getChildSessions().size() == 0))
    {
      Object localObject = paramSession.getParentSession();
      long l2;
      if (localObject != null)
      {
        paramSession.setParentSession(null);
        ((Session)localObject).removeChild(paramSession);
        if (((Session)localObject).isExternal())
        {
          long l1 = System.currentTimeMillis();
          l2 = paramSession.getExecutionStartTimeMilliseconds();
          notifySessionCompleteListeners(paramSession.getShortMethodName(), l1 - l2);
        }
        endParentSessions((Session)localObject);
      }
      else
      {
        l2 = System.currentTimeMillis() - paramSession.getExecutionStartTimeMilliseconds();
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("END_SESSION (dur: ");
        ((StringBuilder)localObject).append(l2);
        ((StringBuilder)localObject).append(" ms): ");
        ((StringBuilder)localObject).append(paramSession.toString());
        Log.d("Logging", ((StringBuilder)localObject).toString(), new Object[0]);
        if (!paramSession.isExternal()) {
          notifySessionCompleteListeners(paramSession.getShortMethodName(), l2);
        }
      }
      return;
    }
  }
  
  private String getBase64Encoding(int paramInt)
  {
    return Base64.encodeToString(Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(paramInt).array(), 2, 4), 3);
  }
  
  private int getCallingThreadId()
  {
    return mCurrentThreadId.get();
  }
  
  private long getCleanupTimeout(Context paramContext)
  {
    return Settings.Secure.getLong(paramContext.getContentResolver(), "telecom.stale_session_cleanup_timeout_millis", 30000L);
  }
  
  private String getNextSessionID()
  {
    try
    {
      int i = sCodeEntryCounter;
      sCodeEntryCounter = (i + 1);
      Integer localInteger = Integer.valueOf(i);
      Object localObject1 = localInteger;
      if (localInteger.intValue() >= 262144L)
      {
        restartSessionCounter();
        i = sCodeEntryCounter;
        sCodeEntryCounter = (i + 1);
        localObject1 = Integer.valueOf(i);
      }
      localObject1 = getBase64Encoding(((Integer)localObject1).intValue());
      return localObject1;
    }
    finally {}
  }
  
  private long getSessionCleanupTimeoutMs()
  {
    return mSessionCleanupTimeoutMs.get();
  }
  
  private void notifySessionCompleteListeners(String paramString, long paramLong)
  {
    Iterator localIterator = mSessionListeners.iterator();
    while (localIterator.hasNext()) {
      ((ISessionListener)localIterator.next()).sessionComplete(paramString, paramLong);
    }
  }
  
  private void resetStaleSessionTimer()
  {
    try
    {
      mSessionCleanupHandler.removeCallbacksAndMessages(null);
      if (mCleanStaleSessions != null) {
        mSessionCleanupHandler.postDelayed(mCleanStaleSessions, getSessionCleanupTimeoutMs());
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  private void restartSessionCounter()
  {
    try
    {
      sCodeEntryCounter = 0;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void cancelSubsession(Session paramSession)
  {
    if (paramSession == null) {
      return;
    }
    try
    {
      paramSession.markSessionCompleted(-1L);
      endParentSessions(paramSession);
      return;
    }
    finally
    {
      paramSession = finally;
      throw paramSession;
    }
  }
  
  @VisibleForTesting
  public void cleanupStaleSessions(long paramLong)
  {
    Object localObject1 = "Stale Sessions Cleaned:\n";
    int i = 0;
    try
    {
      long l = System.currentTimeMillis();
      Iterator localIterator = mSessionMapper.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Session localSession = (Session)((Map.Entry)localIterator.next()).getValue();
        Object localObject3 = localObject1;
        if (l - localSession.getExecutionStartTimeMilliseconds() > paramLong)
        {
          localIterator.remove();
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append((String)localObject1);
          ((StringBuilder)localObject3).append(localSession.printFullSessionTree());
          ((StringBuilder)localObject3).append("\n");
          localObject3 = ((StringBuilder)localObject3).toString();
          i = 1;
        }
        localObject1 = localObject3;
      }
      if (i != 0) {
        Log.w("Logging", (String)localObject1, new Object[0]);
      } else {
        Log.v("Logging", "No stale logging sessions needed to be cleaned...", new Object[0]);
      }
      return;
    }
    finally {}
  }
  
  public void continueSession(Session paramSession, String paramString)
  {
    if (paramSession == null) {
      return;
    }
    try
    {
      resetStaleSessionTimer();
      paramSession.setShortMethodName(paramString);
      paramSession.setExecutionStartTimeMs(System.currentTimeMillis());
      if (paramSession.getParentSession() == null)
      {
        paramSession = new java/lang/StringBuilder;
        paramSession.<init>();
        paramSession.append("Log.continueSession was called with no session active for method ");
        paramSession.append(paramString);
        Log.i("Logging", paramSession.toString(), new Object[0]);
        return;
      }
      mSessionMapper.put(Integer.valueOf(getCallingThreadId()), paramSession);
      if (!paramSession.isStartedFromActiveSession())
      {
        Log.v("Logging", "CONTINUE_SUBSESSION", new Object[0]);
      }
      else
      {
        paramSession = new java/lang/StringBuilder;
        paramSession.<init>();
        paramSession.append("CONTINUE_SUBSESSION (Invisible Subsession) with Method ");
        paramSession.append(paramString);
        Log.v("Logging", paramSession.toString(), new Object[0]);
      }
      return;
    }
    finally {}
  }
  
  public Session createSubsession()
  {
    return createSubsession(false);
  }
  
  public void endSession()
  {
    try
    {
      int i = getCallingThreadId();
      Session localSession = (Session)mSessionMapper.get(Integer.valueOf(i));
      if (localSession == null)
      {
        Log.w("Logging", "Log.endSession was called with no session active.", new Object[0]);
        return;
      }
      localSession.markSessionCompleted(System.currentTimeMillis());
      if (!localSession.isStartedFromActiveSession())
      {
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("END_SUBSESSION (dur: ");
        ((StringBuilder)localObject2).append(localSession.getLocalExecutionTime());
        ((StringBuilder)localObject2).append(" mS)");
        Log.v("Logging", ((StringBuilder)localObject2).toString(), new Object[0]);
      }
      else
      {
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("END_SUBSESSION (Invisible Subsession) (dur: ");
        ((StringBuilder)localObject2).append(localSession.getLocalExecutionTime());
        ((StringBuilder)localObject2).append(" ms)");
        Log.v("Logging", ((StringBuilder)localObject2).toString(), new Object[0]);
      }
      Object localObject2 = localSession.getParentSession();
      mSessionMapper.remove(Integer.valueOf(i));
      endParentSessions(localSession);
      if ((localObject2 != null) && (!((Session)localObject2).isSessionCompleted()) && (localSession.isStartedFromActiveSession())) {
        mSessionMapper.put(Integer.valueOf(i), localObject2);
      }
      return;
    }
    finally {}
  }
  
  public Session.Info getExternalSession()
  {
    try
    {
      int i = getCallingThreadId();
      Object localObject1 = (Session)mSessionMapper.get(Integer.valueOf(i));
      if (localObject1 == null)
      {
        Log.d("Logging", "Log.getExternalSession was called with no session active.", new Object[0]);
        return null;
      }
      localObject1 = ((Session)localObject1).getInfo();
      return localObject1;
    }
    finally {}
  }
  
  public String getSessionId()
  {
    Object localObject = (Session)mSessionMapper.get(Integer.valueOf(getCallingThreadId()));
    if (localObject != null) {
      localObject = ((Session)localObject).toString();
    } else {
      localObject = "";
    }
    return localObject;
  }
  
  public void registerSessionListener(ISessionListener paramISessionListener)
  {
    if (paramISessionListener != null) {}
    try
    {
      mSessionListeners.add(paramISessionListener);
    }
    finally {}
  }
  
  public void setContext(Context paramContext)
  {
    mContext = paramContext;
  }
  
  public void startExternalSession(Session.Info paramInfo, String paramString)
  {
    if (paramInfo == null) {
      return;
    }
    try
    {
      int i = getCallingThreadId();
      if ((Session)mSessionMapper.get(Integer.valueOf(i)) != null)
      {
        Log.w("Logging", "trying to start an external session with a session already active.", new Object[0]);
        return;
      }
      Log.d("Logging", "START_EXTERNAL_SESSION", new Object[0]);
      Session localSession = new android/telecom/Logging/Session;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("E-");
      localStringBuilder.append(sessionId);
      localSession.<init>(localStringBuilder.toString(), methodPath, System.currentTimeMillis(), false, null);
      localSession.setIsExternal(true);
      localSession.markSessionCompleted(-1L);
      mSessionMapper.put(Integer.valueOf(i), localSession);
      continueSession(createSubsession(), paramString);
      return;
    }
    finally {}
  }
  
  public void startSession(Session.Info paramInfo, String paramString1, String paramString2)
  {
    if (paramInfo == null) {
      try
      {
        startSession(paramString1, paramString2);
      }
      finally
      {
        break label28;
      }
    } else {
      startExternalSession(paramInfo, paramString1);
    }
    return;
    label28:
    throw paramInfo;
  }
  
  public void startSession(String paramString1, String paramString2)
  {
    try
    {
      resetStaleSessionTimer();
      int i = getCallingThreadId();
      if ((Session)mSessionMapper.get(Integer.valueOf(i)) != null)
      {
        continueSession(createSubsession(true), paramString1);
        return;
      }
      Log.d("Logging", "START_SESSION", new Object[0]);
      Session localSession = new android/telecom/Logging/Session;
      localSession.<init>(getNextSessionID(), paramString1, System.currentTimeMillis(), false, paramString2);
      mSessionMapper.put(Integer.valueOf(i), localSession);
      return;
    }
    finally {}
  }
  
  public static abstract interface ICurrentThreadId
  {
    public abstract int get();
  }
  
  private static abstract interface ISessionCleanupTimeoutMs
  {
    public abstract long get();
  }
  
  public static abstract interface ISessionIdQueryHandler
  {
    public abstract String getSessionId();
  }
  
  public static abstract interface ISessionListener
  {
    public abstract void sessionComplete(String paramString, long paramLong);
  }
}
