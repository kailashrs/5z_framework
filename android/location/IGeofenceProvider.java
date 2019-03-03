package android.location;

import android.hardware.location.IGeofenceHardware;
import android.hardware.location.IGeofenceHardware.Stub;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IGeofenceProvider
  extends IInterface
{
  public abstract void setGeofenceHardware(IGeofenceHardware paramIGeofenceHardware)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGeofenceProvider
  {
    private static final String DESCRIPTOR = "android.location.IGeofenceProvider";
    static final int TRANSACTION_setGeofenceHardware = 1;
    
    public Stub()
    {
      attachInterface(this, "android.location.IGeofenceProvider");
    }
    
    public static IGeofenceProvider asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.location.IGeofenceProvider");
      if ((localIInterface != null) && ((localIInterface instanceof IGeofenceProvider))) {
        return (IGeofenceProvider)localIInterface;
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
        paramParcel2.writeString("android.location.IGeofenceProvider");
        return true;
      }
      paramParcel1.enforceInterface("android.location.IGeofenceProvider");
      setGeofenceHardware(IGeofenceHardware.Stub.asInterface(paramParcel1.readStrongBinder()));
      return true;
    }
    
    private static class Proxy
      implements IGeofenceProvider
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
        return "android.location.IGeofenceProvider";
      }
      
      public void setGeofenceHardware(IGeofenceHardware paramIGeofenceHardware)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.IGeofenceProvider");
          if (paramIGeofenceHardware != null) {
            paramIGeofenceHardware = paramIGeofenceHardware.asBinder();
          } else {
            paramIGeofenceHardware = null;
          }
          localParcel.writeStrongBinder(paramIGeofenceHardware);
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
