package com.android.internal.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IInputMethodClient
  extends IInterface
{
  public abstract void onBindMethod(InputBindResult paramInputBindResult)
    throws RemoteException;
  
  public abstract void onUnbindMethod(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void reportFullscreenMode(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setActive(boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void setUserActionNotificationSequenceNumber(int paramInt)
    throws RemoteException;
  
  public abstract void setUsingInputMethod(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputMethodClient
  {
    private static final String DESCRIPTOR = "com.android.internal.view.IInputMethodClient";
    static final int TRANSACTION_onBindMethod = 2;
    static final int TRANSACTION_onUnbindMethod = 3;
    static final int TRANSACTION_reportFullscreenMode = 6;
    static final int TRANSACTION_setActive = 4;
    static final int TRANSACTION_setUserActionNotificationSequenceNumber = 5;
    static final int TRANSACTION_setUsingInputMethod = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.view.IInputMethodClient");
    }
    
    public static IInputMethodClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.view.IInputMethodClient");
      if ((localIInterface != null) && ((localIInterface instanceof IInputMethodClient))) {
        return (IInputMethodClient)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodClient");
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          reportFullscreenMode(bool3);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodClient");
          setUserActionNotificationSequenceNumber(paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodClient");
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          setActive(bool3, bool1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodClient");
          onUnbindMethod(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (InputBindResult)InputBindResult.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          onBindMethod(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.view.IInputMethodClient");
        bool3 = bool2;
        if (paramParcel1.readInt() != 0) {
          bool3 = true;
        }
        setUsingInputMethod(bool3);
        return true;
      }
      paramParcel2.writeString("com.android.internal.view.IInputMethodClient");
      return true;
    }
    
    private static class Proxy
      implements IInputMethodClient
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
        return "com.android.internal.view.IInputMethodClient";
      }
      
      public void onBindMethod(InputBindResult paramInputBindResult)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodClient");
          if (paramInputBindResult != null)
          {
            localParcel.writeInt(1);
            paramInputBindResult.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUnbindMethod(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodClient");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void reportFullscreenMode(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodClient");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setActive(boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodClient");
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setUserActionNotificationSequenceNumber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodClient");
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setUsingInputMethod(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodClient");
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
