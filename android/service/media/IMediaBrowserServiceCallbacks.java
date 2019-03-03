package android.service.media;

import android.content.pm.ParceledListSlice;
import android.media.session.MediaSession.Token;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IMediaBrowserServiceCallbacks
  extends IInterface
{
  public abstract void onConnect(String paramString, MediaSession.Token paramToken, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onConnectFailed()
    throws RemoteException;
  
  public abstract void onLoadChildren(String paramString, ParceledListSlice paramParceledListSlice)
    throws RemoteException;
  
  public abstract void onLoadChildrenWithOptions(String paramString, ParceledListSlice paramParceledListSlice, Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMediaBrowserServiceCallbacks
  {
    private static final String DESCRIPTOR = "android.service.media.IMediaBrowserServiceCallbacks";
    static final int TRANSACTION_onConnect = 1;
    static final int TRANSACTION_onConnectFailed = 2;
    static final int TRANSACTION_onLoadChildren = 3;
    static final int TRANSACTION_onLoadChildrenWithOptions = 4;
    
    public Stub()
    {
      attachInterface(this, "android.service.media.IMediaBrowserServiceCallbacks");
    }
    
    public static IMediaBrowserServiceCallbacks asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.media.IMediaBrowserServiceCallbacks");
      if ((localIInterface != null) && ((localIInterface instanceof IMediaBrowserServiceCallbacks))) {
        return (IMediaBrowserServiceCallbacks)localIInterface;
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
        Object localObject = null;
        String str1 = null;
        String str2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("android.service.media.IMediaBrowserServiceCallbacks");
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          onLoadChildrenWithOptions(str1, paramParcel2, paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.media.IMediaBrowserServiceCallbacks");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject;
          }
          onLoadChildren(paramParcel2, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.media.IMediaBrowserServiceCallbacks");
          onConnectFailed();
          return true;
        }
        paramParcel1.enforceInterface("android.service.media.IMediaBrowserServiceCallbacks");
        str2 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (MediaSession.Token)MediaSession.Token.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = str1;
        }
        onConnect(str2, paramParcel2, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.service.media.IMediaBrowserServiceCallbacks");
      return true;
    }
    
    private static class Proxy
      implements IMediaBrowserServiceCallbacks
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
        return "android.service.media.IMediaBrowserServiceCallbacks";
      }
      
      public void onConnect(String paramString, MediaSession.Token paramToken, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.media.IMediaBrowserServiceCallbacks");
          localParcel.writeString(paramString);
          if (paramToken != null)
          {
            localParcel.writeInt(1);
            paramToken.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onConnectFailed()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.media.IMediaBrowserServiceCallbacks");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLoadChildren(String paramString, ParceledListSlice paramParceledListSlice)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.media.IMediaBrowserServiceCallbacks");
          localParcel.writeString(paramString);
          if (paramParceledListSlice != null)
          {
            localParcel.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLoadChildrenWithOptions(String paramString, ParceledListSlice paramParceledListSlice, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.media.IMediaBrowserServiceCallbacks");
          localParcel.writeString(paramString);
          if (paramParceledListSlice != null)
          {
            localParcel.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
