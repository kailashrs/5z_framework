package android.hardware.location;

import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IGeofenceHardwareCallback
  extends IInterface
{
  public abstract void onGeofenceAdd(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onGeofencePause(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onGeofenceRemove(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onGeofenceResume(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onGeofenceTransition(int paramInt1, int paramInt2, Location paramLocation, long paramLong, int paramInt3)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGeofenceHardwareCallback
  {
    private static final String DESCRIPTOR = "android.hardware.location.IGeofenceHardwareCallback";
    static final int TRANSACTION_onGeofenceAdd = 2;
    static final int TRANSACTION_onGeofencePause = 4;
    static final int TRANSACTION_onGeofenceRemove = 3;
    static final int TRANSACTION_onGeofenceResume = 5;
    static final int TRANSACTION_onGeofenceTransition = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.location.IGeofenceHardwareCallback");
    }
    
    public static IGeofenceHardwareCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.location.IGeofenceHardwareCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IGeofenceHardwareCallback))) {
        return (IGeofenceHardwareCallback)localIInterface;
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
        case 5: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardwareCallback");
          onGeofenceResume(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardwareCallback");
          onGeofencePause(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardwareCallback");
          onGeofenceRemove(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardwareCallback");
          onGeofenceAdd(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.location.IGeofenceHardwareCallback");
        paramInt1 = paramParcel1.readInt();
        paramInt2 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {}
        for (paramParcel2 = (Location)Location.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = null) {
          break;
        }
        onGeofenceTransition(paramInt1, paramInt2, paramParcel2, paramParcel1.readLong(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.hardware.location.IGeofenceHardwareCallback");
      return true;
    }
    
    private static class Proxy
      implements IGeofenceHardwareCallback
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
        return "android.hardware.location.IGeofenceHardwareCallback";
      }
      
      public void onGeofenceAdd(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IGeofenceHardwareCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGeofencePause(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IGeofenceHardwareCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGeofenceRemove(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IGeofenceHardwareCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGeofenceResume(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IGeofenceHardwareCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGeofenceTransition(int paramInt1, int paramInt2, Location paramLocation, long paramLong, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IGeofenceHardwareCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramLocation != null)
          {
            localParcel.writeInt(1);
            paramLocation.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt3);
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
