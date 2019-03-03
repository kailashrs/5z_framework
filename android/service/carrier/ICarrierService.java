package android.service.carrier;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;

public abstract interface ICarrierService
  extends IInterface
{
  public abstract void getCarrierConfig(CarrierIdentifier paramCarrierIdentifier, ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICarrierService
  {
    private static final String DESCRIPTOR = "android.service.carrier.ICarrierService";
    static final int TRANSACTION_getCarrierConfig = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.carrier.ICarrierService");
    }
    
    public static ICarrierService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.carrier.ICarrierService");
      if ((localIInterface != null) && ((localIInterface instanceof ICarrierService))) {
        return (ICarrierService)localIInterface;
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
        paramParcel2.writeString("android.service.carrier.ICarrierService");
        return true;
      }
      paramParcel1.enforceInterface("android.service.carrier.ICarrierService");
      paramInt1 = paramParcel1.readInt();
      Object localObject = null;
      if (paramInt1 != 0) {
        paramParcel2 = (CarrierIdentifier)CarrierIdentifier.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = localObject;
      }
      getCarrierConfig(paramParcel2, paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements ICarrierService
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
      
      public void getCarrierConfig(CarrierIdentifier paramCarrierIdentifier, ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.carrier.ICarrierService");
          if (paramCarrierIdentifier != null)
          {
            localParcel.writeInt(1);
            paramCarrierIdentifier.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramResultReceiver != null)
          {
            localParcel.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.service.carrier.ICarrierService";
      }
    }
  }
}
