package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IUidObserver
  extends IInterface
{
  public abstract void onUidActive(int paramInt)
    throws RemoteException;
  
  public abstract void onUidCachedChanged(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onUidGone(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onUidIdle(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onUidStateChanged(int paramInt1, int paramInt2, long paramLong)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IUidObserver
  {
    private static final String DESCRIPTOR = "android.app.IUidObserver";
    static final int TRANSACTION_onUidActive = 2;
    static final int TRANSACTION_onUidCachedChanged = 5;
    static final int TRANSACTION_onUidGone = 1;
    static final int TRANSACTION_onUidIdle = 3;
    static final int TRANSACTION_onUidStateChanged = 4;
    
    public Stub()
    {
      attachInterface(this, "android.app.IUidObserver");
    }
    
    public static IUidObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IUidObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IUidObserver))) {
        return (IUidObserver)localIInterface;
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
        boolean bool3 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 5: 
          paramParcel1.enforceInterface("android.app.IUidObserver");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          onUidCachedChanged(paramInt1, bool3);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.IUidObserver");
          onUidStateChanged(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readLong());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.IUidObserver");
          paramInt1 = paramParcel1.readInt();
          bool3 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          onUidIdle(paramInt1, bool3);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.IUidObserver");
          onUidActive(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.app.IUidObserver");
        paramInt1 = paramParcel1.readInt();
        bool3 = bool2;
        if (paramParcel1.readInt() != 0) {
          bool3 = true;
        }
        onUidGone(paramInt1, bool3);
        return true;
      }
      paramParcel2.writeString("android.app.IUidObserver");
      return true;
    }
    
    private static class Proxy
      implements IUidObserver
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
        return "android.app.IUidObserver";
      }
      
      public void onUidActive(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IUidObserver");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUidCachedChanged(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IUidObserver");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUidGone(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IUidObserver");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUidIdle(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IUidObserver");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUidStateChanged(int paramInt1, int paramInt2, long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IUidObserver");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeLong(paramLong);
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
