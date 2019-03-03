package android.net.wifi.aware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IWifiAwareEventCallback
  extends IInterface
{
  public abstract void onConnectFail(int paramInt)
    throws RemoteException;
  
  public abstract void onConnectSuccess(int paramInt)
    throws RemoteException;
  
  public abstract void onIdentityChanged(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWifiAwareEventCallback
  {
    private static final String DESCRIPTOR = "android.net.wifi.aware.IWifiAwareEventCallback";
    static final int TRANSACTION_onConnectFail = 2;
    static final int TRANSACTION_onConnectSuccess = 1;
    static final int TRANSACTION_onIdentityChanged = 3;
    
    public Stub()
    {
      attachInterface(this, "android.net.wifi.aware.IWifiAwareEventCallback");
    }
    
    public static IWifiAwareEventCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.wifi.aware.IWifiAwareEventCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IWifiAwareEventCallback))) {
        return (IWifiAwareEventCallback)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 3: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareEventCallback");
          onIdentityChanged(paramParcel1.createByteArray());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareEventCallback");
          onConnectFail(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareEventCallback");
        onConnectSuccess(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.net.wifi.aware.IWifiAwareEventCallback");
      return true;
    }
    
    private static class Proxy
      implements IWifiAwareEventCallback
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
        return "android.net.wifi.aware.IWifiAwareEventCallback";
      }
      
      public void onConnectFail(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareEventCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onConnectSuccess(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareEventCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onIdentityChanged(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareEventCallback");
          localParcel.writeByteArray(paramArrayOfByte);
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
