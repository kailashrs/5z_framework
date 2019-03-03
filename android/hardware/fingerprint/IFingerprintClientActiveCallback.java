package android.hardware.fingerprint;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IFingerprintClientActiveCallback
  extends IInterface
{
  public abstract void onClientActiveChanged(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IFingerprintClientActiveCallback
  {
    private static final String DESCRIPTOR = "android.hardware.fingerprint.IFingerprintClientActiveCallback";
    static final int TRANSACTION_onClientActiveChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.fingerprint.IFingerprintClientActiveCallback");
    }
    
    public static IFingerprintClientActiveCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.fingerprint.IFingerprintClientActiveCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IFingerprintClientActiveCallback))) {
        return (IFingerprintClientActiveCallback)localIInterface;
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
        paramParcel2.writeString("android.hardware.fingerprint.IFingerprintClientActiveCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintClientActiveCallback");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onClientActiveChanged(bool);
      return true;
    }
    
    private static class Proxy
      implements IFingerprintClientActiveCallback
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
        return "android.hardware.fingerprint.IFingerprintClientActiveCallback";
      }
      
      public void onClientActiveChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.fingerprint.IFingerprintClientActiveCallback");
          localParcel.writeInt(paramBoolean);
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
