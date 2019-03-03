package android.view;

import android.content.pm.ParceledListSlice;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPinnedStackListener
  extends IInterface
{
  public abstract void onActionsChanged(ParceledListSlice paramParceledListSlice)
    throws RemoteException;
  
  public abstract void onImeVisibilityChanged(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void onListenerRegistered(IPinnedStackController paramIPinnedStackController)
    throws RemoteException;
  
  public abstract void onMinimizedStateChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onMovementBoundsChanged(Rect paramRect1, Rect paramRect2, Rect paramRect3, boolean paramBoolean1, boolean paramBoolean2, int paramInt)
    throws RemoteException;
  
  public abstract void onShelfVisibilityChanged(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPinnedStackListener
  {
    private static final String DESCRIPTOR = "android.view.IPinnedStackListener";
    static final int TRANSACTION_onActionsChanged = 6;
    static final int TRANSACTION_onImeVisibilityChanged = 3;
    static final int TRANSACTION_onListenerRegistered = 1;
    static final int TRANSACTION_onMinimizedStateChanged = 5;
    static final int TRANSACTION_onMovementBoundsChanged = 2;
    static final int TRANSACTION_onShelfVisibilityChanged = 4;
    
    public Stub()
    {
      attachInterface(this, "android.view.IPinnedStackListener");
    }
    
    public static IPinnedStackListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IPinnedStackListener");
      if ((localIInterface != null) && ((localIInterface instanceof IPinnedStackListener))) {
        return (IPinnedStackListener)localIInterface;
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
        Rect localRect1 = null;
        Rect localRect2 = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("android.view.IPinnedStackListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localRect2;
          }
          onActionsChanged(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.view.IPinnedStackListener");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onMinimizedStateChanged(bool4);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.view.IPinnedStackListener");
          bool4 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onShelfVisibilityChanged(bool4, paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.view.IPinnedStackListener");
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onImeVisibilityChanged(bool4, paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.IPinnedStackListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localRect2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localRect2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localRect1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          onMovementBoundsChanged(paramParcel2, localRect2, localRect1, bool4, bool3, paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.view.IPinnedStackListener");
        onListenerRegistered(IPinnedStackController.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.view.IPinnedStackListener");
      return true;
    }
    
    private static class Proxy
      implements IPinnedStackListener
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
        return "android.view.IPinnedStackListener";
      }
      
      public void onActionsChanged(ParceledListSlice paramParceledListSlice)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IPinnedStackListener");
          if (paramParceledListSlice != null)
          {
            localParcel.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onImeVisibilityChanged(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IPinnedStackListener");
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onListenerRegistered(IPinnedStackController paramIPinnedStackController)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IPinnedStackListener");
          if (paramIPinnedStackController != null) {
            paramIPinnedStackController = paramIPinnedStackController.asBinder();
          } else {
            paramIPinnedStackController = null;
          }
          localParcel.writeStrongBinder(paramIPinnedStackController);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onMinimizedStateChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IPinnedStackListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onMovementBoundsChanged(Rect paramRect1, Rect paramRect2, Rect paramRect3, boolean paramBoolean1, boolean paramBoolean2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IPinnedStackListener");
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
          if (paramRect3 != null)
          {
            localParcel.writeInt(1);
            paramRect3.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onShelfVisibilityChanged(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IPinnedStackListener");
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
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
