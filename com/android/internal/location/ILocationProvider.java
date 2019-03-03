package com.android.internal.location;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.WorkSource;

public abstract interface ILocationProvider
  extends IInterface
{
  public abstract void disable()
    throws RemoteException;
  
  public abstract void enable()
    throws RemoteException;
  
  public abstract ProviderProperties getProperties()
    throws RemoteException;
  
  public abstract int getStatus(Bundle paramBundle)
    throws RemoteException;
  
  public abstract long getStatusUpdateTime()
    throws RemoteException;
  
  public abstract boolean sendExtraCommand(String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void setRequest(ProviderRequest paramProviderRequest, WorkSource paramWorkSource)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ILocationProvider
  {
    private static final String DESCRIPTOR = "com.android.internal.location.ILocationProvider";
    static final int TRANSACTION_disable = 2;
    static final int TRANSACTION_enable = 1;
    static final int TRANSACTION_getProperties = 4;
    static final int TRANSACTION_getStatus = 5;
    static final int TRANSACTION_getStatusUpdateTime = 6;
    static final int TRANSACTION_sendExtraCommand = 7;
    static final int TRANSACTION_setRequest = 3;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.location.ILocationProvider");
    }
    
    public static ILocationProvider asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.location.ILocationProvider");
      if ((localIInterface != null) && ((localIInterface instanceof ILocationProvider))) {
        return (ILocationProvider)localIInterface;
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
        String str = null;
        ProviderRequest localProviderRequest = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.location.ILocationProvider");
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localProviderRequest;
          }
          paramInt1 = sendExtraCommand(str, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
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
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.location.ILocationProvider");
          long l = getStatusUpdateTime();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.location.ILocationProvider");
          paramParcel1 = new Bundle();
          paramInt1 = getStatus(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.location.ILocationProvider");
          paramParcel1 = getProperties();
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
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.location.ILocationProvider");
          if (paramParcel1.readInt() != 0) {
            localProviderRequest = (ProviderRequest)ProviderRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localProviderRequest = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          setRequest(localProviderRequest, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.location.ILocationProvider");
          disable();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.location.ILocationProvider");
        enable();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.location.ILocationProvider");
      return true;
    }
    
    private static class Proxy
      implements ILocationProvider
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
      
      public void disable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.location.ILocationProvider");
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
      
      public void enable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.location.ILocationProvider");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.location.ILocationProvider";
      }
      
      public ProviderProperties getProperties()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.location.ILocationProvider");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ProviderProperties localProviderProperties;
          if (localParcel2.readInt() != 0) {
            localProviderProperties = (ProviderProperties)ProviderProperties.CREATOR.createFromParcel(localParcel2);
          } else {
            localProviderProperties = null;
          }
          return localProviderProperties;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getStatus(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.location.ILocationProvider");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramBundle.readFromParcel(localParcel2);
          }
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getStatusUpdateTime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.location.ILocationProvider");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean sendExtraCommand(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.location.ILocationProvider");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() == 0) {
            bool = false;
          }
          if (localParcel2.readInt() != 0) {
            paramBundle.readFromParcel(localParcel2);
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRequest(ProviderRequest paramProviderRequest, WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.location.ILocationProvider");
          if (paramProviderRequest != null)
          {
            localParcel1.writeInt(1);
            paramProviderRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
