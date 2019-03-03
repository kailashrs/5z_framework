package android.se.omapi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ISecureElementChannel
  extends IInterface
{
  public abstract void close()
    throws RemoteException;
  
  public abstract byte[] getSelectResponse()
    throws RemoteException;
  
  public abstract boolean isBasicChannel()
    throws RemoteException;
  
  public abstract boolean isClosed()
    throws RemoteException;
  
  public abstract boolean selectNext()
    throws RemoteException;
  
  public abstract byte[] transmit(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISecureElementChannel
  {
    private static final String DESCRIPTOR = "android.se.omapi.ISecureElementChannel";
    static final int TRANSACTION_close = 1;
    static final int TRANSACTION_getSelectResponse = 4;
    static final int TRANSACTION_isBasicChannel = 3;
    static final int TRANSACTION_isClosed = 2;
    static final int TRANSACTION_selectNext = 6;
    static final int TRANSACTION_transmit = 5;
    
    public Stub()
    {
      attachInterface(this, "android.se.omapi.ISecureElementChannel");
    }
    
    public static ISecureElementChannel asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.se.omapi.ISecureElementChannel");
      if ((localIInterface != null) && ((localIInterface instanceof ISecureElementChannel))) {
        return (ISecureElementChannel)localIInterface;
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
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementChannel");
          paramInt1 = selectNext();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementChannel");
          paramParcel1 = transmit(paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementChannel");
          paramParcel1 = getSelectResponse();
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementChannel");
          paramInt1 = isBasicChannel();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementChannel");
          paramInt1 = isClosed();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.se.omapi.ISecureElementChannel");
        close();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.se.omapi.ISecureElementChannel");
      return true;
    }
    
    private static class Proxy
      implements ISecureElementChannel
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
      
      public void close()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementChannel");
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
      
      public String getInterfaceDescriptor()
      {
        return "android.se.omapi.ISecureElementChannel";
      }
      
      public byte[] getSelectResponse()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementChannel");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isBasicChannel()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementChannel");
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
      
      public boolean isClosed()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementChannel");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(2, localParcel1, localParcel2, 0);
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
      
      public boolean selectNext()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementChannel");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(6, localParcel1, localParcel2, 0);
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
      
      public byte[] transmit(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementChannel");
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfByte = localParcel2.createByteArray();
          return paramArrayOfByte;
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
