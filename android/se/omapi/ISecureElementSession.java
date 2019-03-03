package android.se.omapi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ISecureElementSession
  extends IInterface
{
  public abstract void close()
    throws RemoteException;
  
  public abstract void closeChannels()
    throws RemoteException;
  
  public abstract byte[] getAtr()
    throws RemoteException;
  
  public abstract boolean isClosed()
    throws RemoteException;
  
  public abstract ISecureElementChannel openBasicChannel(byte[] paramArrayOfByte, byte paramByte, ISecureElementListener paramISecureElementListener)
    throws RemoteException;
  
  public abstract ISecureElementChannel openLogicalChannel(byte[] paramArrayOfByte, byte paramByte, ISecureElementListener paramISecureElementListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISecureElementSession
  {
    private static final String DESCRIPTOR = "android.se.omapi.ISecureElementSession";
    static final int TRANSACTION_close = 2;
    static final int TRANSACTION_closeChannels = 3;
    static final int TRANSACTION_getAtr = 1;
    static final int TRANSACTION_isClosed = 4;
    static final int TRANSACTION_openBasicChannel = 5;
    static final int TRANSACTION_openLogicalChannel = 6;
    
    public Stub()
    {
      attachInterface(this, "android.se.omapi.ISecureElementSession");
    }
    
    public static ISecureElementSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.se.omapi.ISecureElementSession");
      if ((localIInterface != null) && ((localIInterface instanceof ISecureElementSession))) {
        return (ISecureElementSession)localIInterface;
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
        ISecureElementChannel localISecureElementChannel1 = null;
        ISecureElementChannel localISecureElementChannel2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementSession");
          localISecureElementChannel1 = openLogicalChannel(paramParcel1.createByteArray(), paramParcel1.readByte(), ISecureElementListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel1 = localISecureElementChannel2;
          if (localISecureElementChannel1 != null) {
            paramParcel1 = localISecureElementChannel1.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementSession");
          localISecureElementChannel2 = openBasicChannel(paramParcel1.createByteArray(), paramParcel1.readByte(), ISecureElementListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel1 = localISecureElementChannel1;
          if (localISecureElementChannel2 != null) {
            paramParcel1 = localISecureElementChannel2.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementSession");
          paramInt1 = isClosed();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementSession");
          closeChannels();
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementSession");
          close();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.se.omapi.ISecureElementSession");
        paramParcel1 = getAtr();
        paramParcel2.writeNoException();
        paramParcel2.writeByteArray(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.se.omapi.ISecureElementSession");
      return true;
    }
    
    private static class Proxy
      implements ISecureElementSession
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
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementSession");
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
      
      public void closeChannels()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementSession");
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
      
      public byte[] getAtr()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementSession");
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.se.omapi.ISecureElementSession";
      }
      
      public boolean isClosed()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementSession");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(4, localParcel1, localParcel2, 0);
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
      
      public ISecureElementChannel openBasicChannel(byte[] paramArrayOfByte, byte paramByte, ISecureElementListener paramISecureElementListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementSession");
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeByte(paramByte);
          if (paramISecureElementListener != null) {
            paramArrayOfByte = paramISecureElementListener.asBinder();
          } else {
            paramArrayOfByte = null;
          }
          localParcel1.writeStrongBinder(paramArrayOfByte);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfByte = ISecureElementChannel.Stub.asInterface(localParcel2.readStrongBinder());
          return paramArrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ISecureElementChannel openLogicalChannel(byte[] paramArrayOfByte, byte paramByte, ISecureElementListener paramISecureElementListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementSession");
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeByte(paramByte);
          if (paramISecureElementListener != null) {
            paramArrayOfByte = paramISecureElementListener.asBinder();
          } else {
            paramArrayOfByte = null;
          }
          localParcel1.writeStrongBinder(paramArrayOfByte);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfByte = ISecureElementChannel.Stub.asInterface(localParcel2.readStrongBinder());
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
