package android.view;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IRecentsAnimationRunner
  extends IInterface
{
  public abstract void onAnimationCanceled()
    throws RemoteException;
  
  public abstract void onAnimationStart(IRecentsAnimationController paramIRecentsAnimationController, RemoteAnimationTarget[] paramArrayOfRemoteAnimationTarget, Rect paramRect1, Rect paramRect2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRecentsAnimationRunner
  {
    private static final String DESCRIPTOR = "android.view.IRecentsAnimationRunner";
    static final int TRANSACTION_onAnimationCanceled = 2;
    static final int TRANSACTION_onAnimationStart = 3;
    
    public Stub()
    {
      attachInterface(this, "android.view.IRecentsAnimationRunner");
    }
    
    public static IRecentsAnimationRunner asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IRecentsAnimationRunner");
      if ((localIInterface != null) && ((localIInterface instanceof IRecentsAnimationRunner))) {
        return (IRecentsAnimationRunner)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.view.IRecentsAnimationRunner");
          IRecentsAnimationController localIRecentsAnimationController = IRecentsAnimationController.Stub.asInterface(paramParcel1.readStrongBinder());
          RemoteAnimationTarget[] arrayOfRemoteAnimationTarget = (RemoteAnimationTarget[])paramParcel1.createTypedArray(RemoteAnimationTarget.CREATOR);
          paramInt1 = paramParcel1.readInt();
          Object localObject = null;
          if (paramInt1 != 0) {
            paramParcel2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject;
          }
          onAnimationStart(localIRecentsAnimationController, arrayOfRemoteAnimationTarget, paramParcel2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.view.IRecentsAnimationRunner");
        onAnimationCanceled();
        return true;
      }
      paramParcel2.writeString("android.view.IRecentsAnimationRunner");
      return true;
    }
    
    private static class Proxy
      implements IRecentsAnimationRunner
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
        return "android.view.IRecentsAnimationRunner";
      }
      
      public void onAnimationCanceled()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IRecentsAnimationRunner");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onAnimationStart(IRecentsAnimationController paramIRecentsAnimationController, RemoteAnimationTarget[] paramArrayOfRemoteAnimationTarget, Rect paramRect1, Rect paramRect2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IRecentsAnimationRunner");
          if (paramIRecentsAnimationController != null) {
            paramIRecentsAnimationController = paramIRecentsAnimationController.asBinder();
          } else {
            paramIRecentsAnimationController = null;
          }
          localParcel.writeStrongBinder(paramIRecentsAnimationController);
          localParcel.writeTypedArray(paramArrayOfRemoteAnimationTarget, 0);
          if (paramRect1 != null)
          {
            localParcel.writeInt(1);
            paramRect1.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramRect2 != null)
          {
            localParcel.writeInt(1);
            paramRect2.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
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
