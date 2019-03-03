package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface INetworkStatsService
  extends IInterface
{
  public abstract void forceUpdate()
    throws RemoteException;
  
  public abstract void forceUpdateIfaces(Network[] paramArrayOfNetwork)
    throws RemoteException;
  
  public abstract NetworkStats getDataLayerSnapshotForUid(int paramInt)
    throws RemoteException;
  
  public abstract NetworkStats getDetailedUidStats(String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract long getIfaceStats(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract String[] getMobileIfaces()
    throws RemoteException;
  
  public abstract long getTotalStats(int paramInt)
    throws RemoteException;
  
  public abstract long getUidStats(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void incrementOperationCount(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract INetworkStatsSession openSession()
    throws RemoteException;
  
  public abstract INetworkStatsSession openSessionForUsageStats(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract DataUsageRequest registerUsageCallback(String paramString, DataUsageRequest paramDataUsageRequest, Messenger paramMessenger, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void setNetTotalBytesAdjust(String paramString, long paramLong1, long paramLong2)
    throws RemoteException;
  
  public abstract void setNetworkTotalBytesAdjust(long paramLong1, long paramLong2)
    throws RemoteException;
  
  public abstract void setUidNetStatsNegligible(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setUidNetworkStatsNegligible(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void unregisterUsageRequest(DataUsageRequest paramDataUsageRequest)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkStatsService
  {
    private static final String DESCRIPTOR = "android.net.INetworkStatsService";
    static final int TRANSACTION_forceUpdate = 8;
    static final int TRANSACTION_forceUpdateIfaces = 7;
    static final int TRANSACTION_getDataLayerSnapshotForUid = 3;
    static final int TRANSACTION_getDetailedUidStats = 4;
    static final int TRANSACTION_getIfaceStats = 12;
    static final int TRANSACTION_getMobileIfaces = 5;
    static final int TRANSACTION_getTotalStats = 13;
    static final int TRANSACTION_getUidStats = 11;
    static final int TRANSACTION_incrementOperationCount = 6;
    static final int TRANSACTION_openSession = 1;
    static final int TRANSACTION_openSessionForUsageStats = 2;
    static final int TRANSACTION_registerUsageCallback = 9;
    static final int TRANSACTION_setNetTotalBytesAdjust = 17;
    static final int TRANSACTION_setNetworkTotalBytesAdjust = 16;
    static final int TRANSACTION_setUidNetStatsNegligible = 15;
    static final int TRANSACTION_setUidNetworkStatsNegligible = 14;
    static final int TRANSACTION_unregisterUsageRequest = 10;
    
    public Stub()
    {
      attachInterface(this, "android.net.INetworkStatsService");
    }
    
    public static INetworkStatsService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.INetworkStatsService");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkStatsService))) {
        return (INetworkStatsService)localIInterface;
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
        String str = null;
        Object localObject3 = null;
        boolean bool1 = false;
        boolean bool2 = false;
        long l;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 17: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          setNetTotalBytesAdjust(paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          setNetworkTotalBytesAdjust(paramParcel1.readLong(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          localObject2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setUidNetStatsNegligible((String)localObject2, paramInt1, bool2);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          paramInt1 = paramParcel1.readInt();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setUidNetworkStatsNegligible(paramInt1, bool2);
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          l = getTotalStats(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          l = getIfaceStats(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          l = getUidStats(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (DataUsageRequest)DataUsageRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          unregisterUsageRequest(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (DataUsageRequest)DataUsageRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Messenger)Messenger.CREATOR.createFromParcel(paramParcel1);
          }
          paramParcel1 = registerUsageCallback(str, (DataUsageRequest)localObject2, (Messenger)localObject1, paramParcel1.readStrongBinder());
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
        case 8: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          forceUpdate();
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          forceUpdateIfaces((Network[])paramParcel1.createTypedArray(Network.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          incrementOperationCount(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          paramParcel1 = getMobileIfaces();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          paramParcel1 = getDetailedUidStats(paramParcel1.createStringArray());
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
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          paramParcel1 = getDataLayerSnapshotForUid(paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.net.INetworkStatsService");
          localObject1 = openSessionForUsageStats(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject2;
          if (localObject1 != null) {
            paramParcel1 = ((INetworkStatsSession)localObject1).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.net.INetworkStatsService");
        localObject2 = openSession();
        paramParcel2.writeNoException();
        paramParcel1 = str;
        if (localObject2 != null) {
          paramParcel1 = ((INetworkStatsSession)localObject2).asBinder();
        }
        paramParcel2.writeStrongBinder(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.net.INetworkStatsService");
      return true;
    }
    
    private static class Proxy
      implements INetworkStatsService
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
      
      public void forceUpdate()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
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
      
      public void forceUpdateIfaces(Network[] paramArrayOfNetwork)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeTypedArray(paramArrayOfNetwork, 0);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStats getDataLayerSnapshotForUid(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
      
      public NetworkStats getDetailedUidStats(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramArrayOfString = (NetworkStats)NetworkStats.CREATOR.createFromParcel(localParcel2);
          } else {
            paramArrayOfString = null;
          }
          return paramArrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getIfaceStats(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.INetworkStatsService";
      }
      
      public String[] getMobileIfaces()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getTotalStats(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getUidStats(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void incrementOperationCount(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public INetworkStatsSession openSession()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          INetworkStatsSession localINetworkStatsSession = INetworkStatsSession.Stub.asInterface(localParcel2.readStrongBinder());
          return localINetworkStatsSession;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public INetworkStatsSession openSessionForUsageStats(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = INetworkStatsSession.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public DataUsageRequest registerUsageCallback(String paramString, DataUsageRequest paramDataUsageRequest, Messenger paramMessenger, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeString(paramString);
          if (paramDataUsageRequest != null)
          {
            localParcel1.writeInt(1);
            paramDataUsageRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramMessenger != null)
          {
            localParcel1.writeInt(1);
            paramMessenger.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (DataUsageRequest)DataUsageRequest.CREATOR.createFromParcel(localParcel2);
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
      
      public void setNetTotalBytesAdjust(String paramString, long paramLong1, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNetworkTotalBytesAdjust(long paramLong1, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUidNetStatsNegligible(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUidNetworkStatsNegligible(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterUsageRequest(DataUsageRequest paramDataUsageRequest)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkStatsService");
          if (paramDataUsageRequest != null)
          {
            localParcel1.writeInt(1);
            paramDataUsageRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
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
