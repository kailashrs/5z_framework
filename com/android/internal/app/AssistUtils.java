package com.android.internal.app;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings.Secure;
import android.util.Log;

public class AssistUtils
{
  private static final String TAG = "AssistUtils";
  private final Context mContext;
  private final IVoiceInteractionManagerService mVoiceInteractionManagerService;
  
  public AssistUtils(Context paramContext)
  {
    mContext = paramContext;
    mVoiceInteractionManagerService = IVoiceInteractionManagerService.Stub.asInterface(ServiceManager.getService("voiceinteraction"));
  }
  
  public static boolean allowDisablingAssistDisclosure(Context paramContext)
  {
    return paramContext.getResources().getBoolean(17956874);
  }
  
  private static boolean isDisclosureEnabled(Context paramContext)
  {
    paramContext = paramContext.getContentResolver();
    boolean bool = false;
    if (Settings.Secure.getInt(paramContext, "assist_disclosure_enabled", 0) != 0) {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isPreinstalledAssistant(Context paramContext, ComponentName paramComponentName)
  {
    boolean bool = false;
    if (paramComponentName == null) {
      return false;
    }
    try
    {
      paramContext = paramContext.getPackageManager().getApplicationInfo(paramComponentName.getPackageName(), 0);
      if ((!paramContext.isSystemApp()) && (!paramContext.isUpdatedSystemApp())) {
        break label43;
      }
      bool = true;
      label43:
      return bool;
    }
    catch (PackageManager.NameNotFoundException paramContext) {}
    return false;
  }
  
  public static boolean shouldDisclose(Context paramContext, ComponentName paramComponentName)
  {
    boolean bool1 = allowDisablingAssistDisclosure(paramContext);
    boolean bool2 = true;
    if (!bool1) {
      return true;
    }
    bool1 = bool2;
    if (!isDisclosureEnabled(paramContext)) {
      if (!isPreinstalledAssistant(paramContext, paramComponentName)) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }
    }
    return bool1;
  }
  
  public boolean activeServiceSupportsAssistGesture()
  {
    boolean bool1 = false;
    boolean bool2 = bool1;
    try
    {
      if (mVoiceInteractionManagerService != null)
      {
        boolean bool3 = mVoiceInteractionManagerService.activeServiceSupportsAssist();
        bool2 = bool1;
        if (bool3) {
          bool2 = true;
        }
      }
      return bool2;
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("AssistUtils", "Failed to call activeServiceSupportsAssistGesture", localRemoteException);
    }
    return false;
  }
  
  public boolean activeServiceSupportsLaunchFromKeyguard()
  {
    boolean bool1 = false;
    boolean bool2 = bool1;
    try
    {
      if (mVoiceInteractionManagerService != null)
      {
        boolean bool3 = mVoiceInteractionManagerService.activeServiceSupportsLaunchFromKeyguard();
        bool2 = bool1;
        if (bool3) {
          bool2 = true;
        }
      }
      return bool2;
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("AssistUtils", "Failed to call activeServiceSupportsLaunchFromKeyguard", localRemoteException);
    }
    return false;
  }
  
  public ComponentName getActiveServiceComponentName()
  {
    try
    {
      if (mVoiceInteractionManagerService != null)
      {
        ComponentName localComponentName = mVoiceInteractionManagerService.getActiveServiceComponentName();
        return localComponentName;
      }
      return null;
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("AssistUtils", "Failed to call getActiveServiceComponentName", localRemoteException);
    }
    return null;
  }
  
  public ComponentName getAssistComponentForUser(int paramInt)
  {
    Object localObject = Settings.Secure.getStringForUser(mContext.getContentResolver(), "assistant", paramInt);
    if (localObject != null) {
      return ComponentName.unflattenFromString((String)localObject);
    }
    localObject = mContext.getResources().getString(17039686);
    if (localObject != null) {
      return ComponentName.unflattenFromString((String)localObject);
    }
    if (activeServiceSupportsAssistGesture()) {
      return getActiveServiceComponentName();
    }
    localObject = (SearchManager)mContext.getSystemService("search");
    if (localObject == null) {
      return null;
    }
    localObject = ((SearchManager)localObject).getAssistIntent(false);
    localObject = mContext.getPackageManager().resolveActivityAsUser((Intent)localObject, 65536, paramInt);
    if (localObject != null) {
      return new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
    }
    return null;
  }
  
  public void hideCurrentSession()
  {
    try
    {
      if (mVoiceInteractionManagerService != null) {
        mVoiceInteractionManagerService.hideCurrentSession();
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("AssistUtils", "Failed to call hideCurrentSession", localRemoteException);
    }
  }
  
  public boolean isSessionRunning()
  {
    boolean bool1 = false;
    boolean bool2 = bool1;
    try
    {
      if (mVoiceInteractionManagerService != null)
      {
        boolean bool3 = mVoiceInteractionManagerService.isSessionRunning();
        bool2 = bool1;
        if (bool3) {
          bool2 = true;
        }
      }
      return bool2;
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("AssistUtils", "Failed to call isSessionRunning", localRemoteException);
    }
    return false;
  }
  
  public void launchVoiceAssistFromKeyguard()
  {
    try
    {
      if (mVoiceInteractionManagerService != null) {
        mVoiceInteractionManagerService.launchVoiceAssistFromKeyguard();
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("AssistUtils", "Failed to call launchVoiceAssistFromKeyguard", localRemoteException);
    }
  }
  
  public void onLockscreenShown()
  {
    try
    {
      if (mVoiceInteractionManagerService != null) {
        mVoiceInteractionManagerService.onLockscreenShown();
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("AssistUtils", "Failed to call onLockscreenShown", localRemoteException);
    }
  }
  
  public void registerVoiceInteractionSessionListener(IVoiceInteractionSessionListener paramIVoiceInteractionSessionListener)
  {
    try
    {
      if (mVoiceInteractionManagerService != null) {
        mVoiceInteractionManagerService.registerVoiceInteractionSessionListener(paramIVoiceInteractionSessionListener);
      }
    }
    catch (RemoteException paramIVoiceInteractionSessionListener)
    {
      Log.w("AssistUtils", "Failed to register voice interaction listener", paramIVoiceInteractionSessionListener);
    }
  }
  
  public boolean showSessionForActiveService(Bundle paramBundle, int paramInt, IVoiceInteractionSessionShowCallback paramIVoiceInteractionSessionShowCallback, IBinder paramIBinder)
  {
    try
    {
      if (mVoiceInteractionManagerService != null)
      {
        boolean bool = mVoiceInteractionManagerService.showSessionForActiveService(paramBundle, paramInt, paramIVoiceInteractionSessionShowCallback, paramIBinder);
        return bool;
      }
    }
    catch (RemoteException paramBundle)
    {
      Log.w("AssistUtils", "Failed to call showSessionForActiveService", paramBundle);
    }
    return false;
  }
}
