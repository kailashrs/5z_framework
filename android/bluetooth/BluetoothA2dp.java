package android.bluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public final class BluetoothA2dp
  implements BluetoothProfile
{
  public static final String ACTION_ACTIVE_DEVICE_CHANGED = "android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED";
  public static final String ACTION_AVRCP_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.AVRCP_CONNECTION_STATE_CHANGED";
  public static final String ACTION_CODEC_CONFIG_CHANGED = "android.bluetooth.a2dp.profile.action.CODEC_CONFIG_CHANGED";
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED";
  public static final String ACTION_PLAYING_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED";
  private static final boolean DBG = true;
  public static final int OPTIONAL_CODECS_NOT_SUPPORTED = 0;
  public static final int OPTIONAL_CODECS_PREF_DISABLED = 0;
  public static final int OPTIONAL_CODECS_PREF_ENABLED = 1;
  public static final int OPTIONAL_CODECS_PREF_UNKNOWN = -1;
  public static final int OPTIONAL_CODECS_SUPPORTED = 1;
  public static final int OPTIONAL_CODECS_SUPPORT_UNKNOWN = -1;
  public static final int STATE_NOT_PLAYING = 11;
  public static final int STATE_PLAYING = 10;
  private static final String TAG = "BluetoothA2dp";
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
      //   32: ifne +90 -> 122
      //   35: aload_0
      //   36: getfield 12	android/bluetooth/BluetoothA2dp$1:this$0	Landroid/bluetooth/BluetoothA2dp;
      //   39: invokestatic 48	android/bluetooth/BluetoothA2dp:access$000	(Landroid/bluetooth/BluetoothA2dp;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   42: invokevirtual 54	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
      //   45: invokevirtual 59	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
      //   48: aload_0
      //   49: getfield 12	android/bluetooth/BluetoothA2dp$1:this$0	Landroid/bluetooth/BluetoothA2dp;
      //   52: aconst_null
      //   53: invokestatic 63	android/bluetooth/BluetoothA2dp:access$102	(Landroid/bluetooth/BluetoothA2dp;Landroid/bluetooth/IBluetoothA2dp;)Landroid/bluetooth/IBluetoothA2dp;
      //   56: pop
      //   57: aload_0
      //   58: getfield 12	android/bluetooth/BluetoothA2dp$1:this$0	Landroid/bluetooth/BluetoothA2dp;
      //   61: invokestatic 67	android/bluetooth/BluetoothA2dp:access$300	(Landroid/bluetooth/BluetoothA2dp;)Landroid/content/Context;
      //   64: aload_0
      //   65: getfield 12	android/bluetooth/BluetoothA2dp$1:this$0	Landroid/bluetooth/BluetoothA2dp;
      //   68: invokestatic 71	android/bluetooth/BluetoothA2dp:access$200	(Landroid/bluetooth/BluetoothA2dp;)Landroid/content/ServiceConnection;
      //   71: invokevirtual 77	android/content/Context:unbindService	(Landroid/content/ServiceConnection;)V
      //   74: goto +17 -> 91
      //   77: astore_2
      //   78: goto +29 -> 107
      //   81: astore_2
      //   82: ldc 34
      //   84: ldc 79
      //   86: aload_2
      //   87: invokestatic 83	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   90: pop
      //   91: aload_0
      //   92: getfield 12	android/bluetooth/BluetoothA2dp$1:this$0	Landroid/bluetooth/BluetoothA2dp;
      //   95: invokestatic 48	android/bluetooth/BluetoothA2dp:access$000	(Landroid/bluetooth/BluetoothA2dp;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   98: invokevirtual 54	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
      //   101: invokevirtual 86	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
      //   104: goto +79 -> 183
      //   107: aload_0
      //   108: getfield 12	android/bluetooth/BluetoothA2dp$1:this$0	Landroid/bluetooth/BluetoothA2dp;
      //   111: invokestatic 48	android/bluetooth/BluetoothA2dp:access$000	(Landroid/bluetooth/BluetoothA2dp;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   114: invokevirtual 54	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
      //   117: invokevirtual 86	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
      //   120: aload_2
      //   121: athrow
      //   122: aload_0
      //   123: getfield 12	android/bluetooth/BluetoothA2dp$1:this$0	Landroid/bluetooth/BluetoothA2dp;
      //   126: invokestatic 48	android/bluetooth/BluetoothA2dp:access$000	(Landroid/bluetooth/BluetoothA2dp;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   129: invokevirtual 90	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
      //   132: invokevirtual 93	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
      //   135: aload_0
      //   136: getfield 12	android/bluetooth/BluetoothA2dp$1:this$0	Landroid/bluetooth/BluetoothA2dp;
      //   139: invokestatic 97	android/bluetooth/BluetoothA2dp:access$100	(Landroid/bluetooth/BluetoothA2dp;)Landroid/bluetooth/IBluetoothA2dp;
      //   142: ifnonnull +28 -> 170
      //   145: aload_0
      //   146: getfield 12	android/bluetooth/BluetoothA2dp$1:this$0	Landroid/bluetooth/BluetoothA2dp;
      //   149: invokevirtual 101	android/bluetooth/BluetoothA2dp:doBind	()Z
      //   152: pop
      //   153: goto +17 -> 170
      //   156: astore_2
      //   157: goto +27 -> 184
      //   160: astore_2
      //   161: ldc 34
      //   163: ldc 79
      //   165: aload_2
      //   166: invokestatic 83	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   169: pop
      //   170: aload_0
      //   171: getfield 12	android/bluetooth/BluetoothA2dp$1:this$0	Landroid/bluetooth/BluetoothA2dp;
      //   174: invokestatic 48	android/bluetooth/BluetoothA2dp:access$000	(Landroid/bluetooth/BluetoothA2dp;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   177: invokevirtual 90	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
      //   180: invokevirtual 102	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
      //   183: return
      //   184: aload_0
      //   185: getfield 12	android/bluetooth/BluetoothA2dp$1:this$0	Landroid/bluetooth/BluetoothA2dp;
      //   188: invokestatic 48	android/bluetooth/BluetoothA2dp:access$000	(Landroid/bluetooth/BluetoothA2dp;)Ljava/util/concurrent/locks/ReentrantReadWriteLock;
      //   191: invokevirtual 90	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
      //   194: invokevirtual 102	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
      //   197: aload_2
      //   198: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	199	0	this	1
      //   0	199	1	paramAnonymousBoolean	boolean
      //   7	17	2	localStringBuilder	StringBuilder
      //   77	1	2	localObject1	Object
      //   81	40	2	localException1	Exception
      //   156	1	2	localObject2	Object
      //   160	38	2	localException2	Exception
      // Exception table:
      //   from	to	target	type
      //   35	74	77	finally
      //   82	91	77	finally
      //   35	74	81	java/lang/Exception
      //   122	153	156	finally
      //   161	170	156	finally
      //   122	153	160	java/lang/Exception
    }
  };
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      Log.d("BluetoothA2dp", "Proxy object connected");
      try
      {
        mServiceLock.writeLock().lock();
        BluetoothA2dp.access$102(BluetoothA2dp.this, IBluetoothA2dp.Stub.asInterface(Binder.allowBlocking(paramAnonymousIBinder)));
        mServiceLock.writeLock().unlock();
        if (mServiceListener != null) {
          mServiceListener.onServiceConnected(2, BluetoothA2dp.this);
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
      Log.d("BluetoothA2dp", "Proxy object disconnected");
      try
      {
        mServiceLock.writeLock().lock();
        BluetoothA2dp.access$102(BluetoothA2dp.this, null);
        mServiceLock.writeLock().unlock();
        if (mServiceListener != null) {
          mServiceListener.onServiceDisconnected(2);
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
  private IBluetoothA2dp mService;
  private BluetoothProfile.ServiceListener mServiceListener;
  private final ReentrantReadWriteLock mServiceLock = new ReentrantReadWriteLock();
  
  BluetoothA2dp(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
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
        Log.e("BluetoothA2dp", "", paramContext);
      }
    }
    doBind();
  }
  
  /* Error */
  private void enableDisableOptionalCodecs(BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   14: ifnull +37 -> 51
    //   17: aload_0
    //   18: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   21: ifeq +30 -> 51
    //   24: iload_2
    //   25: ifeq +16 -> 41
    //   28: aload_0
    //   29: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   32: aload_1
    //   33: invokeinterface 151 2 0
    //   38: goto +13 -> 51
    //   41: aload_0
    //   42: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   45: aload_1
    //   46: invokeinterface 154 2 0
    //   51: aload_0
    //   52: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   55: ifnonnull +11 -> 66
    //   58: ldc 45
    //   60: ldc -100
    //   62: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   65: pop
    //   66: aload_0
    //   67: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   70: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   73: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   76: return
    //   77: astore_1
    //   78: goto +24 -> 102
    //   81: astore_1
    //   82: ldc 45
    //   84: ldc -91
    //   86: aload_1
    //   87: invokestatic 112	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   90: pop
    //   91: aload_0
    //   92: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   95: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   98: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   101: return
    //   102: aload_0
    //   103: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   106: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   109: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   112: aload_1
    //   113: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	114	0	this	BluetoothA2dp
    //   0	114	1	paramBluetoothDevice	BluetoothDevice
    //   0	114	2	paramBoolean	boolean
    // Exception table:
    //   from	to	target	type
    //   0	24	77	finally
    //   28	38	77	finally
    //   41	51	77	finally
    //   51	66	77	finally
    //   82	91	77	finally
    //   0	24	81	android/os/RemoteException
    //   28	38	81	android/os/RemoteException
    //   41	51	81	android/os/RemoteException
    //   51	66	81	android/os/RemoteException
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
    Log.d("BluetoothA2dp", paramString);
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
  void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: aconst_null
    //   2: putfield 86	android/bluetooth/BluetoothA2dp:mServiceListener	Landroid/bluetooth/BluetoothProfile$ServiceListener;
    //   5: aload_0
    //   6: getfield 94	android/bluetooth/BluetoothA2dp:mAdapter	Landroid/bluetooth/BluetoothAdapter;
    //   9: invokevirtual 98	android/bluetooth/BluetoothAdapter:getBluetoothManager	()Landroid/bluetooth/IBluetoothManager;
    //   12: astore_1
    //   13: aload_1
    //   14: ifnull +26 -> 40
    //   17: aload_1
    //   18: aload_0
    //   19: getfield 79	android/bluetooth/BluetoothA2dp:mBluetoothStateChangeCallback	Landroid/bluetooth/IBluetoothStateChangeCallback;
    //   22: invokeinterface 223 2 0
    //   27: goto +13 -> 40
    //   30: astore_1
    //   31: ldc 45
    //   33: ldc 106
    //   35: aload_1
    //   36: invokestatic 112	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   39: pop
    //   40: aload_0
    //   41: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   44: invokevirtual 227	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   47: invokevirtual 230	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
    //   50: aload_0
    //   51: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   54: ifnull +36 -> 90
    //   57: aload_0
    //   58: aconst_null
    //   59: putfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   62: aload_0
    //   63: getfield 84	android/bluetooth/BluetoothA2dp:mContext	Landroid/content/Context;
    //   66: aload_0
    //   67: getfield 82	android/bluetooth/BluetoothA2dp:mConnection	Landroid/content/ServiceConnection;
    //   70: invokevirtual 236	android/content/Context:unbindService	(Landroid/content/ServiceConnection;)V
    //   73: goto +17 -> 90
    //   76: astore_1
    //   77: goto +24 -> 101
    //   80: astore_1
    //   81: ldc 45
    //   83: ldc 106
    //   85: aload_1
    //   86: invokestatic 112	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   89: pop
    //   90: aload_0
    //   91: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   94: invokevirtual 227	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   97: invokevirtual 237	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   100: return
    //   101: aload_0
    //   102: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   105: invokevirtual 227	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   108: invokevirtual 237	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   111: aload_1
    //   112: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	113	0	this	BluetoothA2dp
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
    //   0: new 190	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   7: astore_2
    //   8: aload_2
    //   9: ldc -16
    //   11: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   14: pop
    //   15: aload_2
    //   16: aload_1
    //   17: invokevirtual 243	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   20: pop
    //   21: aload_2
    //   22: ldc -11
    //   24: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: pop
    //   28: aload_2
    //   29: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   32: invokestatic 247	android/bluetooth/BluetoothA2dp:log	(Ljava/lang/String;)V
    //   35: aload_0
    //   36: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   39: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   42: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   45: aload_0
    //   46: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   49: ifnull +41 -> 90
    //   52: aload_0
    //   53: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   56: ifeq +34 -> 90
    //   59: aload_0
    //   60: aload_1
    //   61: invokespecial 249	android/bluetooth/BluetoothA2dp:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   64: ifeq +26 -> 90
    //   67: aload_0
    //   68: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   71: aload_1
    //   72: invokeinterface 251 2 0
    //   77: istore_3
    //   78: aload_0
    //   79: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   82: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   85: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   88: iload_3
    //   89: ireturn
    //   90: aload_0
    //   91: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   94: ifnonnull +11 -> 105
    //   97: ldc 45
    //   99: ldc -100
    //   101: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   104: pop
    //   105: aload_0
    //   106: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   109: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   112: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   115: iconst_0
    //   116: ireturn
    //   117: astore_1
    //   118: goto +58 -> 176
    //   121: astore_1
    //   122: new 190	java/lang/StringBuilder
    //   125: astore_2
    //   126: aload_2
    //   127: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   130: aload_2
    //   131: ldc -3
    //   133: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: pop
    //   137: new 255	java/lang/Throwable
    //   140: astore_1
    //   141: aload_1
    //   142: invokespecial 256	java/lang/Throwable:<init>	()V
    //   145: aload_2
    //   146: aload_1
    //   147: invokestatic 260	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   150: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   153: pop
    //   154: ldc 45
    //   156: aload_2
    //   157: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   160: invokestatic 262	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   163: pop
    //   164: aload_0
    //   165: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   168: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   171: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   174: iconst_0
    //   175: ireturn
    //   176: aload_0
    //   177: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   180: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   183: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   186: aload_1
    //   187: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	188	0	this	BluetoothA2dp
    //   0	188	1	paramBluetoothDevice	BluetoothDevice
    //   7	150	2	localStringBuilder	StringBuilder
    //   77	12	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   35	78	117	finally
    //   90	105	117	finally
    //   122	164	117	finally
    //   35	78	121	android/os/RemoteException
    //   90	105	121	android/os/RemoteException
  }
  
  public void disableOptionalCodecs(BluetoothDevice paramBluetoothDevice)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("disableOptionalCodecs(");
    localStringBuilder.append(paramBluetoothDevice);
    localStringBuilder.append(")");
    Log.d("BluetoothA2dp", localStringBuilder.toString());
    enableDisableOptionalCodecs(paramBluetoothDevice, false);
  }
  
  /* Error */
  public boolean disconnect(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: new 190	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   7: astore_2
    //   8: aload_2
    //   9: ldc_w 269
    //   12: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload_2
    //   17: aload_1
    //   18: invokevirtual 243	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   21: pop
    //   22: aload_2
    //   23: ldc -11
    //   25: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: pop
    //   29: aload_2
    //   30: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   33: invokestatic 247	android/bluetooth/BluetoothA2dp:log	(Ljava/lang/String;)V
    //   36: aload_0
    //   37: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   40: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   43: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   46: aload_0
    //   47: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   50: ifnull +41 -> 91
    //   53: aload_0
    //   54: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   57: ifeq +34 -> 91
    //   60: aload_0
    //   61: aload_1
    //   62: invokespecial 249	android/bluetooth/BluetoothA2dp:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   65: ifeq +26 -> 91
    //   68: aload_0
    //   69: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   72: aload_1
    //   73: invokeinterface 271 2 0
    //   78: istore_3
    //   79: aload_0
    //   80: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   83: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   86: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   89: iload_3
    //   90: ireturn
    //   91: aload_0
    //   92: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   95: ifnonnull +11 -> 106
    //   98: ldc 45
    //   100: ldc -100
    //   102: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   105: pop
    //   106: aload_0
    //   107: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   110: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   113: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   116: iconst_0
    //   117: ireturn
    //   118: astore_1
    //   119: goto +58 -> 177
    //   122: astore_1
    //   123: new 190	java/lang/StringBuilder
    //   126: astore_2
    //   127: aload_2
    //   128: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   131: aload_2
    //   132: ldc -3
    //   134: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: pop
    //   138: new 255	java/lang/Throwable
    //   141: astore_1
    //   142: aload_1
    //   143: invokespecial 256	java/lang/Throwable:<init>	()V
    //   146: aload_2
    //   147: aload_1
    //   148: invokestatic 260	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   151: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   154: pop
    //   155: ldc 45
    //   157: aload_2
    //   158: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   161: invokestatic 262	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   164: pop
    //   165: aload_0
    //   166: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   169: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   172: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   175: iconst_0
    //   176: ireturn
    //   177: aload_0
    //   178: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   181: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   184: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   187: aload_1
    //   188: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	189	0	this	BluetoothA2dp
    //   0	189	1	paramBluetoothDevice	BluetoothDevice
    //   7	151	2	localStringBuilder	StringBuilder
    //   78	12	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   36	79	118	finally
    //   91	106	118	finally
    //   123	165	118	finally
    //   36	79	122	android/os/RemoteException
    //   91	106	122	android/os/RemoteException
  }
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothA2dp.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, mContext.getUser()))) {
      return true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth A2DP Service with ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothA2dp", ((StringBuilder)localObject).toString());
    return false;
  }
  
  public void enableOptionalCodecs(BluetoothDevice paramBluetoothDevice)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("enableOptionalCodecs(");
    localStringBuilder.append(paramBluetoothDevice);
    localStringBuilder.append(")");
    Log.d("BluetoothA2dp", localStringBuilder.toString());
    enableDisableOptionalCodecs(paramBluetoothDevice, true);
  }
  
  public void finalize() {}
  
  /* Error */
  public BluetoothDevice getActiveDevice()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   14: ifnull +32 -> 46
    //   17: aload_0
    //   18: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   21: ifeq +25 -> 46
    //   24: aload_0
    //   25: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   28: invokeinterface 309 1 0
    //   33: astore_1
    //   34: aload_0
    //   35: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   38: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   41: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   44: aload_1
    //   45: areturn
    //   46: aload_0
    //   47: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   50: ifnonnull +11 -> 61
    //   53: ldc 45
    //   55: ldc -100
    //   57: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   60: pop
    //   61: aload_0
    //   62: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   65: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   68: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   71: aconst_null
    //   72: areturn
    //   73: astore_1
    //   74: goto +58 -> 132
    //   77: astore_1
    //   78: new 190	java/lang/StringBuilder
    //   81: astore_2
    //   82: aload_2
    //   83: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   86: aload_2
    //   87: ldc -3
    //   89: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: pop
    //   93: new 255	java/lang/Throwable
    //   96: astore_1
    //   97: aload_1
    //   98: invokespecial 256	java/lang/Throwable:<init>	()V
    //   101: aload_2
    //   102: aload_1
    //   103: invokestatic 260	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   106: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: pop
    //   110: ldc 45
    //   112: aload_2
    //   113: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   116: invokestatic 262	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   119: pop
    //   120: aload_0
    //   121: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   124: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   127: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   130: aconst_null
    //   131: areturn
    //   132: aload_0
    //   133: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   136: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   139: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   142: aload_1
    //   143: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	144	0	this	BluetoothA2dp
    //   33	12	1	localBluetoothDevice	BluetoothDevice
    //   73	1	1	localObject	Object
    //   77	1	1	localRemoteException	RemoteException
    //   96	47	1	localThrowable	Throwable
    //   81	32	2	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   0	34	73	finally
    //   46	61	73	finally
    //   78	120	73	finally
    //   0	34	77	android/os/RemoteException
    //   46	61	77	android/os/RemoteException
  }
  
  /* Error */
  public BluetoothCodecStatus getCodecStatus(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: new 190	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   7: astore_2
    //   8: aload_2
    //   9: ldc_w 313
    //   12: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload_2
    //   17: aload_1
    //   18: invokevirtual 243	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   21: pop
    //   22: aload_2
    //   23: ldc -11
    //   25: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: pop
    //   29: ldc 45
    //   31: aload_2
    //   32: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   35: invokestatic 186	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   38: pop
    //   39: aload_0
    //   40: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   43: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   46: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   49: aload_0
    //   50: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   53: ifnull +33 -> 86
    //   56: aload_0
    //   57: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   60: ifeq +26 -> 86
    //   63: aload_0
    //   64: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   67: aload_1
    //   68: invokeinterface 315 2 0
    //   73: astore_1
    //   74: aload_0
    //   75: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   78: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   81: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   84: aload_1
    //   85: areturn
    //   86: aload_0
    //   87: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   90: ifnonnull +11 -> 101
    //   93: ldc 45
    //   95: ldc -100
    //   97: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   100: pop
    //   101: aload_0
    //   102: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   105: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   108: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   111: aconst_null
    //   112: areturn
    //   113: astore_1
    //   114: goto +26 -> 140
    //   117: astore_1
    //   118: ldc 45
    //   120: ldc_w 317
    //   123: aload_1
    //   124: invokestatic 112	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   127: pop
    //   128: aload_0
    //   129: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   132: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   135: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   138: aconst_null
    //   139: areturn
    //   140: aload_0
    //   141: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   144: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   147: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   150: aload_1
    //   151: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	152	0	this	BluetoothA2dp
    //   0	152	1	paramBluetoothDevice	BluetoothDevice
    //   7	25	2	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   39	74	113	finally
    //   86	101	113	finally
    //   118	128	113	finally
    //   39	74	117	android/os/RemoteException
    //   86	101	117	android/os/RemoteException
  }
  
  /* Error */
  public java.util.List<BluetoothDevice> getConnectedDevices()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   14: ifnull +32 -> 46
    //   17: aload_0
    //   18: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   21: ifeq +25 -> 46
    //   24: aload_0
    //   25: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   28: invokeinterface 321 1 0
    //   33: astore_1
    //   34: aload_0
    //   35: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   38: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   41: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   44: aload_1
    //   45: areturn
    //   46: aload_0
    //   47: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   50: ifnonnull +11 -> 61
    //   53: ldc 45
    //   55: ldc -100
    //   57: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   60: pop
    //   61: new 323	java/util/ArrayList
    //   64: dup
    //   65: invokespecial 324	java/util/ArrayList:<init>	()V
    //   68: astore_1
    //   69: aload_0
    //   70: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   73: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   76: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   79: aload_1
    //   80: areturn
    //   81: astore_1
    //   82: goto +66 -> 148
    //   85: astore_1
    //   86: new 190	java/lang/StringBuilder
    //   89: astore_2
    //   90: aload_2
    //   91: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   94: aload_2
    //   95: ldc -3
    //   97: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: new 255	java/lang/Throwable
    //   104: astore_1
    //   105: aload_1
    //   106: invokespecial 256	java/lang/Throwable:<init>	()V
    //   109: aload_2
    //   110: aload_1
    //   111: invokestatic 260	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   114: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: pop
    //   118: ldc 45
    //   120: aload_2
    //   121: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   124: invokestatic 262	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   127: pop
    //   128: new 323	java/util/ArrayList
    //   131: dup
    //   132: invokespecial 324	java/util/ArrayList:<init>	()V
    //   135: astore_1
    //   136: aload_0
    //   137: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   140: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   143: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   146: aload_1
    //   147: areturn
    //   148: aload_0
    //   149: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   152: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   155: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   158: aload_1
    //   159: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	160	0	this	BluetoothA2dp
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
    //   1: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   14: ifnull +41 -> 55
    //   17: aload_0
    //   18: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   21: ifeq +34 -> 55
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 249	android/bluetooth/BluetoothA2dp:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   29: ifeq +26 -> 55
    //   32: aload_0
    //   33: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   36: aload_1
    //   37: invokeinterface 330 2 0
    //   42: istore_2
    //   43: aload_0
    //   44: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   47: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   50: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   53: iload_2
    //   54: ireturn
    //   55: aload_0
    //   56: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   59: ifnonnull +11 -> 70
    //   62: ldc 45
    //   64: ldc -100
    //   66: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_0
    //   81: ireturn
    //   82: astore_1
    //   83: goto +58 -> 141
    //   86: astore_1
    //   87: new 190	java/lang/StringBuilder
    //   90: astore_1
    //   91: aload_1
    //   92: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   95: aload_1
    //   96: ldc -3
    //   98: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: new 255	java/lang/Throwable
    //   105: astore_3
    //   106: aload_3
    //   107: invokespecial 256	java/lang/Throwable:<init>	()V
    //   110: aload_1
    //   111: aload_3
    //   112: invokestatic 260	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 45
    //   121: aload_1
    //   122: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 262	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: aload_0
    //   130: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   133: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   136: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   139: iconst_0
    //   140: ireturn
    //   141: aload_0
    //   142: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   145: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   148: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   151: aload_1
    //   152: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	this	BluetoothA2dp
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
    //   1: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   14: ifnull +33 -> 47
    //   17: aload_0
    //   18: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   21: ifeq +26 -> 47
    //   24: aload_0
    //   25: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   28: aload_1
    //   29: invokeinterface 334 2 0
    //   34: astore_1
    //   35: aload_0
    //   36: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   39: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   42: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   45: aload_1
    //   46: areturn
    //   47: aload_0
    //   48: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   51: ifnonnull +11 -> 62
    //   54: ldc 45
    //   56: ldc -100
    //   58: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   61: pop
    //   62: new 323	java/util/ArrayList
    //   65: dup
    //   66: invokespecial 324	java/util/ArrayList:<init>	()V
    //   69: astore_1
    //   70: aload_0
    //   71: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: aload_1
    //   81: areturn
    //   82: astore_1
    //   83: goto +66 -> 149
    //   86: astore_1
    //   87: new 190	java/lang/StringBuilder
    //   90: astore_2
    //   91: aload_2
    //   92: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   95: aload_2
    //   96: ldc -3
    //   98: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: new 255	java/lang/Throwable
    //   105: astore_1
    //   106: aload_1
    //   107: invokespecial 256	java/lang/Throwable:<init>	()V
    //   110: aload_2
    //   111: aload_1
    //   112: invokestatic 260	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 45
    //   121: aload_2
    //   122: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 262	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: new 323	java/util/ArrayList
    //   132: dup
    //   133: invokespecial 324	java/util/ArrayList:<init>	()V
    //   136: astore_1
    //   137: aload_0
    //   138: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   141: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   144: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   147: aload_1
    //   148: areturn
    //   149: aload_0
    //   150: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   153: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   156: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   159: aload_1
    //   160: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	161	0	this	BluetoothA2dp
    //   0	161	1	paramArrayOfInt	int[]
    //   90	32	2	localStringBuilder	StringBuilder
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
  public int getOptionalCodecsEnabled(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   14: ifnull +41 -> 55
    //   17: aload_0
    //   18: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   21: ifeq +34 -> 55
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 249	android/bluetooth/BluetoothA2dp:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   29: ifeq +26 -> 55
    //   32: aload_0
    //   33: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   36: aload_1
    //   37: invokeinterface 338 2 0
    //   42: istore_2
    //   43: aload_0
    //   44: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   47: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   50: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   53: iload_2
    //   54: ireturn
    //   55: aload_0
    //   56: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   59: ifnonnull +11 -> 70
    //   62: ldc 45
    //   64: ldc -100
    //   66: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_m1
    //   81: ireturn
    //   82: astore_1
    //   83: goto +26 -> 109
    //   86: astore_1
    //   87: ldc 45
    //   89: ldc_w 340
    //   92: aload_1
    //   93: invokestatic 112	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   96: pop
    //   97: aload_0
    //   98: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   101: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   104: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   107: iconst_m1
    //   108: ireturn
    //   109: aload_0
    //   110: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   113: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   116: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   119: aload_1
    //   120: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	121	0	this	BluetoothA2dp
    //   0	121	1	paramBluetoothDevice	BluetoothDevice
    //   42	12	2	i	int
    // Exception table:
    //   from	to	target	type
    //   0	43	82	finally
    //   55	70	82	finally
    //   87	97	82	finally
    //   0	43	86	android/os/RemoteException
    //   55	70	86	android/os/RemoteException
  }
  
  /* Error */
  public int getPriority(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   14: ifnull +41 -> 55
    //   17: aload_0
    //   18: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   21: ifeq +34 -> 55
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 249	android/bluetooth/BluetoothA2dp:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   29: ifeq +26 -> 55
    //   32: aload_0
    //   33: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   36: aload_1
    //   37: invokeinterface 343 2 0
    //   42: istore_2
    //   43: aload_0
    //   44: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   47: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   50: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   53: iload_2
    //   54: ireturn
    //   55: aload_0
    //   56: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   59: ifnonnull +11 -> 70
    //   62: ldc 45
    //   64: ldc -100
    //   66: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_0
    //   81: ireturn
    //   82: astore_1
    //   83: goto +58 -> 141
    //   86: astore_1
    //   87: new 190	java/lang/StringBuilder
    //   90: astore_1
    //   91: aload_1
    //   92: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   95: aload_1
    //   96: ldc -3
    //   98: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: new 255	java/lang/Throwable
    //   105: astore_3
    //   106: aload_3
    //   107: invokespecial 256	java/lang/Throwable:<init>	()V
    //   110: aload_1
    //   111: aload_3
    //   112: invokestatic 260	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 45
    //   121: aload_1
    //   122: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 262	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: aload_0
    //   130: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   133: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   136: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   139: iconst_0
    //   140: ireturn
    //   141: aload_0
    //   142: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   145: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   148: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   151: aload_1
    //   152: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	this	BluetoothA2dp
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
  public boolean isA2dpPlaying(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   14: ifnull +41 -> 55
    //   17: aload_0
    //   18: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   21: ifeq +34 -> 55
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 249	android/bluetooth/BluetoothA2dp:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   29: ifeq +26 -> 55
    //   32: aload_0
    //   33: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   36: aload_1
    //   37: invokeinterface 346 2 0
    //   42: istore_2
    //   43: aload_0
    //   44: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   47: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   50: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   53: iload_2
    //   54: ireturn
    //   55: aload_0
    //   56: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   59: ifnonnull +11 -> 70
    //   62: ldc 45
    //   64: ldc -100
    //   66: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_0
    //   81: ireturn
    //   82: astore_1
    //   83: goto +58 -> 141
    //   86: astore_1
    //   87: new 190	java/lang/StringBuilder
    //   90: astore_1
    //   91: aload_1
    //   92: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   95: aload_1
    //   96: ldc -3
    //   98: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: new 255	java/lang/Throwable
    //   105: astore_3
    //   106: aload_3
    //   107: invokespecial 256	java/lang/Throwable:<init>	()V
    //   110: aload_1
    //   111: aload_3
    //   112: invokestatic 260	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   115: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: ldc 45
    //   121: aload_1
    //   122: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokestatic 262	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: pop
    //   129: aload_0
    //   130: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   133: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   136: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   139: iconst_0
    //   140: ireturn
    //   141: aload_0
    //   142: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   145: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   148: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   151: aload_1
    //   152: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	this	BluetoothA2dp
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
  public boolean isAvrcpAbsoluteVolumeSupported()
  {
    // Byte code:
    //   0: ldc 45
    //   2: ldc_w 348
    //   5: invokestatic 186	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   8: pop
    //   9: aload_0
    //   10: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   13: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   16: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   19: aload_0
    //   20: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   23: ifnull +32 -> 55
    //   26: aload_0
    //   27: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   30: ifeq +25 -> 55
    //   33: aload_0
    //   34: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   37: invokeinterface 350 1 0
    //   42: istore_1
    //   43: aload_0
    //   44: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   47: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   50: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   53: iload_1
    //   54: ireturn
    //   55: aload_0
    //   56: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   59: ifnonnull +11 -> 70
    //   62: ldc 45
    //   64: ldc -100
    //   66: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_0
    //   81: ireturn
    //   82: astore_2
    //   83: goto +26 -> 109
    //   86: astore_2
    //   87: ldc 45
    //   89: ldc_w 352
    //   92: aload_2
    //   93: invokestatic 112	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   96: pop
    //   97: aload_0
    //   98: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   101: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   104: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   107: iconst_0
    //   108: ireturn
    //   109: aload_0
    //   110: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   113: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   116: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   119: aload_2
    //   120: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	121	0	this	BluetoothA2dp
    //   42	12	1	bool	boolean
    //   82	1	2	localObject	Object
    //   86	34	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   9	43	82	finally
    //   55	70	82	finally
    //   87	97	82	finally
    //   9	43	86	android/os/RemoteException
    //   55	70	86	android/os/RemoteException
  }
  
  /* Error */
  public boolean setActiveDevice(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: new 190	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   7: astore_2
    //   8: aload_2
    //   9: ldc_w 355
    //   12: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload_2
    //   17: aload_1
    //   18: invokevirtual 243	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   21: pop
    //   22: aload_2
    //   23: ldc -11
    //   25: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: pop
    //   29: aload_2
    //   30: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   33: invokestatic 247	android/bluetooth/BluetoothA2dp:log	(Ljava/lang/String;)V
    //   36: aload_0
    //   37: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   40: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   43: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   46: aload_0
    //   47: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   50: ifnull +45 -> 95
    //   53: aload_0
    //   54: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   57: ifeq +38 -> 95
    //   60: aload_1
    //   61: ifnull +11 -> 72
    //   64: aload_0
    //   65: aload_1
    //   66: invokespecial 249	android/bluetooth/BluetoothA2dp:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   69: ifeq +26 -> 95
    //   72: aload_0
    //   73: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   76: aload_1
    //   77: invokeinterface 357 2 0
    //   82: istore_3
    //   83: aload_0
    //   84: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   87: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   90: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   93: iload_3
    //   94: ireturn
    //   95: aload_0
    //   96: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   99: ifnonnull +11 -> 110
    //   102: ldc 45
    //   104: ldc -100
    //   106: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   109: pop
    //   110: aload_0
    //   111: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   114: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   117: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   120: iconst_0
    //   121: ireturn
    //   122: astore_1
    //   123: goto +58 -> 181
    //   126: astore_1
    //   127: new 190	java/lang/StringBuilder
    //   130: astore_2
    //   131: aload_2
    //   132: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   135: aload_2
    //   136: ldc -3
    //   138: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: pop
    //   142: new 255	java/lang/Throwable
    //   145: astore_1
    //   146: aload_1
    //   147: invokespecial 256	java/lang/Throwable:<init>	()V
    //   150: aload_2
    //   151: aload_1
    //   152: invokestatic 260	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   155: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   158: pop
    //   159: ldc 45
    //   161: aload_2
    //   162: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   165: invokestatic 262	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   168: pop
    //   169: aload_0
    //   170: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   173: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   176: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   179: iconst_0
    //   180: ireturn
    //   181: aload_0
    //   182: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   185: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   188: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   191: aload_1
    //   192: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	193	0	this	BluetoothA2dp
    //   0	193	1	paramBluetoothDevice	BluetoothDevice
    //   7	155	2	localStringBuilder	StringBuilder
    //   82	12	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   36	60	122	finally
    //   64	72	122	finally
    //   72	83	122	finally
    //   95	110	122	finally
    //   127	169	122	finally
    //   36	60	126	android/os/RemoteException
    //   64	72	126	android/os/RemoteException
    //   72	83	126	android/os/RemoteException
    //   95	110	126	android/os/RemoteException
  }
  
  /* Error */
  public void setAvrcpAbsoluteVolume(int paramInt)
  {
    // Byte code:
    //   0: ldc 45
    //   2: ldc_w 360
    //   5: invokestatic 186	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   8: pop
    //   9: aload_0
    //   10: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   13: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   16: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   19: aload_0
    //   20: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   23: ifnull +20 -> 43
    //   26: aload_0
    //   27: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   30: ifeq +13 -> 43
    //   33: aload_0
    //   34: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   37: iload_1
    //   38: invokeinterface 362 2 0
    //   43: aload_0
    //   44: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   47: ifnonnull +29 -> 76
    //   50: ldc 45
    //   52: ldc -100
    //   54: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   57: pop
    //   58: goto +18 -> 76
    //   61: astore_2
    //   62: goto +25 -> 87
    //   65: astore_2
    //   66: ldc 45
    //   68: ldc_w 364
    //   71: aload_2
    //   72: invokestatic 112	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   75: pop
    //   76: aload_0
    //   77: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   80: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   83: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   86: return
    //   87: aload_0
    //   88: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   91: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   94: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   97: aload_2
    //   98: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	99	0	this	BluetoothA2dp
    //   0	99	1	paramInt	int
    //   61	1	2	localObject	Object
    //   65	33	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   9	43	61	finally
    //   43	58	61	finally
    //   66	76	61	finally
    //   9	43	65	android/os/RemoteException
    //   43	58	65	android/os/RemoteException
  }
  
  /* Error */
  public void setCodecConfigPreference(BluetoothDevice paramBluetoothDevice, BluetoothCodecConfig paramBluetoothCodecConfig)
  {
    // Byte code:
    //   0: new 190	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   7: astore_3
    //   8: aload_3
    //   9: ldc_w 368
    //   12: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload_3
    //   17: aload_1
    //   18: invokevirtual 243	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   21: pop
    //   22: aload_3
    //   23: ldc -11
    //   25: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: pop
    //   29: ldc 45
    //   31: aload_3
    //   32: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   35: invokestatic 186	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   38: pop
    //   39: aload_0
    //   40: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   43: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   46: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   49: aload_0
    //   50: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   53: ifnull +21 -> 74
    //   56: aload_0
    //   57: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   60: ifeq +14 -> 74
    //   63: aload_0
    //   64: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   67: aload_1
    //   68: aload_2
    //   69: invokeinterface 370 3 0
    //   74: aload_0
    //   75: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   78: ifnonnull +11 -> 89
    //   81: ldc 45
    //   83: ldc -100
    //   85: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   88: pop
    //   89: aload_0
    //   90: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   93: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   96: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   99: return
    //   100: astore_1
    //   101: goto +25 -> 126
    //   104: astore_1
    //   105: ldc 45
    //   107: ldc_w 372
    //   110: aload_1
    //   111: invokestatic 112	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   114: pop
    //   115: aload_0
    //   116: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   119: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   122: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   125: return
    //   126: aload_0
    //   127: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   130: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   133: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   136: aload_1
    //   137: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	138	0	this	BluetoothA2dp
    //   0	138	1	paramBluetoothDevice	BluetoothDevice
    //   0	138	2	paramBluetoothCodecConfig	BluetoothCodecConfig
    //   7	25	3	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   39	74	100	finally
    //   74	89	100	finally
    //   105	115	100	finally
    //   39	74	104	android/os/RemoteException
    //   74	89	104	android/os/RemoteException
  }
  
  /* Error */
  public void setOptionalCodecsEnabled(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    // Byte code:
    //   0: iload_2
    //   1: iconst_m1
    //   2: if_icmpeq +55 -> 57
    //   5: iload_2
    //   6: ifeq +51 -> 57
    //   9: iload_2
    //   10: iconst_1
    //   11: if_icmpeq +46 -> 57
    //   14: new 190	java/lang/StringBuilder
    //   17: astore_1
    //   18: aload_1
    //   19: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   22: aload_1
    //   23: ldc_w 376
    //   26: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: pop
    //   30: aload_1
    //   31: iload_2
    //   32: invokevirtual 200	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   35: pop
    //   36: ldc 45
    //   38: aload_1
    //   39: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   42: invokestatic 262	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   45: pop
    //   46: aload_0
    //   47: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   50: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   53: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   56: return
    //   57: aload_0
    //   58: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   61: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   64: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   67: aload_0
    //   68: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   71: ifnull +29 -> 100
    //   74: aload_0
    //   75: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   78: ifeq +22 -> 100
    //   81: aload_0
    //   82: aload_1
    //   83: invokespecial 249	android/bluetooth/BluetoothA2dp:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   86: ifeq +14 -> 100
    //   89: aload_0
    //   90: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   93: aload_1
    //   94: iload_2
    //   95: invokeinterface 378 3 0
    //   100: aload_0
    //   101: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   104: ifnonnull +11 -> 115
    //   107: ldc 45
    //   109: ldc -100
    //   111: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   114: pop
    //   115: aload_0
    //   116: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   119: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   122: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   125: return
    //   126: astore_1
    //   127: goto +57 -> 184
    //   130: astore_1
    //   131: new 190	java/lang/StringBuilder
    //   134: astore_3
    //   135: aload_3
    //   136: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   139: aload_3
    //   140: ldc -3
    //   142: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   145: pop
    //   146: new 255	java/lang/Throwable
    //   149: astore_1
    //   150: aload_1
    //   151: invokespecial 256	java/lang/Throwable:<init>	()V
    //   154: aload_3
    //   155: aload_1
    //   156: invokestatic 260	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   159: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   162: pop
    //   163: ldc 45
    //   165: aload_3
    //   166: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   169: invokestatic 262	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   172: pop
    //   173: aload_0
    //   174: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   177: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   180: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   183: return
    //   184: aload_0
    //   185: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   188: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   191: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   194: aload_1
    //   195: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	196	0	this	BluetoothA2dp
    //   0	196	1	paramBluetoothDevice	BluetoothDevice
    //   0	196	2	paramInt	int
    //   134	32	3	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   14	46	126	finally
    //   57	100	126	finally
    //   100	115	126	finally
    //   131	173	126	finally
    //   14	46	130	android/os/RemoteException
    //   57	100	130	android/os/RemoteException
    //   100	115	130	android/os/RemoteException
  }
  
  /* Error */
  public boolean setPriority(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    // Byte code:
    //   0: new 190	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   7: astore_3
    //   8: aload_3
    //   9: ldc_w 382
    //   12: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload_3
    //   17: aload_1
    //   18: invokevirtual 243	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   21: pop
    //   22: aload_3
    //   23: ldc_w 384
    //   26: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: pop
    //   30: aload_3
    //   31: iload_2
    //   32: invokevirtual 200	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   35: pop
    //   36: aload_3
    //   37: ldc -11
    //   39: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   42: pop
    //   43: aload_3
    //   44: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   47: invokestatic 247	android/bluetooth/BluetoothA2dp:log	(Ljava/lang/String;)V
    //   50: aload_0
    //   51: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   54: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   57: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   60: aload_0
    //   61: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   64: ifnull +70 -> 134
    //   67: aload_0
    //   68: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   71: ifeq +63 -> 134
    //   74: aload_0
    //   75: aload_1
    //   76: invokespecial 249	android/bluetooth/BluetoothA2dp:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   79: istore 4
    //   81: iload 4
    //   83: ifeq +51 -> 134
    //   86: iload_2
    //   87: ifeq +21 -> 108
    //   90: iload_2
    //   91: bipush 100
    //   93: if_icmpeq +15 -> 108
    //   96: aload_0
    //   97: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   100: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   103: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   106: iconst_0
    //   107: ireturn
    //   108: aload_0
    //   109: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   112: aload_1
    //   113: iload_2
    //   114: invokeinterface 386 3 0
    //   119: istore 4
    //   121: aload_0
    //   122: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   125: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   128: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   131: iload 4
    //   133: ireturn
    //   134: aload_0
    //   135: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   138: ifnonnull +11 -> 149
    //   141: ldc 45
    //   143: ldc -100
    //   145: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   148: pop
    //   149: aload_0
    //   150: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   153: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   156: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   159: iconst_0
    //   160: ireturn
    //   161: astore_1
    //   162: goto +58 -> 220
    //   165: astore_1
    //   166: new 190	java/lang/StringBuilder
    //   169: astore_3
    //   170: aload_3
    //   171: invokespecial 191	java/lang/StringBuilder:<init>	()V
    //   174: aload_3
    //   175: ldc -3
    //   177: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: pop
    //   181: new 255	java/lang/Throwable
    //   184: astore_1
    //   185: aload_1
    //   186: invokespecial 256	java/lang/Throwable:<init>	()V
    //   189: aload_3
    //   190: aload_1
    //   191: invokestatic 260	android/util/Log:getStackTraceString	(Ljava/lang/Throwable;)Ljava/lang/String;
    //   194: invokevirtual 197	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   197: pop
    //   198: ldc 45
    //   200: aload_3
    //   201: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   204: invokestatic 262	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   207: pop
    //   208: aload_0
    //   209: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   212: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   215: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   218: iconst_0
    //   219: ireturn
    //   220: aload_0
    //   221: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   224: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   227: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   230: aload_1
    //   231: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	232	0	this	BluetoothA2dp
    //   0	232	1	paramBluetoothDevice	BluetoothDevice
    //   0	232	2	paramInt	int
    //   7	194	3	localStringBuilder	StringBuilder
    //   79	53	4	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   50	81	161	finally
    //   108	121	161	finally
    //   134	149	161	finally
    //   166	208	161	finally
    //   50	81	165	android/os/RemoteException
    //   108	121	165	android/os/RemoteException
    //   134	149	165	android/os/RemoteException
  }
  
  public boolean shouldSendVolumeKeys(BluetoothDevice paramBluetoothDevice)
  {
    if ((isEnabled()) && (isValidDevice(paramBluetoothDevice)))
    {
      paramBluetoothDevice = paramBluetoothDevice.getUuids();
      if (paramBluetoothDevice == null) {
        return false;
      }
      int i = paramBluetoothDevice.length;
      for (int j = 0; j < i; j++) {
        if (BluetoothUuid.isAvrcpTarget(paramBluetoothDevice[j])) {
          return true;
        }
      }
    }
    return false;
  }
  
  /* Error */
  public int supportsOptionalCodecs(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 142	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: aload_0
    //   11: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   14: ifnull +41 -> 55
    //   17: aload_0
    //   18: invokespecial 145	android/bluetooth/BluetoothA2dp:isEnabled	()Z
    //   21: ifeq +34 -> 55
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 249	android/bluetooth/BluetoothA2dp:isValidDevice	(Landroid/bluetooth/BluetoothDevice;)Z
    //   29: ifeq +26 -> 55
    //   32: aload_0
    //   33: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   36: aload_1
    //   37: invokeinterface 400 2 0
    //   42: istore_2
    //   43: aload_0
    //   44: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   47: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   50: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   53: iload_2
    //   54: ireturn
    //   55: aload_0
    //   56: getfield 123	android/bluetooth/BluetoothA2dp:mService	Landroid/bluetooth/IBluetoothA2dp;
    //   59: ifnonnull +11 -> 70
    //   62: ldc 45
    //   64: ldc -100
    //   66: invokestatic 160	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   77: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   80: iconst_m1
    //   81: ireturn
    //   82: astore_1
    //   83: goto +26 -> 109
    //   86: astore_1
    //   87: ldc 45
    //   89: ldc_w 340
    //   92: aload_1
    //   93: invokestatic 112	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   96: pop
    //   97: aload_0
    //   98: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   101: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   104: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   107: iconst_m1
    //   108: ireturn
    //   109: aload_0
    //   110: getfield 74	android/bluetooth/BluetoothA2dp:mServiceLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   113: invokevirtual 137	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   116: invokevirtual 163	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   119: aload_1
    //   120: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	121	0	this	BluetoothA2dp
    //   0	121	1	paramBluetoothDevice	BluetoothDevice
    //   42	12	2	i	int
    // Exception table:
    //   from	to	target	type
    //   0	43	82	finally
    //   55	70	82	finally
    //   87	97	82	finally
    //   0	43	86	android/os/RemoteException
    //   55	70	86	android/os/RemoteException
  }
}
