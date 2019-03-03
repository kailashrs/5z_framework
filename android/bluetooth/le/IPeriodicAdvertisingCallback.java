package android.bluetooth.le;

import android.bluetooth.BluetoothDevice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPeriodicAdvertisingCallback
  extends IInterface
{
  public abstract void onPeriodicAdvertisingReport(PeriodicAdvertisingReport paramPeriodicAdvertisingReport)
    throws RemoteException;
  
  public abstract void onSyncEstablished(int paramInt1, BluetoothDevice paramBluetoothDevice, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    throws RemoteException;
  
  public abstract void onSyncLost(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPeriodicAdvertisingCallback
  {
    private static final String DESCRIPTOR = "android.bluetooth.le.IPeriodicAdvertisingCallback";
    static final int TRANSACTION_onPeriodicAdvertisingReport = 2;
    static final int TRANSACTION_onSyncEstablished = 1;
    static final int TRANSACTION_onSyncLost = 3;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.le.IPeriodicAdvertisingCallback");
    }
    
    public static IPeriodicAdvertisingCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.le.IPeriodicAdvertisingCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IPeriodicAdvertisingCallback))) {
        return (IPeriodicAdvertisingCallback)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.le.IPeriodicAdvertisingCallback");
          onSyncLost(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.le.IPeriodicAdvertisingCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PeriodicAdvertisingReport)PeriodicAdvertisingReport.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onPeriodicAdvertisingReport(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.le.IPeriodicAdvertisingCallback");
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {}
        for (paramParcel2 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = localObject1) {
          break;
        }
        onSyncEstablished(paramInt1, paramParcel2, paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.bluetooth.le.IPeriodicAdvertisingCallback");
      return true;
    }
    
    private static class Proxy
      implements IPeriodicAdvertisingCallback
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
      
      public String getInterfaceDescriptor()
      {
        return "android.bluetooth.le.IPeriodicAdvertisingCallback";
      }
      
      public void onPeriodicAdvertisingReport(PeriodicAdvertisingReport paramPeriodicAdvertisingReport)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IPeriodicAdvertisingCallback");
          if (paramPeriodicAdvertisingReport != null)
          {
            localParcel.writeInt(1);
            paramPeriodicAdvertisingReport.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSyncEstablished(int paramInt1, BluetoothDevice paramBluetoothDevice, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IPeriodicAdvertisingCallback");
          localParcel.writeInt(paramInt1);
          if (paramBluetoothDevice != null)
          {
            localParcel.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          localParcel.writeInt(paramInt5);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSyncLost(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IPeriodicAdvertisingCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}
