package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface INetworkScoreService
  extends IInterface
{
  public abstract boolean clearScores()
    throws RemoteException;
  
  public abstract void disableScoring()
    throws RemoteException;
  
  public abstract NetworkScorerAppData getActiveScorer()
    throws RemoteException;
  
  public abstract String getActiveScorerPackage()
    throws RemoteException;
  
  public abstract List<NetworkScorerAppData> getAllValidScorers()
    throws RemoteException;
  
  public abstract boolean isCallerActiveScorer(int paramInt)
    throws RemoteException;
  
  public abstract void registerNetworkScoreCache(int paramInt1, INetworkScoreCache paramINetworkScoreCache, int paramInt2)
    throws RemoteException;
  
  public abstract boolean requestScores(NetworkKey[] paramArrayOfNetworkKey)
    throws RemoteException;
  
  public abstract boolean setActiveScorer(String paramString)
    throws RemoteException;
  
  public abstract void unregisterNetworkScoreCache(int paramInt, INetworkScoreCache paramINetworkScoreCache)
    throws RemoteException;
  
  public abstract boolean updateScores(ScoredNetwork[] paramArrayOfScoredNetwork)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkScoreService
  {
    private static final String DESCRIPTOR = "android.net.INetworkScoreService";
    static final int TRANSACTION_clearScores = 2;
    static final int TRANSACTION_disableScoring = 4;
    static final int TRANSACTION_getActiveScorer = 10;
    static final int TRANSACTION_getActiveScorerPackage = 9;
    static final int TRANSACTION_getAllValidScorers = 11;
    static final int TRANSACTION_isCallerActiveScorer = 8;
    static final int TRANSACTION_registerNetworkScoreCache = 5;
    static final int TRANSACTION_requestScores = 7;
    static final int TRANSACTION_setActiveScorer = 3;
    static final int TRANSACTION_unregisterNetworkScoreCache = 6;
    static final int TRANSACTION_updateScores = 1;
    
    public Stub()
    {
      attachInterface(this, "android.net.INetworkScoreService");
    }
    
    public static INetworkScoreService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.INetworkScoreService");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkScoreService))) {
        return (INetworkScoreService)localIInterface;
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
        case 11: 
          paramParcel1.enforceInterface("android.net.INetworkScoreService");
          paramParcel1 = getAllValidScorers();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.net.INetworkScoreService");
          paramParcel1 = getActiveScorer();
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
        case 9: 
          paramParcel1.enforceInterface("android.net.INetworkScoreService");
          paramParcel1 = getActiveScorerPackage();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.net.INetworkScoreService");
          paramInt1 = isCallerActiveScorer(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.INetworkScoreService");
          paramInt1 = requestScores((NetworkKey[])paramParcel1.createTypedArray(NetworkKey.CREATOR));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.INetworkScoreService");
          unregisterNetworkScoreCache(paramParcel1.readInt(), INetworkScoreCache.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.INetworkScoreService");
          registerNetworkScoreCache(paramParcel1.readInt(), INetworkScoreCache.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.INetworkScoreService");
          disableScoring();
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.INetworkScoreService");
          paramInt1 = setActiveScorer(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.INetworkScoreService");
          paramInt1 = clearScores();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.net.INetworkScoreService");
        paramInt1 = updateScores((ScoredNetwork[])paramParcel1.createTypedArray(ScoredNetwork.CREATOR));
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.net.INetworkScoreService");
      return true;
    }
    
    private static class Proxy
      implements INetworkScoreService
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
      
      public boolean clearScores()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkScoreService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(2, localParcel1, localParcel2, 0);
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
      
      public void disableScoring()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkScoreService");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkScorerAppData getActiveScorer()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkScoreService");
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkScorerAppData localNetworkScorerAppData;
          if (localParcel2.readInt() != 0) {
            localNetworkScorerAppData = (NetworkScorerAppData)NetworkScorerAppData.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetworkScorerAppData = null;
          }
          return localNetworkScorerAppData;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getActiveScorerPackage()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkScoreService");
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<NetworkScorerAppData> getAllValidScorers()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkScoreService");
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(NetworkScorerAppData.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.INetworkScoreService";
      }
      
      public boolean isCallerActiveScorer(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkScoreService");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public void registerNetworkScoreCache(int paramInt1, INetworkScoreCache paramINetworkScoreCache, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkScoreService");
          localParcel1.writeInt(paramInt1);
          if (paramINetworkScoreCache != null) {
            paramINetworkScoreCache = paramINetworkScoreCache.asBinder();
          } else {
            paramINetworkScoreCache = null;
          }
          localParcel1.writeStrongBinder(paramINetworkScoreCache);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean requestScores(NetworkKey[] paramArrayOfNetworkKey)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkScoreService");
          boolean bool = false;
          localParcel1.writeTypedArray(paramArrayOfNetworkKey, 0);
          mRemote.transact(7, localParcel1, localParcel2, 0);
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
      
      public boolean setActiveScorer(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkScoreService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(3, localParcel1, localParcel2, 0);
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
      
      public void unregisterNetworkScoreCache(int paramInt, INetworkScoreCache paramINetworkScoreCache)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkScoreService");
          localParcel1.writeInt(paramInt);
          if (paramINetworkScoreCache != null) {
            paramINetworkScoreCache = paramINetworkScoreCache.asBinder();
          } else {
            paramINetworkScoreCache = null;
          }
          localParcel1.writeStrongBinder(paramINetworkScoreCache);
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
      
      public boolean updateScores(ScoredNetwork[] paramArrayOfScoredNetwork)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkScoreService");
          boolean bool = false;
          localParcel1.writeTypedArray(paramArrayOfScoredNetwork, 0);
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
