package android.bluetooth;

import android.app.PendingIntent;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.IAdvertisingSetCallback;
import android.bluetooth.le.IAdvertisingSetCallback.Stub;
import android.bluetooth.le.IPeriodicAdvertisingCallback;
import android.bluetooth.le.IPeriodicAdvertisingCallback.Stub;
import android.bluetooth.le.IScannerCallback;
import android.bluetooth.le.IScannerCallback.Stub;
import android.bluetooth.le.PeriodicAdvertisingParameters;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.WorkSource;
import java.util.List;

public abstract interface IBluetoothGatt
  extends IInterface
{
  public abstract void addService(int paramInt, BluetoothGattService paramBluetoothGattService)
    throws RemoteException;
  
  public abstract void beginReliableWrite(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void clearServices(int paramInt)
    throws RemoteException;
  
  public abstract void clientConnect(int paramInt1, String paramString, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, int paramInt3)
    throws RemoteException;
  
  public abstract void clientDisconnect(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void clientReadPhy(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void clientSetPreferredPhy(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void configureMTU(int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void connectionParameterUpdate(int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void disconnectAll()
    throws RemoteException;
  
  public abstract void discoverServiceByUuid(int paramInt, String paramString, ParcelUuid paramParcelUuid)
    throws RemoteException;
  
  public abstract void discoverServices(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void enableAdvertisingSet(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void endReliableWrite(int paramInt, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void flushPendingBatchResults(int paramInt)
    throws RemoteException;
  
  public abstract List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract void getOwnAddress(int paramInt)
    throws RemoteException;
  
  public abstract void leConnectionUpdate(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
    throws RemoteException;
  
  public abstract int numHwTrackFiltersAvailable()
    throws RemoteException;
  
  public abstract void readCharacteristic(int paramInt1, String paramString, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void readDescriptor(int paramInt1, String paramString, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void readRemoteRssi(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void readUsingCharacteristicUuid(int paramInt1, String paramString, ParcelUuid paramParcelUuid, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void refreshDevice(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void registerClient(ParcelUuid paramParcelUuid, IBluetoothGattCallback paramIBluetoothGattCallback)
    throws RemoteException;
  
  public abstract void registerForNotification(int paramInt1, String paramString, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void registerScanner(IScannerCallback paramIScannerCallback, WorkSource paramWorkSource)
    throws RemoteException;
  
  public abstract void registerServer(ParcelUuid paramParcelUuid, IBluetoothGattServerCallback paramIBluetoothGattServerCallback)
    throws RemoteException;
  
  public abstract void registerSync(ScanResult paramScanResult, int paramInt1, int paramInt2, IPeriodicAdvertisingCallback paramIPeriodicAdvertisingCallback)
    throws RemoteException;
  
  public abstract void removeService(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void sendNotification(int paramInt1, String paramString, int paramInt2, boolean paramBoolean, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void sendResponse(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void serverConnect(int paramInt1, String paramString, boolean paramBoolean, int paramInt2)
    throws RemoteException;
  
  public abstract void serverDisconnect(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void serverReadPhy(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void serverSetPreferredPhy(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void setAdvertisingData(int paramInt, AdvertiseData paramAdvertiseData)
    throws RemoteException;
  
  public abstract void setAdvertisingParameters(int paramInt, AdvertisingSetParameters paramAdvertisingSetParameters)
    throws RemoteException;
  
  public abstract void setPeriodicAdvertisingData(int paramInt, AdvertiseData paramAdvertiseData)
    throws RemoteException;
  
  public abstract void setPeriodicAdvertisingEnable(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPeriodicAdvertisingParameters(int paramInt, PeriodicAdvertisingParameters paramPeriodicAdvertisingParameters)
    throws RemoteException;
  
  public abstract void setScanResponseData(int paramInt, AdvertiseData paramAdvertiseData)
    throws RemoteException;
  
  public abstract void startAdvertisingSet(AdvertisingSetParameters paramAdvertisingSetParameters, AdvertiseData paramAdvertiseData1, AdvertiseData paramAdvertiseData2, PeriodicAdvertisingParameters paramPeriodicAdvertisingParameters, AdvertiseData paramAdvertiseData3, int paramInt1, int paramInt2, IAdvertisingSetCallback paramIAdvertisingSetCallback)
    throws RemoteException;
  
  public abstract void startScan(int paramInt, ScanSettings paramScanSettings, List<ScanFilter> paramList, List paramList1, String paramString)
    throws RemoteException;
  
  public abstract void startScanForIntent(PendingIntent paramPendingIntent, ScanSettings paramScanSettings, List<ScanFilter> paramList, String paramString)
    throws RemoteException;
  
  public abstract void stopAdvertisingSet(IAdvertisingSetCallback paramIAdvertisingSetCallback)
    throws RemoteException;
  
  public abstract void stopScan(int paramInt)
    throws RemoteException;
  
  public abstract void stopScanForIntent(PendingIntent paramPendingIntent, String paramString)
    throws RemoteException;
  
  public abstract void unregAll()
    throws RemoteException;
  
  public abstract void unregisterClient(int paramInt)
    throws RemoteException;
  
  public abstract void unregisterScanner(int paramInt)
    throws RemoteException;
  
  public abstract void unregisterServer(int paramInt)
    throws RemoteException;
  
  public abstract void unregisterSync(IPeriodicAdvertisingCallback paramIPeriodicAdvertisingCallback)
    throws RemoteException;
  
  public abstract void writeCharacteristic(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void writeDescriptor(int paramInt1, String paramString, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothGatt
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothGatt";
    static final int TRANSACTION_addService = 48;
    static final int TRANSACTION_beginReliableWrite = 36;
    static final int TRANSACTION_clearServices = 50;
    static final int TRANSACTION_clientConnect = 23;
    static final int TRANSACTION_clientDisconnect = 24;
    static final int TRANSACTION_clientReadPhy = 26;
    static final int TRANSACTION_clientSetPreferredPhy = 25;
    static final int TRANSACTION_configureMTU = 39;
    static final int TRANSACTION_connectionParameterUpdate = 40;
    static final int TRANSACTION_disconnectAll = 53;
    static final int TRANSACTION_discoverServiceByUuid = 29;
    static final int TRANSACTION_discoverServices = 28;
    static final int TRANSACTION_enableAdvertisingSet = 12;
    static final int TRANSACTION_endReliableWrite = 37;
    static final int TRANSACTION_flushPendingBatchResults = 8;
    static final int TRANSACTION_getDevicesMatchingConnectionStates = 1;
    static final int TRANSACTION_getOwnAddress = 11;
    static final int TRANSACTION_leConnectionUpdate = 41;
    static final int TRANSACTION_numHwTrackFiltersAvailable = 55;
    static final int TRANSACTION_readCharacteristic = 30;
    static final int TRANSACTION_readDescriptor = 33;
    static final int TRANSACTION_readRemoteRssi = 38;
    static final int TRANSACTION_readUsingCharacteristicUuid = 31;
    static final int TRANSACTION_refreshDevice = 27;
    static final int TRANSACTION_registerClient = 21;
    static final int TRANSACTION_registerForNotification = 35;
    static final int TRANSACTION_registerScanner = 2;
    static final int TRANSACTION_registerServer = 42;
    static final int TRANSACTION_registerSync = 19;
    static final int TRANSACTION_removeService = 49;
    static final int TRANSACTION_sendNotification = 52;
    static final int TRANSACTION_sendResponse = 51;
    static final int TRANSACTION_serverConnect = 44;
    static final int TRANSACTION_serverDisconnect = 45;
    static final int TRANSACTION_serverReadPhy = 47;
    static final int TRANSACTION_serverSetPreferredPhy = 46;
    static final int TRANSACTION_setAdvertisingData = 13;
    static final int TRANSACTION_setAdvertisingParameters = 15;
    static final int TRANSACTION_setPeriodicAdvertisingData = 17;
    static final int TRANSACTION_setPeriodicAdvertisingEnable = 18;
    static final int TRANSACTION_setPeriodicAdvertisingParameters = 16;
    static final int TRANSACTION_setScanResponseData = 14;
    static final int TRANSACTION_startAdvertisingSet = 9;
    static final int TRANSACTION_startScan = 4;
    static final int TRANSACTION_startScanForIntent = 5;
    static final int TRANSACTION_stopAdvertisingSet = 10;
    static final int TRANSACTION_stopScan = 7;
    static final int TRANSACTION_stopScanForIntent = 6;
    static final int TRANSACTION_unregAll = 54;
    static final int TRANSACTION_unregisterClient = 22;
    static final int TRANSACTION_unregisterScanner = 3;
    static final int TRANSACTION_unregisterServer = 43;
    static final int TRANSACTION_unregisterSync = 20;
    static final int TRANSACTION_writeCharacteristic = 32;
    static final int TRANSACTION_writeDescriptor = 34;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothGatt");
    }
    
    public static IBluetoothGatt asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothGatt");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothGatt))) {
        return (IBluetoothGatt)localIInterface;
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
        boolean bool4 = false;
        boolean bool5 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        AdvertiseData localAdvertiseData1 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        AdvertiseData localAdvertiseData2 = null;
        PeriodicAdvertisingParameters localPeriodicAdvertisingParameters = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 55: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = numHwTrackFiltersAvailable();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 54: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          unregAll();
          paramParcel2.writeNoException();
          return true;
        case 53: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          disconnectAll();
          paramParcel2.writeNoException();
          return true;
        case 52: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt2 = paramParcel1.readInt();
          localObject2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          } else {
            bool5 = false;
          }
          sendNotification(paramInt2, (String)localObject2, paramInt1, bool5, paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 51: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          sendResponse(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 50: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          clearServices(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 49: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          removeService(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothGattService)BluetoothGattService.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localPeriodicAdvertisingParameters;
          }
          addService(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          serverReadPhy(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          serverSetPreferredPhy(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          serverDisconnect(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          serverConnect(paramInt1, (String)localObject2, bool5, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          unregisterServer(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject1;
          }
          registerServer((ParcelUuid)localObject2, IBluetoothGattServerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          leConnectionUpdate(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          connectionParameterUpdate(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          configureMTU(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          readRemoteRssi(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          localObject2 = paramParcel1.readString();
          bool5 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          endReliableWrite(paramInt1, (String)localObject2, bool5);
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          beginReliableWrite(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          localObject2 = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          bool5 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          registerForNotification(paramInt1, (String)localObject2, paramInt2, bool5);
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          writeDescriptor(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          readDescriptor(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          writeCharacteristic(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          localObject11 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          readUsingCharacteristicUuid(paramInt1, (String)localObject11, (ParcelUuid)localObject2, paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          readCharacteristic(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          localObject11 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject2;
          }
          discoverServiceByUuid(paramInt1, (String)localObject11, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          discoverServices(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          refreshDevice(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          clientReadPhy(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          clientSetPreferredPhy(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          clientDisconnect(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt2 = paramParcel1.readInt();
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          } else {
            bool5 = false;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          clientConnect(paramInt2, (String)localObject2, bool5, paramInt1, bool1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          unregisterClient(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject3;
          }
          registerClient((ParcelUuid)localObject2, IBluetoothGattCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          unregisterSync(IPeriodicAdvertisingCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ScanResult)ScanResult.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject4;
          }
          registerSync((ScanResult)localObject2, paramParcel1.readInt(), paramParcel1.readInt(), IPeriodicAdvertisingCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          bool5 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          setPeriodicAdvertisingEnable(paramInt1, bool5);
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AdvertiseData)AdvertiseData.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          setPeriodicAdvertisingData(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PeriodicAdvertisingParameters)PeriodicAdvertisingParameters.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          setPeriodicAdvertisingParameters(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AdvertisingSetParameters)AdvertisingSetParameters.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          setAdvertisingParameters(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AdvertiseData)AdvertiseData.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          setScanResponseData(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AdvertiseData)AdvertiseData.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          setAdvertisingData(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          bool5 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          enableAdvertisingSet(paramInt1, bool5, paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          getOwnAddress(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          stopAdvertisingSet(IAdvertisingSetCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (AdvertisingSetParameters)AdvertisingSetParameters.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject11 = (AdvertiseData)AdvertiseData.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject11 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localAdvertiseData2 = (AdvertiseData)AdvertiseData.CREATOR.createFromParcel(paramParcel1);
          } else {
            localAdvertiseData2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localPeriodicAdvertisingParameters = (PeriodicAdvertisingParameters)PeriodicAdvertisingParameters.CREATOR.createFromParcel(paramParcel1);
          } else {
            localPeriodicAdvertisingParameters = null;
          }
          if (paramParcel1.readInt() != 0) {
            localAdvertiseData1 = (AdvertiseData)AdvertiseData.CREATOR.createFromParcel(paramParcel1);
          }
          startAdvertisingSet((AdvertisingSetParameters)localObject2, (AdvertiseData)localObject11, localAdvertiseData2, localPeriodicAdvertisingParameters, localAdvertiseData1, paramParcel1.readInt(), paramParcel1.readInt(), IAdvertisingSetCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          flushPendingBatchResults(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          stopScan(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject10;
          }
          stopScanForIntent((PendingIntent)localObject2, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject11 = (ScanSettings)ScanSettings.CREATOR.createFromParcel(paramParcel1);
          }
          startScanForIntent((PendingIntent)localObject2, (ScanSettings)localObject11, paramParcel1.createTypedArrayList(ScanFilter.CREATOR), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ScanSettings)ScanSettings.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          startScan(paramInt1, (ScanSettings)localObject2, paramParcel1.createTypedArrayList(ScanFilter.CREATOR), paramParcel1.readArrayList(getClass().getClassLoader()), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          unregisterScanner(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
          localObject2 = IScannerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localAdvertiseData2;
          }
          registerScanner((IScannerCallback)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothGatt");
        paramParcel1 = getDevicesMatchingConnectionStates(paramParcel1.createIntArray());
        paramParcel2.writeNoException();
        paramParcel2.writeTypedList(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothGatt");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothGatt
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addService(int paramInt, BluetoothGattService paramBluetoothGattService)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          if (paramBluetoothGattService != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothGattService.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void beginReliableWrite(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearServices(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clientConnect(int paramInt1, String paramString, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clientDisconnect(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clientReadPhy(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clientSetPreferredPhy(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void configureMTU(int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void connectionParameterUpdate(int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disconnectAll()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
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
      
      public void discoverServiceByUuid(int paramInt, String paramString, ParcelUuid paramParcelUuid)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void discoverServices(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enableAdvertisingSet(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void endReliableWrite(int paramInt, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void flushPendingBatchResults(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfInt = localParcel2.createTypedArrayList(BluetoothDevice.CREATOR);
          return paramArrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.bluetooth.IBluetoothGatt";
      }
      
      public void getOwnAddress(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void leConnectionUpdate(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeInt(paramInt5);
          localParcel1.writeInt(paramInt6);
          localParcel1.writeInt(paramInt7);
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int numHwTrackFiltersAvailable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          mRemote.transact(55, localParcel1, localParcel2, 0);
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
      
      public void readCharacteristic(int paramInt1, String paramString, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void readDescriptor(int paramInt1, String paramString, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void readRemoteRssi(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void readUsingCharacteristicUuid(int paramInt1, String paramString, ParcelUuid paramParcelUuid, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void refreshDevice(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerClient(ParcelUuid paramParcelUuid, IBluetoothGattCallback paramIBluetoothGattCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIBluetoothGattCallback != null) {
            paramParcelUuid = paramIBluetoothGattCallback.asBinder();
          } else {
            paramParcelUuid = null;
          }
          localParcel1.writeStrongBinder(paramParcelUuid);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerForNotification(int paramInt1, String paramString, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerScanner(IScannerCallback paramIScannerCallback, WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          if (paramIScannerCallback != null) {
            paramIScannerCallback = paramIScannerCallback.asBinder();
          } else {
            paramIScannerCallback = null;
          }
          localParcel1.writeStrongBinder(paramIScannerCallback);
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerServer(ParcelUuid paramParcelUuid, IBluetoothGattServerCallback paramIBluetoothGattServerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIBluetoothGattServerCallback != null) {
            paramParcelUuid = paramIBluetoothGattServerCallback.asBinder();
          } else {
            paramParcelUuid = null;
          }
          localParcel1.writeStrongBinder(paramParcelUuid);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerSync(ScanResult paramScanResult, int paramInt1, int paramInt2, IPeriodicAdvertisingCallback paramIPeriodicAdvertisingCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          if (paramScanResult != null)
          {
            localParcel1.writeInt(1);
            paramScanResult.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramIPeriodicAdvertisingCallback != null) {
            paramScanResult = paramIPeriodicAdvertisingCallback.asBinder();
          } else {
            paramScanResult = null;
          }
          localParcel1.writeStrongBinder(paramScanResult);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeService(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendNotification(int paramInt1, String paramString, int paramInt2, boolean paramBoolean, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeByteArray(paramArrayOfByte);
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
      
      public void sendResponse(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void serverConnect(int paramInt1, String paramString, boolean paramBoolean, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void serverDisconnect(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void serverReadPhy(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void serverSetPreferredPhy(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAdvertisingData(int paramInt, AdvertiseData paramAdvertiseData)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          if (paramAdvertiseData != null)
          {
            localParcel1.writeInt(1);
            paramAdvertiseData.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAdvertisingParameters(int paramInt, AdvertisingSetParameters paramAdvertisingSetParameters)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          if (paramAdvertisingSetParameters != null)
          {
            localParcel1.writeInt(1);
            paramAdvertisingSetParameters.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPeriodicAdvertisingData(int paramInt, AdvertiseData paramAdvertiseData)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          if (paramAdvertiseData != null)
          {
            localParcel1.writeInt(1);
            paramAdvertiseData.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPeriodicAdvertisingEnable(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPeriodicAdvertisingParameters(int paramInt, PeriodicAdvertisingParameters paramPeriodicAdvertisingParameters)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          if (paramPeriodicAdvertisingParameters != null)
          {
            localParcel1.writeInt(1);
            paramPeriodicAdvertisingParameters.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setScanResponseData(int paramInt, AdvertiseData paramAdvertiseData)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          if (paramAdvertiseData != null)
          {
            localParcel1.writeInt(1);
            paramAdvertiseData.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startAdvertisingSet(AdvertisingSetParameters paramAdvertisingSetParameters, AdvertiseData paramAdvertiseData1, AdvertiseData paramAdvertiseData2, PeriodicAdvertisingParameters paramPeriodicAdvertisingParameters, AdvertiseData paramAdvertiseData3, int paramInt1, int paramInt2, IAdvertisingSetCallback paramIAdvertisingSetCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          if (paramAdvertisingSetParameters != null)
          {
            localParcel1.writeInt(1);
            paramAdvertisingSetParameters.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramAdvertiseData1 != null)
          {
            localParcel1.writeInt(1);
            paramAdvertiseData1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramAdvertiseData2 != null)
          {
            localParcel1.writeInt(1);
            paramAdvertiseData2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPeriodicAdvertisingParameters != null)
          {
            localParcel1.writeInt(1);
            paramPeriodicAdvertisingParameters.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramAdvertiseData3 != null)
          {
            localParcel1.writeInt(1);
            paramAdvertiseData3.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramIAdvertisingSetCallback != null) {
            paramAdvertisingSetParameters = paramIAdvertisingSetCallback.asBinder();
          } else {
            paramAdvertisingSetParameters = null;
          }
          localParcel1.writeStrongBinder(paramAdvertisingSetParameters);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startScan(int paramInt, ScanSettings paramScanSettings, List<ScanFilter> paramList, List paramList1, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          if (paramScanSettings != null)
          {
            localParcel1.writeInt(1);
            paramScanSettings.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeTypedList(paramList);
          localParcel1.writeList(paramList1);
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startScanForIntent(PendingIntent paramPendingIntent, ScanSettings paramScanSettings, List<ScanFilter> paramList, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramScanSettings != null)
          {
            localParcel1.writeInt(1);
            paramScanSettings.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeTypedList(paramList);
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopAdvertisingSet(IAdvertisingSetCallback paramIAdvertisingSetCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          if (paramIAdvertisingSetCallback != null) {
            paramIAdvertisingSetCallback = paramIAdvertisingSetCallback.asBinder();
          } else {
            paramIAdvertisingSetCallback = null;
          }
          localParcel1.writeStrongBinder(paramIAdvertisingSetCallback);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopScan(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopScanForIntent(PendingIntent paramPendingIntent, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregAll()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
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
      
      public void unregisterClient(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterScanner(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterServer(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt);
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterSync(IPeriodicAdvertisingCallback paramIPeriodicAdvertisingCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          if (paramIPeriodicAdvertisingCallback != null) {
            paramIPeriodicAdvertisingCallback = paramIPeriodicAdvertisingCallback.asBinder();
          } else {
            paramIPeriodicAdvertisingCallback = null;
          }
          localParcel1.writeStrongBinder(paramIPeriodicAdvertisingCallback);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void writeCharacteristic(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void writeDescriptor(int paramInt1, String paramString, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothGatt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(34, localParcel1, localParcel2, 0);
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
