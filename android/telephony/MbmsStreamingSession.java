package android.telephony;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.telephony.mbms.InternalStreamingServiceCallback;
import android.telephony.mbms.InternalStreamingSessionCallback;
import android.telephony.mbms.MbmsStreamingSessionCallback;
import android.telephony.mbms.MbmsUtils;
import android.telephony.mbms.StreamingService;
import android.telephony.mbms.StreamingServiceCallback;
import android.telephony.mbms.StreamingServiceInfo;
import android.telephony.mbms.vendor.IMbmsStreamingService;
import android.telephony.mbms.vendor.IMbmsStreamingService.Stub;
import android.util.ArraySet;
import android.util.Log;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MbmsStreamingSession
  implements AutoCloseable
{
  private static final String LOG_TAG = "MbmsStreamingSession";
  @SystemApi
  public static final String MBMS_STREAMING_SERVICE_ACTION = "android.telephony.action.EmbmsStreaming";
  public static final String MBMS_STREAMING_SERVICE_OVERRIDE_METADATA = "mbms-streaming-service-override";
  private static AtomicBoolean sIsInitialized = new AtomicBoolean(false);
  private final Context mContext;
  private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient()
  {
    public void binderDied()
    {
      MbmsStreamingSession.sIsInitialized.set(false);
      MbmsStreamingSession.this.sendErrorToApp(3, "Received death notification");
    }
  };
  private InternalStreamingSessionCallback mInternalCallback;
  private Set<StreamingService> mKnownActiveStreamingServices = new ArraySet();
  private AtomicReference<IMbmsStreamingService> mService = new AtomicReference(null);
  private int mSubscriptionId = -1;
  
  private MbmsStreamingSession(Context paramContext, Executor paramExecutor, int paramInt, MbmsStreamingSessionCallback paramMbmsStreamingSessionCallback)
  {
    mContext = paramContext;
    mSubscriptionId = paramInt;
    mInternalCallback = new InternalStreamingSessionCallback(paramMbmsStreamingSessionCallback, paramExecutor);
  }
  
  private int bindAndInitialize()
  {
    MbmsUtils.startBinding(mContext, "android.telephony.action.EmbmsStreaming", new ServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        paramAnonymousComponentName = IMbmsStreamingService.Stub.asInterface(paramAnonymousIBinder);
        try
        {
          int i = paramAnonymousComponentName.initialize(mInternalCallback, mSubscriptionId);
          if (i != -1)
          {
            if (i != 0)
            {
              MbmsStreamingSession.this.sendErrorToApp(i, "Error returned during initialization");
              MbmsStreamingSession.sIsInitialized.set(false);
              return;
            }
            try
            {
              paramAnonymousComponentName.asBinder().linkToDeath(mDeathRecipient, 0);
              mService.set(paramAnonymousComponentName);
              return;
            }
            catch (RemoteException paramAnonymousComponentName)
            {
              MbmsStreamingSession.this.sendErrorToApp(3, "Middleware lost during initialization");
              MbmsStreamingSession.sIsInitialized.set(false);
              return;
            }
          }
          close();
          throw new IllegalStateException("Middleware must not return an unknown error code");
        }
        catch (RuntimeException paramAnonymousComponentName)
        {
          Log.e("MbmsStreamingSession", "Runtime exception during initialization");
          MbmsStreamingSession.this.sendErrorToApp(103, paramAnonymousComponentName.toString());
          MbmsStreamingSession.sIsInitialized.set(false);
          return;
        }
        catch (RemoteException paramAnonymousComponentName)
        {
          Log.e("MbmsStreamingSession", "Service died before initialization");
          MbmsStreamingSession.this.sendErrorToApp(103, paramAnonymousComponentName.toString());
          MbmsStreamingSession.sIsInitialized.set(false);
        }
      }
      
      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
        MbmsStreamingSession.sIsInitialized.set(false);
        mService.set(null);
      }
    });
  }
  
  public static MbmsStreamingSession create(Context paramContext, Executor paramExecutor, final int paramInt, MbmsStreamingSessionCallback paramMbmsStreamingSessionCallback)
  {
    if (sIsInitialized.compareAndSet(false, true))
    {
      paramContext = new MbmsStreamingSession(paramContext, paramExecutor, paramInt, paramMbmsStreamingSessionCallback);
      paramInt = paramContext.bindAndInitialize();
      if (paramInt != 0)
      {
        sIsInitialized.set(false);
        paramExecutor.execute(new Runnable()
        {
          public void run()
          {
            onError(paramInt, null);
          }
        });
        return null;
      }
      return paramContext;
    }
    throw new IllegalStateException("Cannot create two instances of MbmsStreamingSession");
  }
  
  public static MbmsStreamingSession create(Context paramContext, Executor paramExecutor, MbmsStreamingSessionCallback paramMbmsStreamingSessionCallback)
  {
    return create(paramContext, paramExecutor, SubscriptionManager.getDefaultSubscriptionId(), paramMbmsStreamingSessionCallback);
  }
  
  private void sendErrorToApp(int paramInt, String paramString)
  {
    try
    {
      mInternalCallback.onError(paramInt, paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  /* Error */
  public void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 60	android/telephony/MbmsStreamingSession:mService	Ljava/util/concurrent/atomic/AtomicReference;
    //   4: invokevirtual 153	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
    //   7: checkcast 155	android/telephony/mbms/vendor/IMbmsStreamingService
    //   10: astore_1
    //   11: aload_1
    //   12: ifnonnull +26 -> 38
    //   15: aload_0
    //   16: getfield 60	android/telephony/MbmsStreamingSession:mService	Ljava/util/concurrent/atomic/AtomicReference;
    //   19: aconst_null
    //   20: invokevirtual 157	java/util/concurrent/atomic/AtomicReference:set	(Ljava/lang/Object;)V
    //   23: getstatic 49	android/telephony/MbmsStreamingSession:sIsInitialized	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   26: iconst_0
    //   27: invokevirtual 119	java/util/concurrent/atomic/AtomicBoolean:set	(Z)V
    //   30: aload_0
    //   31: getfield 81	android/telephony/MbmsStreamingSession:mInternalCallback	Landroid/telephony/mbms/InternalStreamingSessionCallback;
    //   34: invokevirtual 160	android/telephony/mbms/InternalStreamingSessionCallback:stop	()V
    //   37: return
    //   38: aload_1
    //   39: aload_0
    //   40: getfield 72	android/telephony/MbmsStreamingSession:mSubscriptionId	I
    //   43: invokeinterface 164 2 0
    //   48: aload_0
    //   49: getfield 70	android/telephony/MbmsStreamingSession:mKnownActiveStreamingServices	Ljava/util/Set;
    //   52: invokeinterface 170 1 0
    //   57: astore_1
    //   58: aload_1
    //   59: invokeinterface 176 1 0
    //   64: ifeq +21 -> 85
    //   67: aload_1
    //   68: invokeinterface 179 1 0
    //   73: checkcast 181	android/telephony/mbms/StreamingService
    //   76: invokevirtual 185	android/telephony/mbms/StreamingService:getCallback	()Landroid/telephony/mbms/InternalStreamingServiceCallback;
    //   79: invokevirtual 188	android/telephony/mbms/InternalStreamingServiceCallback:stop	()V
    //   82: goto -24 -> 58
    //   85: aload_0
    //   86: getfield 70	android/telephony/MbmsStreamingSession:mKnownActiveStreamingServices	Ljava/util/Set;
    //   89: invokeinterface 191 1 0
    //   94: goto +29 -> 123
    //   97: astore_1
    //   98: aload_0
    //   99: getfield 60	android/telephony/MbmsStreamingSession:mService	Ljava/util/concurrent/atomic/AtomicReference;
    //   102: aconst_null
    //   103: invokevirtual 157	java/util/concurrent/atomic/AtomicReference:set	(Ljava/lang/Object;)V
    //   106: getstatic 49	android/telephony/MbmsStreamingSession:sIsInitialized	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   109: iconst_0
    //   110: invokevirtual 119	java/util/concurrent/atomic/AtomicBoolean:set	(Z)V
    //   113: aload_0
    //   114: getfield 81	android/telephony/MbmsStreamingSession:mInternalCallback	Landroid/telephony/mbms/InternalStreamingSessionCallback;
    //   117: invokevirtual 160	android/telephony/mbms/InternalStreamingSessionCallback:stop	()V
    //   120: aload_1
    //   121: athrow
    //   122: astore_1
    //   123: aload_0
    //   124: getfield 60	android/telephony/MbmsStreamingSession:mService	Ljava/util/concurrent/atomic/AtomicReference;
    //   127: aconst_null
    //   128: invokevirtual 157	java/util/concurrent/atomic/AtomicReference:set	(Ljava/lang/Object;)V
    //   131: getstatic 49	android/telephony/MbmsStreamingSession:sIsInitialized	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   134: iconst_0
    //   135: invokevirtual 119	java/util/concurrent/atomic/AtomicBoolean:set	(Z)V
    //   138: aload_0
    //   139: getfield 81	android/telephony/MbmsStreamingSession:mInternalCallback	Landroid/telephony/mbms/InternalStreamingSessionCallback;
    //   142: invokevirtual 160	android/telephony/mbms/InternalStreamingSessionCallback:stop	()V
    //   145: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	146	0	this	MbmsStreamingSession
    //   10	58	1	localObject1	Object
    //   97	24	1	localObject2	Object
    //   122	1	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   0	11	97	finally
    //   38	58	97	finally
    //   58	82	97	finally
    //   85	94	97	finally
    //   0	11	122	android/os/RemoteException
    //   38	58	122	android/os/RemoteException
    //   58	82	122	android/os/RemoteException
    //   85	94	122	android/os/RemoteException
  }
  
  public void onStreamingServiceStopped(StreamingService paramStreamingService)
  {
    mKnownActiveStreamingServices.remove(paramStreamingService);
  }
  
  public void requestUpdateStreamingServices(List<String> paramList)
  {
    IMbmsStreamingService localIMbmsStreamingService = (IMbmsStreamingService)mService.get();
    if (localIMbmsStreamingService != null)
    {
      try
      {
        int i = localIMbmsStreamingService.requestUpdateStreamingServices(mSubscriptionId, paramList);
        if (i != -1)
        {
          if (i != 0) {
            sendErrorToApp(i, null);
          }
        }
        else
        {
          close();
          paramList = new java/lang/IllegalStateException;
          paramList.<init>("Middleware must not return an unknown error code");
          throw paramList;
        }
      }
      catch (RemoteException paramList)
      {
        Log.w("MbmsStreamingSession", "Remote process died");
        mService.set(null);
        sIsInitialized.set(false);
        sendErrorToApp(3, null);
      }
      return;
    }
    throw new IllegalStateException("Middleware not yet bound");
  }
  
  public StreamingService startStreaming(StreamingServiceInfo paramStreamingServiceInfo, Executor paramExecutor, StreamingServiceCallback paramStreamingServiceCallback)
  {
    IMbmsStreamingService localIMbmsStreamingService = (IMbmsStreamingService)mService.get();
    if (localIMbmsStreamingService != null)
    {
      paramExecutor = new InternalStreamingServiceCallback(paramStreamingServiceCallback, paramExecutor);
      paramStreamingServiceCallback = new StreamingService(mSubscriptionId, localIMbmsStreamingService, this, paramStreamingServiceInfo, paramExecutor);
      mKnownActiveStreamingServices.add(paramStreamingServiceCallback);
      try
      {
        int i = localIMbmsStreamingService.startStreaming(mSubscriptionId, paramStreamingServiceInfo.getServiceId(), paramExecutor);
        if (i != -1)
        {
          if (i != 0)
          {
            sendErrorToApp(i, null);
            return null;
          }
          return paramStreamingServiceCallback;
        }
        close();
        paramStreamingServiceInfo = new java/lang/IllegalStateException;
        paramStreamingServiceInfo.<init>("Middleware must not return an unknown error code");
        throw paramStreamingServiceInfo;
      }
      catch (RemoteException paramStreamingServiceInfo)
      {
        Log.w("MbmsStreamingSession", "Remote process died");
        mService.set(null);
        sIsInitialized.set(false);
        sendErrorToApp(3, null);
        return null;
      }
    }
    throw new IllegalStateException("Middleware not yet bound");
  }
}
