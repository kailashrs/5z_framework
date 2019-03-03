package android.app;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public abstract class IntentService
  extends Service
{
  private String mName;
  private boolean mRedelivery;
  private volatile ServiceHandler mServiceHandler;
  private volatile Looper mServiceLooper;
  
  public IntentService(String paramString)
  {
    mName = paramString;
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }
  
  public void onCreate()
  {
    super.onCreate();
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("IntentService[");
    ((StringBuilder)localObject).append(mName);
    ((StringBuilder)localObject).append("]");
    localObject = new HandlerThread(((StringBuilder)localObject).toString());
    ((HandlerThread)localObject).start();
    mServiceLooper = ((HandlerThread)localObject).getLooper();
    mServiceHandler = new ServiceHandler(mServiceLooper);
  }
  
  public void onDestroy()
  {
    mServiceLooper.quit();
  }
  
  protected abstract void onHandleIntent(Intent paramIntent);
  
  public void onStart(Intent paramIntent, int paramInt)
  {
    Message localMessage = mServiceHandler.obtainMessage();
    arg1 = paramInt;
    obj = paramIntent;
    mServiceHandler.sendMessage(localMessage);
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    onStart(paramIntent, paramInt2);
    if (mRedelivery) {
      paramInt1 = 3;
    } else {
      paramInt1 = 2;
    }
    return paramInt1;
  }
  
  public void setIntentRedelivery(boolean paramBoolean)
  {
    mRedelivery = paramBoolean;
  }
  
  private final class ServiceHandler
    extends Handler
  {
    public ServiceHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      onHandleIntent((Intent)obj);
      stopSelf(arg1);
    }
  }
}
