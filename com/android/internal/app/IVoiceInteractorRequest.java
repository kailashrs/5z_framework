package com.android.internal.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IVoiceInteractorRequest
  extends IInterface
{
  public abstract void cancel()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVoiceInteractorRequest
  {
    private static final String DESCRIPTOR = "com.android.internal.app.IVoiceInteractorRequest";
    static final int TRANSACTION_cancel = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.IVoiceInteractorRequest");
    }
    
    public static IVoiceInteractorRequest asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.IVoiceInteractorRequest");
      if ((localIInterface != null) && ((localIInterface instanceof IVoiceInteractorRequest))) {
        return (IVoiceInteractorRequest)localIInterface;
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
        paramParcel2.writeString("com.android.internal.app.IVoiceInteractorRequest");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractorRequest");
      cancel();
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements IVoiceInteractorRequest
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
      
      public void cancel()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractorRequest");
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.app.IVoiceInteractorRequest";
      }
    }
  }
}
