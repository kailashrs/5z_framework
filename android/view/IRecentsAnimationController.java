package android.view;

import android.app.ActivityManager.TaskSnapshot;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IRecentsAnimationController
  extends IInterface
{
  public abstract void finish(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void hideCurrentInputMethod()
    throws RemoteException;
  
  public abstract ActivityManager.TaskSnapshot screenshotTask(int paramInt)
    throws RemoteException;
  
  public abstract void setAnimationTargetsBehindSystemBars(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setInputConsumerEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSplitScreenMinimized(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRecentsAnimationController
  {
    private static final String DESCRIPTOR = "android.view.IRecentsAnimationController";
    static final int TRANSACTION_finish = 2;
    static final int TRANSACTION_hideCurrentInputMethod = 6;
    static final int TRANSACTION_screenshotTask = 1;
    static final int TRANSACTION_setAnimationTargetsBehindSystemBars = 4;
    static final int TRANSACTION_setInputConsumerEnabled = 3;
    static final int TRANSACTION_setSplitScreenMinimized = 5;
    
    public Stub()
    {
      attachInterface(this, "android.view.IRecentsAnimationController");
    }
    
    public static IRecentsAnimationController asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IRecentsAnimationController");
      if ((localIInterface != null) && ((localIInterface instanceof IRecentsAnimationController))) {
        return (IRecentsAnimationController)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("android.view.IRecentsAnimationController");
          hideCurrentInputMethod();
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.view.IRecentsAnimationController");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setSplitScreenMinimized(bool4);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.view.IRecentsAnimationController");
          bool4 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setAnimationTargetsBehindSystemBars(bool4);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.view.IRecentsAnimationController");
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setInputConsumerEnabled(bool4);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.IRecentsAnimationController");
          bool4 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          finish(bool4);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.view.IRecentsAnimationController");
        paramParcel1 = screenshotTask(paramParcel1.readInt());
        paramParcel2.writeNoException();
        if (paramParcel1 != null)
        {
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
        }
        else
        {
          paramParcel2.writeInt(0);
        }
        return true;
      }
      paramParcel2.writeString("android.view.IRecentsAnimationController");
      return true;
    }
    
    private static class Proxy
      implements IRecentsAnimationController
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
      
      public void finish(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IRecentsAnimationController");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.view.IRecentsAnimationController";
      }
      
      public void hideCurrentInputMethod()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IRecentsAnimationController");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ActivityManager.TaskSnapshot screenshotTask(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IRecentsAnimationController");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ActivityManager.TaskSnapshot localTaskSnapshot;
          if (localParcel2.readInt() != 0) {
            localTaskSnapshot = (ActivityManager.TaskSnapshot)ActivityManager.TaskSnapshot.CREATOR.createFromParcel(localParcel2);
          } else {
            localTaskSnapshot = null;
          }
          return localTaskSnapshot;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAnimationTargetsBehindSystemBars(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IRecentsAnimationController");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setInputConsumerEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IRecentsAnimationController");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSplitScreenMinimized(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IRecentsAnimationController");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
