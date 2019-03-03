package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IRemoteAnimationFinishedCallback
  extends IInterface
{
  public abstract void onAnimationFinished()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRemoteAnimationFinishedCallback
  {
    private static final String DESCRIPTOR = "android.view.IRemoteAnimationFinishedCallback";
    static final int TRANSACTION_onAnimationFinished = 1;
    
    public Stub()
    {
      attachInterface(this, "android.view.IRemoteAnimationFinishedCallback");
    }
    
    public static IRemoteAnimationFinishedCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IRemoteAnimationFinishedCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IRemoteAnimationFinishedCallback))) {
        return (IRemoteAnimationFinishedCallback)localIInterface;
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
        paramParcel2.writeString("android.view.IRemoteAnimationFinishedCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.view.IRemoteAnimationFinishedCallback");
      onAnimationFinished();
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements IRemoteAnimationFinishedCallback
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
        return "android.view.IRemoteAnimationFinishedCallback";
      }
      
      public void onAnimationFinished()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IRemoteAnimationFinishedCallback");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
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
