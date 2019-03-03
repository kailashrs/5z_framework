package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IPlaybackConfigDispatcher
  extends IInterface
{
  public abstract void dispatchPlaybackConfigChange(List<AudioPlaybackConfiguration> paramList, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPlaybackConfigDispatcher
  {
    private static final String DESCRIPTOR = "android.media.IPlaybackConfigDispatcher";
    static final int TRANSACTION_dispatchPlaybackConfigChange = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.IPlaybackConfigDispatcher");
    }
    
    public static IPlaybackConfigDispatcher asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IPlaybackConfigDispatcher");
      if ((localIInterface != null) && ((localIInterface instanceof IPlaybackConfigDispatcher))) {
        return (IPlaybackConfigDispatcher)localIInterface;
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
        paramParcel2.writeString("android.media.IPlaybackConfigDispatcher");
        return true;
      }
      paramParcel1.enforceInterface("android.media.IPlaybackConfigDispatcher");
      paramParcel2 = paramParcel1.createTypedArrayList(AudioPlaybackConfiguration.CREATOR);
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      dispatchPlaybackConfigChange(paramParcel2, bool);
      return true;
    }
    
    private static class Proxy
      implements IPlaybackConfigDispatcher
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
      
      public void dispatchPlaybackConfigChange(List<AudioPlaybackConfiguration> paramList, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IPlaybackConfigDispatcher");
          localParcel.writeTypedList(paramList);
          localParcel.writeInt(paramBoolean);
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
        return "android.media.IPlaybackConfigDispatcher";
      }
    }
  }
}
