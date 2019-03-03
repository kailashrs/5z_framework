package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ISoftApCallback
  extends IInterface
{
  public abstract void onNumClientsChanged(int paramInt)
    throws RemoteException;
  
  public abstract void onStaConnected(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onStaDisconnected(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onStateChanged(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISoftApCallback
  {
    private static final String DESCRIPTOR = "android.net.wifi.ISoftApCallback";
    static final int TRANSACTION_onNumClientsChanged = 2;
    static final int TRANSACTION_onStaConnected = 3;
    static final int TRANSACTION_onStaDisconnected = 4;
    static final int TRANSACTION_onStateChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.net.wifi.ISoftApCallback");
    }
    
    public static ISoftApCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.wifi.ISoftApCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ISoftApCallback))) {
        return (ISoftApCallback)localIInterface;
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
        case 4: 
          paramParcel1.enforceInterface("android.net.wifi.ISoftApCallback");
          onStaDisconnected(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.wifi.ISoftApCallback");
          onStaConnected(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.wifi.ISoftApCallback");
          onNumClientsChanged(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.net.wifi.ISoftApCallback");
        onStateChanged(paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.net.wifi.ISoftApCallback");
      return true;
    }
    
    private static class Proxy
      implements ISoftApCallback
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
        return "android.net.wifi.ISoftApCallback";
      }
      
      public void onNumClientsChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.ISoftApCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStaConnected(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.ISoftApCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStaDisconnected(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.ISoftApCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStateChanged(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.ISoftApCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
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
