package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsRegistration
  extends IInterface
{
  public abstract void addRegistrationCallback(IImsRegistrationCallback paramIImsRegistrationCallback)
    throws RemoteException;
  
  public abstract int getRegistrationTechnology()
    throws RemoteException;
  
  public abstract void removeRegistrationCallback(IImsRegistrationCallback paramIImsRegistrationCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsRegistration
  {
    private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsRegistration";
    static final int TRANSACTION_addRegistrationCallback = 2;
    static final int TRANSACTION_getRegistrationTechnology = 1;
    static final int TRANSACTION_removeRegistrationCallback = 3;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.ims.aidl.IImsRegistration");
    }
    
    public static IImsRegistration asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.ims.aidl.IImsRegistration");
      if ((localIInterface != null) && ((localIInterface instanceof IImsRegistration))) {
        return (IImsRegistration)localIInterface;
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
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsRegistration");
          removeRegistrationCallback(IImsRegistrationCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsRegistration");
          addRegistrationCallback(IImsRegistrationCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsRegistration");
        paramInt1 = getRegistrationTechnology();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.telephony.ims.aidl.IImsRegistration");
      return true;
    }
    
    private static class Proxy
      implements IImsRegistration
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addRegistrationCallback(IImsRegistrationCallback paramIImsRegistrationCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsRegistration");
          if (paramIImsRegistrationCallback != null) {
            paramIImsRegistrationCallback = paramIImsRegistrationCallback.asBinder();
          } else {
            paramIImsRegistrationCallback = null;
          }
          localParcel.writeStrongBinder(paramIImsRegistrationCallback);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.telephony.ims.aidl.IImsRegistration";
      }
      
      public int getRegistrationTechnology()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsRegistration");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeRegistrationCallback(IImsRegistrationCallback paramIImsRegistrationCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsRegistration");
          if (paramIImsRegistrationCallback != null) {
            paramIImsRegistrationCallback = paramIImsRegistrationCallback.asBinder();
          } else {
            paramIImsRegistrationCallback = null;
          }
          localParcel.writeStrongBinder(paramIImsRegistrationCallback);
          mRemote.transact(3, localParcel, null, 1);
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
