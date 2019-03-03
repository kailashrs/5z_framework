package android.app.admin;

import android.util.EventLog;

public class SecurityLogTags
{
  public static final int SECURITY_ADB_SHELL_COMMAND = 210002;
  public static final int SECURITY_ADB_SHELL_INTERACTIVE = 210001;
  public static final int SECURITY_ADB_SYNC_RECV = 210003;
  public static final int SECURITY_ADB_SYNC_SEND = 210004;
  public static final int SECURITY_APP_PROCESS_START = 210005;
  public static final int SECURITY_CERT_AUTHORITY_INSTALLED = 210029;
  public static final int SECURITY_CERT_AUTHORITY_REMOVED = 210030;
  public static final int SECURITY_CERT_VALIDATION_FAILURE = 210033;
  public static final int SECURITY_CRYPTO_SELF_TEST_COMPLETED = 210031;
  public static final int SECURITY_KEYGUARD_DISABLED_FEATURES_SET = 210021;
  public static final int SECURITY_KEYGUARD_DISMISSED = 210006;
  public static final int SECURITY_KEYGUARD_DISMISS_AUTH_ATTEMPT = 210007;
  public static final int SECURITY_KEYGUARD_SECURED = 210008;
  public static final int SECURITY_KEY_DESTROYED = 210026;
  public static final int SECURITY_KEY_GENERATED = 210024;
  public static final int SECURITY_KEY_IMPORTED = 210025;
  public static final int SECURITY_KEY_INTEGRITY_VIOLATION = 210032;
  public static final int SECURITY_LOGGING_STARTED = 210011;
  public static final int SECURITY_LOGGING_STOPPED = 210012;
  public static final int SECURITY_LOG_BUFFER_SIZE_CRITICAL = 210015;
  public static final int SECURITY_MAX_PASSWORD_ATTEMPTS_SET = 210020;
  public static final int SECURITY_MAX_SCREEN_LOCK_TIMEOUT_SET = 210019;
  public static final int SECURITY_MEDIA_MOUNTED = 210013;
  public static final int SECURITY_MEDIA_UNMOUNTED = 210014;
  public static final int SECURITY_OS_SHUTDOWN = 210010;
  public static final int SECURITY_OS_STARTUP = 210009;
  public static final int SECURITY_PASSWORD_COMPLEXITY_SET = 210017;
  public static final int SECURITY_PASSWORD_EXPIRATION_SET = 210016;
  public static final int SECURITY_PASSWORD_HISTORY_LENGTH_SET = 210018;
  public static final int SECURITY_REMOTE_LOCK = 210022;
  public static final int SECURITY_USER_RESTRICTION_ADDED = 210027;
  public static final int SECURITY_USER_RESTRICTION_REMOVED = 210028;
  public static final int SECURITY_WIPE_FAILED = 210023;
  
  private SecurityLogTags() {}
  
  public static void writeSecurityAdbShellCommand(String paramString)
  {
    EventLog.writeEvent(210002, paramString);
  }
  
  public static void writeSecurityAdbShellInteractive()
  {
    EventLog.writeEvent(210001, new Object[0]);
  }
  
  public static void writeSecurityAdbSyncRecv(String paramString)
  {
    EventLog.writeEvent(210003, paramString);
  }
  
  public static void writeSecurityAdbSyncSend(String paramString)
  {
    EventLog.writeEvent(210004, paramString);
  }
  
  public static void writeSecurityAppProcessStart(String paramString1, long paramLong, int paramInt1, int paramInt2, String paramString2, String paramString3)
  {
    EventLog.writeEvent(210005, new Object[] { paramString1, Long.valueOf(paramLong), Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramString2, paramString3 });
  }
  
  public static void writeSecurityCertAuthorityInstalled(int paramInt, String paramString)
  {
    EventLog.writeEvent(210029, new Object[] { Integer.valueOf(paramInt), paramString });
  }
  
  public static void writeSecurityCertAuthorityRemoved(int paramInt, String paramString)
  {
    EventLog.writeEvent(210030, new Object[] { Integer.valueOf(paramInt), paramString });
  }
  
  public static void writeSecurityCertValidationFailure(String paramString)
  {
    EventLog.writeEvent(210033, paramString);
  }
  
  public static void writeSecurityCryptoSelfTestCompleted(int paramInt)
  {
    EventLog.writeEvent(210031, paramInt);
  }
  
  public static void writeSecurityKeyDestroyed(int paramInt1, String paramString, int paramInt2)
  {
    EventLog.writeEvent(210026, new Object[] { Integer.valueOf(paramInt1), paramString, Integer.valueOf(paramInt2) });
  }
  
  public static void writeSecurityKeyGenerated(int paramInt1, String paramString, int paramInt2)
  {
    EventLog.writeEvent(210024, new Object[] { Integer.valueOf(paramInt1), paramString, Integer.valueOf(paramInt2) });
  }
  
  public static void writeSecurityKeyImported(int paramInt1, String paramString, int paramInt2)
  {
    EventLog.writeEvent(210025, new Object[] { Integer.valueOf(paramInt1), paramString, Integer.valueOf(paramInt2) });
  }
  
  public static void writeSecurityKeyIntegrityViolation(String paramString, int paramInt)
  {
    EventLog.writeEvent(210032, new Object[] { paramString, Integer.valueOf(paramInt) });
  }
  
  public static void writeSecurityKeyguardDisabledFeaturesSet(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    EventLog.writeEvent(210021, new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
  }
  
  public static void writeSecurityKeyguardDismissAuthAttempt(int paramInt1, int paramInt2)
  {
    EventLog.writeEvent(210007, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
  }
  
  public static void writeSecurityKeyguardDismissed()
  {
    EventLog.writeEvent(210006, new Object[0]);
  }
  
  public static void writeSecurityKeyguardSecured()
  {
    EventLog.writeEvent(210008, new Object[0]);
  }
  
  public static void writeSecurityLogBufferSizeCritical()
  {
    EventLog.writeEvent(210015, new Object[0]);
  }
  
  public static void writeSecurityLoggingStarted()
  {
    EventLog.writeEvent(210011, new Object[0]);
  }
  
  public static void writeSecurityLoggingStopped()
  {
    EventLog.writeEvent(210012, new Object[0]);
  }
  
  public static void writeSecurityMaxPasswordAttemptsSet(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    EventLog.writeEvent(210020, new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
  }
  
  public static void writeSecurityMaxScreenLockTimeoutSet(String paramString, int paramInt1, int paramInt2, long paramLong)
  {
    EventLog.writeEvent(210019, new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Long.valueOf(paramLong) });
  }
  
  public static void writeSecurityMediaMounted(String paramString1, String paramString2)
  {
    EventLog.writeEvent(210013, new Object[] { paramString1, paramString2 });
  }
  
  public static void writeSecurityMediaUnmounted(String paramString1, String paramString2)
  {
    EventLog.writeEvent(210014, new Object[] { paramString1, paramString2 });
  }
  
  public static void writeSecurityOsShutdown()
  {
    EventLog.writeEvent(210010, new Object[0]);
  }
  
  public static void writeSecurityOsStartup(String paramString1, String paramString2)
  {
    EventLog.writeEvent(210009, new Object[] { paramString1, paramString2 });
  }
  
  public static void writeSecurityPasswordComplexitySet(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10)
  {
    EventLog.writeEvent(210017, new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), Integer.valueOf(paramInt4), Integer.valueOf(paramInt5), Integer.valueOf(paramInt6), Integer.valueOf(paramInt7), Integer.valueOf(paramInt8), Integer.valueOf(paramInt9), Integer.valueOf(paramInt10) });
  }
  
  public static void writeSecurityPasswordExpirationSet(String paramString, int paramInt1, int paramInt2, long paramLong)
  {
    EventLog.writeEvent(210016, new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Long.valueOf(paramLong) });
  }
  
  public static void writeSecurityPasswordHistoryLengthSet(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    EventLog.writeEvent(210018, new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
  }
  
  public static void writeSecurityRemoteLock(String paramString, int paramInt1, int paramInt2)
  {
    EventLog.writeEvent(210022, new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
  }
  
  public static void writeSecurityUserRestrictionAdded(String paramString1, int paramInt, String paramString2)
  {
    EventLog.writeEvent(210027, new Object[] { paramString1, Integer.valueOf(paramInt), paramString2 });
  }
  
  public static void writeSecurityUserRestrictionRemoved(String paramString1, int paramInt, String paramString2)
  {
    EventLog.writeEvent(210028, new Object[] { paramString1, Integer.valueOf(paramInt), paramString2 });
  }
  
  public static void writeSecurityWipeFailed(String paramString, int paramInt)
  {
    EventLog.writeEvent(210023, new Object[] { paramString, Integer.valueOf(paramInt) });
  }
}
