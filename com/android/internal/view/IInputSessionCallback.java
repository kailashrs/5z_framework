package com.android.internal.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IInputSessionCallback
  extends IInterface
{
  public abstract void sessionCreated(IInputMethodSession paramIInputMethodSession)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputSessionCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.view.IInputSessionCallback";
    static final int TRANSACTION_sessionCreated = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.view.IInputSessionCallback");
    }
    
    public static IInputSessionCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.view.IInputSessionCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IInputSessionCallback))) {
        return (IInputSessionCallback)localIInterface;
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
        paramParcel2.writeString("com.android.internal.view.IInputSessionCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.view.IInputSessionCallback");
      sessionCreated(IInputMethodSession.Stub.asInterface(paramParcel1.readStrongBinder()));
      return true;
    }
    
    private static class Proxy
      implements IInputSessionCallback
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
        return "com.android.internal.view.IInputSessionCallback";
      }
      
      public void sessionCreated(IInputMethodSession paramIInputMethodSession)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputSessionCallback");
          if (paramIInputMethodSession != null) {
            paramIInputMethodSession = paramIInputMethodSession.asBinder();
          } else {
            paramIInputMethodSession = null;
          }
          localParcel.writeStrongBinder(paramIInputMethodSession);
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
