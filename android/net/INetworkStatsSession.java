package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface INetworkStatsSession
  extends IInterface
{
  public abstract void close()
    throws RemoteException;
  
  public abstract NetworkStats getDeviceSummaryForNetwork(NetworkTemplate paramNetworkTemplate, long paramLong1, long paramLong2)
    throws RemoteException;
  
  public abstract NetworkStatsHistory getHistoryForNetwork(NetworkTemplate paramNetworkTemplate, int paramInt)
    throws RemoteException;
  
  public abstract NetworkStatsHistory getHistoryForUid(NetworkTemplate paramNetworkTemplate, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract NetworkStatsHistory getHistoryIntervalForUid(NetworkTemplate paramNetworkTemplate, int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2)
    throws RemoteException;
  
  public abstract int[] getRelevantUids()
    throws RemoteException;
  
  public abstract NetworkStats getSummaryForAllUid(NetworkTemplate paramNetworkTemplate, long paramLong1, long paramLong2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract NetworkStats getSummaryForNetwork(NetworkTemplate paramNetworkTemplate, long paramLong1, long paramLong2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkStatsSession
  {
    private static final String DESCRIPTOR = "android.net.INetworkStatsSession";
    static final int TRANSACTION_close = 8;
    static final int TRANSACTION_getDeviceSummaryForNetwork = 1;
    static final int TRANSACTION_getHistoryForNetwork = 3;
    static final int TRANSACTION_getHistoryForUid = 5;
    static final int TRANSACTION_getHistoryIntervalForUid = 6;
    static final int TRANSACTION_getRelevantUids = 7;
    static final int TRANSACTION_getSummaryForAllUid = 4;
    static final int TRANSACTION_getSummaryForNetwork = 2;
    
    public Stub()
    {
      attachInterface(this, "android.net.INetworkStatsSession");
    }
    
    public static INetworkStatsSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.INetworkStatsSession");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkStatsSession))) {
        return (INetworkStatsSession)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 8: 
          paramParcel1.enforceInterface("android.net.INetworkStatsSession");
          close();
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.INetworkStatsSession");
          paramParcel1 = getRelevantUids();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.INetworkStatsSession");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (NetworkTemplate)NetworkTemplate.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramParcel1 = getHistoryIntervalForUid((NetworkTemplate)localObject6, paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readLong());
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
        case 5: 
          paramParcel1.enforceInterface("android.net.INetworkStatsSession");
          if (paramParcel1.readInt() != 0) {}
          for (localObject6 = (NetworkTemplate)NetworkTemplate.CREATOR.createFromParcel(paramParcel1);; localObject6 = localObject1) {
            break;
          }
          paramParcel1 = getHistoryForUid((NetworkTemplate)localObject6, paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 4: 
          paramParcel1.enforceInterface("android.net.INetworkStatsSession");
          if (paramParcel1.readInt() != 0) {}
          for (localObject6 = (NetworkTemplate)NetworkTemplate.CREATOR.createFromParcel(paramParcel1);; localObject6 = localObject2) {
            break;
          }
          long l1 = paramParcel1.readLong();
          long l2 = paramParcel1.readLong();
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          paramParcel1 = getSummaryForAllUid((NetworkTemplate)localObject6, l1, l2, bool);
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
        case 3: 
          paramParcel1.enforceInterface("android.net.INetworkStatsSession");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (NetworkTemplate)NetworkTemplate.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject3;
          }
          paramParcel1 = getHistoryForNetwork((NetworkTemplate)localObject6, paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.net.INetworkStatsSession");
          if (paramParcel1.readInt() != 0) {}
          for (localObject6 = (NetworkTemplate)NetworkTemplate.CREATOR.createFromParcel(paramParcel1);; localObject6 = localObject4) {
            break;
          }
          paramParcel1 = getSummaryForNetwork((NetworkTemplate)localObject6, paramParcel1.readLong(), paramParcel1.readLong());
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
        paramParcel1.enforceInterface("android.net.INetworkStatsSession");
        if (paramParcel1.readInt() != 0) {}
        for (localObject6 = (NetworkTemplate)NetworkTemplate.CREATOR.createFromParcel(paramParcel1);; localObject6 = localObject5) {
          break;
        }
        paramParcel1 = getDeviceSummaryForNetwork((NetworkTemplate)localObject6, paramParcel1.readLong(), paramParcel1.readLong());
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
      paramParcel2.writeString("android.net.INetworkStatsSession");
      return true;
    }
    
    private static class Proxy
      implements INetworkStatsSession
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
      
      public void close()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsSession");
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStats getDeviceSummaryForNetwork(NetworkTemplate paramNetworkTemplate, long paramLong1, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsSession");
          if (paramNetworkTemplate != null)
          {
            localParcel1.writeInt(1);
            paramNetworkTemplate.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetworkTemplate = (NetworkStats)NetworkStats.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetworkTemplate = null;
          }
          return paramNetworkTemplate;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStatsHistory getHistoryForNetwork(NetworkTemplate paramNetworkTemplate, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsSession");
          if (paramNetworkTemplate != null)
          {
            localParcel1.writeInt(1);
            paramNetworkTemplate.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetworkTemplate = (NetworkStatsHistory)NetworkStatsHistory.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetworkTemplate = null;
          }
          return paramNetworkTemplate;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStatsHistory getHistoryForUid(NetworkTemplate paramNetworkTemplate, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsSession");
          if (paramNetworkTemplate != null)
          {
            localParcel1.writeInt(1);
            paramNetworkTemplate.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetworkTemplate = (NetworkStatsHistory)NetworkStatsHistory.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetworkTemplate = null;
          }
          return paramNetworkTemplate;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStatsHistory getHistoryIntervalForUid(NetworkTemplate paramNetworkTemplate, int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsSession");
          if (paramNetworkTemplate != null)
          {
            localParcel1.writeInt(1);
            paramNetworkTemplate.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetworkTemplate = (NetworkStatsHistory)NetworkStatsHistory.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetworkTemplate = null;
          }
          return paramNetworkTemplate;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.INetworkStatsSession";
      }
      
      public int[] getRelevantUids()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsSession");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStats getSummaryForAllUid(NetworkTemplate paramNetworkTemplate, long paramLong1, long paramLong2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsSession");
          if (paramNetworkTemplate != null)
          {
            localParcel1.writeInt(1);
            paramNetworkTemplate.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetworkTemplate = (NetworkStats)NetworkStats.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetworkTemplate = null;
          }
          return paramNetworkTemplate;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStats getSummaryForNetwork(NetworkTemplate paramNetworkTemplate, long paramLong1, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsSession");
          if (paramNetworkTemplate != null)
          {
            localParcel1.writeInt(1);
            paramNetworkTemplate.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetworkTemplate = (NetworkStats)NetworkStats.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetworkTemplate = null;
          }
          return paramNetworkTemplate;
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
