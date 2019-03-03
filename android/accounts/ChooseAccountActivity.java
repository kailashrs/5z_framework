package android.accounts;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.HashMap;

public class ChooseAccountActivity
  extends Activity
{
  private static final String TAG = "AccountManager";
  private AccountManagerResponse mAccountManagerResponse = null;
  private Parcelable[] mAccounts = null;
  private String mCallingPackage;
  private int mCallingUid;
  private Bundle mResult;
  private HashMap<String, AuthenticatorDescription> mTypeToAuthDescription = new HashMap();
  
  public ChooseAccountActivity() {}
  
  private void getAuthDescriptions()
  {
    for (AuthenticatorDescription localAuthenticatorDescription : AccountManager.get(this).getAuthenticatorTypes()) {
      mTypeToAuthDescription.put(type, localAuthenticatorDescription);
    }
  }
  
  private Drawable getDrawableForType(String paramString)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = localObject1;
    Object localObject5;
    if (mTypeToAuthDescription.containsKey(paramString)) {
      try
      {
        localObject3 = (AuthenticatorDescription)mTypeToAuthDescription.get(paramString);
        localObject3 = createPackageContext(packageName, 0).getDrawable(iconId);
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        Object localObject4 = localObject1;
        if (Log.isLoggable("AccountManager", 5))
        {
          localObject4 = new StringBuilder();
          ((StringBuilder)localObject4).append("No icon resource for account type ");
          ((StringBuilder)localObject4).append(paramString);
          Log.w("AccountManager", ((StringBuilder)localObject4).toString());
          localObject4 = localObject1;
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        for (;;)
        {
          localObject5 = localObject2;
          if (Log.isLoggable("AccountManager", 5))
          {
            localObject5 = new StringBuilder();
            ((StringBuilder)localObject5).append("No icon name for account type ");
            ((StringBuilder)localObject5).append(paramString);
            Log.w("AccountManager", ((StringBuilder)localObject5).toString());
            localObject5 = localObject2;
          }
        }
      }
    }
    return localObject5;
  }
  
  public void finish()
  {
    if (mAccountManagerResponse != null) {
      if (mResult != null) {
        mAccountManagerResponse.onResult(mResult);
      } else {
        mAccountManagerResponse.onError(4, "canceled");
      }
    }
    super.finish();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    mAccounts = getIntent().getParcelableArrayExtra("accounts");
    mAccountManagerResponse = ((AccountManagerResponse)getIntent().getParcelableExtra("accountManagerResponse"));
    paramBundle = mAccounts;
    int i = 0;
    if (paramBundle == null)
    {
      setResult(0);
      finish();
      return;
    }
    try
    {
      paramBundle = getActivityToken();
      mCallingUid = ActivityManager.getService().getLaunchedFromUid(paramBundle);
      mCallingPackage = ActivityManager.getService().getLaunchedFromPackage(paramBundle);
    }
    catch (RemoteException localRemoteException)
    {
      String str = getClass().getSimpleName();
      paramBundle = new StringBuilder();
      paramBundle.append("Unable to get caller identity \n");
      paramBundle.append(localRemoteException);
      Log.w(str, paramBundle.toString());
    }
    if ((UserHandle.isSameApp(mCallingUid, 1000)) && (getIntent().getStringExtra("androidPackageName") != null)) {
      mCallingPackage = getIntent().getStringExtra("androidPackageName");
    }
    if ((!UserHandle.isSameApp(mCallingUid, 1000)) && (getIntent().getStringExtra("androidPackageName") != null))
    {
      paramBundle = getClass().getSimpleName();
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Non-system Uid: ");
      ((StringBuilder)localObject).append(mCallingUid);
      ((StringBuilder)localObject).append(" tried to override packageName \n");
      Log.w(paramBundle, ((StringBuilder)localObject).toString());
    }
    getAuthDescriptions();
    Object localObject = new AccountInfo[mAccounts.length];
    while (i < mAccounts.length)
    {
      localObject[i] = new AccountInfo(mAccounts[i]).name, getDrawableForType(mAccounts[i]).type));
      i++;
    }
    setContentView(17367142);
    paramBundle = (ListView)findViewById(16908298);
    paramBundle.setAdapter(new AccountArrayAdapter(this, 17367043, (AccountInfo[])localObject));
    paramBundle.setChoiceMode(1);
    paramBundle.setTextFilterEnabled(true);
    paramBundle.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        onListItemClick((ListView)paramAnonymousAdapterView, paramAnonymousView, paramAnonymousInt, paramAnonymousLong);
      }
    });
  }
  
  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    paramListView = (Account)mAccounts[paramInt];
    paramView = AccountManager.get(this);
    Integer localInteger = Integer.valueOf(paramView.getAccountVisibility(paramListView, mCallingPackage));
    if ((localInteger != null) && (localInteger.intValue() == 4)) {
      paramView.setAccountVisibility(paramListView, mCallingPackage, 2);
    }
    paramView = new StringBuilder();
    paramView.append("selected account ");
    paramView.append(paramListView);
    Log.d("AccountManager", paramView.toString());
    paramView = new Bundle();
    paramView.putString("authAccount", name);
    paramView.putString("accountType", type);
    mResult = paramView;
    finish();
  }
  
  private static class AccountArrayAdapter
    extends ArrayAdapter<ChooseAccountActivity.AccountInfo>
  {
    private ChooseAccountActivity.AccountInfo[] mInfos;
    private LayoutInflater mLayoutInflater;
    
    public AccountArrayAdapter(Context paramContext, int paramInt, ChooseAccountActivity.AccountInfo[] paramArrayOfAccountInfo)
    {
      super(paramInt, paramArrayOfAccountInfo);
      mInfos = paramArrayOfAccountInfo;
      mLayoutInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        paramViewGroup = mLayoutInflater.inflate(17367143, null);
        paramView = new ChooseAccountActivity.ViewHolder(null);
        text = ((TextView)paramViewGroup.findViewById(16908684));
        icon = ((ImageView)paramViewGroup.findViewById(16908683));
        paramViewGroup.setTag(paramView);
      }
      else
      {
        ChooseAccountActivity.ViewHolder localViewHolder = (ChooseAccountActivity.ViewHolder)paramView.getTag();
        paramViewGroup = paramView;
        paramView = localViewHolder;
      }
      text.setText(mInfos[paramInt].name);
      icon.setImageDrawable(mInfos[paramInt].drawable);
      return paramViewGroup;
    }
  }
  
  private static class AccountInfo
  {
    final Drawable drawable;
    final String name;
    
    AccountInfo(String paramString, Drawable paramDrawable)
    {
      name = paramString;
      drawable = paramDrawable;
    }
  }
  
  private static class ViewHolder
  {
    ImageView icon;
    TextView text;
    
    private ViewHolder() {}
  }
}
