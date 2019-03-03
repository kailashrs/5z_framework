package android.hardware.input;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IInputDevicesChangedListener
  extends IInterface
{
  public abstract void onInputDevicesChanged(int[] paramArrayOfInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputDevicesChangedListener
  {
    private static final String DESCRIPTOR = "android.hardware.input.IInputDevicesChangedListener";
    static final int TRANSACTION_onInputDevicesChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.input.IInputDevicesChangedListener");
    }
    
    public static IInputDevicesChangedListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.input.IInputDevicesChangedListener");
      if ((localIInterface != null) && ((localIInterface instanceof IInputDevicesChangedListener))) {
        return (IInputDevicesChangedListener)localIInterface;
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
        paramParcel2.writeString("android.hardware.input.IInputDevicesChangedListener");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.input.IInputDevicesChangedListener");
      onInputDevicesChanged(paramParcel1.createIntArray());
      return true;
    }
    
    private static class Proxy
      implements IInputDevicesChangedListener
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
        return "android.hardware.input.IInputDevicesChangedListener";
      }
      
      public void onInputDevicesChanged(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.input.IInputDevicesChangedListener");
          localParcel.writeIntArray(paramArrayOfInt);
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
