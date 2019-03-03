package android.media.projection;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IMediaProjection
  extends IInterface
{
  public abstract int applyVirtualDisplayFlags(int paramInt)
    throws RemoteException;
  
  public abstract boolean canProjectAudio()
    throws RemoteException;
  
  public abstract boolean canProjectSecureVideo()
    throws RemoteException;
  
  public abstract boolean canProjectVideo()
    throws RemoteException;
  
  public abstract void registerCallback(IMediaProjectionCallback paramIMediaProjectionCallback)
    throws RemoteException;
  
  public abstract void start(IMediaProjectionCallback paramIMediaProjectionCallback)
    throws RemoteException;
  
  public abstract void stop()
    throws RemoteException;
  
  public abstract void unregisterCallback(IMediaProjectionCallback paramIMediaProjectionCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMediaProjection
  {
    private static final String DESCRIPTOR = "android.media.projection.IMediaProjection";
    static final int TRANSACTION_applyVirtualDisplayFlags = 6;
    static final int TRANSACTION_canProjectAudio = 3;
    static final int TRANSACTION_canProjectSecureVideo = 5;
    static final int TRANSACTION_canProjectVideo = 4;
    static final int TRANSACTION_registerCallback = 7;
    static final int TRANSACTION_start = 1;
    static final int TRANSACTION_stop = 2;
    static final int TRANSACTION_unregisterCallback = 8;
    
    public Stub()
    {
      attachInterface(this, "android.media.projection.IMediaProjection");
    }
    
    public static IMediaProjection asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.projection.IMediaProjection");
      if ((localIInterface != null) && ((localIInterface instanceof IMediaProjection))) {
        return (IMediaProjection)localIInterface;
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
        case 8: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjection");
          unregisterCallback(IMediaProjectionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjection");
          registerCallback(IMediaProjectionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjection");
          paramInt1 = applyVirtualDisplayFlags(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjection");
          paramInt1 = canProjectSecureVideo();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjection");
          paramInt1 = canProjectVideo();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjection");
          paramInt1 = canProjectAudio();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjection");
          stop();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.media.projection.IMediaProjection");
        start(IMediaProjectionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.media.projection.IMediaProjection");
      return true;
    }
    
    private static class Proxy
      implements IMediaProjection
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public int applyVirtualDisplayFlags(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjection");
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
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
      
      public boolean canProjectAudio()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjection");
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
      
      public boolean canProjectSecureVideo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjection");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(5, localParcel1, localParcel2, 0);
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
      
      public boolean canProjectVideo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjection");
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
      
      public String getInterfaceDescriptor()
      {
        return "android.media.projection.IMediaProjection";
      }
      
      public void registerCallback(IMediaProjectionCallback paramIMediaProjectionCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjection");
          if (paramIMediaProjectionCallback != null) {
            paramIMediaProjectionCallback = paramIMediaProjectionCallback.asBinder();
          } else {
            paramIMediaProjectionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIMediaProjectionCallback);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void start(IMediaProjectionCallback paramIMediaProjectionCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjection");
          if (paramIMediaProjectionCallback != null) {
            paramIMediaProjectionCallback = paramIMediaProjectionCallback.asBinder();
          } else {
            paramIMediaProjectionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIMediaProjectionCallback);
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
      
      public void stop()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjection");
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
      
      public void unregisterCallback(IMediaProjectionCallback paramIMediaProjectionCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjection");
          if (paramIMediaProjectionCallback != null) {
            paramIMediaProjectionCallback = paramIMediaProjectionCallback.asBinder();
          } else {
            paramIMediaProjectionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIMediaProjectionCallback);
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
