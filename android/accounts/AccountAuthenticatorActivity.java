package android.accounts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AccountAuthenticatorActivity
  extends Activity
{
  private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
  private Bundle mResultBundle = null;
  
  public AccountAuthenticatorActivity() {}
  
  public void finish()
  {
    if (mAccountAuthenticatorResponse != null)
    {
      if (mResultBundle != null) {
        mAccountAuthenticatorResponse.onResult(mResultBundle);
      } else {
        mAccountAuthenticatorResponse.onError(4, "canceled");
      }
      mAccountAuthenticatorResponse = null;
    }
    super.finish();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    mAccountAuthenticatorResponse = ((AccountAuthenticatorResponse)getIntent().getParcelableExtra("accountAuthenticatorResponse"));
    if (mAccountAuthenticatorResponse != null) {
      mAccountAuthenticatorResponse.onRequestContinued();
    }
  }
  
  public final void setAccountAuthenticatorResult(Bundle paramBundle)
  {
    mResultBundle = paramBundle;
  }
}
