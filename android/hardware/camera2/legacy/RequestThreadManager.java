package android.hardware.camera2.legacy;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.utils.SizeAreaComparator;
import android.hardware.camera2.utils.SubmitInfo;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RequestThreadManager
{
  private static final float ASPECT_RATIO_TOLERANCE = 0.01F;
  private static final boolean DEBUG = false;
  private static final int JPEG_FRAME_TIMEOUT = 4000;
  private static final int MAX_IN_FLIGHT_REQUESTS = 2;
  private static final int MSG_CLEANUP = 3;
  private static final int MSG_CONFIGURE_OUTPUTS = 1;
  private static final int MSG_SUBMIT_CAPTURE_REQUEST = 2;
  private static final int PREVIEW_FRAME_TIMEOUT = 1000;
  private static final int REQUEST_COMPLETE_TIMEOUT = 4000;
  private static final boolean USE_BLOB_FORMAT_OVERRIDE = true;
  private static final boolean VERBOSE = false;
  private final String TAG;
  private final List<Surface> mCallbackOutputs = new ArrayList();
  private Camera mCamera;
  private final int mCameraId;
  private final CaptureCollector mCaptureCollector;
  private final CameraCharacteristics mCharacteristics;
  private final CameraDeviceState mDeviceState;
  private Surface mDummySurface;
  private SurfaceTexture mDummyTexture;
  private final Camera.ErrorCallback mErrorCallback = new Camera.ErrorCallback()
  {
    public void onError(int paramAnonymousInt, Camera paramAnonymousCamera)
    {
      switch (paramAnonymousInt)
      {
      default: 
        paramAnonymousCamera = TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Received error ");
        localStringBuilder.append(paramAnonymousInt);
        localStringBuilder.append(" from the Camera1 ErrorCallback");
        Log.e(paramAnonymousCamera, localStringBuilder.toString());
        mDeviceState.setError(1);
        break;
      case 3: 
        flush();
        mDeviceState.setError(6);
        break;
      case 2: 
        flush();
        mDeviceState.setError(0);
      }
    }
  };
  private final LegacyFaceDetectMapper mFaceDetectMapper;
  private final LegacyFocusStateMapper mFocusStateMapper;
  private GLThreadManager mGLThreadManager;
  private final Object mIdleLock = new Object();
  private Size mIntermediateBufferSize;
  private final Camera.PictureCallback mJpegCallback = new Camera.PictureCallback()
  {
    public void onPictureTaken(byte[] paramAnonymousArrayOfByte, Camera paramAnonymousCamera)
    {
      Log.i(TAG, "Received jpeg.");
      paramAnonymousCamera = mCaptureCollector.jpegProduced();
      if ((paramAnonymousCamera != null) && (first != null))
      {
        Object localObject = (RequestHolder)first;
        long l = ((Long)second).longValue();
        paramAnonymousCamera = ((RequestHolder)localObject).getHolderTargets().iterator();
        while (paramAnonymousCamera.hasNext())
        {
          localObject = (Surface)paramAnonymousCamera.next();
          try
          {
            if (LegacyCameraDevice.containsSurfaceId((Surface)localObject, mJpegSurfaceIds))
            {
              Log.i(TAG, "Producing jpeg buffer...");
              int i = paramAnonymousArrayOfByte.length;
              int j = LegacyCameraDevice.nativeGetJpegFooterSize();
              LegacyCameraDevice.setNextTimestamp((Surface)localObject, l);
              LegacyCameraDevice.setSurfaceFormat((Surface)localObject, 1);
              j = (int)Math.ceil(Math.sqrt(i + j + 3 & 0xFFFFFFFC)) + 15 & 0xFFFFFFF0;
              LegacyCameraDevice.setSurfaceDimens((Surface)localObject, j, j);
              LegacyCameraDevice.produceFrame((Surface)localObject, paramAnonymousArrayOfByte, j, j, 33);
            }
          }
          catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException)
          {
            Log.w(TAG, "Surface abandoned, dropping frame. ", localBufferQueueAbandonedException);
          }
        }
        mReceivedJpeg.open();
        return;
      }
      Log.e(TAG, "Dropping jpeg frame.");
    }
  };
  private final Camera.ShutterCallback mJpegShutterCallback = new Camera.ShutterCallback()
  {
    public void onShutter()
    {
      mCaptureCollector.jpegCaptured(SystemClock.elapsedRealtimeNanos());
    }
  };
  private final List<Long> mJpegSurfaceIds = new ArrayList();
  private LegacyRequest mLastRequest = null;
  private Camera.Parameters mParams;
  private final FpsCounter mPrevCounter = new FpsCounter("Incoming Preview");
  private final SurfaceTexture.OnFrameAvailableListener mPreviewCallback = new SurfaceTexture.OnFrameAvailableListener()
  {
    public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
    {
      mGLThreadManager.queueNewFrame();
    }
  };
  private final List<Surface> mPreviewOutputs = new ArrayList();
  private boolean mPreviewRunning = false;
  private SurfaceTexture mPreviewTexture;
  private final AtomicBoolean mQuit = new AtomicBoolean(false);
  private final ConditionVariable mReceivedJpeg = new ConditionVariable(false);
  private final FpsCounter mRequestCounter = new FpsCounter("Incoming Requests");
  private final Handler.Callback mRequestHandlerCb = new Handler.Callback()
  {
    private boolean mCleanup = false;
    private final LegacyResultMapper mMapper = new LegacyResultMapper();
    
    /* Error */
    public boolean handleMessage(android.os.Message paramAnonymousMessage)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 23	android/hardware/camera2/legacy/RequestThreadManager$5:mCleanup	Z
      //   4: ifeq +5 -> 9
      //   7: iconst_1
      //   8: ireturn
      //   9: lconst_0
      //   10: lstore_2
      //   11: aload_1
      //   12: getfield 43	android/os/Message:what	I
      //   15: istore 4
      //   17: iload 4
      //   19: iconst_m1
      //   20: if_icmpeq +1493 -> 1513
      //   23: iload 4
      //   25: tableswitch	default:+27->52, 1:+1305->1330, 2:+226->251, 3:+75->100
      //   52: new 45	java/lang/StringBuilder
      //   55: dup
      //   56: invokespecial 46	java/lang/StringBuilder:<init>	()V
      //   59: astore 5
      //   61: aload 5
      //   63: ldc 48
      //   65: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   68: pop
      //   69: aload 5
      //   71: aload_1
      //   72: getfield 43	android/os/Message:what	I
      //   75: invokevirtual 55	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   78: pop
      //   79: aload 5
      //   81: ldc 57
      //   83: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   86: pop
      //   87: new 59	java/lang/AssertionError
      //   90: dup
      //   91: aload 5
      //   93: invokevirtual 63	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   96: invokespecial 66	java/lang/AssertionError:<init>	(Ljava/lang/Object;)V
      //   99: athrow
      //   100: aload_0
      //   101: iconst_1
      //   102: putfield 23	android/hardware/camera2/legacy/RequestThreadManager$5:mCleanup	Z
      //   105: aload_0
      //   106: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   109: invokestatic 70	android/hardware/camera2/legacy/RequestThreadManager:access$200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CaptureCollector;
      //   112: ldc2_w 71
      //   115: getstatic 78	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
      //   118: invokevirtual 84	android/hardware/camera2/legacy/CaptureCollector:waitForEmpty	(JLjava/util/concurrent/TimeUnit;)Z
      //   121: ifne +26 -> 147
      //   124: aload_0
      //   125: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   128: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   131: ldc 90
      //   133: invokestatic 96	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   136: pop
      //   137: aload_0
      //   138: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   141: invokestatic 70	android/hardware/camera2/legacy/RequestThreadManager:access$200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CaptureCollector;
      //   144: invokevirtual 99	android/hardware/camera2/legacy/CaptureCollector:failAll	()V
      //   147: goto +29 -> 176
      //   150: astore_1
      //   151: aload_0
      //   152: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   155: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   158: ldc 101
      //   160: aload_1
      //   161: invokestatic 104	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   164: pop
      //   165: aload_0
      //   166: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   169: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   172: iconst_1
      //   173: invokevirtual 114	android/hardware/camera2/legacy/CameraDeviceState:setError	(I)V
      //   176: aload_0
      //   177: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   180: invokestatic 118	android/hardware/camera2/legacy/RequestThreadManager:access$500	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/GLThreadManager;
      //   183: ifnull +22 -> 205
      //   186: aload_0
      //   187: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   190: invokestatic 118	android/hardware/camera2/legacy/RequestThreadManager:access$500	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/GLThreadManager;
      //   193: invokevirtual 123	android/hardware/camera2/legacy/GLThreadManager:quit	()V
      //   196: aload_0
      //   197: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   200: aconst_null
      //   201: invokestatic 127	android/hardware/camera2/legacy/RequestThreadManager:access$502	(Landroid/hardware/camera2/legacy/RequestThreadManager;Landroid/hardware/camera2/legacy/GLThreadManager;)Landroid/hardware/camera2/legacy/GLThreadManager;
      //   204: pop
      //   205: aload_0
      //   206: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   209: invokestatic 131	android/hardware/camera2/legacy/RequestThreadManager:access$1300	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/Camera;
      //   212: ifnull +22 -> 234
      //   215: aload_0
      //   216: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   219: invokestatic 131	android/hardware/camera2/legacy/RequestThreadManager:access$1300	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/Camera;
      //   222: invokevirtual 136	android/hardware/Camera:release	()V
      //   225: aload_0
      //   226: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   229: aconst_null
      //   230: invokestatic 140	android/hardware/camera2/legacy/RequestThreadManager:access$1302	(Landroid/hardware/camera2/legacy/RequestThreadManager;Landroid/hardware/Camera;)Landroid/hardware/Camera;
      //   233: pop
      //   234: aload_0
      //   235: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   238: aload_0
      //   239: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   242: invokestatic 144	android/hardware/camera2/legacy/RequestThreadManager:access$1900	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/util/List;
      //   245: invokestatic 148	android/hardware/camera2/legacy/RequestThreadManager:access$2000	(Landroid/hardware/camera2/legacy/RequestThreadManager;Ljava/util/Collection;)V
      //   248: goto +162 -> 410
      //   251: aload_0
      //   252: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   255: invokestatic 152	android/hardware/camera2/legacy/RequestThreadManager:access$700	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/RequestHandlerThread;
      //   258: invokevirtual 158	android/hardware/camera2/legacy/RequestHandlerThread:getHandler	()Landroid/os/Handler;
      //   261: astore 6
      //   263: iconst_0
      //   264: istore 4
      //   266: aload_0
      //   267: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   270: invokestatic 162	android/hardware/camera2/legacy/RequestThreadManager:access$800	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/RequestQueue;
      //   273: invokevirtual 168	android/hardware/camera2/legacy/RequestQueue:getNext	()Landroid/hardware/camera2/legacy/RequestQueue$RequestQueueEntry;
      //   276: astore 5
      //   278: aload 5
      //   280: astore_1
      //   281: aload 5
      //   283: ifnonnull +130 -> 413
      //   286: aload_0
      //   287: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   290: invokestatic 70	android/hardware/camera2/legacy/RequestThreadManager:access$200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CaptureCollector;
      //   293: ldc2_w 71
      //   296: getstatic 78	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
      //   299: invokevirtual 84	android/hardware/camera2/legacy/CaptureCollector:waitForEmpty	(JLjava/util/concurrent/TimeUnit;)Z
      //   302: ifne +26 -> 328
      //   305: aload_0
      //   306: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   309: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   312: ldc -86
      //   314: invokestatic 96	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   317: pop
      //   318: aload_0
      //   319: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   322: invokestatic 70	android/hardware/camera2/legacy/RequestThreadManager:access$200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CaptureCollector;
      //   325: invokevirtual 99	android/hardware/camera2/legacy/CaptureCollector:failAll	()V
      //   328: aload_0
      //   329: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   332: invokestatic 174	android/hardware/camera2/legacy/RequestThreadManager:access$900	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/Object;
      //   335: astore 5
      //   337: aload 5
      //   339: monitorenter
      //   340: aload_0
      //   341: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   344: invokestatic 162	android/hardware/camera2/legacy/RequestThreadManager:access$800	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/RequestQueue;
      //   347: invokevirtual 168	android/hardware/camera2/legacy/RequestQueue:getNext	()Landroid/hardware/camera2/legacy/RequestQueue$RequestQueueEntry;
      //   350: astore_1
      //   351: aload_1
      //   352: ifnonnull +20 -> 372
      //   355: aload_0
      //   356: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   359: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   362: invokevirtual 178	android/hardware/camera2/legacy/CameraDeviceState:setIdle	()Z
      //   365: pop
      //   366: aload 5
      //   368: monitorexit
      //   369: goto +41 -> 410
      //   372: aload 5
      //   374: monitorexit
      //   375: goto +38 -> 413
      //   378: astore_1
      //   379: aload 5
      //   381: monitorexit
      //   382: aload_1
      //   383: athrow
      //   384: astore_1
      //   385: aload_0
      //   386: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   389: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   392: ldc 101
      //   394: aload_1
      //   395: invokestatic 104	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   398: pop
      //   399: aload_0
      //   400: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   403: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   406: iconst_1
      //   407: invokevirtual 114	android/hardware/camera2/legacy/CameraDeviceState:setError	(I)V
      //   410: goto +1103 -> 1513
      //   413: aload_1
      //   414: ifnull +27 -> 441
      //   417: aload 6
      //   419: iconst_2
      //   420: invokevirtual 184	android/os/Handler:sendEmptyMessage	(I)Z
      //   423: pop
      //   424: aload_1
      //   425: invokevirtual 189	android/hardware/camera2/legacy/RequestQueue$RequestQueueEntry:isQueueEmpty	()Z
      //   428: ifeq +13 -> 441
      //   431: aload_0
      //   432: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   435: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   438: invokevirtual 192	android/hardware/camera2/legacy/CameraDeviceState:setRequestQueueEmpty	()V
      //   441: aload_1
      //   442: invokevirtual 196	android/hardware/camera2/legacy/RequestQueue$RequestQueueEntry:getBurstHolder	()Landroid/hardware/camera2/legacy/BurstHolder;
      //   445: astore 5
      //   447: aload 5
      //   449: aload_1
      //   450: invokevirtual 200	android/hardware/camera2/legacy/RequestQueue$RequestQueueEntry:getFrameNumber	()Ljava/lang/Long;
      //   453: invokevirtual 206	java/lang/Long:longValue	()J
      //   456: invokevirtual 212	android/hardware/camera2/legacy/BurstHolder:produceRequestHolders	(J)Ljava/util/List;
      //   459: invokeinterface 218 1 0
      //   464: astore 6
      //   466: aload 6
      //   468: invokeinterface 223 1 0
      //   473: ifeq +812 -> 1285
      //   476: aload 6
      //   478: invokeinterface 227 1 0
      //   483: checkcast 229	android/hardware/camera2/legacy/RequestHolder
      //   486: astore_1
      //   487: aload_1
      //   488: invokevirtual 233	android/hardware/camera2/legacy/RequestHolder:getRequest	()Landroid/hardware/camera2/CaptureRequest;
      //   491: astore 7
      //   493: iconst_0
      //   494: istore 8
      //   496: iconst_0
      //   497: istore 9
      //   499: aload_0
      //   500: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   503: invokestatic 237	android/hardware/camera2/legacy/RequestThreadManager:access$1000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/LegacyRequest;
      //   506: ifnull +24 -> 530
      //   509: aload_0
      //   510: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   513: invokestatic 237	android/hardware/camera2/legacy/RequestThreadManager:access$1000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/LegacyRequest;
      //   516: getfield 243	android/hardware/camera2/legacy/LegacyRequest:captureRequest	Landroid/hardware/camera2/CaptureRequest;
      //   519: aload 7
      //   521: if_acmpeq +6 -> 527
      //   524: goto +6 -> 530
      //   527: goto +158 -> 685
      //   530: aload_0
      //   531: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   534: invokestatic 247	android/hardware/camera2/legacy/RequestThreadManager:access$1100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/Camera$Parameters;
      //   537: invokevirtual 253	android/hardware/Camera$Parameters:getPreviewSize	()Landroid/hardware/Camera$Size;
      //   540: invokestatic 259	android/hardware/camera2/legacy/ParameterUtils:convertSize	(Landroid/hardware/Camera$Size;)Landroid/util/Size;
      //   543: astore 10
      //   545: new 239	android/hardware/camera2/legacy/LegacyRequest
      //   548: dup
      //   549: aload_0
      //   550: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   553: invokestatic 263	android/hardware/camera2/legacy/RequestThreadManager:access$1200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/CameraCharacteristics;
      //   556: aload 7
      //   558: aload 10
      //   560: aload_0
      //   561: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   564: invokestatic 247	android/hardware/camera2/legacy/RequestThreadManager:access$1100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/Camera$Parameters;
      //   567: invokespecial 266	android/hardware/camera2/legacy/LegacyRequest:<init>	(Landroid/hardware/camera2/CameraCharacteristics;Landroid/hardware/camera2/CaptureRequest;Landroid/util/Size;Landroid/hardware/Camera$Parameters;)V
      //   570: astore 10
      //   572: aload 10
      //   574: invokestatic 272	android/hardware/camera2/legacy/LegacyMetadataMapper:convertRequestMetadata	(Landroid/hardware/camera2/legacy/LegacyRequest;)V
      //   577: aload_0
      //   578: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   581: invokestatic 247	android/hardware/camera2/legacy/RequestThreadManager:access$1100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/Camera$Parameters;
      //   584: aload 10
      //   586: getfield 276	android/hardware/camera2/legacy/LegacyRequest:parameters	Landroid/hardware/Camera$Parameters;
      //   589: invokevirtual 280	android/hardware/Camera$Parameters:same	(Landroid/hardware/Camera$Parameters;)Z
      //   592: ifne +76 -> 668
      //   595: aload_0
      //   596: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   599: invokestatic 131	android/hardware/camera2/legacy/RequestThreadManager:access$1300	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/Camera;
      //   602: aload 10
      //   604: getfield 276	android/hardware/camera2/legacy/LegacyRequest:parameters	Landroid/hardware/Camera$Parameters;
      //   607: invokevirtual 284	android/hardware/Camera:setParameters	(Landroid/hardware/Camera$Parameters;)V
      //   610: aload_0
      //   611: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   614: aload 10
      //   616: getfield 276	android/hardware/camera2/legacy/LegacyRequest:parameters	Landroid/hardware/Camera$Parameters;
      //   619: invokestatic 288	android/hardware/camera2/legacy/RequestThreadManager:access$1102	(Landroid/hardware/camera2/legacy/RequestThreadManager;Landroid/hardware/Camera$Parameters;)Landroid/hardware/Camera$Parameters;
      //   622: pop
      //   623: iconst_1
      //   624: istore 9
      //   626: goto +46 -> 672
      //   629: astore 7
      //   631: aload_0
      //   632: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   635: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   638: ldc_w 290
      //   641: aload 7
      //   643: invokestatic 104	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   646: pop
      //   647: aload_1
      //   648: invokevirtual 293	android/hardware/camera2/legacy/RequestHolder:failRequest	()V
      //   651: aload_0
      //   652: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   655: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   658: aload_1
      //   659: lconst_0
      //   660: iconst_3
      //   661: invokevirtual 297	android/hardware/camera2/legacy/CameraDeviceState:setCaptureStart	(Landroid/hardware/camera2/legacy/RequestHolder;JI)Z
      //   664: pop
      //   665: goto +91 -> 756
      //   668: iload 8
      //   670: istore 9
      //   672: aload_0
      //   673: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   676: aload 10
      //   678: invokestatic 301	android/hardware/camera2/legacy/RequestThreadManager:access$1002	(Landroid/hardware/camera2/legacy/RequestThreadManager;Landroid/hardware/camera2/legacy/LegacyRequest;)Landroid/hardware/camera2/legacy/LegacyRequest;
      //   681: pop
      //   682: goto -155 -> 527
      //   685: aload_0
      //   686: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   689: invokestatic 70	android/hardware/camera2/legacy/RequestThreadManager:access$200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CaptureCollector;
      //   692: astore 10
      //   694: aload_0
      //   695: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   698: invokestatic 237	android/hardware/camera2/legacy/RequestThreadManager:access$1000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/LegacyRequest;
      //   701: astore 11
      //   703: getstatic 78	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
      //   706: astore 12
      //   708: aload 10
      //   710: aload_1
      //   711: aload 11
      //   713: ldc2_w 71
      //   716: aload 12
      //   718: invokevirtual 305	android/hardware/camera2/legacy/CaptureCollector:queueRequest	(Landroid/hardware/camera2/legacy/RequestHolder;Landroid/hardware/camera2/legacy/LegacyRequest;JLjava/util/concurrent/TimeUnit;)Z
      //   721: ifne +38 -> 759
      //   724: aload_0
      //   725: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   728: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   731: ldc_w 307
      //   734: invokestatic 96	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   737: pop
      //   738: aload_1
      //   739: invokevirtual 293	android/hardware/camera2/legacy/RequestHolder:failRequest	()V
      //   742: aload_0
      //   743: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   746: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   749: aload_1
      //   750: lconst_0
      //   751: iconst_3
      //   752: invokevirtual 297	android/hardware/camera2/legacy/CameraDeviceState:setCaptureStart	(Landroid/hardware/camera2/legacy/RequestHolder;JI)Z
      //   755: pop
      //   756: goto -290 -> 466
      //   759: aload_1
      //   760: invokevirtual 310	android/hardware/camera2/legacy/RequestHolder:hasPreviewTargets	()Z
      //   763: ifeq +11 -> 774
      //   766: aload_0
      //   767: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   770: aload_1
      //   771: invokestatic 314	android/hardware/camera2/legacy/RequestThreadManager:access$1400	(Landroid/hardware/camera2/legacy/RequestThreadManager;Landroid/hardware/camera2/legacy/RequestHolder;)V
      //   774: aload_1
      //   775: invokevirtual 317	android/hardware/camera2/legacy/RequestHolder:hasJpegTargets	()Z
      //   778: ifeq +67 -> 845
      //   781: aload_0
      //   782: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   785: invokestatic 70	android/hardware/camera2/legacy/RequestThreadManager:access$200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CaptureCollector;
      //   788: ldc2_w 318
      //   791: getstatic 78	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
      //   794: invokevirtual 322	android/hardware/camera2/legacy/CaptureCollector:waitForPreviewsEmpty	(JLjava/util/concurrent/TimeUnit;)Z
      //   797: ifne +30 -> 827
      //   800: aload_0
      //   801: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   804: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   807: ldc_w 324
      //   810: invokestatic 96	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   813: pop
      //   814: aload_0
      //   815: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   818: invokestatic 70	android/hardware/camera2/legacy/RequestThreadManager:access$200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CaptureCollector;
      //   821: invokevirtual 327	android/hardware/camera2/legacy/CaptureCollector:failNextPreview	()V
      //   824: goto -43 -> 781
      //   827: aload_0
      //   828: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   831: invokestatic 331	android/hardware/camera2/legacy/RequestThreadManager:access$400	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/os/ConditionVariable;
      //   834: invokevirtual 336	android/os/ConditionVariable:close	()V
      //   837: aload_0
      //   838: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   841: aload_1
      //   842: invokestatic 339	android/hardware/camera2/legacy/RequestThreadManager:access$1500	(Landroid/hardware/camera2/legacy/RequestThreadManager;Landroid/hardware/camera2/legacy/RequestHolder;)V
      //   845: aload_0
      //   846: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   849: invokestatic 343	android/hardware/camera2/legacy/RequestThreadManager:access$1600	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/LegacyFaceDetectMapper;
      //   852: aload 7
      //   854: aload_0
      //   855: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   858: invokestatic 247	android/hardware/camera2/legacy/RequestThreadManager:access$1100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/Camera$Parameters;
      //   861: invokevirtual 349	android/hardware/camera2/legacy/LegacyFaceDetectMapper:processFaceDetectMode	(Landroid/hardware/camera2/CaptureRequest;Landroid/hardware/Camera$Parameters;)V
      //   864: aload_0
      //   865: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   868: invokestatic 353	android/hardware/camera2/legacy/RequestThreadManager:access$1700	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/LegacyFocusStateMapper;
      //   871: aload 7
      //   873: aload_0
      //   874: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   877: invokestatic 247	android/hardware/camera2/legacy/RequestThreadManager:access$1100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/Camera$Parameters;
      //   880: invokevirtual 358	android/hardware/camera2/legacy/LegacyFocusStateMapper:processRequestTriggers	(Landroid/hardware/camera2/CaptureRequest;Landroid/hardware/Camera$Parameters;)V
      //   883: aload_1
      //   884: invokevirtual 317	android/hardware/camera2/legacy/RequestHolder:hasJpegTargets	()Z
      //   887: ifeq +51 -> 938
      //   890: aload_0
      //   891: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   894: aload_1
      //   895: invokestatic 361	android/hardware/camera2/legacy/RequestThreadManager:access$1800	(Landroid/hardware/camera2/legacy/RequestThreadManager;Landroid/hardware/camera2/legacy/RequestHolder;)V
      //   898: aload_0
      //   899: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   902: invokestatic 331	android/hardware/camera2/legacy/RequestThreadManager:access$400	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/os/ConditionVariable;
      //   905: ldc2_w 71
      //   908: invokevirtual 365	android/os/ConditionVariable:block	(J)Z
      //   911: ifne +27 -> 938
      //   914: aload_0
      //   915: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   918: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   921: ldc_w 367
      //   924: invokestatic 96	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   927: pop
      //   928: aload_0
      //   929: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   932: invokestatic 70	android/hardware/camera2/legacy/RequestThreadManager:access$200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CaptureCollector;
      //   935: invokevirtual 370	android/hardware/camera2/legacy/CaptureCollector:failNextJpeg	()V
      //   938: iload 9
      //   940: ifeq +71 -> 1011
      //   943: aload_0
      //   944: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   947: aload_0
      //   948: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   951: invokestatic 131	android/hardware/camera2/legacy/RequestThreadManager:access$1300	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/Camera;
      //   954: invokevirtual 374	android/hardware/Camera:getParameters	()Landroid/hardware/Camera$Parameters;
      //   957: invokestatic 288	android/hardware/camera2/legacy/RequestThreadManager:access$1102	(Landroid/hardware/camera2/legacy/RequestThreadManager;Landroid/hardware/Camera$Parameters;)Landroid/hardware/Camera$Parameters;
      //   960: pop
      //   961: aload_0
      //   962: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   965: invokestatic 237	android/hardware/camera2/legacy/RequestThreadManager:access$1000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/LegacyRequest;
      //   968: aload_0
      //   969: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   972: invokestatic 247	android/hardware/camera2/legacy/RequestThreadManager:access$1100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/Camera$Parameters;
      //   975: invokevirtual 375	android/hardware/camera2/legacy/LegacyRequest:setParameters	(Landroid/hardware/Camera$Parameters;)V
      //   978: goto +33 -> 1011
      //   981: astore_1
      //   982: aload_0
      //   983: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   986: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   989: ldc_w 377
      //   992: aload_1
      //   993: invokestatic 104	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   996: pop
      //   997: aload_0
      //   998: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1001: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   1004: iconst_1
      //   1005: invokevirtual 114	android/hardware/camera2/legacy/CameraDeviceState:setError	(I)V
      //   1008: goto +277 -> 1285
      //   1011: new 379	android/util/MutableLong
      //   1014: dup
      //   1015: lconst_0
      //   1016: invokespecial 382	android/util/MutableLong:<init>	(J)V
      //   1019: astore 7
      //   1021: aload_0
      //   1022: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1025: invokestatic 70	android/hardware/camera2/legacy/RequestThreadManager:access$200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CaptureCollector;
      //   1028: aload_1
      //   1029: ldc2_w 71
      //   1032: getstatic 78	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
      //   1035: aload 7
      //   1037: invokevirtual 386	android/hardware/camera2/legacy/CaptureCollector:waitForRequestCompleted	(Landroid/hardware/camera2/legacy/RequestHolder;JLjava/util/concurrent/TimeUnit;Landroid/util/MutableLong;)Z
      //   1040: ifne +27 -> 1067
      //   1043: aload_0
      //   1044: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1047: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   1050: ldc_w 388
      //   1053: invokestatic 96	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   1056: pop
      //   1057: aload_0
      //   1058: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1061: invokestatic 70	android/hardware/camera2/legacy/RequestThreadManager:access$200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CaptureCollector;
      //   1064: invokevirtual 99	android/hardware/camera2/legacy/CaptureCollector:failAll	()V
      //   1067: aload_0
      //   1068: getfield 28	android/hardware/camera2/legacy/RequestThreadManager$5:mMapper	Landroid/hardware/camera2/legacy/LegacyResultMapper;
      //   1071: aload_0
      //   1072: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1075: invokestatic 237	android/hardware/camera2/legacy/RequestThreadManager:access$1000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/LegacyRequest;
      //   1078: aload 7
      //   1080: getfield 392	android/util/MutableLong:value	J
      //   1083: invokevirtual 396	android/hardware/camera2/legacy/LegacyResultMapper:cachedConvertResultMetadata	(Landroid/hardware/camera2/legacy/LegacyRequest;J)Landroid/hardware/camera2/impl/CameraMetadataNative;
      //   1086: astore 7
      //   1088: aload_0
      //   1089: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1092: invokestatic 353	android/hardware/camera2/legacy/RequestThreadManager:access$1700	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/LegacyFocusStateMapper;
      //   1095: aload 7
      //   1097: invokevirtual 400	android/hardware/camera2/legacy/LegacyFocusStateMapper:mapResultTriggers	(Landroid/hardware/camera2/impl/CameraMetadataNative;)V
      //   1100: aload_0
      //   1101: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1104: invokestatic 343	android/hardware/camera2/legacy/RequestThreadManager:access$1600	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/LegacyFaceDetectMapper;
      //   1107: aload 7
      //   1109: aload_0
      //   1110: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1113: invokestatic 237	android/hardware/camera2/legacy/RequestThreadManager:access$1000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/LegacyRequest;
      //   1116: invokevirtual 404	android/hardware/camera2/legacy/LegacyFaceDetectMapper:mapResultFaces	(Landroid/hardware/camera2/impl/CameraMetadataNative;Landroid/hardware/camera2/legacy/LegacyRequest;)V
      //   1119: aload_1
      //   1120: invokevirtual 407	android/hardware/camera2/legacy/RequestHolder:requestFailed	()Z
      //   1123: ifne +17 -> 1140
      //   1126: aload_0
      //   1127: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1130: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   1133: aload_1
      //   1134: aload 7
      //   1136: invokevirtual 411	android/hardware/camera2/legacy/CameraDeviceState:setCaptureResult	(Landroid/hardware/camera2/legacy/RequestHolder;Landroid/hardware/camera2/impl/CameraMetadataNative;)Z
      //   1139: pop
      //   1140: aload_1
      //   1141: invokevirtual 414	android/hardware/camera2/legacy/RequestHolder:isOutputAbandoned	()Z
      //   1144: ifeq +6 -> 1150
      //   1147: iconst_1
      //   1148: istore 4
      //   1150: goto -394 -> 756
      //   1153: astore_1
      //   1154: aload_0
      //   1155: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1158: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   1161: ldc_w 416
      //   1164: aload_1
      //   1165: invokestatic 104	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   1168: pop
      //   1169: aload_0
      //   1170: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1173: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   1176: iconst_1
      //   1177: invokevirtual 114	android/hardware/camera2/legacy/CameraDeviceState:setError	(I)V
      //   1180: goto +105 -> 1285
      //   1183: astore_1
      //   1184: goto +12 -> 1196
      //   1187: astore_1
      //   1188: goto +38 -> 1226
      //   1191: astore_1
      //   1192: goto +64 -> 1256
      //   1195: astore_1
      //   1196: aload_0
      //   1197: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1200: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   1203: ldc_w 418
      //   1206: aload_1
      //   1207: invokestatic 104	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   1210: pop
      //   1211: aload_0
      //   1212: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1215: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   1218: iconst_1
      //   1219: invokevirtual 114	android/hardware/camera2/legacy/CameraDeviceState:setError	(I)V
      //   1222: goto +63 -> 1285
      //   1225: astore_1
      //   1226: aload_0
      //   1227: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1230: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   1233: ldc_w 420
      //   1236: aload_1
      //   1237: invokestatic 104	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   1240: pop
      //   1241: aload_0
      //   1242: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1245: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   1248: iconst_1
      //   1249: invokevirtual 114	android/hardware/camera2/legacy/CameraDeviceState:setError	(I)V
      //   1252: goto +33 -> 1285
      //   1255: astore_1
      //   1256: aload_0
      //   1257: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1260: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   1263: ldc_w 418
      //   1266: aload_1
      //   1267: invokestatic 104	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   1270: pop
      //   1271: aload_0
      //   1272: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1275: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   1278: iconst_1
      //   1279: invokevirtual 114	android/hardware/camera2/legacy/CameraDeviceState:setError	(I)V
      //   1282: goto +3 -> 1285
      //   1285: iload 4
      //   1287: ifeq +40 -> 1327
      //   1290: aload 5
      //   1292: invokevirtual 423	android/hardware/camera2/legacy/BurstHolder:isRepeating	()Z
      //   1295: ifeq +32 -> 1327
      //   1298: aload_0
      //   1299: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1302: aload 5
      //   1304: invokevirtual 427	android/hardware/camera2/legacy/BurstHolder:getRequestId	()I
      //   1307: invokevirtual 431	android/hardware/camera2/legacy/RequestThreadManager:cancelRepeating	(I)J
      //   1310: lstore_2
      //   1311: aload_0
      //   1312: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1315: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   1318: lload_2
      //   1319: aload 5
      //   1321: invokevirtual 427	android/hardware/camera2/legacy/BurstHolder:getRequestId	()I
      //   1324: invokevirtual 435	android/hardware/camera2/legacy/CameraDeviceState:setRepeatingRequestError	(JI)V
      //   1327: goto +186 -> 1513
      //   1330: aload_1
      //   1331: getfield 439	android/os/Message:obj	Ljava/lang/Object;
      //   1334: checkcast 441	android/hardware/camera2/legacy/RequestThreadManager$ConfigureHolder
      //   1337: astore 6
      //   1339: aload 6
      //   1341: getfield 445	android/hardware/camera2/legacy/RequestThreadManager$ConfigureHolder:surfaces	Ljava/util/Collection;
      //   1344: ifnull +18 -> 1362
      //   1347: aload 6
      //   1349: getfield 445	android/hardware/camera2/legacy/RequestThreadManager$ConfigureHolder:surfaces	Ljava/util/Collection;
      //   1352: invokeinterface 450 1 0
      //   1357: istore 4
      //   1359: goto +6 -> 1365
      //   1362: iconst_0
      //   1363: istore 4
      //   1365: aload_0
      //   1366: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1369: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   1372: astore_1
      //   1373: new 45	java/lang/StringBuilder
      //   1376: dup
      //   1377: invokespecial 46	java/lang/StringBuilder:<init>	()V
      //   1380: astore 5
      //   1382: aload 5
      //   1384: ldc_w 452
      //   1387: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1390: pop
      //   1391: aload 5
      //   1393: iload 4
      //   1395: invokevirtual 55	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   1398: pop
      //   1399: aload 5
      //   1401: ldc_w 454
      //   1404: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1407: pop
      //   1408: aload_1
      //   1409: aload 5
      //   1411: invokevirtual 63	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   1414: invokestatic 457	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
      //   1417: pop
      //   1418: aload_0
      //   1419: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1422: invokestatic 70	android/hardware/camera2/legacy/RequestThreadManager:access$200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CaptureCollector;
      //   1425: ldc2_w 71
      //   1428: getstatic 78	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
      //   1431: invokevirtual 84	android/hardware/camera2/legacy/CaptureCollector:waitForEmpty	(JLjava/util/concurrent/TimeUnit;)Z
      //   1434: ifne +27 -> 1461
      //   1437: aload_0
      //   1438: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1441: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   1444: ldc_w 459
      //   1447: invokestatic 96	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   1450: pop
      //   1451: aload_0
      //   1452: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1455: invokestatic 70	android/hardware/camera2/legacy/RequestThreadManager:access$200	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CaptureCollector;
      //   1458: invokevirtual 99	android/hardware/camera2/legacy/CaptureCollector:failAll	()V
      //   1461: aload_0
      //   1462: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1465: aload 6
      //   1467: getfield 445	android/hardware/camera2/legacy/RequestThreadManager$ConfigureHolder:surfaces	Ljava/util/Collection;
      //   1470: invokestatic 462	android/hardware/camera2/legacy/RequestThreadManager:access$600	(Landroid/hardware/camera2/legacy/RequestThreadManager;Ljava/util/Collection;)V
      //   1473: aload 6
      //   1475: getfield 466	android/hardware/camera2/legacy/RequestThreadManager$ConfigureHolder:condition	Landroid/os/ConditionVariable;
      //   1478: invokevirtual 469	android/os/ConditionVariable:open	()V
      //   1481: goto -154 -> 1327
      //   1484: astore_1
      //   1485: aload_0
      //   1486: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1489: invokestatic 88	android/hardware/camera2/legacy/RequestThreadManager:access$100	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Ljava/lang/String;
      //   1492: ldc_w 471
      //   1495: invokestatic 96	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   1498: pop
      //   1499: aload_0
      //   1500: getfield 18	android/hardware/camera2/legacy/RequestThreadManager$5:this$0	Landroid/hardware/camera2/legacy/RequestThreadManager;
      //   1503: invokestatic 108	android/hardware/camera2/legacy/RequestThreadManager:access$000	(Landroid/hardware/camera2/legacy/RequestThreadManager;)Landroid/hardware/camera2/legacy/CameraDeviceState;
      //   1506: iconst_1
      //   1507: invokevirtual 114	android/hardware/camera2/legacy/CameraDeviceState:setError	(I)V
      //   1510: goto +3 -> 1513
      //   1513: iconst_1
      //   1514: ireturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	1515	0	this	5
      //   0	1515	1	paramAnonymousMessage	android.os.Message
      //   10	1309	2	l	long
      //   15	1379	4	i	int
      //   59	1351	5	localObject1	Object
      //   261	1213	6	localObject2	Object
      //   491	66	7	localCaptureRequest	CaptureRequest
      //   629	243	7	localRuntimeException	RuntimeException
      //   1019	116	7	localObject3	Object
      //   494	175	8	j	int
      //   497	442	9	k	int
      //   543	166	10	localObject4	Object
      //   701	11	11	localLegacyRequest	LegacyRequest
      //   706	11	12	localTimeUnit	java.util.concurrent.TimeUnit
      // Exception table:
      //   from	to	target	type
      //   105	147	150	java/lang/InterruptedException
      //   340	351	378	finally
      //   355	369	378	finally
      //   372	375	378	finally
      //   379	382	378	finally
      //   286	328	384	java/lang/InterruptedException
      //   595	610	629	java/lang/RuntimeException
      //   943	961	981	java/lang/RuntimeException
      //   1021	1067	1153	java/lang/InterruptedException
      //   708	756	1183	java/lang/RuntimeException
      //   759	774	1183	java/lang/RuntimeException
      //   774	781	1183	java/lang/RuntimeException
      //   781	824	1183	java/lang/RuntimeException
      //   827	845	1183	java/lang/RuntimeException
      //   845	938	1183	java/lang/RuntimeException
      //   708	756	1187	java/lang/InterruptedException
      //   759	774	1187	java/lang/InterruptedException
      //   774	781	1187	java/lang/InterruptedException
      //   781	824	1187	java/lang/InterruptedException
      //   827	845	1187	java/lang/InterruptedException
      //   845	938	1187	java/lang/InterruptedException
      //   708	756	1191	java/io/IOException
      //   759	774	1191	java/io/IOException
      //   774	781	1191	java/io/IOException
      //   781	824	1191	java/io/IOException
      //   827	845	1191	java/io/IOException
      //   845	938	1191	java/io/IOException
      //   685	708	1195	java/lang/RuntimeException
      //   685	708	1225	java/lang/InterruptedException
      //   685	708	1255	java/io/IOException
      //   1418	1461	1484	java/lang/InterruptedException
    }
  };
  private final RequestQueue mRequestQueue = new RequestQueue(mJpegSurfaceIds);
  private final RequestHandlerThread mRequestThread;
  
  public RequestThreadManager(int paramInt, Camera paramCamera, CameraCharacteristics paramCameraCharacteristics, CameraDeviceState paramCameraDeviceState)
  {
    mCamera = ((Camera)Preconditions.checkNotNull(paramCamera, "camera must not be null"));
    mCameraId = paramInt;
    mCharacteristics = ((CameraCharacteristics)Preconditions.checkNotNull(paramCameraCharacteristics, "characteristics must not be null"));
    paramCamera = String.format("RequestThread-%d", new Object[] { Integer.valueOf(paramInt) });
    TAG = paramCamera;
    mDeviceState = ((CameraDeviceState)Preconditions.checkNotNull(paramCameraDeviceState, "deviceState must not be null"));
    mFocusStateMapper = new LegacyFocusStateMapper(mCamera);
    mFaceDetectMapper = new LegacyFaceDetectMapper(mCamera, mCharacteristics);
    mCaptureCollector = new CaptureCollector(2, mDeviceState);
    mRequestThread = new RequestHandlerThread(paramCamera, mRequestHandlerCb);
    mCamera.setDetailedErrorCallback(mErrorCallback);
  }
  
  private Size calculatePictureSize(List<Surface> paramList, List<Size> paramList1, Camera.Parameters paramParameters)
  {
    if (paramList.size() == paramList1.size())
    {
      Object localObject = new ArrayList();
      paramList1 = paramList1.iterator();
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        Surface localSurface = (Surface)localIterator.next();
        paramList = (Size)paramList1.next();
        if (LegacyCameraDevice.containsSurfaceId(localSurface, mJpegSurfaceIds)) {
          ((List)localObject).add(paramList);
        }
      }
      if (!((List)localObject).isEmpty())
      {
        int i = -1;
        int j = -1;
        paramList1 = ((List)localObject).iterator();
        while (paramList1.hasNext())
        {
          paramList = (Size)paramList1.next();
          if (paramList.getWidth() > i) {
            i = paramList.getWidth();
          }
          if (paramList.getHeight() > j) {
            j = paramList.getHeight();
          }
        }
        paramList = new Size(i, j);
        paramParameters = ParameterUtils.convertSizeList(paramParameters.getSupportedPictureSizes());
        paramList1 = new ArrayList();
        paramParameters = paramParameters.iterator();
        while (paramParameters.hasNext())
        {
          localObject = (Size)paramParameters.next();
          if ((((Size)localObject).getWidth() >= i) && (((Size)localObject).getHeight() >= j)) {
            paramList1.add(localObject);
          }
        }
        if (!paramList1.isEmpty())
        {
          paramList1 = (Size)Collections.min(paramList1, new SizeAreaComparator());
          if (!paramList1.equals(paramList)) {
            Log.w(TAG, String.format("configureOutputs - Will need to crop picture %s into smallest bound size %s", new Object[] { paramList1, paramList }));
          }
          return paramList1;
        }
        paramList1 = new StringBuilder();
        paramList1.append("Could not find any supported JPEG sizes large enough to fit ");
        paramList1.append(paramList);
        throw new AssertionError(paramList1.toString());
      }
      return null;
    }
    throw new IllegalStateException("Input collections must be same length");
  }
  
  private static boolean checkAspectRatiosMatch(Size paramSize1, Size paramSize2)
  {
    boolean bool;
    if (Math.abs(paramSize1.getWidth() / paramSize1.getHeight() - paramSize2.getWidth() / paramSize2.getHeight()) < 0.01F) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void configureOutputs(Collection<Pair<Surface, Size>> paramCollection)
  {
    try
    {
      stopPreview();
      try
      {
        mCamera.setPreviewTexture(null);
      }
      catch (RuntimeException paramCollection)
      {
        Log.e(TAG, "Received device exception in configure call: ", paramCollection);
        mDeviceState.setError(1);
        return;
      }
      catch (IOException localIOException)
      {
        Log.w(TAG, "Failed to clear prior SurfaceTexture, may cause GL deadlock: ", localIOException);
      }
      if (mGLThreadManager != null)
      {
        mGLThreadManager.waitUntilStarted();
        mGLThreadManager.ignoreNewFrames();
        mGLThreadManager.waitUntilIdle();
      }
      resetJpegSurfaceFormats(mCallbackOutputs);
      Object localObject1 = mCallbackOutputs.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        Surface localSurface1 = (Surface)((Iterator)localObject1).next();
        try
        {
          LegacyCameraDevice.disconnectSurface(localSurface1);
        }
        catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException2)
        {
          Log.w(TAG, "Surface abandoned, skipping...", localBufferQueueAbandonedException2);
        }
      }
      mPreviewOutputs.clear();
      mCallbackOutputs.clear();
      mJpegSurfaceIds.clear();
      mPreviewTexture = null;
      localObject1 = new ArrayList();
      Object localObject2 = new ArrayList();
      int i = ((Integer)mCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue();
      int j = ((Integer)mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
      Object localObject3;
      if (paramCollection != null)
      {
        paramCollection = paramCollection.iterator();
        while (paramCollection.hasNext())
        {
          localObject3 = (Pair)paramCollection.next();
          Surface localSurface2 = (Surface)first;
          localObject3 = (Size)second;
          try
          {
            int k = LegacyCameraDevice.detectSurfaceType(localSurface2);
            LegacyCameraDevice.setSurfaceOrientation(localSurface2, i, j);
            if (k != 33)
            {
              LegacyCameraDevice.setScalingMode(localSurface2, 1);
              mPreviewOutputs.add(localSurface2);
              ((List)localObject1).add(localObject3);
            }
            else
            {
              LegacyCameraDevice.setSurfaceFormat(localSurface2, 1);
              mJpegSurfaceIds.add(Long.valueOf(LegacyCameraDevice.getSurfaceId(localSurface2)));
              mCallbackOutputs.add(localSurface2);
              ((List)localObject2).add(localObject3);
              LegacyCameraDevice.connectSurface(localSurface2);
            }
          }
          catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException3)
          {
            Log.w(TAG, "Surface abandoned, skipping...", localBufferQueueAbandonedException3);
          }
        }
      }
      try
      {
        mParams = mCamera.getParameters();
        List localList = mParams.getSupportedPreviewFpsRange();
        paramCollection = getPhotoPreviewFpsRange(localList);
        mParams.setPreviewFpsRange(paramCollection[0], paramCollection[1]);
        Size localSize1 = calculatePictureSize(mCallbackOutputs, (List)localObject2, mParams);
        if (((List)localObject1).size() > 0)
        {
          Size localSize2 = SizeAreaComparator.findLargestByArea((List)localObject1);
          localObject2 = ParameterUtils.getLargestSupportedJpegSizeByArea(mParams);
          if (localSize1 != null) {
            localObject2 = localSize1;
          }
          paramCollection = ParameterUtils.convertSizeList(mParams.getSupportedPreviewSizes());
          long l1 = localSize2.getHeight();
          long l2 = localSize2.getWidth();
          localObject3 = SizeAreaComparator.findLargestByArea(paramCollection);
          Object localObject4 = paramCollection.iterator();
          paramCollection = (Collection<Pair<Surface, Size>>)localObject1;
          localObject1 = localObject4;
          while (((Iterator)localObject1).hasNext())
          {
            Size localSize3 = (Size)((Iterator)localObject1).next();
            long l3 = localSize3.getWidth() * localSize3.getHeight();
            long l4 = ((Size)localObject3).getWidth() * ((Size)localObject3).getHeight();
            localObject4 = localObject3;
            if (checkAspectRatiosMatch((Size)localObject2, localSize3))
            {
              localObject4 = localObject3;
              if (l3 < l4)
              {
                localObject4 = localObject3;
                if (l3 >= l1 * l2) {
                  localObject4 = localSize3;
                }
              }
            }
            localObject3 = localObject4;
          }
          mIntermediateBufferSize = ((Size)localObject3);
          mParams.setPreviewSize(mIntermediateBufferSize.getWidth(), mIntermediateBufferSize.getHeight());
        }
        else
        {
          paramCollection = (Collection<Pair<Surface, Size>>)localObject1;
          mIntermediateBufferSize = null;
        }
        if (localSize1 != null)
        {
          localObject2 = TAG;
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("configureOutputs - set take picture size to ");
          ((StringBuilder)localObject1).append(localSize1);
          Log.i((String)localObject2, ((StringBuilder)localObject1).toString());
          mParams.setPictureSize(localSize1.getWidth(), localSize1.getHeight());
        }
        if (mGLThreadManager == null)
        {
          mGLThreadManager = new GLThreadManager(mCameraId, i, mDeviceState);
          mGLThreadManager.start();
        }
        mGLThreadManager.waitUntilStarted();
        localObject1 = new ArrayList();
        paramCollection = paramCollection.iterator();
        localObject2 = mPreviewOutputs.iterator();
        while (((Iterator)localObject2).hasNext()) {
          ((List)localObject1).add(new Pair((Surface)((Iterator)localObject2).next(), (Size)paramCollection.next()));
        }
        mGLThreadManager.setConfigurationAndWait((Collection)localObject1, mCaptureCollector);
        paramCollection = mPreviewOutputs.iterator();
        while (paramCollection.hasNext())
        {
          localObject1 = (Surface)paramCollection.next();
          try
          {
            LegacyCameraDevice.setSurfaceOrientation((Surface)localObject1, i, j);
          }
          catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException1)
          {
            Log.e(TAG, "Surface abandoned, skipping setSurfaceOrientation()", localBufferQueueAbandonedException1);
          }
        }
        mGLThreadManager.allowNewFrames();
        mPreviewTexture = mGLThreadManager.getCurrentSurfaceTexture();
        if (mPreviewTexture != null) {
          mPreviewTexture.setOnFrameAvailableListener(mPreviewCallback);
        }
        try
        {
          mCamera.setParameters(mParams);
        }
        catch (RuntimeException paramCollection)
        {
          Log.e(TAG, "Received device exception while configuring: ", paramCollection);
          mDeviceState.setError(1);
        }
        return;
      }
      catch (RuntimeException paramCollection)
      {
        Log.e(TAG, "Received device exception: ", paramCollection);
        mDeviceState.setError(1);
        return;
      }
      return;
    }
    catch (RuntimeException paramCollection)
    {
      Log.e(TAG, "Received device exception in configure call: ", paramCollection);
      mDeviceState.setError(1);
    }
  }
  
  private void createDummySurface()
  {
    if ((mDummyTexture == null) || (mDummySurface == null))
    {
      mDummyTexture = new SurfaceTexture(0);
      mDummyTexture.setDefaultBufferSize(640, 480);
      mDummySurface = new Surface(mDummyTexture);
    }
  }
  
  private void doJpegCapture(RequestHolder paramRequestHolder)
  {
    mCamera.takePicture(mJpegShutterCallback, null, mJpegCallback);
    mPreviewRunning = false;
  }
  
  private void doJpegCapturePrepare(RequestHolder paramRequestHolder)
    throws IOException
  {
    if (!mPreviewRunning)
    {
      createDummySurface();
      mCamera.setPreviewTexture(mDummyTexture);
      startPreview();
    }
  }
  
  private void doPreviewCapture(RequestHolder paramRequestHolder)
    throws IOException
  {
    if (mPreviewRunning) {
      return;
    }
    if (mPreviewTexture != null)
    {
      mPreviewTexture.setDefaultBufferSize(mIntermediateBufferSize.getWidth(), mIntermediateBufferSize.getHeight());
      mCamera.setPreviewTexture(mPreviewTexture);
      startPreview();
      return;
    }
    throw new IllegalStateException("Preview capture called with no preview surfaces configured.");
  }
  
  private int[] getPhotoPreviewFpsRange(List<int[]> paramList)
  {
    if (paramList.size() == 0)
    {
      Log.e(TAG, "No supported frame rates returned!");
      return null;
    }
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      int[] arrayOfInt = (int[])localIterator.next();
      int n = arrayOfInt[0];
      int i1 = arrayOfInt[1];
      int i2;
      int i3;
      int i4;
      if (i1 <= j)
      {
        i2 = i;
        i3 = j;
        i4 = k;
        if (i1 == j)
        {
          i2 = i;
          i3 = j;
          i4 = k;
          if (n <= i) {}
        }
      }
      else
      {
        i2 = n;
        i3 = i1;
        i4 = m;
      }
      m++;
      i = i2;
      j = i3;
      k = i4;
    }
    return (int[])paramList.get(k);
  }
  
  private void resetJpegSurfaceFormats(Collection<Surface> paramCollection)
  {
    if (paramCollection == null) {
      return;
    }
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext())
    {
      Surface localSurface = (Surface)paramCollection.next();
      if ((localSurface != null) && (localSurface.isValid())) {
        try
        {
          LegacyCameraDevice.setSurfaceFormat(localSurface, 33);
        }
        catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException)
        {
          Log.w(TAG, "Surface abandoned, skipping...", localBufferQueueAbandonedException);
        }
      } else {
        Log.w(TAG, "Jpeg surface is invalid, skipping...");
      }
    }
  }
  
  private void startPreview()
  {
    if (!mPreviewRunning)
    {
      mCamera.startPreview();
      mPreviewRunning = true;
    }
  }
  
  private void stopPreview()
  {
    if (mPreviewRunning)
    {
      mCamera.stopPreview();
      mPreviewRunning = false;
    }
  }
  
  public long cancelRepeating(int paramInt)
  {
    return mRequestQueue.stopRepeating(paramInt);
  }
  
  public void configure(Collection<Pair<Surface, Size>> paramCollection)
  {
    Handler localHandler = mRequestThread.waitAndGetHandler();
    ConditionVariable localConditionVariable = new ConditionVariable(false);
    localHandler.sendMessage(localHandler.obtainMessage(1, 0, 0, new ConfigureHolder(localConditionVariable, paramCollection)));
    localConditionVariable.block();
  }
  
  public long flush()
  {
    Log.i(TAG, "Flushing all pending requests.");
    long l = mRequestQueue.stopRepeating();
    mCaptureCollector.failAll();
    return l;
  }
  
  public void quit()
  {
    if (!mQuit.getAndSet(true))
    {
      Handler localHandler = mRequestThread.waitAndGetHandler();
      localHandler.sendMessageAtFrontOfQueue(localHandler.obtainMessage(3));
      mRequestThread.quitSafely();
      try
      {
        mRequestThread.join();
      }
      catch (InterruptedException localInterruptedException)
      {
        Log.e(TAG, String.format("Thread %s (%d) interrupted while quitting.", new Object[] { mRequestThread.getName(), Long.valueOf(mRequestThread.getId()) }));
      }
    }
  }
  
  public void start()
  {
    mRequestThread.start();
  }
  
  public SubmitInfo submitCaptureRequests(CaptureRequest[] paramArrayOfCaptureRequest, boolean paramBoolean)
  {
    Handler localHandler = mRequestThread.waitAndGetHandler();
    synchronized (mIdleLock)
    {
      paramArrayOfCaptureRequest = mRequestQueue.submit(paramArrayOfCaptureRequest, paramBoolean);
      localHandler.sendEmptyMessage(2);
      return paramArrayOfCaptureRequest;
    }
  }
  
  private static class ConfigureHolder
  {
    public final ConditionVariable condition;
    public final Collection<Pair<Surface, Size>> surfaces;
    
    public ConfigureHolder(ConditionVariable paramConditionVariable, Collection<Pair<Surface, Size>> paramCollection)
    {
      condition = paramConditionVariable;
      surfaces = paramCollection;
    }
  }
  
  public static class FpsCounter
  {
    private static final long NANO_PER_SECOND = 1000000000L;
    private static final String TAG = "FpsCounter";
    private int mFrameCount = 0;
    private double mLastFps = 0.0D;
    private long mLastPrintTime = 0L;
    private long mLastTime = 0L;
    private final String mStreamType;
    
    public FpsCounter(String paramString)
    {
      mStreamType = paramString;
    }
    
    public double checkFps()
    {
      try
      {
        double d = mLastFps;
        return d;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    public void countAndLog()
    {
      try
      {
        countFrame();
        staggeredLog();
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    public void countFrame()
    {
      try
      {
        mFrameCount += 1;
        long l1 = SystemClock.elapsedRealtimeNanos();
        if (mLastTime == 0L) {
          mLastTime = l1;
        }
        if (l1 > mLastTime + 1000000000L)
        {
          long l2 = mLastTime;
          mLastFps = (mFrameCount * (1.0E9D / (l1 - l2)));
          mFrameCount = 0;
          mLastTime = l1;
        }
        return;
      }
      finally {}
    }
    
    public void staggeredLog()
    {
      try
      {
        if (mLastTime > mLastPrintTime + 5000000000L)
        {
          mLastPrintTime = mLastTime;
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("FPS for ");
          localStringBuilder.append(mStreamType);
          localStringBuilder.append(" stream: ");
          localStringBuilder.append(mLastFps);
          Log.d("FpsCounter", localStringBuilder.toString());
        }
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
  }
}
