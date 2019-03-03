package android.service.notification;

import android.annotation.SystemApi;
import android.app.INotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.os.SomeArgs;
import java.util.List;

@SystemApi
public abstract class NotificationAssistantService
  extends NotificationListenerService
{
  public static final String SERVICE_INTERFACE = "android.service.notification.NotificationAssistantService";
  private static final String TAG = "NotificationAssistants";
  protected Handler mHandler;
  
  public NotificationAssistantService() {}
  
  public final void adjustNotification(Adjustment paramAdjustment)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().applyAdjustmentFromAssistant(mWrapper, paramAdjustment);
      return;
    }
    catch (RemoteException paramAdjustment)
    {
      Log.v("NotificationAssistants", "Unable to contact notification manager", paramAdjustment);
      throw paramAdjustment.rethrowFromSystemServer();
    }
  }
  
  public final void adjustNotifications(List<Adjustment> paramList)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().applyAdjustmentsFromAssistant(mWrapper, paramList);
      return;
    }
    catch (RemoteException paramList)
    {
      Log.v("NotificationAssistants", "Unable to contact notification manager", paramList);
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  protected void attachBaseContext(Context paramContext)
  {
    super.attachBaseContext(paramContext);
    mHandler = new MyHandler(getContext().getMainLooper());
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    if (mWrapper == null) {
      mWrapper = new NotificationAssistantServiceWrapper(null);
    }
    return mWrapper;
  }
  
  public abstract Adjustment onNotificationEnqueued(StatusBarNotification paramStatusBarNotification);
  
  public void onNotificationRemoved(StatusBarNotification paramStatusBarNotification, NotificationListenerService.RankingMap paramRankingMap, NotificationStats paramNotificationStats, int paramInt)
  {
    onNotificationRemoved(paramStatusBarNotification, paramRankingMap, paramInt);
  }
  
  public abstract void onNotificationSnoozedUntilContext(StatusBarNotification paramStatusBarNotification, String paramString);
  
  public final void unsnoozeNotification(String paramString)
  {
    if (!isBound()) {
      return;
    }
    try
    {
      getNotificationInterface().unsnoozeNotificationFromAssistant(mWrapper, paramString);
    }
    catch (RemoteException paramString)
    {
      Log.v("NotificationAssistants", "Unable to contact notification manager", paramString);
    }
  }
  
  private final class MyHandler
    extends Handler
  {
    public static final int MSG_ON_NOTIFICATION_ENQUEUED = 1;
    public static final int MSG_ON_NOTIFICATION_SNOOZED = 2;
    
    public MyHandler(Looper paramLooper)
    {
      super(null, false);
    }
    
    public void handleMessage(Message paramMessage)
    {
      StatusBarNotification localStatusBarNotification;
      switch (what)
      {
      default: 
        break;
      case 2: 
        SomeArgs localSomeArgs = (SomeArgs)obj;
        localStatusBarNotification = (StatusBarNotification)arg1;
        paramMessage = (String)arg2;
        localSomeArgs.recycle();
        onNotificationSnoozedUntilContext(localStatusBarNotification, paramMessage);
        break;
      case 1: 
        paramMessage = (SomeArgs)obj;
        localStatusBarNotification = (StatusBarNotification)arg1;
        paramMessage.recycle();
        paramMessage = onNotificationEnqueued(localStatusBarNotification);
        if (paramMessage != null)
        {
          if (!isBound()) {
            return;
          }
          try
          {
            getNotificationInterface().applyEnqueuedAdjustmentFromAssistant(mWrapper, paramMessage);
          }
          catch (RemoteException paramMessage)
          {
            Log.v("NotificationAssistants", "Unable to contact notification manager", paramMessage);
            throw paramMessage.rethrowFromSystemServer();
          }
        }
        break;
      }
    }
  }
  
  private class NotificationAssistantServiceWrapper
    extends NotificationListenerService.NotificationListenerWrapper
  {
    private NotificationAssistantServiceWrapper()
    {
      super();
    }
    
    public void onNotificationEnqueued(IStatusBarNotificationHolder paramIStatusBarNotificationHolder)
    {
      try
      {
        paramIStatusBarNotificationHolder = paramIStatusBarNotificationHolder.get();
        SomeArgs localSomeArgs = SomeArgs.obtain();
        arg1 = paramIStatusBarNotificationHolder;
        mHandler.obtainMessage(1, localSomeArgs).sendToTarget();
        return;
      }
      catch (RemoteException paramIStatusBarNotificationHolder)
      {
        Log.w("NotificationAssistants", "onNotificationEnqueued: Error receiving StatusBarNotification", paramIStatusBarNotificationHolder);
      }
    }
    
    public void onNotificationSnoozedUntilContext(IStatusBarNotificationHolder paramIStatusBarNotificationHolder, String paramString)
      throws RemoteException
    {
      try
      {
        StatusBarNotification localStatusBarNotification = paramIStatusBarNotificationHolder.get();
        paramIStatusBarNotificationHolder = SomeArgs.obtain();
        arg1 = localStatusBarNotification;
        arg2 = paramString;
        mHandler.obtainMessage(2, paramIStatusBarNotificationHolder).sendToTarget();
        return;
      }
      catch (RemoteException paramIStatusBarNotificationHolder)
      {
        Log.w("NotificationAssistants", "onNotificationSnoozed: Error receiving StatusBarNotification", paramIStatusBarNotificationHolder);
      }
    }
  }
}
