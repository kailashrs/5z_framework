package android.media.soundtrigger;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ISoundTriggerDetectionServiceClient
  extends IInterface
{
  public abstract void onOpFinished(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISoundTriggerDetectionServiceClient
  {
    private static final String DESCRIPTOR = "android.media.soundtrigger.ISoundTriggerDetectionServiceClient";
    static final int TRANSACTION_onOpFinished = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.soundtrigger.ISoundTriggerDetectionServiceClient");
    }
    
    public static ISoundTriggerDetectionServiceClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.soundtrigger.ISoundTriggerDetectionServiceClient");
      if ((localIInterface != null) && ((localIInterface instanceof ISoundTriggerDetectionServiceClient))) {
        return (ISoundTriggerDetectionServiceClient)localIInterface;
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
        paramParcel2.writeString("android.media.soundtrigger.ISoundTriggerDetectionServiceClient");
        return true;
      }
      paramParcel1.enforceInterface("android.media.soundtrigger.ISoundTriggerDetectionServiceClient");
      onOpFinished(paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements ISoundTriggerDetectionServiceClient
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
        return "android.media.soundtrigger.ISoundTriggerDetectionServiceClient";
      }
      
      public void onOpFinished(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.soundtrigger.ISoundTriggerDetectionServiceClient");
          localParcel.writeInt(paramInt);
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
