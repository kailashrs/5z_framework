package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IPinnedStackController
  extends IInterface
{
  public abstract int getDisplayRotation()
    throws RemoteException;
  
  public abstract void setIsMinimized(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setMinEdgeSize(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPinnedStackController
  {
    private static final String DESCRIPTOR = "android.view.IPinnedStackController";
    static final int TRANSACTION_getDisplayRotation = 3;
    static final int TRANSACTION_setIsMinimized = 1;
    static final int TRANSACTION_setMinEdgeSize = 2;
    
    public Stub()
    {
      attachInterface(this, "android.view.IPinnedStackController");
    }
    
    public static IPinnedStackController asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IPinnedStackController");
      if ((localIInterface != null) && ((localIInterface instanceof IPinnedStackController))) {
        return (IPinnedStackController)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.view.IPinnedStackController");
          paramInt1 = getDisplayRotation();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.IPinnedStackController");
          setMinEdgeSize(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.view.IPinnedStackController");
        boolean bool;
        if (paramParcel1.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        setIsMinimized(bool);
        return true;
      }
      paramParcel2.writeString("android.view.IPinnedStackController");
      return true;
    }
    
    private static class Proxy
      implements IPinnedStackController
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
      
      public int getDisplayRotation()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IPinnedStackController");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.view.IPinnedStackController";
      }
      
      public void setIsMinimized(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IPinnedStackController");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setMinEdgeSize(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IPinnedStackController");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
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
