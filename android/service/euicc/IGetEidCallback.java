package android.service.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IGetEidCallback
  extends IInterface
{
  public abstract void onSuccess(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGetEidCallback
  {
    private static final String DESCRIPTOR = "android.service.euicc.IGetEidCallback";
    static final int TRANSACTION_onSuccess = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.euicc.IGetEidCallback");
    }
    
    public static IGetEidCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.euicc.IGetEidCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IGetEidCallback))) {
        return (IGetEidCallback)localIInterface;
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
        paramParcel2.writeString("android.service.euicc.IGetEidCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.service.euicc.IGetEidCallback");
      onSuccess(paramParcel1.readString());
      return true;
    }
    
    private static class Proxy
      implements IGetEidCallback
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
        return "android.service.euicc.IGetEidCallback";
      }
      
      public void onSuccess(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IGetEidCallback");
          localParcel.writeString(paramString);
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
