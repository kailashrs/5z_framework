package android.telecom;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class AuthenticatorService
  extends Service
{
  private static Authenticator mAuthenticator;
  
  public AuthenticatorService() {}
  
  public IBinder onBind(Intent paramIntent)
  {
    return mAuthenticator.getIBinder();
  }
  
  public void onCreate()
  {
    mAuthenticator = new Authenticator(this);
  }
  
  public class Authenticator
    extends AbstractAccountAuthenticator
  {
    public Authenticator(Context paramContext)
    {
      super();
    }
    
    public Bundle addAccount(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, String paramString1, String paramString2, String[] paramArrayOfString, Bundle paramBundle)
      throws NetworkErrorException
    {
      return null;
    }
    
    public Bundle confirmCredentials(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, Account paramAccount, Bundle paramBundle)
      throws NetworkErrorException
    {
      return null;
    }
    
    public Bundle editProperties(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, String paramString)
    {
      throw new UnsupportedOperationException();
    }
    
    public Bundle getAuthToken(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
      throws NetworkErrorException
    {
      throw new UnsupportedOperationException();
    }
    
    public String getAuthTokenLabel(String paramString)
    {
      throw new UnsupportedOperationException();
    }
    
    public Bundle hasFeatures(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, Account paramAccount, String[] paramArrayOfString)
      throws NetworkErrorException
    {
      throw new UnsupportedOperationException();
    }
    
    public Bundle updateCredentials(AccountAuthenticatorResponse paramAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
      throws NetworkErrorException
    {
      throw new UnsupportedOperationException();
    }
  }
}
