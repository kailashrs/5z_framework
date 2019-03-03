package android.accounts;

import android.annotation.SystemApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SeempLog;
import com.google.android.collect.Maps;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class AccountManager
{
  public static final String ACCOUNT_ACCESS_TOKEN_TYPE = "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE";
  public static final String ACTION_ACCOUNT_REMOVED = "android.accounts.action.ACCOUNT_REMOVED";
  public static final String ACTION_AUTHENTICATOR_INTENT = "android.accounts.AccountAuthenticator";
  public static final String ACTION_VISIBLE_ACCOUNTS_CHANGED = "android.accounts.action.VISIBLE_ACCOUNTS_CHANGED";
  public static final String AUTHENTICATOR_ATTRIBUTES_NAME = "account-authenticator";
  public static final String AUTHENTICATOR_META_DATA_NAME = "android.accounts.AccountAuthenticator";
  public static final int ERROR_CODE_BAD_ARGUMENTS = 7;
  public static final int ERROR_CODE_BAD_AUTHENTICATION = 9;
  public static final int ERROR_CODE_BAD_REQUEST = 8;
  public static final int ERROR_CODE_CANCELED = 4;
  public static final int ERROR_CODE_INVALID_RESPONSE = 5;
  public static final int ERROR_CODE_MANAGEMENT_DISABLED_FOR_ACCOUNT_TYPE = 101;
  public static final int ERROR_CODE_NETWORK_ERROR = 3;
  public static final int ERROR_CODE_REMOTE_EXCEPTION = 1;
  public static final int ERROR_CODE_UNSUPPORTED_OPERATION = 6;
  public static final int ERROR_CODE_USER_RESTRICTED = 100;
  public static final String KEY_ACCOUNTS = "accounts";
  public static final String KEY_ACCOUNT_ACCESS_ID = "accountAccessId";
  public static final String KEY_ACCOUNT_AUTHENTICATOR_RESPONSE = "accountAuthenticatorResponse";
  public static final String KEY_ACCOUNT_MANAGER_RESPONSE = "accountManagerResponse";
  public static final String KEY_ACCOUNT_NAME = "authAccount";
  public static final String KEY_ACCOUNT_SESSION_BUNDLE = "accountSessionBundle";
  public static final String KEY_ACCOUNT_STATUS_TOKEN = "accountStatusToken";
  public static final String KEY_ACCOUNT_TYPE = "accountType";
  public static final String KEY_ANDROID_PACKAGE_NAME = "androidPackageName";
  public static final String KEY_AUTHENTICATOR_TYPES = "authenticator_types";
  public static final String KEY_AUTHTOKEN = "authtoken";
  public static final String KEY_AUTH_FAILED_MESSAGE = "authFailedMessage";
  public static final String KEY_AUTH_TOKEN_LABEL = "authTokenLabelKey";
  public static final String KEY_BOOLEAN_RESULT = "booleanResult";
  public static final String KEY_CALLER_PID = "callerPid";
  public static final String KEY_CALLER_UID = "callerUid";
  public static final String KEY_ERROR_CODE = "errorCode";
  public static final String KEY_ERROR_MESSAGE = "errorMessage";
  public static final String KEY_INTENT = "intent";
  public static final String KEY_LAST_AUTHENTICATED_TIME = "lastAuthenticatedTime";
  public static final String KEY_NOTIFY_ON_FAILURE = "notifyOnAuthFailure";
  public static final String KEY_PASSWORD = "password";
  public static final String KEY_USERDATA = "userdata";
  public static final String LOGIN_ACCOUNTS_CHANGED_ACTION = "android.accounts.LOGIN_ACCOUNTS_CHANGED";
  public static final String PACKAGE_NAME_KEY_LEGACY_NOT_VISIBLE = "android:accounts:key_legacy_not_visible";
  public static final String PACKAGE_NAME_KEY_LEGACY_VISIBLE = "android:accounts:key_legacy_visible";
  private static final String TAG = "AccountManager";
  public static final int VISIBILITY_NOT_VISIBLE = 3;
  public static final int VISIBILITY_UNDEFINED = 0;
  public static final int VISIBILITY_USER_MANAGED_NOT_VISIBLE = 4;
  public static final int VISIBILITY_USER_MANAGED_VISIBLE = 2;
  public static final int VISIBILITY_VISIBLE = 1;
  private final BroadcastReceiver mAccountsChangedBroadcastReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context arg1, Intent paramAnonymousIntent)
    {
      paramAnonymousIntent = getAccounts();
      synchronized (mAccountsUpdatedListeners)
      {
        Iterator localIterator = mAccountsUpdatedListeners.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          AccountManager.this.postToHandler((Handler)localEntry.getValue(), (OnAccountsUpdateListener)localEntry.getKey(), paramAnonymousIntent);
        }
        return;
      }
    }
  };
  private final HashMap<OnAccountsUpdateListener, Handler> mAccountsUpdatedListeners = Maps.newHashMap();
  private final HashMap<OnAccountsUpdateListener, Set<String>> mAccountsUpdatedListenersTypes = Maps.newHashMap();
  private final Context mContext;
  private final Handler mMainHandler;
  private final IAccountManager mService;
  
  public AccountManager(Context paramContext, IAccountManager paramIAccountManager)
  {
    mContext = paramContext;
    mService = paramIAccountManager;
    mMainHandler = new Handler(mContext.getMainLooper());
  }
  
  public AccountManager(Context paramContext, IAccountManager paramIAccountManager, Handler paramHandler)
  {
    mContext = paramContext;
    mService = paramIAccountManager;
    mMainHandler = paramHandler;
  }
  
  private Exception convertErrorToException(int paramInt, String paramString)
  {
    if (paramInt == 3) {
      return new IOException(paramString);
    }
    if (paramInt == 6) {
      return new UnsupportedOperationException(paramString);
    }
    if (paramInt == 5) {
      return new AuthenticatorException(paramString);
    }
    if (paramInt == 7) {
      return new IllegalArgumentException(paramString);
    }
    return new AuthenticatorException(paramString);
  }
  
  private void ensureNotOnMainThread()
  {
    Object localObject = Looper.myLooper();
    if ((localObject != null) && (localObject == mContext.getMainLooper()))
    {
      localObject = new IllegalStateException("calling this from your main thread can lead to deadlock");
      Log.e("AccountManager", "calling this from your main thread can lead to deadlock and/or ANRs", (Throwable)localObject);
      if (mContext.getApplicationInfo().targetSdkVersion >= 8) {
        throw ((Throwable)localObject);
      }
    }
  }
  
  public static AccountManager get(Context paramContext)
  {
    if (paramContext != null) {
      return (AccountManager)paramContext.getSystemService("account");
    }
    throw new IllegalArgumentException("context is null");
  }
  
  private void getAccountByTypeAndFeatures(final String paramString, final String[] paramArrayOfString, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    new AmsTask(null, paramHandler, paramAccountManagerCallback, paramString)
    {
      public void doWork()
        throws RemoteException
      {
        mService.getAccountByTypeAndFeatures(mResponse, paramString, paramArrayOfString, mContext.getOpPackageName());
      }
    }.start();
  }
  
  @Deprecated
  public static Intent newChooseAccountIntent(Account paramAccount, ArrayList<Account> paramArrayList, String[] paramArrayOfString1, boolean paramBoolean, String paramString1, String paramString2, String[] paramArrayOfString2, Bundle paramBundle)
  {
    return newChooseAccountIntent(paramAccount, paramArrayList, paramArrayOfString1, paramString1, paramString2, paramArrayOfString2, paramBundle);
  }
  
  public static Intent newChooseAccountIntent(Account paramAccount, List<Account> paramList, String[] paramArrayOfString1, String paramString1, String paramString2, String[] paramArrayOfString2, Bundle paramBundle)
  {
    Intent localIntent = new Intent();
    ComponentName localComponentName = ComponentName.unflattenFromString(Resources.getSystem().getString(17039675));
    localIntent.setClassName(localComponentName.getPackageName(), localComponentName.getClassName());
    if (paramList == null) {
      paramList = null;
    } else {
      paramList = new ArrayList(paramList);
    }
    localIntent.putExtra("allowableAccounts", paramList);
    localIntent.putExtra("allowableAccountTypes", paramArrayOfString1);
    localIntent.putExtra("addAccountOptions", paramBundle);
    localIntent.putExtra("selectedAccount", paramAccount);
    localIntent.putExtra("descriptionTextOverride", paramString1);
    localIntent.putExtra("authTokenType", paramString2);
    localIntent.putExtra("addAccountRequiredFeatures", paramArrayOfString2);
    return localIntent;
  }
  
  private void postToHandler(Handler paramHandler, final AccountManagerCallback<Bundle> paramAccountManagerCallback, final AccountManagerFuture<Bundle> paramAccountManagerFuture)
  {
    if (paramHandler == null) {
      paramHandler = mMainHandler;
    }
    paramHandler.post(new Runnable()
    {
      public void run()
      {
        paramAccountManagerCallback.run(paramAccountManagerFuture);
      }
    });
  }
  
  private void postToHandler(Handler paramHandler, final OnAccountsUpdateListener paramOnAccountsUpdateListener, Account[] paramArrayOfAccount)
  {
    final Account[] arrayOfAccount = new Account[paramArrayOfAccount.length];
    System.arraycopy(paramArrayOfAccount, 0, arrayOfAccount, 0, arrayOfAccount.length);
    if (paramHandler == null) {
      paramHandler = mMainHandler;
    }
    paramHandler.post(new Runnable()
    {
      public void run()
      {
        try
        {
          synchronized (mAccountsUpdatedListeners)
          {
            if (mAccountsUpdatedListeners.containsKey(paramOnAccountsUpdateListener))
            {
              Set localSet = (Set)mAccountsUpdatedListenersTypes.get(paramOnAccountsUpdateListener);
              if (localSet != null)
              {
                ArrayList localArrayList = new java/util/ArrayList;
                localArrayList.<init>();
                for (Account localAccount : arrayOfAccount) {
                  if (localSet.contains(type)) {
                    localArrayList.add(localAccount);
                  }
                }
                paramOnAccountsUpdateListener.onAccountsUpdated((Account[])localArrayList.toArray(new Account[localArrayList.size()]));
              }
              else
              {
                paramOnAccountsUpdateListener.onAccountsUpdated(arrayOfAccount);
              }
            }
          }
        }
        catch (SQLException localSQLException)
        {
          Log.e("AccountManager", "Can't update accounts", localSQLException);
          return;
        }
      }
    });
  }
  
  public static Bundle sanitizeResult(Bundle paramBundle)
  {
    if ((paramBundle != null) && (paramBundle.containsKey("authtoken")) && (!TextUtils.isEmpty(paramBundle.getString("authtoken"))))
    {
      paramBundle = new Bundle(paramBundle);
      paramBundle.putString("authtoken", "<omitted for logging purposes>");
      return paramBundle;
    }
    return paramBundle;
  }
  
  public AccountManagerFuture<Bundle> addAccount(final String paramString1, final String paramString2, final String[] paramArrayOfString, Bundle paramBundle, final Activity paramActivity, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    SeempLog.record(29);
    if (paramString1 != null)
    {
      final Bundle localBundle = new Bundle();
      if (paramBundle != null) {
        localBundle.putAll(paramBundle);
      }
      localBundle.putString("androidPackageName", mContext.getPackageName());
      new AmsTask(paramActivity, paramHandler, paramAccountManagerCallback, paramString1)
      {
        public void doWork()
          throws RemoteException
        {
          SeempLog.record(31);
          IAccountManager localIAccountManager = mService;
          IAccountManagerResponse localIAccountManagerResponse = mResponse;
          String str1 = paramString1;
          String str2 = paramString2;
          String[] arrayOfString = paramArrayOfString;
          if (paramActivity != null) {}
          for (boolean bool = true;; bool = false) {
            break;
          }
          localIAccountManager.addAccount(localIAccountManagerResponse, str1, str2, arrayOfString, bool, localBundle);
        }
      }.start();
    }
    throw new IllegalArgumentException("accountType is null");
  }
  
  public AccountManagerFuture<Bundle> addAccountAsUser(final String paramString1, final String paramString2, final String[] paramArrayOfString, Bundle paramBundle, final Activity paramActivity, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler, final UserHandle paramUserHandle)
  {
    if (paramString1 != null)
    {
      if (paramUserHandle != null)
      {
        final Bundle localBundle = new Bundle();
        if (paramBundle != null) {
          localBundle.putAll(paramBundle);
        }
        localBundle.putString("androidPackageName", mContext.getPackageName());
        new AmsTask(paramActivity, paramHandler, paramAccountManagerCallback, paramString1)
        {
          public void doWork()
            throws RemoteException
          {
            SeempLog.record(31);
            IAccountManager localIAccountManager = mService;
            IAccountManagerResponse localIAccountManagerResponse = mResponse;
            String str1 = paramString1;
            String str2 = paramString2;
            String[] arrayOfString = paramArrayOfString;
            if (paramActivity != null) {}
            for (boolean bool = true;; bool = false) {
              break;
            }
            localIAccountManager.addAccountAsUser(localIAccountManagerResponse, str1, str2, arrayOfString, bool, localBundle, paramUserHandle.getIdentifier());
          }
        }.start();
      }
      throw new IllegalArgumentException("userHandle is null");
    }
    throw new IllegalArgumentException("accountType is null");
  }
  
  public boolean addAccountExplicitly(Account paramAccount, String paramString, Bundle paramBundle)
  {
    SeempLog.record(24);
    if (paramAccount != null) {
      try
      {
        boolean bool = mService.addAccountExplicitly(paramAccount, paramString, paramBundle);
        return bool;
      }
      catch (RemoteException paramAccount)
      {
        throw paramAccount.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public boolean addAccountExplicitly(Account paramAccount, String paramString, Bundle paramBundle, Map<String, Integer> paramMap)
  {
    if (paramAccount != null) {
      try
      {
        boolean bool = mService.addAccountExplicitlyWithVisibility(paramAccount, paramString, paramBundle, paramMap);
        return bool;
      }
      catch (RemoteException paramAccount)
      {
        throw paramAccount.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public void addOnAccountsUpdatedListener(OnAccountsUpdateListener paramOnAccountsUpdateListener, Handler paramHandler, boolean paramBoolean)
  {
    addOnAccountsUpdatedListener(paramOnAccountsUpdateListener, paramHandler, paramBoolean, null);
  }
  
  public void addOnAccountsUpdatedListener(OnAccountsUpdateListener paramOnAccountsUpdateListener, Handler paramHandler, boolean paramBoolean, String[] paramArrayOfString)
  {
    if (paramOnAccountsUpdateListener != null) {
      synchronized (mAccountsUpdatedListeners)
      {
        if (!mAccountsUpdatedListeners.containsKey(paramOnAccountsUpdateListener))
        {
          boolean bool = mAccountsUpdatedListeners.isEmpty();
          mAccountsUpdatedListeners.put(paramOnAccountsUpdateListener, paramHandler);
          Object localObject;
          if (paramArrayOfString != null)
          {
            localObject = mAccountsUpdatedListenersTypes;
            HashSet localHashSet = new java/util/HashSet;
            localHashSet.<init>(Arrays.asList(paramArrayOfString));
            ((HashMap)localObject).put(paramOnAccountsUpdateListener, localHashSet);
          }
          else
          {
            mAccountsUpdatedListenersTypes.put(paramOnAccountsUpdateListener, null);
          }
          if (bool)
          {
            localObject = new android/content/IntentFilter;
            ((IntentFilter)localObject).<init>();
            ((IntentFilter)localObject).addAction("android.accounts.action.VISIBLE_ACCOUNTS_CHANGED");
            ((IntentFilter)localObject).addAction("android.intent.action.DEVICE_STORAGE_OK");
            mContext.registerReceiver(mAccountsChangedBroadcastReceiver, (IntentFilter)localObject);
          }
          try
          {
            mService.registerAccountListener(paramArrayOfString, mContext.getOpPackageName());
            if (paramBoolean) {
              postToHandler(paramHandler, paramOnAccountsUpdateListener, getAccounts());
            }
            return;
          }
          catch (RemoteException paramOnAccountsUpdateListener)
          {
            throw paramOnAccountsUpdateListener.rethrowFromSystemServer();
          }
        }
        paramOnAccountsUpdateListener = new java/lang/IllegalStateException;
        paramOnAccountsUpdateListener.<init>("this listener is already added");
        throw paramOnAccountsUpdateListener;
      }
    }
    throw new IllegalArgumentException("the listener is null");
  }
  
  public void addSharedAccountsFromParentUser(UserHandle paramUserHandle1, UserHandle paramUserHandle2)
  {
    try
    {
      mService.addSharedAccountsFromParentUser(paramUserHandle1.getIdentifier(), paramUserHandle2.getIdentifier(), mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramUserHandle1)
    {
      throw paramUserHandle1.rethrowFromSystemServer();
    }
  }
  
  public String blockingGetAuthToken(Account paramAccount, String paramString, boolean paramBoolean)
    throws OperationCanceledException, IOException, AuthenticatorException
  {
    if (paramAccount != null)
    {
      if (paramString != null)
      {
        Object localObject = (Bundle)getAuthToken(paramAccount, paramString, paramBoolean, null, null).getResult();
        if (localObject == null)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("blockingGetAuthToken: null was returned from getResult() for ");
          ((StringBuilder)localObject).append(paramAccount);
          ((StringBuilder)localObject).append(", authTokenType ");
          ((StringBuilder)localObject).append(paramString);
          Log.e("AccountManager", ((StringBuilder)localObject).toString());
          return null;
        }
        return ((Bundle)localObject).getString("authtoken");
      }
      throw new IllegalArgumentException("authTokenType is null");
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public void clearPassword(Account paramAccount)
  {
    SeempLog.record(27);
    if (paramAccount != null) {
      try
      {
        mService.clearPassword(paramAccount);
        return;
      }
      catch (RemoteException paramAccount)
      {
        throw paramAccount.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public AccountManagerFuture<Bundle> confirmCredentials(Account paramAccount, Bundle paramBundle, Activity paramActivity, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    return confirmCredentialsAsUser(paramAccount, paramBundle, paramActivity, paramAccountManagerCallback, paramHandler, mContext.getUser());
  }
  
  public AccountManagerFuture<Bundle> confirmCredentialsAsUser(final Account paramAccount, final Bundle paramBundle, final Activity paramActivity, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler, UserHandle paramUserHandle)
  {
    if (paramAccount != null) {
      new AmsTask(paramActivity, paramHandler, paramAccountManagerCallback, paramAccount)
      {
        public void doWork()
          throws RemoteException
        {
          SeempLog.record(31);
          IAccountManager localIAccountManager = mService;
          IAccountManagerResponse localIAccountManagerResponse = mResponse;
          Account localAccount = paramAccount;
          Bundle localBundle = paramBundle;
          if (paramActivity != null) {}
          for (boolean bool = true;; bool = false) {
            break;
          }
          localIAccountManager.confirmCredentialsAsUser(localIAccountManagerResponse, localAccount, localBundle, bool, val$userId);
        }
      }.start();
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public AccountManagerFuture<Boolean> copyAccountToUser(final Account paramAccount, final UserHandle paramUserHandle1, final UserHandle paramUserHandle2, AccountManagerCallback<Boolean> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramAccount != null)
    {
      if ((paramUserHandle2 != null) && (paramUserHandle1 != null)) {
        new Future2Task(paramHandler, paramAccountManagerCallback, paramAccount)
        {
          public Boolean bundleToResult(Bundle paramAnonymousBundle)
            throws AuthenticatorException
          {
            if (paramAnonymousBundle.containsKey("booleanResult")) {
              return Boolean.valueOf(paramAnonymousBundle.getBoolean("booleanResult"));
            }
            throw new AuthenticatorException("no result in response");
          }
          
          public void doWork()
            throws RemoteException
          {
            SeempLog.record(34);
            mService.copyAccountToUser(mResponse, paramAccount, paramUserHandle1.getIdentifier(), paramUserHandle2.getIdentifier());
          }
        }.start();
      }
      throw new IllegalArgumentException("fromUser and toUser cannot be null");
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public IntentSender createRequestAccountAccessIntentSenderAsUser(Account paramAccount, String paramString, UserHandle paramUserHandle)
  {
    try
    {
      paramAccount = mService.createRequestAccountAccessIntentSenderAsUser(paramAccount, paramString, paramUserHandle);
      return paramAccount;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public AccountManagerFuture<Bundle> editProperties(final String paramString, final Activity paramActivity, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    SeempLog.record(30);
    if (paramString != null) {
      new AmsTask(paramActivity, paramHandler, paramAccountManagerCallback, paramString)
      {
        public void doWork()
          throws RemoteException
        {
          SeempLog.record(31);
          IAccountManager localIAccountManager = mService;
          IAccountManagerResponse localIAccountManagerResponse = mResponse;
          String str = paramString;
          boolean bool;
          if (paramActivity != null) {
            bool = true;
          } else {
            bool = false;
          }
          localIAccountManager.editProperties(localIAccountManagerResponse, str, bool);
        }
      }.start();
    }
    throw new IllegalArgumentException("accountType is null");
  }
  
  public AccountManagerFuture<Bundle> finishSession(Bundle paramBundle, Activity paramActivity, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    return finishSessionAsUser(paramBundle, paramActivity, mContext.getUser(), paramAccountManagerCallback, paramHandler);
  }
  
  @SystemApi
  public AccountManagerFuture<Bundle> finishSessionAsUser(final Bundle paramBundle, final Activity paramActivity, final UserHandle paramUserHandle, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramBundle != null)
    {
      final Bundle localBundle = new Bundle();
      localBundle.putString("androidPackageName", mContext.getPackageName());
      new AmsTask(paramActivity, paramHandler, paramAccountManagerCallback, paramBundle)
      {
        public void doWork()
          throws RemoteException
        {
          IAccountManager localIAccountManager = mService;
          IAccountManagerResponse localIAccountManagerResponse = mResponse;
          Bundle localBundle = paramBundle;
          if (paramActivity != null) {}
          for (boolean bool = true;; bool = false) {
            break;
          }
          localIAccountManager.finishSessionAsUser(localIAccountManagerResponse, localBundle, bool, localBundle, paramUserHandle.getIdentifier());
        }
      }.start();
    }
    throw new IllegalArgumentException("sessionBundle is null");
  }
  
  public int getAccountVisibility(Account paramAccount, String paramString)
  {
    if (paramAccount != null) {
      try
      {
        int i = mService.getAccountVisibility(paramAccount, paramString);
        return i;
      }
      catch (RemoteException paramAccount)
      {
        throw paramAccount.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public Account[] getAccounts()
  {
    try
    {
      Account[] arrayOfAccount = mService.getAccounts(null, mContext.getOpPackageName());
      return arrayOfAccount;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Map<Account, Integer> getAccountsAndVisibilityForPackage(String paramString1, String paramString2)
  {
    try
    {
      paramString1 = mService.getAccountsAndVisibilityForPackage(paramString1, paramString2);
      return paramString1;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public Account[] getAccountsAsUser(int paramInt)
  {
    try
    {
      Account[] arrayOfAccount = mService.getAccountsAsUser(null, paramInt, mContext.getOpPackageName());
      return arrayOfAccount;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Account[] getAccountsByType(String paramString)
  {
    return getAccountsByTypeAsUser(paramString, mContext.getUser());
  }
  
  public AccountManagerFuture<Account[]> getAccountsByTypeAndFeatures(final String paramString, final String[] paramArrayOfString, AccountManagerCallback<Account[]> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramString != null) {
      new Future2Task(paramHandler, paramAccountManagerCallback, paramString)
      {
        public Account[] bundleToResult(Bundle paramAnonymousBundle)
          throws AuthenticatorException
        {
          if (paramAnonymousBundle.containsKey("accounts"))
          {
            Parcelable[] arrayOfParcelable = paramAnonymousBundle.getParcelableArray("accounts");
            paramAnonymousBundle = new Account[arrayOfParcelable.length];
            for (int i = 0; i < arrayOfParcelable.length; i++) {
              paramAnonymousBundle[i] = ((Account)arrayOfParcelable[i]);
            }
            return paramAnonymousBundle;
          }
          throw new AuthenticatorException("no result in response");
        }
        
        public void doWork()
          throws RemoteException
        {
          SeempLog.record(31);
          mService.getAccountsByFeatures(mResponse, paramString, paramArrayOfString, mContext.getOpPackageName());
        }
      }.start();
    }
    throw new IllegalArgumentException("type is null");
  }
  
  public Account[] getAccountsByTypeAsUser(String paramString, UserHandle paramUserHandle)
  {
    try
    {
      paramString = mService.getAccountsAsUser(paramString, paramUserHandle.getIdentifier(), mContext.getOpPackageName());
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Account[] getAccountsByTypeForPackage(String paramString1, String paramString2)
  {
    try
    {
      paramString1 = mService.getAccountsByTypeForPackage(paramString1, paramString2, mContext.getOpPackageName());
      return paramString1;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public Account[] getAccountsForPackage(String paramString, int paramInt)
  {
    try
    {
      paramString = mService.getAccountsForPackage(paramString, paramInt, mContext.getOpPackageName());
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public AccountManagerFuture<Bundle> getAuthToken(final Account paramAccount, final String paramString, Bundle paramBundle, Activity paramActivity, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramAccount != null)
    {
      if (paramString != null)
      {
        final Bundle localBundle = new Bundle();
        if (paramBundle != null) {
          localBundle.putAll(paramBundle);
        }
        localBundle.putString("androidPackageName", mContext.getPackageName());
        new AmsTask(paramActivity, paramHandler, paramAccountManagerCallback, paramAccount)
        {
          public void doWork()
            throws RemoteException
          {
            SeempLog.record(31);
            mService.getAuthToken(mResponse, paramAccount, paramString, false, true, localBundle);
          }
        }.start();
      }
      throw new IllegalArgumentException("authTokenType is null");
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public AccountManagerFuture<Bundle> getAuthToken(final Account paramAccount, final String paramString, Bundle paramBundle, final boolean paramBoolean, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramAccount != null)
    {
      if (paramString != null)
      {
        final Bundle localBundle = new Bundle();
        if (paramBundle != null) {
          localBundle.putAll(paramBundle);
        }
        localBundle.putString("androidPackageName", mContext.getPackageName());
        new AmsTask(null, paramHandler, paramAccountManagerCallback, paramAccount)
        {
          public void doWork()
            throws RemoteException
          {
            SeempLog.record(31);
            mService.getAuthToken(mResponse, paramAccount, paramString, paramBoolean, false, localBundle);
          }
        }.start();
      }
      throw new IllegalArgumentException("authTokenType is null");
    }
    throw new IllegalArgumentException("account is null");
  }
  
  @Deprecated
  public AccountManagerFuture<Bundle> getAuthToken(Account paramAccount, String paramString, boolean paramBoolean, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    return getAuthToken(paramAccount, paramString, null, paramBoolean, paramAccountManagerCallback, paramHandler);
  }
  
  public AccountManagerFuture<Bundle> getAuthTokenByFeatures(String paramString1, String paramString2, String[] paramArrayOfString, Activity paramActivity, Bundle paramBundle1, Bundle paramBundle2, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramString1 != null)
    {
      if (paramString2 != null)
      {
        paramString1 = new GetAuthTokenByTypeAndFeaturesTask(paramString1, paramString2, paramArrayOfString, paramActivity, paramBundle1, paramBundle2, paramAccountManagerCallback, paramHandler);
        paramString1.start();
        return paramString1;
      }
      throw new IllegalArgumentException("authTokenType is null");
    }
    throw new IllegalArgumentException("account type is null");
  }
  
  public AccountManagerFuture<String> getAuthTokenLabel(final String paramString1, final String paramString2, AccountManagerCallback<String> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramString1 != null)
    {
      if (paramString2 != null) {
        new Future2Task(paramHandler, paramAccountManagerCallback, paramString1)
        {
          public String bundleToResult(Bundle paramAnonymousBundle)
            throws AuthenticatorException
          {
            if (paramAnonymousBundle.containsKey("authTokenLabelKey")) {
              return paramAnonymousBundle.getString("authTokenLabelKey");
            }
            throw new AuthenticatorException("no result in response");
          }
          
          public void doWork()
            throws RemoteException
          {
            SeempLog.record(31);
            mService.getAuthTokenLabel(mResponse, paramString1, paramString2);
          }
        }.start();
      }
      throw new IllegalArgumentException("authTokenType is null");
    }
    throw new IllegalArgumentException("accountType is null");
  }
  
  public AuthenticatorDescription[] getAuthenticatorTypes()
  {
    try
    {
      AuthenticatorDescription[] arrayOfAuthenticatorDescription = mService.getAuthenticatorTypes(UserHandle.getCallingUserId());
      return arrayOfAuthenticatorDescription;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public AuthenticatorDescription[] getAuthenticatorTypesAsUser(int paramInt)
  {
    try
    {
      AuthenticatorDescription[] arrayOfAuthenticatorDescription = mService.getAuthenticatorTypes(paramInt);
      return arrayOfAuthenticatorDescription;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Map<String, Integer> getPackagesAndVisibilityForAccount(Account paramAccount)
  {
    if (paramAccount != null) {}
    try
    {
      return mService.getPackagesAndVisibilityForAccount(paramAccount);
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
    paramAccount = new java/lang/IllegalArgumentException;
    paramAccount.<init>("account is null");
    throw paramAccount;
  }
  
  public String getPassword(Account paramAccount)
  {
    SeempLog.record(22);
    if (paramAccount != null) {
      try
      {
        paramAccount = mService.getPassword(paramAccount);
        return paramAccount;
      }
      catch (RemoteException paramAccount)
      {
        throw paramAccount.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public String getPreviousName(Account paramAccount)
  {
    if (paramAccount != null) {
      try
      {
        paramAccount = mService.getPreviousName(paramAccount);
        return paramAccount;
      }
      catch (RemoteException paramAccount)
      {
        throw paramAccount.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public Account[] getSharedAccounts(UserHandle paramUserHandle)
  {
    try
    {
      paramUserHandle = mService.getSharedAccountsAsUser(paramUserHandle.getIdentifier());
      return paramUserHandle;
    }
    catch (RemoteException paramUserHandle)
    {
      throw paramUserHandle.rethrowFromSystemServer();
    }
  }
  
  public String getUserData(Account paramAccount, String paramString)
  {
    SeempLog.record(23);
    if (paramAccount != null)
    {
      if (paramString != null) {
        try
        {
          paramAccount = mService.getUserData(paramAccount, paramString);
          return paramAccount;
        }
        catch (RemoteException paramAccount)
        {
          throw paramAccount.rethrowFromSystemServer();
        }
      }
      throw new IllegalArgumentException("key is null");
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public boolean hasAccountAccess(Account paramAccount, String paramString, UserHandle paramUserHandle)
  {
    try
    {
      boolean bool = mService.hasAccountAccess(paramAccount, paramString, paramUserHandle);
      return bool;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public AccountManagerFuture<Boolean> hasFeatures(final Account paramAccount, final String[] paramArrayOfString, AccountManagerCallback<Boolean> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramAccount != null)
    {
      if (paramArrayOfString != null) {
        new Future2Task(paramHandler, paramAccountManagerCallback, paramAccount)
        {
          public Boolean bundleToResult(Bundle paramAnonymousBundle)
            throws AuthenticatorException
          {
            if (paramAnonymousBundle.containsKey("booleanResult")) {
              return Boolean.valueOf(paramAnonymousBundle.getBoolean("booleanResult"));
            }
            throw new AuthenticatorException("no result in response");
          }
          
          public void doWork()
            throws RemoteException
          {
            SeempLog.record(31);
            mService.hasFeatures(mResponse, paramAccount, paramArrayOfString, mContext.getOpPackageName());
          }
        }.start();
      }
      throw new IllegalArgumentException("features is null");
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public void invalidateAuthToken(String paramString1, String paramString2)
  {
    if (paramString1 != null)
    {
      if (paramString2 != null) {
        try
        {
          mService.invalidateAuthToken(paramString1, paramString2);
        }
        catch (RemoteException paramString1)
        {
          throw paramString1.rethrowFromSystemServer();
        }
      }
      return;
    }
    throw new IllegalArgumentException("accountType is null");
  }
  
  public AccountManagerFuture<Boolean> isCredentialsUpdateSuggested(final Account paramAccount, final String paramString, AccountManagerCallback<Boolean> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramAccount != null)
    {
      if (!TextUtils.isEmpty(paramString)) {
        new Future2Task(paramHandler, paramAccountManagerCallback, paramAccount)
        {
          public Boolean bundleToResult(Bundle paramAnonymousBundle)
            throws AuthenticatorException
          {
            if (paramAnonymousBundle.containsKey("booleanResult")) {
              return Boolean.valueOf(paramAnonymousBundle.getBoolean("booleanResult"));
            }
            throw new AuthenticatorException("no result in response");
          }
          
          public void doWork()
            throws RemoteException
          {
            mService.isCredentialsUpdateSuggested(mResponse, paramAccount, paramString);
          }
        }.start();
      }
      throw new IllegalArgumentException("status token is empty");
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public boolean notifyAccountAuthenticated(Account paramAccount)
  {
    if (paramAccount != null) {
      try
      {
        boolean bool = mService.accountAuthenticated(paramAccount);
        return bool;
      }
      catch (RemoteException paramAccount)
      {
        throw paramAccount.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public String peekAuthToken(Account paramAccount, String paramString)
  {
    if (paramAccount != null)
    {
      if (paramString != null) {
        try
        {
          paramAccount = mService.peekAuthToken(paramAccount, paramString);
          return paramAccount;
        }
        catch (RemoteException paramAccount)
        {
          throw paramAccount.rethrowFromSystemServer();
        }
      }
      throw new IllegalArgumentException("authTokenType is null");
    }
    throw new IllegalArgumentException("account is null");
  }
  
  @Deprecated
  public AccountManagerFuture<Boolean> removeAccount(final Account paramAccount, AccountManagerCallback<Boolean> paramAccountManagerCallback, Handler paramHandler)
  {
    SeempLog.record(25);
    if (paramAccount != null) {
      new Future2Task(paramHandler, paramAccountManagerCallback, paramAccount)
      {
        public Boolean bundleToResult(Bundle paramAnonymousBundle)
          throws AuthenticatorException
        {
          if (paramAnonymousBundle.containsKey("booleanResult")) {
            return Boolean.valueOf(paramAnonymousBundle.getBoolean("booleanResult"));
          }
          throw new AuthenticatorException("no result in response");
        }
        
        public void doWork()
          throws RemoteException
        {
          SeempLog.record(31);
          mService.removeAccount(mResponse, paramAccount, false);
        }
      }.start();
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public AccountManagerFuture<Bundle> removeAccount(final Account paramAccount, final Activity paramActivity, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    SeempLog.record(28);
    if (paramAccount != null) {
      new AmsTask(paramActivity, paramHandler, paramAccountManagerCallback, paramAccount)
      {
        public void doWork()
          throws RemoteException
        {
          SeempLog.record(34);
          IAccountManager localIAccountManager = mService;
          IAccountManagerResponse localIAccountManagerResponse = mResponse;
          Account localAccount = paramAccount;
          boolean bool;
          if (paramActivity != null) {
            bool = true;
          } else {
            bool = false;
          }
          localIAccountManager.removeAccount(localIAccountManagerResponse, localAccount, bool);
        }
      }.start();
    }
    throw new IllegalArgumentException("account is null");
  }
  
  @Deprecated
  public AccountManagerFuture<Boolean> removeAccountAsUser(final Account paramAccount, AccountManagerCallback<Boolean> paramAccountManagerCallback, Handler paramHandler, final UserHandle paramUserHandle)
  {
    if (paramAccount != null)
    {
      if (paramUserHandle != null) {
        new Future2Task(paramHandler, paramAccountManagerCallback, paramAccount)
        {
          public Boolean bundleToResult(Bundle paramAnonymousBundle)
            throws AuthenticatorException
          {
            if (paramAnonymousBundle.containsKey("booleanResult")) {
              return Boolean.valueOf(paramAnonymousBundle.getBoolean("booleanResult"));
            }
            throw new AuthenticatorException("no result in response");
          }
          
          public void doWork()
            throws RemoteException
          {
            SeempLog.record(31);
            mService.removeAccountAsUser(mResponse, paramAccount, false, paramUserHandle.getIdentifier());
          }
        }.start();
      }
      throw new IllegalArgumentException("userHandle is null");
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public AccountManagerFuture<Bundle> removeAccountAsUser(final Account paramAccount, final Activity paramActivity, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler, final UserHandle paramUserHandle)
  {
    if (paramAccount != null)
    {
      if (paramUserHandle != null) {
        new AmsTask(paramActivity, paramHandler, paramAccountManagerCallback, paramAccount)
        {
          public void doWork()
            throws RemoteException
          {
            SeempLog.record(34);
            IAccountManager localIAccountManager = mService;
            IAccountManagerResponse localIAccountManagerResponse = mResponse;
            Account localAccount = paramAccount;
            boolean bool;
            if (paramActivity != null) {
              bool = true;
            } else {
              bool = false;
            }
            localIAccountManager.removeAccountAsUser(localIAccountManagerResponse, localAccount, bool, paramUserHandle.getIdentifier());
          }
        }.start();
      }
      throw new IllegalArgumentException("userHandle is null");
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public boolean removeAccountExplicitly(Account paramAccount)
  {
    if (paramAccount != null) {
      try
      {
        boolean bool = mService.removeAccountExplicitly(paramAccount);
        return bool;
      }
      catch (RemoteException paramAccount)
      {
        throw paramAccount.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public void removeOnAccountsUpdatedListener(OnAccountsUpdateListener paramOnAccountsUpdateListener)
  {
    if (paramOnAccountsUpdateListener != null) {
      synchronized (mAccountsUpdatedListeners)
      {
        if (!mAccountsUpdatedListeners.containsKey(paramOnAccountsUpdateListener))
        {
          Log.e("AccountManager", "Listener was not previously added");
          return;
        }
        Object localObject = (Set)mAccountsUpdatedListenersTypes.get(paramOnAccountsUpdateListener);
        if (localObject != null) {
          localObject = (String[])((Set)localObject).toArray(new String[((Set)localObject).size()]);
        } else {
          localObject = null;
        }
        mAccountsUpdatedListeners.remove(paramOnAccountsUpdateListener);
        mAccountsUpdatedListenersTypes.remove(paramOnAccountsUpdateListener);
        if (mAccountsUpdatedListeners.isEmpty()) {
          mContext.unregisterReceiver(mAccountsChangedBroadcastReceiver);
        }
        try
        {
          mService.unregisterAccountListener((String[])localObject, mContext.getOpPackageName());
          return;
        }
        catch (RemoteException paramOnAccountsUpdateListener)
        {
          throw paramOnAccountsUpdateListener.rethrowFromSystemServer();
        }
      }
    }
    throw new IllegalArgumentException("listener is null");
  }
  
  public boolean removeSharedAccount(Account paramAccount, UserHandle paramUserHandle)
  {
    try
    {
      boolean bool = mService.removeSharedAccountAsUser(paramAccount, paramUserHandle.getIdentifier());
      return bool;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public AccountManagerFuture<Account> renameAccount(final Account paramAccount, final String paramString, AccountManagerCallback<Account> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramAccount != null)
    {
      if (!TextUtils.isEmpty(paramString)) {
        new Future2Task(paramHandler, paramAccountManagerCallback, paramAccount)
        {
          public Account bundleToResult(Bundle paramAnonymousBundle)
            throws AuthenticatorException
          {
            return new Account(paramAnonymousBundle.getString("authAccount"), paramAnonymousBundle.getString("accountType"), paramAnonymousBundle.getString("accountAccessId"));
          }
          
          public void doWork()
            throws RemoteException
          {
            SeempLog.record(31);
            mService.renameAccount(mResponse, paramAccount, paramString);
          }
        }.start();
      }
      throw new IllegalArgumentException("newName is empty or null.");
    }
    throw new IllegalArgumentException("account is null.");
  }
  
  public boolean setAccountVisibility(Account paramAccount, String paramString, int paramInt)
  {
    if (paramAccount != null) {
      try
      {
        boolean bool = mService.setAccountVisibility(paramAccount, paramString, paramInt);
        return bool;
      }
      catch (RemoteException paramAccount)
      {
        throw paramAccount.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public void setAuthToken(Account paramAccount, String paramString1, String paramString2)
  {
    if (paramAccount != null)
    {
      if (paramString1 != null) {
        try
        {
          mService.setAuthToken(paramAccount, paramString1, paramString2);
          return;
        }
        catch (RemoteException paramAccount)
        {
          throw paramAccount.rethrowFromSystemServer();
        }
      }
      throw new IllegalArgumentException("authTokenType is null");
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public void setPassword(Account paramAccount, String paramString)
  {
    SeempLog.record(26);
    if (paramAccount != null) {
      try
      {
        mService.setPassword(paramAccount, paramString);
        return;
      }
      catch (RemoteException paramAccount)
      {
        throw paramAccount.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public void setUserData(Account paramAccount, String paramString1, String paramString2)
  {
    SeempLog.record(28);
    if (paramAccount != null)
    {
      if (paramString1 != null) {
        try
        {
          mService.setUserData(paramAccount, paramString1, paramString2);
          return;
        }
        catch (RemoteException paramAccount)
        {
          throw paramAccount.rethrowFromSystemServer();
        }
      }
      throw new IllegalArgumentException("key is null");
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public boolean someUserHasAccount(Account paramAccount)
  {
    try
    {
      boolean bool = mService.someUserHasAccount(paramAccount);
      return bool;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public AccountManagerFuture<Bundle> startAddAccountSession(final String paramString1, final String paramString2, final String[] paramArrayOfString, Bundle paramBundle, final Activity paramActivity, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramString1 != null)
    {
      final Bundle localBundle = new Bundle();
      if (paramBundle != null) {
        localBundle.putAll(paramBundle);
      }
      localBundle.putString("androidPackageName", mContext.getPackageName());
      new AmsTask(paramActivity, paramHandler, paramAccountManagerCallback, paramString1)
      {
        public void doWork()
          throws RemoteException
        {
          IAccountManager localIAccountManager = mService;
          IAccountManagerResponse localIAccountManagerResponse = mResponse;
          String str1 = paramString1;
          String str2 = paramString2;
          String[] arrayOfString = paramArrayOfString;
          if (paramActivity != null) {}
          for (boolean bool = true;; bool = false) {
            break;
          }
          localIAccountManager.startAddAccountSession(localIAccountManagerResponse, str1, str2, arrayOfString, bool, localBundle);
        }
      }.start();
    }
    throw new IllegalArgumentException("accountType is null");
  }
  
  public AccountManagerFuture<Bundle> startUpdateCredentialsSession(final Account paramAccount, final String paramString, Bundle paramBundle, final Activity paramActivity, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramAccount != null)
    {
      final Bundle localBundle = new Bundle();
      if (paramBundle != null) {
        localBundle.putAll(paramBundle);
      }
      localBundle.putString("androidPackageName", mContext.getPackageName());
      new AmsTask(paramActivity, paramHandler, paramAccountManagerCallback, paramAccount)
      {
        public void doWork()
          throws RemoteException
        {
          IAccountManager localIAccountManager = mService;
          IAccountManagerResponse localIAccountManagerResponse = mResponse;
          Account localAccount = paramAccount;
          String str = paramString;
          if (paramActivity != null) {}
          for (boolean bool = true;; bool = false) {
            break;
          }
          localIAccountManager.startUpdateCredentialsSession(localIAccountManagerResponse, localAccount, str, bool, localBundle);
        }
      }.start();
    }
    throw new IllegalArgumentException("account is null");
  }
  
  public void updateAppPermission(Account paramAccount, String paramString, int paramInt, boolean paramBoolean)
  {
    try
    {
      mService.updateAppPermission(paramAccount, paramString, paramInt, paramBoolean);
      return;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public AccountManagerFuture<Bundle> updateCredentials(final Account paramAccount, final String paramString, final Bundle paramBundle, final Activity paramActivity, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
  {
    if (paramAccount != null) {
      new AmsTask(paramActivity, paramHandler, paramAccountManagerCallback, paramAccount)
      {
        public void doWork()
          throws RemoteException
        {
          IAccountManager localIAccountManager = mService;
          IAccountManagerResponse localIAccountManagerResponse = mResponse;
          Account localAccount = paramAccount;
          String str = paramString;
          if (paramActivity != null) {}
          for (boolean bool = true;; bool = false) {
            break;
          }
          localIAccountManager.updateCredentials(localIAccountManagerResponse, localAccount, str, bool, paramBundle);
        }
      }.start();
    }
    throw new IllegalArgumentException("account is null");
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AccountVisibility {}
  
  private abstract class AmsTask
    extends FutureTask<Bundle>
    implements AccountManagerFuture<Bundle>
  {
    final Activity mActivity;
    final AccountManagerCallback<Bundle> mCallback;
    final Handler mHandler;
    final IAccountManagerResponse mResponse;
    
    public AmsTask(Handler paramHandler, AccountManagerCallback<Bundle> paramAccountManagerCallback)
    {
      super()
      {
        public Bundle call()
          throws Exception
        {
          throw new IllegalStateException("this should never be called");
        }
      };
      mHandler = paramAccountManagerCallback;
      Object localObject;
      mCallback = localObject;
      mActivity = paramHandler;
      mResponse = new Response(null);
    }
    
    /* Error */
    private Bundle internalGetResult(Long paramLong, TimeUnit paramTimeUnit)
      throws OperationCanceledException, IOException, AuthenticatorException
    {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual 76	android/accounts/AccountManager$AmsTask:isDone	()Z
      //   4: ifne +10 -> 14
      //   7: aload_0
      //   8: getfield 30	android/accounts/AccountManager$AmsTask:this$0	Landroid/accounts/AccountManager;
      //   11: invokestatic 79	android/accounts/AccountManager:access$500	(Landroid/accounts/AccountManager;)V
      //   14: aload_1
      //   15: ifnonnull +39 -> 54
      //   18: aload_0
      //   19: invokevirtual 83	android/accounts/AccountManager$AmsTask:get	()Ljava/lang/Object;
      //   22: checkcast 85	android/os/Bundle
      //   25: astore_1
      //   26: aload_0
      //   27: iconst_1
      //   28: invokevirtual 89	android/accounts/AccountManager$AmsTask:cancel	(Z)Z
      //   31: pop
      //   32: aload_1
      //   33: areturn
      //   34: astore_1
      //   35: goto +149 -> 184
      //   38: astore_1
      //   39: goto +36 -> 75
      //   42: astore_1
      //   43: goto +114 -> 157
      //   46: astore_1
      //   47: goto +113 -> 160
      //   50: astore_1
      //   51: goto +123 -> 174
      //   54: aload_0
      //   55: aload_1
      //   56: invokevirtual 95	java/lang/Long:longValue	()J
      //   59: aload_2
      //   60: invokevirtual 98	android/accounts/AccountManager$AmsTask:get	(JLjava/util/concurrent/TimeUnit;)Ljava/lang/Object;
      //   63: checkcast 85	android/os/Bundle
      //   66: astore_1
      //   67: aload_0
      //   68: iconst_1
      //   69: invokevirtual 89	android/accounts/AccountManager$AmsTask:cancel	(Z)Z
      //   72: pop
      //   73: aload_1
      //   74: areturn
      //   75: aload_1
      //   76: invokevirtual 102	java/util/concurrent/ExecutionException:getCause	()Ljava/lang/Throwable;
      //   79: astore_1
      //   80: aload_1
      //   81: instanceof 62
      //   84: ifne +68 -> 152
      //   87: aload_1
      //   88: instanceof 104
      //   91: ifne +50 -> 141
      //   94: aload_1
      //   95: instanceof 64
      //   98: ifne +38 -> 136
      //   101: aload_1
      //   102: instanceof 106
      //   105: ifne +26 -> 131
      //   108: aload_1
      //   109: instanceof 108
      //   112: ifeq +8 -> 120
      //   115: aload_1
      //   116: checkcast 108	java/lang/Error
      //   119: athrow
      //   120: new 110	java/lang/IllegalStateException
      //   123: astore_2
      //   124: aload_2
      //   125: aload_1
      //   126: invokespecial 112	java/lang/IllegalStateException:<init>	(Ljava/lang/Throwable;)V
      //   129: aload_2
      //   130: athrow
      //   131: aload_1
      //   132: checkcast 106	java/lang/RuntimeException
      //   135: athrow
      //   136: aload_1
      //   137: checkcast 64	android/accounts/AuthenticatorException
      //   140: athrow
      //   141: new 64	android/accounts/AuthenticatorException
      //   144: astore_2
      //   145: aload_2
      //   146: aload_1
      //   147: invokespecial 113	android/accounts/AuthenticatorException:<init>	(Ljava/lang/Throwable;)V
      //   150: aload_2
      //   151: athrow
      //   152: aload_1
      //   153: checkcast 62	java/io/IOException
      //   156: athrow
      //   157: goto +3 -> 160
      //   160: aload_0
      //   161: iconst_1
      //   162: invokevirtual 89	android/accounts/AccountManager$AmsTask:cancel	(Z)Z
      //   165: pop
      //   166: new 60	android/accounts/OperationCanceledException
      //   169: dup
      //   170: invokespecial 116	android/accounts/OperationCanceledException:<init>	()V
      //   173: athrow
      //   174: new 60	android/accounts/OperationCanceledException
      //   177: astore_1
      //   178: aload_1
      //   179: invokespecial 116	android/accounts/OperationCanceledException:<init>	()V
      //   182: aload_1
      //   183: athrow
      //   184: aload_0
      //   185: iconst_1
      //   186: invokevirtual 89	android/accounts/AccountManager$AmsTask:cancel	(Z)Z
      //   189: pop
      //   190: aload_1
      //   191: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	192	0	this	AmsTask
      //   0	192	1	paramLong	Long
      //   0	192	2	paramTimeUnit	TimeUnit
      // Exception table:
      //   from	to	target	type
      //   18	26	34	finally
      //   54	67	34	finally
      //   75	120	34	finally
      //   120	131	34	finally
      //   131	136	34	finally
      //   136	141	34	finally
      //   141	152	34	finally
      //   152	157	34	finally
      //   174	184	34	finally
      //   18	26	38	java/util/concurrent/ExecutionException
      //   54	67	38	java/util/concurrent/ExecutionException
      //   18	26	42	java/lang/InterruptedException
      //   54	67	42	java/lang/InterruptedException
      //   18	26	46	java/util/concurrent/TimeoutException
      //   54	67	46	java/util/concurrent/TimeoutException
      //   18	26	50	java/util/concurrent/CancellationException
      //   54	67	50	java/util/concurrent/CancellationException
    }
    
    public abstract void doWork()
      throws RemoteException;
    
    protected void done()
    {
      if (mCallback != null) {
        AccountManager.this.postToHandler(mHandler, mCallback, this);
      }
    }
    
    public Bundle getResult()
      throws OperationCanceledException, IOException, AuthenticatorException
    {
      return internalGetResult(null, null);
    }
    
    public Bundle getResult(long paramLong, TimeUnit paramTimeUnit)
      throws OperationCanceledException, IOException, AuthenticatorException
    {
      return internalGetResult(Long.valueOf(paramLong), paramTimeUnit);
    }
    
    protected void set(Bundle paramBundle)
    {
      if (paramBundle == null) {
        Log.e("AccountManager", "the bundle must not be null", new Exception());
      }
      super.set(paramBundle);
    }
    
    public final AccountManagerFuture<Bundle> start()
    {
      try
      {
        doWork();
      }
      catch (RemoteException localRemoteException)
      {
        setException(localRemoteException);
      }
      return this;
    }
    
    private class Response
      extends IAccountManagerResponse.Stub
    {
      private Response() {}
      
      public void onError(int paramInt, String paramString)
      {
        if ((paramInt != 4) && (paramInt != 100) && (paramInt != 101))
        {
          setException(AccountManager.access$700(AccountManager.this, paramInt, paramString));
          return;
        }
        cancel(true);
      }
      
      public void onResult(Bundle paramBundle)
      {
        if (paramBundle == null)
        {
          onError(5, "null bundle returned");
          return;
        }
        Intent localIntent = (Intent)paramBundle.getParcelable("intent");
        if ((localIntent != null) && (mActivity != null)) {
          mActivity.startActivity(localIntent);
        } else if (paramBundle.getBoolean("retry")) {
          try
          {
            doWork();
          }
          catch (RemoteException paramBundle)
          {
            throw paramBundle.rethrowFromSystemServer();
          }
        } else {
          set(paramBundle);
        }
      }
    }
  }
  
  private abstract class BaseFutureTask<T>
    extends FutureTask<T>
  {
    final Handler mHandler;
    public final IAccountManagerResponse mResponse;
    
    public BaseFutureTask(Handler paramHandler)
    {
      super()
      {
        public T call()
          throws Exception
        {
          throw new IllegalStateException("this should never be called");
        }
      };
      mHandler = paramHandler;
      mResponse = new Response();
    }
    
    public abstract T bundleToResult(Bundle paramBundle)
      throws AuthenticatorException;
    
    public abstract void doWork()
      throws RemoteException;
    
    protected void postRunnableToHandler(Runnable paramRunnable)
    {
      Handler localHandler;
      if (mHandler == null) {
        localHandler = mMainHandler;
      } else {
        localHandler = mHandler;
      }
      localHandler.post(paramRunnable);
    }
    
    protected void startTask()
    {
      try
      {
        doWork();
      }
      catch (RemoteException localRemoteException)
      {
        setException(localRemoteException);
      }
    }
    
    protected class Response
      extends IAccountManagerResponse.Stub
    {
      protected Response() {}
      
      public void onError(int paramInt, String paramString)
      {
        if ((paramInt != 4) && (paramInt != 100) && (paramInt != 101))
        {
          setException(AccountManager.access$700(AccountManager.this, paramInt, paramString));
          return;
        }
        cancel(true);
      }
      
      public void onResult(Bundle paramBundle)
      {
        try
        {
          paramBundle = bundleToResult(paramBundle);
          if (paramBundle == null) {
            return;
          }
          set(paramBundle);
          return;
        }
        catch (AuthenticatorException paramBundle) {}catch (ClassCastException paramBundle) {}
        onError(5, "no result in response");
      }
    }
  }
  
  private abstract class Future2Task<T>
    extends AccountManager.BaseFutureTask<T>
    implements AccountManagerFuture<T>
  {
    final AccountManagerCallback<T> mCallback;
    
    public Future2Task(AccountManagerCallback<T> paramAccountManagerCallback)
    {
      super(paramAccountManagerCallback);
      Object localObject;
      mCallback = localObject;
    }
    
    /* Error */
    private T internalGetResult(Long paramLong, TimeUnit paramTimeUnit)
      throws OperationCanceledException, IOException, AuthenticatorException
    {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual 49	android/accounts/AccountManager$Future2Task:isDone	()Z
      //   4: ifne +10 -> 14
      //   7: aload_0
      //   8: getfield 21	android/accounts/AccountManager$Future2Task:this$0	Landroid/accounts/AccountManager;
      //   11: invokestatic 53	android/accounts/AccountManager:access$500	(Landroid/accounts/AccountManager;)V
      //   14: aload_1
      //   15: ifnonnull +36 -> 51
      //   18: aload_0
      //   19: invokevirtual 57	android/accounts/AccountManager$Future2Task:get	()Ljava/lang/Object;
      //   22: astore_1
      //   23: aload_0
      //   24: iconst_1
      //   25: invokevirtual 61	android/accounts/AccountManager$Future2Task:cancel	(Z)Z
      //   28: pop
      //   29: aload_1
      //   30: areturn
      //   31: astore_1
      //   32: goto +119 -> 151
      //   35: astore_1
      //   36: goto +33 -> 69
      //   39: astore_1
      //   40: goto +119 -> 159
      //   43: astore_1
      //   44: goto +118 -> 162
      //   47: astore_1
      //   48: goto +117 -> 165
      //   51: aload_0
      //   52: aload_1
      //   53: invokevirtual 67	java/lang/Long:longValue	()J
      //   56: aload_2
      //   57: invokevirtual 70	android/accounts/AccountManager$Future2Task:get	(JLjava/util/concurrent/TimeUnit;)Ljava/lang/Object;
      //   60: astore_1
      //   61: aload_0
      //   62: iconst_1
      //   63: invokevirtual 61	android/accounts/AccountManager$Future2Task:cancel	(Z)Z
      //   66: pop
      //   67: aload_1
      //   68: areturn
      //   69: aload_1
      //   70: invokevirtual 74	java/util/concurrent/ExecutionException:getCause	()Ljava/lang/Throwable;
      //   73: astore_1
      //   74: aload_1
      //   75: instanceof 35
      //   78: ifne +68 -> 146
      //   81: aload_1
      //   82: instanceof 76
      //   85: ifne +50 -> 135
      //   88: aload_1
      //   89: instanceof 37
      //   92: ifne +38 -> 130
      //   95: aload_1
      //   96: instanceof 78
      //   99: ifne +26 -> 125
      //   102: aload_1
      //   103: instanceof 80
      //   106: ifeq +8 -> 114
      //   109: aload_1
      //   110: checkcast 80	java/lang/Error
      //   113: athrow
      //   114: new 82	java/lang/IllegalStateException
      //   117: astore_2
      //   118: aload_2
      //   119: aload_1
      //   120: invokespecial 85	java/lang/IllegalStateException:<init>	(Ljava/lang/Throwable;)V
      //   123: aload_2
      //   124: athrow
      //   125: aload_1
      //   126: checkcast 78	java/lang/RuntimeException
      //   129: athrow
      //   130: aload_1
      //   131: checkcast 37	android/accounts/AuthenticatorException
      //   134: athrow
      //   135: new 37	android/accounts/AuthenticatorException
      //   138: astore_2
      //   139: aload_2
      //   140: aload_1
      //   141: invokespecial 86	android/accounts/AuthenticatorException:<init>	(Ljava/lang/Throwable;)V
      //   144: aload_2
      //   145: athrow
      //   146: aload_1
      //   147: checkcast 35	java/io/IOException
      //   150: athrow
      //   151: aload_0
      //   152: iconst_1
      //   153: invokevirtual 61	android/accounts/AccountManager$Future2Task:cancel	(Z)Z
      //   156: pop
      //   157: aload_1
      //   158: athrow
      //   159: goto +6 -> 165
      //   162: goto +3 -> 165
      //   165: aload_0
      //   166: iconst_1
      //   167: invokevirtual 61	android/accounts/AccountManager$Future2Task:cancel	(Z)Z
      //   170: pop
      //   171: new 33	android/accounts/OperationCanceledException
      //   174: dup
      //   175: invokespecial 89	android/accounts/OperationCanceledException:<init>	()V
      //   178: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	179	0	this	Future2Task
      //   0	179	1	paramLong	Long
      //   0	179	2	paramTimeUnit	TimeUnit
      // Exception table:
      //   from	to	target	type
      //   18	23	31	finally
      //   51	61	31	finally
      //   69	114	31	finally
      //   114	125	31	finally
      //   125	130	31	finally
      //   130	135	31	finally
      //   135	146	31	finally
      //   146	151	31	finally
      //   18	23	35	java/util/concurrent/ExecutionException
      //   51	61	35	java/util/concurrent/ExecutionException
      //   18	23	39	java/util/concurrent/CancellationException
      //   51	61	39	java/util/concurrent/CancellationException
      //   18	23	43	java/util/concurrent/TimeoutException
      //   51	61	43	java/util/concurrent/TimeoutException
      //   18	23	47	java/lang/InterruptedException
      //   51	61	47	java/lang/InterruptedException
    }
    
    protected void done()
    {
      if (mCallback != null) {
        postRunnableToHandler(new Runnable()
        {
          public void run()
          {
            mCallback.run(AccountManager.Future2Task.this);
          }
        });
      }
    }
    
    public T getResult()
      throws OperationCanceledException, IOException, AuthenticatorException
    {
      return internalGetResult(null, null);
    }
    
    public T getResult(long paramLong, TimeUnit paramTimeUnit)
      throws OperationCanceledException, IOException, AuthenticatorException
    {
      return internalGetResult(Long.valueOf(paramLong), paramTimeUnit);
    }
    
    public Future2Task<T> start()
    {
      startTask();
      return this;
    }
  }
  
  private class GetAuthTokenByTypeAndFeaturesTask
    extends AccountManager.AmsTask
    implements AccountManagerCallback<Bundle>
  {
    final String mAccountType;
    final Bundle mAddAccountOptions;
    final String mAuthTokenType;
    final String[] mFeatures;
    volatile AccountManagerFuture<Bundle> mFuture = null;
    final Bundle mLoginOptions;
    final AccountManagerCallback<Bundle> mMyCallback;
    private volatile int mNumAccounts = 0;
    
    GetAuthTokenByTypeAndFeaturesTask(String paramString, String[] paramArrayOfString, Activity paramActivity, Bundle paramBundle1, Bundle paramBundle2, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
    {
      super(paramBundle1, localHandler, paramHandler);
      if (paramString != null)
      {
        mAccountType = paramString;
        mAuthTokenType = paramArrayOfString;
        mFeatures = paramActivity;
        mAddAccountOptions = paramBundle2;
        mLoginOptions = paramAccountManagerCallback;
        mMyCallback = this;
        return;
      }
      throw new IllegalArgumentException("account type is null");
    }
    
    public void doWork()
      throws RemoteException
    {
      SeempLog.record(31);
      AccountManager.this.getAccountByTypeAndFeatures(mAccountType, mFeatures, new AccountManagerCallback()
      {
        public void run(AccountManagerFuture<Bundle> paramAnonymousAccountManagerFuture)
        {
          try
          {
            Object localObject = (Bundle)paramAnonymousAccountManagerFuture.getResult();
            paramAnonymousAccountManagerFuture = ((Bundle)localObject).getString("authAccount");
            localObject = ((Bundle)localObject).getString("accountType");
            if (paramAnonymousAccountManagerFuture == null)
            {
              if (mActivity != null)
              {
                mFuture = addAccount(mAccountType, mAuthTokenType, mFeatures, mAddAccountOptions, mActivity, mMyCallback, mHandler);
              }
              else
              {
                paramAnonymousAccountManagerFuture = new Bundle();
                paramAnonymousAccountManagerFuture.putString("authAccount", null);
                paramAnonymousAccountManagerFuture.putString("accountType", null);
                paramAnonymousAccountManagerFuture.putString("authtoken", null);
                paramAnonymousAccountManagerFuture.putBinder("accountAccessId", null);
                try
                {
                  mResponse.onResult(paramAnonymousAccountManagerFuture);
                }
                catch (RemoteException paramAnonymousAccountManagerFuture) {}
              }
            }
            else
            {
              AccountManager.GetAuthTokenByTypeAndFeaturesTask.access$1502(AccountManager.GetAuthTokenByTypeAndFeaturesTask.this, 1);
              paramAnonymousAccountManagerFuture = new Account(paramAnonymousAccountManagerFuture, (String)localObject);
              if (mActivity == null) {
                mFuture = getAuthToken(paramAnonymousAccountManagerFuture, mAuthTokenType, false, mMyCallback, mHandler);
              } else {
                mFuture = getAuthToken(paramAnonymousAccountManagerFuture, mAuthTokenType, mLoginOptions, mActivity, mMyCallback, mHandler);
              }
            }
            return;
          }
          catch (AuthenticatorException paramAnonymousAccountManagerFuture)
          {
            setException(paramAnonymousAccountManagerFuture);
            return;
          }
          catch (IOException paramAnonymousAccountManagerFuture)
          {
            setException(paramAnonymousAccountManagerFuture);
            return;
          }
          catch (OperationCanceledException paramAnonymousAccountManagerFuture)
          {
            setException(paramAnonymousAccountManagerFuture);
          }
        }
      }, mHandler);
    }
    
    public void run(AccountManagerFuture<Bundle> paramAccountManagerFuture)
    {
      try
      {
        Object localObject = (Bundle)paramAccountManagerFuture.getResult();
        if (mNumAccounts == 0)
        {
          paramAccountManagerFuture = ((Bundle)localObject).getString("authAccount");
          String str = ((Bundle)localObject).getString("accountType");
          if ((!TextUtils.isEmpty(paramAccountManagerFuture)) && (!TextUtils.isEmpty(str)))
          {
            localObject = ((Bundle)localObject).getString("accountAccessId");
            Account localAccount = new android/accounts/Account;
            localAccount.<init>(paramAccountManagerFuture, str, (String)localObject);
            mNumAccounts = 1;
            getAuthToken(localAccount, mAuthTokenType, null, mActivity, mMyCallback, mHandler);
            return;
          }
          paramAccountManagerFuture = new android/accounts/AuthenticatorException;
          paramAccountManagerFuture.<init>("account not in result");
          setException(paramAccountManagerFuture);
          return;
        }
        set((Bundle)localObject);
      }
      catch (AuthenticatorException paramAccountManagerFuture)
      {
        setException(paramAccountManagerFuture);
      }
      catch (IOException paramAccountManagerFuture)
      {
        setException(paramAccountManagerFuture);
      }
      catch (OperationCanceledException paramAccountManagerFuture)
      {
        cancel(true);
      }
    }
  }
}
