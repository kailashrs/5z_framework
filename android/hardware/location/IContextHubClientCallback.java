package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IContextHubClientCallback
  extends IInterface
{
  public abstract void onHubReset()
    throws RemoteException;
  
  public abstract void onMessageFromNanoApp(NanoAppMessage paramNanoAppMessage)
    throws RemoteException;
  
  public abstract void onNanoAppAborted(long paramLong, int paramInt)
    throws RemoteException;
  
  public abstract void onNanoAppDisabled(long paramLong)
    throws RemoteException;
  
  public abstract void onNanoAppEnabled(long paramLong)
    throws RemoteException;
  
  public abstract void onNanoAppLoaded(long paramLong)
    throws RemoteException;
  
  public abstract void onNanoAppUnloaded(long paramLong)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IContextHubClientCallback
  {
    private static final String DESCRIPTOR = "android.hardware.location.IContextHubClientCallback";
    static final int TRANSACTION_onHubReset = 2;
    static final int TRANSACTION_onMessageFromNanoApp = 1;
    static final int TRANSACTION_onNanoAppAborted = 3;
    static final int TRANSACTION_onNanoAppDisabled = 7;
    static final int TRANSACTION_onNanoAppEnabled = 6;
    static final int TRANSACTION_onNanoAppLoaded = 4;
    static final int TRANSACTION_onNanoAppUnloaded = 5;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.location.IContextHubClientCallback");
    }
    
    public static IContextHubClientCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.location.IContextHubClientCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IContextHubClientCallback))) {
        return (IContextHubClientCallback)localIInterface;
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
        case 7: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubClientCallback");
          onNanoAppDisabled(paramParcel1.readLong());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubClientCallback");
          onNanoAppEnabled(paramParcel1.readLong());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubClientCallback");
          onNanoAppUnloaded(paramParcel1.readLong());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubClientCallback");
          onNanoAppLoaded(paramParcel1.readLong());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubClientCallback");
          onNanoAppAborted(paramParcel1.readLong(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubClientCallback");
          onHubReset();
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.location.IContextHubClientCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (NanoAppMessage)NanoAppMessage.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onMessageFromNanoApp(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.hardware.location.IContextHubClientCallback");
      return true;
    }
    
    private static class Proxy
      implements IContextHubClientCallback
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
        return "android.hardware.location.IContextHubClientCallback";
      }
      
      public void onHubReset()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IContextHubClientCallback");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onMessageFromNanoApp(NanoAppMessage paramNanoAppMessage)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IContextHubClientCallback");
          if (paramNanoAppMessage != null)
          {
            localParcel.writeInt(1);
            paramNanoAppMessage.writeToParcel(localParcel, 0);
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
      
      public void onNanoAppAborted(long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IContextHubClientCallback");
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNanoAppDisabled(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IContextHubClientCallback");
          localParcel.writeLong(paramLong);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNanoAppEnabled(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IContextHubClientCallback");
          localParcel.writeLong(paramLong);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNanoAppLoaded(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IContextHubClientCallback");
          localParcel.writeLong(paramLong);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNanoAppUnloaded(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IContextHubClientCallback");
          localParcel.writeLong(paramLong);
          mRemote.transact(5, localParcel, null, 1);
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
