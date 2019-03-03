package android.hardware.camera2.legacy;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.ICameraDeviceCallbacks;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CaptureResultExtras;
import android.hardware.camera2.impl.PhysicalCaptureResultInfo;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.utils.ArrayUtils;
import android.hardware.camera2.utils.SubmitInfo;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.util.SparseArray;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LegacyCameraDevice
  implements AutoCloseable
{
  private static final boolean DEBUG = false;
  private static final int GRALLOC_USAGE_HW_COMPOSER = 2048;
  private static final int GRALLOC_USAGE_HW_RENDER = 512;
  private static final int GRALLOC_USAGE_HW_TEXTURE = 256;
  private static final int GRALLOC_USAGE_HW_VIDEO_ENCODER = 65536;
  private static final int GRALLOC_USAGE_RENDERSCRIPT = 1048576;
  private static final int GRALLOC_USAGE_SW_READ_OFTEN = 3;
  private static final int ILLEGAL_VALUE = -1;
  public static final int MAX_DIMEN_FOR_ROUNDING = 1920;
  public static final int NATIVE_WINDOW_SCALING_MODE_SCALE_TO_WINDOW = 1;
  private final String TAG;
  private final Handler mCallbackHandler;
  private final HandlerThread mCallbackHandlerThread = new HandlerThread("CallbackThread");
  private final int mCameraId;
  private boolean mClosed = false;
  private SparseArray<Surface> mConfiguredSurfaces;
  private final ICameraDeviceCallbacks mDeviceCallbacks;
  private final CameraDeviceState mDeviceState = new CameraDeviceState();
  private final ConditionVariable mIdle = new ConditionVariable(true);
  private final RequestThreadManager mRequestThreadManager;
  private final Handler mResultHandler;
  private final HandlerThread mResultThread = new HandlerThread("ResultThread");
  private final CameraDeviceState.CameraDeviceStateListener mStateListener = new CameraDeviceState.CameraDeviceStateListener()
  {
    public void onBusy()
    {
      mIdle.close();
    }
    
    public void onCaptureResult(final CameraMetadataNative paramAnonymousCameraMetadataNative, final RequestHolder paramAnonymousRequestHolder)
    {
      final CaptureResultExtras localCaptureResultExtras = LegacyCameraDevice.this.getExtrasFromRequest(paramAnonymousRequestHolder);
      mResultHandler.post(new Runnable()
      {
        public void run()
        {
          try
          {
            mDeviceCallbacks.onResultReceived(paramAnonymousCameraMetadataNative, localCaptureResultExtras, new PhysicalCaptureResultInfo[0]);
            return;
          }
          catch (RemoteException localRemoteException)
          {
            throw new IllegalStateException("Received remote exception during onCameraError callback: ", localRemoteException);
          }
        }
      });
    }
    
    public void onCaptureStarted(final RequestHolder paramAnonymousRequestHolder, final long paramAnonymousLong)
    {
      final CaptureResultExtras localCaptureResultExtras = LegacyCameraDevice.this.getExtrasFromRequest(paramAnonymousRequestHolder);
      mResultHandler.post(new Runnable()
      {
        public void run()
        {
          try
          {
            mDeviceCallbacks.onCaptureStarted(localCaptureResultExtras, paramAnonymousLong);
            return;
          }
          catch (RemoteException localRemoteException)
          {
            throw new IllegalStateException("Received remote exception during onCameraError callback: ", localRemoteException);
          }
        }
      });
    }
    
    public void onConfiguring() {}
    
    public void onError(final int paramAnonymousInt, final Object paramAnonymousObject, final RequestHolder paramAnonymousRequestHolder)
    {
      switch (paramAnonymousInt)
      {
      default: 
        break;
      case 0: 
      case 1: 
      case 2: 
        mIdle.open();
      }
      paramAnonymousObject = LegacyCameraDevice.this.getExtrasFromRequest(paramAnonymousRequestHolder, paramAnonymousInt, paramAnonymousObject);
      mResultHandler.post(new Runnable()
      {
        public void run()
        {
          try
          {
            mDeviceCallbacks.onDeviceError(paramAnonymousInt, paramAnonymousObject);
            return;
          }
          catch (RemoteException localRemoteException)
          {
            throw new IllegalStateException("Received remote exception during onCameraError callback: ", localRemoteException);
          }
        }
      });
    }
    
    public void onIdle()
    {
      mIdle.open();
      mResultHandler.post(new Runnable()
      {
        public void run()
        {
          try
          {
            mDeviceCallbacks.onDeviceIdle();
            return;
          }
          catch (RemoteException localRemoteException)
          {
            throw new IllegalStateException("Received remote exception during onCameraIdle callback: ", localRemoteException);
          }
        }
      });
    }
    
    public void onRepeatingRequestError(final long paramAnonymousLong, int paramAnonymousInt)
    {
      mResultHandler.post(new Runnable()
      {
        public void run()
        {
          try
          {
            mDeviceCallbacks.onRepeatingRequestError(paramAnonymousLong, val$repeatingRequestId);
            return;
          }
          catch (RemoteException localRemoteException)
          {
            throw new IllegalStateException("Received remote exception during onRepeatingRequestError callback: ", localRemoteException);
          }
        }
      });
    }
    
    public void onRequestQueueEmpty()
    {
      mResultHandler.post(new Runnable()
      {
        public void run()
        {
          try
          {
            mDeviceCallbacks.onRequestQueueEmpty();
            return;
          }
          catch (RemoteException localRemoteException)
          {
            throw new IllegalStateException("Received remote exception during onRequestQueueEmpty callback: ", localRemoteException);
          }
        }
      });
    }
  };
  private final CameraCharacteristics mStaticCharacteristics;
  
  public LegacyCameraDevice(int paramInt, Camera paramCamera, CameraCharacteristics paramCameraCharacteristics, ICameraDeviceCallbacks paramICameraDeviceCallbacks)
  {
    mCameraId = paramInt;
    mDeviceCallbacks = paramICameraDeviceCallbacks;
    TAG = String.format("CameraDevice-%d-LE", new Object[] { Integer.valueOf(mCameraId) });
    mResultThread.start();
    mResultHandler = new Handler(mResultThread.getLooper());
    mCallbackHandlerThread.start();
    mCallbackHandler = new Handler(mCallbackHandlerThread.getLooper());
    mDeviceState.setCameraDeviceCallbacks(mCallbackHandler, mStateListener);
    mStaticCharacteristics = paramCameraCharacteristics;
    mRequestThreadManager = new RequestThreadManager(paramInt, paramCamera, paramCameraCharacteristics, mDeviceState);
    mRequestThreadManager.start();
  }
  
  static void connectSurface(Surface paramSurface)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    Preconditions.checkNotNull(paramSurface);
    LegacyExceptionUtils.throwOnError(nativeConnectSurface(paramSurface));
  }
  
  static boolean containsSurfaceId(Surface paramSurface, Collection<Long> paramCollection)
  {
    try
    {
      long l = getSurfaceId(paramSurface);
      return paramCollection.contains(Long.valueOf(l));
    }
    catch (LegacyExceptionUtils.BufferQueueAbandonedException paramSurface) {}
    return false;
  }
  
  public static int detectSurfaceDataspace(Surface paramSurface)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    Preconditions.checkNotNull(paramSurface);
    return LegacyExceptionUtils.throwOnError(nativeDetectSurfaceDataspace(paramSurface));
  }
  
  public static int detectSurfaceType(Surface paramSurface)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    Preconditions.checkNotNull(paramSurface);
    int i = nativeDetectSurfaceType(paramSurface);
    int j = i;
    if (i >= 1)
    {
      j = i;
      if (i <= 5) {
        j = 34;
      }
    }
    return LegacyExceptionUtils.throwOnError(j);
  }
  
  static int detectSurfaceUsageFlags(Surface paramSurface)
  {
    Preconditions.checkNotNull(paramSurface);
    return nativeDetectSurfaceUsageFlags(paramSurface);
  }
  
  static void disconnectSurface(Surface paramSurface)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    if (paramSurface == null) {
      return;
    }
    LegacyExceptionUtils.throwOnError(nativeDisconnectSurface(paramSurface));
  }
  
  static Size findClosestSize(Size paramSize, Size[] paramArrayOfSize)
  {
    if ((paramSize != null) && (paramArrayOfSize != null))
    {
      Object localObject1 = null;
      int i = paramArrayOfSize.length;
      int j = 0;
      while (j < i)
      {
        Size localSize = paramArrayOfSize[j];
        if (localSize.equals(paramSize)) {
          return paramSize;
        }
        Object localObject2 = localObject1;
        if (localSize.getWidth() <= 1920) {
          if (localObject1 != null)
          {
            localObject2 = localObject1;
            if (findEuclidDistSquare(paramSize, localSize) >= findEuclidDistSquare((Size)localObject1, localSize)) {}
          }
          else
          {
            localObject2 = localSize;
          }
        }
        j++;
        localObject1 = localObject2;
      }
      return localObject1;
    }
    return null;
  }
  
  static long findEuclidDistSquare(Size paramSize1, Size paramSize2)
  {
    long l1 = paramSize1.getWidth() - paramSize2.getWidth();
    long l2 = paramSize1.getHeight() - paramSize2.getHeight();
    return l1 * l1 + l2 * l2;
  }
  
  private CaptureResultExtras getExtrasFromRequest(RequestHolder paramRequestHolder)
  {
    return getExtrasFromRequest(paramRequestHolder, -1, null);
  }
  
  private CaptureResultExtras getExtrasFromRequest(RequestHolder paramRequestHolder, int paramInt, Object paramObject)
  {
    int i = -1;
    int j = i;
    if (paramInt == 5)
    {
      paramObject = (Surface)paramObject;
      paramInt = mConfiguredSurfaces.indexOfValue(paramObject);
      if (paramInt < 0)
      {
        Log.e(TAG, "Buffer drop error reported for unknown Surface");
        j = i;
      }
      else
      {
        j = mConfiguredSurfaces.keyAt(paramInt);
      }
    }
    if (paramRequestHolder == null) {
      return new CaptureResultExtras(-1, -1, -1, -1, -1L, -1, -1);
    }
    return new CaptureResultExtras(paramRequestHolder.getRequestId(), paramRequestHolder.getSubsequeceId(), 0, 0, paramRequestHolder.getFrameNumber(), 1, j);
  }
  
  public static long getSurfaceId(Surface paramSurface)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    Preconditions.checkNotNull(paramSurface);
    try
    {
      long l = nativeGetSurfaceId(paramSurface);
      return l;
    }
    catch (IllegalArgumentException paramSurface)
    {
      throw new LegacyExceptionUtils.BufferQueueAbandonedException();
    }
  }
  
  static List<Long> getSurfaceIds(SparseArray<Surface> paramSparseArray)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    if (paramSparseArray != null)
    {
      ArrayList localArrayList = new ArrayList();
      int i = paramSparseArray.size();
      int j = 0;
      while (j < i)
      {
        long l = getSurfaceId((Surface)paramSparseArray.valueAt(j));
        if (l != 0L)
        {
          localArrayList.add(Long.valueOf(l));
          j++;
        }
        else
        {
          throw new IllegalStateException("Configured surface had null native GraphicBufferProducer pointer!");
        }
      }
      return localArrayList;
    }
    throw new NullPointerException("Null argument surfaces");
  }
  
  static List<Long> getSurfaceIds(Collection<Surface> paramCollection)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    if (paramCollection != null)
    {
      ArrayList localArrayList = new ArrayList();
      paramCollection = paramCollection.iterator();
      while (paramCollection.hasNext())
      {
        long l = getSurfaceId((Surface)paramCollection.next());
        if (l != 0L) {
          localArrayList.add(Long.valueOf(l));
        } else {
          throw new IllegalStateException("Configured surface had null native GraphicBufferProducer pointer!");
        }
      }
      return localArrayList;
    }
    throw new NullPointerException("Null argument surfaces");
  }
  
  public static Size getSurfaceSize(Surface paramSurface)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    Preconditions.checkNotNull(paramSurface);
    int[] arrayOfInt = new int[2];
    LegacyExceptionUtils.throwOnError(nativeDetectSurfaceDimens(paramSurface, arrayOfInt));
    return new Size(arrayOfInt[0], arrayOfInt[1]);
  }
  
  static Size getTextureSize(SurfaceTexture paramSurfaceTexture)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    Preconditions.checkNotNull(paramSurfaceTexture);
    int[] arrayOfInt = new int[2];
    LegacyExceptionUtils.throwOnError(nativeDetectTextureDimens(paramSurfaceTexture, arrayOfInt));
    return new Size(arrayOfInt[0], arrayOfInt[1]);
  }
  
  public static boolean isFlexibleConsumer(Surface paramSurface)
  {
    int i = detectSurfaceUsageFlags(paramSurface);
    boolean bool;
    if (((i & 0x110000) == 0) && ((i & 0x903) != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isPreviewConsumer(Surface paramSurface)
  {
    int i = detectSurfaceUsageFlags(paramSurface);
    boolean bool;
    if (((i & 0x110003) == 0) && ((i & 0xB00) != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    try
    {
      detectSurfaceType(paramSurface);
      return bool;
    }
    catch (LegacyExceptionUtils.BufferQueueAbandonedException paramSurface)
    {
      throw new IllegalArgumentException("Surface was abandoned", paramSurface);
    }
  }
  
  public static boolean isVideoEncoderConsumer(Surface paramSurface)
  {
    int i = detectSurfaceUsageFlags(paramSurface);
    boolean bool;
    if (((i & 0x100903) == 0) && ((i & 0x10000) != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    try
    {
      detectSurfaceType(paramSurface);
      return bool;
    }
    catch (LegacyExceptionUtils.BufferQueueAbandonedException paramSurface)
    {
      throw new IllegalArgumentException("Surface was abandoned", paramSurface);
    }
  }
  
  private static native int nativeConnectSurface(Surface paramSurface);
  
  private static native int nativeDetectSurfaceDataspace(Surface paramSurface);
  
  private static native int nativeDetectSurfaceDimens(Surface paramSurface, int[] paramArrayOfInt);
  
  private static native int nativeDetectSurfaceType(Surface paramSurface);
  
  private static native int nativeDetectSurfaceUsageFlags(Surface paramSurface);
  
  private static native int nativeDetectTextureDimens(SurfaceTexture paramSurfaceTexture, int[] paramArrayOfInt);
  
  private static native int nativeDisconnectSurface(Surface paramSurface);
  
  static native int nativeGetJpegFooterSize();
  
  private static native long nativeGetSurfaceId(Surface paramSurface);
  
  private static native int nativeProduceFrame(Surface paramSurface, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3);
  
  private static native int nativeSetNextTimestamp(Surface paramSurface, long paramLong);
  
  private static native int nativeSetScalingMode(Surface paramSurface, int paramInt);
  
  private static native int nativeSetSurfaceDimens(Surface paramSurface, int paramInt1, int paramInt2);
  
  private static native int nativeSetSurfaceFormat(Surface paramSurface, int paramInt);
  
  private static native int nativeSetSurfaceOrientation(Surface paramSurface, int paramInt1, int paramInt2);
  
  static boolean needsConversion(Surface paramSurface)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    int i = detectSurfaceType(paramSurface);
    boolean bool;
    if ((i != 35) && (i != 842094169) && (i != 17)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static void produceFrame(Surface paramSurface, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    Preconditions.checkNotNull(paramSurface);
    Preconditions.checkNotNull(paramArrayOfByte);
    Preconditions.checkArgumentPositive(paramInt1, "width must be positive.");
    Preconditions.checkArgumentPositive(paramInt2, "height must be positive.");
    LegacyExceptionUtils.throwOnError(nativeProduceFrame(paramSurface, paramArrayOfByte, paramInt1, paramInt2, paramInt3));
  }
  
  static void setNextTimestamp(Surface paramSurface, long paramLong)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    Preconditions.checkNotNull(paramSurface);
    LegacyExceptionUtils.throwOnError(nativeSetNextTimestamp(paramSurface, paramLong));
  }
  
  static void setScalingMode(Surface paramSurface, int paramInt)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    Preconditions.checkNotNull(paramSurface);
    LegacyExceptionUtils.throwOnError(nativeSetScalingMode(paramSurface, paramInt));
  }
  
  static void setSurfaceDimens(Surface paramSurface, int paramInt1, int paramInt2)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    Preconditions.checkNotNull(paramSurface);
    Preconditions.checkArgumentPositive(paramInt1, "width must be positive.");
    Preconditions.checkArgumentPositive(paramInt2, "height must be positive.");
    LegacyExceptionUtils.throwOnError(nativeSetSurfaceDimens(paramSurface, paramInt1, paramInt2));
  }
  
  static void setSurfaceFormat(Surface paramSurface, int paramInt)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    Preconditions.checkNotNull(paramSurface);
    LegacyExceptionUtils.throwOnError(nativeSetSurfaceFormat(paramSurface, paramInt));
  }
  
  static void setSurfaceOrientation(Surface paramSurface, int paramInt1, int paramInt2)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    Preconditions.checkNotNull(paramSurface);
    LegacyExceptionUtils.throwOnError(nativeSetSurfaceOrientation(paramSurface, paramInt1, paramInt2));
  }
  
  public long cancelRequest(int paramInt)
  {
    return mRequestThreadManager.cancelRepeating(paramInt);
  }
  
  public void close()
  {
    mRequestThreadManager.quit();
    mCallbackHandlerThread.quitSafely();
    mResultThread.quitSafely();
    try
    {
      mCallbackHandlerThread.join();
    }
    catch (InterruptedException localInterruptedException1)
    {
      Log.e(TAG, String.format("Thread %s (%d) interrupted while quitting.", new Object[] { mCallbackHandlerThread.getName(), Long.valueOf(mCallbackHandlerThread.getId()) }));
    }
    try
    {
      mResultThread.join();
    }
    catch (InterruptedException localInterruptedException2)
    {
      Log.e(TAG, String.format("Thread %s (%d) interrupted while quitting.", new Object[] { mResultThread.getName(), Long.valueOf(mResultThread.getId()) }));
    }
    mClosed = true;
  }
  
  public int configureOutputs(SparseArray<Surface> paramSparseArray)
  {
    ArrayList localArrayList = new ArrayList();
    if (paramSparseArray != null)
    {
      int i = paramSparseArray.size();
      int j = 0;
      while (j < i)
      {
        Surface localSurface = (Surface)paramSparseArray.valueAt(j);
        if (localSurface == null)
        {
          Log.e(TAG, "configureOutputs - null outputs are not allowed");
          return LegacyExceptionUtils.BAD_VALUE;
        }
        if (!localSurface.isValid())
        {
          Log.e(TAG, "configureOutputs - invalid output surfaces are not allowed");
          return LegacyExceptionUtils.BAD_VALUE;
        }
        Object localObject1 = (StreamConfigurationMap)mStaticCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        try
        {
          Object localObject2 = getSurfaceSize(localSurface);
          int k = detectSurfaceType(localSurface);
          bool = isFlexibleConsumer(localSurface);
          Object localObject3 = ((StreamConfigurationMap)localObject1).getOutputSizes(k);
          Object localObject4 = localObject3;
          if (localObject3 == null) {
            if (k == 34)
            {
              localObject4 = ((StreamConfigurationMap)localObject1).getOutputSizes(35);
            }
            else
            {
              localObject4 = localObject3;
              if (k == 33) {
                localObject4 = ((StreamConfigurationMap)localObject1).getOutputSizes(256);
              }
            }
          }
          if (!ArrayUtils.contains((Object[])localObject4, localObject2))
          {
            localObject3 = localObject2;
            if (bool)
            {
              localObject1 = findClosestSize((Size)localObject2, (Size[])localObject4);
              localObject2 = localObject1;
              localObject3 = localObject2;
              if (localObject1 != null)
              {
                localObject4 = new android/util/Pair;
                ((Pair)localObject4).<init>(localSurface, localObject2);
                localArrayList.add(localObject4);
                break label363;
              }
            }
            if (localObject4 == null)
            {
              paramSparseArray = "format is invalid.";
            }
            else
            {
              paramSparseArray = new java/lang/StringBuilder;
              paramSparseArray.<init>();
              paramSparseArray.append("size not in valid set: ");
              paramSparseArray.append(Arrays.toString((Object[])localObject4));
              paramSparseArray = paramSparseArray.toString();
            }
            Log.e(TAG, String.format("Surface with size (w=%d, h=%d) and format 0x%x is not valid, %s", new Object[] { Integer.valueOf(((Size)localObject3).getWidth()), Integer.valueOf(((Size)localObject3).getHeight()), Integer.valueOf(k), paramSparseArray }));
            return LegacyExceptionUtils.BAD_VALUE;
          }
          else
          {
            localObject4 = new android/util/Pair;
            ((Pair)localObject4).<init>(localSurface, localObject2);
            localArrayList.add(localObject4);
          }
          label363:
          setSurfaceDimens(localSurface, ((Size)localObject2).getWidth(), ((Size)localObject2).getHeight());
          j++;
        }
        catch (LegacyExceptionUtils.BufferQueueAbandonedException paramSparseArray)
        {
          Log.e(TAG, "Surface bufferqueue is abandoned, cannot configure as output: ", paramSparseArray);
          return LegacyExceptionUtils.BAD_VALUE;
        }
      }
    }
    boolean bool = false;
    if (mDeviceState.setConfiguring())
    {
      mRequestThreadManager.configure(localArrayList);
      bool = mDeviceState.setIdle();
    }
    if (bool)
    {
      mConfiguredSurfaces = paramSparseArray;
      return 0;
    }
    return LegacyExceptionUtils.INVALID_OPERATION;
  }
  
  /* Error */
  protected void finalize()
    throws java.lang.Throwable
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 529	android/hardware/camera2/legacy/LegacyCameraDevice:close	()V
    //   4: aload_0
    //   5: invokespecial 531	java/lang/Object:finalize	()V
    //   8: goto +50 -> 58
    //   11: astore_1
    //   12: goto +47 -> 59
    //   15: astore_2
    //   16: aload_0
    //   17: getfield 124	android/hardware/camera2/legacy/LegacyCameraDevice:TAG	Ljava/lang/String;
    //   20: astore_3
    //   21: new 484	java/lang/StringBuilder
    //   24: astore_1
    //   25: aload_1
    //   26: invokespecial 485	java/lang/StringBuilder:<init>	()V
    //   29: aload_1
    //   30: ldc_w 533
    //   33: invokevirtual 491	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   36: pop
    //   37: aload_1
    //   38: aload_2
    //   39: invokevirtual 536	android/os/ServiceSpecificException:getMessage	()Ljava/lang/String;
    //   42: invokevirtual 491	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   45: pop
    //   46: aload_3
    //   47: aload_1
    //   48: invokevirtual 499	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   51: invokestatic 264	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   54: pop
    //   55: goto -51 -> 4
    //   58: return
    //   59: aload_0
    //   60: invokespecial 531	java/lang/Object:finalize	()V
    //   63: aload_1
    //   64: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	65	0	this	LegacyCameraDevice
    //   11	1	1	localObject	Object
    //   24	40	1	localStringBuilder	StringBuilder
    //   15	24	2	localServiceSpecificException	ServiceSpecificException
    //   20	27	3	str	String
    // Exception table:
    //   from	to	target	type
    //   0	4	11	finally
    //   16	55	11	finally
    //   0	4	15	android/os/ServiceSpecificException
  }
  
  public long flush()
  {
    long l = mRequestThreadManager.flush();
    waitUntilIdle();
    return l;
  }
  
  public boolean isClosed()
  {
    return mClosed;
  }
  
  public SubmitInfo submitRequest(CaptureRequest paramCaptureRequest, boolean paramBoolean)
  {
    return submitRequestList(new CaptureRequest[] { paramCaptureRequest }, paramBoolean);
  }
  
  public SubmitInfo submitRequestList(CaptureRequest[] paramArrayOfCaptureRequest, boolean paramBoolean)
  {
    if ((paramArrayOfCaptureRequest != null) && (paramArrayOfCaptureRequest.length != 0)) {
      try
      {
        Object localObject1;
        if (mConfiguredSurfaces == null) {
          localObject1 = new ArrayList();
        } else {
          localObject1 = getSurfaceIds(mConfiguredSurfaces);
        }
        int i = paramArrayOfCaptureRequest.length;
        int j = 0;
        while (j < i)
        {
          Object localObject2 = paramArrayOfCaptureRequest[j];
          if (!((CaptureRequest)localObject2).getTargets().isEmpty())
          {
            localObject2 = ((CaptureRequest)localObject2).getTargets().iterator();
            while (((Iterator)localObject2).hasNext())
            {
              Surface localSurface = (Surface)((Iterator)localObject2).next();
              if (localSurface != null)
              {
                if (mConfiguredSurfaces != null)
                {
                  if (!containsSurfaceId(localSurface, (Collection)localObject1))
                  {
                    Log.e(TAG, "submitRequestList - cannot use a surface that wasn't configured");
                    throw new ServiceSpecificException(LegacyExceptionUtils.BAD_VALUE, "submitRequestList - cannot use a surface that wasn't configured");
                  }
                }
                else
                {
                  Log.e(TAG, "submitRequestList - must configure  device with valid surfaces before submitting requests");
                  throw new ServiceSpecificException(LegacyExceptionUtils.INVALID_OPERATION, "submitRequestList - must configure  device with valid surfaces before submitting requests");
                }
              }
              else
              {
                Log.e(TAG, "submitRequestList - Null Surface targets are not allowed");
                throw new ServiceSpecificException(LegacyExceptionUtils.BAD_VALUE, "submitRequestList - Null Surface targets are not allowed");
              }
            }
            j++;
          }
          else
          {
            Log.e(TAG, "submitRequestList - Each request must have at least one Surface target");
            throw new ServiceSpecificException(LegacyExceptionUtils.BAD_VALUE, "submitRequestList - Each request must have at least one Surface target");
          }
        }
        mIdle.close();
        return mRequestThreadManager.submitCaptureRequests(paramArrayOfCaptureRequest, paramBoolean);
      }
      catch (LegacyExceptionUtils.BufferQueueAbandonedException paramArrayOfCaptureRequest)
      {
        throw new ServiceSpecificException(LegacyExceptionUtils.BAD_VALUE, "submitRequestList - configured surface is abandoned.");
      }
    }
    Log.e(TAG, "submitRequestList - Empty/null requests are not allowed");
    throw new ServiceSpecificException(LegacyExceptionUtils.BAD_VALUE, "submitRequestList - Empty/null requests are not allowed");
  }
  
  public void waitUntilIdle()
  {
    mIdle.block();
  }
}
