package android.telecom;

import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.view.Surface;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.internal.telecom.IVideoCallback.Stub;
import com.android.internal.telecom.IVideoProvider;

public class VideoCallImpl
  extends InCallService.VideoCall
{
  private final VideoCallListenerBinder mBinder;
  private InCallService.VideoCall.Callback mCallback;
  private final String mCallingPackageName;
  private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient()
  {
    public void binderDied()
    {
      mVideoProvider.asBinder().unlinkToDeath(this, 0);
    }
  };
  private Handler mHandler;
  private int mTargetSdkVersion;
  private final IVideoProvider mVideoProvider;
  private int mVideoQuality = 0;
  private int mVideoState = 0;
  
  VideoCallImpl(IVideoProvider paramIVideoProvider, String paramString, int paramInt)
    throws RemoteException
  {
    mVideoProvider = paramIVideoProvider;
    mVideoProvider.asBinder().linkToDeath(mDeathRecipient, 0);
    mBinder = new VideoCallListenerBinder(null);
    mVideoProvider.addVideoCallback(mBinder);
    mCallingPackageName = paramString;
    setTargetSdkVersion(paramInt);
  }
  
  public void destroy()
  {
    unregisterCallback(mCallback);
  }
  
  public void registerCallback(InCallService.VideoCall.Callback paramCallback)
  {
    registerCallback(paramCallback, null);
  }
  
  public void registerCallback(InCallService.VideoCall.Callback paramCallback, Handler paramHandler)
  {
    mCallback = paramCallback;
    if (paramHandler == null) {
      mHandler = new MessageHandler(Looper.getMainLooper());
    } else {
      mHandler = new MessageHandler(paramHandler.getLooper());
    }
  }
  
  public void requestCallDataUsage()
  {
    try
    {
      mVideoProvider.requestCallDataUsage();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void requestCameraCapabilities()
  {
    try
    {
      mVideoProvider.requestCameraCapabilities();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void sendSessionModifyRequest(VideoProfile paramVideoProfile)
  {
    try
    {
      VideoProfile localVideoProfile = new android/telecom/VideoProfile;
      localVideoProfile.<init>(mVideoState, mVideoQuality);
      mVideoProvider.sendSessionModifyRequest(localVideoProfile, paramVideoProfile);
    }
    catch (RemoteException paramVideoProfile) {}
  }
  
  public void sendSessionModifyResponse(VideoProfile paramVideoProfile)
  {
    try
    {
      mVideoProvider.sendSessionModifyResponse(paramVideoProfile);
    }
    catch (RemoteException paramVideoProfile) {}
  }
  
  public void setCamera(String paramString)
  {
    try
    {
      Log.w(this, "setCamera: cameraId=%s, calling=%s", new Object[] { paramString, mCallingPackageName });
      mVideoProvider.setCamera(paramString, mCallingPackageName, mTargetSdkVersion);
    }
    catch (RemoteException paramString) {}
  }
  
  public void setDeviceOrientation(int paramInt)
  {
    try
    {
      mVideoProvider.setDeviceOrientation(paramInt);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void setDisplaySurface(Surface paramSurface)
  {
    try
    {
      mVideoProvider.setDisplaySurface(paramSurface);
    }
    catch (RemoteException paramSurface) {}
  }
  
  public void setPauseImage(Uri paramUri)
  {
    try
    {
      mVideoProvider.setPauseImage(paramUri);
    }
    catch (RemoteException paramUri) {}
  }
  
  public void setPreviewSurface(Surface paramSurface)
  {
    try
    {
      mVideoProvider.setPreviewSurface(paramSurface);
    }
    catch (RemoteException paramSurface) {}
  }
  
  @VisibleForTesting
  public void setTargetSdkVersion(int paramInt)
  {
    mTargetSdkVersion = paramInt;
  }
  
  public void setVideoState(int paramInt)
  {
    mVideoState = paramInt;
  }
  
  public void setZoom(float paramFloat)
  {
    try
    {
      mVideoProvider.setZoom(paramFloat);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void unregisterCallback(InCallService.VideoCall.Callback paramCallback)
  {
    if (paramCallback != mCallback) {
      return;
    }
    mCallback = null;
    try
    {
      mVideoProvider.removeVideoCallback(mBinder);
    }
    catch (RemoteException paramCallback) {}
  }
  
  private final class MessageHandler
    extends Handler
  {
    private static final int MSG_CHANGE_CALL_DATA_USAGE = 5;
    private static final int MSG_CHANGE_CAMERA_CAPABILITIES = 6;
    private static final int MSG_CHANGE_PEER_DIMENSIONS = 4;
    private static final int MSG_CHANGE_VIDEO_QUALITY = 7;
    private static final int MSG_HANDLE_CALL_SESSION_EVENT = 3;
    private static final int MSG_RECEIVE_SESSION_MODIFY_REQUEST = 1;
    private static final int MSG_RECEIVE_SESSION_MODIFY_RESPONSE = 2;
    
    public MessageHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (mCallback == null) {
        return;
      }
      Object localObject1;
      int j;
      switch (what)
      {
      default: 
        break;
      case 7: 
        VideoCallImpl.access$302(VideoCallImpl.this, arg1);
        mCallback.onVideoQualityChanged(arg1);
        break;
      case 6: 
        mCallback.onCameraCapabilitiesChanged((VideoProfile.CameraCapabilities)obj);
        break;
      case 5: 
        mCallback.onCallDataUsageChanged(((Long)obj).longValue());
        break;
      case 4: 
        localObject1 = (SomeArgs)obj;
      case 3: 
        try
        {
          int i = ((Integer)arg1).intValue();
          j = ((Integer)arg2).intValue();
          mCallback.onPeerDimensionsChanged(i, j);
          ((SomeArgs)localObject1).recycle();
        }
        finally
        {
          ((SomeArgs)localObject1).recycle();
        }
        break;
      case 2: 
        paramMessage = (SomeArgs)obj;
      case 1: 
        try
        {
          j = ((Integer)arg1).intValue();
          VideoProfile localVideoProfile = (VideoProfile)arg2;
          localObject1 = (VideoProfile)arg3;
          mCallback.onSessionModifyResponseReceived(j, localVideoProfile, (VideoProfile)localObject1);
          paramMessage.recycle();
        }
        finally
        {
          paramMessage.recycle();
        }
      }
    }
  }
  
  private final class VideoCallListenerBinder
    extends IVideoCallback.Stub
  {
    private VideoCallListenerBinder() {}
    
    public void changeCallDataUsage(long paramLong)
    {
      if (mHandler == null) {
        return;
      }
      mHandler.obtainMessage(5, Long.valueOf(paramLong)).sendToTarget();
    }
    
    public void changeCameraCapabilities(VideoProfile.CameraCapabilities paramCameraCapabilities)
    {
      if (mHandler == null) {
        return;
      }
      mHandler.obtainMessage(6, paramCameraCapabilities).sendToTarget();
    }
    
    public void changePeerDimensions(int paramInt1, int paramInt2)
    {
      if (mHandler == null) {
        return;
      }
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = Integer.valueOf(paramInt1);
      arg2 = Integer.valueOf(paramInt2);
      mHandler.obtainMessage(4, localSomeArgs).sendToTarget();
    }
    
    public void changeVideoQuality(int paramInt)
    {
      if (mHandler == null) {
        return;
      }
      mHandler.obtainMessage(7, paramInt, 0).sendToTarget();
    }
    
    public void handleCallSessionEvent(int paramInt)
    {
      if (mHandler == null) {
        return;
      }
      mHandler.obtainMessage(3, Integer.valueOf(paramInt)).sendToTarget();
    }
    
    public void receiveSessionModifyRequest(VideoProfile paramVideoProfile)
    {
      if (mHandler == null) {
        return;
      }
      mHandler.obtainMessage(1, paramVideoProfile).sendToTarget();
    }
    
    public void receiveSessionModifyResponse(int paramInt, VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2)
    {
      if (mHandler == null) {
        return;
      }
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = Integer.valueOf(paramInt);
      arg2 = paramVideoProfile1;
      arg3 = paramVideoProfile2;
      mHandler.obtainMessage(2, localSomeArgs).sendToTarget();
    }
  }
}
