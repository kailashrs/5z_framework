package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsFeatureStatusCallback
  extends IInterface
{
  public abstract void notifyImsFeatureStatus(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsFeatureStatusCallback
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsFeatureStatusCallback";
    static final int TRANSACTION_notifyImsFeatureStatus = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsFeatureStatusCallback");
    }
    
    public static IImsFeatureStatusCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsFeatureStatusCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IImsFeatureStatusCallback))) {
        return (IImsFeatureStatusCallback)localIInterface;
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
        paramParcel2.writeString("com.android.ims.internal.IImsFeatureStatusCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.ims.internal.IImsFeatureStatusCallback");
      notifyImsFeatureStatus(paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IImsFeatureStatusCallback
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
        return "com.android.ims.internal.IImsFeatureStatusCallback";
      }
      
      public void notifyImsFeatureStatus(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsFeatureStatusCallback");
          localParcel.writeInt(paramInt);
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
