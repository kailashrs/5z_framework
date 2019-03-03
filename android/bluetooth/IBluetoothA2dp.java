package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IBluetoothA2dp
  extends IInterface
{
  public abstract boolean connect(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract void disableOptionalCodecs(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean disconnect(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract void enableOptionalCodecs(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract BluetoothDevice getActiveDevice()
    throws RemoteException;
  
  public abstract BluetoothCodecStatus getCodecStatus(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract List<BluetoothDevice> getConnectedDevices()
    throws RemoteException;
  
  public abstract int getConnectionState(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract int getOptionalCodecsEnabled(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract int getPriority(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean isA2dpPlaying(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean isAvrcpAbsoluteVolumeSupported()
    throws RemoteException;
  
  public abstract boolean setActiveDevice(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract void setAvrcpAbsoluteVolume(int paramInt)
    throws RemoteException;
  
  public abstract void setCodecConfigPreference(BluetoothDevice paramBluetoothDevice, BluetoothCodecConfig paramBluetoothCodecConfig)
    throws RemoteException;
  
  public abstract void setOptionalCodecsEnabled(BluetoothDevice paramBluetoothDevice, int paramInt)
    throws RemoteException;
  
  public abstract boolean setPriority(BluetoothDevice paramBluetoothDevice, int paramInt)
    throws RemoteException;
  
  public abstract int supportsOptionalCodecs(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothA2dp
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothA2dp";
    static final int TRANSACTION_connect = 1;
    static final int TRANSACTION_disableOptionalCodecs = 16;
    static final int TRANSACTION_disconnect = 2;
    static final int TRANSACTION_enableOptionalCodecs = 15;
    static final int TRANSACTION_getActiveDevice = 7;
    static final int TRANSACTION_getCodecStatus = 13;
    static final int TRANSACTION_getConnectedDevices = 3;
    static final int TRANSACTION_getConnectionState = 5;
    static final int TRANSACTION_getDevicesMatchingConnectionStates = 4;
    static final int TRANSACTION_getOptionalCodecsEnabled = 18;
    static final int TRANSACTION_getPriority = 9;
    static final int TRANSACTION_isA2dpPlaying = 12;
    static final int TRANSACTION_isAvrcpAbsoluteVolumeSupported = 10;
    static final int TRANSACTION_setActiveDevice = 6;
    static final int TRANSACTION_setAvrcpAbsoluteVolume = 11;
    static final int TRANSACTION_setCodecConfigPreference = 14;
    static final int TRANSACTION_setOptionalCodecsEnabled = 19;
    static final int TRANSACTION_setPriority = 8;
    static final int TRANSACTION_supportsOptionalCodecs = 17;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothA2dp");
    }
    
    public static IBluetoothA2dp asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothA2dp");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothA2dp))) {
        return (IBluetoothA2dp)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 19: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject14;
          }
          setOptionalCodecsEnabled(paramParcel2, paramParcel1.readInt());
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = getOptionalCodecsEnabled(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramInt1 = supportsOptionalCodecs(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          disableOptionalCodecs(paramParcel1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          enableOptionalCodecs(paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothCodecConfig)BluetoothCodecConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject5;
          }
          setCodecConfigPreference(paramParcel2, paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          paramParcel1 = getCodecStatus(paramParcel1);
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
        case 12: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          paramInt1 = isA2dpPlaying(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          setAvrcpAbsoluteVolume(paramParcel1.readInt());
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          paramInt1 = isAvrcpAbsoluteVolumeSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          paramInt1 = getPriority(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            localObject5 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = localObject9;
          }
          paramInt1 = setPriority((BluetoothDevice)localObject5, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          paramParcel1 = getActiveDevice();
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
        case 6: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject10;
          }
          paramInt1 = setActiveDevice(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject11;
          }
          paramInt1 = getConnectionState(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          paramParcel1 = getDevicesMatchingConnectionStates(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          paramParcel1 = getConnectedDevices();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          paramInt1 = disconnect(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothA2dp");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject13;
        }
        paramInt1 = connect(paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothA2dp");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothA2dp
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
      
      public boolean connect(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
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
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public void disableOptionalCodecs(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          if (paramBluetoothDevice != null)
          {
            localParcel.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public boolean disconnect(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
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
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
      
      public void enableOptionalCodecs(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          if (paramBluetoothDevice != null)
          {
            localParcel.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public BluetoothDevice getActiveDevice()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          BluetoothDevice localBluetoothDevice;
          if (localParcel2.readInt() != 0) {
            localBluetoothDevice = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(localParcel2);
          } else {
            localBluetoothDevice = null;
          }
          return localBluetoothDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public BluetoothCodecStatus getCodecStatus(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramBluetoothDevice = (BluetoothCodecStatus)BluetoothCodecStatus.CREATOR.createFromParcel(localParcel2);
          } else {
            paramBluetoothDevice = null;
          }
          return paramBluetoothDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<BluetoothDevice> getConnectedDevices()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(BluetoothDevice.CREATOR);
          return localArrayList;
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
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
      
      public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
        return "android.bluetooth.IBluetoothA2dp";
      }
      
      public int getOptionalCodecsEnabled(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(18, localParcel1, localParcel2, 0);
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
      
      public int getPriority(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public boolean isA2dpPlaying(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
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
          mRemote.transact(12, localParcel1, localParcel2, 0);
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
      
      public boolean isAvrcpAbsoluteVolumeSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(10, localParcel1, localParcel2, 0);
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
      
      public boolean setActiveDevice(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
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
          mRemote.transact(6, localParcel1, localParcel2, 0);
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
      
      public void setAvrcpAbsoluteVolume(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          localParcel.writeInt(paramInt);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setCodecConfigPreference(BluetoothDevice paramBluetoothDevice, BluetoothCodecConfig paramBluetoothCodecConfig)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          if (paramBluetoothDevice != null)
          {
            localParcel.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramBluetoothCodecConfig != null)
          {
            localParcel.writeInt(1);
            paramBluetoothCodecConfig.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setOptionalCodecsEnabled(BluetoothDevice paramBluetoothDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          if (paramBluetoothDevice != null)
          {
            localParcel.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public boolean setPriority(BluetoothDevice paramBluetoothDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
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
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
      
      public int supportsOptionalCodecs(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothA2dp");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(17, localParcel1, localParcel2, 0);
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
    }
  }
}
