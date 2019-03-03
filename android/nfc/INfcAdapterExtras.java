package android.nfc;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface INfcAdapterExtras
  extends IInterface
{
  public abstract void authenticate(String paramString, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract Bundle close(String paramString, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int getCardEmulationRoute(String paramString)
    throws RemoteException;
  
  public abstract String getDriverName(String paramString)
    throws RemoteException;
  
  public abstract Bundle open(String paramString, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void setCardEmulationRoute(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract Bundle transceive(String paramString, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INfcAdapterExtras
  {
    private static final String DESCRIPTOR = "android.nfc.INfcAdapterExtras";
    static final int TRANSACTION_authenticate = 6;
    static final int TRANSACTION_close = 2;
    static final int TRANSACTION_getCardEmulationRoute = 4;
    static final int TRANSACTION_getDriverName = 7;
    static final int TRANSACTION_open = 1;
    static final int TRANSACTION_setCardEmulationRoute = 5;
    static final int TRANSACTION_transceive = 3;
    
    public Stub()
    {
      attachInterface(this, "android.nfc.INfcAdapterExtras");
    }
    
    public static INfcAdapterExtras asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.nfc.INfcAdapterExtras");
      if ((localIInterface != null) && ((localIInterface instanceof INfcAdapterExtras))) {
        return (INfcAdapterExtras)localIInterface;
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
        case 7: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapterExtras");
          paramParcel1 = getDriverName(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapterExtras");
          authenticate(paramParcel1.readString(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapterExtras");
          setCardEmulationRoute(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapterExtras");
          paramInt1 = getCardEmulationRoute(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapterExtras");
          paramParcel1 = transceive(paramParcel1.readString(), paramParcel1.createByteArray());
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
        case 2: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapterExtras");
          paramParcel1 = close(paramParcel1.readString(), paramParcel1.readStrongBinder());
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
        paramParcel1.enforceInterface("android.nfc.INfcAdapterExtras");
        paramParcel1 = open(paramParcel1.readString(), paramParcel1.readStrongBinder());
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
      paramParcel2.writeString("android.nfc.INfcAdapterExtras");
      return true;
    }
    
    private static class Proxy
      implements INfcAdapterExtras
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
      
      public void authenticate(String paramString, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapterExtras");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
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
      
      public Bundle close(String paramString, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapterExtras");
          localParcel1.writeString(paramString);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
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
      
      public int getCardEmulationRoute(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapterExtras");
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
      
      public String getDriverName(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapterExtras");
          localParcel1.writeString(paramString);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.nfc.INfcAdapterExtras";
      }
      
      public Bundle open(String paramString, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapterExtras");
          localParcel1.writeString(paramString);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
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
      
      public void setCardEmulationRoute(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapterExtras");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public Bundle transceive(String paramString, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapterExtras");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
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
    }
  }
}
