package android.service.notification;

import android.annotation.SystemApi;
import android.app.INotificationManager;
import android.app.INotificationManager.Stub;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

public abstract class ConditionProviderService
  extends Service
{
  public static final String EXTRA_RULE_ID = "android.service.notification.extra.RULE_ID";
  public static final String META_DATA_CONFIGURATION_ACTIVITY = "android.service.zen.automatic.configurationActivity";
  public static final String META_DATA_RULE_INSTANCE_LIMIT = "android.service.zen.automatic.ruleInstanceLimit";
  public static final String META_DATA_RULE_TYPE = "android.service.zen.automatic.ruleType";
  public static final String SERVICE_INTERFACE = "android.service.notification.ConditionProviderService";
  private final String TAG;
  private final H mHandler;
  private INotificationManager mNoMan;
  private Provider mProvider;
  
  public ConditionProviderService()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(ConditionProviderService.class.getSimpleName());
    localStringBuilder.append("[");
    localStringBuilder.append(getClass().getSimpleName());
    localStringBuilder.append("]");
    TAG = localStringBuilder.toString();
    mHandler = new H(null);
  }
  
  private final INotificationManager getNotificationInterface()
  {
    if (mNoMan == null) {
      mNoMan = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
    }
    return mNoMan;
  }
  
  public static final void requestRebind(ComponentName paramComponentName)
  {
    INotificationManager localINotificationManager = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
    try
    {
      localINotificationManager.requestBindProvider(paramComponentName);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean isBound()
  {
    if (mProvider == null)
    {
      Log.w(TAG, "Condition provider service not yet bound.");
      return false;
    }
    return true;
  }
  
  public final void notifyCondition(Condition paramCondition)
  {
    if (paramCondition == null) {
      return;
    }
    notifyConditions(new Condition[] { paramCondition });
  }
  
  public final void notifyConditions(Condition... paramVarArgs)
  {
    if ((isBound()) && (paramVarArgs != null))
    {
      try
      {
        getNotificationInterface().notifyConditions(getPackageName(), mProvider, paramVarArgs);
      }
      catch (RemoteException paramVarArgs)
      {
        Log.v(TAG, "Unable to contact notification manager", paramVarArgs);
      }
      return;
    }
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    if (mProvider == null) {
      mProvider = new Provider(null);
    }
    return mProvider;
  }
  
  public abstract void onConnected();
  
  @SystemApi
  public void onRequestConditions(int paramInt) {}
  
  public abstract void onSubscribe(Uri paramUri);
  
  public abstract void onUnsubscribe(Uri paramUri);
  
  public final void requestUnbind()
  {
    INotificationManager localINotificationManager = getNotificationInterface();
    try
    {
      localINotificationManager.requestUnbindProvider(mProvider);
      mProvider = null;
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  private final class H
    extends Handler
  {
    private static final int ON_CONNECTED = 1;
    private static final int ON_SUBSCRIBE = 3;
    private static final int ON_UNSUBSCRIBE = 4;
    
    private H() {}
    
    public void handleMessage(Message paramMessage)
    {
      String str1 = null;
      if (!isBound()) {
        return;
      }
      try
      {
        int i = what;
        if (i != 1)
        {
          switch (i)
          {
          default: 
            break;
          case 4: 
            str1 = "onUnsubscribe";
            onUnsubscribe((Uri)obj);
            break;
          case 3: 
            str1 = "onSubscribe";
            onSubscribe((Uri)obj);
            break;
          }
        }
        else
        {
          str1 = "onConnected";
          onConnected();
        }
      }
      catch (Throwable paramMessage)
      {
        String str2 = TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Error running ");
        localStringBuilder.append(str1);
        Log.w(str2, localStringBuilder.toString(), paramMessage);
      }
    }
  }
  
  private final class Provider
    extends IConditionProvider.Stub
  {
    private Provider() {}
    
    public void onConnected()
    {
      mHandler.obtainMessage(1).sendToTarget();
    }
    
    public void onSubscribe(Uri paramUri)
    {
      mHandler.obtainMessage(3, paramUri).sendToTarget();
    }
    
    public void onUnsubscribe(Uri paramUri)
    {
      mHandler.obtainMessage(4, paramUri).sendToTarget();
    }
  }
}
