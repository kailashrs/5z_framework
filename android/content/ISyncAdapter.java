package android.content;

import android.accounts.Account;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ISyncAdapter
  extends IInterface
{
  public abstract void cancelSync(ISyncContext paramISyncContext)
    throws RemoteException;
  
  public abstract void onUnsyncableAccount(ISyncAdapterUnsyncableAccountCallback paramISyncAdapterUnsyncableAccountCallback)
    throws RemoteException;
  
  public abstract void startSync(ISyncContext paramISyncContext, String paramString, Account paramAccount, Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISyncAdapter
  {
    private static final String DESCRIPTOR = "android.content.ISyncAdapter";
    static final int TRANSACTION_cancelSync = 3;
    static final int TRANSACTION_onUnsyncableAccount = 1;
    static final int TRANSACTION_startSync = 2;
    
    public Stub()
    {
      attachInterface(this, "android.content.ISyncAdapter");
    }
    
    public static ISyncAdapter asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.ISyncAdapter");
      if ((localIInterface != null) && ((localIInterface instanceof ISyncAdapter))) {
        return (ISyncAdapter)localIInterface;
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
          paramParcel1.enforceInterface("android.content.ISyncAdapter");
          cancelSync(ISyncContext.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.content.ISyncAdapter");
          ISyncContext localISyncContext = ISyncContext.Stub.asInterface(paramParcel1.readStrongBinder());
          String str = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          Object localObject = null;
          if (paramInt1 != 0) {
            paramParcel2 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject;
          }
          startSync(localISyncContext, str, paramParcel2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.content.ISyncAdapter");
        onUnsyncableAccount(ISyncAdapterUnsyncableAccountCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.content.ISyncAdapter");
      return true;
    }
    
    private static class Proxy
      implements ISyncAdapter
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
          localParcel.writeInterfaceToken("android.content.ISyncAdapter");
          if (paramISyncContext != null) {
            paramISyncContext = paramISyncContext.asBinder();
          } else {
            paramISyncContext = null;
          }
          localParcel.writeStrongBinder(paramISyncContext);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.content.ISyncAdapter";
      }
      
      public void onUnsyncableAccount(ISyncAdapterUnsyncableAccountCallback paramISyncAdapterUnsyncableAccountCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.ISyncAdapter");
          if (paramISyncAdapterUnsyncableAccountCallback != null) {
            paramISyncAdapterUnsyncableAccountCallback = paramISyncAdapterUnsyncableAccountCallback.asBinder();
          } else {
            paramISyncAdapterUnsyncableAccountCallback = null;
          }
          localParcel.writeStrongBinder(paramISyncAdapterUnsyncableAccountCallback);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void startSync(ISyncContext paramISyncContext, String paramString, Account paramAccount, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.ISyncAdapter");
          if (paramISyncContext != null) {
            paramISyncContext = paramISyncContext.asBinder();
          } else {
            paramISyncContext = null;
          }
          localParcel.writeStrongBinder(paramISyncContext);
          localParcel.writeString(paramString);
          if (paramAccount != null)
          {
            localParcel.writeInt(1);
            paramAccount.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
