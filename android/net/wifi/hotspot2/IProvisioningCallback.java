package android.net.wifi.hotspot2;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IProvisioningCallback
  extends IInterface
{
  public abstract void onProvisioningFailure(int paramInt)
    throws RemoteException;
  
  public abstract void onProvisioningStatus(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IProvisioningCallback
  {
    private static final String DESCRIPTOR = "android.net.wifi.hotspot2.IProvisioningCallback";
    static final int TRANSACTION_onProvisioningFailure = 1;
    static final int TRANSACTION_onProvisioningStatus = 2;
    
    public Stub()
    {
      attachInterface(this, "android.net.wifi.hotspot2.IProvisioningCallback");
    }
    
    public static IProvisioningCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.wifi.hotspot2.IProvisioningCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IProvisioningCallback))) {
        return (IProvisioningCallback)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.net.wifi.hotspot2.IProvisioningCallback");
          onProvisioningStatus(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.net.wifi.hotspot2.IProvisioningCallback");
        onProvisioningFailure(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.net.wifi.hotspot2.IProvisioningCallback");
      return true;
    }
    
    private static class Proxy
      implements IProvisioningCallback
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
        return "android.net.wifi.hotspot2.IProvisioningCallback";
      }
      
      public void onProvisioningFailure(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.hotspot2.IProvisioningCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onProvisioningStatus(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.hotspot2.IProvisioningCallback");
          localParcel.writeInt(paramInt);
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
