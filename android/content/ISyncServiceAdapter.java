package android.content;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ISyncServiceAdapter
  extends IInterface
{
  public abstract void cancelSync(ISyncContext paramISyncContext)
    throws RemoteException;
  
  public abstract void startSync(ISyncContext paramISyncContext, Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISyncServiceAdapter
  {
    private static final String DESCRIPTOR = "android.content.ISyncServiceAdapter";
    static final int TRANSACTION_cancelSync = 2;
    static final int TRANSACTION_startSync = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.ISyncServiceAdapter");
    }
    
    public static ISyncServiceAdapter asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.ISyncServiceAdapter");
      if ((localIInterface != null) && ((localIInterface instanceof ISyncServiceAdapter))) {
        return (ISyncServiceAdapter)localIInterface;
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
          paramParcel1.enforceInterface("android.content.ISyncServiceAdapter");
          cancelSync(ISyncContext.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.content.ISyncServiceAdapter");
        paramParcel2 = ISyncContext.Stub.asInterface(paramParcel1.readStrongBinder());
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        startSync(paramParcel2, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.content.ISyncServiceAdapter");
      return true;
    }
    
    private static class Proxy
      implements ISyncServiceAdapter
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
      
      public void cancelSync(ISyncContext paramISyncContext)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.ISyncServiceAdapter");
          if (paramISyncContext != null) {
            paramISyncContext = paramISyncContext.asBinder();
          } else {
            paramISyncContext = null;
          }
          localParcel.writeStrongBinder(paramISyncContext);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.content.ISyncServiceAdapter";
      }
      
      public void startSync(ISyncContext paramISyncContext, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.ISyncServiceAdapter");
          if (paramISyncContext != null) {
            paramISyncContext = paramISyncContext.asBinder();
          } else {
            paramISyncContext = null;
          }
          localParcel.writeStrongBinder(paramISyncContext);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
    }
  }
}
