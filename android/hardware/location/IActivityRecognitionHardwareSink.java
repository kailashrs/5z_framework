package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IActivityRecognitionHardwareSink
  extends IInterface
{
  public abstract void onActivityChanged(ActivityChangedEvent paramActivityChangedEvent)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IActivityRecognitionHardwareSink
  {
    private static final String DESCRIPTOR = "android.hardware.location.IActivityRecognitionHardwareSink";
    static final int TRANSACTION_onActivityChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.location.IActivityRecognitionHardwareSink");
    }
    
    public static IActivityRecognitionHardwareSink asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.location.IActivityRecognitionHardwareSink");
      if ((localIInterface != null) && ((localIInterface instanceof IActivityRecognitionHardwareSink))) {
        return (IActivityRecognitionHardwareSink)localIInterface;
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
        paramParcel2.writeString("android.hardware.location.IActivityRecognitionHardwareSink");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.location.IActivityRecognitionHardwareSink");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (ActivityChangedEvent)ActivityChangedEvent.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onActivityChanged(paramParcel1);
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements IActivityRecognitionHardwareSink
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
        return "android.hardware.location.IActivityRecognitionHardwareSink";
      }
      
      public void onActivityChanged(ActivityChangedEvent paramActivityChangedEvent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IActivityRecognitionHardwareSink");
          if (paramActivityChangedEvent != null)
          {
            localParcel1.writeInt(1);
            paramActivityChangedEvent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
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
