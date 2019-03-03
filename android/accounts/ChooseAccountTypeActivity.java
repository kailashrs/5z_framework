package android.accounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class ChooseAccountTypeActivity
  extends Activity
{
  private static final String TAG = "AccountChooser";
  private ArrayList<AuthInfo> mAuthenticatorInfosToDisplay;
  private HashMap<String, AuthInfo> mTypeToAuthenticatorInfo = new HashMap();
  
  public ChooseAccountTypeActivity() {}
  
  private void buildTypeToAuthDescriptionMap()
  {
    label334:
    for (AuthenticatorDescription localAuthenticatorDescription : AccountManager.get(this).getAuthenticatorTypes())
    {
      String str = null;
      Object localObject1 = null;
      Object localObject2 = null;
      CharSequence localCharSequence = null;
      Object localObject3 = null;
      Object localObject4 = str;
      Object localObject5 = localObject3;
      Object localObject7 = localObject1;
      Object localObject8 = localCharSequence;
      Object localObject6;
      try
      {
        Context localContext = createPackageContext(packageName, 0);
        localObject4 = str;
        localObject5 = localObject3;
        localObject7 = localObject1;
        localObject8 = localCharSequence;
        localObject3 = localContext.getDrawable(iconId);
        localObject4 = str;
        localObject5 = localObject3;
        localObject7 = localObject1;
        localObject8 = localObject3;
        localCharSequence = localContext.getResources().getText(labelId);
        localObject8 = localObject2;
        if (localCharSequence != null)
        {
          localObject4 = str;
          localObject5 = localObject3;
          localObject7 = localObject1;
          localObject8 = localObject3;
          str = localCharSequence.toString();
          localObject8 = str;
        }
        localObject4 = localObject8;
        localObject5 = localObject3;
        localObject7 = localObject8;
        localObject8 = localObject3;
        str = localCharSequence.toString();
        localObject5 = str;
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        localObject9 = localObject4;
        localObject3 = localObject5;
        if (!Log.isLoggable("AccountChooser", 5)) {
          break label334;
        }
        localObject9 = new StringBuilder();
        ((StringBuilder)localObject9).append("No icon resource for account type ");
        ((StringBuilder)localObject9).append(type);
        Log.w("AccountChooser", ((StringBuilder)localObject9).toString());
        localObject9 = localObject4;
        localObject3 = localObject5;
        break label334;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        localObject6 = localObject7;
        localObject3 = localObject9;
        if (Log.isLoggable("AccountChooser", 5))
        {
          localObject6 = new StringBuilder();
          ((StringBuilder)localObject6).append("No icon name for account type ");
          ((StringBuilder)localObject6).append(type);
          Log.w("AccountChooser", ((StringBuilder)localObject6).toString());
          localObject3 = localObject9;
          localObject6 = localObject7;
        }
      }
      Object localObject9 = localObject6;
      localObject9 = new AuthInfo(localAuthenticatorDescription, (String)localObject9, (Drawable)localObject3);
      mTypeToAuthenticatorInfo.put(type, localObject9);
    }
  }
  
  private void setResultAndFinish(String paramString)
  {
    Object localObject = new Bundle();
    ((Bundle)localObject).putString("accountType", paramString);
    setResult(-1, new Intent().putExtras((Bundle)localObject));
    if (Log.isLoggable("AccountChooser", 2))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("ChooseAccountTypeActivity.setResultAndFinish: selected account type ");
      ((StringBuilder)localObject).append(paramString);
      Log.v("AccountChooser", ((StringBuilder)localObject).toString());
    }
    finish();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Object localObject1;
    if (Log.isLoggable("AccountChooser", 2))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("ChooseAccountTypeActivity.onCreate(savedInstanceState=");
      ((StringBuilder)localObject1).append(paramBundle);
      ((StringBuilder)localObject1).append(")");
      Log.v("AccountChooser", ((StringBuilder)localObject1).toString());
    }
    paramBundle = null;
    Object localObject2 = getIntent().getStringArrayExtra("allowableAccountTypes");
    if (localObject2 != null)
    {
      localObject1 = new HashSet(localObject2.length);
      int i = localObject2.length;
      for (int j = 0;; j++)
      {
        paramBundle = (Bundle)localObject1;
        if (j >= i) {
          break;
        }
        ((Set)localObject1).add(localObject2[j]);
      }
    }
    buildTypeToAuthDescriptionMap();
    mAuthenticatorInfosToDisplay = new ArrayList(mTypeToAuthenticatorInfo.size());
    localObject2 = mTypeToAuthenticatorInfo.entrySet().iterator();
    while (((Iterator)localObject2).hasNext())
    {
      Object localObject3 = (Map.Entry)((Iterator)localObject2).next();
      localObject1 = (String)((Map.Entry)localObject3).getKey();
      localObject3 = (AuthInfo)((Map.Entry)localObject3).getValue();
      if ((paramBundle == null) || (paramBundle.contains(localObject1))) {
        mAuthenticatorInfosToDisplay.add(localObject3);
      }
    }
    if (mAuthenticatorInfosToDisplay.isEmpty())
    {
      paramBundle = new Bundle();
      paramBundle.putString("errorMessage", "no allowable account types");
      setResult(-1, new Intent().putExtras(paramBundle));
      finish();
      return;
    }
    if (mAuthenticatorInfosToDisplay.size() == 1)
    {
      setResultAndFinish(mAuthenticatorInfosToDisplay.get(0)).desc.type);
      return;
    }
    setContentView(17367144);
    paramBundle = (ListView)findViewById(16908298);
    paramBundle.setAdapter(new AccountArrayAdapter(this, 17367043, mAuthenticatorInfosToDisplay));
    paramBundle.setChoiceMode(0);
    paramBundle.setTextFilterEnabled(false);
    paramBundle.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        ChooseAccountTypeActivity.this.setResultAndFinish(mAuthenticatorInfosToDisplay.get(paramAnonymousInt)).desc.type);
      }
    });
  }
  
  private static class AccountArrayAdapter
    extends ArrayAdapter<ChooseAccountTypeActivity.AuthInfo>
  {
    private ArrayList<ChooseAccountTypeActivity.AuthInfo> mInfos;
    private LayoutInflater mLayoutInflater;
    
    public AccountArrayAdapter(Context paramContext, int paramInt, ArrayList<ChooseAccountTypeActivity.AuthInfo> paramArrayList)
    {
      super(paramInt, paramArrayList);
      mInfos = paramArrayList;
      mLayoutInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        paramViewGroup = mLayoutInflater.inflate(17367143, null);
        paramView = new ChooseAccountTypeActivity.ViewHolder(null);
        text = ((TextView)paramViewGroup.findViewById(16908684));
        icon = ((ImageView)paramViewGroup.findViewById(16908683));
        paramViewGroup.setTag(paramView);
      }
      else
      {
        ChooseAccountTypeActivity.ViewHolder localViewHolder = (ChooseAccountTypeActivity.ViewHolder)paramView.getTag();
        paramViewGroup = paramView;
        paramView = localViewHolder;
      }
      text.setText(mInfos.get(paramInt)).name);
      icon.setImageDrawable(mInfos.get(paramInt)).drawable);
      return paramViewGroup;
    }
  }
  
  private static class AuthInfo
  {
    final AuthenticatorDescription desc;
    final Drawable drawable;
    final String name;
    
    AuthInfo(AuthenticatorDescription paramAuthenticatorDescription, String paramString, Drawable paramDrawable)
    {
      desc = paramAuthenticatorDescription;
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
