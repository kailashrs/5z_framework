package com.android.internal.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IAppOpsCallback
  extends IInterface
{
  public abstract void opChanged(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAppOpsCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.app.IAppOpsCallback";
    static final int TRANSACTION_opChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.IAppOpsCallback");
    }
    
    public static IAppOpsCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.IAppOpsCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IAppOpsCallback))) {
        return (IAppOpsCallback)localIInterface;
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
        paramParcel2.writeString("com.android.internal.app.IAppOpsCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.app.IAppOpsCallback");
      opChanged(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
      return true;
    }
    
    private static class Proxy
      implements IAppOpsCallback
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
        return "com.android.internal.app.IAppOpsCallback";
      }
      
      public void opChanged(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IAppOpsCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeString(paramString);
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
