package android.telephony.mbms.vendor;

import android.annotation.SystemApi;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.telephony.mbms.IMbmsStreamingSessionCallback;
import android.telephony.mbms.IStreamingServiceCallback;
import android.telephony.mbms.MbmsStreamingSessionCallback;
import android.telephony.mbms.StreamingServiceCallback;
import android.telephony.mbms.StreamingServiceInfo;
import java.util.List;

@SystemApi
public class MbmsStreamingServiceBase
  extends IMbmsStreamingService.Stub
{
  public MbmsStreamingServiceBase() {}
  
  public void dispose(int paramInt)
    throws RemoteException
  {}
  
  public Uri getPlaybackUri(int paramInt, String paramString)
    throws RemoteException
  {
    return null;
  }
  
  public final int initialize(final IMbmsStreamingSessionCallback paramIMbmsStreamingSessionCallback, final int paramInt)
    throws RemoteException
  {
    if (paramIMbmsStreamingSessionCallback != null)
    {
      final int i = Binder.getCallingUid();
      int j = initialize(new MbmsStreamingSessionCallback()
      {
        public void onError(int paramAnonymousInt, String paramAnonymousString)
        {
          if (paramAnonymousInt != -1) {}
          try
          {
            paramIMbmsStreamingSessionCallback.onError(paramAnonymousInt, paramAnonymousString);
          }
          catch (RemoteException paramAnonymousString)
          {
            onAppCallbackDied(i, paramInt);
          }
          paramAnonymousString = new java/lang/IllegalArgumentException;
          paramAnonymousString.<init>("Middleware cannot send an unknown error.");
          throw paramAnonymousString;
        }
        
        public void onMiddlewareReady()
        {
          try
          {
            paramIMbmsStreamingSessionCallback.onMiddlewareReady();
          }
          catch (RemoteException localRemoteException)
          {
            onAppCallbackDied(i, paramInt);
          }
        }
        
        public void onStreamingServicesUpdated(List<StreamingServiceInfo> paramAnonymousList)
        {
          try
          {
            paramIMbmsStreamingSessionCallback.onStreamingServicesUpdated(paramAnonymousList);
          }
          catch (RemoteException paramAnonymousList)
          {
            onAppCallbackDied(i, paramInt);
          }
        }
      }, paramInt);
      if (j == 0) {
        paramIMbmsStreamingSessionCallback.asBinder().linkToDeath(new IBinder.DeathRecipient()
        {
          public void binderDied()
          {
            onAppCallbackDied(i, paramInt);
          }
        }, 0);
      }
      return j;
    }
    throw new NullPointerException("Callback must not be null");
  }
  
  public int initialize(MbmsStreamingSessionCallback paramMbmsStreamingSessionCallback, int paramInt)
    throws RemoteException
  {
    return 0;
  }
  
  public void onAppCallbackDied(int paramInt1, int paramInt2) {}
  
  public int requestUpdateStreamingServices(int paramInt, List<String> paramList)
    throws RemoteException
  {
    return 0;
  }
  
  public int startStreaming(final int paramInt, String paramString, final IStreamingServiceCallback paramIStreamingServiceCallback)
    throws RemoteException
  {
    if (paramIStreamingServiceCallback != null)
    {
      final int i = Binder.getCallingUid();
      int j = startStreaming(paramInt, paramString, new StreamingServiceCallback()
      {
        public void onBroadcastSignalStrengthUpdated(int paramAnonymousInt)
        {
          try
          {
            paramIStreamingServiceCallback.onBroadcastSignalStrengthUpdated(paramAnonymousInt);
          }
          catch (RemoteException localRemoteException)
          {
            onAppCallbackDied(i, paramInt);
          }
        }
        
        public void onError(int paramAnonymousInt, String paramAnonymousString)
        {
          if (paramAnonymousInt != -1) {}
          try
          {
            paramIStreamingServiceCallback.onError(paramAnonymousInt, paramAnonymousString);
          }
          catch (RemoteException paramAnonymousString)
          {
            onAppCallbackDied(i, paramInt);
          }
          paramAnonymousString = new java/lang/IllegalArgumentException;
          paramAnonymousString.<init>("Middleware cannot send an unknown error.");
          throw paramAnonymousString;
        }
        
        public void onMediaDescriptionUpdated()
        {
          try
          {
            paramIStreamingServiceCallback.onMediaDescriptionUpdated();
          }
          catch (RemoteException localRemoteException)
          {
            onAppCallbackDied(i, paramInt);
          }
        }
        
        public void onStreamMethodUpdated(int paramAnonymousInt)
        {
          try
          {
            paramIStreamingServiceCallback.onStreamMethodUpdated(paramAnonymousInt);
          }
          catch (RemoteException localRemoteException)
          {
            onAppCallbackDied(i, paramInt);
          }
        }
        
        public void onStreamStateUpdated(int paramAnonymousInt1, int paramAnonymousInt2)
        {
          try
          {
            paramIStreamingServiceCallback.onStreamStateUpdated(paramAnonymousInt1, paramAnonymousInt2);
          }
          catch (RemoteException localRemoteException)
          {
            onAppCallbackDied(i, paramInt);
          }
        }
      });
      if (j == 0) {
        paramIStreamingServiceCallback.asBinder().linkToDeath(new IBinder.DeathRecipient()
        {
          public void binderDied()
          {
            onAppCallbackDied(i, paramInt);
          }
        }, 0);
      }
      return j;
    }
    throw new NullPointerException("Callback must not be null");
  }
  
  public int startStreaming(int paramInt, String paramString, StreamingServiceCallback paramStreamingServiceCallback)
    throws RemoteException
  {
    return 0;
  }
  
  public void stopStreaming(int paramInt, String paramString)
    throws RemoteException
  {}
}
