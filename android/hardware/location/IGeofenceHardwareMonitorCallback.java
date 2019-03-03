package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IGeofenceHardwareMonitorCallback
  extends IInterface
{
  public abstract void onMonitoringSystemChange(GeofenceHardwareMonitorEvent paramGeofenceHardwareMonitorEvent)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGeofenceHardwareMonitorCallback
  {
    private static final String DESCRIPTOR = "android.hardware.location.IGeofenceHardwareMonitorCallback";
    static final int TRANSACTION_onMonitoringSystemChange = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.location.IGeofenceHardwareMonitorCallback");
    }
    
    public static IGeofenceHardwareMonitorCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.location.IGeofenceHardwareMonitorCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IGeofenceHardwareMonitorCallback))) {
        return (IGeofenceHardwareMonitorCallback)localIInterface;
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
        paramParcel2.writeString("android.hardware.location.IGeofenceHardwareMonitorCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardwareMonitorCallback");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (GeofenceHardwareMonitorEvent)GeofenceHardwareMonitorEvent.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onMonitoringSystemChange(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IGeofenceHardwareMonitorCallback
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
        return "android.hardware.location.IGeofenceHardwareMonitorCallback";
      }
      
      public void onMonitoringSystemChange(GeofenceHardwareMonitorEvent paramGeofenceHardwareMonitorEvent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IGeofenceHardwareMonitorCallback");
          if (paramGeofenceHardwareMonitorEvent != null)
          {
            localParcel.writeInt(1);
            paramGeofenceHardwareMonitorEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
