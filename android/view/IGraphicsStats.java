package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IGraphicsStats
  extends IInterface
{
  public abstract ParcelFileDescriptor requestBufferForProcess(String paramString, IGraphicsStatsCallback paramIGraphicsStatsCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGraphicsStats
  {
    private static final String DESCRIPTOR = "android.view.IGraphicsStats";
    static final int TRANSACTION_requestBufferForProcess = 1;
    
    public Stub()
    {
      attachInterface(this, "android.view.IGraphicsStats");
    }
    
    public static IGraphicsStats asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IGraphicsStats");
      if ((localIInterface != null) && ((localIInterface instanceof IGraphicsStats))) {
        return (IGraphicsStats)localIInterface;
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
        paramParcel2.writeString("android.view.IGraphicsStats");
        return true;
      }
      paramParcel1.enforceInterface("android.view.IGraphicsStats");
      paramParcel1 = requestBufferForProcess(paramParcel1.readString(), IGraphicsStatsCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
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
    }
    
    private static class Proxy
      implements IGraphicsStats
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
        return "android.view.IGraphicsStats";
      }
      
      public ParcelFileDescriptor requestBufferForProcess(String paramString, IGraphicsStatsCallback paramIGraphicsStatsCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IGraphicsStats");
          localParcel1.writeString(paramString);
          Object localObject = null;
          if (paramIGraphicsStatsCallback != null) {
            paramString = paramIGraphicsStatsCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = localObject;
          }
          return paramString;
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
