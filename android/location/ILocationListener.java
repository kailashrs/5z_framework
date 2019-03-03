package android.location;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ILocationListener
  extends IInterface
{
  public abstract void onLocationChanged(Location paramLocation)
    throws RemoteException;
  
  public abstract void onProviderDisabled(String paramString)
    throws RemoteException;
  
  public abstract void onProviderEnabled(String paramString)
    throws RemoteException;
  
  public abstract void onStatusChanged(String paramString, int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ILocationListener
  {
    private static final String DESCRIPTOR = "android.location.ILocationListener";
    static final int TRANSACTION_onLocationChanged = 1;
    static final int TRANSACTION_onProviderDisabled = 4;
    static final int TRANSACTION_onProviderEnabled = 3;
    static final int TRANSACTION_onStatusChanged = 2;
    
    public Stub()
    {
      attachInterface(this, "android.location.ILocationListener");
    }
    
    public static ILocationListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.location.ILocationListener");
      if ((localIInterface != null) && ((localIInterface instanceof ILocationListener))) {
        return (ILocationListener)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("android.location.ILocationListener");
          onProviderDisabled(paramParcel1.readString());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.location.ILocationListener");
          onProviderEnabled(paramParcel1.readString());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.location.ILocationListener");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onStatusChanged(paramParcel2, paramInt1, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.location.ILocationListener");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Location)Location.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        onLocationChanged(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.location.ILocationListener");
      return true;
    }
    
    private static class Proxy
      implements ILocationListener
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
        return "android.location.ILocationListener";
      }
      
      public void onLocationChanged(Location paramLocation)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.ILocationListener");
          if (paramLocation != null)
          {
            localParcel.writeInt(1);
            paramLocation.writeToParcel(localParcel, 0);
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
      
      public void onProviderDisabled(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.ILocationListener");
          localParcel.writeString(paramString);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onProviderEnabled(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.ILocationListener");
          localParcel.writeString(paramString);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.ILocationListener");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
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
