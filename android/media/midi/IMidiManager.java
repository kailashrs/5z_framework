package android.media.midi;

import android.bluetooth.BluetoothDevice;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IMidiManager
  extends IInterface
{
  public abstract void closeDevice(IBinder paramIBinder1, IBinder paramIBinder2)
    throws RemoteException;
  
  public abstract MidiDeviceStatus getDeviceStatus(MidiDeviceInfo paramMidiDeviceInfo)
    throws RemoteException;
  
  public abstract MidiDeviceInfo[] getDevices()
    throws RemoteException;
  
  public abstract MidiDeviceInfo getServiceDeviceInfo(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void openBluetoothDevice(IBinder paramIBinder, BluetoothDevice paramBluetoothDevice, IMidiDeviceOpenCallback paramIMidiDeviceOpenCallback)
    throws RemoteException;
  
  public abstract void openDevice(IBinder paramIBinder, MidiDeviceInfo paramMidiDeviceInfo, IMidiDeviceOpenCallback paramIMidiDeviceOpenCallback)
    throws RemoteException;
  
  public abstract MidiDeviceInfo registerDeviceServer(IMidiDeviceServer paramIMidiDeviceServer, int paramInt1, int paramInt2, String[] paramArrayOfString1, String[] paramArrayOfString2, Bundle paramBundle, int paramInt3)
    throws RemoteException;
  
  public abstract void registerListener(IBinder paramIBinder, IMidiDeviceListener paramIMidiDeviceListener)
    throws RemoteException;
  
  public abstract void setDeviceStatus(IMidiDeviceServer paramIMidiDeviceServer, MidiDeviceStatus paramMidiDeviceStatus)
    throws RemoteException;
  
  public abstract void unregisterDeviceServer(IMidiDeviceServer paramIMidiDeviceServer)
    throws RemoteException;
  
  public abstract void unregisterListener(IBinder paramIBinder, IMidiDeviceListener paramIMidiDeviceListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMidiManager
  {
    private static final String DESCRIPTOR = "android.media.midi.IMidiManager";
    static final int TRANSACTION_closeDevice = 6;
    static final int TRANSACTION_getDeviceStatus = 10;
    static final int TRANSACTION_getDevices = 1;
    static final int TRANSACTION_getServiceDeviceInfo = 9;
    static final int TRANSACTION_openBluetoothDevice = 5;
    static final int TRANSACTION_openDevice = 4;
    static final int TRANSACTION_registerDeviceServer = 7;
    static final int TRANSACTION_registerListener = 2;
    static final int TRANSACTION_setDeviceStatus = 11;
    static final int TRANSACTION_unregisterDeviceServer = 8;
    static final int TRANSACTION_unregisterListener = 3;
    
    public Stub()
    {
      attachInterface(this, "android.media.midi.IMidiManager");
    }
    
    public static IMidiManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.midi.IMidiManager");
      if ((localIInterface != null) && ((localIInterface instanceof IMidiManager))) {
        return (IMidiManager)localIInterface;
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
        String[] arrayOfString1 = null;
        String[] arrayOfString2 = null;
        Object localObject3 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 11: 
          paramParcel1.enforceInterface("android.media.midi.IMidiManager");
          localObject2 = IMidiDeviceServer.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (MidiDeviceStatus)MidiDeviceStatus.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject3;
          }
          setDeviceStatus((IMidiDeviceServer)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.media.midi.IMidiManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (MidiDeviceInfo)MidiDeviceInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramParcel1 = getDeviceStatus(paramParcel1);
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
          paramParcel1.enforceInterface("android.media.midi.IMidiManager");
          paramParcel1 = getServiceDeviceInfo(paramParcel1.readString(), paramParcel1.readString());
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
        case 8: 
          paramParcel1.enforceInterface("android.media.midi.IMidiManager");
          unregisterDeviceServer(IMidiDeviceServer.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.media.midi.IMidiManager");
          localObject3 = IMidiDeviceServer.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          arrayOfString1 = paramParcel1.createStringArray();
          arrayOfString2 = paramParcel1.createStringArray();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramParcel1 = registerDeviceServer((IMidiDeviceServer)localObject3, paramInt2, paramInt1, arrayOfString1, arrayOfString2, (Bundle)localObject2, paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.media.midi.IMidiManager");
          closeDevice(paramParcel1.readStrongBinder(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.midi.IMidiManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = arrayOfString1;
          }
          openBluetoothDevice((IBinder)localObject3, (BluetoothDevice)localObject2, IMidiDeviceOpenCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.midi.IMidiManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (MidiDeviceInfo)MidiDeviceInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = arrayOfString2;
          }
          openDevice((IBinder)localObject3, (MidiDeviceInfo)localObject2, IMidiDeviceOpenCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.midi.IMidiManager");
          unregisterListener(paramParcel1.readStrongBinder(), IMidiDeviceListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.midi.IMidiManager");
          registerListener(paramParcel1.readStrongBinder(), IMidiDeviceListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.media.midi.IMidiManager");
        paramParcel1 = getDevices();
        paramParcel2.writeNoException();
        paramParcel2.writeTypedArray(paramParcel1, 1);
        return true;
      }
      paramParcel2.writeString("android.media.midi.IMidiManager");
      return true;
    }
    
    private static class Proxy
      implements IMidiManager
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
      
      public void closeDevice(IBinder paramIBinder1, IBinder paramIBinder2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiManager");
          localParcel1.writeStrongBinder(paramIBinder1);
          localParcel1.writeStrongBinder(paramIBinder2);
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
      
      public MidiDeviceStatus getDeviceStatus(MidiDeviceInfo paramMidiDeviceInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiManager");
          if (paramMidiDeviceInfo != null)
          {
            localParcel1.writeInt(1);
            paramMidiDeviceInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramMidiDeviceInfo = (MidiDeviceStatus)MidiDeviceStatus.CREATOR.createFromParcel(localParcel2);
          } else {
            paramMidiDeviceInfo = null;
          }
          return paramMidiDeviceInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public MidiDeviceInfo[] getDevices()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiManager");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          MidiDeviceInfo[] arrayOfMidiDeviceInfo = (MidiDeviceInfo[])localParcel2.createTypedArray(MidiDeviceInfo.CREATOR);
          return arrayOfMidiDeviceInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.midi.IMidiManager";
      }
      
      public MidiDeviceInfo getServiceDeviceInfo(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (MidiDeviceInfo)MidiDeviceInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void openBluetoothDevice(IBinder paramIBinder, BluetoothDevice paramBluetoothDevice, IMidiDeviceOpenCallback paramIMidiDeviceOpenCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIMidiDeviceOpenCallback != null) {
            paramIBinder = paramIMidiDeviceOpenCallback.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void openDevice(IBinder paramIBinder, MidiDeviceInfo paramMidiDeviceInfo, IMidiDeviceOpenCallback paramIMidiDeviceOpenCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramMidiDeviceInfo != null)
          {
            localParcel1.writeInt(1);
            paramMidiDeviceInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIMidiDeviceOpenCallback != null) {
            paramIBinder = paramIMidiDeviceOpenCallback.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public MidiDeviceInfo registerDeviceServer(IMidiDeviceServer paramIMidiDeviceServer, int paramInt1, int paramInt2, String[] paramArrayOfString1, String[] paramArrayOfString2, Bundle paramBundle, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiManager");
          Object localObject = null;
          if (paramIMidiDeviceServer != null) {
            paramIMidiDeviceServer = paramIMidiDeviceServer.asBinder();
          } else {
            paramIMidiDeviceServer = null;
          }
          localParcel1.writeStrongBinder(paramIMidiDeviceServer);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeStringArray(paramArrayOfString1);
          localParcel1.writeStringArray(paramArrayOfString2);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt3);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIMidiDeviceServer = (MidiDeviceInfo)MidiDeviceInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIMidiDeviceServer = localObject;
          }
          return paramIMidiDeviceServer;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerListener(IBinder paramIBinder, IMidiDeviceListener paramIMidiDeviceListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramIMidiDeviceListener != null) {
            paramIBinder = paramIMidiDeviceListener.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void setDeviceStatus(IMidiDeviceServer paramIMidiDeviceServer, MidiDeviceStatus paramMidiDeviceStatus)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiManager");
          if (paramIMidiDeviceServer != null) {
            paramIMidiDeviceServer = paramIMidiDeviceServer.asBinder();
          } else {
            paramIMidiDeviceServer = null;
          }
          localParcel1.writeStrongBinder(paramIMidiDeviceServer);
          if (paramMidiDeviceStatus != null)
          {
            localParcel1.writeInt(1);
            paramMidiDeviceStatus.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void unregisterDeviceServer(IMidiDeviceServer paramIMidiDeviceServer)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiManager");
          if (paramIMidiDeviceServer != null) {
            paramIMidiDeviceServer = paramIMidiDeviceServer.asBinder();
          } else {
            paramIMidiDeviceServer = null;
          }
          localParcel1.writeStrongBinder(paramIMidiDeviceServer);
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
      
      public void unregisterListener(IBinder paramIBinder, IMidiDeviceListener paramIMidiDeviceListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramIMidiDeviceListener != null) {
            paramIBinder = paramIMidiDeviceListener.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
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
    }
  }
}
