package android.accounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;

public class GrantCredentialsPermissionActivity
  extends Activity
  implements View.OnClickListener
{
  public static final String EXTRAS_ACCOUNT = "account";
  public static final String EXTRAS_AUTH_TOKEN_TYPE = "authTokenType";
  public static final String EXTRAS_REQUESTING_UID = "uid";
  public static final String EXTRAS_RESPONSE = "response";
  private Account mAccount;
  private String mAuthTokenType;
  protected LayoutInflater mInflater;
  private Bundle mResultBundle = null;
  private int mUid;
  
  public GrantCredentialsPermissionActivity() {}
  
  private String getAccountLabel(Account paramAccount)
  {
    AuthenticatorDescription[] arrayOfAuthenticatorDescription = AccountManager.get(this).getAuthenticatorTypes();
    int i = 0;
    int j = arrayOfAuthenticatorDescription.length;
    while (i < j)
    {
      Object localObject = arrayOfAuthenticatorDescription[i];
      if (type.equals(type)) {
        try
        {
          localObject = createPackageContext(packageName, 0).getString(labelId);
          return localObject;
        }
        catch (Resources.NotFoundException localNotFoundException)
        {
          return type;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          return type;
        }
      }
      i++;
    }
    return type;
  }
  
  private View newPackageView(String paramString)
  {
    View localView = mInflater.inflate(17367242, null);
    ((TextView)localView.findViewById(16909217)).setText(paramString);
    return localView;
  }
  
  public void finish()
  {
    AccountAuthenticatorResponse localAccountAuthenticatorResponse = (AccountAuthenticatorResponse)getIntent().getParcelableExtra("response");
    if (localAccountAuthenticatorResponse != null) {
      if (mResultBundle != null) {
        localAccountAuthenticatorResponse.onResult(mResultBundle);
      } else {
        localAccountAuthenticatorResponse.onError(4, "canceled");
      }
    }
    super.finish();
  }
  
  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i != 16908732)
    {
      if (i == 16908903)
      {
        AccountManager.get(this).updateAppPermission(mAccount, mAuthTokenType, mUid, false);
        setResult(0);
      }
    }
    else
    {
      AccountManager.get(this).updateAppPermission(mAccount, mAuthTokenType, mUid, true);
      paramView = new Intent();
      paramView.putExtra("retry", true);
      setResult(-1, paramView);
      setAccountAuthenticatorResult(paramView.getExtras());
    }
    finish();
  }
  
  protected void onCreate(final Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(17367180);
    setTitle(17040073);
    mInflater = ((LayoutInflater)getSystemService("layout_inflater"));
    paramBundle = getIntent().getExtras();
    if (paramBundle == null)
    {
      setResult(0);
      finish();
      return;
    }
    mAccount = ((Account)paramBundle.getParcelable("account"));
    mAuthTokenType = paramBundle.getString("authTokenType");
    mUid = paramBundle.getInt("uid");
    PackageManager localPackageManager = getPackageManager();
    String[] arrayOfString = localPackageManager.getPackagesForUid(mUid);
    if ((mAccount != null) && (mAuthTokenType != null) && (arrayOfString != null)) {
      try
      {
        String str1 = getAccountLabel(mAccount);
        paramBundle = (TextView)findViewById(16908787);
        paramBundle.setVisibility(8);
        paramBundle = new AccountManagerCallback()
        {
          public void run(AccountManagerFuture<String> paramAnonymousAccountManagerFuture)
          {
            try
            {
              paramAnonymousAccountManagerFuture = (String)paramAnonymousAccountManagerFuture.getResult();
              if (!TextUtils.isEmpty(paramAnonymousAccountManagerFuture))
              {
                GrantCredentialsPermissionActivity localGrantCredentialsPermissionActivity = GrantCredentialsPermissionActivity.this;
                Runnable local1 = new android/accounts/GrantCredentialsPermissionActivity$1$1;
                local1.<init>(this, paramAnonymousAccountManagerFuture);
                localGrantCredentialsPermissionActivity.runOnUiThread(local1);
              }
            }
            catch (AuthenticatorException paramAnonymousAccountManagerFuture) {}catch (IOException paramAnonymousAccountManagerFuture) {}catch (OperationCanceledException paramAnonymousAccountManagerFuture) {}
          }
        };
        if (!"com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE".equals(mAuthTokenType)) {
          AccountManager.get(this).getAuthTokenLabel(mAccount.type, mAuthTokenType, paramBundle, null);
        }
        findViewById(16908732).setOnClickListener(this);
        findViewById(16908903).setOnClickListener(this);
        LinearLayout localLinearLayout = (LinearLayout)findViewById(16909218);
        int i = arrayOfString.length;
        for (int j = 0; j < i; j++)
        {
          paramBundle = arrayOfString[j];
          try
          {
            String str2 = localPackageManager.getApplicationLabel(localPackageManager.getApplicationInfo(paramBundle, 0)).toString();
            paramBundle = str2;
          }
          catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
          localLinearLayout.addView(newPackageView(paramBundle));
        }
        ((TextView)findViewById(16908682)).setText(mAccount.name);
        ((TextView)findViewById(16908685)).setText(str1);
        return;
      }
      catch (IllegalArgumentException paramBundle)
      {
        setResult(0);
        finish();
        return;
      }
    }
    setResult(0);
    finish();
  }
  
  public final void setAccountAuthenticatorResult(Bundle paramBundle)
  {
    mResultBundle = paramBundle;
  }
}
