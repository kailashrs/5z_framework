package com.android.internal.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface INetworkWatchlistManager
  extends IInterface
{
  public abstract byte[] getWatchlistConfigHash()
    throws RemoteException;
  
  public abstract void reloadWatchlist()
    throws RemoteException;
  
  public abstract void reportWatchlistIfNecessary()
    throws RemoteException;
  
  public abstract boolean startWatchlistLogging()
    throws RemoteException;
  
  public abstract boolean stopWatchlistLogging()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkWatchlistManager
  {
    private static final String DESCRIPTOR = "com.android.internal.net.INetworkWatchlistManager";
    static final int TRANSACTION_getWatchlistConfigHash = 5;
    static final int TRANSACTION_reloadWatchlist = 3;
    static final int TRANSACTION_reportWatchlistIfNecessary = 4;
    static final int TRANSACTION_startWatchlistLogging = 1;
    static final int TRANSACTION_stopWatchlistLogging = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.net.INetworkWatchlistManager");
    }
    
    public static INetworkWatchlistManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.net.INetworkWatchlistManager");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkWatchlistManager))) {
        return (INetworkWatchlistManager)localIInterface;
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
          paramParcel1.enforceInterface("com.android.internal.net.INetworkWatchlistManager");
          paramParcel1 = getWatchlistConfigHash();
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.net.INetworkWatchlistManager");
          reportWatchlistIfNecessary();
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.net.INetworkWatchlistManager");
          reloadWatchlist();
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.net.INetworkWatchlistManager");
          paramInt1 = stopWatchlistLogging();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.net.INetworkWatchlistManager");
        paramInt1 = startWatchlistLogging();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.net.INetworkWatchlistManager");
      return true;
    }
    
    private static class Proxy
      implements INetworkWatchlistManager
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
        return "com.android.internal.net.INetworkWatchlistManager";
      }
      
      public byte[] getWatchlistConfigHash()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.net.INetworkWatchlistManager");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reloadWatchlist()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.net.INetworkWatchlistManager");
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
      
      public void reportWatchlistIfNecessary()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.net.INetworkWatchlistManager");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean startWatchlistLogging()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.net.INetworkWatchlistManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(1, localParcel1, localParcel2, 0);
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
      
      public boolean stopWatchlistLogging()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.net.INetworkWatchlistManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(2, localParcel1, localParcel2, 0);
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
    }
  }
}
