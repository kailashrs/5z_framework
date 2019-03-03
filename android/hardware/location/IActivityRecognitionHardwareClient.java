package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IActivityRecognitionHardwareClient
  extends IInterface
{
  public abstract void onAvailabilityChanged(boolean paramBoolean, IActivityRecognitionHardware paramIActivityRecognitionHardware)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IActivityRecognitionHardwareClient
  {
    private static final String DESCRIPTOR = "android.hardware.location.IActivityRecognitionHardwareClient";
    static final int TRANSACTION_onAvailabilityChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.location.IActivityRecognitionHardwareClient");
    }
    
    public static IActivityRecognitionHardwareClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.location.IActivityRecognitionHardwareClient");
      if ((localIInterface != null) && ((localIInterface instanceof IActivityRecognitionHardwareClient))) {
        return (IActivityRecognitionHardwareClient)localIInterface;
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
        paramParcel2.writeString("android.hardware.location.IActivityRecognitionHardwareClient");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.location.IActivityRecognitionHardwareClient");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onAvailabilityChanged(bool, IActivityRecognitionHardware.Stub.asInterface(paramParcel1.readStrongBinder()));
      return true;
    }
    
    private static class Proxy
      implements IActivityRecognitionHardwareClient
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
        return "android.hardware.location.IActivityRecognitionHardwareClient";
      }
      
      public void onAvailabilityChanged(boolean paramBoolean, IActivityRecognitionHardware paramIActivityRecognitionHardware)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IActivityRecognitionHardwareClient");
          localParcel.writeInt(paramBoolean);
          if (paramIActivityRecognitionHardware != null) {
            paramIActivityRecognitionHardware = paramIActivityRecognitionHardware.asBinder();
          } else {
            paramIActivityRecognitionHardware = null;
          }
          localParcel.writeStrongBinder(paramIActivityRecognitionHardware);
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
