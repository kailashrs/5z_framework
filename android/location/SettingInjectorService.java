package android.location;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public abstract class SettingInjectorService
  extends Service
{
  public static final String ACTION_INJECTED_SETTING_CHANGED = "android.location.InjectedSettingChanged";
  public static final String ACTION_SERVICE_INTENT = "android.location.SettingInjectorService";
  public static final String ATTRIBUTES_NAME = "injected-location-setting";
  public static final String ENABLED_KEY = "enabled";
  public static final String MESSENGER_KEY = "messenger";
  public static final String META_DATA_NAME = "android.location.SettingInjectorService";
  private static final String TAG = "SettingInjectorService";
  private final String mName;
  
  public SettingInjectorService(String paramString)
  {
    mName = paramString;
  }
  
  private void onHandleIntent(Intent paramIntent)
  {
    try
    {
      boolean bool = onGetEnabled();
      sendStatus(paramIntent, bool);
      return;
    }
    catch (RuntimeException localRuntimeException)
    {
      sendStatus(paramIntent, true);
      throw localRuntimeException;
    }
  }
  
  private void sendStatus(Intent paramIntent, boolean paramBoolean)
  {
    Message localMessage = Message.obtain();
    Object localObject = new Bundle();
    ((Bundle)localObject).putBoolean("enabled", paramBoolean);
    localMessage.setData((Bundle)localObject);
    if (Log.isLoggable("SettingInjectorService", 3))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(mName);
      ((StringBuilder)localObject).append(": received ");
      ((StringBuilder)localObject).append(paramIntent);
      ((StringBuilder)localObject).append(", enabled=");
      ((StringBuilder)localObject).append(paramBoolean);
      ((StringBuilder)localObject).append(", sending message: ");
      ((StringBuilder)localObject).append(localMessage);
      Log.d("SettingInjectorService", ((StringBuilder)localObject).toString());
    }
    paramIntent = (Messenger)paramIntent.getParcelableExtra("messenger");
    try
    {
      paramIntent.send(localMessage);
    }
    catch (RemoteException localRemoteException)
    {
      paramIntent = new StringBuilder();
      paramIntent.append(mName);
      paramIntent.append(": sending dynamic status failed");
      Log.e("SettingInjectorService", paramIntent.toString(), localRemoteException);
    }
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    return null;
  }
  
  protected abstract boolean onGetEnabled();
  
  @Deprecated
  protected abstract String onGetSummary();
  
  public final void onStart(Intent paramIntent, int paramInt)
  {
    super.onStart(paramIntent, paramInt);
  }
  
  public final int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    onHandleIntent(paramIntent);
    stopSelf(paramInt2);
    return 2;
  }
}
