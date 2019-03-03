package android.telecom;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Process;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultDialerManager
{
  private static final String TAG = "DefaultDialerManager";
  
  public DefaultDialerManager() {}
  
  private static List<String> filterByIntent(Context paramContext, List<String> paramList, Intent paramIntent, int paramInt)
  {
    if ((paramList != null) && (!paramList.isEmpty()))
    {
      ArrayList localArrayList = new ArrayList();
      paramContext = paramContext.getPackageManager();
      int i = 0;
      paramIntent = paramContext.queryIntentActivitiesAsUser(paramIntent, 0, paramInt);
      int j = paramIntent.size();
      for (paramInt = i; paramInt < j; paramInt++)
      {
        paramContext = getactivityInfo;
        if ((paramContext != null) && (paramList.contains(packageName)) && (!localArrayList.contains(packageName))) {
          localArrayList.add(packageName);
        }
      }
      return localArrayList;
    }
    return new ArrayList();
  }
  
  public static String getDefaultDialerApplication(Context paramContext)
  {
    return getDefaultDialerApplication(paramContext, paramContext.getUserId());
  }
  
  public static String getDefaultDialerApplication(Context paramContext, int paramInt)
  {
    String str = Settings.Secure.getStringForUser(paramContext.getContentResolver(), "dialer_default_application", paramInt);
    List localList = getInstalledDialerApplications(paramContext, paramInt);
    if (localList.contains(str)) {
      return str;
    }
    paramContext = getTelecomManager(paramContext).getSystemDialerPackage();
    if (TextUtils.isEmpty(paramContext)) {
      return null;
    }
    if (localList.contains(paramContext)) {
      return paramContext;
    }
    return null;
  }
  
  public static List<String> getInstalledDialerApplications(Context paramContext)
  {
    return getInstalledDialerApplications(paramContext, Process.myUserHandle().getIdentifier());
  }
  
  public static List<String> getInstalledDialerApplications(Context paramContext, int paramInt)
  {
    Object localObject1 = paramContext.getPackageManager();
    Object localObject2 = new Intent("android.intent.action.DIAL");
    localObject2 = ((PackageManager)localObject1).queryIntentActivitiesAsUser((Intent)localObject2, 0, paramInt);
    localObject1 = new ArrayList();
    Iterator localIterator = ((List)localObject2).iterator();
    while (localIterator.hasNext())
    {
      localObject2 = (ResolveInfo)localIterator.next();
      ActivityInfo localActivityInfo = activityInfo;
      if ((localActivityInfo != null) && (!((List)localObject1).contains(packageName)) && (targetUserId == -2)) {
        ((List)localObject1).add(packageName);
      }
    }
    localObject2 = new Intent("android.intent.action.DIAL");
    ((Intent)localObject2).setData(Uri.fromParts("tel", "", null));
    return filterByIntent(paramContext, (List)localObject1, (Intent)localObject2, paramInt);
  }
  
  private static TelecomManager getTelecomManager(Context paramContext)
  {
    return (TelecomManager)paramContext.getSystemService("telecom");
  }
  
  public static boolean isDefaultOrSystemDialer(Context paramContext, String paramString)
  {
    boolean bool1 = TextUtils.isEmpty(paramString);
    boolean bool2 = false;
    if (bool1) {
      return false;
    }
    paramContext = getTelecomManager(paramContext);
    if ((!paramString.equals(paramContext.getDefaultDialerPackage())) && (!paramString.equals(paramContext.getSystemDialerPackage()))) {
      return bool2;
    }
    bool2 = true;
    return bool2;
  }
  
  public static boolean setDefaultDialerApplication(Context paramContext, String paramString)
  {
    return setDefaultDialerApplication(paramContext, paramString, ActivityManager.getCurrentUser());
  }
  
  public static boolean setDefaultDialerApplication(Context paramContext, String paramString, int paramInt)
  {
    String str = Settings.Secure.getStringForUser(paramContext.getContentResolver(), "dialer_default_application", paramInt);
    if ((paramString != null) && (str != null) && (paramString.equals(str))) {
      return false;
    }
    if (getInstalledDialerApplications(paramContext).contains(paramString))
    {
      Settings.Secure.putStringForUser(paramContext.getContentResolver(), "dialer_default_application", paramString, paramInt);
      return true;
    }
    return false;
  }
}
