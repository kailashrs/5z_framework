package com.android.internal.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.UserManager;
import android.util.Log;

public class ConfirmUserCreationActivity
  extends AlertActivity
  implements DialogInterface.OnClickListener
{
  private static final String TAG = "CreateUser";
  private String mAccountName;
  private PersistableBundle mAccountOptions;
  private String mAccountType;
  private boolean mCanProceed;
  private UserManager mUserManager;
  private String mUserName;
  
  public ConfirmUserCreationActivity() {}
  
  private String checkUserCreationRequirements()
  {
    Object localObject = getCallingPackage();
    if (localObject != null) {
      try
      {
        ApplicationInfo localApplicationInfo = getPackageManager().getApplicationInfo((String)localObject, 0);
        int i;
        if ((!mUserManager.hasUserRestriction("no_add_user")) && (mUserManager.isAdminUser())) {
          i = 0;
        } else {
          i = 1;
        }
        boolean bool = mUserManager.canAddMoreUsers();
        localObject = new Account(mAccountName, mAccountType);
        int j;
        if ((mAccountName != null) && (mAccountType != null) && ((AccountManager.get(this).someUserHasAccount((Account)localObject) | mUserManager.someUserHasSeedAccount(mAccountName, mAccountType)))) {
          j = 1;
        } else {
          j = 0;
        }
        mCanProceed = true;
        localObject = localApplicationInfo.loadLabel(getPackageManager()).toString();
        if (i != 0)
        {
          setResult(1);
          return null;
        }
        if ((bool ^ true))
        {
          setResult(2);
          return null;
        }
        if (j != 0) {
          localObject = getString(17041157, new Object[] { localObject, mAccountName });
        } else {
          localObject = getString(17041158, new Object[] { localObject, mAccountName });
        }
        return localObject;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        throw new SecurityException("Cannot find the calling package");
      }
    }
    throw new SecurityException("User Creation intent must be launched with startActivityForResult");
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    setResult(0);
    if ((paramInt == -1) && (mCanProceed))
    {
      Log.i("CreateUser", "Ok, creating user");
      paramDialogInterface = mUserManager.createUser(mUserName, 0);
      if (paramDialogInterface == null)
      {
        Log.e("CreateUser", "Couldn't create user");
        finish();
        return;
      }
      mUserManager.setSeedAccountData(id, mAccountName, mAccountType, mAccountOptions);
      setResult(-1);
    }
    finish();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent();
    mUserName = paramBundle.getStringExtra("android.os.extra.USER_NAME");
    mAccountName = paramBundle.getStringExtra("android.os.extra.USER_ACCOUNT_NAME");
    mAccountType = paramBundle.getStringExtra("android.os.extra.USER_ACCOUNT_TYPE");
    mAccountOptions = ((PersistableBundle)paramBundle.getParcelableExtra("android.os.extra.USER_ACCOUNT_OPTIONS"));
    mUserManager = ((UserManager)getSystemService(UserManager.class));
    paramBundle = checkUserCreationRequirements();
    if (paramBundle == null)
    {
      finish();
      return;
    }
    AlertController.AlertParams localAlertParams = mAlertParams;
    mMessage = paramBundle;
    mPositiveButtonText = getString(17039370);
    mPositiveButtonListener = this;
    if (mCanProceed)
    {
      mNegativeButtonText = getString(17039360);
      mNegativeButtonListener = this;
    }
    setupAlert();
  }
}
