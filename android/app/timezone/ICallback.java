package android.app.timezone;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ICallback
  extends IInterface
{
  public abstract void onFinished(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICallback
  {
    private static final String DESCRIPTOR = "android.app.timezone.ICallback";
    static final int TRANSACTION_onFinished = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.timezone.ICallback");
    }
    
    public static ICallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.timezone.ICallback");
      if ((localIInterface != null) && ((localIInterface instanceof ICallback))) {
        return (ICallback)localIInterface;
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
        paramParcel2.writeString("android.app.timezone.ICallback");
        return true;
      }
      paramParcel1.enforceInterface("android.app.timezone.ICallback");
      onFinished(paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements ICallback
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
        return "android.app.timezone.ICallback";
      }
      
      public void onFinished(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.timezone.ICallback");
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
