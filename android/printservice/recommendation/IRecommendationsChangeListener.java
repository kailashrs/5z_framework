package android.printservice.recommendation;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IRecommendationsChangeListener
  extends IInterface
{
  public abstract void onRecommendationsChanged()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRecommendationsChangeListener
  {
    private static final String DESCRIPTOR = "android.printservice.recommendation.IRecommendationsChangeListener";
    static final int TRANSACTION_onRecommendationsChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.printservice.recommendation.IRecommendationsChangeListener");
    }
    
    public static IRecommendationsChangeListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.printservice.recommendation.IRecommendationsChangeListener");
      if ((localIInterface != null) && ((localIInterface instanceof IRecommendationsChangeListener))) {
        return (IRecommendationsChangeListener)localIInterface;
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
        paramParcel2.writeString("android.printservice.recommendation.IRecommendationsChangeListener");
        return true;
      }
      paramParcel1.enforceInterface("android.printservice.recommendation.IRecommendationsChangeListener");
      onRecommendationsChanged();
      return true;
    }
    
    private static class Proxy
      implements IRecommendationsChangeListener
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
        return "android.printservice.recommendation.IRecommendationsChangeListener";
      }
      
      public void onRecommendationsChanged()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.recommendation.IRecommendationsChangeListener");
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
