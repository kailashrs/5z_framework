package com.android.internal.widget;

import android.app.admin.DevicePolicyManager;
import android.app.admin.PasswordMetrics;
import android.app.trust.IStrongAuthTracker.Stub;
import android.app.trust.TrustManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.IStorageManager;
import android.os.storage.IStorageManager.Stub;
import android.os.storage.StorageManager;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import com.google.android.collect.Lists;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import libcore.util.HexEncoding;

public class LockPatternUtils
{
  @Deprecated
  public static final String BIOMETRIC_WEAK_EVER_CHOSEN_KEY = "lockscreen.biometricweakeverchosen";
  public static final int CREDENTIAL_TYPE_NONE = -1;
  public static final int CREDENTIAL_TYPE_PASSWORD = 2;
  public static final int CREDENTIAL_TYPE_PATTERN = 1;
  private static final boolean DEBUG = false;
  public static final String DISABLE_LOCKSCREEN_KEY = "lockscreen.disabled";
  private static final String ENABLED_TRUST_AGENTS = "lockscreen.enabledtrustagents";
  public static final int FAILED_ATTEMPTS_BEFORE_RESET = 20;
  public static final int FAILED_ATTEMPTS_BEFORE_WIPE_GRACE = 5;
  public static final long FAILED_ATTEMPT_COUNTDOWN_INTERVAL_MS = 1000L;
  private static final boolean FRP_CREDENTIAL_ENABLED = true;
  private static final String HISTORY_DELIMITER = ",";
  private static final String IS_TRUST_USUALLY_MANAGED = "lockscreen.istrustusuallymanaged";
  public static final String LEGACY_LOCK_PATTERN_ENABLED = "legacy_lock_pattern_enabled";
  @Deprecated
  public static final String LOCKOUT_PERMANENT_KEY = "lockscreen.lockedoutpermanently";
  @Deprecated
  public static final String LOCKSCREEN_BIOMETRIC_WEAK_FALLBACK = "lockscreen.biometric_weak_fallback";
  public static final String LOCKSCREEN_OPTIONS = "lockscreen.options";
  public static final String LOCKSCREEN_POWER_BUTTON_INSTANTLY_LOCKS = "lockscreen.power_button_instantly_locks";
  @Deprecated
  public static final String LOCKSCREEN_WIDGETS_ENABLED = "lockscreen.widgets_enabled";
  public static final String LOCK_PASSWORD_SALT_KEY = "lockscreen.password_salt";
  private static final String LOCK_SCREEN_DEVICE_OWNER_INFO = "lockscreen.device_owner_info";
  private static final String LOCK_SCREEN_OWNER_INFO = "lock_screen_owner_info";
  private static final String LOCK_SCREEN_OWNER_INFO_ENABLED = "lock_screen_owner_info_enabled";
  public static final int MIN_LOCK_PASSWORD_SIZE = 4;
  public static final int MIN_LOCK_PATTERN_SIZE = 4;
  public static final int MIN_PATTERN_REGISTER_FAIL = 4;
  public static final String PASSWORD_HISTORY_KEY = "lockscreen.passwordhistory";
  @Deprecated
  public static final String PASSWORD_TYPE_ALTERNATE_KEY = "lockscreen.password_type_alternate";
  public static final String PASSWORD_TYPE_KEY = "lockscreen.password_type";
  public static final String PATTERN_EVER_CHOSEN_KEY = "lockscreen.patterneverchosen";
  public static final String PROFILE_KEY_NAME_DECRYPT = "profile_key_name_decrypt_";
  public static final String PROFILE_KEY_NAME_ENCRYPT = "profile_key_name_encrypt_";
  public static final String SYNTHETIC_PASSWORD_ENABLED_KEY = "enable-sp";
  public static final String SYNTHETIC_PASSWORD_HANDLE_KEY = "sp-handle";
  public static final String SYNTHETIC_PASSWORD_KEY_PREFIX = "synthetic_password_";
  private static final String TAG = "LockPatternUtils";
  public static final int USER_FRP = -9999;
  private final ContentResolver mContentResolver;
  private final Context mContext;
  private DevicePolicyManager mDevicePolicyManager;
  private final Handler mHandler;
  private ILockSettings mLockSettingsService;
  private final SparseLongArray mLockoutDeadlines = new SparseLongArray();
  private UserManager mUserManager;
  
  public LockPatternUtils(Context paramContext)
  {
    mContext = paramContext;
    mContentResolver = paramContext.getContentResolver();
    paramContext = Looper.myLooper();
    if (paramContext != null) {
      paramContext = new Handler(paramContext);
    } else {
      paramContext = null;
    }
    mHandler = paramContext;
  }
  
  private boolean checkCredential(String paramString, int paramInt1, int paramInt2, CheckCredentialProgressCallback paramCheckCredentialProgressCallback)
    throws LockPatternUtils.RequestThrottledException
  {
    try
    {
      paramString = getLockSettings().checkCredential(paramString, paramInt1, paramInt2, wrapCallback(paramCheckCredentialProgressCallback));
      if (paramString.getResponseCode() == 0) {
        return true;
      }
      if (paramString.getResponseCode() != 1) {
        return false;
      }
      paramCheckCredentialProgressCallback = new com/android/internal/widget/LockPatternUtils$RequestThrottledException;
      paramCheckCredentialProgressCallback.<init>(paramString.getTimeout());
      throw paramCheckCredentialProgressCallback;
    }
    catch (RemoteException paramString) {}
    return false;
  }
  
  private int computePasswordQuality(int paramInt1, String paramString, int paramInt2)
  {
    if (paramInt1 == 2) {
      paramInt1 = Math.max(paramInt2, computeForPasswordquality);
    } else if (paramInt1 == 1) {
      paramInt1 = 65536;
    } else {
      paramInt1 = 0;
    }
    return paramInt1;
  }
  
  public static boolean frpCredentialEnabled(Context paramContext)
  {
    return paramContext.getResources().getBoolean(17956953);
  }
  
  private boolean getBoolean(String paramString, boolean paramBoolean, int paramInt)
  {
    try
    {
      boolean bool = getLockSettings().getBoolean(paramString, paramBoolean, paramInt);
      return bool;
    }
    catch (RemoteException paramString) {}
    return paramBoolean;
  }
  
  private LockSettingsInternal getLockSettingsInternal()
  {
    LockSettingsInternal localLockSettingsInternal = (LockSettingsInternal)LocalServices.getService(LockSettingsInternal.class);
    if (localLockSettingsInternal != null) {
      return localLockSettingsInternal;
    }
    throw new SecurityException("Only available to system server itself");
  }
  
  private long getLong(String paramString, long paramLong, int paramInt)
  {
    try
    {
      long l = getLockSettings().getLong(paramString, paramLong, paramInt);
      return l;
    }
    catch (RemoteException paramString) {}
    return paramLong;
  }
  
  private int getRequestedPasswordHistoryLength(int paramInt)
  {
    return getDevicePolicyManager().getPasswordHistoryLength(null, paramInt);
  }
  
  private String getSalt(int paramInt)
  {
    long l1 = getLong("lockscreen.password_salt", 0L, paramInt);
    long l2 = l1;
    if (l1 == 0L) {
      try
      {
        l2 = SecureRandom.getInstance("SHA1PRNG").nextLong();
        setLong("lockscreen.password_salt", l2, paramInt);
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Initialized lock password salt for user: ");
        localStringBuilder.append(paramInt);
        Log.v("LockPatternUtils", localStringBuilder.toString());
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        throw new IllegalStateException("Couldn't get SecureRandom number", localNoSuchAlgorithmException);
      }
    }
    return Long.toHexString(l2);
  }
  
  private String getString(String paramString, int paramInt)
  {
    try
    {
      paramString = getLockSettings().getString(paramString, null, paramInt);
      return paramString;
    }
    catch (RemoteException paramString) {}
    return null;
  }
  
  private TrustManager getTrustManager()
  {
    TrustManager localTrustManager = (TrustManager)mContext.getSystemService("trust");
    if (localTrustManager == null) {
      Log.e("LockPatternUtils", "Can't get TrustManagerService: is it running?", new IllegalStateException("Stack trace:"));
    }
    return localTrustManager;
  }
  
  private UserManager getUserManager()
  {
    if (mUserManager == null) {
      mUserManager = UserManager.get(mContext);
    }
    return mUserManager;
  }
  
  private boolean hasSeparateChallenge(int paramInt)
  {
    try
    {
      boolean bool = getLockSettings().getSeparateProfileChallengeEnabled(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("LockPatternUtils", "Couldn't get separate profile challenge enabled");
    }
    return false;
  }
  
  public static boolean isDeviceEncryptionEnabled()
  {
    return StorageManager.isEncrypted();
  }
  
  private boolean isDoNotAskCredentialsOnBootSet()
  {
    return getDevicePolicyManager().getDoNotAskCredentialsOnBoot();
  }
  
  public static boolean isFileEncryptionEnabled()
  {
    return StorageManager.isFileEncryptedNativeOrEmulated();
  }
  
  private boolean isLockPasswordEnabled(int paramInt1, int paramInt2)
  {
    boolean bool = true;
    if ((paramInt1 != 262144) && (paramInt1 != 131072) && (paramInt1 != 196608) && (paramInt1 != 327680) && (paramInt1 != 393216) && (paramInt1 != 524288)) {
      paramInt1 = 0;
    } else {
      paramInt1 = 1;
    }
    if ((paramInt1 == 0) || (!savedPasswordExists(paramInt2))) {
      bool = false;
    }
    return bool;
  }
  
  private boolean isLockPatternEnabled(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 == 65536) && (savedPatternExists(paramInt2))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isManagedProfile(int paramInt)
  {
    UserInfo localUserInfo = getUserManager().getUserInfo(paramInt);
    boolean bool;
    if ((localUserInfo != null) && (localUserInfo.isManagedProfile())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void onAfterChangingPassword(int paramInt)
  {
    getTrustManager().reportEnabledTrustAgentsChanged(paramInt);
  }
  
  private String passwordToHistoryHash(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    if ((!TextUtils.isEmpty(paramString)) && (paramArrayOfByte != null)) {
      try
      {
        MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-256");
        localMessageDigest.update(paramArrayOfByte);
        paramArrayOfByte = new java/lang/StringBuilder;
        paramArrayOfByte.<init>();
        paramArrayOfByte.append(paramString);
        paramArrayOfByte.append(getSalt(paramInt));
        localMessageDigest.update(paramArrayOfByte.toString().getBytes());
        paramString = new String(HexEncoding.encode(localMessageDigest.digest()));
        return paramString;
      }
      catch (NoSuchAlgorithmException paramString)
      {
        throw new AssertionError("Missing digest algorithm: ", paramString);
      }
    }
    return null;
  }
  
  public static String patternStringToBaseZero(String paramString)
  {
    if (paramString == null) {
      return "";
    }
    int i = paramString.length();
    byte[] arrayOfByte = new byte[i];
    paramString = paramString.getBytes();
    for (int j = 0; j < i; j++) {
      arrayOfByte[j] = ((byte)(byte)(paramString[j] - 49));
    }
    return new String(arrayOfByte);
  }
  
  public static byte[] patternToHash(List<LockPatternView.Cell> paramList)
  {
    if (paramList == null) {
      return null;
    }
    int i = paramList.size();
    byte[] arrayOfByte = new byte[i];
    for (int j = 0; j < i; j++)
    {
      LockPatternView.Cell localCell = (LockPatternView.Cell)paramList.get(j);
      arrayOfByte[j] = ((byte)(byte)(localCell.getRow() * 3 + localCell.getColumn()));
    }
    try
    {
      paramList = MessageDigest.getInstance("SHA-1").digest(arrayOfByte);
      return paramList;
    }
    catch (NoSuchAlgorithmException paramList) {}
    return arrayOfByte;
  }
  
  public static String patternToString(List<LockPatternView.Cell> paramList)
  {
    if (paramList == null) {
      return "";
    }
    int i = paramList.size();
    byte[] arrayOfByte = new byte[i];
    for (int j = 0; j < i; j++)
    {
      LockPatternView.Cell localCell = (LockPatternView.Cell)paramList.get(j);
      arrayOfByte[j] = ((byte)(byte)(localCell.getRow() * 3 + localCell.getColumn() + 49));
    }
    return new String(arrayOfByte);
  }
  
  private boolean savedPasswordExists(int paramInt)
  {
    try
    {
      boolean bool = getLockSettings().havePassword(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  private boolean savedPatternExists(int paramInt)
  {
    try
    {
      boolean bool = getLockSettings().havePattern(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  private void setBoolean(String paramString, boolean paramBoolean, int paramInt)
  {
    try
    {
      getLockSettings().setBoolean(paramString, paramBoolean, paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Couldn't write boolean ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(localRemoteException);
      Log.e("LockPatternUtils", localStringBuilder.toString());
    }
  }
  
  private void setKeyguardStoredPasswordQuality(int paramInt1, int paramInt2)
  {
    setLong("lockscreen.password_type", paramInt1, paramInt2);
  }
  
  private void setLong(String paramString, long paramLong, int paramInt)
  {
    try
    {
      getLockSettings().setLong(paramString, paramLong, paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Couldn't write long ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(localRemoteException);
      Log.e("LockPatternUtils", localStringBuilder.toString());
    }
  }
  
  private void setString(String paramString1, String paramString2, int paramInt)
  {
    try
    {
      getLockSettings().setString(paramString1, paramString2, paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      paramString2 = new StringBuilder();
      paramString2.append("Couldn't write string ");
      paramString2.append(paramString1);
      paramString2.append(localRemoteException);
      Log.e("LockPatternUtils", paramString2.toString());
    }
  }
  
  private boolean shouldEncryptWithCredentials(boolean paramBoolean)
  {
    if ((isCredentialRequiredToDecrypt(paramBoolean)) && (!isDoNotAskCredentialsOnBootSet())) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  public static List<LockPatternView.Cell> stringToPattern(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    ArrayList localArrayList = Lists.newArrayList();
    paramString = paramString.getBytes();
    for (int i = 0; i < paramString.length; i++)
    {
      int j = (byte)(paramString[i] - 49);
      localArrayList.add(LockPatternView.Cell.of(j / 3, j % 3));
    }
    return localArrayList;
  }
  
  private void throwIfCalledOnMainThread()
  {
    if (!Looper.getMainLooper().isCurrentThread()) {
      return;
    }
    throw new IllegalStateException("should not be called from the main thread.");
  }
  
  private void updateCryptoUserInfo(int paramInt)
  {
    if (paramInt != 0) {
      return;
    }
    String str;
    if (isOwnerInfoEnabled(paramInt)) {
      str = getOwnerInfo(paramInt);
    } else {
      str = "";
    }
    Object localObject = ServiceManager.getService("mount");
    if (localObject == null)
    {
      Log.e("LockPatternUtils", "Could not find the mount service to update the user info");
      return;
    }
    localObject = IStorageManager.Stub.asInterface((IBinder)localObject);
    try
    {
      Log.d("LockPatternUtils", "Setting owner info");
      ((IStorageManager)localObject).setField("OwnerInfo", str);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("LockPatternUtils", "Error changing user info", localRemoteException);
    }
  }
  
  private void updateEncryptionPassword(final int paramInt, final String paramString)
  {
    if (!isDeviceEncryptionEnabled()) {
      return;
    }
    final IBinder localIBinder = ServiceManager.getService("mount");
    if (localIBinder == null)
    {
      Log.e("LockPatternUtils", "Could not find the mount service to update the encryption password");
      return;
    }
    new AsyncTask()
    {
      protected Void doInBackground(Void... paramAnonymousVarArgs)
      {
        paramAnonymousVarArgs = IStorageManager.Stub.asInterface(localIBinder);
        try
        {
          paramAnonymousVarArgs.changeEncryptionPassword(paramInt, paramString);
        }
        catch (RemoteException paramAnonymousVarArgs)
        {
          Log.e("LockPatternUtils", "Error changing encryption password", paramAnonymousVarArgs);
        }
        return null;
      }
    }.execute(new Void[0]);
  }
  
  private void updateEncryptionPasswordIfNeeded(String paramString, int paramInt1, int paramInt2)
  {
    if ((paramInt2 == 0) && (isDeviceEncryptionEnabled()))
    {
      int i = 1;
      if (!shouldEncryptWithCredentials(true))
      {
        clearEncryptionPassword();
      }
      else
      {
        int j = 0;
        if (paramInt1 == 131072) {
          paramInt2 = 1;
        } else {
          paramInt2 = 0;
        }
        if (paramInt1 == 196608) {
          paramInt1 = i;
        } else {
          paramInt1 = 0;
        }
        if ((paramInt2 == 0) && (paramInt1 == 0)) {
          paramInt1 = j;
        } else {
          paramInt1 = 3;
        }
        updateEncryptionPassword(paramInt1, paramString);
      }
    }
  }
  
  private void updatePasswordHistory(String paramString, int paramInt)
  {
    if (TextUtils.isEmpty(paramString))
    {
      Log.e("LockPatternUtils", "checkPasswordHistory: empty password");
      return;
    }
    Object localObject1 = getString("lockscreen.passwordhistory", paramInt);
    Object localObject2 = localObject1;
    if (localObject1 == null) {
      localObject2 = "";
    }
    int i = getRequestedPasswordHistoryLength(paramInt);
    if (i == 0)
    {
      paramString = "";
    }
    else
    {
      String str = passwordToHistoryHash(paramString, getPasswordHistoryHashFactor(paramString, paramInt), paramInt);
      localObject1 = str;
      if (str == null)
      {
        Log.e("LockPatternUtils", "Compute new style password hash failed, fallback to legacy style");
        localObject1 = legacyPasswordToHash(paramString, paramInt);
      }
      if (TextUtils.isEmpty((CharSequence)localObject2))
      {
        paramString = (String)localObject1;
      }
      else
      {
        localObject2 = ((String)localObject2).split(",");
        paramString = new StringJoiner(",");
        paramString.add((CharSequence)localObject1);
        for (int j = 0; (j < i - 1) && (j < localObject2.length); j++) {
          paramString.add(localObject2[j]);
        }
        paramString = paramString.toString();
      }
    }
    setString("lockscreen.passwordhistory", paramString, paramInt);
  }
  
  public static boolean userOwnsFrpCredential(Context paramContext, UserInfo paramUserInfo)
  {
    boolean bool;
    if ((paramUserInfo != null) && (paramUserInfo.isPrimary()) && (paramUserInfo.isAdmin()) && (frpCredentialEnabled(paramContext))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private byte[] verifyCredential(String paramString, int paramInt1, long paramLong, int paramInt2)
    throws LockPatternUtils.RequestThrottledException
  {
    try
    {
      paramString = getLockSettings().verifyCredential(paramString, paramInt1, paramLong, paramInt2);
      if (paramString.getResponseCode() == 0) {
        return paramString.getPayload();
      }
      if (paramString.getResponseCode() != 1) {
        return null;
      }
      RequestThrottledException localRequestThrottledException = new com/android/internal/widget/LockPatternUtils$RequestThrottledException;
      localRequestThrottledException.<init>(paramString.getTimeout());
      throw localRequestThrottledException;
    }
    catch (RemoteException paramString) {}
    return null;
  }
  
  private ICheckCredentialProgressCallback wrapCallback(final CheckCredentialProgressCallback paramCheckCredentialProgressCallback)
  {
    if (paramCheckCredentialProgressCallback == null) {
      return null;
    }
    if (mHandler != null) {
      new ICheckCredentialProgressCallback.Stub()
      {
        public void onCredentialVerified()
          throws RemoteException
        {
          Handler localHandler = mHandler;
          LockPatternUtils.CheckCredentialProgressCallback localCheckCredentialProgressCallback = paramCheckCredentialProgressCallback;
          Objects.requireNonNull(localCheckCredentialProgressCallback);
          localHandler.post(new _..Lambda.gPQuiuEDuOmrh2MixBcV6a5gu5s(localCheckCredentialProgressCallback));
        }
      };
    }
    throw new IllegalStateException("Must construct LockPatternUtils on a looper thread to use progress callbacks.");
  }
  
  public long addEscrowToken(byte[] paramArrayOfByte, int paramInt)
  {
    return getLockSettingsInternal().addEscrowToken(paramArrayOfByte, paramInt);
  }
  
  public boolean checkPassword(String paramString, int paramInt)
    throws LockPatternUtils.RequestThrottledException
  {
    return checkPassword(paramString, paramInt, null);
  }
  
  public boolean checkPassword(String paramString, int paramInt, CheckCredentialProgressCallback paramCheckCredentialProgressCallback)
    throws LockPatternUtils.RequestThrottledException
  {
    throwIfCalledOnMainThread();
    return checkCredential(paramString, 2, paramInt, paramCheckCredentialProgressCallback);
  }
  
  public boolean checkPasswordHistory(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    if (TextUtils.isEmpty(paramString))
    {
      Log.e("LockPatternUtils", "checkPasswordHistory: empty password");
      return false;
    }
    String str1 = getString("lockscreen.passwordhistory", paramInt);
    if (TextUtils.isEmpty(str1)) {
      return false;
    }
    int i = getRequestedPasswordHistoryLength(paramInt);
    if (i == 0) {
      return false;
    }
    String str2 = legacyPasswordToHash(paramString, paramInt);
    paramString = passwordToHistoryHash(paramString, paramArrayOfByte, paramInt);
    paramArrayOfByte = str1.split(",");
    paramInt = 0;
    while (paramInt < Math.min(i, paramArrayOfByte.length)) {
      if ((!paramArrayOfByte[paramInt].equals(str2)) && (!paramArrayOfByte[paramInt].equals(paramString))) {
        paramInt++;
      } else {
        return true;
      }
    }
    return false;
  }
  
  public boolean checkPattern(List<LockPatternView.Cell> paramList, int paramInt)
    throws LockPatternUtils.RequestThrottledException
  {
    return checkPattern(paramList, paramInt, null);
  }
  
  public boolean checkPattern(List<LockPatternView.Cell> paramList, int paramInt, CheckCredentialProgressCallback paramCheckCredentialProgressCallback)
    throws LockPatternUtils.RequestThrottledException
  {
    throwIfCalledOnMainThread();
    return checkCredential(patternToString(paramList), 1, paramInt, paramCheckCredentialProgressCallback);
  }
  
  public boolean checkVoldPassword(int paramInt)
  {
    try
    {
      boolean bool = getLockSettings().checkVoldPassword(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public void clearEncryptionPassword()
  {
    updateEncryptionPassword(1, null);
  }
  
  public void clearLock(String paramString, int paramInt)
  {
    int i = getKeyguardStoredPasswordQuality(paramInt);
    setKeyguardStoredPasswordQuality(0, paramInt);
    try
    {
      getLockSettings().setLockCredential(null, -1, paramString, 0, paramInt);
      if (paramInt == 0)
      {
        updateEncryptionPassword(1, null);
        setCredentialRequiredToDecrypt(false);
      }
      onAfterChangingPassword(paramInt);
      return;
    }
    catch (Exception paramString)
    {
      Log.e("LockPatternUtils", "Failed to clear lock", paramString);
      setKeyguardStoredPasswordQuality(i, paramInt);
    }
  }
  
  public void disableSyntheticPassword()
  {
    setLong("enable-sp", 0L, 0);
  }
  
  public void enableSyntheticPassword()
  {
    setLong("enable-sp", 1L, 0);
  }
  
  public int getActivePasswordQuality(int paramInt)
  {
    int i = getKeyguardStoredPasswordQuality(paramInt);
    if (isLockPasswordEnabled(i, paramInt)) {
      return i;
    }
    if (isLockPatternEnabled(i, paramInt)) {
      return i;
    }
    return 0;
  }
  
  public String getAsusString(String paramString1, String paramString2)
  {
    try
    {
      paramString1 = getLockSettings().getAsusString(paramString1, paramString2);
      return paramString1;
    }
    catch (RemoteException paramString1) {}
    return paramString2;
  }
  
  public int getCurrentFailedPasswordAttempts(int paramInt)
  {
    if ((paramInt == 55537) && (frpCredentialEnabled(mContext))) {
      return 0;
    }
    return getDevicePolicyManager().getCurrentFailedPasswordAttempts(paramInt);
  }
  
  public String getDeviceOwnerInfo()
  {
    return getString("lockscreen.device_owner_info", 0);
  }
  
  public DevicePolicyManager getDevicePolicyManager()
  {
    if (mDevicePolicyManager == null)
    {
      mDevicePolicyManager = ((DevicePolicyManager)mContext.getSystemService("device_policy"));
      if (mDevicePolicyManager == null) {
        Log.e("LockPatternUtils", "Can't get DevicePolicyManagerService: is it running?", new IllegalStateException("Stack trace:"));
      }
    }
    return mDevicePolicyManager;
  }
  
  public List<ComponentName> getEnabledTrustAgents(int paramInt)
  {
    String str = getString("lockscreen.enabledtrustagents", paramInt);
    if (TextUtils.isEmpty(str)) {
      return null;
    }
    String[] arrayOfString = str.split(",");
    ArrayList localArrayList = new ArrayList(arrayOfString.length);
    int i = arrayOfString.length;
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      str = arrayOfString[paramInt];
      if (!TextUtils.isEmpty(str)) {
        localArrayList.add(ComponentName.unflattenFromString(str));
      }
    }
    return localArrayList;
  }
  
  public int getKeyguardStoredPasswordQuality(int paramInt)
  {
    return (int)getLong("lockscreen.password_type", 0L, paramInt);
  }
  
  @VisibleForTesting
  public ILockSettings getLockSettings()
  {
    if (mLockSettingsService == null) {
      mLockSettingsService = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));
    }
    return mLockSettingsService;
  }
  
  public long getLockoutAttemptDeadline(int paramInt)
  {
    long l = mLockoutDeadlines.get(paramInt, 0L);
    if ((l < SystemClock.elapsedRealtime()) && (l != 0L))
    {
      mLockoutDeadlines.put(paramInt, 0L);
      return 0L;
    }
    return l;
  }
  
  public int getMaximumFailedPasswordsForWipe(int paramInt)
  {
    if ((paramInt == 55537) && (frpCredentialEnabled(mContext))) {
      return 0;
    }
    return getDevicePolicyManager().getMaximumFailedPasswordsForWipe(null, paramInt);
  }
  
  public String getOwnerInfo(int paramInt)
  {
    return getString("lock_screen_owner_info", paramInt);
  }
  
  public byte[] getPasswordHistoryHashFactor(String paramString, int paramInt)
  {
    try
    {
      paramString = getLockSettings().getHashFactor(paramString, paramInt);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      Log.e("LockPatternUtils", "failed to get hash factor", paramString);
    }
    return null;
  }
  
  public boolean getPowerButtonInstantlyLocks(int paramInt)
  {
    return getBoolean("lockscreen.power_button_instantly_locks", true, paramInt);
  }
  
  public int getRequestedMinimumPasswordLength(int paramInt)
  {
    return getDevicePolicyManager().getPasswordMinimumLength(null, paramInt);
  }
  
  public int getRequestedPasswordMinimumLetters(int paramInt)
  {
    return getDevicePolicyManager().getPasswordMinimumLetters(null, paramInt);
  }
  
  public int getRequestedPasswordMinimumLowerCase(int paramInt)
  {
    return getDevicePolicyManager().getPasswordMinimumLowerCase(null, paramInt);
  }
  
  public int getRequestedPasswordMinimumNonLetter(int paramInt)
  {
    return getDevicePolicyManager().getPasswordMinimumNonLetter(null, paramInt);
  }
  
  public int getRequestedPasswordMinimumNumeric(int paramInt)
  {
    return getDevicePolicyManager().getPasswordMinimumNumeric(null, paramInt);
  }
  
  public int getRequestedPasswordMinimumSymbols(int paramInt)
  {
    return getDevicePolicyManager().getPasswordMinimumSymbols(null, paramInt);
  }
  
  public int getRequestedPasswordMinimumUpperCase(int paramInt)
  {
    return getDevicePolicyManager().getPasswordMinimumUpperCase(null, paramInt);
  }
  
  public int getRequestedPasswordQuality(int paramInt)
  {
    return getDevicePolicyManager().getPasswordQuality(null, paramInt);
  }
  
  public int getStrongAuthForUser(int paramInt)
  {
    try
    {
      paramInt = getLockSettings().getStrongAuthForUser(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("LockPatternUtils", "Could not get StrongAuth", localRemoteException);
    }
    return StrongAuthTracker.getDefaultFlags(mContext);
  }
  
  public boolean isCredentialRequiredToDecrypt(boolean paramBoolean)
  {
    int i = Settings.Global.getInt(mContentResolver, "require_password_to_decrypt", -1);
    if (i != -1) {
      if (i != 0) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
    }
    return paramBoolean;
  }
  
  public boolean isDeviceOwnerInfoEnabled()
  {
    boolean bool;
    if (getDeviceOwnerInfo() != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEscrowTokenActive(long paramLong, int paramInt)
  {
    return getLockSettingsInternal().isEscrowTokenActive(paramLong, paramInt);
  }
  
  public boolean isFingerprintAllowedForUser(int paramInt)
  {
    boolean bool;
    if ((getStrongAuthForUser(paramInt) & 0xFFFFFFFB) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public boolean isLegacyLockPatternEnabled(int paramInt)
  {
    return getBoolean("legacy_lock_pattern_enabled", true, paramInt);
  }
  
  public boolean isLockPasswordEnabled(int paramInt)
  {
    return isLockPasswordEnabled(getKeyguardStoredPasswordQuality(paramInt), paramInt);
  }
  
  public boolean isLockPatternEnabled(int paramInt)
  {
    return isLockPatternEnabled(getKeyguardStoredPasswordQuality(paramInt), paramInt);
  }
  
  public boolean isLockScreenDisabled(int paramInt)
  {
    boolean bool1 = isSecure(paramInt);
    boolean bool2 = false;
    if (bool1) {
      return false;
    }
    bool1 = mContext.getResources().getBoolean(17956929);
    int i;
    if ((UserManager.isSplitSystemUser()) && (paramInt == 0)) {
      i = 1;
    } else {
      i = 0;
    }
    UserInfo localUserInfo = getUserManager().getUserInfo(paramInt);
    int j;
    if ((UserManager.isDeviceInDemoMode(mContext)) && (localUserInfo != null) && (localUserInfo.isDemo())) {
      j = 1;
    } else {
      j = 0;
    }
    if ((!getBoolean("lockscreen.disabled", false, paramInt)) && ((!bool1) || (i != 0)) && (j == 0)) {
      return bool2;
    }
    bool2 = true;
    return bool2;
  }
  
  public boolean isManagedProfileWithUnifiedChallenge(int paramInt)
  {
    boolean bool;
    if ((isManagedProfile(paramInt)) && (!hasSeparateChallenge(paramInt))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isOwnerInfoEnabled(int paramInt)
  {
    return getBoolean("lock_screen_owner_info_enabled", false, paramInt);
  }
  
  public boolean isPatternEverChosen(int paramInt)
  {
    return getBoolean("lockscreen.patterneverchosen", false, paramInt);
  }
  
  public boolean isPowerButtonInstantlyLocksEverChosen(int paramInt)
  {
    boolean bool;
    if (getString("lockscreen.power_button_instantly_locks", paramInt) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSecure(int paramInt)
  {
    int i = getKeyguardStoredPasswordQuality(paramInt);
    boolean bool;
    if ((!isLockPatternEnabled(i, paramInt)) && (!isLockPasswordEnabled(i, paramInt))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isSeparateProfileChallengeAllowed(int paramInt)
  {
    boolean bool;
    if ((isManagedProfile(paramInt)) && (getDevicePolicyManager().isSeparateProfileChallengeAllowed(paramInt))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSeparateProfileChallengeAllowedToUnify(int paramInt)
  {
    boolean bool;
    if ((getDevicePolicyManager().isProfileActivePasswordSufficientForParent(paramInt)) && (!getUserManager().hasUserRestriction("no_unified_password", UserHandle.of(paramInt)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSeparateProfileChallengeEnabled(int paramInt)
  {
    boolean bool;
    if ((isManagedProfile(paramInt)) && (hasSeparateChallenge(paramInt))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSyntheticPasswordEnabled()
  {
    boolean bool = false;
    if (getLong("enable-sp", 0L, 0) != 0L) {
      bool = true;
    }
    return bool;
  }
  
  public boolean isTactileFeedbackEnabled()
  {
    ContentResolver localContentResolver = mContentResolver;
    boolean bool = true;
    if (Settings.System.getIntForUser(localContentResolver, "haptic_feedback_enabled", 1, -2) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTrustAllowedForUser(int paramInt)
  {
    boolean bool;
    if (getStrongAuthForUser(paramInt) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTrustUsuallyManaged(int paramInt)
  {
    if ((mLockSettingsService instanceof ILockSettings.Stub)) {
      try
      {
        boolean bool = getLockSettings().getBoolean("lockscreen.istrustusuallymanaged", false, paramInt);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        return false;
      }
    }
    throw new IllegalStateException("May only be called by TrustManagerService. Use TrustManager.isTrustUsuallyManaged()");
  }
  
  public boolean isUserInLockdown(int paramInt)
  {
    boolean bool;
    if (getStrongAuthForUser(paramInt) == 32) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isVisiblePatternEnabled(int paramInt)
  {
    return getBoolean("lock_pattern_visible_pattern", false, paramInt);
  }
  
  public boolean isVisiblePatternEverChosen(int paramInt)
  {
    boolean bool;
    if (getString("lock_pattern_visible_pattern", paramInt) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String legacyPasswordToHash(String paramString, int paramInt)
  {
    if (paramString == null) {
      return null;
    }
    try
    {
      Object localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append(getSalt(paramInt));
      localObject = ((StringBuilder)localObject).toString().getBytes();
      paramString = MessageDigest.getInstance("SHA-1").digest((byte[])localObject);
      byte[] arrayOfByte = MessageDigest.getInstance("MD5").digest((byte[])localObject);
      localObject = new byte[paramString.length + arrayOfByte.length];
      System.arraycopy(paramString, 0, (byte[])localObject, 0, paramString.length);
      System.arraycopy(arrayOfByte, 0, (byte[])localObject, paramString.length, arrayOfByte.length);
      paramString = new String(HexEncoding.encode((byte[])localObject));
      return paramString;
    }
    catch (NoSuchAlgorithmException paramString)
    {
      throw new AssertionError("Missing digest algorithm: ", paramString);
    }
  }
  
  public void registerStrongAuthTracker(StrongAuthTracker paramStrongAuthTracker)
  {
    try
    {
      getLockSettings().registerStrongAuthTracker(mStub);
      return;
    }
    catch (RemoteException paramStrongAuthTracker)
    {
      throw new RuntimeException("Could not register StrongAuthTracker");
    }
  }
  
  public boolean removeEscrowToken(long paramLong, int paramInt)
  {
    return getLockSettingsInternal().removeEscrowToken(paramLong, paramInt);
  }
  
  public void reportFailedPasswordAttempt(int paramInt)
  {
    if ((paramInt == 55537) && (frpCredentialEnabled(mContext))) {
      return;
    }
    getDevicePolicyManager().reportFailedPasswordAttempt(paramInt);
    getTrustManager().reportUnlockAttempt(false, paramInt);
  }
  
  public void reportPasswordLockout(int paramInt1, int paramInt2)
  {
    if ((paramInt2 == 55537) && (frpCredentialEnabled(mContext))) {
      return;
    }
    getTrustManager().reportUnlockLockout(paramInt1, paramInt2);
  }
  
  public void reportPatternWasChosen(int paramInt)
  {
    setBoolean("lockscreen.patterneverchosen", true, paramInt);
  }
  
  public void reportSuccessfulPasswordAttempt(int paramInt)
  {
    if ((paramInt == 55537) && (frpCredentialEnabled(mContext))) {
      return;
    }
    getDevicePolicyManager().reportSuccessfulPasswordAttempt(paramInt);
    getTrustManager().reportUnlockAttempt(true, paramInt);
  }
  
  public void requireCredentialEntry(int paramInt)
  {
    requireStrongAuth(4, paramInt);
  }
  
  public void requireStrongAuth(int paramInt1, int paramInt2)
  {
    try
    {
      getLockSettings().requireStrongAuth(paramInt1, paramInt2);
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error while requesting strong auth: ");
      localStringBuilder.append(localRemoteException);
      Log.e("LockPatternUtils", localStringBuilder.toString());
    }
  }
  
  public void resetKeyStore(int paramInt)
  {
    try
    {
      getLockSettings().resetKeyStore(paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Couldn't reset keystore ");
      localStringBuilder.append(localRemoteException);
      Log.e("LockPatternUtils", localStringBuilder.toString());
    }
  }
  
  public void sanitizePassword()
  {
    try
    {
      getLockSettings().sanitizePassword();
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Couldn't sanitize password");
      localStringBuilder.append(localRemoteException);
      Log.e("LockPatternUtils", localStringBuilder.toString());
    }
  }
  
  public void saveLockPassword(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    if ((paramString1 != null) && (paramString1.length() >= 4))
    {
      int i = getKeyguardStoredPasswordQuality(paramInt2);
      setKeyguardStoredPasswordQuality(computePasswordQuality(2, paramString1, paramInt1), paramInt2);
      try
      {
        getLockSettings().setLockCredential(paramString1, 2, paramString2, paramInt1, paramInt2);
        updateEncryptionPasswordIfNeeded(paramString1, computeForPasswordquality, paramInt2);
        updatePasswordHistory(paramString1, paramInt2);
        onAfterChangingPassword(paramInt2);
        return;
      }
      catch (Exception paramString1)
      {
        Log.e("LockPatternUtils", "Unable to save lock password", paramString1);
        setKeyguardStoredPasswordQuality(i, paramInt2);
        return;
      }
    }
    throw new IllegalArgumentException("password must not be null and at least of length 4");
  }
  
  public void saveLockPattern(List<LockPatternView.Cell> paramList, int paramInt)
  {
    saveLockPattern(paramList, null, paramInt);
  }
  
  public void saveLockPattern(List<LockPatternView.Cell> paramList, String paramString, int paramInt)
  {
    if ((paramList != null) && (paramList.size() >= 4))
    {
      paramList = patternToString(paramList);
      int i = getKeyguardStoredPasswordQuality(paramInt);
      setKeyguardStoredPasswordQuality(65536, paramInt);
      try
      {
        getLockSettings().setLockCredential(paramList, 1, paramString, 65536, paramInt);
        if ((paramInt == 0) && (isDeviceEncryptionEnabled())) {
          if (!shouldEncryptWithCredentials(true)) {
            clearEncryptionPassword();
          } else {
            updateEncryptionPassword(2, paramList);
          }
        }
        reportPatternWasChosen(paramInt);
        onAfterChangingPassword(paramInt);
        return;
      }
      catch (Exception paramList)
      {
        Log.e("LockPatternUtils", "Couldn't save lock pattern", paramList);
        setKeyguardStoredPasswordQuality(i, paramInt);
        return;
      }
    }
    throw new IllegalArgumentException("pattern must not be null and at least 4 dots long.");
  }
  
  public void setAsusString(String paramString1, String paramString2)
  {
    try
    {
      getLockSettings().setAsusString(paramString1, paramString2);
    }
    catch (RemoteException paramString2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Couldn't write setAsusString ");
      localStringBuilder.append(paramString1);
      localStringBuilder.append(paramString2);
      Log.e("LockPatternUtils", localStringBuilder.toString());
    }
  }
  
  public void setCredentialRequiredToDecrypt(boolean paramBoolean)
  {
    if ((!getUserManager().isSystemUser()) && (!getUserManager().isPrimaryUser())) {
      throw new IllegalStateException("Only the system or primary user may call setCredentialRequiredForDecrypt()");
    }
    if (isDeviceEncryptionEnabled())
    {
      ContentResolver localContentResolver = mContext.getContentResolver();
      Settings.Global.putInt(localContentResolver, "require_password_to_decrypt", paramBoolean);
    }
  }
  
  public void setDeviceOwnerInfo(String paramString)
  {
    String str = paramString;
    if (paramString != null)
    {
      str = paramString;
      if (paramString.isEmpty()) {
        str = null;
      }
    }
    setString("lockscreen.device_owner_info", str, 0);
  }
  
  public void setEnabledTrustAgents(Collection<ComponentName> paramCollection, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      paramCollection = (ComponentName)localIterator.next();
      if (localStringBuilder.length() > 0) {
        localStringBuilder.append(',');
      }
      localStringBuilder.append(paramCollection.flattenToShortString());
    }
    setString("lockscreen.enabledtrustagents", localStringBuilder.toString(), paramInt);
    getTrustManager().reportEnabledTrustAgentsChanged(paramInt);
  }
  
  @Deprecated
  public void setLegacyLockPatternEnabled(int paramInt)
  {
    setBoolean("lock_pattern_autolock", true, paramInt);
  }
  
  public boolean setLockCredentialWithToken(String paramString, int paramInt1, int paramInt2, long paramLong, byte[] paramArrayOfByte, int paramInt3)
  {
    LockSettingsInternal localLockSettingsInternal = getLockSettingsInternal();
    if (paramInt1 != -1)
    {
      if ((!TextUtils.isEmpty(paramString)) && (paramString.length() >= 4))
      {
        paramInt2 = computePasswordQuality(paramInt1, paramString, paramInt2);
        if (!localLockSettingsInternal.setLockCredentialWithToken(paramString, paramInt1, paramLong, paramArrayOfByte, paramInt2, paramInt3)) {
          return false;
        }
        setKeyguardStoredPasswordQuality(paramInt2, paramInt3);
        updateEncryptionPasswordIfNeeded(paramString, paramInt2, paramInt3);
        updatePasswordHistory(paramString, paramInt3);
        onAfterChangingPassword(paramInt3);
      }
      else
      {
        throw new IllegalArgumentException("password must not be null and at least of length 4");
      }
    }
    else
    {
      if (!TextUtils.isEmpty(paramString)) {
        break label155;
      }
      if (!localLockSettingsInternal.setLockCredentialWithToken(null, -1, paramLong, paramArrayOfByte, 0, paramInt3)) {
        return false;
      }
      setKeyguardStoredPasswordQuality(0, paramInt3);
      if (paramInt3 == 0)
      {
        updateEncryptionPassword(1, null);
        setCredentialRequiredToDecrypt(false);
      }
    }
    onAfterChangingPassword(paramInt3);
    return true;
    label155:
    throw new IllegalArgumentException("password must be emtpy for NONE type");
  }
  
  public void setLockScreenDisabled(boolean paramBoolean, int paramInt)
  {
    setBoolean("lockscreen.disabled", paramBoolean, paramInt);
  }
  
  public long setLockoutAttemptDeadline(int paramInt1, int paramInt2)
  {
    long l = SystemClock.elapsedRealtime() + paramInt2;
    if (paramInt1 == 55537) {
      return l;
    }
    mLockoutDeadlines.put(paramInt1, l);
    return l;
  }
  
  public void setOwnerInfo(String paramString, int paramInt)
  {
    setString("lock_screen_owner_info", paramString, paramInt);
    updateCryptoUserInfo(paramInt);
  }
  
  public void setOwnerInfoEnabled(boolean paramBoolean, int paramInt)
  {
    setBoolean("lock_screen_owner_info_enabled", paramBoolean, paramInt);
    updateCryptoUserInfo(paramInt);
  }
  
  public void setPowerButtonInstantlyLocks(boolean paramBoolean, int paramInt)
  {
    setBoolean("lockscreen.power_button_instantly_locks", paramBoolean, paramInt);
  }
  
  public void setSeparateProfileChallengeEnabled(int paramInt, boolean paramBoolean, String paramString)
  {
    if (!isManagedProfile(paramInt)) {
      return;
    }
    try
    {
      getLockSettings().setSeparateProfileChallengeEnabled(paramInt, paramBoolean, paramString);
      onAfterChangingPassword(paramInt);
    }
    catch (RemoteException paramString)
    {
      Log.e("LockPatternUtils", "Couldn't update work profile challenge enabled");
    }
  }
  
  public void setTrustUsuallyManaged(boolean paramBoolean, int paramInt)
  {
    try
    {
      getLockSettings().setBoolean("lockscreen.istrustusuallymanaged", paramBoolean, paramInt);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void setVisiblePasswordEnabled(boolean paramBoolean, int paramInt)
  {
    if (paramInt != 0) {
      return;
    }
    Object localObject = ServiceManager.getService("mount");
    if (localObject == null)
    {
      Log.e("LockPatternUtils", "Could not find the mount service to update the user info");
      return;
    }
    IStorageManager localIStorageManager = IStorageManager.Stub.asInterface((IBinder)localObject);
    if (paramBoolean) {
      localObject = "1";
    } else {
      localObject = "0";
    }
    try
    {
      localIStorageManager.setField("PasswordVisible", (String)localObject);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("LockPatternUtils", "Error changing password visible state", localRemoteException);
    }
  }
  
  public void setVisiblePatternEnabled(boolean paramBoolean, int paramInt)
  {
    setBoolean("lock_pattern_visible_pattern", paramBoolean, paramInt);
    if (paramInt != 0) {
      return;
    }
    Object localObject = ServiceManager.getService("mount");
    if (localObject == null)
    {
      Log.e("LockPatternUtils", "Could not find the mount service to update the user info");
      return;
    }
    IStorageManager localIStorageManager = IStorageManager.Stub.asInterface((IBinder)localObject);
    if (paramBoolean) {
      localObject = "1";
    } else {
      localObject = "0";
    }
    try
    {
      localIStorageManager.setField("PatternVisible", (String)localObject);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("LockPatternUtils", "Error changing pattern visible state", localRemoteException);
    }
  }
  
  public boolean unlockUserWithToken(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    return getLockSettingsInternal().unlockUserWithToken(paramLong, paramArrayOfByte, paramInt);
  }
  
  public void unregisterStrongAuthTracker(StrongAuthTracker paramStrongAuthTracker)
  {
    try
    {
      getLockSettings().unregisterStrongAuthTracker(mStub);
    }
    catch (RemoteException paramStrongAuthTracker)
    {
      Log.e("LockPatternUtils", "Could not unregister StrongAuthTracker", paramStrongAuthTracker);
    }
  }
  
  public void userPresent(int paramInt)
  {
    try
    {
      getLockSettings().userPresent(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public byte[] verifyPassword(String paramString, long paramLong, int paramInt)
    throws LockPatternUtils.RequestThrottledException
  {
    throwIfCalledOnMainThread();
    return verifyCredential(paramString, 2, paramLong, paramInt);
  }
  
  public byte[] verifyPattern(List<LockPatternView.Cell> paramList, long paramLong, int paramInt)
    throws LockPatternUtils.RequestThrottledException
  {
    throwIfCalledOnMainThread();
    return verifyCredential(patternToString(paramList), 1, paramLong, paramInt);
  }
  
  public byte[] verifyTiedProfileChallenge(String paramString, boolean paramBoolean, long paramLong, int paramInt)
    throws LockPatternUtils.RequestThrottledException
  {
    throwIfCalledOnMainThread();
    try
    {
      Object localObject = getLockSettings();
      int i;
      if (paramBoolean) {
        i = 1;
      } else {
        i = 2;
      }
      localObject = ((ILockSettings)localObject).verifyTiedProfileChallenge(paramString, i, paramLong, paramInt);
      if (((VerifyCredentialResponse)localObject).getResponseCode() == 0) {
        return ((VerifyCredentialResponse)localObject).getPayload();
      }
      if (((VerifyCredentialResponse)localObject).getResponseCode() != 1) {
        return null;
      }
      paramString = new com/android/internal/widget/LockPatternUtils$RequestThrottledException;
      paramString.<init>(((VerifyCredentialResponse)localObject).getTimeout());
      throw paramString;
    }
    catch (RemoteException paramString) {}
    return null;
  }
  
  public static abstract interface CheckCredentialProgressCallback
  {
    public abstract void onEarlyMatched();
  }
  
  public static final class RequestThrottledException
    extends Exception
  {
    private int mTimeoutMs;
    
    public RequestThrottledException(int paramInt)
    {
      mTimeoutMs = paramInt;
    }
    
    public int getTimeoutMs()
    {
      return mTimeoutMs;
    }
  }
  
  public static class StrongAuthTracker
  {
    private static final int ALLOWING_FINGERPRINT = 4;
    public static final int SOME_AUTH_REQUIRED_AFTER_USER_REQUEST = 4;
    public static final int STRONG_AUTH_NOT_REQUIRED = 0;
    public static final int STRONG_AUTH_REQUIRED_AFTER_BOOT = 1;
    public static final int STRONG_AUTH_REQUIRED_AFTER_DPM_LOCK_NOW = 2;
    public static final int STRONG_AUTH_REQUIRED_AFTER_LOCKOUT = 8;
    public static final int STRONG_AUTH_REQUIRED_AFTER_TIMEOUT = 16;
    public static final int STRONG_AUTH_REQUIRED_AFTER_USER_LOCKDOWN = 32;
    private final int mDefaultStrongAuthFlags;
    private final H mHandler;
    private final SparseIntArray mStrongAuthRequiredForUser = new SparseIntArray();
    protected final IStrongAuthTracker.Stub mStub = new IStrongAuthTracker.Stub()
    {
      public void onStrongAuthRequiredChanged(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        mHandler.obtainMessage(1, paramAnonymousInt1, paramAnonymousInt2).sendToTarget();
      }
    };
    
    public StrongAuthTracker(Context paramContext)
    {
      this(paramContext, Looper.myLooper());
    }
    
    public StrongAuthTracker(Context paramContext, Looper paramLooper)
    {
      mHandler = new H(paramLooper);
      mDefaultStrongAuthFlags = getDefaultFlags(paramContext);
    }
    
    public static int getDefaultFlags(Context paramContext)
    {
      return paramContext.getResources().getBoolean(17957039);
    }
    
    public int getStrongAuthForUser(int paramInt)
    {
      return mStrongAuthRequiredForUser.get(paramInt, mDefaultStrongAuthFlags);
    }
    
    protected void handleStrongAuthRequiredChanged(int paramInt1, int paramInt2)
    {
      if (paramInt1 != getStrongAuthForUser(paramInt2))
      {
        if (paramInt1 == mDefaultStrongAuthFlags) {
          mStrongAuthRequiredForUser.delete(paramInt2);
        } else {
          mStrongAuthRequiredForUser.put(paramInt2, paramInt1);
        }
        onStrongAuthRequiredChanged(paramInt2);
      }
    }
    
    public boolean isFingerprintAllowedForUser(int paramInt)
    {
      boolean bool;
      if ((getStrongAuthForUser(paramInt) & 0xFFFFFFFB) == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isTrustAllowedForUser(int paramInt)
    {
      boolean bool;
      if (getStrongAuthForUser(paramInt) == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void onStrongAuthRequiredChanged(int paramInt) {}
    
    private class H
      extends Handler
    {
      static final int MSG_ON_STRONG_AUTH_REQUIRED_CHANGED = 1;
      
      public H(Looper paramLooper)
      {
        super();
      }
      
      public void handleMessage(Message paramMessage)
      {
        if (what == 1) {
          handleStrongAuthRequiredChanged(arg1, arg2);
        }
      }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface StrongAuthFlags {}
  }
}
