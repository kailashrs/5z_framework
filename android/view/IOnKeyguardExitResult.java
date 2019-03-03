package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IOnKeyguardExitResult
  extends IInterface
{
  public abstract void onKeyguardExitResult(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IOnKeyguardExitResult
  {
    private static final String DESCRIPTOR = "android.view.IOnKeyguardExitResult";
    static final int TRANSACTION_onKeyguardExitResult = 1;
    
    public Stub()
    {
      attachInterface(this, "android.view.IOnKeyguardExitResult");
    }
    
    public static IOnKeyguardExitResult asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IOnKeyguardExitResult");
      if ((localIInterface != null) && ((localIInterface instanceof IOnKeyguardExitResult))) {
        return (IOnKeyguardExitResult)localIInterface;
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
        paramParcel2.writeString("android.view.IOnKeyguardExitResult");
        return true;
      }
      paramParcel1.enforceInterface("android.view.IOnKeyguardExitResult");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onKeyguardExitResult(bool);
      return true;
    }
    
    private static class Proxy
      implements IOnKeyguardExitResult
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
        return "android.view.IOnKeyguardExitResult";
      }
      
      public void onKeyguardExitResult(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IOnKeyguardExitResult");
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
