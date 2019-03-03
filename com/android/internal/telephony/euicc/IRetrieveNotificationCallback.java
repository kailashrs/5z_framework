package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.euicc.EuiccNotification;

public abstract interface IRetrieveNotificationCallback
  extends IInterface
{
  public abstract void onComplete(int paramInt, EuiccNotification paramEuiccNotification)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRetrieveNotificationCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IRetrieveNotificationCallback";
    static final int TRANSACTION_onComplete = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.euicc.IRetrieveNotificationCallback");
    }
    
    public static IRetrieveNotificationCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.euicc.IRetrieveNotificationCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IRetrieveNotificationCallback))) {
        return (IRetrieveNotificationCallback)localIInterface;
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
        paramParcel2.writeString("com.android.internal.telephony.euicc.IRetrieveNotificationCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IRetrieveNotificationCallback");
      paramInt1 = paramParcel1.readInt();
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (EuiccNotification)EuiccNotification.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onComplete(paramInt1, paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IRetrieveNotificationCallback
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
        return "com.android.internal.telephony.euicc.IRetrieveNotificationCallback";
      }
      
      public void onComplete(int paramInt, EuiccNotification paramEuiccNotification)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IRetrieveNotificationCallback");
          localParcel.writeInt(paramInt);
          if (paramEuiccNotification != null)
          {
            localParcel.writeInt(1);
            paramEuiccNotification.writeToParcel(localParcel, 0);
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
