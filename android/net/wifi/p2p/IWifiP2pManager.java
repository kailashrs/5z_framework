package android.net.wifi.p2p;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IWifiP2pManager
  extends IInterface
{
  public abstract void checkConfigureWifiDisplayPermission()
    throws RemoteException;
  
  public abstract void close(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract Messenger getMessenger(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract Messenger getP2pStateMachineMessenger()
    throws RemoteException;
  
  public abstract void setMiracastMode(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWifiP2pManager
  {
    private static final String DESCRIPTOR = "android.net.wifi.p2p.IWifiP2pManager";
    static final int TRANSACTION_checkConfigureWifiDisplayPermission = 5;
    static final int TRANSACTION_close = 3;
    static final int TRANSACTION_getMessenger = 1;
    static final int TRANSACTION_getP2pStateMachineMessenger = 2;
    static final int TRANSACTION_setMiracastMode = 4;
    
    public Stub()
    {
      attachInterface(this, "android.net.wifi.p2p.IWifiP2pManager");
    }
    
    public static IWifiP2pManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.wifi.p2p.IWifiP2pManager");
      if ((localIInterface != null) && ((localIInterface instanceof IWifiP2pManager))) {
        return (IWifiP2pManager)localIInterface;
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
        case 5: 
          paramParcel1.enforceInterface("android.net.wifi.p2p.IWifiP2pManager");
          checkConfigureWifiDisplayPermission();
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.wifi.p2p.IWifiP2pManager");
          setMiracastMode(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.wifi.p2p.IWifiP2pManager");
          close(paramParcel1.readStrongBinder());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.wifi.p2p.IWifiP2pManager");
          paramParcel1 = getP2pStateMachineMessenger();
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
        paramParcel1.enforceInterface("android.net.wifi.p2p.IWifiP2pManager");
        paramParcel1 = getMessenger(paramParcel1.readStrongBinder());
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
      paramParcel2.writeString("android.net.wifi.p2p.IWifiP2pManager");
      return true;
    }
    
    private static class Proxy
      implements IWifiP2pManager
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
      
      public void checkConfigureWifiDisplayPermission()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.p2p.IWifiP2pManager");
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
      
      public void close(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.p2p.IWifiP2pManager");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.wifi.p2p.IWifiP2pManager";
      }
      
      public Messenger getMessenger(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.p2p.IWifiP2pManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIBinder = (Messenger)Messenger.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIBinder = null;
          }
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Messenger getP2pStateMachineMessenger()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.p2p.IWifiP2pManager");
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
      
      public void setMiracastMode(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.p2p.IWifiP2pManager");
          localParcel1.writeInt(paramInt);
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
    }
  }
}
