package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IDockedStackListener
  extends IInterface
{
  public abstract void onAdjustedForImeChanged(boolean paramBoolean, long paramLong)
    throws RemoteException;
  
  public abstract void onDividerVisibilityChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onDockSideChanged(int paramInt)
    throws RemoteException;
  
  public abstract void onDockedStackExistsChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onDockedStackMinimizedChanged(boolean paramBoolean1, long paramLong, boolean paramBoolean2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDockedStackListener
  {
    private static final String DESCRIPTOR = "android.view.IDockedStackListener";
    static final int TRANSACTION_onAdjustedForImeChanged = 4;
    static final int TRANSACTION_onDividerVisibilityChanged = 1;
    static final int TRANSACTION_onDockSideChanged = 5;
    static final int TRANSACTION_onDockedStackExistsChanged = 2;
    static final int TRANSACTION_onDockedStackMinimizedChanged = 3;
    
    public Stub()
    {
      attachInterface(this, "android.view.IDockedStackListener");
    }
    
    public static IDockedStackListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IDockedStackListener");
      if ((localIInterface != null) && ((localIInterface instanceof IDockedStackListener))) {
        return (IDockedStackListener)localIInterface;
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
        case 5: 
          paramParcel1.enforceInterface("android.view.IDockedStackListener");
          onDockSideChanged(paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.view.IDockedStackListener");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onAdjustedForImeChanged(bool4, paramParcel1.readLong());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.view.IDockedStackListener");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          long l = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          onDockedStackMinimizedChanged(bool4, l, bool1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.IDockedStackListener");
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onDockedStackExistsChanged(bool4);
          return true;
        }
        paramParcel1.enforceInterface("android.view.IDockedStackListener");
        bool4 = bool3;
        if (paramParcel1.readInt() != 0) {
          bool4 = true;
        }
        onDividerVisibilityChanged(bool4);
        return true;
      }
      paramParcel2.writeString("android.view.IDockedStackListener");
      return true;
    }
    
    private static class Proxy
      implements IDockedStackListener
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
        return "android.view.IDockedStackListener";
      }
      
      public void onAdjustedForImeChanged(boolean paramBoolean, long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IDockedStackListener");
          localParcel.writeInt(paramBoolean);
          localParcel.writeLong(paramLong);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDividerVisibilityChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IDockedStackListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDockSideChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IDockedStackListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDockedStackExistsChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IDockedStackListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDockedStackMinimizedChanged(boolean paramBoolean1, long paramLong, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IDockedStackListener");
          localParcel.writeInt(paramBoolean1);
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramBoolean2);
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
