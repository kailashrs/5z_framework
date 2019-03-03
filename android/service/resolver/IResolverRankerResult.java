package android.service.resolver;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IResolverRankerResult
  extends IInterface
{
  public abstract void sendResult(List<ResolverTarget> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IResolverRankerResult
  {
    private static final String DESCRIPTOR = "android.service.resolver.IResolverRankerResult";
    static final int TRANSACTION_sendResult = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.resolver.IResolverRankerResult");
    }
    
    public static IResolverRankerResult asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.resolver.IResolverRankerResult");
      if ((localIInterface != null) && ((localIInterface instanceof IResolverRankerResult))) {
        return (IResolverRankerResult)localIInterface;
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
        paramParcel2.writeString("android.service.resolver.IResolverRankerResult");
        return true;
      }
      paramParcel1.enforceInterface("android.service.resolver.IResolverRankerResult");
      sendResult(paramParcel1.createTypedArrayList(ResolverTarget.CREATOR));
      return true;
    }
    
    private static class Proxy
      implements IResolverRankerResult
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
        return "android.service.resolver.IResolverRankerResult";
      }
      
      public void sendResult(List<ResolverTarget> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.resolver.IResolverRankerResult");
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
