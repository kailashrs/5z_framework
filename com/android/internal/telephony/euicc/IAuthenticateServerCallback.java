package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IAuthenticateServerCallback
  extends IInterface
{
  public abstract void onComplete(int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAuthenticateServerCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IAuthenticateServerCallback";
    static final int TRANSACTION_onComplete = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.euicc.IAuthenticateServerCallback");
    }
    
    public static IAuthenticateServerCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.euicc.IAuthenticateServerCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IAuthenticateServerCallback))) {
        return (IAuthenticateServerCallback)localIInterface;
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
        paramParcel2.writeString("com.android.internal.telephony.euicc.IAuthenticateServerCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IAuthenticateServerCallback");
      onComplete(paramParcel1.readInt(), paramParcel1.createByteArray());
      return true;
    }
    
    private static class Proxy
      implements IAuthenticateServerCallback
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
        return "com.android.internal.telephony.euicc.IAuthenticateServerCallback";
      }
      
      public void onComplete(int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IAuthenticateServerCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeByteArray(paramArrayOfByte);
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
