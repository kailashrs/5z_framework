package com.android.internal.policy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IKeyguardDismissCallback
  extends IInterface
{
  public abstract void onDismissCancelled()
    throws RemoteException;
  
  public abstract void onDismissError()
    throws RemoteException;
  
  public abstract void onDismissSucceeded()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IKeyguardDismissCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.policy.IKeyguardDismissCallback";
    static final int TRANSACTION_onDismissCancelled = 3;
    static final int TRANSACTION_onDismissError = 1;
    static final int TRANSACTION_onDismissSucceeded = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.policy.IKeyguardDismissCallback");
    }
    
    public static IKeyguardDismissCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.policy.IKeyguardDismissCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IKeyguardDismissCallback))) {
        return (IKeyguardDismissCallback)localIInterface;
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
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardDismissCallback");
          onDismissCancelled();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardDismissCallback");
          onDismissSucceeded();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardDismissCallback");
        onDismissError();
        return true;
      }
      paramParcel2.writeString("com.android.internal.policy.IKeyguardDismissCallback");
      return true;
    }
    
    private static class Proxy
      implements IKeyguardDismissCallback
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
        return "com.android.internal.policy.IKeyguardDismissCallback";
      }
      
      public void onDismissCancelled()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardDismissCallback");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDismissError()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardDismissCallback");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDismissSucceeded()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardDismissCallback");
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
