package android.service.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IOtaStatusChangedCallback
  extends IInterface
{
  public abstract void onOtaStatusChanged(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IOtaStatusChangedCallback
  {
    private static final String DESCRIPTOR = "android.service.euicc.IOtaStatusChangedCallback";
    static final int TRANSACTION_onOtaStatusChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.euicc.IOtaStatusChangedCallback");
    }
    
    public static IOtaStatusChangedCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.euicc.IOtaStatusChangedCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IOtaStatusChangedCallback))) {
        return (IOtaStatusChangedCallback)localIInterface;
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
        paramParcel2.writeString("android.service.euicc.IOtaStatusChangedCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.service.euicc.IOtaStatusChangedCallback");
      onOtaStatusChanged(paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IOtaStatusChangedCallback
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
        return "android.service.euicc.IOtaStatusChangedCallback";
      }
      
      public void onOtaStatusChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IOtaStatusChangedCallback");
          localParcel.writeInt(paramInt);
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
