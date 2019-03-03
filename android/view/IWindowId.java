package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IWindowId
  extends IInterface
{
  public abstract boolean isFocused()
    throws RemoteException;
  
  public abstract void registerFocusObserver(IWindowFocusObserver paramIWindowFocusObserver)
    throws RemoteException;
  
  public abstract void unregisterFocusObserver(IWindowFocusObserver paramIWindowFocusObserver)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWindowId
  {
    private static final String DESCRIPTOR = "android.view.IWindowId";
    static final int TRANSACTION_isFocused = 3;
    static final int TRANSACTION_registerFocusObserver = 1;
    static final int TRANSACTION_unregisterFocusObserver = 2;
    
    public Stub()
    {
      attachInterface(this, "android.view.IWindowId");
    }
    
    public static IWindowId asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IWindowId");
      if ((localIInterface != null) && ((localIInterface instanceof IWindowId))) {
        return (IWindowId)localIInterface;
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
          paramParcel1.enforceInterface("android.view.IWindowId");
          paramInt1 = isFocused();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.IWindowId");
          unregisterFocusObserver(IWindowFocusObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.view.IWindowId");
        registerFocusObserver(IWindowFocusObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.view.IWindowId");
      return true;
    }
    
    private static class Proxy
      implements IWindowId
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
        return "android.view.IWindowId";
      }
      
      public boolean isFocused()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowId");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerFocusObserver(IWindowFocusObserver paramIWindowFocusObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowId");
          if (paramIWindowFocusObserver != null) {
            paramIWindowFocusObserver = paramIWindowFocusObserver.asBinder();
          } else {
            paramIWindowFocusObserver = null;
          }
          localParcel1.writeStrongBinder(paramIWindowFocusObserver);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterFocusObserver(IWindowFocusObserver paramIWindowFocusObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowId");
          if (paramIWindowFocusObserver != null) {
            paramIWindowFocusObserver = paramIWindowFocusObserver.asBinder();
          } else {
            paramIWindowFocusObserver = null;
          }
          localParcel1.writeStrongBinder(paramIWindowFocusObserver);
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
