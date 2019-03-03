package com.android.internal.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.app.IActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Slog;
import android.widget.Toast;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Iterator;
import java.util.List;

public class IntentForwarderActivity
  extends Activity
{
  public static String FORWARD_INTENT_TO_MANAGED_PROFILE = "com.android.internal.app.ForwardIntentToManagedProfile";
  public static String FORWARD_INTENT_TO_PARENT;
  public static String TAG = "IntentForwarderActivity";
  private Injector mInjector;
  
  static
  {
    FORWARD_INTENT_TO_PARENT = "com.android.internal.app.ForwardIntentToParent";
  }
  
  public IntentForwarderActivity() {}
  
  private int getManagedProfile()
  {
    Object localObject1 = mInjector.getUserManager().getProfiles(UserHandle.myUserId()).iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (UserInfo)((Iterator)localObject1).next();
      if (((UserInfo)localObject2).isManagedProfile()) {
        return id;
      }
    }
    Object localObject2 = TAG;
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(FORWARD_INTENT_TO_MANAGED_PROFILE);
    ((StringBuilder)localObject1).append(" has been called, but there is no managed profile");
    Slog.wtf((String)localObject2, ((StringBuilder)localObject1).toString());
    return 55536;
  }
  
  private int getProfileParent()
  {
    Object localObject = mInjector.getUserManager().getProfileParent(UserHandle.myUserId());
    if (localObject == null)
    {
      localObject = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(FORWARD_INTENT_TO_PARENT);
      localStringBuilder.append(" has been called, but there is no parent");
      Slog.wtf((String)localObject, localStringBuilder.toString());
      return 55536;
    }
    return id;
  }
  
  private void sanitizeIntent(Intent paramIntent)
  {
    paramIntent.setPackage(null);
    paramIntent.setComponent(null);
  }
  
  Intent canForward(Intent paramIntent, int paramInt)
  {
    Intent localIntent = new Intent(paramIntent);
    localIntent.addFlags(50331648);
    sanitizeIntent(localIntent);
    paramIntent = localIntent;
    if ("android.intent.action.CHOOSER".equals(localIntent.getAction()))
    {
      if (localIntent.hasExtra("android.intent.extra.INITIAL_INTENTS"))
      {
        Slog.wtf(TAG, "An chooser intent with extra initial intents cannot be forwarded to a different user");
        return null;
      }
      if (localIntent.hasExtra("android.intent.extra.REPLACEMENT_EXTRAS"))
      {
        Slog.wtf(TAG, "A chooser intent with replacement extras cannot be forwarded to a different user");
        return null;
      }
      localObject = (Intent)localIntent.getParcelableExtra("android.intent.extra.INTENT");
      paramIntent = (Intent)localObject;
      if (localObject == null)
      {
        Slog.wtf(TAG, "Cannot forward a chooser intent with no extra android.intent.extra.INTENT");
        return null;
      }
    }
    if (localIntent.getSelector() != null) {
      paramIntent = localIntent.getSelector();
    }
    Object localObject = paramIntent.resolveTypeIfNeeded(getContentResolver());
    sanitizeIntent(paramIntent);
    try
    {
      boolean bool = mInjector.getIPackageManager().canForwardTo(paramIntent, (String)localObject, getUserId(), paramInt);
      if (bool) {
        return localIntent;
      }
    }
    catch (RemoteException paramIntent)
    {
      Slog.e(TAG, "PackageManagerService is dead?");
    }
    return null;
  }
  
  @VisibleForTesting
  protected Injector createInjector()
  {
    return new InjectorImpl(null);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    mInjector = createInjector();
    paramBundle = getIntent();
    Object localObject1 = paramBundle.getComponent().getClassName();
    int i;
    int j;
    if (((String)localObject1).equals(FORWARD_INTENT_TO_PARENT))
    {
      i = 17040035;
      j = getProfileParent();
    }
    for (;;)
    {
      break;
      if (((String)localObject1).equals(FORWARD_INTENT_TO_MANAGED_PROFILE))
      {
        i = 17040036;
        j = getManagedProfile();
      }
      else
      {
        String str1 = TAG;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append(IntentForwarderActivity.class.getName());
        ((StringBuilder)localObject1).append(" cannot be called directly");
        Slog.wtf(str1, ((StringBuilder)localObject1).toString());
        i = -1;
        j = 55536;
      }
    }
    if (j == 55536)
    {
      finish();
      return;
    }
    int k = getUserId();
    localObject1 = canForward(paramBundle, j);
    Object localObject2;
    if (localObject1 != null)
    {
      if ("android.intent.action.CHOOSER".equals(((Intent)localObject1).getAction())) {
        ((Intent)((Intent)localObject1).getParcelableExtra("android.intent.extra.INTENT")).prepareToLeaveUser(k);
      } else {
        ((Intent)localObject1).prepareToLeaveUser(k);
      }
      paramBundle = mInjector.getPackageManager().resolveActivityAsUser((Intent)localObject1, 65536, j);
      int m;
      if ((paramBundle != null) && (activityInfo != null) && ("android".equals(activityInfo.packageName)) && ((ResolverActivity.class.getName().equals(activityInfo.name)) || (ChooserActivity.class.getName().equals(activityInfo.name)))) {
        m = 0;
      } else {
        m = 1;
      }
      try
      {
        startActivityAsCaller((Intent)localObject1, null, false, j);
      }
      catch (RuntimeException localRuntimeException)
      {
        j = -1;
        paramBundle = "?";
        try
        {
          n = ActivityManager.getService().getLaunchedFromUid(getActivityToken());
          j = n;
          localObject1 = ActivityManager.getService().getLaunchedFromPackage(getActivityToken());
          paramBundle = (Bundle)localObject1;
          j = n;
        }
        catch (RemoteException localRemoteException) {}
        String str2 = TAG;
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Unable to launch as UID ");
        ((StringBuilder)localObject2).append(j);
        ((StringBuilder)localObject2).append(" package ");
        ((StringBuilder)localObject2).append(paramBundle);
        ((StringBuilder)localObject2).append(", while running in ");
        ((StringBuilder)localObject2).append(ActivityThread.currentProcessName());
        Slog.wtf(str2, ((StringBuilder)localObject2).toString(), localRuntimeException);
      }
      int n = 0;
      j = n;
      if (getPackageManager().hasSystemFeature("asus.software.twinapps"))
      {
        j = n;
        if (k == ((UserManager)getSystemService("user")).getTwinAppsId()) {
          j = 1;
        }
      }
      if ((m != 0) && (j == 0)) {
        Toast.makeText(this, getString(i), 1).show();
      }
    }
    else
    {
      localObject2 = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("the intent: ");
      localStringBuilder.append(paramBundle);
      localStringBuilder.append(" cannot be forwarded from user ");
      localStringBuilder.append(k);
      localStringBuilder.append(" to user ");
      localStringBuilder.append(j);
      Slog.wtf((String)localObject2, localStringBuilder.toString());
    }
    finish();
  }
  
  public static abstract interface Injector
  {
    public abstract IPackageManager getIPackageManager();
    
    public abstract PackageManager getPackageManager();
    
    public abstract UserManager getUserManager();
  }
  
  private class InjectorImpl
    implements IntentForwarderActivity.Injector
  {
    private InjectorImpl() {}
    
    public IPackageManager getIPackageManager()
    {
      return AppGlobals.getPackageManager();
    }
    
    public PackageManager getPackageManager()
    {
      return IntentForwarderActivity.this.getPackageManager();
    }
    
    public UserManager getUserManager()
    {
      return (UserManager)getSystemService(UserManager.class);
    }
  }
}
