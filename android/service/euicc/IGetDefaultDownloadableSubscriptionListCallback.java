package android.service.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IGetDefaultDownloadableSubscriptionListCallback
  extends IInterface
{
  public abstract void onComplete(GetDefaultDownloadableSubscriptionListResult paramGetDefaultDownloadableSubscriptionListResult)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGetDefaultDownloadableSubscriptionListCallback
  {
    private static final String DESCRIPTOR = "android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback";
    static final int TRANSACTION_onComplete = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback");
    }
    
    public static IGetDefaultDownloadableSubscriptionListCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IGetDefaultDownloadableSubscriptionListCallback))) {
        return (IGetDefaultDownloadableSubscriptionListCallback)localIInterface;
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
        paramParcel2.writeString("android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (GetDefaultDownloadableSubscriptionListResult)GetDefaultDownloadableSubscriptionListResult.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onComplete(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IGetDefaultDownloadableSubscriptionListCallback
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
        return "android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback";
      }
      
      public void onComplete(GetDefaultDownloadableSubscriptionListResult paramGetDefaultDownloadableSubscriptionListResult)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback");
          if (paramGetDefaultDownloadableSubscriptionListResult != null)
          {
            localParcel.writeInt(1);
            paramGetDefaultDownloadableSubscriptionListResult.writeToParcel(localParcel, 0);
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
    }
  }
}
