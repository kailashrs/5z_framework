package android.telephony.ims;

import android.annotation.SystemApi;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telecom.VideoProfile;
import android.telecom.VideoProfile.CameraCapabilities;
import android.view.Surface;
import com.android.ims.internal.IImsVideoCallCallback;
import com.android.ims.internal.IImsVideoCallProvider;
import com.android.ims.internal.IImsVideoCallProvider.Stub;
import com.android.internal.os.SomeArgs;

@SystemApi
public abstract class ImsVideoCallProvider
{
  private static final int MSG_REQUEST_CALL_DATA_USAGE = 10;
  private static final int MSG_REQUEST_CAMERA_CAPABILITIES = 9;
  private static final int MSG_SEND_SESSION_MODIFY_REQUEST = 7;
  private static final int MSG_SEND_SESSION_MODIFY_RESPONSE = 8;
  private static final int MSG_SET_CALLBACK = 1;
  private static final int MSG_SET_CAMERA = 2;
  private static final int MSG_SET_DEVICE_ORIENTATION = 5;
  private static final int MSG_SET_DISPLAY_SURFACE = 4;
  private static final int MSG_SET_PAUSE_IMAGE = 11;
  private static final int MSG_SET_PREVIEW_SURFACE = 3;
  private static final int MSG_SET_ZOOM = 6;
  private final ImsVideoCallProviderBinder mBinder = new ImsVideoCallProviderBinder(null);
  private IImsVideoCallCallback mCallback;
  private final Handler mProviderHandler = new Handler(Looper.getMainLooper())
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 11: 
        onSetPauseImage((Uri)obj);
        break;
      case 10: 
        onRequestCallDataUsage();
        break;
      case 9: 
        onRequestCameraCapabilities();
        break;
      case 8: 
        onSendSessionModifyResponse((VideoProfile)obj);
        break;
      case 7: 
        paramAnonymousMessage = (SomeArgs)obj;
      case 6: 
        try
        {
          VideoProfile localVideoProfile1 = (VideoProfile)arg1;
          VideoProfile localVideoProfile2 = (VideoProfile)arg2;
          onSendSessionModifyRequest(localVideoProfile1, localVideoProfile2);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
        break;
      case 5: 
        onSetDeviceOrientation(arg1);
        break;
      case 4: 
        onSetDisplaySurface((Surface)obj);
        break;
      case 3: 
        onSetPreviewSurface((Surface)obj);
        break;
      case 2: 
        paramAnonymousMessage = (SomeArgs)obj;
      case 1: 
        try
        {
          onSetCamera((String)arg1);
          onSetCamera((String)arg1, argi1);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
      }
    }
  };
  
  public ImsVideoCallProvider() {}
  
  public void changeCallDataUsage(long paramLong)
  {
    if (mCallback != null) {
      try
      {
        mCallback.changeCallDataUsage(paramLong);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  public void changeCameraCapabilities(VideoProfile.CameraCapabilities paramCameraCapabilities)
  {
    if (mCallback != null) {
      try
      {
        mCallback.changeCameraCapabilities(paramCameraCapabilities);
      }
      catch (RemoteException paramCameraCapabilities) {}
    }
  }
  
  public void changePeerDimensions(int paramInt1, int paramInt2)
  {
    if (mCallback != null) {
      try
      {
        mCallback.changePeerDimensions(paramInt1, paramInt2);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  public void changeVideoQuality(int paramInt)
  {
    if (mCallback != null) {
      try
      {
        mCallback.changeVideoQuality(paramInt);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  public final IImsVideoCallProvider getInterface()
  {
    return mBinder;
  }
  
  public void handleCallSessionEvent(int paramInt)
  {
    if (mCallback != null) {
      try
      {
        mCallback.handleCallSessionEvent(paramInt);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  public abstract void onRequestCallDataUsage();
  
  public abstract void onRequestCameraCapabilities();
  
  public abstract void onSendSessionModifyRequest(VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2);
  
  public abstract void onSendSessionModifyResponse(VideoProfile paramVideoProfile);
  
  public abstract void onSetCamera(String paramString);
  
  public void onSetCamera(String paramString, int paramInt) {}
  
  public abstract void onSetDeviceOrientation(int paramInt);
  
  public abstract void onSetDisplaySurface(Surface paramSurface);
  
  public abstract void onSetPauseImage(Uri paramUri);
  
  public abstract void onSetPreviewSurface(Surface paramSurface);
  
  public abstract void onSetZoom(float paramFloat);
  
  public void receiveSessionModifyRequest(VideoProfile paramVideoProfile)
  {
    if (mCallback != null) {
      try
      {
        mCallback.receiveSessionModifyRequest(paramVideoProfile);
      }
      catch (RemoteException paramVideoProfile) {}
    }
  }
  
  public void receiveSessionModifyResponse(int paramInt, VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2)
  {
    if (mCallback != null) {
      try
      {
        mCallback.receiveSessionModifyResponse(paramInt, paramVideoProfile1, paramVideoProfile2);
      }
      catch (RemoteException paramVideoProfile1) {}
    }
  }
  
  private final class ImsVideoCallProviderBinder
    extends IImsVideoCallProvider.Stub
  {
    private ImsVideoCallProviderBinder() {}
    
    public void requestCallDataUsage()
    {
      mProviderHandler.obtainMessage(10).sendToTarget();
    }
    
    public void requestCameraCapabilities()
    {
      mProviderHandler.obtainMessage(9).sendToTarget();
    }
    
    public void sendSessionModifyRequest(VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramVideoProfile1;
      arg2 = paramVideoProfile2;
      mProviderHandler.obtainMessage(7, localSomeArgs).sendToTarget();
    }
    
    public void sendSessionModifyResponse(VideoProfile paramVideoProfile)
    {
      mProviderHandler.obtainMessage(8, paramVideoProfile).sendToTarget();
    }
    
    public void setCallback(IImsVideoCallCallback paramIImsVideoCallCallback)
    {
      mProviderHandler.obtainMessage(1, paramIImsVideoCallCallback).sendToTarget();
    }
    
    public void setCamera(String paramString, int paramInt)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramString;
      argi1 = paramInt;
      mProviderHandler.obtainMessage(2, localSomeArgs).sendToTarget();
    }
    
    public void setDeviceOrientation(int paramInt)
    {
      mProviderHandler.obtainMessage(5, paramInt, 0).sendToTarget();
    }
    
    public void setDisplaySurface(Surface paramSurface)
    {
      mProviderHandler.obtainMessage(4, paramSurface).sendToTarget();
    }
    
    public void setPauseImage(Uri paramUri)
    {
      mProviderHandler.obtainMessage(11, paramUri).sendToTarget();
    }
    
    public void setPreviewSurface(Surface paramSurface)
    {
      mProviderHandler.obtainMessage(3, paramSurface).sendToTarget();
    }
    
    public void setZoom(float paramFloat)
    {
      mProviderHandler.obtainMessage(6, Float.valueOf(paramFloat)).sendToTarget();
    }
  }
}
