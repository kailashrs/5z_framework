package android.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface INetworkServiceCallback
  extends IInterface
{
  public abstract void onGetNetworkRegistrationStateComplete(int paramInt, NetworkRegistrationState paramNetworkRegistrationState)
    throws RemoteException;
  
  public abstract void onNetworkStateChanged()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkServiceCallback
  {
    private static final String DESCRIPTOR = "android.telephony.INetworkServiceCallback";
    static final int TRANSACTION_onGetNetworkRegistrationStateComplete = 1;
    static final int TRANSACTION_onNetworkStateChanged = 2;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.INetworkServiceCallback");
    }
    
    public static INetworkServiceCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.INetworkServiceCallback");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkServiceCallback))) {
        return (INetworkServiceCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.telephony.INetworkServiceCallback");
          onNetworkStateChanged();
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.INetworkServiceCallback");
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (NetworkRegistrationState)NetworkRegistrationState.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onGetNetworkRegistrationStateComplete(paramInt1, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.telephony.INetworkServiceCallback");
      return true;
    }
    
    private static class Proxy
      implements INetworkServiceCallback
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
        return "android.telephony.INetworkServiceCallback";
      }
      
      public void onGetNetworkRegistrationStateComplete(int paramInt, NetworkRegistrationState paramNetworkRegistrationState)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.INetworkServiceCallback");
          localParcel.writeInt(paramInt);
          if (paramNetworkRegistrationState != null)
          {
            localParcel.writeInt(1);
            paramNetworkRegistrationState.writeToParcel(localParcel, 0);
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
      
      public void onNetworkStateChanged()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.INetworkServiceCallback");
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
