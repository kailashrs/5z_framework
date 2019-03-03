package android.app;

import android.content.ComponentName;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.service.voice.IVoiceInteractionSession;
import android.util.SparseIntArray;
import com.android.internal.app.IVoiceInteractor;
import java.util.List;

public abstract class ActivityManagerInternal
{
  public static final int APP_TRANSITION_RECENTS_ANIM = 5;
  public static final int APP_TRANSITION_SNAPSHOT = 4;
  public static final int APP_TRANSITION_SPLASH_SCREEN = 1;
  public static final int APP_TRANSITION_TIMEOUT = 3;
  public static final int APP_TRANSITION_WINDOWS_DRAWN = 2;
  public static final String ASSIST_KEY_CONTENT = "content";
  public static final String ASSIST_KEY_DATA = "data";
  public static final String ASSIST_KEY_RECEIVER_EXTRAS = "receiverExtras";
  public static final String ASSIST_KEY_STRUCTURE = "structure";
  
  public ActivityManagerInternal() {}
  
  public abstract SleepToken acquireSleepToken(String paramString, int paramInt);
  
  public abstract boolean canStartMoreUsers();
  
  public abstract void cancelRecentsAnimation(boolean paramBoolean);
  
  public abstract String checkContentProviderAccess(String paramString, int paramInt);
  
  public abstract void clearSavedANRState();
  
  public abstract void enforceCallerIsRecentsOrHasPermission(String paramString1, String paramString2);
  
  public abstract ComponentName getHomeActivityForUser(int paramInt);
  
  public abstract int getMaxRunningUsers();
  
  public abstract List<ProcessMemoryState> getMemoryStateForProcesses();
  
  public abstract List<IBinder> getTopVisibleActivities();
  
  public abstract int getUidProcessState(int paramInt);
  
  public abstract void grantUriPermissionFromIntent(int paramInt1, String paramString, Intent paramIntent, int paramInt2);
  
  public abstract boolean hasRunningActivity(int paramInt, String paramString);
  
  public abstract boolean isCallerRecents(int paramInt);
  
  public abstract boolean isRecentsComponentHomeActivity(int paramInt);
  
  public abstract boolean isRuntimeRestarted();
  
  public abstract boolean isSystemReady();
  
  public abstract boolean isUidActive(int paramInt);
  
  public abstract void killForegroundAppsForUser(int paramInt);
  
  public abstract void notifyActiveVoiceInteractionServiceChanged(ComponentName paramComponentName);
  
  public abstract void notifyAppTransitionCancelled();
  
  public abstract void notifyAppTransitionFinished();
  
  public abstract void notifyAppTransitionStarting(SparseIntArray paramSparseIntArray, long paramLong);
  
  public abstract void notifyDockedStackMinimizedChanged(boolean paramBoolean);
  
  public abstract void notifyKeyguardFlagsChanged(Runnable paramRunnable);
  
  public abstract void notifyKeyguardTrustedChanged();
  
  public abstract void notifyNetworkPolicyRulesUpdated(int paramInt, long paramLong);
  
  public abstract void onLocalVoiceInteractionStarted(IBinder paramIBinder, IVoiceInteractionSession paramIVoiceInteractionSession, IVoiceInteractor paramIVoiceInteractor);
  
  public abstract void onUserRemoved(int paramInt);
  
  public abstract void onWakefulnessChanged(int paramInt);
  
  public abstract void registerScreenObserver(ScreenObserver paramScreenObserver);
  
  public abstract void saveANRState(String paramString);
  
  public abstract void setAllowAppSwitches(String paramString, int paramInt1, int paramInt2);
  
  public abstract void setDeviceIdleWhitelist(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
  
  public abstract void setFocusedActivity(IBinder paramIBinder);
  
  public abstract void setHasOverlayUi(int paramInt, boolean paramBoolean);
  
  public abstract void setPendingIntentWhitelistDuration(IIntentSender paramIIntentSender, IBinder paramIBinder, long paramLong);
  
  public abstract void setRunningRemoteAnimation(int paramInt, boolean paramBoolean);
  
  public abstract void setSwitchingFromSystemUserMessage(String paramString);
  
  public abstract void setSwitchingToSystemUserMessage(String paramString);
  
  public abstract void setVr2dDisplayId(int paramInt);
  
  public abstract int startActivitiesAsPackage(String paramString, int paramInt, Intent[] paramArrayOfIntent, Bundle paramBundle);
  
  public abstract int startActivityAsUser(IApplicationThread paramIApplicationThread, String paramString, Intent paramIntent, Bundle paramBundle, int paramInt);
  
  public abstract boolean startIsolatedProcess(String paramString1, String[] paramArrayOfString, String paramString2, String paramString3, int paramInt, Runnable paramRunnable);
  
  public abstract void updateDeviceIdleTempWhitelist(int[] paramArrayOfInt, int paramInt, boolean paramBoolean);
  
  public abstract void updatePersistentConfigurationForUser(Configuration paramConfiguration, int paramInt);
  
  public static abstract interface ScreenObserver
  {
    public abstract void onAwakeStateChanged(boolean paramBoolean);
    
    public abstract void onKeyguardStateChanged(boolean paramBoolean);
  }
  
  public static abstract class SleepToken
  {
    public SleepToken() {}
    
    public abstract void release();
  }
}
