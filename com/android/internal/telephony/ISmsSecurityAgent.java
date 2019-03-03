package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ISmsSecurityAgent
  extends IInterface
{
  public abstract void onAuthorize(SmsAuthorizationRequest paramSmsAuthorizationRequest)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISmsSecurityAgent
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.ISmsSecurityAgent";
    static final int TRANSACTION_onAuthorize = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.ISmsSecurityAgent");
    }
    
    public static ISmsSecurityAgent asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.ISmsSecurityAgent");
      if ((localIInterface != null) && ((localIInterface instanceof ISmsSecurityAgent))) {
        return (ISmsSecurityAgent)localIInterface;
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
        paramParcel2.writeString("com.android.internal.telephony.ISmsSecurityAgent");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.telephony.ISmsSecurityAgent");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (SmsAuthorizationRequest)SmsAuthorizationRequest.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onAuthorize(paramParcel1);
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements ISmsSecurityAgent
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
        return "com.android.internal.telephony.ISmsSecurityAgent";
      }
      
      public void onAuthorize(SmsAuthorizationRequest paramSmsAuthorizationRequest)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISmsSecurityAgent");
          if (paramSmsAuthorizationRequest != null)
          {
            localParcel1.writeInt(1);
            paramSmsAuthorizationRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
