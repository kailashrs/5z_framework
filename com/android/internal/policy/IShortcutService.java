package com.android.internal.policy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IShortcutService
  extends IInterface
{
  public abstract void notifyShortcutKeyPressed(long paramLong)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IShortcutService
  {
    private static final String DESCRIPTOR = "com.android.internal.policy.IShortcutService";
    static final int TRANSACTION_notifyShortcutKeyPressed = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.policy.IShortcutService");
    }
    
    public static IShortcutService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.policy.IShortcutService");
      if ((localIInterface != null) && ((localIInterface instanceof IShortcutService))) {
        return (IShortcutService)localIInterface;
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
        paramParcel2.writeString("com.android.internal.policy.IShortcutService");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.policy.IShortcutService");
      notifyShortcutKeyPressed(paramParcel1.readLong());
      return true;
    }
    
    private static class Proxy
      implements IShortcutService
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
        return "com.android.internal.policy.IShortcutService";
      }
      
      public void notifyShortcutKeyPressed(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IShortcutService");
          localParcel.writeLong(paramLong);
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
