package android.app;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.IIntentReceiver.Stub;
import android.content.IIntentSender;
import android.content.IIntentSender.Stub;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageDataObserver.Stub;
import android.content.pm.ParceledListSlice;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug.MemoryInfo;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IProgressListener;
import android.os.IProgressListener.Stub;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.StrictMode.ViolationInfo;
import android.os.WorkSource;
import android.service.voice.IVoiceInteractionSession;
import android.service.voice.IVoiceInteractionSession.Stub;
import android.text.TextUtils;
import android.view.IRecentsAnimationRunner;
import android.view.IRecentsAnimationRunner.Stub;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationDefinition;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.IVoiceInteractor.Stub;
import com.android.internal.os.IResultReceiver;
import com.android.internal.os.IResultReceiver.Stub;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IKeyguardDismissCallback.Stub;
import java.util.ArrayList;
import java.util.List;

public abstract interface IActivityManager
  extends IInterface
{
  public abstract void activityDestroyed(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void activityIdle(IBinder paramIBinder, Configuration paramConfiguration, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void activityPaused(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void activityRelaunched(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void activityResumed(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void activitySlept(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void activityStopped(IBinder paramIBinder, Bundle paramBundle, PersistableBundle paramPersistableBundle, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract int addAppTask(IBinder paramIBinder, Intent paramIntent, ActivityManager.TaskDescription paramTaskDescription, Bitmap paramBitmap)
    throws RemoteException;
  
  public abstract void addInstrumentationResults(IApplicationThread paramIApplicationThread, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void addPackageDependency(String paramString)
    throws RemoteException;
  
  public abstract void alwaysShowUnsupportedCompileSdkWarning(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void appNotRespondingViaProvider(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void attachApplication(IApplicationThread paramIApplicationThread, long paramLong)
    throws RemoteException;
  
  public abstract void backgroundWhitelistUid(int paramInt)
    throws RemoteException;
  
  public abstract void backupAgentCreated(String paramString, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean bindBackupAgent(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int bindService(IApplicationThread paramIApplicationThread, IBinder paramIBinder, Intent paramIntent, String paramString1, IServiceConnection paramIServiceConnection, int paramInt1, String paramString2, int paramInt2)
    throws RemoteException;
  
  public abstract void bootAnimationComplete()
    throws RemoteException;
  
  public abstract int broadcastIntent(IApplicationThread paramIApplicationThread, Intent paramIntent, String paramString1, IIntentReceiver paramIIntentReceiver, int paramInt1, String paramString2, Bundle paramBundle1, String[] paramArrayOfString, int paramInt2, Bundle paramBundle2, boolean paramBoolean1, boolean paramBoolean2, int paramInt3)
    throws RemoteException;
  
  public abstract void cancelIntentSender(IIntentSender paramIIntentSender)
    throws RemoteException;
  
  public abstract void cancelRecentsAnimation(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void cancelTaskWindowTransition(int paramInt)
    throws RemoteException;
  
  public abstract int checkGrantUriPermission(int paramInt1, String paramString, Uri paramUri, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract int checkPermission(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int checkPermissionWithToken(String paramString, int paramInt1, int paramInt2, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int checkUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3, int paramInt4, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean clearApplicationUserData(String paramString, boolean paramBoolean, IPackageDataObserver paramIPackageDataObserver, int paramInt)
    throws RemoteException;
  
  public abstract void clearGrantedUriPermissions(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void clearPendingBackup()
    throws RemoteException;
  
  public abstract void closeSystemDialogs(String paramString)
    throws RemoteException;
  
  public abstract boolean convertFromTranslucent(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean convertToTranslucent(IBinder paramIBinder, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void crashApplication(int paramInt1, int paramInt2, String paramString1, int paramInt3, String paramString2)
    throws RemoteException;
  
  public abstract int createStackOnDisplay(int paramInt)
    throws RemoteException;
  
  public abstract void dismissKeyguard(IBinder paramIBinder, IKeyguardDismissCallback paramIKeyguardDismissCallback, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void dismissPip(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void dismissSplitScreenMode(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean dumpHeap(String paramString1, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString2, ParcelFileDescriptor paramParcelFileDescriptor)
    throws RemoteException;
  
  public abstract void dumpHeapFinished(String paramString)
    throws RemoteException;
  
  public abstract boolean enterPictureInPictureMode(IBinder paramIBinder, PictureInPictureParams paramPictureInPictureParams)
    throws RemoteException;
  
  public abstract void enterSafeMode()
    throws RemoteException;
  
  public abstract void exitFreeformMode(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean finishActivity(IBinder paramIBinder, int paramInt1, Intent paramIntent, int paramInt2)
    throws RemoteException;
  
  public abstract boolean finishActivityAffinity(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void finishHeavyWeightApp()
    throws RemoteException;
  
  public abstract void finishInstrumentation(IApplicationThread paramIApplicationThread, int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void finishReceiver(IBinder paramIBinder, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean, int paramInt2)
    throws RemoteException;
  
  public abstract void finishSubActivity(IBinder paramIBinder, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void finishVoiceTask(IVoiceInteractionSession paramIVoiceInteractionSession)
    throws RemoteException;
  
  public abstract void forceStopNativeProcess(String paramString)
    throws RemoteException;
  
  public abstract void forceStopPackage(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ComponentName getActivityClassForToken(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int getActivityDisplayId(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract Bundle getActivityOptions(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract List<ActivityManager.StackInfo> getAllStackInfos()
    throws RemoteException;
  
  public abstract Point getAppTaskThumbnailSize()
    throws RemoteException;
  
  public abstract List<IBinder> getAppTasks(String paramString)
    throws RemoteException;
  
  public abstract Bundle getAssistContextExtras(int paramInt)
    throws RemoteException;
  
  public abstract ComponentName getCallingActivity(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract String getCallingPackage(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract Configuration getConfiguration()
    throws RemoteException;
  
  public abstract ContentProviderHolder getContentProvider(IApplicationThread paramIApplicationThread, String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract ContentProviderHolder getContentProviderExternal(String paramString, int paramInt, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract UserInfo getCurrentUser()
    throws RemoteException;
  
  public abstract ConfigurationInfo getDeviceConfigurationInfo()
    throws RemoteException;
  
  public abstract List<ActivityManager.RunningTaskInfo> getFilteredTasks(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract int getFocusedAppNotchUiMode()
    throws RemoteException;
  
  public abstract int getFocusedAppScaleMode()
    throws RemoteException;
  
  public abstract ActivityManager.StackInfo getFocusedStackInfo()
    throws RemoteException;
  
  public abstract int getFrontActivityScreenCompatMode()
    throws RemoteException;
  
  public abstract ParceledListSlice getGrantedUriPermissions(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract Intent getIntentForIntentSender(IIntentSender paramIIntentSender)
    throws RemoteException;
  
  public abstract IIntentSender getIntentSender(int paramInt1, String paramString1, IBinder paramIBinder, String paramString2, int paramInt2, Intent[] paramArrayOfIntent, String[] paramArrayOfString, int paramInt3, Bundle paramBundle, int paramInt4)
    throws RemoteException;
  
  public abstract int getLastResumedActivityUserId()
    throws RemoteException;
  
  public abstract String getLaunchedFromPackage(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int getLaunchedFromUid(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int getLockTaskModeState()
    throws RemoteException;
  
  public abstract int getMaxNumPictureInPictureActions(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void getMemoryInfo(ActivityManager.MemoryInfo paramMemoryInfo)
    throws RemoteException;
  
  public abstract int getMemoryTrimLevel()
    throws RemoteException;
  
  public abstract void getMyMemoryState(ActivityManager.RunningAppProcessInfo paramRunningAppProcessInfo)
    throws RemoteException;
  
  public abstract boolean getPackageAskScreenCompat(String paramString)
    throws RemoteException;
  
  public abstract String getPackageForIntentSender(IIntentSender paramIIntentSender)
    throws RemoteException;
  
  public abstract String getPackageForToken(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int getPackageProcessState(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract int getPackageScreenCompatMode(String paramString)
    throws RemoteException;
  
  public abstract ParceledListSlice getPersistedUriPermissions(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getProcessLimit()
    throws RemoteException;
  
  public abstract Debug.MemoryInfo[] getProcessMemoryInfo(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract long[] getProcessPss(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState()
    throws RemoteException;
  
  public abstract String getProviderMimeType(Uri paramUri, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getRecentTasks(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract int getRequestedOrientation(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses()
    throws RemoteException;
  
  public abstract List<ApplicationInfo> getRunningExternalApplications()
    throws RemoteException;
  
  public abstract PendingIntent getRunningServiceControlPanel(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract int[] getRunningUserIds()
    throws RemoteException;
  
  public abstract List<ActivityManager.RunningServiceInfo> getServices(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ActivityManager.StackInfo getStackInfo(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract String getTagForIntentSender(IIntentSender paramIIntentSender, String paramString)
    throws RemoteException;
  
  public abstract Rect getTaskBounds(int paramInt)
    throws RemoteException;
  
  public abstract ActivityManager.TaskDescription getTaskDescription(int paramInt)
    throws RemoteException;
  
  public abstract Bitmap getTaskDescriptionIcon(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int getTaskForActivity(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract ActivityManager.TaskSnapshot getTaskSnapshot(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract List<ActivityManager.RunningTaskInfo> getTasks(int paramInt)
    throws RemoteException;
  
  public abstract int getUidForIntentSender(IIntentSender paramIIntentSender)
    throws RemoteException;
  
  public abstract int getUidProcessState(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract IBinder getUriPermissionOwnerForActivity(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int getVisibleAppScaleMode(int paramInt)
    throws RemoteException;
  
  public abstract void grantUriPermission(IApplicationThread paramIApplicationThread, String paramString, Uri paramUri, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void grantUriPermissionFromOwner(IBinder paramIBinder, int paramInt1, String paramString, Uri paramUri, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void handleApplicationCrash(IBinder paramIBinder, ApplicationErrorReport.ParcelableCrashInfo paramParcelableCrashInfo)
    throws RemoteException;
  
  public abstract void handleApplicationStrictModeViolation(IBinder paramIBinder, int paramInt, StrictMode.ViolationInfo paramViolationInfo)
    throws RemoteException;
  
  public abstract boolean handleApplicationWtf(IBinder paramIBinder, String paramString, boolean paramBoolean, ApplicationErrorReport.ParcelableCrashInfo paramParcelableCrashInfo)
    throws RemoteException;
  
  public abstract int handleIncomingUser(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void hang(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract long inputDispatchingTimedOut(int paramInt, boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract boolean isAppForeground(int paramInt)
    throws RemoteException;
  
  public abstract boolean isAppStartModeDisabled(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isAssistDataAllowedOnCurrentActivity()
    throws RemoteException;
  
  public abstract boolean isBackgroundRestricted(String paramString)
    throws RemoteException;
  
  public abstract boolean isImmersive(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean isInLockTaskMode()
    throws RemoteException;
  
  public abstract boolean isInMultiWindowMode(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean isInPictureInPictureMode(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean isIntentSenderAForegroundService(IIntentSender paramIIntentSender)
    throws RemoteException;
  
  public abstract boolean isIntentSenderAnActivity(IIntentSender paramIIntentSender)
    throws RemoteException;
  
  public abstract boolean isIntentSenderTargetedToPackage(IIntentSender paramIIntentSender)
    throws RemoteException;
  
  public abstract boolean isRestrictStartActivity(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isRootVoiceInteraction(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean isTopActivityImmersive()
    throws RemoteException;
  
  public abstract boolean isTopOfTask(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean isUidActive(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isUserAMonkey()
    throws RemoteException;
  
  public abstract boolean isUserRunning(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean isVrModePackageEnabled(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void keyguardGoingAway(int paramInt)
    throws RemoteException;
  
  public abstract void killAllBackgroundProcesses()
    throws RemoteException;
  
  public abstract void killApplication(String paramString1, int paramInt1, int paramInt2, String paramString2)
    throws RemoteException;
  
  public abstract void killApplicationProcess(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void killBackgroundProcesses(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void killPackageDependents(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean killPids(int[] paramArrayOfInt, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean killProcessesBelowForeground(String paramString)
    throws RemoteException;
  
  public abstract void killUid(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract boolean launchAssistIntent(Intent paramIntent, int paramInt1, String paramString, int paramInt2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void makePackageIdle(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean moveActivityTaskToBack(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void moveStackToDisplay(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void moveTaskBackwards(int paramInt)
    throws RemoteException;
  
  public abstract void moveTaskToFront(int paramInt1, int paramInt2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void moveTaskToStack(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void moveTasksToFullscreenStack(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean moveTopActivityToPinnedStack(int paramInt, Rect paramRect)
    throws RemoteException;
  
  public abstract boolean navigateUpTo(IBinder paramIBinder, Intent paramIntent1, int paramInt, Intent paramIntent2)
    throws RemoteException;
  
  public abstract IBinder newUriPermissionOwner(String paramString)
    throws RemoteException;
  
  public abstract void noteAlarmFinish(IIntentSender paramIIntentSender, WorkSource paramWorkSource, int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void noteAlarmStart(IIntentSender paramIIntentSender, WorkSource paramWorkSource, int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void noteWakeupAlarm(IIntentSender paramIIntentSender, WorkSource paramWorkSource, int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void notifyActivityDrawn(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void notifyCleartextNetwork(int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void notifyEnterAnimationComplete(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void notifyLaunchTaskBehindComplete(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void notifyLockedProfile(int paramInt)
    throws RemoteException;
  
  public abstract void notifyPinnedStackAnimationEnded()
    throws RemoteException;
  
  public abstract void notifyPinnedStackAnimationStarted()
    throws RemoteException;
  
  public abstract ParcelFileDescriptor openContentUri(String paramString)
    throws RemoteException;
  
  public abstract void overridePendingTransition(IBinder paramIBinder, String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract IBinder peekService(Intent paramIntent, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void performIdleMaintenance()
    throws RemoteException;
  
  public abstract void positionTaskInStack(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract boolean profileControl(String paramString, int paramInt1, boolean paramBoolean, ProfilerInfo paramProfilerInfo, int paramInt2)
    throws RemoteException;
  
  public abstract void publishContentProviders(IApplicationThread paramIApplicationThread, List<ContentProviderHolder> paramList)
    throws RemoteException;
  
  public abstract void publishService(IBinder paramIBinder1, Intent paramIntent, IBinder paramIBinder2)
    throws RemoteException;
  
  public abstract boolean refContentProvider(IBinder paramIBinder, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void registerIntentSenderCancelListener(IIntentSender paramIIntentSender, IResultReceiver paramIResultReceiver)
    throws RemoteException;
  
  public abstract void registerProcessObserver(IProcessObserver paramIProcessObserver)
    throws RemoteException;
  
  public abstract Intent registerReceiver(IApplicationThread paramIApplicationThread, String paramString1, IIntentReceiver paramIIntentReceiver, IntentFilter paramIntentFilter, String paramString2, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void registerRemoteAnimationForNextActivityStart(String paramString, RemoteAnimationAdapter paramRemoteAnimationAdapter)
    throws RemoteException;
  
  public abstract void registerRemoteAnimations(IBinder paramIBinder, RemoteAnimationDefinition paramRemoteAnimationDefinition)
    throws RemoteException;
  
  public abstract void registerTaskStackListener(ITaskStackListener paramITaskStackListener)
    throws RemoteException;
  
  public abstract void registerUidObserver(IUidObserver paramIUidObserver, int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract void registerUserSwitchObserver(IUserSwitchObserver paramIUserSwitchObserver, String paramString)
    throws RemoteException;
  
  public abstract boolean releaseActivityInstance(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void releasePersistableUriPermission(Uri paramUri, int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void releaseSomeActivities(IApplicationThread paramIApplicationThread)
    throws RemoteException;
  
  public abstract void removeContentProvider(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void removeContentProviderExternal(String paramString, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void removeStack(int paramInt)
    throws RemoteException;
  
  public abstract void removeStacksInWindowingModes(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract void removeStacksWithActivityTypes(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract boolean removeTask(int paramInt)
    throws RemoteException;
  
  public abstract void reportActivityFullyDrawn(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void reportAssistContextExtras(IBinder paramIBinder, Bundle paramBundle, AssistStructure paramAssistStructure, AssistContent paramAssistContent, Uri paramUri)
    throws RemoteException;
  
  public abstract void reportSizeConfigurations(IBinder paramIBinder, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3)
    throws RemoteException;
  
  public abstract boolean requestAssistContextExtras(int paramInt, IAssistDataReceiver paramIAssistDataReceiver, Bundle paramBundle, IBinder paramIBinder, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract boolean requestAutofillData(IAssistDataReceiver paramIAssistDataReceiver, Bundle paramBundle, IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void requestBugReport(int paramInt)
    throws RemoteException;
  
  public abstract void requestFocusedAppFillNotchRegion(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void requestFocusedAppFitScreen(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void requestTelephonyBugReport(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void requestVisibleAppFitScreen(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void requestWifiBugReport(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void resizeDockedStack(Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, Rect paramRect5)
    throws RemoteException;
  
  public abstract void resizePinnedStack(Rect paramRect1, Rect paramRect2)
    throws RemoteException;
  
  public abstract void resizeStack(int paramInt1, Rect paramRect, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt2)
    throws RemoteException;
  
  public abstract void resizeTask(int paramInt1, Rect paramRect, int paramInt2)
    throws RemoteException;
  
  public abstract void restart()
    throws RemoteException;
  
  public abstract int restartUserInBackground(int paramInt)
    throws RemoteException;
  
  public abstract void restrictStartActivity(int paramInt, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void resumeAppSwitches()
    throws RemoteException;
  
  public abstract void revokeUriPermission(IApplicationThread paramIApplicationThread, String paramString, Uri paramUri, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void revokeUriPermissionFromOwner(IBinder paramIBinder, Uri paramUri, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void scheduleApplicationInfoChanged(List<String> paramList, int paramInt)
    throws RemoteException;
  
  public abstract void sendIdleJobTrigger()
    throws RemoteException;
  
  public abstract int sendIntentSender(IIntentSender paramIIntentSender, IBinder paramIBinder, int paramInt, Intent paramIntent, String paramString1, IIntentReceiver paramIIntentReceiver, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void serviceDoneExecuting(IBinder paramIBinder, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void setActivityController(IActivityController paramIActivityController, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setAgentApp(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setAlwaysFinish(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setDebugApp(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void setDisablePreviewScreenshots(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setDumpHeapDebugLimit(String paramString1, int paramInt, long paramLong, String paramString2)
    throws RemoteException;
  
  public abstract void setFocusedStack(int paramInt)
    throws RemoteException;
  
  public abstract void setFocusedTask(int paramInt)
    throws RemoteException;
  
  public abstract void setFrontActivityScreenCompatMode(int paramInt)
    throws RemoteException;
  
  public abstract void setHasTopUi(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setImmersive(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setLockScreenShown(boolean paramBoolean1, boolean paramBoolean2, int paramInt)
    throws RemoteException;
  
  public abstract void setPackageAskScreenCompat(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPackageScreenCompatMode(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setPersistentVrThread(int paramInt)
    throws RemoteException;
  
  public abstract void setPictureInPictureParams(IBinder paramIBinder, PictureInPictureParams paramPictureInPictureParams)
    throws RemoteException;
  
  public abstract void setProcessImportant(IBinder paramIBinder, int paramInt, boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract void setProcessLimit(int paramInt)
    throws RemoteException;
  
  public abstract boolean setProcessMemoryTrimLevel(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setRenderThread(int paramInt)
    throws RemoteException;
  
  public abstract void setRequestedOrientation(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void setServiceForeground(ComponentName paramComponentName, IBinder paramIBinder, int paramInt1, Notification paramNotification, int paramInt2)
    throws RemoteException;
  
  public abstract void setShowWhenLocked(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSplitScreenResizing(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setTaskDescription(IBinder paramIBinder, ActivityManager.TaskDescription paramTaskDescription)
    throws RemoteException;
  
  public abstract void setTaskResizeable(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setTaskWindowingMode(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setTaskWindowingModeSplitScreenPrimary(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, Rect paramRect, boolean paramBoolean3)
    throws RemoteException;
  
  public abstract void setTurnScreenOn(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setUserIsMonkey(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setVoiceKeepAwake(IVoiceInteractionSession paramIVoiceInteractionSession, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int setVrMode(IBinder paramIBinder, boolean paramBoolean, ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void setVrThread(int paramInt)
    throws RemoteException;
  
  public abstract boolean shouldUpRecreateTask(IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract boolean showAssistFromActivity(IBinder paramIBinder, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void showBootMessage(CharSequence paramCharSequence, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void showLockTaskEscapeMessage(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void showWaitingForDebugger(IApplicationThread paramIApplicationThread, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean shutdown(int paramInt)
    throws RemoteException;
  
  public abstract void signalPersistentProcesses(int paramInt)
    throws RemoteException;
  
  public abstract int startActivities(IApplicationThread paramIApplicationThread, String paramString, Intent[] paramArrayOfIntent, String[] paramArrayOfString, IBinder paramIBinder, Bundle paramBundle, int paramInt)
    throws RemoteException;
  
  public abstract int startActivity(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, ProfilerInfo paramProfilerInfo, Bundle paramBundle)
    throws RemoteException;
  
  public abstract WaitResult startActivityAndWait(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, ProfilerInfo paramProfilerInfo, Bundle paramBundle, int paramInt3)
    throws RemoteException;
  
  public abstract int startActivityAsCaller(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, ProfilerInfo paramProfilerInfo, Bundle paramBundle, boolean paramBoolean, int paramInt3)
    throws RemoteException;
  
  public abstract int startActivityAsUser(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, ProfilerInfo paramProfilerInfo, Bundle paramBundle, int paramInt3)
    throws RemoteException;
  
  public abstract int startActivityFromRecents(int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract int startActivityIntentSender(IApplicationThread paramIApplicationThread, IIntentSender paramIIntentSender, IBinder paramIBinder1, Intent paramIntent, String paramString1, IBinder paramIBinder2, String paramString2, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle)
    throws RemoteException;
  
  public abstract int startActivityWithConfig(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, Configuration paramConfiguration, Bundle paramBundle, int paramInt3)
    throws RemoteException;
  
  public abstract int startAssistantActivity(String paramString1, int paramInt1, int paramInt2, Intent paramIntent, String paramString2, Bundle paramBundle, int paramInt3)
    throws RemoteException;
  
  public abstract boolean startBinderTracking()
    throws RemoteException;
  
  public abstract void startConfirmDeviceCredentialIntent(Intent paramIntent, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void startInPlaceAnimationOnFrontMostApplication(Bundle paramBundle)
    throws RemoteException;
  
  public abstract boolean startInstrumentation(ComponentName paramComponentName, String paramString1, int paramInt1, Bundle paramBundle, IInstrumentationWatcher paramIInstrumentationWatcher, IUiAutomationConnection paramIUiAutomationConnection, int paramInt2, String paramString2)
    throws RemoteException;
  
  public abstract void startLocalVoiceInteraction(IBinder paramIBinder, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void startLockTaskModeByToken(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean startNextMatchingActivity(IBinder paramIBinder, Intent paramIntent, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void startRecentsActivity(Intent paramIntent, IAssistDataReceiver paramIAssistDataReceiver, IRecentsAnimationRunner paramIRecentsAnimationRunner)
    throws RemoteException;
  
  public abstract ComponentName startService(IApplicationThread paramIApplicationThread, Intent paramIntent, String paramString1, boolean paramBoolean, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract void startSystemLockTaskMode(int paramInt)
    throws RemoteException;
  
  public abstract boolean startUserInBackground(int paramInt)
    throws RemoteException;
  
  public abstract boolean startUserInBackgroundWithListener(int paramInt, IProgressListener paramIProgressListener)
    throws RemoteException;
  
  public abstract int startVoiceActivity(String paramString1, int paramInt1, int paramInt2, Intent paramIntent, String paramString2, IVoiceInteractionSession paramIVoiceInteractionSession, IVoiceInteractor paramIVoiceInteractor, int paramInt3, ProfilerInfo paramProfilerInfo, Bundle paramBundle, int paramInt4)
    throws RemoteException;
  
  public abstract void stopAppSwitches()
    throws RemoteException;
  
  public abstract boolean stopBinderTrackingAndDump(ParcelFileDescriptor paramParcelFileDescriptor)
    throws RemoteException;
  
  public abstract void stopLocalVoiceInteraction(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void stopLockTaskModeByToken(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int stopService(IApplicationThread paramIApplicationThread, Intent paramIntent, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean stopServiceToken(ComponentName paramComponentName, IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void stopSystemLockTaskMode()
    throws RemoteException;
  
  public abstract int stopUser(int paramInt, boolean paramBoolean, IStopUserCallback paramIStopUserCallback)
    throws RemoteException;
  
  public abstract boolean supportsLocalVoiceInteraction()
    throws RemoteException;
  
  public abstract void suppressResizeConfigChanges(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean switchUser(int paramInt)
    throws RemoteException;
  
  public abstract void takePersistableUriPermission(Uri paramUri, int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void unbindBackupAgent(ApplicationInfo paramApplicationInfo)
    throws RemoteException;
  
  public abstract void unbindFinished(IBinder paramIBinder, Intent paramIntent, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean unbindService(IServiceConnection paramIServiceConnection)
    throws RemoteException;
  
  public abstract void unbroadcastIntent(IApplicationThread paramIApplicationThread, Intent paramIntent, int paramInt)
    throws RemoteException;
  
  public abstract void unhandledBack()
    throws RemoteException;
  
  public abstract boolean unlockUser(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, IProgressListener paramIProgressListener)
    throws RemoteException;
  
  public abstract void unregisterIntentSenderCancelListener(IIntentSender paramIIntentSender, IResultReceiver paramIResultReceiver)
    throws RemoteException;
  
  public abstract void unregisterProcessObserver(IProcessObserver paramIProcessObserver)
    throws RemoteException;
  
  public abstract void unregisterReceiver(IIntentReceiver paramIIntentReceiver)
    throws RemoteException;
  
  public abstract void unregisterTaskStackListener(ITaskStackListener paramITaskStackListener)
    throws RemoteException;
  
  public abstract void unregisterUidObserver(IUidObserver paramIUidObserver)
    throws RemoteException;
  
  public abstract void unregisterUserSwitchObserver(IUserSwitchObserver paramIUserSwitchObserver)
    throws RemoteException;
  
  public abstract void unstableProviderDied(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean updateConfiguration(Configuration paramConfiguration)
    throws RemoteException;
  
  public abstract void updateDeviceOwner(String paramString)
    throws RemoteException;
  
  public abstract boolean updateDisplayOverrideConfiguration(Configuration paramConfiguration, int paramInt)
    throws RemoteException;
  
  public abstract void updateLockTaskFeatures(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void updateLockTaskPackages(int paramInt, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void updatePersistentConfiguration(Configuration paramConfiguration)
    throws RemoteException;
  
  public abstract void waitForNetworkStateUpdate(long paramLong)
    throws RemoteException;
  
  public abstract boolean willActivityBeVisible(IBinder paramIBinder)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IActivityManager
  {
    private static final String DESCRIPTOR = "android.app.IActivityManager";
    static final int TRANSACTION_activityDestroyed = 58;
    static final int TRANSACTION_activityIdle = 15;
    static final int TRANSACTION_activityPaused = 16;
    static final int TRANSACTION_activityRelaunched = 258;
    static final int TRANSACTION_activityResumed = 35;
    static final int TRANSACTION_activitySlept = 121;
    static final int TRANSACTION_activityStopped = 17;
    static final int TRANSACTION_addAppTask = 208;
    static final int TRANSACTION_addInstrumentationResults = 40;
    static final int TRANSACTION_addPackageDependency = 93;
    static final int TRANSACTION_alwaysShowUnsupportedCompileSdkWarning = 305;
    static final int TRANSACTION_appNotRespondingViaProvider = 184;
    static final int TRANSACTION_attachApplication = 14;
    static final int TRANSACTION_backgroundWhitelistUid = 298;
    static final int TRANSACTION_backupAgentCreated = 89;
    static final int TRANSACTION_bindBackupAgent = 88;
    static final int TRANSACTION_bindService = 32;
    static final int TRANSACTION_bootAnimationComplete = 212;
    static final int TRANSACTION_broadcastIntent = 11;
    static final int TRANSACTION_cancelIntentSender = 60;
    static final int TRANSACTION_cancelRecentsAnimation = 197;
    static final int TRANSACTION_cancelTaskWindowTransition = 291;
    static final int TRANSACTION_checkGrantUriPermission = 117;
    static final int TRANSACTION_checkPermission = 49;
    static final int TRANSACTION_checkPermissionWithToken = 216;
    static final int TRANSACTION_checkUriPermission = 50;
    static final int TRANSACTION_clearApplicationUserData = 76;
    static final int TRANSACTION_clearGrantedUriPermissions = 264;
    static final int TRANSACTION_clearPendingBackup = 160;
    static final int TRANSACTION_closeSystemDialogs = 95;
    static final int TRANSACTION_convertFromTranslucent = 175;
    static final int TRANSACTION_convertToTranslucent = 176;
    static final int TRANSACTION_crashApplication = 112;
    static final int TRANSACTION_createStackOnDisplay = 220;
    static final int TRANSACTION_dismissKeyguard = 289;
    static final int TRANSACTION_dismissPip = 246;
    static final int TRANSACTION_dismissSplitScreenMode = 245;
    static final int TRANSACTION_dumpHeap = 118;
    static final int TRANSACTION_dumpHeapFinished = 226;
    static final int TRANSACTION_enterPictureInPictureMode = 255;
    static final int TRANSACTION_enterSafeMode = 64;
    static final int TRANSACTION_exitFreeformMode = 242;
    static final int TRANSACTION_finishActivity = 8;
    static final int TRANSACTION_finishActivityAffinity = 146;
    static final int TRANSACTION_finishHeavyWeightApp = 107;
    static final int TRANSACTION_finishInstrumentation = 41;
    static final int TRANSACTION_finishReceiver = 13;
    static final int TRANSACTION_finishSubActivity = 28;
    static final int TRANSACTION_finishVoiceTask = 203;
    static final int TRANSACTION_forceStopNativeProcess = 306;
    static final int TRANSACTION_forceStopPackage = 77;
    static final int TRANSACTION_getActivityClassForToken = 45;
    static final int TRANSACTION_getActivityDisplayId = 186;
    static final int TRANSACTION_getActivityOptions = 199;
    static final int TRANSACTION_getAllStackInfos = 171;
    static final int TRANSACTION_getAppTaskThumbnailSize = 209;
    static final int TRANSACTION_getAppTasks = 200;
    static final int TRANSACTION_getAssistContextExtras = 162;
    static final int TRANSACTION_getCallingActivity = 19;
    static final int TRANSACTION_getCallingPackage = 18;
    static final int TRANSACTION_getConfiguration = 42;
    static final int TRANSACTION_getContentProvider = 25;
    static final int TRANSACTION_getContentProviderExternal = 138;
    static final int TRANSACTION_getCurrentUser = 142;
    static final int TRANSACTION_getDeviceConfigurationInfo = 82;
    static final int TRANSACTION_getFilteredTasks = 21;
    static final int TRANSACTION_getFocusedAppNotchUiMode = 313;
    static final int TRANSACTION_getFocusedAppScaleMode = 309;
    static final int TRANSACTION_getFocusedStackInfo = 173;
    static final int TRANSACTION_getFrontActivityScreenCompatMode = 122;
    static final int TRANSACTION_getGrantedUriPermissions = 263;
    static final int TRANSACTION_getIntentForIntentSender = 161;
    static final int TRANSACTION_getIntentSender = 59;
    static final int TRANSACTION_getLastResumedActivityUserId = 297;
    static final int TRANSACTION_getLaunchedFromPackage = 164;
    static final int TRANSACTION_getLaunchedFromUid = 147;
    static final int TRANSACTION_getLockTaskModeState = 224;
    static final int TRANSACTION_getMaxNumPictureInPictureActions = 257;
    static final int TRANSACTION_getMemoryInfo = 74;
    static final int TRANSACTION_getMemoryTrimLevel = 275;
    static final int TRANSACTION_getMyMemoryState = 140;
    static final int TRANSACTION_getPackageAskScreenCompat = 126;
    static final int TRANSACTION_getPackageForIntentSender = 61;
    static final int TRANSACTION_getPackageForToken = 46;
    static final int TRANSACTION_getPackageProcessState = 231;
    static final int TRANSACTION_getPackageScreenCompatMode = 124;
    static final int TRANSACTION_getPersistedUriPermissions = 183;
    static final int TRANSACTION_getProcessLimit = 48;
    static final int TRANSACTION_getProcessMemoryInfo = 96;
    static final int TRANSACTION_getProcessPss = 135;
    static final int TRANSACTION_getProcessesInErrorState = 75;
    static final int TRANSACTION_getProviderMimeType = 113;
    static final int TRANSACTION_getRecentTasks = 56;
    static final int TRANSACTION_getRequestedOrientation = 69;
    static final int TRANSACTION_getRunningAppProcesses = 81;
    static final int TRANSACTION_getRunningExternalApplications = 106;
    static final int TRANSACTION_getRunningServiceControlPanel = 29;
    static final int TRANSACTION_getRunningUserIds = 155;
    static final int TRANSACTION_getServices = 79;
    static final int TRANSACTION_getStackInfo = 174;
    static final int TRANSACTION_getTagForIntentSender = 188;
    static final int TRANSACTION_getTaskBounds = 185;
    static final int TRANSACTION_getTaskDescription = 80;
    static final int TRANSACTION_getTaskDescriptionIcon = 213;
    static final int TRANSACTION_getTaskForActivity = 24;
    static final int TRANSACTION_getTaskSnapshot = 292;
    static final int TRANSACTION_getTasks = 20;
    static final int TRANSACTION_getUidForIntentSender = 91;
    static final int TRANSACTION_getUidProcessState = 235;
    static final int TRANSACTION_getUriPermissionOwnerForActivity = 259;
    static final int TRANSACTION_getVisibleAppScaleMode = 310;
    static final int TRANSACTION_grantUriPermission = 51;
    static final int TRANSACTION_grantUriPermissionFromOwner = 115;
    static final int TRANSACTION_handleApplicationCrash = 5;
    static final int TRANSACTION_handleApplicationStrictModeViolation = 108;
    static final int TRANSACTION_handleApplicationWtf = 100;
    static final int TRANSACTION_handleIncomingUser = 92;
    static final int TRANSACTION_hang = 167;
    static final int TRANSACTION_inputDispatchingTimedOut = 159;
    static final int TRANSACTION_isAppForeground = 265;
    static final int TRANSACTION_isAppStartModeDisabled = 250;
    static final int TRANSACTION_isAssistDataAllowedOnCurrentActivity = 236;
    static final int TRANSACTION_isBackgroundRestricted = 282;
    static final int TRANSACTION_isImmersive = 109;
    static final int TRANSACTION_isInLockTaskMode = 192;
    static final int TRANSACTION_isInMultiWindowMode = 252;
    static final int TRANSACTION_isInPictureInPictureMode = 253;
    static final int TRANSACTION_isIntentSenderAForegroundService = 150;
    static final int TRANSACTION_isIntentSenderAnActivity = 149;
    static final int TRANSACTION_isIntentSenderTargetedToPackage = 133;
    static final int TRANSACTION_isRestrictStartActivity = 308;
    static final int TRANSACTION_isRootVoiceInteraction = 238;
    static final int TRANSACTION_isTopActivityImmersive = 111;
    static final int TRANSACTION_isTopOfTask = 204;
    static final int TRANSACTION_isUidActive = 4;
    static final int TRANSACTION_isUserAMonkey = 102;
    static final int TRANSACTION_isUserRunning = 120;
    static final int TRANSACTION_isVrModePackageEnabled = 277;
    static final int TRANSACTION_keyguardGoingAway = 234;
    static final int TRANSACTION_killAllBackgroundProcesses = 137;
    static final int TRANSACTION_killApplication = 94;
    static final int TRANSACTION_killApplicationProcess = 97;
    static final int TRANSACTION_killBackgroundProcesses = 101;
    static final int TRANSACTION_killPackageDependents = 254;
    static final int TRANSACTION_killPids = 78;
    static final int TRANSACTION_killProcessesBelowForeground = 141;
    static final int TRANSACTION_killUid = 165;
    static final int TRANSACTION_launchAssistIntent = 214;
    static final int TRANSACTION_makePackageIdle = 274;
    static final int TRANSACTION_moveActivityTaskToBack = 73;
    static final int TRANSACTION_moveStackToDisplay = 287;
    static final int TRANSACTION_moveTaskBackwards = 23;
    static final int TRANSACTION_moveTaskToFront = 22;
    static final int TRANSACTION_moveTaskToStack = 169;
    static final int TRANSACTION_moveTasksToFullscreenStack = 248;
    static final int TRANSACTION_moveTopActivityToPinnedStack = 249;
    static final int TRANSACTION_navigateUpTo = 144;
    static final int TRANSACTION_newUriPermissionOwner = 114;
    static final int TRANSACTION_noteAlarmFinish = 230;
    static final int TRANSACTION_noteAlarmStart = 229;
    static final int TRANSACTION_noteWakeupAlarm = 66;
    static final int TRANSACTION_notifyActivityDrawn = 177;
    static final int TRANSACTION_notifyCleartextNetwork = 219;
    static final int TRANSACTION_notifyEnterAnimationComplete = 206;
    static final int TRANSACTION_notifyLaunchTaskBehindComplete = 205;
    static final int TRANSACTION_notifyLockedProfile = 278;
    static final int TRANSACTION_notifyPinnedStackAnimationEnded = 270;
    static final int TRANSACTION_notifyPinnedStackAnimationStarted = 269;
    static final int TRANSACTION_openContentUri = 1;
    static final int TRANSACTION_overridePendingTransition = 99;
    static final int TRANSACTION_peekService = 83;
    static final int TRANSACTION_performIdleMaintenance = 180;
    static final int TRANSACTION_positionTaskInStack = 241;
    static final int TRANSACTION_profileControl = 84;
    static final int TRANSACTION_publishContentProviders = 26;
    static final int TRANSACTION_publishService = 34;
    static final int TRANSACTION_refContentProvider = 27;
    static final int TRANSACTION_registerIntentSenderCancelListener = 62;
    static final int TRANSACTION_registerProcessObserver = 131;
    static final int TRANSACTION_registerReceiver = 9;
    static final int TRANSACTION_registerRemoteAnimationForNextActivityStart = 304;
    static final int TRANSACTION_registerRemoteAnimations = 303;
    static final int TRANSACTION_registerTaskStackListener = 217;
    static final int TRANSACTION_registerUidObserver = 2;
    static final int TRANSACTION_registerUserSwitchObserver = 153;
    static final int TRANSACTION_releaseActivityInstance = 210;
    static final int TRANSACTION_releasePersistableUriPermission = 182;
    static final int TRANSACTION_releaseSomeActivities = 211;
    static final int TRANSACTION_removeContentProvider = 67;
    static final int TRANSACTION_removeContentProviderExternal = 139;
    static final int TRANSACTION_removeStack = 271;
    static final int TRANSACTION_removeStacksInWindowingModes = 272;
    static final int TRANSACTION_removeStacksWithActivityTypes = 273;
    static final int TRANSACTION_removeTask = 130;
    static final int TRANSACTION_reportActivityFullyDrawn = 178;
    static final int TRANSACTION_reportAssistContextExtras = 163;
    static final int TRANSACTION_reportSizeConfigurations = 243;
    static final int TRANSACTION_requestAssistContextExtras = 222;
    static final int TRANSACTION_requestAutofillData = 288;
    static final int TRANSACTION_requestBugReport = 156;
    static final int TRANSACTION_requestFocusedAppFillNotchRegion = 314;
    static final int TRANSACTION_requestFocusedAppFitScreen = 311;
    static final int TRANSACTION_requestTelephonyBugReport = 157;
    static final int TRANSACTION_requestVisibleAppFitScreen = 312;
    static final int TRANSACTION_requestWifiBugReport = 158;
    static final int TRANSACTION_resizeDockedStack = 260;
    static final int TRANSACTION_resizePinnedStack = 276;
    static final int TRANSACTION_resizeStack = 170;
    static final int TRANSACTION_resizeTask = 223;
    static final int TRANSACTION_restart = 179;
    static final int TRANSACTION_restartUserInBackground = 290;
    static final int TRANSACTION_restrictStartActivity = 307;
    static final int TRANSACTION_resumeAppSwitches = 87;
    static final int TRANSACTION_revokeUriPermission = 52;
    static final int TRANSACTION_revokeUriPermissionFromOwner = 116;
    static final int TRANSACTION_scheduleApplicationInfoChanged = 293;
    static final int TRANSACTION_sendIdleJobTrigger = 280;
    static final int TRANSACTION_sendIntentSender = 281;
    static final int TRANSACTION_serviceDoneExecuting = 57;
    static final int TRANSACTION_setActivityController = 53;
    static final int TRANSACTION_setAgentApp = 37;
    static final int TRANSACTION_setAlwaysFinish = 38;
    static final int TRANSACTION_setDebugApp = 36;
    static final int TRANSACTION_setDisablePreviewScreenshots = 296;
    static final int TRANSACTION_setDumpHeapDebugLimit = 225;
    static final int TRANSACTION_setFocusedStack = 172;
    static final int TRANSACTION_setFocusedTask = 129;
    static final int TRANSACTION_setFrontActivityScreenCompatMode = 123;
    static final int TRANSACTION_setHasTopUi = 285;
    static final int TRANSACTION_setImmersive = 110;
    static final int TRANSACTION_setLockScreenShown = 145;
    static final int TRANSACTION_setPackageAskScreenCompat = 127;
    static final int TRANSACTION_setPackageScreenCompatMode = 125;
    static final int TRANSACTION_setPersistentVrThread = 294;
    static final int TRANSACTION_setPictureInPictureParams = 256;
    static final int TRANSACTION_setProcessImportant = 71;
    static final int TRANSACTION_setProcessLimit = 47;
    static final int TRANSACTION_setProcessMemoryTrimLevel = 187;
    static final int TRANSACTION_setRenderThread = 284;
    static final int TRANSACTION_setRequestedOrientation = 68;
    static final int TRANSACTION_setServiceForeground = 72;
    static final int TRANSACTION_setShowWhenLocked = 300;
    static final int TRANSACTION_setSplitScreenResizing = 261;
    static final int TRANSACTION_setTaskDescription = 193;
    static final int TRANSACTION_setTaskResizeable = 221;
    static final int TRANSACTION_setTaskWindowingMode = 168;
    static final int TRANSACTION_setTaskWindowingModeSplitScreenPrimary = 244;
    static final int TRANSACTION_setTurnScreenOn = 301;
    static final int TRANSACTION_setUserIsMonkey = 166;
    static final int TRANSACTION_setVoiceKeepAwake = 227;
    static final int TRANSACTION_setVrMode = 262;
    static final int TRANSACTION_setVrThread = 283;
    static final int TRANSACTION_shouldUpRecreateTask = 143;
    static final int TRANSACTION_showAssistFromActivity = 237;
    static final int TRANSACTION_showBootMessage = 136;
    static final int TRANSACTION_showLockTaskEscapeMessage = 232;
    static final int TRANSACTION_showWaitingForDebugger = 54;
    static final int TRANSACTION_shutdown = 85;
    static final int TRANSACTION_signalPersistentProcesses = 55;
    static final int TRANSACTION_startActivities = 119;
    static final int TRANSACTION_startActivity = 6;
    static final int TRANSACTION_startActivityAndWait = 103;
    static final int TRANSACTION_startActivityAsCaller = 207;
    static final int TRANSACTION_startActivityAsUser = 151;
    static final int TRANSACTION_startActivityFromRecents = 198;
    static final int TRANSACTION_startActivityIntentSender = 98;
    static final int TRANSACTION_startActivityWithConfig = 105;
    static final int TRANSACTION_startAssistantActivity = 195;
    static final int TRANSACTION_startBinderTracking = 239;
    static final int TRANSACTION_startConfirmDeviceCredentialIntent = 279;
    static final int TRANSACTION_startInPlaceAnimationOnFrontMostApplication = 215;
    static final int TRANSACTION_startInstrumentation = 39;
    static final int TRANSACTION_startLocalVoiceInteraction = 266;
    static final int TRANSACTION_startLockTaskModeByToken = 190;
    static final int TRANSACTION_startNextMatchingActivity = 65;
    static final int TRANSACTION_startRecentsActivity = 196;
    static final int TRANSACTION_startService = 30;
    static final int TRANSACTION_startSystemLockTaskMode = 201;
    static final int TRANSACTION_startUserInBackground = 189;
    static final int TRANSACTION_startUserInBackgroundWithListener = 302;
    static final int TRANSACTION_startVoiceActivity = 194;
    static final int TRANSACTION_stopAppSwitches = 86;
    static final int TRANSACTION_stopBinderTrackingAndDump = 240;
    static final int TRANSACTION_stopLocalVoiceInteraction = 267;
    static final int TRANSACTION_stopLockTaskModeByToken = 191;
    static final int TRANSACTION_stopService = 31;
    static final int TRANSACTION_stopServiceToken = 44;
    static final int TRANSACTION_stopSystemLockTaskMode = 202;
    static final int TRANSACTION_stopUser = 152;
    static final int TRANSACTION_supportsLocalVoiceInteraction = 268;
    static final int TRANSACTION_suppressResizeConfigChanges = 247;
    static final int TRANSACTION_switchUser = 128;
    static final int TRANSACTION_takePersistableUriPermission = 181;
    static final int TRANSACTION_unbindBackupAgent = 90;
    static final int TRANSACTION_unbindFinished = 70;
    static final int TRANSACTION_unbindService = 33;
    static final int TRANSACTION_unbroadcastIntent = 12;
    static final int TRANSACTION_unhandledBack = 7;
    static final int TRANSACTION_unlockUser = 251;
    static final int TRANSACTION_unregisterIntentSenderCancelListener = 63;
    static final int TRANSACTION_unregisterProcessObserver = 132;
    static final int TRANSACTION_unregisterReceiver = 10;
    static final int TRANSACTION_unregisterTaskStackListener = 218;
    static final int TRANSACTION_unregisterUidObserver = 3;
    static final int TRANSACTION_unregisterUserSwitchObserver = 154;
    static final int TRANSACTION_unstableProviderDied = 148;
    static final int TRANSACTION_updateConfiguration = 43;
    static final int TRANSACTION_updateDeviceOwner = 233;
    static final int TRANSACTION_updateDisplayOverrideConfiguration = 286;
    static final int TRANSACTION_updateLockTaskFeatures = 299;
    static final int TRANSACTION_updateLockTaskPackages = 228;
    static final int TRANSACTION_updatePersistentConfiguration = 134;
    static final int TRANSACTION_waitForNetworkStateUpdate = 295;
    static final int TRANSACTION_willActivityBeVisible = 104;
    
    public Stub()
    {
      attachInterface(this, "android.app.IActivityManager");
    }
    
    public static IActivityManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IActivityManager");
      if ((localIInterface != null) && ((localIInterface instanceof IActivityManager))) {
        return (IActivityManager)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    private boolean onTransact$bindService$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      IBinder localIBinder = paramParcel1.readStrongBinder();
      if (paramParcel1.readInt() != 0) {}
      for (Intent localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);; localIntent = null) {
        break;
      }
      int i = bindService(localIApplicationThread, localIBinder, localIntent, paramParcel1.readString(), IServiceConnection.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    private boolean onTransact$broadcastIntent$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      Intent localIntent;
      if (paramParcel1.readInt() != 0) {
        localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        localIntent = null;
      }
      String str1 = paramParcel1.readString();
      IIntentReceiver localIIntentReceiver = IIntentReceiver.Stub.asInterface(paramParcel1.readStrongBinder());
      int i = paramParcel1.readInt();
      String str2 = paramParcel1.readString();
      Bundle localBundle1;
      if (paramParcel1.readInt() != 0) {
        localBundle1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        localBundle1 = null;
      }
      String[] arrayOfString = paramParcel1.createStringArray();
      int j = paramParcel1.readInt();
      Bundle localBundle2;
      if (paramParcel1.readInt() != 0) {
        localBundle2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        localBundle2 = null;
      }
      boolean bool1;
      if (paramParcel1.readInt() != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramParcel1.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      i = broadcastIntent(localIApplicationThread, localIntent, str1, localIIntentReceiver, i, str2, localBundle1, arrayOfString, j, localBundle2, bool1, bool2, paramParcel1.readInt());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    private boolean onTransact$checkGrantUriPermission$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      int i = paramParcel1.readInt();
      String str = paramParcel1.readString();
      if (paramParcel1.readInt() != 0) {}
      for (Uri localUri = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);; localUri = null) {
        break;
      }
      i = checkGrantUriPermission(i, str, localUri, paramParcel1.readInt(), paramParcel1.readInt());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    private boolean onTransact$checkUriPermission$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      if (paramParcel1.readInt() != 0) {}
      for (Uri localUri = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);; localUri = null) {
        break;
      }
      int i = checkUriPermission(localUri, paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readStrongBinder());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    private boolean onTransact$crashApplication$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      crashApplication(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
      paramParcel2.writeNoException();
      return true;
    }
    
    private boolean onTransact$dumpHeap$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      String str1 = paramParcel1.readString();
      int i = paramParcel1.readInt();
      boolean bool1;
      if (paramParcel1.readInt() != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramParcel1.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      boolean bool3;
      if (paramParcel1.readInt() != 0) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      String str2 = paramParcel1.readString();
      if (paramParcel1.readInt() != 0) {}
      for (paramParcel1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = null) {
        break;
      }
      int j = dumpHeap(str1, i, bool1, bool2, bool3, str2, paramParcel1);
      paramParcel2.writeNoException();
      paramParcel2.writeInt(j);
      return true;
    }
    
    private boolean onTransact$finishReceiver$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IBinder localIBinder = paramParcel1.readStrongBinder();
      int i = paramParcel1.readInt();
      String str = paramParcel1.readString();
      if (paramParcel1.readInt() != 0) {}
      for (paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = null) {
        break;
      }
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      finishReceiver(localIBinder, i, str, paramParcel2, bool, paramParcel1.readInt());
      return true;
    }
    
    private boolean onTransact$getIntentSender$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      int i = paramParcel1.readInt();
      String str1 = paramParcel1.readString();
      IBinder localIBinder = paramParcel1.readStrongBinder();
      String str2 = paramParcel1.readString();
      int j = paramParcel1.readInt();
      Intent[] arrayOfIntent = (Intent[])paramParcel1.createTypedArray(Intent.CREATOR);
      String[] arrayOfString = paramParcel1.createStringArray();
      int k = paramParcel1.readInt();
      int m = paramParcel1.readInt();
      Object localObject = null;
      Bundle localBundle;
      if (m != 0) {
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        localBundle = null;
      }
      paramParcel1 = getIntentSender(i, str1, localIBinder, str2, j, arrayOfIntent, arrayOfString, k, localBundle, paramParcel1.readInt());
      paramParcel2.writeNoException();
      if (paramParcel1 != null) {}
      for (paramParcel1 = paramParcel1.asBinder();; paramParcel1 = localObject) {
        break;
      }
      paramParcel2.writeStrongBinder(paramParcel1);
      return true;
    }
    
    private boolean onTransact$grantUriPermission$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      String str = paramParcel1.readString();
      if (paramParcel1.readInt() != 0) {}
      for (Uri localUri = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);; localUri = null) {
        break;
      }
      grantUriPermission(localIApplicationThread, str, localUri, paramParcel1.readInt(), paramParcel1.readInt());
      paramParcel2.writeNoException();
      return true;
    }
    
    private boolean onTransact$grantUriPermissionFromOwner$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IBinder localIBinder = paramParcel1.readStrongBinder();
      int i = paramParcel1.readInt();
      String str = paramParcel1.readString();
      if (paramParcel1.readInt() != 0) {}
      for (Uri localUri = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);; localUri = null) {
        break;
      }
      grantUriPermissionFromOwner(localIBinder, i, str, localUri, paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
      paramParcel2.writeNoException();
      return true;
    }
    
    private boolean onTransact$handleIncomingUser$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      int i = paramParcel1.readInt();
      int j = paramParcel1.readInt();
      int k = paramParcel1.readInt();
      boolean bool1;
      if (paramParcel1.readInt() != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramParcel1.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      k = handleIncomingUser(i, j, k, bool1, bool2, paramParcel1.readString(), paramParcel1.readString());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(k);
      return true;
    }
    
    private boolean onTransact$launchAssistIntent$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      int i = paramParcel1.readInt();
      Object localObject = null;
      Intent localIntent;
      if (i != 0) {
        localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        localIntent = null;
      }
      i = paramParcel1.readInt();
      String str = paramParcel1.readString();
      int k = paramParcel1.readInt();
      if (paramParcel1.readInt() != 0) {}
      for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject) {
        break;
      }
      int j = launchAssistIntent(localIntent, i, str, k, paramParcel1);
      paramParcel2.writeNoException();
      paramParcel2.writeInt(j);
      return true;
    }
    
    private boolean onTransact$noteAlarmFinish$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IIntentSender localIIntentSender = IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder());
      WorkSource localWorkSource;
      if (paramParcel1.readInt() != 0) {
        localWorkSource = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
      } else {
        localWorkSource = null;
      }
      noteAlarmFinish(localIIntentSender, localWorkSource, paramParcel1.readInt(), paramParcel1.readString());
      paramParcel2.writeNoException();
      return true;
    }
    
    private boolean onTransact$noteAlarmStart$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IIntentSender localIIntentSender = IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder());
      WorkSource localWorkSource;
      if (paramParcel1.readInt() != 0) {
        localWorkSource = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
      } else {
        localWorkSource = null;
      }
      noteAlarmStart(localIIntentSender, localWorkSource, paramParcel1.readInt(), paramParcel1.readString());
      paramParcel2.writeNoException();
      return true;
    }
    
    private boolean onTransact$noteWakeupAlarm$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IIntentSender localIIntentSender = IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder());
      if (paramParcel1.readInt() != 0) {}
      for (WorkSource localWorkSource = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);; localWorkSource = null) {
        break;
      }
      noteWakeupAlarm(localIIntentSender, localWorkSource, paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
      paramParcel2.writeNoException();
      return true;
    }
    
    private boolean onTransact$profileControl$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      String str = paramParcel1.readString();
      int i = paramParcel1.readInt();
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      if (paramParcel1.readInt() != 0) {}
      for (ProfilerInfo localProfilerInfo = (ProfilerInfo)ProfilerInfo.CREATOR.createFromParcel(paramParcel1);; localProfilerInfo = null) {
        break;
      }
      int j = profileControl(str, i, bool, localProfilerInfo, paramParcel1.readInt());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(j);
      return true;
    }
    
    private boolean onTransact$registerReceiver$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      String str = paramParcel1.readString();
      IIntentReceiver localIIntentReceiver = IIntentReceiver.Stub.asInterface(paramParcel1.readStrongBinder());
      if (paramParcel1.readInt() != 0) {}
      for (IntentFilter localIntentFilter = (IntentFilter)IntentFilter.CREATOR.createFromParcel(paramParcel1);; localIntentFilter = null) {
        break;
      }
      paramParcel1 = registerReceiver(localIApplicationThread, str, localIIntentReceiver, localIntentFilter, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
      paramParcel2.writeNoException();
      if (paramParcel1 != null)
      {
        paramParcel2.writeInt(1);
        paramParcel1.writeToParcel(paramParcel2, 1);
      }
      else
      {
        paramParcel2.writeInt(0);
      }
      return true;
    }
    
    private boolean onTransact$reportAssistContextExtras$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IBinder localIBinder = paramParcel1.readStrongBinder();
      Bundle localBundle;
      if (paramParcel1.readInt() != 0) {
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        localBundle = null;
      }
      AssistStructure localAssistStructure;
      if (paramParcel1.readInt() != 0) {
        localAssistStructure = (AssistStructure)AssistStructure.CREATOR.createFromParcel(paramParcel1);
      } else {
        localAssistStructure = null;
      }
      AssistContent localAssistContent;
      if (paramParcel1.readInt() != 0) {
        localAssistContent = (AssistContent)AssistContent.CREATOR.createFromParcel(paramParcel1);
      } else {
        localAssistContent = null;
      }
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      reportAssistContextExtras(localIBinder, localBundle, localAssistStructure, localAssistContent, paramParcel1);
      paramParcel2.writeNoException();
      return true;
    }
    
    private boolean onTransact$reportSizeConfigurations$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      reportSizeConfigurations(paramParcel1.readStrongBinder(), paramParcel1.createIntArray(), paramParcel1.createIntArray(), paramParcel1.createIntArray());
      paramParcel2.writeNoException();
      return true;
    }
    
    private boolean onTransact$requestAssistContextExtras$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      int i = paramParcel1.readInt();
      IAssistDataReceiver localIAssistDataReceiver = IAssistDataReceiver.Stub.asInterface(paramParcel1.readStrongBinder());
      if (paramParcel1.readInt() != 0) {}
      for (Bundle localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; localBundle = null) {
        break;
      }
      IBinder localIBinder = paramParcel1.readStrongBinder();
      boolean bool1;
      if (paramParcel1.readInt() != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramParcel1.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      int j = requestAssistContextExtras(i, localIAssistDataReceiver, localBundle, localIBinder, bool1, bool2);
      paramParcel2.writeNoException();
      paramParcel2.writeInt(j);
      return true;
    }
    
    private boolean onTransact$requestAutofillData$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IAssistDataReceiver localIAssistDataReceiver = IAssistDataReceiver.Stub.asInterface(paramParcel1.readStrongBinder());
      Bundle localBundle;
      if (paramParcel1.readInt() != 0) {
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        localBundle = null;
      }
      int i = requestAutofillData(localIAssistDataReceiver, localBundle, paramParcel1.readStrongBinder(), paramParcel1.readInt());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    private boolean onTransact$resizeDockedStack$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      int i = paramParcel1.readInt();
      Object localObject = null;
      Rect localRect1;
      if (i != 0) {
        localRect1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
      } else {
        localRect1 = null;
      }
      Rect localRect2;
      if (paramParcel1.readInt() != 0) {
        localRect2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
      } else {
        localRect2 = null;
      }
      Rect localRect3;
      if (paramParcel1.readInt() != 0) {
        localRect3 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
      } else {
        localRect3 = null;
      }
      Rect localRect4;
      if (paramParcel1.readInt() != 0) {
        localRect4 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
      } else {
        localRect4 = null;
      }
      if (paramParcel1.readInt() != 0) {}
      for (paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject) {
        break;
      }
      resizeDockedStack(localRect1, localRect2, localRect3, localRect4, paramParcel1);
      paramParcel2.writeNoException();
      return true;
    }
    
    private boolean onTransact$resizeStack$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      int i = paramParcel1.readInt();
      if (paramParcel1.readInt() != 0) {}
      for (Rect localRect = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);; localRect = null) {
        break;
      }
      boolean bool1;
      if (paramParcel1.readInt() != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramParcel1.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      boolean bool3;
      if (paramParcel1.readInt() != 0) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      resizeStack(i, localRect, bool1, bool2, bool3, paramParcel1.readInt());
      paramParcel2.writeNoException();
      return true;
    }
    
    private boolean onTransact$revokeUriPermission$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      String str = paramParcel1.readString();
      if (paramParcel1.readInt() != 0) {}
      for (Uri localUri = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);; localUri = null) {
        break;
      }
      revokeUriPermission(localIApplicationThread, str, localUri, paramParcel1.readInt(), paramParcel1.readInt());
      paramParcel2.writeNoException();
      return true;
    }
    
    private boolean onTransact$sendIntentSender$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IIntentSender localIIntentSender = IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder());
      IBinder localIBinder = paramParcel1.readStrongBinder();
      int i = paramParcel1.readInt();
      Intent localIntent;
      if (paramParcel1.readInt() != 0) {
        localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        localIntent = null;
      }
      String str1 = paramParcel1.readString();
      IIntentReceiver localIIntentReceiver = IIntentReceiver.Stub.asInterface(paramParcel1.readStrongBinder());
      String str2 = paramParcel1.readString();
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      i = sendIntentSender(localIIntentSender, localIBinder, i, localIntent, str1, localIIntentReceiver, str2, paramParcel1);
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    private boolean onTransact$setServiceForeground$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      int i = paramParcel1.readInt();
      Notification localNotification = null;
      ComponentName localComponentName;
      if (i != 0) {
        localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
      } else {
        localComponentName = null;
      }
      IBinder localIBinder = paramParcel1.readStrongBinder();
      i = paramParcel1.readInt();
      if (paramParcel1.readInt() != 0) {
        localNotification = (Notification)Notification.CREATOR.createFromParcel(paramParcel1);
      }
      for (;;)
      {
        break;
      }
      setServiceForeground(localComponentName, localIBinder, i, localNotification, paramParcel1.readInt());
      paramParcel2.writeNoException();
      return true;
    }
    
    private boolean onTransact$setTaskWindowingModeSplitScreenPrimary$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      int i = paramParcel1.readInt();
      int k = paramParcel1.readInt();
      boolean bool1;
      if (paramParcel1.readInt() != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramParcel1.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      if (paramParcel1.readInt() != 0) {}
      for (Rect localRect = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);; localRect = null) {
        break;
      }
      boolean bool3;
      if (paramParcel1.readInt() != 0) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      int j = setTaskWindowingModeSplitScreenPrimary(i, k, bool1, bool2, localRect, bool3);
      paramParcel2.writeNoException();
      paramParcel2.writeInt(j);
      return true;
    }
    
    private boolean onTransact$startActivities$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      String str = paramParcel1.readString();
      Intent[] arrayOfIntent = (Intent[])paramParcel1.createTypedArray(Intent.CREATOR);
      String[] arrayOfString = paramParcel1.createStringArray();
      IBinder localIBinder = paramParcel1.readStrongBinder();
      if (paramParcel1.readInt() != 0) {}
      for (Bundle localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; localBundle = null) {
        break;
      }
      int i = startActivities(localIApplicationThread, str, arrayOfIntent, arrayOfString, localIBinder, localBundle, paramParcel1.readInt());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    private boolean onTransact$startActivity$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      String str1 = paramParcel1.readString();
      Intent localIntent;
      if (paramParcel1.readInt() != 0) {
        localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        localIntent = null;
      }
      String str2 = paramParcel1.readString();
      IBinder localIBinder = paramParcel1.readStrongBinder();
      String str3 = paramParcel1.readString();
      int i = paramParcel1.readInt();
      int j = paramParcel1.readInt();
      ProfilerInfo localProfilerInfo;
      if (paramParcel1.readInt() != 0) {
        localProfilerInfo = (ProfilerInfo)ProfilerInfo.CREATOR.createFromParcel(paramParcel1);
      } else {
        localProfilerInfo = null;
      }
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      i = startActivity(localIApplicationThread, str1, localIntent, str2, localIBinder, str3, i, j, localProfilerInfo, paramParcel1);
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    private boolean onTransact$startActivityAndWait$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      String str1 = paramParcel1.readString();
      Intent localIntent;
      if (paramParcel1.readInt() != 0) {
        localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        localIntent = null;
      }
      String str2 = paramParcel1.readString();
      IBinder localIBinder = paramParcel1.readStrongBinder();
      String str3 = paramParcel1.readString();
      int i = paramParcel1.readInt();
      int j = paramParcel1.readInt();
      ProfilerInfo localProfilerInfo;
      if (paramParcel1.readInt() != 0) {
        localProfilerInfo = (ProfilerInfo)ProfilerInfo.CREATOR.createFromParcel(paramParcel1);
      } else {
        localProfilerInfo = null;
      }
      Bundle localBundle;
      if (paramParcel1.readInt() != 0) {
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        localBundle = null;
      }
      paramParcel1 = startActivityAndWait(localIApplicationThread, str1, localIntent, str2, localIBinder, str3, i, j, localProfilerInfo, localBundle, paramParcel1.readInt());
      paramParcel2.writeNoException();
      if (paramParcel1 != null)
      {
        paramParcel2.writeInt(1);
        paramParcel1.writeToParcel(paramParcel2, 1);
      }
      else
      {
        paramParcel2.writeInt(0);
      }
      return true;
    }
    
    private boolean onTransact$startActivityAsCaller$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      String str1 = paramParcel1.readString();
      Intent localIntent;
      if (paramParcel1.readInt() != 0) {
        localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        localIntent = null;
      }
      String str2 = paramParcel1.readString();
      IBinder localIBinder = paramParcel1.readStrongBinder();
      String str3 = paramParcel1.readString();
      int i = paramParcel1.readInt();
      int j = paramParcel1.readInt();
      ProfilerInfo localProfilerInfo;
      if (paramParcel1.readInt() != 0) {
        localProfilerInfo = (ProfilerInfo)ProfilerInfo.CREATOR.createFromParcel(paramParcel1);
      } else {
        localProfilerInfo = null;
      }
      Bundle localBundle;
      if (paramParcel1.readInt() != 0) {
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        localBundle = null;
      }
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      i = startActivityAsCaller(localIApplicationThread, str1, localIntent, str2, localIBinder, str3, i, j, localProfilerInfo, localBundle, bool, paramParcel1.readInt());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    private boolean onTransact$startActivityAsUser$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      String str1 = paramParcel1.readString();
      Intent localIntent;
      if (paramParcel1.readInt() != 0) {
        localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        localIntent = null;
      }
      String str2 = paramParcel1.readString();
      IBinder localIBinder = paramParcel1.readStrongBinder();
      String str3 = paramParcel1.readString();
      int i = paramParcel1.readInt();
      int j = paramParcel1.readInt();
      ProfilerInfo localProfilerInfo;
      if (paramParcel1.readInt() != 0) {
        localProfilerInfo = (ProfilerInfo)ProfilerInfo.CREATOR.createFromParcel(paramParcel1);
      } else {
        localProfilerInfo = null;
      }
      Bundle localBundle;
      if (paramParcel1.readInt() != 0) {
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        localBundle = null;
      }
      j = startActivityAsUser(localIApplicationThread, str1, localIntent, str2, localIBinder, str3, i, j, localProfilerInfo, localBundle, paramParcel1.readInt());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(j);
      return true;
    }
    
    private boolean onTransact$startActivityIntentSender$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      IIntentSender localIIntentSender = IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder());
      IBinder localIBinder1 = paramParcel1.readStrongBinder();
      Intent localIntent;
      if (paramParcel1.readInt() != 0) {
        localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        localIntent = null;
      }
      String str1 = paramParcel1.readString();
      IBinder localIBinder2 = paramParcel1.readStrongBinder();
      String str2 = paramParcel1.readString();
      int i = paramParcel1.readInt();
      int j = paramParcel1.readInt();
      int k = paramParcel1.readInt();
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      i = startActivityIntentSender(localIApplicationThread, localIIntentSender, localIBinder1, localIntent, str1, localIBinder2, str2, i, j, k, paramParcel1);
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    private boolean onTransact$startActivityWithConfig$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      String str1 = paramParcel1.readString();
      Intent localIntent;
      if (paramParcel1.readInt() != 0) {
        localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        localIntent = null;
      }
      String str2 = paramParcel1.readString();
      IBinder localIBinder = paramParcel1.readStrongBinder();
      String str3 = paramParcel1.readString();
      int i = paramParcel1.readInt();
      int j = paramParcel1.readInt();
      Configuration localConfiguration;
      if (paramParcel1.readInt() != 0) {
        localConfiguration = (Configuration)Configuration.CREATOR.createFromParcel(paramParcel1);
      } else {
        localConfiguration = null;
      }
      Bundle localBundle;
      if (paramParcel1.readInt() != 0) {
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        localBundle = null;
      }
      i = startActivityWithConfig(localIApplicationThread, str1, localIntent, str2, localIBinder, str3, i, j, localConfiguration, localBundle, paramParcel1.readInt());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    private boolean onTransact$startAssistantActivity$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      String str1 = paramParcel1.readString();
      int i = paramParcel1.readInt();
      int j = paramParcel1.readInt();
      Intent localIntent;
      if (paramParcel1.readInt() != 0) {
        localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        localIntent = null;
      }
      String str2 = paramParcel1.readString();
      Bundle localBundle;
      if (paramParcel1.readInt() != 0) {
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        localBundle = null;
      }
      j = startAssistantActivity(str1, i, j, localIntent, str2, localBundle, paramParcel1.readInt());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(j);
      return true;
    }
    
    private boolean onTransact$startInstrumentation$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      int i = paramParcel1.readInt();
      Bundle localBundle = null;
      ComponentName localComponentName;
      if (i != 0) {
        localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
      } else {
        localComponentName = null;
      }
      String str = paramParcel1.readString();
      i = paramParcel1.readInt();
      if (paramParcel1.readInt() != 0) {
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      }
      for (;;)
      {
        break;
      }
      int j = startInstrumentation(localComponentName, str, i, localBundle, IInstrumentationWatcher.Stub.asInterface(paramParcel1.readStrongBinder()), IUiAutomationConnection.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readString());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(j);
      return true;
    }
    
    private boolean onTransact$startService$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
      if (paramParcel1.readInt() != 0) {}
      for (Intent localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);; localIntent = null) {
        break;
      }
      String str = paramParcel1.readString();
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      paramParcel1 = startService(localIApplicationThread, localIntent, str, bool, paramParcel1.readString(), paramParcel1.readInt());
      paramParcel2.writeNoException();
      if (paramParcel1 != null)
      {
        paramParcel2.writeInt(1);
        paramParcel1.writeToParcel(paramParcel2, 1);
      }
      else
      {
        paramParcel2.writeInt(0);
      }
      return true;
    }
    
    private boolean onTransact$startVoiceActivity$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      String str1 = paramParcel1.readString();
      int i = paramParcel1.readInt();
      int j = paramParcel1.readInt();
      Intent localIntent;
      if (paramParcel1.readInt() != 0) {
        localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        localIntent = null;
      }
      String str2 = paramParcel1.readString();
      IVoiceInteractionSession localIVoiceInteractionSession = IVoiceInteractionSession.Stub.asInterface(paramParcel1.readStrongBinder());
      IVoiceInteractor localIVoiceInteractor = IVoiceInteractor.Stub.asInterface(paramParcel1.readStrongBinder());
      int k = paramParcel1.readInt();
      ProfilerInfo localProfilerInfo;
      if (paramParcel1.readInt() != 0) {
        localProfilerInfo = (ProfilerInfo)ProfilerInfo.CREATOR.createFromParcel(paramParcel1);
      } else {
        localProfilerInfo = null;
      }
      Bundle localBundle;
      if (paramParcel1.readInt() != 0) {
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        localBundle = null;
      }
      j = startVoiceActivity(str1, i, j, localIntent, str2, localIVoiceInteractionSession, localIVoiceInteractor, k, localProfilerInfo, localBundle, paramParcel1.readInt());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(j);
      return true;
    }
    
    private boolean onTransact$unlockUser$(Parcel paramParcel1, Parcel paramParcel2)
      throws RemoteException
    {
      paramParcel1.enforceInterface("android.app.IActivityManager");
      int i = unlockUser(paramParcel1.readInt(), paramParcel1.createByteArray(), paramParcel1.createByteArray(), IProgressListener.Stub.asInterface(paramParcel1.readStrongBinder()));
      paramParcel2.writeNoException();
      paramParcel2.writeInt(i);
      return true;
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        Object localObject14 = null;
        Object localObject15 = null;
        Object localObject16 = null;
        Object localObject17 = null;
        Object localObject18 = null;
        Object localObject19 = null;
        Object localObject20 = null;
        Object localObject21 = null;
        Object localObject22 = null;
        Object localObject23 = null;
        IBinder localIBinder = null;
        Object localObject24 = null;
        Object localObject25 = null;
        Object localObject26 = null;
        Object localObject27 = null;
        Object localObject28 = null;
        Object localObject29 = null;
        Object localObject30 = null;
        Object localObject31 = null;
        Object localObject32 = null;
        Object localObject33 = null;
        Object localObject34 = null;
        Object localObject35 = null;
        Object localObject36 = null;
        Object localObject37 = null;
        Object localObject38 = null;
        Object localObject39 = null;
        Object localObject40 = null;
        Object localObject41 = null;
        Object localObject42 = null;
        Object localObject43 = null;
        Object localObject44 = null;
        Object localObject45 = null;
        Object localObject46 = null;
        Object localObject47 = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        boolean bool7 = false;
        boolean bool8 = false;
        boolean bool9 = false;
        boolean bool10 = false;
        boolean bool11 = false;
        boolean bool12 = false;
        boolean bool13 = false;
        boolean bool14 = false;
        boolean bool15 = false;
        boolean bool16 = false;
        boolean bool17 = false;
        boolean bool18 = false;
        boolean bool19 = false;
        boolean bool20 = false;
        boolean bool21 = false;
        boolean bool22 = false;
        boolean bool23 = false;
        boolean bool24 = false;
        boolean bool25 = false;
        boolean bool26 = false;
        boolean bool27 = false;
        boolean bool28 = false;
        boolean bool29 = false;
        boolean bool30 = false;
        boolean bool31 = false;
        boolean bool32 = false;
        boolean bool33 = false;
        boolean bool34 = false;
        boolean bool35 = false;
        boolean bool36 = false;
        boolean bool37 = false;
        boolean bool38 = false;
        boolean bool39 = false;
        boolean bool40 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 314: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          requestFocusedAppFillNotchRegion(bool40);
          paramParcel2.writeNoException();
          return true;
        case 313: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getFocusedAppNotchUiMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 312: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          bool40 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          requestVisibleAppFitScreen(bool40, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 311: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          bool40 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          requestFocusedAppFitScreen(bool40);
          paramParcel2.writeNoException();
          return true;
        case 310: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getVisibleAppScaleMode(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 309: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getFocusedAppScaleMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 308: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isRestrictStartActivity(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 307: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = paramParcel1.readInt();
          localObject3 = paramParcel1.readString();
          bool40 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          restrictStartActivity(paramInt1, (String)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 306: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          forceStopNativeProcess(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 305: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject47;
          }
          alwaysShowUnsupportedCompileSdkWarning(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 304: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RemoteAnimationAdapter)RemoteAnimationAdapter.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          registerRemoteAnimationForNextActivityStart((String)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 303: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RemoteAnimationDefinition)RemoteAnimationDefinition.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          registerRemoteAnimations((IBinder)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 302: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = startUserInBackgroundWithListener(paramParcel1.readInt(), IProgressListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 301: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          bool40 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setTurnScreenOn((IBinder)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 300: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          bool40 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setShowWhenLocked((IBinder)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 299: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          updateLockTaskFeatures(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 298: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          backgroundWhitelistUid(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 297: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getLastResumedActivityUserId();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 296: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          bool40 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setDisablePreviewScreenshots((IBinder)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 295: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          waitForNetworkStateUpdate(paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 294: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          setPersistentVrThread(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 293: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          scheduleApplicationInfoChanged(paramParcel1.createStringArrayList(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 292: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          } else {
            bool40 = false;
          }
          paramParcel1 = getTaskSnapshot(paramInt1, bool40);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 291: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          cancelTaskWindowTransition(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 290: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = restartUserInBackground(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 289: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject42 = paramParcel1.readStrongBinder();
          localObject17 = IKeyguardDismissCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject3;
          }
          dismissKeyguard((IBinder)localObject42, (IKeyguardDismissCallback)localObject17, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 288: 
          return onTransact$requestAutofillData$(paramParcel1, paramParcel2);
        case 287: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          moveStackToDisplay(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 286: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Configuration)Configuration.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject4;
          }
          paramInt1 = updateDisplayOverrideConfiguration((Configuration)localObject3, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 285: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          bool40 = bool7;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setHasTopUi(bool40);
          paramParcel2.writeNoException();
          return true;
        case 284: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          setRenderThread(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 283: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          setVrThread(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 282: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isBackgroundRestricted(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 281: 
          return onTransact$sendIntentSender$(paramParcel1, paramParcel2);
        case 280: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          sendIdleJobTrigger();
          paramParcel2.writeNoException();
          return true;
        case 279: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          startConfirmDeviceCredentialIntent((Intent)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 278: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          notifyLockedProfile(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 277: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          paramInt1 = isVrModePackageEnabled(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 276: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          resizePinnedStack((Rect)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 275: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getMemoryTrimLevel();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 274: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          makePackageIdle(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 273: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          removeStacksWithActivityTypes(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          return true;
        case 272: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          removeStacksInWindowingModes(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          return true;
        case 271: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          removeStack(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 270: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          notifyPinnedStackAnimationEnded();
          paramParcel2.writeNoException();
          return true;
        case 269: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          notifyPinnedStackAnimationStarted();
          paramParcel2.writeNoException();
          return true;
        case 268: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = supportsLocalVoiceInteraction();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 267: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          stopLocalVoiceInteraction(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 266: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          startLocalVoiceInteraction((IBinder)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 265: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isAppForeground(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 264: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          clearGrantedUriPermissions(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 263: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getGrantedUriPermissions(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 262: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          bool40 = bool8;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          paramInt1 = setVrMode((IBinder)localObject3, bool40, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 261: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          bool40 = bool9;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setSplitScreenResizing(bool40);
          paramParcel2.writeNoException();
          return true;
        case 260: 
          return onTransact$resizeDockedStack$(paramParcel1, paramParcel2);
        case 259: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getUriPermissionOwnerForActivity(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 258: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          activityRelaunched(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 257: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getMaxNumPictureInPictureActions(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 256: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PictureInPictureParams)PictureInPictureParams.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject10;
          }
          setPictureInPictureParams((IBinder)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 255: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PictureInPictureParams)PictureInPictureParams.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject11;
          }
          paramInt1 = enterPictureInPictureMode((IBinder)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 254: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          killPackageDependents(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 253: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isInPictureInPictureMode(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 252: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isInMultiWindowMode(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 251: 
          return onTransact$unlockUser$(paramParcel1, paramParcel2);
        case 250: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isAppStartModeDisabled(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 249: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          paramInt1 = moveTopActivityToPinnedStack(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 248: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = paramParcel1.readInt();
          bool40 = bool10;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          moveTasksToFullscreenStack(paramInt1, bool40);
          paramParcel2.writeNoException();
          return true;
        case 247: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          bool40 = bool11;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          suppressResizeConfigChanges(bool40);
          paramParcel2.writeNoException();
          return true;
        case 246: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          bool40 = bool12;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          dismissPip(bool40, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 245: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          bool40 = bool13;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          dismissSplitScreenMode(bool40);
          paramParcel2.writeNoException();
          return true;
        case 244: 
          return onTransact$setTaskWindowingModeSplitScreenPrimary$(paramParcel1, paramParcel2);
        case 243: 
          return onTransact$reportSizeConfigurations$(paramParcel1, paramParcel2);
        case 242: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          exitFreeformMode(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 241: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          positionTaskInStack(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 240: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject13;
          }
          paramInt1 = stopBinderTrackingAndDump(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 239: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = startBinderTracking();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 238: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isRootVoiceInteraction(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 237: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject14;
          }
          paramInt1 = showAssistFromActivity((IBinder)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 236: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isAssistDataAllowedOnCurrentActivity();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 235: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getUidProcessState(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 234: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          keyguardGoingAway(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 233: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          updateDeviceOwner(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 232: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          showLockTaskEscapeMessage(paramParcel1.readStrongBinder());
          return true;
        case 231: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getPackageProcessState(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 230: 
          return onTransact$noteAlarmFinish$(paramParcel1, paramParcel2);
        case 229: 
          return onTransact$noteAlarmStart$(paramParcel1, paramParcel2);
        case 228: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          updateLockTaskPackages(paramParcel1.readInt(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 227: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = IVoiceInteractionSession.Stub.asInterface(paramParcel1.readStrongBinder());
          bool40 = bool14;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setVoiceKeepAwake((IVoiceInteractionSession)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 226: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          dumpHeapFinished(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 225: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          setDumpHeapDebugLimit(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 224: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getLockTaskModeState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 223: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject15;
          }
          resizeTask(paramInt1, (Rect)localObject3, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 222: 
          return onTransact$requestAssistContextExtras$(paramParcel1, paramParcel2);
        case 221: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          setTaskResizeable(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 220: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = createStackOnDisplay(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 219: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          notifyCleartextNetwork(paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 218: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          unregisterTaskStackListener(ITaskStackListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 217: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          registerTaskStackListener(ITaskStackListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 216: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = checkPermissionWithToken(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 215: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject16;
          }
          startInPlaceAnimationOnFrontMostApplication(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 214: 
          return onTransact$launchAssistIntent$(paramParcel1, paramParcel2);
        case 213: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getTaskDescriptionIcon(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 212: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          bootAnimationComplete();
          paramParcel2.writeNoException();
          return true;
        case 211: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          releaseSomeActivities(IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 210: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = releaseActivityInstance(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 209: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getAppTaskThumbnailSize();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 208: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localIBinder = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject42 = (ActivityManager.TaskDescription)ActivityManager.TaskDescription.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject42 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bitmap)Bitmap.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject17;
          }
          paramInt1 = addAppTask(localIBinder, (Intent)localObject3, (ActivityManager.TaskDescription)localObject42, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 207: 
          return onTransact$startActivityAsCaller$(paramParcel1, paramParcel2);
        case 206: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          notifyEnterAnimationComplete(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 205: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          notifyLaunchTaskBehindComplete(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 204: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isTopOfTask(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 203: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          finishVoiceTask(IVoiceInteractionSession.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 202: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          stopSystemLockTaskMode();
          paramParcel2.writeNoException();
          return true;
        case 201: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          startSystemLockTaskMode(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 200: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getAppTasks(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeBinderList(paramParcel1);
          return true;
        case 199: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getActivityOptions(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 198: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject18;
          }
          paramInt1 = startActivityFromRecents(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 197: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          bool40 = bool15;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          cancelRecentsAnimation(bool40);
          paramParcel2.writeNoException();
          return true;
        case 196: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject19;
          }
          startRecentsActivity((Intent)localObject3, IAssistDataReceiver.Stub.asInterface(paramParcel1.readStrongBinder()), IRecentsAnimationRunner.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 195: 
          return onTransact$startAssistantActivity$(paramParcel1, paramParcel2);
        case 194: 
          return onTransact$startVoiceActivity$(paramParcel1, paramParcel2);
        case 193: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ActivityManager.TaskDescription)ActivityManager.TaskDescription.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject20;
          }
          setTaskDescription((IBinder)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 192: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isInLockTaskMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 191: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          stopLockTaskModeByToken(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 190: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          startLockTaskModeByToken(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 189: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = startUserInBackground(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 188: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getTagForIntentSender(IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 187: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = setProcessMemoryTrimLevel(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 186: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getActivityDisplayId(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 185: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getTaskBounds(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 184: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          appNotRespondingViaProvider(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 183: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          } else {
            bool40 = false;
          }
          paramParcel1 = getPersistedUriPermissions((String)localObject3, bool40);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 182: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject21;
          }
          releasePersistableUriPermission((Uri)localObject3, paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 181: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject22;
          }
          takePersistableUriPermission((Uri)localObject3, paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 180: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          performIdleMaintenance();
          paramParcel2.writeNoException();
          return true;
        case 179: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          restart();
          paramParcel2.writeNoException();
          return true;
        case 178: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          bool40 = bool16;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          reportActivityFullyDrawn((IBinder)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 177: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          notifyActivityDrawn(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 176: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject23;
          }
          paramInt1 = convertToTranslucent((IBinder)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 175: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = convertFromTranslucent(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 174: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getStackInfo(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 173: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getFocusedStackInfo();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 172: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          setFocusedStack(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 171: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getAllStackInfos();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 170: 
          return onTransact$resizeStack$(paramParcel1, paramParcel2);
        case 169: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          bool40 = bool17;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          moveTaskToStack(paramInt1, paramInt2, bool40);
          paramParcel2.writeNoException();
          return true;
        case 168: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          bool40 = bool18;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setTaskWindowingMode(paramInt2, paramInt1, bool40);
          paramParcel2.writeNoException();
          return true;
        case 167: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          bool40 = bool19;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          hang((IBinder)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 166: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          bool40 = bool20;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setUserIsMonkey(bool40);
          paramParcel2.writeNoException();
          return true;
        case 165: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          killUid(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 164: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getLaunchedFromPackage(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 163: 
          return onTransact$reportAssistContextExtras$(paramParcel1, paramParcel2);
        case 162: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getAssistContextExtras(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 161: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getIntentForIntentSender(IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 160: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          clearPendingBackup();
          paramParcel2.writeNoException();
          return true;
        case 159: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = paramParcel1.readInt();
          bool40 = bool21;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          long l = inputDispatchingTimedOut(paramInt1, bool40, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 158: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          requestWifiBugReport(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 157: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          requestTelephonyBugReport(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 156: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          requestBugReport(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 155: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getRunningUserIds();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 154: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          unregisterUserSwitchObserver(IUserSwitchObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 153: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          registerUserSwitchObserver(IUserSwitchObserver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 152: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = paramParcel1.readInt();
          bool40 = bool22;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          paramInt1 = stopUser(paramInt1, bool40, IStopUserCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 151: 
          return onTransact$startActivityAsUser$(paramParcel1, paramParcel2);
        case 150: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isIntentSenderAForegroundService(IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 149: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isIntentSenderAnActivity(IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 148: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          unstableProviderDied(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 147: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getLaunchedFromUid(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 146: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = finishActivityAffinity(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 145: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          } else {
            bool40 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool23 = true;
          }
          setLockScreenShown(bool40, bool23, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 144: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject42 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIBinder;
          }
          paramInt1 = navigateUpTo((IBinder)localObject42, (Intent)localObject3, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 143: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = shouldUpRecreateTask(paramParcel1.readStrongBinder(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 142: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getCurrentUser();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 141: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = killProcessesBelowForeground(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 140: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = new ActivityManager.RunningAppProcessInfo();
          getMyMemoryState(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 139: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          removeContentProviderExternal(paramParcel1.readString(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 138: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getContentProviderExternal(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 137: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          killAllBackgroundProcesses();
          paramParcel2.writeNoException();
          return true;
        case 136: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject3 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject24;
          }
          bool40 = bool24;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          showBootMessage((CharSequence)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 135: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getProcessPss(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeLongArray(paramParcel1);
          return true;
        case 134: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Configuration)Configuration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject25;
          }
          updatePersistentConfiguration(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 133: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isIntentSenderTargetedToPackage(IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 132: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          unregisterProcessObserver(IProcessObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 131: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          registerProcessObserver(IProcessObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 130: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = removeTask(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 129: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          setFocusedTask(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 128: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = switchUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 127: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readString();
          bool40 = bool25;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setPackageAskScreenCompat((String)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 126: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getPackageAskScreenCompat(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 125: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          setPackageScreenCompatMode(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 124: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getPackageScreenCompatMode(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 123: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          setFrontActivityScreenCompatMode(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 122: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getFrontActivityScreenCompatMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 121: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          activitySlept(paramParcel1.readStrongBinder());
          return true;
        case 120: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isUserRunning(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 119: 
          return onTransact$startActivities$(paramParcel1, paramParcel2);
        case 118: 
          return onTransact$dumpHeap$(paramParcel1, paramParcel2);
        case 117: 
          return onTransact$checkGrantUriPermission$(paramParcel1, paramParcel2);
        case 116: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject42 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject26;
          }
          revokeUriPermissionFromOwner((IBinder)localObject42, (Uri)localObject3, paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 115: 
          return onTransact$grantUriPermissionFromOwner$(paramParcel1, paramParcel2);
        case 114: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = newUriPermissionOwner(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 113: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject27;
          }
          paramParcel1 = getProviderMimeType((Uri)localObject3, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 112: 
          return onTransact$crashApplication$(paramParcel1, paramParcel2);
        case 111: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isTopActivityImmersive();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 110: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          bool40 = bool26;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setImmersive((IBinder)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 109: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isImmersive(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 108: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (StrictMode.ViolationInfo)StrictMode.ViolationInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject28;
          }
          handleApplicationStrictModeViolation((IBinder)localObject3, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 107: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          finishHeavyWeightApp();
          paramParcel2.writeNoException();
          return true;
        case 106: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getRunningExternalApplications();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 105: 
          return onTransact$startActivityWithConfig$(paramParcel1, paramParcel2);
        case 104: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = willActivityBeVisible(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 103: 
          return onTransact$startActivityAndWait$(paramParcel1, paramParcel2);
        case 102: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isUserAMonkey();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 101: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          killBackgroundProcesses(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 100: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          localObject42 = paramParcel1.readString();
          bool40 = bool27;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ApplicationErrorReport.ParcelableCrashInfo)ApplicationErrorReport.ParcelableCrashInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject29;
          }
          paramInt1 = handleApplicationWtf((IBinder)localObject3, (String)localObject42, bool40, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 99: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          overridePendingTransition(paramParcel1.readStrongBinder(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 98: 
          return onTransact$startActivityIntentSender$(paramParcel1, paramParcel2);
        case 97: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          killApplicationProcess(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 96: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getProcessMemoryInfo(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 95: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          closeSystemDialogs(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 94: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          killApplication(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 93: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          addPackageDependency(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 92: 
          return onTransact$handleIncomingUser$(paramParcel1, paramParcel2);
        case 91: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getUidForIntentSender(IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 90: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ApplicationInfo)ApplicationInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject30;
          }
          unbindBackupAgent(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 89: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          backupAgentCreated(paramParcel1.readString(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 88: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = bindBackupAgent(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 87: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          resumeAppSwitches();
          paramParcel2.writeNoException();
          return true;
        case 86: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          stopAppSwitches();
          paramParcel2.writeNoException();
          return true;
        case 85: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = shutdown(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 84: 
          return onTransact$profileControl$(paramParcel1, paramParcel2);
        case 83: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject31;
          }
          paramParcel1 = peekService((Intent)localObject3, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 82: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getDeviceConfigurationInfo();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 81: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getRunningAppProcesses();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 80: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getTaskDescription(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 79: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getServices(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 78: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject42 = paramParcel1.createIntArray();
          localObject3 = paramParcel1.readString();
          bool40 = bool28;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          paramInt1 = killPids((int[])localObject42, (String)localObject3, bool40);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 77: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          forceStopPackage(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 76: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readString();
          bool40 = bool29;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          paramInt1 = clearApplicationUserData((String)localObject3, bool40, IPackageDataObserver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 75: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getProcessesInErrorState();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 74: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = new ActivityManager.MemoryInfo();
          getMemoryInfo(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 73: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          bool40 = bool30;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          paramInt1 = moveActivityTaskToBack((IBinder)localObject3, bool40);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 72: 
          return onTransact$setServiceForeground$(paramParcel1, paramParcel2);
        case 71: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          paramInt1 = paramParcel1.readInt();
          bool40 = bool31;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setProcessImportant((IBinder)localObject3, paramInt1, bool40, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 70: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject42 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject32;
          }
          bool40 = bool32;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          unbindFinished((IBinder)localObject42, (Intent)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 69: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getRequestedOrientation(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 68: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          setRequestedOrientation(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 67: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          bool40 = bool33;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          removeContentProvider((IBinder)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 66: 
          return onTransact$noteWakeupAlarm$(paramParcel1, paramParcel2);
        case 65: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject42 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject33;
          }
          paramInt1 = startNextMatchingActivity((IBinder)localObject42, (Intent)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 64: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          enterSafeMode();
          paramParcel2.writeNoException();
          return true;
        case 63: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          unregisterIntentSenderCancelListener(IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder()), IResultReceiver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 62: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          registerIntentSenderCancelListener(IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder()), IResultReceiver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 61: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getPackageForIntentSender(IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 60: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          cancelIntentSender(IIntentSender.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 59: 
          return onTransact$getIntentSender$(paramParcel1, paramParcel2);
        case 58: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          activityDestroyed(paramParcel1.readStrongBinder());
          return true;
        case 57: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          serviceDoneExecuting(paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 56: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getRecentTasks(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 55: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          signalPersistentProcesses(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 54: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
          bool40 = bool34;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          showWaitingForDebugger((IApplicationThread)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 53: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = IActivityController.Stub.asInterface(paramParcel1.readStrongBinder());
          bool40 = bool35;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setActivityController((IActivityController)localObject3, bool40);
          paramParcel2.writeNoException();
          return true;
        case 52: 
          return onTransact$revokeUriPermission$(paramParcel1, paramParcel2);
        case 51: 
          return onTransact$grantUriPermission$(paramParcel1, paramParcel2);
        case 50: 
          return onTransact$checkUriPermission$(paramParcel1, paramParcel2);
        case 49: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = checkPermission(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = getProcessLimit();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          setProcessLimit(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getPackageForToken(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getActivityClassForToken(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject3 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject34;
          }
          paramInt1 = stopServiceToken((ComponentName)localObject3, paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Configuration)Configuration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject35;
          }
          paramInt1 = updateConfiguration(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getConfiguration();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject36;
          }
          finishInstrumentation((IApplicationThread)localObject3, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject37;
          }
          addInstrumentationResults((IApplicationThread)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 39: 
          return onTransact$startInstrumentation$(paramParcel1, paramParcel2);
        case 38: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          bool40 = bool36;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          setAlwaysFinish(bool40);
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          setAgentApp(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          } else {
            bool40 = false;
          }
          bool23 = bool37;
          if (paramParcel1.readInt() != 0) {
            bool23 = true;
          }
          setDebugApp((String)localObject3, bool40, bool23);
          paramParcel2.writeNoException();
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          activityResumed(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject42 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject38;
          }
          publishService((IBinder)localObject42, (Intent)localObject3, paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = unbindService(IServiceConnection.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 32: 
          return onTransact$bindService$(paramParcel1, paramParcel2);
        case 31: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject42 = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject39;
          }
          paramInt1 = stopService((IApplicationThread)localObject42, (Intent)localObject3, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 30: 
          return onTransact$startService$(paramParcel1, paramParcel2);
        case 29: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject40;
          }
          paramParcel1 = getRunningServiceControlPanel(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          finishSubActivity(paramParcel1.readStrongBinder(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = refContentProvider(paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          publishContentProviders(IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.createTypedArrayList(ContentProviderHolder.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject42 = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject3 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          } else {
            bool40 = false;
          }
          paramParcel1 = getContentProvider((IApplicationThread)localObject42, (String)localObject3, paramInt1, bool40);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          bool40 = bool38;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          paramInt1 = getTaskForActivity((IBinder)localObject3, bool40);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          moveTaskBackwards(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject41;
          }
          moveTaskToFront(paramInt2, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getFilteredTasks(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getTasks(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getCallingActivity(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramParcel1 = getCallingPackage(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject17 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject3 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject42;
          }
          activityStopped((IBinder)localObject17, paramParcel2, (PersistableBundle)localObject3, paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          activityPaused(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Configuration)Configuration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject43;
          }
          bool40 = bool39;
          if (paramParcel1.readInt() != 0) {
            bool40 = true;
          }
          activityIdle((IBinder)localObject3, paramParcel2, bool40);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          attachApplication(IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          return onTransact$finishReceiver$(paramParcel1, paramParcel2);
        case 12: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject42 = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject44;
          }
          unbroadcastIntent((IApplicationThread)localObject42, (Intent)localObject3, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          return onTransact$broadcastIntent$(paramParcel1, paramParcel2);
        case 10: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          unregisterReceiver(IIntentReceiver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 9: 
          return onTransact$registerReceiver$(paramParcel1, paramParcel2);
        case 8: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject42 = paramParcel1.readStrongBinder();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject45;
          }
          paramInt1 = finishActivity((IBinder)localObject42, paramInt1, (Intent)localObject3, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          unhandledBack();
          paramParcel2.writeNoException();
          return true;
        case 6: 
          return onTransact$startActivity$(paramParcel1, paramParcel2);
        case 5: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ApplicationErrorReport.ParcelableCrashInfo)ApplicationErrorReport.ParcelableCrashInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject46;
          }
          handleApplicationCrash((IBinder)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          paramInt1 = isUidActive(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          unregisterUidObserver(IUidObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.IActivityManager");
          registerUidObserver(IUidObserver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.app.IActivityManager");
        paramParcel1 = openContentUri(paramParcel1.readString());
        paramParcel2.writeNoException();
        if (paramParcel1 != null)
        {
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
        }
        else
        {
          paramParcel2.writeInt(0);
        }
        return true;
      }
      paramParcel2.writeString("android.app.IActivityManager");
      return true;
    }
    
    private static class Proxy
      implements IActivityManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void activityDestroyed(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IActivityManager");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(58, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void activityIdle(IBinder paramIBinder, Configuration paramConfiguration, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IActivityManager");
          localParcel.writeStrongBinder(paramIBinder);
          if (paramConfiguration != null)
          {
            localParcel.writeInt(1);
            paramConfiguration.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void activityPaused(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void activityRelaunched(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(258, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void activityResumed(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void activitySlept(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IActivityManager");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(121, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void activityStopped(IBinder paramIBinder, Bundle paramBundle, PersistableBundle paramPersistableBundle, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IActivityManager");
          localParcel.writeStrongBinder(paramIBinder);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramPersistableBundle != null)
          {
            localParcel.writeInt(1);
            paramPersistableBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramCharSequence != null)
          {
            localParcel.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public int addAppTask(IBinder paramIBinder, Intent paramIntent, ActivityManager.TaskDescription paramTaskDescription, Bitmap paramBitmap)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramTaskDescription != null)
          {
            localParcel1.writeInt(1);
            paramTaskDescription.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBitmap != null)
          {
            localParcel1.writeInt(1);
            paramBitmap.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(208, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addInstrumentationResults(IApplicationThread paramIApplicationThread, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addPackageDependency(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          mRemote.transact(93, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void alwaysShowUnsupportedCompileSdkWarning(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(305, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void appNotRespondingViaProvider(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(184, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void attachApplication(IApplicationThread paramIApplicationThread, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeLong(paramLong);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void backgroundWhitelistUid(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(298, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void backupAgentCreated(String paramString, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(89, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean bindBackupAgent(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(88, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int bindService(IApplicationThread paramIApplicationThread, IBinder paramIBinder, Intent paramIntent, String paramString1, IServiceConnection paramIServiceConnection, int paramInt1, String paramString2, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          Object localObject = null;
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          paramIApplicationThread = localObject;
          if (paramIServiceConnection != null) {
            paramIApplicationThread = paramIServiceConnection.asBinder();
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void bootAnimationComplete()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(212, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public int broadcastIntent(IApplicationThread paramIApplicationThread, Intent paramIntent, String paramString1, IIntentReceiver paramIIntentReceiver, int paramInt1, String paramString2, Bundle paramBundle1, String[] paramArrayOfString, int paramInt2, Bundle paramBundle2, boolean paramBoolean1, boolean paramBoolean2, int paramInt3)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 14
        //   5: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 15
        //   10: aload 14
        //   12: ldc 31
        //   14: invokevirtual 35	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aconst_null
        //   18: astore 16
        //   20: aload_1
        //   21: ifnull +13 -> 34
        //   24: aload_1
        //   25: invokeinterface 103 1 0
        //   30: astore_1
        //   31: goto +5 -> 36
        //   34: aconst_null
        //   35: astore_1
        //   36: aload 14
        //   38: aload_1
        //   39: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   42: aload_2
        //   43: ifnull +19 -> 62
        //   46: aload 14
        //   48: iconst_1
        //   49: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   52: aload_2
        //   53: aload 14
        //   55: iconst_0
        //   56: invokevirtual 85	android/content/Intent:writeToParcel	(Landroid/os/Parcel;I)V
        //   59: goto +9 -> 68
        //   62: aload 14
        //   64: iconst_0
        //   65: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   68: aload 14
        //   70: aload_3
        //   71: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   74: aload 16
        //   76: astore_1
        //   77: aload 4
        //   79: ifnull +11 -> 90
        //   82: aload 4
        //   84: invokeinterface 135 1 0
        //   89: astore_1
        //   90: aload 14
        //   92: aload_1
        //   93: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   96: aload 14
        //   98: iload 5
        //   100: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   103: aload 14
        //   105: aload 6
        //   107: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   110: aload 7
        //   112: ifnull +20 -> 132
        //   115: aload 14
        //   117: iconst_1
        //   118: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   121: aload 7
        //   123: aload 14
        //   125: iconst_0
        //   126: invokevirtual 72	android/os/Bundle:writeToParcel	(Landroid/os/Parcel;I)V
        //   129: goto +9 -> 138
        //   132: aload 14
        //   134: iconst_0
        //   135: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   138: aload 14
        //   140: aload 8
        //   142: invokevirtual 139	android/os/Parcel:writeStringArray	([Ljava/lang/String;)V
        //   145: aload 14
        //   147: iload 9
        //   149: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   152: aload 10
        //   154: ifnull +20 -> 174
        //   157: aload 14
        //   159: iconst_1
        //   160: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   163: aload 10
        //   165: aload 14
        //   167: iconst_0
        //   168: invokevirtual 72	android/os/Bundle:writeToParcel	(Landroid/os/Parcel;I)V
        //   171: goto +9 -> 180
        //   174: aload 14
        //   176: iconst_0
        //   177: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   180: aload 14
        //   182: iload 11
        //   184: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   187: aload 14
        //   189: iload 12
        //   191: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   194: aload 14
        //   196: iload 13
        //   198: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   201: aload_0
        //   202: getfield 19	android/app/IActivityManager$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   205: bipush 11
        //   207: aload 14
        //   209: aload 15
        //   211: iconst_0
        //   212: invokeinterface 44 5 0
        //   217: pop
        //   218: aload 15
        //   220: invokevirtual 64	android/os/Parcel:readException	()V
        //   223: aload 15
        //   225: invokevirtual 95	android/os/Parcel:readInt	()I
        //   228: istore 5
        //   230: aload 15
        //   232: invokevirtual 47	android/os/Parcel:recycle	()V
        //   235: aload 14
        //   237: invokevirtual 47	android/os/Parcel:recycle	()V
        //   240: iload 5
        //   242: ireturn
        //   243: astore_1
        //   244: goto +32 -> 276
        //   247: astore_1
        //   248: goto +28 -> 276
        //   251: astore_1
        //   252: goto +24 -> 276
        //   255: astore_1
        //   256: goto +20 -> 276
        //   259: astore_1
        //   260: goto +16 -> 276
        //   263: astore_1
        //   264: goto +12 -> 276
        //   267: astore_1
        //   268: goto +8 -> 276
        //   271: astore_1
        //   272: goto +4 -> 276
        //   275: astore_1
        //   276: aload 15
        //   278: invokevirtual 47	android/os/Parcel:recycle	()V
        //   281: aload 14
        //   283: invokevirtual 47	android/os/Parcel:recycle	()V
        //   286: aload_1
        //   287: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	288	0	this	Proxy
        //   0	288	1	paramIApplicationThread	IApplicationThread
        //   0	288	2	paramIntent	Intent
        //   0	288	3	paramString1	String
        //   0	288	4	paramIIntentReceiver	IIntentReceiver
        //   0	288	5	paramInt1	int
        //   0	288	6	paramString2	String
        //   0	288	7	paramBundle1	Bundle
        //   0	288	8	paramArrayOfString	String[]
        //   0	288	9	paramInt2	int
        //   0	288	10	paramBundle2	Bundle
        //   0	288	11	paramBoolean1	boolean
        //   0	288	12	paramBoolean2	boolean
        //   0	288	13	paramInt3	int
        //   3	279	14	localParcel1	Parcel
        //   8	269	15	localParcel2	Parcel
        //   18	57	16	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   194	230	243	finally
        //   187	194	247	finally
        //   180	187	251	finally
        //   145	152	255	finally
        //   157	171	255	finally
        //   174	180	255	finally
        //   138	145	259	finally
        //   103	110	263	finally
        //   115	129	263	finally
        //   132	138	263	finally
        //   96	103	267	finally
        //   68	74	271	finally
        //   82	90	271	finally
        //   90	96	271	finally
        //   10	17	275	finally
        //   24	31	275	finally
        //   36	42	275	finally
        //   46	59	275	finally
        //   62	68	275	finally
      }
      
      public void cancelIntentSender(IIntentSender paramIIntentSender)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          mRemote.transact(60, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void cancelRecentsAnimation(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(197, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void cancelTaskWindowTransition(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(291, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int checkGrantUriPermission(int paramInt1, String paramString, Uri paramUri, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(117, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int checkPermission(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int checkPermissionWithToken(String paramString, int paramInt1, int paramInt2, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(216, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int checkUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3, int paramInt4, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean clearApplicationUserData(String paramString, boolean paramBoolean, IPackageDataObserver paramIPackageDataObserver, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          if (paramIPackageDataObserver != null) {
            paramString = paramIPackageDataObserver.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(76, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearGrantedUriPermissions(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(264, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearPendingBackup()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(160, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void closeSystemDialogs(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          mRemote.transact(95, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean convertFromTranslucent(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(175, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean convertToTranslucent(IBinder paramIBinder, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          boolean bool = true;
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(176, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void crashApplication(int paramInt1, int paramInt2, String paramString1, int paramInt3, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString2);
          mRemote.transact(112, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int createStackOnDisplay(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(220, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dismissKeyguard(IBinder paramIBinder, IKeyguardDismissCallback paramIKeyguardDismissCallback, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramIKeyguardDismissCallback != null) {
            paramIBinder = paramIKeyguardDismissCallback.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(289, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dismissPip(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(246, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dismissSplitScreenMode(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(245, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean dumpHeap(String paramString1, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString2, ParcelFileDescriptor paramParcelFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          localParcel1.writeInt(paramBoolean3);
          localParcel1.writeString(paramString2);
          boolean bool = true;
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(118, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dumpHeapFinished(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          mRemote.transact(226, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean enterPictureInPictureMode(IBinder paramIBinder, PictureInPictureParams paramPictureInPictureParams)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          boolean bool = true;
          if (paramPictureInPictureParams != null)
          {
            localParcel1.writeInt(1);
            paramPictureInPictureParams.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(255, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enterSafeMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(64, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void exitFreeformMode(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(242, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean finishActivity(IBinder paramIBinder, int paramInt1, Intent paramIntent, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          boolean bool = true;
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean finishActivityAffinity(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(146, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void finishHeavyWeightApp()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(107, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void finishInstrumentation(IApplicationThread paramIApplicationThread, int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeInt(paramInt);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void finishReceiver(IBinder paramIBinder, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IActivityManager");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramInt1);
          localParcel.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt2);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void finishSubActivity(IBinder paramIBinder, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void finishVoiceTask(IVoiceInteractionSession paramIVoiceInteractionSession)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIVoiceInteractionSession != null) {
            paramIVoiceInteractionSession = paramIVoiceInteractionSession.asBinder();
          } else {
            paramIVoiceInteractionSession = null;
          }
          localParcel1.writeStrongBinder(paramIVoiceInteractionSession);
          mRemote.transact(203, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void forceStopNativeProcess(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          mRemote.transact(306, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void forceStopPackage(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(77, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName getActivityClassForToken(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIBinder = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIBinder = null;
          }
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getActivityDisplayId(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(186, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle getActivityOptions(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(199, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIBinder = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIBinder = null;
          }
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ActivityManager.StackInfo> getAllStackInfos()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(171, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(ActivityManager.StackInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Point getAppTaskThumbnailSize()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(209, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Point localPoint;
          if (localParcel2.readInt() != 0) {
            localPoint = (Point)Point.CREATOR.createFromParcel(localParcel2);
          } else {
            localPoint = null;
          }
          return localPoint;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<IBinder> getAppTasks(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          mRemote.transact(200, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createBinderArrayList();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle getAssistContextExtras(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(162, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Bundle localBundle;
          if (localParcel2.readInt() != 0) {
            localBundle = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            localBundle = null;
          }
          return localBundle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName getCallingActivity(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIBinder = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIBinder = null;
          }
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getCallingPackage(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIBinder = localParcel2.readString();
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Configuration getConfiguration()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Configuration localConfiguration;
          if (localParcel2.readInt() != 0) {
            localConfiguration = (Configuration)Configuration.CREATOR.createFromParcel(localParcel2);
          } else {
            localConfiguration = null;
          }
          return localConfiguration;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ContentProviderHolder getContentProvider(IApplicationThread paramIApplicationThread, String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          Object localObject = null;
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIApplicationThread = (ContentProviderHolder)ContentProviderHolder.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIApplicationThread = localObject;
          }
          return paramIApplicationThread;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ContentProviderHolder getContentProviderExternal(String paramString, int paramInt, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(138, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ContentProviderHolder)ContentProviderHolder.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public UserInfo getCurrentUser()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(142, localParcel1, localParcel2, 0);
          localParcel2.readException();
          UserInfo localUserInfo;
          if (localParcel2.readInt() != 0) {
            localUserInfo = (UserInfo)UserInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localUserInfo = null;
          }
          return localUserInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ConfigurationInfo getDeviceConfigurationInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(82, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ConfigurationInfo localConfigurationInfo;
          if (localParcel2.readInt() != 0) {
            localConfigurationInfo = (ConfigurationInfo)ConfigurationInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localConfigurationInfo = null;
          }
          return localConfigurationInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ActivityManager.RunningTaskInfo> getFilteredTasks(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(ActivityManager.RunningTaskInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getFocusedAppNotchUiMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(313, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getFocusedAppScaleMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(309, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ActivityManager.StackInfo getFocusedStackInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(173, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ActivityManager.StackInfo localStackInfo;
          if (localParcel2.readInt() != 0) {
            localStackInfo = (ActivityManager.StackInfo)ActivityManager.StackInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localStackInfo = null;
          }
          return localStackInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getFrontActivityScreenCompatMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(122, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getGrantedUriPermissions(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(263, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Intent getIntentForIntentSender(IIntentSender paramIIntentSender)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          Object localObject = null;
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          mRemote.transact(161, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIIntentSender = (Intent)Intent.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIIntentSender = localObject;
          }
          return paramIIntentSender;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IIntentSender getIntentSender(int paramInt1, String paramString1, IBinder paramIBinder, String paramString2, int paramInt2, Intent[] paramArrayOfIntent, String[] paramArrayOfString, int paramInt3, Bundle paramBundle, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString1);
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeTypedArray(paramArrayOfIntent, 0);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramInt3);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt4);
          mRemote.transact(59, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = IIntentSender.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.IActivityManager";
      }
      
      public int getLastResumedActivityUserId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(297, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getLaunchedFromPackage(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(164, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIBinder = localParcel2.readString();
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getLaunchedFromUid(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(147, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getLockTaskModeState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(224, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getMaxNumPictureInPictureActions(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(257, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getMemoryInfo(ActivityManager.MemoryInfo paramMemoryInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(74, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramMemoryInfo.readFromParcel(localParcel2);
          }
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getMemoryTrimLevel()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(275, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getMyMemoryState(ActivityManager.RunningAppProcessInfo paramRunningAppProcessInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(140, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramRunningAppProcessInfo.readFromParcel(localParcel2);
          }
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getPackageAskScreenCompat(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(126, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getPackageForIntentSender(IIntentSender paramIIntentSender)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          mRemote.transact(61, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIIntentSender = localParcel2.readString();
          return paramIIntentSender;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getPackageForToken(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIBinder = localParcel2.readString();
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPackageProcessState(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(231, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPackageScreenCompatMode(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          mRemote.transact(124, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getPersistedUriPermissions(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(183, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getProcessLimit()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Debug.MemoryInfo[] getProcessMemoryInfo(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(96, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfInt = (Debug.MemoryInfo[])localParcel2.createTypedArray(Debug.MemoryInfo.CREATOR);
          return paramArrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long[] getProcessPss(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(135, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfInt = localParcel2.createLongArray();
          return paramArrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(75, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(ActivityManager.ProcessErrorStateInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getProviderMimeType(Uri paramUri, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(113, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramUri = localParcel2.readString();
          return paramUri;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getRecentTasks(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(56, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParceledListSlice localParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localParceledListSlice = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localParceledListSlice = null;
          }
          return localParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getRequestedOrientation(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(69, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(81, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(ActivityManager.RunningAppProcessInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ApplicationInfo> getRunningExternalApplications()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(106, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(ApplicationInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public PendingIntent getRunningServiceControlPanel(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (PendingIntent)PendingIntent.CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getRunningUserIds()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(155, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ActivityManager.RunningServiceInfo> getServices(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(79, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(ActivityManager.RunningServiceInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ActivityManager.StackInfo getStackInfo(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(174, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ActivityManager.StackInfo localStackInfo;
          if (localParcel2.readInt() != 0) {
            localStackInfo = (ActivityManager.StackInfo)ActivityManager.StackInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localStackInfo = null;
          }
          return localStackInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getTagForIntentSender(IIntentSender paramIIntentSender, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          localParcel1.writeString(paramString);
          mRemote.transact(188, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIIntentSender = localParcel2.readString();
          return paramIIntentSender;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Rect getTaskBounds(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(185, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Rect localRect;
          if (localParcel2.readInt() != 0) {
            localRect = (Rect)Rect.CREATOR.createFromParcel(localParcel2);
          } else {
            localRect = null;
          }
          return localRect;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ActivityManager.TaskDescription getTaskDescription(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(80, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ActivityManager.TaskDescription localTaskDescription;
          if (localParcel2.readInt() != 0) {
            localTaskDescription = (ActivityManager.TaskDescription)ActivityManager.TaskDescription.CREATOR.createFromParcel(localParcel2);
          } else {
            localTaskDescription = null;
          }
          return localTaskDescription;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bitmap getTaskDescriptionIcon(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(213, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Bitmap)Bitmap.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getTaskForActivity(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          return paramBoolean;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ActivityManager.TaskSnapshot getTaskSnapshot(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(292, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ActivityManager.TaskSnapshot localTaskSnapshot;
          if (localParcel2.readInt() != 0) {
            localTaskSnapshot = (ActivityManager.TaskSnapshot)ActivityManager.TaskSnapshot.CREATOR.createFromParcel(localParcel2);
          } else {
            localTaskSnapshot = null;
          }
          return localTaskSnapshot;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ActivityManager.RunningTaskInfo> getTasks(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(ActivityManager.RunningTaskInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getUidForIntentSender(IIntentSender paramIIntentSender)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          mRemote.transact(91, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getUidProcessState(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(235, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder getUriPermissionOwnerForActivity(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(259, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIBinder = localParcel2.readStrongBinder();
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getVisibleAppScaleMode(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(310, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void grantUriPermission(IApplicationThread paramIApplicationThread, String paramString, Uri paramUri, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeString(paramString);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void grantUriPermissionFromOwner(IBinder paramIBinder, int paramInt1, String paramString, Uri paramUri, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(115, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void handleApplicationCrash(IBinder paramIBinder, ApplicationErrorReport.ParcelableCrashInfo paramParcelableCrashInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramParcelableCrashInfo != null)
          {
            localParcel1.writeInt(1);
            paramParcelableCrashInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void handleApplicationStrictModeViolation(IBinder paramIBinder, int paramInt, StrictMode.ViolationInfo paramViolationInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          if (paramViolationInfo != null)
          {
            localParcel1.writeInt(1);
            paramViolationInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(108, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean handleApplicationWtf(IBinder paramIBinder, String paramString, boolean paramBoolean, ApplicationErrorReport.ParcelableCrashInfo paramParcelableCrashInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          boolean bool = true;
          if (paramParcelableCrashInfo != null)
          {
            localParcel1.writeInt(1);
            paramParcelableCrashInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(100, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int handleIncomingUser(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(92, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void hang(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(167, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long inputDispatchingTimedOut(int paramInt, boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
          mRemote.transact(159, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isAppForeground(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(265, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isAppStartModeDisabled(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(250, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isAssistDataAllowedOnCurrentActivity()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(236, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isBackgroundRestricted(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(282, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isImmersive(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(109, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isInLockTaskMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(192, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isInMultiWindowMode(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(252, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isInPictureInPictureMode(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(253, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isIntentSenderAForegroundService(IIntentSender paramIIntentSender)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          paramIIntentSender = mRemote;
          boolean bool = false;
          paramIIntentSender.transact(150, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isIntentSenderAnActivity(IIntentSender paramIIntentSender)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          paramIIntentSender = mRemote;
          boolean bool = false;
          paramIIntentSender.transact(149, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isIntentSenderTargetedToPackage(IIntentSender paramIIntentSender)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          paramIIntentSender = mRemote;
          boolean bool = false;
          paramIIntentSender.transact(133, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isRestrictStartActivity(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(308, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isRootVoiceInteraction(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(238, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isTopActivityImmersive()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(111, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isTopOfTask(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(204, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isUidActive(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isUserAMonkey()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(102, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isUserRunning(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(120, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isVrModePackageEnabled(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(277, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void keyguardGoingAway(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(234, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void killAllBackgroundProcesses()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(137, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void killApplication(String paramString1, int paramInt1, int paramInt2, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString2);
          mRemote.transact(94, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void killApplicationProcess(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(97, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void killBackgroundProcesses(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(101, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void killPackageDependents(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(254, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean killPids(int[] paramArrayOfInt, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeIntArray(paramArrayOfInt);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          paramArrayOfInt = mRemote;
          boolean bool = false;
          paramArrayOfInt.transact(78, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean killProcessesBelowForeground(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(141, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void killUid(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          mRemote.transact(165, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean launchAssistIntent(Intent paramIntent, int paramInt1, String paramString, int paramInt2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          boolean bool = true;
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(214, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void makePackageIdle(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(274, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean moveActivityTaskToBack(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(73, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void moveStackToDisplay(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(287, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void moveTaskBackwards(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void moveTaskToFront(int paramInt1, int paramInt2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void moveTaskToStack(int paramInt1, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(169, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void moveTasksToFullscreenStack(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(248, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean moveTopActivityToPinnedStack(int paramInt, Rect paramRect)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          boolean bool = true;
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(249, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean navigateUpTo(IBinder paramIBinder, Intent paramIntent1, int paramInt, Intent paramIntent2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          boolean bool = true;
          if (paramIntent1 != null)
          {
            localParcel1.writeInt(1);
            paramIntent1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          if (paramIntent2 != null)
          {
            localParcel1.writeInt(1);
            paramIntent2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(144, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder newUriPermissionOwner(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          mRemote.transact(114, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readStrongBinder();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteAlarmFinish(IIntentSender paramIIntentSender, WorkSource paramWorkSource, int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(230, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteAlarmStart(IIntentSender paramIIntentSender, WorkSource paramWorkSource, int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(229, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWakeupAlarm(IIntentSender paramIIntentSender, WorkSource paramWorkSource, int paramInt, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(66, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyActivityDrawn(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(177, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyCleartextNetwork(int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(219, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyEnterAnimationComplete(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(206, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyLaunchTaskBehindComplete(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(205, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyLockedProfile(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(278, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyPinnedStackAnimationEnded()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(270, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyPinnedStackAnimationStarted()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(269, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParcelFileDescriptor openContentUri(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void overridePendingTransition(IBinder paramIBinder, String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(99, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder peekService(Intent paramIntent, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(83, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIntent = localParcel2.readStrongBinder();
          return paramIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void performIdleMaintenance()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(180, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void positionTaskInStack(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(241, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean profileControl(String paramString, int paramInt1, boolean paramBoolean, ProfilerInfo paramProfilerInfo, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramBoolean);
          boolean bool = true;
          if (paramProfilerInfo != null)
          {
            localParcel1.writeInt(1);
            paramProfilerInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(84, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void publishContentProviders(IApplicationThread paramIApplicationThread, List<ContentProviderHolder> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeTypedList(paramList);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void publishService(IBinder paramIBinder1, Intent paramIntent, IBinder paramIBinder2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder1);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder2);
          mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean refContentProvider(IBinder paramIBinder, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerIntentSenderCancelListener(IIntentSender paramIIntentSender, IResultReceiver paramIResultReceiver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          Object localObject = null;
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          paramIIntentSender = localObject;
          if (paramIResultReceiver != null) {
            paramIIntentSender = paramIResultReceiver.asBinder();
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          mRemote.transact(62, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerProcessObserver(IProcessObserver paramIProcessObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIProcessObserver != null) {
            paramIProcessObserver = paramIProcessObserver.asBinder();
          } else {
            paramIProcessObserver = null;
          }
          localParcel1.writeStrongBinder(paramIProcessObserver);
          mRemote.transact(131, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Intent registerReceiver(IApplicationThread paramIApplicationThread, String paramString1, IIntentReceiver paramIIntentReceiver, IntentFilter paramIntentFilter, String paramString2, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          Object localObject = null;
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeString(paramString1);
          if (paramIIntentReceiver != null) {
            paramIApplicationThread = paramIIntentReceiver.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          if (paramIntentFilter != null)
          {
            localParcel1.writeInt(1);
            paramIntentFilter.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIApplicationThread = (Intent)Intent.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIApplicationThread = localObject;
          }
          return paramIApplicationThread;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerRemoteAnimationForNextActivityStart(String paramString, RemoteAnimationAdapter paramRemoteAnimationAdapter)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          if (paramRemoteAnimationAdapter != null)
          {
            localParcel1.writeInt(1);
            paramRemoteAnimationAdapter.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(304, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerRemoteAnimations(IBinder paramIBinder, RemoteAnimationDefinition paramRemoteAnimationDefinition)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramRemoteAnimationDefinition != null)
          {
            localParcel1.writeInt(1);
            paramRemoteAnimationDefinition.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(303, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerTaskStackListener(ITaskStackListener paramITaskStackListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramITaskStackListener != null) {
            paramITaskStackListener = paramITaskStackListener.asBinder();
          } else {
            paramITaskStackListener = null;
          }
          localParcel1.writeStrongBinder(paramITaskStackListener);
          mRemote.transact(217, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerUidObserver(IUidObserver paramIUidObserver, int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIUidObserver != null) {
            paramIUidObserver = paramIUidObserver.asBinder();
          } else {
            paramIUidObserver = null;
          }
          localParcel1.writeStrongBinder(paramIUidObserver);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerUserSwitchObserver(IUserSwitchObserver paramIUserSwitchObserver, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIUserSwitchObserver != null) {
            paramIUserSwitchObserver = paramIUserSwitchObserver.asBinder();
          } else {
            paramIUserSwitchObserver = null;
          }
          localParcel1.writeStrongBinder(paramIUserSwitchObserver);
          localParcel1.writeString(paramString);
          mRemote.transact(153, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean releaseActivityInstance(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(210, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void releasePersistableUriPermission(Uri paramUri, int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(182, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void releaseSomeActivities(IApplicationThread paramIApplicationThread)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          mRemote.transact(211, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeContentProvider(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(67, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeContentProviderExternal(String paramString, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(139, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeStack(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(271, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeStacksInWindowingModes(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(272, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeStacksWithActivityTypes(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(273, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean removeTask(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(130, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportActivityFullyDrawn(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(178, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportAssistContextExtras(IBinder paramIBinder, Bundle paramBundle, AssistStructure paramAssistStructure, AssistContent paramAssistContent, Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramAssistStructure != null)
          {
            localParcel1.writeInt(1);
            paramAssistStructure.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramAssistContent != null)
          {
            localParcel1.writeInt(1);
            paramAssistContent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(163, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportSizeConfigurations(IBinder paramIBinder, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeIntArray(paramArrayOfInt1);
          localParcel1.writeIntArray(paramArrayOfInt2);
          localParcel1.writeIntArray(paramArrayOfInt3);
          mRemote.transact(243, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean requestAssistContextExtras(int paramInt, IAssistDataReceiver paramIAssistDataReceiver, Bundle paramBundle, IBinder paramIBinder, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          if (paramIAssistDataReceiver != null) {
            paramIAssistDataReceiver = paramIAssistDataReceiver.asBinder();
          } else {
            paramIAssistDataReceiver = null;
          }
          localParcel1.writeStrongBinder(paramIAssistDataReceiver);
          boolean bool = true;
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          mRemote.transact(222, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean requestAutofillData(IAssistDataReceiver paramIAssistDataReceiver, Bundle paramBundle, IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIAssistDataReceiver != null) {
            paramIAssistDataReceiver = paramIAssistDataReceiver.asBinder();
          } else {
            paramIAssistDataReceiver = null;
          }
          localParcel1.writeStrongBinder(paramIAssistDataReceiver);
          boolean bool = true;
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(288, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestBugReport(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(156, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestFocusedAppFillNotchRegion(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(314, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestFocusedAppFitScreen(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(311, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestTelephonyBugReport(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(157, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestVisibleAppFitScreen(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(312, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestWifiBugReport(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(158, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resizeDockedStack(Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, Rect paramRect5)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramRect1 != null)
          {
            localParcel1.writeInt(1);
            paramRect1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRect2 != null)
          {
            localParcel1.writeInt(1);
            paramRect2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRect3 != null)
          {
            localParcel1.writeInt(1);
            paramRect3.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRect4 != null)
          {
            localParcel1.writeInt(1);
            paramRect4.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRect5 != null)
          {
            localParcel1.writeInt(1);
            paramRect5.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(260, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resizePinnedStack(Rect paramRect1, Rect paramRect2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramRect1 != null)
          {
            localParcel1.writeInt(1);
            paramRect1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRect2 != null)
          {
            localParcel1.writeInt(1);
            paramRect2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(276, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resizeStack(int paramInt1, Rect paramRect, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          localParcel1.writeInt(paramBoolean3);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(170, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resizeTask(int paramInt1, Rect paramRect, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(223, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void restart()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(179, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int restartUserInBackground(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(290, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void restrictStartActivity(int paramInt, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(307, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resumeAppSwitches()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(87, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void revokeUriPermission(IApplicationThread paramIApplicationThread, String paramString, Uri paramUri, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeString(paramString);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(52, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void revokeUriPermissionFromOwner(IBinder paramIBinder, Uri paramUri, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(116, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void scheduleApplicationInfoChanged(List<String> paramList, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStringList(paramList);
          localParcel1.writeInt(paramInt);
          mRemote.transact(293, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendIdleJobTrigger()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(280, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int sendIntentSender(IIntentSender paramIIntentSender, IBinder paramIBinder, int paramInt, Intent paramIntent, String paramString1, IIntentReceiver paramIIntentReceiver, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          Object localObject = null;
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          paramIIntentSender = localObject;
          if (paramIIntentReceiver != null) {
            paramIIntentSender = paramIIntentReceiver.asBinder();
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          localParcel1.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(281, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void serviceDoneExecuting(IBinder paramIBinder, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IActivityManager");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(57, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setActivityController(IActivityController paramIActivityController, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIActivityController != null) {
            paramIActivityController = paramIActivityController.asBinder();
          } else {
            paramIActivityController = null;
          }
          localParcel1.writeStrongBinder(paramIActivityController);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(53, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAgentApp(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAlwaysFinish(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDebugApp(String paramString, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDisablePreviewScreenshots(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(296, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDumpHeapDebugLimit(String paramString1, int paramInt, long paramLong, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeLong(paramLong);
          localParcel1.writeString(paramString2);
          mRemote.transact(225, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setFocusedStack(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(172, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setFocusedTask(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(129, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setFrontActivityScreenCompatMode(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(123, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setHasTopUi(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(285, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setImmersive(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(110, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setLockScreenShown(boolean paramBoolean1, boolean paramBoolean2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(145, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPackageAskScreenCompat(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(127, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPackageScreenCompatMode(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(125, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPersistentVrThread(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(294, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPictureInPictureParams(IBinder paramIBinder, PictureInPictureParams paramPictureInPictureParams)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramPictureInPictureParams != null)
          {
            localParcel1.writeInt(1);
            paramPictureInPictureParams.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(256, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setProcessImportant(IBinder paramIBinder, int paramInt, boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
          mRemote.transact(71, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setProcessLimit(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setProcessMemoryTrimLevel(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(187, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRenderThread(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(284, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRequestedOrientation(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(68, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setServiceForeground(ComponentName paramComponentName, IBinder paramIBinder, int paramInt1, Notification paramNotification, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          if (paramNotification != null)
          {
            localParcel1.writeInt(1);
            paramNotification.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(72, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setShowWhenLocked(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(300, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSplitScreenResizing(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(261, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setTaskDescription(IBinder paramIBinder, ActivityManager.TaskDescription paramTaskDescription)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramTaskDescription != null)
          {
            localParcel1.writeInt(1);
            paramTaskDescription.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(193, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setTaskResizeable(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(221, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setTaskWindowingMode(int paramInt1, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(168, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setTaskWindowingModeSplitScreenPrimary(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, Rect paramRect, boolean paramBoolean3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          boolean bool = true;
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean3);
          mRemote.transact(244, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setTurnScreenOn(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(301, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUserIsMonkey(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(166, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVoiceKeepAwake(IVoiceInteractionSession paramIVoiceInteractionSession, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIVoiceInteractionSession != null) {
            paramIVoiceInteractionSession = paramIVoiceInteractionSession.asBinder();
          } else {
            paramIVoiceInteractionSession = null;
          }
          localParcel1.writeStrongBinder(paramIVoiceInteractionSession);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(227, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int setVrMode(IBinder paramIBinder, boolean paramBoolean, ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(262, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          return paramBoolean;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVrThread(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(283, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean shouldUpRecreateTask(IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(143, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean showAssistFromActivity(IBinder paramIBinder, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          boolean bool = true;
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(237, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void showBootMessage(CharSequence paramCharSequence, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(136, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void showLockTaskEscapeMessage(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IActivityManager");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(232, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showWaitingForDebugger(IApplicationThread paramIApplicationThread, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(54, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean shutdown(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(85, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void signalPersistentProcesses(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(55, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int startActivities(IApplicationThread paramIApplicationThread, String paramString, Intent[] paramArrayOfIntent, String[] paramArrayOfString, IBinder paramIBinder, Bundle paramBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeString(paramString);
          localParcel1.writeTypedArray(paramArrayOfIntent, 0);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(119, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int startActivity(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, ProfilerInfo paramProfilerInfo, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeString(paramString1);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString2);
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramProfilerInfo != null)
          {
            localParcel1.writeInt(1);
            paramProfilerInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public WaitResult startActivityAndWait(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, ProfilerInfo paramProfilerInfo, Bundle paramBundle, int paramInt3)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 13
        //   10: aload 12
        //   12: ldc 31
        //   14: invokevirtual 35	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload_1
        //   18: ifnull +13 -> 31
        //   21: aload_1
        //   22: invokeinterface 103 1 0
        //   27: astore_1
        //   28: goto +5 -> 33
        //   31: aconst_null
        //   32: astore_1
        //   33: aload 12
        //   35: aload_1
        //   36: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   39: aload 12
        //   41: aload_2
        //   42: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   45: aload_3
        //   46: ifnull +19 -> 65
        //   49: aload 12
        //   51: iconst_1
        //   52: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   55: aload_3
        //   56: aload 12
        //   58: iconst_0
        //   59: invokevirtual 85	android/content/Intent:writeToParcel	(Landroid/os/Parcel;I)V
        //   62: goto +9 -> 71
        //   65: aload 12
        //   67: iconst_0
        //   68: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   71: aload 12
        //   73: aload 4
        //   75: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   78: aload 12
        //   80: aload 5
        //   82: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   85: aload 12
        //   87: aload 6
        //   89: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   92: aload 12
        //   94: iload 7
        //   96: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   99: aload 12
        //   101: iload 8
        //   103: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   106: aload 9
        //   108: ifnull +20 -> 128
        //   111: aload 12
        //   113: iconst_1
        //   114: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   117: aload 9
        //   119: aload 12
        //   121: iconst_0
        //   122: invokevirtual 559	android/app/ProfilerInfo:writeToParcel	(Landroid/os/Parcel;I)V
        //   125: goto +9 -> 134
        //   128: aload 12
        //   130: iconst_0
        //   131: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   134: aload 10
        //   136: ifnull +20 -> 156
        //   139: aload 12
        //   141: iconst_1
        //   142: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   145: aload 10
        //   147: aload 12
        //   149: iconst_0
        //   150: invokevirtual 72	android/os/Bundle:writeToParcel	(Landroid/os/Parcel;I)V
        //   153: goto +9 -> 162
        //   156: aload 12
        //   158: iconst_0
        //   159: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   162: aload 12
        //   164: iload 11
        //   166: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   169: aload_0
        //   170: getfield 19	android/app/IActivityManager$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   173: bipush 103
        //   175: aload 12
        //   177: aload 13
        //   179: iconst_0
        //   180: invokeinterface 44 5 0
        //   185: pop
        //   186: aload 13
        //   188: invokevirtual 64	android/os/Parcel:readException	()V
        //   191: aload 13
        //   193: invokevirtual 95	android/os/Parcel:readInt	()I
        //   196: ifeq +20 -> 216
        //   199: getstatic 744	android/app/WaitResult:CREATOR	Landroid/os/Parcelable$Creator;
        //   202: aload 13
        //   204: invokeinterface 225 2 0
        //   209: checkcast 743	android/app/WaitResult
        //   212: astore_1
        //   213: goto +5 -> 218
        //   216: aconst_null
        //   217: astore_1
        //   218: aload 13
        //   220: invokevirtual 47	android/os/Parcel:recycle	()V
        //   223: aload 12
        //   225: invokevirtual 47	android/os/Parcel:recycle	()V
        //   228: aload_1
        //   229: areturn
        //   230: astore_1
        //   231: goto +32 -> 263
        //   234: astore_1
        //   235: goto +28 -> 263
        //   238: astore_1
        //   239: goto +24 -> 263
        //   242: astore_1
        //   243: goto +20 -> 263
        //   246: astore_1
        //   247: goto +16 -> 263
        //   250: astore_1
        //   251: goto +12 -> 263
        //   254: astore_1
        //   255: goto +8 -> 263
        //   258: astore_1
        //   259: goto +4 -> 263
        //   262: astore_1
        //   263: aload 13
        //   265: invokevirtual 47	android/os/Parcel:recycle	()V
        //   268: aload 12
        //   270: invokevirtual 47	android/os/Parcel:recycle	()V
        //   273: aload_1
        //   274: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	275	0	this	Proxy
        //   0	275	1	paramIApplicationThread	IApplicationThread
        //   0	275	2	paramString1	String
        //   0	275	3	paramIntent	Intent
        //   0	275	4	paramString2	String
        //   0	275	5	paramIBinder	IBinder
        //   0	275	6	paramString3	String
        //   0	275	7	paramInt1	int
        //   0	275	8	paramInt2	int
        //   0	275	9	paramProfilerInfo	ProfilerInfo
        //   0	275	10	paramBundle	Bundle
        //   0	275	11	paramInt3	int
        //   3	266	12	localParcel1	Parcel
        //   8	256	13	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   169	213	230	finally
        //   162	169	234	finally
        //   99	106	238	finally
        //   111	125	238	finally
        //   128	134	238	finally
        //   139	153	238	finally
        //   156	162	238	finally
        //   92	99	242	finally
        //   85	92	246	finally
        //   78	85	250	finally
        //   71	78	254	finally
        //   39	45	258	finally
        //   49	62	258	finally
        //   65	71	258	finally
        //   10	17	262	finally
        //   21	28	262	finally
        //   33	39	262	finally
      }
      
      /* Error */
      public int startActivityAsCaller(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, ProfilerInfo paramProfilerInfo, Bundle paramBundle, boolean paramBoolean, int paramInt3)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 13
        //   5: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 14
        //   10: aload 13
        //   12: ldc 31
        //   14: invokevirtual 35	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload_1
        //   18: ifnull +13 -> 31
        //   21: aload_1
        //   22: invokeinterface 103 1 0
        //   27: astore_1
        //   28: goto +5 -> 33
        //   31: aconst_null
        //   32: astore_1
        //   33: aload 13
        //   35: aload_1
        //   36: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   39: aload 13
        //   41: aload_2
        //   42: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   45: aload_3
        //   46: ifnull +19 -> 65
        //   49: aload 13
        //   51: iconst_1
        //   52: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   55: aload_3
        //   56: aload 13
        //   58: iconst_0
        //   59: invokevirtual 85	android/content/Intent:writeToParcel	(Landroid/os/Parcel;I)V
        //   62: goto +9 -> 71
        //   65: aload 13
        //   67: iconst_0
        //   68: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   71: aload 13
        //   73: aload 4
        //   75: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   78: aload 13
        //   80: aload 5
        //   82: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   85: aload 13
        //   87: aload 6
        //   89: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   92: aload 13
        //   94: iload 7
        //   96: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   99: aload 13
        //   101: iload 8
        //   103: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   106: aload 9
        //   108: ifnull +20 -> 128
        //   111: aload 13
        //   113: iconst_1
        //   114: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   117: aload 9
        //   119: aload 13
        //   121: iconst_0
        //   122: invokevirtual 559	android/app/ProfilerInfo:writeToParcel	(Landroid/os/Parcel;I)V
        //   125: goto +9 -> 134
        //   128: aload 13
        //   130: iconst_0
        //   131: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   134: aload 10
        //   136: ifnull +20 -> 156
        //   139: aload 13
        //   141: iconst_1
        //   142: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   145: aload 10
        //   147: aload 13
        //   149: iconst_0
        //   150: invokevirtual 72	android/os/Bundle:writeToParcel	(Landroid/os/Parcel;I)V
        //   153: goto +9 -> 162
        //   156: aload 13
        //   158: iconst_0
        //   159: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   162: aload 13
        //   164: iload 11
        //   166: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   169: aload 13
        //   171: iload 12
        //   173: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   176: aload_0
        //   177: getfield 19	android/app/IActivityManager$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   180: sipush 207
        //   183: aload 13
        //   185: aload 14
        //   187: iconst_0
        //   188: invokeinterface 44 5 0
        //   193: pop
        //   194: aload 14
        //   196: invokevirtual 64	android/os/Parcel:readException	()V
        //   199: aload 14
        //   201: invokevirtual 95	android/os/Parcel:readInt	()I
        //   204: istore 7
        //   206: aload 14
        //   208: invokevirtual 47	android/os/Parcel:recycle	()V
        //   211: aload 13
        //   213: invokevirtual 47	android/os/Parcel:recycle	()V
        //   216: iload 7
        //   218: ireturn
        //   219: astore_1
        //   220: goto +36 -> 256
        //   223: astore_1
        //   224: goto +32 -> 256
        //   227: astore_1
        //   228: goto +28 -> 256
        //   231: astore_1
        //   232: goto +24 -> 256
        //   235: astore_1
        //   236: goto +20 -> 256
        //   239: astore_1
        //   240: goto +16 -> 256
        //   243: astore_1
        //   244: goto +12 -> 256
        //   247: astore_1
        //   248: goto +8 -> 256
        //   251: astore_1
        //   252: goto +4 -> 256
        //   255: astore_1
        //   256: aload 14
        //   258: invokevirtual 47	android/os/Parcel:recycle	()V
        //   261: aload 13
        //   263: invokevirtual 47	android/os/Parcel:recycle	()V
        //   266: aload_1
        //   267: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	268	0	this	Proxy
        //   0	268	1	paramIApplicationThread	IApplicationThread
        //   0	268	2	paramString1	String
        //   0	268	3	paramIntent	Intent
        //   0	268	4	paramString2	String
        //   0	268	5	paramIBinder	IBinder
        //   0	268	6	paramString3	String
        //   0	268	7	paramInt1	int
        //   0	268	8	paramInt2	int
        //   0	268	9	paramProfilerInfo	ProfilerInfo
        //   0	268	10	paramBundle	Bundle
        //   0	268	11	paramBoolean	boolean
        //   0	268	12	paramInt3	int
        //   3	259	13	localParcel1	Parcel
        //   8	249	14	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   176	206	219	finally
        //   169	176	223	finally
        //   162	169	227	finally
        //   99	106	231	finally
        //   111	125	231	finally
        //   128	134	231	finally
        //   139	153	231	finally
        //   156	162	231	finally
        //   92	99	235	finally
        //   85	92	239	finally
        //   78	85	243	finally
        //   71	78	247	finally
        //   39	45	251	finally
        //   49	62	251	finally
        //   65	71	251	finally
        //   10	17	255	finally
        //   21	28	255	finally
        //   33	39	255	finally
      }
      
      /* Error */
      public int startActivityAsUser(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, ProfilerInfo paramProfilerInfo, Bundle paramBundle, int paramInt3)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 13
        //   10: aload 12
        //   12: ldc 31
        //   14: invokevirtual 35	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload_1
        //   18: ifnull +13 -> 31
        //   21: aload_1
        //   22: invokeinterface 103 1 0
        //   27: astore_1
        //   28: goto +5 -> 33
        //   31: aconst_null
        //   32: astore_1
        //   33: aload 12
        //   35: aload_1
        //   36: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   39: aload 12
        //   41: aload_2
        //   42: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   45: aload_3
        //   46: ifnull +19 -> 65
        //   49: aload 12
        //   51: iconst_1
        //   52: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   55: aload_3
        //   56: aload 12
        //   58: iconst_0
        //   59: invokevirtual 85	android/content/Intent:writeToParcel	(Landroid/os/Parcel;I)V
        //   62: goto +9 -> 71
        //   65: aload 12
        //   67: iconst_0
        //   68: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   71: aload 12
        //   73: aload 4
        //   75: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   78: aload 12
        //   80: aload 5
        //   82: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   85: aload 12
        //   87: aload 6
        //   89: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   92: aload 12
        //   94: iload 7
        //   96: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   99: aload 12
        //   101: iload 8
        //   103: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   106: aload 9
        //   108: ifnull +20 -> 128
        //   111: aload 12
        //   113: iconst_1
        //   114: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   117: aload 9
        //   119: aload 12
        //   121: iconst_0
        //   122: invokevirtual 559	android/app/ProfilerInfo:writeToParcel	(Landroid/os/Parcel;I)V
        //   125: goto +9 -> 134
        //   128: aload 12
        //   130: iconst_0
        //   131: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   134: aload 10
        //   136: ifnull +20 -> 156
        //   139: aload 12
        //   141: iconst_1
        //   142: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   145: aload 10
        //   147: aload 12
        //   149: iconst_0
        //   150: invokevirtual 72	android/os/Bundle:writeToParcel	(Landroid/os/Parcel;I)V
        //   153: goto +9 -> 162
        //   156: aload 12
        //   158: iconst_0
        //   159: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   162: aload 12
        //   164: iload 11
        //   166: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   169: aload_0
        //   170: getfield 19	android/app/IActivityManager$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   173: sipush 151
        //   176: aload 12
        //   178: aload 13
        //   180: iconst_0
        //   181: invokeinterface 44 5 0
        //   186: pop
        //   187: aload 13
        //   189: invokevirtual 64	android/os/Parcel:readException	()V
        //   192: aload 13
        //   194: invokevirtual 95	android/os/Parcel:readInt	()I
        //   197: istore 7
        //   199: aload 13
        //   201: invokevirtual 47	android/os/Parcel:recycle	()V
        //   204: aload 12
        //   206: invokevirtual 47	android/os/Parcel:recycle	()V
        //   209: iload 7
        //   211: ireturn
        //   212: astore_1
        //   213: goto +32 -> 245
        //   216: astore_1
        //   217: goto +28 -> 245
        //   220: astore_1
        //   221: goto +24 -> 245
        //   224: astore_1
        //   225: goto +20 -> 245
        //   228: astore_1
        //   229: goto +16 -> 245
        //   232: astore_1
        //   233: goto +12 -> 245
        //   236: astore_1
        //   237: goto +8 -> 245
        //   240: astore_1
        //   241: goto +4 -> 245
        //   244: astore_1
        //   245: aload 13
        //   247: invokevirtual 47	android/os/Parcel:recycle	()V
        //   250: aload 12
        //   252: invokevirtual 47	android/os/Parcel:recycle	()V
        //   255: aload_1
        //   256: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	257	0	this	Proxy
        //   0	257	1	paramIApplicationThread	IApplicationThread
        //   0	257	2	paramString1	String
        //   0	257	3	paramIntent	Intent
        //   0	257	4	paramString2	String
        //   0	257	5	paramIBinder	IBinder
        //   0	257	6	paramString3	String
        //   0	257	7	paramInt1	int
        //   0	257	8	paramInt2	int
        //   0	257	9	paramProfilerInfo	ProfilerInfo
        //   0	257	10	paramBundle	Bundle
        //   0	257	11	paramInt3	int
        //   3	248	12	localParcel1	Parcel
        //   8	238	13	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   169	199	212	finally
        //   162	169	216	finally
        //   99	106	220	finally
        //   111	125	220	finally
        //   128	134	220	finally
        //   139	153	220	finally
        //   156	162	220	finally
        //   92	99	224	finally
        //   85	92	228	finally
        //   78	85	232	finally
        //   71	78	236	finally
        //   39	45	240	finally
        //   49	62	240	finally
        //   65	71	240	finally
        //   10	17	244	finally
        //   21	28	244	finally
        //   33	39	244	finally
      }
      
      public int startActivityFromRecents(int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(198, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public int startActivityIntentSender(IApplicationThread paramIApplicationThread, IIntentSender paramIIntentSender, IBinder paramIBinder1, Intent paramIntent, String paramString1, IBinder paramIBinder2, String paramString2, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 13
        //   10: aload 12
        //   12: ldc 31
        //   14: invokevirtual 35	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aconst_null
        //   18: astore 14
        //   20: aload_1
        //   21: ifnull +13 -> 34
        //   24: aload_1
        //   25: invokeinterface 103 1 0
        //   30: astore_1
        //   31: goto +5 -> 36
        //   34: aconst_null
        //   35: astore_1
        //   36: aload 12
        //   38: aload_1
        //   39: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   42: aload 14
        //   44: astore_1
        //   45: aload_2
        //   46: ifnull +10 -> 56
        //   49: aload_2
        //   50: invokeinterface 144 1 0
        //   55: astore_1
        //   56: aload 12
        //   58: aload_1
        //   59: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   62: aload 12
        //   64: aload_3
        //   65: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   68: aload 4
        //   70: ifnull +20 -> 90
        //   73: aload 12
        //   75: iconst_1
        //   76: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   79: aload 4
        //   81: aload 12
        //   83: iconst_0
        //   84: invokevirtual 85	android/content/Intent:writeToParcel	(Landroid/os/Parcel;I)V
        //   87: goto +9 -> 96
        //   90: aload 12
        //   92: iconst_0
        //   93: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   96: aload 12
        //   98: aload 5
        //   100: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   103: aload 12
        //   105: aload 6
        //   107: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   110: aload 12
        //   112: aload 7
        //   114: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   117: aload 12
        //   119: iload 8
        //   121: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   124: aload 12
        //   126: iload 9
        //   128: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   131: aload 12
        //   133: iload 10
        //   135: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   138: aload 11
        //   140: ifnull +20 -> 160
        //   143: aload 12
        //   145: iconst_1
        //   146: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   149: aload 11
        //   151: aload 12
        //   153: iconst_0
        //   154: invokevirtual 72	android/os/Bundle:writeToParcel	(Landroid/os/Parcel;I)V
        //   157: goto +9 -> 166
        //   160: aload 12
        //   162: iconst_0
        //   163: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   166: aload_0
        //   167: getfield 19	android/app/IActivityManager$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   170: bipush 98
        //   172: aload 12
        //   174: aload 13
        //   176: iconst_0
        //   177: invokeinterface 44 5 0
        //   182: pop
        //   183: aload 13
        //   185: invokevirtual 64	android/os/Parcel:readException	()V
        //   188: aload 13
        //   190: invokevirtual 95	android/os/Parcel:readInt	()I
        //   193: istore 8
        //   195: aload 13
        //   197: invokevirtual 47	android/os/Parcel:recycle	()V
        //   200: aload 12
        //   202: invokevirtual 47	android/os/Parcel:recycle	()V
        //   205: iload 8
        //   207: ireturn
        //   208: astore_1
        //   209: goto +32 -> 241
        //   212: astore_1
        //   213: goto +28 -> 241
        //   216: astore_1
        //   217: goto +24 -> 241
        //   220: astore_1
        //   221: goto +20 -> 241
        //   224: astore_1
        //   225: goto +16 -> 241
        //   228: astore_1
        //   229: goto +12 -> 241
        //   232: astore_1
        //   233: goto +8 -> 241
        //   236: astore_1
        //   237: goto +4 -> 241
        //   240: astore_1
        //   241: aload 13
        //   243: invokevirtual 47	android/os/Parcel:recycle	()V
        //   246: aload 12
        //   248: invokevirtual 47	android/os/Parcel:recycle	()V
        //   251: aload_1
        //   252: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	253	0	this	Proxy
        //   0	253	1	paramIApplicationThread	IApplicationThread
        //   0	253	2	paramIIntentSender	IIntentSender
        //   0	253	3	paramIBinder1	IBinder
        //   0	253	4	paramIntent	Intent
        //   0	253	5	paramString1	String
        //   0	253	6	paramIBinder2	IBinder
        //   0	253	7	paramString2	String
        //   0	253	8	paramInt1	int
        //   0	253	9	paramInt2	int
        //   0	253	10	paramInt3	int
        //   0	253	11	paramBundle	Bundle
        //   3	244	12	localParcel1	Parcel
        //   8	234	13	localParcel2	Parcel
        //   18	25	14	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   166	195	208	finally
        //   131	138	212	finally
        //   143	157	212	finally
        //   160	166	212	finally
        //   124	131	216	finally
        //   117	124	220	finally
        //   110	117	224	finally
        //   103	110	228	finally
        //   96	103	232	finally
        //   62	68	236	finally
        //   73	87	236	finally
        //   90	96	236	finally
        //   10	17	240	finally
        //   24	31	240	finally
        //   36	42	240	finally
        //   49	56	240	finally
        //   56	62	240	finally
      }
      
      /* Error */
      public int startActivityWithConfig(IApplicationThread paramIApplicationThread, String paramString1, Intent paramIntent, String paramString2, IBinder paramIBinder, String paramString3, int paramInt1, int paramInt2, Configuration paramConfiguration, Bundle paramBundle, int paramInt3)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 13
        //   10: aload 12
        //   12: ldc 31
        //   14: invokevirtual 35	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload_1
        //   18: ifnull +13 -> 31
        //   21: aload_1
        //   22: invokeinterface 103 1 0
        //   27: astore_1
        //   28: goto +5 -> 33
        //   31: aconst_null
        //   32: astore_1
        //   33: aload 12
        //   35: aload_1
        //   36: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   39: aload 12
        //   41: aload_2
        //   42: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   45: aload_3
        //   46: ifnull +19 -> 65
        //   49: aload 12
        //   51: iconst_1
        //   52: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   55: aload_3
        //   56: aload 12
        //   58: iconst_0
        //   59: invokevirtual 85	android/content/Intent:writeToParcel	(Landroid/os/Parcel;I)V
        //   62: goto +9 -> 71
        //   65: aload 12
        //   67: iconst_0
        //   68: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   71: aload 12
        //   73: aload 4
        //   75: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   78: aload 12
        //   80: aload 5
        //   82: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   85: aload 12
        //   87: aload 6
        //   89: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   92: aload 12
        //   94: iload 7
        //   96: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   99: aload 12
        //   101: iload 8
        //   103: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   106: aload 9
        //   108: ifnull +20 -> 128
        //   111: aload 12
        //   113: iconst_1
        //   114: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   117: aload 9
        //   119: aload 12
        //   121: iconst_0
        //   122: invokevirtual 60	android/content/res/Configuration:writeToParcel	(Landroid/os/Parcel;I)V
        //   125: goto +9 -> 134
        //   128: aload 12
        //   130: iconst_0
        //   131: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   134: aload 10
        //   136: ifnull +20 -> 156
        //   139: aload 12
        //   141: iconst_1
        //   142: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   145: aload 10
        //   147: aload 12
        //   149: iconst_0
        //   150: invokevirtual 72	android/os/Bundle:writeToParcel	(Landroid/os/Parcel;I)V
        //   153: goto +9 -> 162
        //   156: aload 12
        //   158: iconst_0
        //   159: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   162: aload 12
        //   164: iload 11
        //   166: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   169: aload_0
        //   170: getfield 19	android/app/IActivityManager$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   173: bipush 105
        //   175: aload 12
        //   177: aload 13
        //   179: iconst_0
        //   180: invokeinterface 44 5 0
        //   185: pop
        //   186: aload 13
        //   188: invokevirtual 64	android/os/Parcel:readException	()V
        //   191: aload 13
        //   193: invokevirtual 95	android/os/Parcel:readInt	()I
        //   196: istore 7
        //   198: aload 13
        //   200: invokevirtual 47	android/os/Parcel:recycle	()V
        //   203: aload 12
        //   205: invokevirtual 47	android/os/Parcel:recycle	()V
        //   208: iload 7
        //   210: ireturn
        //   211: astore_1
        //   212: goto +32 -> 244
        //   215: astore_1
        //   216: goto +28 -> 244
        //   219: astore_1
        //   220: goto +24 -> 244
        //   223: astore_1
        //   224: goto +20 -> 244
        //   227: astore_1
        //   228: goto +16 -> 244
        //   231: astore_1
        //   232: goto +12 -> 244
        //   235: astore_1
        //   236: goto +8 -> 244
        //   239: astore_1
        //   240: goto +4 -> 244
        //   243: astore_1
        //   244: aload 13
        //   246: invokevirtual 47	android/os/Parcel:recycle	()V
        //   249: aload 12
        //   251: invokevirtual 47	android/os/Parcel:recycle	()V
        //   254: aload_1
        //   255: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	256	0	this	Proxy
        //   0	256	1	paramIApplicationThread	IApplicationThread
        //   0	256	2	paramString1	String
        //   0	256	3	paramIntent	Intent
        //   0	256	4	paramString2	String
        //   0	256	5	paramIBinder	IBinder
        //   0	256	6	paramString3	String
        //   0	256	7	paramInt1	int
        //   0	256	8	paramInt2	int
        //   0	256	9	paramConfiguration	Configuration
        //   0	256	10	paramBundle	Bundle
        //   0	256	11	paramInt3	int
        //   3	247	12	localParcel1	Parcel
        //   8	237	13	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   169	198	211	finally
        //   162	169	215	finally
        //   99	106	219	finally
        //   111	125	219	finally
        //   128	134	219	finally
        //   139	153	219	finally
        //   156	162	219	finally
        //   92	99	223	finally
        //   85	92	227	finally
        //   78	85	231	finally
        //   71	78	235	finally
        //   39	45	239	finally
        //   49	62	239	finally
        //   65	71	239	finally
        //   10	17	243	finally
        //   21	28	243	finally
        //   33	39	243	finally
      }
      
      public int startAssistantActivity(String paramString1, int paramInt1, int paramInt2, Intent paramIntent, String paramString2, Bundle paramBundle, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt3);
          mRemote.transact(195, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean startBinderTracking()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(239, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startConfirmDeviceCredentialIntent(Intent paramIntent, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(279, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startInPlaceAnimationOnFrontMostApplication(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(215, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean startInstrumentation(ComponentName paramComponentName, String paramString1, int paramInt1, Bundle paramBundle, IInstrumentationWatcher paramIInstrumentationWatcher, IUiAutomationConnection paramIUiAutomationConnection, int paramInt2, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt1);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          paramString1 = null;
          if (paramIInstrumentationWatcher != null) {
            paramComponentName = paramIInstrumentationWatcher.asBinder();
          } else {
            paramComponentName = null;
          }
          localParcel1.writeStrongBinder(paramComponentName);
          paramComponentName = paramString1;
          if (paramIUiAutomationConnection != null) {
            paramComponentName = paramIUiAutomationConnection.asBinder();
          }
          localParcel1.writeStrongBinder(paramComponentName);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString2);
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startLocalVoiceInteraction(IBinder paramIBinder, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(266, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startLockTaskModeByToken(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(190, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean startNextMatchingActivity(IBinder paramIBinder, Intent paramIntent, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          boolean bool = true;
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(65, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startRecentsActivity(Intent paramIntent, IAssistDataReceiver paramIAssistDataReceiver, IRecentsAnimationRunner paramIRecentsAnimationRunner)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          Object localObject = null;
          if (paramIAssistDataReceiver != null) {
            paramIntent = paramIAssistDataReceiver.asBinder();
          } else {
            paramIntent = null;
          }
          localParcel1.writeStrongBinder(paramIntent);
          paramIntent = localObject;
          if (paramIRecentsAnimationRunner != null) {
            paramIntent = paramIRecentsAnimationRunner.asBinder();
          }
          localParcel1.writeStrongBinder(paramIntent);
          mRemote.transact(196, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName startService(IApplicationThread paramIApplicationThread, Intent paramIntent, String paramString1, boolean paramBoolean, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          Object localObject = null;
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIApplicationThread = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIApplicationThread = localObject;
          }
          return paramIApplicationThread;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startSystemLockTaskMode(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(201, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean startUserInBackground(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(189, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean startUserInBackgroundWithListener(int paramInt, IProgressListener paramIProgressListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          if (paramIProgressListener != null) {
            paramIProgressListener = paramIProgressListener.asBinder();
          } else {
            paramIProgressListener = null;
          }
          localParcel1.writeStrongBinder(paramIProgressListener);
          paramIProgressListener = mRemote;
          boolean bool = false;
          paramIProgressListener.transact(302, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public int startVoiceActivity(String paramString1, int paramInt1, int paramInt2, Intent paramIntent, String paramString2, IVoiceInteractionSession paramIVoiceInteractionSession, IVoiceInteractor paramIVoiceInteractor, int paramInt3, ProfilerInfo paramProfilerInfo, Bundle paramBundle, int paramInt4)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: invokestatic 29	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 13
        //   10: aload 12
        //   12: ldc 31
        //   14: invokevirtual 35	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload 12
        //   19: aload_1
        //   20: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   23: aload 12
        //   25: iload_2
        //   26: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   29: aload 12
        //   31: iload_3
        //   32: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   35: aload 4
        //   37: ifnull +20 -> 57
        //   40: aload 12
        //   42: iconst_1
        //   43: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   46: aload 4
        //   48: aload 12
        //   50: iconst_0
        //   51: invokevirtual 85	android/content/Intent:writeToParcel	(Landroid/os/Parcel;I)V
        //   54: goto +9 -> 63
        //   57: aload 12
        //   59: iconst_0
        //   60: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   63: aload 12
        //   65: aload 5
        //   67: invokevirtual 107	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   70: aconst_null
        //   71: astore 4
        //   73: aload 6
        //   75: ifnull +14 -> 89
        //   78: aload 6
        //   80: invokeinterface 211 1 0
        //   85: astore_1
        //   86: goto +5 -> 91
        //   89: aconst_null
        //   90: astore_1
        //   91: aload 12
        //   93: aload_1
        //   94: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   97: aload 4
        //   99: astore_1
        //   100: aload 7
        //   102: ifnull +11 -> 113
        //   105: aload 7
        //   107: invokeinterface 793 1 0
        //   112: astore_1
        //   113: aload 12
        //   115: aload_1
        //   116: invokevirtual 38	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   119: aload 12
        //   121: iload 8
        //   123: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   126: aload 9
        //   128: ifnull +20 -> 148
        //   131: aload 12
        //   133: iconst_1
        //   134: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   137: aload 9
        //   139: aload 12
        //   141: iconst_0
        //   142: invokevirtual 559	android/app/ProfilerInfo:writeToParcel	(Landroid/os/Parcel;I)V
        //   145: goto +9 -> 154
        //   148: aload 12
        //   150: iconst_0
        //   151: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   154: aload 10
        //   156: ifnull +20 -> 176
        //   159: aload 12
        //   161: iconst_1
        //   162: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   165: aload 10
        //   167: aload 12
        //   169: iconst_0
        //   170: invokevirtual 72	android/os/Bundle:writeToParcel	(Landroid/os/Parcel;I)V
        //   173: goto +9 -> 182
        //   176: aload 12
        //   178: iconst_0
        //   179: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   182: aload 12
        //   184: iload 11
        //   186: invokevirtual 54	android/os/Parcel:writeInt	(I)V
        //   189: aload_0
        //   190: getfield 19	android/app/IActivityManager$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   193: sipush 194
        //   196: aload 12
        //   198: aload 13
        //   200: iconst_0
        //   201: invokeinterface 44 5 0
        //   206: pop
        //   207: aload 13
        //   209: invokevirtual 64	android/os/Parcel:readException	()V
        //   212: aload 13
        //   214: invokevirtual 95	android/os/Parcel:readInt	()I
        //   217: istore_2
        //   218: aload 13
        //   220: invokevirtual 47	android/os/Parcel:recycle	()V
        //   223: aload 12
        //   225: invokevirtual 47	android/os/Parcel:recycle	()V
        //   228: iload_2
        //   229: ireturn
        //   230: astore_1
        //   231: goto +28 -> 259
        //   234: astore_1
        //   235: goto +24 -> 259
        //   238: astore_1
        //   239: goto +20 -> 259
        //   242: astore_1
        //   243: goto +16 -> 259
        //   246: astore_1
        //   247: goto +12 -> 259
        //   250: astore_1
        //   251: goto +8 -> 259
        //   254: astore_1
        //   255: goto +4 -> 259
        //   258: astore_1
        //   259: aload 13
        //   261: invokevirtual 47	android/os/Parcel:recycle	()V
        //   264: aload 12
        //   266: invokevirtual 47	android/os/Parcel:recycle	()V
        //   269: aload_1
        //   270: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	271	0	this	Proxy
        //   0	271	1	paramString1	String
        //   0	271	2	paramInt1	int
        //   0	271	3	paramInt2	int
        //   0	271	4	paramIntent	Intent
        //   0	271	5	paramString2	String
        //   0	271	6	paramIVoiceInteractionSession	IVoiceInteractionSession
        //   0	271	7	paramIVoiceInteractor	IVoiceInteractor
        //   0	271	8	paramInt3	int
        //   0	271	9	paramProfilerInfo	ProfilerInfo
        //   0	271	10	paramBundle	Bundle
        //   0	271	11	paramInt4	int
        //   3	262	12	localParcel1	Parcel
        //   8	252	13	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   189	218	230	finally
        //   182	189	234	finally
        //   119	126	238	finally
        //   131	145	238	finally
        //   148	154	238	finally
        //   159	173	238	finally
        //   176	182	238	finally
        //   63	70	242	finally
        //   78	86	242	finally
        //   91	97	242	finally
        //   105	113	242	finally
        //   113	119	242	finally
        //   29	35	246	finally
        //   40	54	246	finally
        //   57	63	246	finally
        //   23	29	250	finally
        //   17	23	254	finally
        //   10	17	258	finally
      }
      
      public void stopAppSwitches()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(86, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean stopBinderTrackingAndDump(ParcelFileDescriptor paramParcelFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          boolean bool = true;
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(240, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopLocalVoiceInteraction(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(267, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopLockTaskModeByToken(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(191, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int stopService(IApplicationThread paramIApplicationThread, Intent paramIntent, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean stopServiceToken(ComponentName paramComponentName, IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopSystemLockTaskMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(202, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int stopUser(int paramInt, boolean paramBoolean, IStopUserCallback paramIStopUserCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          if (paramIStopUserCallback != null) {
            paramIStopUserCallback = paramIStopUserCallback.asBinder();
          } else {
            paramIStopUserCallback = null;
          }
          localParcel1.writeStrongBinder(paramIStopUserCallback);
          mRemote.transact(152, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean supportsLocalVoiceInteraction()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(268, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void suppressResizeConfigChanges(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(247, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean switchUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(128, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void takePersistableUriPermission(Uri paramUri, int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(181, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unbindBackupAgent(ApplicationInfo paramApplicationInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramApplicationInfo != null)
          {
            localParcel1.writeInt(1);
            paramApplicationInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(90, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unbindFinished(IBinder paramIBinder, Intent paramIntent, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(70, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean unbindService(IServiceConnection paramIServiceConnection)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIServiceConnection != null) {
            paramIServiceConnection = paramIServiceConnection.asBinder();
          } else {
            paramIServiceConnection = null;
          }
          localParcel1.writeStrongBinder(paramIServiceConnection);
          paramIServiceConnection = mRemote;
          boolean bool = false;
          paramIServiceConnection.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unbroadcastIntent(IApplicationThread paramIApplicationThread, Intent paramIntent, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unhandledBack()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean unlockUser(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, IProgressListener paramIProgressListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          if (paramIProgressListener != null) {
            paramArrayOfByte1 = paramIProgressListener.asBinder();
          } else {
            paramArrayOfByte1 = null;
          }
          localParcel1.writeStrongBinder(paramArrayOfByte1);
          paramArrayOfByte1 = mRemote;
          boolean bool = false;
          paramArrayOfByte1.transact(251, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterIntentSenderCancelListener(IIntentSender paramIIntentSender, IResultReceiver paramIResultReceiver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          Object localObject = null;
          if (paramIIntentSender != null) {
            paramIIntentSender = paramIIntentSender.asBinder();
          } else {
            paramIIntentSender = null;
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          paramIIntentSender = localObject;
          if (paramIResultReceiver != null) {
            paramIIntentSender = paramIResultReceiver.asBinder();
          }
          localParcel1.writeStrongBinder(paramIIntentSender);
          mRemote.transact(63, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterProcessObserver(IProcessObserver paramIProcessObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIProcessObserver != null) {
            paramIProcessObserver = paramIProcessObserver.asBinder();
          } else {
            paramIProcessObserver = null;
          }
          localParcel1.writeStrongBinder(paramIProcessObserver);
          mRemote.transact(132, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterReceiver(IIntentReceiver paramIIntentReceiver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIIntentReceiver != null) {
            paramIIntentReceiver = paramIIntentReceiver.asBinder();
          } else {
            paramIIntentReceiver = null;
          }
          localParcel1.writeStrongBinder(paramIIntentReceiver);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterTaskStackListener(ITaskStackListener paramITaskStackListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramITaskStackListener != null) {
            paramITaskStackListener = paramITaskStackListener.asBinder();
          } else {
            paramITaskStackListener = null;
          }
          localParcel1.writeStrongBinder(paramITaskStackListener);
          mRemote.transact(218, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterUidObserver(IUidObserver paramIUidObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIUidObserver != null) {
            paramIUidObserver = paramIUidObserver.asBinder();
          } else {
            paramIUidObserver = null;
          }
          localParcel1.writeStrongBinder(paramIUidObserver);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterUserSwitchObserver(IUserSwitchObserver paramIUserSwitchObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramIUserSwitchObserver != null) {
            paramIUserSwitchObserver = paramIUserSwitchObserver.asBinder();
          } else {
            paramIUserSwitchObserver = null;
          }
          localParcel1.writeStrongBinder(paramIUserSwitchObserver);
          mRemote.transact(154, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unstableProviderDied(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(148, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean updateConfiguration(Configuration paramConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          boolean bool = true;
          if (paramConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateDeviceOwner(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeString(paramString);
          mRemote.transact(233, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean updateDisplayOverrideConfiguration(Configuration paramConfiguration, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          boolean bool = true;
          if (paramConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(286, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateLockTaskFeatures(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(299, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateLockTaskPackages(int paramInt, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(228, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updatePersistentConfiguration(Configuration paramConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          if (paramConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(134, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void waitForNetworkStateUpdate(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeLong(paramLong);
          mRemote.transact(295, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean willActivityBeVisible(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(104, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
