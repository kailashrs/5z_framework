package android.net.lowpan;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ILowpanManager
  extends IInterface
{
  public static final String LOWPAN_SERVICE_NAME = "lowpan";
  
  public abstract void addInterface(ILowpanInterface paramILowpanInterface)
    throws RemoteException;
  
  public abstract void addListener(ILowpanManagerListener paramILowpanManagerListener)
    throws RemoteException;
  
  public abstract ILowpanInterface getInterface(String paramString)
    throws RemoteException;
  
  public abstract String[] getInterfaceList()
    throws RemoteException;
  
  public abstract void removeInterface(ILowpanInterface paramILowpanInterface)
    throws RemoteException;
  
  public abstract void removeListener(ILowpanManagerListener paramILowpanManagerListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ILowpanManager
  {
    private static final String DESCRIPTOR = "android.net.lowpan.ILowpanManager";
    static final int TRANSACTION_addInterface = 5;
    static final int TRANSACTION_addListener = 3;
    static final int TRANSACTION_getInterface = 1;
    static final int TRANSACTION_getInterfaceList = 2;
    static final int TRANSACTION_removeInterface = 6;
    static final int TRANSACTION_removeListener = 4;
    
    public Stub()
    {
      attachInterface(this, "android.net.lowpan.ILowpanManager");
    }
    
    public static ILowpanManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.lowpan.ILowpanManager");
      if ((localIInterface != null) && ((localIInterface instanceof ILowpanManager))) {
        return (ILowpanManager)localIInterface;
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
        case 6: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanManager");
          removeInterface(ILowpanInterface.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanManager");
          addInterface(ILowpanInterface.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanManager");
          removeListener(ILowpanManagerListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanManager");
          addListener(ILowpanManagerListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanManager");
          paramParcel1 = getInterfaceList();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.net.lowpan.ILowpanManager");
        paramParcel1 = getInterface(paramParcel1.readString());
        paramParcel2.writeNoException();
        if (paramParcel1 != null) {
          paramParcel1 = paramParcel1.asBinder();
        } else {
          paramParcel1 = null;
        }
        paramParcel2.writeStrongBinder(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.net.lowpan.ILowpanManager");
      return true;
    }
    
    private static class Proxy
      implements ILowpanManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addInterface(ILowpanInterface paramILowpanInterface)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanManager");
          if (paramILowpanInterface != null) {
            paramILowpanInterface = paramILowpanInterface.asBinder();
          } else {
            paramILowpanInterface = null;
          }
          localParcel1.writeStrongBinder(paramILowpanInterface);
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
      
      public void addListener(ILowpanManagerListener paramILowpanManagerListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanManager");
          if (paramILowpanManagerListener != null) {
            paramILowpanManagerListener = paramILowpanManagerListener.asBinder();
          } else {
            paramILowpanManagerListener = null;
          }
          localParcel1.writeStrongBinder(paramILowpanManagerListener);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public ILowpanInterface getInterface(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanManager");
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = ILowpanInterface.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.lowpan.ILowpanManager";
      }
      
      public String[] getInterfaceList()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanManager");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeInterface(ILowpanInterface paramILowpanInterface)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanManager");
          if (paramILowpanInterface != null) {
            paramILowpanInterface = paramILowpanInterface.asBinder();
          } else {
            paramILowpanInterface = null;
          }
          localParcel1.writeStrongBinder(paramILowpanInterface);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeListener(ILowpanManagerListener paramILowpanManagerListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanManager");
          if (paramILowpanManagerListener != null) {
            paramILowpanManagerListener = paramILowpanManagerListener.asBinder();
          } else {
            paramILowpanManagerListener = null;
          }
          localParcel1.writeStrongBinder(paramILowpanManagerListener);
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
