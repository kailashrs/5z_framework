package android.telecom;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import com.android.internal.os.SomeArgs;
import com.android.internal.telecom.IVideoCallback;
import com.android.internal.telecom.IVideoCallback.Stub;

final class VideoCallbackServant
{
  private static final int MSG_CHANGE_CALL_DATA_USAGE = 4;
  private static final int MSG_CHANGE_CAMERA_CAPABILITIES = 5;
  private static final int MSG_CHANGE_PEER_DIMENSIONS = 3;
  private static final int MSG_CHANGE_VIDEO_QUALITY = 6;
  private static final int MSG_HANDLE_CALL_SESSION_EVENT = 2;
  private static final int MSG_RECEIVE_SESSION_MODIFY_REQUEST = 0;
  private static final int MSG_RECEIVE_SESSION_MODIFY_RESPONSE = 1;
  private final IVideoCallback mDelegate;
  private final Handler mHandler = new Handler()
  {
    private void internalHandleMessage(Message paramAnonymousMessage)
      throws RemoteException
    {
      switch (what)
      {
      default: 
        break;
      case 6: 
        mDelegate.changeVideoQuality(arg1);
        break;
      case 5: 
        mDelegate.changeCameraCapabilities((VideoProfile.CameraCapabilities)obj);
        break;
      case 4: 
        paramAnonymousMessage = (SomeArgs)obj;
      case 3: 
        try
        {
          mDelegate.changeCallDataUsage(((Long)arg1).longValue());
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
      case 2: 
        try
        {
          mDelegate.changePeerDimensions(argi1, argi2);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
      case 1: 
        try
        {
          mDelegate.handleCallSessionEvent(argi1);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
      case 0: 
        try
        {
          mDelegate.receiveSessionModifyResponse(argi1, (VideoProfile)arg1, (VideoProfile)arg2);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
      }
    }
    
    public void handleMessage(Message paramAnonymousMessage)
    {
      try
      {
        internalHandleMessage(paramAnonymousMessage);
      }
      catch (RemoteException paramAnonymousMessage) {}
    }
  };
  private final IVideoCallback mStub = new IVideoCallback.Stub()
  {
    public void changeCallDataUsage(long paramAnonymousLong)
      throws RemoteException
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = Long.valueOf(paramAnonymousLong);
      mHandler.obtainMessage(4, localSomeArgs).sendToTarget();
    }
    
    public void changeCameraCapabilities(VideoProfile.CameraCapabilities paramAnonymousCameraCapabilities)
      throws RemoteException
    {
      mHandler.obtainMessage(5, paramAnonymousCameraCapabilities).sendToTarget();
    }
    
    public void changePeerDimensions(int paramAnonymousInt1, int paramAnonymousInt2)
      throws RemoteException
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      argi1 = paramAnonymousInt1;
      argi2 = paramAnonymousInt2;
      mHandler.obtainMessage(3, localSomeArgs).sendToTarget();
    }
    
    public void changeVideoQuality(int paramAnonymousInt)
      throws RemoteException
    {
      mHandler.obtainMessage(6, paramAnonymousInt, 0).sendToTarget();
    }
    
    public void handleCallSessionEvent(int paramAnonymousInt)
      throws RemoteException
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      argi1 = paramAnonymousInt;
      mHandler.obtainMessage(2, localSomeArgs).sendToTarget();
    }
    
    public void receiveSessionModifyRequest(VideoProfile paramAnonymousVideoProfile)
      throws RemoteException
    {
      mHandler.obtainMessage(0, paramAnonymousVideoProfile).sendToTarget();
    }
    
    public void receiveSessionModifyResponse(int paramAnonymousInt, VideoProfile paramAnonymousVideoProfile1, VideoProfile paramAnonymousVideoProfile2)
      throws RemoteException
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      argi1 = paramAnonymousInt;
      arg1 = paramAnonymousVideoProfile1;
      arg2 = paramAnonymousVideoProfile2;
      mHandler.obtainMessage(1, localSomeArgs).sendToTarget();
    }
  };
  
  public VideoCallbackServant(IVideoCallback paramIVideoCallback)
  {
    mDelegate = paramIVideoCallback;
  }
  
  public IVideoCallback getStub()
  {
    return mStub;
  }
}
