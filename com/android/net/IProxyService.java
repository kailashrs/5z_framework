package com.android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IProxyService
  extends IInterface
{
  public abstract String resolvePacFile(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setPacFile(String paramString)
    throws RemoteException;
  
  public abstract void startPacSystem()
    throws RemoteException;
  
  public abstract void stopPacSystem()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IProxyService
  {
    private static final String DESCRIPTOR = "com.android.net.IProxyService";
    static final int TRANSACTION_resolvePacFile = 1;
    static final int TRANSACTION_setPacFile = 2;
    static final int TRANSACTION_startPacSystem = 3;
    static final int TRANSACTION_stopPacSystem = 4;
    
    public Stub()
    {
      attachInterface(this, "com.android.net.IProxyService");
    }
    
    public static IProxyService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.net.IProxyService");
      if ((localIInterface != null) && ((localIInterface instanceof IProxyService))) {
        return (IProxyService)localIInterface;
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
          paramParcel1.enforceInterface("com.android.net.IProxyService");
          stopPacSystem();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.net.IProxyService");
          startPacSystem();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.net.IProxyService");
          setPacFile(paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("com.android.net.IProxyService");
        paramParcel1 = resolvePacFile(paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeString(paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.net.IProxyService");
      return true;
    }
    
    private static class Proxy
      implements IProxyService
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
        return "com.android.net.IProxyService";
      }
      
      public String resolvePacFile(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.net.IProxyService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.readString();
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPacFile(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.net.IProxyService");
          localParcel.writeString(paramString);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void startPacSystem()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.net.IProxyService");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopPacSystem()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.net.IProxyService");
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
