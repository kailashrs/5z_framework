package android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ISyncStatusObserver
  extends IInterface
{
  public abstract void onStatusChanged(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISyncStatusObserver
  {
    private static final String DESCRIPTOR = "android.content.ISyncStatusObserver";
    static final int TRANSACTION_onStatusChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.ISyncStatusObserver");
    }
    
    public static ISyncStatusObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.ISyncStatusObserver");
      if ((localIInterface != null) && ((localIInterface instanceof ISyncStatusObserver))) {
        return (ISyncStatusObserver)localIInterface;
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
        paramParcel2.writeString("android.content.ISyncStatusObserver");
        return true;
      }
      paramParcel1.enforceInterface("android.content.ISyncStatusObserver");
      onStatusChanged(paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements ISyncStatusObserver
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
        return "android.content.ISyncStatusObserver";
      }
      
      public void onStatusChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.ISyncStatusObserver");
          localParcel.writeInt(paramInt);
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
