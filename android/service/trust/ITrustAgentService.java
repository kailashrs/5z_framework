package android.service.trust;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.UserHandle;
import java.util.List;

public abstract interface ITrustAgentService
  extends IInterface
{
  public abstract void onConfigure(List<PersistableBundle> paramList, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void onDeviceLocked()
    throws RemoteException;
  
  public abstract void onDeviceUnlocked()
    throws RemoteException;
  
  public abstract void onEscrowTokenAdded(byte[] paramArrayOfByte, long paramLong, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract void onEscrowTokenRemoved(long paramLong, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onTokenStateReceived(long paramLong, int paramInt)
    throws RemoteException;
  
  public abstract void onTrustTimeout()
    throws RemoteException;
  
  public abstract void onUnlockAttempt(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onUnlockLockout(int paramInt)
    throws RemoteException;
  
  public abstract void setCallback(ITrustAgentServiceCallback paramITrustAgentServiceCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITrustAgentService
  {
    private static final String DESCRIPTOR = "android.service.trust.ITrustAgentService";
    static final int TRANSACTION_onConfigure = 6;
    static final int TRANSACTION_onDeviceLocked = 4;
    static final int TRANSACTION_onDeviceUnlocked = 5;
    static final int TRANSACTION_onEscrowTokenAdded = 8;
    static final int TRANSACTION_onEscrowTokenRemoved = 10;
    static final int TRANSACTION_onTokenStateReceived = 9;
    static final int TRANSACTION_onTrustTimeout = 3;
    static final int TRANSACTION_onUnlockAttempt = 1;
    static final int TRANSACTION_onUnlockLockout = 2;
    static final int TRANSACTION_setCallback = 7;
    
    public Stub()
    {
      attachInterface(this, "android.service.trust.ITrustAgentService");
    }
    
    public static ITrustAgentService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.trust.ITrustAgentService");
      if ((localIInterface != null) && ((localIInterface instanceof ITrustAgentService))) {
        return (ITrustAgentService)localIInterface;
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
        long l;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 10: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentService");
          l = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onEscrowTokenRemoved(l, bool2);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentService");
          onTokenStateReceived(paramParcel1.readLong(), paramParcel1.readInt());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentService");
          paramParcel2 = paramParcel1.createByteArray();
          l = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          onEscrowTokenAdded(paramParcel2, l, paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentService");
          setCallback(ITrustAgentServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentService");
          onConfigure(paramParcel1.createTypedArrayList(PersistableBundle.CREATOR), paramParcel1.readStrongBinder());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentService");
          onDeviceUnlocked();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentService");
          onDeviceLocked();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentService");
          onTrustTimeout();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentService");
          onUnlockLockout(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.service.trust.ITrustAgentService");
        bool2 = bool1;
        if (paramParcel1.readInt() != 0) {
          bool2 = true;
        }
        onUnlockAttempt(bool2);
        return true;
      }
      paramParcel2.writeString("android.service.trust.ITrustAgentService");
      return true;
    }
    
    private static class Proxy
      implements ITrustAgentService
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
        return "android.service.trust.ITrustAgentService";
      }
      
      public void onConfigure(List<PersistableBundle> paramList, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentService");
          localParcel.writeTypedList(paramList);
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDeviceLocked()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentService");
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDeviceUnlocked()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentService");
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onEscrowTokenAdded(byte[] paramArrayOfByte, long paramLong, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentService");
          localParcel.writeByteArray(paramArrayOfByte);
          localParcel.writeLong(paramLong);
          if (paramUserHandle != null)
          {
            localParcel.writeInt(1);
            paramUserHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onEscrowTokenRemoved(long paramLong, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentService");
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTokenStateReceived(long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentService");
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTrustTimeout()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentService");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUnlockAttempt(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentService");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUnlockLockout(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentService");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setCallback(ITrustAgentServiceCallback paramITrustAgentServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentService");
          if (paramITrustAgentServiceCallback != null) {
            paramITrustAgentServiceCallback = paramITrustAgentServiceCallback.asBinder();
          } else {
            paramITrustAgentServiceCallback = null;
          }
          localParcel.writeStrongBinder(paramITrustAgentServiceCallback);
          mRemote.transact(7, localParcel, null, 1);
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
