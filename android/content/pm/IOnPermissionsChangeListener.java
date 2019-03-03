package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IOnPermissionsChangeListener
  extends IInterface
{
  public abstract void onPermissionsChanged(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IOnPermissionsChangeListener
  {
    private static final String DESCRIPTOR = "android.content.pm.IOnPermissionsChangeListener";
    static final int TRANSACTION_onPermissionsChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IOnPermissionsChangeListener");
    }
    
    public static IOnPermissionsChangeListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IOnPermissionsChangeListener");
      if ((localIInterface != null) && ((localIInterface instanceof IOnPermissionsChangeListener))) {
        return (IOnPermissionsChangeListener)localIInterface;
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
        paramParcel2.writeString("android.content.pm.IOnPermissionsChangeListener");
        return true;
      }
      paramParcel1.enforceInterface("android.content.pm.IOnPermissionsChangeListener");
      onPermissionsChanged(paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IOnPermissionsChangeListener
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
        return "android.content.pm.IOnPermissionsChangeListener";
      }
      
      public void onPermissionsChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IOnPermissionsChangeListener");
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
