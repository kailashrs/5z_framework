package android.service.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IGetDownloadableSubscriptionMetadataCallback
  extends IInterface
{
  public abstract void onComplete(GetDownloadableSubscriptionMetadataResult paramGetDownloadableSubscriptionMetadataResult)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGetDownloadableSubscriptionMetadataCallback
  {
    private static final String DESCRIPTOR = "android.service.euicc.IGetDownloadableSubscriptionMetadataCallback";
    static final int TRANSACTION_onComplete = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.euicc.IGetDownloadableSubscriptionMetadataCallback");
    }
    
    public static IGetDownloadableSubscriptionMetadataCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.euicc.IGetDownloadableSubscriptionMetadataCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IGetDownloadableSubscriptionMetadataCallback))) {
        return (IGetDownloadableSubscriptionMetadataCallback)localIInterface;
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
        paramParcel2.writeString("android.service.euicc.IGetDownloadableSubscriptionMetadataCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.service.euicc.IGetDownloadableSubscriptionMetadataCallback");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (GetDownloadableSubscriptionMetadataResult)GetDownloadableSubscriptionMetadataResult.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onComplete(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IGetDownloadableSubscriptionMetadataCallback
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
        return "android.service.euicc.IGetDownloadableSubscriptionMetadataCallback";
      }
      
      public void onComplete(GetDownloadableSubscriptionMetadataResult paramGetDownloadableSubscriptionMetadataResult)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IGetDownloadableSubscriptionMetadataCallback");
          if (paramGetDownloadableSubscriptionMetadataResult != null)
          {
            localParcel.writeInt(1);
            paramGetDownloadableSubscriptionMetadataResult.writeToParcel(localParcel, 0);
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
