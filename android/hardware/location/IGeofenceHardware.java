package android.hardware.location;

import android.location.IFusedGeofenceHardware;
import android.location.IFusedGeofenceHardware.Stub;
import android.location.IGpsGeofenceHardware;
import android.location.IGpsGeofenceHardware.Stub;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IGeofenceHardware
  extends IInterface
{
  public abstract boolean addCircularFence(int paramInt, GeofenceHardwareRequestParcelable paramGeofenceHardwareRequestParcelable, IGeofenceHardwareCallback paramIGeofenceHardwareCallback)
    throws RemoteException;
  
  public abstract int[] getMonitoringTypes()
    throws RemoteException;
  
  public abstract int getStatusOfMonitoringType(int paramInt)
    throws RemoteException;
  
  public abstract boolean pauseGeofence(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean registerForMonitorStateChangeCallback(int paramInt, IGeofenceHardwareMonitorCallback paramIGeofenceHardwareMonitorCallback)
    throws RemoteException;
  
  public abstract boolean removeGeofence(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean resumeGeofence(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void setFusedGeofenceHardware(IFusedGeofenceHardware paramIFusedGeofenceHardware)
    throws RemoteException;
  
  public abstract void setGpsGeofenceHardware(IGpsGeofenceHardware paramIGpsGeofenceHardware)
    throws RemoteException;
  
  public abstract boolean unregisterForMonitorStateChangeCallback(int paramInt, IGeofenceHardwareMonitorCallback paramIGeofenceHardwareMonitorCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGeofenceHardware
  {
    private static final String DESCRIPTOR = "android.hardware.location.IGeofenceHardware";
    static final int TRANSACTION_addCircularFence = 5;
    static final int TRANSACTION_getMonitoringTypes = 3;
    static final int TRANSACTION_getStatusOfMonitoringType = 4;
    static final int TRANSACTION_pauseGeofence = 7;
    static final int TRANSACTION_registerForMonitorStateChangeCallback = 9;
    static final int TRANSACTION_removeGeofence = 6;
    static final int TRANSACTION_resumeGeofence = 8;
    static final int TRANSACTION_setFusedGeofenceHardware = 2;
    static final int TRANSACTION_setGpsGeofenceHardware = 1;
    static final int TRANSACTION_unregisterForMonitorStateChangeCallback = 10;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.location.IGeofenceHardware");
    }
    
    public static IGeofenceHardware asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.location.IGeofenceHardware");
      if ((localIInterface != null) && ((localIInterface instanceof IGeofenceHardware))) {
        return (IGeofenceHardware)localIInterface;
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
        case 10: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardware");
          paramInt1 = unregisterForMonitorStateChangeCallback(paramParcel1.readInt(), IGeofenceHardwareMonitorCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardware");
          paramInt1 = registerForMonitorStateChangeCallback(paramParcel1.readInt(), IGeofenceHardwareMonitorCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardware");
          paramInt1 = resumeGeofence(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardware");
          paramInt1 = pauseGeofence(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardware");
          paramInt1 = removeGeofence(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardware");
          paramInt1 = paramParcel1.readInt();
          GeofenceHardwareRequestParcelable localGeofenceHardwareRequestParcelable;
          if (paramParcel1.readInt() != 0) {
            localGeofenceHardwareRequestParcelable = (GeofenceHardwareRequestParcelable)GeofenceHardwareRequestParcelable.CREATOR.createFromParcel(paramParcel1);
          } else {
            localGeofenceHardwareRequestParcelable = null;
          }
          paramInt1 = addCircularFence(paramInt1, localGeofenceHardwareRequestParcelable, IGeofenceHardwareCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardware");
          paramInt1 = getStatusOfMonitoringType(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardware");
          paramParcel1 = getMonitoringTypes();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardware");
          setFusedGeofenceHardware(IFusedGeofenceHardware.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardware");
        setGpsGeofenceHardware(IGpsGeofenceHardware.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.hardware.location.IGeofenceHardware");
      return true;
    }
    
    private static class Proxy
      implements IGeofenceHardware
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public boolean addCircularFence(int paramInt, GeofenceHardwareRequestParcelable paramGeofenceHardwareRequestParcelable, IGeofenceHardwareCallback paramIGeofenceHardwareCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IGeofenceHardware");
          localParcel1.writeInt(paramInt);
          boolean bool = true;
          if (paramGeofenceHardwareRequestParcelable != null)
          {
            localParcel1.writeInt(1);
            paramGeofenceHardwareRequestParcelable.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIGeofenceHardwareCallback != null) {
            paramGeofenceHardwareRequestParcelable = paramIGeofenceHardwareCallback.asBinder();
          } else {
            paramGeofenceHardwareRequestParcelable = null;
          }
          localParcel1.writeStrongBinder(paramGeofenceHardwareRequestParcelable);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
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
        return "android.hardware.location.IGeofenceHardware";
      }
      
      public int[] getMonitoringTypes()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IGeofenceHardware");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getStatusOfMonitoringType(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IGeofenceHardware");
          localParcel1.writeInt(paramInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean pauseGeofence(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IGeofenceHardware");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
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
      
      public boolean registerForMonitorStateChangeCallback(int paramInt, IGeofenceHardwareMonitorCallback paramIGeofenceHardwareMonitorCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IGeofenceHardware");
          localParcel1.writeInt(paramInt);
          if (paramIGeofenceHardwareMonitorCallback != null) {
            paramIGeofenceHardwareMonitorCallback = paramIGeofenceHardwareMonitorCallback.asBinder();
          } else {
            paramIGeofenceHardwareMonitorCallback = null;
          }
          localParcel1.writeStrongBinder(paramIGeofenceHardwareMonitorCallback);
          paramIGeofenceHardwareMonitorCallback = mRemote;
          boolean bool = false;
          paramIGeofenceHardwareMonitorCallback.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public boolean removeGeofence(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IGeofenceHardware");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
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
      
      public boolean resumeGeofence(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IGeofenceHardware");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
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
      
      public void setFusedGeofenceHardware(IFusedGeofenceHardware paramIFusedGeofenceHardware)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IGeofenceHardware");
          if (paramIFusedGeofenceHardware != null) {
            paramIFusedGeofenceHardware = paramIFusedGeofenceHardware.asBinder();
          } else {
            paramIFusedGeofenceHardware = null;
          }
          localParcel1.writeStrongBinder(paramIFusedGeofenceHardware);
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
      
      public void setGpsGeofenceHardware(IGpsGeofenceHardware paramIGpsGeofenceHardware)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IGeofenceHardware");
          if (paramIGpsGeofenceHardware != null) {
            paramIGpsGeofenceHardware = paramIGpsGeofenceHardware.asBinder();
          } else {
            paramIGpsGeofenceHardware = null;
          }
          localParcel1.writeStrongBinder(paramIGpsGeofenceHardware);
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
      
      public boolean unregisterForMonitorStateChangeCallback(int paramInt, IGeofenceHardwareMonitorCallback paramIGeofenceHardwareMonitorCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IGeofenceHardware");
          localParcel1.writeInt(paramInt);
          if (paramIGeofenceHardwareMonitorCallback != null) {
            paramIGeofenceHardwareMonitorCallback = paramIGeofenceHardwareMonitorCallback.asBinder();
          } else {
            paramIGeofenceHardwareMonitorCallback = null;
          }
          localParcel1.writeStrongBinder(paramIGeofenceHardwareMonitorCallback);
          paramIGeofenceHardwareMonitorCallback = mRemote;
          boolean bool = false;
          paramIGeofenceHardwareMonitorCallback.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
    }
  }
}
