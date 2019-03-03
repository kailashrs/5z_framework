package android.bluetooth;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IBluetoothProfileServiceConnection
  extends IInterface
{
  public abstract void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void onServiceDisconnected(ComponentName paramComponentName)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothProfileServiceConnection
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothProfileServiceConnection";
    static final int TRANSACTION_onServiceConnected = 1;
    static final int TRANSACTION_onServiceDisconnected = 2;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothProfileServiceConnection");
    }
    
    public static IBluetoothProfileServiceConnection asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothProfileServiceConnection");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothProfileServiceConnection))) {
        return (IBluetoothProfileServiceConnection)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothProfileServiceConnection");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onServiceDisconnected(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothProfileServiceConnection");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = localObject1;
        }
        onServiceConnected(paramParcel2, paramParcel1.readStrongBinder());
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothProfileServiceConnection");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothProfileServiceConnection
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
        return "android.bluetooth.IBluetoothProfileServiceConnection";
      }
      
      public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothProfileServiceConnection");
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onServiceDisconnected(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.IBluetoothProfileServiceConnection");
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
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
    }
  }
}
