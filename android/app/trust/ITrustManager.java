package android.app.trust;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ITrustManager
  extends IInterface
{
  public abstract void clearAllFingerprints()
    throws RemoteException;
  
  public abstract boolean isDeviceLocked(int paramInt)
    throws RemoteException;
  
  public abstract boolean isDeviceSecure(int paramInt)
    throws RemoteException;
  
  public abstract boolean isTrustUsuallyManaged(int paramInt)
    throws RemoteException;
  
  public abstract void registerTrustListener(ITrustListener paramITrustListener)
    throws RemoteException;
  
  public abstract void reportEnabledTrustAgentsChanged(int paramInt)
    throws RemoteException;
  
  public abstract void reportKeyguardShowingChanged()
    throws RemoteException;
  
  public abstract void reportUnlockAttempt(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void reportUnlockLockout(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setDeviceLockedForUser(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void unlockedByFingerprintForUser(int paramInt)
    throws RemoteException;
  
  public abstract void unregisterTrustListener(ITrustListener paramITrustListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITrustManager
  {
    private static final String DESCRIPTOR = "android.app.trust.ITrustManager";
    static final int TRANSACTION_clearAllFingerprints = 12;
    static final int TRANSACTION_isDeviceLocked = 8;
    static final int TRANSACTION_isDeviceSecure = 9;
    static final int TRANSACTION_isTrustUsuallyManaged = 10;
    static final int TRANSACTION_registerTrustListener = 4;
    static final int TRANSACTION_reportEnabledTrustAgentsChanged = 3;
    static final int TRANSACTION_reportKeyguardShowingChanged = 6;
    static final int TRANSACTION_reportUnlockAttempt = 1;
    static final int TRANSACTION_reportUnlockLockout = 2;
    static final int TRANSACTION_setDeviceLockedForUser = 7;
    static final int TRANSACTION_unlockedByFingerprintForUser = 11;
    static final int TRANSACTION_unregisterTrustListener = 5;
    
    public Stub()
    {
      attachInterface(this, "android.app.trust.ITrustManager");
    }
    
    public static ITrustManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.trust.ITrustManager");
      if ((localIInterface != null) && ((localIInterface instanceof ITrustManager))) {
        return (ITrustManager)localIInterface;
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
        case 12: 
          paramParcel1.enforceInterface("android.app.trust.ITrustManager");
          clearAllFingerprints();
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.app.trust.ITrustManager");
          unlockedByFingerprintForUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.app.trust.ITrustManager");
          paramInt1 = isTrustUsuallyManaged(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.app.trust.ITrustManager");
          paramInt1 = isDeviceSecure(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.app.trust.ITrustManager");
          paramInt1 = isDeviceLocked(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.app.trust.ITrustManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setDeviceLockedForUser(paramInt1, bool2);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.app.trust.ITrustManager");
          reportKeyguardShowingChanged();
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.trust.ITrustManager");
          unregisterTrustListener(ITrustListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.trust.ITrustManager");
          registerTrustListener(ITrustListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.trust.ITrustManager");
          reportEnabledTrustAgentsChanged(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.trust.ITrustManager");
          reportUnlockLockout(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.app.trust.ITrustManager");
        bool2 = bool1;
        if (paramParcel1.readInt() != 0) {
          bool2 = true;
        }
        reportUnlockAttempt(bool2, paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.app.trust.ITrustManager");
      return true;
    }
    
    private static class Proxy
      implements ITrustManager
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
      
      public void clearAllFingerprints()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.trust.ITrustManager");
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.trust.ITrustManager";
      }
      
      public boolean isDeviceLocked(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.trust.ITrustManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public boolean isDeviceSecure(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.trust.ITrustManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public boolean isTrustUsuallyManaged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.trust.ITrustManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public void registerTrustListener(ITrustListener paramITrustListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.trust.ITrustManager");
          if (paramITrustListener != null) {
            paramITrustListener = paramITrustListener.asBinder();
          } else {
            paramITrustListener = null;
          }
          localParcel1.writeStrongBinder(paramITrustListener);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportEnabledTrustAgentsChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.trust.ITrustManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportKeyguardShowingChanged()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.trust.ITrustManager");
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
      
      public void reportUnlockAttempt(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.trust.ITrustManager");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
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
      
      public void reportUnlockLockout(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.trust.ITrustManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void setDeviceLockedForUser(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.trust.ITrustManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unlockedByFingerprintForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.trust.ITrustManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterTrustListener(ITrustListener paramITrustListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.trust.ITrustManager");
          if (paramITrustListener != null) {
            paramITrustListener = paramITrustListener.asBinder();
          } else {
            paramITrustListener = null;
          }
          localParcel1.writeStrongBinder(paramITrustListener);
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
    }
  }
}
