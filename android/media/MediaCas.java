package android.media;

import android.hardware.cas.V1_0.HidlCasPluginDescriptor;
import android.hardware.cas.V1_0.ICas;
import android.hardware.cas.V1_0.ICas.openSessionCallback;
import android.hardware.cas.V1_0.ICasListener.Stub;
import android.hardware.cas.V1_0.IMediaCasService;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IHwBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Singleton;
import java.util.ArrayList;

public final class MediaCas
  implements AutoCloseable
{
  private static final String TAG = "MediaCas";
  private static final Singleton<IMediaCasService> gDefault = new Singleton()
  {
    protected IMediaCasService create()
    {
      try
      {
        IMediaCasService localIMediaCasService = IMediaCasService.getService();
        return localIMediaCasService;
      }
      catch (RemoteException localRemoteException) {}
      return null;
    }
  };
  private final ICasListener.Stub mBinder;
  private EventHandler mEventHandler;
  private HandlerThread mHandlerThread;
  private ICas mICas;
  private EventListener mListener;
  
  /* Error */
  public MediaCas(int paramInt)
    throws MediaCasException.UnsupportedCasException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 56	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: new 10	android/media/MediaCas$2
    //   8: dup
    //   9: aload_0
    //   10: invokespecial 59	android/media/MediaCas$2:<init>	(Landroid/media/MediaCas;)V
    //   13: putfield 61	android/media/MediaCas:mBinder	Landroid/hardware/cas/V1_0/ICasListener$Stub;
    //   16: aload_0
    //   17: invokestatic 65	android/media/MediaCas:getService	()Landroid/hardware/cas/V1_0/IMediaCasService;
    //   20: iload_1
    //   21: aload_0
    //   22: getfield 61	android/media/MediaCas:mBinder	Landroid/hardware/cas/V1_0/ICasListener$Stub;
    //   25: invokeinterface 71 3 0
    //   30: putfield 73	android/media/MediaCas:mICas	Landroid/hardware/cas/V1_0/ICas;
    //   33: aload_0
    //   34: getfield 73	android/media/MediaCas:mICas	Landroid/hardware/cas/V1_0/ICas;
    //   37: ifnull +6 -> 43
    //   40: goto +84 -> 124
    //   43: new 75	java/lang/StringBuilder
    //   46: dup
    //   47: invokespecial 76	java/lang/StringBuilder:<init>	()V
    //   50: astore_2
    //   51: aload_2
    //   52: ldc 78
    //   54: invokevirtual 82	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   57: pop
    //   58: aload_2
    //   59: iload_1
    //   60: invokevirtual 85	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   63: pop
    //   64: new 53	android/media/MediaCasException$UnsupportedCasException
    //   67: dup
    //   68: aload_2
    //   69: invokevirtual 89	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   72: invokespecial 92	android/media/MediaCasException$UnsupportedCasException:<init>	(Ljava/lang/String;)V
    //   75: athrow
    //   76: astore_2
    //   77: goto +81 -> 158
    //   80: astore_3
    //   81: new 75	java/lang/StringBuilder
    //   84: astore_2
    //   85: aload_2
    //   86: invokespecial 76	java/lang/StringBuilder:<init>	()V
    //   89: aload_2
    //   90: ldc 94
    //   92: invokevirtual 82	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   95: pop
    //   96: aload_2
    //   97: aload_3
    //   98: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: ldc 29
    //   104: aload_2
    //   105: invokevirtual 89	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   108: invokestatic 103	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   111: pop
    //   112: aload_0
    //   113: aconst_null
    //   114: putfield 73	android/media/MediaCas:mICas	Landroid/hardware/cas/V1_0/ICas;
    //   117: aload_0
    //   118: getfield 73	android/media/MediaCas:mICas	Landroid/hardware/cas/V1_0/ICas;
    //   121: ifnull +4 -> 125
    //   124: return
    //   125: new 75	java/lang/StringBuilder
    //   128: dup
    //   129: invokespecial 76	java/lang/StringBuilder:<init>	()V
    //   132: astore_2
    //   133: aload_2
    //   134: ldc 78
    //   136: invokevirtual 82	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: pop
    //   140: aload_2
    //   141: iload_1
    //   142: invokevirtual 85	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   145: pop
    //   146: new 53	android/media/MediaCasException$UnsupportedCasException
    //   149: dup
    //   150: aload_2
    //   151: invokevirtual 89	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   154: invokespecial 92	android/media/MediaCasException$UnsupportedCasException:<init>	(Ljava/lang/String;)V
    //   157: athrow
    //   158: aload_0
    //   159: getfield 73	android/media/MediaCas:mICas	Landroid/hardware/cas/V1_0/ICas;
    //   162: ifnonnull +36 -> 198
    //   165: new 75	java/lang/StringBuilder
    //   168: dup
    //   169: invokespecial 76	java/lang/StringBuilder:<init>	()V
    //   172: astore_2
    //   173: aload_2
    //   174: ldc 78
    //   176: invokevirtual 82	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   179: pop
    //   180: aload_2
    //   181: iload_1
    //   182: invokevirtual 85	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   185: pop
    //   186: new 53	android/media/MediaCasException$UnsupportedCasException
    //   189: dup
    //   190: aload_2
    //   191: invokevirtual 89	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   194: invokespecial 92	android/media/MediaCasException$UnsupportedCasException:<init>	(Ljava/lang/String;)V
    //   197: athrow
    //   198: aload_2
    //   199: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	200	0	this	MediaCas
    //   0	200	1	paramInt	int
    //   50	19	2	localStringBuilder1	StringBuilder
    //   76	1	2	localObject	Object
    //   84	115	2	localStringBuilder2	StringBuilder
    //   80	18	3	localException	Exception
    // Exception table:
    //   from	to	target	type
    //   16	33	76	finally
    //   81	117	76	finally
    //   16	33	80	java/lang/Exception
  }
  
  private void cleanupAndRethrowIllegalState()
  {
    mICas = null;
    throw new IllegalStateException();
  }
  
  public static PluginDescriptor[] enumeratePlugins()
  {
    Object localObject = getService();
    if (localObject != null) {
      try
      {
        localObject = ((IMediaCasService)localObject).enumeratePlugins();
        if (((ArrayList)localObject).size() == 0) {
          return null;
        }
        PluginDescriptor[] arrayOfPluginDescriptor = new PluginDescriptor[((ArrayList)localObject).size()];
        for (int i = 0; i < arrayOfPluginDescriptor.length; i++) {
          arrayOfPluginDescriptor[i] = new PluginDescriptor((HidlCasPluginDescriptor)((ArrayList)localObject).get(i));
        }
        return arrayOfPluginDescriptor;
      }
      catch (RemoteException localRemoteException) {}
    }
    return null;
  }
  
  static IMediaCasService getService()
  {
    return (IMediaCasService)gDefault.get();
  }
  
  public static boolean isSystemIdSupported(int paramInt)
  {
    IMediaCasService localIMediaCasService = getService();
    if (localIMediaCasService != null) {
      try
      {
        boolean bool = localIMediaCasService.isSystemIdSupported(paramInt);
        return bool;
      }
      catch (RemoteException localRemoteException) {}
    }
    return false;
  }
  
  private ArrayList<Byte> toByteArray(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null) {
      return new ArrayList();
    }
    return toByteArray(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  private ArrayList<Byte> toByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    ArrayList localArrayList = new ArrayList(paramInt2);
    for (int i = 0; i < paramInt2; i++) {
      localArrayList.add(Byte.valueOf(paramArrayOfByte[(paramInt1 + i)]));
    }
    return localArrayList;
  }
  
  private byte[] toBytes(ArrayList<Byte> paramArrayList)
  {
    Object localObject = null;
    if (paramArrayList != null)
    {
      byte[] arrayOfByte = new byte[paramArrayList.size()];
      for (int i = 0;; i++)
      {
        localObject = arrayOfByte;
        if (i >= arrayOfByte.length) {
          break;
        }
        arrayOfByte[i] = ((Byte)paramArrayList.get(i)).byteValue();
      }
    }
    return localObject;
  }
  
  private void validateInternalStates()
  {
    if (mICas != null) {
      return;
    }
    throw new IllegalStateException();
  }
  
  /* Error */
  public void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 73	android/media/MediaCas:mICas	Landroid/hardware/cas/V1_0/ICas;
    //   4: ifnull +30 -> 34
    //   7: aload_0
    //   8: getfield 73	android/media/MediaCas:mICas	Landroid/hardware/cas/V1_0/ICas;
    //   11: invokeinterface 196 1 0
    //   16: pop
    //   17: goto +12 -> 29
    //   20: astore_1
    //   21: aload_0
    //   22: aconst_null
    //   23: putfield 73	android/media/MediaCas:mICas	Landroid/hardware/cas/V1_0/ICas;
    //   26: aload_1
    //   27: athrow
    //   28: astore_1
    //   29: aload_0
    //   30: aconst_null
    //   31: putfield 73	android/media/MediaCas:mICas	Landroid/hardware/cas/V1_0/ICas;
    //   34: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	35	0	this	MediaCas
    //   20	7	1	localObject	Object
    //   28	1	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   7	17	20	finally
    //   7	17	28	android/os/RemoteException
  }
  
  Session createFromSessionId(ArrayList<Byte> paramArrayList)
  {
    if ((paramArrayList != null) && (paramArrayList.size() != 0)) {
      return new Session(paramArrayList);
    }
    return null;
  }
  
  protected void finalize()
  {
    close();
  }
  
  IHwBinder getBinder()
  {
    validateInternalStates();
    return mICas.asBinder();
  }
  
  public Session openSession()
    throws MediaCasException
  {
    validateInternalStates();
    try
    {
      Object localObject = new android/media/MediaCas$OpenSessionCallback;
      ((OpenSessionCallback)localObject).<init>(this, null);
      mICas.openSession((ICas.openSessionCallback)localObject);
      MediaCasException.throwExceptionIfNeeded(mStatus);
      localObject = mSession;
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      cleanupAndRethrowIllegalState();
    }
    return null;
  }
  
  public void processEmm(byte[] paramArrayOfByte)
    throws MediaCasException
  {
    processEmm(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public void processEmm(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws MediaCasException
  {
    validateInternalStates();
    try
    {
      MediaCasException.throwExceptionIfNeeded(mICas.processEmm(toByteArray(paramArrayOfByte, paramInt1, paramInt2)));
    }
    catch (RemoteException paramArrayOfByte)
    {
      cleanupAndRethrowIllegalState();
    }
  }
  
  public void provision(String paramString)
    throws MediaCasException
  {
    validateInternalStates();
    try
    {
      MediaCasException.throwExceptionIfNeeded(mICas.provision(paramString));
    }
    catch (RemoteException paramString)
    {
      cleanupAndRethrowIllegalState();
    }
  }
  
  public void refreshEntitlements(int paramInt, byte[] paramArrayOfByte)
    throws MediaCasException
  {
    validateInternalStates();
    try
    {
      MediaCasException.throwExceptionIfNeeded(mICas.refreshEntitlements(paramInt, toByteArray(paramArrayOfByte)));
    }
    catch (RemoteException paramArrayOfByte)
    {
      cleanupAndRethrowIllegalState();
    }
  }
  
  public void sendEvent(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
    throws MediaCasException
  {
    validateInternalStates();
    try
    {
      MediaCasException.throwExceptionIfNeeded(mICas.sendEvent(paramInt1, paramInt2, toByteArray(paramArrayOfByte)));
    }
    catch (RemoteException paramArrayOfByte)
    {
      cleanupAndRethrowIllegalState();
    }
  }
  
  public void setEventListener(EventListener paramEventListener, Handler paramHandler)
  {
    mListener = paramEventListener;
    EventListener localEventListener = mListener;
    paramEventListener = null;
    if (localEventListener == null)
    {
      mEventHandler = null;
      return;
    }
    if (paramHandler != null) {
      paramEventListener = paramHandler.getLooper();
    }
    paramHandler = paramEventListener;
    paramEventListener = paramHandler;
    if (paramHandler == null)
    {
      paramHandler = Looper.myLooper();
      paramEventListener = paramHandler;
      if (paramHandler == null)
      {
        paramHandler = Looper.getMainLooper();
        paramEventListener = paramHandler;
        if (paramHandler == null)
        {
          if ((mHandlerThread == null) || (!mHandlerThread.isAlive()))
          {
            mHandlerThread = new HandlerThread("MediaCasEventThread", -2);
            mHandlerThread.start();
          }
          paramEventListener = mHandlerThread.getLooper();
        }
      }
    }
    mEventHandler = new EventHandler(paramEventListener);
  }
  
  public void setPrivateData(byte[] paramArrayOfByte)
    throws MediaCasException
  {
    validateInternalStates();
    try
    {
      MediaCasException.throwExceptionIfNeeded(mICas.setPrivateData(toByteArray(paramArrayOfByte, 0, paramArrayOfByte.length)));
    }
    catch (RemoteException paramArrayOfByte)
    {
      cleanupAndRethrowIllegalState();
    }
  }
  
  private class EventHandler
    extends Handler
  {
    private static final int MSG_CAS_EVENT = 0;
    
    public EventHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (what == 0) {
        mListener.onEvent(MediaCas.this, arg1, arg2, MediaCas.this.toBytes((ArrayList)obj));
      }
    }
  }
  
  public static abstract interface EventListener
  {
    public abstract void onEvent(MediaCas paramMediaCas, int paramInt1, int paramInt2, byte[] paramArrayOfByte);
  }
  
  private class OpenSessionCallback
    implements ICas.openSessionCallback
  {
    public MediaCas.Session mSession;
    public int mStatus;
    
    private OpenSessionCallback() {}
    
    public void onValues(int paramInt, ArrayList<Byte> paramArrayList)
    {
      mStatus = paramInt;
      mSession = createFromSessionId(paramArrayList);
    }
  }
  
  public static class PluginDescriptor
  {
    private final int mCASystemId;
    private final String mName;
    
    private PluginDescriptor()
    {
      mCASystemId = 65535;
      mName = null;
    }
    
    PluginDescriptor(HidlCasPluginDescriptor paramHidlCasPluginDescriptor)
    {
      mCASystemId = caSystemId;
      mName = name;
    }
    
    public String getName()
    {
      return mName;
    }
    
    public int getSystemId()
    {
      return mCASystemId;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PluginDescriptor {");
      localStringBuilder.append(mCASystemId);
      localStringBuilder.append(", ");
      localStringBuilder.append(mName);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  public final class Session
    implements AutoCloseable
  {
    final ArrayList<Byte> mSessionId;
    
    Session()
    {
      Object localObject;
      mSessionId = localObject;
    }
    
    public void close()
    {
      MediaCas.this.validateInternalStates();
      try
      {
        MediaCasStateException.throwExceptionIfNeeded(mICas.closeSession(mSessionId));
      }
      catch (RemoteException localRemoteException)
      {
        MediaCas.this.cleanupAndRethrowIllegalState();
      }
    }
    
    public void processEcm(byte[] paramArrayOfByte)
      throws MediaCasException
    {
      processEcm(paramArrayOfByte, 0, paramArrayOfByte.length);
    }
    
    public void processEcm(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws MediaCasException
    {
      MediaCas.this.validateInternalStates();
      try
      {
        MediaCasException.throwExceptionIfNeeded(mICas.processEcm(mSessionId, MediaCas.this.toByteArray(paramArrayOfByte, paramInt1, paramInt2)));
      }
      catch (RemoteException paramArrayOfByte)
      {
        MediaCas.this.cleanupAndRethrowIllegalState();
      }
    }
    
    public void setPrivateData(byte[] paramArrayOfByte)
      throws MediaCasException
    {
      MediaCas.this.validateInternalStates();
      try
      {
        MediaCasException.throwExceptionIfNeeded(mICas.setSessionPrivateData(mSessionId, MediaCas.this.toByteArray(paramArrayOfByte, 0, paramArrayOfByte.length)));
      }
      catch (RemoteException paramArrayOfByte)
      {
        MediaCas.this.cleanupAndRethrowIllegalState();
      }
    }
  }
}
