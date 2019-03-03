package android.app.usage;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArrayMap;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class UsageStatsManager
{
  @SystemApi
  public static final String EXTRA_OBSERVER_ID = "android.app.usage.extra.OBSERVER_ID";
  @SystemApi
  public static final String EXTRA_TIME_LIMIT = "android.app.usage.extra.TIME_LIMIT";
  @SystemApi
  public static final String EXTRA_TIME_USED = "android.app.usage.extra.TIME_USED";
  public static final int INTERVAL_BEST = 4;
  public static final int INTERVAL_COUNT = 4;
  public static final int INTERVAL_DAILY = 0;
  public static final int INTERVAL_MONTHLY = 2;
  public static final int INTERVAL_WEEKLY = 1;
  public static final int INTERVAL_YEARLY = 3;
  public static final int REASON_MAIN_DEFAULT = 256;
  public static final int REASON_MAIN_FORCED = 1024;
  public static final int REASON_MAIN_MASK = 65280;
  public static final int REASON_MAIN_PREDICTED = 1280;
  public static final int REASON_MAIN_TIMEOUT = 512;
  public static final int REASON_MAIN_USAGE = 768;
  public static final int REASON_SUB_MASK = 255;
  public static final int REASON_SUB_PREDICTED_RESTORED = 1;
  public static final int REASON_SUB_USAGE_ACTIVE_TIMEOUT = 7;
  public static final int REASON_SUB_USAGE_EXEMPTED_SYNC_SCHEDULED_DOZE = 12;
  public static final int REASON_SUB_USAGE_EXEMPTED_SYNC_SCHEDULED_NON_DOZE = 11;
  public static final int REASON_SUB_USAGE_EXEMPTED_SYNC_START = 13;
  public static final int REASON_SUB_USAGE_MOVE_TO_BACKGROUND = 5;
  public static final int REASON_SUB_USAGE_MOVE_TO_FOREGROUND = 4;
  public static final int REASON_SUB_USAGE_NOTIFICATION_SEEN = 2;
  public static final int REASON_SUB_USAGE_SLICE_PINNED = 9;
  public static final int REASON_SUB_USAGE_SLICE_PINNED_PRIV = 10;
  public static final int REASON_SUB_USAGE_SYNC_ADAPTER = 8;
  public static final int REASON_SUB_USAGE_SYSTEM_INTERACTION = 1;
  public static final int REASON_SUB_USAGE_SYSTEM_UPDATE = 6;
  public static final int REASON_SUB_USAGE_USER_INTERACTION = 3;
  public static final int STANDBY_BUCKET_ACTIVE = 10;
  @SystemApi
  public static final int STANDBY_BUCKET_EXEMPTED = 5;
  public static final int STANDBY_BUCKET_FREQUENT = 30;
  @SystemApi
  public static final int STANDBY_BUCKET_NEVER = 50;
  public static final int STANDBY_BUCKET_RARE = 40;
  public static final int STANDBY_BUCKET_WORKING_SET = 20;
  private static final UsageEvents sEmptyResults = new UsageEvents();
  private final Context mContext;
  private final IUsageStatsManager mService;
  
  public UsageStatsManager(Context paramContext, IUsageStatsManager paramIUsageStatsManager)
  {
    mContext = paramContext;
    mService = paramIUsageStatsManager;
  }
  
  public static String reasonToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0xFF00 & paramInt;
    if (i != 256)
    {
      if (i != 512)
      {
        if (i != 768)
        {
          if (i != 1024)
          {
            if (i == 1280)
            {
              localStringBuilder.append("p");
              if ((paramInt & 0xFF) == 1) {
                localStringBuilder.append("-r");
              }
            }
          }
          else {
            localStringBuilder.append("f");
          }
        }
        else
        {
          localStringBuilder.append("u");
          switch (paramInt & 0xFF)
          {
          default: 
            break;
          case 13: 
            localStringBuilder.append("-es");
            break;
          case 12: 
            localStringBuilder.append("-ed");
            break;
          case 11: 
            localStringBuilder.append("-en");
            break;
          case 10: 
            localStringBuilder.append("-lv");
            break;
          case 9: 
            localStringBuilder.append("-lp");
            break;
          case 8: 
            localStringBuilder.append("-sa");
            break;
          case 7: 
            localStringBuilder.append("-at");
            break;
          case 6: 
            localStringBuilder.append("-su");
            break;
          case 5: 
            localStringBuilder.append("-mb");
            break;
          case 4: 
            localStringBuilder.append("-mf");
            break;
          case 3: 
            localStringBuilder.append("-ui");
            break;
          case 2: 
            localStringBuilder.append("-ns");
            break;
          case 1: 
            localStringBuilder.append("-si");
            break;
          }
        }
      }
      else {
        localStringBuilder.append("t");
      }
    }
    else {
      localStringBuilder.append("d");
    }
    return localStringBuilder.toString();
  }
  
  public int getAppStandbyBucket()
  {
    try
    {
      int i = mService.getAppStandbyBucket(mContext.getOpPackageName(), mContext.getOpPackageName(), mContext.getUserId());
      return i;
    }
    catch (RemoteException localRemoteException) {}
    return 10;
  }
  
  @SystemApi
  public int getAppStandbyBucket(String paramString)
  {
    try
    {
      int i = mService.getAppStandbyBucket(paramString, mContext.getOpPackageName(), mContext.getUserId());
      return i;
    }
    catch (RemoteException paramString) {}
    return 10;
  }
  
  @SystemApi
  public Map<String, Integer> getAppStandbyBuckets()
  {
    try
    {
      List localList = mService.getAppStandbyBuckets(mContext.getOpPackageName(), mContext.getUserId()).getList();
      ArrayMap localArrayMap = new android/util/ArrayMap;
      localArrayMap.<init>();
      int i = localList.size();
      for (int j = 0; j < i; j++)
      {
        AppStandbyInfo localAppStandbyInfo = (AppStandbyInfo)localList.get(j);
        localArrayMap.put(mPackageName, Integer.valueOf(mStandbyBucket));
      }
      return localArrayMap;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isAppInactive(String paramString)
  {
    try
    {
      boolean bool = mService.isAppInactive(paramString, mContext.getUserId());
      return bool;
    }
    catch (RemoteException paramString) {}
    return false;
  }
  
  public void onCarrierPrivilegedAppsChanged()
  {
    try
    {
      mService.onCarrierPrivilegedAppsChanged();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Map<String, UsageStats> queryAndAggregateUsageStats(long paramLong1, long paramLong2)
  {
    List localList = queryUsageStats(4, paramLong1, paramLong2);
    if (localList.isEmpty()) {
      return Collections.emptyMap();
    }
    ArrayMap localArrayMap = new ArrayMap();
    int i = localList.size();
    for (int j = 0; j < i; j++)
    {
      UsageStats localUsageStats1 = (UsageStats)localList.get(j);
      UsageStats localUsageStats2 = (UsageStats)localArrayMap.get(localUsageStats1.getPackageName());
      if (localUsageStats2 == null) {
        localArrayMap.put(mPackageName, localUsageStats1);
      } else {
        localUsageStats2.add(localUsageStats1);
      }
    }
    return localArrayMap;
  }
  
  public Map<String, UsageStats> queryAndAggregateUsageStatsAsUser(long paramLong1, long paramLong2, int paramInt)
  {
    List localList = queryUsageStatsAsUser(4, paramLong1, paramLong2, paramInt);
    if (localList.isEmpty()) {
      return Collections.emptyMap();
    }
    ArrayMap localArrayMap = new ArrayMap();
    int i = localList.size();
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      UsageStats localUsageStats1 = (UsageStats)localList.get(paramInt);
      UsageStats localUsageStats2 = (UsageStats)localArrayMap.get(localUsageStats1.getPackageName());
      if (localUsageStats2 == null) {
        localArrayMap.put(mPackageName, localUsageStats1);
      } else {
        localUsageStats2.add(localUsageStats1);
      }
    }
    return localArrayMap;
  }
  
  public List<ConfigurationStats> queryConfigurations(int paramInt, long paramLong1, long paramLong2)
  {
    try
    {
      Object localObject = mService.queryConfigurationStats(paramInt, paramLong1, paramLong2, mContext.getOpPackageName());
      if (localObject != null)
      {
        localObject = ((ParceledListSlice)localObject).getList();
        return localObject;
      }
    }
    catch (RemoteException localRemoteException) {}
    return Collections.emptyList();
  }
  
  public List<EventStats> queryEventStats(int paramInt, long paramLong1, long paramLong2)
  {
    try
    {
      Object localObject = mService.queryEventStats(paramInt, paramLong1, paramLong2, mContext.getOpPackageName());
      if (localObject != null)
      {
        localObject = ((ParceledListSlice)localObject).getList();
        return localObject;
      }
    }
    catch (RemoteException localRemoteException) {}
    return Collections.emptyList();
  }
  
  public UsageEvents queryEvents(long paramLong1, long paramLong2)
  {
    try
    {
      UsageEvents localUsageEvents = mService.queryEvents(paramLong1, paramLong2, mContext.getOpPackageName());
      if (localUsageEvents != null) {
        return localUsageEvents;
      }
    }
    catch (RemoteException localRemoteException) {}
    return sEmptyResults;
  }
  
  public UsageEvents queryEventsAsUser(long paramLong1, long paramLong2, int paramInt)
  {
    try
    {
      UsageEvents localUsageEvents = mService.queryEventsAsUser(paramLong1, paramLong2, mContext.getOpPackageName(), paramInt);
      if (localUsageEvents != null) {
        return localUsageEvents;
      }
    }
    catch (RemoteException localRemoteException) {}
    return sEmptyResults;
  }
  
  public UsageEvents queryEventsForSelf(long paramLong1, long paramLong2)
  {
    try
    {
      UsageEvents localUsageEvents = mService.queryEventsForPackage(paramLong1, paramLong2, mContext.getOpPackageName());
      if (localUsageEvents != null) {
        return localUsageEvents;
      }
    }
    catch (RemoteException localRemoteException) {}
    return sEmptyResults;
  }
  
  public List<UsageStats> queryUsageStats(int paramInt, long paramLong1, long paramLong2)
  {
    try
    {
      Object localObject = mService.queryUsageStats(paramInt, paramLong1, paramLong2, mContext.getOpPackageName());
      if (localObject != null)
      {
        localObject = ((ParceledListSlice)localObject).getList();
        return localObject;
      }
    }
    catch (RemoteException localRemoteException) {}
    return Collections.emptyList();
  }
  
  public List<UsageStats> queryUsageStatsAsUser(int paramInt1, long paramLong1, long paramLong2, int paramInt2)
  {
    try
    {
      Object localObject = mService.queryUsageStatsAsUser(paramInt1, paramLong1, paramLong2, mContext.getOpPackageName(), paramInt2);
      if (localObject != null)
      {
        localObject = ((ParceledListSlice)localObject).getList();
        return localObject;
      }
    }
    catch (RemoteException localRemoteException) {}
    return Collections.emptyList();
  }
  
  @SystemApi
  public void registerAppUsageObserver(int paramInt, String[] paramArrayOfString, long paramLong, TimeUnit paramTimeUnit, PendingIntent paramPendingIntent)
  {
    try
    {
      mService.registerAppUsageObserver(paramInt, paramArrayOfString, paramTimeUnit.toMillis(paramLong), paramPendingIntent, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramArrayOfString)
    {
      throw paramArrayOfString.rethrowFromSystemServer();
    }
  }
  
  public void reportChooserSelection(String paramString1, int paramInt, String paramString2, String[] paramArrayOfString, String paramString3)
  {
    try
    {
      mService.reportChooserSelection(paramString1, paramInt, paramString2, paramArrayOfString, paramString3);
    }
    catch (RemoteException paramString1) {}
  }
  
  public void setAppInactive(String paramString, boolean paramBoolean)
  {
    try
    {
      mService.setAppInactive(paramString, paramBoolean, mContext.getUserId());
    }
    catch (RemoteException paramString) {}
  }
  
  @SystemApi
  public void setAppStandbyBucket(String paramString, int paramInt)
  {
    try
    {
      mService.setAppStandbyBucket(paramString, paramInt, mContext.getUserId());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void setAppStandbyBuckets(Map<String, Integer> paramMap)
  {
    if (paramMap == null) {
      return;
    }
    ArrayList localArrayList = new ArrayList(paramMap.size());
    paramMap = paramMap.entrySet().iterator();
    while (paramMap.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramMap.next();
      localArrayList.add(new AppStandbyInfo((String)localEntry.getKey(), ((Integer)localEntry.getValue()).intValue()));
    }
    paramMap = new ParceledListSlice(localArrayList);
    try
    {
      mService.setAppStandbyBuckets(paramMap, mContext.getUserId());
      return;
    }
    catch (RemoteException paramMap)
    {
      throw paramMap.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void unregisterAppUsageObserver(int paramInt)
  {
    try
    {
      mService.unregisterAppUsageObserver(paramInt, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void whitelistAppTemporarily(String paramString, long paramLong, UserHandle paramUserHandle)
  {
    try
    {
      mService.whitelistAppTemporarily(paramString, paramLong, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface StandbyBuckets {}
}
