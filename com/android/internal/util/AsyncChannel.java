package com.android.internal.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Slog;
import java.util.Objects;
import java.util.Stack;

public class AsyncChannel
{
  private static final int BASE = 69632;
  public static final int CMD_CHANNEL_DISCONNECT = 69635;
  public static final int CMD_CHANNEL_DISCONNECTED = 69636;
  public static final int CMD_CHANNEL_FULLY_CONNECTED = 69634;
  public static final int CMD_CHANNEL_FULL_CONNECTION = 69633;
  public static final int CMD_CHANNEL_HALF_CONNECTED = 69632;
  private static final int CMD_TO_STRING_COUNT = 5;
  private static final boolean DBG = false;
  public static final int STATUS_BINDING_UNSUCCESSFUL = 1;
  public static final int STATUS_FULL_CONNECTION_REFUSED_ALREADY_CONNECTED = 3;
  public static final int STATUS_REMOTE_DISCONNECTION = 4;
  public static final int STATUS_SEND_UNSUCCESSFUL = 2;
  public static final int STATUS_SUCCESSFUL = 0;
  private static final String TAG = "AsyncChannel";
  private static String[] sCmdToString = new String[5];
  private AsyncChannelConnection mConnection;
  private DeathMonitor mDeathMonitor;
  private Messenger mDstMessenger;
  private Context mSrcContext;
  private Handler mSrcHandler;
  private Messenger mSrcMessenger;
  
  static
  {
    sCmdToString[0] = "CMD_CHANNEL_HALF_CONNECTED";
    sCmdToString[1] = "CMD_CHANNEL_FULL_CONNECTION";
    sCmdToString[2] = "CMD_CHANNEL_FULLY_CONNECTED";
    sCmdToString[3] = "CMD_CHANNEL_DISCONNECT";
    sCmdToString[4] = "CMD_CHANNEL_DISCONNECTED";
  }
  
  public AsyncChannel() {}
  
  protected static String cmdToString(int paramInt)
  {
    paramInt -= 69632;
    if ((paramInt >= 0) && (paramInt < sCmdToString.length)) {
      return sCmdToString[paramInt];
    }
    return null;
  }
  
  private boolean linkToDeathMonitor()
  {
    if ((mConnection == null) && (mDeathMonitor == null))
    {
      mDeathMonitor = new DeathMonitor();
      try
      {
        mDstMessenger.getBinder().linkToDeath(mDeathMonitor, 0);
      }
      catch (RemoteException localRemoteException)
      {
        mDeathMonitor = null;
        return false;
      }
    }
    return true;
  }
  
  private static void log(String paramString)
  {
    Slog.d("AsyncChannel", paramString);
  }
  
  private void replyDisconnected(int paramInt)
  {
    if (mSrcHandler == null) {
      return;
    }
    Message localMessage = mSrcHandler.obtainMessage(69636);
    arg1 = paramInt;
    obj = this;
    replyTo = mDstMessenger;
    mSrcHandler.sendMessage(localMessage);
  }
  
  private void replyHalfConnected(int paramInt)
  {
    Message localMessage = mSrcHandler.obtainMessage(69632);
    arg1 = paramInt;
    obj = this;
    replyTo = mDstMessenger;
    if (!linkToDeathMonitor()) {
      arg1 = 1;
    }
    mSrcHandler.sendMessage(localMessage);
  }
  
  public void connect(Context paramContext, Handler paramHandler1, Handler paramHandler2)
  {
    connect(paramContext, paramHandler1, new Messenger(paramHandler2));
  }
  
  public void connect(Context paramContext, Handler paramHandler, Messenger paramMessenger)
  {
    connected(paramContext, paramHandler, paramMessenger);
    replyHalfConnected(0);
  }
  
  public void connect(Context paramContext, Handler paramHandler, Class<?> paramClass)
  {
    connect(paramContext, paramHandler, paramClass.getPackage().getName(), paramClass.getName());
  }
  
  public void connect(Context paramContext, Handler paramHandler, String paramString1, String paramString2)
  {
    new Thread(new Runnable()
    {
      String mDstClassName;
      String mDstPackageName;
      Context mSrcCtx;
      Handler mSrcHdlr;
      
      public void run()
      {
        int i = connectSrcHandlerToPackageSync(mSrcCtx, mSrcHdlr, mDstPackageName, mDstClassName);
        AsyncChannel.this.replyHalfConnected(i);
      }
    }).start();
  }
  
  public void connect(AsyncService paramAsyncService, Messenger paramMessenger)
  {
    connect(paramAsyncService, paramAsyncService.getHandler(), paramMessenger);
  }
  
  public int connectSrcHandlerToPackageSync(Context paramContext, Handler paramHandler, String paramString1, String paramString2)
  {
    mConnection = new AsyncChannelConnection();
    mSrcContext = paramContext;
    mSrcHandler = paramHandler;
    mSrcMessenger = new Messenger(paramHandler);
    mDstMessenger = null;
    paramHandler = new Intent("android.intent.action.MAIN");
    paramHandler.setClassName(paramString1, paramString2);
    return paramContext.bindService(paramHandler, mConnection, 1) ^ true;
  }
  
  public int connectSync(Context paramContext, Handler paramHandler1, Handler paramHandler2)
  {
    return connectSync(paramContext, paramHandler1, new Messenger(paramHandler2));
  }
  
  public int connectSync(Context paramContext, Handler paramHandler, Messenger paramMessenger)
  {
    connected(paramContext, paramHandler, paramMessenger);
    return 0;
  }
  
  public void connected(Context paramContext, Handler paramHandler, Messenger paramMessenger)
  {
    mSrcContext = paramContext;
    mSrcHandler = paramHandler;
    mSrcMessenger = new Messenger(mSrcHandler);
    mDstMessenger = paramMessenger;
  }
  
  public void disconnect()
  {
    if ((mConnection != null) && (mSrcContext != null))
    {
      mSrcContext.unbindService(mConnection);
      mConnection = null;
    }
    try
    {
      Message localMessage = Message.obtain();
      what = 69636;
      replyTo = mSrcMessenger;
      mDstMessenger.send(localMessage);
    }
    catch (Exception localException) {}
    replyDisconnected(0);
    mSrcHandler = null;
    if ((mConnection == null) && (mDstMessenger != null) && (mDeathMonitor != null))
    {
      mDstMessenger.getBinder().unlinkToDeath(mDeathMonitor, 0);
      mDeathMonitor = null;
    }
  }
  
  public void disconnected()
  {
    mSrcContext = null;
    mSrcHandler = null;
    mSrcMessenger = null;
    mDstMessenger = null;
    mDeathMonitor = null;
    mConnection = null;
  }
  
  public int fullyConnectSync(Context paramContext, Handler paramHandler1, Handler paramHandler2)
  {
    int i = connectSync(paramContext, paramHandler1, paramHandler2);
    int j = i;
    if (i == 0) {
      j = sendMessageSynchronously69633arg1;
    }
    return j;
  }
  
  public void replyToMessage(Message paramMessage, int paramInt)
  {
    Message localMessage = Message.obtain();
    what = paramInt;
    replyToMessage(paramMessage, localMessage);
  }
  
  public void replyToMessage(Message paramMessage, int paramInt1, int paramInt2)
  {
    Message localMessage = Message.obtain();
    what = paramInt1;
    arg1 = paramInt2;
    replyToMessage(paramMessage, localMessage);
  }
  
  public void replyToMessage(Message paramMessage, int paramInt1, int paramInt2, int paramInt3)
  {
    Message localMessage = Message.obtain();
    what = paramInt1;
    arg1 = paramInt2;
    arg2 = paramInt3;
    replyToMessage(paramMessage, localMessage);
  }
  
  public void replyToMessage(Message paramMessage, int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    Message localMessage = Message.obtain();
    what = paramInt1;
    arg1 = paramInt2;
    arg2 = paramInt3;
    obj = paramObject;
    replyToMessage(paramMessage, localMessage);
  }
  
  public void replyToMessage(Message paramMessage, int paramInt, Object paramObject)
  {
    Message localMessage = Message.obtain();
    what = paramInt;
    obj = paramObject;
    replyToMessage(paramMessage, localMessage);
  }
  
  public void replyToMessage(Message paramMessage1, Message paramMessage2)
  {
    try
    {
      replyTo = mSrcMessenger;
      replyTo.send(paramMessage2);
    }
    catch (RemoteException paramMessage2)
    {
      paramMessage1 = new StringBuilder();
      paramMessage1.append("TODO: handle replyToMessage RemoteException");
      paramMessage1.append(paramMessage2);
      log(paramMessage1.toString());
      paramMessage2.printStackTrace();
    }
  }
  
  public void sendMessage(int paramInt)
  {
    Message localMessage = Message.obtain();
    what = paramInt;
    sendMessage(localMessage);
  }
  
  public void sendMessage(int paramInt1, int paramInt2)
  {
    Message localMessage = Message.obtain();
    what = paramInt1;
    arg1 = paramInt2;
    sendMessage(localMessage);
  }
  
  public void sendMessage(int paramInt1, int paramInt2, int paramInt3)
  {
    Message localMessage = Message.obtain();
    what = paramInt1;
    arg1 = paramInt2;
    arg2 = paramInt3;
    sendMessage(localMessage);
  }
  
  public void sendMessage(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    Message localMessage = Message.obtain();
    what = paramInt1;
    arg1 = paramInt2;
    arg2 = paramInt3;
    obj = paramObject;
    sendMessage(localMessage);
  }
  
  public void sendMessage(int paramInt, Object paramObject)
  {
    Message localMessage = Message.obtain();
    what = paramInt;
    obj = paramObject;
    sendMessage(localMessage);
  }
  
  public void sendMessage(Message paramMessage)
  {
    replyTo = mSrcMessenger;
    try
    {
      mDstMessenger.send(paramMessage);
    }
    catch (RemoteException paramMessage)
    {
      replyDisconnected(2);
    }
  }
  
  public Message sendMessageSynchronously(int paramInt)
  {
    Message localMessage = Message.obtain();
    what = paramInt;
    return sendMessageSynchronously(localMessage);
  }
  
  public Message sendMessageSynchronously(int paramInt1, int paramInt2)
  {
    Message localMessage = Message.obtain();
    what = paramInt1;
    arg1 = paramInt2;
    return sendMessageSynchronously(localMessage);
  }
  
  public Message sendMessageSynchronously(int paramInt1, int paramInt2, int paramInt3)
  {
    Message localMessage = Message.obtain();
    what = paramInt1;
    arg1 = paramInt2;
    arg2 = paramInt3;
    return sendMessageSynchronously(localMessage);
  }
  
  public Message sendMessageSynchronously(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    Message localMessage = Message.obtain();
    what = paramInt1;
    arg1 = paramInt2;
    arg2 = paramInt3;
    obj = paramObject;
    return sendMessageSynchronously(localMessage);
  }
  
  public Message sendMessageSynchronously(int paramInt, Object paramObject)
  {
    Message localMessage = Message.obtain();
    what = paramInt;
    obj = paramObject;
    return sendMessageSynchronously(localMessage);
  }
  
  public Message sendMessageSynchronously(Message paramMessage)
  {
    return SyncMessenger.sendMessageSynchronously(mDstMessenger, paramMessage);
  }
  
  class AsyncChannelConnection
    implements ServiceConnection
  {
    AsyncChannelConnection() {}
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      AsyncChannel.access$502(AsyncChannel.this, new Messenger(paramIBinder));
      AsyncChannel.this.replyHalfConnected(0);
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      AsyncChannel.this.replyDisconnected(0);
    }
  }
  
  private final class DeathMonitor
    implements IBinder.DeathRecipient
  {
    DeathMonitor() {}
    
    public void binderDied()
    {
      AsyncChannel.this.replyDisconnected(4);
    }
  }
  
  private static class SyncMessenger
  {
    private static int sCount = 0;
    private static Stack<SyncMessenger> sStack = new Stack();
    private SyncHandler mHandler;
    private HandlerThread mHandlerThread;
    private Messenger mMessenger;
    
    private SyncMessenger() {}
    
    private static SyncMessenger obtain()
    {
      synchronized (sStack)
      {
        SyncMessenger localSyncMessenger;
        if (sStack.isEmpty())
        {
          localSyncMessenger = new com/android/internal/util/AsyncChannel$SyncMessenger;
          localSyncMessenger.<init>();
          Object localObject2 = new android/os/HandlerThread;
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("SyncHandler-");
          int i = sCount;
          sCount = i + 1;
          localStringBuilder.append(i);
          ((HandlerThread)localObject2).<init>(localStringBuilder.toString());
          mHandlerThread = ((HandlerThread)localObject2);
          mHandlerThread.start();
          localObject2 = new com/android/internal/util/AsyncChannel$SyncMessenger$SyncHandler;
          Objects.requireNonNull(localSyncMessenger);
          ((SyncHandler)localObject2).<init>(localSyncMessenger, mHandlerThread.getLooper(), null);
          mHandler = ((SyncHandler)localObject2);
          localObject2 = new android/os/Messenger;
          ((Messenger)localObject2).<init>(mHandler);
          mMessenger = ((Messenger)localObject2);
        }
        else
        {
          localSyncMessenger = (SyncMessenger)sStack.pop();
        }
        return localSyncMessenger;
      }
    }
    
    private void recycle()
    {
      synchronized (sStack)
      {
        sStack.push(this);
        return;
      }
    }
    
    private static Message sendMessageSynchronously(Messenger paramMessenger, Message paramMessage)
    {
      SyncMessenger localSyncMessenger = obtain();
      Object localObject1 = null;
      Object localObject2 = null;
      Object localObject3 = null;
      Object localObject4 = null;
      Object localObject5 = localObject3;
      if (paramMessenger != null)
      {
        localObject5 = localObject3;
        if (paramMessage != null)
        {
          localObject3 = localObject1;
          localObject5 = localObject2;
          try
          {
            replyTo = mMessenger;
            localObject3 = localObject1;
            localObject5 = localObject2;
            Object localObject6 = mHandler.mLockObject;
            localObject3 = localObject1;
            localObject5 = localObject2;
            localObject5 = localObject4;
            try
            {
              if (mHandler.mResultMsg != null)
              {
                localObject5 = localObject4;
                Slog.wtf("AsyncChannel", "mResultMsg should be null here");
                localObject5 = localObject4;
                SyncHandler.access$402(mHandler, null);
              }
              localObject5 = localObject4;
              paramMessenger.send(paramMessage);
              localObject5 = localObject4;
              mHandler.mLockObject.wait();
              localObject5 = localObject4;
              paramMessenger = mHandler.mResultMsg;
              localObject5 = paramMessenger;
              SyncHandler.access$402(mHandler, null);
              localObject5 = paramMessenger;
            }
            finally
            {
              localObject3 = localObject5;
            }
            localSyncMessenger.recycle();
          }
          catch (RemoteException paramMessenger)
          {
            Slog.e("AsyncChannel", "error in sendMessageSynchronously", paramMessenger);
            localObject5 = localObject3;
          }
          catch (InterruptedException paramMessenger)
          {
            Slog.e("AsyncChannel", "error in sendMessageSynchronously", paramMessenger);
          }
        }
      }
      return localObject5;
    }
    
    private class SyncHandler
      extends Handler
    {
      private Object mLockObject = new Object();
      private Message mResultMsg;
      
      private SyncHandler(Looper paramLooper)
      {
        super();
      }
      
      public void handleMessage(Message arg1)
      {
        Message localMessage = Message.obtain();
        localMessage.copyFrom(???);
        synchronized (mLockObject)
        {
          mResultMsg = localMessage;
          mLockObject.notify();
          return;
        }
      }
    }
  }
}
