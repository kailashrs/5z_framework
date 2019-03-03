package android.preference;

import android.annotation.SystemApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class PreferenceManager
{
  public static final String KEY_HAS_SET_DEFAULT_VALUES = "_has_set_default_values";
  public static final String METADATA_KEY_PREFERENCES = "android.preference";
  private static final int STORAGE_CREDENTIAL_PROTECTED = 2;
  private static final int STORAGE_DEFAULT = 0;
  private static final int STORAGE_DEVICE_PROTECTED = 1;
  private static final String TAG = "PreferenceManager";
  private Activity mActivity;
  private List<OnActivityDestroyListener> mActivityDestroyListeners;
  private List<OnActivityResultListener> mActivityResultListeners;
  private List<OnActivityStopListener> mActivityStopListeners;
  private Context mContext;
  private SharedPreferences.Editor mEditor;
  private PreferenceFragment mFragment;
  private long mNextId = 0L;
  private int mNextRequestCode;
  private boolean mNoCommit;
  private OnPreferenceTreeClickListener mOnPreferenceTreeClickListener;
  private PreferenceDataStore mPreferenceDataStore;
  private PreferenceScreen mPreferenceScreen;
  private List<DialogInterface> mPreferencesScreens;
  private SharedPreferences mSharedPreferences;
  private int mSharedPreferencesMode;
  private String mSharedPreferencesName;
  private int mStorage = 0;
  
  public PreferenceManager(Activity paramActivity, int paramInt)
  {
    mActivity = paramActivity;
    mNextRequestCode = paramInt;
    init(paramActivity);
  }
  
  PreferenceManager(Context paramContext)
  {
    init(paramContext);
  }
  
  private void dismissAllScreens()
  {
    try
    {
      if (mPreferencesScreens == null) {
        return;
      }
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>(mPreferencesScreens);
      mPreferencesScreens.clear();
      for (int i = localArrayList.size() - 1; i >= 0; i--) {
        ((DialogInterface)localArrayList.get(i)).dismiss();
      }
      return;
    }
    finally {}
  }
  
  public static SharedPreferences getDefaultSharedPreferences(Context paramContext)
  {
    return paramContext.getSharedPreferences(getDefaultSharedPreferencesName(paramContext), getDefaultSharedPreferencesMode());
  }
  
  private static int getDefaultSharedPreferencesMode()
  {
    return 0;
  }
  
  public static String getDefaultSharedPreferencesName(Context paramContext)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramContext.getPackageName());
    localStringBuilder.append("_preferences");
    return localStringBuilder.toString();
  }
  
  private void init(Context paramContext)
  {
    mContext = paramContext;
    setSharedPreferencesName(getDefaultSharedPreferencesName(paramContext));
  }
  
  private List<ResolveInfo> queryIntentActivities(Intent paramIntent)
  {
    return mContext.getPackageManager().queryIntentActivities(paramIntent, 128);
  }
  
  public static void setDefaultValues(Context paramContext, int paramInt, boolean paramBoolean)
  {
    setDefaultValues(paramContext, getDefaultSharedPreferencesName(paramContext), getDefaultSharedPreferencesMode(), paramInt, paramBoolean);
  }
  
  public static void setDefaultValues(Context paramContext, String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    SharedPreferences localSharedPreferences = paramContext.getSharedPreferences("_has_set_default_values", 0);
    if ((paramBoolean) || (!localSharedPreferences.getBoolean("_has_set_default_values", false)))
    {
      PreferenceManager localPreferenceManager = new PreferenceManager(paramContext);
      localPreferenceManager.setSharedPreferencesName(paramString);
      localPreferenceManager.setSharedPreferencesMode(paramInt1);
      localPreferenceManager.inflateFromResource(paramContext, paramInt2, null);
      paramString = localSharedPreferences.edit().putBoolean("_has_set_default_values", true);
      try
      {
        paramString.apply();
      }
      catch (AbstractMethodError paramContext)
      {
        paramString.commit();
      }
    }
  }
  
  private void setNoCommit(boolean paramBoolean)
  {
    if ((!paramBoolean) && (mEditor != null)) {
      try
      {
        mEditor.apply();
      }
      catch (AbstractMethodError localAbstractMethodError)
      {
        mEditor.commit();
      }
    }
    mNoCommit = paramBoolean;
  }
  
  void addPreferencesScreen(DialogInterface paramDialogInterface)
  {
    try
    {
      if (mPreferencesScreens == null)
      {
        ArrayList localArrayList = new java/util/ArrayList;
        localArrayList.<init>();
        mPreferencesScreens = localArrayList;
      }
      mPreferencesScreens.add(paramDialogInterface);
      return;
    }
    finally {}
  }
  
  public PreferenceScreen createPreferenceScreen(Context paramContext)
  {
    paramContext = new PreferenceScreen(paramContext, null);
    paramContext.onAttachedToHierarchy(this);
    return paramContext;
  }
  
  void dispatchActivityDestroy()
  {
    ArrayList localArrayList = null;
    try
    {
      if (mActivityDestroyListeners != null)
      {
        localArrayList = new java/util/ArrayList;
        localArrayList.<init>(mActivityDestroyListeners);
      }
      if (localArrayList != null)
      {
        int i = localArrayList.size();
        for (int j = 0; j < i; j++) {
          ((OnActivityDestroyListener)localArrayList.get(j)).onActivityDestroy();
        }
      }
      dismissAllScreens();
      return;
    }
    finally {}
  }
  
  void dispatchActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    try
    {
      if (mActivityResultListeners == null) {
        return;
      }
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>(mActivityResultListeners);
      int i = localArrayList.size();
      for (int j = 0; (j < i) && (!((OnActivityResultListener)localArrayList.get(j)).onActivityResult(paramInt1, paramInt2, paramIntent)); j++) {}
      return;
    }
    finally {}
  }
  
  void dispatchActivityStop()
  {
    try
    {
      if (mActivityStopListeners == null) {
        return;
      }
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>(mActivityStopListeners);
      int i = localArrayList.size();
      for (int j = 0; j < i; j++) {
        ((OnActivityStopListener)localArrayList.get(j)).onActivityStop();
      }
      return;
    }
    finally {}
  }
  
  void dispatchNewIntent(Intent paramIntent)
  {
    dismissAllScreens();
  }
  
  public Preference findPreference(CharSequence paramCharSequence)
  {
    if (mPreferenceScreen == null) {
      return null;
    }
    return mPreferenceScreen.findPreference(paramCharSequence);
  }
  
  Activity getActivity()
  {
    return mActivity;
  }
  
  Context getContext()
  {
    return mContext;
  }
  
  SharedPreferences.Editor getEditor()
  {
    if (mPreferenceDataStore != null) {
      return null;
    }
    if (mNoCommit)
    {
      if (mEditor == null) {
        mEditor = getSharedPreferences().edit();
      }
      return mEditor;
    }
    return getSharedPreferences().edit();
  }
  
  PreferenceFragment getFragment()
  {
    return mFragment;
  }
  
  long getNextId()
  {
    try
    {
      long l = mNextId;
      mNextId = (1L + l);
      return l;
    }
    finally {}
  }
  
  int getNextRequestCode()
  {
    try
    {
      int i = mNextRequestCode;
      mNextRequestCode = (i + 1);
      return i;
    }
    finally {}
  }
  
  OnPreferenceTreeClickListener getOnPreferenceTreeClickListener()
  {
    return mOnPreferenceTreeClickListener;
  }
  
  public PreferenceDataStore getPreferenceDataStore()
  {
    return mPreferenceDataStore;
  }
  
  PreferenceScreen getPreferenceScreen()
  {
    return mPreferenceScreen;
  }
  
  public SharedPreferences getSharedPreferences()
  {
    if (mPreferenceDataStore != null) {
      return null;
    }
    if (mSharedPreferences == null)
    {
      Context localContext;
      switch (mStorage)
      {
      default: 
        localContext = mContext;
        break;
      case 2: 
        localContext = mContext.createCredentialProtectedStorageContext();
        break;
      case 1: 
        localContext = mContext.createDeviceProtectedStorageContext();
      }
      mSharedPreferences = localContext.getSharedPreferences(mSharedPreferencesName, mSharedPreferencesMode);
    }
    return mSharedPreferences;
  }
  
  public int getSharedPreferencesMode()
  {
    return mSharedPreferencesMode;
  }
  
  public String getSharedPreferencesName()
  {
    return mSharedPreferencesName;
  }
  
  PreferenceScreen inflateFromIntent(Intent paramIntent, PreferenceScreen paramPreferenceScreen)
  {
    List localList = queryIntentActivities(paramIntent);
    HashSet localHashSet = new HashSet();
    int i = localList.size() - 1;
    while (i >= 0)
    {
      Object localObject1 = getactivityInfo;
      Object localObject2 = metaData;
      paramIntent = paramPreferenceScreen;
      if (localObject2 != null) {
        if (!((Bundle)localObject2).containsKey("android.preference"))
        {
          paramIntent = paramPreferenceScreen;
        }
        else
        {
          paramIntent = new StringBuilder();
          paramIntent.append(packageName);
          paramIntent.append(":");
          paramIntent.append(metaData.getInt("android.preference"));
          localObject2 = paramIntent.toString();
          paramIntent = paramPreferenceScreen;
          if (!localHashSet.contains(localObject2))
          {
            localHashSet.add(localObject2);
            try
            {
              localObject2 = mContext.createPackageContext(packageName, 0);
              paramIntent = new PreferenceInflater((Context)localObject2, this);
              localObject1 = ((ActivityInfo)localObject1).loadXmlMetaData(((Context)localObject2).getPackageManager(), "android.preference");
              paramIntent = (PreferenceScreen)paramIntent.inflate((XmlPullParser)localObject1, paramPreferenceScreen, true);
              ((XmlResourceParser)localObject1).close();
            }
            catch (PackageManager.NameNotFoundException localNameNotFoundException)
            {
              paramIntent = new StringBuilder();
              paramIntent.append("Could not create context for ");
              paramIntent.append(packageName);
              paramIntent.append(": ");
              paramIntent.append(Log.getStackTraceString(localNameNotFoundException));
              Log.w("PreferenceManager", paramIntent.toString());
              paramIntent = paramPreferenceScreen;
            }
          }
        }
      }
      i--;
      paramPreferenceScreen = paramIntent;
    }
    paramPreferenceScreen.onAttachedToHierarchy(this);
    return paramPreferenceScreen;
  }
  
  public PreferenceScreen inflateFromResource(Context paramContext, int paramInt, PreferenceScreen paramPreferenceScreen)
  {
    setNoCommit(true);
    paramContext = (PreferenceScreen)new PreferenceInflater(paramContext, this).inflate(paramInt, paramPreferenceScreen, true);
    paramContext.onAttachedToHierarchy(this);
    setNoCommit(false);
    return paramContext;
  }
  
  @SystemApi
  public boolean isStorageCredentialProtected()
  {
    boolean bool;
    if (mStorage == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isStorageDefault()
  {
    boolean bool;
    if (mStorage == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isStorageDeviceProtected()
  {
    int i = mStorage;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  void registerOnActivityDestroyListener(OnActivityDestroyListener paramOnActivityDestroyListener)
  {
    try
    {
      if (mActivityDestroyListeners == null)
      {
        ArrayList localArrayList = new java/util/ArrayList;
        localArrayList.<init>();
        mActivityDestroyListeners = localArrayList;
      }
      if (!mActivityDestroyListeners.contains(paramOnActivityDestroyListener)) {
        mActivityDestroyListeners.add(paramOnActivityDestroyListener);
      }
      return;
    }
    finally {}
  }
  
  void registerOnActivityResultListener(OnActivityResultListener paramOnActivityResultListener)
  {
    try
    {
      if (mActivityResultListeners == null)
      {
        ArrayList localArrayList = new java/util/ArrayList;
        localArrayList.<init>();
        mActivityResultListeners = localArrayList;
      }
      if (!mActivityResultListeners.contains(paramOnActivityResultListener)) {
        mActivityResultListeners.add(paramOnActivityResultListener);
      }
      return;
    }
    finally {}
  }
  
  public void registerOnActivityStopListener(OnActivityStopListener paramOnActivityStopListener)
  {
    try
    {
      if (mActivityStopListeners == null)
      {
        ArrayList localArrayList = new java/util/ArrayList;
        localArrayList.<init>();
        mActivityStopListeners = localArrayList;
      }
      if (!mActivityStopListeners.contains(paramOnActivityStopListener)) {
        mActivityStopListeners.add(paramOnActivityStopListener);
      }
      return;
    }
    finally {}
  }
  
  void removePreferencesScreen(DialogInterface paramDialogInterface)
  {
    try
    {
      if (mPreferencesScreens == null) {
        return;
      }
      mPreferencesScreens.remove(paramDialogInterface);
      return;
    }
    finally {}
  }
  
  void setFragment(PreferenceFragment paramPreferenceFragment)
  {
    mFragment = paramPreferenceFragment;
  }
  
  void setOnPreferenceTreeClickListener(OnPreferenceTreeClickListener paramOnPreferenceTreeClickListener)
  {
    mOnPreferenceTreeClickListener = paramOnPreferenceTreeClickListener;
  }
  
  public void setPreferenceDataStore(PreferenceDataStore paramPreferenceDataStore)
  {
    mPreferenceDataStore = paramPreferenceDataStore;
  }
  
  boolean setPreferences(PreferenceScreen paramPreferenceScreen)
  {
    if (paramPreferenceScreen != mPreferenceScreen)
    {
      mPreferenceScreen = paramPreferenceScreen;
      return true;
    }
    return false;
  }
  
  public void setSharedPreferencesMode(int paramInt)
  {
    mSharedPreferencesMode = paramInt;
    mSharedPreferences = null;
  }
  
  public void setSharedPreferencesName(String paramString)
  {
    mSharedPreferencesName = paramString;
    mSharedPreferences = null;
  }
  
  @SystemApi
  public void setStorageCredentialProtected()
  {
    mStorage = 2;
    mSharedPreferences = null;
  }
  
  public void setStorageDefault()
  {
    mStorage = 0;
    mSharedPreferences = null;
  }
  
  public void setStorageDeviceProtected()
  {
    mStorage = 1;
    mSharedPreferences = null;
  }
  
  boolean shouldCommit()
  {
    return mNoCommit ^ true;
  }
  
  void unregisterOnActivityDestroyListener(OnActivityDestroyListener paramOnActivityDestroyListener)
  {
    try
    {
      if (mActivityDestroyListeners != null) {
        mActivityDestroyListeners.remove(paramOnActivityDestroyListener);
      }
      return;
    }
    finally {}
  }
  
  void unregisterOnActivityResultListener(OnActivityResultListener paramOnActivityResultListener)
  {
    try
    {
      if (mActivityResultListeners != null) {
        mActivityResultListeners.remove(paramOnActivityResultListener);
      }
      return;
    }
    finally {}
  }
  
  public void unregisterOnActivityStopListener(OnActivityStopListener paramOnActivityStopListener)
  {
    try
    {
      if (mActivityStopListeners != null) {
        mActivityStopListeners.remove(paramOnActivityStopListener);
      }
      return;
    }
    finally {}
  }
  
  public static abstract interface OnActivityDestroyListener
  {
    public abstract void onActivityDestroy();
  }
  
  public static abstract interface OnActivityResultListener
  {
    public abstract boolean onActivityResult(int paramInt1, int paramInt2, Intent paramIntent);
  }
  
  public static abstract interface OnActivityStopListener
  {
    public abstract void onActivityStop();
  }
  
  public static abstract interface OnPreferenceTreeClickListener
  {
    public abstract boolean onPreferenceTreeClick(PreferenceScreen paramPreferenceScreen, Preference paramPreference);
  }
}
