package android.accounts;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class CantAddAccountActivity
  extends Activity
{
  public static final String EXTRA_ERROR_CODE = "android.accounts.extra.ERROR_CODE";
  
  public CantAddAccountActivity() {}
  
  public void onCancelButtonClicked(View paramView)
  {
    onBackPressed();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(17367094);
  }
}
