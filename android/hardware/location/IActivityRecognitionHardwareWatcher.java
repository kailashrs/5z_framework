package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IActivityRecognitionHardwareWatcher
  extends IInterface
{
  public abstract void onInstanceChanged(IActivityRecognitionHardware paramIActivityRecognitionHardware)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IActivityRecognitionHardwareWatcher
  {
    private static final String DESCRIPTOR = "android.hardware.location.IActivityRecognitionHardwareWatcher";
    static final int TRANSACTION_onInstanceChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.location.IActivityRecognitionHardwareWatcher");
    }
    
    public static IActivityRecognitionHardwareWatcher asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.location.IActivityRecognitionHardwareWatcher");
      if ((localIInterface != null) && ((localIInterface instanceof IActivityRecognitionHardwareWatcher))) {
        return (IActivityRecognitionHardwareWatcher)localIInterface;
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
        paramParcel2.writeString("android.hardware.location.IActivityRecognitionHardwareWatcher");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.location.IActivityRecognitionHardwareWatcher");
      onInstanceChanged(IActivityRecognitionHardware.Stub.asInterface(paramParcel1.readStrongBinder()));
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements IActivityRecognitionHardwareWatcher
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
        return "android.hardware.location.IActivityRecognitionHardwareWatcher";
      }
      
      public void onInstanceChanged(IActivityRecognitionHardware paramIActivityRecognitionHardware)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IActivityRecognitionHardwareWatcher");
          if (paramIActivityRecognitionHardware != null) {
            paramIActivityRecognitionHardware = paramIActivityRecognitionHardware.asBinder();
          } else {
            paramIActivityRecognitionHardware = null;
          }
          localParcel1.writeStrongBinder(paramIActivityRecognitionHardware);
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
    }
  }
}
