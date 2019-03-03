package android.hardware.radio;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ICloseHandle
  extends IInterface
{
  public abstract void close()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICloseHandle
  {
    private static final String DESCRIPTOR = "android.hardware.radio.ICloseHandle";
    static final int TRANSACTION_close = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.radio.ICloseHandle");
    }
    
    public static ICloseHandle asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.radio.ICloseHandle");
      if ((localIInterface != null) && ((localIInterface instanceof ICloseHandle))) {
        return (ICloseHandle)localIInterface;
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
        paramParcel2.writeString("android.hardware.radio.ICloseHandle");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.radio.ICloseHandle");
      close();
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements ICloseHandle
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
      
      public void close()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ICloseHandle");
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
      
      public String getInterfaceDescriptor()
      {
        return "android.hardware.radio.ICloseHandle";
      }
    }
  }
}
