package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ParceledListSlice;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.service.notification.ZenModeConfig;
import android.service.notification.ZenModeConfig.ZenRule;
import android.util.ArraySet;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NotificationManager
{
  public static final String ACTION_APP_BLOCK_STATE_CHANGED = "android.app.action.APP_BLOCK_STATE_CHANGED";
  public static final String ACTION_EFFECTS_SUPPRESSOR_CHANGED = "android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED";
  public static final String ACTION_INTERRUPTION_FILTER_CHANGED = "android.app.action.INTERRUPTION_FILTER_CHANGED";
  public static final String ACTION_INTERRUPTION_FILTER_CHANGED_INTERNAL = "android.app.action.INTERRUPTION_FILTER_CHANGED_INTERNAL";
  public static final String ACTION_NOTIFICATION_CHANNEL_BLOCK_STATE_CHANGED = "android.app.action.NOTIFICATION_CHANNEL_BLOCK_STATE_CHANGED";
  public static final String ACTION_NOTIFICATION_CHANNEL_GROUP_BLOCK_STATE_CHANGED = "android.app.action.NOTIFICATION_CHANNEL_GROUP_BLOCK_STATE_CHANGED";
  public static final String ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED = "android.app.action.NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED";
  public static final String ACTION_NOTIFICATION_POLICY_CHANGED = "android.app.action.NOTIFICATION_POLICY_CHANGED";
  public static final String EXTRA_BLOCKED_STATE = "android.app.extra.BLOCKED_STATE";
  public static final String EXTRA_NOTIFICATION_CHANNEL_GROUP_ID = "android.app.extra.NOTIFICATION_CHANNEL_GROUP_ID";
  public static final String EXTRA_NOTIFICATION_CHANNEL_ID = "android.app.extra.NOTIFICATION_CHANNEL_ID";
  public static final int IMPORTANCE_DEFAULT = 3;
  public static final int IMPORTANCE_HIGH = 4;
  public static final int IMPORTANCE_LOW = 2;
  public static final int IMPORTANCE_MAX = 5;
  public static final int IMPORTANCE_MIN = 1;
  public static final int IMPORTANCE_NONE = 0;
  public static final int IMPORTANCE_UNSPECIFIED = -1000;
  public static final int INTERRUPTION_FILTER_ALARMS = 4;
  public static final int INTERRUPTION_FILTER_ALL = 1;
  public static final int INTERRUPTION_FILTER_NONE = 3;
  public static final int INTERRUPTION_FILTER_PRIORITY = 2;
  public static final int INTERRUPTION_FILTER_UNKNOWN = 0;
  private static String TAG = "NotificationManager";
  public static final int VISIBILITY_NO_OVERRIDE = -1000;
  private static boolean localLOGV = false;
  private static INotificationManager sService;
  private Context mContext;
  
  NotificationManager(Context paramContext, Handler paramHandler)
  {
    mContext = paramContext;
  }
  
  private static void checkRequired(String paramString, Object paramObject)
  {
    if (paramObject != null) {
      return;
    }
    paramObject = new StringBuilder();
    paramObject.append(paramString);
    paramObject.append(" is required");
    throw new IllegalArgumentException(paramObject.toString());
  }
  
  private void fixLegacySmallIcon(Notification paramNotification, String paramString)
  {
    if ((paramNotification.getSmallIcon() == null) && (icon != 0)) {
      paramNotification.setSmallIcon(Icon.createWithResource(paramString, icon));
    }
  }
  
  public static NotificationManager from(Context paramContext)
  {
    return (NotificationManager)paramContext.getSystemService("notification");
  }
  
  public static INotificationManager getService()
  {
    if (sService != null) {
      return sService;
    }
    sService = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
    return sService;
  }
  
  public static int zenModeFromInterruptionFilter(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      return paramInt2;
    case 4: 
      return 3;
    case 3: 
      return 2;
    case 2: 
      return 1;
    }
    return 0;
  }
  
  public static int zenModeToInterruptionFilter(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0;
    case 3: 
      return 4;
    case 2: 
      return 3;
    case 1: 
      return 2;
    }
    return 1;
  }
  
  public String addAutomaticZenRule(AutomaticZenRule paramAutomaticZenRule)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      paramAutomaticZenRule = localINotificationManager.addAutomaticZenRule(paramAutomaticZenRule);
      return paramAutomaticZenRule;
    }
    catch (RemoteException paramAutomaticZenRule)
    {
      throw paramAutomaticZenRule.rethrowFromSystemServer();
    }
  }
  
  public final void addGameDndBlackPackage(String[] paramArrayOfString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      localINotificationManager.addGameDndBlackPackage(mContext.getOpPackageName(), paramArrayOfString);
      return;
    }
    catch (RemoteException paramArrayOfString)
    {
      throw paramArrayOfString.rethrowFromSystemServer();
    }
  }
  
  public boolean areNotificationsEnabled()
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      boolean bool = localINotificationManager.areNotificationsEnabled(mContext.getPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void cancel(int paramInt)
  {
    cancel(null, paramInt);
  }
  
  public void cancel(String paramString, int paramInt)
  {
    cancelAsUser(paramString, paramInt, mContext.getUser());
  }
  
  public void cancelAll()
  {
    INotificationManager localINotificationManager = getService();
    String str1 = mContext.getPackageName();
    if (localLOGV)
    {
      String str2 = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(str1);
      localStringBuilder.append(": cancelAll()");
      Log.v(str2, localStringBuilder.toString());
    }
    try
    {
      localINotificationManager.cancelAllNotifications(str1, mContext.getUserId());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void cancelAsUser(String paramString, int paramInt, UserHandle paramUserHandle)
  {
    INotificationManager localINotificationManager = getService();
    String str1 = mContext.getPackageName();
    if (localLOGV)
    {
      String str2 = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(str1);
      localStringBuilder.append(": cancel(");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(")");
      Log.v(str2, localStringBuilder.toString());
    }
    try
    {
      localINotificationManager.cancelNotificationWithTag(str1, paramString, paramInt, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void createNotificationChannel(NotificationChannel paramNotificationChannel)
  {
    createNotificationChannels(Arrays.asList(new NotificationChannel[] { paramNotificationChannel }));
  }
  
  public void createNotificationChannelGroup(NotificationChannelGroup paramNotificationChannelGroup)
  {
    createNotificationChannelGroups(Arrays.asList(new NotificationChannelGroup[] { paramNotificationChannelGroup }));
  }
  
  public void createNotificationChannelGroups(List<NotificationChannelGroup> paramList)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      String str = mContext.getPackageName();
      ParceledListSlice localParceledListSlice = new android/content/pm/ParceledListSlice;
      localParceledListSlice.<init>(paramList);
      localINotificationManager.createNotificationChannelGroups(str, localParceledListSlice);
      return;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public void createNotificationChannels(List<NotificationChannel> paramList)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      String str = mContext.getPackageName();
      ParceledListSlice localParceledListSlice = new android/content/pm/ParceledListSlice;
      localParceledListSlice.<init>(paramList);
      localINotificationManager.createNotificationChannels(str, localParceledListSlice);
      return;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public void deleteNotificationChannel(String paramString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      localINotificationManager.deleteNotificationChannel(mContext.getPackageName(), paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void deleteNotificationChannelGroup(String paramString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      localINotificationManager.deleteNotificationChannelGroup(mContext.getPackageName(), paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public StatusBarNotification[] getActiveNotifications()
  {
    INotificationManager localINotificationManager = getService();
    Object localObject = mContext.getPackageName();
    try
    {
      localObject = localINotificationManager.getAppActiveNotifications((String)localObject, mContext.getUserId()).getList();
      localObject = (StatusBarNotification[])((List)localObject).toArray(new StatusBarNotification[((List)localObject).size()]);
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public AutomaticZenRule getAutomaticZenRule(String paramString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      paramString = localINotificationManager.getAutomaticZenRule(paramString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Map<String, AutomaticZenRule> getAutomaticZenRules()
  {
    Object localObject1 = getService();
    try
    {
      Object localObject2 = ((INotificationManager)localObject1).getZenRules();
      localObject1 = new java/util/HashMap;
      ((HashMap)localObject1).<init>();
      Iterator localIterator = ((List)localObject2).iterator();
      while (localIterator.hasNext())
      {
        ZenModeConfig.ZenRule localZenRule = (ZenModeConfig.ZenRule)localIterator.next();
        localObject2 = id;
        AutomaticZenRule localAutomaticZenRule = new android/app/AutomaticZenRule;
        localAutomaticZenRule.<init>(name, component, conditionId, zenModeToInterruptionFilter(zenMode), enabled, creationTime);
        ((Map)localObject1).put(localObject2, localAutomaticZenRule);
      }
      return localObject1;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public final int getCurrentInterruptionFilter()
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      int i = zenModeToInterruptionFilter(localINotificationManager.getZenMode());
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public ComponentName getEffectsSuppressor()
  {
    Object localObject = getService();
    try
    {
      localObject = ((INotificationManager)localObject).getEffectsSuppressor();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<String> getEnabledNotificationListenerPackages()
  {
    Object localObject = getService();
    try
    {
      localObject = ((INotificationManager)localObject).getEnabledNotificationListenerPackages();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<ComponentName> getEnabledNotificationListeners(int paramInt)
  {
    Object localObject = getService();
    try
    {
      localObject = ((INotificationManager)localObject).getEnabledNotificationListeners(paramInt);
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public final ArraySet<String> getGameDndBlackPackages()
  {
    Object localObject = getService();
    try
    {
      String[] arrayOfString = ((INotificationManager)localObject).getGameDndBlackPackages();
      if ((arrayOfString != null) && (arrayOfString.length > 0))
      {
        localObject = new android/util/ArraySet;
        ((ArraySet)localObject).<init>(arrayOfString.length);
        for (int i = 0; i < arrayOfString.length; i++) {
          ((ArraySet)localObject).add(arrayOfString[i]);
        }
        return localObject;
      }
      return new ArraySet();
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public final boolean getGameDndLock()
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      boolean bool = localINotificationManager.getGameDndLock(mContext.getOpPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getImportance()
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      int i = localINotificationManager.getPackageImportance(mContext.getPackageName());
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public NotificationChannel getNotificationChannel(String paramString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      paramString = localINotificationManager.getNotificationChannel(mContext.getPackageName(), paramString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public NotificationChannelGroup getNotificationChannelGroup(String paramString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      paramString = localINotificationManager.getNotificationChannelGroup(mContext.getPackageName(), paramString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public List<NotificationChannelGroup> getNotificationChannelGroups()
  {
    Object localObject = getService();
    try
    {
      localObject = ((INotificationManager)localObject).getNotificationChannelGroups(mContext.getPackageName()).getList();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<NotificationChannel> getNotificationChannels()
  {
    Object localObject = getService();
    try
    {
      localObject = ((INotificationManager)localObject).getNotificationChannels(mContext.getPackageName()).getList();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Policy getNotificationPolicy()
  {
    Object localObject = getService();
    try
    {
      localObject = ((INotificationManager)localObject).getNotificationPolicy(mContext.getOpPackageName());
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getRuleInstanceCount(ComponentName paramComponentName)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      int i = localINotificationManager.getRuleInstanceCount(paramComponentName);
      return i;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public int getZenMode()
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      int i = localINotificationManager.getZenMode();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public ZenModeConfig getZenModeConfig()
  {
    Object localObject = getService();
    try
    {
      localObject = ((INotificationManager)localObject).getZenModeConfig();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public final boolean isGameDndBlackPackage(String paramString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      boolean bool = localINotificationManager.isGameDndBlackPackage(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isNotificationAssistantAccessGranted(ComponentName paramComponentName)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      boolean bool = localINotificationManager.isNotificationAssistantAccessGranted(paramComponentName);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean isNotificationListenerAccessGranted(ComponentName paramComponentName)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      boolean bool = localINotificationManager.isNotificationListenerAccessGranted(paramComponentName);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean isNotificationPolicyAccessGranted()
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      boolean bool = localINotificationManager.isNotificationPolicyAccessGranted(mContext.getOpPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isNotificationPolicyAccessGrantedForPackage(String paramString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      boolean bool = localINotificationManager.isNotificationPolicyAccessGrantedForPackage(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isSystemConditionProviderEnabled(String paramString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      boolean bool = localINotificationManager.isSystemConditionProviderEnabled(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean matchesCallFilter(Bundle paramBundle)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      boolean bool = localINotificationManager.matchesCallFilter(paramBundle);
      return bool;
    }
    catch (RemoteException paramBundle)
    {
      throw paramBundle.rethrowFromSystemServer();
    }
  }
  
  public void notify(int paramInt, Notification paramNotification)
  {
    notify(null, paramInt, paramNotification);
  }
  
  public void notify(String paramString, int paramInt, Notification paramNotification)
  {
    notifyAsUser(paramString, paramInt, paramNotification, mContext.getUser());
  }
  
  public void notifyAsUser(String paramString, int paramInt, Notification paramNotification, UserHandle paramUserHandle)
  {
    INotificationManager localINotificationManager = getService();
    String str1 = mContext.getPackageName();
    Notification.addFieldsFromContext(mContext, paramNotification);
    if (sound != null)
    {
      sound = sound.getCanonicalUri();
      if (StrictMode.vmFileUriExposureEnabled()) {
        sound.checkFileUriExposed("Notification.sound");
      }
    }
    fixLegacySmallIcon(paramNotification, str1);
    if ((mContext.getApplicationInfo().targetSdkVersion > 22) && (paramNotification.getSmallIcon() == null))
    {
      paramString = new StringBuilder();
      paramString.append("Invalid notification (no valid small icon): ");
      paramString.append(paramNotification);
      throw new IllegalArgumentException(paramString.toString());
    }
    if (localLOGV)
    {
      String str2 = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(str1);
      localStringBuilder.append(": notify(");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(", ");
      localStringBuilder.append(paramNotification);
      localStringBuilder.append(")");
      Log.v(str2, localStringBuilder.toString());
    }
    paramNotification.reduceImageSizes(mContext);
    paramNotification = Notification.Builder.maybeCloneStrippedForDelivery(paramNotification, ((ActivityManager)mContext.getSystemService("activity")).isLowRamDevice(), mContext);
    try
    {
      localINotificationManager.enqueueNotificationWithTag(str1, mContext.getOpPackageName(), paramString, paramInt, paramNotification, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean removeAutomaticZenRule(String paramString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      boolean bool = localINotificationManager.removeAutomaticZenRule(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean removeAutomaticZenRules(String paramString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      boolean bool = localINotificationManager.removeAutomaticZenRules(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public final void removeGameDndBlackPackage(String[] paramArrayOfString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      localINotificationManager.removeGameDndBlackPackage(mContext.getOpPackageName(), paramArrayOfString);
      return;
    }
    catch (RemoteException paramArrayOfString)
    {
      throw paramArrayOfString.rethrowFromSystemServer();
    }
  }
  
  public final void setGameDndLock(boolean paramBoolean)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      localINotificationManager.setGameDndLock(mContext.getOpPackageName(), paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public final void setInterruptionFilter(int paramInt)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      localINotificationManager.setInterruptionFilter(mContext.getOpPackageName(), paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setNotificationListenerAccessGranted(ComponentName paramComponentName, boolean paramBoolean)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      localINotificationManager.setNotificationListenerAccessGranted(paramComponentName, paramBoolean);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setNotificationListenerAccessGrantedForUser(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      localINotificationManager.setNotificationListenerAccessGrantedForUser(paramComponentName, paramInt, paramBoolean);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setNotificationPolicy(Policy paramPolicy)
  {
    checkRequired("policy", paramPolicy);
    INotificationManager localINotificationManager = getService();
    try
    {
      localINotificationManager.setNotificationPolicy(mContext.getOpPackageName(), paramPolicy);
      return;
    }
    catch (RemoteException paramPolicy)
    {
      throw paramPolicy.rethrowFromSystemServer();
    }
  }
  
  public void setNotificationPolicyAccessGranted(String paramString, boolean paramBoolean)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      localINotificationManager.setNotificationPolicyAccessGranted(paramString, paramBoolean);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setZenMode(int paramInt, Uri paramUri, String paramString)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      localINotificationManager.setZenMode(paramInt, paramUri, paramString);
      return;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public boolean updateAutomaticZenRule(String paramString, AutomaticZenRule paramAutomaticZenRule)
  {
    INotificationManager localINotificationManager = getService();
    try
    {
      boolean bool = localINotificationManager.updateAutomaticZenRule(paramString, paramAutomaticZenRule);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Importance {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface InterruptionFilter {}
  
  public static class Policy
    implements Parcelable
  {
    public static final int[] ALL_PRIORITY_CATEGORIES = { 32, 64, 128, 1, 2, 4, 8, 16 };
    private static final int[] ALL_SUPPRESSED_EFFECTS = { 1, 2, 4, 8, 16, 32, 64, 128, 256 };
    public static final Parcelable.Creator<Policy> CREATOR = new Parcelable.Creator()
    {
      public NotificationManager.Policy createFromParcel(Parcel paramAnonymousParcel)
      {
        return new NotificationManager.Policy(paramAnonymousParcel);
      }
      
      public NotificationManager.Policy[] newArray(int paramAnonymousInt)
      {
        return new NotificationManager.Policy[paramAnonymousInt];
      }
    };
    public static final int PRIORITY_CATEGORY_ALARMS = 32;
    public static final int PRIORITY_CATEGORY_CALLS = 8;
    public static final int PRIORITY_CATEGORY_EVENTS = 2;
    public static final int PRIORITY_CATEGORY_MEDIA = 64;
    public static final int PRIORITY_CATEGORY_MESSAGES = 4;
    public static final int PRIORITY_CATEGORY_REMINDERS = 1;
    public static final int PRIORITY_CATEGORY_REPEAT_CALLERS = 16;
    public static final int PRIORITY_CATEGORY_SYSTEM = 128;
    public static final int PRIORITY_SENDERS_ANY = 0;
    public static final int PRIORITY_SENDERS_CONTACTS = 1;
    public static final int PRIORITY_SENDERS_STARRED = 2;
    private static final int[] SCREEN_OFF_SUPPRESSED_EFFECTS = { 1, 4, 8, 128 };
    private static final int[] SCREEN_ON_SUPPRESSED_EFFECTS = { 2, 16, 32, 64, 256 };
    public static final int STATE_CHANNELS_BYPASSING_DND = 1;
    public static final int STATE_UNSET = -1;
    public static final int SUPPRESSED_EFFECTS_UNSET = -1;
    public static final int SUPPRESSED_EFFECT_AMBIENT = 128;
    public static final int SUPPRESSED_EFFECT_BADGE = 64;
    public static final int SUPPRESSED_EFFECT_FULL_SCREEN_INTENT = 4;
    public static final int SUPPRESSED_EFFECT_LIGHTS = 8;
    public static final int SUPPRESSED_EFFECT_NOTIFICATION_LIST = 256;
    public static final int SUPPRESSED_EFFECT_PEEK = 16;
    @Deprecated
    public static final int SUPPRESSED_EFFECT_SCREEN_OFF = 1;
    @Deprecated
    public static final int SUPPRESSED_EFFECT_SCREEN_ON = 2;
    public static final int SUPPRESSED_EFFECT_STATUS_BAR = 32;
    public final int priorityCallSenders;
    public final int priorityCategories;
    public final int priorityMessageSenders;
    public final int state;
    public final int suppressedVisualEffects;
    
    public Policy(int paramInt1, int paramInt2, int paramInt3)
    {
      this(paramInt1, paramInt2, paramInt3, -1, -1);
    }
    
    public Policy(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      priorityCategories = paramInt1;
      priorityCallSenders = paramInt2;
      priorityMessageSenders = paramInt3;
      suppressedVisualEffects = paramInt4;
      state = -1;
    }
    
    public Policy(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      priorityCategories = paramInt1;
      priorityCallSenders = paramInt2;
      priorityMessageSenders = paramInt3;
      suppressedVisualEffects = paramInt4;
      state = paramInt5;
    }
    
    public Policy(Parcel paramParcel)
    {
      this(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
    }
    
    public static boolean areAllVisualEffectsSuppressed(int paramInt)
    {
      for (int i = 0; i < ALL_SUPPRESSED_EFFECTS.length; i++) {
        if ((paramInt & ALL_SUPPRESSED_EFFECTS[i]) == 0) {
          return false;
        }
      }
      return true;
    }
    
    public static boolean areAnyScreenOffEffectsSuppressed(int paramInt)
    {
      for (int i = 0; i < SCREEN_OFF_SUPPRESSED_EFFECTS.length; i++) {
        if ((paramInt & SCREEN_OFF_SUPPRESSED_EFFECTS[i]) != 0) {
          return true;
        }
      }
      return false;
    }
    
    public static boolean areAnyScreenOnEffectsSuppressed(int paramInt)
    {
      for (int i = 0; i < SCREEN_ON_SUPPRESSED_EFFECTS.length; i++) {
        if ((paramInt & SCREEN_ON_SUPPRESSED_EFFECTS[i]) != 0) {
          return true;
        }
      }
      return false;
    }
    
    private static void bitwiseToProtoEnum(ProtoOutputStream paramProtoOutputStream, long paramLong, int paramInt)
    {
      int i = 1;
      while (paramInt > 0)
      {
        if ((paramInt & 0x1) == 1) {
          paramProtoOutputStream.write(paramLong, i);
        }
        i++;
        paramInt >>>= 1;
      }
    }
    
    private static String effectToString(int paramInt)
    {
      if (paramInt != -1)
      {
        if (paramInt != 4)
        {
          if (paramInt != 8)
          {
            if (paramInt != 16)
            {
              if (paramInt != 32)
              {
                if (paramInt != 64)
                {
                  if (paramInt != 128)
                  {
                    if (paramInt != 256)
                    {
                      switch (paramInt)
                      {
                      default: 
                        StringBuilder localStringBuilder = new StringBuilder();
                        localStringBuilder.append("UNKNOWN_");
                        localStringBuilder.append(paramInt);
                        return localStringBuilder.toString();
                      case 2: 
                        return "SUPPRESSED_EFFECT_SCREEN_ON";
                      }
                      return "SUPPRESSED_EFFECT_SCREEN_OFF";
                    }
                    return "SUPPRESSED_EFFECT_NOTIFICATION_LIST";
                  }
                  return "SUPPRESSED_EFFECT_AMBIENT";
                }
                return "SUPPRESSED_EFFECT_BADGE";
              }
              return "SUPPRESSED_EFFECT_STATUS_BAR";
            }
            return "SUPPRESSED_EFFECT_PEEK";
          }
          return "SUPPRESSED_EFFECT_LIGHTS";
        }
        return "SUPPRESSED_EFFECT_FULL_SCREEN_INTENT";
      }
      return "SUPPRESSED_EFFECTS_UNSET";
    }
    
    public static int getAllSuppressedVisualEffects()
    {
      int i = 0;
      for (int j = 0; j < ALL_SUPPRESSED_EFFECTS.length; j++) {
        i |= ALL_SUPPRESSED_EFFECTS[j];
      }
      return i;
    }
    
    public static String priorityCategoriesToString(int paramInt)
    {
      if (paramInt == 0) {
        return "";
      }
      StringBuilder localStringBuilder = new StringBuilder();
      for (int i = 0; i < ALL_PRIORITY_CATEGORIES.length; i++)
      {
        int j = ALL_PRIORITY_CATEGORIES[i];
        if ((paramInt & j) != 0)
        {
          if (localStringBuilder.length() > 0) {
            localStringBuilder.append(',');
          }
          localStringBuilder.append(priorityCategoryToString(j));
        }
        paramInt &= j;
      }
      if (paramInt != 0)
      {
        if (localStringBuilder.length() > 0) {
          localStringBuilder.append(',');
        }
        localStringBuilder.append("PRIORITY_CATEGORY_UNKNOWN_");
        localStringBuilder.append(paramInt);
      }
      return localStringBuilder.toString();
    }
    
    private static String priorityCategoryToString(int paramInt)
    {
      if (paramInt != 4)
      {
        if (paramInt != 8)
        {
          if (paramInt != 16)
          {
            if (paramInt != 32)
            {
              if (paramInt != 64)
              {
                if (paramInt != 128)
                {
                  switch (paramInt)
                  {
                  default: 
                    StringBuilder localStringBuilder = new StringBuilder();
                    localStringBuilder.append("PRIORITY_CATEGORY_UNKNOWN_");
                    localStringBuilder.append(paramInt);
                    return localStringBuilder.toString();
                  case 2: 
                    return "PRIORITY_CATEGORY_EVENTS";
                  }
                  return "PRIORITY_CATEGORY_REMINDERS";
                }
                return "PRIORITY_CATEGORY_SYSTEM";
              }
              return "PRIORITY_CATEGORY_MEDIA";
            }
            return "PRIORITY_CATEGORY_ALARMS";
          }
          return "PRIORITY_CATEGORY_REPEAT_CALLERS";
        }
        return "PRIORITY_CATEGORY_CALLS";
      }
      return "PRIORITY_CATEGORY_MESSAGES";
    }
    
    public static String prioritySendersToString(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("PRIORITY_SENDERS_UNKNOWN_");
        localStringBuilder.append(paramInt);
        return localStringBuilder.toString();
      case 2: 
        return "PRIORITY_SENDERS_STARRED";
      case 1: 
        return "PRIORITY_SENDERS_CONTACTS";
      }
      return "PRIORITY_SENDERS_ANY";
    }
    
    public static String suppressedEffectsToString(int paramInt)
    {
      if (paramInt <= 0) {
        return "";
      }
      StringBuilder localStringBuilder = new StringBuilder();
      for (int i = 0; i < ALL_SUPPRESSED_EFFECTS.length; i++)
      {
        int j = ALL_SUPPRESSED_EFFECTS[i];
        if ((paramInt & j) != 0)
        {
          if (localStringBuilder.length() > 0) {
            localStringBuilder.append(',');
          }
          localStringBuilder.append(effectToString(j));
        }
        paramInt &= j;
      }
      if (paramInt != 0)
      {
        if (localStringBuilder.length() > 0) {
          localStringBuilder.append(',');
        }
        localStringBuilder.append("UNKNOWN_");
        localStringBuilder.append(paramInt);
      }
      return localStringBuilder.toString();
    }
    
    private static int toggleEffects(int paramInt, int[] paramArrayOfInt, boolean paramBoolean)
    {
      for (int i = 0; i < paramArrayOfInt.length; i++)
      {
        int j = paramArrayOfInt[i];
        if (paramBoolean) {
          paramInt |= j;
        } else {
          paramInt &= j;
        }
      }
      return paramInt;
    }
    
    public static int toggleScreenOffEffectsSuppressed(int paramInt, boolean paramBoolean)
    {
      return toggleEffects(paramInt, SCREEN_OFF_SUPPRESSED_EFFECTS, paramBoolean);
    }
    
    public static int toggleScreenOnEffectsSuppressed(int paramInt, boolean paramBoolean)
    {
      return toggleEffects(paramInt, SCREEN_ON_SUPPRESSED_EFFECTS, paramBoolean);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof Policy)) {
        return false;
      }
      boolean bool = true;
      if (paramObject == this) {
        return true;
      }
      paramObject = (Policy)paramObject;
      if ((priorityCategories != priorityCategories) || (priorityCallSenders != priorityCallSenders) || (priorityMessageSenders != priorityMessageSenders) || (suppressedVisualEffects != suppressedVisualEffects)) {
        bool = false;
      }
      return bool;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { Integer.valueOf(priorityCategories), Integer.valueOf(priorityCallSenders), Integer.valueOf(priorityMessageSenders), Integer.valueOf(suppressedVisualEffects) });
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("NotificationManager.Policy[priorityCategories=");
      localStringBuilder.append(priorityCategoriesToString(priorityCategories));
      localStringBuilder.append(",priorityCallSenders=");
      localStringBuilder.append(prioritySendersToString(priorityCallSenders));
      localStringBuilder.append(",priorityMessageSenders=");
      localStringBuilder.append(prioritySendersToString(priorityMessageSenders));
      localStringBuilder.append(",suppressedVisualEffects=");
      localStringBuilder.append(suppressedEffectsToString(suppressedVisualEffects));
      localStringBuilder.append(",areChannelsBypassingDnd=");
      String str;
      if ((state & 0x1) != 0) {
        str = "true";
      } else {
        str = "false";
      }
      localStringBuilder.append(str);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(priorityCategories);
      paramParcel.writeInt(priorityCallSenders);
      paramParcel.writeInt(priorityMessageSenders);
      paramParcel.writeInt(suppressedVisualEffects);
      paramParcel.writeInt(state);
    }
    
    public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
    {
      paramLong = paramProtoOutputStream.start(paramLong);
      bitwiseToProtoEnum(paramProtoOutputStream, 2259152797697L, priorityCategories);
      paramProtoOutputStream.write(1159641169922L, priorityCallSenders);
      paramProtoOutputStream.write(1159641169923L, priorityMessageSenders);
      bitwiseToProtoEnum(paramProtoOutputStream, 2259152797700L, suppressedVisualEffects);
      paramProtoOutputStream.end(paramLong);
    }
  }
}
