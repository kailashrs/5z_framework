package com.android.internal.policy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IKeyguardStateCallback
  extends IInterface
{
  public abstract void onHasLockscreenWallpaperChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onInputRestrictedStateChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onShowingStateChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onSimSecureStateChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onTrustedChanged(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IKeyguardStateCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.policy.IKeyguardStateCallback";
    static final int TRANSACTION_onHasLockscreenWallpaperChanged = 5;
    static final int TRANSACTION_onInputRestrictedStateChanged = 3;
    static final int TRANSACTION_onShowingStateChanged = 1;
    static final int TRANSACTION_onSimSecureStateChanged = 2;
    static final int TRANSACTION_onTrustedChanged = 4;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.policy.IKeyguardStateCallback");
    }
    
    public static IKeyguardStateCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.policy.IKeyguardStateCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IKeyguardStateCallback))) {
        return (IKeyguardStateCallback)localIInterface;
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
        boolean bool5 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardStateCallback");
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          onHasLockscreenWallpaperChanged(bool5);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardStateCallback");
          bool5 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          onTrustedChanged(bool5);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardStateCallback");
          bool5 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          onInputRestrictedStateChanged(bool5);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardStateCallback");
          bool5 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          onSimSecureStateChanged(bool5);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardStateCallback");
        bool5 = bool4;
        if (paramParcel1.readInt() != 0) {
          bool5 = true;
        }
        onShowingStateChanged(bool5);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.policy.IKeyguardStateCallback");
      return true;
    }
    
    private static class Proxy
      implements IKeyguardStateCallback
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
        return "com.android.internal.policy.IKeyguardStateCallback";
      }
      
      public void onHasLockscreenWallpaperChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.policy.IKeyguardStateCallback");
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
      
      public void onInputRestrictedStateChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.policy.IKeyguardStateCallback");
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
      
      public void onShowingStateChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.policy.IKeyguardStateCallback");
          localParcel1.writeInt(paramBoolean);
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
      
      public void onSimSecureStateChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.policy.IKeyguardStateCallback");
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
      
      public void onTrustedChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.policy.IKeyguardStateCallback");
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
    }
  }
}
