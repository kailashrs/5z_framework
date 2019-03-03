package android.app;

import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.autofill.AutofillManager.AutofillClient;
import android.view.autofill.Helper;
import java.util.ArrayList;

public class Application
  extends ContextWrapper
  implements ComponentCallbacks2
{
  private static final String TAG = "Application";
  private ArrayList<ActivityLifecycleCallbacks> mActivityLifecycleCallbacks = new ArrayList();
  private ArrayList<OnProvideAssistDataListener> mAssistCallbacks = null;
  private ArrayList<ComponentCallbacks> mComponentCallbacks = new ArrayList();
  private int mFlipfont = 0;
  public LoadedApk mLoadedApk;
  
  public Application()
  {
    super(null);
  }
  
  private Object[] collectActivityLifecycleCallbacks()
  {
    Object[] arrayOfObject = null;
    synchronized (mActivityLifecycleCallbacks)
    {
      if (mActivityLifecycleCallbacks.size() > 0) {
        arrayOfObject = mActivityLifecycleCallbacks.toArray();
      }
      return arrayOfObject;
    }
  }
  
  private Object[] collectComponentCallbacks()
  {
    Object[] arrayOfObject = null;
    synchronized (mComponentCallbacks)
    {
      if (mComponentCallbacks.size() > 0) {
        arrayOfObject = mComponentCallbacks.toArray();
      }
      return arrayOfObject;
    }
  }
  
  public static String getProcessName()
  {
    return ActivityThread.currentProcessName();
  }
  
  final void attach(Context paramContext)
  {
    attachBaseContext(paramContext);
    mLoadedApk = getImplmPackageInfo;
  }
  
  void dispatchActivityCreated(Activity paramActivity, Bundle paramBundle)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    if (arrayOfObject != null) {
      for (int i = 0; i < arrayOfObject.length; i++) {
        ((ActivityLifecycleCallbacks)arrayOfObject[i]).onActivityCreated(paramActivity, paramBundle);
      }
    }
  }
  
  void dispatchActivityDestroyed(Activity paramActivity)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    if (arrayOfObject != null) {
      for (int i = 0; i < arrayOfObject.length; i++) {
        ((ActivityLifecycleCallbacks)arrayOfObject[i]).onActivityDestroyed(paramActivity);
      }
    }
  }
  
  void dispatchActivityPaused(Activity paramActivity)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    if (arrayOfObject != null) {
      for (int i = 0; i < arrayOfObject.length; i++) {
        ((ActivityLifecycleCallbacks)arrayOfObject[i]).onActivityPaused(paramActivity);
      }
    }
  }
  
  void dispatchActivityResumed(Activity paramActivity)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    if (arrayOfObject != null) {
      for (int i = 0; i < arrayOfObject.length; i++) {
        ((ActivityLifecycleCallbacks)arrayOfObject[i]).onActivityResumed(paramActivity);
      }
    }
  }
  
  void dispatchActivitySaveInstanceState(Activity paramActivity, Bundle paramBundle)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    if (arrayOfObject != null) {
      for (int i = 0; i < arrayOfObject.length; i++) {
        ((ActivityLifecycleCallbacks)arrayOfObject[i]).onActivitySaveInstanceState(paramActivity, paramBundle);
      }
    }
  }
  
  void dispatchActivityStarted(Activity paramActivity)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    if (arrayOfObject != null) {
      for (int i = 0; i < arrayOfObject.length; i++) {
        ((ActivityLifecycleCallbacks)arrayOfObject[i]).onActivityStarted(paramActivity);
      }
    }
  }
  
  void dispatchActivityStopped(Activity paramActivity)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    if (arrayOfObject != null) {
      for (int i = 0; i < arrayOfObject.length; i++) {
        ((ActivityLifecycleCallbacks)arrayOfObject[i]).onActivityStopped(paramActivity);
      }
    }
  }
  
  void dispatchOnProvideAssistData(Activity paramActivity, Bundle paramBundle)
  {
    try
    {
      if (mAssistCallbacks == null) {
        return;
      }
      Object[] arrayOfObject = mAssistCallbacks.toArray();
      if (arrayOfObject != null) {
        for (int i = 0; i < arrayOfObject.length; i++) {
          ((OnProvideAssistDataListener)arrayOfObject[i]).onProvideAssistData(paramActivity, paramBundle);
        }
      }
      return;
    }
    finally {}
  }
  
  public AutofillManager.AutofillClient getAutofillClient()
  {
    Object localObject1 = super.getAutofillClient();
    if (localObject1 != null) {
      return localObject1;
    }
    if (Helper.sVerbose) {
      Log.v("Application", "getAutofillClient(): null on super, trying to find activity thread");
    }
    localObject1 = ActivityThread.currentActivityThread();
    if (localObject1 == null) {
      return null;
    }
    int i = mActivities.size();
    for (int j = 0; j < i; j++)
    {
      Object localObject2 = (ActivityThread.ActivityClientRecord)mActivities.valueAt(j);
      if (localObject2 != null)
      {
        localObject2 = activity;
        if ((localObject2 != null) && (((Activity)localObject2).getWindow().getDecorView().hasFocus()))
        {
          if (Helper.sVerbose)
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("getAutofillClient(): found activity for ");
            ((StringBuilder)localObject1).append(this);
            ((StringBuilder)localObject1).append(": ");
            ((StringBuilder)localObject1).append(localObject2);
            Log.v("Application", ((StringBuilder)localObject1).toString());
          }
          return localObject2;
        }
      }
    }
    if (Helper.sVerbose)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("getAutofillClient(): none of the ");
      ((StringBuilder)localObject1).append(i);
      ((StringBuilder)localObject1).append(" activities on ");
      ((StringBuilder)localObject1).append(this);
      ((StringBuilder)localObject1).append(" have focus");
      Log.v("Application", ((StringBuilder)localObject1).toString());
    }
    return null;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    if ((paramConfiguration != null) && (FlipFont > 0) && (mFlipfont != FlipFont))
    {
      Typeface.SetAppTypeFace(getApplicationContext(), getPackageName());
      mFlipfont = FlipFont;
    }
    Object[] arrayOfObject = collectComponentCallbacks();
    if (arrayOfObject != null) {
      for (int i = 0; i < arrayOfObject.length; i++) {
        ((ComponentCallbacks)arrayOfObject[i]).onConfigurationChanged(paramConfiguration);
      }
    }
  }
  
  public void onCreate()
  {
    Object localObject = null;
    try
    {
      Context localContext = getApplicationContext();
      localObject = localContext;
    }
    catch (Exception localException) {}
    Typeface.SetAppTypeFace(localObject, getPackageName());
  }
  
  public void onLowMemory()
  {
    Object[] arrayOfObject = collectComponentCallbacks();
    if (arrayOfObject != null) {
      for (int i = 0; i < arrayOfObject.length; i++) {
        ((ComponentCallbacks)arrayOfObject[i]).onLowMemory();
      }
    }
  }
  
  public void onTerminate() {}
  
  public void onTrimMemory(int paramInt)
  {
    Object[] arrayOfObject = collectComponentCallbacks();
    if (arrayOfObject != null) {
      for (int i = 0; i < arrayOfObject.length; i++)
      {
        Object localObject = arrayOfObject[i];
        if ((localObject instanceof ComponentCallbacks2)) {
          ((ComponentCallbacks2)localObject).onTrimMemory(paramInt);
        }
      }
    }
  }
  
  public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks paramActivityLifecycleCallbacks)
  {
    synchronized (mActivityLifecycleCallbacks)
    {
      mActivityLifecycleCallbacks.add(paramActivityLifecycleCallbacks);
      return;
    }
  }
  
  public void registerComponentCallbacks(ComponentCallbacks paramComponentCallbacks)
  {
    synchronized (mComponentCallbacks)
    {
      mComponentCallbacks.add(paramComponentCallbacks);
      return;
    }
  }
  
  public void registerOnProvideAssistDataListener(OnProvideAssistDataListener paramOnProvideAssistDataListener)
  {
    try
    {
      if (mAssistCallbacks == null)
      {
        ArrayList localArrayList = new java/util/ArrayList;
        localArrayList.<init>();
        mAssistCallbacks = localArrayList;
      }
      mAssistCallbacks.add(paramOnProvideAssistDataListener);
      return;
    }
    finally {}
  }
  
  public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks paramActivityLifecycleCallbacks)
  {
    synchronized (mActivityLifecycleCallbacks)
    {
      mActivityLifecycleCallbacks.remove(paramActivityLifecycleCallbacks);
      return;
    }
  }
  
  public void unregisterComponentCallbacks(ComponentCallbacks paramComponentCallbacks)
  {
    synchronized (mComponentCallbacks)
    {
      mComponentCallbacks.remove(paramComponentCallbacks);
      return;
    }
  }
  
  public void unregisterOnProvideAssistDataListener(OnProvideAssistDataListener paramOnProvideAssistDataListener)
  {
    try
    {
      if (mAssistCallbacks != null) {
        mAssistCallbacks.remove(paramOnProvideAssistDataListener);
      }
      return;
    }
    finally {}
  }
  
  public static abstract interface ActivityLifecycleCallbacks
  {
    public abstract void onActivityCreated(Activity paramActivity, Bundle paramBundle);
    
    public abstract void onActivityDestroyed(Activity paramActivity);
    
    public abstract void onActivityPaused(Activity paramActivity);
    
    public abstract void onActivityResumed(Activity paramActivity);
    
    public abstract void onActivitySaveInstanceState(Activity paramActivity, Bundle paramBundle);
    
    public abstract void onActivityStarted(Activity paramActivity);
    
    public abstract void onActivityStopped(Activity paramActivity);
  }
  
  public static abstract interface OnProvideAssistDataListener
  {
    public abstract void onProvideAssistData(Activity paramActivity, Bundle paramBundle);
  }
}
