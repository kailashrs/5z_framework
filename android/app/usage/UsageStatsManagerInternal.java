package android.app.usage;

import android.content.ComponentName;
import android.content.res.Configuration;
import java.util.List;
import java.util.Set;

public abstract class UsageStatsManagerInternal
{
  public UsageStatsManagerInternal() {}
  
  public abstract void addAppIdleStateChangeListener(AppIdleStateChangeListener paramAppIdleStateChangeListener);
  
  public abstract void applyRestoredPayload(int paramInt, String paramString, byte[] paramArrayOfByte);
  
  public abstract int getAppStandbyBucket(String paramString, int paramInt, long paramLong);
  
  public abstract byte[] getBackupPayload(int paramInt, String paramString);
  
  public abstract int[] getIdleUidsForUser(int paramInt);
  
  public abstract long getTimeSinceLastJobRun(String paramString, int paramInt);
  
  public abstract boolean isAppIdle(String paramString, int paramInt1, int paramInt2);
  
  public abstract boolean isAppIdleParoleOn();
  
  public abstract void onActiveAdminAdded(String paramString, int paramInt);
  
  public abstract void onAdminDataAvailable();
  
  public abstract void prepareShutdown();
  
  public abstract List<UsageStats> queryUsageStatsForUser(int paramInt1, int paramInt2, long paramLong1, long paramLong2, boolean paramBoolean);
  
  public abstract void removeAppIdleStateChangeListener(AppIdleStateChangeListener paramAppIdleStateChangeListener);
  
  public abstract void reportAppJobState(String paramString, int paramInt1, int paramInt2, long paramLong);
  
  public abstract void reportConfigurationChange(Configuration paramConfiguration, int paramInt);
  
  public abstract void reportContentProviderUsage(String paramString1, String paramString2, int paramInt);
  
  public abstract void reportEvent(ComponentName paramComponentName, int paramInt1, int paramInt2);
  
  public abstract void reportEvent(String paramString, int paramInt1, int paramInt2);
  
  public abstract void reportExemptedSyncScheduled(String paramString, int paramInt);
  
  public abstract void reportExemptedSyncStart(String paramString, int paramInt);
  
  public abstract void reportInterruptiveNotification(String paramString1, String paramString2, int paramInt);
  
  public abstract void reportShortcutUsage(String paramString1, String paramString2, int paramInt);
  
  public abstract void setActiveAdminApps(Set<String> paramSet, int paramInt);
  
  public abstract void setLastJobRunTime(String paramString, int paramInt, long paramLong);
  
  public static abstract class AppIdleStateChangeListener
  {
    public AppIdleStateChangeListener() {}
    
    public abstract void onAppIdleStateChanged(String paramString, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3);
    
    public abstract void onParoleStateChanged(boolean paramBoolean);
    
    public void onUserInteractionStarted(String paramString, int paramInt) {}
  }
}
