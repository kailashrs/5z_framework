package com.android.internal.telecom;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ICallScreeningAdapter
  extends IInterface
{
  public abstract void allowCall(String paramString)
    throws RemoteException;
  
  public abstract void disallowCall(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICallScreeningAdapter
  {
    private static final String DESCRIPTOR = "com.android.internal.telecom.ICallScreeningAdapter";
    static final int TRANSACTION_allowCall = 1;
    static final int TRANSACTION_disallowCall = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telecom.ICallScreeningAdapter");
    }
    
    public static ICallScreeningAdapter asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telecom.ICallScreeningAdapter");
      if ((localIInterface != null) && ((localIInterface instanceof ICallScreeningAdapter))) {
        return (ICallScreeningAdapter)localIInterface;
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
          paramParcel1.enforceInterface("com.android.internal.telecom.ICallScreeningAdapter");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          boolean bool1 = false;
          boolean bool2;
          if (paramInt1 != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          boolean bool3;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          disallowCall(paramParcel2, bool2, bool3, bool1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telecom.ICallScreeningAdapter");
        allowCall(paramParcel1.readString());
        return true;
      }
      paramParcel2.writeString("com.android.internal.telecom.ICallScreeningAdapter");
      return true;
    }
    
    private static class Proxy
      implements ICallScreeningAdapter
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void allowCall(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.ICallScreeningAdapter");
          localParcel.writeString(paramString);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void disallowCall(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.ICallScreeningAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          localParcel.writeInt(paramBoolean3);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telecom.ICallScreeningAdapter";
      }
    }
  }
}
