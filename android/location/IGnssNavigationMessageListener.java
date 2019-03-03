package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IGnssNavigationMessageListener
  extends IInterface
{
  public abstract void onGnssNavigationMessageReceived(GnssNavigationMessage paramGnssNavigationMessage)
    throws RemoteException;
  
  public abstract void onStatusChanged(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGnssNavigationMessageListener
  {
    private static final String DESCRIPTOR = "android.location.IGnssNavigationMessageListener";
    static final int TRANSACTION_onGnssNavigationMessageReceived = 1;
    static final int TRANSACTION_onStatusChanged = 2;
    
    public Stub()
    {
      attachInterface(this, "android.location.IGnssNavigationMessageListener");
    }
    
    public static IGnssNavigationMessageListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.location.IGnssNavigationMessageListener");
      if ((localIInterface != null) && ((localIInterface instanceof IGnssNavigationMessageListener))) {
        return (IGnssNavigationMessageListener)localIInterface;
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
          paramParcel1.enforceInterface("android.location.IGnssNavigationMessageListener");
          onStatusChanged(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.location.IGnssNavigationMessageListener");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (GnssNavigationMessage)GnssNavigationMessage.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onGnssNavigationMessageReceived(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.location.IGnssNavigationMessageListener");
      return true;
    }
    
    private static class Proxy
      implements IGnssNavigationMessageListener
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
        return "android.location.IGnssNavigationMessageListener";
      }
      
      public void onGnssNavigationMessageReceived(GnssNavigationMessage paramGnssNavigationMessage)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.IGnssNavigationMessageListener");
          if (paramGnssNavigationMessage != null)
          {
            localParcel.writeInt(1);
            paramGnssNavigationMessage.writeToParcel(localParcel, 0);
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
      
      public void onStatusChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.IGnssNavigationMessageListener");
          localParcel.writeInt(paramInt);
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
