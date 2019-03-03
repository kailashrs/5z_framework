package com.android.internal.policy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IKeyguardExitCallback
  extends IInterface
{
  public abstract void onKeyguardExitResult(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IKeyguardExitCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.policy.IKeyguardExitCallback";
    static final int TRANSACTION_onKeyguardExitResult = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.policy.IKeyguardExitCallback");
    }
    
    public static IKeyguardExitCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.policy.IKeyguardExitCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IKeyguardExitCallback))) {
        return (IKeyguardExitCallback)localIInterface;
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
        paramParcel2.writeString("com.android.internal.policy.IKeyguardExitCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardExitCallback");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onKeyguardExitResult(bool);
      return true;
    }
    
    private static class Proxy
      implements IKeyguardExitCallback
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
        return "com.android.internal.policy.IKeyguardExitCallback";
      }
      
      public void onKeyguardExitResult(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardExitCallback");
          localParcel.writeInt(paramBoolean);
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
