package com.android.internal.policy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IKeyguardDrawnCallback
  extends IInterface
{
  public abstract void onDrawn()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IKeyguardDrawnCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.policy.IKeyguardDrawnCallback";
    static final int TRANSACTION_onDrawn = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.policy.IKeyguardDrawnCallback");
    }
    
    public static IKeyguardDrawnCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.policy.IKeyguardDrawnCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IKeyguardDrawnCallback))) {
        return (IKeyguardDrawnCallback)localIInterface;
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
        paramParcel2.writeString("com.android.internal.policy.IKeyguardDrawnCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardDrawnCallback");
      onDrawn();
      return true;
    }
    
    private static class Proxy
      implements IKeyguardDrawnCallback
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
        return "com.android.internal.policy.IKeyguardDrawnCallback";
      }
      
      public void onDrawn()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardDrawnCallback");
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
