package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;

public abstract interface IBluetooth
  extends IInterface
{
  public abstract boolean cancelBondProcess(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean cancelDiscovery()
    throws RemoteException;
  
  public abstract boolean createBond(BluetoothDevice paramBluetoothDevice, int paramInt)
    throws RemoteException;
  
  public abstract boolean createBondOutOfBand(BluetoothDevice paramBluetoothDevice, int paramInt, OobData paramOobData)
    throws RemoteException;
  
  public abstract boolean disable()
    throws RemoteException;
  
  public abstract boolean enable()
    throws RemoteException;
  
  public abstract boolean enableNoAutoConnect()
    throws RemoteException;
  
  public abstract boolean factoryReset()
    throws RemoteException;
  
  public abstract boolean fetchRemoteUuids(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract int getAdapterConnectionState()
    throws RemoteException;
  
  public abstract String getAddress()
    throws RemoteException;
  
  public abstract int getBatteryLevel(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract BluetoothClass getBluetoothClass()
    throws RemoteException;
  
  public abstract int getBondState(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract BluetoothDevice[] getBondedDevices()
    throws RemoteException;
  
  public abstract int getConnectionState(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract int getDiscoverableTimeout()
    throws RemoteException;
  
  public abstract long getDiscoveryEndMillis()
    throws RemoteException;
  
  public abstract int getLeMaximumAdvertisingDataLength()
    throws RemoteException;
  
  public abstract int getMaxConnectedAudioDevices()
    throws RemoteException;
  
  public abstract int getMessageAccessPermission(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract String getName()
    throws RemoteException;
  
  public abstract int getPhonebookAccessPermission(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract int getProfileConnectionState(int paramInt)
    throws RemoteException;
  
  public abstract String getRemoteAlias(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract int getRemoteClass(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract String getRemoteName(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract int getRemoteType(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract ParcelUuid[] getRemoteUuids(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract int getScanMode()
    throws RemoteException;
  
  public abstract int getSimAccessPermission(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract IBluetoothSocketManager getSocketManager()
    throws RemoteException;
  
  public abstract int getSocketOpt(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract int getState()
    throws RemoteException;
  
  public abstract long getSupportedProfiles()
    throws RemoteException;
  
  public abstract String getTwsPlusPeerAddress(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract ParcelUuid[] getUuids()
    throws RemoteException;
  
  public abstract boolean isActivityAndEnergyReportingSupported()
    throws RemoteException;
  
  public abstract boolean isBondingInitiatedLocally(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean isDiscovering()
    throws RemoteException;
  
  public abstract boolean isEnabled()
    throws RemoteException;
  
  public abstract boolean isLe2MPhySupported()
    throws RemoteException;
  
  public abstract boolean isLeCodedPhySupported()
    throws RemoteException;
  
  public abstract boolean isLeExtendedAdvertisingSupported()
    throws RemoteException;
  
  public abstract boolean isLePeriodicAdvertisingSupported()
    throws RemoteException;
  
  public abstract boolean isMultiAdvertisementSupported()
    throws RemoteException;
  
  public abstract boolean isOffloadedFilteringSupported()
    throws RemoteException;
  
  public abstract boolean isOffloadedScanBatchingSupported()
    throws RemoteException;
  
  public abstract boolean isTwsPlusDevice(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract void onBrEdrDown()
    throws RemoteException;
  
  public abstract void onLeServiceUp()
    throws RemoteException;
  
  public abstract void registerCallback(IBluetoothCallback paramIBluetoothCallback)
    throws RemoteException;
  
  public abstract boolean removeBond(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract BluetoothActivityEnergyInfo reportActivityInfo()
    throws RemoteException;
  
  public abstract void requestActivityInfo(ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public abstract boolean sdpSearch(BluetoothDevice paramBluetoothDevice, ParcelUuid paramParcelUuid)
    throws RemoteException;
  
  public abstract void sendConnectionStateChange(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract boolean setBluetoothClass(BluetoothClass paramBluetoothClass)
    throws RemoteException;
  
  public abstract boolean setDiscoverableTimeout(int paramInt)
    throws RemoteException;
  
  public abstract boolean setMessageAccessPermission(BluetoothDevice paramBluetoothDevice, int paramInt)
    throws RemoteException;
  
  public abstract boolean setName(String paramString)
    throws RemoteException;
  
  public abstract boolean setPairingConfirmation(BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setPasskey(BluetoothDevice paramBluetoothDevice, boolean paramBoolean, int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract boolean setPhonebookAccessPermission(BluetoothDevice paramBluetoothDevice, int paramInt)
    throws RemoteException;
  
  public abstract boolean setPin(BluetoothDevice paramBluetoothDevice, boolean paramBoolean, int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract boolean setRemoteAlias(BluetoothDevice paramBluetoothDevice, String paramString)
    throws RemoteException;
  
  public abstract boolean setScanMode(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean setSimAccessPermission(BluetoothDevice paramBluetoothDevice, int paramInt)
    throws RemoteException;
  
  public abstract int setSocketOpt(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int paramInt4)
    throws RemoteException;
  
  public abstract boolean startDiscovery()
    throws RemoteException;
  
  public abstract void unregisterCallback(IBluetoothCallback paramIBluetoothCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetooth
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetooth";
    static final int TRANSACTION_cancelBondProcess = 25;
    static final int TRANSACTION_cancelDiscovery = 17;
    static final int TRANSACTION_createBond = 23;
    static final int TRANSACTION_createBondOutOfBand = 24;
    static final int TRANSACTION_disable = 5;
    static final int TRANSACTION_enable = 3;
    static final int TRANSACTION_enableNoAutoConnect = 4;
    static final int TRANSACTION_factoryReset = 56;
    static final int TRANSACTION_fetchRemoteUuids = 37;
    static final int TRANSACTION_getAdapterConnectionState = 20;
    static final int TRANSACTION_getAddress = 6;
    static final int TRANSACTION_getBatteryLevel = 39;
    static final int TRANSACTION_getBluetoothClass = 10;
    static final int TRANSACTION_getBondState = 27;
    static final int TRANSACTION_getBondedDevices = 22;
    static final int TRANSACTION_getConnectionState = 30;
    static final int TRANSACTION_getDiscoverableTimeout = 14;
    static final int TRANSACTION_getDiscoveryEndMillis = 19;
    static final int TRANSACTION_getLeMaximumAdvertisingDataLength = 65;
    static final int TRANSACTION_getMaxConnectedAudioDevices = 40;
    static final int TRANSACTION_getMessageAccessPermission = 48;
    static final int TRANSACTION_getName = 9;
    static final int TRANSACTION_getPhonebookAccessPermission = 46;
    static final int TRANSACTION_getProfileConnectionState = 21;
    static final int TRANSACTION_getRemoteAlias = 33;
    static final int TRANSACTION_getRemoteClass = 35;
    static final int TRANSACTION_getRemoteName = 31;
    static final int TRANSACTION_getRemoteType = 32;
    static final int TRANSACTION_getRemoteUuids = 36;
    static final int TRANSACTION_getScanMode = 12;
    static final int TRANSACTION_getSimAccessPermission = 50;
    static final int TRANSACTION_getSocketManager = 55;
    static final int TRANSACTION_getSocketOpt = 71;
    static final int TRANSACTION_getState = 2;
    static final int TRANSACTION_getSupportedProfiles = 29;
    static final int TRANSACTION_getTwsPlusPeerAddress = 42;
    static final int TRANSACTION_getUuids = 7;
    static final int TRANSACTION_isActivityAndEnergyReportingSupported = 60;
    static final int TRANSACTION_isBondingInitiatedLocally = 28;
    static final int TRANSACTION_isDiscovering = 18;
    static final int TRANSACTION_isEnabled = 1;
    static final int TRANSACTION_isLe2MPhySupported = 61;
    static final int TRANSACTION_isLeCodedPhySupported = 62;
    static final int TRANSACTION_isLeExtendedAdvertisingSupported = 63;
    static final int TRANSACTION_isLePeriodicAdvertisingSupported = 64;
    static final int TRANSACTION_isMultiAdvertisementSupported = 57;
    static final int TRANSACTION_isOffloadedFilteringSupported = 58;
    static final int TRANSACTION_isOffloadedScanBatchingSupported = 59;
    static final int TRANSACTION_isTwsPlusDevice = 41;
    static final int TRANSACTION_onBrEdrDown = 69;
    static final int TRANSACTION_onLeServiceUp = 68;
    static final int TRANSACTION_registerCallback = 53;
    static final int TRANSACTION_removeBond = 26;
    static final int TRANSACTION_reportActivityInfo = 66;
    static final int TRANSACTION_requestActivityInfo = 67;
    static final int TRANSACTION_sdpSearch = 38;
    static final int TRANSACTION_sendConnectionStateChange = 52;
    static final int TRANSACTION_setBluetoothClass = 11;
    static final int TRANSACTION_setDiscoverableTimeout = 15;
    static final int TRANSACTION_setMessageAccessPermission = 49;
    static final int TRANSACTION_setName = 8;
    static final int TRANSACTION_setPairingConfirmation = 45;
    static final int TRANSACTION_setPasskey = 44;
    static final int TRANSACTION_setPhonebookAccessPermission = 47;
    static final int TRANSACTION_setPin = 43;
    static final int TRANSACTION_setRemoteAlias = 34;
    static final int TRANSACTION_setScanMode = 13;
    static final int TRANSACTION_setSimAccessPermission = 51;
    static final int TRANSACTION_setSocketOpt = 70;
    static final int TRANSACTION_startDiscovery = 16;
    static final int TRANSACTION_unregisterCallback = 54;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetooth");
    }
    
    public static IBluetooth asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetooth");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetooth))) {
        return (IBluetooth)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        Object localObject14 = null;
        IBluetoothSocketManager localIBluetoothSocketManager = null;
        Object localObject15 = null;
        Object localObject16 = null;
        Object localObject17 = null;
        Object localObject18 = null;
        Object localObject19 = null;
        Object localObject20 = null;
        Object localObject21 = null;
        Object localObject22 = null;
        Object localObject23 = null;
        Object localObject24 = null;
        Object localObject25 = null;
        Object localObject26 = null;
        Object localObject27 = null;
        Object localObject28 = null;
        Object localObject29 = null;
        Object localObject30 = null;
        long l;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 71: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          int i = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          int j = paramParcel1.readInt();
          if (j < 0) {
            paramParcel1 = null;
          } else {
            paramParcel1 = new byte[j];
          }
          paramInt1 = getSocketOpt(i, paramInt2, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 70: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = setSocketOpt(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 69: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          onBrEdrDown();
          paramParcel2.writeNoException();
          return true;
        case 68: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          onLeServiceUp();
          paramParcel2.writeNoException();
          return true;
        case 67: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject30;
          }
          requestActivityInfo(paramParcel1);
          return true;
        case 66: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramParcel1 = reportActivityInfo();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 65: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = getLeMaximumAdvertisingDataLength();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 64: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = isLePeriodicAdvertisingSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 63: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = isLeExtendedAdvertisingSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 62: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = isLeCodedPhySupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 61: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = isLe2MPhySupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 60: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = isActivityAndEnergyReportingSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 59: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = isOffloadedScanBatchingSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 58: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = isOffloadedFilteringSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 57: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = isMultiAdvertisementSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 56: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = factoryReset();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 55: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          localIBluetoothSocketManager = getSocketManager();
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject1;
          if (localIBluetoothSocketManager != null) {
            paramParcel1 = localIBluetoothSocketManager.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 54: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          unregisterCallback(IBluetoothCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 53: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          registerCallback(IBluetoothCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 52: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject2;
          }
          sendConnectionStateChange((BluetoothDevice)localObject1, paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 51: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject3;
          }
          paramInt1 = setSimAccessPermission((BluetoothDevice)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 50: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          paramInt1 = getSimAccessPermission(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 49: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject5;
          }
          paramInt1 = setMessageAccessPermission((BluetoothDevice)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          paramInt1 = getMessageAccessPermission(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject7;
          }
          paramInt1 = setPhonebookAccessPermission((BluetoothDevice)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          paramInt1 = getPhonebookAccessPermission(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject9;
          }
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          paramInt1 = setPairingConfirmation((BluetoothDevice)localObject1, bool3);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject10;
          }
          bool3 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          paramInt1 = setPasskey((BluetoothDevice)localObject1, bool3, paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject11;
          }
          bool3 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          paramInt1 = setPin((BluetoothDevice)localObject1, bool3, paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          paramParcel1 = getTwsPlusPeerAddress(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject13;
          }
          paramInt1 = isTwsPlusDevice(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = getMaxConnectedAudioDevices();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject14;
          }
          paramInt1 = getBatteryLevel(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIBluetoothSocketManager;
          }
          paramInt1 = sdpSearch((BluetoothDevice)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject15;
          }
          paramInt1 = fetchRemoteUuids(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject16;
          }
          paramParcel1 = getRemoteUuids(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject17;
          }
          paramInt1 = getRemoteClass(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject18;
          }
          paramInt1 = setRemoteAlias((BluetoothDevice)localObject1, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject19;
          }
          paramParcel1 = getRemoteAlias(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject20;
          }
          paramInt1 = getRemoteType(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject21;
          }
          paramParcel1 = getRemoteName(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject22;
          }
          paramInt1 = getConnectionState(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          l = getSupportedProfiles();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject23;
          }
          paramInt1 = isBondingInitiatedLocally(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject24;
          }
          paramInt1 = getBondState(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject25;
          }
          paramInt1 = removeBond(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject26;
          }
          paramInt1 = cancelBondProcess(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (OobData)OobData.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject27;
          }
          paramInt1 = createBondOutOfBand((BluetoothDevice)localObject1, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject28;
          }
          paramInt1 = createBond((BluetoothDevice)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramParcel1 = getBondedDevices();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = getProfileConnectionState(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = getAdapterConnectionState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          l = getDiscoveryEndMillis();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = isDiscovering();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = cancelDiscovery();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = startDiscovery();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = setDiscoverableTimeout(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = getDiscoverableTimeout();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = setScanMode(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = getScanMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothClass)BluetoothClass.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject29;
          }
          paramInt1 = setBluetoothClass(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramParcel1 = getBluetoothClass();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramParcel1 = getName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = setName(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramParcel1 = getUuids();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramParcel1 = getAddress();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = disable();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = enableNoAutoConnect();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = enable();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
          paramInt1 = getState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetooth");
        paramInt1 = isEnabled();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetooth");
      return true;
    }
    
    private static class Proxy
      implements IBluetooth
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean cancelBondProcess(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean cancelDiscovery()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean createBond(BluetoothDevice paramBluetoothDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean createBondOutOfBand(BluetoothDevice paramBluetoothDevice, int paramInt, OobData paramOobData)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          if (paramOobData != null)
          {
            localParcel1.writeInt(1);
            paramOobData.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean disable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean enable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean enableNoAutoConnect()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean factoryReset()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(56, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean fetchRemoteUuids(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getAdapterConnectionState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getAddress()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getBatteryLevel(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public BluetoothClass getBluetoothClass()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          BluetoothClass localBluetoothClass;
          if (localParcel2.readInt() != 0) {
            localBluetoothClass = (BluetoothClass)BluetoothClass.CREATOR.createFromParcel(localParcel2);
          } else {
            localBluetoothClass = null;
          }
          return localBluetoothClass;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getBondState(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public BluetoothDevice[] getBondedDevices()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          BluetoothDevice[] arrayOfBluetoothDevice = (BluetoothDevice[])localParcel2.createTypedArray(BluetoothDevice.CREATOR);
          return arrayOfBluetoothDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getConnectionState(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getDiscoverableTimeout()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getDiscoveryEndMillis()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.bluetooth.IBluetooth";
      }
      
      public int getLeMaximumAdvertisingDataLength()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(65, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getMaxConnectedAudioDevices()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getMessageAccessPermission(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPhonebookAccessPermission(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getProfileConnectionState(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          localParcel1.writeInt(paramInt);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getRemoteAlias(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBluetoothDevice = localParcel2.readString();
          return paramBluetoothDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getRemoteClass(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getRemoteName(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBluetoothDevice = localParcel2.readString();
          return paramBluetoothDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getRemoteType(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParcelUuid[] getRemoteUuids(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBluetoothDevice = (ParcelUuid[])localParcel2.createTypedArray(ParcelUuid.CREATOR);
          return paramBluetoothDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getScanMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getSimAccessPermission(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBluetoothSocketManager getSocketManager()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(55, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IBluetoothSocketManager localIBluetoothSocketManager = IBluetoothSocketManager.Stub.asInterface(localParcel2.readStrongBinder());
          return localIBluetoothSocketManager;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getSocketOpt(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          if (paramArrayOfByte == null) {
            localParcel1.writeInt(-1);
          } else {
            localParcel1.writeInt(paramArrayOfByte.length);
          }
          mRemote.transact(71, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          localParcel2.readByteArray(paramArrayOfByte);
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getSupportedProfiles()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getTwsPlusPeerAddress(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBluetoothDevice = localParcel2.readString();
          return paramBluetoothDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParcelUuid[] getUuids()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParcelUuid[] arrayOfParcelUuid = (ParcelUuid[])localParcel2.createTypedArray(ParcelUuid.CREATOR);
          return arrayOfParcelUuid;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isActivityAndEnergyReportingSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(60, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isBondingInitiatedLocally(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isDiscovering()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isLe2MPhySupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(61, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isLeCodedPhySupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(62, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isLeExtendedAdvertisingSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(63, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isLePeriodicAdvertisingSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(64, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isMultiAdvertisementSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(57, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isOffloadedFilteringSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(58, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isOffloadedScanBatchingSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(59, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isTwsPlusDevice(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onBrEdrDown()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(69, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onLeServiceUp()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(68, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerCallback(IBluetoothCallback paramIBluetoothCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramIBluetoothCallback != null) {
            paramIBluetoothCallback = paramIBluetoothCallback.asBinder();
          } else {
            paramIBluetoothCallback = null;
          }
          localParcel1.writeStrongBinder(paramIBluetoothCallback);
          mRemote.transact(53, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean removeBond(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public BluetoothActivityEnergyInfo reportActivityInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          mRemote.transact(66, localParcel1, localParcel2, 0);
          localParcel2.readException();
          BluetoothActivityEnergyInfo localBluetoothActivityEnergyInfo;
          if (localParcel2.readInt() != 0) {
            localBluetoothActivityEnergyInfo = (BluetoothActivityEnergyInfo)BluetoothActivityEnergyInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localBluetoothActivityEnergyInfo = null;
          }
          return localBluetoothActivityEnergyInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestActivityInfo(ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramResultReceiver != null)
          {
            localParcel.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(67, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public boolean sdpSearch(BluetoothDevice paramBluetoothDevice, ParcelUuid paramParcelUuid)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendConnectionStateChange(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(52, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setBluetoothClass(BluetoothClass paramBluetoothClass)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothClass != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothClass.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setDiscoverableTimeout(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setMessageAccessPermission(BluetoothDevice paramBluetoothDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setName(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setPairingConfirmation(BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setPasskey(BluetoothDevice paramBluetoothDevice, boolean paramBoolean, int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setPhonebookAccessPermission(BluetoothDevice paramBluetoothDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setPin(BluetoothDevice paramBluetoothDevice, boolean paramBoolean, int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setRemoteAlias(BluetoothDevice paramBluetoothDevice, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setScanMode(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setSimAccessPermission(BluetoothDevice paramBluetoothDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int setSocketOpt(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(70, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean startDiscovery()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterCallback(IBluetoothCallback paramIBluetoothCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetooth");
          if (paramIBluetoothCallback != null) {
            paramIBluetoothCallback = paramIBluetoothCallback.asBinder();
          } else {
            paramIBluetoothCallback = null;
          }
          localParcel1.writeStrongBinder(paramIBluetoothCallback);
          mRemote.transact(54, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
