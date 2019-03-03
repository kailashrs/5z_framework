package android.hardware.camera2.impl;

import android.app.ActivityThread;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession.StateCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.ICameraDeviceCallbacks.Stub;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.utils.SubmitInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.os.SystemProperties;
import android.text.TextUtils.SimpleStringSplitter;
import android.text.TextUtils.StringSplitter;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.util.SparseArray;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public class CameraDeviceImpl
  extends CameraDevice
  implements IBinder.DeathRecipient
{
  private static final long NANO_PER_SECOND = 1000000000L;
  private static final int REQUEST_ID_NONE = -1;
  private final boolean DEBUG = false;
  private final String TAG;
  private int customOpMode = 0;
  private final int mAppTargetSdkVersion;
  private final Runnable mCallOnActive = new Runnable()
  {
    public void run()
    {
      synchronized (mInterfaceLock)
      {
        if (mRemoteDevice == null) {
          return;
        }
        CameraDeviceImpl.StateCallbackKK localStateCallbackKK = mSessionStateCallback;
        if (localStateCallbackKK != null) {
          localStateCallbackKK.onActive(CameraDeviceImpl.this);
        }
        return;
      }
    }
  };
  private final Runnable mCallOnBusy = new Runnable()
  {
    public void run()
    {
      synchronized (mInterfaceLock)
      {
        if (mRemoteDevice == null) {
          return;
        }
        CameraDeviceImpl.StateCallbackKK localStateCallbackKK = mSessionStateCallback;
        if (localStateCallbackKK != null) {
          localStateCallbackKK.onBusy(CameraDeviceImpl.this);
        }
        return;
      }
    }
  };
  private final Runnable mCallOnClosed = new Runnable()
  {
    private boolean mClosedOnce = false;
    
    public void run()
    {
      if (!mClosedOnce) {
        synchronized (mInterfaceLock)
        {
          CameraDeviceImpl.StateCallbackKK localStateCallbackKK = mSessionStateCallback;
          if (localStateCallbackKK != null) {
            localStateCallbackKK.onClosed(CameraDeviceImpl.this);
          }
          mDeviceCallback.onClosed(CameraDeviceImpl.this);
          mClosedOnce = true;
          return;
        }
      }
      throw new AssertionError("Don't post #onClosed more than once");
    }
  };
  private final Runnable mCallOnDisconnected = new Runnable()
  {
    public void run()
    {
      synchronized (mInterfaceLock)
      {
        if (mRemoteDevice == null) {
          return;
        }
        CameraDeviceImpl.StateCallbackKK localStateCallbackKK = mSessionStateCallback;
        if (localStateCallbackKK != null) {
          localStateCallbackKK.onDisconnected(CameraDeviceImpl.this);
        }
        mDeviceCallback.onDisconnected(CameraDeviceImpl.this);
        return;
      }
    }
  };
  private final Runnable mCallOnIdle = new Runnable()
  {
    public void run()
    {
      synchronized (mInterfaceLock)
      {
        if (mRemoteDevice == null) {
          return;
        }
        CameraDeviceImpl.StateCallbackKK localStateCallbackKK = mSessionStateCallback;
        if (localStateCallbackKK != null) {
          localStateCallbackKK.onIdle(CameraDeviceImpl.this);
        }
        return;
      }
    }
  };
  private final Runnable mCallOnOpened = new Runnable()
  {
    public void run()
    {
      synchronized (mInterfaceLock)
      {
        if (mRemoteDevice == null) {
          return;
        }
        CameraDeviceImpl.StateCallbackKK localStateCallbackKK = mSessionStateCallback;
        if (localStateCallbackKK != null) {
          localStateCallbackKK.onOpened(CameraDeviceImpl.this);
        }
        mDeviceCallback.onOpened(CameraDeviceImpl.this);
        return;
      }
    }
  };
  private final Runnable mCallOnUnconfigured = new Runnable()
  {
    public void run()
    {
      synchronized (mInterfaceLock)
      {
        if (mRemoteDevice == null) {
          return;
        }
        CameraDeviceImpl.StateCallbackKK localStateCallbackKK = mSessionStateCallback;
        if (localStateCallbackKK != null) {
          localStateCallbackKK.onUnconfigured(CameraDeviceImpl.this);
        }
        return;
      }
    }
  };
  private final CameraDeviceCallbacks mCallbacks = new CameraDeviceCallbacks();
  private final String mCameraId;
  private final SparseArray<CaptureCallbackHolder> mCaptureCallbackMap = new SparseArray();
  private final CameraCharacteristics mCharacteristics;
  private final AtomicBoolean mClosing = new AtomicBoolean();
  private AbstractMap.SimpleEntry<Integer, InputConfiguration> mConfiguredInput = new AbstractMap.SimpleEntry(Integer.valueOf(-1), null);
  private final SparseArray<OutputConfiguration> mConfiguredOutputs = new SparseArray();
  private CameraCaptureSessionCore mCurrentSession;
  private final CameraDevice.StateCallback mDeviceCallback;
  private final Executor mDeviceExecutor;
  private final FrameNumberTracker mFrameNumberTracker = new FrameNumberTracker();
  private boolean mIdle = true;
  private boolean mInError = false;
  final Object mInterfaceLock = new Object();
  private boolean mIsPrivilegedApp = false;
  private int mNextSessionId = 0;
  private ICameraDeviceUserWrapper mRemoteDevice;
  private int mRepeatingRequestId = -1;
  private final List<RequestLastFrameNumbersHolder> mRequestLastFrameNumbersList = new ArrayList();
  private volatile StateCallbackKK mSessionStateCallback;
  private final int mTotalPartialCount;
  
  public CameraDeviceImpl(String paramString, CameraDevice.StateCallback paramStateCallback, Executor paramExecutor, CameraCharacteristics paramCameraCharacteristics, int paramInt)
  {
    if ((paramString != null) && (paramStateCallback != null) && (paramExecutor != null) && (paramCameraCharacteristics != null))
    {
      mCameraId = paramString;
      mDeviceCallback = paramStateCallback;
      mDeviceExecutor = paramExecutor;
      mCharacteristics = paramCameraCharacteristics;
      mAppTargetSdkVersion = paramInt;
      paramStateCallback = String.format("CameraDevice-JV-%s", new Object[] { mCameraId });
      paramString = paramStateCallback;
      if (paramStateCallback.length() > 23) {
        paramString = paramStateCallback.substring(0, 23);
      }
      TAG = paramString;
      paramString = (Integer)mCharacteristics.get(CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT);
      if (paramString == null) {
        mTotalPartialCount = 1;
      } else {
        mTotalPartialCount = paramString.intValue();
      }
      mIsPrivilegedApp = checkPrivilegedAppList();
      return;
    }
    throw new IllegalArgumentException("Null argument given");
  }
  
  private void checkAndFireSequenceComplete()
  {
    long l1 = mFrameNumberTracker.getCompletedFrameNumber();
    long l2 = mFrameNumberTracker.getCompletedReprocessFrameNumber();
    Iterator localIterator = mRequestLastFrameNumbersList.iterator();
    if (localIterator.hasNext())
    {
      final RequestLastFrameNumbersHolder localRequestLastFrameNumbersHolder = (RequestLastFrameNumbersHolder)localIterator.next();
      int i = 0;
      final int j = localRequestLastFrameNumbersHolder.getRequestId();
      Object localObject6;
      int k;
      long l4;
      synchronized (mInterfaceLock)
      {
        localObject6 = mRemoteDevice;
        if (localObject6 == null) {
          try
          {
            Log.w(TAG, "Camera closed while checking sequences");
            return;
          }
          finally
          {
            break label264;
          }
        }
        k = mCaptureCallbackMap.indexOfKey(j);
        if (k >= 0) {
          localObject6 = (CaptureCallbackHolder)mCaptureCallbackMap.valueAt(k);
        } else {
          localObject6 = null;
        }
        if (localObject6 != null)
        {
          long l3 = localRequestLastFrameNumbersHolder.getLastRegularFrameNumber();
          l4 = localRequestLastFrameNumbersHolder.getLastReprocessFrameNumber();
          if ((l3 <= l1) && (l4 <= l2)) {
            i = 1;
          }
        }
      }
      try
      {
        mCaptureCallbackMap.removeAt(k);
        if ((localObject6 == null) || (i != 0)) {
          localObject1.remove();
        }
        if (i != 0)
        {
          ??? = new Runnable()
          {
            public void run()
            {
              if (!CameraDeviceImpl.this.isClosed()) {
                val$holder.getCallback().onCaptureSequenceCompleted(CameraDeviceImpl.this, j, localRequestLastFrameNumbersHolder.getLastFrameNumber());
              }
            }
          };
          l4 = Binder.clearCallingIdentity();
        }
        try
        {
          ((CaptureCallbackHolder)localObject6).getExecutor().execute((Runnable)???);
          Binder.restoreCallingIdentity(l4);
        }
        finally
        {
          Binder.restoreCallingIdentity(l4);
        }
        localObject3 = finally;
      }
      finally
      {
        label264:
        for (;;) {}
      }
      throw localObject3;
    }
  }
  
  public static Executor checkAndWrapHandler(Handler paramHandler)
  {
    return new CameraHandlerExecutor(checkHandler(paramHandler));
  }
  
  private void checkEarlyTriggerSequenceComplete(final int paramInt, long paramLong)
  {
    if (paramLong == -1L)
    {
      int i = mCaptureCallbackMap.indexOfKey(paramInt);
      final CaptureCallbackHolder localCaptureCallbackHolder;
      if (i >= 0) {
        localCaptureCallbackHolder = (CaptureCallbackHolder)mCaptureCallbackMap.valueAt(i);
      } else {
        localCaptureCallbackHolder = null;
      }
      if (localCaptureCallbackHolder != null) {
        mCaptureCallbackMap.removeAt(i);
      }
      Runnable local9;
      if (localCaptureCallbackHolder != null)
      {
        local9 = new Runnable()
        {
          public void run()
          {
            if (!CameraDeviceImpl.this.isClosed()) {
              localCaptureCallbackHolder.getCallback().onCaptureSequenceAborted(CameraDeviceImpl.this, paramInt);
            }
          }
        };
        paramLong = Binder.clearCallingIdentity();
      }
      try
      {
        localCaptureCallbackHolder.getExecutor().execute(local9);
        Binder.restoreCallingIdentity(paramLong);
      }
      finally
      {
        Binder.restoreCallingIdentity(paramLong);
      }
      tmp118_115[0] = Integer.valueOf(paramInt);
      Log.w(TAG, String.format("did not register callback to request %d", tmp118_115));
    }
    else
    {
      mRequestLastFrameNumbersList.add(new RequestLastFrameNumbersHolder(paramInt, paramLong));
      checkAndFireSequenceComplete();
    }
  }
  
  static Executor checkExecutor(Executor paramExecutor)
  {
    if (paramExecutor == null) {
      paramExecutor = checkAndWrapHandler(null);
    }
    return paramExecutor;
  }
  
  public static <T> Executor checkExecutor(Executor paramExecutor, T paramT)
  {
    if (paramT != null) {
      paramExecutor = checkExecutor(paramExecutor);
    }
    return paramExecutor;
  }
  
  static Handler checkHandler(Handler paramHandler)
  {
    Handler localHandler = paramHandler;
    if (paramHandler == null)
    {
      paramHandler = Looper.myLooper();
      if (paramHandler != null) {
        localHandler = new Handler(paramHandler);
      } else {
        throw new IllegalArgumentException("No handler given, and current thread has no looper!");
      }
    }
    return localHandler;
  }
  
  static <T> Handler checkHandler(Handler paramHandler, T paramT)
  {
    if (paramT != null) {
      return checkHandler(paramHandler);
    }
    return paramHandler;
  }
  
  private void checkIfCameraClosedOrInError()
    throws CameraAccessException
  {
    if (mRemoteDevice != null)
    {
      if (!mInError) {
        return;
      }
      throw new CameraAccessException(3, "The camera device has encountered a serious error");
    }
    throw new IllegalStateException("CameraDevice was already closed");
  }
  
  private void checkInputConfiguration(InputConfiguration paramInputConfiguration)
  {
    if (paramInputConfiguration != null)
    {
      Object localObject1 = (StreamConfigurationMap)mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
      if (isPrivilegedApp())
      {
        Log.w(TAG, "ignore input format/size check for white listed app");
        return;
      }
      Object localObject2 = ((StreamConfigurationMap)localObject1).getInputFormats();
      int i = localObject2.length;
      int j = 0;
      int k = 0;
      for (int m = 0; m < i; m++) {
        if (localObject2[m] == paramInputConfiguration.getFormat()) {
          k = 1;
        }
      }
      if (k != 0)
      {
        k = 0;
        localObject1 = ((StreamConfigurationMap)localObject1).getInputSizes(paramInputConfiguration.getFormat());
        i = localObject1.length;
        m = j;
        while (m < i)
        {
          localObject2 = localObject1[m];
          j = k;
          if (paramInputConfiguration.getWidth() == ((Size)localObject2).getWidth())
          {
            j = k;
            if (paramInputConfiguration.getHeight() == ((Size)localObject2).getHeight()) {
              j = 1;
            }
          }
          m++;
          k = j;
        }
        if (k == 0)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("input size ");
          ((StringBuilder)localObject2).append(paramInputConfiguration.getWidth());
          ((StringBuilder)localObject2).append("x");
          ((StringBuilder)localObject2).append(paramInputConfiguration.getHeight());
          ((StringBuilder)localObject2).append(" is not valid");
          throw new IllegalArgumentException(((StringBuilder)localObject2).toString());
        }
      }
      else
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("input format ");
        ((StringBuilder)localObject2).append(paramInputConfiguration.getFormat());
        ((StringBuilder)localObject2).append(" is not valid");
        throw new IllegalArgumentException(((StringBuilder)localObject2).toString());
      }
    }
  }
  
  private boolean checkPrivilegedAppList()
  {
    String str = ActivityThread.currentOpPackageName();
    Object localObject = SystemProperties.get("persist.vendor.camera.privapp.list");
    if (((String)localObject).length() > 0)
    {
      TextUtils.SimpleStringSplitter localSimpleStringSplitter = new TextUtils.SimpleStringSplitter(',');
      localSimpleStringSplitter.setString((String)localObject);
      localObject = localSimpleStringSplitter.iterator();
      while (((Iterator)localObject).hasNext()) {
        if (str.equals((String)((Iterator)localObject).next())) {
          return true;
        }
      }
    }
    return false;
  }
  
  private void createCaptureSessionInternal(InputConfiguration paramInputConfiguration, List<OutputConfiguration> paramList, CameraCaptureSession.StateCallback paramStateCallback, Executor paramExecutor, int paramInt, CaptureRequest paramCaptureRequest)
    throws CameraAccessException
  {
    int i;
    Object localObject2;
    boolean bool;
    synchronized (mInterfaceLock)
    {
      checkIfCameraClosedOrInError();
      if (paramInt == 1) {
        i = 1;
      } else {
        i = 0;
      }
      if ((i != 0) && (paramInputConfiguration != null))
      {
        paramInputConfiguration = new java/lang/IllegalArgumentException;
        paramInputConfiguration.<init>("Constrained high speed session doesn't support input configuration yet.");
        throw paramInputConfiguration;
      }
      if (mCurrentSession != null) {
        mCurrentSession.replaceSessionClose();
      }
      localObject2 = null;
    }
    throw paramInputConfiguration;
  }
  
  private CameraCharacteristics getCharacteristics()
  {
    return mCharacteristics;
  }
  
  private boolean isClosed()
  {
    return mClosing.get();
  }
  
  private void overrideEnableZsl(CameraMetadataNative paramCameraMetadataNative, boolean paramBoolean)
  {
    if ((Boolean)paramCameraMetadataNative.get(CaptureRequest.CONTROL_ENABLE_ZSL) == null) {
      return;
    }
    paramCameraMetadataNative.set(CaptureRequest.CONTROL_ENABLE_ZSL, Boolean.valueOf(paramBoolean));
  }
  
  private int submitCaptureRequest(List<CaptureRequest> paramList, CaptureCallback paramCaptureCallback, Executor paramExecutor, boolean paramBoolean)
    throws CameraAccessException
  {
    paramExecutor = checkExecutor(paramExecutor, paramCaptureCallback);
    ??? = paramList.iterator();
    Object localObject2;
    Object localObject3;
    int i;
    Object localObject4;
    while (((Iterator)???).hasNext())
    {
      localObject2 = (CaptureRequest)((Iterator)???).next();
      if (!((CaptureRequest)localObject2).getTargets().isEmpty())
      {
        localObject3 = ((CaptureRequest)localObject2).getTargets().iterator();
        while (((Iterator)localObject3).hasNext())
        {
          Surface localSurface = (Surface)((Iterator)localObject3).next();
          if (localSurface != null) {
            for (i = 0; i < mConfiguredOutputs.size(); i++)
            {
              localObject4 = (OutputConfiguration)mConfiguredOutputs.valueAt(i);
              if ((((OutputConfiguration)localObject4).isForPhysicalCamera()) && (((OutputConfiguration)localObject4).getSurfaces().contains(localSurface)) && (((CaptureRequest)localObject2).isReprocess())) {
                throw new IllegalArgumentException("Reprocess request on physical stream is not allowed");
              }
            }
          } else {
            throw new IllegalArgumentException("Null Surface targets are not allowed");
          }
        }
      }
      else
      {
        throw new IllegalArgumentException("Each request must have at least one Surface target");
      }
    }
    synchronized (mInterfaceLock)
    {
      checkIfCameraClosedOrInError();
      if (paramBoolean) {
        stopRepeating();
      }
      localObject2 = (CaptureRequest[])paramList.toArray(new CaptureRequest[paramList.size()]);
      int j = localObject2.length;
      for (i = 0; i < j; i++) {
        localObject2[i].convertSurfaceToStreamId(mConfiguredOutputs);
      }
      localObject3 = mRemoteDevice.submitRequestList((CaptureRequest[])localObject2, paramBoolean);
      j = localObject2.length;
      for (i = 0; i < j; i++) {
        localObject2[i].recoverStreamIdToSurface();
      }
      if (paramCaptureCallback != null)
      {
        localObject4 = mCaptureCallbackMap;
        i = ((SubmitInfo)localObject3).getRequestId();
        localObject2 = new android/hardware/camera2/impl/CameraDeviceImpl$CaptureCallbackHolder;
        ((CaptureCallbackHolder)localObject2).<init>(paramCaptureCallback, paramList, paramExecutor, paramBoolean, mNextSessionId - 1);
        ((SparseArray)localObject4).put(i, localObject2);
      }
      if (paramBoolean)
      {
        if (mRepeatingRequestId != -1) {
          checkEarlyTriggerSequenceComplete(mRepeatingRequestId, ((SubmitInfo)localObject3).getLastFrameNumber());
        }
        mRepeatingRequestId = ((SubmitInfo)localObject3).getRequestId();
      }
      else
      {
        paramExecutor = mRequestLastFrameNumbersList;
        paramCaptureCallback = new android/hardware/camera2/impl/CameraDeviceImpl$RequestLastFrameNumbersHolder;
        paramCaptureCallback.<init>(paramList, (SubmitInfo)localObject3);
        paramExecutor.add(paramCaptureCallback);
      }
      if (mIdle) {
        mDeviceExecutor.execute(mCallOnActive);
      }
      mIdle = false;
      i = ((SubmitInfo)localObject3).getRequestId();
      return i;
    }
  }
  
  private void waitUntilIdle()
    throws CameraAccessException
  {
    synchronized (mInterfaceLock)
    {
      checkIfCameraClosedOrInError();
      if (mRepeatingRequestId == -1)
      {
        mRemoteDevice.waitUntilIdle();
        return;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      localIllegalStateException.<init>("Active repeating request ongoing");
      throw localIllegalStateException;
    }
  }
  
  public void binderDied()
  {
    String str = TAG;
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("CameraDevice ");
    ((StringBuilder)localObject1).append(mCameraId);
    ((StringBuilder)localObject1).append(" died unexpectedly");
    Log.w(str, ((StringBuilder)localObject1).toString());
    if (mRemoteDevice == null) {
      return;
    }
    mInError = true;
    localObject1 = new Runnable()
    {
      public void run()
      {
        if (!CameraDeviceImpl.this.isClosed()) {
          mDeviceCallback.onError(CameraDeviceImpl.this, 5);
        }
      }
    };
    long l = Binder.clearCallingIdentity();
    try
    {
      mDeviceExecutor.execute((Runnable)localObject1);
      return;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public int capture(CaptureRequest paramCaptureRequest, CaptureCallback paramCaptureCallback, Executor paramExecutor)
    throws CameraAccessException
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(paramCaptureRequest);
    return submitCaptureRequest(localArrayList, paramCaptureCallback, paramExecutor, false);
  }
  
  public int captureBurst(List<CaptureRequest> paramList, CaptureCallback paramCaptureCallback, Executor paramExecutor)
    throws CameraAccessException
  {
    if ((paramList != null) && (!paramList.isEmpty())) {
      return submitCaptureRequest(paramList, paramCaptureCallback, paramExecutor, false);
    }
    throw new IllegalArgumentException("At least one request must be given");
  }
  
  public void close()
  {
    synchronized (mInterfaceLock)
    {
      if (mClosing.getAndSet(true)) {
        return;
      }
      if (mRemoteDevice != null)
      {
        mRemoteDevice.disconnect();
        mRemoteDevice.unlinkToDeath(this, 0);
      }
      if ((mRemoteDevice != null) || (mInError)) {
        mDeviceExecutor.execute(mCallOnClosed);
      }
      mRemoteDevice = null;
      return;
    }
  }
  
  public void configureOutputs(List<Surface> paramList)
    throws CameraAccessException
  {
    ArrayList localArrayList = new ArrayList(paramList.size());
    paramList = paramList.iterator();
    while (paramList.hasNext()) {
      localArrayList.add(new OutputConfiguration((Surface)paramList.next()));
    }
    configureStreamsChecked(null, localArrayList, 0, null);
  }
  
  /* Error */
  public boolean configureStreamsChecked(InputConfiguration paramInputConfiguration, List<OutputConfiguration> paramList, int paramInt, CaptureRequest paramCaptureRequest)
    throws CameraAccessException
  {
    // Byte code:
    //   0: aload_2
    //   1: astore 5
    //   3: aload_2
    //   4: ifnonnull +12 -> 16
    //   7: new 170	java/util/ArrayList
    //   10: dup
    //   11: invokespecial 171	java/util/ArrayList:<init>	()V
    //   14: astore 5
    //   16: aload 5
    //   18: invokeinterface 555 1 0
    //   23: ifne +21 -> 44
    //   26: aload_1
    //   27: ifnonnull +6 -> 33
    //   30: goto +14 -> 44
    //   33: new 250	java/lang/IllegalArgumentException
    //   36: dup
    //   37: ldc_w 722
    //   40: invokespecial 255	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   43: athrow
    //   44: aload_0
    //   45: aload_1
    //   46: invokespecial 724	android/hardware/camera2/impl/CameraDeviceImpl:checkInputConfiguration	(Landroid/hardware/camera2/params/InputConfiguration;)V
    //   49: aload_0
    //   50: getfield 132	android/hardware/camera2/impl/CameraDeviceImpl:mInterfaceLock	Ljava/lang/Object;
    //   53: astore_2
    //   54: aload_2
    //   55: monitorenter
    //   56: aload_0
    //   57: invokespecial 535	android/hardware/camera2/impl/CameraDeviceImpl:checkIfCameraClosedOrInError	()V
    //   60: new 726	java/util/HashSet
    //   63: astore 6
    //   65: aload 6
    //   67: aload 5
    //   69: invokespecial 729	java/util/HashSet:<init>	(Ljava/util/Collection;)V
    //   72: new 170	java/util/ArrayList
    //   75: astore 7
    //   77: aload 7
    //   79: invokespecial 171	java/util/ArrayList:<init>	()V
    //   82: iconst_0
    //   83: istore 8
    //   85: iload 8
    //   87: aload_0
    //   88: getfield 168	android/hardware/camera2/impl/CameraDeviceImpl:mConfiguredOutputs	Landroid/util/SparseArray;
    //   91: invokevirtual 625	android/util/SparseArray:size	()I
    //   94: if_icmpge +81 -> 175
    //   97: aload_0
    //   98: getfield 168	android/hardware/camera2/impl/CameraDeviceImpl:mConfiguredOutputs	Landroid/util/SparseArray;
    //   101: iload 8
    //   103: invokevirtual 732	android/util/SparseArray:keyAt	(I)I
    //   106: istore 9
    //   108: aload_0
    //   109: getfield 168	android/hardware/camera2/impl/CameraDeviceImpl:mConfiguredOutputs	Landroid/util/SparseArray;
    //   112: iload 8
    //   114: invokevirtual 353	android/util/SparseArray:valueAt	(I)Ljava/lang/Object;
    //   117: checkcast 559	android/hardware/camera2/params/OutputConfiguration
    //   120: astore 10
    //   122: aload 5
    //   124: aload 10
    //   126: invokeinterface 635 2 0
    //   131: ifeq +25 -> 156
    //   134: aload 10
    //   136: invokevirtual 735	android/hardware/camera2/params/OutputConfiguration:isDeferredConfiguration	()Z
    //   139: ifeq +6 -> 145
    //   142: goto +14 -> 156
    //   145: aload 6
    //   147: aload 10
    //   149: invokevirtual 737	java/util/HashSet:remove	(Ljava/lang/Object;)Z
    //   152: pop
    //   153: goto +16 -> 169
    //   156: aload 7
    //   158: iload 9
    //   160: invokestatic 161	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   163: invokeinterface 410 2 0
    //   168: pop
    //   169: iinc 8 1
    //   172: goto -87 -> 85
    //   175: aload_0
    //   176: getfield 207	android/hardware/camera2/impl/CameraDeviceImpl:mDeviceExecutor	Ljava/util/concurrent/Executor;
    //   179: aload_0
    //   180: getfield 192	android/hardware/camera2/impl/CameraDeviceImpl:mCallOnBusy	Ljava/lang/Runnable;
    //   183: invokeinterface 384 2 0
    //   188: aload_0
    //   189: invokevirtual 647	android/hardware/camera2/impl/CameraDeviceImpl:stopRepeating	()V
    //   192: aload_0
    //   193: invokespecial 738	android/hardware/camera2/impl/CameraDeviceImpl:waitUntilIdle	()V
    //   196: aload_0
    //   197: getfield 260	android/hardware/camera2/impl/CameraDeviceImpl:mRemoteDevice	Landroid/hardware/camera2/impl/ICameraDeviceUserWrapper;
    //   200: invokevirtual 741	android/hardware/camera2/impl/ICameraDeviceUserWrapper:beginConfigure	()V
    //   203: aload_0
    //   204: getfield 166	android/hardware/camera2/impl/CameraDeviceImpl:mConfiguredInput	Ljava/util/AbstractMap$SimpleEntry;
    //   207: invokevirtual 744	java/util/AbstractMap$SimpleEntry:getValue	()Ljava/lang/Object;
    //   210: checkcast 466	android/hardware/camera2/params/InputConfiguration
    //   213: astore 10
    //   215: aload_1
    //   216: aload 10
    //   218: if_acmpeq +109 -> 327
    //   221: aload_1
    //   222: ifnull +12 -> 234
    //   225: aload_1
    //   226: aload 10
    //   228: invokevirtual 745	android/hardware/camera2/params/InputConfiguration:equals	(Ljava/lang/Object;)Z
    //   231: ifne +96 -> 327
    //   234: aload 10
    //   236: ifnull +44 -> 280
    //   239: aload_0
    //   240: getfield 260	android/hardware/camera2/impl/CameraDeviceImpl:mRemoteDevice	Landroid/hardware/camera2/impl/ICameraDeviceUserWrapper;
    //   243: aload_0
    //   244: getfield 166	android/hardware/camera2/impl/CameraDeviceImpl:mConfiguredInput	Ljava/util/AbstractMap$SimpleEntry;
    //   247: invokevirtual 748	java/util/AbstractMap$SimpleEntry:getKey	()Ljava/lang/Object;
    //   250: checkcast 157	java/lang/Integer
    //   253: invokevirtual 244	java/lang/Integer:intValue	()I
    //   256: invokevirtual 751	android/hardware/camera2/impl/ICameraDeviceUserWrapper:deleteStream	(I)V
    //   259: new 155	java/util/AbstractMap$SimpleEntry
    //   262: astore 10
    //   264: aload 10
    //   266: iconst_m1
    //   267: invokestatic 161	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   270: aconst_null
    //   271: invokespecial 164	java/util/AbstractMap$SimpleEntry:<init>	(Ljava/lang/Object;Ljava/lang/Object;)V
    //   274: aload_0
    //   275: aload 10
    //   277: putfield 166	android/hardware/camera2/impl/CameraDeviceImpl:mConfiguredInput	Ljava/util/AbstractMap$SimpleEntry;
    //   280: aload_1
    //   281: ifnull +46 -> 327
    //   284: aload_0
    //   285: getfield 260	android/hardware/camera2/impl/CameraDeviceImpl:mRemoteDevice	Landroid/hardware/camera2/impl/ICameraDeviceUserWrapper;
    //   288: aload_1
    //   289: invokevirtual 476	android/hardware/camera2/params/InputConfiguration:getWidth	()I
    //   292: aload_1
    //   293: invokevirtual 482	android/hardware/camera2/params/InputConfiguration:getHeight	()I
    //   296: aload_1
    //   297: invokevirtual 469	android/hardware/camera2/params/InputConfiguration:getFormat	()I
    //   300: invokevirtual 755	android/hardware/camera2/impl/ICameraDeviceUserWrapper:createInputStream	(III)I
    //   303: istore 8
    //   305: new 155	java/util/AbstractMap$SimpleEntry
    //   308: astore 10
    //   310: aload 10
    //   312: iload 8
    //   314: invokestatic 161	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   317: aload_1
    //   318: invokespecial 164	java/util/AbstractMap$SimpleEntry:<init>	(Ljava/lang/Object;Ljava/lang/Object;)V
    //   321: aload_0
    //   322: aload 10
    //   324: putfield 166	android/hardware/camera2/impl/CameraDeviceImpl:mConfiguredInput	Ljava/util/AbstractMap$SimpleEntry;
    //   327: aload 7
    //   329: invokeinterface 325 1 0
    //   334: astore 7
    //   336: aload 7
    //   338: invokeinterface 330 1 0
    //   343: ifeq +39 -> 382
    //   346: aload 7
    //   348: invokeinterface 334 1 0
    //   353: checkcast 157	java/lang/Integer
    //   356: astore_1
    //   357: aload_0
    //   358: getfield 260	android/hardware/camera2/impl/CameraDeviceImpl:mRemoteDevice	Landroid/hardware/camera2/impl/ICameraDeviceUserWrapper;
    //   361: aload_1
    //   362: invokevirtual 244	java/lang/Integer:intValue	()I
    //   365: invokevirtual 751	android/hardware/camera2/impl/ICameraDeviceUserWrapper:deleteStream	(I)V
    //   368: aload_0
    //   369: getfield 168	android/hardware/camera2/impl/CameraDeviceImpl:mConfiguredOutputs	Landroid/util/SparseArray;
    //   372: aload_1
    //   373: invokevirtual 244	java/lang/Integer:intValue	()I
    //   376: invokevirtual 758	android/util/SparseArray:delete	(I)V
    //   379: goto -43 -> 336
    //   382: aload 5
    //   384: invokeinterface 325 1 0
    //   389: astore_1
    //   390: aload_1
    //   391: invokeinterface 330 1 0
    //   396: ifeq +49 -> 445
    //   399: aload_1
    //   400: invokeinterface 334 1 0
    //   405: checkcast 559	android/hardware/camera2/params/OutputConfiguration
    //   408: astore 7
    //   410: aload 6
    //   412: aload 7
    //   414: invokevirtual 759	java/util/HashSet:contains	(Ljava/lang/Object;)Z
    //   417: ifeq +25 -> 442
    //   420: aload_0
    //   421: getfield 260	android/hardware/camera2/impl/CameraDeviceImpl:mRemoteDevice	Landroid/hardware/camera2/impl/ICameraDeviceUserWrapper;
    //   424: aload 7
    //   426: invokevirtual 763	android/hardware/camera2/impl/ICameraDeviceUserWrapper:createStream	(Landroid/hardware/camera2/params/OutputConfiguration;)I
    //   429: istore 8
    //   431: aload_0
    //   432: getfield 168	android/hardware/camera2/impl/CameraDeviceImpl:mConfiguredOutputs	Landroid/util/SparseArray;
    //   435: iload 8
    //   437: aload 7
    //   439: invokevirtual 674	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   442: goto -52 -> 390
    //   445: iload_3
    //   446: aload_0
    //   447: getfield 127	android/hardware/camera2/impl/CameraDeviceImpl:customOpMode	I
    //   450: bipush 16
    //   452: ishl
    //   453: ior
    //   454: istore_3
    //   455: aload 4
    //   457: ifnull +19 -> 476
    //   460: aload_0
    //   461: getfield 260	android/hardware/camera2/impl/CameraDeviceImpl:mRemoteDevice	Landroid/hardware/camera2/impl/ICameraDeviceUserWrapper;
    //   464: iload_3
    //   465: aload 4
    //   467: invokevirtual 767	android/hardware/camera2/CaptureRequest:getNativeCopy	()Landroid/hardware/camera2/impl/CameraMetadataNative;
    //   470: invokevirtual 771	android/hardware/camera2/impl/ICameraDeviceUserWrapper:endConfigure	(ILandroid/hardware/camera2/impl/CameraMetadataNative;)V
    //   473: goto +12 -> 485
    //   476: aload_0
    //   477: getfield 260	android/hardware/camera2/impl/CameraDeviceImpl:mRemoteDevice	Landroid/hardware/camera2/impl/ICameraDeviceUserWrapper;
    //   480: iload_3
    //   481: aconst_null
    //   482: invokevirtual 771	android/hardware/camera2/impl/ICameraDeviceUserWrapper:endConfigure	(ILandroid/hardware/camera2/impl/CameraMetadataNative;)V
    //   485: iconst_1
    //   486: ifeq +29 -> 515
    //   489: aload 5
    //   491: invokeinterface 555 1 0
    //   496: ifle +19 -> 515
    //   499: aload_0
    //   500: getfield 207	android/hardware/camera2/impl/CameraDeviceImpl:mDeviceExecutor	Ljava/util/concurrent/Executor;
    //   503: aload_0
    //   504: getfield 198	android/hardware/camera2/impl/CameraDeviceImpl:mCallOnIdle	Ljava/lang/Runnable;
    //   507: invokeinterface 384 2 0
    //   512: goto +16 -> 528
    //   515: aload_0
    //   516: getfield 207	android/hardware/camera2/impl/CameraDeviceImpl:mDeviceExecutor	Ljava/util/concurrent/Executor;
    //   519: aload_0
    //   520: getfield 186	android/hardware/camera2/impl/CameraDeviceImpl:mCallOnUnconfigured	Ljava/lang/Runnable;
    //   523: invokeinterface 384 2 0
    //   528: aload_2
    //   529: monitorexit
    //   530: iconst_1
    //   531: ireturn
    //   532: astore_1
    //   533: goto +126 -> 659
    //   536: astore 4
    //   538: aload 4
    //   540: invokevirtual 774	android/hardware/camera2/CameraAccessException:getReason	()I
    //   543: iconst_4
    //   544: if_icmpne +18 -> 562
    //   547: new 444	java/lang/IllegalStateException
    //   550: astore_1
    //   551: aload_1
    //   552: ldc_w 776
    //   555: aload 4
    //   557: invokespecial 779	java/lang/IllegalStateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   560: aload_1
    //   561: athrow
    //   562: aload 4
    //   564: athrow
    //   565: astore_1
    //   566: aload_0
    //   567: getfield 229	android/hardware/camera2/impl/CameraDeviceImpl:TAG	Ljava/lang/String;
    //   570: astore 4
    //   572: new 485	java/lang/StringBuilder
    //   575: astore 6
    //   577: aload 6
    //   579: invokespecial 486	java/lang/StringBuilder:<init>	()V
    //   582: aload 6
    //   584: ldc_w 781
    //   587: invokevirtual 492	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   590: pop
    //   591: aload 6
    //   593: aload_1
    //   594: invokevirtual 784	java/lang/IllegalArgumentException:getMessage	()Ljava/lang/String;
    //   597: invokevirtual 492	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   600: pop
    //   601: aload 4
    //   603: aload 6
    //   605: invokevirtual 503	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   608: invokestatic 345	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   611: pop
    //   612: iconst_0
    //   613: ifeq +29 -> 642
    //   616: aload 5
    //   618: invokeinterface 555 1 0
    //   623: ifle +19 -> 642
    //   626: aload_0
    //   627: getfield 207	android/hardware/camera2/impl/CameraDeviceImpl:mDeviceExecutor	Ljava/util/concurrent/Executor;
    //   630: aload_0
    //   631: getfield 198	android/hardware/camera2/impl/CameraDeviceImpl:mCallOnIdle	Ljava/lang/Runnable;
    //   634: invokeinterface 384 2 0
    //   639: goto +16 -> 655
    //   642: aload_0
    //   643: getfield 207	android/hardware/camera2/impl/CameraDeviceImpl:mDeviceExecutor	Ljava/util/concurrent/Executor;
    //   646: aload_0
    //   647: getfield 186	android/hardware/camera2/impl/CameraDeviceImpl:mCallOnUnconfigured	Ljava/lang/Runnable;
    //   650: invokeinterface 384 2 0
    //   655: aload_2
    //   656: monitorexit
    //   657: iconst_0
    //   658: ireturn
    //   659: iconst_0
    //   660: ifeq +29 -> 689
    //   663: aload 5
    //   665: invokeinterface 555 1 0
    //   670: ifle +19 -> 689
    //   673: aload_0
    //   674: getfield 207	android/hardware/camera2/impl/CameraDeviceImpl:mDeviceExecutor	Ljava/util/concurrent/Executor;
    //   677: aload_0
    //   678: getfield 198	android/hardware/camera2/impl/CameraDeviceImpl:mCallOnIdle	Ljava/lang/Runnable;
    //   681: invokeinterface 384 2 0
    //   686: goto +16 -> 702
    //   689: aload_0
    //   690: getfield 207	android/hardware/camera2/impl/CameraDeviceImpl:mDeviceExecutor	Ljava/util/concurrent/Executor;
    //   693: aload_0
    //   694: getfield 186	android/hardware/camera2/impl/CameraDeviceImpl:mCallOnUnconfigured	Ljava/lang/Runnable;
    //   697: invokeinterface 384 2 0
    //   702: aload_1
    //   703: athrow
    //   704: astore_1
    //   705: aload_2
    //   706: monitorexit
    //   707: aload_1
    //   708: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	709	0	this	CameraDeviceImpl
    //   0	709	1	paramInputConfiguration	InputConfiguration
    //   0	709	2	paramList	List<OutputConfiguration>
    //   0	709	3	paramInt	int
    //   0	709	4	paramCaptureRequest	CaptureRequest
    //   1	663	5	localObject1	Object
    //   63	541	6	localObject2	Object
    //   75	363	7	localObject3	Object
    //   83	353	8	i	int
    //   106	53	9	j	int
    //   120	203	10	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   192	215	532	finally
    //   225	234	532	finally
    //   239	280	532	finally
    //   284	327	532	finally
    //   327	336	532	finally
    //   336	379	532	finally
    //   382	390	532	finally
    //   390	442	532	finally
    //   445	455	532	finally
    //   460	473	532	finally
    //   476	485	532	finally
    //   538	562	532	finally
    //   562	565	532	finally
    //   566	612	532	finally
    //   192	215	536	android/hardware/camera2/CameraAccessException
    //   225	234	536	android/hardware/camera2/CameraAccessException
    //   239	280	536	android/hardware/camera2/CameraAccessException
    //   284	327	536	android/hardware/camera2/CameraAccessException
    //   327	336	536	android/hardware/camera2/CameraAccessException
    //   336	379	536	android/hardware/camera2/CameraAccessException
    //   382	390	536	android/hardware/camera2/CameraAccessException
    //   390	442	536	android/hardware/camera2/CameraAccessException
    //   445	455	536	android/hardware/camera2/CameraAccessException
    //   460	473	536	android/hardware/camera2/CameraAccessException
    //   476	485	536	android/hardware/camera2/CameraAccessException
    //   192	215	565	java/lang/IllegalArgumentException
    //   225	234	565	java/lang/IllegalArgumentException
    //   239	280	565	java/lang/IllegalArgumentException
    //   284	327	565	java/lang/IllegalArgumentException
    //   327	336	565	java/lang/IllegalArgumentException
    //   336	379	565	java/lang/IllegalArgumentException
    //   382	390	565	java/lang/IllegalArgumentException
    //   390	442	565	java/lang/IllegalArgumentException
    //   445	455	565	java/lang/IllegalArgumentException
    //   460	473	565	java/lang/IllegalArgumentException
    //   476	485	565	java/lang/IllegalArgumentException
    //   56	82	704	finally
    //   85	142	704	finally
    //   145	153	704	finally
    //   156	169	704	finally
    //   175	192	704	finally
    //   489	512	704	finally
    //   515	528	704	finally
    //   528	530	704	finally
    //   616	639	704	finally
    //   642	655	704	finally
    //   655	657	704	finally
    //   663	686	704	finally
    //   689	702	704	finally
    //   702	704	704	finally
    //   705	707	704	finally
  }
  
  public CaptureRequest.Builder createCaptureRequest(int paramInt)
    throws CameraAccessException
  {
    synchronized (mInterfaceLock)
    {
      checkIfCameraClosedOrInError();
      CameraMetadataNative localCameraMetadataNative = mRemoteDevice.createDefaultRequest(paramInt);
      if ((mAppTargetSdkVersion < 26) || (paramInt != 2)) {
        overrideEnableZsl(localCameraMetadataNative, false);
      }
      CaptureRequest.Builder localBuilder = new android/hardware/camera2/CaptureRequest$Builder;
      localBuilder.<init>(localCameraMetadataNative, false, -1, getId(), null);
      return localBuilder;
    }
  }
  
  public CaptureRequest.Builder createCaptureRequest(int paramInt, Set<String> paramSet)
    throws CameraAccessException
  {
    synchronized (mInterfaceLock)
    {
      checkIfCameraClosedOrInError();
      Object localObject2 = paramSet.iterator();
      while (((Iterator)localObject2).hasNext()) {
        if ((String)((Iterator)localObject2).next() == getId())
        {
          paramSet = new java/lang/IllegalStateException;
          paramSet.<init>("Physical id matches the logical id!");
          throw paramSet;
        }
      }
      localObject2 = mRemoteDevice.createDefaultRequest(paramInt);
      if ((mAppTargetSdkVersion < 26) || (paramInt != 2)) {
        overrideEnableZsl((CameraMetadataNative)localObject2, false);
      }
      CaptureRequest.Builder localBuilder = new android/hardware/camera2/CaptureRequest$Builder;
      localBuilder.<init>((CameraMetadataNative)localObject2, false, -1, getId(), paramSet);
      return localBuilder;
    }
  }
  
  public void createCaptureSession(SessionConfiguration paramSessionConfiguration)
    throws CameraAccessException
  {
    if (paramSessionConfiguration != null)
    {
      List localList = paramSessionConfiguration.getOutputConfigurations();
      if (localList != null)
      {
        if (paramSessionConfiguration.getExecutor() != null)
        {
          createCaptureSessionInternal(paramSessionConfiguration.getInputConfiguration(), localList, paramSessionConfiguration.getStateCallback(), paramSessionConfiguration.getExecutor(), paramSessionConfiguration.getSessionType(), paramSessionConfiguration.getSessionParameters());
          return;
        }
        throw new IllegalArgumentException("Invalid executor");
      }
      throw new IllegalArgumentException("Invalid output configurations");
    }
    throw new IllegalArgumentException("Invalid session configuration");
  }
  
  public void createCaptureSession(List<Surface> paramList, CameraCaptureSession.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException
  {
    ArrayList localArrayList = new ArrayList(paramList.size());
    paramList = paramList.iterator();
    while (paramList.hasNext()) {
      localArrayList.add(new OutputConfiguration((Surface)paramList.next()));
    }
    createCaptureSessionInternal(null, localArrayList, paramStateCallback, checkAndWrapHandler(paramHandler), 0, null);
  }
  
  public void createCaptureSessionByOutputConfigurations(List<OutputConfiguration> paramList, CameraCaptureSession.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException
  {
    createCaptureSessionInternal(null, new ArrayList(paramList), paramStateCallback, checkAndWrapHandler(paramHandler), 0, null);
  }
  
  public void createConstrainedHighSpeedCaptureSession(List<Surface> paramList, CameraCaptureSession.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException
  {
    if ((paramList != null) && (paramList.size() != 0) && (paramList.size() <= 2))
    {
      ArrayList localArrayList = new ArrayList(paramList.size());
      paramList = paramList.iterator();
      while (paramList.hasNext()) {
        localArrayList.add(new OutputConfiguration((Surface)paramList.next()));
      }
      createCaptureSessionInternal(null, localArrayList, paramStateCallback, checkAndWrapHandler(paramHandler), 1, null);
      return;
    }
    throw new IllegalArgumentException("Output surface list must not be null and the size must be no more than 2");
  }
  
  public void createCustomCaptureSession(InputConfiguration paramInputConfiguration, List<OutputConfiguration> paramList, int paramInt, CameraCaptureSession.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException
  {
    ArrayList localArrayList = new ArrayList();
    paramList = paramList.iterator();
    while (paramList.hasNext()) {
      localArrayList.add(new OutputConfiguration((OutputConfiguration)paramList.next()));
    }
    createCaptureSessionInternal(paramInputConfiguration, localArrayList, paramStateCallback, checkAndWrapHandler(paramHandler), paramInt, null);
  }
  
  public CaptureRequest.Builder createReprocessCaptureRequest(TotalCaptureResult paramTotalCaptureResult)
    throws CameraAccessException
  {
    synchronized (mInterfaceLock)
    {
      checkIfCameraClosedOrInError();
      CameraMetadataNative localCameraMetadataNative = new android/hardware/camera2/impl/CameraMetadataNative;
      localCameraMetadataNative.<init>(paramTotalCaptureResult.getNativeCopy());
      CaptureRequest.Builder localBuilder = new android/hardware/camera2/CaptureRequest$Builder;
      localBuilder.<init>(localCameraMetadataNative, true, paramTotalCaptureResult.getSessionId(), getId(), null);
      return localBuilder;
    }
  }
  
  public void createReprocessableCaptureSession(InputConfiguration paramInputConfiguration, List<Surface> paramList, CameraCaptureSession.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException
  {
    if (paramInputConfiguration != null)
    {
      ArrayList localArrayList = new ArrayList(paramList.size());
      paramList = paramList.iterator();
      while (paramList.hasNext()) {
        localArrayList.add(new OutputConfiguration((Surface)paramList.next()));
      }
      createCaptureSessionInternal(paramInputConfiguration, localArrayList, paramStateCallback, checkAndWrapHandler(paramHandler), 0, null);
      return;
    }
    throw new IllegalArgumentException("inputConfig cannot be null when creating a reprocessable capture session");
  }
  
  public void createReprocessableCaptureSessionByConfigurations(InputConfiguration paramInputConfiguration, List<OutputConfiguration> paramList, CameraCaptureSession.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException
  {
    if (paramInputConfiguration != null)
    {
      if (paramList != null)
      {
        ArrayList localArrayList = new ArrayList();
        paramList = paramList.iterator();
        while (paramList.hasNext()) {
          localArrayList.add(new OutputConfiguration((OutputConfiguration)paramList.next()));
        }
        createCaptureSessionInternal(paramInputConfiguration, localArrayList, paramStateCallback, checkAndWrapHandler(paramHandler), 0, null);
        return;
      }
      throw new IllegalArgumentException("Output configurations cannot be null when creating a reprocessable capture session");
    }
    throw new IllegalArgumentException("inputConfig cannot be null when creating a reprocessable capture session");
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
  
  public void finalizeOutputConfigs(List<OutputConfiguration> paramList)
    throws CameraAccessException
  {
    if ((paramList != null) && (paramList.size() != 0)) {
      synchronized (mInterfaceLock)
      {
        Object localObject2 = paramList.iterator();
        while (((Iterator)localObject2).hasNext())
        {
          paramList = (OutputConfiguration)((Iterator)localObject2).next();
          int i = -1;
          int k;
          for (int j = 0;; j++)
          {
            k = i;
            if (j >= mConfiguredOutputs.size()) {
              break;
            }
            if (paramList.equals(mConfiguredOutputs.valueAt(j)))
            {
              k = mConfiguredOutputs.keyAt(j);
              break;
            }
          }
          if (k != -1)
          {
            if (paramList.getSurfaces().size() != 0)
            {
              mRemoteDevice.finalizeOutputConfigurations(k, paramList);
              mConfiguredOutputs.put(k, paramList);
            }
            else
            {
              paramList = new java/lang/IllegalArgumentException;
              localObject2 = new java/lang/StringBuilder;
              ((StringBuilder)localObject2).<init>();
              ((StringBuilder)localObject2).append("The final config for stream ");
              ((StringBuilder)localObject2).append(k);
              ((StringBuilder)localObject2).append(" must have at least 1 surface");
              paramList.<init>(((StringBuilder)localObject2).toString());
              throw paramList;
            }
          }
          else
          {
            paramList = new java/lang/IllegalArgumentException;
            paramList.<init>("Deferred config is not part of this session");
            throw paramList;
          }
        }
        return;
      }
    }
    throw new IllegalArgumentException("deferred config is null or empty");
  }
  
  public void flush()
    throws CameraAccessException
  {
    synchronized (mInterfaceLock)
    {
      checkIfCameraClosedOrInError();
      mDeviceExecutor.execute(mCallOnBusy);
      if (mIdle)
      {
        mDeviceExecutor.execute(mCallOnIdle);
        return;
      }
      long l = mRemoteDevice.flush();
      if (mRepeatingRequestId != -1)
      {
        checkEarlyTriggerSequenceComplete(mRepeatingRequestId, l);
        mRepeatingRequestId = -1;
      }
      return;
    }
  }
  
  public CameraDeviceCallbacks getCallbacks()
  {
    return mCallbacks;
  }
  
  public String getId()
  {
    return mCameraId;
  }
  
  public boolean isPrivilegedApp()
  {
    return mIsPrivilegedApp;
  }
  
  public void prepare(int paramInt, Surface paramSurface)
    throws CameraAccessException
  {
    if (paramSurface != null)
    {
      if (paramInt > 0)
      {
        Object localObject = mInterfaceLock;
        int i = -1;
        int j = 0;
        for (;;)
        {
          int k = i;
          try
          {
            if (j < mConfiguredOutputs.size()) {
              if (paramSurface == ((OutputConfiguration)mConfiguredOutputs.valueAt(j)).getSurface())
              {
                k = mConfiguredOutputs.keyAt(j);
              }
              else
              {
                j++;
                continue;
              }
            }
            if (k != -1)
            {
              mRemoteDevice.prepare2(paramInt, k);
              return;
            }
            paramSurface = new java/lang/IllegalArgumentException;
            paramSurface.<init>("Surface is not part of this session");
            throw paramSurface;
          }
          finally {}
        }
      }
      paramSurface = new StringBuilder();
      paramSurface.append("Invalid maxCount given: ");
      paramSurface.append(paramInt);
      throw new IllegalArgumentException(paramSurface.toString());
    }
    throw new IllegalArgumentException("Surface is null");
  }
  
  public void prepare(Surface paramSurface)
    throws CameraAccessException
  {
    if (paramSurface != null)
    {
      Object localObject = mInterfaceLock;
      int i = -1;
      int j = 0;
      for (;;)
      {
        int k = i;
        try
        {
          if (j < mConfiguredOutputs.size()) {
            if (((OutputConfiguration)mConfiguredOutputs.valueAt(j)).getSurfaces().contains(paramSurface))
            {
              k = mConfiguredOutputs.keyAt(j);
            }
            else
            {
              j++;
              continue;
            }
          }
          if (k != -1)
          {
            mRemoteDevice.prepare(k);
            return;
          }
          paramSurface = new java/lang/IllegalArgumentException;
          paramSurface.<init>("Surface is not part of this session");
          throw paramSurface;
        }
        finally {}
      }
    }
    throw new IllegalArgumentException("Surface is null");
  }
  
  public void setRemoteDevice(ICameraDeviceUser paramICameraDeviceUser)
    throws CameraAccessException
  {
    synchronized (mInterfaceLock)
    {
      if (mInError) {
        return;
      }
      ICameraDeviceUserWrapper localICameraDeviceUserWrapper = new android/hardware/camera2/impl/ICameraDeviceUserWrapper;
      localICameraDeviceUserWrapper.<init>(paramICameraDeviceUser);
      mRemoteDevice = localICameraDeviceUserWrapper;
      paramICameraDeviceUser = paramICameraDeviceUser.asBinder();
      if (paramICameraDeviceUser != null) {
        try
        {
          paramICameraDeviceUser.linkToDeath(this, 0);
        }
        catch (RemoteException paramICameraDeviceUser)
        {
          mDeviceExecutor.execute(mCallOnDisconnected);
          paramICameraDeviceUser = new android/hardware/camera2/CameraAccessException;
          paramICameraDeviceUser.<init>(2, "The camera device has encountered a serious error");
          throw paramICameraDeviceUser;
        }
      }
      mDeviceExecutor.execute(mCallOnOpened);
      mDeviceExecutor.execute(mCallOnUnconfigured);
      return;
    }
  }
  
  public void setRemoteFailure(ServiceSpecificException arg1)
  {
    int i = 4;
    boolean bool = true;
    int j = errorCode;
    Object localObject1;
    Object localObject2;
    if (j != 4)
    {
      if (j != 10) {
        switch (j)
        {
        default: 
          localObject1 = TAG;
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Unexpected failure in opening camera device: ");
          ((StringBuilder)localObject2).append(errorCode);
          ((StringBuilder)localObject2).append(???.getMessage());
          Log.e((String)localObject1, ((StringBuilder)localObject2).toString());
          break;
        case 8: 
          i = 2;
          break;
        case 7: 
          i = 1;
          break;
        case 6: 
          i = 3;
          break;
        }
      } else {
        i = 4;
      }
    }
    else {
      bool = false;
    }
    synchronized (mInterfaceLock)
    {
      mInError = true;
      localObject2 = mDeviceExecutor;
      localObject1 = new android/hardware/camera2/impl/CameraDeviceImpl$8;
      ((8)localObject1).<init>(this, bool, i);
      ((Executor)localObject2).execute((Runnable)localObject1);
      return;
    }
  }
  
  public int setRepeatingBurst(List<CaptureRequest> paramList, CaptureCallback paramCaptureCallback, Executor paramExecutor)
    throws CameraAccessException
  {
    if ((paramList != null) && (!paramList.isEmpty())) {
      return submitCaptureRequest(paramList, paramCaptureCallback, paramExecutor, true);
    }
    throw new IllegalArgumentException("At least one request must be given");
  }
  
  public int setRepeatingRequest(CaptureRequest paramCaptureRequest, CaptureCallback paramCaptureCallback, Executor paramExecutor)
    throws CameraAccessException
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(paramCaptureRequest);
    return submitCaptureRequest(localArrayList, paramCaptureCallback, paramExecutor, true);
  }
  
  public void setSessionListener(StateCallbackKK paramStateCallbackKK)
  {
    synchronized (mInterfaceLock)
    {
      mSessionStateCallback = paramStateCallbackKK;
      return;
    }
  }
  
  public void setVendorStreamConfigMode(int paramInt)
  {
    customOpMode = paramInt;
  }
  
  public void stopRepeating()
    throws CameraAccessException
  {
    synchronized (mInterfaceLock)
    {
      checkIfCameraClosedOrInError();
      if (mRepeatingRequestId != -1)
      {
        int i = mRepeatingRequestId;
        mRepeatingRequestId = -1;
        try
        {
          long l = mRemoteDevice.cancelRequest(i);
          checkEarlyTriggerSequenceComplete(i, l);
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          return;
        }
      }
      return;
    }
  }
  
  public void tearDown(Surface paramSurface)
    throws CameraAccessException
  {
    if (paramSurface != null)
    {
      Object localObject = mInterfaceLock;
      int i = -1;
      int j = 0;
      for (;;)
      {
        int k = i;
        try
        {
          if (j < mConfiguredOutputs.size()) {
            if (paramSurface == ((OutputConfiguration)mConfiguredOutputs.valueAt(j)).getSurface())
            {
              k = mConfiguredOutputs.keyAt(j);
            }
            else
            {
              j++;
              continue;
            }
          }
          if (k != -1)
          {
            mRemoteDevice.tearDown(k);
            return;
          }
          paramSurface = new java/lang/IllegalArgumentException;
          paramSurface.<init>("Surface is not part of this session");
          throw paramSurface;
        }
        finally {}
      }
    }
    throw new IllegalArgumentException("Surface is null");
  }
  
  public void updateOutputConfiguration(OutputConfiguration paramOutputConfiguration)
    throws CameraAccessException
  {
    Object localObject = mInterfaceLock;
    int i = -1;
    int j = 0;
    for (;;)
    {
      int k = i;
      try
      {
        if (j < mConfiguredOutputs.size()) {
          if (paramOutputConfiguration.getSurface() == ((OutputConfiguration)mConfiguredOutputs.valueAt(j)).getSurface())
          {
            k = mConfiguredOutputs.keyAt(j);
          }
          else
          {
            j++;
            continue;
          }
        }
        if (k != -1)
        {
          mRemoteDevice.updateOutputConfiguration(k, paramOutputConfiguration);
          mConfiguredOutputs.put(k, paramOutputConfiguration);
          return;
        }
        paramOutputConfiguration = new java/lang/IllegalArgumentException;
        paramOutputConfiguration.<init>("Invalid output configuration");
        throw paramOutputConfiguration;
      }
      finally {}
    }
  }
  
  public class CameraDeviceCallbacks
    extends ICameraDeviceCallbacks.Stub
  {
    public CameraDeviceCallbacks() {}
    
    private void notifyError(int paramInt)
    {
      if (!CameraDeviceImpl.this.isClosed()) {
        mDeviceCallback.onError(CameraDeviceImpl.this, paramInt);
      }
    }
    
    private void onCaptureErrorLocked(int paramInt, CaptureResultExtras paramCaptureResultExtras)
    {
      int i = paramCaptureResultExtras.getRequestId();
      int j = paramCaptureResultExtras.getSubsequenceId();
      final long l1 = paramCaptureResultExtras.getFrameNumber();
      final CameraDeviceImpl.CaptureCallbackHolder localCaptureCallbackHolder = (CameraDeviceImpl.CaptureCallbackHolder)mCaptureCallbackMap.get(i);
      final CaptureRequest localCaptureRequest = localCaptureCallbackHolder.getRequest(j);
      if (paramInt == 5)
      {
        List localList = ((OutputConfiguration)mConfiguredOutputs.get(paramCaptureResultExtras.getErrorStreamId())).getSurfaces();
        paramCaptureResultExtras = localList.iterator();
        for (;;)
        {
          Object localObject;
          if (paramCaptureResultExtras.hasNext())
          {
            localObject = (Surface)paramCaptureResultExtras.next();
            if (localCaptureRequest.containsTarget((Surface)localObject))
            {
              localObject = new Runnable()
              {
                public void run()
                {
                  if (!CameraDeviceImpl.this.isClosed()) {
                    localCaptureCallbackHolder.getCallback().onCaptureBufferLost(CameraDeviceImpl.this, localCaptureRequest, val$surface, l1);
                  }
                }
              };
              l2 = Binder.clearCallingIdentity();
            }
          }
          else
          {
            try
            {
              localCaptureCallbackHolder.getExecutor().execute((Runnable)localObject);
              Binder.restoreCallingIdentity(l2);
            }
            finally
            {
              Binder.restoreCallingIdentity(l2);
            }
          }
        }
      }
      boolean bool;
      if (paramInt == 4) {
        bool = true;
      } else {
        bool = false;
      }
      if ((mCurrentSession != null) && (mCurrentSession.isAborting())) {
        paramInt = 1;
      } else {
        paramInt = 0;
      }
      paramCaptureResultExtras = new Runnable()
      {
        public void run()
        {
          if (!CameraDeviceImpl.this.isClosed()) {
            localCaptureCallbackHolder.getCallback().onCaptureFailed(CameraDeviceImpl.this, localCaptureRequest, val$failure);
          }
        }
      };
      mFrameNumberTracker.updateTracker(l1, true, localCaptureRequest.isReprocess());
      CameraDeviceImpl.this.checkAndFireSequenceComplete();
      long l2 = Binder.clearCallingIdentity();
      try
      {
        localCaptureCallbackHolder.getExecutor().execute(paramCaptureResultExtras);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l2);
      }
    }
    
    private void scheduleNotifyError(int paramInt)
    {
      CameraDeviceImpl.access$702(CameraDeviceImpl.this, true);
      long l = Binder.clearCallingIdentity();
      try
      {
        mDeviceExecutor.execute(PooledLambda.obtainRunnable(_..Lambda.CameraDeviceImpl.CameraDeviceCallbacks.Sm85frAzwGZVMAK_NE_gwckYXVQ.INSTANCE, this, Integer.valueOf(paramInt)));
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    /* Error */
    public void onCaptureStarted(CaptureResultExtras paramCaptureResultExtras, long paramLong)
    {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual 55	android/hardware/camera2/impl/CaptureResultExtras:getRequestId	()I
      //   4: istore 4
      //   6: aload_1
      //   7: invokevirtual 62	android/hardware/camera2/impl/CaptureResultExtras:getFrameNumber	()J
      //   10: lstore 5
      //   12: aload_0
      //   13: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   16: getfield 204	android/hardware/camera2/impl/CameraDeviceImpl:mInterfaceLock	Ljava/lang/Object;
      //   19: astore 7
      //   21: aload 7
      //   23: monitorenter
      //   24: aload_0
      //   25: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   28: invokestatic 208	android/hardware/camera2/impl/CameraDeviceImpl:access$000	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)Landroid/hardware/camera2/impl/ICameraDeviceUserWrapper;
      //   31: astore 8
      //   33: aload 8
      //   35: ifnonnull +11 -> 46
      //   38: aload 7
      //   40: monitorexit
      //   41: return
      //   42: astore_1
      //   43: goto +107 -> 150
      //   46: aload_0
      //   47: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   50: invokestatic 66	android/hardware/camera2/impl/CameraDeviceImpl:access$1200	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)Landroid/util/SparseArray;
      //   53: iload 4
      //   55: invokevirtual 72	android/util/SparseArray:get	(I)Ljava/lang/Object;
      //   58: checkcast 74	android/hardware/camera2/impl/CameraDeviceImpl$CaptureCallbackHolder
      //   61: astore 9
      //   63: aload 9
      //   65: ifnonnull +7 -> 72
      //   68: aload 7
      //   70: monitorexit
      //   71: return
      //   72: aload_0
      //   73: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   76: invokestatic 37	android/hardware/camera2/impl/CameraDeviceImpl:access$300	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)Z
      //   79: istore 10
      //   81: iload 10
      //   83: ifeq +7 -> 90
      //   86: aload 7
      //   88: monitorexit
      //   89: return
      //   90: invokestatic 122	android/os/Binder:clearCallingIdentity	()J
      //   93: lstore 11
      //   95: aload 9
      //   97: invokevirtual 126	android/hardware/camera2/impl/CameraDeviceImpl$CaptureCallbackHolder:getExecutor	()Ljava/util/concurrent/Executor;
      //   100: astore 8
      //   102: new 9	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks$1
      //   105: astore 13
      //   107: aload 13
      //   109: aload_0
      //   110: aload_1
      //   111: aload 9
      //   113: lload_2
      //   114: lload 5
      //   116: invokespecial 211	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks$1:<init>	(Landroid/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks;Landroid/hardware/camera2/impl/CaptureResultExtras;Landroid/hardware/camera2/impl/CameraDeviceImpl$CaptureCallbackHolder;JJ)V
      //   119: aload 8
      //   121: aload 13
      //   123: invokeinterface 132 2 0
      //   128: lload 11
      //   130: invokestatic 136	android/os/Binder:restoreCallingIdentity	(J)V
      //   133: aload 7
      //   135: monitorexit
      //   136: return
      //   137: astore_1
      //   138: goto +4 -> 142
      //   141: astore_1
      //   142: lload 11
      //   144: invokestatic 136	android/os/Binder:restoreCallingIdentity	(J)V
      //   147: aload_1
      //   148: athrow
      //   149: astore_1
      //   150: aload 7
      //   152: monitorexit
      //   153: aload_1
      //   154: athrow
      //   155: astore_1
      //   156: goto -6 -> 150
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	159	0	this	CameraDeviceCallbacks
      //   0	159	1	paramCaptureResultExtras	CaptureResultExtras
      //   0	159	2	paramLong	long
      //   4	50	4	i	int
      //   10	105	5	l1	long
      //   19	132	7	localObject1	Object
      //   31	89	8	localObject2	Object
      //   61	51	9	localCaptureCallbackHolder	CameraDeviceImpl.CaptureCallbackHolder
      //   79	3	10	bool	boolean
      //   93	50	11	l2	long
      //   105	17	13	local1	1
      // Exception table:
      //   from	to	target	type
      //   38	41	42	finally
      //   68	71	42	finally
      //   86	89	42	finally
      //   107	128	137	finally
      //   95	107	141	finally
      //   24	33	149	finally
      //   46	63	149	finally
      //   72	81	149	finally
      //   90	95	149	finally
      //   128	133	155	finally
      //   133	136	155	finally
      //   142	149	155	finally
      //   150	153	155	finally
    }
    
    public void onDeviceError(int paramInt, CaptureResultExtras paramCaptureResultExtras)
    {
      synchronized (mInterfaceLock)
      {
        if (mRemoteDevice == null) {
          return;
        }
        long l;
        switch (paramInt)
        {
        case 2: 
        default: 
          paramCaptureResultExtras = CameraDeviceImpl.this;
          break;
        case 6: 
          scheduleNotifyError(3);
          break;
        case 3: 
        case 4: 
        case 5: 
          onCaptureErrorLocked(paramInt, paramCaptureResultExtras);
          break;
        case 1: 
          scheduleNotifyError(4);
          break;
        case 0: 
          l = Binder.clearCallingIdentity();
        }
        String str;
        try
        {
          mDeviceExecutor.execute(mCallOnDisconnected);
          Binder.restoreCallingIdentity(l);
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
        paramCaptureResultExtras = new java/lang/StringBuilder;
        paramCaptureResultExtras.<init>();
        paramCaptureResultExtras.append("Unknown error from camera device: ");
        paramCaptureResultExtras.append(paramInt);
        Log.e(str, paramCaptureResultExtras.toString());
        scheduleNotifyError(5);
        return;
      }
    }
    
    public void onDeviceIdle()
    {
      synchronized (mInterfaceLock)
      {
        if (mRemoteDevice == null) {
          return;
        }
        long l;
        if (!mIdle) {
          l = Binder.clearCallingIdentity();
        }
        try
        {
          mDeviceExecutor.execute(mCallOnIdle);
          Binder.restoreCallingIdentity(l);
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
        return;
      }
    }
    
    public void onPrepared(int paramInt)
    {
      synchronized (mInterfaceLock)
      {
        OutputConfiguration localOutputConfiguration = (OutputConfiguration)mConfiguredOutputs.get(paramInt);
        CameraDeviceImpl.StateCallbackKK localStateCallbackKK = mSessionStateCallback;
        if (localStateCallbackKK == null) {
          return;
        }
        if (localOutputConfiguration == null)
        {
          Log.w(TAG, "onPrepared invoked for unknown output Surface");
          return;
        }
        ??? = localOutputConfiguration.getSurfaces().iterator();
        while (((Iterator)???).hasNext()) {
          localStateCallbackKK.onSurfacePrepared((Surface)((Iterator)???).next());
        }
        return;
      }
    }
    
    public void onRepeatingRequestError(long paramLong, int paramInt)
    {
      synchronized (mInterfaceLock)
      {
        if ((mRemoteDevice != null) && (mRepeatingRequestId != -1))
        {
          CameraDeviceImpl.this.checkEarlyTriggerSequenceComplete(mRepeatingRequestId, paramLong);
          if (mRepeatingRequestId == paramInt) {
            CameraDeviceImpl.access$802(CameraDeviceImpl.this, -1);
          }
          return;
        }
        return;
      }
    }
    
    public void onRequestQueueEmpty()
    {
      synchronized (mInterfaceLock)
      {
        CameraDeviceImpl.StateCallbackKK localStateCallbackKK = mSessionStateCallback;
        if (localStateCallbackKK == null) {
          return;
        }
        localStateCallbackKK.onRequestQueueEmpty();
        return;
      }
    }
    
    /* Error */
    public void onResultReceived(CameraMetadataNative paramCameraMetadataNative, final CaptureResultExtras paramCaptureResultExtras, final PhysicalCaptureResultInfo[] paramArrayOfPhysicalCaptureResultInfo)
      throws RemoteException
    {
      // Byte code:
      //   0: aload_2
      //   1: invokevirtual 55	android/hardware/camera2/impl/CaptureResultExtras:getRequestId	()I
      //   4: istore 4
      //   6: aload_2
      //   7: invokevirtual 62	android/hardware/camera2/impl/CaptureResultExtras:getFrameNumber	()J
      //   10: lstore 5
      //   12: aload_0
      //   13: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   16: getfield 204	android/hardware/camera2/impl/CameraDeviceImpl:mInterfaceLock	Ljava/lang/Object;
      //   19: astore 7
      //   21: aload 7
      //   23: monitorenter
      //   24: aload_0
      //   25: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   28: invokestatic 208	android/hardware/camera2/impl/CameraDeviceImpl:access$000	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)Landroid/hardware/camera2/impl/ICameraDeviceUserWrapper;
      //   31: astore 8
      //   33: aload 8
      //   35: ifnonnull +14 -> 49
      //   38: aload 7
      //   40: monitorexit
      //   41: return
      //   42: astore_1
      //   43: aload 7
      //   45: astore_2
      //   46: goto +418 -> 464
      //   49: aload_1
      //   50: getstatic 299	android/hardware/camera2/CameraCharacteristics:LENS_INFO_SHADING_MAP_SIZE	Landroid/hardware/camera2/CameraCharacteristics$Key;
      //   53: aload_0
      //   54: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   57: invokestatic 303	android/hardware/camera2/impl/CameraDeviceImpl:access$1300	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)Landroid/hardware/camera2/CameraCharacteristics;
      //   60: getstatic 299	android/hardware/camera2/CameraCharacteristics:LENS_INFO_SHADING_MAP_SIZE	Landroid/hardware/camera2/CameraCharacteristics$Key;
      //   63: invokevirtual 306	android/hardware/camera2/CameraCharacteristics:get	(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
      //   66: checkcast 308	android/util/Size
      //   69: invokevirtual 314	android/hardware/camera2/impl/CameraMetadataNative:set	(Landroid/hardware/camera2/CameraCharacteristics$Key;Ljava/lang/Object;)V
      //   72: aload_0
      //   73: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   76: invokestatic 66	android/hardware/camera2/impl/CameraDeviceImpl:access$1200	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)Landroid/util/SparseArray;
      //   79: iload 4
      //   81: invokevirtual 72	android/util/SparseArray:get	(I)Ljava/lang/Object;
      //   84: checkcast 74	android/hardware/camera2/impl/CameraDeviceImpl$CaptureCallbackHolder
      //   87: astore 9
      //   89: aload 9
      //   91: aload_2
      //   92: invokevirtual 58	android/hardware/camera2/impl/CaptureResultExtras:getSubsequenceId	()I
      //   95: invokevirtual 78	android/hardware/camera2/impl/CameraDeviceImpl$CaptureCallbackHolder:getRequest	(I)Landroid/hardware/camera2/CaptureRequest;
      //   98: astore 10
      //   100: aload_2
      //   101: invokevirtual 317	android/hardware/camera2/impl/CaptureResultExtras:getPartialResultCount	()I
      //   104: aload_0
      //   105: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   108: invokestatic 320	android/hardware/camera2/impl/CameraDeviceImpl:access$1400	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)I
      //   111: if_icmpge +9 -> 120
      //   114: iconst_1
      //   115: istore 11
      //   117: goto +6 -> 123
      //   120: iconst_0
      //   121: istore 11
      //   123: aload 10
      //   125: invokevirtual 160	android/hardware/camera2/CaptureRequest:isReprocess	()Z
      //   128: istore 12
      //   130: aload 9
      //   132: ifnonnull +24 -> 156
      //   135: aload_0
      //   136: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   139: invokestatic 157	android/hardware/camera2/impl/CameraDeviceImpl:access$1500	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)Landroid/hardware/camera2/impl/CameraDeviceImpl$FrameNumberTracker;
      //   142: lload 5
      //   144: aconst_null
      //   145: iload 11
      //   147: iload 12
      //   149: invokevirtual 323	android/hardware/camera2/impl/CameraDeviceImpl$FrameNumberTracker:updateTracker	(JLandroid/hardware/camera2/CaptureResult;ZZ)V
      //   152: aload 7
      //   154: monitorexit
      //   155: return
      //   156: aload_0
      //   157: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   160: invokestatic 37	android/hardware/camera2/impl/CameraDeviceImpl:access$300	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)Z
      //   163: istore 13
      //   165: iload 13
      //   167: ifeq +24 -> 191
      //   170: aload_0
      //   171: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   174: invokestatic 157	android/hardware/camera2/impl/CameraDeviceImpl:access$1500	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)Landroid/hardware/camera2/impl/CameraDeviceImpl$FrameNumberTracker;
      //   177: lload 5
      //   179: aconst_null
      //   180: iload 11
      //   182: iload 12
      //   184: invokevirtual 323	android/hardware/camera2/impl/CameraDeviceImpl$FrameNumberTracker:updateTracker	(JLandroid/hardware/camera2/CaptureResult;ZZ)V
      //   187: aload 7
      //   189: monitorexit
      //   190: return
      //   191: aload 9
      //   193: invokevirtual 326	android/hardware/camera2/impl/CameraDeviceImpl$CaptureCallbackHolder:hasBatchedOutputs	()Z
      //   196: istore 13
      //   198: iload 13
      //   200: ifeq +17 -> 217
      //   203: new 310	android/hardware/camera2/impl/CameraMetadataNative
      //   206: astore 8
      //   208: aload 8
      //   210: aload_1
      //   211: invokespecial 329	android/hardware/camera2/impl/CameraMetadataNative:<init>	(Landroid/hardware/camera2/impl/CameraMetadataNative;)V
      //   214: goto +6 -> 220
      //   217: aconst_null
      //   218: astore 8
      //   220: iload 11
      //   222: ifeq +37 -> 259
      //   225: new 331	android/hardware/camera2/CaptureResult
      //   228: astore_3
      //   229: aload_3
      //   230: aload_1
      //   231: aload 10
      //   233: aload_2
      //   234: invokespecial 334	android/hardware/camera2/CaptureResult:<init>	(Landroid/hardware/camera2/impl/CameraMetadataNative;Landroid/hardware/camera2/CaptureRequest;Landroid/hardware/camera2/impl/CaptureResultExtras;)V
      //   237: new 11	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks$2
      //   240: dup
      //   241: aload_0
      //   242: aload 9
      //   244: aload 8
      //   246: aload_2
      //   247: aload 10
      //   249: aload_3
      //   250: invokespecial 337	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks$2:<init>	(Landroid/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks;Landroid/hardware/camera2/impl/CameraDeviceImpl$CaptureCallbackHolder;Landroid/hardware/camera2/impl/CameraMetadataNative;Landroid/hardware/camera2/impl/CaptureResultExtras;Landroid/hardware/camera2/CaptureRequest;Landroid/hardware/camera2/CaptureResult;)V
      //   253: astore_2
      //   254: aload_3
      //   255: astore_1
      //   256: goto +109 -> 365
      //   259: aload_0
      //   260: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   263: invokestatic 157	android/hardware/camera2/impl/CameraDeviceImpl:access$1500	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)Landroid/hardware/camera2/impl/CameraDeviceImpl$FrameNumberTracker;
      //   266: lload 5
      //   268: invokevirtual 341	android/hardware/camera2/impl/CameraDeviceImpl$FrameNumberTracker:popPartialResults	(J)Ljava/util/List;
      //   271: astore 14
      //   273: aload_1
      //   274: getstatic 345	android/hardware/camera2/CaptureResult:SENSOR_TIMESTAMP	Landroid/hardware/camera2/CaptureResult$Key;
      //   277: invokevirtual 348	android/hardware/camera2/impl/CameraMetadataNative:get	(Landroid/hardware/camera2/CaptureResult$Key;)Ljava/lang/Object;
      //   280: checkcast 350	java/lang/Long
      //   283: invokevirtual 353	java/lang/Long:longValue	()J
      //   286: lstore 15
      //   288: aload 9
      //   290: astore 17
      //   292: aload 10
      //   294: getstatic 357	android/hardware/camera2/CaptureRequest:CONTROL_AE_TARGET_FPS_RANGE	Landroid/hardware/camera2/CaptureRequest$Key;
      //   297: invokevirtual 360	android/hardware/camera2/CaptureRequest:get	(Landroid/hardware/camera2/CaptureRequest$Key;)Ljava/lang/Object;
      //   300: checkcast 362	android/util/Range
      //   303: astore 18
      //   305: aload_2
      //   306: invokevirtual 58	android/hardware/camera2/impl/CaptureResultExtras:getSubsequenceId	()I
      //   309: istore 4
      //   311: new 364	android/hardware/camera2/TotalCaptureResult
      //   314: astore 19
      //   316: aload 19
      //   318: aload_1
      //   319: aload 10
      //   321: aload_2
      //   322: aload 14
      //   324: aload 17
      //   326: invokevirtual 367	android/hardware/camera2/impl/CameraDeviceImpl$CaptureCallbackHolder:getSessionId	()I
      //   329: aload_3
      //   330: invokespecial 370	android/hardware/camera2/TotalCaptureResult:<init>	(Landroid/hardware/camera2/impl/CameraMetadataNative;Landroid/hardware/camera2/CaptureRequest;Landroid/hardware/camera2/impl/CaptureResultExtras;Ljava/util/List;I[Landroid/hardware/camera2/impl/PhysicalCaptureResultInfo;)V
      //   333: aload 7
      //   335: astore_3
      //   336: new 13	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks$3
      //   339: dup
      //   340: aload_0
      //   341: aload 17
      //   343: aload 8
      //   345: lload 15
      //   347: iload 4
      //   349: aload 18
      //   351: aload_2
      //   352: aload 14
      //   354: aload 10
      //   356: aload 19
      //   358: invokespecial 373	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks$3:<init>	(Landroid/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks;Landroid/hardware/camera2/impl/CameraDeviceImpl$CaptureCallbackHolder;Landroid/hardware/camera2/impl/CameraMetadataNative;JILandroid/util/Range;Landroid/hardware/camera2/impl/CaptureResultExtras;Ljava/util/List;Landroid/hardware/camera2/CaptureRequest;Landroid/hardware/camera2/TotalCaptureResult;)V
      //   361: astore_2
      //   362: aload 19
      //   364: astore_1
      //   365: aload_0
      //   366: astore 8
      //   368: aload 7
      //   370: astore_3
      //   371: invokestatic 122	android/os/Binder:clearCallingIdentity	()J
      //   374: lstore 15
      //   376: aload 9
      //   378: invokevirtual 126	android/hardware/camera2/impl/CameraDeviceImpl$CaptureCallbackHolder:getExecutor	()Ljava/util/concurrent/Executor;
      //   381: aload_2
      //   382: invokeinterface 132 2 0
      //   387: aload 7
      //   389: astore_3
      //   390: lload 15
      //   392: invokestatic 136	android/os/Binder:restoreCallingIdentity	(J)V
      //   395: aload 7
      //   397: astore_3
      //   398: aload 8
      //   400: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   403: invokestatic 157	android/hardware/camera2/impl/CameraDeviceImpl:access$1500	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)Landroid/hardware/camera2/impl/CameraDeviceImpl$FrameNumberTracker;
      //   406: lload 5
      //   408: aload_1
      //   409: iload 11
      //   411: iload 12
      //   413: invokevirtual 323	android/hardware/camera2/impl/CameraDeviceImpl$FrameNumberTracker:updateTracker	(JLandroid/hardware/camera2/CaptureResult;ZZ)V
      //   416: iload 11
      //   418: ifne +14 -> 432
      //   421: aload 7
      //   423: astore_3
      //   424: aload 8
      //   426: getfield 23	android/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks:this$0	Landroid/hardware/camera2/impl/CameraDeviceImpl;
      //   429: invokestatic 169	android/hardware/camera2/impl/CameraDeviceImpl:access$1600	(Landroid/hardware/camera2/impl/CameraDeviceImpl;)V
      //   432: aload 7
      //   434: astore_3
      //   435: aload 7
      //   437: monitorexit
      //   438: return
      //   439: astore_1
      //   440: aload 7
      //   442: astore_3
      //   443: lload 15
      //   445: invokestatic 136	android/os/Binder:restoreCallingIdentity	(J)V
      //   448: aload 7
      //   450: astore_3
      //   451: aload_1
      //   452: athrow
      //   453: astore_1
      //   454: aload 7
      //   456: astore_2
      //   457: goto +7 -> 464
      //   460: astore_1
      //   461: aload 7
      //   463: astore_2
      //   464: aload_2
      //   465: astore_3
      //   466: aload_2
      //   467: monitorexit
      //   468: aload_1
      //   469: athrow
      //   470: astore_1
      //   471: aload_3
      //   472: astore_2
      //   473: goto -9 -> 464
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	476	0	this	CameraDeviceCallbacks
      //   0	476	1	paramCameraMetadataNative	CameraMetadataNative
      //   0	476	2	paramCaptureResultExtras	CaptureResultExtras
      //   0	476	3	paramArrayOfPhysicalCaptureResultInfo	PhysicalCaptureResultInfo[]
      //   4	344	4	i	int
      //   10	397	5	l1	long
      //   19	443	7	localObject1	Object
      //   31	394	8	localObject2	Object
      //   87	290	9	localCaptureCallbackHolder1	CameraDeviceImpl.CaptureCallbackHolder
      //   98	257	10	localCaptureRequest	CaptureRequest
      //   115	302	11	bool1	boolean
      //   128	284	12	bool2	boolean
      //   163	36	13	bool3	boolean
      //   271	82	14	localList	List
      //   286	158	15	l2	long
      //   290	52	17	localCaptureCallbackHolder2	CameraDeviceImpl.CaptureCallbackHolder
      //   303	47	18	localRange	Range
      //   314	49	19	localTotalCaptureResult	TotalCaptureResult
      // Exception table:
      //   from	to	target	type
      //   38	41	42	finally
      //   135	155	42	finally
      //   170	190	42	finally
      //   203	214	42	finally
      //   225	254	42	finally
      //   376	387	439	finally
      //   305	333	453	finally
      //   24	33	460	finally
      //   49	100	460	finally
      //   100	114	460	finally
      //   123	130	460	finally
      //   156	165	460	finally
      //   191	198	460	finally
      //   259	288	460	finally
      //   292	305	460	finally
      //   336	362	470	finally
      //   371	376	470	finally
      //   390	395	470	finally
      //   398	416	470	finally
      //   424	432	470	finally
      //   435	438	470	finally
      //   443	448	470	finally
      //   451	453	470	finally
      //   466	468	470	finally
    }
  }
  
  private static class CameraHandlerExecutor
    implements Executor
  {
    private final Handler mHandler;
    
    public CameraHandlerExecutor(Handler paramHandler)
    {
      mHandler = ((Handler)Preconditions.checkNotNull(paramHandler));
    }
    
    public void execute(Runnable paramRunnable)
    {
      mHandler.post(paramRunnable);
    }
  }
  
  public static abstract interface CaptureCallback
  {
    public static final int NO_FRAMES_CAPTURED = -1;
    
    public abstract void onCaptureBufferLost(CameraDevice paramCameraDevice, CaptureRequest paramCaptureRequest, Surface paramSurface, long paramLong);
    
    public abstract void onCaptureCompleted(CameraDevice paramCameraDevice, CaptureRequest paramCaptureRequest, TotalCaptureResult paramTotalCaptureResult);
    
    public abstract void onCaptureFailed(CameraDevice paramCameraDevice, CaptureRequest paramCaptureRequest, CaptureFailure paramCaptureFailure);
    
    public abstract void onCapturePartial(CameraDevice paramCameraDevice, CaptureRequest paramCaptureRequest, CaptureResult paramCaptureResult);
    
    public abstract void onCaptureProgressed(CameraDevice paramCameraDevice, CaptureRequest paramCaptureRequest, CaptureResult paramCaptureResult);
    
    public abstract void onCaptureSequenceAborted(CameraDevice paramCameraDevice, int paramInt);
    
    public abstract void onCaptureSequenceCompleted(CameraDevice paramCameraDevice, int paramInt, long paramLong);
    
    public abstract void onCaptureStarted(CameraDevice paramCameraDevice, CaptureRequest paramCaptureRequest, long paramLong1, long paramLong2);
  }
  
  static class CaptureCallbackHolder
  {
    private final CameraDeviceImpl.CaptureCallback mCallback;
    private final Executor mExecutor;
    private final boolean mHasBatchedOutputs;
    private final boolean mRepeating;
    private final List<CaptureRequest> mRequestList;
    private final int mSessionId;
    
    CaptureCallbackHolder(CameraDeviceImpl.CaptureCallback paramCaptureCallback, List<CaptureRequest> paramList, Executor paramExecutor, boolean paramBoolean, int paramInt)
    {
      if ((paramCaptureCallback != null) && (paramExecutor != null))
      {
        mRepeating = paramBoolean;
        mExecutor = paramExecutor;
        mRequestList = new ArrayList(paramList);
        mCallback = paramCaptureCallback;
        mSessionId = paramInt;
        boolean bool = true;
        for (paramInt = 0;; paramInt++)
        {
          paramBoolean = bool;
          if (paramInt >= paramList.size()) {
            break;
          }
          paramCaptureCallback = (CaptureRequest)paramList.get(paramInt);
          if (!paramCaptureCallback.isPartOfCRequestList())
          {
            paramBoolean = false;
            break;
          }
          if ((paramInt == 0) && (paramCaptureCallback.getTargets().size() != 2))
          {
            paramBoolean = false;
            break;
          }
        }
        mHasBatchedOutputs = paramBoolean;
        return;
      }
      throw new UnsupportedOperationException("Must have a valid handler and a valid callback");
    }
    
    public CameraDeviceImpl.CaptureCallback getCallback()
    {
      return mCallback;
    }
    
    public Executor getExecutor()
    {
      return mExecutor;
    }
    
    public CaptureRequest getRequest()
    {
      return getRequest(0);
    }
    
    public CaptureRequest getRequest(int paramInt)
    {
      if (paramInt < mRequestList.size())
      {
        if (paramInt >= 0) {
          return (CaptureRequest)mRequestList.get(paramInt);
        }
        throw new IllegalArgumentException(String.format("Requested subsequenceId %d is negative", new Object[] { Integer.valueOf(paramInt) }));
      }
      throw new IllegalArgumentException(String.format("Requested subsequenceId %d is larger than request list size %d.", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(mRequestList.size()) }));
    }
    
    public int getRequestCount()
    {
      return mRequestList.size();
    }
    
    public int getSessionId()
    {
      return mSessionId;
    }
    
    public boolean hasBatchedOutputs()
    {
      return mHasBatchedOutputs;
    }
    
    public boolean isRepeating()
    {
      return mRepeating;
    }
  }
  
  public class FrameNumberTracker
  {
    private long mCompletedFrameNumber = -1L;
    private long mCompletedReprocessFrameNumber = -1L;
    private final TreeMap<Long, Boolean> mFutureErrorMap = new TreeMap();
    private final HashMap<Long, List<CaptureResult>> mPartialResults = new HashMap();
    private final LinkedList<Long> mSkippedRegularFrameNumbers = new LinkedList();
    private final LinkedList<Long> mSkippedReprocessFrameNumbers = new LinkedList();
    
    public FrameNumberTracker() {}
    
    private void update()
    {
      Iterator localIterator = mFutureErrorMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Object localObject = (Map.Entry)localIterator.next();
        Long localLong = (Long)((Map.Entry)localObject).getKey();
        Boolean localBoolean = (Boolean)((Map.Entry)localObject).getValue();
        localObject = Boolean.valueOf(true);
        if (localBoolean.booleanValue())
        {
          if (localLong.longValue() == mCompletedReprocessFrameNumber + 1L)
          {
            mCompletedReprocessFrameNumber = localLong.longValue();
          }
          else if ((mSkippedReprocessFrameNumbers.isEmpty() != true) && (localLong == mSkippedReprocessFrameNumbers.element()))
          {
            mCompletedReprocessFrameNumber = localLong.longValue();
            mSkippedReprocessFrameNumbers.remove();
          }
          else
          {
            localObject = Boolean.valueOf(false);
          }
        }
        else if (localLong.longValue() == mCompletedFrameNumber + 1L)
        {
          mCompletedFrameNumber = localLong.longValue();
        }
        else if ((mSkippedRegularFrameNumbers.isEmpty() != true) && (localLong == mSkippedRegularFrameNumbers.element()))
        {
          mCompletedFrameNumber = localLong.longValue();
          mSkippedRegularFrameNumbers.remove();
        }
        else
        {
          localObject = Boolean.valueOf(false);
        }
        if (((Boolean)localObject).booleanValue()) {
          localIterator.remove();
        }
      }
    }
    
    private void updateCompletedFrameNumber(long paramLong)
      throws IllegalArgumentException
    {
      if (paramLong > mCompletedFrameNumber)
      {
        if (paramLong <= mCompletedReprocessFrameNumber)
        {
          if ((mSkippedRegularFrameNumbers.isEmpty() != true) && (paramLong >= ((Long)mSkippedRegularFrameNumbers.element()).longValue()))
          {
            if (paramLong <= ((Long)mSkippedRegularFrameNumbers.element()).longValue())
            {
              mSkippedRegularFrameNumbers.remove();
            }
            else
            {
              localStringBuilder = new StringBuilder();
              localStringBuilder.append("frame number ");
              localStringBuilder.append(paramLong);
              localStringBuilder.append(" comes out of order. Expecting ");
              localStringBuilder.append(mSkippedRegularFrameNumbers.element());
              throw new IllegalArgumentException(localStringBuilder.toString());
            }
          }
          else
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("frame number ");
            localStringBuilder.append(paramLong);
            localStringBuilder.append(" is a repeat");
            throw new IllegalArgumentException(localStringBuilder.toString());
          }
        }
        else {
          for (long l = Math.max(mCompletedFrameNumber, mCompletedReprocessFrameNumber) + 1L; l < paramLong; l += 1L) {
            mSkippedReprocessFrameNumbers.add(Long.valueOf(l));
          }
        }
        mCompletedFrameNumber = paramLong;
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("frame number ");
      localStringBuilder.append(paramLong);
      localStringBuilder.append(" is a repeat");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    private void updateCompletedReprocessFrameNumber(long paramLong)
      throws IllegalArgumentException
    {
      if (paramLong >= mCompletedReprocessFrameNumber)
      {
        if (paramLong < mCompletedFrameNumber)
        {
          if ((mSkippedReprocessFrameNumbers.isEmpty() != true) && (paramLong >= ((Long)mSkippedReprocessFrameNumbers.element()).longValue()))
          {
            if (paramLong <= ((Long)mSkippedReprocessFrameNumbers.element()).longValue())
            {
              mSkippedReprocessFrameNumbers.remove();
            }
            else
            {
              localStringBuilder = new StringBuilder();
              localStringBuilder.append("frame number ");
              localStringBuilder.append(paramLong);
              localStringBuilder.append(" comes out of order. Expecting ");
              localStringBuilder.append(mSkippedReprocessFrameNumbers.element());
              throw new IllegalArgumentException(localStringBuilder.toString());
            }
          }
          else
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("frame number ");
            localStringBuilder.append(paramLong);
            localStringBuilder.append(" is a repeat");
            throw new IllegalArgumentException(localStringBuilder.toString());
          }
        }
        else {
          for (long l = Math.max(mCompletedFrameNumber, mCompletedReprocessFrameNumber) + 1L; l < paramLong; l += 1L) {
            mSkippedRegularFrameNumbers.add(Long.valueOf(l));
          }
        }
        mCompletedReprocessFrameNumber = paramLong;
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("frame number ");
      localStringBuilder.append(paramLong);
      localStringBuilder.append(" is a repeat");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public long getCompletedFrameNumber()
    {
      return mCompletedFrameNumber;
    }
    
    public long getCompletedReprocessFrameNumber()
    {
      return mCompletedReprocessFrameNumber;
    }
    
    public List<CaptureResult> popPartialResults(long paramLong)
    {
      return (List)mPartialResults.remove(Long.valueOf(paramLong));
    }
    
    public void updateTracker(long paramLong, CaptureResult paramCaptureResult, boolean paramBoolean1, boolean paramBoolean2)
    {
      if (!paramBoolean1)
      {
        updateTracker(paramLong, false, paramBoolean2);
        return;
      }
      if (paramCaptureResult == null) {
        return;
      }
      List localList = (List)mPartialResults.get(Long.valueOf(paramLong));
      Object localObject = localList;
      if (localList == null)
      {
        localObject = new ArrayList();
        mPartialResults.put(Long.valueOf(paramLong), localObject);
      }
      ((List)localObject).add(paramCaptureResult);
    }
    
    public void updateTracker(long paramLong, boolean paramBoolean1, boolean paramBoolean2)
    {
      if (paramBoolean1)
      {
        mFutureErrorMap.put(Long.valueOf(paramLong), Boolean.valueOf(paramBoolean2));
      }
      else
      {
        if (paramBoolean2) {
          try
          {
            updateCompletedReprocessFrameNumber(paramLong);
          }
          catch (IllegalArgumentException localIllegalArgumentException)
          {
            break label50;
          }
        } else {
          updateCompletedFrameNumber(paramLong);
        }
        break label66;
        label50:
        Log.e(TAG, localIllegalArgumentException.getMessage());
      }
      label66:
      update();
    }
  }
  
  static class RequestLastFrameNumbersHolder
  {
    private final long mLastRegularFrameNumber;
    private final long mLastReprocessFrameNumber;
    private final int mRequestId;
    
    public RequestLastFrameNumbersHolder(int paramInt, long paramLong)
    {
      mLastRegularFrameNumber = paramLong;
      mLastReprocessFrameNumber = -1L;
      mRequestId = paramInt;
    }
    
    public RequestLastFrameNumbersHolder(List<CaptureRequest> paramList, SubmitInfo paramSubmitInfo)
    {
      long l1 = -1L;
      long l2 = -1L;
      long l3 = paramSubmitInfo.getLastFrameNumber();
      if (paramSubmitInfo.getLastFrameNumber() >= paramList.size() - 1)
      {
        int i = paramList.size() - 1;
        long l4;
        long l5;
        for (;;)
        {
          l4 = l1;
          l5 = l2;
          if (i < 0) {
            break;
          }
          localObject = (CaptureRequest)paramList.get(i);
          if ((((CaptureRequest)localObject).isReprocess()) && (l2 == -1L))
          {
            l5 = l3;
            l4 = l1;
          }
          else
          {
            l4 = l1;
            l5 = l2;
            if (!((CaptureRequest)localObject).isReprocess())
            {
              l4 = l1;
              l5 = l2;
              if (l1 == -1L)
              {
                l4 = l3;
                l5 = l2;
              }
            }
          }
          if ((l5 != -1L) && (l4 != -1L)) {
            break;
          }
          l3 -= 1L;
          i--;
          l1 = l4;
          l2 = l5;
        }
        mLastRegularFrameNumber = l4;
        mLastReprocessFrameNumber = l5;
        mRequestId = paramSubmitInfo.getRequestId();
        return;
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("lastFrameNumber: ");
      ((StringBuilder)localObject).append(paramSubmitInfo.getLastFrameNumber());
      ((StringBuilder)localObject).append(" should be at least ");
      ((StringBuilder)localObject).append(paramList.size() - 1);
      ((StringBuilder)localObject).append(" for the number of  requests in the list: ");
      ((StringBuilder)localObject).append(paramList.size());
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    
    public long getLastFrameNumber()
    {
      return Math.max(mLastRegularFrameNumber, mLastReprocessFrameNumber);
    }
    
    public long getLastRegularFrameNumber()
    {
      return mLastRegularFrameNumber;
    }
    
    public long getLastReprocessFrameNumber()
    {
      return mLastReprocessFrameNumber;
    }
    
    public int getRequestId()
    {
      return mRequestId;
    }
  }
  
  public static abstract class StateCallbackKK
    extends CameraDevice.StateCallback
  {
    public StateCallbackKK() {}
    
    public void onActive(CameraDevice paramCameraDevice) {}
    
    public void onBusy(CameraDevice paramCameraDevice) {}
    
    public void onIdle(CameraDevice paramCameraDevice) {}
    
    public void onRequestQueueEmpty() {}
    
    public void onSurfacePrepared(Surface paramSurface) {}
    
    public void onUnconfigured(CameraDevice paramCameraDevice) {}
  }
}
