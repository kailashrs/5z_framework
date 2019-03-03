package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.service.euicc.EuiccProfileInfo;

public abstract interface IGetProfileCallback
  extends IInterface
{
  public abstract void onComplete(int paramInt, EuiccProfileInfo paramEuiccProfileInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGetProfileCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IGetProfileCallback";
    static final int TRANSACTION_onComplete = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.euicc.IGetProfileCallback");
    }
    
    public static IGetProfileCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.euicc.IGetProfileCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IGetProfileCallback))) {
        return (IGetProfileCallback)localIInterface;
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
        paramParcel2.writeString("com.android.internal.telephony.euicc.IGetProfileCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IGetProfileCallback");
      paramInt1 = paramParcel1.readInt();
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (EuiccProfileInfo)EuiccProfileInfo.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onComplete(paramInt1, paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IGetProfileCallback
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
        return "com.android.internal.telephony.euicc.IGetProfileCallback";
      }
      
      public void onComplete(int paramInt, EuiccProfileInfo paramEuiccProfileInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IGetProfileCallback");
          localParcel.writeInt(paramInt);
          if (paramEuiccProfileInfo != null)
          {
            localParcel.writeInt(1);
            paramEuiccProfileInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
