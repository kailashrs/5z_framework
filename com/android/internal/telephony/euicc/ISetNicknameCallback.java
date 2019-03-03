package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ISetNicknameCallback
  extends IInterface
{
  public abstract void onComplete(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISetNicknameCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.euicc.ISetNicknameCallback";
    static final int TRANSACTION_onComplete = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.euicc.ISetNicknameCallback");
    }
    
    public static ISetNicknameCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.euicc.ISetNicknameCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ISetNicknameCallback))) {
        return (ISetNicknameCallback)localIInterface;
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
        paramParcel2.writeString("com.android.internal.telephony.euicc.ISetNicknameCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.telephony.euicc.ISetNicknameCallback");
      onComplete(paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements ISetNicknameCallback
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
        return "com.android.internal.telephony.euicc.ISetNicknameCallback";
      }
      
      public void onComplete(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.ISetNicknameCallback");
          localParcel.writeInt(paramInt);
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
