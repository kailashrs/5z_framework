package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsConfig
  extends IInterface
{
  public abstract void addImsConfigCallback(IImsConfigCallback paramIImsConfigCallback)
    throws RemoteException;
  
  public abstract int getConfigInt(int paramInt)
    throws RemoteException;
  
  public abstract String getConfigString(int paramInt)
    throws RemoteException;
  
  public abstract void removeImsConfigCallback(IImsConfigCallback paramIImsConfigCallback)
    throws RemoteException;
  
  public abstract int setConfigInt(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int setConfigString(int paramInt, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsConfig
  {
    private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsConfig";
    static final int TRANSACTION_addImsConfigCallback = 1;
    static final int TRANSACTION_getConfigInt = 3;
    static final int TRANSACTION_getConfigString = 4;
    static final int TRANSACTION_removeImsConfigCallback = 2;
    static final int TRANSACTION_setConfigInt = 5;
    static final int TRANSACTION_setConfigString = 6;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.ims.aidl.IImsConfig");
    }
    
    public static IImsConfig asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.ims.aidl.IImsConfig");
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
        case 6: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsConfig");
          paramInt1 = setConfigString(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsConfig");
          paramInt1 = setConfigInt(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsConfig");
          paramParcel1 = getConfigString(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsConfig");
          paramInt1 = getConfigInt(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsConfig");
          removeImsConfigCallback(IImsConfigCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsConfig");
        addImsConfigCallback(IImsConfigCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.telephony.ims.aidl.IImsConfig");
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
      
      public void addImsConfigCallback(IImsConfigCallback paramIImsConfigCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsConfig");
          if (paramIImsConfigCallback != null) {
            paramIImsConfigCallback = paramIImsConfigCallback.asBinder();
          } else {
            paramIImsConfigCallback = null;
          }
          localParcel1.writeStrongBinder(paramIImsConfigCallback);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public int getConfigInt(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsConfig");
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
      
      public String getConfigString(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsConfig");
          localParcel1.writeInt(paramInt);
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
        return "android.telephony.ims.aidl.IImsConfig";
      }
      
      public void removeImsConfigCallback(IImsConfigCallback paramIImsConfigCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsConfig");
          if (paramIImsConfigCallback != null) {
            paramIImsConfigCallback = paramIImsConfigCallback.asBinder();
          } else {
            paramIImsConfigCallback = null;
          }
          localParcel1.writeStrongBinder(paramIImsConfigCallback);
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
      
      public int setConfigInt(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsConfig");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
      
      public int setConfigString(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsConfig");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(6, localParcel1, localParcel2, 0);
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
    }
  }
}
