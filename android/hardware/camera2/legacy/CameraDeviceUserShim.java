package android.hardware.camera2.legacy;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.ICameraDeviceCallbacks;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CaptureResultExtras;
import android.hardware.camera2.impl.PhysicalCaptureResultInfo;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.utils.SubmitInfo;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.system.OsConstants;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;

public class CameraDeviceUserShim
  implements ICameraDeviceUser
{
  private static final boolean DEBUG = false;
  private static final int OPEN_CAMERA_TIMEOUT_MS = 5000;
  private static final String TAG = "CameraDeviceUserShim";
  private final CameraCallbackThread mCameraCallbacks;
  private final CameraCharacteristics mCameraCharacteristics;
  private final CameraLooper mCameraInit;
  private final Object mConfigureLock = new Object();
  private boolean mConfiguring;
  private final LegacyCameraDevice mLegacyDevice;
  private int mSurfaceIdCounter;
  private final SparseArray<Surface> mSurfaces;
  
  protected CameraDeviceUserShim(int paramInt, LegacyCameraDevice paramLegacyCameraDevice, CameraCharacteristics paramCameraCharacteristics, CameraLooper paramCameraLooper, CameraCallbackThread paramCameraCallbackThread)
  {
    mLegacyDevice = paramLegacyCameraDevice;
    mConfiguring = false;
    mSurfaces = new SparseArray();
    mCameraCharacteristics = paramCameraCharacteristics;
    mCameraInit = paramCameraLooper;
    mCameraCallbacks = paramCameraCallbackThread;
    mSurfaceIdCounter = 0;
  }
  
  public static CameraDeviceUserShim connectBinderShim(ICameraDeviceCallbacks paramICameraDeviceCallbacks, int paramInt)
  {
    Object localObject1 = new CameraLooper(paramInt);
    CameraCallbackThread localCameraCallbackThread = new CameraCallbackThread(paramICameraDeviceCallbacks);
    int i = ((CameraLooper)localObject1).waitForOpen(5000);
    paramICameraDeviceCallbacks = ((CameraLooper)localObject1).getCamera();
    LegacyExceptionUtils.throwOnServiceError(i);
    paramICameraDeviceCallbacks.disableShutterSound();
    Camera.CameraInfo localCameraInfo = new Camera.CameraInfo();
    Camera.getCameraInfo(paramInt, localCameraInfo);
    try
    {
      Object localObject2 = paramICameraDeviceCallbacks.getParameters();
      localObject2 = LegacyMetadataMapper.createCharacteristics((Camera.Parameters)localObject2, localCameraInfo);
      return new CameraDeviceUserShim(paramInt, new LegacyCameraDevice(paramInt, paramICameraDeviceCallbacks, (CameraCharacteristics)localObject2, localCameraCallbackThread), (CameraCharacteristics)localObject2, (CameraLooper)localObject1, localCameraCallbackThread);
    }
    catch (RuntimeException paramICameraDeviceCallbacks)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Unable to get initial parameters: ");
      ((StringBuilder)localObject1).append(paramICameraDeviceCallbacks.getMessage());
      throw new ServiceSpecificException(10, ((StringBuilder)localObject1).toString());
    }
  }
  
  private static int translateErrorsFromCamera1(int paramInt)
  {
    if (paramInt == -OsConstants.EACCES) {
      return 1;
    }
    return paramInt;
  }
  
  public IBinder asBinder()
  {
    return null;
  }
  
  public void beginConfigure()
  {
    if (!mLegacyDevice.isClosed()) {
      synchronized (mConfigureLock)
      {
        if (!mConfiguring)
        {
          mConfiguring = true;
          return;
        }
        Log.e("CameraDeviceUserShim", "Cannot begin configure, configuration change already in progress.");
        ServiceSpecificException localServiceSpecificException = new android/os/ServiceSpecificException;
        localServiceSpecificException.<init>(10, "Cannot begin configure, configuration change already in progress.");
        throw localServiceSpecificException;
      }
    }
    Log.e("CameraDeviceUserShim", "Cannot begin configure, device has been closed.");
    throw new ServiceSpecificException(4, "Cannot begin configure, device has been closed.");
  }
  
  public long cancelRequest(int paramInt)
  {
    if (!mLegacyDevice.isClosed()) {
      synchronized (mConfigureLock)
      {
        if (!mConfiguring) {
          return mLegacyDevice.cancelRequest(paramInt);
        }
        Log.e("CameraDeviceUserShim", "Cannot cancel request, configuration change in progress.");
        ServiceSpecificException localServiceSpecificException = new android/os/ServiceSpecificException;
        localServiceSpecificException.<init>(10, "Cannot cancel request, configuration change in progress.");
        throw localServiceSpecificException;
      }
    }
    Log.e("CameraDeviceUserShim", "Cannot cancel request, device has been closed.");
    throw new ServiceSpecificException(4, "Cannot cancel request, device has been closed.");
  }
  
  public CameraMetadataNative createDefaultRequest(int paramInt)
  {
    if (!mLegacyDevice.isClosed()) {
      try
      {
        CameraMetadataNative localCameraMetadataNative = LegacyMetadataMapper.createRequestTemplate(mCameraCharacteristics, paramInt);
        return localCameraMetadataNative;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Log.e("CameraDeviceUserShim", "createDefaultRequest - invalid templateId specified");
        throw new ServiceSpecificException(3, "createDefaultRequest - invalid templateId specified");
      }
    }
    Log.e("CameraDeviceUserShim", "Cannot create default request, device has been closed.");
    throw new ServiceSpecificException(4, "Cannot create default request, device has been closed.");
  }
  
  public int createInputStream(int paramInt1, int paramInt2, int paramInt3)
  {
    Log.e("CameraDeviceUserShim", "Creating input stream is not supported on legacy devices");
    throw new ServiceSpecificException(10, "Creating input stream is not supported on legacy devices");
  }
  
  public int createStream(OutputConfiguration paramOutputConfiguration)
  {
    if (!mLegacyDevice.isClosed()) {
      synchronized (mConfigureLock)
      {
        if (mConfiguring)
        {
          if (paramOutputConfiguration.getRotation() == 0)
          {
            int i = mSurfaceIdCounter + 1;
            mSurfaceIdCounter = i;
            mSurfaces.put(i, paramOutputConfiguration.getSurface());
            return i;
          }
          Log.e("CameraDeviceUserShim", "Cannot create stream, stream rotation is not supported.");
          paramOutputConfiguration = new android/os/ServiceSpecificException;
          paramOutputConfiguration.<init>(3, "Cannot create stream, stream rotation is not supported.");
          throw paramOutputConfiguration;
        }
        Log.e("CameraDeviceUserShim", "Cannot create stream, beginConfigure hasn't been called yet.");
        paramOutputConfiguration = new android/os/ServiceSpecificException;
        paramOutputConfiguration.<init>(10, "Cannot create stream, beginConfigure hasn't been called yet.");
        throw paramOutputConfiguration;
      }
    }
    Log.e("CameraDeviceUserShim", "Cannot create stream, device has been closed.");
    throw new ServiceSpecificException(4, "Cannot create stream, device has been closed.");
  }
  
  public void deleteStream(int paramInt)
  {
    if (!mLegacyDevice.isClosed()) {
      synchronized (mConfigureLock)
      {
        if (mConfiguring)
        {
          int i = mSurfaces.indexOfKey(paramInt);
          if (i >= 0)
          {
            mSurfaces.removeAt(i);
            return;
          }
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Cannot delete stream, stream id ");
          ((StringBuilder)localObject2).append(paramInt);
          ((StringBuilder)localObject2).append(" doesn't exist.");
          String str = ((StringBuilder)localObject2).toString();
          Log.e("CameraDeviceUserShim", str);
          localObject2 = new android/os/ServiceSpecificException;
          ((ServiceSpecificException)localObject2).<init>(3, str);
          throw ((Throwable)localObject2);
        }
        Log.e("CameraDeviceUserShim", "Cannot delete stream, no configuration change in progress.");
        Object localObject2 = new android/os/ServiceSpecificException;
        ((ServiceSpecificException)localObject2).<init>(10, "Cannot delete stream, no configuration change in progress.");
        throw ((Throwable)localObject2);
      }
    }
    Log.e("CameraDeviceUserShim", "Cannot delete stream, device has been closed.");
    throw new ServiceSpecificException(4, "Cannot delete stream, device has been closed.");
  }
  
  public void disconnect()
  {
    if (mLegacyDevice.isClosed()) {
      Log.w("CameraDeviceUserShim", "Cannot disconnect, device has already been closed.");
    }
    try
    {
      mLegacyDevice.close();
      return;
    }
    finally
    {
      mCameraInit.close();
      mCameraCallbacks.close();
    }
  }
  
  public void endConfigure(int paramInt, CameraMetadataNative arg2)
  {
    if (!mLegacyDevice.isClosed())
    {
      if (paramInt == 0)
      {
        ??? = null;
        synchronized (mConfigureLock)
        {
          if (mConfiguring)
          {
            if (mSurfaces != null) {
              ??? = mSurfaces.clone();
            }
            mConfiguring = false;
            mLegacyDevice.configureOutputs(???);
            return;
          }
          Log.e("CameraDeviceUserShim", "Cannot end configure, no configuration change in progress.");
          ??? = new android/os/ServiceSpecificException;
          ???.<init>(10, "Cannot end configure, no configuration change in progress.");
          throw ???;
        }
      }
      Log.e("CameraDeviceUserShim", "LEGACY devices do not support this operating mode");
    }
    synchronized (mConfigureLock)
    {
      mConfiguring = false;
      throw new ServiceSpecificException(3, "LEGACY devices do not support this operating mode");
    }
  }
  
  public void finalizeOutputConfigurations(int paramInt, OutputConfiguration paramOutputConfiguration)
  {
    Log.e("CameraDeviceUserShim", "Finalizing output configuration is not supported on legacy devices");
    throw new ServiceSpecificException(10, "Finalizing output configuration is not supported on legacy devices");
  }
  
  public long flush()
  {
    if (!mLegacyDevice.isClosed()) {
      synchronized (mConfigureLock)
      {
        if (!mConfiguring) {
          return mLegacyDevice.flush();
        }
        Log.e("CameraDeviceUserShim", "Cannot flush, configuration change in progress.");
        ServiceSpecificException localServiceSpecificException = new android/os/ServiceSpecificException;
        localServiceSpecificException.<init>(10, "Cannot flush, configuration change in progress.");
        throw localServiceSpecificException;
      }
    }
    Log.e("CameraDeviceUserShim", "Cannot flush, device has been closed.");
    throw new ServiceSpecificException(4, "Cannot flush, device has been closed.");
  }
  
  public CameraMetadataNative getCameraInfo()
  {
    Log.e("CameraDeviceUserShim", "getCameraInfo unimplemented.");
    return null;
  }
  
  public Surface getInputSurface()
  {
    Log.e("CameraDeviceUserShim", "Getting input surface is not supported on legacy devices");
    throw new ServiceSpecificException(10, "Getting input surface is not supported on legacy devices");
  }
  
  public void prepare(int paramInt)
  {
    if (!mLegacyDevice.isClosed())
    {
      mCameraCallbacks.onPrepared(paramInt);
      return;
    }
    Log.e("CameraDeviceUserShim", "Cannot prepare stream, device has been closed.");
    throw new ServiceSpecificException(4, "Cannot prepare stream, device has been closed.");
  }
  
  public void prepare2(int paramInt1, int paramInt2)
  {
    prepare(paramInt2);
  }
  
  public SubmitInfo submitRequest(CaptureRequest paramCaptureRequest, boolean paramBoolean)
  {
    if (!mLegacyDevice.isClosed()) {
      synchronized (mConfigureLock)
      {
        if (!mConfiguring) {
          return mLegacyDevice.submitRequest(paramCaptureRequest, paramBoolean);
        }
        Log.e("CameraDeviceUserShim", "Cannot submit request, configuration change in progress.");
        paramCaptureRequest = new android/os/ServiceSpecificException;
        paramCaptureRequest.<init>(10, "Cannot submit request, configuration change in progress.");
        throw paramCaptureRequest;
      }
    }
    Log.e("CameraDeviceUserShim", "Cannot submit request, device has been closed.");
    throw new ServiceSpecificException(4, "Cannot submit request, device has been closed.");
  }
  
  public SubmitInfo submitRequestList(CaptureRequest[] paramArrayOfCaptureRequest, boolean paramBoolean)
  {
    if (!mLegacyDevice.isClosed()) {
      synchronized (mConfigureLock)
      {
        if (!mConfiguring) {
          return mLegacyDevice.submitRequestList(paramArrayOfCaptureRequest, paramBoolean);
        }
        Log.e("CameraDeviceUserShim", "Cannot submit request, configuration change in progress.");
        paramArrayOfCaptureRequest = new android/os/ServiceSpecificException;
        paramArrayOfCaptureRequest.<init>(10, "Cannot submit request, configuration change in progress.");
        throw paramArrayOfCaptureRequest;
      }
    }
    Log.e("CameraDeviceUserShim", "Cannot submit request list, device has been closed.");
    throw new ServiceSpecificException(4, "Cannot submit request list, device has been closed.");
  }
  
  public void tearDown(int paramInt)
  {
    if (!mLegacyDevice.isClosed()) {
      return;
    }
    Log.e("CameraDeviceUserShim", "Cannot tear down stream, device has been closed.");
    throw new ServiceSpecificException(4, "Cannot tear down stream, device has been closed.");
  }
  
  public void updateOutputConfiguration(int paramInt, OutputConfiguration paramOutputConfiguration) {}
  
  public void waitUntilIdle()
    throws RemoteException
  {
    if (!mLegacyDevice.isClosed()) {
      synchronized (mConfigureLock)
      {
        if (!mConfiguring)
        {
          mLegacyDevice.waitUntilIdle();
          return;
        }
        Log.e("CameraDeviceUserShim", "Cannot wait until idle, configuration change in progress.");
        ServiceSpecificException localServiceSpecificException = new android/os/ServiceSpecificException;
        localServiceSpecificException.<init>(10, "Cannot wait until idle, configuration change in progress.");
        throw localServiceSpecificException;
      }
    }
    Log.e("CameraDeviceUserShim", "Cannot wait until idle, device has been closed.");
    throw new ServiceSpecificException(4, "Cannot wait until idle, device has been closed.");
  }
  
  private static class CameraCallbackThread
    implements ICameraDeviceCallbacks
  {
    private static final int CAMERA_ERROR = 0;
    private static final int CAMERA_IDLE = 1;
    private static final int CAPTURE_STARTED = 2;
    private static final int PREPARED = 4;
    private static final int REPEATING_REQUEST_ERROR = 5;
    private static final int REQUEST_QUEUE_EMPTY = 6;
    private static final int RESULT_RECEIVED = 3;
    private final ICameraDeviceCallbacks mCallbacks;
    private Handler mHandler;
    private final HandlerThread mHandlerThread;
    
    public CameraCallbackThread(ICameraDeviceCallbacks paramICameraDeviceCallbacks)
    {
      mCallbacks = paramICameraDeviceCallbacks;
      mHandlerThread = new HandlerThread("LegacyCameraCallback");
      mHandlerThread.start();
    }
    
    private Handler getHandler()
    {
      if (mHandler == null) {
        mHandler = new CallbackHandler(mHandlerThread.getLooper());
      }
      return mHandler;
    }
    
    public IBinder asBinder()
    {
      return null;
    }
    
    public void close()
    {
      mHandlerThread.quitSafely();
    }
    
    public void onCaptureStarted(CaptureResultExtras paramCaptureResultExtras, long paramLong)
    {
      paramCaptureResultExtras = getHandler().obtainMessage(2, (int)(paramLong & 0xFFFFFFFF), (int)(0xFFFFFFFF & paramLong >> 32), paramCaptureResultExtras);
      getHandler().sendMessage(paramCaptureResultExtras);
    }
    
    public void onDeviceError(int paramInt, CaptureResultExtras paramCaptureResultExtras)
    {
      paramCaptureResultExtras = getHandler().obtainMessage(0, paramInt, 0, paramCaptureResultExtras);
      getHandler().sendMessage(paramCaptureResultExtras);
    }
    
    public void onDeviceIdle()
    {
      Message localMessage = getHandler().obtainMessage(1);
      getHandler().sendMessage(localMessage);
    }
    
    public void onPrepared(int paramInt)
    {
      Message localMessage = getHandler().obtainMessage(4, paramInt, 0);
      getHandler().sendMessage(localMessage);
    }
    
    public void onRepeatingRequestError(long paramLong, int paramInt)
    {
      Message localMessage = getHandler().obtainMessage(5, new Object[] { Long.valueOf(paramLong), Integer.valueOf(paramInt) });
      getHandler().sendMessage(localMessage);
    }
    
    public void onRequestQueueEmpty()
    {
      Message localMessage = getHandler().obtainMessage(6, 0, 0);
      getHandler().sendMessage(localMessage);
    }
    
    public void onResultReceived(CameraMetadataNative paramCameraMetadataNative, CaptureResultExtras paramCaptureResultExtras, PhysicalCaptureResultInfo[] paramArrayOfPhysicalCaptureResultInfo)
    {
      paramCameraMetadataNative = getHandler().obtainMessage(3, new Object[] { paramCameraMetadataNative, paramCaptureResultExtras });
      getHandler().sendMessage(paramCameraMetadataNative);
    }
    
    private class CallbackHandler
      extends Handler
    {
      public CallbackHandler(Looper paramLooper)
      {
        super();
      }
      
      public void handleMessage(Message paramMessage)
      {
        try
        {
          long l1;
          int i;
          switch (what)
          {
          default: 
            localObject1 = new java/lang/IllegalArgumentException;
            break;
          case 6: 
            mCallbacks.onRequestQueueEmpty();
            break;
          case 5: 
            localObject1 = (Object[])obj;
            l1 = ((Long)localObject1[0]).longValue();
            i = ((Integer)localObject1[1]).intValue();
            mCallbacks.onRepeatingRequestError(l1, i);
            break;
          case 4: 
            i = arg1;
            mCallbacks.onPrepared(i);
            break;
          case 3: 
            localObject2 = (Object[])obj;
            localObject1 = (CameraMetadataNative)localObject2[0];
            localObject2 = (CaptureResultExtras)localObject2[1];
            mCallbacks.onResultReceived((CameraMetadataNative)localObject1, (CaptureResultExtras)localObject2, new PhysicalCaptureResultInfo[0]);
            break;
          case 2: 
            l1 = arg2;
            long l2 = arg1;
            localObject1 = (CaptureResultExtras)obj;
            mCallbacks.onCaptureStarted((CaptureResultExtras)localObject1, (l1 & 0xFFFFFFFF) << 32 | 0xFFFFFFFF & l2);
            break;
          case 1: 
            mCallbacks.onDeviceIdle();
            break;
          case 0: 
            i = arg1;
            localObject1 = (CaptureResultExtras)obj;
            mCallbacks.onDeviceError(i, (CaptureResultExtras)localObject1);
          }
          return;
          Object localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Unknown callback message ");
          ((StringBuilder)localObject2).append(what);
          ((IllegalArgumentException)localObject1).<init>(((StringBuilder)localObject2).toString());
          throw ((Throwable)localObject1);
        }
        catch (RemoteException localRemoteException)
        {
          Object localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Received remote exception during camera callback ");
          ((StringBuilder)localObject1).append(what);
          throw new IllegalStateException(((StringBuilder)localObject1).toString(), localRemoteException);
        }
      }
    }
  }
  
  private static class CameraLooper
    implements Runnable, AutoCloseable
  {
    private final Camera mCamera = Camera.openUninitialized();
    private final int mCameraId;
    private volatile int mInitErrors;
    private Looper mLooper;
    private final ConditionVariable mStartDone = new ConditionVariable();
    private final Thread mThread;
    
    public CameraLooper(int paramInt)
    {
      mCameraId = paramInt;
      mThread = new Thread(this);
      mThread.start();
    }
    
    public void close()
    {
      if (mLooper == null) {
        return;
      }
      mLooper.quitSafely();
      try
      {
        mThread.join();
        mLooper = null;
        return;
      }
      catch (InterruptedException localInterruptedException)
      {
        throw new AssertionError(localInterruptedException);
      }
    }
    
    public Camera getCamera()
    {
      return mCamera;
    }
    
    public void run()
    {
      Looper.prepare();
      mLooper = Looper.myLooper();
      mInitErrors = mCamera.cameraInitUnspecified(mCameraId);
      mStartDone.open();
      Looper.loop();
    }
    
    public int waitForOpen(int paramInt)
    {
      if (mStartDone.block(paramInt)) {
        return mInitErrors;
      }
      Log.e("CameraDeviceUserShim", "waitForOpen - Camera failed to open after timeout of 5000 ms");
      try
      {
        mCamera.release();
      }
      catch (RuntimeException localRuntimeException)
      {
        Log.e("CameraDeviceUserShim", "connectBinderShim - Failed to release camera after timeout ", localRuntimeException);
      }
      throw new ServiceSpecificException(10);
    }
  }
}
