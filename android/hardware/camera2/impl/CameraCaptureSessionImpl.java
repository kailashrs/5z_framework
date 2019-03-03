package android.hardware.camera2.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCaptureSession.StateCallback;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.utils.TaskDrainer;
import android.hardware.camera2.utils.TaskDrainer.DrainListener;
import android.hardware.camera2.utils.TaskSingleDrainer;
import android.os.Binder;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class CameraCaptureSessionImpl
  extends CameraCaptureSession
  implements CameraCaptureSessionCore
{
  private static final boolean DEBUG = false;
  private static final String TAG = "CameraCaptureSession";
  private final TaskSingleDrainer mAbortDrainer;
  private volatile boolean mAborting;
  private boolean mClosed = false;
  private final boolean mConfigureSuccess;
  private final Executor mDeviceExecutor;
  private final CameraDeviceImpl mDeviceImpl;
  private final int mId;
  private final String mIdString;
  private final TaskSingleDrainer mIdleDrainer;
  private final Surface mInput;
  private final TaskDrainer<Integer> mSequenceDrainer;
  private boolean mSkipUnconfigure = false;
  private final CameraCaptureSession.StateCallback mStateCallback;
  private final Executor mStateExecutor;
  
  CameraCaptureSessionImpl(int paramInt, Surface paramSurface, CameraCaptureSession.StateCallback paramStateCallback, Executor paramExecutor1, CameraDeviceImpl paramCameraDeviceImpl, Executor paramExecutor2, boolean paramBoolean)
  {
    if (paramStateCallback != null)
    {
      mId = paramInt;
      mIdString = String.format("Session %d: ", new Object[] { Integer.valueOf(mId) });
      mInput = paramSurface;
      mStateExecutor = ((Executor)Preconditions.checkNotNull(paramExecutor1, "stateExecutor must not be null"));
      mStateCallback = createUserStateCallbackProxy(mStateExecutor, paramStateCallback);
      mDeviceExecutor = ((Executor)Preconditions.checkNotNull(paramExecutor2, "deviceStateExecutor must not be null"));
      mDeviceImpl = ((CameraDeviceImpl)Preconditions.checkNotNull(paramCameraDeviceImpl, "deviceImpl must not be null"));
      mSequenceDrainer = new TaskDrainer(mDeviceExecutor, new SequenceDrainListener(null), "seq");
      mIdleDrainer = new TaskSingleDrainer(mDeviceExecutor, new IdleDrainListener(null), "idle");
      mAbortDrainer = new TaskSingleDrainer(mDeviceExecutor, new AbortDrainListener(null), "abort");
      if (paramBoolean)
      {
        mStateCallback.onConfigured(this);
        mConfigureSuccess = true;
      }
      else
      {
        mStateCallback.onConfigureFailed(this);
        mClosed = true;
        paramSurface = new StringBuilder();
        paramSurface.append(mIdString);
        paramSurface.append("Failed to create capture session; configuration failed");
        Log.e("CameraCaptureSession", paramSurface.toString());
        mConfigureSuccess = false;
      }
      return;
    }
    throw new IllegalArgumentException("callback must not be null");
  }
  
  private int addPendingSequence(int paramInt)
  {
    mSequenceDrainer.taskStarted(Integer.valueOf(paramInt));
    return paramInt;
  }
  
  private void checkCaptureRequest(CaptureRequest paramCaptureRequest)
  {
    if (paramCaptureRequest != null)
    {
      if ((paramCaptureRequest.isReprocess()) && (!isReprocessable())) {
        throw new IllegalArgumentException("this capture session cannot handle reprocess requests");
      }
      if ((!mDeviceImpl.isPrivilegedApp()) && (paramCaptureRequest.isReprocess()) && (paramCaptureRequest.getReprocessableSessionId() != mId)) {
        throw new IllegalArgumentException("capture request was created for another session");
      }
      return;
    }
    throw new IllegalArgumentException("request must not be null");
  }
  
  private void checkCaptureRequests(List<CaptureRequest> paramList)
  {
    if (paramList != null)
    {
      if (!paramList.isEmpty())
      {
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          paramList = (CaptureRequest)localIterator.next();
          if (paramList.isReprocess()) {
            if (isReprocessable())
            {
              if (paramList.getReprocessableSessionId() != mId) {
                throw new IllegalArgumentException("Capture request was created for another session");
              }
            }
            else {
              throw new IllegalArgumentException("This capture session cannot handle reprocess requests");
            }
          }
        }
        return;
      }
      throw new IllegalArgumentException("Requests must have at least one element");
    }
    throw new IllegalArgumentException("Requests must not be null");
  }
  
  private void checkNotClosed()
  {
    if (!mClosed) {
      return;
    }
    throw new IllegalStateException("Session has been closed; further changes are illegal.");
  }
  
  private void checkRepeatingRequest(CaptureRequest paramCaptureRequest)
  {
    if (paramCaptureRequest != null)
    {
      if (!paramCaptureRequest.isReprocess()) {
        return;
      }
      throw new IllegalArgumentException("repeating reprocess requests are not supported");
    }
    throw new IllegalArgumentException("request must not be null");
  }
  
  private void checkRepeatingRequests(List<CaptureRequest> paramList)
  {
    if (paramList != null)
    {
      if (!paramList.isEmpty())
      {
        paramList = paramList.iterator();
        while (paramList.hasNext()) {
          if (((CaptureRequest)paramList.next()).isReprocess()) {
            throw new IllegalArgumentException("repeating reprocess burst requests are not supported");
          }
        }
        return;
      }
      throw new IllegalArgumentException("requests must have at least one element");
    }
    throw new IllegalArgumentException("requests must not be null");
  }
  
  private CameraDeviceImpl.CaptureCallback createCaptureCallbackProxy(Handler paramHandler, CameraCaptureSession.CaptureCallback paramCaptureCallback)
  {
    if (paramCaptureCallback != null) {
      paramHandler = CameraDeviceImpl.checkAndWrapHandler(paramHandler);
    } else {
      paramHandler = null;
    }
    return createCaptureCallbackProxyWithExecutor(paramHandler, paramCaptureCallback);
  }
  
  private CameraDeviceImpl.CaptureCallback createCaptureCallbackProxyWithExecutor(final Executor paramExecutor, final CameraCaptureSession.CaptureCallback paramCaptureCallback)
  {
    new CameraDeviceImpl.CaptureCallback()
    {
      public void onCaptureBufferLost(CameraDevice paramAnonymousCameraDevice, CaptureRequest paramAnonymousCaptureRequest, Surface paramAnonymousSurface, long paramAnonymousLong)
      {
        if ((paramCaptureCallback != null) && (paramExecutor != null))
        {
          long l = Binder.clearCallingIdentity();
          try
          {
            paramAnonymousCameraDevice = paramExecutor;
            CameraCaptureSession.CaptureCallback localCaptureCallback = paramCaptureCallback;
            _..Lambda.CameraCaptureSessionImpl.1.VuYVXvwmJMkbTnKaOD_h_DOjJpE localVuYVXvwmJMkbTnKaOD_h_DOjJpE = new android/hardware/camera2/impl/_$$Lambda$CameraCaptureSessionImpl$1$VuYVXvwmJMkbTnKaOD_h_DOjJpE;
            localVuYVXvwmJMkbTnKaOD_h_DOjJpE.<init>(this, localCaptureCallback, paramAnonymousCaptureRequest, paramAnonymousSurface, paramAnonymousLong);
            paramAnonymousCameraDevice.execute(localVuYVXvwmJMkbTnKaOD_h_DOjJpE);
            Binder.restoreCallingIdentity(l);
          }
          finally
          {
            Binder.restoreCallingIdentity(l);
          }
        }
      }
      
      public void onCaptureCompleted(CameraDevice paramAnonymousCameraDevice, CaptureRequest paramAnonymousCaptureRequest, TotalCaptureResult paramAnonymousTotalCaptureResult)
      {
        if ((paramCaptureCallback != null) && (paramExecutor != null))
        {
          long l = Binder.clearCallingIdentity();
          try
          {
            paramAnonymousCameraDevice = paramExecutor;
            CameraCaptureSession.CaptureCallback localCaptureCallback = paramCaptureCallback;
            _..Lambda.CameraCaptureSessionImpl.1.OA1Yz_YgzMO8qcV8esRjyt7ykp4 localOA1Yz_YgzMO8qcV8esRjyt7ykp4 = new android/hardware/camera2/impl/_$$Lambda$CameraCaptureSessionImpl$1$OA1Yz_YgzMO8qcV8esRjyt7ykp4;
            localOA1Yz_YgzMO8qcV8esRjyt7ykp4.<init>(this, localCaptureCallback, paramAnonymousCaptureRequest, paramAnonymousTotalCaptureResult);
            paramAnonymousCameraDevice.execute(localOA1Yz_YgzMO8qcV8esRjyt7ykp4);
            Binder.restoreCallingIdentity(l);
          }
          finally
          {
            Binder.restoreCallingIdentity(l);
          }
        }
      }
      
      public void onCaptureFailed(CameraDevice paramAnonymousCameraDevice, CaptureRequest paramAnonymousCaptureRequest, CaptureFailure paramAnonymousCaptureFailure)
      {
        if ((paramCaptureCallback != null) && (paramExecutor != null))
        {
          long l = Binder.clearCallingIdentity();
          try
          {
            paramAnonymousCameraDevice = paramExecutor;
            CameraCaptureSession.CaptureCallback localCaptureCallback = paramCaptureCallback;
            _..Lambda.CameraCaptureSessionImpl.1.VsKq1alEqL3XH_hLTWXgi7fSF3s localVsKq1alEqL3XH_hLTWXgi7fSF3s = new android/hardware/camera2/impl/_$$Lambda$CameraCaptureSessionImpl$1$VsKq1alEqL3XH_hLTWXgi7fSF3s;
            localVsKq1alEqL3XH_hLTWXgi7fSF3s.<init>(this, localCaptureCallback, paramAnonymousCaptureRequest, paramAnonymousCaptureFailure);
            paramAnonymousCameraDevice.execute(localVsKq1alEqL3XH_hLTWXgi7fSF3s);
            Binder.restoreCallingIdentity(l);
          }
          finally
          {
            Binder.restoreCallingIdentity(l);
          }
        }
      }
      
      public void onCapturePartial(CameraDevice paramAnonymousCameraDevice, CaptureRequest paramAnonymousCaptureRequest, CaptureResult paramAnonymousCaptureResult)
      {
        if ((paramCaptureCallback != null) && (paramExecutor != null))
        {
          long l = Binder.clearCallingIdentity();
          try
          {
            Executor localExecutor = paramExecutor;
            paramAnonymousCameraDevice = paramCaptureCallback;
            _..Lambda.CameraCaptureSessionImpl.1.HRzGZkXU2X5JDcudK0jcqdLZzV8 localHRzGZkXU2X5JDcudK0jcqdLZzV8 = new android/hardware/camera2/impl/_$$Lambda$CameraCaptureSessionImpl$1$HRzGZkXU2X5JDcudK0jcqdLZzV8;
            localHRzGZkXU2X5JDcudK0jcqdLZzV8.<init>(this, paramAnonymousCameraDevice, paramAnonymousCaptureRequest, paramAnonymousCaptureResult);
            localExecutor.execute(localHRzGZkXU2X5JDcudK0jcqdLZzV8);
            Binder.restoreCallingIdentity(l);
          }
          finally
          {
            Binder.restoreCallingIdentity(l);
          }
        }
      }
      
      public void onCaptureProgressed(CameraDevice paramAnonymousCameraDevice, CaptureRequest paramAnonymousCaptureRequest, CaptureResult paramAnonymousCaptureResult)
      {
        if ((paramCaptureCallback != null) && (paramExecutor != null))
        {
          long l = Binder.clearCallingIdentity();
          try
          {
            Executor localExecutor = paramExecutor;
            CameraCaptureSession.CaptureCallback localCaptureCallback = paramCaptureCallback;
            paramAnonymousCameraDevice = new android/hardware/camera2/impl/_$$Lambda$CameraCaptureSessionImpl$1$7mSdNTTAoYA0D3ITDxzDJKGykz0;
            paramAnonymousCameraDevice.<init>(this, localCaptureCallback, paramAnonymousCaptureRequest, paramAnonymousCaptureResult);
            localExecutor.execute(paramAnonymousCameraDevice);
            Binder.restoreCallingIdentity(l);
          }
          finally
          {
            Binder.restoreCallingIdentity(l);
          }
        }
      }
      
      public void onCaptureSequenceAborted(CameraDevice paramAnonymousCameraDevice, int paramAnonymousInt)
      {
        long l;
        if ((paramCaptureCallback != null) && (paramExecutor != null)) {
          l = Binder.clearCallingIdentity();
        }
        try
        {
          Executor localExecutor = paramExecutor;
          paramAnonymousCameraDevice = paramCaptureCallback;
          _..Lambda.CameraCaptureSessionImpl.1.TIJELOXvjSbPh6mpBLfBJ5ciNic localTIJELOXvjSbPh6mpBLfBJ5ciNic = new android/hardware/camera2/impl/_$$Lambda$CameraCaptureSessionImpl$1$TIJELOXvjSbPh6mpBLfBJ5ciNic;
          localTIJELOXvjSbPh6mpBLfBJ5ciNic.<init>(this, paramAnonymousCameraDevice, paramAnonymousInt);
          localExecutor.execute(localTIJELOXvjSbPh6mpBLfBJ5ciNic);
          Binder.restoreCallingIdentity(l);
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
      }
      
      public void onCaptureSequenceCompleted(CameraDevice paramAnonymousCameraDevice, int paramAnonymousInt, long paramAnonymousLong)
      {
        long l;
        if ((paramCaptureCallback != null) && (paramExecutor != null)) {
          l = Binder.clearCallingIdentity();
        }
        try
        {
          paramAnonymousCameraDevice = paramExecutor;
          CameraCaptureSession.CaptureCallback localCaptureCallback = paramCaptureCallback;
          _..Lambda.CameraCaptureSessionImpl.1.KZ4tthx5TnA5BizPVljsPqqdHck localKZ4tthx5TnA5BizPVljsPqqdHck = new android/hardware/camera2/impl/_$$Lambda$CameraCaptureSessionImpl$1$KZ4tthx5TnA5BizPVljsPqqdHck;
          localKZ4tthx5TnA5BizPVljsPqqdHck.<init>(this, localCaptureCallback, paramAnonymousInt, paramAnonymousLong);
          paramAnonymousCameraDevice.execute(localKZ4tthx5TnA5BizPVljsPqqdHck);
          Binder.restoreCallingIdentity(l);
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
      }
      
      public void onCaptureStarted(CameraDevice paramAnonymousCameraDevice, CaptureRequest paramAnonymousCaptureRequest, long paramAnonymousLong1, long paramAnonymousLong2)
      {
        if ((paramCaptureCallback != null) && (paramExecutor != null))
        {
          long l = Binder.clearCallingIdentity();
          try
          {
            Executor localExecutor = paramExecutor;
            CameraCaptureSession.CaptureCallback localCaptureCallback = paramCaptureCallback;
            paramAnonymousCameraDevice = new android/hardware/camera2/impl/_$$Lambda$CameraCaptureSessionImpl$1$uPVvNnGFdZcxxscdYQ5erNgaRWA;
            paramAnonymousCameraDevice.<init>(this, localCaptureCallback, paramAnonymousCaptureRequest, paramAnonymousLong1, paramAnonymousLong2);
            localExecutor.execute(paramAnonymousCameraDevice);
            Binder.restoreCallingIdentity(l);
          }
          finally
          {
            Binder.restoreCallingIdentity(l);
          }
        }
      }
    };
  }
  
  private CameraCaptureSession.StateCallback createUserStateCallbackProxy(Executor paramExecutor, CameraCaptureSession.StateCallback paramStateCallback)
  {
    return new CallbackProxies.SessionStateCallbackProxy(paramExecutor, paramStateCallback);
  }
  
  private void finishPendingSequence(int paramInt)
  {
    try
    {
      mSequenceDrainer.taskFinished(Integer.valueOf(paramInt));
    }
    catch (IllegalStateException localIllegalStateException)
    {
      Log.w("CameraCaptureSession", localIllegalStateException.getMessage());
    }
  }
  
  public void abortCaptures()
    throws CameraAccessException
  {
    synchronized (mDeviceImpl.mInterfaceLock)
    {
      checkNotClosed();
      if (mAborting)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append(mIdString);
        localStringBuilder.append("abortCaptures - Session is already aborting; doing nothing");
        Log.w("CameraCaptureSession", localStringBuilder.toString());
        return;
      }
      mAborting = true;
      mAbortDrainer.taskStarted();
      mDeviceImpl.flush();
      return;
    }
  }
  
  public int capture(CaptureRequest paramCaptureRequest, CameraCaptureSession.CaptureCallback paramCaptureCallback, Handler paramHandler)
    throws CameraAccessException
  {
    checkCaptureRequest(paramCaptureRequest);
    synchronized (mDeviceImpl.mInterfaceLock)
    {
      checkNotClosed();
      paramHandler = CameraDeviceImpl.checkHandler(paramHandler, paramCaptureCallback);
      int i = addPendingSequence(mDeviceImpl.capture(paramCaptureRequest, createCaptureCallbackProxy(paramHandler, paramCaptureCallback), mDeviceExecutor));
      return i;
    }
  }
  
  public int captureBurst(List<CaptureRequest> paramList, CameraCaptureSession.CaptureCallback paramCaptureCallback, Handler paramHandler)
    throws CameraAccessException
  {
    checkCaptureRequests(paramList);
    synchronized (mDeviceImpl.mInterfaceLock)
    {
      checkNotClosed();
      paramHandler = CameraDeviceImpl.checkHandler(paramHandler, paramCaptureCallback);
      int i = addPendingSequence(mDeviceImpl.captureBurst(paramList, createCaptureCallbackProxy(paramHandler, paramCaptureCallback), mDeviceExecutor));
      return i;
    }
  }
  
  public int captureBurstRequests(List<CaptureRequest> paramList, Executor paramExecutor, CameraCaptureSession.CaptureCallback paramCaptureCallback)
    throws CameraAccessException
  {
    if (paramExecutor != null)
    {
      if (paramCaptureCallback != null)
      {
        checkCaptureRequests(paramList);
        synchronized (mDeviceImpl.mInterfaceLock)
        {
          checkNotClosed();
          paramExecutor = CameraDeviceImpl.checkExecutor(paramExecutor, paramCaptureCallback);
          int i = addPendingSequence(mDeviceImpl.captureBurst(paramList, createCaptureCallbackProxyWithExecutor(paramExecutor, paramCaptureCallback), mDeviceExecutor));
          return i;
        }
      }
      throw new IllegalArgumentException("callback must not be null");
    }
    throw new IllegalArgumentException("executor must not be null");
  }
  
  public int captureSingleRequest(CaptureRequest paramCaptureRequest, Executor paramExecutor, CameraCaptureSession.CaptureCallback paramCaptureCallback)
    throws CameraAccessException
  {
    if (paramExecutor != null)
    {
      if (paramCaptureCallback != null)
      {
        checkCaptureRequest(paramCaptureRequest);
        synchronized (mDeviceImpl.mInterfaceLock)
        {
          checkNotClosed();
          paramExecutor = CameraDeviceImpl.checkExecutor(paramExecutor, paramCaptureCallback);
          int i = addPendingSequence(mDeviceImpl.capture(paramCaptureRequest, createCaptureCallbackProxyWithExecutor(paramExecutor, paramCaptureCallback), mDeviceExecutor));
          return i;
        }
      }
      throw new IllegalArgumentException("callback must not be null");
    }
    throw new IllegalArgumentException("executor must not be null");
  }
  
  public void close()
  {
    synchronized (mDeviceImpl.mInterfaceLock)
    {
      if (mClosed) {
        return;
      }
      mClosed = true;
      try
      {
        try
        {
          mDeviceImpl.stopRepeating();
        }
        catch (CameraAccessException localCameraAccessException)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append(mIdString);
          localStringBuilder.append("Exception while stopping repeating: ");
          Log.e("CameraCaptureSession", localStringBuilder.toString(), localCameraAccessException);
        }
        mSequenceDrainer.beginDrain();
        if (mInput != null) {
          mInput.release();
        }
        return;
      }
      catch (IllegalStateException localIllegalStateException)
      {
        mStateCallback.onClosed(this);
        return;
      }
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public void finalizeOutputConfigurations(List<OutputConfiguration> paramList)
    throws CameraAccessException
  {
    mDeviceImpl.finalizeOutputConfigs(paramList);
  }
  
  public CameraDevice getDevice()
  {
    return mDeviceImpl;
  }
  
  public CameraDeviceImpl.StateCallbackKK getDeviceStateCallback()
  {
    new CameraDeviceImpl.StateCallbackKK()
    {
      private boolean mActive = false;
      private boolean mBusy = false;
      
      public void onActive(CameraDevice paramAnonymousCameraDevice)
      {
        mIdleDrainer.taskStarted();
        mActive = true;
        mStateCallback.onActive(jdField_this);
      }
      
      public void onBusy(CameraDevice paramAnonymousCameraDevice)
      {
        mBusy = true;
      }
      
      public void onDisconnected(CameraDevice paramAnonymousCameraDevice)
      {
        close();
      }
      
      public void onError(CameraDevice paramAnonymousCameraDevice, int paramAnonymousInt)
      {
        paramAnonymousCameraDevice = new StringBuilder();
        paramAnonymousCameraDevice.append(mIdString);
        paramAnonymousCameraDevice.append("Got device error ");
        paramAnonymousCameraDevice.append(paramAnonymousInt);
        Log.wtf("CameraCaptureSession", paramAnonymousCameraDevice.toString());
      }
      
      public void onIdle(CameraDevice arg1)
      {
        synchronized (val$interfaceLock)
        {
          boolean bool = mAborting;
          if ((mBusy) && (bool))
          {
            mAbortDrainer.taskFinished();
            synchronized (val$interfaceLock)
            {
              CameraCaptureSessionImpl.access$702(CameraCaptureSessionImpl.this, false);
            }
          }
          if (mActive) {
            mIdleDrainer.taskFinished();
          }
          mBusy = false;
          mActive = false;
          mStateCallback.onReady(jdField_this);
          return;
        }
      }
      
      public void onOpened(CameraDevice paramAnonymousCameraDevice)
      {
        throw new AssertionError("Camera must already be open before creating a session");
      }
      
      public void onRequestQueueEmpty()
      {
        mStateCallback.onCaptureQueueEmpty(jdField_this);
      }
      
      public void onSurfacePrepared(Surface paramAnonymousSurface)
      {
        mStateCallback.onSurfacePrepared(jdField_this, paramAnonymousSurface);
      }
      
      public void onUnconfigured(CameraDevice paramAnonymousCameraDevice) {}
    };
  }
  
  public Surface getInputSurface()
  {
    return mInput;
  }
  
  public boolean isAborting()
  {
    return mAborting;
  }
  
  public boolean isReprocessable()
  {
    boolean bool;
    if (mInput != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void prepare(int paramInt, Surface paramSurface)
    throws CameraAccessException
  {
    mDeviceImpl.prepare(paramInt, paramSurface);
  }
  
  public void prepare(Surface paramSurface)
    throws CameraAccessException
  {
    mDeviceImpl.prepare(paramSurface);
  }
  
  public void replaceSessionClose()
  {
    synchronized (mDeviceImpl.mInterfaceLock)
    {
      mSkipUnconfigure = true;
      close();
      return;
    }
  }
  
  public int setRepeatingBurst(List<CaptureRequest> paramList, CameraCaptureSession.CaptureCallback paramCaptureCallback, Handler paramHandler)
    throws CameraAccessException
  {
    checkRepeatingRequests(paramList);
    synchronized (mDeviceImpl.mInterfaceLock)
    {
      checkNotClosed();
      paramHandler = CameraDeviceImpl.checkHandler(paramHandler, paramCaptureCallback);
      int i = addPendingSequence(mDeviceImpl.setRepeatingBurst(paramList, createCaptureCallbackProxy(paramHandler, paramCaptureCallback), mDeviceExecutor));
      return i;
    }
  }
  
  public int setRepeatingBurstRequests(List<CaptureRequest> paramList, Executor paramExecutor, CameraCaptureSession.CaptureCallback paramCaptureCallback)
    throws CameraAccessException
  {
    if (paramExecutor != null)
    {
      if (paramCaptureCallback != null)
      {
        checkRepeatingRequests(paramList);
        synchronized (mDeviceImpl.mInterfaceLock)
        {
          checkNotClosed();
          paramExecutor = CameraDeviceImpl.checkExecutor(paramExecutor, paramCaptureCallback);
          int i = addPendingSequence(mDeviceImpl.setRepeatingBurst(paramList, createCaptureCallbackProxyWithExecutor(paramExecutor, paramCaptureCallback), mDeviceExecutor));
          return i;
        }
      }
      throw new IllegalArgumentException("callback must not be null");
    }
    throw new IllegalArgumentException("executor must not be null");
  }
  
  public int setRepeatingRequest(CaptureRequest paramCaptureRequest, CameraCaptureSession.CaptureCallback paramCaptureCallback, Handler paramHandler)
    throws CameraAccessException
  {
    checkRepeatingRequest(paramCaptureRequest);
    synchronized (mDeviceImpl.mInterfaceLock)
    {
      checkNotClosed();
      paramHandler = CameraDeviceImpl.checkHandler(paramHandler, paramCaptureCallback);
      int i = addPendingSequence(mDeviceImpl.setRepeatingRequest(paramCaptureRequest, createCaptureCallbackProxy(paramHandler, paramCaptureCallback), mDeviceExecutor));
      return i;
    }
  }
  
  public int setSingleRepeatingRequest(CaptureRequest paramCaptureRequest, Executor paramExecutor, CameraCaptureSession.CaptureCallback paramCaptureCallback)
    throws CameraAccessException
  {
    if (paramExecutor != null)
    {
      if (paramCaptureCallback != null)
      {
        checkRepeatingRequest(paramCaptureRequest);
        synchronized (mDeviceImpl.mInterfaceLock)
        {
          checkNotClosed();
          paramExecutor = CameraDeviceImpl.checkExecutor(paramExecutor, paramCaptureCallback);
          int i = addPendingSequence(mDeviceImpl.setRepeatingRequest(paramCaptureRequest, createCaptureCallbackProxyWithExecutor(paramExecutor, paramCaptureCallback), mDeviceExecutor));
          return i;
        }
      }
      throw new IllegalArgumentException("callback must not be null");
    }
    throw new IllegalArgumentException("executor must not be null");
  }
  
  public void stopRepeating()
    throws CameraAccessException
  {
    synchronized (mDeviceImpl.mInterfaceLock)
    {
      checkNotClosed();
      mDeviceImpl.stopRepeating();
      return;
    }
  }
  
  public void tearDown(Surface paramSurface)
    throws CameraAccessException
  {
    mDeviceImpl.tearDown(paramSurface);
  }
  
  public void updateOutputConfiguration(OutputConfiguration paramOutputConfiguration)
    throws CameraAccessException
  {
    synchronized (mDeviceImpl.mInterfaceLock)
    {
      checkNotClosed();
      mDeviceImpl.updateOutputConfiguration(paramOutputConfiguration);
      return;
    }
  }
  
  private class AbortDrainListener
    implements TaskDrainer.DrainListener
  {
    private AbortDrainListener() {}
    
    public void onDrained()
    {
      synchronized (mDeviceImpl.mInterfaceLock)
      {
        if (mSkipUnconfigure) {
          return;
        }
        mIdleDrainer.beginDrain();
        return;
      }
    }
  }
  
  private class IdleDrainListener
    implements TaskDrainer.DrainListener
  {
    private IdleDrainListener() {}
    
    public void onDrained()
    {
      synchronized (mDeviceImpl.mInterfaceLock)
      {
        if (mSkipUnconfigure) {
          return;
        }
        try
        {
          mDeviceImpl.configureStreamsChecked(null, null, 0, null);
        }
        catch (IllegalStateException localIllegalStateException) {}catch (CameraAccessException localCameraAccessException)
        {
          for (;;)
          {
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append(mIdString);
            localStringBuilder.append("Exception while unconfiguring outputs: ");
            Log.e("CameraCaptureSession", localStringBuilder.toString(), localCameraAccessException);
          }
        }
        return;
      }
    }
  }
  
  private class SequenceDrainListener
    implements TaskDrainer.DrainListener
  {
    private SequenceDrainListener() {}
    
    public void onDrained()
    {
      mStateCallback.onClosed(CameraCaptureSessionImpl.this);
      if (mSkipUnconfigure) {
        return;
      }
      mAbortDrainer.beginDrain();
    }
  }
}
