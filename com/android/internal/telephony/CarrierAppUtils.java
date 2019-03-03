package com.android.internal.telephony;

import android.content.ContentResolver;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.SystemConfig;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class CarrierAppUtils
{
  private static final boolean DEBUG = false;
  private static final String TAG = "CarrierAppUtils";
  
  private CarrierAppUtils() {}
  
  public static void disableCarrierAppsUntilPrivileged(String paramString, IPackageManager paramIPackageManager, ContentResolver paramContentResolver, int paramInt)
  {
    try
    {
      SystemConfig localSystemConfig = SystemConfig.getInstance();
      ArraySet localArraySet = localSystemConfig.getDisabledUntilUsedPreinstalledCarrierApps();
      disableCarrierAppsUntilPrivileged(paramString, paramIPackageManager, null, paramContentResolver, paramInt, localArraySet, localSystemConfig.getDisabledUntilUsedPreinstalledCarrierAssociatedApps());
      return;
    }
    finally {}
  }
  
  public static void disableCarrierAppsUntilPrivileged(String paramString, IPackageManager paramIPackageManager, TelephonyManager paramTelephonyManager, ContentResolver paramContentResolver, int paramInt)
  {
    try
    {
      SystemConfig localSystemConfig = SystemConfig.getInstance();
      ArraySet localArraySet = localSystemConfig.getDisabledUntilUsedPreinstalledCarrierApps();
      disableCarrierAppsUntilPrivileged(paramString, paramIPackageManager, paramTelephonyManager, paramContentResolver, paramInt, localArraySet, localSystemConfig.getDisabledUntilUsedPreinstalledCarrierAssociatedApps());
      return;
    }
    finally {}
  }
  
  @VisibleForTesting
  public static void disableCarrierAppsUntilPrivileged(String paramString, IPackageManager paramIPackageManager, TelephonyManager paramTelephonyManager, ContentResolver paramContentResolver, int paramInt, ArraySet<String> paramArraySet, ArrayMap<String, List<String>> paramArrayMap)
  {
    paramArraySet = getDefaultCarrierAppCandidatesHelper(paramIPackageManager, paramInt, paramArraySet);
    if ((paramArraySet != null) && (!paramArraySet.isEmpty()))
    {
      Map localMap = getDefaultCarrierAssociatedAppsHelper(paramIPackageManager, paramInt, paramArrayMap);
      ArrayList localArrayList = new ArrayList();
      int i = 0;
      int j;
      if (Settings.Secure.getIntForUser(paramContentResolver, "carrier_apps_handled", 0, paramInt) == 1) {
        j = 1;
      } else {
        j = 0;
      }
      try
      {
        paramArrayMap = paramArraySet.iterator();
        while (paramArrayMap.hasNext())
        {
          Object localObject1 = (ApplicationInfo)paramArrayMap.next();
          paramArraySet = packageName;
          int k;
          if ((paramTelephonyManager != null) && (paramTelephonyManager.checkCarrierPrivilegesForPackageAnyPhone(paramArraySet) == 1)) {
            k = 1;
          } else {
            k = i;
          }
          Object localObject2;
          label239:
          Object localObject3;
          if (k != 0)
          {
            if (!((ApplicationInfo)localObject1).isUpdatedSystemApp())
            {
              if ((enabledSetting != 0) && (enabledSetting != 4)) {
                break label239;
              }
              localObject2 = new java/lang/StringBuilder;
              ((StringBuilder)localObject2).<init>();
              ((StringBuilder)localObject2).append("Update state(");
              ((StringBuilder)localObject2).append(paramArraySet);
              ((StringBuilder)localObject2).append("): ENABLED for user ");
              ((StringBuilder)localObject2).append(paramInt);
              Slog.i("CarrierAppUtils", ((StringBuilder)localObject2).toString());
              paramIPackageManager.setApplicationEnabledSetting(paramArraySet, 1, 1, paramInt, paramString);
            }
            paramArraySet = (List)localMap.get(paramArraySet);
            localObject3 = paramArraySet;
            if (paramArraySet != null)
            {
              localObject2 = paramArraySet.iterator();
              label392:
              for (;;)
              {
                localObject3 = paramArraySet;
                if (!((Iterator)localObject2).hasNext()) {
                  break;
                }
                localObject3 = (ApplicationInfo)((Iterator)localObject2).next();
                if ((enabledSetting != 0) && (enabledSetting != 4)) {
                  break label392;
                }
                StringBuilder localStringBuilder = new java/lang/StringBuilder;
                localStringBuilder.<init>();
                localStringBuilder.append("Update associated state(");
                localStringBuilder.append(packageName);
                localStringBuilder.append("): ENABLED for user ");
                localStringBuilder.append(paramInt);
                Slog.i("CarrierAppUtils", localStringBuilder.toString());
                paramIPackageManager.setApplicationEnabledSetting(packageName, 1, 1, paramInt, paramString);
              }
            }
            localArrayList.add(packageName);
          }
          else
          {
            if ((!((ApplicationInfo)localObject1).isUpdatedSystemApp()) && (enabledSetting == 0))
            {
              localObject2 = new java/lang/StringBuilder;
              ((StringBuilder)localObject2).<init>();
              ((StringBuilder)localObject2).append("Update state(");
              ((StringBuilder)localObject2).append(paramArraySet);
              ((StringBuilder)localObject2).append("): DISABLED_UNTIL_USED for user ");
              ((StringBuilder)localObject2).append(paramInt);
              Slog.i("CarrierAppUtils", ((StringBuilder)localObject2).toString());
              paramIPackageManager.setApplicationEnabledSetting(paramArraySet, 4, 0, paramInt, paramString);
            }
            if (j == 0)
            {
              paramArraySet = (List)localMap.get(paramArraySet);
              if (paramArraySet != null)
              {
                localObject2 = paramArraySet.iterator();
                while (((Iterator)localObject2).hasNext())
                {
                  localObject3 = (ApplicationInfo)((Iterator)localObject2).next();
                  if (enabledSetting == 0)
                  {
                    localObject1 = new java/lang/StringBuilder;
                    ((StringBuilder)localObject1).<init>();
                    ((StringBuilder)localObject1).append("Update associated state(");
                    ((StringBuilder)localObject1).append(packageName);
                    ((StringBuilder)localObject1).append("): DISABLED_UNTIL_USED for user ");
                    ((StringBuilder)localObject1).append(paramInt);
                    Slog.i("CarrierAppUtils", ((StringBuilder)localObject1).toString());
                    paramIPackageManager.setApplicationEnabledSetting(packageName, 4, 0, paramInt, paramString);
                  }
                }
              }
            }
          }
        }
        if (j == 0) {
          Settings.Secure.putIntForUser(paramContentResolver, "carrier_apps_handled", 1, paramInt);
        }
        if (!localArrayList.isEmpty())
        {
          paramString = new String[localArrayList.size()];
          localArrayList.toArray(paramString);
          paramIPackageManager.grantDefaultPermissionsToEnabledCarrierApps(paramString, paramInt);
        }
      }
      catch (RemoteException paramString)
      {
        Slog.w("CarrierAppUtils", "Could not reach PackageManager", paramString);
      }
      return;
    }
  }
  
  private static ApplicationInfo getApplicationInfoIfSystemApp(IPackageManager paramIPackageManager, int paramInt, String paramString)
  {
    try
    {
      paramIPackageManager = paramIPackageManager.getApplicationInfo(paramString, 32768, paramInt);
      if (paramIPackageManager != null)
      {
        boolean bool = paramIPackageManager.isSystemApp();
        if (bool) {
          return paramIPackageManager;
        }
      }
    }
    catch (RemoteException paramIPackageManager)
    {
      Slog.w("CarrierAppUtils", "Could not reach PackageManager", paramIPackageManager);
    }
    return null;
  }
  
  public static List<ApplicationInfo> getDefaultCarrierAppCandidates(IPackageManager paramIPackageManager, int paramInt)
  {
    return getDefaultCarrierAppCandidatesHelper(paramIPackageManager, paramInt, SystemConfig.getInstance().getDisabledUntilUsedPreinstalledCarrierApps());
  }
  
  private static List<ApplicationInfo> getDefaultCarrierAppCandidatesHelper(IPackageManager paramIPackageManager, int paramInt, ArraySet<String> paramArraySet)
  {
    if (paramArraySet == null) {
      return null;
    }
    int i = paramArraySet.size();
    if (i == 0) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(i);
    for (int j = 0; j < i; j++)
    {
      Object localObject = (String)paramArraySet.valueAt(j);
      localObject = getApplicationInfoIfSystemApp(paramIPackageManager, paramInt, (String)localObject);
      if (localObject != null) {
        localArrayList.add(localObject);
      }
    }
    return localArrayList;
  }
  
  public static List<ApplicationInfo> getDefaultCarrierApps(IPackageManager paramIPackageManager, TelephonyManager paramTelephonyManager, int paramInt)
  {
    List localList = getDefaultCarrierAppCandidates(paramIPackageManager, paramInt);
    if ((localList != null) && (!localList.isEmpty()))
    {
      for (paramInt = localList.size() - 1; paramInt >= 0; paramInt--)
      {
        paramIPackageManager = getpackageName;
        int i;
        if (paramTelephonyManager.checkCarrierPrivilegesForPackageAnyPhone(paramIPackageManager) == 1) {
          i = 1;
        } else {
          i = 0;
        }
        if (i == 0) {
          localList.remove(paramInt);
        }
      }
      return localList;
    }
    return null;
  }
  
  private static Map<String, List<ApplicationInfo>> getDefaultCarrierAssociatedAppsHelper(IPackageManager paramIPackageManager, int paramInt, ArrayMap<String, List<String>> paramArrayMap)
  {
    int i = paramArrayMap.size();
    ArrayMap localArrayMap = new ArrayMap(i);
    for (int j = 0; j < i; j++)
    {
      String str = (String)paramArrayMap.keyAt(j);
      List localList1 = (List)paramArrayMap.valueAt(j);
      for (int k = 0; k < localList1.size(); k++)
      {
        ApplicationInfo localApplicationInfo = getApplicationInfoIfSystemApp(paramIPackageManager, paramInt, (String)localList1.get(k));
        if ((localApplicationInfo != null) && (!localApplicationInfo.isUpdatedSystemApp()))
        {
          List localList2 = (List)localArrayMap.get(str);
          Object localObject = localList2;
          if (localList2 == null)
          {
            localObject = new ArrayList();
            localArrayMap.put(str, localObject);
          }
          ((List)localObject).add(localApplicationInfo);
        }
      }
    }
    return localArrayMap;
  }
}
