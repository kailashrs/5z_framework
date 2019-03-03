package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IRecordingConfigDispatcher
  extends IInterface
{
  public abstract void dispatchRecordingConfigChange(List<AudioRecordingConfiguration> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRecordingConfigDispatcher
  {
    private static final String DESCRIPTOR = "android.media.IRecordingConfigDispatcher";
    static final int TRANSACTION_dispatchRecordingConfigChange = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.IRecordingConfigDispatcher");
    }
    
    public static IRecordingConfigDispatcher asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IRecordingConfigDispatcher");
      if ((localIInterface != null) && ((localIInterface instanceof IRecordingConfigDispatcher))) {
        return (IRecordingConfigDispatcher)localIInterface;
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
        paramParcel2.writeString("android.media.IRecordingConfigDispatcher");
        return true;
      }
      paramParcel1.enforceInterface("android.media.IRecordingConfigDispatcher");
      dispatchRecordingConfigChange(paramParcel1.createTypedArrayList(AudioRecordingConfiguration.CREATOR));
      return true;
    }
    
    private static class Proxy
      implements IRecordingConfigDispatcher
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
      
      public void dispatchRecordingConfigChange(List<AudioRecordingConfiguration> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRecordingConfigDispatcher");
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
        return "android.media.IRecordingConfigDispatcher";
      }
    }
  }
}
