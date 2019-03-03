package android.hardware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ICameraClient
  extends IInterface
{
  public static abstract class Stub
    extends Binder
    implements ICameraClient
  {
    private static final String DESCRIPTOR = "android.hardware.ICameraClient";
    
    public Stub()
    {
      attachInterface(this, "android.hardware.ICameraClient");
    }
    
    public static ICameraClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.ICameraClient");
      if ((localIInterface != null) && ((localIInterface instanceof ICameraClient))) {
        return (ICameraClient)localIInterface;
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
      if (paramInt1 != 1598968902) {
        return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      }
      paramParcel2.writeString("android.hardware.ICameraClient");
      return true;
    }
    
    private static class Proxy
      implements ICameraClient
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
        return "android.hardware.ICameraClient";
      }
    }
  }
}
