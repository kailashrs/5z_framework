package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.RemoteException;

public abstract interface ICarrierConfigLoader
  extends IInterface
{
  public abstract PersistableBundle getConfigForSubId(int paramInt)
    throws RemoteException;
  
  public abstract String getDefaultCarrierServicePackageName()
    throws RemoteException;
  
  public abstract boolean isCarrierConfigVaild(int paramInt)
    throws RemoteException;
  
  public abstract void notifyConfigChangedForSubId(int paramInt)
    throws RemoteException;
  
  public abstract void updateConfigForPhoneId(int paramInt, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICarrierConfigLoader
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.ICarrierConfigLoader";
    static final int TRANSACTION_getConfigForSubId = 1;
    static final int TRANSACTION_getDefaultCarrierServicePackageName = 4;
    static final int TRANSACTION_isCarrierConfigVaild = 5;
    static final int TRANSACTION_notifyConfigChangedForSubId = 2;
    static final int TRANSACTION_updateConfigForPhoneId = 3;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.ICarrierConfigLoader");
    }
    
    public static ICarrierConfigLoader asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.ICarrierConfigLoader");
      if ((localIInterface != null) && ((localIInterface instanceof ICarrierConfigLoader))) {
        return (ICarrierConfigLoader)localIInterface;
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
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ICarrierConfigLoader");
          paramInt1 = isCarrierConfigVaild(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ICarrierConfigLoader");
          paramParcel1 = getDefaultCarrierServicePackageName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ICarrierConfigLoader");
          updateConfigForPhoneId(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ICarrierConfigLoader");
          notifyConfigChangedForSubId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.ICarrierConfigLoader");
        paramParcel1 = getConfigForSubId(paramParcel1.readInt());
        paramParcel2.writeNoException();
        if (paramParcel1 != null)
        {
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
        }
        else
        {
          paramParcel2.writeInt(0);
        }
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.ICarrierConfigLoader");
      return true;
    }
    
    private static class Proxy
      implements ICarrierConfigLoader
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
      
      public PersistableBundle getConfigForSubId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ICarrierConfigLoader");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          PersistableBundle localPersistableBundle;
          if (localParcel2.readInt() != 0) {
            localPersistableBundle = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(localParcel2);
          } else {
            localPersistableBundle = null;
          }
          return localPersistableBundle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getDefaultCarrierServicePackageName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ICarrierConfigLoader");
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telephony.ICarrierConfigLoader";
      }
      
      public boolean isCarrierConfigVaild(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ICarrierConfigLoader");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public void notifyConfigChangedForSubId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ICarrierConfigLoader");
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateConfigForPhoneId(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ICarrierConfigLoader");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
