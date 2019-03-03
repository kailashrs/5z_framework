package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsServiceFeatureCallback
  extends IInterface
{
  public abstract void imsFeatureCreated(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void imsFeatureRemoved(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void imsStatusChanged(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsServiceFeatureCallback
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsServiceFeatureCallback";
    static final int TRANSACTION_imsFeatureCreated = 1;
    static final int TRANSACTION_imsFeatureRemoved = 2;
    static final int TRANSACTION_imsStatusChanged = 3;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsServiceFeatureCallback");
    }
    
    public static IImsServiceFeatureCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsServiceFeatureCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IImsServiceFeatureCallback))) {
        return (IImsServiceFeatureCallback)localIInterface;
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
          paramParcel1.enforceInterface("com.android.ims.internal.IImsServiceFeatureCallback");
          imsStatusChanged(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsServiceFeatureCallback");
          imsFeatureRemoved(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.IImsServiceFeatureCallback");
        imsFeatureCreated(paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.IImsServiceFeatureCallback");
      return true;
    }
    
    private static class Proxy
      implements IImsServiceFeatureCallback
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
        return "com.android.ims.internal.IImsServiceFeatureCallback";
      }
      
      public void imsFeatureCreated(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsServiceFeatureCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void imsFeatureRemoved(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsServiceFeatureCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void imsStatusChanged(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsServiceFeatureCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(3, localParcel, null, 1);
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
