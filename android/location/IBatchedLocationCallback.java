package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IBatchedLocationCallback
  extends IInterface
{
  public abstract void onLocationBatch(List<Location> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBatchedLocationCallback
  {
    private static final String DESCRIPTOR = "android.location.IBatchedLocationCallback";
    static final int TRANSACTION_onLocationBatch = 1;
    
    public Stub()
    {
      attachInterface(this, "android.location.IBatchedLocationCallback");
    }
    
    public static IBatchedLocationCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.location.IBatchedLocationCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IBatchedLocationCallback))) {
        return (IBatchedLocationCallback)localIInterface;
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
        paramParcel2.writeString("android.location.IBatchedLocationCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.location.IBatchedLocationCallback");
      onLocationBatch(paramParcel1.createTypedArrayList(Location.CREATOR));
      return true;
    }
    
    private static class Proxy
      implements IBatchedLocationCallback
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
        return "android.location.IBatchedLocationCallback";
      }
      
      public void onLocationBatch(List<Location> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.IBatchedLocationCallback");
          localParcel.writeTypedList(paramList);
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
