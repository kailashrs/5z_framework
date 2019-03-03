package android.companion;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ICompanionDeviceDiscoveryServiceCallback
  extends IInterface
{
  public abstract void onDeviceSelected(String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract void onDeviceSelectionCancel()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICompanionDeviceDiscoveryServiceCallback
  {
    private static final String DESCRIPTOR = "android.companion.ICompanionDeviceDiscoveryServiceCallback";
    static final int TRANSACTION_onDeviceSelected = 1;
    static final int TRANSACTION_onDeviceSelectionCancel = 2;
    
    public Stub()
    {
      attachInterface(this, "android.companion.ICompanionDeviceDiscoveryServiceCallback");
    }
    
    public static ICompanionDeviceDiscoveryServiceCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.companion.ICompanionDeviceDiscoveryServiceCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ICompanionDeviceDiscoveryServiceCallback))) {
        return (ICompanionDeviceDiscoveryServiceCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.companion.ICompanionDeviceDiscoveryServiceCallback");
          onDeviceSelectionCancel();
          return true;
        }
        paramParcel1.enforceInterface("android.companion.ICompanionDeviceDiscoveryServiceCallback");
        onDeviceSelected(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
        return true;
      }
      paramParcel2.writeString("android.companion.ICompanionDeviceDiscoveryServiceCallback");
      return true;
    }
    
    private static class Proxy
      implements ICompanionDeviceDiscoveryServiceCallback
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
        return "android.companion.ICompanionDeviceDiscoveryServiceCallback";
      }
      
      public void onDeviceSelected(String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.companion.ICompanionDeviceDiscoveryServiceCallback");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString2);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDeviceSelectionCancel()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.companion.ICompanionDeviceDiscoveryServiceCallback");
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
