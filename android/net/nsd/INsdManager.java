package android.net.nsd;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface INsdManager
  extends IInterface
{
  public abstract Messenger getMessenger()
    throws RemoteException;
  
  public abstract void setEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INsdManager
  {
    private static final String DESCRIPTOR = "android.net.nsd.INsdManager";
    static final int TRANSACTION_getMessenger = 1;
    static final int TRANSACTION_setEnabled = 2;
    
    public Stub()
    {
      attachInterface(this, "android.net.nsd.INsdManager");
    }
    
    public static INsdManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.nsd.INsdManager");
      if ((localIInterface != null) && ((localIInterface instanceof INsdManager))) {
        return (INsdManager)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.net.nsd.INsdManager");
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          setEnabled(bool);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.net.nsd.INsdManager");
        paramParcel1 = getMessenger();
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
      }
      paramParcel2.writeString("android.net.nsd.INsdManager");
      return true;
    }
    
    private static class Proxy
      implements INsdManager
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
        return "android.net.nsd.INsdManager";
      }
      
      public Messenger getMessenger()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.nsd.INsdManager");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Messenger localMessenger;
          if (localParcel2.readInt() != 0) {
            localMessenger = (Messenger)Messenger.CREATOR.createFromParcel(localParcel2);
          } else {
            localMessenger = null;
          }
          return localMessenger;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.nsd.INsdManager");
          localParcel1.writeInt(paramBoolean);
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
