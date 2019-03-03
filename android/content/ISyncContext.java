package android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ISyncContext
  extends IInterface
{
  public abstract void onFinished(SyncResult paramSyncResult)
    throws RemoteException;
  
  public abstract void sendHeartbeat()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISyncContext
  {
    private static final String DESCRIPTOR = "android.content.ISyncContext";
    static final int TRANSACTION_onFinished = 2;
    static final int TRANSACTION_sendHeartbeat = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.ISyncContext");
    }
    
    public static ISyncContext asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.ISyncContext");
      if ((localIInterface != null) && ((localIInterface instanceof ISyncContext))) {
        return (ISyncContext)localIInterface;
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
          paramParcel1.enforceInterface("android.content.ISyncContext");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SyncResult)SyncResult.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          onFinished(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.content.ISyncContext");
        sendHeartbeat();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.content.ISyncContext");
      return true;
    }
    
    private static class Proxy
      implements ISyncContext
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
        return "android.content.ISyncContext";
      }
      
      public void onFinished(SyncResult paramSyncResult)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.ISyncContext");
          if (paramSyncResult != null)
          {
            localParcel1.writeInt(1);
            paramSyncResult.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void sendHeartbeat()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.ISyncContext");
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
