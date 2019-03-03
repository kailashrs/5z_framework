package android.app.backup;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IBackupManagerMonitor
  extends IInterface
{
  public abstract void onEvent(Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBackupManagerMonitor
  {
    private static final String DESCRIPTOR = "android.app.backup.IBackupManagerMonitor";
    static final int TRANSACTION_onEvent = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.backup.IBackupManagerMonitor");
    }
    
    public static IBackupManagerMonitor asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.backup.IBackupManagerMonitor");
      if ((localIInterface != null) && ((localIInterface instanceof IBackupManagerMonitor))) {
        return (IBackupManagerMonitor)localIInterface;
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
        paramParcel2.writeString("android.app.backup.IBackupManagerMonitor");
        return true;
      }
      paramParcel1.enforceInterface("android.app.backup.IBackupManagerMonitor");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onEvent(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IBackupManagerMonitor
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
        return "android.app.backup.IBackupManagerMonitor";
      }
      
      public void onEvent(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.backup.IBackupManagerMonitor");
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
