package android.bluetooth;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public final class BluetoothDevice
  implements Parcelable
{
  @SystemApi
  public static final int ACCESS_ALLOWED = 1;
  @SystemApi
  public static final int ACCESS_REJECTED = 2;
  @SystemApi
  public static final int ACCESS_UNKNOWN = 0;
  public static final String ACTION_ACL_CONNECTED = "android.bluetooth.device.action.ACL_CONNECTED";
  public static final String ACTION_ACL_DISCONNECTED = "android.bluetooth.device.action.ACL_DISCONNECTED";
  public static final String ACTION_ACL_DISCONNECT_REQUESTED = "android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED";
  public static final String ACTION_ALIAS_CHANGED = "android.bluetooth.device.action.ALIAS_CHANGED";
  public static final String ACTION_BATTERY_LEVEL_CHANGED = "android.bluetooth.device.action.BATTERY_LEVEL_CHANGED";
  public static final String ACTION_BOND_STATE_CHANGED = "android.bluetooth.device.action.BOND_STATE_CHANGED";
  public static final String ACTION_CLASS_CHANGED = "android.bluetooth.device.action.CLASS_CHANGED";
  public static final String ACTION_CONNECTION_ACCESS_CANCEL = "android.bluetooth.device.action.CONNECTION_ACCESS_CANCEL";
  public static final String ACTION_CONNECTION_ACCESS_REPLY = "android.bluetooth.device.action.CONNECTION_ACCESS_REPLY";
  public static final String ACTION_CONNECTION_ACCESS_REQUEST = "android.bluetooth.device.action.CONNECTION_ACCESS_REQUEST";
  public static final String ACTION_DISAPPEARED = "android.bluetooth.device.action.DISAPPEARED";
  public static final String ACTION_FOUND = "android.bluetooth.device.action.FOUND";
  public static final String ACTION_MAS_INSTANCE = "android.bluetooth.device.action.MAS_INSTANCE";
  public static final String ACTION_NAME_CHANGED = "android.bluetooth.device.action.NAME_CHANGED";
  public static final String ACTION_NAME_FAILED = "android.bluetooth.device.action.NAME_FAILED";
  public static final String ACTION_PAIRING_CANCEL = "android.bluetooth.device.action.PAIRING_CANCEL";
  public static final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
  public static final String ACTION_REMOTE_ISSUE_OCCURRED = "org.codeaurora.intent.bluetooth.action.REMOTE_ISSUE_OCCURRED";
  public static final String ACTION_SDP_RECORD = "android.bluetooth.device.action.SDP_RECORD";
  public static final String ACTION_TWS_PLUS_DEVICE_PAIR = "android.bluetooth.device.action.TWS_PLUS_DEVICE_PAIR";
  public static final String ACTION_UUID = "android.bluetooth.device.action.UUID";
  public static final int BATTERY_LEVEL_UNKNOWN = -1;
  public static final int BOND_BONDED = 12;
  public static final int BOND_BONDING = 11;
  public static final int BOND_NONE = 10;
  public static final int BOND_SUCCESS = 0;
  public static final int CONNECTION_ACCESS_NO = 2;
  public static final int CONNECTION_ACCESS_YES = 1;
  private static final int CONNECTION_STATE_CONNECTED = 1;
  private static final int CONNECTION_STATE_DISCONNECTED = 0;
  private static final int CONNECTION_STATE_ENCRYPTED_BREDR = 2;
  private static final int CONNECTION_STATE_ENCRYPTED_LE = 4;
  public static final Parcelable.Creator<BluetoothDevice> CREATOR = new Parcelable.Creator()
  {
    public BluetoothDevice createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothDevice(paramAnonymousParcel.readString());
    }
    
    public BluetoothDevice[] newArray(int paramAnonymousInt)
    {
      return new BluetoothDevice[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  public static final int DEVICE_TYPE_CLASSIC = 1;
  public static final int DEVICE_TYPE_DUAL = 3;
  public static final int DEVICE_TYPE_LE = 2;
  public static final int DEVICE_TYPE_UNKNOWN = 0;
  public static final int ERROR = Integer.MIN_VALUE;
  public static final String EXTRA_ACCESS_REQUEST_TYPE = "android.bluetooth.device.extra.ACCESS_REQUEST_TYPE";
  public static final String EXTRA_ALWAYS_ALLOWED = "android.bluetooth.device.extra.ALWAYS_ALLOWED";
  public static final String EXTRA_BATTERY_LEVEL = "android.bluetooth.device.extra.BATTERY_LEVEL";
  public static final String EXTRA_BOND_STATE = "android.bluetooth.device.extra.BOND_STATE";
  public static final String EXTRA_CLASS = "android.bluetooth.device.extra.CLASS";
  public static final String EXTRA_CLASS_NAME = "android.bluetooth.device.extra.CLASS_NAME";
  public static final String EXTRA_CONNECTION_ACCESS_RESULT = "android.bluetooth.device.extra.CONNECTION_ACCESS_RESULT";
  public static final String EXTRA_DEVICE = "android.bluetooth.device.extra.DEVICE";
  public static final String EXTRA_ERROR_CODE = "android.bluetooth.qti.extra.ERROR_CODE";
  public static final String EXTRA_ERROR_EVENT_MASK = "android.bluetooth.qti.extra.ERROR_EVENT_MASK";
  public static final String EXTRA_GLITCH_COUNT = "android.bluetooth.qti.extra.EXTRA_GLITCH_COUNT";
  public static final String EXTRA_ISSUE_TYPE = "android.bluetooth.qti.extra.ERROR_TYPE";
  public static final String EXTRA_LINK_QUALITY = "android.bluetooth.qti.extra.EXTRA_LINK_QUALITY";
  public static final String EXTRA_LMP_SUBVER = "android.bluetooth.qti.extra.EXTRA_LMP_SUBVER";
  public static final String EXTRA_LMP_VERSION = "android.bluetooth.qti.extra.EXTRA_LMP_VERSION";
  public static final String EXTRA_MANUFACTURER = "android.bluetooth.qti.extra.EXTRA_MANUFACTURER";
  public static final String EXTRA_MAS_INSTANCE = "android.bluetooth.device.extra.MAS_INSTANCE";
  public static final String EXTRA_NAME = "android.bluetooth.device.extra.NAME";
  public static final String EXTRA_PACKAGE_NAME = "android.bluetooth.device.extra.PACKAGE_NAME";
  public static final String EXTRA_PAIRING_KEY = "android.bluetooth.device.extra.PAIRING_KEY";
  public static final String EXTRA_PAIRING_VARIANT = "android.bluetooth.device.extra.PAIRING_VARIANT";
  public static final String EXTRA_POWER_LEVEL = "android.bluetooth.qti.extra.EXTRA_POWER_LEVEL";
  public static final String EXTRA_PREVIOUS_BOND_STATE = "android.bluetooth.device.extra.PREVIOUS_BOND_STATE";
  public static final String EXTRA_REASON = "android.bluetooth.device.extra.REASON";
  public static final String EXTRA_RSSI = "android.bluetooth.device.extra.RSSI";
  public static final String EXTRA_SDP_RECORD = "android.bluetooth.device.extra.SDP_RECORD";
  public static final String EXTRA_SDP_SEARCH_STATUS = "android.bluetooth.device.extra.SDP_SEARCH_STATUS";
  public static final String EXTRA_TWS_PLUS_DEVICE1 = "android.bluetooth.device.extra.EXTRA_TWS_PLUS_DEVICE1";
  public static final String EXTRA_TWS_PLUS_DEVICE2 = "android.bluetooth.device.extra.EXTRA_TWS_PLUS_DEVICE2";
  public static final String EXTRA_UUID = "android.bluetooth.device.extra.UUID";
  public static final int PAIRING_VARIANT_CONSENT = 3;
  public static final int PAIRING_VARIANT_DISPLAY_PASSKEY = 4;
  public static final int PAIRING_VARIANT_DISPLAY_PIN = 5;
  public static final int PAIRING_VARIANT_OOB_CONSENT = 6;
  public static final int PAIRING_VARIANT_PASSKEY = 1;
  public static final int PAIRING_VARIANT_PASSKEY_CONFIRMATION = 2;
  public static final int PAIRING_VARIANT_PIN = 0;
  public static final int PAIRING_VARIANT_PIN_16_DIGITS = 7;
  public static final int PHY_LE_1M = 1;
  public static final int PHY_LE_1M_MASK = 1;
  public static final int PHY_LE_2M = 2;
  public static final int PHY_LE_2M_MASK = 2;
  public static final int PHY_LE_CODED = 3;
  public static final int PHY_LE_CODED_MASK = 4;
  public static final int PHY_OPTION_NO_PREFERRED = 0;
  public static final int PHY_OPTION_S2 = 1;
  public static final int PHY_OPTION_S8 = 2;
  public static final int REQUEST_TYPE_MESSAGE_ACCESS = 3;
  public static final int REQUEST_TYPE_PHONEBOOK_ACCESS = 2;
  public static final int REQUEST_TYPE_PROFILE_CONNECTION = 1;
  public static final int REQUEST_TYPE_SIM_ACCESS = 4;
  private static final String TAG = "BluetoothDevice";
  public static final int TRANSPORT_AUTO = 0;
  public static final int TRANSPORT_BREDR = 1;
  public static final int TRANSPORT_LE = 2;
  public static final int UNBOND_REASON_AUTH_CANCELED = 3;
  public static final int UNBOND_REASON_AUTH_FAILED = 1;
  public static final int UNBOND_REASON_AUTH_REJECTED = 2;
  public static final int UNBOND_REASON_AUTH_TIMEOUT = 6;
  public static final int UNBOND_REASON_DISCOVERY_IN_PROGRESS = 5;
  public static final int UNBOND_REASON_REMOTE_AUTH_CANCELED = 8;
  public static final int UNBOND_REASON_REMOTE_DEVICE_DOWN = 4;
  public static final int UNBOND_REASON_REMOVED = 9;
  public static final int UNBOND_REASON_REPEATED_ATTEMPTS = 7;
  private static volatile IBluetooth sService;
  static IBluetoothManagerCallback sStateChangeCallback = new IBluetoothManagerCallback.Stub()
  {
    public void onBluetoothServiceDown()
      throws RemoteException
    {
      try
      {
        BluetoothDevice.access$002(null);
        return;
      }
      finally {}
    }
    
    public void onBluetoothServiceUp(IBluetooth paramAnonymousIBluetooth)
      throws RemoteException
    {
      try
      {
        if (BluetoothDevice.sService != null) {
          Log.w("BluetoothDevice", "sService is not NULL");
        }
        BluetoothDevice.access$002(paramAnonymousIBluetooth);
        return;
      }
      finally {}
    }
    
    public void onBrEdrDown() {}
  };
  private final String mAddress;
  
  BluetoothDevice(String paramString)
  {
    getService();
    if (BluetoothAdapter.checkBluetoothAddress(paramString))
    {
      mAddress = paramString;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" is not a valid Bluetooth address");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static byte[] convertPinToBytes(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    try
    {
      paramString = paramString.getBytes("UTF-8");
      if ((paramString.length > 0) && (paramString.length <= 16)) {
        return paramString;
      }
      return null;
    }
    catch (UnsupportedEncodingException paramString)
    {
      Log.e("BluetoothDevice", "UTF-8 not supported?!?");
    }
    return null;
  }
  
  static IBluetooth getService()
  {
    try
    {
      if (sService == null) {
        sService = BluetoothAdapter.getDefaultAdapter().getBluetoothService(sStateChangeCallback);
      }
      return sService;
    }
    finally {}
  }
  
  @SystemApi
  public boolean cancelBondProcess()
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot cancel Remote Device bond");
      return false;
    }
    try
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("cancelBondProcess() for device ");
      localStringBuilder.append(getAddress());
      localStringBuilder.append(" called by pid: ");
      localStringBuilder.append(Process.myPid());
      localStringBuilder.append(" tid: ");
      localStringBuilder.append(Process.myTid());
      Log.i("BluetoothDevice", localStringBuilder.toString());
      boolean bool = localIBluetooth.cancelBondProcess(this);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return false;
  }
  
  public boolean cancelPairingUserInput()
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot create pairing user input");
      return false;
    }
    try
    {
      boolean bool = localIBluetooth.cancelBondProcess(this);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return false;
  }
  
  public BluetoothGatt connectGatt(Context paramContext, boolean paramBoolean, BluetoothGattCallback paramBluetoothGattCallback)
  {
    return connectGatt(paramContext, paramBoolean, paramBluetoothGattCallback, 0);
  }
  
  public BluetoothGatt connectGatt(Context paramContext, boolean paramBoolean, BluetoothGattCallback paramBluetoothGattCallback, int paramInt)
  {
    return connectGatt(paramContext, paramBoolean, paramBluetoothGattCallback, paramInt, 1);
  }
  
  public BluetoothGatt connectGatt(Context paramContext, boolean paramBoolean, BluetoothGattCallback paramBluetoothGattCallback, int paramInt1, int paramInt2)
  {
    return connectGatt(paramContext, paramBoolean, paramBluetoothGattCallback, paramInt1, paramInt2, null);
  }
  
  public BluetoothGatt connectGatt(Context paramContext, boolean paramBoolean, BluetoothGattCallback paramBluetoothGattCallback, int paramInt1, int paramInt2, Handler paramHandler)
  {
    return connectGatt(paramContext, paramBoolean, paramBluetoothGattCallback, paramInt1, false, paramInt2, paramHandler);
  }
  
  public BluetoothGatt connectGatt(Context paramContext, boolean paramBoolean1, BluetoothGattCallback paramBluetoothGattCallback, int paramInt1, boolean paramBoolean2, int paramInt2, Handler paramHandler)
  {
    if (paramBluetoothGattCallback != null)
    {
      paramContext = BluetoothAdapter.getDefaultAdapter().getBluetoothManager();
      try
      {
        IBluetoothGatt localIBluetoothGatt = paramContext.getBluetoothGatt();
        if (localIBluetoothGatt == null) {
          return null;
        }
        paramContext = new android/bluetooth/BluetoothGatt;
        paramContext.<init>(localIBluetoothGatt, this, paramInt1, paramBoolean2, paramInt2);
        try
        {
          paramContext.connect(Boolean.valueOf(paramBoolean1), paramBluetoothGattCallback, paramHandler);
          return paramContext;
        }
        catch (RemoteException paramContext) {}
        Log.e("BluetoothDevice", "", paramContext);
      }
      catch (RemoteException paramContext) {}
      return null;
    }
    throw new NullPointerException("callback is null");
  }
  
  public boolean createBond()
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot create bond to Remote Device");
      return false;
    }
    try
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("createBond() for device ");
      localStringBuilder.append(getAddress());
      localStringBuilder.append(" called by pid: ");
      localStringBuilder.append(Process.myPid());
      localStringBuilder.append(" tid: ");
      localStringBuilder.append(Process.myTid());
      Log.i("BluetoothDevice", localStringBuilder.toString());
      boolean bool = localIBluetooth.createBond(this, 0);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return false;
  }
  
  public boolean createBond(int paramInt)
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot create bond to Remote Device");
      return false;
    }
    if ((paramInt >= 0) && (paramInt <= 2)) {
      try
      {
        StringBuilder localStringBuilder2 = new java/lang/StringBuilder;
        localStringBuilder2.<init>();
        localStringBuilder2.append("createBond() for device ");
        localStringBuilder2.append(getAddress());
        localStringBuilder2.append(" called by pid: ");
        localStringBuilder2.append(Process.myPid());
        localStringBuilder2.append(" tid: ");
        localStringBuilder2.append(Process.myTid());
        Log.i("BluetoothDevice", localStringBuilder2.toString());
        boolean bool = localIBluetooth.createBond(this, paramInt);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothDevice", "", localRemoteException);
        return false;
      }
    }
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append(paramInt);
    localStringBuilder1.append(" is not a valid Bluetooth transport");
    throw new IllegalArgumentException(localStringBuilder1.toString());
  }
  
  public boolean createBondOutOfBand(int paramInt, OobData paramOobData)
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.w("BluetoothDevice", "BT not enabled, createBondOutOfBand failed");
      return false;
    }
    try
    {
      boolean bool = localIBluetooth.createBondOutOfBand(this, paramInt, paramOobData);
      return bool;
    }
    catch (RemoteException paramOobData)
    {
      Log.e("BluetoothDevice", "", paramOobData);
    }
    return false;
  }
  
  public BluetoothSocket createInsecureL2capCocSocket(int paramInt1, int paramInt2)
    throws IOException
  {
    if (isBluetoothEnabled())
    {
      if (paramInt1 == 2) {
        return new BluetoothSocket(4, -1, false, false, this, paramInt2, null);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported transport: ");
      localStringBuilder.append(paramInt1);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    Log.e("BluetoothDevice", "createInsecureL2capCocSocket: Bluetooth is not enabled");
    throw new IOException();
  }
  
  public BluetoothSocket createInsecureL2capSocket(int paramInt)
    throws IOException
  {
    return new BluetoothSocket(3, -1, false, false, this, paramInt, null);
  }
  
  public BluetoothSocket createInsecureRfcommSocket(int paramInt)
    throws IOException
  {
    if (isBluetoothEnabled()) {
      return new BluetoothSocket(1, -1, false, false, this, paramInt, null);
    }
    Log.e("BluetoothDevice", "Bluetooth is not enabled");
    throw new IOException();
  }
  
  public BluetoothSocket createInsecureRfcommSocketToServiceRecord(UUID paramUUID)
    throws IOException
  {
    if (isBluetoothEnabled()) {
      return new BluetoothSocket(1, -1, false, false, this, -1, new ParcelUuid(paramUUID));
    }
    Log.e("BluetoothDevice", "Bluetooth is not enabled");
    throw new IOException();
  }
  
  public BluetoothSocket createL2capCocSocket(int paramInt1, int paramInt2)
    throws IOException
  {
    if (isBluetoothEnabled())
    {
      if (paramInt1 == 2) {
        return new BluetoothSocket(4, -1, true, true, this, paramInt2, null);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported transport: ");
      localStringBuilder.append(paramInt1);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    Log.e("BluetoothDevice", "createL2capCocSocket: Bluetooth is not enabled");
    throw new IOException();
  }
  
  public BluetoothSocket createL2capSocket(int paramInt)
    throws IOException
  {
    return new BluetoothSocket(3, -1, true, true, this, paramInt, null);
  }
  
  public BluetoothSocket createRfcommSocket(int paramInt)
    throws IOException
  {
    if (isBluetoothEnabled()) {
      return new BluetoothSocket(1, -1, true, true, this, paramInt, null);
    }
    Log.e("BluetoothDevice", "Bluetooth is not enabled");
    throw new IOException();
  }
  
  public BluetoothSocket createRfcommSocketToServiceRecord(UUID paramUUID)
    throws IOException
  {
    if (isBluetoothEnabled()) {
      return new BluetoothSocket(1, -1, true, true, this, -1, new ParcelUuid(paramUUID));
    }
    Log.e("BluetoothDevice", "Bluetooth is not enabled");
    throw new IOException();
  }
  
  public BluetoothSocket createScoSocket()
    throws IOException
  {
    if (isBluetoothEnabled()) {
      return new BluetoothSocket(2, -1, true, true, this, -1, null);
    }
    Log.e("BluetoothDevice", "Bluetooth is not enabled");
    throw new IOException();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof BluetoothDevice)) {
      return mAddress.equals(((BluetoothDevice)paramObject).getAddress());
    }
    return false;
  }
  
  public boolean fetchUuidsWithSdp()
  {
    IBluetooth localIBluetooth = sService;
    if ((localIBluetooth != null) && (isBluetoothEnabled())) {
      try
      {
        boolean bool = localIBluetooth.fetchRemoteUuids(this);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothDevice", "", localRemoteException);
        return false;
      }
    }
    Log.e("BluetoothDevice", "BT not enabled. Cannot fetchUuidsWithSdp");
    return false;
  }
  
  public String getAddress()
  {
    return mAddress;
  }
  
  public String getAlias()
  {
    Object localObject = sService;
    if (localObject == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot get Remote Device Alias");
      return null;
    }
    try
    {
      localObject = ((IBluetooth)localObject).getRemoteAlias(this);
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return null;
  }
  
  public String getAliasName()
  {
    String str1 = getAlias();
    String str2 = str1;
    if (str1 == null) {
      str2 = getName();
    }
    return str2;
  }
  
  public int getBatteryLevel()
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.e("BluetoothDevice", "Bluetooth disabled. Cannot get remote device battery level");
      return -1;
    }
    try
    {
      int i = localIBluetooth.getBatteryLevel(this);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return -1;
  }
  
  public BluetoothClass getBluetoothClass()
  {
    Object localObject = sService;
    if (localObject == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot get Bluetooth Class");
      return null;
    }
    try
    {
      int i = ((IBluetooth)localObject).getRemoteClass(this);
      if (i == -16777216) {
        return null;
      }
      localObject = new BluetoothClass(i);
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return null;
  }
  
  public int getBondState()
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot get bond state");
      return 10;
    }
    try
    {
      int i = localIBluetooth.getBondState(this);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return 10;
  }
  
  public int getMessageAccessPermission()
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null) {
      return 0;
    }
    try
    {
      int i = localIBluetooth.getMessageAccessPermission(this);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return 0;
  }
  
  public String getName()
  {
    Object localObject = sService;
    if (localObject == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot get Remote Device name");
      return null;
    }
    try
    {
      localObject = ((IBluetooth)localObject).getRemoteName(this);
      if (localObject != null)
      {
        localObject = ((String)localObject).replaceAll("[\\t\\n\\r]+", " ");
        return localObject;
      }
      return null;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return null;
  }
  
  public int getPhonebookAccessPermission()
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null) {
      return 0;
    }
    try
    {
      int i = localIBluetooth.getPhonebookAccessPermission(this);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return 0;
  }
  
  public int getSimAccessPermission()
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null) {
      return 0;
    }
    try
    {
      int i = localIBluetooth.getSimAccessPermission(this);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return 0;
  }
  
  public String getTwsPlusPeerAddress()
  {
    if (sService == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot get Remote Device name");
      return null;
    }
    try
    {
      String str = sService.getTwsPlusPeerAddress(this);
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return null;
  }
  
  public int getType()
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot get Remote Device type");
      return 0;
    }
    try
    {
      int i = localIBluetooth.getRemoteType(this);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return 0;
  }
  
  public ParcelUuid[] getUuids()
  {
    Object localObject = sService;
    if ((localObject != null) && (isBluetoothEnabled())) {
      try
      {
        localObject = ((IBluetooth)localObject).getRemoteUuids(this);
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BluetoothDevice", "", localRemoteException);
        return null;
      }
    }
    Log.e("BluetoothDevice", "BT not enabled. Cannot get remote device Uuids");
    return null;
  }
  
  public int hashCode()
  {
    return mAddress.hashCode();
  }
  
  public boolean isBluetoothDock()
  {
    return false;
  }
  
  boolean isBluetoothEnabled()
  {
    boolean bool1 = false;
    BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    boolean bool2 = bool1;
    if (localBluetoothAdapter != null)
    {
      bool2 = bool1;
      if (localBluetoothAdapter.isEnabled()) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public boolean isBondingInitiatedLocally()
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.w("BluetoothDevice", "BT not enabled, isBondingInitiatedLocally failed");
      return false;
    }
    try
    {
      boolean bool = localIBluetooth.isBondingInitiatedLocally(this);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return false;
  }
  
  @SystemApi
  public boolean isConnected()
  {
    IBluetooth localIBluetooth = sService;
    boolean bool = false;
    if (localIBluetooth == null) {
      return false;
    }
    try
    {
      int i = localIBluetooth.getConnectionState(this);
      if (i != 0) {
        bool = true;
      }
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return false;
  }
  
  @SystemApi
  public boolean isEncrypted()
  {
    IBluetooth localIBluetooth = sService;
    boolean bool = false;
    if (localIBluetooth == null) {
      return false;
    }
    try
    {
      int i = localIBluetooth.getConnectionState(this);
      if (i > 1) {
        bool = true;
      }
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return false;
  }
  
  public boolean isTwsPlusDevice()
  {
    if (sService == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot query remote device sdp records");
      return false;
    }
    try
    {
      boolean bool = sService.isTwsPlusDevice(this);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return false;
  }
  
  @SystemApi
  public boolean removeBond()
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot remove Remote Device bond");
      return false;
    }
    try
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("removeBond() for device ");
      localStringBuilder.append(getAddress());
      localStringBuilder.append(" called by pid: ");
      localStringBuilder.append(Process.myPid());
      localStringBuilder.append(" tid: ");
      localStringBuilder.append(Process.myTid());
      Log.i("BluetoothDevice", localStringBuilder.toString());
      boolean bool = localIBluetooth.removeBond(this);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return false;
  }
  
  public boolean sdpSearch(ParcelUuid paramParcelUuid)
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot query remote device sdp records");
      return false;
    }
    try
    {
      boolean bool = localIBluetooth.sdpSearch(this, paramParcelUuid);
      return bool;
    }
    catch (RemoteException paramParcelUuid)
    {
      Log.e("BluetoothDevice", "", paramParcelUuid);
    }
    return false;
  }
  
  public boolean setAlias(String paramString)
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot set Remote Device name");
      return false;
    }
    try
    {
      boolean bool = localIBluetooth.setRemoteAlias(this, paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      Log.e("BluetoothDevice", "", paramString);
    }
    return false;
  }
  
  public boolean setDeviceOutOfBandData(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    return false;
  }
  
  public boolean setMessageAccessPermission(int paramInt)
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null) {
      return false;
    }
    try
    {
      boolean bool = localIBluetooth.setMessageAccessPermission(this, paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return false;
  }
  
  public boolean setPairingConfirmation(boolean paramBoolean)
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot set pairing confirmation");
      return false;
    }
    try
    {
      paramBoolean = localIBluetooth.setPairingConfirmation(this, paramBoolean);
      return paramBoolean;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return false;
  }
  
  public boolean setPasskey(int paramInt)
  {
    return false;
  }
  
  @SystemApi
  public boolean setPhonebookAccessPermission(int paramInt)
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null) {
      return false;
    }
    try
    {
      boolean bool = localIBluetooth.setPhonebookAccessPermission(this, paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return false;
  }
  
  public boolean setPin(byte[] paramArrayOfByte)
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null)
    {
      Log.e("BluetoothDevice", "BT not enabled. Cannot set Remote Device pin");
      return false;
    }
    try
    {
      boolean bool = localIBluetooth.setPin(this, true, paramArrayOfByte.length, paramArrayOfByte);
      return bool;
    }
    catch (RemoteException paramArrayOfByte)
    {
      Log.e("BluetoothDevice", "", paramArrayOfByte);
    }
    return false;
  }
  
  public boolean setRemoteOutOfBandData()
  {
    return false;
  }
  
  public boolean setSimAccessPermission(int paramInt)
  {
    IBluetooth localIBluetooth = sService;
    if (localIBluetooth == null) {
      return false;
    }
    try
    {
      boolean bool = localIBluetooth.setSimAccessPermission(this, paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("BluetoothDevice", "", localRemoteException);
    }
    return false;
  }
  
  public String toString()
  {
    return mAddress;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mAddress);
  }
}
