package com.android.internal.telephony;

import android.content.ContentValues;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IApnSourceService
  extends IInterface
{
  public abstract ContentValues[] getApns()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IApnSourceService
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.IApnSourceService";
    static final int TRANSACTION_getApns = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.IApnSourceService");
    }
    
    public static IApnSourceService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.IApnSourceService");
      if ((localIInterface != null) && ((localIInterface instanceof IApnSourceService))) {
        return (IApnSourceService)localIInterface;
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
        paramParcel2.writeString("com.android.internal.telephony.IApnSourceService");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.telephony.IApnSourceService");
      paramParcel1 = getApns();
      paramParcel2.writeNoException();
      paramParcel2.writeTypedArray(paramParcel1, 1);
      return true;
    }
    
    private static class Proxy
      implements IApnSourceService
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
      
      public ContentValues[] getApns()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IApnSourceService");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ContentValues[] arrayOfContentValues = (ContentValues[])localParcel2.createTypedArray(ContentValues.CREATOR);
          return arrayOfContentValues;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telephony.IApnSourceService";
      }
    }
  }
}
