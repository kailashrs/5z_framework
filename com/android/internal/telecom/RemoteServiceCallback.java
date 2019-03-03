package com.android.internal.telecom;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface RemoteServiceCallback
  extends IInterface
{
  public abstract void onError()
    throws RemoteException;
  
  public abstract void onResult(List<ComponentName> paramList, List<IBinder> paramList1)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements RemoteServiceCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.telecom.RemoteServiceCallback";
    static final int TRANSACTION_onError = 1;
    static final int TRANSACTION_onResult = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telecom.RemoteServiceCallback");
    }
    
    public static RemoteServiceCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telecom.RemoteServiceCallback");
      if ((localIInterface != null) && ((localIInterface instanceof RemoteServiceCallback))) {
        return (RemoteServiceCallback)localIInterface;
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
          paramParcel1.enforceInterface("com.android.internal.telecom.RemoteServiceCallback");
          onResult(paramParcel1.createTypedArrayList(ComponentName.CREATOR), paramParcel1.createBinderArrayList());
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telecom.RemoteServiceCallback");
        onError();
        return true;
      }
      paramParcel2.writeString("com.android.internal.telecom.RemoteServiceCallback");
      return true;
    }
    
    private static class Proxy
      implements RemoteServiceCallback
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
        return "com.android.internal.telecom.RemoteServiceCallback";
      }
      
      public void onError()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.RemoteServiceCallback");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onResult(List<ComponentName> paramList, List<IBinder> paramList1)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.RemoteServiceCallback");
          localParcel.writeTypedList(paramList);
          localParcel.writeBinderList(paramList1);
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
