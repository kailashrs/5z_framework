package android.hardware.camera2.legacy;

import android.hardware.camera2.impl.CameraMetadataNative;
import android.os.Handler;
import android.util.Log;

public class CameraDeviceState
{
  private static final boolean DEBUG = false;
  public static final int NO_CAPTURE_ERROR = -1;
  private static final int STATE_CAPTURING = 4;
  private static final int STATE_CONFIGURING = 2;
  private static final int STATE_ERROR = 0;
  private static final int STATE_IDLE = 3;
  private static final int STATE_UNCONFIGURED = 1;
  private static final String TAG = "CameraDeviceState";
  private static final String[] sStateNames = { "ERROR", "UNCONFIGURED", "CONFIGURING", "IDLE", "CAPTURING" };
  private int mCurrentError = -1;
  private Handler mCurrentHandler = null;
  private CameraDeviceStateListener mCurrentListener = null;
  private RequestHolder mCurrentRequest = null;
  private int mCurrentState = 1;
  
  public CameraDeviceState() {}
  
  private void doStateTransition(int paramInt)
  {
    doStateTransition(paramInt, 0L, -1);
  }
  
  private void doStateTransition(int paramInt1, final long paramLong, final int paramInt2)
  {
    Object localObject2;
    if (paramInt1 != mCurrentState)
    {
      Object localObject1 = "UNKNOWN";
      localObject2 = localObject1;
      if (paramInt1 >= 0)
      {
        localObject2 = localObject1;
        if (paramInt1 < sStateNames.length) {
          localObject2 = sStateNames[paramInt1];
        }
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Legacy camera service transitioning to state ");
      ((StringBuilder)localObject1).append((String)localObject2);
      Log.i("CameraDeviceState", ((StringBuilder)localObject1).toString());
    }
    if ((paramInt1 != 0) && (paramInt1 != 3) && (mCurrentState != paramInt1) && (mCurrentHandler != null) && (mCurrentListener != null)) {
      mCurrentHandler.post(new Runnable()
      {
        public void run()
        {
          mCurrentListener.onBusy();
        }
      });
    }
    if (paramInt1 != 0)
    {
      switch (paramInt1)
      {
      default: 
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Transition to unknown state: ");
        ((StringBuilder)localObject2).append(paramInt1);
        throw new IllegalStateException(((StringBuilder)localObject2).toString());
      case 4: 
        if ((mCurrentState != 3) && (mCurrentState != 4))
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Cannot call capture while in state: ");
          ((StringBuilder)localObject2).append(mCurrentState);
          Log.e("CameraDeviceState", ((StringBuilder)localObject2).toString());
          mCurrentError = 1;
          doStateTransition(0);
        }
        else
        {
          if ((mCurrentHandler != null) && (mCurrentListener != null)) {
            if (paramInt2 != -1) {
              mCurrentHandler.post(new Runnable()
              {
                public void run()
                {
                  mCurrentListener.onError(paramInt2, null, mCurrentRequest);
                }
              });
            } else {
              mCurrentHandler.post(new Runnable()
              {
                public void run()
                {
                  mCurrentListener.onCaptureStarted(mCurrentRequest, paramLong);
                }
              });
            }
          }
          mCurrentState = 4;
        }
        break;
      case 3: 
        if (mCurrentState == 3) {
          break;
        }
        if ((mCurrentState != 2) && (mCurrentState != 4))
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Cannot call idle while in state: ");
          ((StringBuilder)localObject2).append(mCurrentState);
          Log.e("CameraDeviceState", ((StringBuilder)localObject2).toString());
          mCurrentError = 1;
          doStateTransition(0);
        }
        else
        {
          if ((mCurrentState != 3) && (mCurrentHandler != null) && (mCurrentListener != null)) {
            mCurrentHandler.post(new Runnable()
            {
              public void run()
              {
                mCurrentListener.onIdle();
              }
            });
          }
          mCurrentState = 3;
        }
        break;
      case 2: 
        if ((mCurrentState != 1) && (mCurrentState != 3))
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Cannot call configure while in state: ");
          ((StringBuilder)localObject2).append(mCurrentState);
          Log.e("CameraDeviceState", ((StringBuilder)localObject2).toString());
          mCurrentError = 1;
          doStateTransition(0);
        }
        else
        {
          if ((mCurrentState != 2) && (mCurrentHandler != null) && (mCurrentListener != null)) {
            mCurrentHandler.post(new Runnable()
            {
              public void run()
              {
                mCurrentListener.onConfiguring();
              }
            });
          }
          mCurrentState = 2;
        }
        break;
      }
    }
    else
    {
      if ((mCurrentState != 0) && (mCurrentHandler != null) && (mCurrentListener != null)) {
        mCurrentHandler.post(new Runnable()
        {
          public void run()
          {
            mCurrentListener.onError(mCurrentError, null, mCurrentRequest);
          }
        });
      }
      mCurrentState = 0;
    }
  }
  
  public void setCameraDeviceCallbacks(Handler paramHandler, CameraDeviceStateListener paramCameraDeviceStateListener)
  {
    try
    {
      mCurrentHandler = paramHandler;
      mCurrentListener = paramCameraDeviceStateListener;
      return;
    }
    finally
    {
      paramHandler = finally;
      throw paramHandler;
    }
  }
  
  public boolean setCaptureResult(RequestHolder paramRequestHolder, CameraMetadataNative paramCameraMetadataNative)
  {
    try
    {
      boolean bool = setCaptureResult(paramRequestHolder, paramCameraMetadataNative, -1, null);
      return bool;
    }
    finally
    {
      paramRequestHolder = finally;
      throw paramRequestHolder;
    }
  }
  
  public boolean setCaptureResult(RequestHolder paramRequestHolder, CameraMetadataNative paramCameraMetadataNative, int paramInt, Object paramObject)
  {
    try
    {
      int i = mCurrentState;
      boolean bool1 = false;
      boolean bool2 = false;
      if (i != 4)
      {
        paramRequestHolder = new java/lang/StringBuilder;
        paramRequestHolder.<init>();
        paramRequestHolder.append("Cannot receive result while in state: ");
        paramRequestHolder.append(mCurrentState);
        Log.e("CameraDeviceState", paramRequestHolder.toString());
        mCurrentError = 1;
        doStateTransition(0);
        paramInt = mCurrentError;
        if (paramInt == -1) {
          bool2 = true;
        }
        return bool2;
      }
      if ((mCurrentHandler != null) && (mCurrentListener != null))
      {
        Object localObject;
        if (paramInt != -1)
        {
          paramCameraMetadataNative = mCurrentHandler;
          localObject = new android/hardware/camera2/legacy/CameraDeviceState$1;
          ((1)localObject).<init>(this, paramInt, paramObject, paramRequestHolder);
          paramCameraMetadataNative.post((Runnable)localObject);
        }
        else
        {
          paramObject = mCurrentHandler;
          localObject = new android/hardware/camera2/legacy/CameraDeviceState$2;
          ((2)localObject).<init>(this, paramCameraMetadataNative, paramRequestHolder);
          paramObject.post((Runnable)localObject);
        }
      }
      paramInt = mCurrentError;
      bool2 = bool1;
      if (paramInt == -1) {
        bool2 = true;
      }
      return bool2;
    }
    finally {}
  }
  
  public boolean setCaptureStart(RequestHolder paramRequestHolder, long paramLong, int paramInt)
  {
    try
    {
      mCurrentRequest = paramRequestHolder;
      doStateTransition(4, paramLong, paramInt);
      paramInt = mCurrentError;
      boolean bool;
      if (paramInt == -1) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    finally
    {
      paramRequestHolder = finally;
      throw paramRequestHolder;
    }
  }
  
  public boolean setConfiguring()
  {
    try
    {
      doStateTransition(2);
      int i = mCurrentError;
      boolean bool;
      if (i == -1) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setError(int paramInt)
  {
    try
    {
      mCurrentError = paramInt;
      doStateTransition(0);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean setIdle()
  {
    try
    {
      doStateTransition(3);
      int i = mCurrentError;
      boolean bool;
      if (i == -1) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setRepeatingRequestError(long paramLong, int paramInt)
  {
    try
    {
      Handler localHandler = mCurrentHandler;
      Runnable local3 = new android/hardware/camera2/legacy/CameraDeviceState$3;
      local3.<init>(this, paramLong, paramInt);
      localHandler.post(local3);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setRequestQueueEmpty()
  {
    try
    {
      Handler localHandler = mCurrentHandler;
      Runnable local4 = new android/hardware/camera2/legacy/CameraDeviceState$4;
      local4.<init>(this);
      localHandler.post(local4);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public static abstract interface CameraDeviceStateListener
  {
    public abstract void onBusy();
    
    public abstract void onCaptureResult(CameraMetadataNative paramCameraMetadataNative, RequestHolder paramRequestHolder);
    
    public abstract void onCaptureStarted(RequestHolder paramRequestHolder, long paramLong);
    
    public abstract void onConfiguring();
    
    public abstract void onError(int paramInt, Object paramObject, RequestHolder paramRequestHolder);
    
    public abstract void onIdle();
    
    public abstract void onRepeatingRequestError(long paramLong, int paramInt);
    
    public abstract void onRequestQueueEmpty();
  }
}
