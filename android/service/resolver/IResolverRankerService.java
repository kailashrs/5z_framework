package android.service.resolver;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IResolverRankerService
  extends IInterface
{
  public abstract void predict(List<ResolverTarget> paramList, IResolverRankerResult paramIResolverRankerResult)
    throws RemoteException;
  
  public abstract void train(List<ResolverTarget> paramList, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IResolverRankerService
  {
    private static final String DESCRIPTOR = "android.service.resolver.IResolverRankerService";
    static final int TRANSACTION_predict = 1;
    static final int TRANSACTION_train = 2;
    
    public Stub()
    {
      attachInterface(this, "android.service.resolver.IResolverRankerService");
    }
    
    public static IResolverRankerService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.resolver.IResolverRankerService");
      if ((localIInterface != null) && ((localIInterface instanceof IResolverRankerService))) {
        return (IResolverRankerService)localIInterface;
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
          paramParcel1.enforceInterface("android.service.resolver.IResolverRankerService");
          train(paramParcel1.createTypedArrayList(ResolverTarget.CREATOR), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.service.resolver.IResolverRankerService");
        predict(paramParcel1.createTypedArrayList(ResolverTarget.CREATOR), IResolverRankerResult.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.service.resolver.IResolverRankerService");
      return true;
    }
    
    private static class Proxy
      implements IResolverRankerService
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
        return "android.service.resolver.IResolverRankerService";
      }
      
      public void predict(List<ResolverTarget> paramList, IResolverRankerResult paramIResolverRankerResult)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.resolver.IResolverRankerService");
          localParcel.writeTypedList(paramList);
          if (paramIResolverRankerResult != null) {
            paramList = paramIResolverRankerResult.asBinder();
          } else {
            paramList = null;
          }
          localParcel.writeStrongBinder(paramList);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void train(List<ResolverTarget> paramList, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.resolver.IResolverRankerService");
          localParcel.writeTypedList(paramList);
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
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
