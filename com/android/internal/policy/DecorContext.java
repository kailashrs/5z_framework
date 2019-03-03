package com.android.internal.policy;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import java.lang.ref.WeakReference;

class DecorContext
  extends ContextThemeWrapper
{
  private WeakReference<Context> mActivityContext;
  private Resources mActivityResources;
  private PhoneWindow mPhoneWindow;
  private WindowManager mWindowManager;
  
  public DecorContext(Context paramContext1, Context paramContext2)
  {
    super(paramContext1, null);
    mActivityContext = new WeakReference(paramContext2);
    mActivityResources = paramContext2.getResources();
  }
  
  public AssetManager getAssets()
  {
    return mActivityResources.getAssets();
  }
  
  public Resources getResources()
  {
    Context localContext = (Context)mActivityContext.get();
    if (localContext != null) {
      mActivityResources = localContext.getResources();
    }
    return mActivityResources;
  }
  
  public Object getSystemService(String paramString)
  {
    if ("window".equals(paramString))
    {
      if (mWindowManager == null) {
        mWindowManager = ((WindowManagerImpl)super.getSystemService("window")).createLocalWindowManager(mPhoneWindow);
      }
      return mWindowManager;
    }
    return super.getSystemService(paramString);
  }
  
  void setPhoneWindow(PhoneWindow paramPhoneWindow)
  {
    mPhoneWindow = paramPhoneWindow;
    mWindowManager = null;
  }
}
