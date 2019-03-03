package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IContextHubClient
  extends IInterface
{
  public abstract void close()
    throws RemoteException;
  
  public abstract int sendMessageToNanoApp(NanoAppMessage paramNanoAppMessage)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IContextHubClient
  {
    private static final String DESCRIPTOR = "android.hardware.location.IContextHubClient";
    static final int TRANSACTION_close = 2;
    static final int TRANSACTION_sendMessageToNanoApp = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.location.IContextHubClient");
    }
    
    public static IContextHubClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.location.IContextHubClient");
      if ((localIInterface != null) && ((localIInterface instanceof IContextHubClient))) {
        return (IContextHubClient)localIInterface;
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
          paramParcel1.enforceInterface("android.hardware.location.IContextHubClient");
          close();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.location.IContextHubClient");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (NanoAppMessage)NanoAppMessage.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        paramInt1 = sendMessageToNanoApp(paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.hardware.location.IContextHubClient");
      return true;
    }
    
    private static class Proxy
      implements IContextHubClient
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
      
      public void close()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubClient");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.hardware.location.IContextHubClient";
      }
      
      public int sendMessageToNanoApp(NanoAppMessage paramNanoAppMessage)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubClient");
          if (paramNanoAppMessage != null)
          {
            localParcel1.writeInt(1);
            paramNanoAppMessage.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
