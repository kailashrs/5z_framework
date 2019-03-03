package android.bluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothA2dpSink
  implements BluetoothProfile
{
  public static final String ACTION_AUDIO_CONFIG_CHANGED = "android.bluetooth.a2dp-sink.profile.action.AUDIO_CONFIG_CHANGED";
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp-sink.profile.action.CONNECTION_STATE_CHANGED";
  public static final String ACTION_PLAYING_STATE_CHANGED = "android.bluetooth.a2dp-sink.profile.action.PLAYING_STATE_CHANGED";
  private static final boolean DBG = true;
  public static final String EXTRA_AUDIO_CONFIG = "android.bluetooth.a2dp-sink.profile.extra.AUDIO_CONFIG";
  public static final int STATE_NOT_PLAYING = 11;
  public static final int STATE_PLAYING = 10;
  private static final String TAG = "BluetoothA2dpSink";
  private static final boolean VDBG = false;
  private BluetoothAdapter mAdapter;
  private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("onBluetoothStateChange: up=");
      ((StringBuilder)???).append(paramAnonymousBoolean);
      Log.d("BluetoothA2dpSink", ((StringBuilder)???).toString());
      if (!paramAnonymousBoolean)
      {
        try
        {
          synchronized (mConnection)
          {
            BluetoothA2dpSink.access$102(BluetoothA2dpSink.this, null);
            mContext.unbindService(mConnection);
          }
        }
        catch (Exception localException1)
        {
          Log.e("BluetoothA2dpSink", "", localException1);
        }
        throw localException1;
      }
      else
      {
        try
        {
          synchronized (mConnection)
          {
            if (mService == null) {
              doBind();
            }
          }
          return;
        }
        catch (Exception localException2)
        {
          Log.e("BluetoothA2dpSink", "", localException2);
        }
      }
    }
  };
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      Log.d("BluetoothA2dpSink", "Proxy object connected");
      BluetoothA2dpSink.access$102(BluetoothA2dpSink.this, IBluetoothA2dpSink.Stub.asInterface(Binder.allowBlocking(paramAnonymousIBinder)));
      if (mServiceListener != null) {
        mServiceListener.onServiceConnected(11, BluetoothA2dpSink.this);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      Log.d("BluetoothA2dpSink", "Proxy object disconnected");
      BluetoothA2dpSink.access$102(BluetoothA2dpSink.this, null);
      if (mServiceListener != null) {
        mServiceListener.onServiceDisconnected(11);
      }
    }
  };
  private Context mContext;
  private volatile IBluetoothA2dpSink mService;
  private BluetoothProfile.ServiceListener mServiceListener;
  
  BluetoothA2dpSink(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
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
        Log.e("BluetoothA2dpSink", "", paramContext);
      }
    }
    doBind();
  }
  
  private boolean isEnabled()
  {
    boolean bool;
    if (mAdapter.getState() == 12) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isValidDevice(BluetoothDevice paramBluetoothDevice)
  {
    boolean bool;
    if ((paramBluetoothDevice != null) && (BluetoothAdapter.checkBluetoothAddress(paramBluetoothDevice.getAddress()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static void log(String paramString)
  {
    Log.d("BluetoothA2dpSink", paramString);
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
  
  void close()
  {
    mServiceListener = null;
    IBluetoothManager localIBluetoothManager = mAdapter.getBluetoothManager();
    if (localIBluetoothManager != null) {
      try
      {
        localIBluetoothManager.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
      }
      catch (Exception localException1)
      {
        Log.e("BluetoothA2dpSink", "", localException1);
      }
    }
    synchronized (mConnection)
    {
      IBluetoothA2dpSink localIBluetoothA2dpSink = mService;
      if (localIBluetoothA2dpSink != null) {
        try
        {
          mService = null;
          mContext.unbindService(mConnection);
        }
        catch (Exception localException2)
        {
          Log.e("BluetoothA2dpSink", "", localException2);
        }
      }
      return;
    }
  }
  
  public boolean connect(BluetoothDevice paramBluetoothDevice)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("connect(");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothA2dpSink)localObject).connect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothA2dpSink", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothA2dpSink", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean disconnect(BluetoothDevice paramBluetoothDevice)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("disconnect(");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = ((IBluetoothA2dpSink)localObject).disconnect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothA2dpSink", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothA2dpSink", "Proxy not attached to service");
    }
    return false;
  }
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothA2dpSink.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, mContext.getUser()))) {
      return true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth A2DP Service with ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothA2dpSink", ((StringBuilder)localObject).toString());
    return false;
  }
  
  public void finalize()
  {
    close();
  }
  
  public BluetoothAudioConfig getAudioConfig(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothA2dpSink localIBluetoothA2dpSink = mService;
    if ((localIBluetoothA2dpSink != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        paramBluetoothDevice = localIBluetoothA2dpSink.getAudioConfig(paramBluetoothDevice);
        return paramBluetoothDevice;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothA2dpSink", paramBluetoothDevice.toString());
        return null;
      }
    }
    if (localIBluetoothA2dpSink == null) {
      Log.w("BluetoothA2dpSink", "Proxy not attached to service");
    }
    return null;
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    Object localObject = mService;
    StringBuilder localStringBuilder;
    if ((localObject != null) && (isEnabled())) {
      try
      {
        localObject = ((IBluetoothA2dpSink)localObject).getConnectedDevices();
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Stack:");
        localStringBuilder.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothA2dpSink", localStringBuilder.toString());
        return new ArrayList();
      }
    }
    if (localStringBuilder == null) {
      Log.w("BluetoothA2dpSink", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothA2dpSink localIBluetoothA2dpSink = mService;
    if ((localIBluetoothA2dpSink != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = localIBluetoothA2dpSink.getConnectionState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothA2dpSink", paramBluetoothDevice.toString());
        return 0;
      }
    }
    if (localIBluetoothA2dpSink == null) {
      Log.w("BluetoothA2dpSink", "Proxy not attached to service");
    }
    return 0;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    IBluetoothA2dpSink localIBluetoothA2dpSink = mService;
    if ((localIBluetoothA2dpSink != null) && (isEnabled())) {
      try
      {
        paramArrayOfInt = localIBluetoothA2dpSink.getDevicesMatchingConnectionStates(paramArrayOfInt);
        return paramArrayOfInt;
      }
      catch (RemoteException paramArrayOfInt)
      {
        paramArrayOfInt = new StringBuilder();
        paramArrayOfInt.append("Stack:");
        paramArrayOfInt.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothA2dpSink", paramArrayOfInt.toString());
        return new ArrayList();
      }
    }
    if (localIBluetoothA2dpSink == null) {
      Log.w("BluetoothA2dpSink", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getPriority(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothA2dpSink localIBluetoothA2dpSink = mService;
    if ((localIBluetoothA2dpSink != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = localIBluetoothA2dpSink.getPriority(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothA2dpSink", paramBluetoothDevice.toString());
        return 0;
      }
    }
    if (localIBluetoothA2dpSink == null) {
      Log.w("BluetoothA2dpSink", "Proxy not attached to service");
    }
    return 0;
  }
  
  public boolean isA2dpPlaying(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothA2dpSink localIBluetoothA2dpSink = mService;
    if ((localIBluetoothA2dpSink != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothA2dpSink.isA2dpPlaying(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothA2dpSink", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localIBluetoothA2dpSink == null) {
      Log.w("BluetoothA2dpSink", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean setPriority(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setPriority(");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    ((StringBuilder)localObject).append(", ");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice)))
    {
      if ((paramInt != 0) && (paramInt != 100)) {
        return false;
      }
      try
      {
        boolean bool = ((IBluetoothA2dpSink)localObject).setPriority(paramBluetoothDevice, paramInt);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        paramBluetoothDevice = new StringBuilder();
        paramBluetoothDevice.append("Stack:");
        paramBluetoothDevice.append(Log.getStackTraceString(new Throwable()));
        Log.e("BluetoothA2dpSink", paramBluetoothDevice.toString());
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothA2dpSink", "Proxy not attached to service");
    }
    return false;
  }
}
