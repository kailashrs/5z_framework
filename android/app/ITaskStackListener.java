package android.app;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ITaskStackListener
  extends IInterface
{
  public static final int FORCED_RESIZEABLE_REASON_SECONDARY_DISPLAY = 2;
  public static final int FORCED_RESIZEABLE_REASON_SPLIT_SCREEN = 1;
  
  public abstract void onActivityDismissingDockedStack()
    throws RemoteException;
  
  public abstract void onActivityForcedResizable(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onActivityLaunchOnSecondaryDisplayFailed()
    throws RemoteException;
  
  public abstract void onActivityPinned(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onActivityRequestedOrientationChanged(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onActivityUnpinned()
    throws RemoteException;
  
  public abstract void onPinnedActivityRestartAttempt(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onPinnedStackAnimationEnded()
    throws RemoteException;
  
  public abstract void onPinnedStackAnimationStarted()
    throws RemoteException;
  
  public abstract void onTaskCreated(int paramInt, ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void onTaskDescriptionChanged(int paramInt, ActivityManager.TaskDescription paramTaskDescription)
    throws RemoteException;
  
  public abstract void onTaskMovedToFront(int paramInt)
    throws RemoteException;
  
  public abstract void onTaskProfileLocked(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onTaskRemovalStarted(int paramInt)
    throws RemoteException;
  
  public abstract void onTaskRemoved(int paramInt)
    throws RemoteException;
  
  public abstract void onTaskSnapshotChanged(int paramInt, ActivityManager.TaskSnapshot paramTaskSnapshot)
    throws RemoteException;
  
  public abstract void onTaskStackChanged()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITaskStackListener
  {
    private static final String DESCRIPTOR = "android.app.ITaskStackListener";
    static final int TRANSACTION_onActivityDismissingDockedStack = 8;
    static final int TRANSACTION_onActivityForcedResizable = 7;
    static final int TRANSACTION_onActivityLaunchOnSecondaryDisplayFailed = 9;
    static final int TRANSACTION_onActivityPinned = 2;
    static final int TRANSACTION_onActivityRequestedOrientationChanged = 14;
    static final int TRANSACTION_onActivityUnpinned = 3;
    static final int TRANSACTION_onPinnedActivityRestartAttempt = 4;
    static final int TRANSACTION_onPinnedStackAnimationEnded = 6;
    static final int TRANSACTION_onPinnedStackAnimationStarted = 5;
    static final int TRANSACTION_onTaskCreated = 10;
    static final int TRANSACTION_onTaskDescriptionChanged = 13;
    static final int TRANSACTION_onTaskMovedToFront = 12;
    static final int TRANSACTION_onTaskProfileLocked = 16;
    static final int TRANSACTION_onTaskRemovalStarted = 15;
    static final int TRANSACTION_onTaskRemoved = 11;
    static final int TRANSACTION_onTaskSnapshotChanged = 17;
    static final int TRANSACTION_onTaskStackChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.ITaskStackListener");
    }
    
    public static ITaskStackListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.ITaskStackListener");
      if ((localIInterface != null) && ((localIInterface instanceof ITaskStackListener))) {
        return (ITaskStackListener)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 17: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ActivityManager.TaskSnapshot)ActivityManager.TaskSnapshot.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          onTaskSnapshotChanged(paramInt1, paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          onTaskProfileLocked(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          onTaskRemovalStarted(paramParcel1.readInt());
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          onActivityRequestedOrientationChanged(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ActivityManager.TaskDescription)ActivityManager.TaskDescription.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onTaskDescriptionChanged(paramInt1, paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          onTaskMovedToFront(paramParcel1.readInt());
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          onTaskRemoved(paramParcel1.readInt());
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onTaskCreated(paramInt1, paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          onActivityLaunchOnSecondaryDisplayFailed();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          onActivityDismissingDockedStack();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          onActivityForcedResizable(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          onPinnedStackAnimationEnded();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          onPinnedStackAnimationStarted();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          onPinnedActivityRestartAttempt(bool);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          onActivityUnpinned();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.ITaskStackListener");
          onActivityPinned(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.app.ITaskStackListener");
        onTaskStackChanged();
        return true;
      }
      paramParcel2.writeString("android.app.ITaskStackListener");
      return true;
    }
    
    private static class Proxy
      implements ITaskStackListener
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
        return "android.app.ITaskStackListener";
      }
      
      public void onActivityDismissingDockedStack()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onActivityForcedResizable(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onActivityLaunchOnSecondaryDisplayFailed()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onActivityPinned(String paramString, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onActivityRequestedOrientationChanged(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onActivityUnpinned()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPinnedActivityRestartAttempt(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPinnedStackAnimationEnded()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPinnedStackAnimationStarted()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTaskCreated(int paramInt, ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          localParcel.writeInt(paramInt);
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTaskDescriptionChanged(int paramInt, ActivityManager.TaskDescription paramTaskDescription)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          localParcel.writeInt(paramInt);
          if (paramTaskDescription != null)
          {
            localParcel.writeInt(1);
            paramTaskDescription.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTaskMovedToFront(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTaskProfileLocked(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTaskRemovalStarted(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTaskRemoved(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTaskSnapshotChanged(int paramInt, ActivityManager.TaskSnapshot paramTaskSnapshot)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
          localParcel.writeInt(paramInt);
          if (paramTaskSnapshot != null)
          {
            localParcel.writeInt(1);
            paramTaskSnapshot.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTaskStackChanged()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.ITaskStackListener");
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
