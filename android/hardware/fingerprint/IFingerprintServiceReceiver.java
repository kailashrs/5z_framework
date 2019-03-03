package android.hardware.fingerprint;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IFingerprintServiceReceiver
  extends IInterface
{
  public abstract void onAcquired(long paramLong, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onAuthenticationFailed(long paramLong)
    throws RemoteException;
  
  public abstract void onAuthenticationSucceeded(long paramLong, Fingerprint paramFingerprint, int paramInt)
    throws RemoteException;
  
  public abstract void onEnrollResult(long paramLong, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onEnumerated(long paramLong, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onError(long paramLong, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onRemoved(long paramLong, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IFingerprintServiceReceiver
  {
    private static final String DESCRIPTOR = "android.hardware.fingerprint.IFingerprintServiceReceiver";
    static final int TRANSACTION_onAcquired = 2;
    static final int TRANSACTION_onAuthenticationFailed = 4;
    static final int TRANSACTION_onAuthenticationSucceeded = 3;
    static final int TRANSACTION_onEnrollResult = 1;
    static final int TRANSACTION_onEnumerated = 7;
    static final int TRANSACTION_onError = 5;
    static final int TRANSACTION_onRemoved = 6;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.fingerprint.IFingerprintServiceReceiver");
    }
    
    public static IFingerprintServiceReceiver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.fingerprint.IFingerprintServiceReceiver");
      if ((localIInterface != null) && ((localIInterface instanceof IFingerprintServiceReceiver))) {
        return (IFingerprintServiceReceiver)localIInterface;
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
        case 7: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintServiceReceiver");
          onEnumerated(paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintServiceReceiver");
          onRemoved(paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintServiceReceiver");
          onError(paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintServiceReceiver");
          onAuthenticationFailed(paramParcel1.readLong());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintServiceReceiver");
          long l = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Fingerprint)Fingerprint.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          onAuthenticationSucceeded(l, paramParcel2, paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintServiceReceiver");
          onAcquired(paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintServiceReceiver");
        onEnrollResult(paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.hardware.fingerprint.IFingerprintServiceReceiver");
      return true;
    }
    
    private static class Proxy
      implements IFingerprintServiceReceiver
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
        return "android.hardware.fingerprint.IFingerprintServiceReceiver";
      }
      
      public void onAcquired(long paramLong, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.fingerprint.IFingerprintServiceReceiver");
          localParcel.writeLong(paramLong);
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
      
      public void onAuthenticationFailed(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.fingerprint.IFingerprintServiceReceiver");
          localParcel.writeLong(paramLong);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onAuthenticationSucceeded(long paramLong, Fingerprint paramFingerprint, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.fingerprint.IFingerprintServiceReceiver");
          localParcel.writeLong(paramLong);
          if (paramFingerprint != null)
          {
            localParcel.writeInt(1);
            paramFingerprint.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onEnrollResult(long paramLong, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.fingerprint.IFingerprintServiceReceiver");
          localParcel.writeLong(paramLong);
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
      
      public void onEnumerated(long paramLong, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.fingerprint.IFingerprintServiceReceiver");
          localParcel.writeLong(paramLong);
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
      
      public void onError(long paramLong, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.fingerprint.IFingerprintServiceReceiver");
          localParcel.writeLong(paramLong);
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
      
      public void onRemoved(long paramLong, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.fingerprint.IFingerprintServiceReceiver");
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
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
