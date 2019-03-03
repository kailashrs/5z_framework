package android.net.lowpan;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class LowpanManager
{
  private static final String TAG = LowpanManager.class.getSimpleName();
  private final Map<IBinder, WeakReference<LowpanInterface>> mBinderCache = new WeakHashMap();
  private final Context mContext;
  private final Map<String, LowpanInterface> mInterfaceCache = new HashMap();
  private final Map<Integer, ILowpanManagerListener> mListenerMap = new HashMap();
  private final Looper mLooper;
  private final ILowpanManager mService;
  
  public LowpanManager(Context paramContext, ILowpanManager paramILowpanManager, Looper paramLooper)
  {
    mContext = paramContext;
    mService = paramILowpanManager;
    mLooper = paramLooper;
  }
  
  LowpanManager(ILowpanManager paramILowpanManager)
  {
    mService = paramILowpanManager;
    mContext = null;
    mLooper = null;
  }
  
  public static LowpanManager from(Context paramContext)
  {
    return (LowpanManager)paramContext.getSystemService("lowpan");
  }
  
  public static LowpanManager getManager()
  {
    IBinder localIBinder = ServiceManager.getService("lowpan");
    if (localIBinder != null) {
      return new LowpanManager(ILowpanManager.Stub.asInterface(localIBinder));
    }
    return null;
  }
  
  public LowpanInterface getInterface()
  {
    String[] arrayOfString = getInterfaceList();
    if (arrayOfString.length > 0) {
      return getInterface(arrayOfString[0]);
    }
    return null;
  }
  
  /* Error */
  public LowpanInterface getInterface(ILowpanInterface paramILowpanInterface)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: getfield 53	android/net/lowpan/LowpanManager:mBinderCache	Ljava/util/Map;
    //   6: astore_3
    //   7: aload_3
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield 53	android/net/lowpan/LowpanManager:mBinderCache	Ljava/util/Map;
    //   13: aload_1
    //   14: invokeinterface 108 1 0
    //   19: invokeinterface 114 2 0
    //   24: ifeq +28 -> 52
    //   27: aload_0
    //   28: getfield 53	android/net/lowpan/LowpanManager:mBinderCache	Ljava/util/Map;
    //   31: aload_1
    //   32: invokeinterface 108 1 0
    //   37: invokeinterface 118 2 0
    //   42: checkcast 120	java/lang/ref/WeakReference
    //   45: invokevirtual 123	java/lang/ref/WeakReference:get	()Ljava/lang/Object;
    //   48: checkcast 125	android/net/lowpan/LowpanInterface
    //   51: astore_2
    //   52: aload_2
    //   53: astore 4
    //   55: aload_2
    //   56: ifnonnull +136 -> 192
    //   59: aload_1
    //   60: invokeinterface 128 1 0
    //   65: astore 4
    //   67: new 125	android/net/lowpan/LowpanInterface
    //   70: astore_2
    //   71: aload_2
    //   72: aload_0
    //   73: getfield 55	android/net/lowpan/LowpanManager:mContext	Landroid/content/Context;
    //   76: aload_1
    //   77: aload_0
    //   78: getfield 59	android/net/lowpan/LowpanManager:mLooper	Landroid/os/Looper;
    //   81: invokespecial 131	android/net/lowpan/LowpanInterface:<init>	(Landroid/content/Context;Landroid/net/lowpan/ILowpanInterface;Landroid/os/Looper;)V
    //   84: aload_0
    //   85: getfield 48	android/net/lowpan/LowpanManager:mInterfaceCache	Ljava/util/Map;
    //   88: astore 5
    //   90: aload 5
    //   92: monitorenter
    //   93: aload_0
    //   94: getfield 48	android/net/lowpan/LowpanManager:mInterfaceCache	Ljava/util/Map;
    //   97: aload_2
    //   98: invokevirtual 132	android/net/lowpan/LowpanInterface:getName	()Ljava/lang/String;
    //   101: aload_2
    //   102: invokeinterface 136 3 0
    //   107: pop
    //   108: aload 5
    //   110: monitorexit
    //   111: aload_0
    //   112: getfield 53	android/net/lowpan/LowpanManager:mBinderCache	Ljava/util/Map;
    //   115: astore 6
    //   117: aload_1
    //   118: invokeinterface 108 1 0
    //   123: astore 7
    //   125: new 120	java/lang/ref/WeakReference
    //   128: astore 5
    //   130: aload 5
    //   132: aload_2
    //   133: invokespecial 139	java/lang/ref/WeakReference:<init>	(Ljava/lang/Object;)V
    //   136: aload 6
    //   138: aload 7
    //   140: aload 5
    //   142: invokeinterface 136 3 0
    //   147: pop
    //   148: aload_1
    //   149: invokeinterface 108 1 0
    //   154: astore 5
    //   156: new 6	android/net/lowpan/LowpanManager$1
    //   159: astore 7
    //   161: aload 7
    //   163: aload_0
    //   164: aload 4
    //   166: aload_1
    //   167: invokespecial 142	android/net/lowpan/LowpanManager$1:<init>	(Landroid/net/lowpan/LowpanManager;Ljava/lang/String;Landroid/net/lowpan/ILowpanInterface;)V
    //   170: aload 5
    //   172: aload 7
    //   174: iconst_0
    //   175: invokeinterface 148 3 0
    //   180: aload_2
    //   181: astore 4
    //   183: goto +9 -> 192
    //   186: astore_1
    //   187: aload 5
    //   189: monitorexit
    //   190: aload_1
    //   191: athrow
    //   192: aload_3
    //   193: monitorexit
    //   194: aload 4
    //   196: areturn
    //   197: astore_1
    //   198: aload_3
    //   199: monitorexit
    //   200: aload_1
    //   201: athrow
    //   202: astore_1
    //   203: aload_1
    //   204: invokevirtual 152	android/os/RemoteException:rethrowAsRuntimeException	()Ljava/lang/RuntimeException;
    //   207: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	208	0	this	LowpanManager
    //   0	208	1	paramILowpanInterface	ILowpanInterface
    //   1	180	2	localLowpanInterface	LowpanInterface
    //   53	142	4	localObject1	Object
    //   115	22	6	localMap2	Map
    //   123	50	7	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   93	111	186	finally
    //   187	190	186	finally
    //   9	52	197	finally
    //   59	84	197	finally
    //   84	93	197	finally
    //   111	148	197	finally
    //   148	180	197	finally
    //   190	192	197	finally
    //   192	194	197	finally
    //   198	200	197	finally
    //   2	9	202	android/os/RemoteException
    //   200	202	202	android/os/RemoteException
  }
  
  /* Error */
  public LowpanInterface getInterface(String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: getfield 48	android/net/lowpan/LowpanManager:mInterfaceCache	Ljava/util/Map;
    //   6: astore_3
    //   7: aload_3
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield 48	android/net/lowpan/LowpanManager:mInterfaceCache	Ljava/util/Map;
    //   13: aload_1
    //   14: invokeinterface 114 2 0
    //   19: ifeq +20 -> 39
    //   22: aload_0
    //   23: getfield 48	android/net/lowpan/LowpanManager:mInterfaceCache	Ljava/util/Map;
    //   26: aload_1
    //   27: invokeinterface 118 2 0
    //   32: checkcast 125	android/net/lowpan/LowpanInterface
    //   35: astore_1
    //   36: goto +29 -> 65
    //   39: aload_0
    //   40: getfield 57	android/net/lowpan/LowpanManager:mService	Landroid/net/lowpan/ILowpanManager;
    //   43: aload_1
    //   44: invokeinterface 157 2 0
    //   49: astore 4
    //   51: aload_2
    //   52: astore_1
    //   53: aload 4
    //   55: ifnull +10 -> 65
    //   58: aload_0
    //   59: aload 4
    //   61: invokevirtual 159	android/net/lowpan/LowpanManager:getInterface	(Landroid/net/lowpan/ILowpanInterface;)Landroid/net/lowpan/LowpanInterface;
    //   64: astore_1
    //   65: aload_3
    //   66: monitorexit
    //   67: aload_1
    //   68: areturn
    //   69: astore_1
    //   70: aload_3
    //   71: monitorexit
    //   72: aload_1
    //   73: athrow
    //   74: astore_1
    //   75: aload_1
    //   76: invokevirtual 162	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   79: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	80	0	this	LowpanManager
    //   0	80	1	paramString	String
    //   1	51	2	localObject	Object
    //   49	11	4	localILowpanInterface	ILowpanInterface
    // Exception table:
    //   from	to	target	type
    //   9	36	69	finally
    //   39	51	69	finally
    //   58	65	69	finally
    //   65	67	69	finally
    //   70	72	69	finally
    //   2	9	74	android/os/RemoteException
    //   72	74	74	android/os/RemoteException
  }
  
  public String[] getInterfaceList()
  {
    try
    {
      String[] arrayOfString = mService.getInterfaceList();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public LowpanInterface getInterfaceNoCreate(ILowpanInterface paramILowpanInterface)
  {
    LowpanInterface localLowpanInterface = null;
    synchronized (mBinderCache)
    {
      if (mBinderCache.containsKey(paramILowpanInterface.asBinder())) {
        localLowpanInterface = (LowpanInterface)((WeakReference)mBinderCache.get(paramILowpanInterface.asBinder())).get();
      }
      return localLowpanInterface;
    }
  }
  
  public void registerCallback(Callback paramCallback)
    throws LowpanException
  {
    registerCallback(paramCallback, null);
  }
  
  /* Error */
  public void registerCallback(final Callback paramCallback, Handler arg2)
    throws LowpanException
  {
    // Byte code:
    //   0: new 8	android/net/lowpan/LowpanManager$2
    //   3: dup
    //   4: aload_0
    //   5: aload_2
    //   6: aload_1
    //   7: invokespecial 175	android/net/lowpan/LowpanManager$2:<init>	(Landroid/net/lowpan/LowpanManager;Landroid/os/Handler;Landroid/net/lowpan/LowpanManager$Callback;)V
    //   10: astore_3
    //   11: aload_0
    //   12: getfield 57	android/net/lowpan/LowpanManager:mService	Landroid/net/lowpan/ILowpanManager;
    //   15: aload_3
    //   16: invokeinterface 179 2 0
    //   21: aload_0
    //   22: getfield 46	android/net/lowpan/LowpanManager:mListenerMap	Ljava/util/Map;
    //   25: astore_2
    //   26: aload_2
    //   27: monitorenter
    //   28: aload_0
    //   29: getfield 46	android/net/lowpan/LowpanManager:mListenerMap	Ljava/util/Map;
    //   32: aload_1
    //   33: invokestatic 185	java/lang/System:identityHashCode	(Ljava/lang/Object;)I
    //   36: invokestatic 191	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   39: aload_3
    //   40: invokeinterface 136 3 0
    //   45: pop
    //   46: aload_2
    //   47: monitorexit
    //   48: return
    //   49: astore_1
    //   50: aload_2
    //   51: monitorexit
    //   52: aload_1
    //   53: athrow
    //   54: astore_1
    //   55: aload_1
    //   56: invokevirtual 162	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   59: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	60	0	this	LowpanManager
    //   0	60	1	paramCallback	Callback
    //   10	30	3	local2	2
    // Exception table:
    //   from	to	target	type
    //   28	48	49	finally
    //   50	52	49	finally
    //   11	21	54	android/os/RemoteException
  }
  
  public void unregisterCallback(Callback arg1)
  {
    Integer localInteger = Integer.valueOf(System.identityHashCode(???));
    synchronized (mListenerMap)
    {
      ILowpanManagerListener localILowpanManagerListener = (ILowpanManagerListener)mListenerMap.get(localInteger);
      mListenerMap.remove(localInteger);
      if (localILowpanManagerListener != null) {
        try
        {
          mService.removeListener(localILowpanManagerListener);
          return;
        }
        catch (RemoteException ???)
        {
          throw ???.rethrowFromSystemServer();
        }
      }
      throw new RuntimeException("Attempt to unregister an unknown callback");
    }
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public void onInterfaceAdded(LowpanInterface paramLowpanInterface) {}
    
    public void onInterfaceRemoved(LowpanInterface paramLowpanInterface) {}
  }
}
