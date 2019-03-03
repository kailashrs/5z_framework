package android.app;

import android.content.ComponentName;
import android.content.pm.ParceledListSlice;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.Adjustment;
import android.service.notification.Condition;
import android.service.notification.IConditionProvider;
import android.service.notification.IConditionProvider.Stub;
import android.service.notification.INotificationListener;
import android.service.notification.INotificationListener.Stub;
import android.service.notification.StatusBarNotification;
import android.service.notification.ZenModeConfig;
import android.service.notification.ZenModeConfig.ZenRule;
import java.util.ArrayList;
import java.util.List;

public abstract interface INotificationManager
  extends IInterface
{
  public abstract String addAutomaticZenRule(AutomaticZenRule paramAutomaticZenRule)
    throws RemoteException;
  
  public abstract void addGameDndBlackPackage(String paramString, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void applyAdjustmentFromAssistant(INotificationListener paramINotificationListener, Adjustment paramAdjustment)
    throws RemoteException;
  
  public abstract void applyAdjustmentsFromAssistant(INotificationListener paramINotificationListener, List<Adjustment> paramList)
    throws RemoteException;
  
  public abstract void applyEnqueuedAdjustmentFromAssistant(INotificationListener paramINotificationListener, Adjustment paramAdjustment)
    throws RemoteException;
  
  public abstract void applyRestore(byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public abstract boolean areChannelsBypassingDnd()
    throws RemoteException;
  
  public abstract boolean areNotificationsEnabled(String paramString)
    throws RemoteException;
  
  public abstract boolean areNotificationsEnabledForPackage(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean canShowBadge(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void cancelAllNotifications(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void cancelNotificationFromListener(INotificationListener paramINotificationListener, String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract void cancelNotificationWithTag(String paramString1, String paramString2, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void cancelNotificationsFromListener(INotificationListener paramINotificationListener, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void cancelToast(String paramString, ITransientNotification paramITransientNotification)
    throws RemoteException;
  
  public abstract void clearData(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void createNotificationChannelGroups(String paramString, ParceledListSlice paramParceledListSlice)
    throws RemoteException;
  
  public abstract void createNotificationChannels(String paramString, ParceledListSlice paramParceledListSlice)
    throws RemoteException;
  
  public abstract void createNotificationChannelsForPackage(String paramString, int paramInt, ParceledListSlice paramParceledListSlice)
    throws RemoteException;
  
  public abstract void deleteNotificationChannel(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void deleteNotificationChannelGroup(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void enqueueNotificationWithTag(String paramString1, String paramString2, String paramString3, int paramInt1, Notification paramNotification, int paramInt2)
    throws RemoteException;
  
  public abstract void enqueueToast(String paramString, ITransientNotification paramITransientNotification, int paramInt)
    throws RemoteException;
  
  public abstract void finishToken(String paramString, ITransientNotification paramITransientNotification)
    throws RemoteException;
  
  public abstract StatusBarNotification[] getActiveNotifications(String paramString)
    throws RemoteException;
  
  public abstract ParceledListSlice getActiveNotificationsFromListener(INotificationListener paramINotificationListener, String[] paramArrayOfString, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getAppActiveNotifications(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract AutomaticZenRule getAutomaticZenRule(String paramString)
    throws RemoteException;
  
  public abstract byte[] getBackupPayload(int paramInt)
    throws RemoteException;
  
  public abstract int getBlockedAppCount(int paramInt)
    throws RemoteException;
  
  public abstract int getBlockedChannelCount(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int getDeletedChannelCount(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ComponentName getEffectsSuppressor()
    throws RemoteException;
  
  public abstract List<String> getEnabledNotificationListenerPackages()
    throws RemoteException;
  
  public abstract List<ComponentName> getEnabledNotificationListeners(int paramInt)
    throws RemoteException;
  
  public abstract String[] getGameDndBlackPackages()
    throws RemoteException;
  
  public abstract boolean getGameDndLock(String paramString)
    throws RemoteException;
  
  public abstract int getHintsFromListener(INotificationListener paramINotificationListener)
    throws RemoteException;
  
  public abstract StatusBarNotification[] getHistoricalNotifications(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int getInterruptionFilterFromListener(INotificationListener paramINotificationListener)
    throws RemoteException;
  
  public abstract NotificationChannel getNotificationChannel(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract NotificationChannel getNotificationChannelForPackage(String paramString1, int paramInt, String paramString2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract NotificationChannelGroup getNotificationChannelGroup(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract NotificationChannelGroup getNotificationChannelGroupForPackage(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getNotificationChannelGroups(String paramString)
    throws RemoteException;
  
  public abstract ParceledListSlice getNotificationChannelGroupsForPackage(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract ParceledListSlice getNotificationChannelGroupsFromPrivilegedListener(INotificationListener paramINotificationListener, String paramString, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract ParceledListSlice getNotificationChannels(String paramString)
    throws RemoteException;
  
  public abstract ParceledListSlice getNotificationChannelsForPackage(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract ParceledListSlice getNotificationChannelsFromPrivilegedListener(INotificationListener paramINotificationListener, String paramString, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract NotificationManager.Policy getNotificationPolicy(String paramString)
    throws RemoteException;
  
  public abstract int getNumNotificationChannelsForPackage(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getPackageImportance(String paramString)
    throws RemoteException;
  
  public abstract NotificationChannelGroup getPopulatedNotificationChannelGroupForPackage(String paramString1, int paramInt, String paramString2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract ParceledListSlice getRecentNotifyingAppsForUser(int paramInt)
    throws RemoteException;
  
  public abstract int getRuleInstanceCount(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract ParceledListSlice getSnoozedNotificationsFromListener(INotificationListener paramINotificationListener, int paramInt)
    throws RemoteException;
  
  public abstract int getZenMode()
    throws RemoteException;
  
  public abstract ZenModeConfig getZenModeConfig()
    throws RemoteException;
  
  public abstract List<ZenModeConfig.ZenRule> getZenRules()
    throws RemoteException;
  
  public abstract boolean isGameDndBlackPackage(String paramString)
    throws RemoteException;
  
  public abstract boolean isNotificationAssistantAccessGranted(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isNotificationListenerAccessGranted(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isNotificationListenerAccessGrantedForUser(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract boolean isNotificationPolicyAccessGranted(String paramString)
    throws RemoteException;
  
  public abstract boolean isNotificationPolicyAccessGrantedForPackage(String paramString)
    throws RemoteException;
  
  public abstract boolean isSystemConditionProviderEnabled(String paramString)
    throws RemoteException;
  
  public abstract boolean matchesCallFilter(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void notifyConditions(String paramString, IConditionProvider paramIConditionProvider, Condition[] paramArrayOfCondition)
    throws RemoteException;
  
  public abstract boolean onlyHasDefaultChannel(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void registerListener(INotificationListener paramINotificationListener, ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract boolean removeAutomaticZenRule(String paramString)
    throws RemoteException;
  
  public abstract boolean removeAutomaticZenRules(String paramString)
    throws RemoteException;
  
  public abstract void removeGameDndBlackPackage(String paramString, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void requestBindListener(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void requestBindProvider(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void requestHintsFromListener(INotificationListener paramINotificationListener, int paramInt)
    throws RemoteException;
  
  public abstract void requestInterruptionFilterFromListener(INotificationListener paramINotificationListener, int paramInt)
    throws RemoteException;
  
  public abstract void requestUnbindListener(INotificationListener paramINotificationListener)
    throws RemoteException;
  
  public abstract void requestUnbindProvider(IConditionProvider paramIConditionProvider)
    throws RemoteException;
  
  public abstract void setGameDndLock(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setInterruptionFilter(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setNotificationAssistantAccessGranted(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setNotificationAssistantAccessGrantedForUser(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setNotificationListenerAccessGranted(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setNotificationListenerAccessGrantedForUser(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setNotificationPolicy(String paramString, NotificationManager.Policy paramPolicy)
    throws RemoteException;
  
  public abstract void setNotificationPolicyAccessGranted(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setNotificationPolicyAccessGrantedForUser(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setNotificationsEnabledForPackage(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setNotificationsEnabledWithImportanceLockForPackage(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setNotificationsShownFromListener(INotificationListener paramINotificationListener, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void setOnNotificationPostedTrimFromListener(INotificationListener paramINotificationListener, int paramInt)
    throws RemoteException;
  
  public abstract void setShowBadge(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setZenMode(int paramInt, Uri paramUri, String paramString)
    throws RemoteException;
  
  public abstract void snoozeNotificationUntilContextFromListener(INotificationListener paramINotificationListener, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void snoozeNotificationUntilFromListener(INotificationListener paramINotificationListener, String paramString, long paramLong)
    throws RemoteException;
  
  public abstract void unregisterListener(INotificationListener paramINotificationListener, int paramInt)
    throws RemoteException;
  
  public abstract void unsnoozeNotificationFromAssistant(INotificationListener paramINotificationListener, String paramString)
    throws RemoteException;
  
  public abstract boolean updateAutomaticZenRule(String paramString, AutomaticZenRule paramAutomaticZenRule)
    throws RemoteException;
  
  public abstract void updateNotificationChannelForPackage(String paramString, int paramInt, NotificationChannel paramNotificationChannel)
    throws RemoteException;
  
  public abstract void updateNotificationChannelFromPrivilegedListener(INotificationListener paramINotificationListener, String paramString, UserHandle paramUserHandle, NotificationChannel paramNotificationChannel)
    throws RemoteException;
  
  public abstract void updateNotificationChannelGroupForPackage(String paramString, int paramInt, NotificationChannelGroup paramNotificationChannelGroup)
    throws RemoteException;
  
  public abstract void updateNotificationChannelGroupFromPrivilegedListener(INotificationListener paramINotificationListener, String paramString, UserHandle paramUserHandle, NotificationChannelGroup paramNotificationChannelGroup)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INotificationManager
  {
    private static final String DESCRIPTOR = "android.app.INotificationManager";
    static final int TRANSACTION_addAutomaticZenRule = 91;
    static final int TRANSACTION_addGameDndBlackPackage = 102;
    static final int TRANSACTION_applyAdjustmentFromAssistant = 64;
    static final int TRANSACTION_applyAdjustmentsFromAssistant = 65;
    static final int TRANSACTION_applyEnqueuedAdjustmentFromAssistant = 63;
    static final int TRANSACTION_applyRestore = 97;
    static final int TRANSACTION_areChannelsBypassingDnd = 37;
    static final int TRANSACTION_areNotificationsEnabled = 13;
    static final int TRANSACTION_areNotificationsEnabledForPackage = 12;
    static final int TRANSACTION_canShowBadge = 9;
    static final int TRANSACTION_cancelAllNotifications = 1;
    static final int TRANSACTION_cancelNotificationFromListener = 42;
    static final int TRANSACTION_cancelNotificationWithTag = 7;
    static final int TRANSACTION_cancelNotificationsFromListener = 43;
    static final int TRANSACTION_cancelToast = 4;
    static final int TRANSACTION_clearData = 2;
    static final int TRANSACTION_createNotificationChannelGroups = 15;
    static final int TRANSACTION_createNotificationChannels = 16;
    static final int TRANSACTION_createNotificationChannelsForPackage = 17;
    static final int TRANSACTION_deleteNotificationChannel = 25;
    static final int TRANSACTION_deleteNotificationChannelGroup = 31;
    static final int TRANSACTION_enqueueNotificationWithTag = 6;
    static final int TRANSACTION_enqueueToast = 3;
    static final int TRANSACTION_finishToken = 5;
    static final int TRANSACTION_getActiveNotifications = 38;
    static final int TRANSACTION_getActiveNotificationsFromListener = 51;
    static final int TRANSACTION_getAppActiveNotifications = 98;
    static final int TRANSACTION_getAutomaticZenRule = 89;
    static final int TRANSACTION_getBackupPayload = 96;
    static final int TRANSACTION_getBlockedAppCount = 36;
    static final int TRANSACTION_getBlockedChannelCount = 30;
    static final int TRANSACTION_getDeletedChannelCount = 29;
    static final int TRANSACTION_getEffectsSuppressor = 67;
    static final int TRANSACTION_getEnabledNotificationListenerPackages = 77;
    static final int TRANSACTION_getEnabledNotificationListeners = 78;
    static final int TRANSACTION_getGameDndBlackPackages = 104;
    static final int TRANSACTION_getGameDndLock = 100;
    static final int TRANSACTION_getHintsFromListener = 54;
    static final int TRANSACTION_getHistoricalNotifications = 39;
    static final int TRANSACTION_getInterruptionFilterFromListener = 56;
    static final int TRANSACTION_getNotificationChannel = 23;
    static final int TRANSACTION_getNotificationChannelForPackage = 24;
    static final int TRANSACTION_getNotificationChannelGroup = 32;
    static final int TRANSACTION_getNotificationChannelGroupForPackage = 19;
    static final int TRANSACTION_getNotificationChannelGroups = 33;
    static final int TRANSACTION_getNotificationChannelGroupsForPackage = 18;
    static final int TRANSACTION_getNotificationChannelGroupsFromPrivilegedListener = 62;
    static final int TRANSACTION_getNotificationChannels = 26;
    static final int TRANSACTION_getNotificationChannelsForPackage = 27;
    static final int TRANSACTION_getNotificationChannelsFromPrivilegedListener = 61;
    static final int TRANSACTION_getNotificationPolicy = 84;
    static final int TRANSACTION_getNumNotificationChannelsForPackage = 28;
    static final int TRANSACTION_getPackageImportance = 14;
    static final int TRANSACTION_getPopulatedNotificationChannelGroupForPackage = 20;
    static final int TRANSACTION_getRecentNotifyingAppsForUser = 35;
    static final int TRANSACTION_getRuleInstanceCount = 95;
    static final int TRANSACTION_getSnoozedNotificationsFromListener = 52;
    static final int TRANSACTION_getZenMode = 79;
    static final int TRANSACTION_getZenModeConfig = 80;
    static final int TRANSACTION_getZenRules = 90;
    static final int TRANSACTION_isGameDndBlackPackage = 101;
    static final int TRANSACTION_isNotificationAssistantAccessGranted = 72;
    static final int TRANSACTION_isNotificationListenerAccessGranted = 70;
    static final int TRANSACTION_isNotificationListenerAccessGrantedForUser = 71;
    static final int TRANSACTION_isNotificationPolicyAccessGranted = 83;
    static final int TRANSACTION_isNotificationPolicyAccessGrantedForPackage = 86;
    static final int TRANSACTION_isSystemConditionProviderEnabled = 69;
    static final int TRANSACTION_matchesCallFilter = 68;
    static final int TRANSACTION_notifyConditions = 82;
    static final int TRANSACTION_onlyHasDefaultChannel = 34;
    static final int TRANSACTION_registerListener = 40;
    static final int TRANSACTION_removeAutomaticZenRule = 93;
    static final int TRANSACTION_removeAutomaticZenRules = 94;
    static final int TRANSACTION_removeGameDndBlackPackage = 103;
    static final int TRANSACTION_requestBindListener = 46;
    static final int TRANSACTION_requestBindProvider = 48;
    static final int TRANSACTION_requestHintsFromListener = 53;
    static final int TRANSACTION_requestInterruptionFilterFromListener = 55;
    static final int TRANSACTION_requestUnbindListener = 47;
    static final int TRANSACTION_requestUnbindProvider = 49;
    static final int TRANSACTION_setGameDndLock = 99;
    static final int TRANSACTION_setInterruptionFilter = 58;
    static final int TRANSACTION_setNotificationAssistantAccessGranted = 74;
    static final int TRANSACTION_setNotificationAssistantAccessGrantedForUser = 76;
    static final int TRANSACTION_setNotificationListenerAccessGranted = 73;
    static final int TRANSACTION_setNotificationListenerAccessGrantedForUser = 75;
    static final int TRANSACTION_setNotificationPolicy = 85;
    static final int TRANSACTION_setNotificationPolicyAccessGranted = 87;
    static final int TRANSACTION_setNotificationPolicyAccessGrantedForUser = 88;
    static final int TRANSACTION_setNotificationsEnabledForPackage = 10;
    static final int TRANSACTION_setNotificationsEnabledWithImportanceLockForPackage = 11;
    static final int TRANSACTION_setNotificationsShownFromListener = 50;
    static final int TRANSACTION_setOnNotificationPostedTrimFromListener = 57;
    static final int TRANSACTION_setShowBadge = 8;
    static final int TRANSACTION_setZenMode = 81;
    static final int TRANSACTION_snoozeNotificationUntilContextFromListener = 44;
    static final int TRANSACTION_snoozeNotificationUntilFromListener = 45;
    static final int TRANSACTION_unregisterListener = 41;
    static final int TRANSACTION_unsnoozeNotificationFromAssistant = 66;
    static final int TRANSACTION_updateAutomaticZenRule = 92;
    static final int TRANSACTION_updateNotificationChannelForPackage = 22;
    static final int TRANSACTION_updateNotificationChannelFromPrivilegedListener = 60;
    static final int TRANSACTION_updateNotificationChannelGroupForPackage = 21;
    static final int TRANSACTION_updateNotificationChannelGroupFromPrivilegedListener = 59;
    
    public Stub()
    {
      attachInterface(this, "android.app.INotificationManager");
    }
    
    public static INotificationManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.INotificationManager");
      if ((localIInterface != null) && ((localIInterface instanceof INotificationManager))) {
        return (INotificationManager)localIInterface;
      }
      return new Proxy(paramIBinder);
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
        String str = null;
        Object localObject18 = null;
        Object localObject19 = null;
        Object localObject20 = null;
        Object localObject21 = null;
        Object localObject22 = null;
        Object localObject23 = null;
        Object localObject24 = null;
        Object localObject25 = null;
        Object localObject26 = null;
        Object localObject27 = null;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 104: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getGameDndBlackPackages();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 103: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          removeGameDndBlackPackage(paramParcel1.readString(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 102: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          addGameDndBlackPackage(paramParcel1.readString(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 101: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = isGameDndBlackPackage(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 100: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = getGameDndLock(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 99: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setGameDndLock((String)localObject26, bool12);
          paramParcel2.writeNoException();
          return true;
        case 98: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getAppActiveNotifications(paramParcel1.readString(), paramParcel1.readInt());
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
        case 97: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          applyRestore(paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 96: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getBackupPayload(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 95: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject27;
          }
          paramInt1 = getRuleInstanceCount(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 94: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = removeAutomaticZenRules(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 93: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = removeAutomaticZenRule(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 92: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AutomaticZenRule)AutomaticZenRule.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = updateAutomaticZenRule((String)localObject26, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 91: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AutomaticZenRule)AutomaticZenRule.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramParcel1 = addAutomaticZenRule(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 90: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getZenRules();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 89: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getAutomaticZenRule(paramParcel1.readString());
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
        case 88: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          bool12 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setNotificationPolicyAccessGrantedForUser((String)localObject26, paramInt1, bool12);
          paramParcel2.writeNoException();
          return true;
        case 87: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          bool12 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setNotificationPolicyAccessGranted((String)localObject26, bool12);
          paramParcel2.writeNoException();
          return true;
        case 86: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = isNotificationPolicyAccessGrantedForPackage(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 85: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NotificationManager.Policy)NotificationManager.Policy.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          setNotificationPolicy((String)localObject26, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 84: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getNotificationPolicy(paramParcel1.readString());
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
        case 83: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = isNotificationPolicyAccessGranted(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 82: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          notifyConditions(paramParcel1.readString(), IConditionProvider.Stub.asInterface(paramParcel1.readStrongBinder()), (Condition[])paramParcel1.createTypedArray(Condition.CREATOR));
          return true;
        case 81: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject4;
          }
          setZenMode(paramInt1, paramParcel2, paramParcel1.readString());
          return true;
        case 80: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getZenModeConfig();
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
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = getZenMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 78: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getEnabledNotificationListeners(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 77: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getEnabledNotificationListenerPackages();
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 76: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          if (paramParcel1.readInt() != 0) {
            localObject26 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject26 = localObject5;
          }
          paramInt1 = paramParcel1.readInt();
          bool12 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setNotificationAssistantAccessGrantedForUser((ComponentName)localObject26, paramInt1, bool12);
          paramParcel2.writeNoException();
          return true;
        case 75: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          if (paramParcel1.readInt() != 0) {
            localObject26 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject26 = localObject6;
          }
          paramInt1 = paramParcel1.readInt();
          bool12 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setNotificationListenerAccessGrantedForUser((ComponentName)localObject26, paramInt1, bool12);
          paramParcel2.writeNoException();
          return true;
        case 74: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          if (paramParcel1.readInt() != 0) {
            localObject26 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject26 = localObject7;
          }
          bool12 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setNotificationAssistantAccessGranted((ComponentName)localObject26, bool12);
          paramParcel2.writeNoException();
          return true;
        case 73: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          if (paramParcel1.readInt() != 0) {
            localObject26 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject26 = localObject8;
          }
          bool12 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setNotificationListenerAccessGranted((ComponentName)localObject26, bool12);
          paramParcel2.writeNoException();
          return true;
        case 72: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          paramInt1 = isNotificationAssistantAccessGranted(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 71: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          if (paramParcel1.readInt() != 0) {
            localObject26 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject26 = localObject10;
          }
          paramInt1 = isNotificationListenerAccessGrantedForUser((ComponentName)localObject26, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 70: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject11;
          }
          paramInt1 = isNotificationListenerAccessGranted(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 69: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = isSystemConditionProviderEnabled(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 68: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          paramInt1 = matchesCallFilter(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 67: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getEffectsSuppressor();
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
        case 66: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          unsnoozeNotificationFromAssistant(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 65: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          applyAdjustmentsFromAssistant(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.createTypedArrayList(Adjustment.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 64: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Adjustment)Adjustment.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject13;
          }
          applyAdjustmentFromAssistant((INotificationListener)localObject26, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 63: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Adjustment)Adjustment.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject14;
          }
          applyEnqueuedAdjustmentFromAssistant((INotificationListener)localObject26, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 62: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject17 = INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject26 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject15;
          }
          paramParcel1 = getNotificationChannelGroupsFromPrivilegedListener((INotificationListener)localObject17, (String)localObject26, paramParcel1);
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
        case 61: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject17 = INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject26 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject16;
          }
          paramParcel1 = getNotificationChannelsFromPrivilegedListener((INotificationListener)localObject17, (String)localObject26, paramParcel1);
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
        case 60: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject15 = INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder());
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject26 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject26 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NotificationChannel)NotificationChannel.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject17;
          }
          updateNotificationChannelFromPrivilegedListener((INotificationListener)localObject15, str, (UserHandle)localObject26, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 59: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject17 = INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject15 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject26 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject26 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NotificationChannelGroup)NotificationChannelGroup.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          updateNotificationChannelGroupFromPrivilegedListener((INotificationListener)localObject17, (String)localObject15, (UserHandle)localObject26, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 58: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          setInterruptionFilter(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 57: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          setOnNotificationPostedTrimFromListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 56: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = getInterruptionFilterFromListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 55: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          requestInterruptionFilterFromListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 54: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = getHintsFromListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 53: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          requestHintsFromListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 52: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getSnoozedNotificationsFromListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
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
        case 51: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getActiveNotificationsFromListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.createStringArray(), paramParcel1.readInt());
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
        case 50: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          setNotificationsShownFromListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 49: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          requestUnbindProvider(IConditionProvider.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject18;
          }
          requestBindProvider(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          requestUnbindListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject19;
          }
          requestBindListener(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          snoozeNotificationUntilFromListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          snoozeNotificationUntilContextFromListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          cancelNotificationsFromListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          cancelNotificationFromListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          unregisterListener(INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject17 = INotificationListener.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject26 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject26 = localObject20;
          }
          registerListener((INotificationListener)localObject17, (ComponentName)localObject26, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getHistoricalNotifications(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getActiveNotifications(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = areChannelsBypassingDnd();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = getBlockedAppCount(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getRecentNotifyingAppsForUser(paramParcel1.readInt());
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
        case 34: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = onlyHasDefaultChannel(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getNotificationChannelGroups(paramParcel1.readString());
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
        case 32: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getNotificationChannelGroup(paramParcel1.readString(), paramParcel1.readString());
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
        case 31: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          deleteNotificationChannelGroup(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = getBlockedChannelCount(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = getDeletedChannelCount(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          bool12 = bool7;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          paramInt1 = getNumNotificationChannelsForPackage((String)localObject26, paramInt1, bool12);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          } else {
            bool12 = false;
          }
          paramParcel1 = getNotificationChannelsForPackage((String)localObject26, paramInt1, bool12);
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
        case 26: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getNotificationChannels(paramParcel1.readString());
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
        case 25: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          deleteNotificationChannel(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          localObject17 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          } else {
            bool12 = false;
          }
          paramParcel1 = getNotificationChannelForPackage((String)localObject26, paramInt1, (String)localObject17, bool12);
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
        case 23: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getNotificationChannel(paramParcel1.readString(), paramParcel1.readString());
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
        case 22: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NotificationChannel)NotificationChannel.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject21;
          }
          updateNotificationChannelForPackage((String)localObject26, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NotificationChannelGroup)NotificationChannelGroup.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject22;
          }
          updateNotificationChannelGroupForPackage((String)localObject26, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject17 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          localObject26 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          } else {
            bool12 = false;
          }
          paramParcel1 = getPopulatedNotificationChannelGroupForPackage((String)localObject17, paramInt1, (String)localObject26, bool12);
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
        case 19: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramParcel1 = getNotificationChannelGroupForPackage(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          } else {
            bool12 = false;
          }
          paramParcel1 = getNotificationChannelGroupsForPackage((String)localObject26, paramInt1, bool12);
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
        case 17: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject23;
          }
          createNotificationChannelsForPackage((String)localObject26, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject24;
          }
          createNotificationChannels((String)localObject26, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject25;
          }
          createNotificationChannelGroups((String)localObject26, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = getPackageImportance(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = areNotificationsEnabled(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = areNotificationsEnabledForPackage(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          bool12 = bool8;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setNotificationsEnabledWithImportanceLockForPackage((String)localObject26, paramInt1, bool12);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          bool12 = bool9;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setNotificationsEnabledForPackage((String)localObject26, paramInt1, bool12);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          paramInt1 = canShowBadge(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          bool12 = bool10;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setShowBadge((String)localObject26, paramInt1, bool12);
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          cancelNotificationWithTag(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject17 = paramParcel1.readString();
          localObject15 = paramParcel1.readString();
          str = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject26 = (Notification)Notification.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          enqueueNotificationWithTag((String)localObject17, (String)localObject15, str, paramInt1, (Notification)localObject26, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          finishToken(paramParcel1.readString(), ITransientNotification.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          cancelToast(paramParcel1.readString(), ITransientNotification.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          enqueueToast(paramParcel1.readString(), ITransientNotification.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.INotificationManager");
          localObject26 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          bool12 = bool11;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          clearData((String)localObject26, paramInt1, bool12);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.app.INotificationManager");
        cancelAllNotifications(paramParcel1.readString(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.app.INotificationManager");
      return true;
    }
    
    private static class Proxy
      implements INotificationManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public String addAutomaticZenRule(AutomaticZenRule paramAutomaticZenRule)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramAutomaticZenRule != null)
          {
            localParcel1.writeInt(1);
            paramAutomaticZenRule.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(91, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramAutomaticZenRule = localParcel2.readString();
          return paramAutomaticZenRule;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addGameDndBlackPackage(String paramString, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(102, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void applyAdjustmentFromAssistant(INotificationListener paramINotificationListener, Adjustment paramAdjustment)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          if (paramAdjustment != null)
          {
            localParcel1.writeInt(1);
            paramAdjustment.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void applyAdjustmentsFromAssistant(INotificationListener paramINotificationListener, List<Adjustment> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeTypedList(paramList);
          mRemote.transact(65, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void applyEnqueuedAdjustmentFromAssistant(INotificationListener paramINotificationListener, Adjustment paramAdjustment)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          if (paramAdjustment != null)
          {
            localParcel1.writeInt(1);
            paramAdjustment.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void applyRestore(byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeByteArray(paramArrayOfByte);
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
      
      public boolean areChannelsBypassingDnd()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(37, localParcel1, localParcel2, 0);
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
      
      public boolean areNotificationsEnabled(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(13, localParcel1, localParcel2, 0);
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
      
      public boolean areNotificationsEnabledForPackage(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(12, localParcel1, localParcel2, 0);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean canShowBadge(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(9, localParcel1, localParcel2, 0);
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
      
      public void cancelAllNotifications(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void cancelNotificationFromListener(INotificationListener paramINotificationListener, String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void cancelNotificationWithTag(String paramString1, String paramString2, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void cancelNotificationsFromListener(INotificationListener paramINotificationListener, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void cancelToast(String paramString, ITransientNotification paramITransientNotification)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          if (paramITransientNotification != null) {
            paramString = paramITransientNotification.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearData(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
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
      
      public void createNotificationChannelGroups(String paramString, ParceledListSlice paramParceledListSlice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          if (paramParceledListSlice != null)
          {
            localParcel1.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void createNotificationChannels(String paramString, ParceledListSlice paramParceledListSlice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          if (paramParceledListSlice != null)
          {
            localParcel1.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void createNotificationChannelsForPackage(String paramString, int paramInt, ParceledListSlice paramParceledListSlice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          if (paramParceledListSlice != null)
          {
            localParcel1.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void deleteNotificationChannel(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void deleteNotificationChannelGroup(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enqueueNotificationWithTag(String paramString1, String paramString2, String paramString3, int paramInt1, Notification paramNotification, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
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
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enqueueToast(String paramString, ITransientNotification paramITransientNotification, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          if (paramITransientNotification != null) {
            paramString = paramITransientNotification.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void finishToken(String paramString, ITransientNotification paramITransientNotification)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          if (paramITransientNotification != null) {
            paramString = paramITransientNotification.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public StatusBarNotification[] getActiveNotifications(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = (StatusBarNotification[])localParcel2.createTypedArray(StatusBarNotification.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getActiveNotificationsFromListener(INotificationListener paramINotificationListener, String[] paramArrayOfString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          Object localObject = null;
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramINotificationListener = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramINotificationListener = localObject;
          }
          return paramINotificationListener;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getAppActiveNotifications(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(98, localParcel1, localParcel2, 0);
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
      
      public AutomaticZenRule getAutomaticZenRule(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          mRemote.transact(89, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (AutomaticZenRule)AutomaticZenRule.CREATOR.createFromParcel(localParcel2);
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
      
      public byte[] getBackupPayload(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(96, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getBlockedAppCount(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(36, localParcel1, localParcel2, 0);
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
      
      public int getBlockedChannelCount(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(30, localParcel1, localParcel2, 0);
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
      
      public int getDeletedChannelCount(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(29, localParcel1, localParcel2, 0);
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
      
      public ComponentName getEffectsSuppressor()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          mRemote.transact(67, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getEnabledNotificationListenerPackages()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          mRemote.transact(77, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createStringArrayList();
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ComponentName> getEnabledNotificationListeners(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(78, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(ComponentName.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getGameDndBlackPackages()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          mRemote.transact(104, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getGameDndLock(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(100, localParcel1, localParcel2, 0);
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
      
      public int getHintsFromListener(INotificationListener paramINotificationListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          mRemote.transact(54, localParcel1, localParcel2, 0);
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
      
      public StatusBarNotification[] getHistoricalNotifications(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = (StatusBarNotification[])localParcel2.createTypedArray(StatusBarNotification.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.INotificationManager";
      }
      
      public int getInterruptionFilterFromListener(INotificationListener paramINotificationListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          mRemote.transact(56, localParcel1, localParcel2, 0);
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
      
      public NotificationChannel getNotificationChannel(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (NotificationChannel)NotificationChannel.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NotificationChannel getNotificationChannelForPackage(String paramString1, int paramInt, String paramString2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (NotificationChannel)NotificationChannel.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NotificationChannelGroup getNotificationChannelGroup(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (NotificationChannelGroup)NotificationChannelGroup.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NotificationChannelGroup getNotificationChannelGroupForPackage(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (NotificationChannelGroup)NotificationChannelGroup.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getNotificationChannelGroups(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          mRemote.transact(33, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getNotificationChannelGroupsForPackage(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(18, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getNotificationChannelGroupsFromPrivilegedListener(INotificationListener paramINotificationListener, String paramString, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          Object localObject = null;
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeString(paramString);
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(62, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramINotificationListener = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramINotificationListener = localObject;
          }
          return paramINotificationListener;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getNotificationChannels(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          mRemote.transact(26, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getNotificationChannelsForPackage(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(27, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getNotificationChannelsFromPrivilegedListener(INotificationListener paramINotificationListener, String paramString, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          Object localObject = null;
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeString(paramString);
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(61, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramINotificationListener = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramINotificationListener = localObject;
          }
          return paramINotificationListener;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NotificationManager.Policy getNotificationPolicy(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          mRemote.transact(84, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (NotificationManager.Policy)NotificationManager.Policy.CREATOR.createFromParcel(localParcel2);
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
      
      public int getNumNotificationChannelsForPackage(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(28, localParcel1, localParcel2, 0);
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
      
      public int getPackageImportance(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          mRemote.transact(14, localParcel1, localParcel2, 0);
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
      
      public NotificationChannelGroup getPopulatedNotificationChannelGroupForPackage(String paramString1, int paramInt, String paramString2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (NotificationChannelGroup)NotificationChannelGroup.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getRecentNotifyingAppsForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(35, localParcel1, localParcel2, 0);
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
      
      public int getRuleInstanceCount(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(95, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getSnoozedNotificationsFromListener(INotificationListener paramINotificationListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          Object localObject = null;
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeInt(paramInt);
          mRemote.transact(52, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramINotificationListener = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramINotificationListener = localObject;
          }
          return paramINotificationListener;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getZenMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          mRemote.transact(79, localParcel1, localParcel2, 0);
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
      
      public ZenModeConfig getZenModeConfig()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          mRemote.transact(80, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ZenModeConfig localZenModeConfig;
          if (localParcel2.readInt() != 0) {
            localZenModeConfig = (ZenModeConfig)ZenModeConfig.CREATOR.createFromParcel(localParcel2);
          } else {
            localZenModeConfig = null;
          }
          return localZenModeConfig;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ZenModeConfig.ZenRule> getZenRules()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          mRemote.transact(90, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(ZenModeConfig.ZenRule.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isGameDndBlackPackage(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(101, localParcel1, localParcel2, 0);
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
      
      public boolean isNotificationAssistantAccessGranted(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
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
          mRemote.transact(72, localParcel1, localParcel2, 0);
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
      
      public boolean isNotificationListenerAccessGranted(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
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
          mRemote.transact(70, localParcel1, localParcel2, 0);
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
      
      public boolean isNotificationListenerAccessGrantedForUser(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
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
          localParcel1.writeInt(paramInt);
          mRemote.transact(71, localParcel1, localParcel2, 0);
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
      
      public boolean isNotificationPolicyAccessGranted(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(83, localParcel1, localParcel2, 0);
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
      
      public boolean isNotificationPolicyAccessGrantedForPackage(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(86, localParcel1, localParcel2, 0);
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
      
      public boolean isSystemConditionProviderEnabled(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(69, localParcel1, localParcel2, 0);
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
      
      public boolean matchesCallFilter(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
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
          mRemote.transact(68, localParcel1, localParcel2, 0);
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
      
      public void notifyConditions(String paramString, IConditionProvider paramIConditionProvider, Condition[] paramArrayOfCondition)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.INotificationManager");
          localParcel.writeString(paramString);
          if (paramIConditionProvider != null) {
            paramString = paramIConditionProvider.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          localParcel.writeTypedArray(paramArrayOfCondition, 0);
          mRemote.transact(82, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public boolean onlyHasDefaultChannel(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(34, localParcel1, localParcel2, 0);
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
      
      public void registerListener(INotificationListener paramINotificationListener, ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
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
      
      public boolean removeAutomaticZenRule(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(93, localParcel1, localParcel2, 0);
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
      
      public boolean removeAutomaticZenRules(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(94, localParcel1, localParcel2, 0);
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
      
      public void removeGameDndBlackPackage(String paramString, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(103, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestBindListener(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestBindProvider(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestHintsFromListener(INotificationListener paramINotificationListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeInt(paramInt);
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
      
      public void requestInterruptionFilterFromListener(INotificationListener paramINotificationListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
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
      
      public void requestUnbindListener(INotificationListener paramINotificationListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
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
      
      public void requestUnbindProvider(IConditionProvider paramIConditionProvider)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramIConditionProvider != null) {
            paramIConditionProvider = paramIConditionProvider.asBinder();
          } else {
            paramIConditionProvider = null;
          }
          localParcel1.writeStrongBinder(paramIConditionProvider);
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setGameDndLock(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
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
      
      public void setInterruptionFilter(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(58, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNotificationAssistantAccessGranted(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(74, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNotificationAssistantAccessGrantedForUser(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(76, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNotificationListenerAccessGranted(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(73, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNotificationListenerAccessGrantedForUser(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(75, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNotificationPolicy(String paramString, NotificationManager.Policy paramPolicy)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          if (paramPolicy != null)
          {
            localParcel1.writeInt(1);
            paramPolicy.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(85, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNotificationPolicyAccessGranted(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
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
      
      public void setNotificationPolicyAccessGrantedForUser(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(88, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNotificationsEnabledForPackage(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
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
      
      public void setNotificationsEnabledWithImportanceLockForPackage(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNotificationsShownFromListener(INotificationListener paramINotificationListener, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setOnNotificationPostedTrimFromListener(INotificationListener paramINotificationListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeInt(paramInt);
          mRemote.transact(57, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setShowBadge(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setZenMode(int paramInt, Uri paramUri, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.INotificationManager");
          localParcel.writeInt(paramInt);
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString);
          mRemote.transact(81, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void snoozeNotificationUntilContextFromListener(INotificationListener paramINotificationListener, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void snoozeNotificationUntilFromListener(INotificationListener paramINotificationListener, String paramString, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterListener(INotificationListener paramINotificationListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeInt(paramInt);
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
      
      public void unsnoozeNotificationFromAssistant(INotificationListener paramINotificationListener, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeString(paramString);
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
      
      public boolean updateAutomaticZenRule(String paramString, AutomaticZenRule paramAutomaticZenRule)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramAutomaticZenRule != null)
          {
            localParcel1.writeInt(1);
            paramAutomaticZenRule.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(92, localParcel1, localParcel2, 0);
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
      
      public void updateNotificationChannelForPackage(String paramString, int paramInt, NotificationChannel paramNotificationChannel)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          if (paramNotificationChannel != null)
          {
            localParcel1.writeInt(1);
            paramNotificationChannel.writeToParcel(localParcel1, 0);
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
      
      public void updateNotificationChannelFromPrivilegedListener(INotificationListener paramINotificationListener, String paramString, UserHandle paramUserHandle, NotificationChannel paramNotificationChannel)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeString(paramString);
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramNotificationChannel != null)
          {
            localParcel1.writeInt(1);
            paramNotificationChannel.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void updateNotificationChannelGroupForPackage(String paramString, int paramInt, NotificationChannelGroup paramNotificationChannelGroup)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          if (paramNotificationChannelGroup != null)
          {
            localParcel1.writeInt(1);
            paramNotificationChannelGroup.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateNotificationChannelGroupFromPrivilegedListener(INotificationListener paramINotificationListener, String paramString, UserHandle paramUserHandle, NotificationChannelGroup paramNotificationChannelGroup)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.INotificationManager");
          if (paramINotificationListener != null) {
            paramINotificationListener = paramINotificationListener.asBinder();
          } else {
            paramINotificationListener = null;
          }
          localParcel1.writeStrongBinder(paramINotificationListener);
          localParcel1.writeString(paramString);
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramNotificationChannelGroup != null)
          {
            localParcel1.writeInt(1);
            paramNotificationChannelGroup.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(59, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
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
