package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IEthernetManager
  extends IInterface
{
  public abstract void addListener(IEthernetServiceListener paramIEthernetServiceListener)
    throws RemoteException;
  
  public abstract String[] getAvailableInterfaces()
    throws RemoteException;
  
  public abstract IpConfiguration getConfiguration(String paramString)
    throws RemoteException;
  
  public abstract int getEthernetSleepPolicy()
    throws RemoteException;
  
  public abstract int getEthernetState()
    throws RemoteException;
  
  public abstract boolean isAvailable(String paramString)
    throws RemoteException;
  
  public abstract void removeListener(IEthernetServiceListener paramIEthernetServiceListener)
    throws RemoteException;
  
  public abstract void setConfiguration(String paramString, IpConfiguration paramIpConfiguration)
    throws RemoteException;
  
  public abstract boolean setEthernetEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setEthernetSleepPolicy(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IEthernetManager
  {
    private static final String DESCRIPTOR = "android.net.IEthernetManager";
    static final int TRANSACTION_addListener = 5;
    static final int TRANSACTION_getAvailableInterfaces = 1;
    static final int TRANSACTION_getConfiguration = 2;
    static final int TRANSACTION_getEthernetSleepPolicy = 10;
    static final int TRANSACTION_getEthernetState = 8;
    static final int TRANSACTION_isAvailable = 4;
    static final int TRANSACTION_removeListener = 6;
    static final int TRANSACTION_setConfiguration = 3;
    static final int TRANSACTION_setEthernetEnabled = 7;
    static final int TRANSACTION_setEthernetSleepPolicy = 9;
    
    public Stub()
    {
      attachInterface(this, "android.net.IEthernetManager");
    }
    
    public static IEthernetManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.IEthernetManager");
      if ((localIInterface != null) && ((localIInterface instanceof IEthernetManager))) {
        return (IEthernetManager)localIInterface;
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
        boolean bool = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 10: 
          paramParcel1.enforceInterface("android.net.IEthernetManager");
          paramInt1 = getEthernetSleepPolicy();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.net.IEthernetManager");
          setEthernetSleepPolicy(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.net.IEthernetManager");
          paramInt1 = getEthernetState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.IEthernetManager");
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          paramInt1 = setEthernetEnabled(bool);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.IEthernetManager");
          removeListener(IEthernetServiceListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.IEthernetManager");
          addListener(IEthernetServiceListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.IEthernetManager");
          paramInt1 = isAvailable(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.IEthernetManager");
          String str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (IpConfiguration)IpConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          setConfiguration(str, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.IEthernetManager");
          paramParcel1 = getConfiguration(paramParcel1.readString());
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
        paramParcel1.enforceInterface("android.net.IEthernetManager");
        paramParcel1 = getAvailableInterfaces();
        paramParcel2.writeNoException();
        paramParcel2.writeStringArray(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.net.IEthernetManager");
      return true;
    }
    
    private static class Proxy
      implements IEthernetManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addListener(IEthernetServiceListener paramIEthernetServiceListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IEthernetManager");
          if (paramIEthernetServiceListener != null) {
            paramIEthernetServiceListener = paramIEthernetServiceListener.asBinder();
          } else {
            paramIEthernetServiceListener = null;
          }
          localParcel1.writeStrongBinder(paramIEthernetServiceListener);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String[] getAvailableInterfaces()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IEthernetManager");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IpConfiguration getConfiguration(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IEthernetManager");
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (IpConfiguration)IpConfiguration.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getEthernetSleepPolicy()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IEthernetManager");
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getEthernetState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IEthernetManager");
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.IEthernetManager";
      }
      
      public boolean isAvailable(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IEthernetManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(4, localParcel1, localParcel2, 0);
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
      
      public void removeListener(IEthernetServiceListener paramIEthernetServiceListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IEthernetManager");
          if (paramIEthernetServiceListener != null) {
            paramIEthernetServiceListener = paramIEthernetServiceListener.asBinder();
          } else {
            paramIEthernetServiceListener = null;
          }
          localParcel1.writeStrongBinder(paramIEthernetServiceListener);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setConfiguration(String paramString, IpConfiguration paramIpConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IEthernetManager");
          localParcel1.writeString(paramString);
          if (paramIpConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramIpConfiguration.writeToParcel(localParcel1, 0);
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
      
      public boolean setEthernetEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IEthernetManager");
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
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
      
      public void setEthernetSleepPolicy(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IEthernetManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
