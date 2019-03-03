package android.app;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IServiceConnection
  extends IInterface
{
  public abstract void connected(ComponentName paramComponentName, IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IServiceConnection
  {
    private static final String DESCRIPTOR = "android.app.IServiceConnection";
    static final int TRANSACTION_connected = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.IServiceConnection");
    }
    
    public static IServiceConnection asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IServiceConnection");
      if ((localIInterface != null) && ((localIInterface instanceof IServiceConnection))) {
        return (IServiceConnection)localIInterface;
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
        paramParcel2.writeString("android.app.IServiceConnection");
        return true;
      }
      paramParcel1.enforceInterface("android.app.IServiceConnection");
      if (paramParcel1.readInt() != 0) {
        paramParcel2 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      IBinder localIBinder = paramParcel1.readStrongBinder();
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      connected(paramParcel2, localIBinder, bool);
      return true;
    }
    
    private static class Proxy
      implements IServiceConnection
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
      
      public void connected(ComponentName paramComponentName, IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IServiceConnection");
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.IServiceConnection";
      }
    }
  }
}
