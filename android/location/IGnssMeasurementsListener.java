package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IGnssMeasurementsListener
  extends IInterface
{
  public abstract void onGnssMeasurementsReceived(GnssMeasurementsEvent paramGnssMeasurementsEvent)
    throws RemoteException;
  
  public abstract void onStatusChanged(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGnssMeasurementsListener
  {
    private static final String DESCRIPTOR = "android.location.IGnssMeasurementsListener";
    static final int TRANSACTION_onGnssMeasurementsReceived = 1;
    static final int TRANSACTION_onStatusChanged = 2;
    
    public Stub()
    {
      attachInterface(this, "android.location.IGnssMeasurementsListener");
    }
    
    public static IGnssMeasurementsListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.location.IGnssMeasurementsListener");
      if ((localIInterface != null) && ((localIInterface instanceof IGnssMeasurementsListener))) {
        return (IGnssMeasurementsListener)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.location.IGnssMeasurementsListener");
          onStatusChanged(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.location.IGnssMeasurementsListener");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (GnssMeasurementsEvent)GnssMeasurementsEvent.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onGnssMeasurementsReceived(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.location.IGnssMeasurementsListener");
      return true;
    }
    
    private static class Proxy
      implements IGnssMeasurementsListener
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
        return "android.location.IGnssMeasurementsListener";
      }
      
      public void onGnssMeasurementsReceived(GnssMeasurementsEvent paramGnssMeasurementsEvent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.IGnssMeasurementsListener");
          if (paramGnssMeasurementsEvent != null)
          {
            localParcel.writeInt(1);
            paramGnssMeasurementsEvent.writeToParcel(localParcel, 0);
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
      
      public void onStatusChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.IGnssMeasurementsListener");
          localParcel.writeInt(paramInt);
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
