package android.media.session;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.KeyEvent;

public abstract interface ICallback
  extends IInterface
{
  public abstract void onAddressedPlayerChangedToMediaButtonReceiver(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void onAddressedPlayerChangedToMediaSession(MediaSession.Token paramToken)
    throws RemoteException;
  
  public abstract void onMediaKeyEventDispatchedToMediaButtonReceiver(KeyEvent paramKeyEvent, ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void onMediaKeyEventDispatchedToMediaSession(KeyEvent paramKeyEvent, MediaSession.Token paramToken)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICallback
  {
    private static final String DESCRIPTOR = "android.media.session.ICallback";
    static final int TRANSACTION_onAddressedPlayerChangedToMediaButtonReceiver = 4;
    static final int TRANSACTION_onAddressedPlayerChangedToMediaSession = 3;
    static final int TRANSACTION_onMediaKeyEventDispatchedToMediaButtonReceiver = 2;
    static final int TRANSACTION_onMediaKeyEventDispatchedToMediaSession = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.session.ICallback");
    }
    
    public static ICallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.session.ICallback");
      if ((localIInterface != null) && ((localIInterface instanceof ICallback))) {
        return (ICallback)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("android.media.session.ICallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          onAddressedPlayerChangedToMediaButtonReceiver(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.session.ICallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (MediaSession.Token)MediaSession.Token.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onAddressedPlayerChangedToMediaSession(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.session.ICallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onMediaKeyEventDispatchedToMediaButtonReceiver(paramParcel2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.media.session.ICallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (MediaSession.Token)MediaSession.Token.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject3;
        }
        onMediaKeyEventDispatchedToMediaSession(paramParcel2, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.media.session.ICallback");
      return true;
    }
    
    private static class Proxy
      implements ICallback
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
        return "android.media.session.ICallback";
      }
      
      public void onAddressedPlayerChangedToMediaButtonReceiver(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ICallback");
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
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
      
      public void onAddressedPlayerChangedToMediaSession(MediaSession.Token paramToken)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ICallback");
          if (paramToken != null)
          {
            localParcel.writeInt(1);
            paramToken.writeToParcel(localParcel, 0);
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
      
      public void onMediaKeyEventDispatchedToMediaButtonReceiver(KeyEvent paramKeyEvent, ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ICallback");
          if (paramKeyEvent != null)
          {
            localParcel.writeInt(1);
            paramKeyEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onMediaKeyEventDispatchedToMediaSession(KeyEvent paramKeyEvent, MediaSession.Token paramToken)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ICallback");
          if (paramKeyEvent != null)
          {
            localParcel.writeInt(1);
            paramKeyEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramToken != null)
          {
            localParcel.writeInt(1);
            paramToken.writeToParcel(localParcel, 0);
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
    }
  }
}
