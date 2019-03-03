package android.net.lowpan;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ILowpanNetScanCallback
  extends IInterface
{
  public abstract void onNetScanBeacon(LowpanBeaconInfo paramLowpanBeaconInfo)
    throws RemoteException;
  
  public abstract void onNetScanFinished()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ILowpanNetScanCallback
  {
    private static final String DESCRIPTOR = "android.net.lowpan.ILowpanNetScanCallback";
    static final int TRANSACTION_onNetScanBeacon = 1;
    static final int TRANSACTION_onNetScanFinished = 2;
    
    public Stub()
    {
      attachInterface(this, "android.net.lowpan.ILowpanNetScanCallback");
    }
    
    public static ILowpanNetScanCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.lowpan.ILowpanNetScanCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ILowpanNetScanCallback))) {
        return (ILowpanNetScanCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanNetScanCallback");
          onNetScanFinished();
          return true;
        }
        paramParcel1.enforceInterface("android.net.lowpan.ILowpanNetScanCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (LowpanBeaconInfo)LowpanBeaconInfo.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onNetScanBeacon(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.net.lowpan.ILowpanNetScanCallback");
      return true;
    }
    
    private static class Proxy
      implements ILowpanNetScanCallback
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
        return "android.net.lowpan.ILowpanNetScanCallback";
      }
      
      public void onNetScanBeacon(LowpanBeaconInfo paramLowpanBeaconInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanNetScanCallback");
          if (paramLowpanBeaconInfo != null)
          {
            localParcel.writeInt(1);
            paramLowpanBeaconInfo.writeToParcel(localParcel, 0);
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
      
      public void onNetScanFinished()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanNetScanCallback");
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
