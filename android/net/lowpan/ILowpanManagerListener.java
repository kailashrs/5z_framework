package android.net.lowpan;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ILowpanManagerListener
  extends IInterface
{
  public abstract void onInterfaceAdded(ILowpanInterface paramILowpanInterface)
    throws RemoteException;
  
  public abstract void onInterfaceRemoved(ILowpanInterface paramILowpanInterface)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ILowpanManagerListener
  {
    private static final String DESCRIPTOR = "android.net.lowpan.ILowpanManagerListener";
    static final int TRANSACTION_onInterfaceAdded = 1;
    static final int TRANSACTION_onInterfaceRemoved = 2;
    
    public Stub()
    {
      attachInterface(this, "android.net.lowpan.ILowpanManagerListener");
    }
    
    public static ILowpanManagerListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.lowpan.ILowpanManagerListener");
      if ((localIInterface != null) && ((localIInterface instanceof ILowpanManagerListener))) {
        return (ILowpanManagerListener)localIInterface;
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
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanManagerListener");
          onInterfaceRemoved(ILowpanInterface.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.net.lowpan.ILowpanManagerListener");
        onInterfaceAdded(ILowpanInterface.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.net.lowpan.ILowpanManagerListener");
      return true;
    }
    
    private static class Proxy
      implements ILowpanManagerListener
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
        return "android.net.lowpan.ILowpanManagerListener";
      }
      
      public void onInterfaceAdded(ILowpanInterface paramILowpanInterface)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanManagerListener");
          if (paramILowpanInterface != null) {
            paramILowpanInterface = paramILowpanInterface.asBinder();
          } else {
            paramILowpanInterface = null;
          }
          localParcel.writeStrongBinder(paramILowpanInterface);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onInterfaceRemoved(ILowpanInterface paramILowpanInterface)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanManagerListener");
          if (paramILowpanInterface != null) {
            paramILowpanInterface = paramILowpanInterface.asBinder();
          } else {
            paramILowpanInterface = null;
          }
          localParcel.writeStrongBinder(paramILowpanInterface);
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
