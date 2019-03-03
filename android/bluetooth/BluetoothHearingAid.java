package android.bluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public final class BluetoothHearingAid
  implements BluetoothProfile
{
  public static final String ACTION_ACTIVE_DEVICE_CHANGED = "android.bluetooth.hearingaid.profile.action.ACTIVE_DEVICE_CHANGED";
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED";
  public static final String ACTION_PLAYING_STATE_CHANGED = "android.bluetooth.hearingaid.profile.action.PLAYING_STATE_CHANGED";
  private static final boolean DBG = false;
  public static final long HI_SYNC_ID_INVALID = 0L;
  public static final int MODE_BINAURAL = 1;
  public static final int MODE_MONAURAL = 0;
  public static final int SIDE_LEFT = 0;
  public static final int SIDE_RIGHT = 1;
  public static final int STATE_NOT_PLAYING = 11;
  public static final int STATE_PLAYING = 10;
  private static final String TAG = "BluetoothHearingAid";
  private static final boolean VDBG = false;
  private BluetoothAdapter mAdapter;
  private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    /* Error */
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      // Byte code:
      //   0: iload_1
      //   1: ifne +100 -> 101
      //   4: aload_0
      //   5: getfield 12	android/bluetooth/BluetoothHearingAid$1:this$0	Landroid/bluetooth/BluetoothHearingAid;
      //   8: invokestatic 24	android/bluetooth/BluetoothHearingAid:access$000	(Landroid/bluetooth/BluetoothHearingAid;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   11: invokevirtual 30	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
      //   14: invokevirtual 35	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
      //   17: aload_0
      //   18: getfield 12	android/bluetooth/BluetoothHearingAid$1:this$0	Landroid/bluetooth/BluetoothHearingAid;
      //   21: invokestatic 39	android/bluetooth/BluetoothHearingAid:access$100	(Landroid/bluetooth/BluetoothHearingAid;)Landroid/bluetooth/IBluetoothHearingAid;
      //   24: ifnull +46 -> 70
      //   27: aload_0
      //   28: getfield 12	android/bluetooth/BluetoothHearingAid$1:this$0	Landroid/bluetooth/BluetoothHearingAid;
      //   31: aconst_null
      //   32: invokestatic 43	android/bluetooth/BluetoothHearingAid:access$102	(Landroid/bluetooth/BluetoothHearingAid;Landroid/bluetooth/IBluetoothHearingAid;)Landroid/bluetooth/IBluetoothHearingAid;
      //   35: pop
      //   36: aload_0
      //   37: getfield 12	android/bluetooth/BluetoothHearingAid$1:this$0	Landroid/bluetooth/BluetoothHearingAid;
      //   40: invokestatic 47	android/bluetooth/BluetoothHearingAid:access$300	(Landroid/bluetooth/BluetoothHearingAid;)Landroid/content/Context;
      //   43: aload_0
      //   44: getfield 12	android/bluetooth/BluetoothHearingAid$1:this$0	Landroid/bluetooth/BluetoothHearingAid;
      //   47: invokestatic 51	android/bluetooth/BluetoothHearingAid:access$200	(Landroid/bluetooth/BluetoothHearingAid;)Landroid/content/ServiceConnection;
      //   50: invokevirtual 57	android/content/Context:unbindService	(Landroid/content/ServiceConnection;)V
      //   53: goto +17 -> 70
      //   56: astore_2
      //   57: goto +29 -> 86
      //   60: astore_2
      //   61: ldc 59
      //   63: ldc 61
      //   65: aload_2
      //   66: invokestatic 67	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   69: pop
      //   70: aload_0
      //   71: getfield 12	android/bluetooth/BluetoothHearingAid$1:this$0	Landroid/bluetooth/BluetoothHearingAid;
      //   74: invokestatic 24	android/bluetooth/BluetoothHearingAid:access$000	(Landroid/bluetooth/BluetoothHearingAid;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   77: invokevirtual 30	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
      //   80: invokevirtual 70	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
      //   83: goto +78 -> 161
      //   86: aload_0
      //   87: getfield 12	android/bluetooth/BluetoothHearingAid$1:this$0	Landroid/bluetooth/BluetoothHearingAid;
      //   90: invokestatic 24	android/bluetooth/BluetoothHearingAid:access$000	(Landroid/bluetooth/BluetoothHearingAid;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   93: invokevirtual 30	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
      //   96: invokevirtual 70	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
      //   99: aload_2
      //   100: athrow
      //   101: aload_0
      //   102: getfield 12	android/bluetooth/BluetoothHearingAid$1:this$0	Landroid/bluetooth/BluetoothHearingAid;
      //   105: invokestatic 24	android/bluetooth/BluetoothHearingAid:access$000	(Landroid/bluetooth/BluetoothHearingAid;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   108: invokevirtual 74	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
      //   111: invokevirtual 77	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
      //   114: aload_0
      //   115: getfield 12	android/bluetooth/BluetoothHearingAid$1:this$0	Landroid/bluetooth/BluetoothHearingAid;
      //   118: invokestatic 39	android/bluetooth/BluetoothHearingAid:access$100	(Landroid/bluetooth/BluetoothHearingAid;)Landroid/bluetooth/IBluetoothHearingAid;
      //   121: ifnonnull +27 -> 148
      //   124: aload_0
      //   125: getfield 12	android/bluetooth/BluetoothHearingAid$1:this$0	Landroid/bluetooth/BluetoothHearingAid;
      //   128: invokevirtual 80	android/bluetooth/BluetoothHearingAid:doBind	()V
      //   131: goto +17 -> 148
      //   134: astore_2
      //   135: goto +27 -> 162
      //   138: astore_2
      //   139: ldc 59
      //   141: ldc 61
      //   143: aload_2
      //   144: invokestatic 67	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   147: pop
      //   148: aload_0
      //   149: getfield 12	android/bluetooth/BluetoothHearingAid$1:this$0	Landroid/bluetooth/BluetoothHearingAid;
      //   152: invokestatic 24	android/bluetooth/BluetoothHearingAid:access$000	(Landroid/bluetooth/BluetoothHearingAid;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   155: invokevirtual 74	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
      //   158: invokevirtual 81	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
      //   161: return
      //   162: aload_0
      //   163: getfield 12	android/bluetooth/BluetoothHearingAid$1:this$0	Landroid/bluetooth/BluetoothHearingAid;
      //   166: invokestatic 24	android/bluetooth/BluetoothHearingAid:access$000	(Landroid/bluetooth/BluetoothHearingAid;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   169: invokevirtual 74	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
      //   172: invokevirtual 81	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
      //   175: aload_2
      //   176: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	177	0	this	1
      //   0	177	1	paramAnonymousBoolean	boolean
      //   56	1	2	localObject1	Object
      //   60	40	2	localException1	Exception
      //   134	1	2	localObject2	Object
      //   138	38	2	localException2	Exception
      // Exception table:
      //   from	to	target	type
      //   4	53	56	finally
      //   61	70	56	finally
      //   4	53	60	java/lang/Exception
      //   101	131	134	finally
      //   139	148	134	finally
      //   101	131	138	java/lang/Exception
    }
  };
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      try
      {
        mServiceLock.writeLock().lock();
        BluetoothHearingAid.access$102(BluetoothHearingAid.this, IBluetoothHearingAid.Stub.asInterface(Binder.allowBlocking(paramAnonymousIBinder)));
        mServiceLock.writeLock().unlock();
        if (mServiceListener != null) {
          mServiceListener.onServiceConnected(21, BluetoothHearingAid.this);
        }
        return;
      }
      finally
      {
        mServiceLock.writeLock().unlock();
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      try
      {
        mServiceLock.writeLock().lock();
        BluetoothHearingAid.access$102(BluetoothHearingAid.this, null);
        mServiceLock.writeLock().unlock();
        if (mServiceListener != null) {
          mServiceListener.onServiceDisconnected(21);
        }
        return;
      }
      finally
      {
        mServiceLock.writeLock().unlock();
      }
    }
  };
  private Context mContext;
  @GuardedBy("mServiceLock")
  private IBluetoothHearingAid mService;
  private BluetoothProfile.ServiceListener mServiceListener;
  private final ReentrantReadWriteLock mServiceLock = new ReentrantReadWriteLock();
  
  BluetoothHearingAid(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
  {
    mContext = paramContext;
    mServiceListener = paramServiceListener;
    mAdapter = BluetoothAdapter.getDefaultAdapter();
    paramContext = mAdapter.getBluetoothManager();
    if (paramContext != null) {
      try
      {
        paramContext.registerStateChangeCallback(mBluetoothStateChangeCallback);
      }
      catch (RemoteException paramContext)
      {
        Log.e("BluetoothHearingAid", "", paramContext);
      }
    }
    doBind();
  }
  
  private boolean isEnabled()
  {
    return mAdapter.getState() == 12;
  }
  
  private boolean isValidDevice(BluetoothDevice paramBluetoothDevice)
  {
    if (paramBluetoothDevice == null) {
      return false;
    }
    return BluetoothAdapter.checkBluetoothAddress(paramBluetoothDevice.getAddress());
  }
  
  private static void log(String paramString)
  {
    Log.d("BluetoothHearingAid", paramString);
  }
  
  public static String stateToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("<unknown state ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(">");
        return localStringBuilder.toString();
      case 11: 
        return "not playing";
      }
      return "playing";
    case 3: 
      return "disconnecting";
    case 2: 
      return "connected";
    case 1: 
      return "connecting";
    }
    return "disconnected";
  }
  
  /* Error */
  public void adjustVolume(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnonnull +22 -> 36
    //   17: ldc 40
    //   19: ldc -63
    //   21: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   24: pop
    //   25: aload_0
    //   26: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   29: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   32: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   35: return
    //   36: aload_0
    //   37: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   40: istore_2
    //   41: iload_2
    //   42: ifne +14 -> 56
    //   45: aload_0
    //   46: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   49: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   52: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   55: return
    //   56: aload_0
    //   57: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   60: iload_1
    //   61: invokeinterface 205 2 0
    //   66: aload_0
    //   67: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   70: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   73: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   76: goto +56 -> 132
    //   79: astore_3
    //   80: goto +53 -> 133
    //   83: astore_3
    //   84: new 153	java/lang/StringBuilder
    //   87: astore_3
    //   88: aload_3
    //   89: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   92: aload_3
    //   93: ldc -49
    //   95: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: new 209	java/lang/Throwable
    //   102: astore 4
    //   104: aload 4
    //   106: invokespecial 210	java/lang/Throwable:<init>	()V
    //   109: aload_3
    //   110: aload 4
    //   112: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 40
    //   121: aload_3
    //   122: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: goto -63 -> 66
    //   132: return
    //   133: aload_0
    //   134: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   137: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   140: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   143: aload_3
    //   144: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	145	0	this	BluetoothHearingAid
    //   0	145	1	paramInt	int
    //   40	2	2	bool	boolean
    //   79	1	3	localObject	Object
    //   83	1	3	localRemoteException	RemoteException
    //   87	57	3	localStringBuilder	StringBuilder
    //   102	9	4	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   0	25	79	finally
    //   36	41	79	finally
    //   56	66	79	finally
    //   84	129	79	finally
    //   0	25	83	android/os/RemoteException
    //   36	41	83	android/os/RemoteException
    //   56	66	83	android/os/RemoteException
  }
  
  /* Error */
  void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: aconst_null
    //   2: putfield 81	android/bluetooth/BluetoothHearingAid:mServiceListener	Landroid/bluetooth/BluetoothProfile$ServiceListener;
    //   5: aload_0
    //   6: getfield 89	android/bluetooth/BluetoothHearingAid:mAdapter	Landroid/bluetooth/BluetoothAdapter;
    //   9: invokevirtual 93	android/bluetooth/BluetoothAdapter:getBluetoothManager	()Landroid/bluetooth/IBluetoothManager;
    //   12: astore_1
    //   13: aload_1
    //   14: ifnull +26 -> 40
    //   17: aload_1
    //   18: aload_0
    //   19: getfield 74	android/bluetooth/BluetoothHearingAid:mBluetoothStateChangeCallback	Landroid/bluetooth/IBluetoothStateChangeCallback;
    //   22: invokeinterface 222 2 0
    //   27: goto +13 -> 40
    //   30: astore_1
    //   31: ldc 40
    //   33: ldc 101
    //   35: aload_1
    //   36: invokestatic 107	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   39: pop
    //   40: aload_0
    //   41: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   44: invokevirtual 226	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   47: invokevirtual 229	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
    //   50: aload_0
    //   51: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   54: ifnull +36 -> 90
    //   57: aload_0
    //   58: aconst_null
    //   59: putfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   62: aload_0
    //   63: getfield 79	android/bluetooth/BluetoothHearingAid:mContext	Landroid/content/Context;
    //   66: aload_0
    //   67: getfield 77	android/bluetooth/BluetoothHearingAid:mConnection	Landroid/content/ServiceConnection;
    //   70: invokevirtual 235	android/content/Context:unbindService	(Landroid/content/ServiceConnection;)V
    //   73: goto +17 -> 90
    //   76: astore_1
    //   77: goto +24 -> 101
    //   80: astore_1
    //   81: ldc 40
    //   83: ldc 101
    //   85: aload_1
    //   86: invokestatic 107	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   89: pop
    //   90: aload_0
    //   91: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   94: invokevirtual 226	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   97: invokevirtual 236	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   100: return
    //   101: aload_0
    //   102: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   105: invokevirtual 226	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   108: invokevirtual 236	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   111: aload_1
    //   112: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	113	0	this	BluetoothHearingAid
    //   12	6	1	localIBluetoothManager	IBluetoothManager
    //   30	6	1	localException1	Exception
    //   76	1	1	localObject	Object
    //   80	32	1	localException2	Exception
    // Exception table:
    //   from	to	target	type
    //   17	27	30	java/lang/Exception
    //   40	73	76	finally
    //   81	90	76	finally
    //   40	73	80	java/lang/Exception
  }
  
  /* Error */
  public boolean connect(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnull +41 -> 55
    //   17: aload_0
    //   18: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   21: ifeq +34 -> 55
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 239	android/bluetooth/BluetoothHearingAid:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   29: ifeq +26 -> 55
    //   32: aload_0
    //   33: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   36: aload_1
    //   37: invokeinterface 241 2 0
    //   42: istore_2
    //   43: aload_0
    //   44: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   47: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   50: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   53: iload_2
    //   54: ireturn
    //   55: aload_0
    //   56: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   59: ifnonnull +11 -> 70
    //   62: ldc 40
    //   64: ldc -63
    //   66: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_0
    //   81: ireturn
    //   82: astore_1
    //   83: goto +58 -> 141
    //   86: astore_1
    //   87: new 153	java/lang/StringBuilder
    //   90: astore_1
    //   91: aload_1
    //   92: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   95: aload_1
    //   96: ldc -49
    //   98: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: new 209	java/lang/Throwable
    //   105: astore_3
    //   106: aload_3
    //   107: invokespecial 210	java/lang/Throwable:<init>	()V
    //   110: aload_1
    //   111: aload_3
    //   112: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 40
    //   121: aload_1
    //   122: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: aload_0
    //   130: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   133: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   136: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   139: iconst_0
    //   140: ireturn
    //   141: aload_0
    //   142: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   145: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   148: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   151: aload_1
    //   152: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	this	BluetoothHearingAid
    //   0	153	1	paramBluetoothDevice	BluetoothDevice
    //   42	12	2	bool	boolean
    //   105	7	3	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   0	43	82	finally
    //   55	70	82	finally
    //   87	129	82	finally
    //   0	43	86	android/os/RemoteException
    //   55	70	86	android/os/RemoteException
  }
  
  /* Error */
  public boolean disconnect(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnull +41 -> 55
    //   17: aload_0
    //   18: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   21: ifeq +34 -> 55
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 239	android/bluetooth/BluetoothHearingAid:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   29: ifeq +26 -> 55
    //   32: aload_0
    //   33: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   36: aload_1
    //   37: invokeinterface 244 2 0
    //   42: istore_2
    //   43: aload_0
    //   44: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   47: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   50: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   53: iload_2
    //   54: ireturn
    //   55: aload_0
    //   56: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   59: ifnonnull +11 -> 70
    //   62: ldc 40
    //   64: ldc -63
    //   66: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_0
    //   81: ireturn
    //   82: astore_1
    //   83: goto +58 -> 141
    //   86: astore_1
    //   87: new 153	java/lang/StringBuilder
    //   90: astore_3
    //   91: aload_3
    //   92: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   95: aload_3
    //   96: ldc -49
    //   98: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: new 209	java/lang/Throwable
    //   105: astore_1
    //   106: aload_1
    //   107: invokespecial 210	java/lang/Throwable:<init>	()V
    //   110: aload_3
    //   111: aload_1
    //   112: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 40
    //   121: aload_3
    //   122: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: aload_0
    //   130: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   133: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   136: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   139: iconst_0
    //   140: ireturn
    //   141: aload_0
    //   142: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   145: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   148: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   151: aload_1
    //   152: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	this	BluetoothHearingAid
    //   0	153	1	paramBluetoothDevice	BluetoothDevice
    //   42	12	2	bool	boolean
    //   90	32	3	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   0	43	82	finally
    //   55	70	82	finally
    //   87	129	82	finally
    //   0	43	86	android/os/RemoteException
    //   55	70	86	android/os/RemoteException
  }
  
  void doBind()
  {
    Intent localIntent = new Intent(IBluetoothHearingAid.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, Process.myUserHandle()))) {
      return;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth Hearing Aid Service with ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothHearingAid", ((StringBuilder)localObject).toString());
  }
  
  public void finalize() {}
  
  /* Error */
  public java.util.List<BluetoothDevice> getActiveDevices()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnull +32 -> 46
    //   17: aload_0
    //   18: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   21: ifeq +25 -> 46
    //   24: aload_0
    //   25: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   28: invokeinterface 285 1 0
    //   33: astore_1
    //   34: aload_0
    //   35: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   38: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   41: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   44: aload_1
    //   45: areturn
    //   46: aload_0
    //   47: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   50: ifnonnull +11 -> 61
    //   53: ldc 40
    //   55: ldc -63
    //   57: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   60: pop
    //   61: new 287	java/util/ArrayList
    //   64: dup
    //   65: invokespecial 288	java/util/ArrayList:<init>	()V
    //   68: astore_1
    //   69: aload_0
    //   70: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   73: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   76: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   79: aload_1
    //   80: areturn
    //   81: astore_1
    //   82: goto +66 -> 148
    //   85: astore_1
    //   86: new 153	java/lang/StringBuilder
    //   89: astore_1
    //   90: aload_1
    //   91: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   94: aload_1
    //   95: ldc -49
    //   97: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: new 209	java/lang/Throwable
    //   104: astore_2
    //   105: aload_2
    //   106: invokespecial 210	java/lang/Throwable:<init>	()V
    //   109: aload_1
    //   110: aload_2
    //   111: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   114: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: pop
    //   118: ldc 40
    //   120: aload_1
    //   121: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   124: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   127: pop
    //   128: new 287	java/util/ArrayList
    //   131: dup
    //   132: invokespecial 288	java/util/ArrayList:<init>	()V
    //   135: astore_1
    //   136: aload_0
    //   137: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   140: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   143: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   146: aload_1
    //   147: areturn
    //   148: aload_0
    //   149: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   152: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   155: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   158: aload_1
    //   159: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	160	0	this	BluetoothHearingAid
    //   33	47	1	localObject1	Object
    //   81	1	1	localObject2	Object
    //   85	1	1	localRemoteException	RemoteException
    //   89	70	1	localObject3	Object
    //   104	7	2	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   0	34	81	finally
    //   46	61	81	finally
    //   61	69	81	finally
    //   86	136	81	finally
    //   0	34	85	android/os/RemoteException
    //   46	61	85	android/os/RemoteException
    //   61	69	85	android/os/RemoteException
  }
  
  /* Error */
  public java.util.List<BluetoothDevice> getConnectedDevices()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnull +32 -> 46
    //   17: aload_0
    //   18: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   21: ifeq +25 -> 46
    //   24: aload_0
    //   25: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   28: invokeinterface 293 1 0
    //   33: astore_1
    //   34: aload_0
    //   35: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   38: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   41: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   44: aload_1
    //   45: areturn
    //   46: aload_0
    //   47: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   50: ifnonnull +11 -> 61
    //   53: ldc 40
    //   55: ldc -63
    //   57: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   60: pop
    //   61: new 287	java/util/ArrayList
    //   64: dup
    //   65: invokespecial 288	java/util/ArrayList:<init>	()V
    //   68: astore_1
    //   69: aload_0
    //   70: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   73: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   76: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   79: aload_1
    //   80: areturn
    //   81: astore_1
    //   82: goto +66 -> 148
    //   85: astore_1
    //   86: new 153	java/lang/StringBuilder
    //   89: astore_2
    //   90: aload_2
    //   91: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   94: aload_2
    //   95: ldc -49
    //   97: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: new 209	java/lang/Throwable
    //   104: astore_1
    //   105: aload_1
    //   106: invokespecial 210	java/lang/Throwable:<init>	()V
    //   109: aload_2
    //   110: aload_1
    //   111: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   114: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: pop
    //   118: ldc 40
    //   120: aload_2
    //   121: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   124: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   127: pop
    //   128: new 287	java/util/ArrayList
    //   131: dup
    //   132: invokespecial 288	java/util/ArrayList:<init>	()V
    //   135: astore_1
    //   136: aload_0
    //   137: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   140: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   143: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   146: aload_1
    //   147: areturn
    //   148: aload_0
    //   149: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   152: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   155: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   158: aload_1
    //   159: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	160	0	this	BluetoothHearingAid
    //   33	47	1	localObject1	Object
    //   81	1	1	localObject2	Object
    //   85	1	1	localRemoteException	RemoteException
    //   104	55	1	localObject3	Object
    //   89	32	2	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   0	34	81	finally
    //   46	61	81	finally
    //   61	69	81	finally
    //   86	136	81	finally
    //   0	34	85	android/os/RemoteException
    //   46	61	85	android/os/RemoteException
    //   61	69	85	android/os/RemoteException
  }
  
  /* Error */
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnull +41 -> 55
    //   17: aload_0
    //   18: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   21: ifeq +34 -> 55
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 239	android/bluetooth/BluetoothHearingAid:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   29: ifeq +26 -> 55
    //   32: aload_0
    //   33: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   36: aload_1
    //   37: invokeinterface 297 2 0
    //   42: istore_2
    //   43: aload_0
    //   44: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   47: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   50: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   53: iload_2
    //   54: ireturn
    //   55: aload_0
    //   56: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   59: ifnonnull +11 -> 70
    //   62: ldc 40
    //   64: ldc -63
    //   66: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_0
    //   81: ireturn
    //   82: astore_1
    //   83: goto +58 -> 141
    //   86: astore_1
    //   87: new 153	java/lang/StringBuilder
    //   90: astore_1
    //   91: aload_1
    //   92: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   95: aload_1
    //   96: ldc -49
    //   98: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: new 209	java/lang/Throwable
    //   105: astore_3
    //   106: aload_3
    //   107: invokespecial 210	java/lang/Throwable:<init>	()V
    //   110: aload_1
    //   111: aload_3
    //   112: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 40
    //   121: aload_1
    //   122: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: aload_0
    //   130: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   133: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   136: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   139: iconst_0
    //   140: ireturn
    //   141: aload_0
    //   142: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   145: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   148: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   151: aload_1
    //   152: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	this	BluetoothHearingAid
    //   0	153	1	paramBluetoothDevice	BluetoothDevice
    //   42	12	2	i	int
    //   105	7	3	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   0	43	82	finally
    //   55	70	82	finally
    //   87	129	82	finally
    //   0	43	86	android/os/RemoteException
    //   55	70	86	android/os/RemoteException
  }
  
  /* Error */
  public int getDeviceMode(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnull +41 -> 55
    //   17: aload_0
    //   18: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   21: ifeq +34 -> 55
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 239	android/bluetooth/BluetoothHearingAid:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   29: ifeq +26 -> 55
    //   32: aload_0
    //   33: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   36: aload_1
    //   37: invokeinterface 300 2 0
    //   42: istore_2
    //   43: aload_0
    //   44: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   47: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   50: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   53: iload_2
    //   54: ireturn
    //   55: aload_0
    //   56: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   59: ifnonnull +11 -> 70
    //   62: ldc 40
    //   64: ldc -63
    //   66: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_0
    //   81: ireturn
    //   82: astore_1
    //   83: goto +58 -> 141
    //   86: astore_1
    //   87: new 153	java/lang/StringBuilder
    //   90: astore_3
    //   91: aload_3
    //   92: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   95: aload_3
    //   96: ldc -49
    //   98: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: new 209	java/lang/Throwable
    //   105: astore_1
    //   106: aload_1
    //   107: invokespecial 210	java/lang/Throwable:<init>	()V
    //   110: aload_3
    //   111: aload_1
    //   112: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 40
    //   121: aload_3
    //   122: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: aload_0
    //   130: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   133: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   136: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   139: iconst_0
    //   140: ireturn
    //   141: aload_0
    //   142: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   145: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   148: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   151: aload_1
    //   152: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	this	BluetoothHearingAid
    //   0	153	1	paramBluetoothDevice	BluetoothDevice
    //   42	12	2	i	int
    //   90	32	3	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   0	43	82	finally
    //   55	70	82	finally
    //   87	129	82	finally
    //   0	43	86	android/os/RemoteException
    //   55	70	86	android/os/RemoteException
  }
  
  /* Error */
  public int getDeviceSide(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnull +41 -> 55
    //   17: aload_0
    //   18: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   21: ifeq +34 -> 55
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 239	android/bluetooth/BluetoothHearingAid:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   29: ifeq +26 -> 55
    //   32: aload_0
    //   33: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   36: aload_1
    //   37: invokeinterface 303 2 0
    //   42: istore_2
    //   43: aload_0
    //   44: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   47: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   50: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   53: iload_2
    //   54: ireturn
    //   55: aload_0
    //   56: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   59: ifnonnull +11 -> 70
    //   62: ldc 40
    //   64: ldc -63
    //   66: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_0
    //   81: ireturn
    //   82: astore_1
    //   83: goto +58 -> 141
    //   86: astore_1
    //   87: new 153	java/lang/StringBuilder
    //   90: astore_1
    //   91: aload_1
    //   92: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   95: aload_1
    //   96: ldc -49
    //   98: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: new 209	java/lang/Throwable
    //   105: astore_3
    //   106: aload_3
    //   107: invokespecial 210	java/lang/Throwable:<init>	()V
    //   110: aload_1
    //   111: aload_3
    //   112: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 40
    //   121: aload_1
    //   122: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: aload_0
    //   130: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   133: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   136: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   139: iconst_0
    //   140: ireturn
    //   141: aload_0
    //   142: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   145: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   148: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   151: aload_1
    //   152: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	this	BluetoothHearingAid
    //   0	153	1	paramBluetoothDevice	BluetoothDevice
    //   42	12	2	i	int
    //   105	7	3	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   0	43	82	finally
    //   55	70	82	finally
    //   87	129	82	finally
    //   0	43	86	android/os/RemoteException
    //   55	70	86	android/os/RemoteException
  }
  
  /* Error */
  public java.util.List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnull +33 -> 47
    //   17: aload_0
    //   18: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   21: ifeq +26 -> 47
    //   24: aload_0
    //   25: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   28: aload_1
    //   29: invokeinterface 307 2 0
    //   34: astore_1
    //   35: aload_0
    //   36: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   39: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   42: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   45: aload_1
    //   46: areturn
    //   47: aload_0
    //   48: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   51: ifnonnull +11 -> 62
    //   54: ldc 40
    //   56: ldc -63
    //   58: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   61: pop
    //   62: new 287	java/util/ArrayList
    //   65: dup
    //   66: invokespecial 288	java/util/ArrayList:<init>	()V
    //   69: astore_1
    //   70: aload_0
    //   71: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: aload_1
    //   81: areturn
    //   82: astore_1
    //   83: goto +66 -> 149
    //   86: astore_1
    //   87: new 153	java/lang/StringBuilder
    //   90: astore_1
    //   91: aload_1
    //   92: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   95: aload_1
    //   96: ldc -49
    //   98: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: new 209	java/lang/Throwable
    //   105: astore_2
    //   106: aload_2
    //   107: invokespecial 210	java/lang/Throwable:<init>	()V
    //   110: aload_1
    //   111: aload_2
    //   112: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 40
    //   121: aload_1
    //   122: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: new 287	java/util/ArrayList
    //   132: dup
    //   133: invokespecial 288	java/util/ArrayList:<init>	()V
    //   136: astore_1
    //   137: aload_0
    //   138: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   141: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   144: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   147: aload_1
    //   148: areturn
    //   149: aload_0
    //   150: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   153: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   156: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   159: aload_1
    //   160: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	161	0	this	BluetoothHearingAid
    //   0	161	1	paramArrayOfInt	int[]
    //   105	7	2	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   0	35	82	finally
    //   47	62	82	finally
    //   62	70	82	finally
    //   87	137	82	finally
    //   0	35	86	android/os/RemoteException
    //   47	62	86	android/os/RemoteException
    //   62	70	86	android/os/RemoteException
  }
  
  /* Error */
  public long getHiSyncId(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnonnull +23 -> 37
    //   17: ldc 40
    //   19: ldc -63
    //   21: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   24: pop
    //   25: aload_0
    //   26: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   29: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   32: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   35: lconst_0
    //   36: lreturn
    //   37: aload_0
    //   38: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   41: ifeq +37 -> 78
    //   44: aload_0
    //   45: aload_1
    //   46: invokespecial 239	android/bluetooth/BluetoothHearingAid:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   49: ifne +6 -> 55
    //   52: goto +26 -> 78
    //   55: aload_0
    //   56: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   59: aload_1
    //   60: invokeinterface 312 2 0
    //   65: lstore_2
    //   66: aload_0
    //   67: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   70: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   73: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   76: lload_2
    //   77: lreturn
    //   78: aload_0
    //   79: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   82: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   85: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   88: lconst_0
    //   89: lreturn
    //   90: astore_1
    //   91: goto +61 -> 152
    //   94: astore_1
    //   95: new 153	java/lang/StringBuilder
    //   98: astore_1
    //   99: aload_1
    //   100: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   103: aload_1
    //   104: ldc -49
    //   106: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: pop
    //   110: new 209	java/lang/Throwable
    //   113: astore 4
    //   115: aload 4
    //   117: invokespecial 210	java/lang/Throwable:<init>	()V
    //   120: aload_1
    //   121: aload 4
    //   123: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   126: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: pop
    //   130: ldc 40
    //   132: aload_1
    //   133: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   136: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   139: pop
    //   140: aload_0
    //   141: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   144: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   147: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   150: lconst_0
    //   151: lreturn
    //   152: aload_0
    //   153: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   156: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   159: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   162: aload_1
    //   163: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	164	0	this	BluetoothHearingAid
    //   0	164	1	paramBluetoothDevice	BluetoothDevice
    //   65	12	2	l	long
    //   113	9	4	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   0	25	90	finally
    //   37	52	90	finally
    //   55	66	90	finally
    //   95	140	90	finally
    //   0	25	94	android/os/RemoteException
    //   37	52	94	android/os/RemoteException
    //   55	66	94	android/os/RemoteException
  }
  
  /* Error */
  public int getPriority(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnull +41 -> 55
    //   17: aload_0
    //   18: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   21: ifeq +34 -> 55
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 239	android/bluetooth/BluetoothHearingAid:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   29: ifeq +26 -> 55
    //   32: aload_0
    //   33: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   36: aload_1
    //   37: invokeinterface 315 2 0
    //   42: istore_2
    //   43: aload_0
    //   44: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   47: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   50: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   53: iload_2
    //   54: ireturn
    //   55: aload_0
    //   56: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   59: ifnonnull +11 -> 70
    //   62: ldc 40
    //   64: ldc -63
    //   66: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_0
    //   81: ireturn
    //   82: astore_1
    //   83: goto +58 -> 141
    //   86: astore_1
    //   87: new 153	java/lang/StringBuilder
    //   90: astore_3
    //   91: aload_3
    //   92: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   95: aload_3
    //   96: ldc -49
    //   98: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: new 209	java/lang/Throwable
    //   105: astore_1
    //   106: aload_1
    //   107: invokespecial 210	java/lang/Throwable:<init>	()V
    //   110: aload_3
    //   111: aload_1
    //   112: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 40
    //   121: aload_3
    //   122: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: aload_0
    //   130: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   133: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   136: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   139: iconst_0
    //   140: ireturn
    //   141: aload_0
    //   142: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   145: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   148: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   151: aload_1
    //   152: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	this	BluetoothHearingAid
    //   0	153	1	paramBluetoothDevice	BluetoothDevice
    //   42	12	2	i	int
    //   90	32	3	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   0	43	82	finally
    //   55	70	82	finally
    //   87	129	82	finally
    //   0	43	86	android/os/RemoteException
    //   55	70	86	android/os/RemoteException
  }
  
  /* Error */
  public int getVolume()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnull +32 -> 46
    //   17: aload_0
    //   18: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   21: ifeq +25 -> 46
    //   24: aload_0
    //   25: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   28: invokeinterface 318 1 0
    //   33: istore_1
    //   34: aload_0
    //   35: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   38: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   41: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   44: iload_1
    //   45: ireturn
    //   46: aload_0
    //   47: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   50: ifnonnull +11 -> 61
    //   53: ldc 40
    //   55: ldc -63
    //   57: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   60: pop
    //   61: aload_0
    //   62: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   65: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   68: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   71: iconst_0
    //   72: ireturn
    //   73: astore_2
    //   74: goto +58 -> 132
    //   77: astore_2
    //   78: new 153	java/lang/StringBuilder
    //   81: astore_3
    //   82: aload_3
    //   83: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   86: aload_3
    //   87: ldc -49
    //   89: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: pop
    //   93: new 209	java/lang/Throwable
    //   96: astore_2
    //   97: aload_2
    //   98: invokespecial 210	java/lang/Throwable:<init>	()V
    //   101: aload_3
    //   102: aload_2
    //   103: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   106: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: pop
    //   110: ldc 40
    //   112: aload_3
    //   113: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   116: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   119: pop
    //   120: aload_0
    //   121: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   124: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   127: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   130: iconst_0
    //   131: ireturn
    //   132: aload_0
    //   133: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   136: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   139: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   142: aload_2
    //   143: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	144	0	this	BluetoothHearingAid
    //   33	12	1	i	int
    //   73	1	2	localObject	Object
    //   77	1	2	localRemoteException	RemoteException
    //   96	47	2	localThrowable	Throwable
    //   81	32	3	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   0	34	73	finally
    //   46	61	73	finally
    //   78	120	73	finally
    //   0	34	77	android/os/RemoteException
    //   46	61	77	android/os/RemoteException
  }
  
  /* Error */
  public boolean setActiveDevice(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnull +45 -> 59
    //   17: aload_0
    //   18: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   21: ifeq +38 -> 59
    //   24: aload_1
    //   25: ifnull +11 -> 36
    //   28: aload_0
    //   29: aload_1
    //   30: invokespecial 239	android/bluetooth/BluetoothHearingAid:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   33: ifeq +26 -> 59
    //   36: aload_0
    //   37: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   40: aload_1
    //   41: invokeinterface 321 2 0
    //   46: pop
    //   47: aload_0
    //   48: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   51: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   54: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   57: iconst_1
    //   58: ireturn
    //   59: aload_0
    //   60: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   63: ifnonnull +11 -> 74
    //   66: ldc 40
    //   68: ldc -63
    //   70: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   73: pop
    //   74: aload_0
    //   75: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   78: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   81: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   84: iconst_0
    //   85: ireturn
    //   86: astore_1
    //   87: goto +58 -> 145
    //   90: astore_1
    //   91: new 153	java/lang/StringBuilder
    //   94: astore_2
    //   95: aload_2
    //   96: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   99: aload_2
    //   100: ldc -49
    //   102: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: pop
    //   106: new 209	java/lang/Throwable
    //   109: astore_1
    //   110: aload_1
    //   111: invokespecial 210	java/lang/Throwable:<init>	()V
    //   114: aload_2
    //   115: aload_1
    //   116: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   119: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   122: pop
    //   123: ldc 40
    //   125: aload_2
    //   126: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   129: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   132: pop
    //   133: aload_0
    //   134: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   137: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   140: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   143: iconst_0
    //   144: ireturn
    //   145: aload_0
    //   146: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   149: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   152: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   155: aload_1
    //   156: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	157	0	this	BluetoothHearingAid
    //   0	157	1	paramBluetoothDevice	BluetoothDevice
    //   94	32	2	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   0	24	86	finally
    //   28	36	86	finally
    //   36	47	86	finally
    //   59	74	86	finally
    //   91	133	86	finally
    //   0	24	90	android/os/RemoteException
    //   28	36	90	android/os/RemoteException
    //   36	47	90	android/os/RemoteException
    //   59	74	90	android/os/RemoteException
  }
  
  /* Error */
  public boolean setPriority(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnull +66 -> 80
    //   17: aload_0
    //   18: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   21: ifeq +59 -> 80
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 239	android/bluetooth/BluetoothHearingAid:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   29: istore_3
    //   30: iload_3
    //   31: ifeq +49 -> 80
    //   34: iload_2
    //   35: ifeq +21 -> 56
    //   38: iload_2
    //   39: bipush 100
    //   41: if_icmpeq +15 -> 56
    //   44: aload_0
    //   45: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   48: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   51: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   54: iconst_0
    //   55: ireturn
    //   56: aload_0
    //   57: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   60: aload_1
    //   61: iload_2
    //   62: invokeinterface 325 3 0
    //   67: istore_3
    //   68: aload_0
    //   69: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   72: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   75: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   78: iload_3
    //   79: ireturn
    //   80: aload_0
    //   81: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   84: ifnonnull +11 -> 95
    //   87: ldc 40
    //   89: ldc -63
    //   91: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   94: pop
    //   95: aload_0
    //   96: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   99: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   102: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   105: iconst_0
    //   106: ireturn
    //   107: astore_1
    //   108: goto +63 -> 171
    //   111: astore_1
    //   112: new 153	java/lang/StringBuilder
    //   115: astore 4
    //   117: aload 4
    //   119: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   122: aload 4
    //   124: ldc -49
    //   126: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: pop
    //   130: new 209	java/lang/Throwable
    //   133: astore_1
    //   134: aload_1
    //   135: invokespecial 210	java/lang/Throwable:<init>	()V
    //   138: aload 4
    //   140: aload_1
    //   141: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   144: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   147: pop
    //   148: ldc 40
    //   150: aload 4
    //   152: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   155: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   158: pop
    //   159: aload_0
    //   160: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   163: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   166: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   169: iconst_0
    //   170: ireturn
    //   171: aload_0
    //   172: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   175: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   178: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   181: aload_1
    //   182: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	183	0	this	BluetoothHearingAid
    //   0	183	1	paramBluetoothDevice	BluetoothDevice
    //   0	183	2	paramInt	int
    //   29	50	3	bool	boolean
    //   115	36	4	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   0	30	107	finally
    //   56	68	107	finally
    //   80	95	107	finally
    //   112	159	107	finally
    //   0	30	111	android/os/RemoteException
    //   56	68	111	android/os/RemoteException
    //   80	95	111	android/os/RemoteException
  }
  
  /* Error */
  public void setVolume(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 191	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   14: ifnonnull +22 -> 36
    //   17: ldc 40
    //   19: ldc -63
    //   21: invokestatic 196	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   24: pop
    //   25: aload_0
    //   26: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   29: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   32: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   35: return
    //   36: aload_0
    //   37: invokespecial 201	android/bluetooth/BluetoothHearingAid:isEnabled	()Z
    //   40: istore_2
    //   41: iload_2
    //   42: ifne +14 -> 56
    //   45: aload_0
    //   46: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   49: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   52: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   55: return
    //   56: aload_0
    //   57: getfield 117	android/bluetooth/BluetoothHearingAid:mService	Landroid/bluetooth/IBluetoothHearingAid;
    //   60: iload_1
    //   61: invokeinterface 328 2 0
    //   66: aload_0
    //   67: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   70: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   73: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   76: goto +56 -> 132
    //   79: astore_3
    //   80: goto +53 -> 133
    //   83: astore_3
    //   84: new 153	java/lang/StringBuilder
    //   87: astore_3
    //   88: aload_3
    //   89: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   92: aload_3
    //   93: ldc -49
    //   95: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: new 209	java/lang/Throwable
    //   102: astore 4
    //   104: aload 4
    //   106: invokespecial 210	java/lang/Throwable:<init>	()V
    //   109: aload_3
    //   110: aload 4
    //   112: invokestatic 214	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 40
    //   121: aload_3
    //   122: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 216	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: goto -63 -> 66
    //   132: return
    //   133: aload_0
    //   134: getfield 69	android/bluetooth/BluetoothHearingAid:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   137: invokevirtual 186	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   140: invokevirtual 199	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   143: aload_3
    //   144: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	145	0	this	BluetoothHearingAid
    //   0	145	1	paramInt	int
    //   40	2	2	bool	boolean
    //   79	1	3	localObject	Object
    //   83	1	3	localRemoteException	RemoteException
    //   87	57	3	localStringBuilder	StringBuilder
    //   102	9	4	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   0	25	79	finally
    //   36	41	79	finally
    //   56	66	79	finally
    //   84	129	79	finally
    //   0	25	83	android/os/RemoteException
    //   36	41	83	android/os/RemoteException
    //   56	66	83	android/os/RemoteException
  }
}
