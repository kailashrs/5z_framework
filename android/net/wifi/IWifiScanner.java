package android.net.wifi;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IWifiScanner
  extends IInterface
{
  public abstract Bundle getAvailableChannels(int paramInt)
    throws RemoteException;
  
  public abstract Messenger getMessenger()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWifiScanner
  {
    private static final String DESCRIPTOR = "android.net.wifi.IWifiScanner";
    static final int TRANSACTION_getAvailableChannels = 2;
    static final int TRANSACTION_getMessenger = 1;
    
    public Stub()
    {
      attachInterface(this, "android.net.wifi.IWifiScanner");
    }
    
    public static IWifiScanner asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.wifi.IWifiScanner");
      if ((localIInterface != null) && ((localIInterface instanceof IWifiScanner))) {
        return (IWifiScanner)localIInterface;
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
          paramParcel1.enforceInterface("android.net.wifi.IWifiScanner");
          paramParcel1 = getAvailableChannels(paramParcel1.readInt());
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
        }
        paramParcel1.enforceInterface("android.net.wifi.IWifiScanner");
        paramParcel1 = getMessenger();
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
      }
      paramParcel2.writeString("android.net.wifi.IWifiScanner");
      return true;
    }
    
    private static class Proxy
      implements IWifiScanner
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
      
      public Bundle getAvailableChannels(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiScanner");
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Bundle localBundle;
          if (localParcel2.readInt() != 0) {
            localBundle = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            localBundle = null;
          }
          return localBundle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.wifi.IWifiScanner";
      }
      
      public Messenger getMessenger()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiScanner");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Messenger localMessenger;
          if (localParcel2.readInt() != 0) {
            localMessenger = (Messenger)Messenger.CREATOR.createFromParcel(localParcel2);
          } else {
            localMessenger = null;
          }
          return localMessenger;
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
