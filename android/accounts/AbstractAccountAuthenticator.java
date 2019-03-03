package android.accounts;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import java.util.Arrays;

public abstract class AbstractAccountAuthenticator
{
  private static final String KEY_ACCOUNT = "android.accounts.AbstractAccountAuthenticator.KEY_ACCOUNT";
  private static final String KEY_AUTH_TOKEN_TYPE = "android.accounts.AbstractAccountAuthenticato.KEY_AUTH_TOKEN_TYPE";
  public static final String KEY_CUSTOM_TOKEN_EXPIRY = "android.accounts.expiry";
  private static final String KEY_OPTIONS = "android.accounts.AbstractAccountAuthenticator.KEY_OPTIONS";
  private static final String KEY_REQUIRED_FEATURES = "android.accounts.AbstractAccountAuthenticator.KEY_REQUIRED_FEATURES";
  private static final String TAG = "AccountAuthenticator";
  private final Context mContext;
  private Transport mTransport = new Transport(null);
  
  public AbstractAccountAuthenticator(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private void checkBinderPermission()
  {
    int i = Binder.getCallingUid();
    if (mContext.checkCallingOrSelfPermission("android.permission.ACCOUNT_MANAGER") == 0) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("caller uid ");
    localStringBuilder.append(i);
    localStringBuilder.append(" lacks ");
    localStringBuilder.append("android.permission.ACCOUNT_MANAGER");
    throw new SecurityException(localStringBuilder.toString());
  }
  
  private void handleException(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString1, String paramString2, Exception paramException)
    throws RemoteException
  {
    StringBuilder localStringBuilder;
    if ((paramException instanceof NetworkErrorException))
    {
      if (Log.isLoggable("AccountAuthenticator", 2))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString1);
        localStringBuilder.append("(");
        localStringBuilder.append(paramString2);
        localStringBuilder.append(")");
        Log.v("AccountAuthenticator", localStringBuilder.toString(), paramException);
      }
      paramIAccountAuthenticatorResponse.onError(3, paramException.getMessage());
    }
    else if ((paramException instanceof UnsupportedOperationException))
    {
      if (Log.isLoggable("AccountAuthenticator", 2))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString1);
        localStringBuilder.append("(");
        localStringBuilder.append(paramString2);
        localStringBuilder.append(")");
        Log.v("AccountAuthenticator", localStringBuilder.toString(), paramException);
      }
      paramString2 = new StringBuilder();
      paramString2.append(paramString1);
      paramString2.append(" not supported");
      paramIAccountAuthenticatorResponse.onError(6, paramString2.toString());
    }
    else if ((paramException instanceof IllegalArgumentException))
    {
      if (Log.isLoggable("AccountAuthenticator", 2))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString1);
        localStringBuilder.append("(");
        localStringBuilder.append(paramString2);
        localStringBuilder.append(")");
        Log.v("AccountAuthenticator", localStringBuilder.toString(), paramException);
      }
      paramString2 = new StringBuilder();
      paramString2.append(paramString1);
      paramString2.append(" not supported");
      paramIAccountAuthenticatorResponse.onError(7, paramString2.toString());
    }
    else
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString1);
      localStringBuilder.append("(");
      localStringBuilder.append(paramString2);
      localStringBuilder.append(")");
      Log.w("AccountAuthenticator", localStringBuilder.toString(), paramException);
      paramString2 = new StringBuilder();
      paramString2.append(paramString1);
      paramString2.append(" failed");
      paramIAccountAuthenticatorResponse.onError(1, paramString2.toString());
    }
  }
  
  public abstract Bundle addAccount(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, String paramString1, String paramString2, String[] paramArrayOfString, Bundle paramBundle)
    throws NetworkErrorException;
  
  public Bundle addAccountFromCredentials(final AccountAuthenticatorResponse paramAccountAuthenticatorResponse, Account paramAccount, Bundle paramBundle)
    throws NetworkErrorException
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        Bundle localBundle = new Bundle();
        localBundle.putBoolean("booleanResult", false);
        paramAccountAuthenticatorResponse.onResult(localBundle);
      }
    }).start();
    return null;
  }
  
  public abstract Bundle confirmCredentials(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, Account paramAccount, Bundle paramBundle)
    throws NetworkErrorException;
  
  public abstract Bundle editProperties(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, String paramString);
  
  public Bundle finishSession(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, String paramString, Bundle paramBundle)
    throws NetworkErrorException
  {
    if (TextUtils.isEmpty(paramString))
    {
      Log.e("AccountAuthenticator", "Account type cannot be empty.");
      paramAccountAuthenticatorResponse = new Bundle();
      paramAccountAuthenticatorResponse.putInt("errorCode", 7);
      paramAccountAuthenticatorResponse.putString("errorMessage", "accountType cannot be empty.");
      return paramAccountAuthenticatorResponse;
    }
    if (paramBundle == null)
    {
      Log.e("AccountAuthenticator", "Session bundle cannot be null.");
      paramAccountAuthenticatorResponse = new Bundle();
      paramAccountAuthenticatorResponse.putInt("errorCode", 7);
      paramAccountAuthenticatorResponse.putString("errorMessage", "sessionBundle cannot be null.");
      return paramAccountAuthenticatorResponse;
    }
    if (!paramBundle.containsKey("android.accounts.AbstractAccountAuthenticato.KEY_AUTH_TOKEN_TYPE"))
    {
      paramString = new Bundle();
      paramString.putInt("errorCode", 6);
      paramString.putString("errorMessage", "Authenticator must override finishSession if startAddAccountSession or startUpdateCredentialsSession is overridden.");
      paramAccountAuthenticatorResponse.onResult(paramString);
      return paramString;
    }
    String str = paramBundle.getString("android.accounts.AbstractAccountAuthenticato.KEY_AUTH_TOKEN_TYPE");
    Bundle localBundle1 = paramBundle.getBundle("android.accounts.AbstractAccountAuthenticator.KEY_OPTIONS");
    String[] arrayOfString = paramBundle.getStringArray("android.accounts.AbstractAccountAuthenticator.KEY_REQUIRED_FEATURES");
    Account localAccount = (Account)paramBundle.getParcelable("android.accounts.AbstractAccountAuthenticator.KEY_ACCOUNT");
    boolean bool = paramBundle.containsKey("android.accounts.AbstractAccountAuthenticator.KEY_ACCOUNT");
    Bundle localBundle2 = new Bundle(paramBundle);
    localBundle2.remove("android.accounts.AbstractAccountAuthenticato.KEY_AUTH_TOKEN_TYPE");
    localBundle2.remove("android.accounts.AbstractAccountAuthenticator.KEY_REQUIRED_FEATURES");
    localBundle2.remove("android.accounts.AbstractAccountAuthenticator.KEY_OPTIONS");
    localBundle2.remove("android.accounts.AbstractAccountAuthenticator.KEY_ACCOUNT");
    paramBundle = localBundle2;
    if (localBundle1 != null)
    {
      localBundle1.putAll(localBundle2);
      paramBundle = localBundle1;
    }
    if (bool) {
      return updateCredentials(paramAccountAuthenticatorResponse, localAccount, str, localBundle1);
    }
    return addAccount(paramAccountAuthenticatorResponse, paramString, str, arrayOfString, paramBundle);
  }
  
  public Bundle getAccountCredentialsForCloning(final AccountAuthenticatorResponse paramAccountAuthenticatorResponse, Account paramAccount)
    throws NetworkErrorException
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        Bundle localBundle = new Bundle();
        localBundle.putBoolean("booleanResult", false);
        paramAccountAuthenticatorResponse.onResult(localBundle);
      }
    }).start();
    return null;
  }
  
  public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, Account paramAccount)
    throws NetworkErrorException
  {
    paramAccountAuthenticatorResponse = new Bundle();
    paramAccountAuthenticatorResponse.putBoolean("booleanResult", true);
    return paramAccountAuthenticatorResponse;
  }
  
  public abstract Bundle getAuthToken(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
    throws NetworkErrorException;
  
  public abstract String getAuthTokenLabel(String paramString);
  
  public final IBinder getIBinder()
  {
    return mTransport.asBinder();
  }
  
  public abstract Bundle hasFeatures(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, Account paramAccount, String[] paramArrayOfString)
    throws NetworkErrorException;
  
  public Bundle isCredentialsUpdateSuggested(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, Account paramAccount, String paramString)
    throws NetworkErrorException
  {
    paramAccountAuthenticatorResponse = new Bundle();
    paramAccountAuthenticatorResponse.putBoolean("booleanResult", false);
    return paramAccountAuthenticatorResponse;
  }
  
  public Bundle startAddAccountSession(final AccountAuthenticatorResponse paramAccountAuthenticatorResponse, String paramString1, final String paramString2, final String[] paramArrayOfString, final Bundle paramBundle)
    throws NetworkErrorException
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        Bundle localBundle1 = new Bundle();
        localBundle1.putString("android.accounts.AbstractAccountAuthenticato.KEY_AUTH_TOKEN_TYPE", paramString2);
        localBundle1.putStringArray("android.accounts.AbstractAccountAuthenticator.KEY_REQUIRED_FEATURES", paramArrayOfString);
        localBundle1.putBundle("android.accounts.AbstractAccountAuthenticator.KEY_OPTIONS", paramBundle);
        Bundle localBundle2 = new Bundle();
        localBundle2.putBundle("accountSessionBundle", localBundle1);
        paramAccountAuthenticatorResponse.onResult(localBundle2);
      }
    }).start();
    return null;
  }
  
  public Bundle startUpdateCredentialsSession(final AccountAuthenticatorResponse paramAccountAuthenticatorResponse, final Account paramAccount, final String paramString, final Bundle paramBundle)
    throws NetworkErrorException
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        Bundle localBundle1 = new Bundle();
        localBundle1.putString("android.accounts.AbstractAccountAuthenticato.KEY_AUTH_TOKEN_TYPE", paramString);
        localBundle1.putParcelable("android.accounts.AbstractAccountAuthenticator.KEY_ACCOUNT", paramAccount);
        localBundle1.putBundle("android.accounts.AbstractAccountAuthenticator.KEY_OPTIONS", paramBundle);
        Bundle localBundle2 = new Bundle();
        localBundle2.putBundle("accountSessionBundle", localBundle1);
        paramAccountAuthenticatorResponse.onResult(localBundle2);
      }
    }).start();
    return null;
  }
  
  public abstract Bundle updateCredentials(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
    throws NetworkErrorException;
  
  private class Transport
    extends IAccountAuthenticator.Stub
  {
    private Transport() {}
    
    public void addAccount(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString1, String paramString2, String[] paramArrayOfString, Bundle paramBundle)
      throws RemoteException
    {
      Object localObject1;
      Object localObject2;
      if (Log.isLoggable("AccountAuthenticator", 2))
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("addAccount: accountType ");
        ((StringBuilder)localObject1).append(paramString1);
        ((StringBuilder)localObject1).append(", authTokenType ");
        ((StringBuilder)localObject1).append(paramString2);
        ((StringBuilder)localObject1).append(", features ");
        if (paramArrayOfString == null) {
          localObject2 = "[]";
        } else {
          localObject2 = Arrays.toString(paramArrayOfString);
        }
        ((StringBuilder)localObject1).append((String)localObject2);
        Log.v("AccountAuthenticator", ((StringBuilder)localObject1).toString());
      }
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        localObject1 = AbstractAccountAuthenticator.this;
        localObject2 = new android/accounts/AccountAuthenticatorResponse;
        ((AccountAuthenticatorResponse)localObject2).<init>(paramIAccountAuthenticatorResponse);
        paramArrayOfString = ((AbstractAccountAuthenticator)localObject1).addAccount((AccountAuthenticatorResponse)localObject2, paramString1, paramString2, paramArrayOfString, paramBundle);
        if (Log.isLoggable("AccountAuthenticator", 2))
        {
          if (paramArrayOfString != null) {
            paramArrayOfString.keySet();
          }
          paramString2 = new java/lang/StringBuilder;
          paramString2.<init>();
          paramString2.append("addAccount: result ");
          paramString2.append(AccountManager.sanitizeResult(paramArrayOfString));
          Log.v("AccountAuthenticator", paramString2.toString());
        }
        if (paramArrayOfString != null) {
          paramIAccountAuthenticatorResponse.onResult(paramArrayOfString);
        } else {
          paramIAccountAuthenticatorResponse.onError(5, "null bundle returned");
        }
      }
      catch (Exception paramString2)
      {
        AbstractAccountAuthenticator.this.handleException(paramIAccountAuthenticatorResponse, "addAccount", paramString1, paramString2);
      }
    }
    
    public void addAccountFromCredentials(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, Bundle paramBundle)
      throws RemoteException
    {
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        AbstractAccountAuthenticator localAbstractAccountAuthenticator = AbstractAccountAuthenticator.this;
        AccountAuthenticatorResponse localAccountAuthenticatorResponse = new android/accounts/AccountAuthenticatorResponse;
        localAccountAuthenticatorResponse.<init>(paramIAccountAuthenticatorResponse);
        paramBundle = localAbstractAccountAuthenticator.addAccountFromCredentials(localAccountAuthenticatorResponse, paramAccount, paramBundle);
        if (paramBundle != null) {
          paramIAccountAuthenticatorResponse.onResult(paramBundle);
        }
      }
      catch (Exception paramBundle)
      {
        AbstractAccountAuthenticator.this.handleException(paramIAccountAuthenticatorResponse, "addAccountFromCredentials", paramAccount.toString(), paramBundle);
      }
    }
    
    public void confirmCredentials(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, Bundle paramBundle)
      throws RemoteException
    {
      Object localObject;
      if (Log.isLoggable("AccountAuthenticator", 2))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("confirmCredentials: ");
        ((StringBuilder)localObject).append(paramAccount);
        Log.v("AccountAuthenticator", ((StringBuilder)localObject).toString());
      }
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        localObject = AbstractAccountAuthenticator.this;
        AccountAuthenticatorResponse localAccountAuthenticatorResponse = new android/accounts/AccountAuthenticatorResponse;
        localAccountAuthenticatorResponse.<init>(paramIAccountAuthenticatorResponse);
        paramBundle = ((AbstractAccountAuthenticator)localObject).confirmCredentials(localAccountAuthenticatorResponse, paramAccount, paramBundle);
        if (Log.isLoggable("AccountAuthenticator", 2))
        {
          if (paramBundle != null) {
            paramBundle.keySet();
          }
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("confirmCredentials: result ");
          ((StringBuilder)localObject).append(AccountManager.sanitizeResult(paramBundle));
          Log.v("AccountAuthenticator", ((StringBuilder)localObject).toString());
        }
        if (paramBundle != null) {
          paramIAccountAuthenticatorResponse.onResult(paramBundle);
        }
      }
      catch (Exception paramBundle)
      {
        AbstractAccountAuthenticator.this.handleException(paramIAccountAuthenticatorResponse, "confirmCredentials", paramAccount.toString(), paramBundle);
      }
    }
    
    public void editProperties(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString)
      throws RemoteException
    {
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        Object localObject = AbstractAccountAuthenticator.this;
        AccountAuthenticatorResponse localAccountAuthenticatorResponse = new android/accounts/AccountAuthenticatorResponse;
        localAccountAuthenticatorResponse.<init>(paramIAccountAuthenticatorResponse);
        localObject = ((AbstractAccountAuthenticator)localObject).editProperties(localAccountAuthenticatorResponse, paramString);
        if (localObject != null) {
          paramIAccountAuthenticatorResponse.onResult((Bundle)localObject);
        }
      }
      catch (Exception localException)
      {
        AbstractAccountAuthenticator.this.handleException(paramIAccountAuthenticatorResponse, "editProperties", paramString, localException);
      }
    }
    
    public void finishSession(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString, Bundle paramBundle)
      throws RemoteException
    {
      Object localObject;
      if (Log.isLoggable("AccountAuthenticator", 2))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("finishSession: accountType ");
        ((StringBuilder)localObject).append(paramString);
        Log.v("AccountAuthenticator", ((StringBuilder)localObject).toString());
      }
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        localObject = AbstractAccountAuthenticator.this;
        AccountAuthenticatorResponse localAccountAuthenticatorResponse = new android/accounts/AccountAuthenticatorResponse;
        localAccountAuthenticatorResponse.<init>(paramIAccountAuthenticatorResponse);
        paramBundle = ((AbstractAccountAuthenticator)localObject).finishSession(localAccountAuthenticatorResponse, paramString, paramBundle);
        if (paramBundle != null) {
          paramBundle.keySet();
        }
        if (Log.isLoggable("AccountAuthenticator", 2))
        {
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("finishSession: result ");
          ((StringBuilder)localObject).append(AccountManager.sanitizeResult(paramBundle));
          Log.v("AccountAuthenticator", ((StringBuilder)localObject).toString());
        }
        if (paramBundle != null) {
          paramIAccountAuthenticatorResponse.onResult(paramBundle);
        }
      }
      catch (Exception paramBundle)
      {
        AbstractAccountAuthenticator.this.handleException(paramIAccountAuthenticatorResponse, "finishSession", paramString, paramBundle);
      }
    }
    
    public void getAccountCredentialsForCloning(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount)
      throws RemoteException
    {
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        Object localObject = AbstractAccountAuthenticator.this;
        AccountAuthenticatorResponse localAccountAuthenticatorResponse = new android/accounts/AccountAuthenticatorResponse;
        localAccountAuthenticatorResponse.<init>(paramIAccountAuthenticatorResponse);
        localObject = ((AbstractAccountAuthenticator)localObject).getAccountCredentialsForCloning(localAccountAuthenticatorResponse, paramAccount);
        if (localObject != null) {
          paramIAccountAuthenticatorResponse.onResult((Bundle)localObject);
        }
      }
      catch (Exception localException)
      {
        AbstractAccountAuthenticator.this.handleException(paramIAccountAuthenticatorResponse, "getAccountCredentialsForCloning", paramAccount.toString(), localException);
      }
    }
    
    public void getAccountRemovalAllowed(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount)
      throws RemoteException
    {
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        AbstractAccountAuthenticator localAbstractAccountAuthenticator = AbstractAccountAuthenticator.this;
        Object localObject = new android/accounts/AccountAuthenticatorResponse;
        ((AccountAuthenticatorResponse)localObject).<init>(paramIAccountAuthenticatorResponse);
        localObject = localAbstractAccountAuthenticator.getAccountRemovalAllowed((AccountAuthenticatorResponse)localObject, paramAccount);
        if (localObject != null) {
          paramIAccountAuthenticatorResponse.onResult((Bundle)localObject);
        }
      }
      catch (Exception localException)
      {
        AbstractAccountAuthenticator.this.handleException(paramIAccountAuthenticatorResponse, "getAccountRemovalAllowed", paramAccount.toString(), localException);
      }
    }
    
    public void getAuthToken(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
      throws RemoteException
    {
      Object localObject;
      if (Log.isLoggable("AccountAuthenticator", 2))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("getAuthToken: ");
        ((StringBuilder)localObject).append(paramAccount);
        ((StringBuilder)localObject).append(", authTokenType ");
        ((StringBuilder)localObject).append(paramString);
        Log.v("AccountAuthenticator", ((StringBuilder)localObject).toString());
      }
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        localObject = AbstractAccountAuthenticator.this;
        AccountAuthenticatorResponse localAccountAuthenticatorResponse = new android/accounts/AccountAuthenticatorResponse;
        localAccountAuthenticatorResponse.<init>(paramIAccountAuthenticatorResponse);
        paramBundle = ((AbstractAccountAuthenticator)localObject).getAuthToken(localAccountAuthenticatorResponse, paramAccount, paramString, paramBundle);
        if (Log.isLoggable("AccountAuthenticator", 2))
        {
          if (paramBundle != null) {
            paramBundle.keySet();
          }
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("getAuthToken: result ");
          ((StringBuilder)localObject).append(AccountManager.sanitizeResult(paramBundle));
          Log.v("AccountAuthenticator", ((StringBuilder)localObject).toString());
        }
        if (paramBundle != null) {
          paramIAccountAuthenticatorResponse.onResult(paramBundle);
        }
      }
      catch (Exception localException)
      {
        paramBundle = AbstractAccountAuthenticator.this;
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramAccount.toString());
        ((StringBuilder)localObject).append(",");
        ((StringBuilder)localObject).append(paramString);
        paramBundle.handleException(paramIAccountAuthenticatorResponse, "getAuthToken", ((StringBuilder)localObject).toString(), localException);
      }
    }
    
    public void getAuthTokenLabel(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString)
      throws RemoteException
    {
      StringBuilder localStringBuilder;
      if (Log.isLoggable("AccountAuthenticator", 2))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("getAuthTokenLabel: authTokenType ");
        localStringBuilder.append(paramString);
        Log.v("AccountAuthenticator", localStringBuilder.toString());
      }
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        Bundle localBundle = new android/os/Bundle;
        localBundle.<init>();
        localBundle.putString("authTokenLabelKey", getAuthTokenLabel(paramString));
        if (Log.isLoggable("AccountAuthenticator", 2))
        {
          localBundle.keySet();
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("getAuthTokenLabel: result ");
          localStringBuilder.append(AccountManager.sanitizeResult(localBundle));
          Log.v("AccountAuthenticator", localStringBuilder.toString());
        }
        paramIAccountAuthenticatorResponse.onResult(localBundle);
      }
      catch (Exception localException)
      {
        AbstractAccountAuthenticator.this.handleException(paramIAccountAuthenticatorResponse, "getAuthTokenLabel", paramString, localException);
      }
    }
    
    public void hasFeatures(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String[] paramArrayOfString)
      throws RemoteException
    {
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        AbstractAccountAuthenticator localAbstractAccountAuthenticator = AbstractAccountAuthenticator.this;
        AccountAuthenticatorResponse localAccountAuthenticatorResponse = new android/accounts/AccountAuthenticatorResponse;
        localAccountAuthenticatorResponse.<init>(paramIAccountAuthenticatorResponse);
        paramArrayOfString = localAbstractAccountAuthenticator.hasFeatures(localAccountAuthenticatorResponse, paramAccount, paramArrayOfString);
        if (paramArrayOfString != null) {
          paramIAccountAuthenticatorResponse.onResult(paramArrayOfString);
        }
      }
      catch (Exception paramArrayOfString)
      {
        AbstractAccountAuthenticator.this.handleException(paramIAccountAuthenticatorResponse, "hasFeatures", paramAccount.toString(), paramArrayOfString);
      }
    }
    
    public void isCredentialsUpdateSuggested(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String paramString)
      throws RemoteException
    {
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        AbstractAccountAuthenticator localAbstractAccountAuthenticator = AbstractAccountAuthenticator.this;
        AccountAuthenticatorResponse localAccountAuthenticatorResponse = new android/accounts/AccountAuthenticatorResponse;
        localAccountAuthenticatorResponse.<init>(paramIAccountAuthenticatorResponse);
        paramString = localAbstractAccountAuthenticator.isCredentialsUpdateSuggested(localAccountAuthenticatorResponse, paramAccount, paramString);
        if (paramString != null) {
          paramIAccountAuthenticatorResponse.onResult(paramString);
        }
      }
      catch (Exception paramString)
      {
        AbstractAccountAuthenticator.this.handleException(paramIAccountAuthenticatorResponse, "isCredentialsUpdateSuggested", paramAccount.toString(), paramString);
      }
    }
    
    public void startAddAccountSession(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString1, String paramString2, String[] paramArrayOfString, Bundle paramBundle)
      throws RemoteException
    {
      Object localObject1;
      Object localObject2;
      if (Log.isLoggable("AccountAuthenticator", 2))
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("startAddAccountSession: accountType ");
        ((StringBuilder)localObject1).append(paramString1);
        ((StringBuilder)localObject1).append(", authTokenType ");
        ((StringBuilder)localObject1).append(paramString2);
        ((StringBuilder)localObject1).append(", features ");
        if (paramArrayOfString == null) {
          localObject2 = "[]";
        } else {
          localObject2 = Arrays.toString(paramArrayOfString);
        }
        ((StringBuilder)localObject1).append((String)localObject2);
        Log.v("AccountAuthenticator", ((StringBuilder)localObject1).toString());
      }
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        localObject1 = AbstractAccountAuthenticator.this;
        localObject2 = new android/accounts/AccountAuthenticatorResponse;
        ((AccountAuthenticatorResponse)localObject2).<init>(paramIAccountAuthenticatorResponse);
        paramArrayOfString = ((AbstractAccountAuthenticator)localObject1).startAddAccountSession((AccountAuthenticatorResponse)localObject2, paramString1, paramString2, paramArrayOfString, paramBundle);
        if (Log.isLoggable("AccountAuthenticator", 2))
        {
          if (paramArrayOfString != null) {
            paramArrayOfString.keySet();
          }
          paramString2 = new java/lang/StringBuilder;
          paramString2.<init>();
          paramString2.append("startAddAccountSession: result ");
          paramString2.append(AccountManager.sanitizeResult(paramArrayOfString));
          Log.v("AccountAuthenticator", paramString2.toString());
        }
        if (paramArrayOfString != null) {
          paramIAccountAuthenticatorResponse.onResult(paramArrayOfString);
        }
      }
      catch (Exception paramString2)
      {
        AbstractAccountAuthenticator.this.handleException(paramIAccountAuthenticatorResponse, "startAddAccountSession", paramString1, paramString2);
      }
    }
    
    public void startUpdateCredentialsSession(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
      throws RemoteException
    {
      Object localObject1;
      if (Log.isLoggable("AccountAuthenticator", 2))
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("startUpdateCredentialsSession: ");
        ((StringBuilder)localObject1).append(paramAccount);
        ((StringBuilder)localObject1).append(", authTokenType ");
        ((StringBuilder)localObject1).append(paramString);
        Log.v("AccountAuthenticator", ((StringBuilder)localObject1).toString());
      }
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        localObject1 = AbstractAccountAuthenticator.this;
        localObject2 = new android/accounts/AccountAuthenticatorResponse;
        ((AccountAuthenticatorResponse)localObject2).<init>(paramIAccountAuthenticatorResponse);
        localObject1 = ((AbstractAccountAuthenticator)localObject1).startUpdateCredentialsSession((AccountAuthenticatorResponse)localObject2, paramAccount, paramString, paramBundle);
        if (Log.isLoggable("AccountAuthenticator", 2))
        {
          if (localObject1 != null) {
            ((Bundle)localObject1).keySet();
          }
          paramBundle = new java/lang/StringBuilder;
          paramBundle.<init>();
          paramBundle.append("startUpdateCredentialsSession: result ");
          paramBundle.append(AccountManager.sanitizeResult((Bundle)localObject1));
          Log.v("AccountAuthenticator", paramBundle.toString());
        }
        if (localObject1 != null) {
          paramIAccountAuthenticatorResponse.onResult((Bundle)localObject1);
        }
      }
      catch (Exception paramBundle)
      {
        Object localObject2 = AbstractAccountAuthenticator.this;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append(paramAccount.toString());
        ((StringBuilder)localObject1).append(",");
        ((StringBuilder)localObject1).append(paramString);
        ((AbstractAccountAuthenticator)localObject2).handleException(paramIAccountAuthenticatorResponse, "startUpdateCredentialsSession", ((StringBuilder)localObject1).toString(), paramBundle);
      }
    }
    
    public void updateCredentials(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
      throws RemoteException
    {
      Object localObject1;
      if (Log.isLoggable("AccountAuthenticator", 2))
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("updateCredentials: ");
        ((StringBuilder)localObject1).append(paramAccount);
        ((StringBuilder)localObject1).append(", authTokenType ");
        ((StringBuilder)localObject1).append(paramString);
        Log.v("AccountAuthenticator", ((StringBuilder)localObject1).toString());
      }
      AbstractAccountAuthenticator.this.checkBinderPermission();
      try
      {
        localObject2 = AbstractAccountAuthenticator.this;
        localObject1 = new android/accounts/AccountAuthenticatorResponse;
        ((AccountAuthenticatorResponse)localObject1).<init>(paramIAccountAuthenticatorResponse);
        localObject1 = ((AbstractAccountAuthenticator)localObject2).updateCredentials((AccountAuthenticatorResponse)localObject1, paramAccount, paramString, paramBundle);
        if (Log.isLoggable("AccountAuthenticator", 2))
        {
          if (localObject1 != null) {
            ((Bundle)localObject1).keySet();
          }
          paramBundle = new java/lang/StringBuilder;
          paramBundle.<init>();
          paramBundle.append("updateCredentials: result ");
          paramBundle.append(AccountManager.sanitizeResult((Bundle)localObject1));
          Log.v("AccountAuthenticator", paramBundle.toString());
        }
        if (localObject1 != null) {
          paramIAccountAuthenticatorResponse.onResult((Bundle)localObject1);
        }
      }
      catch (Exception localException)
      {
        paramBundle = AbstractAccountAuthenticator.this;
        Object localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append(paramAccount.toString());
        ((StringBuilder)localObject2).append(",");
        ((StringBuilder)localObject2).append(paramString);
        paramBundle.handleException(paramIAccountAuthenticatorResponse, "updateCredentials", ((StringBuilder)localObject2).toString(), localException);
      }
    }
  }
}
