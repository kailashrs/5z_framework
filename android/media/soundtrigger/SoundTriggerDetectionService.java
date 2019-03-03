package android.media.soundtrigger;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent;
import android.hardware.soundtrigger.SoundTrigger.RecognitionEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.UUID;

@SystemApi
public abstract class SoundTriggerDetectionService
  extends Service
{
  private static final boolean DEBUG = false;
  private static final String LOG_TAG = SoundTriggerDetectionService.class.getSimpleName();
  @GuardedBy("mLock")
  private final ArrayMap<UUID, ISoundTriggerDetectionServiceClient> mClients = new ArrayMap();
  private Handler mHandler;
  private final Object mLock = new Object();
  
  public SoundTriggerDetectionService() {}
  
  private void removeClient(UUID paramUUID, Bundle paramBundle)
  {
    synchronized (mLock)
    {
      mClients.remove(paramUUID);
      onDisconnected(paramUUID, paramBundle);
      return;
    }
  }
  
  private void setClient(UUID paramUUID, Bundle paramBundle, ISoundTriggerDetectionServiceClient paramISoundTriggerDetectionServiceClient)
  {
    synchronized (mLock)
    {
      mClients.put(paramUUID, paramISoundTriggerDetectionServiceClient);
      onConnected(paramUUID, paramBundle);
      return;
    }
  }
  
  protected final void attachBaseContext(Context paramContext)
  {
    super.attachBaseContext(paramContext);
    mHandler = new Handler(paramContext.getMainLooper());
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    new ISoundTriggerDetectionService.Stub()
    {
      private final Object mBinderLock = new Object();
      @GuardedBy("mBinderLock")
      public final ArrayMap<UUID, Bundle> mParams = new ArrayMap();
      
      public void onError(ParcelUuid arg1, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        UUID localUUID = ???.getUuid();
        synchronized (mBinderLock)
        {
          Bundle localBundle = (Bundle)mParams.get(localUUID);
          mHandler.sendMessage(PooledLambda.obtainMessage(_..Lambda.oNgT3sYhSGVWlnU92bECo_ULGeY.INSTANCE, SoundTriggerDetectionService.this, localUUID, localBundle, Integer.valueOf(paramAnonymousInt1), Integer.valueOf(paramAnonymousInt2)));
          return;
        }
      }
      
      public void onGenericRecognitionEvent(ParcelUuid arg1, int paramAnonymousInt, SoundTrigger.GenericRecognitionEvent paramAnonymousGenericRecognitionEvent)
      {
        UUID localUUID = ???.getUuid();
        synchronized (mBinderLock)
        {
          Bundle localBundle = (Bundle)mParams.get(localUUID);
          mHandler.sendMessage(PooledLambda.obtainMessage(_..Lambda.ISQYIYPBRBIOLBUJy7rrJW_SiJg.INSTANCE, SoundTriggerDetectionService.this, localUUID, localBundle, Integer.valueOf(paramAnonymousInt), paramAnonymousGenericRecognitionEvent));
          return;
        }
      }
      
      public void onStopOperation(ParcelUuid arg1, int paramAnonymousInt)
      {
        UUID localUUID = ???.getUuid();
        synchronized (mBinderLock)
        {
          Bundle localBundle = (Bundle)mParams.get(localUUID);
          mHandler.sendMessage(PooledLambda.obtainMessage(_..Lambda.bPGNpvkCtpPW14oaI3pxn1e6JtQ.INSTANCE, SoundTriggerDetectionService.this, localUUID, localBundle, Integer.valueOf(paramAnonymousInt)));
          return;
        }
      }
      
      public void removeClient(ParcelUuid arg1)
      {
        UUID localUUID = ???.getUuid();
        synchronized (mBinderLock)
        {
          Bundle localBundle = (Bundle)mParams.remove(localUUID);
          mHandler.sendMessage(PooledLambda.obtainMessage(_..Lambda.SoundTriggerDetectionService.1.pKR4r0FzOzoVczcnvLQIZNjkZZw.INSTANCE, SoundTriggerDetectionService.this, localUUID, localBundle));
          return;
        }
      }
      
      public void setClient(ParcelUuid arg1, Bundle paramAnonymousBundle, ISoundTriggerDetectionServiceClient paramAnonymousISoundTriggerDetectionServiceClient)
      {
        UUID localUUID = ???.getUuid();
        synchronized (mBinderLock)
        {
          mParams.put(localUUID, paramAnonymousBundle);
          mHandler.sendMessage(PooledLambda.obtainMessage(_..Lambda.SoundTriggerDetectionService.1.LlOo7TiZplZCgGhS07DqYHocFcw.INSTANCE, SoundTriggerDetectionService.this, localUUID, paramAnonymousBundle, paramAnonymousISoundTriggerDetectionServiceClient));
          return;
        }
      }
    };
  }
  
  public void onConnected(UUID paramUUID, Bundle paramBundle) {}
  
  public void onDisconnected(UUID paramUUID, Bundle paramBundle) {}
  
  public void onError(UUID paramUUID, Bundle paramBundle, int paramInt1, int paramInt2)
  {
    operationFinished(paramUUID, paramInt1);
  }
  
  public void onGenericRecognitionEvent(UUID paramUUID, Bundle paramBundle, int paramInt, SoundTrigger.RecognitionEvent paramRecognitionEvent)
  {
    operationFinished(paramUUID, paramInt);
  }
  
  public abstract void onStopOperation(UUID paramUUID, Bundle paramBundle, int paramInt);
  
  public boolean onUnbind(Intent paramIntent)
  {
    mClients.clear();
    return false;
  }
  
  public final void operationFinished(UUID paramUUID, int paramInt)
  {
    try
    {
      String str;
      synchronized (mLock)
      {
        Object localObject2 = (ISoundTriggerDetectionServiceClient)mClients.get(paramUUID);
        if (localObject2 == null)
        {
          str = LOG_TAG;
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("operationFinished called, but no client for ");
          ((StringBuilder)localObject2).append(paramUUID);
          ((StringBuilder)localObject2).append(". Was this called after onDisconnected?");
          Log.w(str, ((StringBuilder)localObject2).toString());
          return;
        }
        ((ISoundTriggerDetectionServiceClient)localObject2).onOpFinished(paramInt);
      }
      StringBuilder localStringBuilder;
      return;
    }
    catch (RemoteException localRemoteException)
    {
      str = LOG_TAG;
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("operationFinished, remote exception for client ");
      localStringBuilder.append(paramUUID);
      Log.e(str, localStringBuilder.toString(), localRemoteException);
    }
  }
}
