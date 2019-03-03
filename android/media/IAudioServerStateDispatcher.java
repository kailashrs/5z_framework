package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IAudioServerStateDispatcher
  extends IInterface
{
  public abstract void dispatchAudioServerStateChange(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAudioServerStateDispatcher
  {
    private static final String DESCRIPTOR = "android.media.IAudioServerStateDispatcher";
    static final int TRANSACTION_dispatchAudioServerStateChange = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.IAudioServerStateDispatcher");
    }
    
    public static IAudioServerStateDispatcher asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IAudioServerStateDispatcher");
      if ((localIInterface != null) && ((localIInterface instanceof IAudioServerStateDispatcher))) {
        return (IAudioServerStateDispatcher)localIInterface;
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
        paramParcel2.writeString("android.media.IAudioServerStateDispatcher");
        return true;
      }
      paramParcel1.enforceInterface("android.media.IAudioServerStateDispatcher");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      dispatchAudioServerStateChange(bool);
      return true;
    }
    
    private static class Proxy
      implements IAudioServerStateDispatcher
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
      
      public void dispatchAudioServerStateChange(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioServerStateDispatcher");
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
        return "android.media.IAudioServerStateDispatcher";
      }
    }
  }
}
