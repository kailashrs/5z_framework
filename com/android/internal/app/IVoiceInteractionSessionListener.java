package com.android.internal.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IVoiceInteractionSessionListener
  extends IInterface
{
  public abstract void onVoiceSessionHidden()
    throws RemoteException;
  
  public abstract void onVoiceSessionShown()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVoiceInteractionSessionListener
  {
    private static final String DESCRIPTOR = "com.android.internal.app.IVoiceInteractionSessionListener";
    static final int TRANSACTION_onVoiceSessionHidden = 2;
    static final int TRANSACTION_onVoiceSessionShown = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.IVoiceInteractionSessionListener");
    }
    
    public static IVoiceInteractionSessionListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.IVoiceInteractionSessionListener");
      if ((localIInterface != null) && ((localIInterface instanceof IVoiceInteractionSessionListener))) {
        return (IVoiceInteractionSessionListener)localIInterface;
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
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionSessionListener");
          onVoiceSessionHidden();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionSessionListener");
        onVoiceSessionShown();
        return true;
      }
      paramParcel2.writeString("com.android.internal.app.IVoiceInteractionSessionListener");
      return true;
    }
    
    private static class Proxy
      implements IVoiceInteractionSessionListener
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
        return "com.android.internal.app.IVoiceInteractionSessionListener";
      }
      
      public void onVoiceSessionHidden()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IVoiceInteractionSessionListener");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVoiceSessionShown()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IVoiceInteractionSessionListener");
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
