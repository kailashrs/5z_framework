package android.service.carrier;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface ICarrierMessagingService
  extends IInterface
{
  public abstract void downloadMms(Uri paramUri1, int paramInt, Uri paramUri2, ICarrierMessagingCallback paramICarrierMessagingCallback)
    throws RemoteException;
  
  public abstract void filterSms(MessagePdu paramMessagePdu, String paramString, int paramInt1, int paramInt2, ICarrierMessagingCallback paramICarrierMessagingCallback)
    throws RemoteException;
  
  public abstract void sendDataSms(byte[] paramArrayOfByte, int paramInt1, String paramString, int paramInt2, int paramInt3, ICarrierMessagingCallback paramICarrierMessagingCallback)
    throws RemoteException;
  
  public abstract void sendMms(Uri paramUri1, int paramInt, Uri paramUri2, ICarrierMessagingCallback paramICarrierMessagingCallback)
    throws RemoteException;
  
  public abstract void sendMultipartTextSms(List<String> paramList, int paramInt1, String paramString, int paramInt2, ICarrierMessagingCallback paramICarrierMessagingCallback)
    throws RemoteException;
  
  public abstract void sendTextSms(String paramString1, int paramInt1, String paramString2, int paramInt2, ICarrierMessagingCallback paramICarrierMessagingCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICarrierMessagingService
  {
    private static final String DESCRIPTOR = "android.service.carrier.ICarrierMessagingService";
    static final int TRANSACTION_downloadMms = 6;
    static final int TRANSACTION_filterSms = 1;
    static final int TRANSACTION_sendDataSms = 3;
    static final int TRANSACTION_sendMms = 5;
    static final int TRANSACTION_sendMultipartTextSms = 4;
    static final int TRANSACTION_sendTextSms = 2;
    
    public Stub()
    {
      attachInterface(this, "android.service.carrier.ICarrierMessagingService");
    }
    
    public static ICarrierMessagingService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.carrier.ICarrierMessagingService");
      if ((localIInterface != null) && ((localIInterface instanceof ICarrierMessagingService))) {
        return (ICarrierMessagingService)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("android.service.carrier.ICarrierMessagingService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          }
          downloadMms(paramParcel2, paramInt1, (Uri)localObject3, ICarrierMessagingCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.carrier.ICarrierMessagingService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject1;
          }
          sendMms(paramParcel2, paramInt1, (Uri)localObject3, ICarrierMessagingCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.carrier.ICarrierMessagingService");
          sendMultipartTextSms(paramParcel1.createStringArrayList(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), ICarrierMessagingCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.carrier.ICarrierMessagingService");
          sendDataSms(paramParcel1.createByteArray(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ICarrierMessagingCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.carrier.ICarrierMessagingService");
          sendTextSms(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), ICarrierMessagingCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.service.carrier.ICarrierMessagingService");
        if (paramParcel1.readInt() != 0) {}
        for (paramParcel2 = (MessagePdu)MessagePdu.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = localObject2) {
          break;
        }
        filterSms(paramParcel2, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ICarrierMessagingCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.service.carrier.ICarrierMessagingService");
      return true;
    }
    
    private static class Proxy
      implements ICarrierMessagingService
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
      
      public void downloadMms(Uri paramUri1, int paramInt, Uri paramUri2, ICarrierMessagingCallback paramICarrierMessagingCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.carrier.ICarrierMessagingService");
          if (paramUri1 != null)
          {
            localParcel.writeInt(1);
            paramUri1.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          if (paramUri2 != null)
          {
            localParcel.writeInt(1);
            paramUri2.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramICarrierMessagingCallback != null) {
            paramUri1 = paramICarrierMessagingCallback.asBinder();
          } else {
            paramUri1 = null;
          }
          localParcel.writeStrongBinder(paramUri1);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void filterSms(MessagePdu paramMessagePdu, String paramString, int paramInt1, int paramInt2, ICarrierMessagingCallback paramICarrierMessagingCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.carrier.ICarrierMessagingService");
          if (paramMessagePdu != null)
          {
            localParcel.writeInt(1);
            paramMessagePdu.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramICarrierMessagingCallback != null) {
            paramMessagePdu = paramICarrierMessagingCallback.asBinder();
          } else {
            paramMessagePdu = null;
          }
          localParcel.writeStrongBinder(paramMessagePdu);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.service.carrier.ICarrierMessagingService";
      }
      
      public void sendDataSms(byte[] paramArrayOfByte, int paramInt1, String paramString, int paramInt2, int paramInt3, ICarrierMessagingCallback paramICarrierMessagingCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.carrier.ICarrierMessagingService");
          localParcel.writeByteArray(paramArrayOfByte);
          localParcel.writeInt(paramInt1);
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          if (paramICarrierMessagingCallback != null) {
            paramArrayOfByte = paramICarrierMessagingCallback.asBinder();
          } else {
            paramArrayOfByte = null;
          }
          localParcel.writeStrongBinder(paramArrayOfByte);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendMms(Uri paramUri1, int paramInt, Uri paramUri2, ICarrierMessagingCallback paramICarrierMessagingCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.carrier.ICarrierMessagingService");
          if (paramUri1 != null)
          {
            localParcel.writeInt(1);
            paramUri1.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          if (paramUri2 != null)
          {
            localParcel.writeInt(1);
            paramUri2.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramICarrierMessagingCallback != null) {
            paramUri1 = paramICarrierMessagingCallback.asBinder();
          } else {
            paramUri1 = null;
          }
          localParcel.writeStrongBinder(paramUri1);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendMultipartTextSms(List<String> paramList, int paramInt1, String paramString, int paramInt2, ICarrierMessagingCallback paramICarrierMessagingCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.carrier.ICarrierMessagingService");
          localParcel.writeStringList(paramList);
          localParcel.writeInt(paramInt1);
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt2);
          if (paramICarrierMessagingCallback != null) {
            paramList = paramICarrierMessagingCallback.asBinder();
          } else {
            paramList = null;
          }
          localParcel.writeStrongBinder(paramList);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendTextSms(String paramString1, int paramInt1, String paramString2, int paramInt2, ICarrierMessagingCallback paramICarrierMessagingCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.carrier.ICarrierMessagingService");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt1);
          localParcel.writeString(paramString2);
          localParcel.writeInt(paramInt2);
          if (paramICarrierMessagingCallback != null) {
            paramString1 = paramICarrierMessagingCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
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
