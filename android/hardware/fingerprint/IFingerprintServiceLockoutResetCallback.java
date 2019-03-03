package android.hardware.fingerprint;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.IRemoteCallback.Stub;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IFingerprintServiceLockoutResetCallback
  extends IInterface
{
  public abstract void onLockoutReset(long paramLong, IRemoteCallback paramIRemoteCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IFingerprintServiceLockoutResetCallback
  {
    private static final String DESCRIPTOR = "android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback";
    static final int TRANSACTION_onLockoutReset = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback");
    }
    
    public static IFingerprintServiceLockoutResetCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IFingerprintServiceLockoutResetCallback))) {
        return (IFingerprintServiceLockoutResetCallback)localIInterface;
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
      if (paramInt1 != 1)
      {
        if (paramInt1 != 1598968902) {
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        }
        paramParcel2.writeString("android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback");
      onLockoutReset(paramParcel1.readLong(), IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
      return true;
    }
    
    private static class Proxy
      implements IFingerprintServiceLockoutResetCallback
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
        return "android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback";
      }
      
      public void onLockoutReset(long paramLong, IRemoteCallback paramIRemoteCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback");
          localParcel.writeLong(paramLong);
          if (paramIRemoteCallback != null) {
            paramIRemoteCallback = paramIRemoteCallback.asBinder();
          } else {
            paramIRemoteCallback = null;
          }
          localParcel.writeStrongBinder(paramIRemoteCallback);
          mRemote.transact(1, localParcel, null, 1);
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
