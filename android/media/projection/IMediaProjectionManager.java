package android.media.projection;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IMediaProjectionManager
  extends IInterface
{
  public abstract void addCallback(IMediaProjectionWatcherCallback paramIMediaProjectionWatcherCallback)
    throws RemoteException;
  
  public abstract IMediaProjection createProjection(int paramInt1, String paramString, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract MediaProjectionInfo getActiveProjectionInfo()
    throws RemoteException;
  
  public abstract boolean hasProjectionPermission(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isValidMediaProjection(IMediaProjection paramIMediaProjection)
    throws RemoteException;
  
  public abstract void removeCallback(IMediaProjectionWatcherCallback paramIMediaProjectionWatcherCallback)
    throws RemoteException;
  
  public abstract void stopActiveProjection()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMediaProjectionManager
  {
    private static final String DESCRIPTOR = "android.media.projection.IMediaProjectionManager";
    static final int TRANSACTION_addCallback = 6;
    static final int TRANSACTION_createProjection = 2;
    static final int TRANSACTION_getActiveProjectionInfo = 4;
    static final int TRANSACTION_hasProjectionPermission = 1;
    static final int TRANSACTION_isValidMediaProjection = 3;
    static final int TRANSACTION_removeCallback = 7;
    static final int TRANSACTION_stopActiveProjection = 5;
    
    public Stub()
    {
      attachInterface(this, "android.media.projection.IMediaProjectionManager");
    }
    
    public static IMediaProjectionManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.projection.IMediaProjectionManager");
      if ((localIInterface != null) && ((localIInterface instanceof IMediaProjectionManager))) {
        return (IMediaProjectionManager)localIInterface;
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
        boolean bool = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 7: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjectionManager");
          removeCallback(IMediaProjectionWatcherCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjectionManager");
          addCallback(IMediaProjectionWatcherCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjectionManager");
          stopActiveProjection();
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjectionManager");
          paramParcel1 = getActiveProjectionInfo();
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
        case 3: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjectionManager");
          paramInt1 = isValidMediaProjection(IMediaProjection.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjectionManager");
          paramInt1 = paramParcel1.readInt();
          String str = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          paramParcel1 = createProjection(paramInt1, str, paramInt2, bool);
          paramParcel2.writeNoException();
          if (paramParcel1 != null) {
            paramParcel1 = paramParcel1.asBinder();
          } else {
            paramParcel1 = null;
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.media.projection.IMediaProjectionManager");
        paramInt1 = hasProjectionPermission(paramParcel1.readInt(), paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.media.projection.IMediaProjectionManager");
      return true;
    }
    
    private static class Proxy
      implements IMediaProjectionManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addCallback(IMediaProjectionWatcherCallback paramIMediaProjectionWatcherCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjectionManager");
          if (paramIMediaProjectionWatcherCallback != null) {
            paramIMediaProjectionWatcherCallback = paramIMediaProjectionWatcherCallback.asBinder();
          } else {
            paramIMediaProjectionWatcherCallback = null;
          }
          localParcel1.writeStrongBinder(paramIMediaProjectionWatcherCallback);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public IMediaProjection createProjection(int paramInt1, String paramString, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjectionManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = IMediaProjection.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public MediaProjectionInfo getActiveProjectionInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjectionManager");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          MediaProjectionInfo localMediaProjectionInfo;
          if (localParcel2.readInt() != 0) {
            localMediaProjectionInfo = (MediaProjectionInfo)MediaProjectionInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localMediaProjectionInfo = null;
          }
          return localMediaProjectionInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.projection.IMediaProjectionManager";
      }
      
      public boolean hasProjectionPermission(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjectionManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public boolean isValidMediaProjection(IMediaProjection paramIMediaProjection)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjectionManager");
          if (paramIMediaProjection != null) {
            paramIMediaProjection = paramIMediaProjection.asBinder();
          } else {
            paramIMediaProjection = null;
          }
          localParcel1.writeStrongBinder(paramIMediaProjection);
          paramIMediaProjection = mRemote;
          boolean bool = false;
          paramIMediaProjection.transact(3, localParcel1, localParcel2, 0);
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
      
      public void removeCallback(IMediaProjectionWatcherCallback paramIMediaProjectionWatcherCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjectionManager");
          if (paramIMediaProjectionWatcherCallback != null) {
            paramIMediaProjectionWatcherCallback = paramIMediaProjectionWatcherCallback.asBinder();
          } else {
            paramIMediaProjectionWatcherCallback = null;
          }
          localParcel1.writeStrongBinder(paramIMediaProjectionWatcherCallback);
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
      
      public void stopActiveProjection()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.projection.IMediaProjectionManager");
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
    }
  }
}
