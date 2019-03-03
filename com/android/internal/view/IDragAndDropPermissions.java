package com.android.internal.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IDragAndDropPermissions
  extends IInterface
{
  public abstract void release()
    throws RemoteException;
  
  public abstract void take(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void takeTransient(IBinder paramIBinder)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDragAndDropPermissions
  {
    private static final String DESCRIPTOR = "com.android.internal.view.IDragAndDropPermissions";
    static final int TRANSACTION_release = 3;
    static final int TRANSACTION_take = 1;
    static final int TRANSACTION_takeTransient = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.view.IDragAndDropPermissions");
    }
    
    public static IDragAndDropPermissions asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.view.IDragAndDropPermissions");
      if ((localIInterface != null) && ((localIInterface instanceof IDragAndDropPermissions))) {
        return (IDragAndDropPermissions)localIInterface;
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
          paramParcel1.enforceInterface("com.android.internal.view.IDragAndDropPermissions");
          release();
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.view.IDragAndDropPermissions");
          takeTransient(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.view.IDragAndDropPermissions");
        take(paramParcel1.readStrongBinder());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.view.IDragAndDropPermissions");
      return true;
    }
    
    private static class Proxy
      implements IDragAndDropPermissions
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
        return "com.android.internal.view.IDragAndDropPermissions";
      }
      
      public void release()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IDragAndDropPermissions");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void take(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IDragAndDropPermissions");
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void takeTransient(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IDragAndDropPermissions");
          localParcel1.writeStrongBinder(paramIBinder);
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
