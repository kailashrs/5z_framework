package android.service.media;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;

public abstract interface IMediaBrowserService
  extends IInterface
{
  public abstract void addSubscription(String paramString, IBinder paramIBinder, Bundle paramBundle, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
    throws RemoteException;
  
  public abstract void addSubscriptionDeprecated(String paramString, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
    throws RemoteException;
  
  public abstract void connect(String paramString, Bundle paramBundle, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
    throws RemoteException;
  
  public abstract void disconnect(IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
    throws RemoteException;
  
  public abstract void getMediaItem(String paramString, ResultReceiver paramResultReceiver, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
    throws RemoteException;
  
  public abstract void removeSubscription(String paramString, IBinder paramIBinder, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
    throws RemoteException;
  
  public abstract void removeSubscriptionDeprecated(String paramString, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMediaBrowserService
  {
    private static final String DESCRIPTOR = "android.service.media.IMediaBrowserService";
    static final int TRANSACTION_addSubscription = 6;
    static final int TRANSACTION_addSubscriptionDeprecated = 3;
    static final int TRANSACTION_connect = 1;
    static final int TRANSACTION_disconnect = 2;
    static final int TRANSACTION_getMediaItem = 5;
    static final int TRANSACTION_removeSubscription = 7;
    static final int TRANSACTION_removeSubscriptionDeprecated = 4;
    
    public Stub()
    {
      attachInterface(this, "android.service.media.IMediaBrowserService");
    }
    
    public static IMediaBrowserService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.media.IMediaBrowserService");
      if ((localIInterface != null) && ((localIInterface instanceof IMediaBrowserService))) {
        return (IMediaBrowserService)localIInterface;
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
        String str1 = null;
        IBinder localIBinder = null;
        String str2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 7: 
          paramParcel1.enforceInterface("android.service.media.IMediaBrowserService");
          removeSubscription(paramParcel1.readString(), paramParcel1.readStrongBinder(), IMediaBrowserServiceCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.service.media.IMediaBrowserService");
          str1 = paramParcel1.readString();
          localIBinder = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = str2;
          }
          addSubscription(str1, localIBinder, paramParcel2, IMediaBrowserServiceCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.media.IMediaBrowserService");
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = str1;
          }
          getMediaItem(str2, paramParcel2, IMediaBrowserServiceCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.media.IMediaBrowserService");
          removeSubscriptionDeprecated(paramParcel1.readString(), IMediaBrowserServiceCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.media.IMediaBrowserService");
          addSubscriptionDeprecated(paramParcel1.readString(), IMediaBrowserServiceCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.media.IMediaBrowserService");
          disconnect(IMediaBrowserServiceCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.service.media.IMediaBrowserService");
        str2 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = localIBinder;
        }
        connect(str2, paramParcel2, IMediaBrowserServiceCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.service.media.IMediaBrowserService");
      return true;
    }
    
    private static class Proxy
      implements IMediaBrowserService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addSubscription(String paramString, IBinder paramIBinder, Bundle paramBundle, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.media.IMediaBrowserService");
          localParcel.writeString(paramString);
          localParcel.writeStrongBinder(paramIBinder);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIMediaBrowserServiceCallbacks != null) {
            paramString = paramIMediaBrowserServiceCallbacks.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void addSubscriptionDeprecated(String paramString, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.media.IMediaBrowserService");
          localParcel.writeString(paramString);
          if (paramIMediaBrowserServiceCallbacks != null) {
            paramString = paramIMediaBrowserServiceCallbacks.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(3, localParcel, null, 1);
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
      
      public void connect(String paramString, Bundle paramBundle, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.media.IMediaBrowserService");
          localParcel.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIMediaBrowserServiceCallbacks != null) {
            paramString = paramIMediaBrowserServiceCallbacks.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void disconnect(IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.media.IMediaBrowserService");
          if (paramIMediaBrowserServiceCallbacks != null) {
            paramIMediaBrowserServiceCallbacks = paramIMediaBrowserServiceCallbacks.asBinder();
          } else {
            paramIMediaBrowserServiceCallbacks = null;
          }
          localParcel.writeStrongBinder(paramIMediaBrowserServiceCallbacks);
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
        return "android.service.media.IMediaBrowserService";
      }
      
      public void getMediaItem(String paramString, ResultReceiver paramResultReceiver, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.media.IMediaBrowserService");
          localParcel.writeString(paramString);
          if (paramResultReceiver != null)
          {
            localParcel.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIMediaBrowserServiceCallbacks != null) {
            paramString = paramIMediaBrowserServiceCallbacks.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeSubscription(String paramString, IBinder paramIBinder, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.media.IMediaBrowserService");
          localParcel.writeString(paramString);
          localParcel.writeStrongBinder(paramIBinder);
          if (paramIMediaBrowserServiceCallbacks != null) {
            paramString = paramIMediaBrowserServiceCallbacks.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeSubscriptionDeprecated(String paramString, IMediaBrowserServiceCallbacks paramIMediaBrowserServiceCallbacks)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.media.IMediaBrowserService");
          localParcel.writeString(paramString);
          if (paramIMediaBrowserServiceCallbacks != null) {
            paramString = paramIMediaBrowserServiceCallbacks.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
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
