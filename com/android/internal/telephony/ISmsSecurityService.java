package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ISmsSecurityService
  extends IInterface
{
  public abstract boolean register(ISmsSecurityAgent paramISmsSecurityAgent)
    throws RemoteException;
  
  public abstract boolean sendResponse(SmsAuthorizationRequest paramSmsAuthorizationRequest, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean unregister(ISmsSecurityAgent paramISmsSecurityAgent)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISmsSecurityService
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.ISmsSecurityService";
    static final int TRANSACTION_register = 1;
    static final int TRANSACTION_sendResponse = 3;
    static final int TRANSACTION_unregister = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.ISmsSecurityService");
    }
    
    public static ISmsSecurityService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.ISmsSecurityService");
      if ((localIInterface != null) && ((localIInterface instanceof ISmsSecurityService))) {
        return (ISmsSecurityService)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISmsSecurityService");
          SmsAuthorizationRequest localSmsAuthorizationRequest;
          if (paramParcel1.readInt() != 0) {
            localSmsAuthorizationRequest = (SmsAuthorizationRequest)SmsAuthorizationRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localSmsAuthorizationRequest = null;
          }
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          paramInt1 = sendResponse(localSmsAuthorizationRequest, bool);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISmsSecurityService");
          paramInt1 = unregister(ISmsSecurityAgent.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.ISmsSecurityService");
        paramInt1 = register(ISmsSecurityAgent.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.ISmsSecurityService");
      return true;
    }
    
    private static class Proxy
      implements ISmsSecurityService
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
        return "com.android.internal.telephony.ISmsSecurityService";
      }
      
      public boolean register(ISmsSecurityAgent paramISmsSecurityAgent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISmsSecurityService");
          if (paramISmsSecurityAgent != null) {
            paramISmsSecurityAgent = paramISmsSecurityAgent.asBinder();
          } else {
            paramISmsSecurityAgent = null;
          }
          localParcel1.writeStrongBinder(paramISmsSecurityAgent);
          paramISmsSecurityAgent = mRemote;
          boolean bool = false;
          paramISmsSecurityAgent.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean sendResponse(SmsAuthorizationRequest paramSmsAuthorizationRequest, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISmsSecurityService");
          boolean bool = true;
          if (paramSmsAuthorizationRequest != null)
          {
            localParcel1.writeInt(1);
            paramSmsAuthorizationRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean unregister(ISmsSecurityAgent paramISmsSecurityAgent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISmsSecurityService");
          if (paramISmsSecurityAgent != null) {
            paramISmsSecurityAgent = paramISmsSecurityAgent.asBinder();
          } else {
            paramISmsSecurityAgent = null;
          }
          localParcel1.writeStrongBinder(paramISmsSecurityAgent);
          paramISmsSecurityAgent = mRemote;
          boolean bool = false;
          paramISmsSecurityAgent.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
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
