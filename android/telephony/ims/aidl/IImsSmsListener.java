package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsSmsListener
  extends IInterface
{
  public abstract void onSendSmsResult(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void onSmsReceived(int paramInt, String paramString, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onSmsStatusReportReceived(int paramInt1, int paramInt2, String paramString, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsSmsListener
  {
    private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsSmsListener";
    static final int TRANSACTION_onSendSmsResult = 1;
    static final int TRANSACTION_onSmsReceived = 3;
    static final int TRANSACTION_onSmsStatusReportReceived = 2;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.ims.aidl.IImsSmsListener");
    }
    
    public static IImsSmsListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.ims.aidl.IImsSmsListener");
      if ((localIInterface != null) && ((localIInterface instanceof IImsSmsListener))) {
        return (IImsSmsListener)localIInterface;
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
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsSmsListener");
          onSmsReceived(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.createByteArray());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsSmsListener");
          onSmsStatusReportReceived(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.createByteArray());
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsSmsListener");
        onSendSmsResult(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.telephony.ims.aidl.IImsSmsListener");
      return true;
    }
    
    private static class Proxy
      implements IImsSmsListener
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
        return "android.telephony.ims.aidl.IImsSmsListener";
      }
      
      public void onSendSmsResult(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsSmsListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSmsReceived(int paramInt, String paramString, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsSmsListener");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSmsStatusReportReceived(int paramInt1, int paramInt2, String paramString, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsSmsListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeString(paramString);
          localParcel.writeByteArray(paramArrayOfByte);
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
