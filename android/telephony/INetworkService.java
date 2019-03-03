package android.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface INetworkService
  extends IInterface
{
  public abstract void createNetworkServiceProvider(int paramInt)
    throws RemoteException;
  
  public abstract void getNetworkRegistrationState(int paramInt1, int paramInt2, INetworkServiceCallback paramINetworkServiceCallback)
    throws RemoteException;
  
  public abstract void registerForNetworkRegistrationStateChanged(int paramInt, INetworkServiceCallback paramINetworkServiceCallback)
    throws RemoteException;
  
  public abstract void removeNetworkServiceProvider(int paramInt)
    throws RemoteException;
  
  public abstract void unregisterForNetworkRegistrationStateChanged(int paramInt, INetworkServiceCallback paramINetworkServiceCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkService
  {
    private static final String DESCRIPTOR = "android.telephony.INetworkService";
    static final int TRANSACTION_createNetworkServiceProvider = 1;
    static final int TRANSACTION_getNetworkRegistrationState = 3;
    static final int TRANSACTION_registerForNetworkRegistrationStateChanged = 4;
    static final int TRANSACTION_removeNetworkServiceProvider = 2;
    static final int TRANSACTION_unregisterForNetworkRegistrationStateChanged = 5;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.INetworkService");
    }
    
    public static INetworkService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.INetworkService");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkService))) {
        return (INetworkService)localIInterface;
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
          paramParcel1.enforceInterface("android.telephony.INetworkService");
          unregisterForNetworkRegistrationStateChanged(paramParcel1.readInt(), INetworkServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.telephony.INetworkService");
          registerForNetworkRegistrationStateChanged(paramParcel1.readInt(), INetworkServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.telephony.INetworkService");
          getNetworkRegistrationState(paramParcel1.readInt(), paramParcel1.readInt(), INetworkServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.INetworkService");
          removeNetworkServiceProvider(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.INetworkService");
        createNetworkServiceProvider(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.telephony.INetworkService");
      return true;
    }
    
    private static class Proxy
      implements INetworkService
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
      
      public void createNetworkServiceProvider(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.INetworkService");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.telephony.INetworkService";
      }
      
      public void getNetworkRegistrationState(int paramInt1, int paramInt2, INetworkServiceCallback paramINetworkServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.INetworkService");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramINetworkServiceCallback != null) {
            paramINetworkServiceCallback = paramINetworkServiceCallback.asBinder();
          } else {
            paramINetworkServiceCallback = null;
          }
          localParcel.writeStrongBinder(paramINetworkServiceCallback);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registerForNetworkRegistrationStateChanged(int paramInt, INetworkServiceCallback paramINetworkServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.INetworkService");
          localParcel.writeInt(paramInt);
          if (paramINetworkServiceCallback != null) {
            paramINetworkServiceCallback = paramINetworkServiceCallback.asBinder();
          } else {
            paramINetworkServiceCallback = null;
          }
          localParcel.writeStrongBinder(paramINetworkServiceCallback);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeNetworkServiceProvider(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.INetworkService");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unregisterForNetworkRegistrationStateChanged(int paramInt, INetworkServiceCallback paramINetworkServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.INetworkService");
          localParcel.writeInt(paramInt);
          if (paramINetworkServiceCallback != null) {
            paramINetworkServiceCallback = paramINetworkServiceCallback.asBinder();
          } else {
            paramINetworkServiceCallback = null;
          }
          localParcel.writeStrongBinder(paramINetworkServiceCallback);
          mRemote.transact(5, localParcel, null, 1);
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
