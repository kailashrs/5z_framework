package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.ims.ImsConfigListener;
import com.android.ims.ImsConfigListener.Stub;

public abstract interface IImsConfig
  extends IInterface
{
  public abstract void getFeatureValue(int paramInt1, int paramInt2, ImsConfigListener paramImsConfigListener)
    throws RemoteException;
  
  public abstract String getProvisionedStringValue(int paramInt)
    throws RemoteException;
  
  public abstract int getProvisionedValue(int paramInt)
    throws RemoteException;
  
  public abstract void getVideoQuality(ImsConfigListener paramImsConfigListener)
    throws RemoteException;
  
  public abstract boolean getVolteProvisioned()
    throws RemoteException;
  
  public abstract void setFeatureValue(int paramInt1, int paramInt2, int paramInt3, ImsConfigListener paramImsConfigListener)
    throws RemoteException;
  
  public abstract int setProvisionedStringValue(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract int setProvisionedValue(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setVideoQuality(int paramInt, ImsConfigListener paramImsConfigListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsConfig
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsConfig";
    static final int TRANSACTION_getFeatureValue = 5;
    static final int TRANSACTION_getProvisionedStringValue = 2;
    static final int TRANSACTION_getProvisionedValue = 1;
    static final int TRANSACTION_getVideoQuality = 8;
    static final int TRANSACTION_getVolteProvisioned = 7;
    static final int TRANSACTION_setFeatureValue = 6;
    static final int TRANSACTION_setProvisionedStringValue = 4;
    static final int TRANSACTION_setProvisionedValue = 3;
    static final int TRANSACTION_setVideoQuality = 9;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsConfig");
    }
    
    public static IImsConfig asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsConfig");
      if ((localIInterface != null) && ((localIInterface instanceof IImsConfig))) {
        return (IImsConfig)localIInterface;
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
        case 9: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsConfig");
          setVideoQuality(paramParcel1.readInt(), ImsConfigListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsConfig");
          getVideoQuality(ImsConfigListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsConfig");
          paramInt1 = getVolteProvisioned();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsConfig");
          setFeatureValue(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), ImsConfigListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsConfig");
          getFeatureValue(paramParcel1.readInt(), paramParcel1.readInt(), ImsConfigListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsConfig");
          paramInt1 = setProvisionedStringValue(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsConfig");
          paramInt1 = setProvisionedValue(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsConfig");
          paramParcel1 = getProvisionedStringValue(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.IImsConfig");
        paramInt1 = getProvisionedValue(paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.IImsConfig");
      return true;
    }
    
    private static class Proxy
      implements IImsConfig
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
      
      public void getFeatureValue(int paramInt1, int paramInt2, ImsConfigListener paramImsConfigListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsConfig");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramImsConfigListener != null) {
            paramImsConfigListener = paramImsConfigListener.asBinder();
          } else {
            paramImsConfigListener = null;
          }
          localParcel.writeStrongBinder(paramImsConfigListener);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.ims.internal.IImsConfig";
      }
      
      public String getProvisionedStringValue(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsConfig");
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getProvisionedValue(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsConfig");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getVideoQuality(ImsConfigListener paramImsConfigListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsConfig");
          if (paramImsConfigListener != null) {
            paramImsConfigListener = paramImsConfigListener.asBinder();
          } else {
            paramImsConfigListener = null;
          }
          localParcel.writeStrongBinder(paramImsConfigListener);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public boolean getVolteProvisioned()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsConfig");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setFeatureValue(int paramInt1, int paramInt2, int paramInt3, ImsConfigListener paramImsConfigListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsConfig");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          if (paramImsConfigListener != null) {
            paramImsConfigListener = paramImsConfigListener.asBinder();
          } else {
            paramImsConfigListener = null;
          }
          localParcel.writeStrongBinder(paramImsConfigListener);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public int setProvisionedStringValue(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsConfig");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int setProvisionedValue(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsConfig");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVideoQuality(int paramInt, ImsConfigListener paramImsConfigListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsConfig");
          localParcel.writeInt(paramInt);
          if (paramImsConfigListener != null) {
            paramImsConfigListener = paramImsConfigListener.asBinder();
          } else {
            paramImsConfigListener = null;
          }
          localParcel.writeStrongBinder(paramImsConfigListener);
          mRemote.transact(9, localParcel, null, 1);
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
