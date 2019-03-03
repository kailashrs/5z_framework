package com.android.internal.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IAppOpsActiveCallback
  extends IInterface
{
  public abstract void opActiveChanged(int paramInt1, int paramInt2, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAppOpsActiveCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.app.IAppOpsActiveCallback";
    static final int TRANSACTION_opActiveChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.IAppOpsActiveCallback");
    }
    
    public static IAppOpsActiveCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.IAppOpsActiveCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IAppOpsActiveCallback))) {
        return (IAppOpsActiveCallback)localIInterface;
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
        paramParcel2.writeString("com.android.internal.app.IAppOpsActiveCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.app.IAppOpsActiveCallback");
      paramInt2 = paramParcel1.readInt();
      paramInt1 = paramParcel1.readInt();
      paramParcel2 = paramParcel1.readString();
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      opActiveChanged(paramInt2, paramInt1, paramParcel2, bool);
      return true;
    }
    
    private static class Proxy
      implements IAppOpsActiveCallback
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
        return "com.android.internal.app.IAppOpsActiveCallback";
      }
      
      public void opActiveChanged(int paramInt1, int paramInt2, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IAppOpsActiveCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeString(paramString);
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
