package android.media.midi;

import android.bluetooth.BluetoothDevice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IBluetoothMidiService
  extends IInterface
{
  public abstract IBinder addBluetoothDevice(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothMidiService
  {
    private static final String DESCRIPTOR = "android.media.midi.IBluetoothMidiService";
    static final int TRANSACTION_addBluetoothDevice = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.midi.IBluetoothMidiService");
    }
    
    public static IBluetoothMidiService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.midi.IBluetoothMidiService");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothMidiService))) {
        return (IBluetoothMidiService)localIInterface;
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
      if (paramInt1 != 1)
      {
        if (paramInt1 != 1598968902) {
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        }
        paramParcel2.writeString("android.media.midi.IBluetoothMidiService");
        return true;
      }
      paramParcel1.enforceInterface("android.media.midi.IBluetoothMidiService");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      paramParcel1 = addBluetoothDevice(paramParcel1);
      paramParcel2.writeNoException();
      paramParcel2.writeStrongBinder(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IBluetoothMidiService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public IBinder addBluetoothDevice(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IBluetoothMidiService");
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
          paramBluetoothDevice = localParcel2.readStrongBinder();
          return paramBluetoothDevice;
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
      
      public String getInterfaceDescriptor()
      {
        return "android.media.midi.IBluetoothMidiService";
      }
    }
  }
}
