package android.bluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothHeadsetClient
  implements BluetoothProfile
{
  public static final String ACTION_AG_EVENT = "android.bluetooth.headsetclient.profile.action.AG_EVENT";
  public static final String ACTION_AUDIO_STATE_CHANGED = "android.bluetooth.headsetclient.profile.action.AUDIO_STATE_CHANGED";
  public static final String ACTION_CALL_CHANGED = "android.bluetooth.headsetclient.profile.action.AG_CALL_CHANGED";
  public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.headsetclient.profile.action.CONNECTION_STATE_CHANGED";
  public static final String ACTION_LAST_VTAG = "android.bluetooth.headsetclient.profile.action.LAST_VTAG";
  public static final String ACTION_RESULT = "android.bluetooth.headsetclient.profile.action.RESULT";
  public static final int ACTION_RESULT_ERROR = 1;
  public static final int ACTION_RESULT_ERROR_BLACKLISTED = 6;
  public static final int ACTION_RESULT_ERROR_BUSY = 3;
  public static final int ACTION_RESULT_ERROR_CME = 7;
  public static final int ACTION_RESULT_ERROR_DELAYED = 5;
  public static final int ACTION_RESULT_ERROR_NO_ANSWER = 4;
  public static final int ACTION_RESULT_ERROR_NO_CARRIER = 2;
  public static final int ACTION_RESULT_OK = 0;
  public static final int CALL_ACCEPT_HOLD = 1;
  public static final int CALL_ACCEPT_NONE = 0;
  public static final int CALL_ACCEPT_TERMINATE = 2;
  public static final int CME_CORPORATE_PERSONALIZATION_PIN_REQUIRED = 46;
  public static final int CME_CORPORATE_PERSONALIZATION_PUK_REQUIRED = 47;
  public static final int CME_DIAL_STRING_TOO_LONG = 26;
  public static final int CME_EAP_NOT_SUPPORTED = 49;
  public static final int CME_EMERGENCY_SERVICE_ONLY = 32;
  public static final int CME_HIDDEN_KEY_REQUIRED = 48;
  public static final int CME_INCORRECT_PARAMETERS = 50;
  public static final int CME_INCORRECT_PASSWORD = 16;
  public static final int CME_INVALID_CHARACTER_IN_DIAL_STRING = 27;
  public static final int CME_INVALID_CHARACTER_IN_TEXT_STRING = 25;
  public static final int CME_INVALID_INDEX = 21;
  public static final int CME_MEMORY_FAILURE = 23;
  public static final int CME_MEMORY_FULL = 20;
  public static final int CME_NETWORK_PERSONALIZATION_PIN_REQUIRED = 40;
  public static final int CME_NETWORK_PERSONALIZATION_PUK_REQUIRED = 41;
  public static final int CME_NETWORK_SUBSET_PERSONALIZATION_PIN_REQUIRED = 42;
  public static final int CME_NETWORK_SUBSET_PERSONALIZATION_PUK_REQUIRED = 43;
  public static final int CME_NETWORK_TIMEOUT = 31;
  public static final int CME_NOT_FOUND = 22;
  public static final int CME_NOT_SUPPORTED_FOR_VOIP = 34;
  public static final int CME_NO_CONNECTION_TO_PHONE = 1;
  public static final int CME_NO_NETWORK_SERVICE = 30;
  public static final int CME_NO_SIMULTANOUS_VOIP_CS_CALLS = 33;
  public static final int CME_OPERATION_NOT_ALLOWED = 3;
  public static final int CME_OPERATION_NOT_SUPPORTED = 4;
  public static final int CME_PHFSIM_PIN_REQUIRED = 6;
  public static final int CME_PHFSIM_PUK_REQUIRED = 7;
  public static final int CME_PHONE_FAILURE = 0;
  public static final int CME_PHSIM_PIN_REQUIRED = 5;
  public static final int CME_SERVICE_PROVIDER_PERSONALIZATION_PIN_REQUIRED = 44;
  public static final int CME_SERVICE_PROVIDER_PERSONALIZATION_PUK_REQUIRED = 45;
  public static final int CME_SIM_BUSY = 14;
  public static final int CME_SIM_FAILURE = 13;
  public static final int CME_SIM_NOT_INSERTED = 10;
  public static final int CME_SIM_PIN2_REQUIRED = 17;
  public static final int CME_SIM_PIN_REQUIRED = 11;
  public static final int CME_SIM_PUK2_REQUIRED = 18;
  public static final int CME_SIM_PUK_REQUIRED = 12;
  public static final int CME_SIM_WRONG = 15;
  public static final int CME_SIP_RESPONSE_CODE = 35;
  public static final int CME_TEXT_STRING_TOO_LONG = 24;
  private static final boolean DBG = true;
  public static final String EXTRA_AG_FEATURE_3WAY_CALLING = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_3WAY_CALLING";
  public static final String EXTRA_AG_FEATURE_ACCEPT_HELD_OR_WAITING_CALL = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_ACCEPT_HELD_OR_WAITING_CALL";
  public static final String EXTRA_AG_FEATURE_ATTACH_NUMBER_TO_VT = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_ATTACH_NUMBER_TO_VT";
  public static final String EXTRA_AG_FEATURE_ECC = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_ECC";
  public static final String EXTRA_AG_FEATURE_MERGE = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_MERGE";
  public static final String EXTRA_AG_FEATURE_MERGE_AND_DETACH = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_MERGE_AND_DETACH";
  public static final String EXTRA_AG_FEATURE_REJECT_CALL = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_REJECT_CALL";
  public static final String EXTRA_AG_FEATURE_RELEASE_AND_ACCEPT = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_RELEASE_AND_ACCEPT";
  public static final String EXTRA_AG_FEATURE_RELEASE_HELD_OR_WAITING_CALL = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_RELEASE_HELD_OR_WAITING_CALL";
  public static final String EXTRA_AG_FEATURE_RESPONSE_AND_HOLD = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_RESPONSE_AND_HOLD";
  public static final String EXTRA_AG_FEATURE_VOICE_RECOGNITION = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_VOICE_RECOGNITION";
  public static final String EXTRA_AUDIO_WBS = "android.bluetooth.headsetclient.extra.AUDIO_WBS";
  public static final String EXTRA_BATTERY_LEVEL = "android.bluetooth.headsetclient.extra.BATTERY_LEVEL";
  public static final String EXTRA_CALL = "android.bluetooth.headsetclient.extra.CALL";
  public static final String EXTRA_CME_CODE = "android.bluetooth.headsetclient.extra.CME_CODE";
  public static final String EXTRA_IN_BAND_RING = "android.bluetooth.headsetclient.extra.IN_BAND_RING";
  public static final String EXTRA_NETWORK_ROAMING = "android.bluetooth.headsetclient.extra.NETWORK_ROAMING";
  public static final String EXTRA_NETWORK_SIGNAL_STRENGTH = "android.bluetooth.headsetclient.extra.NETWORK_SIGNAL_STRENGTH";
  public static final String EXTRA_NETWORK_STATUS = "android.bluetooth.headsetclient.extra.NETWORK_STATUS";
  public static final String EXTRA_NUMBER = "android.bluetooth.headsetclient.extra.NUMBER";
  public static final String EXTRA_OPERATOR_NAME = "android.bluetooth.headsetclient.extra.OPERATOR_NAME";
  public static final String EXTRA_RESULT_CODE = "android.bluetooth.headsetclient.extra.RESULT_CODE";
  public static final String EXTRA_SUBSCRIBER_INFO = "android.bluetooth.headsetclient.extra.SUBSCRIBER_INFO";
  public static final String EXTRA_VOICE_RECOGNITION = "android.bluetooth.headsetclient.extra.VOICE_RECOGNITION";
  public static final int STATE_AUDIO_CONNECTED = 2;
  public static final int STATE_AUDIO_CONNECTING = 1;
  public static final int STATE_AUDIO_DISCONNECTED = 0;
  private static final String TAG = "BluetoothHeadsetClient";
  private static final boolean VDBG = false;
  private BluetoothAdapter mAdapter;
  private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub()
  {
    public void onBluetoothStateChange(boolean paramAnonymousBoolean)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("onBluetoothStateChange: up=");
      ((StringBuilder)???).append(paramAnonymousBoolean);
      Log.d("BluetoothHeadsetClient", ((StringBuilder)???).toString());
      if (!paramAnonymousBoolean)
      {
        try
        {
          synchronized (mConnection)
          {
            BluetoothHeadsetClient.access$102(BluetoothHeadsetClient.this, null);
            mContext.unbindService(mConnection);
          }
        }
        catch (Exception localException1)
        {
          Log.e("BluetoothHeadsetClient", "", localException1);
        }
        throw localException1;
      }
      else
      {
        try
        {
          synchronized (mConnection)
          {
            if (mService == null)
            {
              new Intent(IBluetoothHeadsetClient.class.getName());
              doBind();
            }
          }
          return;
        }
        catch (Exception localException2)
        {
          Log.e("BluetoothHeadsetClient", "", localException2);
        }
      }
    }
  };
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      Log.d("BluetoothHeadsetClient", "Proxy object connected");
      BluetoothHeadsetClient.access$102(BluetoothHeadsetClient.this, IBluetoothHeadsetClient.Stub.asInterface(Binder.allowBlocking(paramAnonymousIBinder)));
      if (mServiceListener != null) {
        mServiceListener.onServiceConnected(16, BluetoothHeadsetClient.this);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      Log.d("BluetoothHeadsetClient", "Proxy object disconnected");
      BluetoothHeadsetClient.access$102(BluetoothHeadsetClient.this, null);
      if (mServiceListener != null) {
        mServiceListener.onServiceDisconnected(16);
      }
    }
  };
  private Context mContext;
  private volatile IBluetoothHeadsetClient mService;
  private BluetoothProfile.ServiceListener mServiceListener;
  
  BluetoothHeadsetClient(Context paramContext, BluetoothProfile.ServiceListener paramServiceListener)
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
        Log.e("BluetoothHeadsetClient", "", paramContext);
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
    Log.d("BluetoothHeadsetClient", paramString);
  }
  
  public boolean acceptCall(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    log("acceptCall()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.acceptCall(paramBluetoothDevice, paramInt);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
  
  void close()
  {
    IBluetoothManager localIBluetoothManager = mAdapter.getBluetoothManager();
    if (localIBluetoothManager != null) {
      try
      {
        localIBluetoothManager.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
      }
      catch (Exception localException1)
      {
        Log.e("BluetoothHeadsetClient", "", localException1);
      }
    }
    synchronized (mConnection)
    {
      IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
      if (localIBluetoothHeadsetClient != null) {
        try
        {
          mService = null;
          mContext.unbindService(mConnection);
        }
        catch (Exception localException2)
        {
          Log.e("BluetoothHeadsetClient", "", localException2);
        }
      }
      mServiceListener = null;
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
        boolean bool = ((IBluetoothHeadsetClient)localObject).connect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean connectAudio(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()))
    {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.connectAudio(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
      Log.d("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
    }
    return false;
  }
  
  public BluetoothHeadsetClientCall dial(BluetoothDevice paramBluetoothDevice, String paramString)
  {
    log("dial()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        paramBluetoothDevice = localIBluetoothHeadsetClient.dial(paramBluetoothDevice, paramString);
        return paramBluetoothDevice;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return null;
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
        boolean bool = ((IBluetoothHeadsetClient)localObject).disconnect(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean disconnectAudio(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()))
    {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.disconnectAudio(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
      Log.d("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
    }
    return false;
  }
  
  boolean doBind()
  {
    Intent localIntent = new Intent(IBluetoothHeadsetClient.class.getName());
    Object localObject = localIntent.resolveSystemService(mContext.getPackageManager(), 0);
    localIntent.setComponent((ComponentName)localObject);
    if ((localObject != null) && (mContext.bindServiceAsUser(localIntent, mConnection, 0, mContext.getUser()))) {
      return true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Could not bind to Bluetooth Headset Client Service with ");
    ((StringBuilder)localObject).append(localIntent);
    Log.e("BluetoothHeadsetClient", ((StringBuilder)localObject).toString());
    return false;
  }
  
  public boolean enterPrivateMode(BluetoothDevice paramBluetoothDevice, int paramInt)
  {
    log("enterPrivateMode()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.enterPrivateMode(paramBluetoothDevice, paramInt);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean explicitCallTransfer(BluetoothDevice paramBluetoothDevice)
  {
    log("explicitCallTransfer()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.explicitCallTransfer(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean getAudioRouteAllowed(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()))
    {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.getAudioRouteAllowed(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
      Log.d("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
    }
    return false;
  }
  
  public int getAudioState(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()))
    {
      try
      {
        int i = localIBluetoothHeadsetClient.getAudioState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
      Log.d("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
    }
    return 0;
  }
  
  public List<BluetoothDevice> getConnectedDevices()
  {
    Object localObject = mService;
    if ((localObject != null) && (isEnabled())) {
      try
      {
        localObject = ((IBluetoothHeadsetClient)localObject).getConnectedDevices();
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
        return new ArrayList();
      }
    }
    if (localRemoteException == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = localIBluetoothHeadsetClient.getConnectionState(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
        return 0;
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return 0;
  }
  
  public Bundle getCurrentAgEvents(BluetoothDevice paramBluetoothDevice)
  {
    log("getCurrentCalls()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        paramBluetoothDevice = localIBluetoothHeadsetClient.getCurrentAgEvents(paramBluetoothDevice);
        return paramBluetoothDevice;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return null;
  }
  
  public Bundle getCurrentAgFeatures(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()))
    {
      try
      {
        paramBluetoothDevice = localIBluetoothHeadsetClient.getCurrentAgFeatures(paramBluetoothDevice);
        return paramBluetoothDevice;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", paramBluetoothDevice.toString());
      }
    }
    else
    {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
      Log.d("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
    }
    return null;
  }
  
  public List<BluetoothHeadsetClientCall> getCurrentCalls(BluetoothDevice paramBluetoothDevice)
  {
    log("getCurrentCalls()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        paramBluetoothDevice = localIBluetoothHeadsetClient.getCurrentCalls(paramBluetoothDevice);
        return paramBluetoothDevice;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return null;
  }
  
  public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
  {
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled())) {
      try
      {
        paramArrayOfInt = localIBluetoothHeadsetClient.getDevicesMatchingConnectionStates(paramArrayOfInt);
        return paramArrayOfInt;
      }
      catch (RemoteException paramArrayOfInt)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
        return new ArrayList();
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return new ArrayList();
  }
  
  public boolean getLastVoiceTagNumber(BluetoothDevice paramBluetoothDevice)
  {
    log("getLastVoiceTagNumber()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.getLastVoiceTagNumber(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
  
  public int getPriority(BluetoothDevice paramBluetoothDevice)
  {
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        int i = localIBluetoothHeadsetClient.getPriority(paramBluetoothDevice);
        return i;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
        return 0;
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return 0;
  }
  
  public boolean holdCall(BluetoothDevice paramBluetoothDevice)
  {
    log("holdCall()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.holdCall(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean rejectCall(BluetoothDevice paramBluetoothDevice)
  {
    log("rejectCall()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.rejectCall(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean sendDTMF(BluetoothDevice paramBluetoothDevice, byte paramByte)
  {
    log("sendDTMF()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.sendDTMF(paramBluetoothDevice, paramByte);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
  
  public void setAudioRouteAllowed(BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
  {
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()))
    {
      try
      {
        localIBluetoothHeadsetClient.setAudioRouteAllowed(paramBluetoothDevice, paramBoolean);
      }
      catch (RemoteException paramBluetoothDevice)
      {
        for (;;)
        {
          Log.e("BluetoothHeadsetClient", paramBluetoothDevice.toString());
        }
      }
    }
    else
    {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
      Log.d("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
    }
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
        boolean bool = ((IBluetoothHeadsetClient)localObject).setPriority(paramBluetoothDevice, paramInt);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
        return false;
      }
    }
    if (localObject == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean startVoiceRecognition(BluetoothDevice paramBluetoothDevice)
  {
    log("startVoiceRecognition()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.startVoiceRecognition(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean stopVoiceRecognition(BluetoothDevice paramBluetoothDevice)
  {
    log("stopVoiceRecognition()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.stopVoiceRecognition(paramBluetoothDevice);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
  
  public boolean terminateCall(BluetoothDevice paramBluetoothDevice, BluetoothHeadsetClientCall paramBluetoothHeadsetClientCall)
  {
    log("terminateCall()");
    IBluetoothHeadsetClient localIBluetoothHeadsetClient = mService;
    if ((localIBluetoothHeadsetClient != null) && (isEnabled()) && (isValidDevice(paramBluetoothDevice))) {
      try
      {
        boolean bool = localIBluetoothHeadsetClient.terminateCall(paramBluetoothDevice, paramBluetoothHeadsetClientCall);
        return bool;
      }
      catch (RemoteException paramBluetoothDevice)
      {
        Log.e("BluetoothHeadsetClient", Log.getStackTraceString(new Throwable()));
      }
    }
    if (localIBluetoothHeadsetClient == null) {
      Log.w("BluetoothHeadsetClient", "Proxy not attached to service");
    }
    return false;
  }
}
