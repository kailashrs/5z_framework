package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IApplicationToken
  extends IInterface
{
  public abstract String getName()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IApplicationToken
  {
    private static final String DESCRIPTOR = "android.view.IApplicationToken";
    static final int TRANSACTION_getName = 1;
    
    public Stub()
    {
      attachInterface(this, "android.view.IApplicationToken");
    }
    
    public static IApplicationToken asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IApplicationToken");
      if ((localIInterface != null) && ((localIInterface instanceof IApplicationToken))) {
        return (IApplicationToken)localIInterface;
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
        paramParcel2.writeString("android.view.IApplicationToken");
        return true;
      }
      paramParcel1.enforceInterface("android.view.IApplicationToken");
      paramParcel1 = getName();
      paramParcel2.writeNoException();
      paramParcel2.writeString(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IApplicationToken
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
        return "android.view.IApplicationToken";
      }
      
      public String getName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IApplicationToken");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
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
