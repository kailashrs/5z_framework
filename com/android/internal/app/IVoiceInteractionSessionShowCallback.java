package com.android.internal.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IVoiceInteractionSessionShowCallback
  extends IInterface
{
  public abstract void onFailed()
    throws RemoteException;
  
  public abstract void onShown()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVoiceInteractionSessionShowCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.app.IVoiceInteractionSessionShowCallback";
    static final int TRANSACTION_onFailed = 1;
    static final int TRANSACTION_onShown = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.IVoiceInteractionSessionShowCallback");
    }
    
    public static IVoiceInteractionSessionShowCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.IVoiceInteractionSessionShowCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IVoiceInteractionSessionShowCallback))) {
        return (IVoiceInteractionSessionShowCallback)localIInterface;
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
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionSessionShowCallback");
          onShown();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionSessionShowCallback");
        onFailed();
        return true;
      }
      paramParcel2.writeString("com.android.internal.app.IVoiceInteractionSessionShowCallback");
      return true;
    }
    
    private static class Proxy
      implements IVoiceInteractionSessionShowCallback
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
        return "com.android.internal.app.IVoiceInteractionSessionShowCallback";
      }
      
      public void onFailed()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IVoiceInteractionSessionShowCallback");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onShown()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IVoiceInteractionSessionShowCallback");
          mRemote.transact(2, localParcel, null, 1);
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
