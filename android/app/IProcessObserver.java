package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IProcessObserver
  extends IInterface
{
  public abstract void onForegroundActivitiesChanged(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onProcessDied(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IProcessObserver
  {
    private static final String DESCRIPTOR = "android.app.IProcessObserver";
    static final int TRANSACTION_onForegroundActivitiesChanged = 1;
    static final int TRANSACTION_onProcessDied = 2;
    
    public Stub()
    {
      attachInterface(this, "android.app.IProcessObserver");
    }
    
    public static IProcessObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IProcessObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IProcessObserver))) {
        return (IProcessObserver)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.app.IProcessObserver");
          onProcessDied(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.app.IProcessObserver");
        paramInt2 = paramParcel1.readInt();
        paramInt1 = paramParcel1.readInt();
        boolean bool;
        if (paramParcel1.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        onForegroundActivitiesChanged(paramInt2, paramInt1, bool);
        return true;
      }
      paramParcel2.writeString("android.app.IProcessObserver");
      return true;
    }
    
    private static class Proxy
      implements IProcessObserver
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
        return "android.app.IProcessObserver";
      }
      
      public void onForegroundActivitiesChanged(int paramInt1, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IProcessObserver");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onProcessDied(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IProcessObserver");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
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
