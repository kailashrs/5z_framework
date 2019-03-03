package com.android.internal.widget;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ICheckCredentialProgressCallback
  extends IInterface
{
  public abstract void onCredentialVerified()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICheckCredentialProgressCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.widget.ICheckCredentialProgressCallback";
    static final int TRANSACTION_onCredentialVerified = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.widget.ICheckCredentialProgressCallback");
    }
    
    public static ICheckCredentialProgressCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.widget.ICheckCredentialProgressCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ICheckCredentialProgressCallback))) {
        return (ICheckCredentialProgressCallback)localIInterface;
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
      if (paramInt1 != 1)
      {
        if (paramInt1 != 1598968902) {
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        }
        paramParcel2.writeString("com.android.internal.widget.ICheckCredentialProgressCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.widget.ICheckCredentialProgressCallback");
      onCredentialVerified();
      return true;
    }
    
    private static class Proxy
      implements ICheckCredentialProgressCallback
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
        return "com.android.internal.widget.ICheckCredentialProgressCallback";
      }
      
      public void onCredentialVerified()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.widget.ICheckCredentialProgressCallback");
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
