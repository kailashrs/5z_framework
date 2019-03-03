package android.companion;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ICompanionDeviceDiscoveryService
  extends IInterface
{
  public abstract void startDiscovery(AssociationRequest paramAssociationRequest, String paramString, IFindDeviceCallback paramIFindDeviceCallback, ICompanionDeviceDiscoveryServiceCallback paramICompanionDeviceDiscoveryServiceCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICompanionDeviceDiscoveryService
  {
    private static final String DESCRIPTOR = "android.companion.ICompanionDeviceDiscoveryService";
    static final int TRANSACTION_startDiscovery = 1;
    
    public Stub()
    {
      attachInterface(this, "android.companion.ICompanionDeviceDiscoveryService");
    }
    
    public static ICompanionDeviceDiscoveryService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.companion.ICompanionDeviceDiscoveryService");
      if ((localIInterface != null) && ((localIInterface instanceof ICompanionDeviceDiscoveryService))) {
        return (ICompanionDeviceDiscoveryService)localIInterface;
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
        paramParcel2.writeString("android.companion.ICompanionDeviceDiscoveryService");
        return true;
      }
      paramParcel1.enforceInterface("android.companion.ICompanionDeviceDiscoveryService");
      AssociationRequest localAssociationRequest;
      if (paramParcel1.readInt() != 0) {
        localAssociationRequest = (AssociationRequest)AssociationRequest.CREATOR.createFromParcel(paramParcel1);
      } else {
        localAssociationRequest = null;
      }
      startDiscovery(localAssociationRequest, paramParcel1.readString(), IFindDeviceCallback.Stub.asInterface(paramParcel1.readStrongBinder()), ICompanionDeviceDiscoveryServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements ICompanionDeviceDiscoveryService
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
        return "android.companion.ICompanionDeviceDiscoveryService";
      }
      
      public void startDiscovery(AssociationRequest paramAssociationRequest, String paramString, IFindDeviceCallback paramIFindDeviceCallback, ICompanionDeviceDiscoveryServiceCallback paramICompanionDeviceDiscoveryServiceCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.companion.ICompanionDeviceDiscoveryService");
          if (paramAssociationRequest != null)
          {
            localParcel1.writeInt(1);
            paramAssociationRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          paramString = null;
          if (paramIFindDeviceCallback != null) {
            paramAssociationRequest = paramIFindDeviceCallback.asBinder();
          } else {
            paramAssociationRequest = null;
          }
          localParcel1.writeStrongBinder(paramAssociationRequest);
          paramAssociationRequest = paramString;
          if (paramICompanionDeviceDiscoveryServiceCallback != null) {
            paramAssociationRequest = paramICompanionDeviceDiscoveryServiceCallback.asBinder();
          }
          localParcel1.writeStrongBinder(paramAssociationRequest);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
