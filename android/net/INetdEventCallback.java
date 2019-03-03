package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface INetdEventCallback
  extends IInterface
{
  public static final int CALLBACK_CALLER_CONNECTIVITY_SERVICE = 0;
  public static final int CALLBACK_CALLER_DEVICE_POLICY = 1;
  public static final int CALLBACK_CALLER_NETWORK_WATCHLIST = 2;
  
  public abstract void onConnectEvent(String paramString, int paramInt1, long paramLong, int paramInt2)
    throws RemoteException;
  
  public abstract void onDnsEvent(String paramString, String[] paramArrayOfString, int paramInt1, long paramLong, int paramInt2)
    throws RemoteException;
  
  public abstract void onPrivateDnsValidationEvent(int paramInt, String paramString1, String paramString2, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetdEventCallback
  {
    private static final String DESCRIPTOR = "android.net.INetdEventCallback";
    static final int TRANSACTION_onConnectEvent = 3;
    static final int TRANSACTION_onDnsEvent = 1;
    static final int TRANSACTION_onPrivateDnsValidationEvent = 2;
    
    public Stub()
    {
      attachInterface(this, "android.net.INetdEventCallback");
    }
    
    public static INetdEventCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.INetdEventCallback");
      if ((localIInterface != null) && ((localIInterface instanceof INetdEventCallback))) {
        return (INetdEventCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.net.INetdEventCallback");
          onConnectEvent(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.INetdEventCallback");
          paramInt1 = paramParcel1.readInt();
          String str = paramParcel1.readString();
          paramParcel2 = paramParcel1.readString();
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          onPrivateDnsValidationEvent(paramInt1, str, paramParcel2, bool);
          return true;
        }
        paramParcel1.enforceInterface("android.net.INetdEventCallback");
        onDnsEvent(paramParcel1.readString(), paramParcel1.createStringArray(), paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.net.INetdEventCallback");
      return true;
    }
    
    private static class Proxy
      implements INetdEventCallback
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
        return "android.net.INetdEventCallback";
      }
      
      public void onConnectEvent(String paramString, int paramInt1, long paramLong, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.INetdEventCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt2);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDnsEvent(String paramString, String[] paramArrayOfString, int paramInt1, long paramLong, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.INetdEventCallback");
          localParcel.writeString(paramString);
          localParcel.writeStringArray(paramArrayOfString);
          localParcel.writeInt(paramInt1);
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt2);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPrivateDnsValidationEvent(int paramInt, String paramString1, String paramString2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.INetdEventCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeInt(paramBoolean);
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
