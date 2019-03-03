package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IMediaRouterService
  extends IInterface
{
  public abstract MediaRouterClientState getState(IMediaRouterClient paramIMediaRouterClient)
    throws RemoteException;
  
  public abstract boolean isPlaybackActive(IMediaRouterClient paramIMediaRouterClient)
    throws RemoteException;
  
  public abstract void registerClientAsUser(IMediaRouterClient paramIMediaRouterClient, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void requestSetVolume(IMediaRouterClient paramIMediaRouterClient, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void requestUpdateVolume(IMediaRouterClient paramIMediaRouterClient, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setDiscoveryRequest(IMediaRouterClient paramIMediaRouterClient, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSelectedRoute(IMediaRouterClient paramIMediaRouterClient, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void unregisterClient(IMediaRouterClient paramIMediaRouterClient)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMediaRouterService
  {
    private static final String DESCRIPTOR = "android.media.IMediaRouterService";
    static final int TRANSACTION_getState = 3;
    static final int TRANSACTION_isPlaybackActive = 4;
    static final int TRANSACTION_registerClientAsUser = 1;
    static final int TRANSACTION_requestSetVolume = 7;
    static final int TRANSACTION_requestUpdateVolume = 8;
    static final int TRANSACTION_setDiscoveryRequest = 5;
    static final int TRANSACTION_setSelectedRoute = 6;
    static final int TRANSACTION_unregisterClient = 2;
    
    public Stub()
    {
      attachInterface(this, "android.media.IMediaRouterService");
    }
    
    public static IMediaRouterService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IMediaRouterService");
      if ((localIInterface != null) && ((localIInterface instanceof IMediaRouterService))) {
        return (IMediaRouterService)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        IMediaRouterClient localIMediaRouterClient;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 8: 
          paramParcel1.enforceInterface("android.media.IMediaRouterService");
          requestUpdateVolume(IMediaRouterClient.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.media.IMediaRouterService");
          requestSetVolume(IMediaRouterClient.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.IMediaRouterService");
          localIMediaRouterClient = IMediaRouterClient.Stub.asInterface(paramParcel1.readStrongBinder());
          String str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setSelectedRoute(localIMediaRouterClient, str, bool2);
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.IMediaRouterService");
          localIMediaRouterClient = IMediaRouterClient.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setDiscoveryRequest(localIMediaRouterClient, paramInt1, bool2);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.IMediaRouterService");
          paramInt1 = isPlaybackActive(IMediaRouterClient.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.IMediaRouterService");
          paramParcel1 = getState(IMediaRouterClient.Stub.asInterface(paramParcel1.readStrongBinder()));
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
        case 2: 
          paramParcel1.enforceInterface("android.media.IMediaRouterService");
          unregisterClient(IMediaRouterClient.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.media.IMediaRouterService");
        registerClientAsUser(IMediaRouterClient.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.media.IMediaRouterService");
      return true;
    }
    
    private static class Proxy
      implements IMediaRouterService
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
        return "android.media.IMediaRouterService";
      }
      
      public MediaRouterClientState getState(IMediaRouterClient paramIMediaRouterClient)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaRouterService");
          Object localObject = null;
          if (paramIMediaRouterClient != null) {
            paramIMediaRouterClient = paramIMediaRouterClient.asBinder();
          } else {
            paramIMediaRouterClient = null;
          }
          localParcel1.writeStrongBinder(paramIMediaRouterClient);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIMediaRouterClient = (MediaRouterClientState)MediaRouterClientState.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIMediaRouterClient = localObject;
          }
          return paramIMediaRouterClient;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isPlaybackActive(IMediaRouterClient paramIMediaRouterClient)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaRouterService");
          if (paramIMediaRouterClient != null) {
            paramIMediaRouterClient = paramIMediaRouterClient.asBinder();
          } else {
            paramIMediaRouterClient = null;
          }
          localParcel1.writeStrongBinder(paramIMediaRouterClient);
          paramIMediaRouterClient = mRemote;
          boolean bool = false;
          paramIMediaRouterClient.transact(4, localParcel1, localParcel2, 0);
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
      
      public void registerClientAsUser(IMediaRouterClient paramIMediaRouterClient, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaRouterService");
          if (paramIMediaRouterClient != null) {
            paramIMediaRouterClient = paramIMediaRouterClient.asBinder();
          } else {
            paramIMediaRouterClient = null;
          }
          localParcel1.writeStrongBinder(paramIMediaRouterClient);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void requestSetVolume(IMediaRouterClient paramIMediaRouterClient, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaRouterService");
          if (paramIMediaRouterClient != null) {
            paramIMediaRouterClient = paramIMediaRouterClient.asBinder();
          } else {
            paramIMediaRouterClient = null;
          }
          localParcel1.writeStrongBinder(paramIMediaRouterClient);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void requestUpdateVolume(IMediaRouterClient paramIMediaRouterClient, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaRouterService");
          if (paramIMediaRouterClient != null) {
            paramIMediaRouterClient = paramIMediaRouterClient.asBinder();
          } else {
            paramIMediaRouterClient = null;
          }
          localParcel1.writeStrongBinder(paramIMediaRouterClient);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void setDiscoveryRequest(IMediaRouterClient paramIMediaRouterClient, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaRouterService");
          if (paramIMediaRouterClient != null) {
            paramIMediaRouterClient = paramIMediaRouterClient.asBinder();
          } else {
            paramIMediaRouterClient = null;
          }
          localParcel1.writeStrongBinder(paramIMediaRouterClient);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
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
      
      public void setSelectedRoute(IMediaRouterClient paramIMediaRouterClient, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaRouterService");
          if (paramIMediaRouterClient != null) {
            paramIMediaRouterClient = paramIMediaRouterClient.asBinder();
          } else {
            paramIMediaRouterClient = null;
          }
          localParcel1.writeStrongBinder(paramIMediaRouterClient);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
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
      
      public void unregisterClient(IMediaRouterClient paramIMediaRouterClient)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaRouterService");
          if (paramIMediaRouterClient != null) {
            paramIMediaRouterClient = paramIMediaRouterClient.asBinder();
          } else {
            paramIMediaRouterClient = null;
          }
          localParcel1.writeStrongBinder(paramIMediaRouterClient);
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
