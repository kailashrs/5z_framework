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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public final class BluetoothBATransmitter
  implements BluetoothProfile
{
  public static final String ACTION_BAT_AVRCP_VS_CMD = "android.bluetooth.bat.profile.action.BA_AVRCP_VS_CMD";
  public static final String ACTION_BAT_DIV_CHANGED = "android.bluetooth.bat.profile.action.BA_DIV_CHANGED";
  public static final String ACTION_BAT_ENCRYPTION_KEY_CHANGED = "android.bluetooth.bat.profile.action.BA_ENC_KEY_CHANGED";
  public static final String ACTION_BAT_STATE_CHANGED = "android.bluetooth.bat.profile.action.BA_STATE_CHANGED";
  public static final String ACTION_BAT_STREAMING_ID_CHANGED = "android.bluetooth.bat.profile.action.BA_STR_ID_CHANGED";
  private static final boolean DBG = true;
  public static final int DISABLE_BA_TRANSMITTER = 1;
  public static final int ENABLE_BA_TRANSMITTER = 0;
  public static final String EXTRA_AVRCP_VS_ENABLE_BA = "android.bluetooth.bat.profile.extra.ENABLE_BA";
  public static final String EXTRA_AVRCP_VS_ENABLE_RA = "android.bluetooth.bat.profile.extra.ENABLE_RA";
  public static final String EXTRA_DIV_VALUE = "android.bluetooth.bat.profile.extra.DIV";
  public static final String EXTRA_ECNRYPTION_KEY = "android.bluetooth.bat.profile.extra.ENC_KEY";
  public static final String EXTRA_STATE = "android.bluetooth.bat.profile.extra.STATE";
  public static final String EXTRA_STREAM_ID = "android.bluetooth.bat.profile.extra.STR_ID";
  public static final int INVALID_DIV = 65535;
  public static final int STATE_DISABLED = 0;
  public static final int STATE_PAUSED = 1;
  public static final int STATE_PLAYING = 2;
  private static final String TAG = "BluetoothBAT";
  private static final boolean VDBG = false;
  private BluetoothAdapter mAdapter;
  private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    /* Error */
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      // Byte code:
      //   0: new 22	java/lang/StringBuilder
      //   3: dup
      //   4: invokespecial 23	java/lang/StringBuilder:<init>	()V
      //   7: astore_2
      //   8: aload_2
      //   9: ldc 25
      //   11: invokevirtual 29	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   14: pop
      //   15: aload_2
      //   16: iload_1
      //   17: invokevirtual 32	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
      //   20: pop
      //   21: ldc 34
      //   23: aload_2
      //   24: invokevirtual 38	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   27: invokestatic 44	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   30: pop
      //   31: iload_1
      //   32: ifne +98 -> 130
      //   35: ldc 34
      //   37: ldc 46
      //   39: invokestatic 44	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   42: pop
      //   43: aload_0
      //   44: getfield 12	android/bluetooth/BluetoothBATransmitter$1:this$0	Landroid/bluetooth/BluetoothBATransmitter;
      //   47: invokestatic 50	android/bluetooth/BluetoothBATransmitter:access$000	(Landroid/bluetooth/BluetoothBATransmitter;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   50: invokevirtual 56	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
      //   53: invokevirtual 61	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
      //   56: aload_0
      //   57: getfield 12	android/bluetooth/BluetoothBATransmitter$1:this$0	Landroid/bluetooth/BluetoothBATransmitter;
      //   60: aconst_null
      //   61: invokestatic 65	android/bluetooth/BluetoothBATransmitter:access$102	(Landroid/bluetooth/BluetoothBATransmitter;Landroid/bluetooth/IBluetoothBATransmitter;)Landroid/bluetooth/IBluetoothBATransmitter;
      //   64: pop
      //   65: aload_0
      //   66: getfield 12	android/bluetooth/BluetoothBATransmitter$1:this$0	Landroid/bluetooth/BluetoothBATransmitter;
      //   69: invokestatic 69	android/bluetooth/BluetoothBATransmitter:access$300	(Landroid/bluetooth/BluetoothBATransmitter;)Landroid/content/Context;
      //   72: aload_0
      //   73: getfield 12	android/bluetooth/BluetoothBATransmitter$1:this$0	Landroid/bluetooth/BluetoothBATransmitter;
      //   76: invokestatic 73	android/bluetooth/BluetoothBATransmitter:access$200	(Landroid/bluetooth/BluetoothBATransmitter;)Landroid/content/ServiceConnection;
      //   79: invokevirtual 79	android/content/Context:unbindService	(Landroid/content/ServiceConnection;)V
      //   82: goto +17 -> 99
      //   85: astore_2
      //   86: goto +29 -> 115
      //   89: astore_2
      //   90: ldc 34
      //   92: ldc 81
      //   94: aload_2
      //   95: invokestatic 85	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   98: pop
      //   99: aload_0
      //   100: getfield 12	android/bluetooth/BluetoothBATransmitter$1:this$0	Landroid/bluetooth/BluetoothBATransmitter;
      //   103: invokestatic 50	android/bluetooth/BluetoothBATransmitter:access$000	(Landroid/bluetooth/BluetoothBATransmitter;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   106: invokevirtual 56	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
      //   109: invokevirtual 88	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
      //   112: goto +87 -> 199
      //   115: aload_0
      //   116: getfield 12	android/bluetooth/BluetoothBATransmitter$1:this$0	Landroid/bluetooth/BluetoothBATransmitter;
      //   119: invokestatic 50	android/bluetooth/BluetoothBATransmitter:access$000	(Landroid/bluetooth/BluetoothBATransmitter;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   122: invokevirtual 56	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
      //   125: invokevirtual 88	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
      //   128: aload_2
      //   129: athrow
      //   130: aload_0
      //   131: getfield 12	android/bluetooth/BluetoothBATransmitter$1:this$0	Landroid/bluetooth/BluetoothBATransmitter;
      //   134: invokestatic 50	android/bluetooth/BluetoothBATransmitter:access$000	(Landroid/bluetooth/BluetoothBATransmitter;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   137: invokevirtual 92	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
      //   140: invokevirtual 95	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
      //   143: aload_0
      //   144: getfield 12	android/bluetooth/BluetoothBATransmitter$1:this$0	Landroid/bluetooth/BluetoothBATransmitter;
      //   147: invokestatic 99	android/bluetooth/BluetoothBATransmitter:access$100	(Landroid/bluetooth/BluetoothBATransmitter;)Landroid/bluetooth/IBluetoothBATransmitter;
      //   150: ifnonnull +36 -> 186
      //   153: ldc 34
      //   155: ldc 101
      //   157: invokestatic 44	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   160: pop
      //   161: aload_0
      //   162: getfield 12	android/bluetooth/BluetoothBATransmitter$1:this$0	Landroid/bluetooth/BluetoothBATransmitter;
      //   165: invokevirtual 105	android/bluetooth/BluetoothBATransmitter:doBind	()Z
      //   168: pop
      //   169: goto +17 -> 186
      //   172: astore_2
      //   173: goto +27 -> 200
      //   176: astore_2
      //   177: ldc 34
      //   179: ldc 81
      //   181: aload_2
      //   182: invokestatic 85	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   185: pop
      //   186: aload_0
      //   187: getfield 12	android/bluetooth/BluetoothBATransmitter$1:this$0	Landroid/bluetooth/BluetoothBATransmitter;
      //   190: invokestatic 50	android/bluetooth/BluetoothBATransmitter:access$000	(Landroid/bluetooth/BluetoothBATransmitter;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   193: invokevirtual 92	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
      //   196: invokevirtual 106	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
      //   199: return
      //   200: aload_0
      //   201: getfield 12	android/bluetooth/BluetoothBATransmitter$1:this$0	Landroid/bluetooth/BluetoothBATransmitter;
      //   204: invokestatic 50	android/bluetooth/BluetoothBATransmitter:access$000	(Landroid/bluetooth/BluetoothBATransmitter;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   207: invokevirtual 92	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
      //   210: invokevirtual 106	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
      //   213: aload_2
      //   214: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	215	0	this	1
      //   0	215	1	paramAnonymousBoolean	boolean
      //   7	17	2	localStringBuilder	StringBuilder
      //   85	1	2	localObject1	Object
      //   89	40	2	localException1	Exception
      //   172	1	2	localObject2	Object
      //   176	38	2	localException2	Exception
      // Exception table:
      //   from	to	target	type
      //   43	82	85	finally
      //   90	99	85	finally
      //   43	82	89	java/lang/Exception
      //   130	169	172	finally
      //   177	186	172	finally
      //   130	169	176	java/lang/Exception
    }
  };
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      Log.d("BluetoothBAT", "Proxy object connected");
      try
      {
        mServiceLock.writeLock().lock();
        BluetoothBATransmitter.access$102(BluetoothBATransmitter.this, IBluetoothBATransmitter.Stub.asInterface(Binder.allowBlocking(paramAnonymousIBinder)));
        mServiceLock.writeLock().unlock();
        if (mServiceListener != null) {
          mServiceListener.onServiceConnected(23, BluetoothBATransmitter.this);
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
      Log.d("BluetoothBAT", "Proxy object disconnected");
      try
      {
        mServiceLock.writeLock().lock();
        BluetoothBATransmitter.access$102(BluetoothBATransmitter.this, null);
        mServiceLock.writeLock().unlock();
        if (mServiceListener != null) {
          mServiceListener.onServiceDisconnected(23);
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
  private IBluetoothBATransmitter mService;
  private BluetoothProfile.ServiceListener mServiceListener;
  private final ReentrantReadWriteLock mServiceLock = new ReentrantReadWriteLock();
  
  BluetoothBATransmitter(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
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
        Log.e("BluetoothBAT", "", paramContext);
      }
    }
    doBind();
  }
  
  private boolean isEnabled()
  {
    return mAdapter.getState() == 12;
  }
  
  private static void log(String paramString)
  {
    Log.d("BluetoothBAT", paramString);
  }
  
  /* Error */
  void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: aconst_null
    //   2: putfield 101	android/bluetooth/BluetoothBATransmitter:mServiceListener	Landroid/bluetooth/BluetoothProfile$ServiceListener;
    //   5: aload_0
    //   6: getfield 109	android/bluetooth/BluetoothBATransmitter:mAdapter	Landroid/bluetooth/BluetoothAdapter;
    //   9: invokevirtual 113	android/bluetooth/BluetoothAdapter:getBluetoothManager	()Landroid/bluetooth/IBluetoothManager;
    //   12: astore_1
    //   13: aload_1
    //   14: ifnull +26 -> 40
    //   17: aload_1
    //   18: aload_0
    //   19: getfield 94	android/bluetooth/BluetoothBATransmitter:mBluetoothStateChangeCallback	Landroid/bluetooth/IBluetoothStateChangeCallback;
    //   22: invokeinterface 163 2 0
    //   27: goto +13 -> 40
    //   30: astore_1
    //   31: ldc 60
    //   33: ldc 121
    //   35: aload_1
    //   36: invokestatic 127	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   39: pop
    //   40: aload_0
    //   41: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   44: invokevirtual 167	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   47: invokevirtual 172	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
    //   50: aload_0
    //   51: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   54: ifnull +36 -> 90
    //   57: aload_0
    //   58: aconst_null
    //   59: putfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   62: aload_0
    //   63: getfield 99	android/bluetooth/BluetoothBATransmitter:mContext	Landroid/content/Context;
    //   66: aload_0
    //   67: getfield 97	android/bluetooth/BluetoothBATransmitter:mConnection	Landroid/content/ServiceConnection;
    //   70: invokevirtual 178	android/content/Context:unbindService	(Landroid/content/ServiceConnection;)V
    //   73: goto +17 -> 90
    //   76: astore_1
    //   77: goto +24 -> 101
    //   80: astore_1
    //   81: ldc 60
    //   83: ldc 121
    //   85: aload_1
    //   86: invokestatic 127	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   89: pop
    //   90: aload_0
    //   91: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   94: invokevirtual 167	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   97: invokevirtual 181	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   100: return
    //   101: aload_0
    //   102: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   105: invokevirtual 167	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   108: invokevirtual 181	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   111: aload_1
    //   112: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	113	0	this	BluetoothBATransmitter
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
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothBATransmitter.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, Process.myUserHandle()))) {
      return true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth Broadcast Audio Transmitter Service ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothBAT", ((StringBuilder)localObject).toString());
    return false;
  }
  
  public void finalize() {}
  
  /* Error */
  public BluetoothBAStreamServiceRecord getBAServiceRecord()
  {
    // Byte code:
    //   0: ldc -20
    //   2: invokestatic 238	android/bluetooth/BluetoothBATransmitter:log	(Ljava/lang/String;)V
    //   5: aload_0
    //   6: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   9: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   12: invokevirtual 245	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   15: aload_0
    //   16: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   19: ifnull +32 -> 51
    //   22: aload_0
    //   23: invokespecial 247	android/bluetooth/BluetoothBATransmitter:isEnabled	()Z
    //   26: ifeq +25 -> 51
    //   29: aload_0
    //   30: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   33: invokeinterface 249 1 0
    //   38: astore_1
    //   39: aload_0
    //   40: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   43: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   46: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   49: aload_1
    //   50: areturn
    //   51: aload_0
    //   52: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   55: ifnonnull +11 -> 66
    //   58: ldc 60
    //   60: ldc -4
    //   62: invokestatic 255	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   65: pop
    //   66: aload_0
    //   67: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   70: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   73: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   76: aconst_null
    //   77: areturn
    //   78: astore_1
    //   79: goto +59 -> 138
    //   82: astore_1
    //   83: new 217	java/lang/StringBuilder
    //   86: astore_2
    //   87: aload_2
    //   88: invokespecial 218	java/lang/StringBuilder:<init>	()V
    //   91: aload_2
    //   92: ldc_w 257
    //   95: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: new 259	java/lang/Throwable
    //   102: astore_1
    //   103: aload_1
    //   104: invokespecial 260	java/lang/Throwable:<init>	()V
    //   107: aload_2
    //   108: aload_1
    //   109: invokestatic 264	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   112: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   115: pop
    //   116: ldc 60
    //   118: aload_2
    //   119: invokevirtual 230	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   122: invokestatic 232	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   125: pop
    //   126: aload_0
    //   127: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   130: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   133: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   136: aconst_null
    //   137: areturn
    //   138: aload_0
    //   139: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   142: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   145: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   148: aload_1
    //   149: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	150	0	this	BluetoothBATransmitter
    //   38	12	1	localBluetoothBAStreamServiceRecord	BluetoothBAStreamServiceRecord
    //   78	1	1	localObject	Object
    //   82	1	1	localRemoteException	RemoteException
    //   102	47	1	localThrowable	Throwable
    //   86	33	2	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   5	39	78	finally
    //   51	66	78	finally
    //   83	126	78	finally
    //   5	39	82	android/os/RemoteException
    //   51	66	82	android/os/RemoteException
  }
  
  /* Error */
  public int getBATState()
  {
    // Byte code:
    //   0: ldc_w 266
    //   3: invokestatic 238	android/bluetooth/BluetoothBATransmitter:log	(Ljava/lang/String;)V
    //   6: aload_0
    //   7: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   10: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   13: invokevirtual 245	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   16: aload_0
    //   17: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   20: ifnull +32 -> 52
    //   23: aload_0
    //   24: invokespecial 247	android/bluetooth/BluetoothBATransmitter:isEnabled	()Z
    //   27: ifeq +25 -> 52
    //   30: aload_0
    //   31: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   34: invokeinterface 268 1 0
    //   39: istore_1
    //   40: aload_0
    //   41: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   44: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   47: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   50: iload_1
    //   51: ireturn
    //   52: aload_0
    //   53: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   56: ifnonnull +11 -> 67
    //   59: ldc 60
    //   61: ldc -4
    //   63: invokestatic 255	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   66: pop
    //   67: aload_0
    //   68: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   71: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   74: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   77: iconst_0
    //   78: ireturn
    //   79: astore_2
    //   80: goto +59 -> 139
    //   83: astore_2
    //   84: new 217	java/lang/StringBuilder
    //   87: astore_2
    //   88: aload_2
    //   89: invokespecial 218	java/lang/StringBuilder:<init>	()V
    //   92: aload_2
    //   93: ldc_w 257
    //   96: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: new 259	java/lang/Throwable
    //   103: astore_3
    //   104: aload_3
    //   105: invokespecial 260	java/lang/Throwable:<init>	()V
    //   108: aload_2
    //   109: aload_3
    //   110: invokestatic 264	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   113: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: pop
    //   117: ldc 60
    //   119: aload_2
    //   120: invokevirtual 230	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   123: invokestatic 232	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   126: pop
    //   127: aload_0
    //   128: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   131: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   134: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   137: iconst_0
    //   138: ireturn
    //   139: aload_0
    //   140: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   143: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   146: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   149: aload_2
    //   150: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	151	0	this	BluetoothBATransmitter
    //   39	12	1	i	int
    //   79	1	2	localObject	Object
    //   83	1	2	localRemoteException	RemoteException
    //   87	63	2	localStringBuilder	StringBuilder
    //   103	7	3	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   6	40	79	finally
    //   52	67	79	finally
    //   84	127	79	finally
    //   6	40	83	android/os/RemoteException
    //   52	67	83	android/os/RemoteException
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    log("getConnectedDevices() dummy impl");
    return new ArrayList();
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    log("getConnectionState() dummy impl");
    return 0;
  }
  
  /* Error */
  public int getDIV()
  {
    // Byte code:
    //   0: ldc_w 283
    //   3: invokestatic 238	android/bluetooth/BluetoothBATransmitter:log	(Ljava/lang/String;)V
    //   6: aload_0
    //   7: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   10: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   13: invokevirtual 245	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   16: aload_0
    //   17: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   20: ifnull +32 -> 52
    //   23: aload_0
    //   24: invokespecial 247	android/bluetooth/BluetoothBATransmitter:isEnabled	()Z
    //   27: ifeq +25 -> 52
    //   30: aload_0
    //   31: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   34: invokeinterface 285 1 0
    //   39: istore_1
    //   40: aload_0
    //   41: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   44: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   47: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   50: iload_1
    //   51: ireturn
    //   52: aload_0
    //   53: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   56: ifnonnull +11 -> 67
    //   59: ldc 60
    //   61: ldc -4
    //   63: invokestatic 255	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   66: pop
    //   67: aload_0
    //   68: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   71: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   74: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   77: ldc 53
    //   79: ireturn
    //   80: astore_2
    //   81: goto +60 -> 141
    //   84: astore_2
    //   85: new 217	java/lang/StringBuilder
    //   88: astore_2
    //   89: aload_2
    //   90: invokespecial 218	java/lang/StringBuilder:<init>	()V
    //   93: aload_2
    //   94: ldc_w 257
    //   97: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: new 259	java/lang/Throwable
    //   104: astore_3
    //   105: aload_3
    //   106: invokespecial 260	java/lang/Throwable:<init>	()V
    //   109: aload_2
    //   110: aload_3
    //   111: invokestatic 264	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   114: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: pop
    //   118: ldc 60
    //   120: aload_2
    //   121: invokevirtual 230	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   124: invokestatic 232	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   127: pop
    //   128: aload_0
    //   129: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   132: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   135: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   138: ldc 53
    //   140: ireturn
    //   141: aload_0
    //   142: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   145: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   148: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   151: aload_2
    //   152: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	this	BluetoothBATransmitter
    //   39	12	1	i	int
    //   80	1	2	localObject	Object
    //   84	1	2	localRemoteException	RemoteException
    //   88	64	2	localStringBuilder	StringBuilder
    //   104	7	3	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   6	40	80	finally
    //   52	67	80	finally
    //   85	128	80	finally
    //   6	40	84	android/os/RemoteException
    //   52	67	84	android/os/RemoteException
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    log("getDevicesMatchingStates() dummy impl");
    return new ArrayList();
  }
  
  /* Error */
  public BluetoothBAEncryptionKey getEncryptionKey()
  {
    // Byte code:
    //   0: ldc_w 293
    //   3: invokestatic 238	android/bluetooth/BluetoothBATransmitter:log	(Ljava/lang/String;)V
    //   6: aload_0
    //   7: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   10: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   13: invokevirtual 245	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   16: aload_0
    //   17: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   20: ifnull +32 -> 52
    //   23: aload_0
    //   24: invokespecial 247	android/bluetooth/BluetoothBATransmitter:isEnabled	()Z
    //   27: ifeq +25 -> 52
    //   30: aload_0
    //   31: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   34: invokeinterface 295 1 0
    //   39: astore_1
    //   40: aload_0
    //   41: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   44: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   47: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   50: aload_1
    //   51: areturn
    //   52: aload_0
    //   53: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   56: ifnonnull +11 -> 67
    //   59: ldc 60
    //   61: ldc -4
    //   63: invokestatic 255	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   66: pop
    //   67: aload_0
    //   68: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   71: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   74: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   77: aconst_null
    //   78: areturn
    //   79: astore_1
    //   80: goto +59 -> 139
    //   83: astore_1
    //   84: new 217	java/lang/StringBuilder
    //   87: astore_2
    //   88: aload_2
    //   89: invokespecial 218	java/lang/StringBuilder:<init>	()V
    //   92: aload_2
    //   93: ldc_w 257
    //   96: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: new 259	java/lang/Throwable
    //   103: astore_1
    //   104: aload_1
    //   105: invokespecial 260	java/lang/Throwable:<init>	()V
    //   108: aload_2
    //   109: aload_1
    //   110: invokestatic 264	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   113: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: pop
    //   117: ldc 60
    //   119: aload_2
    //   120: invokevirtual 230	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   123: invokestatic 232	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   126: pop
    //   127: aload_0
    //   128: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   131: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   134: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   137: aconst_null
    //   138: areturn
    //   139: aload_0
    //   140: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   143: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   146: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   149: aload_1
    //   150: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	151	0	this	BluetoothBATransmitter
    //   39	12	1	localBluetoothBAEncryptionKey	BluetoothBAEncryptionKey
    //   79	1	1	localObject	Object
    //   83	1	1	localRemoteException	RemoteException
    //   103	47	1	localThrowable	Throwable
    //   87	33	2	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   6	40	79	finally
    //   52	67	79	finally
    //   84	127	79	finally
    //   6	40	83	android/os/RemoteException
    //   52	67	83	android/os/RemoteException
  }
  
  /* Error */
  public long getStreamId()
  {
    // Byte code:
    //   0: ldc_w 298
    //   3: invokestatic 238	android/bluetooth/BluetoothBATransmitter:log	(Ljava/lang/String;)V
    //   6: aload_0
    //   7: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   10: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   13: invokevirtual 245	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   16: aload_0
    //   17: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   20: ifnull +32 -> 52
    //   23: aload_0
    //   24: invokespecial 247	android/bluetooth/BluetoothBATransmitter:isEnabled	()Z
    //   27: ifeq +25 -> 52
    //   30: aload_0
    //   31: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   34: invokeinterface 300 1 0
    //   39: lstore_1
    //   40: aload_0
    //   41: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   44: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   47: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   50: lload_1
    //   51: lreturn
    //   52: aload_0
    //   53: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   56: ifnonnull +11 -> 67
    //   59: ldc 60
    //   61: ldc -4
    //   63: invokestatic 255	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   66: pop
    //   67: aload_0
    //   68: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   71: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   74: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   77: lconst_0
    //   78: lreturn
    //   79: astore_3
    //   80: goto +64 -> 144
    //   83: astore_3
    //   84: new 217	java/lang/StringBuilder
    //   87: astore 4
    //   89: aload 4
    //   91: invokespecial 218	java/lang/StringBuilder:<init>	()V
    //   94: aload 4
    //   96: ldc_w 257
    //   99: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: pop
    //   103: new 259	java/lang/Throwable
    //   106: astore_3
    //   107: aload_3
    //   108: invokespecial 260	java/lang/Throwable:<init>	()V
    //   111: aload 4
    //   113: aload_3
    //   114: invokestatic 264	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   117: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: pop
    //   121: ldc 60
    //   123: aload 4
    //   125: invokevirtual 230	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   128: invokestatic 232	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   131: pop
    //   132: aload_0
    //   133: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   136: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   139: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   142: lconst_0
    //   143: lreturn
    //   144: aload_0
    //   145: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   148: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   151: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   154: aload_3
    //   155: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	156	0	this	BluetoothBATransmitter
    //   39	12	1	l	long
    //   79	1	3	localObject	Object
    //   83	1	3	localRemoteException	RemoteException
    //   106	49	3	localThrowable	Throwable
    //   87	37	4	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   6	40	79	finally
    //   52	67	79	finally
    //   84	132	79	finally
    //   6	40	83	android/os/RemoteException
    //   52	67	83	android/os/RemoteException
  }
  
  /* Error */
  public boolean refreshEncryptionKey()
  {
    // Byte code:
    //   0: ldc_w 293
    //   3: invokestatic 238	android/bluetooth/BluetoothBATransmitter:log	(Ljava/lang/String;)V
    //   6: aload_0
    //   7: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   10: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   13: invokevirtual 245	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   16: aload_0
    //   17: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   20: ifnull +32 -> 52
    //   23: aload_0
    //   24: invokespecial 247	android/bluetooth/BluetoothBATransmitter:isEnabled	()Z
    //   27: ifeq +25 -> 52
    //   30: aload_0
    //   31: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   34: invokeinterface 303 1 0
    //   39: istore_1
    //   40: aload_0
    //   41: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   44: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   47: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   50: iload_1
    //   51: ireturn
    //   52: aload_0
    //   53: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   56: ifnonnull +11 -> 67
    //   59: ldc 60
    //   61: ldc -4
    //   63: invokestatic 255	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   66: pop
    //   67: aload_0
    //   68: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   71: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   74: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   77: iconst_0
    //   78: ireturn
    //   79: astore_2
    //   80: goto +59 -> 139
    //   83: astore_2
    //   84: new 217	java/lang/StringBuilder
    //   87: astore_2
    //   88: aload_2
    //   89: invokespecial 218	java/lang/StringBuilder:<init>	()V
    //   92: aload_2
    //   93: ldc_w 257
    //   96: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: new 259	java/lang/Throwable
    //   103: astore_3
    //   104: aload_3
    //   105: invokespecial 260	java/lang/Throwable:<init>	()V
    //   108: aload_2
    //   109: aload_3
    //   110: invokestatic 264	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   113: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: pop
    //   117: ldc 60
    //   119: aload_2
    //   120: invokevirtual 230	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   123: invokestatic 232	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   126: pop
    //   127: aload_0
    //   128: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   131: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   134: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   137: iconst_0
    //   138: ireturn
    //   139: aload_0
    //   140: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   143: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   146: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   149: aload_2
    //   150: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	151	0	this	BluetoothBATransmitter
    //   39	12	1	bool	boolean
    //   79	1	2	localObject	Object
    //   83	1	2	localRemoteException	RemoteException
    //   87	63	2	localStringBuilder	StringBuilder
    //   103	7	3	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   6	40	79	finally
    //   52	67	79	finally
    //   84	127	79	finally
    //   6	40	83	android/os/RemoteException
    //   52	67	83	android/os/RemoteException
  }
  
  /* Error */
  public boolean setBATState(int paramInt)
  {
    // Byte code:
    //   0: new 217	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 218	java/lang/StringBuilder:<init>	()V
    //   7: astore_2
    //   8: aload_2
    //   9: ldc_w 307
    //   12: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload_2
    //   17: iload_1
    //   18: invokevirtual 310	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   21: pop
    //   22: aload_2
    //   23: ldc_w 312
    //   26: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: pop
    //   30: aload_2
    //   31: invokevirtual 230	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   34: invokestatic 238	android/bluetooth/BluetoothBATransmitter:log	(Ljava/lang/String;)V
    //   37: aload_0
    //   38: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   41: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   44: invokevirtual 245	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   47: aload_0
    //   48: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   51: ifnull +33 -> 84
    //   54: aload_0
    //   55: invokespecial 247	android/bluetooth/BluetoothBATransmitter:isEnabled	()Z
    //   58: ifeq +26 -> 84
    //   61: aload_0
    //   62: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   65: iload_1
    //   66: invokeinterface 314 2 0
    //   71: istore_3
    //   72: aload_0
    //   73: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   76: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   79: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   82: iload_3
    //   83: ireturn
    //   84: aload_0
    //   85: getfield 138	android/bluetooth/BluetoothBATransmitter:mService	Landroid/bluetooth/IBluetoothBATransmitter;
    //   88: ifnonnull +11 -> 99
    //   91: ldc 60
    //   93: ldc -4
    //   95: invokestatic 255	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   98: pop
    //   99: aload_0
    //   100: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   103: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   106: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   109: iconst_0
    //   110: ireturn
    //   111: astore_2
    //   112: goto +62 -> 174
    //   115: astore_2
    //   116: new 217	java/lang/StringBuilder
    //   119: astore_2
    //   120: aload_2
    //   121: invokespecial 218	java/lang/StringBuilder:<init>	()V
    //   124: aload_2
    //   125: ldc_w 257
    //   128: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: pop
    //   132: new 259	java/lang/Throwable
    //   135: astore 4
    //   137: aload 4
    //   139: invokespecial 260	java/lang/Throwable:<init>	()V
    //   142: aload_2
    //   143: aload 4
    //   145: invokestatic 264	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   148: invokevirtual 224	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: pop
    //   152: ldc 60
    //   154: aload_2
    //   155: invokevirtual 230	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   158: invokestatic 232	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   161: pop
    //   162: aload_0
    //   163: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   166: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   169: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   172: iconst_0
    //   173: ireturn
    //   174: aload_0
    //   175: getfield 89	android/bluetooth/BluetoothBATransmitter:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   178: invokevirtual 242	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   181: invokevirtual 250	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   184: aload_2
    //   185: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	186	0	this	BluetoothBATransmitter
    //   0	186	1	paramInt	int
    //   7	24	2	localStringBuilder1	StringBuilder
    //   111	1	2	localObject	Object
    //   115	1	2	localRemoteException	RemoteException
    //   119	66	2	localStringBuilder2	StringBuilder
    //   71	12	3	bool	boolean
    //   135	9	4	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   37	72	111	finally
    //   84	99	111	finally
    //   116	162	111	finally
    //   37	72	115	android/os/RemoteException
    //   84	99	115	android/os/RemoteException
  }
}
