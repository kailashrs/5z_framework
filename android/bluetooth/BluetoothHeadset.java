package android.bluetooth;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public final class BluetoothHeadset
  implements BluetoothProfile
{
  public static final String ACTION_ACTIVE_DEVICE_CHANGED = "android.bluetooth.headset.profile.action.ACTIVE_DEVICE_CHANGED";
  public static final String ACTION_AUDIO_STATE_CHANGED = "android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED";
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED";
  public static final String ACTION_HF_INDICATORS_VALUE_CHANGED = "android.bluetooth.headset.action.HF_INDICATORS_VALUE_CHANGED";
  public static final String ACTION_VENDOR_SPECIFIC_HEADSET_EVENT = "android.bluetooth.headset.action.VENDOR_SPECIFIC_HEADSET_EVENT";
  public static final int AT_CMD_TYPE_ACTION = 4;
  public static final int AT_CMD_TYPE_BASIC = 3;
  public static final int AT_CMD_TYPE_READ = 0;
  public static final int AT_CMD_TYPE_SET = 2;
  public static final int AT_CMD_TYPE_TEST = 1;
  private static final boolean DBG = true;
  public static final String EXTRA_HF_INDICATORS_IND_ID = "android.bluetooth.headset.extra.HF_INDICATORS_IND_ID";
  public static final String EXTRA_HF_INDICATORS_IND_VALUE = "android.bluetooth.headset.extra.HF_INDICATORS_IND_VALUE";
  public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_ARGS";
  public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD";
  public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE";
  private static final int MESSAGE_HEADSET_SERVICE_CONNECTED = 100;
  private static final int MESSAGE_HEADSET_SERVICE_DISCONNECTED = 101;
  public static final int STATE_AUDIO_CONNECTED = 12;
  public static final int STATE_AUDIO_CONNECTING = 11;
  public static final int STATE_AUDIO_DISCONNECTED = 10;
  private static final String TAG = "BluetoothHeadset";
  private static final boolean VDBG = false;
  public static final String VENDOR_RESULT_CODE_COMMAND_ANDROID = "+ANDROID";
  public static final String VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID_CATEGORY = "android.bluetooth.headset.intent.category.companyid";
  public static final String VENDOR_SPECIFIC_HEADSET_EVENT_IPHONEACCEV = "+IPHONEACCEV";
  public static final int VENDOR_SPECIFIC_HEADSET_EVENT_IPHONEACCEV_BATTERY_LEVEL = 1;
  public static final String VENDOR_SPECIFIC_HEADSET_EVENT_XAPL = "+XAPL";
  public static final String VENDOR_SPECIFIC_HEADSET_EVENT_XEVENT = "+XEVENT";
  public static final String VENDOR_SPECIFIC_HEADSET_EVENT_XEVENT_BATTERY_LEVEL = "BATTERY";
  private BluetoothAdapter mAdapter;
  private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("onBluetoothStateChange: up=");
      ((StringBuilder)???).append(paramAnonymousBoolean);
      Log.d("BluetoothHeadset", ((StringBuilder)???).toString());
      if (!paramAnonymousBoolean) {
        doUnbind();
      } else {
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
        catch (Exception localException)
        {
          Log.e("BluetoothHeadset", "", localException);
        }
      }
    }
  };
  private final IBluetoothProfileServiceConnection mConnection = new IBluetoothProfileServiceConnection.Stub()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      Log.d("BluetoothHeadset", "Proxy object connected");
      try
      {
        mServiceLock.writeLock().lock();
        BluetoothHeadset.access$102(BluetoothHeadset.this, IBluetoothHeadset.Stub.asInterface(Binder.allowBlocking(paramAnonymousIBinder)));
        mHandler.sendMessage(mHandler.obtainMessage(100));
        return;
      }
      finally
      {
        mServiceLock.writeLock().unlock();
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      Log.d("BluetoothHeadset", "Proxy object disconnected");
      try
      {
        mServiceLock.writeLock().lock();
        BluetoothHeadset.access$102(BluetoothHeadset.this, null);
        mHandler.sendMessage(mHandler.obtainMessage(101));
        return;
      }
      finally
      {
        mServiceLock.writeLock().unlock();
      }
    }
  };
  private Context mContext;
  private final Handler mHandler = new Handler(Looper.getMainLooper())
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 101: 
        if (mServiceListener != null) {
          mServiceListener.onServiceDisconnected(1);
        }
        break;
      case 100: 
        if (mServiceListener != null) {
          mServiceListener.onServiceConnected(1, BluetoothHeadset.this);
        }
        break;
      }
    }
  };
  @GuardedBy("mServiceLock")
  private IBluetoothHeadset mService;
  private BluetoothProfile.ServiceListener mServiceListener;
  private final ReentrantReadWriteLock mServiceLock = new ReentrantReadWriteLock();
  
  BluetoothHeadset(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
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
        Log.e("BluetoothHeadset", "", paramContext);
      }
    }
    doBind();
  }
  
  public static boolean isBluetoothVoiceDialingEnabled(Context paramContext)
  {
    return paramContext.getResources().getBoolean(17956905);
  }
  
  private boolean isDisabled()
  {
    boolean bool;
    if (mAdapter.getState() == 10) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
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
  
  public static boolean isInbandRingingSupported(Context paramContext)
  {
    return paramContext.getResources().getBoolean(17956901);
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
    Log.d("BluetoothHeadset", paramString);
  }
  
  public void clccResponse(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, String paramString, int paramInt5)
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()))
    {
      try
      {
        localIBluetoothHeadset.clccResponse(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean, paramString, paramInt5);
      }
      catch (RemoteException paramString)
      {
        for (;;)
        {
          Log.e("BluetoothHeadset", paramString.toString());
        }
      }
    }
    else
    {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
      Log.d("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
    }
  }
  
  void close()
  {
    IBluetoothManager localIBluetoothManager = mAdapter.getBluetoothManager();
    if (localIBluetoothManager != null) {
      try
      {
        localIBluetoothManager.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
      }
      catch (Exception localException)
      {
        Log.e("BluetoothHeadset", "", localException);
      }
    }
    mServiceListener = null;
    doUnbind();
  }
  
  @SystemApi
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
        boolean bool = ((IBluetoothHeadset)localObject).connect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean connectAudio()
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()))
    {
      try
      {
        boolean bool = localIBluetoothHeadset.connectAudio();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothHeadset", localRemoteException.toString());
      }
    }
    else
    {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
      Log.d("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
    }
    return false;
  }
  
  @SystemApi
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
        boolean bool = ((IBluetoothHeadset)localObject).disconnect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean disconnectAudio()
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()))
    {
      try
      {
        boolean bool = localIBluetoothHeadset.disconnectAudio();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothHeadset", localRemoteException.toString());
      }
    }
    else
    {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
      Log.d("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
    }
    return false;
  }
  
  boolean doBind()
  {
    try
    {
      boolean bool = mAdapter.getBluetoothManager().bindBluetoothProfileService(1, mConnection);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothHeadset", "Unable to bind HeadsetService", localRemoteException);
    }
    return false;
  }
  
  void doUnbind()
  {
    try
    {
      synchronized (mConnection)
      {
        mAdapter.getBluetoothManager().unbindBluetoothProfileService(1, mConnection);
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothHeadset", "Unable to unbind HeadsetService", localRemoteException);
      return;
    }
  }
  
  public void finalize()
  {
    close();
  }
  
  public BluetoothDevice getActiveDevice()
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled())) {
      try
      {
        BluetoothDevice localBluetoothDevice = localIBluetoothHeadset.getActiveDevice();
        return localBluetoothDevice;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadset == null) {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
    }
    return null;
  }
  
  public boolean getAudioRouteAllowed()
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()))
    {
      try
      {
        boolean bool = localIBluetoothHeadset.getAudioRouteAllowed();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothHeadset", localRemoteException.toString());
      }
    }
    else
    {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
      Log.d("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
    }
    return false;
  }
  
  public int getAudioState(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (!isDisabled()))
    {
      try
      {
        int i = localIBluetoothHeadset.getAudioState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadset", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
      Log.d("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
    }
    return 10;
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    try
    {
      mServiceLock.readLock().lock();
      Object localObject1 = mService;
      if (localObject1 != null)
      {
        boolean bool = isEnabled();
        if (bool) {
          try
          {
            localObject1 = ((IBluetoothHeadset)localObject1).getConnectedDevices();
            return localObject1;
          }
          catch (RemoteException localRemoteException)
          {
            localObject2 = new java/lang/Throwable;
            ((Throwable)localObject2).<init>();
            Log.e("BluetoothHeadset", Log.getStackTraceString((Throwable)localObject2));
            localObject2 = new ArrayList();
            return localObject2;
          }
        }
      }
      if (localObject2 == null) {
        Log.w("BluetoothHeadset", "Proxy not attached to service");
      }
      Object localObject2 = new ArrayList();
      return localObject2;
    }
    finally
    {
      mServiceLock.readLock().unlock();
    }
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = localIBluetoothHeadset.getConnectionState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
        return 0;
      }
    }
    if (localIBluetoothHeadset == null) {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
    }
    return 0;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled())) {
      try
      {
        paramArrayOfInt = localIBluetoothHeadset.getDevicesMatchingConnectionStates(paramArrayOfInt);
        return paramArrayOfInt;
      }
      catch (RemoteException paramArrayOfInt)
      {
        Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
        return new ArrayList();
      }
    }
    if (localIBluetoothHeadset == null) {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getPriority(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = localIBluetoothHeadset.getPriority(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
        return 0;
      }
    }
    if (localIBluetoothHeadset == null) {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
    }
    return 0;
  }
  
  public boolean isAudioConnected(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    try
    {
      mServiceLock.readLock().lock();
      if ((localIBluetoothHeadset != null) && (isEnabled()))
      {
        boolean bool = isValidDevice(paramBluetoothDevice);
        if (bool) {
          try
          {
            bool = localIBluetoothHeadset.isAudioConnected(paramBluetoothDevice);
            return bool;
          }
          catch (RemoteException paramBluetoothDevice)
          {
            paramBluetoothDevice = new java/lang/Throwable;
            paramBluetoothDevice.<init>();
            Log.e("BluetoothHeadset", Log.getStackTraceString(paramBluetoothDevice));
          }
        }
      }
      if (localIBluetoothHeadset == null) {
        Log.w("BluetoothHeadset", "Proxy not attached to service");
      }
      return false;
    }
    finally
    {
      mServiceLock.readLock().unlock();
    }
  }
  
  public boolean isAudioOn()
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled())) {
      try
      {
        boolean bool = localIBluetoothHeadset.isAudioOn();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadset == null) {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean isInbandRingingEnabled()
  {
    log("isInbandRingingEnabled()");
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled())) {
      try
      {
        boolean bool = localIBluetoothHeadset.isInbandRingingEnabled();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadset == null) {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
    }
    return false;
  }
  
  public void phoneStateChanged(int paramInt1, int paramInt2, int paramInt3, String paramString, int paramInt4)
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()))
    {
      try
      {
        localIBluetoothHeadset.phoneStateChanged(paramInt1, paramInt2, paramInt3, paramString, paramInt4);
      }
      catch (RemoteException paramString)
      {
        for (;;)
        {
          Log.e("BluetoothHeadset", paramString.toString());
        }
      }
    }
    else
    {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
      Log.d("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
    }
  }
  
  public boolean sendVendorSpecificResultCode(BluetoothDevice paramBluetoothDevice, String paramString1, String paramString2)
  {
    log("sendVendorSpecificResultCode()");
    if (paramString1 != null)
    {
      IBluetoothHeadset localIBluetoothHeadset = mService;
      if ((localIBluetoothHeadset != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
        try
        {
          boolean bool = localIBluetoothHeadset.sendVendorSpecificResultCode(paramBluetoothDevice, paramString1, paramString2);
          return bool;
        }
        catch (RemoteException paramBluetoothDevice)
        {
          Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
        }
      }
      if (localIBluetoothHeadset == null) {
        Log.w("BluetoothHeadset", "Proxy not attached to service");
      }
      return false;
    }
    throw new IllegalArgumentException("command is null");
  }
  
  public boolean setActiveDevice(BluetoothDevice paramBluetoothDevice)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setActiveDevice: ");
    ((StringBuilder)localObject).append(paramBluetoothDevice);
    Log.d("BluetoothHeadset", ((StringBuilder)localObject).toString());
    localObject = mService;
    if ((localObject != null) && (isEnabled()) && ((paramBluetoothDevice == null) || (isValidDevice(paramBluetoothDevice)))) {
      try
      {
        boolean bool = ((IBluetoothHeadset)localObject).setActiveDevice(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
    }
    return false;
  }
  
  public void setAudioRouteAllowed(boolean paramBoolean)
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()))
    {
      try
      {
        localIBluetoothHeadset.setAudioRouteAllowed(paramBoolean);
      }
      catch (RemoteException localRemoteException)
      {
        for (;;)
        {
          Log.e("BluetoothHeadset", localRemoteException.toString());
        }
      }
    }
    else
    {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
      Log.d("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
    }
  }
  
  public void setForceScoAudio(boolean paramBoolean)
  {
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()))
    {
      try
      {
        localIBluetoothHeadset.setForceScoAudio(paramBoolean);
      }
      catch (RemoteException localRemoteException)
      {
        for (;;)
        {
          Log.e("BluetoothHeadset", localRemoteException.toString());
        }
      }
    }
    else
    {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
      Log.d("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
    }
  }
  
  @SystemApi
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
        boolean bool = ((IBluetoothHeadset)localObject).setPriority(paramBluetoothDevice, paramInt);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean startScoUsingVirtualVoiceCall()
  {
    log("startScoUsingVirtualVoiceCall()");
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()))
    {
      try
      {
        boolean bool = localIBluetoothHeadset.startScoUsingVirtualVoiceCall();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothHeadset", localRemoteException.toString());
      }
    }
    else
    {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
      Log.d("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
    }
    return false;
  }
  
  public boolean startVoiceRecognition(BluetoothDevice paramBluetoothDevice)
  {
    log("startVoiceRecognition()");
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHeadset.startVoiceRecognition(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadset == null) {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean stopScoUsingVirtualVoiceCall()
  {
    log("stopScoUsingVirtualVoiceCall()");
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()))
    {
      try
      {
        boolean bool = localIBluetoothHeadset.stopScoUsingVirtualVoiceCall();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothHeadset", localRemoteException.toString());
      }
    }
    else
    {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
      Log.d("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
    }
    return false;
  }
  
  public boolean stopVoiceRecognition(BluetoothDevice paramBluetoothDevice)
  {
    log("stopVoiceRecognition()");
    IBluetoothHeadset localIBluetoothHeadset = mService;
    if ((localIBluetoothHeadset != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHeadset.stopVoiceRecognition(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadset", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadset == null) {
      Log.w("BluetoothHeadset", "Proxy not attached to service");
    }
    return false;
  }
}
