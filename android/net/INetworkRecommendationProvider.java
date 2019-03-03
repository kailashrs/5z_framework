package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface INetworkRecommendationProvider
  extends IInterface
{
  public abstract void requestScores(NetworkKey[] paramArrayOfNetworkKey)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkRecommendationProvider
  {
    private static final String DESCRIPTOR = "android.net.INetworkRecommendationProvider";
    static final int TRANSACTION_requestScores = 1;
    
    public Stub()
    {
      attachInterface(this, "android.net.INetworkRecommendationProvider");
    }
    
    public static INetworkRecommendationProvider asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.INetworkRecommendationProvider");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkRecommendationProvider))) {
        return (INetworkRecommendationProvider)localIInterface;
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
        paramParcel2.writeString("android.net.INetworkRecommendationProvider");
        return true;
      }
      paramParcel1.enforceInterface("android.net.INetworkRecommendationProvider");
      requestScores((NetworkKey[])paramParcel1.createTypedArray(NetworkKey.CREATOR));
      return true;
    }
    
    private static class Proxy
      implements INetworkRecommendationProvider
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
        return "android.net.INetworkRecommendationProvider";
      }
      
      public void requestScores(NetworkKey[] paramArrayOfNetworkKey)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.INetworkRecommendationProvider");
          localParcel.writeTypedArray(paramArrayOfNetworkKey, 0);
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
