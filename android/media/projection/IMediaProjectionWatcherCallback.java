package android.media.projection;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IMediaProjectionWatcherCallback
  extends IInterface
{
  public abstract void onStart(MediaProjectionInfo paramMediaProjectionInfo)
    throws RemoteException;
  
  public abstract void onStop(MediaProjectionInfo paramMediaProjectionInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMediaProjectionWatcherCallback
  {
    private static final String DESCRIPTOR = "android.media.projection.IMediaProjectionWatcherCallback";
    static final int TRANSACTION_onStart = 1;
    static final int TRANSACTION_onStop = 2;
    
    public Stub()
    {
      attachInterface(this, "android.media.projection.IMediaProjectionWatcherCallback");
    }
    
    public static IMediaProjectionWatcherCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.projection.IMediaProjectionWatcherCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IMediaProjectionWatcherCallback))) {
        return (IMediaProjectionWatcherCallback)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.media.projection.IMediaProjectionWatcherCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (MediaProjectionInfo)MediaProjectionInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onStop(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.media.projection.IMediaProjectionWatcherCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (MediaProjectionInfo)MediaProjectionInfo.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        onStart(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.media.projection.IMediaProjectionWatcherCallback");
      return true;
    }
    
    private static class Proxy
      implements IMediaProjectionWatcherCallback
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
        return "android.media.projection.IMediaProjectionWatcherCallback";
      }
      
      public void onStart(MediaProjectionInfo paramMediaProjectionInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.projection.IMediaProjectionWatcherCallback");
          if (paramMediaProjectionInfo != null)
          {
            localParcel.writeInt(1);
            paramMediaProjectionInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStop(MediaProjectionInfo paramMediaProjectionInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.projection.IMediaProjectionWatcherCallback");
          if (paramMediaProjectionInfo != null)
          {
            localParcel.writeInt(1);
            paramMediaProjectionInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
