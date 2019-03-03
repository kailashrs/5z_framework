package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface INetworkScoreCache
  extends IInterface
{
  public abstract void clearScores()
    throws RemoteException;
  
  public abstract void updateScores(List<ScoredNetwork> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkScoreCache
  {
    private static final String DESCRIPTOR = "android.net.INetworkScoreCache";
    static final int TRANSACTION_clearScores = 2;
    static final int TRANSACTION_updateScores = 1;
    
    public Stub()
    {
      attachInterface(this, "android.net.INetworkScoreCache");
    }
    
    public static INetworkScoreCache asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.INetworkScoreCache");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkScoreCache))) {
        return (INetworkScoreCache)localIInterface;
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
          paramParcel1.enforceInterface("android.net.INetworkScoreCache");
          clearScores();
          return true;
        }
        paramParcel1.enforceInterface("android.net.INetworkScoreCache");
        updateScores(paramParcel1.createTypedArrayList(ScoredNetwork.CREATOR));
        return true;
      }
      paramParcel2.writeString("android.net.INetworkScoreCache");
      return true;
    }
    
    private static class Proxy
      implements INetworkScoreCache
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
      
      public void clearScores()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.INetworkScoreCache");
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
        return "android.net.INetworkScoreCache";
      }
      
      public void updateScores(List<ScoredNetwork> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.INetworkScoreCache");
          localParcel.writeTypedList(paramList);
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
