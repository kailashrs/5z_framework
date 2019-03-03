package android.app.usage;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteCallback;
import android.os.RemoteException;
import java.util.List;

public abstract interface ICacheQuotaService
  extends IInterface
{
  public abstract void computeCacheQuotaHints(RemoteCallback paramRemoteCallback, List<CacheQuotaHint> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICacheQuotaService
  {
    private static final String DESCRIPTOR = "android.app.usage.ICacheQuotaService";
    static final int TRANSACTION_computeCacheQuotaHints = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.usage.ICacheQuotaService");
    }
    
    public static ICacheQuotaService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.usage.ICacheQuotaService");
      if ((localIInterface != null) && ((localIInterface instanceof ICacheQuotaService))) {
        return (ICacheQuotaService)localIInterface;
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
        paramParcel2.writeString("android.app.usage.ICacheQuotaService");
        return true;
      }
      paramParcel1.enforceInterface("android.app.usage.ICacheQuotaService");
      if (paramParcel1.readInt() != 0) {
        paramParcel2 = (RemoteCallback)RemoteCallback.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      computeCacheQuotaHints(paramParcel2, paramParcel1.createTypedArrayList(CacheQuotaHint.CREATOR));
      return true;
    }
    
    private static class Proxy
      implements ICacheQuotaService
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
      
      public void computeCacheQuotaHints(RemoteCallback paramRemoteCallback, List<CacheQuotaHint> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.usage.ICacheQuotaService");
          if (paramRemoteCallback != null)
          {
            localParcel.writeInt(1);
            paramRemoteCallback.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeTypedList(paramList);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.usage.ICacheQuotaService";
      }
    }
  }
}
