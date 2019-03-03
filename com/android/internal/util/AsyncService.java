package com.android.internal.util;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Slog;

public abstract class AsyncService
  extends Service
{
  public static final int CMD_ASYNC_SERVICE_DESTROY = 16777216;
  public static final int CMD_ASYNC_SERVICE_ON_START_INTENT = 16777215;
  protected static final boolean DBG = true;
  private static final String TAG = "AsyncService";
  AsyncServiceInfo mAsyncServiceInfo;
  Handler mHandler;
  protected Messenger mMessenger;
  
  public AsyncService() {}
  
  public abstract AsyncServiceInfo createHandler();
  
  public Handler getHandler()
  {
    return mHandler;
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return mMessenger.getBinder();
  }
  
  public void onCreate()
  {
    super.onCreate();
    mAsyncServiceInfo = createHandler();
    mHandler = mAsyncServiceInfo.mHandler;
    mMessenger = new Messenger(mHandler);
  }
  
  public void onDestroy()
  {
    Slog.d("AsyncService", "onDestroy");
    Message localMessage = mHandler.obtainMessage();
    what = 16777216;
    mHandler.sendMessage(localMessage);
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    Slog.d("AsyncService", "onStartCommand");
    Message localMessage = mHandler.obtainMessage();
    what = 16777215;
    arg1 = paramInt1;
    arg2 = paramInt2;
    obj = paramIntent;
    mHandler.sendMessage(localMessage);
    return mAsyncServiceInfo.mRestartFlags;
  }
  
  public static final class AsyncServiceInfo
  {
    public Handler mHandler;
    public int mRestartFlags;
    
    public AsyncServiceInfo() {}
  }
}
