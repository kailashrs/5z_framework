package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ITetheringStatsProvider
  extends IInterface
{
  public static final int QUOTA_UNLIMITED = -1;
  
  public abstract NetworkStats getTetherStats(int paramInt)
    throws RemoteException;
  
  public abstract void setInterfaceQuota(String paramString, long paramLong)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITetheringStatsProvider
  {
    private static final String DESCRIPTOR = "android.net.ITetheringStatsProvider";
    static final int TRANSACTION_getTetherStats = 1;
    static final int TRANSACTION_setInterfaceQuota = 2;
    
    public Stub()
    {
      attachInterface(this, "android.net.ITetheringStatsProvider");
    }
    
    public static ITetheringStatsProvider asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.ITetheringStatsProvider");
      if ((localIInterface != null) && ((localIInterface instanceof ITetheringStatsProvider))) {
        return (ITetheringStatsProvider)localIInterface;
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
          paramParcel1.enforceInterface("android.net.ITetheringStatsProvider");
          setInterfaceQuota(paramParcel1.readString(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.net.ITetheringStatsProvider");
        paramParcel1 = getTetherStats(paramParcel1.readInt());
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
      paramParcel2.writeString("android.net.ITetheringStatsProvider");
      return true;
    }
    
    private static class Proxy
      implements ITetheringStatsProvider
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
        return "android.net.ITetheringStatsProvider";
      }
      
      public NetworkStats getTetherStats(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.ITetheringStatsProvider");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkStats localNetworkStats;
          if (localParcel2.readInt() != 0) {
            localNetworkStats = (NetworkStats)NetworkStats.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetworkStats = null;
          }
          return localNetworkStats;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setInterfaceQuota(String paramString, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.ITetheringStatsProvider");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
