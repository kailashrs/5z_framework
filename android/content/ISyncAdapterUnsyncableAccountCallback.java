package android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ISyncAdapterUnsyncableAccountCallback
  extends IInterface
{
  public abstract void onUnsyncableAccountDone(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISyncAdapterUnsyncableAccountCallback
  {
    private static final String DESCRIPTOR = "android.content.ISyncAdapterUnsyncableAccountCallback";
    static final int TRANSACTION_onUnsyncableAccountDone = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.ISyncAdapterUnsyncableAccountCallback");
    }
    
    public static ISyncAdapterUnsyncableAccountCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.ISyncAdapterUnsyncableAccountCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ISyncAdapterUnsyncableAccountCallback))) {
        return (ISyncAdapterUnsyncableAccountCallback)localIInterface;
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
        paramParcel2.writeString("android.content.ISyncAdapterUnsyncableAccountCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.content.ISyncAdapterUnsyncableAccountCallback");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onUnsyncableAccountDone(bool);
      return true;
    }
    
    private static class Proxy
      implements ISyncAdapterUnsyncableAccountCallback
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
        return "android.content.ISyncAdapterUnsyncableAccountCallback";
      }
      
      public void onUnsyncableAccountDone(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.ISyncAdapterUnsyncableAccountCallback");
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
