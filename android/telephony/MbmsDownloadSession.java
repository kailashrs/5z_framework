package android.telephony;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.telephony.mbms.DownloadProgressListener;
import android.telephony.mbms.DownloadRequest;
import android.telephony.mbms.DownloadStatusListener;
import android.telephony.mbms.FileInfo;
import android.telephony.mbms.InternalDownloadProgressListener;
import android.telephony.mbms.InternalDownloadSessionCallback;
import android.telephony.mbms.InternalDownloadStatusListener;
import android.telephony.mbms.MbmsDownloadSessionCallback;
import android.telephony.mbms.MbmsUtils;
import android.telephony.mbms.vendor.IMbmsDownloadService;
import android.telephony.mbms.vendor.IMbmsDownloadService.Stub;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MbmsDownloadSession
  implements AutoCloseable
{
  public static final String DEFAULT_TOP_LEVEL_TEMP_DIRECTORY = "androidMbmsTempFileRoot";
  private static final String DESTINATION_SANITY_CHECK_FILE_NAME = "destinationSanityCheckFile";
  public static final String EXTRA_MBMS_COMPLETED_FILE_URI = "android.telephony.extra.MBMS_COMPLETED_FILE_URI";
  public static final String EXTRA_MBMS_DOWNLOAD_REQUEST = "android.telephony.extra.MBMS_DOWNLOAD_REQUEST";
  public static final String EXTRA_MBMS_DOWNLOAD_RESULT = "android.telephony.extra.MBMS_DOWNLOAD_RESULT";
  public static final String EXTRA_MBMS_FILE_INFO = "android.telephony.extra.MBMS_FILE_INFO";
  private static final String LOG_TAG = MbmsDownloadSession.class.getSimpleName();
  @SystemApi
  public static final String MBMS_DOWNLOAD_SERVICE_ACTION = "android.telephony.action.EmbmsDownload";
  public static final String MBMS_DOWNLOAD_SERVICE_OVERRIDE_METADATA = "mbms-download-service-override";
  public static final int RESULT_CANCELLED = 2;
  public static final int RESULT_DOWNLOAD_FAILURE = 6;
  public static final int RESULT_EXPIRED = 3;
  public static final int RESULT_FILE_ROOT_UNREACHABLE = 8;
  public static final int RESULT_IO_ERROR = 4;
  public static final int RESULT_OUT_OF_STORAGE = 7;
  public static final int RESULT_SERVICE_ID_NOT_DEFINED = 5;
  public static final int RESULT_SUCCESSFUL = 1;
  public static final int STATUS_ACTIVELY_DOWNLOADING = 1;
  public static final int STATUS_PENDING_DOWNLOAD = 2;
  public static final int STATUS_PENDING_DOWNLOAD_WINDOW = 4;
  public static final int STATUS_PENDING_REPAIR = 3;
  public static final int STATUS_UNKNOWN = 0;
  private static AtomicBoolean sIsInitialized = new AtomicBoolean(false);
  private final Context mContext;
  private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient()
  {
    public void binderDied()
    {
      MbmsDownloadSession.this.sendErrorToApp(3, "Received death notification");
    }
  };
  private final InternalDownloadSessionCallback mInternalCallback;
  private final Map<DownloadProgressListener, InternalDownloadProgressListener> mInternalDownloadProgressListeners = new HashMap();
  private final Map<DownloadStatusListener, InternalDownloadStatusListener> mInternalDownloadStatusListeners = new HashMap();
  private AtomicReference<IMbmsDownloadService> mService = new AtomicReference(null);
  private int mSubscriptionId = -1;
  
  private MbmsDownloadSession(Context paramContext, Executor paramExecutor, int paramInt, MbmsDownloadSessionCallback paramMbmsDownloadSessionCallback)
  {
    mContext = paramContext;
    mSubscriptionId = paramInt;
    mInternalCallback = new InternalDownloadSessionCallback(paramMbmsDownloadSessionCallback, paramExecutor);
  }
  
  private int bindAndInitialize()
  {
    MbmsUtils.startBinding(mContext, "android.telephony.action.EmbmsDownload", new ServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        paramAnonymousComponentName = IMbmsDownloadService.Stub.asInterface(paramAnonymousIBinder);
        try
        {
          int i = paramAnonymousComponentName.initialize(mSubscriptionId, mInternalCallback);
          if (i != -1)
          {
            if (i != 0)
            {
              MbmsDownloadSession.this.sendErrorToApp(i, "Error returned during initialization");
              MbmsDownloadSession.sIsInitialized.set(false);
              return;
            }
            try
            {
              paramAnonymousComponentName.asBinder().linkToDeath(mDeathRecipient, 0);
              mService.set(paramAnonymousComponentName);
              return;
            }
            catch (RemoteException paramAnonymousComponentName)
            {
              MbmsDownloadSession.this.sendErrorToApp(3, "Middleware lost during initialization");
              MbmsDownloadSession.sIsInitialized.set(false);
              return;
            }
          }
          close();
          throw new IllegalStateException("Middleware must not return an unknown error code");
        }
        catch (RuntimeException paramAnonymousComponentName)
        {
          Log.e(MbmsDownloadSession.LOG_TAG, "Runtime exception during initialization");
          MbmsDownloadSession.this.sendErrorToApp(103, paramAnonymousComponentName.toString());
          MbmsDownloadSession.sIsInitialized.set(false);
          return;
        }
        catch (RemoteException paramAnonymousComponentName)
        {
          Log.e(MbmsDownloadSession.LOG_TAG, "Service died before initialization");
          MbmsDownloadSession.sIsInitialized.set(false);
        }
      }
      
      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
        Log.w(MbmsDownloadSession.LOG_TAG, "bindAndInitialize: Remote service disconnected");
        MbmsDownloadSession.sIsInitialized.set(false);
        mService.set(null);
      }
    });
  }
  
  /* Error */
  private void checkDownloadRequestDestination(DownloadRequest paramDownloadRequest)
  {
    // Byte code:
    //   0: new 169	java/io/File
    //   3: dup
    //   4: aload_1
    //   5: invokevirtual 175	android/telephony/mbms/DownloadRequest:getDestinationUri	()Landroid/net/Uri;
    //   8: invokevirtual 180	android/net/Uri:getPath	()Ljava/lang/String;
    //   11: invokespecial 183	java/io/File:<init>	(Ljava/lang/String;)V
    //   14: astore_2
    //   15: aload_2
    //   16: invokevirtual 187	java/io/File:isDirectory	()Z
    //   19: ifeq +143 -> 162
    //   22: new 169	java/io/File
    //   25: dup
    //   26: aload_0
    //   27: getfield 130	android/telephony/MbmsDownloadSession:mContext	Landroid/content/Context;
    //   30: invokestatic 193	android/telephony/mbms/MbmsTempFileProvider:getEmbmsTempFileDir	(Landroid/content/Context;)Ljava/io/File;
    //   33: ldc 25
    //   35: invokespecial 196	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   38: astore_1
    //   39: new 169	java/io/File
    //   42: dup
    //   43: aload_2
    //   44: ldc 25
    //   46: invokespecial 196	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   49: astore_2
    //   50: aload_1
    //   51: invokevirtual 199	java/io/File:exists	()Z
    //   54: ifne +8 -> 62
    //   57: aload_1
    //   58: invokevirtual 202	java/io/File:createNewFile	()Z
    //   61: pop
    //   62: aload_1
    //   63: aload_2
    //   64: invokevirtual 206	java/io/File:renameTo	(Ljava/io/File;)Z
    //   67: istore_3
    //   68: iload_3
    //   69: ifeq +14 -> 83
    //   72: aload_1
    //   73: invokevirtual 209	java/io/File:delete	()Z
    //   76: pop
    //   77: aload_2
    //   78: invokevirtual 209	java/io/File:delete	()Z
    //   81: pop
    //   82: return
    //   83: new 211	java/lang/IllegalArgumentException
    //   86: astore 4
    //   88: aload 4
    //   90: ldc -43
    //   92: invokespecial 214	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   95: aload 4
    //   97: athrow
    //   98: astore 4
    //   100: goto +49 -> 149
    //   103: astore 5
    //   105: new 216	java/lang/IllegalStateException
    //   108: astore 4
    //   110: new 218	java/lang/StringBuilder
    //   113: astore 6
    //   115: aload 6
    //   117: invokespecial 219	java/lang/StringBuilder:<init>	()V
    //   120: aload 6
    //   122: ldc -35
    //   124: invokevirtual 225	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   127: pop
    //   128: aload 6
    //   130: aload 5
    //   132: invokevirtual 228	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   135: pop
    //   136: aload 4
    //   138: aload 6
    //   140: invokevirtual 231	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   143: invokespecial 232	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   146: aload 4
    //   148: athrow
    //   149: aload_1
    //   150: invokevirtual 209	java/io/File:delete	()Z
    //   153: pop
    //   154: aload_2
    //   155: invokevirtual 209	java/io/File:delete	()Z
    //   158: pop
    //   159: aload 4
    //   161: athrow
    //   162: new 211	java/lang/IllegalArgumentException
    //   165: dup
    //   166: ldc -22
    //   168: invokespecial 214	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   171: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	172	0	this	MbmsDownloadSession
    //   0	172	1	paramDownloadRequest	DownloadRequest
    //   14	141	2	localFile	File
    //   67	2	3	bool	boolean
    //   86	10	4	localIllegalArgumentException	IllegalArgumentException
    //   98	1	4	localObject	Object
    //   108	52	4	localIllegalStateException	IllegalStateException
    //   103	28	5	localIOException	IOException
    //   113	26	6	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   50	62	98	finally
    //   62	68	98	finally
    //   83	98	98	finally
    //   105	149	98	finally
    //   50	62	103	java/io/IOException
    //   62	68	103	java/io/IOException
    //   83	98	103	java/io/IOException
  }
  
  public static MbmsDownloadSession create(Context paramContext, Executor paramExecutor, final int paramInt, MbmsDownloadSessionCallback paramMbmsDownloadSessionCallback)
  {
    if (sIsInitialized.compareAndSet(false, true))
    {
      paramContext = new MbmsDownloadSession(paramContext, paramExecutor, paramInt, paramMbmsDownloadSessionCallback);
      paramInt = paramContext.bindAndInitialize();
      if (paramInt != 0)
      {
        sIsInitialized.set(false);
        paramExecutor.execute(new Runnable()
        {
          public void run()
          {
            onError(paramInt, null);
          }
        });
        return null;
      }
      return paramContext;
    }
    throw new IllegalStateException("Cannot have two active instances");
  }
  
  public static MbmsDownloadSession create(Context paramContext, Executor paramExecutor, MbmsDownloadSessionCallback paramMbmsDownloadSessionCallback)
  {
    return create(paramContext, paramExecutor, SubscriptionManager.getDefaultSubscriptionId(), paramMbmsDownloadSessionCallback);
  }
  
  private void deleteDownloadRequestToken(DownloadRequest paramDownloadRequest)
  {
    paramDownloadRequest = getDownloadRequestTokenPath(paramDownloadRequest);
    String str;
    StringBuilder localStringBuilder;
    if (!paramDownloadRequest.isFile())
    {
      str = LOG_TAG;
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Attempting to delete non-existent download token at ");
      localStringBuilder.append(paramDownloadRequest);
      Log.w(str, localStringBuilder.toString());
      return;
    }
    if (!paramDownloadRequest.delete())
    {
      str = LOG_TAG;
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Couldn't delete download token at ");
      localStringBuilder.append(paramDownloadRequest);
      Log.w(str, localStringBuilder.toString());
    }
  }
  
  private File getDownloadRequestTokenPath(DownloadRequest paramDownloadRequest)
  {
    File localFile = MbmsUtils.getEmbmsTempFileDirForService(mContext, paramDownloadRequest.getFileServiceId());
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramDownloadRequest.getHash());
    localStringBuilder.append(".download_token");
    return new File(localFile, localStringBuilder.toString());
  }
  
  private void sendErrorToApp(int paramInt, String paramString)
  {
    mInternalCallback.onError(paramInt, paramString);
  }
  
  private void validateTempFileRootSanity(File paramFile)
    throws IOException
  {
    if (paramFile.exists())
    {
      if (paramFile.isDirectory())
      {
        paramFile = paramFile.getCanonicalPath();
        if (!mContext.getDataDir().getCanonicalPath().equals(paramFile))
        {
          if (!mContext.getCacheDir().getCanonicalPath().equals(paramFile))
          {
            if (!mContext.getFilesDir().getCanonicalPath().equals(paramFile)) {
              return;
            }
            throw new IllegalArgumentException("Temp file root cannot be your files dir");
          }
          throw new IllegalArgumentException("Temp file root cannot be your cache dir");
        }
        throw new IllegalArgumentException("Temp file root cannot be your data dir");
      }
      throw new IllegalArgumentException("Provided File is not a directory");
    }
    throw new IllegalArgumentException("Provided directory does not exist");
  }
  
  private void writeDownloadRequestToken(DownloadRequest paramDownloadRequest)
  {
    File localFile = getDownloadRequestTokenPath(paramDownloadRequest);
    if (!localFile.getParentFile().exists()) {
      localFile.getParentFile().mkdirs();
    }
    Object localObject;
    if (localFile.exists())
    {
      paramDownloadRequest = LOG_TAG;
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Download token ");
      ((StringBuilder)localObject).append(localFile.getName());
      ((StringBuilder)localObject).append(" already exists");
      Log.w(paramDownloadRequest, ((StringBuilder)localObject).toString());
      return;
    }
    try
    {
      if (localFile.createNewFile()) {
        return;
      }
      localObject = new java/lang/RuntimeException;
      localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Failed to create download token for request ");
      localStringBuilder.append(paramDownloadRequest);
      localStringBuilder.append(". Token location is ");
      localStringBuilder.append(localFile.getPath());
      ((RuntimeException)localObject).<init>(localStringBuilder.toString());
      throw ((Throwable)localObject);
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Failed to create download token for request ");
      localStringBuilder.append(paramDownloadRequest);
      localStringBuilder.append(" due to IOException ");
      localStringBuilder.append(localIOException);
      localStringBuilder.append(". Attempted to write to ");
      localStringBuilder.append(localFile.getPath());
      throw new RuntimeException(localStringBuilder.toString());
    }
  }
  
  public void addProgressListener(DownloadRequest paramDownloadRequest, Executor paramExecutor, DownloadProgressListener paramDownloadProgressListener)
  {
    IMbmsDownloadService localIMbmsDownloadService = (IMbmsDownloadService)mService.get();
    if (localIMbmsDownloadService != null)
    {
      paramExecutor = new InternalDownloadProgressListener(paramDownloadProgressListener, paramExecutor);
      try
      {
        int i = localIMbmsDownloadService.addProgressListener(paramDownloadRequest, paramExecutor);
        if (i != -1)
        {
          if (i != 0)
          {
            if (i != 402)
            {
              sendErrorToApp(i, null);
              return;
            }
            paramDownloadRequest = new java/lang/IllegalArgumentException;
            paramDownloadRequest.<init>("Unknown download request.");
            throw paramDownloadRequest;
          }
          mInternalDownloadProgressListeners.put(paramDownloadProgressListener, paramExecutor);
          return;
        }
        close();
        paramDownloadRequest = new java/lang/IllegalStateException;
        paramDownloadRequest.<init>("Middleware must not return an unknown error code");
        throw paramDownloadRequest;
      }
      catch (RemoteException paramDownloadRequest)
      {
        mService.set(null);
        sIsInitialized.set(false);
        sendErrorToApp(3, null);
        return;
      }
    }
    throw new IllegalStateException("Middleware not yet bound");
  }
  
  public void addStatusListener(DownloadRequest paramDownloadRequest, Executor paramExecutor, DownloadStatusListener paramDownloadStatusListener)
  {
    IMbmsDownloadService localIMbmsDownloadService = (IMbmsDownloadService)mService.get();
    if (localIMbmsDownloadService != null)
    {
      paramExecutor = new InternalDownloadStatusListener(paramDownloadStatusListener, paramExecutor);
      try
      {
        int i = localIMbmsDownloadService.addStatusListener(paramDownloadRequest, paramExecutor);
        if (i != -1)
        {
          if (i != 0)
          {
            if (i != 402)
            {
              sendErrorToApp(i, null);
              return;
            }
            paramDownloadRequest = new java/lang/IllegalArgumentException;
            paramDownloadRequest.<init>("Unknown download request.");
            throw paramDownloadRequest;
          }
          mInternalDownloadStatusListeners.put(paramDownloadStatusListener, paramExecutor);
          return;
        }
        close();
        paramDownloadRequest = new java/lang/IllegalStateException;
        paramDownloadRequest.<init>("Middleware must not return an unknown error code");
        throw paramDownloadRequest;
      }
      catch (RemoteException paramDownloadRequest)
      {
        mService.set(null);
        sIsInitialized.set(false);
        sendErrorToApp(3, null);
        return;
      }
    }
    throw new IllegalStateException("Middleware not yet bound");
  }
  
  public void cancelDownload(DownloadRequest paramDownloadRequest)
  {
    IMbmsDownloadService localIMbmsDownloadService = (IMbmsDownloadService)mService.get();
    if (localIMbmsDownloadService != null)
    {
      try
      {
        int i = localIMbmsDownloadService.cancelDownload(paramDownloadRequest);
        if (i != -1)
        {
          if (i != 0) {
            sendErrorToApp(i, null);
          } else {
            deleteDownloadRequestToken(paramDownloadRequest);
          }
        }
        else
        {
          close();
          paramDownloadRequest = new java/lang/IllegalStateException;
          paramDownloadRequest.<init>("Middleware must not return an unknown error code");
          throw paramDownloadRequest;
        }
      }
      catch (RemoteException paramDownloadRequest)
      {
        mService.set(null);
        sIsInitialized.set(false);
        sendErrorToApp(3, null);
      }
      return;
    }
    throw new IllegalStateException("Middleware not yet bound");
  }
  
  /* Error */
  public void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 121	android/telephony/MbmsDownloadSession:mService	Ljava/util/concurrent/atomic/AtomicReference;
    //   4: invokevirtual 367	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
    //   7: checkcast 369	android/telephony/mbms/vendor/IMbmsDownloadService
    //   10: astore_1
    //   11: aload_1
    //   12: ifnonnull +36 -> 48
    //   15: getstatic 95	android/telephony/MbmsDownloadSession:LOG_TAG	Ljava/lang/String;
    //   18: ldc_w 412
    //   21: invokestatic 415	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   24: pop
    //   25: aload_0
    //   26: getfield 121	android/telephony/MbmsDownloadSession:mService	Ljava/util/concurrent/atomic/AtomicReference;
    //   29: aconst_null
    //   30: invokevirtual 392	java/util/concurrent/atomic/AtomicReference:set	(Ljava/lang/Object;)V
    //   33: getstatic 103	android/telephony/MbmsDownloadSession:sIsInitialized	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   36: iconst_0
    //   37: invokevirtual 247	java/util/concurrent/atomic/AtomicBoolean:set	(Z)V
    //   40: aload_0
    //   41: getfield 137	android/telephony/MbmsDownloadSession:mInternalCallback	Landroid/telephony/mbms/InternalDownloadSessionCallback;
    //   44: invokevirtual 418	android/telephony/mbms/InternalDownloadSessionCallback:stop	()V
    //   47: return
    //   48: aload_1
    //   49: aload_0
    //   50: getfield 109	android/telephony/MbmsDownloadSession:mSubscriptionId	I
    //   53: invokeinterface 422 2 0
    //   58: goto +18 -> 76
    //   61: astore_1
    //   62: goto +37 -> 99
    //   65: astore_1
    //   66: getstatic 95	android/telephony/MbmsDownloadSession:LOG_TAG	Ljava/lang/String;
    //   69: ldc_w 424
    //   72: invokestatic 415	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   75: pop
    //   76: aload_0
    //   77: getfield 121	android/telephony/MbmsDownloadSession:mService	Ljava/util/concurrent/atomic/AtomicReference;
    //   80: aconst_null
    //   81: invokevirtual 392	java/util/concurrent/atomic/AtomicReference:set	(Ljava/lang/Object;)V
    //   84: getstatic 103	android/telephony/MbmsDownloadSession:sIsInitialized	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   87: iconst_0
    //   88: invokevirtual 247	java/util/concurrent/atomic/AtomicBoolean:set	(Z)V
    //   91: aload_0
    //   92: getfield 137	android/telephony/MbmsDownloadSession:mInternalCallback	Landroid/telephony/mbms/InternalDownloadSessionCallback;
    //   95: invokevirtual 418	android/telephony/mbms/InternalDownloadSessionCallback:stop	()V
    //   98: return
    //   99: aload_0
    //   100: getfield 121	android/telephony/MbmsDownloadSession:mService	Ljava/util/concurrent/atomic/AtomicReference;
    //   103: aconst_null
    //   104: invokevirtual 392	java/util/concurrent/atomic/AtomicReference:set	(Ljava/lang/Object;)V
    //   107: getstatic 103	android/telephony/MbmsDownloadSession:sIsInitialized	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   110: iconst_0
    //   111: invokevirtual 247	java/util/concurrent/atomic/AtomicBoolean:set	(Z)V
    //   114: aload_0
    //   115: getfield 137	android/telephony/MbmsDownloadSession:mInternalCallback	Landroid/telephony/mbms/InternalDownloadSessionCallback;
    //   118: invokevirtual 418	android/telephony/mbms/InternalDownloadSessionCallback:stop	()V
    //   121: aload_1
    //   122: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	123	0	this	MbmsDownloadSession
    //   10	39	1	localIMbmsDownloadService	IMbmsDownloadService
    //   61	1	1	localObject	Object
    //   65	57	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   0	11	61	finally
    //   15	25	61	finally
    //   48	58	61	finally
    //   66	76	61	finally
    //   0	11	65	android/os/RemoteException
    //   15	25	65	android/os/RemoteException
    //   48	58	65	android/os/RemoteException
  }
  
  public void download(DownloadRequest paramDownloadRequest)
  {
    IMbmsDownloadService localIMbmsDownloadService = (IMbmsDownloadService)mService.get();
    if (localIMbmsDownloadService != null)
    {
      if (mContext.getSharedPreferences("MbmsTempFileRootPrefs", 0).getString("mbms_temp_file_root", null) == null)
      {
        File localFile = new File(mContext.getFilesDir(), "androidMbmsTempFileRoot");
        localFile.mkdirs();
        setTempFileRootDirectory(localFile);
      }
      checkDownloadRequestDestination(paramDownloadRequest);
      try
      {
        int i = localIMbmsDownloadService.download(paramDownloadRequest);
        if (i == 0)
        {
          writeDownloadRequestToken(paramDownloadRequest);
        }
        else
        {
          if (i == -1) {
            break label108;
          }
          sendErrorToApp(i, null);
        }
        break label147;
        label108:
        close();
        paramDownloadRequest = new java/lang/IllegalStateException;
        paramDownloadRequest.<init>("Middleware must not return an unknown error code");
        throw paramDownloadRequest;
      }
      catch (RemoteException paramDownloadRequest)
      {
        mService.set(null);
        sIsInitialized.set(false);
        sendErrorToApp(3, null);
      }
      label147:
      return;
    }
    throw new IllegalStateException("Middleware not yet bound");
  }
  
  public File getTempFileRootDirectory()
  {
    String str = mContext.getSharedPreferences("MbmsTempFileRootPrefs", 0).getString("mbms_temp_file_root", null);
    if (str != null) {
      return new File(str);
    }
    return null;
  }
  
  public List<DownloadRequest> listPendingDownloads()
  {
    Object localObject = (IMbmsDownloadService)mService.get();
    if (localObject != null) {
      try
      {
        localObject = ((IMbmsDownloadService)localObject).listPendingDownloads(mSubscriptionId);
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        mService.set(null);
        sIsInitialized.set(false);
        sendErrorToApp(3, null);
        return Collections.emptyList();
      }
    }
    throw new IllegalStateException("Middleware not yet bound");
  }
  
  public void removeProgressListener(DownloadRequest paramDownloadRequest, DownloadProgressListener paramDownloadProgressListener)
  {
    try
    {
      IMbmsDownloadService localIMbmsDownloadService = (IMbmsDownloadService)mService.get();
      if (localIMbmsDownloadService != null)
      {
        InternalDownloadProgressListener localInternalDownloadProgressListener = (InternalDownloadProgressListener)mInternalDownloadProgressListeners.get(paramDownloadProgressListener);
        if (localInternalDownloadProgressListener != null) {
          try
          {
            int i = localIMbmsDownloadService.removeProgressListener(paramDownloadRequest, localInternalDownloadProgressListener);
            if (i != -1)
            {
              if (i != 0)
              {
                if (i != 402)
                {
                  sendErrorToApp(i, null);
                  return;
                }
                paramDownloadRequest = new java/lang/IllegalArgumentException;
                paramDownloadRequest.<init>("Unknown download request.");
                throw paramDownloadRequest;
              }
              return;
            }
            close();
            paramDownloadRequest = new java/lang/IllegalStateException;
            paramDownloadRequest.<init>("Middleware must not return an unknown error code");
            throw paramDownloadRequest;
          }
          catch (RemoteException paramDownloadRequest)
          {
            mService.set(null);
            sIsInitialized.set(false);
            sendErrorToApp(3, null);
            return;
          }
        }
        paramDownloadRequest = new java/lang/IllegalArgumentException;
        paramDownloadRequest.<init>("Provided listener was never registered");
        throw paramDownloadRequest;
      }
      paramDownloadRequest = new java/lang/IllegalStateException;
      paramDownloadRequest.<init>("Middleware not yet bound");
      throw paramDownloadRequest;
    }
    finally
    {
      paramDownloadProgressListener = (InternalDownloadProgressListener)mInternalDownloadProgressListeners.remove(paramDownloadProgressListener);
      if (paramDownloadProgressListener != null) {
        paramDownloadProgressListener.stop();
      }
    }
  }
  
  public void removeStatusListener(DownloadRequest paramDownloadRequest, DownloadStatusListener paramDownloadStatusListener)
  {
    try
    {
      IMbmsDownloadService localIMbmsDownloadService = (IMbmsDownloadService)mService.get();
      if (localIMbmsDownloadService != null)
      {
        InternalDownloadStatusListener localInternalDownloadStatusListener = (InternalDownloadStatusListener)mInternalDownloadStatusListeners.get(paramDownloadStatusListener);
        if (localInternalDownloadStatusListener != null) {
          try
          {
            int i = localIMbmsDownloadService.removeStatusListener(paramDownloadRequest, localInternalDownloadStatusListener);
            if (i != -1)
            {
              if (i != 0)
              {
                if (i != 402)
                {
                  sendErrorToApp(i, null);
                  return;
                }
                paramDownloadRequest = new java/lang/IllegalArgumentException;
                paramDownloadRequest.<init>("Unknown download request.");
                throw paramDownloadRequest;
              }
              return;
            }
            close();
            paramDownloadRequest = new java/lang/IllegalStateException;
            paramDownloadRequest.<init>("Middleware must not return an unknown error code");
            throw paramDownloadRequest;
          }
          catch (RemoteException paramDownloadRequest)
          {
            mService.set(null);
            sIsInitialized.set(false);
            sendErrorToApp(3, null);
            return;
          }
        }
        paramDownloadRequest = new java/lang/IllegalArgumentException;
        paramDownloadRequest.<init>("Provided listener was never registered");
        throw paramDownloadRequest;
      }
      paramDownloadRequest = new java/lang/IllegalStateException;
      paramDownloadRequest.<init>("Middleware not yet bound");
      throw paramDownloadRequest;
    }
    finally
    {
      paramDownloadStatusListener = (InternalDownloadStatusListener)mInternalDownloadStatusListeners.remove(paramDownloadStatusListener);
      if (paramDownloadStatusListener != null) {
        paramDownloadStatusListener.stop();
      }
    }
  }
  
  public void requestDownloadState(DownloadRequest paramDownloadRequest, FileInfo paramFileInfo)
  {
    IMbmsDownloadService localIMbmsDownloadService = (IMbmsDownloadService)mService.get();
    if (localIMbmsDownloadService != null)
    {
      try
      {
        int i = localIMbmsDownloadService.requestDownloadState(paramDownloadRequest, paramFileInfo);
        if (i != -1)
        {
          if (i != 0) {
            if (i != 402)
            {
              if (i != 403)
              {
                sendErrorToApp(i, null);
              }
              else
              {
                paramDownloadRequest = new java/lang/IllegalArgumentException;
                paramDownloadRequest.<init>("Unknown file.");
                throw paramDownloadRequest;
              }
            }
            else
            {
              paramDownloadRequest = new java/lang/IllegalArgumentException;
              paramDownloadRequest.<init>("Unknown download request.");
              throw paramDownloadRequest;
            }
          }
        }
        else
        {
          close();
          paramDownloadRequest = new java/lang/IllegalStateException;
          paramDownloadRequest.<init>("Middleware must not return an unknown error code");
          throw paramDownloadRequest;
        }
      }
      catch (RemoteException paramDownloadRequest)
      {
        mService.set(null);
        sIsInitialized.set(false);
        sendErrorToApp(3, null);
      }
      return;
    }
    throw new IllegalStateException("Middleware not yet bound");
  }
  
  public void requestUpdateFileServices(List<String> paramList)
  {
    IMbmsDownloadService localIMbmsDownloadService = (IMbmsDownloadService)mService.get();
    if (localIMbmsDownloadService != null)
    {
      try
      {
        int i = localIMbmsDownloadService.requestUpdateFileServices(mSubscriptionId, paramList);
        if (i != -1)
        {
          if (i != 0) {
            sendErrorToApp(i, null);
          }
        }
        else
        {
          close();
          paramList = new java/lang/IllegalStateException;
          paramList.<init>("Middleware must not return an unknown error code");
          throw paramList;
        }
      }
      catch (RemoteException paramList)
      {
        Log.w(LOG_TAG, "Remote process died");
        mService.set(null);
        sIsInitialized.set(false);
        sendErrorToApp(3, null);
      }
      return;
    }
    throw new IllegalStateException("Middleware not yet bound");
  }
  
  public void resetDownloadKnowledge(DownloadRequest paramDownloadRequest)
  {
    IMbmsDownloadService localIMbmsDownloadService = (IMbmsDownloadService)mService.get();
    if (localIMbmsDownloadService != null)
    {
      try
      {
        int i = localIMbmsDownloadService.resetDownloadKnowledge(paramDownloadRequest);
        if (i != -1)
        {
          if (i != 0) {
            if (i != 402)
            {
              sendErrorToApp(i, null);
            }
            else
            {
              paramDownloadRequest = new java/lang/IllegalArgumentException;
              paramDownloadRequest.<init>("Unknown download request.");
              throw paramDownloadRequest;
            }
          }
        }
        else
        {
          close();
          paramDownloadRequest = new java/lang/IllegalStateException;
          paramDownloadRequest.<init>("Middleware must not return an unknown error code");
          throw paramDownloadRequest;
        }
      }
      catch (RemoteException paramDownloadRequest)
      {
        mService.set(null);
        sIsInitialized.set(false);
        sendErrorToApp(3, null);
      }
      return;
    }
    throw new IllegalStateException("Middleware not yet bound");
  }
  
  /* Error */
  public void setTempFileRootDirectory(File paramFile)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 121	android/telephony/MbmsDownloadSession:mService	Ljava/util/concurrent/atomic/AtomicReference;
    //   4: invokevirtual 367	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
    //   7: checkcast 369	android/telephony/mbms/vendor/IMbmsDownloadService
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull +159 -> 171
    //   15: aload_0
    //   16: aload_1
    //   17: invokespecial 499	android/telephony/MbmsDownloadSession:validateTempFileRootSanity	(Ljava/io/File;)V
    //   20: aload_1
    //   21: invokevirtual 304	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   24: astore_1
    //   25: aload_2
    //   26: aload_0
    //   27: getfield 109	android/telephony/MbmsDownloadSession:mSubscriptionId	I
    //   30: aload_1
    //   31: invokeinterface 502 3 0
    //   36: istore_3
    //   37: iload_3
    //   38: iconst_m1
    //   39: if_icmpeq +45 -> 84
    //   42: iload_3
    //   43: ifeq +10 -> 53
    //   46: aload_0
    //   47: iload_3
    //   48: aconst_null
    //   49: invokespecial 143	android/telephony/MbmsDownloadSession:sendErrorToApp	(ILjava/lang/String;)V
    //   52: return
    //   53: aload_0
    //   54: getfield 130	android/telephony/MbmsDownloadSession:mContext	Landroid/content/Context;
    //   57: ldc_w 427
    //   60: iconst_0
    //   61: invokevirtual 431	android/content/Context:getSharedPreferences	(Ljava/lang/String;I)Landroid/content/SharedPreferences;
    //   64: invokeinterface 506 1 0
    //   69: ldc_w 433
    //   72: aload_1
    //   73: invokeinterface 512 3 0
    //   78: invokeinterface 515 1 0
    //   83: return
    //   84: aload_0
    //   85: invokevirtual 388	android/telephony/MbmsDownloadSession:close	()V
    //   88: new 216	java/lang/IllegalStateException
    //   91: astore_1
    //   92: aload_1
    //   93: ldc_w 390
    //   96: invokespecial 232	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   99: aload_1
    //   100: athrow
    //   101: astore_1
    //   102: aload_0
    //   103: getfield 121	android/telephony/MbmsDownloadSession:mService	Ljava/util/concurrent/atomic/AtomicReference;
    //   106: aconst_null
    //   107: invokevirtual 392	java/util/concurrent/atomic/AtomicReference:set	(Ljava/lang/Object;)V
    //   110: getstatic 103	android/telephony/MbmsDownloadSession:sIsInitialized	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   113: iconst_0
    //   114: invokevirtual 247	java/util/concurrent/atomic/AtomicBoolean:set	(Z)V
    //   117: aload_0
    //   118: iconst_3
    //   119: aconst_null
    //   120: invokespecial 143	android/telephony/MbmsDownloadSession:sendErrorToApp	(ILjava/lang/String;)V
    //   123: return
    //   124: astore_1
    //   125: new 218	java/lang/StringBuilder
    //   128: dup
    //   129: invokespecial 219	java/lang/StringBuilder:<init>	()V
    //   132: astore_2
    //   133: aload_2
    //   134: ldc_w 517
    //   137: invokevirtual 225	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   140: pop
    //   141: aload_2
    //   142: aload_1
    //   143: invokevirtual 228	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   146: pop
    //   147: new 211	java/lang/IllegalArgumentException
    //   150: dup
    //   151: aload_2
    //   152: invokevirtual 231	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   155: invokespecial 214	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   158: athrow
    //   159: astore_1
    //   160: new 216	java/lang/IllegalStateException
    //   163: dup
    //   164: ldc_w 519
    //   167: invokespecial 232	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   170: athrow
    //   171: new 216	java/lang/IllegalStateException
    //   174: dup
    //   175: ldc_w 394
    //   178: invokespecial 232	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   181: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	182	0	this	MbmsDownloadSession
    //   0	182	1	paramFile	File
    //   10	142	2	localObject	Object
    //   36	12	3	i	int
    // Exception table:
    //   from	to	target	type
    //   25	37	101	android/os/RemoteException
    //   46	52	101	android/os/RemoteException
    //   84	101	101	android/os/RemoteException
    //   20	25	124	java/io/IOException
    //   15	20	159	java/io/IOException
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DownloadResultCode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DownloadStatus {}
}
