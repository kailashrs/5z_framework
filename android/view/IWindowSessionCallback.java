package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IWindowSessionCallback
  extends IInterface
{
  public abstract void onAnimatorScaleChanged(float paramFloat)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWindowSessionCallback
  {
    private static final String DESCRIPTOR = "android.view.IWindowSessionCallback";
    static final int TRANSACTION_onAnimatorScaleChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.view.IWindowSessionCallback");
    }
    
    public static IWindowSessionCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IWindowSessionCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IWindowSessionCallback))) {
        return (IWindowSessionCallback)localIInterface;
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
        paramParcel2.writeString("android.view.IWindowSessionCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.view.IWindowSessionCallback");
      onAnimatorScaleChanged(paramParcel1.readFloat());
      return true;
    }
    
    private static class Proxy
      implements IWindowSessionCallback
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
        return "android.view.IWindowSessionCallback";
      }
      
      public void onAnimatorScaleChanged(float paramFloat)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindowSessionCallback");
          localParcel.writeFloat(paramFloat);
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
