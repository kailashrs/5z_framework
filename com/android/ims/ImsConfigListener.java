package com.android.ims;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ImsConfigListener
  extends IInterface
{
  public abstract void onGetFeatureResponse(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void onGetVideoQuality(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onSetFeatureResponse(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void onSetVideoQuality(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ImsConfigListener
  {
    private static final String DESCRIPTOR = "com.android.ims.ImsConfigListener";
    static final int TRANSACTION_onGetFeatureResponse = 1;
    static final int TRANSACTION_onGetVideoQuality = 3;
    static final int TRANSACTION_onSetFeatureResponse = 2;
    static final int TRANSACTION_onSetVideoQuality = 4;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.ImsConfigListener");
    }
    
    public static ImsConfigListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.ImsConfigListener");
      if ((localIInterface != null) && ((localIInterface instanceof ImsConfigListener))) {
        return (ImsConfigListener)localIInterface;
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
        case 4: 
          paramParcel1.enforceInterface("com.android.ims.ImsConfigListener");
          onSetVideoQuality(paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.ims.ImsConfigListener");
          onGetVideoQuality(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.ImsConfigListener");
          onSetFeatureResponse(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.ImsConfigListener");
        onGetFeatureResponse(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("com.android.ims.ImsConfigListener");
      return true;
    }
    
    private static class Proxy
      implements ImsConfigListener
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
        return "com.android.ims.ImsConfigListener";
      }
      
      public void onGetFeatureResponse(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.ImsConfigListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGetVideoQuality(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.ImsConfigListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSetFeatureResponse(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.ImsConfigListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSetVideoQuality(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.ImsConfigListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
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
