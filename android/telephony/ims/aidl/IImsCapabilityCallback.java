package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsCapabilityCallback
  extends IInterface
{
  public abstract void onCapabilitiesStatusChanged(int paramInt)
    throws RemoteException;
  
  public abstract void onChangeCapabilityConfigurationError(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onQueryCapabilityConfiguration(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsCapabilityCallback
  {
    private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsCapabilityCallback";
    static final int TRANSACTION_onCapabilitiesStatusChanged = 3;
    static final int TRANSACTION_onChangeCapabilityConfigurationError = 2;
    static final int TRANSACTION_onQueryCapabilityConfiguration = 1;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.ims.aidl.IImsCapabilityCallback");
    }
    
    public static IImsCapabilityCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.ims.aidl.IImsCapabilityCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IImsCapabilityCallback))) {
        return (IImsCapabilityCallback)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCapabilityCallback");
          onCapabilitiesStatusChanged(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCapabilityCallback");
          onChangeCapabilityConfigurationError(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsCapabilityCallback");
        paramInt1 = paramParcel1.readInt();
        paramInt2 = paramParcel1.readInt();
        boolean bool;
        if (paramParcel1.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        onQueryCapabilityConfiguration(paramInt1, paramInt2, bool);
        return true;
      }
      paramParcel2.writeString("android.telephony.ims.aidl.IImsCapabilityCallback");
      return true;
    }
    
    private static class Proxy
      implements IImsCapabilityCallback
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
        return "android.telephony.ims.aidl.IImsCapabilityCallback";
      }
      
      public void onCapabilitiesStatusChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCapabilityCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onChangeCapabilityConfigurationError(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCapabilityCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onQueryCapabilityConfiguration(int paramInt1, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsCapabilityCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramBoolean);
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
