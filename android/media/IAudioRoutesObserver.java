package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IAudioRoutesObserver
  extends IInterface
{
  public abstract void dispatchAudioRoutesChanged(AudioRoutesInfo paramAudioRoutesInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAudioRoutesObserver
  {
    private static final String DESCRIPTOR = "android.media.IAudioRoutesObserver";
    static final int TRANSACTION_dispatchAudioRoutesChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.IAudioRoutesObserver");
    }
    
    public static IAudioRoutesObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IAudioRoutesObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IAudioRoutesObserver))) {
        return (IAudioRoutesObserver)localIInterface;
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
        paramParcel2.writeString("android.media.IAudioRoutesObserver");
        return true;
      }
      paramParcel1.enforceInterface("android.media.IAudioRoutesObserver");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (AudioRoutesInfo)AudioRoutesInfo.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      dispatchAudioRoutesChanged(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IAudioRoutesObserver
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
      
      public void dispatchAudioRoutesChanged(AudioRoutesInfo paramAudioRoutesInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioRoutesObserver");
          if (paramAudioRoutesInfo != null)
          {
            localParcel.writeInt(1);
            paramAudioRoutesInfo.writeToParcel(localParcel, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.media.IAudioRoutesObserver";
      }
    }
  }
}
