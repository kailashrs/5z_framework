package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.IRemoteCallback.Stub;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IUserSwitchObserver
  extends IInterface
{
  public abstract void onForegroundProfileSwitch(int paramInt)
    throws RemoteException;
  
  public abstract void onLockedBootComplete(int paramInt)
    throws RemoteException;
  
  public abstract void onUserSwitchComplete(int paramInt)
    throws RemoteException;
  
  public abstract void onUserSwitching(int paramInt, IRemoteCallback paramIRemoteCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IUserSwitchObserver
  {
    private static final String DESCRIPTOR = "android.app.IUserSwitchObserver";
    static final int TRANSACTION_onForegroundProfileSwitch = 3;
    static final int TRANSACTION_onLockedBootComplete = 4;
    static final int TRANSACTION_onUserSwitchComplete = 2;
    static final int TRANSACTION_onUserSwitching = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.IUserSwitchObserver");
    }
    
    public static IUserSwitchObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IUserSwitchObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IUserSwitchObserver))) {
        return (IUserSwitchObserver)localIInterface;
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
        case 4: 
          paramParcel1.enforceInterface("android.app.IUserSwitchObserver");
          onLockedBootComplete(paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.IUserSwitchObserver");
          onForegroundProfileSwitch(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.IUserSwitchObserver");
          onUserSwitchComplete(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.app.IUserSwitchObserver");
        onUserSwitching(paramParcel1.readInt(), IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.app.IUserSwitchObserver");
      return true;
    }
    
    private static class Proxy
      implements IUserSwitchObserver
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
        return "android.app.IUserSwitchObserver";
      }
      
      public void onForegroundProfileSwitch(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IUserSwitchObserver");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLockedBootComplete(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IUserSwitchObserver");
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUserSwitchComplete(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IUserSwitchObserver");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUserSwitching(int paramInt, IRemoteCallback paramIRemoteCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IUserSwitchObserver");
          localParcel.writeInt(paramInt);
          if (paramIRemoteCallback != null) {
            paramIRemoteCallback = paramIRemoteCallback.asBinder();
          } else {
            paramIRemoteCallback = null;
          }
          localParcel.writeStrongBinder(paramIRemoteCallback);
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
