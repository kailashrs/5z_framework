package android.se.omapi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ISecureElementReader
  extends IInterface
{
  public abstract void closeSessions()
    throws RemoteException;
  
  public abstract boolean isSecureElementPresent()
    throws RemoteException;
  
  public abstract ISecureElementSession openSession()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISecureElementReader
  {
    private static final String DESCRIPTOR = "android.se.omapi.ISecureElementReader";
    static final int TRANSACTION_closeSessions = 3;
    static final int TRANSACTION_isSecureElementPresent = 1;
    static final int TRANSACTION_openSession = 2;
    
    public Stub()
    {
      attachInterface(this, "android.se.omapi.ISecureElementReader");
    }
    
    public static ISecureElementReader asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.se.omapi.ISecureElementReader");
      if ((localIInterface != null) && ((localIInterface instanceof ISecureElementReader))) {
        return (ISecureElementReader)localIInterface;
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
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementReader");
          closeSessions();
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementReader");
          paramParcel1 = openSession();
          paramParcel2.writeNoException();
          if (paramParcel1 != null) {
            paramParcel1 = paramParcel1.asBinder();
          } else {
            paramParcel1 = null;
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.se.omapi.ISecureElementReader");
        paramInt1 = isSecureElementPresent();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.se.omapi.ISecureElementReader");
      return true;
    }
    
    private static class Proxy
      implements ISecureElementReader
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
      
      public void closeSessions()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementReader");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.se.omapi.ISecureElementReader";
      }
      
      public boolean isSecureElementPresent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementReader");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(1, localParcel1, localParcel2, 0);
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
      
      public ISecureElementSession openSession()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementReader");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ISecureElementSession localISecureElementSession = ISecureElementSession.Stub.asInterface(localParcel2.readStrongBinder());
          return localISecureElementSession;
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
