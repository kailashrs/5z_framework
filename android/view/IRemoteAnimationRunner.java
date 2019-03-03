package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IRemoteAnimationRunner
  extends IInterface
{
  public abstract void onAnimationCancelled()
    throws RemoteException;
  
  public abstract void onAnimationStart(RemoteAnimationTarget[] paramArrayOfRemoteAnimationTarget, IRemoteAnimationFinishedCallback paramIRemoteAnimationFinishedCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRemoteAnimationRunner
  {
    private static final String DESCRIPTOR = "android.view.IRemoteAnimationRunner";
    static final int TRANSACTION_onAnimationCancelled = 2;
    static final int TRANSACTION_onAnimationStart = 1;
    
    public Stub()
    {
      attachInterface(this, "android.view.IRemoteAnimationRunner");
    }
    
    public static IRemoteAnimationRunner asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IRemoteAnimationRunner");
      if ((localIInterface != null) && ((localIInterface instanceof IRemoteAnimationRunner))) {
        return (IRemoteAnimationRunner)localIInterface;
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
      if (paramInt1 != 1598968902)
      {
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.view.IRemoteAnimationRunner");
          onAnimationCancelled();
          return true;
        }
        paramParcel1.enforceInterface("android.view.IRemoteAnimationRunner");
        onAnimationStart((RemoteAnimationTarget[])paramParcel1.createTypedArray(RemoteAnimationTarget.CREATOR), IRemoteAnimationFinishedCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.view.IRemoteAnimationRunner");
      return true;
    }
    
    private static class Proxy
      implements IRemoteAnimationRunner
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
        return "android.view.IRemoteAnimationRunner";
      }
      
      public void onAnimationCancelled()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IRemoteAnimationRunner");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onAnimationStart(RemoteAnimationTarget[] paramArrayOfRemoteAnimationTarget, IRemoteAnimationFinishedCallback paramIRemoteAnimationFinishedCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IRemoteAnimationRunner");
          localParcel.writeTypedArray(paramArrayOfRemoteAnimationTarget, 0);
          if (paramIRemoteAnimationFinishedCallback != null) {
            paramArrayOfRemoteAnimationTarget = paramIRemoteAnimationFinishedCallback.asBinder();
          } else {
            paramArrayOfRemoteAnimationTarget = null;
          }
          localParcel.writeStrongBinder(paramArrayOfRemoteAnimationTarget);
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
