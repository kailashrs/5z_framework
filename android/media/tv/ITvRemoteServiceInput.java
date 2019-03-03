package android.media.tv;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ITvRemoteServiceInput
  extends IInterface
{
  public abstract void clearInputBridge(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void closeInputBridge(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void openInputBridge(IBinder paramIBinder, String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void sendKeyDown(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void sendKeyUp(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void sendPointerDown(IBinder paramIBinder, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void sendPointerSync(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void sendPointerUp(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void sendTimestamp(IBinder paramIBinder, long paramLong)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITvRemoteServiceInput
  {
    private static final String DESCRIPTOR = "android.media.tv.ITvRemoteServiceInput";
    static final int TRANSACTION_clearInputBridge = 3;
    static final int TRANSACTION_closeInputBridge = 2;
    static final int TRANSACTION_openInputBridge = 1;
    static final int TRANSACTION_sendKeyDown = 5;
    static final int TRANSACTION_sendKeyUp = 6;
    static final int TRANSACTION_sendPointerDown = 7;
    static final int TRANSACTION_sendPointerSync = 9;
    static final int TRANSACTION_sendPointerUp = 8;
    static final int TRANSACTION_sendTimestamp = 4;
    
    public Stub()
    {
      attachInterface(this, "android.media.tv.ITvRemoteServiceInput");
    }
    
    public static ITvRemoteServiceInput asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.tv.ITvRemoteServiceInput");
      if ((localIInterface != null) && ((localIInterface instanceof ITvRemoteServiceInput))) {
        return (ITvRemoteServiceInput)localIInterface;
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
        case 9: 
          paramParcel1.enforceInterface("android.media.tv.ITvRemoteServiceInput");
          sendPointerSync(paramParcel1.readStrongBinder());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.media.tv.ITvRemoteServiceInput");
          sendPointerUp(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.media.tv.ITvRemoteServiceInput");
          sendPointerDown(paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.tv.ITvRemoteServiceInput");
          sendKeyUp(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.tv.ITvRemoteServiceInput");
          sendKeyDown(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.tv.ITvRemoteServiceInput");
          sendTimestamp(paramParcel1.readStrongBinder(), paramParcel1.readLong());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.tv.ITvRemoteServiceInput");
          clearInputBridge(paramParcel1.readStrongBinder());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.tv.ITvRemoteServiceInput");
          closeInputBridge(paramParcel1.readStrongBinder());
          return true;
        }
        paramParcel1.enforceInterface("android.media.tv.ITvRemoteServiceInput");
        openInputBridge(paramParcel1.readStrongBinder(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.media.tv.ITvRemoteServiceInput");
      return true;
    }
    
    private static class Proxy
      implements ITvRemoteServiceInput
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
      
      public void clearInputBridge(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvRemoteServiceInput");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void closeInputBridge(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvRemoteServiceInput");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.tv.ITvRemoteServiceInput";
      }
      
      public void openInputBridge(IBinder paramIBinder, String paramString, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvRemoteServiceInput");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendKeyDown(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvRemoteServiceInput");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendKeyUp(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvRemoteServiceInput");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendPointerDown(IBinder paramIBinder, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvRemoteServiceInput");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendPointerSync(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvRemoteServiceInput");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendPointerUp(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvRemoteServiceInput");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramInt);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendTimestamp(IBinder paramIBinder, long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvRemoteServiceInput");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeLong(paramLong);
          mRemote.transact(4, localParcel, null, 1);
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
