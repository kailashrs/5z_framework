package android.service.carrier;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ICarrierMessagingCallback
  extends IInterface
{
  public abstract void onDownloadMmsComplete(int paramInt)
    throws RemoteException;
  
  public abstract void onFilterComplete(int paramInt)
    throws RemoteException;
  
  public abstract void onSendMmsComplete(int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onSendMultipartSmsComplete(int paramInt, int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract void onSendSmsComplete(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICarrierMessagingCallback
  {
    private static final String DESCRIPTOR = "android.service.carrier.ICarrierMessagingCallback";
    static final int TRANSACTION_onDownloadMmsComplete = 5;
    static final int TRANSACTION_onFilterComplete = 1;
    static final int TRANSACTION_onSendMmsComplete = 4;
    static final int TRANSACTION_onSendMultipartSmsComplete = 3;
    static final int TRANSACTION_onSendSmsComplete = 2;
    
    public Stub()
    {
      attachInterface(this, "android.service.carrier.ICarrierMessagingCallback");
    }
    
    public static ICarrierMessagingCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.carrier.ICarrierMessagingCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ICarrierMessagingCallback))) {
        return (ICarrierMessagingCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.service.carrier.ICarrierMessagingCallback");
          onDownloadMmsComplete(paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.carrier.ICarrierMessagingCallback");
          onSendMmsComplete(paramParcel1.readInt(), paramParcel1.createByteArray());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.carrier.ICarrierMessagingCallback");
          onSendMultipartSmsComplete(paramParcel1.readInt(), paramParcel1.createIntArray());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.carrier.ICarrierMessagingCallback");
          onSendSmsComplete(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.service.carrier.ICarrierMessagingCallback");
        onFilterComplete(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.service.carrier.ICarrierMessagingCallback");
      return true;
    }
    
    private static class Proxy
      implements ICarrierMessagingCallback
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
        return "android.service.carrier.ICarrierMessagingCallback";
      }
      
      public void onDownloadMmsComplete(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.carrier.ICarrierMessagingCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onFilterComplete(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.carrier.ICarrierMessagingCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSendMmsComplete(int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.carrier.ICarrierMessagingCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSendMultipartSmsComplete(int paramInt, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.carrier.ICarrierMessagingCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeIntArray(paramArrayOfInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSendSmsComplete(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.carrier.ICarrierMessagingCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(2, localParcel, null, 1);
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
