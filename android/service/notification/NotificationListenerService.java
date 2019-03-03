package android.service.notification;

import android.annotation.SystemApi;
import android.app.INotificationManager;
import android.app.INotificationManager.Stub;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.Person;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ParceledListSlice;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class NotificationListenerService
  extends Service
{
  public static final int HINT_HOST_DISABLE_CALL_EFFECTS = 4;
  public static final int HINT_HOST_DISABLE_EFFECTS = 1;
  public static final int HINT_HOST_DISABLE_NOTIFICATION_EFFECTS = 2;
  public static final int INTERRUPTION_FILTER_ALARMS = 4;
  public static final int INTERRUPTION_FILTER_ALL = 1;
  public static final int INTERRUPTION_FILTER_NONE = 3;
  public static final int INTERRUPTION_FILTER_PRIORITY = 2;
  public static final int INTERRUPTION_FILTER_UNKNOWN = 0;
  public static final int NOTIFICATION_CHANNEL_OR_GROUP_ADDED = 1;
  public static final int NOTIFICATION_CHANNEL_OR_GROUP_DELETED = 3;
  public static final int NOTIFICATION_CHANNEL_OR_GROUP_UPDATED = 2;
  public static final int REASON_APP_CANCEL = 8;
  public static final int REASON_APP_CANCEL_ALL = 9;
  public static final int REASON_CANCEL = 2;
  public static final int REASON_CANCEL_ALL = 3;
  public static final int REASON_CHANNEL_BANNED = 17;
  public static final int REASON_CLICK = 1;
  public static final int REASON_ERROR = 4;
  public static final int REASON_GROUP_OPTIMIZATION = 13;
  public static final int REASON_GROUP_SUMMARY_CANCELED = 12;
  public static final int REASON_LISTENER_CANCEL = 10;
  public static final int REASON_LISTENER_CANCEL_ALL = 11;
  public static final int REASON_PACKAGE_BANNED = 7;
  public static final int REASON_PACKAGE_CHANGED = 5;
  public static final int REASON_PACKAGE_SUSPENDED = 14;
  public static final int REASON_PROFILE_TURNED_OFF = 15;
  public static final int REASON_SNOOZED = 18;
  public static final int REASON_TIMEOUT = 19;
  public static final int REASON_UNAUTOBUNDLED = 16;
  public static final int REASON_USER_STOPPED = 6;
  public static final String SERVICE_INTERFACE = "android.service.notification.NotificationListenerService";
  @Deprecated
  public static final int SUPPRESSED_EFFECT_SCREEN_OFF = 1;
  @Deprecated
  public static final int SUPPRESSED_EFFECT_SCREEN_ON = 2;
  @SystemApi
  public static final int TRIM_FULL = 0;
  @SystemApi
  public static final int TRIM_LIGHT = 1;
  private final String TAG = getClass().getSimpleName();
  private boolean isConnected = false;
  protected int mCurrentUser;
  private Handler mHandler;
  private final Object mLock = new Object();
  protected INotificationManager mNoMan;
  @GuardedBy("mLock")
  private RankingMap mRankingMap;
  protected Context mSystemContext;
  protected NotificationListenerWrapper mWrapper = null;
  
  public NotificationListenerService() {}
  
  private StatusBarNotification[] cleanUpNotificationList(ParceledListSlice<StatusBarNotification> paramParceledListSlice)
  {
    int i = 0;
    if ((paramParceledListSlice != null) && (paramParceledListSlice.getList() != null))
    {
      List localList = paramParceledListSlice.getList();
      paramParceledListSlice = null;
      int j = localList.size();
      while (i < j)
      {
        StatusBarNotification localStatusBarNotification = (StatusBarNotification)localList.get(i);
        Notification localNotification = localStatusBarNotification.getNotification();
        try
        {
          createLegacyIconExtras(localNotification);
          maybePopulateRemoteViews(localNotification);
          maybePopulatePeople(localNotification);
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          Object localObject = paramParceledListSlice;
          if (paramParceledListSlice == null) {
            localObject = new ArrayList(j);
          }
          ((ArrayList)localObject).add(localStatusBarNotification);
          String str = TAG;
          paramParceledListSlice = new StringBuilder();
          paramParceledListSlice.append("get(Active/Snoozed)Notifications: can't rebuild notification from ");
          paramParceledListSlice.append(localStatusBarNotification.getPackageName());
          Log.w(str, paramParceledListSlice.toString());
          paramParceledListSlice = (ParceledListSlice<StatusBarNotification>)localObject;
        }
        i++;
      }
      if (paramParceledListSlice != null) {
        localList.removeAll(paramParceledListSlice);
      }
      return (StatusBarNotification[])localList.toArray(new StatusBarNotification[localList.size()]);
    }
    return new StatusBarNotification[0];
  }
  
  private void createLegacyIconExtras(Notification paramNotification)
  {
    Object localObject = paramNotification.getSmallIcon();
    Icon localIcon = paramNotification.getLargeIcon();
    if ((localObject != null) && (((Icon)localObject).getType() == 2))
    {
      extras.putInt("android.icon", ((Icon)localObject).getResId());
      icon = ((Icon)localObject).getResId();
    }
    if (localIcon != null)
    {
      localObject = localIcon.loadDrawable(getContext());
      if ((localObject != null) && ((localObject instanceof BitmapDrawable)))
      {
        localObject = ((BitmapDrawable)localObject).getBitmap();
        extras.putParcelable("android.largeIcon", (Parcelable)localObject);
        largeIcon = ((Bitmap)localObject);
      }
    }
  }
  
  private void maybePopulatePeople(Notification paramNotification)
  {
    if (getContextgetApplicationInfotargetSdkVersion < 28)
    {
      ArrayList localArrayList = extras.getParcelableArrayList("android.people.list");
      if ((localArrayList != null) && (localArrayList.isEmpty()))
      {
        int i = localArrayList.size();
        String[] arrayOfString = new String[i];
        for (int j = 0; j < i; j++) {
          arrayOfString[j] = ((Person)localArrayList.get(j)).resolveToLegacyUri();
        }
        extras.putStringArray("android.people", arrayOfString);
      }
    }
  }
  
  private void maybePopulateRemoteViews(Notification paramNotification)
  {
    if (getContextgetApplicationInfotargetSdkVersion < 24)
    {
      Object localObject = Notification.Builder.recoverBuilder(getContext(), paramNotification);
      RemoteViews localRemoteViews1 = ((Notification.Builder)localObject).createContentView();
      RemoteViews localRemoteViews2 = ((Notification.Builder)localObject).createBigContentView();
      localObject = ((Notification.Builder)localObject).createHeadsUpContentView();
      contentView = localRemoteViews1;
      bigContentView = localRemoteViews2;
      headsUpContentView = ((RemoteViews)localObject);
    }
  }
  
  public static void requestRebind(ComponentName paramComponentName)
  {
    INotificationManager localINotificationManager = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
    try
    {
      localINotificationManager.requestBindListener(paramComponentName);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  @GuardedBy("mLock")
  public final void applyUpdateLocked(NotificationRankingUpdate paramNotificationRankingUpdate)
  {
    mRankingMap = new RankingMap(paramNotificationRankingUpdate, null);
  }
  
  protected void attachBaseContext(Context paramContext)
  {
    super.attachBaseContext(paramContext);
    mHandler = new MyHandler(getMainLooper());
  }
  
  public final void cancelAllNotifications()
  {
    cancelNotifications(null);
  }
  
  public final void cancelNotification(String paramString)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().cancelNotificationsFromListener(mWrapper, new String[] { paramString });
    }
    catch (RemoteException paramString)
    {
      Log.v(TAG, "Unable to contact notification manager", paramString);
    }
  }
  
  @Deprecated
  public final void cancelNotification(String paramString1, String paramString2, int paramInt)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().cancelNotificationFromListener(mWrapper, paramString1, paramString2, paramInt);
    }
    catch (RemoteException paramString1)
    {
      Log.v(TAG, "Unable to contact notification manager", paramString1);
    }
  }
  
  public final void cancelNotifications(String[] paramArrayOfString)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().cancelNotificationsFromListener(mWrapper, paramArrayOfString);
    }
    catch (RemoteException paramArrayOfString)
    {
      Log.v(TAG, "Unable to contact notification manager", paramArrayOfString);
    }
  }
  
  public StatusBarNotification[] getActiveNotifications()
  {
    StatusBarNotification[] arrayOfStatusBarNotification = getActiveNotifications(null, 0);
    if (arrayOfStatusBarNotification == null) {
      arrayOfStatusBarNotification = new StatusBarNotification[0];
    }
    return arrayOfStatusBarNotification;
  }
  
  @SystemApi
  public StatusBarNotification[] getActiveNotifications(int paramInt)
  {
    StatusBarNotification[] arrayOfStatusBarNotification = getActiveNotifications(null, paramInt);
    if (arrayOfStatusBarNotification == null) {
      arrayOfStatusBarNotification = new StatusBarNotification[0];
    }
    return arrayOfStatusBarNotification;
  }
  
  public StatusBarNotification[] getActiveNotifications(String[] paramArrayOfString)
  {
    paramArrayOfString = getActiveNotifications(paramArrayOfString, 0);
    if (paramArrayOfString == null) {
      paramArrayOfString = new StatusBarNotification[0];
    }
    return paramArrayOfString;
  }
  
  @SystemApi
  public StatusBarNotification[] getActiveNotifications(String[] paramArrayOfString, int paramInt)
  {
    if (!isBound()) {
      return null;
    }
    try
    {
      paramArrayOfString = cleanUpNotificationList(getNotificationInterface().getActiveNotificationsFromListener(mWrapper, paramArrayOfString, paramInt));
      return paramArrayOfString;
    }
    catch (RemoteException paramArrayOfString)
    {
      Log.v(TAG, "Unable to contact notification manager", paramArrayOfString);
    }
    return null;
  }
  
  protected Context getContext()
  {
    if (mSystemContext != null) {
      return mSystemContext;
    }
    return this;
  }
  
  public final int getCurrentInterruptionFilter()
  {
    if (!isBound()) {
      return 0;
    }
    try
    {
      int i = getNotificationInterface().getInterruptionFilterFromListener(mWrapper);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.v(TAG, "Unable to contact notification manager", localRemoteException);
    }
    return 0;
  }
  
  public final int getCurrentListenerHints()
  {
    if (!isBound()) {
      return 0;
    }
    try
    {
      int i = getNotificationInterface().getHintsFromListener(mWrapper);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.v(TAG, "Unable to contact notification manager", localRemoteException);
    }
    return 0;
  }
  
  public RankingMap getCurrentRanking()
  {
    synchronized (mLock)
    {
      RankingMap localRankingMap = mRankingMap;
      return localRankingMap;
    }
  }
  
  public final List<NotificationChannelGroup> getNotificationChannelGroups(String paramString, UserHandle paramUserHandle)
  {
    if (!isBound()) {
      return null;
    }
    try
    {
      paramString = getNotificationInterface().getNotificationChannelGroupsFromPrivilegedListener(mWrapper, paramString, paramUserHandle).getList();
      return paramString;
    }
    catch (RemoteException paramString)
    {
      Log.v(TAG, "Unable to contact notification manager", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public final List<NotificationChannel> getNotificationChannels(String paramString, UserHandle paramUserHandle)
  {
    if (!isBound()) {
      return null;
    }
    try
    {
      paramString = getNotificationInterface().getNotificationChannelsFromPrivilegedListener(mWrapper, paramString, paramUserHandle).getList();
      return paramString;
    }
    catch (RemoteException paramString)
    {
      Log.v(TAG, "Unable to contact notification manager", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  protected final INotificationManager getNotificationInterface()
  {
    if (mNoMan == null) {
      mNoMan = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
    }
    return mNoMan;
  }
  
  public final StatusBarNotification[] getSnoozedNotifications()
  {
    try
    {
      StatusBarNotification[] arrayOfStatusBarNotification = cleanUpNotificationList(getNotificationInterface().getSnoozedNotificationsFromListener(mWrapper, 0));
      return arrayOfStatusBarNotification;
    }
    catch (RemoteException localRemoteException)
    {
      Log.v(TAG, "Unable to contact notification manager", localRemoteException);
    }
    return null;
  }
  
  protected boolean isBound()
  {
    if (mWrapper == null)
    {
      Log.w(TAG, "Notification listener service not yet bound.");
      return false;
    }
    return true;
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    if (mWrapper == null) {
      mWrapper = new NotificationListenerWrapper();
    }
    return mWrapper;
  }
  
  public void onDestroy()
  {
    onListenerDisconnected();
    super.onDestroy();
  }
  
  public void onInterruptionFilterChanged(int paramInt) {}
  
  public void onListenerConnected() {}
  
  public void onListenerDisconnected() {}
  
  public void onListenerHintsChanged(int paramInt) {}
  
  public void onNotificationChannelGroupModified(String paramString, UserHandle paramUserHandle, NotificationChannelGroup paramNotificationChannelGroup, int paramInt) {}
  
  public void onNotificationChannelModified(String paramString, UserHandle paramUserHandle, NotificationChannel paramNotificationChannel, int paramInt) {}
  
  public void onNotificationPosted(StatusBarNotification paramStatusBarNotification) {}
  
  public void onNotificationPosted(StatusBarNotification paramStatusBarNotification, RankingMap paramRankingMap)
  {
    onNotificationPosted(paramStatusBarNotification);
  }
  
  public void onNotificationRankingUpdate(RankingMap paramRankingMap) {}
  
  public void onNotificationRemoved(StatusBarNotification paramStatusBarNotification) {}
  
  public void onNotificationRemoved(StatusBarNotification paramStatusBarNotification, RankingMap paramRankingMap)
  {
    onNotificationRemoved(paramStatusBarNotification);
  }
  
  public void onNotificationRemoved(StatusBarNotification paramStatusBarNotification, RankingMap paramRankingMap, int paramInt)
  {
    onNotificationRemoved(paramStatusBarNotification, paramRankingMap);
  }
  
  public void onNotificationRemoved(StatusBarNotification paramStatusBarNotification, RankingMap paramRankingMap, NotificationStats paramNotificationStats, int paramInt)
  {
    onNotificationRemoved(paramStatusBarNotification, paramRankingMap, paramInt);
  }
  
  @SystemApi
  public void registerAsSystemService(Context paramContext, ComponentName paramComponentName, int paramInt)
    throws RemoteException
  {
    if (mWrapper == null) {
      mWrapper = new NotificationListenerWrapper();
    }
    mSystemContext = paramContext;
    INotificationManager localINotificationManager = getNotificationInterface();
    mHandler = new MyHandler(paramContext.getMainLooper());
    mCurrentUser = paramInt;
    localINotificationManager.registerListener(mWrapper, paramComponentName, paramInt);
  }
  
  public final void requestInterruptionFilter(int paramInt)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().requestInterruptionFilterFromListener(mWrapper, paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      Log.v(TAG, "Unable to contact notification manager", localRemoteException);
    }
  }
  
  public final void requestListenerHints(int paramInt)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().requestHintsFromListener(mWrapper, paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      Log.v(TAG, "Unable to contact notification manager", localRemoteException);
    }
  }
  
  public final void requestUnbind()
  {
    if (mWrapper != null)
    {
      INotificationManager localINotificationManager = getNotificationInterface();
      try
      {
        localINotificationManager.requestUnbindListener(mWrapper);
        isConnected = false;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public final void setNotificationsShown(String[] paramArrayOfString)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().setNotificationsShownFromListener(mWrapper, paramArrayOfString);
    }
    catch (RemoteException paramArrayOfString)
    {
      Log.v(TAG, "Unable to contact notification manager", paramArrayOfString);
    }
  }
  
  @SystemApi
  public final void setOnNotificationPostedTrim(int paramInt)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().setOnNotificationPostedTrimFromListener(mWrapper, paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      Log.v(TAG, "Unable to contact notification manager", localRemoteException);
    }
  }
  
  public final void snoozeNotification(String paramString, long paramLong)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().snoozeNotificationUntilFromListener(mWrapper, paramString, paramLong);
    }
    catch (RemoteException paramString)
    {
      Log.v(TAG, "Unable to contact notification manager", paramString);
    }
  }
  
  @SystemApi
  public final void snoozeNotification(String paramString1, String paramString2)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().snoozeNotificationUntilContextFromListener(mWrapper, paramString1, paramString2);
    }
    catch (RemoteException paramString1)
    {
      Log.v(TAG, "Unable to contact notification manager", paramString1);
    }
  }
  
  @SystemApi
  public void unregisterAsSystemService()
    throws RemoteException
  {
    if (mWrapper != null) {
      getNotificationInterface().unregisterListener(mWrapper, mCurrentUser);
    }
  }
  
  public final void updateNotificationChannel(String paramString, UserHandle paramUserHandle, NotificationChannel paramNotificationChannel)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().updateNotificationChannelFromPrivilegedListener(mWrapper, paramString, paramUserHandle, paramNotificationChannel);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.v(TAG, "Unable to contact notification manager", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ChannelOrGroupModificationTypes {}
  
  private final class MyHandler
    extends Handler
  {
    public static final int MSG_ON_INTERRUPTION_FILTER_CHANGED = 6;
    public static final int MSG_ON_LISTENER_CONNECTED = 3;
    public static final int MSG_ON_LISTENER_HINTS_CHANGED = 5;
    public static final int MSG_ON_NOTIFICATION_CHANNEL_GROUP_MODIFIED = 8;
    public static final int MSG_ON_NOTIFICATION_CHANNEL_MODIFIED = 7;
    public static final int MSG_ON_NOTIFICATION_POSTED = 1;
    public static final int MSG_ON_NOTIFICATION_RANKING_UPDATE = 4;
    public static final int MSG_ON_NOTIFICATION_REMOVED = 2;
    
    public MyHandler(Looper paramLooper)
    {
      super(null, false);
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (!isConnected) {
        return;
      }
      Object localObject1;
      Object localObject2;
      Object localObject3;
      int i;
      switch (what)
      {
      default: 
        break;
      case 8: 
        paramMessage = (SomeArgs)obj;
        localObject1 = (String)arg1;
        localObject2 = (UserHandle)arg2;
        localObject3 = (NotificationChannelGroup)arg3;
        i = ((Integer)arg4).intValue();
        onNotificationChannelGroupModified((String)localObject1, (UserHandle)localObject2, (NotificationChannelGroup)localObject3, i);
        break;
      case 7: 
        localObject1 = (SomeArgs)obj;
        localObject2 = (String)arg1;
        localObject3 = (UserHandle)arg2;
        paramMessage = (NotificationChannel)arg3;
        i = ((Integer)arg4).intValue();
        onNotificationChannelModified((String)localObject2, (UserHandle)localObject3, paramMessage, i);
        break;
      case 6: 
        i = arg1;
        onInterruptionFilterChanged(i);
        break;
      case 5: 
        i = arg1;
        onListenerHintsChanged(i);
        break;
      case 4: 
        paramMessage = (NotificationListenerService.RankingMap)obj;
        onNotificationRankingUpdate(paramMessage);
        break;
      case 3: 
        onListenerConnected();
        break;
      case 2: 
        localObject1 = (SomeArgs)obj;
        localObject3 = (StatusBarNotification)arg1;
        localObject2 = (NotificationListenerService.RankingMap)arg2;
        i = ((Integer)arg3).intValue();
        paramMessage = (NotificationStats)arg4;
        ((SomeArgs)localObject1).recycle();
        onNotificationRemoved((StatusBarNotification)localObject3, (NotificationListenerService.RankingMap)localObject2, paramMessage, i);
        break;
      case 1: 
        localObject1 = (SomeArgs)obj;
        localObject3 = (StatusBarNotification)arg1;
        paramMessage = (NotificationListenerService.RankingMap)arg2;
        ((SomeArgs)localObject1).recycle();
        onNotificationPosted((StatusBarNotification)localObject3, paramMessage);
      }
    }
  }
  
  protected class NotificationListenerWrapper
    extends INotificationListener.Stub
  {
    protected NotificationListenerWrapper() {}
    
    public void onInterruptionFilterChanged(int paramInt)
      throws RemoteException
    {
      mHandler.obtainMessage(6, paramInt, 0).sendToTarget();
    }
    
    public void onListenerConnected(NotificationRankingUpdate paramNotificationRankingUpdate)
    {
      synchronized (mLock)
      {
        applyUpdateLocked(paramNotificationRankingUpdate);
        NotificationListenerService.access$702(NotificationListenerService.this, true);
        mHandler.obtainMessage(3).sendToTarget();
        return;
      }
    }
    
    public void onListenerHintsChanged(int paramInt)
      throws RemoteException
    {
      mHandler.obtainMessage(5, paramInt, 0).sendToTarget();
    }
    
    public void onNotificationChannelGroupModification(String paramString, UserHandle paramUserHandle, NotificationChannelGroup paramNotificationChannelGroup, int paramInt)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramString;
      arg2 = paramUserHandle;
      arg3 = paramNotificationChannelGroup;
      arg4 = Integer.valueOf(paramInt);
      mHandler.obtainMessage(8, localSomeArgs).sendToTarget();
    }
    
    public void onNotificationChannelModification(String paramString, UserHandle paramUserHandle, NotificationChannel paramNotificationChannel, int paramInt)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramString;
      arg2 = paramUserHandle;
      arg3 = paramNotificationChannel;
      arg4 = Integer.valueOf(paramInt);
      mHandler.obtainMessage(7, localSomeArgs).sendToTarget();
    }
    
    public void onNotificationEnqueued(IStatusBarNotificationHolder paramIStatusBarNotificationHolder)
      throws RemoteException
    {}
    
    public void onNotificationPosted(IStatusBarNotificationHolder paramIStatusBarNotificationHolder, NotificationRankingUpdate paramNotificationRankingUpdate)
    {
      try
      {
        paramIStatusBarNotificationHolder = paramIStatusBarNotificationHolder.get();
        try
        {
          NotificationListenerService.this.createLegacyIconExtras(paramIStatusBarNotificationHolder.getNotification());
          NotificationListenerService.this.maybePopulateRemoteViews(paramIStatusBarNotificationHolder.getNotification());
          NotificationListenerService.this.maybePopulatePeople(paramIStatusBarNotificationHolder.getNotification());
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          ??? = TAG;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("onNotificationPosted: can't rebuild notification from ");
          localStringBuilder.append(paramIStatusBarNotificationHolder.getPackageName());
          Log.w((String)???, localStringBuilder.toString());
          paramIStatusBarNotificationHolder = null;
        }
        synchronized (mLock)
        {
          applyUpdateLocked(paramNotificationRankingUpdate);
          if (paramIStatusBarNotificationHolder != null)
          {
            paramNotificationRankingUpdate = SomeArgs.obtain();
            arg1 = paramIStatusBarNotificationHolder;
            arg2 = mRankingMap;
            mHandler.obtainMessage(1, paramNotificationRankingUpdate).sendToTarget();
          }
          else
          {
            mHandler.obtainMessage(4, mRankingMap).sendToTarget();
          }
          return;
        }
        return;
      }
      catch (RemoteException paramIStatusBarNotificationHolder)
      {
        Log.w(TAG, "onNotificationPosted: Error receiving StatusBarNotification", paramIStatusBarNotificationHolder);
      }
    }
    
    public void onNotificationRankingUpdate(NotificationRankingUpdate paramNotificationRankingUpdate)
      throws RemoteException
    {
      synchronized (mLock)
      {
        applyUpdateLocked(paramNotificationRankingUpdate);
        mHandler.obtainMessage(4, mRankingMap).sendToTarget();
        return;
      }
    }
    
    public void onNotificationRemoved(IStatusBarNotificationHolder arg1, NotificationRankingUpdate paramNotificationRankingUpdate, NotificationStats paramNotificationStats, int paramInt)
    {
      try
      {
        StatusBarNotification localStatusBarNotification = ???.get();
        synchronized (mLock)
        {
          applyUpdateLocked(paramNotificationRankingUpdate);
          paramNotificationRankingUpdate = SomeArgs.obtain();
          arg1 = localStatusBarNotification;
          arg2 = mRankingMap;
          arg3 = Integer.valueOf(paramInt);
          arg4 = paramNotificationStats;
          mHandler.obtainMessage(2, paramNotificationRankingUpdate).sendToTarget();
          return;
        }
        return;
      }
      catch (RemoteException ???)
      {
        Log.w(TAG, "onNotificationRemoved: Error receiving StatusBarNotification", ???);
      }
    }
    
    public void onNotificationSnoozedUntilContext(IStatusBarNotificationHolder paramIStatusBarNotificationHolder, String paramString)
      throws RemoteException
    {}
  }
  
  public static class Ranking
  {
    public static final int USER_SENTIMENT_NEGATIVE = -1;
    public static final int USER_SENTIMENT_NEUTRAL = 0;
    public static final int USER_SENTIMENT_POSITIVE = 1;
    public static final int VISIBILITY_NO_OVERRIDE = -1000;
    private NotificationChannel mChannel;
    private boolean mHidden;
    private int mImportance;
    private CharSequence mImportanceExplanation;
    private boolean mIsAmbient;
    private String mKey;
    private boolean mMatchesInterruptionFilter;
    private String mOverrideGroupKey;
    private ArrayList<String> mOverridePeople;
    private int mRank = -1;
    private boolean mShowBadge;
    private ArrayList<SnoozeCriterion> mSnoozeCriteria;
    private int mSuppressedVisualEffects;
    private int mUserSentiment = 0;
    private int mVisibilityOverride;
    
    public Ranking() {}
    
    public static String importanceToString(int paramInt)
    {
      if (paramInt != 64536)
      {
        switch (paramInt)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("UNKNOWN(");
          localStringBuilder.append(String.valueOf(paramInt));
          localStringBuilder.append(")");
          return localStringBuilder.toString();
        case 4: 
        case 5: 
          return "HIGH";
        case 3: 
          return "DEFAULT";
        case 2: 
          return "LOW";
        case 1: 
          return "MIN";
        }
        return "NONE";
      }
      return "UNSPECIFIED";
    }
    
    public boolean canShowBadge()
    {
      return mShowBadge;
    }
    
    @SystemApi
    public List<String> getAdditionalPeople()
    {
      return mOverridePeople;
    }
    
    public NotificationChannel getChannel()
    {
      return mChannel;
    }
    
    public int getImportance()
    {
      return mImportance;
    }
    
    public CharSequence getImportanceExplanation()
    {
      return mImportanceExplanation;
    }
    
    public String getKey()
    {
      return mKey;
    }
    
    public String getOverrideGroupKey()
    {
      return mOverrideGroupKey;
    }
    
    public int getRank()
    {
      return mRank;
    }
    
    @SystemApi
    public List<SnoozeCriterion> getSnoozeCriteria()
    {
      return mSnoozeCriteria;
    }
    
    public int getSuppressedVisualEffects()
    {
      return mSuppressedVisualEffects;
    }
    
    public int getUserSentiment()
    {
      return mUserSentiment;
    }
    
    public int getVisibilityOverride()
    {
      return mVisibilityOverride;
    }
    
    public boolean isAmbient()
    {
      return mIsAmbient;
    }
    
    public boolean isSuspended()
    {
      return mHidden;
    }
    
    public boolean matchesInterruptionFilter()
    {
      return mMatchesInterruptionFilter;
    }
    
    @VisibleForTesting
    public void populate(String paramString1, int paramInt1, boolean paramBoolean1, int paramInt2, int paramInt3, int paramInt4, CharSequence paramCharSequence, String paramString2, NotificationChannel paramNotificationChannel, ArrayList<String> paramArrayList, ArrayList<SnoozeCriterion> paramArrayList1, boolean paramBoolean2, int paramInt5, boolean paramBoolean3)
    {
      mKey = paramString1;
      mRank = paramInt1;
      boolean bool;
      if (paramInt4 < 2) {
        bool = true;
      } else {
        bool = false;
      }
      mIsAmbient = bool;
      mMatchesInterruptionFilter = paramBoolean1;
      mVisibilityOverride = paramInt2;
      mSuppressedVisualEffects = paramInt3;
      mImportance = paramInt4;
      mImportanceExplanation = paramCharSequence;
      mOverrideGroupKey = paramString2;
      mChannel = paramNotificationChannel;
      mOverridePeople = paramArrayList;
      mSnoozeCriteria = paramArrayList1;
      mShowBadge = paramBoolean2;
      mUserSentiment = paramInt5;
      mHidden = paramBoolean3;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface UserSentiment {}
  }
  
  public static class RankingMap
    implements Parcelable
  {
    public static final Parcelable.Creator<RankingMap> CREATOR = new Parcelable.Creator()
    {
      public NotificationListenerService.RankingMap createFromParcel(Parcel paramAnonymousParcel)
      {
        return new NotificationListenerService.RankingMap((NotificationRankingUpdate)paramAnonymousParcel.readParcelable(null), null);
      }
      
      public NotificationListenerService.RankingMap[] newArray(int paramAnonymousInt)
      {
        return new NotificationListenerService.RankingMap[paramAnonymousInt];
      }
    };
    private ArrayMap<String, NotificationChannel> mChannels;
    private ArrayMap<String, Boolean> mHidden;
    private ArrayMap<String, Integer> mImportance;
    private ArrayMap<String, String> mImportanceExplanation;
    private ArraySet<Object> mIntercepted;
    private ArrayMap<String, String> mOverrideGroupKeys;
    private ArrayMap<String, ArrayList<String>> mOverridePeople;
    private final NotificationRankingUpdate mRankingUpdate;
    private ArrayMap<String, Integer> mRanks;
    private ArrayMap<String, Boolean> mShowBadge;
    private ArrayMap<String, ArrayList<SnoozeCriterion>> mSnoozeCriteria;
    private ArrayMap<String, Integer> mSuppressedVisualEffects;
    private ArrayMap<String, Integer> mUserSentiment;
    private ArrayMap<String, Integer> mVisibilityOverrides;
    
    private RankingMap(NotificationRankingUpdate paramNotificationRankingUpdate)
    {
      mRankingUpdate = paramNotificationRankingUpdate;
    }
    
    private void buildChannelsLocked()
    {
      Bundle localBundle = mRankingUpdate.getChannels();
      mChannels = new ArrayMap(localBundle.size());
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        mChannels.put(str, (NotificationChannel)localBundle.getParcelable(str));
      }
    }
    
    private void buildHiddenLocked()
    {
      Bundle localBundle = mRankingUpdate.getHidden();
      mHidden = new ArrayMap(localBundle.size());
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        mHidden.put(str, Boolean.valueOf(localBundle.getBoolean(str)));
      }
    }
    
    private void buildImportanceExplanationLocked()
    {
      Bundle localBundle = mRankingUpdate.getImportanceExplanation();
      mImportanceExplanation = new ArrayMap(localBundle.size());
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        mImportanceExplanation.put(str, localBundle.getString(str));
      }
    }
    
    private void buildImportanceLocked()
    {
      String[] arrayOfString = mRankingUpdate.getOrderedKeys();
      int[] arrayOfInt = mRankingUpdate.getImportance();
      mImportance = new ArrayMap(arrayOfString.length);
      for (int i = 0; i < arrayOfString.length; i++)
      {
        String str = arrayOfString[i];
        mImportance.put(str, Integer.valueOf(arrayOfInt[i]));
      }
    }
    
    private void buildInterceptedSetLocked()
    {
      String[] arrayOfString = mRankingUpdate.getInterceptedKeys();
      mIntercepted = new ArraySet(arrayOfString.length);
      Collections.addAll(mIntercepted, arrayOfString);
    }
    
    private void buildOverrideGroupKeys()
    {
      Bundle localBundle = mRankingUpdate.getOverrideGroupKeys();
      mOverrideGroupKeys = new ArrayMap(localBundle.size());
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        mOverrideGroupKeys.put(str, localBundle.getString(str));
      }
    }
    
    private void buildOverridePeopleLocked()
    {
      Bundle localBundle = mRankingUpdate.getOverridePeople();
      mOverridePeople = new ArrayMap(localBundle.size());
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        mOverridePeople.put(str, localBundle.getStringArrayList(str));
      }
    }
    
    private void buildRanksLocked()
    {
      String[] arrayOfString = mRankingUpdate.getOrderedKeys();
      mRanks = new ArrayMap(arrayOfString.length);
      for (int i = 0; i < arrayOfString.length; i++)
      {
        String str = arrayOfString[i];
        mRanks.put(str, Integer.valueOf(i));
      }
    }
    
    private void buildShowBadgeLocked()
    {
      Bundle localBundle = mRankingUpdate.getShowBadge();
      mShowBadge = new ArrayMap(localBundle.size());
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        mShowBadge.put(str, Boolean.valueOf(localBundle.getBoolean(str)));
      }
    }
    
    private void buildSnoozeCriteriaLocked()
    {
      Bundle localBundle = mRankingUpdate.getSnoozeCriteria();
      mSnoozeCriteria = new ArrayMap(localBundle.size());
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        mSnoozeCriteria.put(str, localBundle.getParcelableArrayList(str));
      }
    }
    
    private void buildSuppressedVisualEffectsLocked()
    {
      Bundle localBundle = mRankingUpdate.getSuppressedVisualEffects();
      mSuppressedVisualEffects = new ArrayMap(localBundle.size());
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        mSuppressedVisualEffects.put(str, Integer.valueOf(localBundle.getInt(str)));
      }
    }
    
    private void buildUserSentimentLocked()
    {
      Bundle localBundle = mRankingUpdate.getUserSentiment();
      mUserSentiment = new ArrayMap(localBundle.size());
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        mUserSentiment.put(str, Integer.valueOf(localBundle.getInt(str)));
      }
    }
    
    private void buildVisibilityOverridesLocked()
    {
      Bundle localBundle = mRankingUpdate.getVisibilityOverrides();
      mVisibilityOverrides = new ArrayMap(localBundle.size());
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        mVisibilityOverrides.put(str, Integer.valueOf(localBundle.getInt(str)));
      }
    }
    
    private NotificationChannel getChannel(String paramString)
    {
      try
      {
        if (mChannels == null) {
          buildChannelsLocked();
        }
        return (NotificationChannel)mChannels.get(paramString);
      }
      finally {}
    }
    
    private boolean getHidden(String paramString)
    {
      try
      {
        if (mHidden == null) {
          buildHiddenLocked();
        }
        paramString = (Boolean)mHidden.get(paramString);
        boolean bool;
        if (paramString == null) {
          bool = false;
        } else {
          bool = paramString.booleanValue();
        }
        return bool;
      }
      finally {}
    }
    
    private int getImportance(String paramString)
    {
      try
      {
        if (mImportance == null) {
          buildImportanceLocked();
        }
        paramString = (Integer)mImportance.get(paramString);
        if (paramString == null) {
          return 3;
        }
        return paramString.intValue();
      }
      finally {}
    }
    
    private String getImportanceExplanation(String paramString)
    {
      try
      {
        if (mImportanceExplanation == null) {
          buildImportanceExplanationLocked();
        }
        return (String)mImportanceExplanation.get(paramString);
      }
      finally {}
    }
    
    private String getOverrideGroupKey(String paramString)
    {
      try
      {
        if (mOverrideGroupKeys == null) {
          buildOverrideGroupKeys();
        }
        return (String)mOverrideGroupKeys.get(paramString);
      }
      finally {}
    }
    
    private ArrayList<String> getOverridePeople(String paramString)
    {
      try
      {
        if (mOverridePeople == null) {
          buildOverridePeopleLocked();
        }
        return (ArrayList)mOverridePeople.get(paramString);
      }
      finally {}
    }
    
    private int getRank(String paramString)
    {
      try
      {
        if (mRanks == null) {
          buildRanksLocked();
        }
        paramString = (Integer)mRanks.get(paramString);
        int i;
        if (paramString != null) {
          i = paramString.intValue();
        } else {
          i = -1;
        }
        return i;
      }
      finally {}
    }
    
    private boolean getShowBadge(String paramString)
    {
      try
      {
        if (mShowBadge == null) {
          buildShowBadgeLocked();
        }
        paramString = (Boolean)mShowBadge.get(paramString);
        boolean bool;
        if (paramString == null) {
          bool = false;
        } else {
          bool = paramString.booleanValue();
        }
        return bool;
      }
      finally {}
    }
    
    private ArrayList<SnoozeCriterion> getSnoozeCriteria(String paramString)
    {
      try
      {
        if (mSnoozeCriteria == null) {
          buildSnoozeCriteriaLocked();
        }
        return (ArrayList)mSnoozeCriteria.get(paramString);
      }
      finally {}
    }
    
    private int getSuppressedVisualEffects(String paramString)
    {
      try
      {
        if (mSuppressedVisualEffects == null) {
          buildSuppressedVisualEffectsLocked();
        }
        paramString = (Integer)mSuppressedVisualEffects.get(paramString);
        if (paramString == null) {
          return 0;
        }
        return paramString.intValue();
      }
      finally {}
    }
    
    private int getUserSentiment(String paramString)
    {
      try
      {
        if (mUserSentiment == null) {
          buildUserSentimentLocked();
        }
        paramString = (Integer)mUserSentiment.get(paramString);
        int i;
        if (paramString == null) {
          i = 0;
        } else {
          i = paramString.intValue();
        }
        return i;
      }
      finally {}
    }
    
    private int getVisibilityOverride(String paramString)
    {
      try
      {
        if (mVisibilityOverrides == null) {
          buildVisibilityOverridesLocked();
        }
        paramString = (Integer)mVisibilityOverrides.get(paramString);
        if (paramString == null) {
          return 64536;
        }
        return paramString.intValue();
      }
      finally {}
    }
    
    private boolean isIntercepted(String paramString)
    {
      try
      {
        if (mIntercepted == null) {
          buildInterceptedSetLocked();
        }
        return mIntercepted.contains(paramString);
      }
      finally {}
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String[] getOrderedKeys()
    {
      return mRankingUpdate.getOrderedKeys();
    }
    
    public boolean getRanking(String paramString, NotificationListenerService.Ranking paramRanking)
    {
      int i = getRank(paramString);
      boolean bool1 = isIntercepted(paramString);
      boolean bool2 = true;
      paramRanking.populate(paramString, i, bool1 ^ true, getVisibilityOverride(paramString), getSuppressedVisualEffects(paramString), getImportance(paramString), getImportanceExplanation(paramString), getOverrideGroupKey(paramString), getChannel(paramString), getOverridePeople(paramString), getSnoozeCriteria(paramString), getShowBadge(paramString), getUserSentiment(paramString), getHidden(paramString));
      if (i < 0) {
        bool2 = false;
      }
      return bool2;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeParcelable(mRankingUpdate, paramInt);
    }
  }
}
