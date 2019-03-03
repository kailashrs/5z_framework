package com.android.internal.app.procstats;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IProcessStats
  extends IInterface
{
  public abstract int getCurrentMemoryState()
    throws RemoteException;
  
  public abstract byte[] getCurrentStats(List<ParcelFileDescriptor> paramList)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor getStatsOverTime(long paramLong)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IProcessStats
  {
    private static final String DESCRIPTOR = "com.android.internal.app.procstats.IProcessStats";
    static final int TRANSACTION_getCurrentMemoryState = 3;
    static final int TRANSACTION_getCurrentStats = 1;
    static final int TRANSACTION_getStatsOverTime = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.procstats.IProcessStats");
    }
    
    public static IProcessStats asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.procstats.IProcessStats");
      if ((localIInterface != null) && ((localIInterface instanceof IProcessStats))) {
        return (IProcessStats)localIInterface;
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
          paramParcel1.enforceInterface("com.android.internal.app.procstats.IProcessStats");
          paramInt1 = getCurrentMemoryState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.app.procstats.IProcessStats");
          paramParcel1 = getStatsOverTime(paramParcel1.readLong());
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
        paramParcel1.enforceInterface("com.android.internal.app.procstats.IProcessStats");
        paramParcel1 = new ArrayList();
        byte[] arrayOfByte = getCurrentStats(paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeByteArray(arrayOfByte);
        paramParcel2.writeTypedList(paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.app.procstats.IProcessStats");
      return true;
    }
    
    private static class Proxy
      implements IProcessStats
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
      
      public int getCurrentMemoryState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.procstats.IProcessStats");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public byte[] getCurrentStats(List<ParcelFileDescriptor> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.procstats.IProcessStats");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          localParcel2.readTypedList(paramList, ParcelFileDescriptor.CREATOR);
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.app.procstats.IProcessStats";
      }
      
      public ParcelFileDescriptor getStatsOverTime(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.procstats.IProcessStats");
          localParcel1.writeLong(paramLong);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParcelFileDescriptor localParcelFileDescriptor;
          if (localParcel2.readInt() != 0) {
            localParcelFileDescriptor = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            localParcelFileDescriptor = null;
          }
          return localParcelFileDescriptor;
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
