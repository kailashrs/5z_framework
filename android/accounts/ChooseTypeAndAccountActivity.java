package android.accounts;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ChooseTypeAndAccountActivity
  extends Activity
  implements AccountManagerCallback<Bundle>
{
  public static final String EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING = "authTokenType";
  public static final String EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE = "addAccountOptions";
  public static final String EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY = "addAccountRequiredFeatures";
  public static final String EXTRA_ALLOWABLE_ACCOUNTS_ARRAYLIST = "allowableAccounts";
  public static final String EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY = "allowableAccountTypes";
  @Deprecated
  public static final String EXTRA_ALWAYS_PROMPT_FOR_ACCOUNT = "alwaysPromptForAccount";
  public static final String EXTRA_DESCRIPTION_TEXT_OVERRIDE = "descriptionTextOverride";
  public static final String EXTRA_SELECTED_ACCOUNT = "selectedAccount";
  private static final String KEY_INSTANCE_STATE_ACCOUNTS_LIST = "accountsList";
  private static final String KEY_INSTANCE_STATE_EXISTING_ACCOUNTS = "existingAccounts";
  private static final String KEY_INSTANCE_STATE_PENDING_REQUEST = "pendingRequest";
  private static final String KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME = "selectedAccountName";
  private static final String KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT = "selectedAddAccount";
  private static final String KEY_INSTANCE_STATE_VISIBILITY_LIST = "visibilityList";
  public static final int REQUEST_ADD_ACCOUNT = 2;
  public static final int REQUEST_CHOOSE_TYPE = 1;
  public static final int REQUEST_NULL = 0;
  private static final int SELECTED_ITEM_NONE = -1;
  private static final String TAG = "AccountChooser";
  private LinkedHashMap<Account, Integer> mAccounts;
  private String mCallingPackage;
  private int mCallingUid;
  private String mDescriptionOverride;
  private boolean mDisallowAddAccounts;
  private boolean mDontShowPicker;
  private Parcelable[] mExistingAccounts = null;
  private Button mOkButton;
  private int mPendingRequest = 0;
  private ArrayList<Account> mPossiblyVisibleAccounts;
  private String mSelectedAccountName = null;
  private boolean mSelectedAddNewAccount = false;
  private int mSelectedItemIndex;
  private Set<Account> mSetOfAllowableAccounts;
  private Set<String> mSetOfRelevantAccountTypes;
  
  public ChooseTypeAndAccountActivity() {}
  
  private LinkedHashMap<Account, Integer> getAcceptableAccountChoices(AccountManager paramAccountManager)
  {
    Map localMap = paramAccountManager.getAccountsAndVisibilityForPackage(mCallingPackage, null);
    paramAccountManager = paramAccountManager.getAccounts();
    LinkedHashMap localLinkedHashMap = new LinkedHashMap(localMap.size());
    int i = paramAccountManager.length;
    for (int j = 0; j < i; j++)
    {
      Object localObject = paramAccountManager[j];
      if (((mSetOfAllowableAccounts == null) || (mSetOfAllowableAccounts.contains(localObject))) && ((mSetOfRelevantAccountTypes == null) || (mSetOfRelevantAccountTypes.contains(type))) && (localMap.get(localObject) != null)) {
        localLinkedHashMap.put(localObject, (Integer)localMap.get(localObject));
      }
    }
    return localLinkedHashMap;
  }
  
  private Set<Account> getAllowableAccountSet(Intent paramIntent)
  {
    HashSet localHashSet = null;
    Object localObject = paramIntent.getParcelableArrayListExtra("allowableAccounts");
    paramIntent = localHashSet;
    if (localObject != null)
    {
      localHashSet = new HashSet(((ArrayList)localObject).size());
      localObject = ((ArrayList)localObject).iterator();
      for (;;)
      {
        paramIntent = localHashSet;
        if (!((Iterator)localObject).hasNext()) {
          break;
        }
        localHashSet.add((Account)((Iterator)localObject).next());
      }
    }
    return paramIntent;
  }
  
  private int getItemIndexToSelect(ArrayList<Account> paramArrayList, String paramString, boolean paramBoolean)
  {
    if (paramBoolean) {
      return paramArrayList.size();
    }
    for (int i = 0; i < paramArrayList.size(); i++) {
      if (getname.equals(paramString)) {
        return i;
      }
    }
    return -1;
  }
  
  private String[] getListOfDisplayableOptions(ArrayList<Account> paramArrayList)
  {
    String[] arrayOfString = new String[paramArrayList.size() + (mDisallowAddAccounts ^ true)];
    for (int i = 0; i < paramArrayList.size(); i++) {
      arrayOfString[i] = getname;
    }
    if (!mDisallowAddAccounts) {
      arrayOfString[paramArrayList.size()] = getResources().getString(17039447);
    }
    return arrayOfString;
  }
  
  private Set<String> getReleventAccountTypes(Intent paramIntent)
  {
    String[] arrayOfString = paramIntent.getStringArrayExtra("allowableAccountTypes");
    Object localObject = AccountManager.get(this).getAuthenticatorTypes();
    paramIntent = new HashSet(localObject.length);
    int i = localObject.length;
    for (int j = 0; j < i; j++) {
      paramIntent.add(type);
    }
    if (arrayOfString != null)
    {
      localObject = Sets.newHashSet(arrayOfString);
      ((Set)localObject).retainAll(paramIntent);
      paramIntent = (Intent)localObject;
    }
    return paramIntent;
  }
  
  private void onAccountSelected(Account paramAccount)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("selected account ");
    localStringBuilder.append(paramAccount);
    Log.d("AccountChooser", localStringBuilder.toString());
    setResultAndFinish(name, type);
  }
  
  private void overrideDescriptionIfSupplied(String paramString)
  {
    TextView localTextView = (TextView)findViewById(16908904);
    if (!TextUtils.isEmpty(paramString)) {
      localTextView.setText(paramString);
    } else {
      localTextView.setVisibility(8);
    }
  }
  
  private final void populateUIAccountList(String[] paramArrayOfString)
  {
    ListView localListView = (ListView)findViewById(16908298);
    localListView.setAdapter(new ArrayAdapter(this, 17367055, paramArrayOfString));
    localListView.setChoiceMode(1);
    localListView.setItemsCanFocus(false);
    localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        ChooseTypeAndAccountActivity.access$002(ChooseTypeAndAccountActivity.this, paramAnonymousInt);
        mOkButton.setEnabled(true);
      }
    });
    if (mSelectedItemIndex != -1)
    {
      localListView.setItemChecked(mSelectedItemIndex, true);
      if (Log.isLoggable("AccountChooser", 2))
      {
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("List item ");
        paramArrayOfString.append(mSelectedItemIndex);
        paramArrayOfString.append(" should be selected");
        Log.v("AccountChooser", paramArrayOfString.toString());
      }
    }
  }
  
  private void setNonLabelThemeAndCallSuperCreate(Bundle paramBundle)
  {
    setTheme(16974132);
    super.onCreate(paramBundle);
  }
  
  private void setResultAndFinish(String paramString1, String paramString2)
  {
    Object localObject = new Account(paramString1, paramString2);
    Integer localInteger = Integer.valueOf(AccountManager.get(this).getAccountVisibility((Account)localObject, mCallingPackage));
    if ((localInteger != null) && (localInteger.intValue() == 4)) {
      AccountManager.get(this).setAccountVisibility((Account)localObject, mCallingPackage, 2);
    }
    if ((localInteger != null) && (localInteger.intValue() == 3))
    {
      setResult(0);
      finish();
      return;
    }
    localObject = new Bundle();
    ((Bundle)localObject).putString("authAccount", paramString1);
    ((Bundle)localObject).putString("accountType", paramString2);
    setResult(-1, new Intent().putExtras((Bundle)localObject));
    if (Log.isLoggable("AccountChooser", 2))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("ChooseTypeAndAccountActivity.setResultAndFinish: selected account ");
      ((StringBuilder)localObject).append(paramString1);
      ((StringBuilder)localObject).append(", ");
      ((StringBuilder)localObject).append(paramString2);
      Log.v("AccountChooser", ((StringBuilder)localObject).toString());
    }
    finish();
  }
  
  private void startChooseAccountTypeActivity()
  {
    if (Log.isLoggable("AccountChooser", 2)) {
      Log.v("AccountChooser", "ChooseAccountTypeActivity.startChooseAccountTypeActivity()");
    }
    Intent localIntent = new Intent(this, ChooseAccountTypeActivity.class);
    localIntent.setFlags(524288);
    localIntent.putExtra("allowableAccountTypes", getIntent().getStringArrayExtra("allowableAccountTypes"));
    localIntent.putExtra("addAccountOptions", getIntent().getBundleExtra("addAccountOptions"));
    localIntent.putExtra("addAccountRequiredFeatures", getIntent().getStringArrayExtra("addAccountRequiredFeatures"));
    localIntent.putExtra("authTokenType", getIntent().getStringExtra("authTokenType"));
    startActivityForResult(localIntent, 1);
    mPendingRequest = 1;
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    Object localObject1;
    Object localObject2;
    if (Log.isLoggable("AccountChooser", 2))
    {
      if ((paramIntent != null) && (paramIntent.getExtras() != null)) {
        paramIntent.getExtras().keySet();
      }
      if (paramIntent != null) {
        localObject1 = paramIntent.getExtras();
      } else {
        localObject1 = null;
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("ChooseTypeAndAccountActivity.onActivityResult(reqCode=");
      ((StringBuilder)localObject2).append(paramInt1);
      ((StringBuilder)localObject2).append(", resCode=");
      ((StringBuilder)localObject2).append(paramInt2);
      ((StringBuilder)localObject2).append(", extras=");
      ((StringBuilder)localObject2).append(localObject1);
      ((StringBuilder)localObject2).append(")");
      Log.v("AccountChooser", ((StringBuilder)localObject2).toString());
    }
    mPendingRequest = 0;
    if (paramInt2 == 0)
    {
      if (mPossiblyVisibleAccounts.isEmpty())
      {
        setResult(0);
        finish();
      }
      return;
    }
    if (paramInt2 == -1)
    {
      if (paramInt1 == 1)
      {
        if (paramIntent != null)
        {
          paramIntent = paramIntent.getStringExtra("accountType");
          if (paramIntent != null)
          {
            runAddAccountForAuthenticator(paramIntent);
            return;
          }
        }
        Log.d("AccountChooser", "ChooseTypeAndAccountActivity.onActivityResult: unable to find account type, pretending the request was canceled");
      }
      else if (paramInt1 == 2)
      {
        localObject1 = null;
        localObject2 = null;
        if (paramIntent != null)
        {
          localObject1 = paramIntent.getStringExtra("authAccount");
          localObject2 = paramIntent.getStringExtra("accountType");
        }
        Object localObject3;
        if (localObject1 != null)
        {
          paramIntent = (Intent)localObject1;
          localObject3 = localObject2;
          if (localObject2 != null) {}
        }
        else
        {
          Account[] arrayOfAccount = AccountManager.get(this).getAccountsForPackage(mCallingPackage, mCallingUid);
          HashSet localHashSet = new HashSet();
          paramIntent = mExistingAccounts;
          paramInt2 = paramIntent.length;
          for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
            localHashSet.add((Account)paramIntent[paramInt1]);
          }
          paramInt2 = arrayOfAccount.length;
          for (paramInt1 = 0;; paramInt1++)
          {
            paramIntent = (Intent)localObject1;
            localObject3 = localObject2;
            if (paramInt1 >= paramInt2) {
              break;
            }
            localObject3 = arrayOfAccount[paramInt1];
            if (!localHashSet.contains(localObject3))
            {
              paramIntent = name;
              localObject3 = type;
              break;
            }
          }
        }
        if ((paramIntent != null) || (localObject3 != null))
        {
          setResultAndFinish(paramIntent, (String)localObject3);
          return;
        }
      }
      Log.d("AccountChooser", "ChooseTypeAndAccountActivity.onActivityResult: unable to find added account, pretending the request was canceled");
    }
    if (Log.isLoggable("AccountChooser", 2)) {
      Log.v("AccountChooser", "ChooseTypeAndAccountActivity.onActivityResult: canceled");
    }
    setResult(0);
    finish();
  }
  
  public void onCancelButtonClicked(View paramView)
  {
    onBackPressed();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    if (Log.isLoggable("AccountChooser", 2))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("ChooseTypeAndAccountActivity.onCreate(savedInstanceState=");
      ((StringBuilder)localObject1).append(paramBundle);
      ((StringBuilder)localObject1).append(")");
      Log.v("AccountChooser", ((StringBuilder)localObject1).toString());
    }
    try
    {
      localObject1 = getActivityToken();
      mCallingUid = ActivityManager.getService().getLaunchedFromUid((IBinder)localObject1);
      mCallingPackage = ActivityManager.getService().getLaunchedFromPackage((IBinder)localObject1);
      if ((mCallingUid != 0) && (mCallingPackage != null))
      {
        localObject2 = UserManager.get(this);
        localObject1 = new android/os/UserHandle;
        ((UserHandle)localObject1).<init>(UserHandle.getUserId(mCallingUid));
        mDisallowAddAccounts = ((UserManager)localObject2).getUserRestrictions((UserHandle)localObject1).getBoolean("no_modify_accounts", false);
      }
    }
    catch (RemoteException localRemoteException)
    {
      localObject2 = getClass().getSimpleName();
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Unable to get caller identity \n");
      ((StringBuilder)localObject1).append(localRemoteException);
      Log.w((String)localObject2, ((StringBuilder)localObject1).toString());
    }
    Object localObject1 = getIntent();
    mSetOfAllowableAccounts = getAllowableAccountSet((Intent)localObject1);
    mSetOfRelevantAccountTypes = getReleventAccountTypes((Intent)localObject1);
    mDescriptionOverride = ((Intent)localObject1).getStringExtra("descriptionTextOverride");
    if (paramBundle != null)
    {
      mPendingRequest = paramBundle.getInt("pendingRequest");
      mExistingAccounts = paramBundle.getParcelableArray("existingAccounts");
      mSelectedAccountName = paramBundle.getString("selectedAccountName");
      mSelectedAddNewAccount = paramBundle.getBoolean("selectedAddAccount", false);
      localObject1 = paramBundle.getParcelableArray("accountsList");
      localObject2 = paramBundle.getIntegerArrayList("visibilityList");
      mAccounts = new LinkedHashMap();
      for (int i = 0; i < localObject1.length; i++) {
        mAccounts.put((Account)localObject1[i], (Integer)((ArrayList)localObject2).get(i));
      }
    }
    else
    {
      mPendingRequest = 0;
      mExistingAccounts = null;
      localObject1 = (Account)((Intent)localObject1).getParcelableExtra("selectedAccount");
      if (localObject1 != null) {
        mSelectedAccountName = name;
      }
      mAccounts = getAcceptableAccountChoices(AccountManager.get(this));
    }
    if (Log.isLoggable("AccountChooser", 2))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("selected account name is ");
      ((StringBuilder)localObject1).append(mSelectedAccountName);
      Log.v("AccountChooser", ((StringBuilder)localObject1).toString());
    }
    mPossiblyVisibleAccounts = new ArrayList(mAccounts.size());
    Object localObject2 = mAccounts.entrySet().iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject1 = (Map.Entry)((Iterator)localObject2).next();
      if (3 != ((Integer)((Map.Entry)localObject1).getValue()).intValue()) {
        mPossiblyVisibleAccounts.add((Account)((Map.Entry)localObject1).getKey());
      }
    }
    boolean bool1 = mPossiblyVisibleAccounts.isEmpty();
    boolean bool2 = true;
    if ((bool1) && (mDisallowAddAccounts))
    {
      requestWindowFeature(1);
      setContentView(17367094);
      mDontShowPicker = true;
    }
    if (mDontShowPicker)
    {
      super.onCreate(paramBundle);
      return;
    }
    if ((mPendingRequest == 0) && (mPossiblyVisibleAccounts.isEmpty()))
    {
      setNonLabelThemeAndCallSuperCreate(paramBundle);
      if (mSetOfRelevantAccountTypes.size() == 1) {
        runAddAccountForAuthenticator((String)mSetOfRelevantAccountTypes.iterator().next());
      } else {
        startChooseAccountTypeActivity();
      }
    }
    localObject1 = getListOfDisplayableOptions(mPossiblyVisibleAccounts);
    mSelectedItemIndex = getItemIndexToSelect(mPossiblyVisibleAccounts, mSelectedAccountName, mSelectedAddNewAccount);
    super.onCreate(paramBundle);
    setContentView(17367145);
    overrideDescriptionIfSupplied(mDescriptionOverride);
    populateUIAccountList((String[])localObject1);
    mOkButton = ((Button)findViewById(16908314));
    paramBundle = mOkButton;
    if (mSelectedItemIndex == -1) {
      bool2 = false;
    }
    paramBundle.setEnabled(bool2);
  }
  
  protected void onDestroy()
  {
    if (Log.isLoggable("AccountChooser", 2)) {
      Log.v("AccountChooser", "ChooseTypeAndAccountActivity.onDestroy()");
    }
    super.onDestroy();
  }
  
  public void onOkButtonClicked(View paramView)
  {
    if (mSelectedItemIndex == mPossiblyVisibleAccounts.size()) {
      startChooseAccountTypeActivity();
    } else if (mSelectedItemIndex != -1) {
      onAccountSelected((Account)mPossiblyVisibleAccounts.get(mSelectedItemIndex));
    }
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("pendingRequest", mPendingRequest);
    if (mPendingRequest == 2) {
      paramBundle.putParcelableArray("existingAccounts", mExistingAccounts);
    }
    if (mSelectedItemIndex != -1) {
      if (mSelectedItemIndex == mPossiblyVisibleAccounts.size())
      {
        paramBundle.putBoolean("selectedAddAccount", true);
      }
      else
      {
        paramBundle.putBoolean("selectedAddAccount", false);
        paramBundle.putString("selectedAccountName", mPossiblyVisibleAccounts.get(mSelectedItemIndex)).name);
      }
    }
    Parcelable[] arrayOfParcelable = new Parcelable[mAccounts.size()];
    ArrayList localArrayList = new ArrayList(mAccounts.size());
    int i = 0;
    Iterator localIterator = mAccounts.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      arrayOfParcelable[i] = ((Parcelable)localEntry.getKey());
      localArrayList.add((Integer)localEntry.getValue());
      i++;
    }
    paramBundle.putParcelableArray("accountsList", arrayOfParcelable);
    paramBundle.putIntegerArrayList("visibilityList", localArrayList);
  }
  
  public void run(AccountManagerFuture<Bundle> paramAccountManagerFuture)
  {
    try
    {
      try
      {
        paramAccountManagerFuture = (Intent)((Bundle)paramAccountManagerFuture.getResult()).getParcelable("intent");
        if (paramAccountManagerFuture != null)
        {
          mPendingRequest = 2;
          mExistingAccounts = AccountManager.get(this).getAccountsForPackage(mCallingPackage, mCallingUid);
          paramAccountManagerFuture.setFlags(paramAccountManagerFuture.getFlags() & 0xEFFFFFFF);
          startActivityForResult(paramAccountManagerFuture, 2);
          return;
        }
      }
      catch (AuthenticatorException paramAccountManagerFuture) {}catch (IOException paramAccountManagerFuture) {}
      paramAccountManagerFuture = new Bundle();
      paramAccountManagerFuture.putString("errorMessage", "error communicating with server");
      setResult(-1, new Intent().putExtras(paramAccountManagerFuture));
      finish();
      return;
    }
    catch (OperationCanceledException paramAccountManagerFuture)
    {
      setResult(0);
      finish();
    }
  }
  
  protected void runAddAccountForAuthenticator(String paramString)
  {
    if (Log.isLoggable("AccountChooser", 2))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("runAddAccountForAuthenticator: ");
      ((StringBuilder)localObject).append(paramString);
      Log.v("AccountChooser", ((StringBuilder)localObject).toString());
    }
    Bundle localBundle = getIntent().getBundleExtra("addAccountOptions");
    Object localObject = getIntent().getStringArrayExtra("addAccountRequiredFeatures");
    String str = getIntent().getStringExtra("authTokenType");
    AccountManager.get(this).addAccount(paramString, str, (String[])localObject, localBundle, null, this, null);
  }
}
