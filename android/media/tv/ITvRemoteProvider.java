package android.media.tv;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ITvRemoteProvider
  extends IInterface
{
  public abstract void onInputBridgeConnected(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void setRemoteServiceInputSink(ITvRemoteServiceInput paramITvRemoteServiceInput)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITvRemoteProvider
  {
    private static final String DESCRIPTOR = "android.media.tv.ITvRemoteProvider";
    static final int TRANSACTION_onInputBridgeConnected = 2;
    static final int TRANSACTION_setRemoteServiceInputSink = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.tv.ITvRemoteProvider");
    }
    
    public static ITvRemoteProvider asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.tv.ITvRemoteProvider");
      if ((localIInterface != null) && ((localIInterface instanceof ITvRemoteProvider))) {
        return (ITvRemoteProvider)localIInterface;
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
          paramParcel1.enforceInterface("android.media.tv.ITvRemoteProvider");
          onInputBridgeConnected(paramParcel1.readStrongBinder());
          return true;
        }
        paramParcel1.enforceInterface("android.media.tv.ITvRemoteProvider");
        setRemoteServiceInputSink(ITvRemoteServiceInput.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.media.tv.ITvRemoteProvider");
      return true;
    }
    
    private static class Proxy
      implements ITvRemoteProvider
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
        return "android.media.tv.ITvRemoteProvider";
      }
      
      public void onInputBridgeConnected(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvRemoteProvider");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setRemoteServiceInputSink(ITvRemoteServiceInput paramITvRemoteServiceInput)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvRemoteProvider");
          if (paramITvRemoteServiceInput != null) {
            paramITvRemoteServiceInput = paramITvRemoteServiceInput.asBinder();
          } else {
            paramITvRemoteServiceInput = null;
          }
          localParcel.writeStrongBinder(paramITvRemoteServiceInput);
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
