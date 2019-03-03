package android.media.tv;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ITvInputHardwareCallback
  extends IInterface
{
  public abstract void onReleased()
    throws RemoteException;
  
  public abstract void onStreamConfigChanged(TvStreamConfig[] paramArrayOfTvStreamConfig)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITvInputHardwareCallback
  {
    private static final String DESCRIPTOR = "android.media.tv.ITvInputHardwareCallback";
    static final int TRANSACTION_onReleased = 1;
    static final int TRANSACTION_onStreamConfigChanged = 2;
    
    public Stub()
    {
      attachInterface(this, "android.media.tv.ITvInputHardwareCallback");
    }
    
    public static ITvInputHardwareCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.tv.ITvInputHardwareCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ITvInputHardwareCallback))) {
        return (ITvInputHardwareCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.media.tv.ITvInputHardwareCallback");
          onStreamConfigChanged((TvStreamConfig[])paramParcel1.createTypedArray(TvStreamConfig.CREATOR));
          return true;
        }
        paramParcel1.enforceInterface("android.media.tv.ITvInputHardwareCallback");
        onReleased();
        return true;
      }
      paramParcel2.writeString("android.media.tv.ITvInputHardwareCallback");
      return true;
    }
    
    private static class Proxy
      implements ITvInputHardwareCallback
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
        return "android.media.tv.ITvInputHardwareCallback";
      }
      
      public void onReleased()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputHardwareCallback");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStreamConfigChanged(TvStreamConfig[] paramArrayOfTvStreamConfig)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputHardwareCallback");
          localParcel.writeTypedArray(paramArrayOfTvStreamConfig, 0);
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
