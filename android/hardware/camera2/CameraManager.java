package android.hardware.camera2;

import android.app.ActivityThread;
import android.content.Context;
import android.hardware.CameraStatus;
import android.hardware.ICameraService;
import android.hardware.ICameraService.Stub;
import android.hardware.ICameraServiceListener.Stub;
import android.hardware.camera2.impl.CameraDeviceImpl;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.legacy.LegacyMetadataMapper;
import android.os.Binder;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.os.SystemProperties;
import android.text.TextUtils.SimpleStringSplitter;
import android.text.TextUtils.StringSplitter;
import android.util.ArrayMap;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class CameraManager
{
  private static final int API_VERSION_1 = 1;
  private static final int API_VERSION_2 = 2;
  private static final int CAMERA_TYPE_ALL = 1;
  private static final int CAMERA_TYPE_BACKWARD_COMPATIBLE = 0;
  private static final String TAG = "CameraManager";
  private static final int USE_CALLING_UID = -1;
  private final boolean DEBUG = false;
  private final Context mContext;
  private ArrayList<String> mDeviceIdList;
  private final Object mLock = new Object();
  
  public CameraManager(Context paramContext)
  {
    synchronized (mLock)
    {
      mContext = paramContext;
      return;
    }
  }
  
  /* Error */
  private CameraDevice openCameraDeviceUserAsync(String paramString, CameraDevice.StateCallback paramStateCallback, Executor paramExecutor, int paramInt)
    throws CameraAccessException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 68	android/hardware/camera2/CameraManager:getCameraCharacteristics	(Ljava/lang/String;)Landroid/hardware/camera2/CameraCharacteristics;
    //   5: astore 5
    //   7: aload_0
    //   8: getfield 51	android/hardware/camera2/CameraManager:mLock	Ljava/lang/Object;
    //   11: astore 6
    //   13: aload 6
    //   15: monitorenter
    //   16: aconst_null
    //   17: astore 7
    //   19: aconst_null
    //   20: astore 8
    //   22: new 70	android/hardware/camera2/impl/CameraDeviceImpl
    //   25: astore 9
    //   27: aload 9
    //   29: aload_1
    //   30: aload_2
    //   31: aload_3
    //   32: aload 5
    //   34: aload_0
    //   35: getfield 53	android/hardware/camera2/CameraManager:mContext	Landroid/content/Context;
    //   38: invokevirtual 76	android/content/Context:getApplicationInfo	()Landroid/content/pm/ApplicationInfo;
    //   41: getfield 81	android/content/pm/ApplicationInfo:targetSdkVersion	I
    //   44: invokespecial 84	android/hardware/camera2/impl/CameraDeviceImpl:<init>	(Ljava/lang/String;Landroid/hardware/camera2/CameraDevice$StateCallback;Ljava/util/concurrent/Executor;Landroid/hardware/camera2/CameraCharacteristics;I)V
    //   47: aload 9
    //   49: invokevirtual 88	android/hardware/camera2/impl/CameraDeviceImpl:getCallbacks	()Landroid/hardware/camera2/impl/CameraDeviceImpl$CameraDeviceCallbacks;
    //   52: astore 5
    //   54: aload_0
    //   55: aload_1
    //   56: invokespecial 92	android/hardware/camera2/CameraManager:supportsCamera2ApiLocked	(Ljava/lang/String;)Z
    //   59: ifeq +51 -> 110
    //   62: invokestatic 96	android/hardware/camera2/CameraManager$CameraManagerGlobal:get	()Landroid/hardware/camera2/CameraManager$CameraManagerGlobal;
    //   65: invokevirtual 100	android/hardware/camera2/CameraManager$CameraManagerGlobal:getCameraService	()Landroid/hardware/ICameraService;
    //   68: astore_3
    //   69: aload_3
    //   70: ifnull +27 -> 97
    //   73: aload_0
    //   74: getfield 53	android/hardware/camera2/CameraManager:mContext	Landroid/content/Context;
    //   77: invokevirtual 104	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   80: astore_2
    //   81: aload_3
    //   82: aload 5
    //   84: aload_1
    //   85: aload_2
    //   86: iload 4
    //   88: invokeinterface 110 5 0
    //   93: astore_1
    //   94: goto +41 -> 135
    //   97: new 60	android/os/ServiceSpecificException
    //   100: astore_1
    //   101: aload_1
    //   102: iconst_4
    //   103: ldc 112
    //   105: invokespecial 115	android/os/ServiceSpecificException:<init>	(ILjava/lang/String;)V
    //   108: aload_1
    //   109: athrow
    //   110: aload_1
    //   111: invokestatic 121	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   114: istore 4
    //   116: ldc 31
    //   118: ldc 123
    //   120: invokestatic 129	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   123: pop
    //   124: aload 5
    //   126: iload 4
    //   128: invokestatic 135	android/hardware/camera2/legacy/CameraDeviceUserShim:connectBinderShim	(Landroid/hardware/camera2/ICameraDeviceCallbacks;I)Landroid/hardware/camera2/legacy/CameraDeviceUserShim;
    //   131: astore_1
    //   132: goto -38 -> 94
    //   135: goto +187 -> 322
    //   138: astore_1
    //   139: goto +44 -> 183
    //   142: astore_2
    //   143: goto +68 -> 211
    //   146: astore_2
    //   147: new 137	java/lang/IllegalArgumentException
    //   150: astore_3
    //   151: new 139	java/lang/StringBuilder
    //   154: astore_2
    //   155: aload_2
    //   156: invokespecial 140	java/lang/StringBuilder:<init>	()V
    //   159: aload_2
    //   160: ldc -114
    //   162: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   165: pop
    //   166: aload_2
    //   167: aload_1
    //   168: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   171: pop
    //   172: aload_3
    //   173: aload_2
    //   174: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   177: invokespecial 152	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   180: aload_3
    //   181: athrow
    //   182: astore_1
    //   183: new 60	android/os/ServiceSpecificException
    //   186: astore_1
    //   187: aload_1
    //   188: iconst_4
    //   189: ldc 112
    //   191: invokespecial 115	android/os/ServiceSpecificException:<init>	(ILjava/lang/String;)V
    //   194: aload 9
    //   196: aload_1
    //   197: invokevirtual 156	android/hardware/camera2/impl/CameraDeviceImpl:setRemoteFailure	(Landroid/os/ServiceSpecificException;)V
    //   200: aload_1
    //   201: invokestatic 160	android/hardware/camera2/CameraManager:throwAsPublicException	(Ljava/lang/Throwable;)V
    //   204: aload 7
    //   206: astore_1
    //   207: goto +115 -> 322
    //   210: astore_2
    //   211: aload_2
    //   212: getfield 163	android/os/ServiceSpecificException:errorCode	I
    //   215: bipush 9
    //   217: if_icmpeq +117 -> 334
    //   220: aload_2
    //   221: getfield 163	android/os/ServiceSpecificException:errorCode	I
    //   224: bipush 7
    //   226: if_icmpeq +51 -> 277
    //   229: aload_2
    //   230: getfield 163	android/os/ServiceSpecificException:errorCode	I
    //   233: bipush 8
    //   235: if_icmpeq +42 -> 277
    //   238: aload_2
    //   239: getfield 163	android/os/ServiceSpecificException:errorCode	I
    //   242: bipush 6
    //   244: if_icmpeq +33 -> 277
    //   247: aload_2
    //   248: getfield 163	android/os/ServiceSpecificException:errorCode	I
    //   251: iconst_4
    //   252: if_icmpeq +25 -> 277
    //   255: aload_2
    //   256: getfield 163	android/os/ServiceSpecificException:errorCode	I
    //   259: bipush 10
    //   261: if_icmpne +6 -> 267
    //   264: goto +13 -> 277
    //   267: aload_2
    //   268: invokestatic 160	android/hardware/camera2/CameraManager:throwAsPublicException	(Ljava/lang/Throwable;)V
    //   271: aload 8
    //   273: astore_1
    //   274: goto -139 -> 135
    //   277: aload 9
    //   279: aload_2
    //   280: invokevirtual 156	android/hardware/camera2/impl/CameraDeviceImpl:setRemoteFailure	(Landroid/os/ServiceSpecificException;)V
    //   283: aload_2
    //   284: getfield 163	android/os/ServiceSpecificException:errorCode	I
    //   287: bipush 6
    //   289: if_icmpeq +23 -> 312
    //   292: aload_2
    //   293: getfield 163	android/os/ServiceSpecificException:errorCode	I
    //   296: iconst_4
    //   297: if_icmpeq +15 -> 312
    //   300: aload 8
    //   302: astore_1
    //   303: aload_2
    //   304: getfield 163	android/os/ServiceSpecificException:errorCode	I
    //   307: bipush 7
    //   309: if_icmpne -174 -> 135
    //   312: aload_2
    //   313: invokestatic 160	android/hardware/camera2/CameraManager:throwAsPublicException	(Ljava/lang/Throwable;)V
    //   316: aload 8
    //   318: astore_1
    //   319: goto -184 -> 135
    //   322: aload 9
    //   324: aload_1
    //   325: invokevirtual 167	android/hardware/camera2/impl/CameraDeviceImpl:setRemoteDevice	(Landroid/hardware/camera2/ICameraDeviceUser;)V
    //   328: aload 6
    //   330: monitorexit
    //   331: aload 9
    //   333: areturn
    //   334: new 169	java/lang/AssertionError
    //   337: astore_1
    //   338: aload_1
    //   339: ldc -85
    //   341: invokespecial 174	java/lang/AssertionError:<init>	(Ljava/lang/Object;)V
    //   344: aload_1
    //   345: athrow
    //   346: astore_1
    //   347: aload 6
    //   349: monitorexit
    //   350: aload_1
    //   351: athrow
    //   352: astore_1
    //   353: goto -6 -> 347
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	356	0	this	CameraManager
    //   0	356	1	paramString	String
    //   0	356	2	paramStateCallback	CameraDevice.StateCallback
    //   0	356	3	paramExecutor	Executor
    //   0	356	4	paramInt	int
    //   5	120	5	localObject1	Object
    //   11	337	6	localObject2	Object
    //   17	188	7	localObject3	Object
    //   20	297	8	localObject4	Object
    //   25	307	9	localCameraDeviceImpl	CameraDeviceImpl
    // Exception table:
    //   from	to	target	type
    //   81	94	138	android/os/RemoteException
    //   97	110	138	android/os/RemoteException
    //   110	116	138	android/os/RemoteException
    //   116	132	138	android/os/RemoteException
    //   147	182	138	android/os/RemoteException
    //   81	94	142	android/os/ServiceSpecificException
    //   97	110	142	android/os/ServiceSpecificException
    //   110	116	142	android/os/ServiceSpecificException
    //   116	132	142	android/os/ServiceSpecificException
    //   147	182	142	android/os/ServiceSpecificException
    //   110	116	146	java/lang/NumberFormatException
    //   54	69	182	android/os/RemoteException
    //   73	81	182	android/os/RemoteException
    //   54	69	210	android/os/ServiceSpecificException
    //   73	81	210	android/os/ServiceSpecificException
    //   22	54	346	finally
    //   54	69	346	finally
    //   73	81	346	finally
    //   81	94	352	finally
    //   97	110	352	finally
    //   110	116	352	finally
    //   116	132	352	finally
    //   147	182	352	finally
    //   183	204	352	finally
    //   211	264	352	finally
    //   267	271	352	finally
    //   277	300	352	finally
    //   303	312	352	finally
    //   312	316	352	finally
    //   322	331	352	finally
    //   334	346	352	finally
    //   347	350	352	finally
  }
  
  private boolean supportsCamera2ApiLocked(String paramString)
  {
    return supportsCameraApiLocked(paramString, 2);
  }
  
  private boolean supportsCameraApiLocked(String paramString, int paramInt)
  {
    try
    {
      ICameraService localICameraService = CameraManagerGlobal.get().getCameraService();
      if (localICameraService == null) {
        return false;
      }
      boolean bool = localICameraService.supportsCameraApi(paramString, paramInt);
      return bool;
    }
    catch (RemoteException paramString) {}
    return false;
  }
  
  public static void throwAsPublicException(Throwable paramThrowable)
    throws CameraAccessException
  {
    if ((paramThrowable instanceof ServiceSpecificException))
    {
      paramThrowable = (ServiceSpecificException)paramThrowable;
      int i;
      switch (errorCode)
      {
      case 5: 
      default: 
        i = 3;
        break;
      case 9: 
        i = 1000;
        break;
      case 8: 
        i = 5;
        break;
      case 7: 
        i = 4;
        break;
      case 6: 
        i = 1;
        break;
      case 4: 
        i = 2;
        break;
      case 2: 
      case 3: 
        throw new IllegalArgumentException(paramThrowable.getMessage(), paramThrowable);
      case 1: 
        throw new SecurityException(paramThrowable.getMessage(), paramThrowable);
      }
      throw new CameraAccessException(i, paramThrowable.getMessage(), paramThrowable);
    }
    if (!(paramThrowable instanceof DeadObjectException))
    {
      if (!(paramThrowable instanceof RemoteException))
      {
        if (!(paramThrowable instanceof RuntimeException)) {
          return;
        }
        throw ((RuntimeException)paramThrowable);
      }
      throw new UnsupportedOperationException("An unknown RemoteException was thrown which should never happen.", paramThrowable);
    }
    throw new CameraAccessException(2, "Camera service has died unexpectedly", paramThrowable);
  }
  
  public CameraCharacteristics getCameraCharacteristics(String paramString)
    throws CameraAccessException
  {
    Object localObject1 = null;
    if (!CameraManagerGlobal.sCameraServiceDisabled) {
      synchronized (mLock)
      {
        ICameraService localICameraService = CameraManagerGlobal.get().getCameraService();
        if (localICameraService != null)
        {
          try
          {
            if (!supportsCamera2ApiLocked(paramString))
            {
              int i = Integer.parseInt(paramString);
              paramString = LegacyMetadataMapper.createCharacteristics(localICameraService.getLegacyParameters(i), localICameraService.getCameraInfo(i));
            }
            else
            {
              paramString = new CameraCharacteristics(localICameraService.getCameraCharacteristics(paramString));
            }
          }
          catch (RemoteException localRemoteException)
          {
            paramString = new android/hardware/camera2/CameraAccessException;
            paramString.<init>(2, "Camera service is currently unavailable", localRemoteException);
            throw paramString;
          }
          catch (ServiceSpecificException paramString)
          {
            throwAsPublicException(paramString);
            paramString = localRemoteException;
          }
          return paramString;
        }
        paramString = new android/hardware/camera2/CameraAccessException;
        paramString.<init>(2, "Camera service is currently unavailable");
        throw paramString;
      }
    }
    throw new IllegalArgumentException("No cameras available on device");
  }
  
  public String[] getCameraIdList()
    throws CameraAccessException
  {
    return CameraManagerGlobal.get().getCameraIdList();
  }
  
  public void openCamera(String paramString, CameraDevice.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException
  {
    openCameraForUid(paramString, paramStateCallback, CameraDeviceImpl.checkAndWrapHandler(paramHandler), -1);
  }
  
  public void openCamera(String paramString, Executor paramExecutor, CameraDevice.StateCallback paramStateCallback)
    throws CameraAccessException
  {
    if (paramExecutor != null)
    {
      openCameraForUid(paramString, paramStateCallback, paramExecutor, -1);
      return;
    }
    throw new IllegalArgumentException("executor was null");
  }
  
  public void openCameraForUid(String paramString, CameraDevice.StateCallback paramStateCallback, Executor paramExecutor, int paramInt)
    throws CameraAccessException
  {
    if (paramString != null)
    {
      if (paramStateCallback != null)
      {
        if (!CameraManagerGlobal.sCameraServiceDisabled)
        {
          openCameraDeviceUserAsync(paramString, paramStateCallback, paramExecutor, paramInt);
          return;
        }
        throw new IllegalArgumentException("No cameras available on device");
      }
      throw new IllegalArgumentException("callback was null");
    }
    throw new IllegalArgumentException("cameraId was null");
  }
  
  public void registerAvailabilityCallback(AvailabilityCallback paramAvailabilityCallback, Handler paramHandler)
  {
    CameraManagerGlobal.get().registerAvailabilityCallback(paramAvailabilityCallback, CameraDeviceImpl.checkAndWrapHandler(paramHandler));
  }
  
  public void registerAvailabilityCallback(Executor paramExecutor, AvailabilityCallback paramAvailabilityCallback)
  {
    if (paramExecutor != null)
    {
      CameraManagerGlobal.get().registerAvailabilityCallback(paramAvailabilityCallback, paramExecutor);
      return;
    }
    throw new IllegalArgumentException("executor was null");
  }
  
  public void registerTorchCallback(TorchCallback paramTorchCallback, Handler paramHandler)
  {
    CameraManagerGlobal.get().registerTorchCallback(paramTorchCallback, CameraDeviceImpl.checkAndWrapHandler(paramHandler));
  }
  
  public void registerTorchCallback(Executor paramExecutor, TorchCallback paramTorchCallback)
  {
    if (paramExecutor != null)
    {
      CameraManagerGlobal.get().registerTorchCallback(paramTorchCallback, paramExecutor);
      return;
    }
    throw new IllegalArgumentException("executor was null");
  }
  
  public void setTorchMode(String paramString, boolean paramBoolean)
    throws CameraAccessException
  {
    if (!CameraManagerGlobal.sCameraServiceDisabled)
    {
      CameraManagerGlobal.get().setTorchMode(paramString, paramBoolean);
      return;
    }
    throw new IllegalArgumentException("No cameras available on device");
  }
  
  public void unregisterAvailabilityCallback(AvailabilityCallback paramAvailabilityCallback)
  {
    CameraManagerGlobal.get().unregisterAvailabilityCallback(paramAvailabilityCallback);
  }
  
  public void unregisterTorchCallback(TorchCallback paramTorchCallback)
  {
    CameraManagerGlobal.get().unregisterTorchCallback(paramTorchCallback);
  }
  
  public static abstract class AvailabilityCallback
  {
    public AvailabilityCallback() {}
    
    public void onCameraAvailable(String paramString) {}
    
    public void onCameraUnavailable(String paramString) {}
  }
  
  private static final class CameraManagerGlobal
    extends ICameraServiceListener.Stub
    implements IBinder.DeathRecipient
  {
    private static final String CAMERA_SERVICE_BINDER_NAME = "media.camera";
    private static final String TAG = "CameraManagerGlobal";
    private static final CameraManagerGlobal gCameraManager = new CameraManagerGlobal();
    public static final boolean sCameraServiceDisabled = SystemProperties.getBoolean("config.disable_cameraservice", false);
    private final int CAMERA_SERVICE_RECONNECT_DELAY_MS = 1000;
    private final boolean DEBUG = false;
    private final ArrayMap<CameraManager.AvailabilityCallback, Executor> mCallbackMap = new ArrayMap();
    private ICameraService mCameraService;
    private final ArrayMap<String, Integer> mDeviceStatus = new ArrayMap();
    private final Object mLock = new Object();
    private final ScheduledExecutorService mScheduler = Executors.newScheduledThreadPool(1);
    private final ArrayMap<CameraManager.TorchCallback, Executor> mTorchCallbackMap = new ArrayMap();
    private Binder mTorchClientBinder = new Binder();
    private final ArrayMap<String, Integer> mTorchStatus = new ArrayMap();
    
    private CameraManagerGlobal() {}
    
    private void connectCameraServiceLocked()
    {
      if ((mCameraService == null) && (!sCameraServiceDisabled))
      {
        Log.i("CameraManagerGlobal", "Connecting to camera service");
        Object localObject = ServiceManager.getService("media.camera");
        if (localObject == null) {
          return;
        }
        int i = 0;
        try
        {
          ((IBinder)localObject).linkToDeath(this, 0);
          localObject = ICameraService.Stub.asInterface((IBinder)localObject);
          try
          {
            CameraMetadataNative.setupGlobalVendorTagDescriptor();
          }
          catch (ServiceSpecificException localServiceSpecificException2)
          {
            handleRecoverableSetupErrors(localServiceSpecificException2);
          }
          try
          {
            try
            {
              CameraStatus[] arrayOfCameraStatus = ((ICameraService)localObject).addListener(this);
              int j = arrayOfCameraStatus.length;
              while (i < j)
              {
                CameraStatus localCameraStatus = arrayOfCameraStatus[i];
                onStatusChangedLocked(status, cameraId);
                i++;
              }
              mCameraService = ((ICameraService)localObject);
            }
            catch (RemoteException localRemoteException1) {}
            return;
          }
          catch (ServiceSpecificException localServiceSpecificException1)
          {
            throw new IllegalStateException("Failed to register a camera service listener", localServiceSpecificException1);
          }
          return;
        }
        catch (RemoteException localRemoteException2) {}
      }
    }
    
    public static CameraManagerGlobal get()
    {
      return gCameraManager;
    }
    
    private void handleRecoverableSetupErrors(ServiceSpecificException paramServiceSpecificException)
    {
      if (errorCode == 4)
      {
        Log.w("CameraManagerGlobal", paramServiceSpecificException.getMessage());
        return;
      }
      throw new IllegalStateException(paramServiceSpecificException);
    }
    
    private boolean isAvailable(int paramInt)
    {
      return paramInt == 1;
    }
    
    private void onStatusChangedLocked(int paramInt, String paramString)
    {
      int i = 0;
      Object localObject1 = ActivityThread.currentOpPackageName();
      Object localObject2 = SystemProperties.get("vendor.camera.aux.packagelist");
      int j = i;
      if (((String)localObject2).length() > 0)
      {
        TextUtils.SimpleStringSplitter localSimpleStringSplitter = new TextUtils.SimpleStringSplitter(',');
        localSimpleStringSplitter.setString((String)localObject2);
        localObject2 = localSimpleStringSplitter.iterator();
        for (;;)
        {
          j = i;
          if (!((Iterator)localObject2).hasNext()) {
            break label114;
          }
          if ((((String)localObject1).equals((String)((Iterator)localObject2).next())) || ((((String)localObject1).startsWith("com.asus")) && (!((String)localObject1).equals("com.asus.cnasusincallui")))) {
            break;
          }
        }
        j = 1;
      }
      label114:
      if ((j == 0) && (Integer.parseInt(paramString) >= 2))
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("[soar.cts] ignore the status update of camera: ");
        ((StringBuilder)localObject1).append(paramString);
        Log.w("CameraManagerGlobal", ((StringBuilder)localObject1).toString());
        return;
      }
      boolean bool = validStatus(paramInt);
      j = 0;
      if (!bool)
      {
        Log.e("CameraManagerGlobal", String.format("Ignoring invalid device %s status 0x%x", new Object[] { paramString, Integer.valueOf(paramInt) }));
        return;
      }
      if (paramInt == 0) {
        localObject1 = (Integer)mDeviceStatus.remove(paramString);
      } else {
        localObject1 = (Integer)mDeviceStatus.put(paramString, Integer.valueOf(paramInt));
      }
      if ((localObject1 != null) && (((Integer)localObject1).intValue() == paramInt)) {
        return;
      }
      if ((localObject1 != null) && (isAvailable(paramInt) == isAvailable(((Integer)localObject1).intValue()))) {
        return;
      }
      i = mCallbackMap.size();
      while (j < i)
      {
        localObject1 = (Executor)mCallbackMap.valueAt(j);
        postSingleUpdate((CameraManager.AvailabilityCallback)mCallbackMap.keyAt(j), (Executor)localObject1, paramString, paramInt);
        j++;
      }
    }
    
    private void onTorchStatusChangedLocked(int paramInt, String paramString)
    {
      int i = 0;
      Object localObject1 = ActivityThread.currentOpPackageName();
      String str = SystemProperties.get("vendor.camera.aux.packagelist");
      int j = i;
      if (str.length() > 0)
      {
        Object localObject2 = new TextUtils.SimpleStringSplitter(',');
        ((TextUtils.StringSplitter)localObject2).setString(str);
        localObject2 = ((TextUtils.StringSplitter)localObject2).iterator();
        for (;;)
        {
          j = i;
          if (!((Iterator)localObject2).hasNext()) {
            break label114;
          }
          if ((((String)localObject1).equals((String)((Iterator)localObject2).next())) || ((((String)localObject1).startsWith("com.asus")) && (!((String)localObject1).equals("com.asus.cnasusincallui")))) {
            break;
          }
        }
        j = 1;
      }
      label114:
      if ((j == 0) && (Integer.parseInt(paramString) >= 2))
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("ignore the torch status update of camera: ");
        ((StringBuilder)localObject1).append(paramString);
        Log.w("CameraManagerGlobal", ((StringBuilder)localObject1).toString());
        return;
      }
      boolean bool = validTorchStatus(paramInt);
      j = 0;
      if (!bool)
      {
        Log.e("CameraManagerGlobal", String.format("Ignoring invalid device %s torch status 0x%x", new Object[] { paramString, Integer.valueOf(paramInt) }));
        return;
      }
      localObject1 = (Integer)mTorchStatus.put(paramString, Integer.valueOf(paramInt));
      if ((localObject1 != null) && (((Integer)localObject1).intValue() == paramInt)) {
        return;
      }
      i = mTorchCallbackMap.size();
      while (j < i)
      {
        localObject1 = (Executor)mTorchCallbackMap.valueAt(j);
        postSingleTorchUpdate((CameraManager.TorchCallback)mTorchCallbackMap.keyAt(j), (Executor)localObject1, paramString, paramInt);
        j++;
      }
    }
    
    private void postSingleTorchUpdate(CameraManager.TorchCallback paramTorchCallback, Executor paramExecutor, String paramString, int paramInt)
    {
      long l;
      Object localObject;
      switch (paramInt)
      {
      default: 
        l = Binder.clearCallingIdentity();
        break;
      case 1: 
      case 2: 
        l = Binder.clearCallingIdentity();
        try
        {
          localObject = new android/hardware/camera2/_$$Lambda$CameraManager$CameraManagerGlobal$CONvadOBAEkcHSpx8j61v67qRGM;
          ((_..Lambda.CameraManager.CameraManagerGlobal.CONvadOBAEkcHSpx8j61v67qRGM)localObject).<init>(paramTorchCallback, paramString, paramInt);
          paramExecutor.execute((Runnable)localObject);
          Binder.restoreCallingIdentity(l);
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
      }
      try
      {
        localObject = new android/hardware/camera2/_$$Lambda$CameraManager$CameraManagerGlobal$6Ptxoe4wF_VCkE_pml8t66mklao;
        ((_..Lambda.CameraManager.CameraManagerGlobal.6Ptxoe4wF_VCkE_pml8t66mklao)localObject).<init>(paramTorchCallback, paramString);
        paramExecutor.execute((Runnable)localObject);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    
    private void postSingleUpdate(CameraManager.AvailabilityCallback paramAvailabilityCallback, Executor paramExecutor, String paramString, int paramInt)
    {
      long l;
      if (isAvailable(paramInt)) {
        l = Binder.clearCallingIdentity();
      }
      Object localObject;
      try
      {
        localObject = new android/hardware/camera2/CameraManager$CameraManagerGlobal$2;
        ((2)localObject).<init>(this, paramAvailabilityCallback, paramString);
        paramExecutor.execute((Runnable)localObject);
        Binder.restoreCallingIdentity(l);
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
      try
      {
        localObject = new android/hardware/camera2/CameraManager$CameraManagerGlobal$3;
        ((3)localObject).<init>(this, paramAvailabilityCallback, paramString);
        paramExecutor.execute((Runnable)localObject);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    
    private void scheduleCameraServiceReconnectionLocked()
    {
      if ((mCallbackMap.isEmpty()) && (mTorchCallbackMap.isEmpty())) {
        return;
      }
      try
      {
        localObject = mScheduler;
        _..Lambda.CameraManager.CameraManagerGlobal.w1y8myi6vgxAcTEs8WArI_NN3R0 localW1y8myi6vgxAcTEs8WArI_NN3R0 = new android/hardware/camera2/_$$Lambda$CameraManager$CameraManagerGlobal$w1y8myi6vgxAcTEs8WArI_NN3R0;
        localW1y8myi6vgxAcTEs8WArI_NN3R0.<init>(this);
        ((ScheduledExecutorService)localObject).schedule(localW1y8myi6vgxAcTEs8WArI_NN3R0, 1000L, TimeUnit.MILLISECONDS);
      }
      catch (RejectedExecutionException localRejectedExecutionException)
      {
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Failed to schedule camera service re-connect: ");
        ((StringBuilder)localObject).append(localRejectedExecutionException);
        Log.e("CameraManagerGlobal", ((StringBuilder)localObject).toString());
      }
    }
    
    private void updateCallbackLocked(CameraManager.AvailabilityCallback paramAvailabilityCallback, Executor paramExecutor)
    {
      for (int i = 0; i < mDeviceStatus.size(); i++) {
        postSingleUpdate(paramAvailabilityCallback, paramExecutor, (String)mDeviceStatus.keyAt(i), ((Integer)mDeviceStatus.valueAt(i)).intValue());
      }
    }
    
    private void updateTorchCallbackLocked(CameraManager.TorchCallback paramTorchCallback, Executor paramExecutor)
    {
      for (int i = 0; i < mTorchStatus.size(); i++) {
        postSingleTorchUpdate(paramTorchCallback, paramExecutor, (String)mTorchStatus.keyAt(i), ((Integer)mTorchStatus.valueAt(i)).intValue());
      }
    }
    
    private boolean validStatus(int paramInt)
    {
      if (paramInt != -2) {
        switch (paramInt)
        {
        default: 
          return false;
        }
      }
      return true;
    }
    
    private boolean validTorchStatus(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return false;
      }
      return true;
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public void binderDied()
    {
      synchronized (mLock)
      {
        if (mCameraService == null) {
          return;
        }
        mCameraService = null;
        for (int i = 0; i < mDeviceStatus.size(); i++) {
          onStatusChangedLocked(0, (String)mDeviceStatus.keyAt(i));
        }
        for (i = 0; i < mTorchStatus.size(); i++) {
          onTorchStatusChangedLocked(0, (String)mTorchStatus.keyAt(i));
        }
        scheduleCameraServiceReconnectionLocked();
        return;
      }
    }
    
    public String[] getCameraIdList()
    {
      synchronized (mLock)
      {
        connectCameraServiceLocked();
        int i = 0;
        Object localObject2 = ActivityThread.currentOpPackageName();
        Object localObject4 = SystemProperties.get("vendor.camera.aux.packagelist");
        int j = i;
        if (((String)localObject4).length() > 0)
        {
          TextUtils.SimpleStringSplitter localSimpleStringSplitter = new android/text/TextUtils$SimpleStringSplitter;
          localSimpleStringSplitter.<init>(',');
          localSimpleStringSplitter.setString((String)localObject4);
          localObject4 = localSimpleStringSplitter.iterator();
          for (;;)
          {
            j = i;
            if (!((Iterator)localObject4).hasNext()) {
              break label122;
            }
            if ((((String)localObject2).equals((String)((Iterator)localObject4).next())) || ((((String)localObject2).startsWith("com.asus")) && (!((String)localObject2).equals("com.asus.cnasusincallui")))) {
              break;
            }
          }
          j = 1;
        }
        label122:
        int k = 0;
        int m = 0;
        int n = 0;
        while ((n < mDeviceStatus.size()) && ((j != 0) || (n != 2)))
        {
          int i1 = ((Integer)mDeviceStatus.valueAt(n)).intValue();
          i = m;
          if (i1 != 0) {
            if (i1 == 2) {
              i = m;
            } else {
              i = m + 1;
            }
          }
          n++;
          m = i;
        }
        localObject2 = new String[m];
        m = 0;
        i = k;
        while ((i < mDeviceStatus.size()) && ((j != 0) || (i != 2)))
        {
          k = ((Integer)mDeviceStatus.valueAt(i)).intValue();
          n = m;
          if (k != 0) {
            if (k == 2)
            {
              n = m;
            }
            else
            {
              localObject2[m] = ((String)mDeviceStatus.keyAt(i));
              n = m + 1;
            }
          }
          i++;
          m = n;
        }
        Arrays.sort((Object[])localObject2, new Comparator()
        {
          public int compare(String paramAnonymousString1, String paramAnonymousString2)
          {
            int i;
            try
            {
              i = Integer.parseInt(paramAnonymousString1);
            }
            catch (NumberFormatException localNumberFormatException1)
            {
              i = -1;
            }
            int j;
            try
            {
              j = Integer.parseInt(paramAnonymousString2);
            }
            catch (NumberFormatException localNumberFormatException2)
            {
              j = -1;
            }
            if ((i >= 0) && (j >= 0)) {
              return i - j;
            }
            if (i >= 0) {
              return -1;
            }
            if (j >= 0) {
              return 1;
            }
            return paramAnonymousString1.compareTo(paramAnonymousString2);
          }
        });
        return localObject2;
      }
    }
    
    public ICameraService getCameraService()
    {
      synchronized (mLock)
      {
        connectCameraServiceLocked();
        if ((mCameraService == null) && (!sCameraServiceDisabled)) {
          Log.e("CameraManagerGlobal", "Camera service is unavailable");
        }
        ICameraService localICameraService = mCameraService;
        return localICameraService;
      }
    }
    
    public void onStatusChanged(int paramInt, String paramString)
      throws RemoteException
    {
      synchronized (mLock)
      {
        onStatusChangedLocked(paramInt, paramString);
        return;
      }
    }
    
    public void onTorchStatusChanged(int paramInt, String paramString)
      throws RemoteException
    {
      synchronized (mLock)
      {
        onTorchStatusChangedLocked(paramInt, paramString);
        return;
      }
    }
    
    public void registerAvailabilityCallback(CameraManager.AvailabilityCallback paramAvailabilityCallback, Executor paramExecutor)
    {
      synchronized (mLock)
      {
        connectCameraServiceLocked();
        if ((Executor)mCallbackMap.put(paramAvailabilityCallback, paramExecutor) == null) {
          updateCallbackLocked(paramAvailabilityCallback, paramExecutor);
        }
        if (mCameraService == null) {
          scheduleCameraServiceReconnectionLocked();
        }
        return;
      }
    }
    
    public void registerTorchCallback(CameraManager.TorchCallback paramTorchCallback, Executor paramExecutor)
    {
      synchronized (mLock)
      {
        connectCameraServiceLocked();
        if ((Executor)mTorchCallbackMap.put(paramTorchCallback, paramExecutor) == null) {
          updateTorchCallbackLocked(paramTorchCallback, paramExecutor);
        }
        if (mCameraService == null) {
          scheduleCameraServiceReconnectionLocked();
        }
        return;
      }
    }
    
    public void setTorchMode(String paramString, boolean paramBoolean)
      throws CameraAccessException
    {
      localObject1 = mLock;
      int i;
      if (paramString != null) {
        i = 0;
      }
      try
      {
        Object localObject2 = ActivityThread.currentOpPackageName();
        String str = SystemProperties.get("vendor.camera.aux.packagelist");
        int j = i;
        if (str.length() > 0)
        {
          Object localObject3 = new android/text/TextUtils$SimpleStringSplitter;
          ((TextUtils.SimpleStringSplitter)localObject3).<init>(',');
          ((TextUtils.StringSplitter)localObject3).setString(str);
          localObject3 = ((TextUtils.StringSplitter)localObject3).iterator();
          for (;;)
          {
            j = i;
            if (!((Iterator)localObject3).hasNext()) {
              break label129;
            }
            if ((((String)localObject2).equals((String)((Iterator)localObject3).next())) || ((((String)localObject2).startsWith("com.asus")) && (!((String)localObject2).equals("com.asus.cnasusincallui")))) {
              break;
            }
          }
          j = 1;
        }
        label129:
        if ((j == 0) && (Integer.parseInt(paramString) >= 2))
        {
          paramString = new java/lang/IllegalArgumentException;
          paramString.<init>("invalid cameraId");
          throw paramString;
        }
        localObject2 = getCameraService();
        if (localObject2 != null)
        {
          try
          {
            ((ICameraService)localObject2).setTorchMode(paramString, paramBoolean, mTorchClientBinder);
          }
          catch (RemoteException paramString)
          {
            paramString = new android/hardware/camera2/CameraAccessException;
            paramString.<init>(2, "Camera service is currently unavailable");
            throw paramString;
          }
          catch (ServiceSpecificException paramString)
          {
            for (;;)
            {
              CameraManager.throwAsPublicException(paramString);
            }
          }
          return;
        }
        paramString = new android/hardware/camera2/CameraAccessException;
        paramString.<init>(2, "Camera service is currently unavailable");
        throw paramString;
      }
      finally {}
      paramString = new java/lang/IllegalArgumentException;
      paramString.<init>("cameraId was null");
      throw paramString;
    }
    
    public void unregisterAvailabilityCallback(CameraManager.AvailabilityCallback paramAvailabilityCallback)
    {
      synchronized (mLock)
      {
        mCallbackMap.remove(paramAvailabilityCallback);
        return;
      }
    }
    
    public void unregisterTorchCallback(CameraManager.TorchCallback paramTorchCallback)
    {
      synchronized (mLock)
      {
        mTorchCallbackMap.remove(paramTorchCallback);
        return;
      }
    }
  }
  
  public static abstract class TorchCallback
  {
    public TorchCallback() {}
    
    public void onTorchModeChanged(String paramString, boolean paramBoolean) {}
    
    public void onTorchModeUnavailable(String paramString) {}
  }
}
