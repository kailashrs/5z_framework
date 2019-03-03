package android.location;

import android.hardware.location.GeofenceHardwareRequestParcelable;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IFusedGeofenceHardware
  extends IInterface
{
  public abstract void addGeofences(GeofenceHardwareRequestParcelable[] paramArrayOfGeofenceHardwareRequestParcelable)
    throws RemoteException;
  
  public abstract boolean isSupported()
    throws RemoteException;
  
  public abstract void modifyGeofenceOptions(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
    throws RemoteException;
  
  public abstract void pauseMonitoringGeofence(int paramInt)
    throws RemoteException;
  
  public abstract void removeGeofences(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract void resumeMonitoringGeofence(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IFusedGeofenceHardware
  {
    private static final String DESCRIPTOR = "android.location.IFusedGeofenceHardware";
    static final int TRANSACTION_addGeofences = 2;
    static final int TRANSACTION_isSupported = 1;
    static final int TRANSACTION_modifyGeofenceOptions = 6;
    static final int TRANSACTION_pauseMonitoringGeofence = 4;
    static final int TRANSACTION_removeGeofences = 3;
    static final int TRANSACTION_resumeMonitoringGeofence = 5;
    
    public Stub()
    {
      attachInterface(this, "android.location.IFusedGeofenceHardware");
    }
    
    public static IFusedGeofenceHardware asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.location.IFusedGeofenceHardware");
      if ((localIInterface != null) && ((localIInterface instanceof IFusedGeofenceHardware))) {
        return (IFusedGeofenceHardware)localIInterface;
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
        case 6: 
          paramParcel1.enforceInterface("android.location.IFusedGeofenceHardware");
          modifyGeofenceOptions(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.location.IFusedGeofenceHardware");
          resumeMonitoringGeofence(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.location.IFusedGeofenceHardware");
          pauseMonitoringGeofence(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.location.IFusedGeofenceHardware");
          removeGeofences(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.location.IFusedGeofenceHardware");
          addGeofences((GeofenceHardwareRequestParcelable[])paramParcel1.createTypedArray(GeofenceHardwareRequestParcelable.CREATOR));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.location.IFusedGeofenceHardware");
        paramInt1 = isSupported();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.location.IFusedGeofenceHardware");
      return true;
    }
    
    private static class Proxy
      implements IFusedGeofenceHardware
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addGeofences(GeofenceHardwareRequestParcelable[] paramArrayOfGeofenceHardwareRequestParcelable)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.IFusedGeofenceHardware");
          localParcel1.writeTypedArray(paramArrayOfGeofenceHardwareRequestParcelable, 0);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.location.IFusedGeofenceHardware";
      }
      
      public boolean isSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.IFusedGeofenceHardware");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
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
      
      public void modifyGeofenceOptions(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.IFusedGeofenceHardware");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeInt(paramInt5);
          localParcel1.writeInt(paramInt6);
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
      
      public void pauseMonitoringGeofence(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.IFusedGeofenceHardware");
          localParcel1.writeInt(paramInt);
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
      
      public void removeGeofences(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.IFusedGeofenceHardware");
          localParcel1.writeIntArray(paramArrayOfInt);
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
      
      public void resumeMonitoringGeofence(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.IFusedGeofenceHardware");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
