package android.net.wifi.rtt;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IRttCallback
  extends IInterface
{
  public abstract void onRangingFailure(int paramInt)
    throws RemoteException;
  
  public abstract void onRangingResults(List<RangingResult> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRttCallback
  {
    private static final String DESCRIPTOR = "android.net.wifi.rtt.IRttCallback";
    static final int TRANSACTION_onRangingFailure = 1;
    static final int TRANSACTION_onRangingResults = 2;
    
    public Stub()
    {
      attachInterface(this, "android.net.wifi.rtt.IRttCallback");
    }
    
    public static IRttCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.wifi.rtt.IRttCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IRttCallback))) {
        return (IRttCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.net.wifi.rtt.IRttCallback");
          onRangingResults(paramParcel1.createTypedArrayList(RangingResult.CREATOR));
          return true;
        }
        paramParcel1.enforceInterface("android.net.wifi.rtt.IRttCallback");
        onRangingFailure(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.net.wifi.rtt.IRttCallback");
      return true;
    }
    
    private static class Proxy
      implements IRttCallback
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
        return "android.net.wifi.rtt.IRttCallback";
      }
      
      public void onRangingFailure(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.rtt.IRttCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRangingResults(List<RangingResult> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.rtt.IRttCallback");
          localParcel.writeTypedList(paramList);
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
