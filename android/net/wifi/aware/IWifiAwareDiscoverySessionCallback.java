package android.net.wifi.aware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IWifiAwareDiscoverySessionCallback
  extends IInterface
{
  public abstract void onMatch(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public abstract void onMatchWithDistance(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2)
    throws RemoteException;
  
  public abstract void onMessageReceived(int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onMessageSendFail(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onMessageSendSuccess(int paramInt)
    throws RemoteException;
  
  public abstract void onSessionConfigFail(int paramInt)
    throws RemoteException;
  
  public abstract void onSessionConfigSuccess()
    throws RemoteException;
  
  public abstract void onSessionStarted(int paramInt)
    throws RemoteException;
  
  public abstract void onSessionTerminated(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWifiAwareDiscoverySessionCallback
  {
    private static final String DESCRIPTOR = "android.net.wifi.aware.IWifiAwareDiscoverySessionCallback";
    static final int TRANSACTION_onMatch = 5;
    static final int TRANSACTION_onMatchWithDistance = 6;
    static final int TRANSACTION_onMessageReceived = 9;
    static final int TRANSACTION_onMessageSendFail = 8;
    static final int TRANSACTION_onMessageSendSuccess = 7;
    static final int TRANSACTION_onSessionConfigFail = 3;
    static final int TRANSACTION_onSessionConfigSuccess = 2;
    static final int TRANSACTION_onSessionStarted = 1;
    static final int TRANSACTION_onSessionTerminated = 4;
    
    public Stub()
    {
      attachInterface(this, "android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
    }
    
    public static IWifiAwareDiscoverySessionCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IWifiAwareDiscoverySessionCallback))) {
        return (IWifiAwareDiscoverySessionCallback)localIInterface;
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
        case 9: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          onMessageReceived(paramParcel1.readInt(), paramParcel1.createByteArray());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          onMessageSendFail(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          onMessageSendSuccess(paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          onMatchWithDistance(paramParcel1.readInt(), paramParcel1.createByteArray(), paramParcel1.createByteArray(), paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          onMatch(paramParcel1.readInt(), paramParcel1.createByteArray(), paramParcel1.createByteArray());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          onSessionTerminated(paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          onSessionConfigFail(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          onSessionConfigSuccess();
          return true;
        }
        paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
        onSessionStarted(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
      return true;
    }
    
    private static class Proxy
      implements IWifiAwareDiscoverySessionCallback
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
        return "android.net.wifi.aware.IWifiAwareDiscoverySessionCallback";
      }
      
      public void onMatch(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeByteArray(paramArrayOfByte1);
          localParcel.writeByteArray(paramArrayOfByte2);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onMatchWithDistance(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeByteArray(paramArrayOfByte1);
          localParcel.writeByteArray(paramArrayOfByte2);
          localParcel.writeInt(paramInt2);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onMessageReceived(int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onMessageSendFail(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
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
      
      public void onMessageSendSuccess(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionConfigFail(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionConfigSuccess()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionStarted(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionTerminated(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.aware.IWifiAwareDiscoverySessionCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
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
