package com.android.internal.telecom;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telecom.ParcelableCall;

public abstract interface ICallScreeningService
  extends IInterface
{
  public abstract void screenCall(ICallScreeningAdapter paramICallScreeningAdapter, ParcelableCall paramParcelableCall)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICallScreeningService
  {
    private static final String DESCRIPTOR = "com.android.internal.telecom.ICallScreeningService";
    static final int TRANSACTION_screenCall = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telecom.ICallScreeningService");
    }
    
    public static ICallScreeningService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telecom.ICallScreeningService");
      if ((localIInterface != null) && ((localIInterface instanceof ICallScreeningService))) {
        return (ICallScreeningService)localIInterface;
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
        paramParcel2.writeString("com.android.internal.telecom.ICallScreeningService");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.telecom.ICallScreeningService");
      paramParcel2 = ICallScreeningAdapter.Stub.asInterface(paramParcel1.readStrongBinder());
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (ParcelableCall)ParcelableCall.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      screenCall(paramParcel2, paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements ICallScreeningService
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
        return "com.android.internal.telecom.ICallScreeningService";
      }
      
      public void screenCall(ICallScreeningAdapter paramICallScreeningAdapter, ParcelableCall paramParcelableCall)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.ICallScreeningService");
          if (paramICallScreeningAdapter != null) {
            paramICallScreeningAdapter = paramICallScreeningAdapter.asBinder();
          } else {
            paramICallScreeningAdapter = null;
          }
          localParcel.writeStrongBinder(paramICallScreeningAdapter);
          if (paramParcelableCall != null)
          {
            localParcel.writeInt(1);
            paramParcelableCall.writeToParcel(localParcel, 0);
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
