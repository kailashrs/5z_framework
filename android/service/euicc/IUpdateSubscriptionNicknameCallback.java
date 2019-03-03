package android.service.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IUpdateSubscriptionNicknameCallback
  extends IInterface
{
  public abstract void onComplete(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IUpdateSubscriptionNicknameCallback
  {
    private static final String DESCRIPTOR = "android.service.euicc.IUpdateSubscriptionNicknameCallback";
    static final int TRANSACTION_onComplete = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.euicc.IUpdateSubscriptionNicknameCallback");
    }
    
    public static IUpdateSubscriptionNicknameCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.euicc.IUpdateSubscriptionNicknameCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IUpdateSubscriptionNicknameCallback))) {
        return (IUpdateSubscriptionNicknameCallback)localIInterface;
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
        paramParcel2.writeString("android.service.euicc.IUpdateSubscriptionNicknameCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.service.euicc.IUpdateSubscriptionNicknameCallback");
      onComplete(paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IUpdateSubscriptionNicknameCallback
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
        return "android.service.euicc.IUpdateSubscriptionNicknameCallback";
      }
      
      public void onComplete(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IUpdateSubscriptionNicknameCallback");
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
