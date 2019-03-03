package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.ImsExternalCallState;
import java.util.List;

public abstract interface IImsExternalCallStateListener
  extends IInterface
{
  public abstract void onImsExternalCallStateUpdate(List<ImsExternalCallState> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsExternalCallStateListener
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsExternalCallStateListener";
    static final int TRANSACTION_onImsExternalCallStateUpdate = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsExternalCallStateListener");
    }
    
    public static IImsExternalCallStateListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsExternalCallStateListener");
      if ((localIInterface != null) && ((localIInterface instanceof IImsExternalCallStateListener))) {
        return (IImsExternalCallStateListener)localIInterface;
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
        paramParcel2.writeString("com.android.ims.internal.IImsExternalCallStateListener");
        return true;
      }
      paramParcel1.enforceInterface("com.android.ims.internal.IImsExternalCallStateListener");
      onImsExternalCallStateUpdate(paramParcel1.createTypedArrayList(ImsExternalCallState.CREATOR));
      return true;
    }
    
    private static class Proxy
      implements IImsExternalCallStateListener
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
        return "com.android.ims.internal.IImsExternalCallStateListener";
      }
      
      public void onImsExternalCallStateUpdate(List<ImsExternalCallState> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsExternalCallStateListener");
          localParcel.writeTypedList(paramList);
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
