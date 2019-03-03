package android.content.pm;

import android.app.AppGlobals;
import android.content.Intent;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;

public class AppsQueryHelper
{
  public static int GET_APPS_WITH_INTERACT_ACROSS_USERS_PERM = 2;
  public static int GET_IMES = 4;
  public static int GET_NON_LAUNCHABLE_APPS = 1;
  public static int GET_REQUIRED_FOR_SYSTEM_USER = 8;
  private List<ApplicationInfo> mAllApps;
  private final IPackageManager mPackageManager;
  
  public AppsQueryHelper()
  {
    this(AppGlobals.getPackageManager());
  }
  
  public AppsQueryHelper(IPackageManager paramIPackageManager)
  {
    mPackageManager = paramIPackageManager;
  }
  
  @VisibleForTesting
  protected List<ApplicationInfo> getAllApps(int paramInt)
  {
    try
    {
      List localList = mPackageManager.getInstalledApplications(8704, paramInt).getList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @VisibleForTesting
  protected List<PackageInfo> getPackagesHoldingPermission(String paramString, int paramInt)
  {
    try
    {
      paramString = mPackageManager.getPackagesHoldingPermissions(new String[] { paramString }, 0, paramInt).getList();
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public List<String> queryApps(int paramInt, boolean paramBoolean, UserHandle paramUserHandle)
  {
    int i = GET_NON_LAUNCHABLE_APPS;
    int j = 1;
    int k;
    if ((paramInt & i) > 0) {
      k = 1;
    } else {
      k = 0;
    }
    int m;
    if ((paramInt & GET_APPS_WITH_INTERACT_ACROSS_USERS_PERM) > 0) {
      m = 1;
    } else {
      m = 0;
    }
    if ((paramInt & GET_IMES) > 0) {
      i = 1;
    } else {
      i = 0;
    }
    if ((paramInt & GET_REQUIRED_FOR_SYSTEM_USER) <= 0) {
      j = 0;
    }
    if (mAllApps == null) {
      mAllApps = getAllApps(paramUserHandle.getIdentifier());
    }
    ArrayList localArrayList = new ArrayList();
    if (paramInt == 0)
    {
      j = mAllApps.size();
      for (paramInt = 0; paramInt < j; paramInt++)
      {
        paramUserHandle = (ApplicationInfo)mAllApps.get(paramInt);
        if ((!paramBoolean) || (paramUserHandle.isSystemApp())) {
          localArrayList.add(packageName);
        }
      }
      return localArrayList;
    }
    Object localObject1;
    Object localObject2;
    if (k != 0)
    {
      localObject1 = new Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER");
      localObject2 = queryIntentActivitiesAsUser((Intent)localObject1, paramUserHandle.getIdentifier());
      localObject1 = new ArraySet();
      k = ((List)localObject2).size();
      for (paramInt = 0; paramInt < k; paramInt++) {
        ((ArraySet)localObject1).add(getactivityInfo.packageName);
      }
      k = mAllApps.size();
      for (paramInt = 0; paramInt < k; paramInt++)
      {
        localObject2 = (ApplicationInfo)mAllApps.get(paramInt);
        if ((!paramBoolean) || (((ApplicationInfo)localObject2).isSystemApp()))
        {
          localObject2 = packageName;
          if (!((ArraySet)localObject1).contains(localObject2)) {
            localArrayList.add(localObject2);
          }
        }
      }
    }
    if (m != 0)
    {
      localObject1 = getPackagesHoldingPermission("android.permission.INTERACT_ACROSS_USERS", paramUserHandle.getIdentifier());
      m = ((List)localObject1).size();
      for (paramInt = 0; paramInt < m; paramInt++)
      {
        localObject2 = (PackageInfo)((List)localObject1).get(paramInt);
        if (((!paramBoolean) || (applicationInfo.isSystemApp())) && (!localArrayList.contains(packageName))) {
          localArrayList.add(packageName);
        }
      }
    }
    if (i != 0)
    {
      localObject1 = queryIntentServicesAsUser(new Intent("android.view.InputMethod"), paramUserHandle.getIdentifier());
      i = ((List)localObject1).size();
      for (paramInt = 0; paramInt < i; paramInt++)
      {
        paramUserHandle = getserviceInfo;
        if (((!paramBoolean) || (applicationInfo.isSystemApp())) && (!localArrayList.contains(packageName))) {
          localArrayList.add(packageName);
        }
      }
    }
    if (j != 0)
    {
      j = mAllApps.size();
      for (paramInt = 0; paramInt < j; paramInt++)
      {
        paramUserHandle = (ApplicationInfo)mAllApps.get(paramInt);
        if (((!paramBoolean) || (paramUserHandle.isSystemApp())) && (paramUserHandle.isRequiredForSystemUser())) {
          localArrayList.add(packageName);
        }
      }
    }
    return localArrayList;
  }
  
  @VisibleForTesting
  protected List<ResolveInfo> queryIntentActivitiesAsUser(Intent paramIntent, int paramInt)
  {
    try
    {
      paramIntent = mPackageManager.queryIntentActivities(paramIntent, null, 795136, paramInt).getList();
      return paramIntent;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  @VisibleForTesting
  protected List<ResolveInfo> queryIntentServicesAsUser(Intent paramIntent, int paramInt)
  {
    try
    {
      paramIntent = mPackageManager.queryIntentServices(paramIntent, null, 819328, paramInt).getList();
      return paramIntent;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
}
