package android.hardware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IConsumerIrService
  extends IInterface
{
  public abstract int[] getCarrierFrequencies()
    throws RemoteException;
  
  public abstract boolean hasIrEmitter()
    throws RemoteException;
  
  public abstract void transmit(String paramString, int paramInt, int[] paramArrayOfInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IConsumerIrService
  {
    private static final String DESCRIPTOR = "android.hardware.IConsumerIrService";
    static final int TRANSACTION_getCarrierFrequencies = 3;
    static final int TRANSACTION_hasIrEmitter = 1;
    static final int TRANSACTION_transmit = 2;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.IConsumerIrService");
    }
    
    public static IConsumerIrService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.IConsumerIrService");
      if ((localIInterface != null) && ((localIInterface instanceof IConsumerIrService))) {
        return (IConsumerIrService)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.hardware.IConsumerIrService");
          paramParcel1 = getCarrierFrequencies();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.IConsumerIrService");
          transmit(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.IConsumerIrService");
        paramInt1 = hasIrEmitter();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.hardware.IConsumerIrService");
      return true;
    }
    
    private static class Proxy
      implements IConsumerIrService
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
      
      public int[] getCarrierFrequencies()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.IConsumerIrService");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.hardware.IConsumerIrService";
      }
      
      public boolean hasIrEmitter()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.IConsumerIrService");
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
      
      public void transmit(String paramString, int paramInt, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.IConsumerIrService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeIntArray(paramArrayOfInt);
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
    }
  }
}
