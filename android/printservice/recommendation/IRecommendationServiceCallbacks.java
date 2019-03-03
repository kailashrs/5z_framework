package android.printservice.recommendation;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IRecommendationServiceCallbacks
  extends IInterface
{
  public abstract void onRecommendationsUpdated(List<RecommendationInfo> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRecommendationServiceCallbacks
  {
    private static final String DESCRIPTOR = "android.printservice.recommendation.IRecommendationServiceCallbacks";
    static final int TRANSACTION_onRecommendationsUpdated = 1;
    
    public Stub()
    {
      attachInterface(this, "android.printservice.recommendation.IRecommendationServiceCallbacks");
    }
    
    public static IRecommendationServiceCallbacks asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.printservice.recommendation.IRecommendationServiceCallbacks");
      if ((localIInterface != null) && ((localIInterface instanceof IRecommendationServiceCallbacks))) {
        return (IRecommendationServiceCallbacks)localIInterface;
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
        paramParcel2.writeString("android.printservice.recommendation.IRecommendationServiceCallbacks");
        return true;
      }
      paramParcel1.enforceInterface("android.printservice.recommendation.IRecommendationServiceCallbacks");
      onRecommendationsUpdated(paramParcel1.createTypedArrayList(RecommendationInfo.CREATOR));
      return true;
    }
    
    private static class Proxy
      implements IRecommendationServiceCallbacks
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
        return "android.printservice.recommendation.IRecommendationServiceCallbacks";
      }
      
      public void onRecommendationsUpdated(List<RecommendationInfo> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.recommendation.IRecommendationServiceCallbacks");
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
