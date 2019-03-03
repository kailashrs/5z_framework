package com.android.internal.os;

import android.os.Binder;
import android.os.DropBoxManager.Entry;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IDropBoxManagerService
  extends IInterface
{
  public abstract void add(DropBoxManager.Entry paramEntry)
    throws RemoteException;
  
  public abstract DropBoxManager.Entry getNextEntry(String paramString, long paramLong)
    throws RemoteException;
  
  public abstract boolean isTagEnabled(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDropBoxManagerService
  {
    private static final String DESCRIPTOR = "com.android.internal.os.IDropBoxManagerService";
    static final int TRANSACTION_add = 1;
    static final int TRANSACTION_getNextEntry = 3;
    static final int TRANSACTION_isTagEnabled = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.os.IDropBoxManagerService");
    }
    
    public static IDropBoxManagerService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.os.IDropBoxManagerService");
      if ((localIInterface != null) && ((localIInterface instanceof IDropBoxManagerService))) {
        return (IDropBoxManagerService)localIInterface;
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
          paramParcel1.enforceInterface("com.android.internal.os.IDropBoxManagerService");
          paramParcel1 = getNextEntry(paramParcel1.readString(), paramParcel1.readLong());
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
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.os.IDropBoxManagerService");
          paramInt1 = isTagEnabled(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.os.IDropBoxManagerService");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (DropBoxManager.Entry)DropBoxManager.Entry.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        add(paramParcel1);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.os.IDropBoxManagerService");
      return true;
    }
    
    private static class Proxy
      implements IDropBoxManagerService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void add(DropBoxManager.Entry paramEntry)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.os.IDropBoxManagerService");
          if (paramEntry != null)
          {
            localParcel1.writeInt(1);
            paramEntry.writeToParcel(localParcel1, 0);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.os.IDropBoxManagerService";
      }
      
      public DropBoxManager.Entry getNextEntry(String paramString, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.os.IDropBoxManagerService");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (DropBoxManager.Entry)DropBoxManager.Entry.CREATOR.createFromParcel(localParcel2);
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
      
      public boolean isTagEnabled(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.os.IDropBoxManagerService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(2, localParcel1, localParcel2, 0);
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
