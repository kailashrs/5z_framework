package android.net.lowpan;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ILowpanEnergyScanCallback
  extends IInterface
{
  public abstract void onEnergyScanFinished()
    throws RemoteException;
  
  public abstract void onEnergyScanResult(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ILowpanEnergyScanCallback
  {
    private static final String DESCRIPTOR = "android.net.lowpan.ILowpanEnergyScanCallback";
    static final int TRANSACTION_onEnergyScanFinished = 2;
    static final int TRANSACTION_onEnergyScanResult = 1;
    
    public Stub()
    {
      attachInterface(this, "android.net.lowpan.ILowpanEnergyScanCallback");
    }
    
    public static ILowpanEnergyScanCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.lowpan.ILowpanEnergyScanCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ILowpanEnergyScanCallback))) {
        return (ILowpanEnergyScanCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanEnergyScanCallback");
          onEnergyScanFinished();
          return true;
        }
        paramParcel1.enforceInterface("android.net.lowpan.ILowpanEnergyScanCallback");
        onEnergyScanResult(paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.net.lowpan.ILowpanEnergyScanCallback");
      return true;
    }
    
    private static class Proxy
      implements ILowpanEnergyScanCallback
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
        return "android.net.lowpan.ILowpanEnergyScanCallback";
      }
      
      public void onEnergyScanFinished()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanEnergyScanCallback");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onEnergyScanResult(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanEnergyScanCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
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
