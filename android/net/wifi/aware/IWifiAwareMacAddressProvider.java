package android.net.wifi.aware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

public abstract interface IWifiAwareMacAddressProvider
  extends IInterface
{
  public abstract void macAddress(Map paramMap)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWifiAwareMacAddressProvider
  {
    private static final String DESCRIPTOR = "android.net.wifi.aware.IWifiAwareMacAddressProvider";
    static final int TRANSACTION_macAddress = 1;
    
    public Stub()
    {
      attachInterface(this, "android.net.wifi.aware.IWifiAwareMacAddressProvider");
    }
    
    public static IWifiAwareMacAddressProvider asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.wifi.aware.IWifiAwareMacAddressProvider");
      if ((localIInterface != null) && ((localIInterface instanceof IWifiAwareMacAddressProvider))) {
        return (IWifiAwareMacAddressProvider)localIInterface;
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
        paramParcel2.writeString("android.net.wifi.aware.IWifiAwareMacAddressProvider");
        return true;
      }
      paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareMacAddressProvider");
      macAddress(paramParcel1.readHashMap(getClass().getClassLoader()));
      return true;
    }
    
    private static class Proxy
      implements IWifiAwareMacAddressProvider
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
        return "android.net.wifi.aware.IWifiAwareMacAddressProvider";
      }
      
      public void macAddress(Map paramMap)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareMacAddressProvider");
          localParcel.writeMap(paramMap);
          mRemote.transact(1, localParcel, null, 1);
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
