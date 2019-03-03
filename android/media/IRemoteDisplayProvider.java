package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IRemoteDisplayProvider
  extends IInterface
{
  public abstract void adjustVolume(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void connect(String paramString)
    throws RemoteException;
  
  public abstract void disconnect(String paramString)
    throws RemoteException;
  
  public abstract void setCallback(IRemoteDisplayCallback paramIRemoteDisplayCallback)
    throws RemoteException;
  
  public abstract void setDiscoveryMode(int paramInt)
    throws RemoteException;
  
  public abstract void setVolume(String paramString, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRemoteDisplayProvider
  {
    private static final String DESCRIPTOR = "android.media.IRemoteDisplayProvider";
    static final int TRANSACTION_adjustVolume = 6;
    static final int TRANSACTION_connect = 3;
    static final int TRANSACTION_disconnect = 4;
    static final int TRANSACTION_setCallback = 1;
    static final int TRANSACTION_setDiscoveryMode = 2;
    static final int TRANSACTION_setVolume = 5;
    
    public Stub()
    {
      attachInterface(this, "android.media.IRemoteDisplayProvider");
    }
    
    public static IRemoteDisplayProvider asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IRemoteDisplayProvider");
      if ((localIInterface != null) && ((localIInterface instanceof IRemoteDisplayProvider))) {
        return (IRemoteDisplayProvider)localIInterface;
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
          paramParcel1.enforceInterface("android.media.IRemoteDisplayProvider");
          adjustVolume(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.IRemoteDisplayProvider");
          setVolume(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.IRemoteDisplayProvider");
          disconnect(paramParcel1.readString());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.IRemoteDisplayProvider");
          connect(paramParcel1.readString());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.IRemoteDisplayProvider");
          setDiscoveryMode(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.media.IRemoteDisplayProvider");
        setCallback(IRemoteDisplayCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.media.IRemoteDisplayProvider");
      return true;
    }
    
    private static class Proxy
      implements IRemoteDisplayProvider
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void adjustVolume(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRemoteDisplayProvider");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void connect(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRemoteDisplayProvider");
          localParcel.writeString(paramString);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void disconnect(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRemoteDisplayProvider");
          localParcel.writeString(paramString);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.IRemoteDisplayProvider";
      }
      
      public void setCallback(IRemoteDisplayCallback paramIRemoteDisplayCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRemoteDisplayProvider");
          if (paramIRemoteDisplayCallback != null) {
            paramIRemoteDisplayCallback = paramIRemoteDisplayCallback.asBinder();
          } else {
            paramIRemoteDisplayCallback = null;
          }
          localParcel.writeStrongBinder(paramIRemoteDisplayCallback);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setDiscoveryMode(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRemoteDisplayProvider");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setVolume(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRemoteDisplayProvider");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
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
