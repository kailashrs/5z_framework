package android.bluetooth.le;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IAdvertisingSetCallback
  extends IInterface
{
  public abstract void onAdvertisingDataSet(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onAdvertisingEnabled(int paramInt1, boolean paramBoolean, int paramInt2)
    throws RemoteException;
  
  public abstract void onAdvertisingParametersUpdated(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onAdvertisingSetStarted(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onAdvertisingSetStopped(int paramInt)
    throws RemoteException;
  
  public abstract void onOwnAddressRead(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract void onPeriodicAdvertisingDataSet(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onPeriodicAdvertisingEnabled(int paramInt1, boolean paramBoolean, int paramInt2)
    throws RemoteException;
  
  public abstract void onPeriodicAdvertisingParametersUpdated(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onScanResponseDataSet(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAdvertisingSetCallback
  {
    private static final String DESCRIPTOR = "android.bluetooth.le.IAdvertisingSetCallback";
    static final int TRANSACTION_onAdvertisingDataSet = 5;
    static final int TRANSACTION_onAdvertisingEnabled = 4;
    static final int TRANSACTION_onAdvertisingParametersUpdated = 7;
    static final int TRANSACTION_onAdvertisingSetStarted = 1;
    static final int TRANSACTION_onAdvertisingSetStopped = 3;
    static final int TRANSACTION_onOwnAddressRead = 2;
    static final int TRANSACTION_onPeriodicAdvertisingDataSet = 9;
    static final int TRANSACTION_onPeriodicAdvertisingEnabled = 10;
    static final int TRANSACTION_onPeriodicAdvertisingParametersUpdated = 8;
    static final int TRANSACTION_onScanResponseDataSet = 6;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.le.IAdvertisingSetCallback");
    }
    
    public static IAdvertisingSetCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.le.IAdvertisingSetCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IAdvertisingSetCallback))) {
        return (IAdvertisingSetCallback)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 10: 
          paramParcel1.enforceInterface("android.bluetooth.le.IAdvertisingSetCallback");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onPeriodicAdvertisingEnabled(paramInt1, bool2, paramParcel1.readInt());
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.bluetooth.le.IAdvertisingSetCallback");
          onPeriodicAdvertisingDataSet(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.bluetooth.le.IAdvertisingSetCallback");
          onPeriodicAdvertisingParametersUpdated(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.bluetooth.le.IAdvertisingSetCallback");
          onAdvertisingParametersUpdated(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.bluetooth.le.IAdvertisingSetCallback");
          onScanResponseDataSet(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.le.IAdvertisingSetCallback");
          onAdvertisingDataSet(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.le.IAdvertisingSetCallback");
          paramInt1 = paramParcel1.readInt();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onAdvertisingEnabled(paramInt1, bool2, paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.le.IAdvertisingSetCallback");
          onAdvertisingSetStopped(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.le.IAdvertisingSetCallback");
          onOwnAddressRead(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.le.IAdvertisingSetCallback");
        onAdvertisingSetStarted(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.bluetooth.le.IAdvertisingSetCallback");
      return true;
    }
    
    private static class Proxy
      implements IAdvertisingSetCallback
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
        return "android.bluetooth.le.IAdvertisingSetCallback";
      }
      
      public void onAdvertisingDataSet(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IAdvertisingSetCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onAdvertisingEnabled(int paramInt1, boolean paramBoolean, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IAdvertisingSetCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt2);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onAdvertisingParametersUpdated(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IAdvertisingSetCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onAdvertisingSetStarted(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IAdvertisingSetCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onAdvertisingSetStopped(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IAdvertisingSetCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onOwnAddressRead(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IAdvertisingSetCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeString(paramString);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPeriodicAdvertisingDataSet(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IAdvertisingSetCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPeriodicAdvertisingEnabled(int paramInt1, boolean paramBoolean, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IAdvertisingSetCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt2);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPeriodicAdvertisingParametersUpdated(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IAdvertisingSetCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onScanResponseDataSet(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IAdvertisingSetCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(6, localParcel, null, 1);
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
