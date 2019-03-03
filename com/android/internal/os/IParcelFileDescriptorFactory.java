package com.android.internal.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IParcelFileDescriptorFactory
  extends IInterface
{
  public abstract ParcelFileDescriptor open(String paramString, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IParcelFileDescriptorFactory
  {
    private static final String DESCRIPTOR = "com.android.internal.os.IParcelFileDescriptorFactory";
    static final int TRANSACTION_open = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.os.IParcelFileDescriptorFactory");
    }
    
    public static IParcelFileDescriptorFactory asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.os.IParcelFileDescriptorFactory");
      if ((localIInterface != null) && ((localIInterface instanceof IParcelFileDescriptorFactory))) {
        return (IParcelFileDescriptorFactory)localIInterface;
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
        paramParcel2.writeString("com.android.internal.os.IParcelFileDescriptorFactory");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.os.IParcelFileDescriptorFactory");
      paramParcel1 = open(paramParcel1.readString(), paramParcel1.readInt());
      paramParcel2.writeNoException();
      if (paramParcel1 != null)
      {
        paramParcel2.writeInt(1);
        paramParcel1.writeToParcel(paramParcel2, 1);
      }
      else
      {
        paramParcel2.writeInt(0);
      }
      return true;
    }
    
    private static class Proxy
      implements IParcelFileDescriptorFactory
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
        return "com.android.internal.os.IParcelFileDescriptorFactory";
      }
      
      public ParcelFileDescriptor open(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.os.IParcelFileDescriptorFactory");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
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
