package android.service.gatekeeper;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IGateKeeperService
  extends IInterface
{
  public abstract void clearSecureUserId(int paramInt)
    throws RemoteException;
  
  public abstract GateKeeperResponse enroll(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
    throws RemoteException;
  
  public abstract long getSecureUserId(int paramInt)
    throws RemoteException;
  
  public abstract void reportDeviceSetupComplete()
    throws RemoteException;
  
  public abstract GateKeeperResponse verify(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public abstract GateKeeperResponse verifyChallenge(int paramInt, long paramLong, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGateKeeperService
  {
    private static final String DESCRIPTOR = "android.service.gatekeeper.IGateKeeperService";
    static final int TRANSACTION_clearSecureUserId = 5;
    static final int TRANSACTION_enroll = 1;
    static final int TRANSACTION_getSecureUserId = 4;
    static final int TRANSACTION_reportDeviceSetupComplete = 6;
    static final int TRANSACTION_verify = 2;
    static final int TRANSACTION_verifyChallenge = 3;
    
    public Stub()
    {
      attachInterface(this, "android.service.gatekeeper.IGateKeeperService");
    }
    
    public static IGateKeeperService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.gatekeeper.IGateKeeperService");
      if ((localIInterface != null) && ((localIInterface instanceof IGateKeeperService))) {
        return (IGateKeeperService)localIInterface;
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
          paramParcel1.enforceInterface("android.service.gatekeeper.IGateKeeperService");
          reportDeviceSetupComplete();
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.gatekeeper.IGateKeeperService");
          clearSecureUserId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.gatekeeper.IGateKeeperService");
          long l = getSecureUserId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.gatekeeper.IGateKeeperService");
          paramParcel1 = verifyChallenge(paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.createByteArray(), paramParcel1.createByteArray());
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
          paramParcel1.enforceInterface("android.service.gatekeeper.IGateKeeperService");
          paramParcel1 = verify(paramParcel1.readInt(), paramParcel1.createByteArray(), paramParcel1.createByteArray());
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
        paramParcel1.enforceInterface("android.service.gatekeeper.IGateKeeperService");
        paramParcel1 = enroll(paramParcel1.readInt(), paramParcel1.createByteArray(), paramParcel1.createByteArray(), paramParcel1.createByteArray());
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
      paramParcel2.writeString("android.service.gatekeeper.IGateKeeperService");
      return true;
    }
    
    private static class Proxy
      implements IGateKeeperService
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
      
      public void clearSecureUserId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
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
      
      public GateKeeperResponse enroll(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          localParcel1.writeByteArray(paramArrayOfByte3);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramArrayOfByte1 = (GateKeeperResponse)GateKeeperResponse.CREATOR.createFromParcel(localParcel2);
          } else {
            paramArrayOfByte1 = null;
          }
          return paramArrayOfByte1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.service.gatekeeper.IGateKeeperService";
      }
      
      public long getSecureUserId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportDeviceSetupComplete()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
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
      
      public GateKeeperResponse verify(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramArrayOfByte1 = (GateKeeperResponse)GateKeeperResponse.CREATOR.createFromParcel(localParcel2);
          } else {
            paramArrayOfByte1 = null;
          }
          return paramArrayOfByte1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public GateKeeperResponse verifyChallenge(int paramInt, long paramLong, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeLong(paramLong);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramArrayOfByte1 = (GateKeeperResponse)GateKeeperResponse.CREATOR.createFromParcel(localParcel2);
          } else {
            paramArrayOfByte1 = null;
          }
          return paramArrayOfByte1;
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
