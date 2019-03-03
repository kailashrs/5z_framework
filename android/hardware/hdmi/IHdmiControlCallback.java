package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IHdmiControlCallback
  extends IInterface
{
  public abstract void onComplete(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IHdmiControlCallback
  {
    private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiControlCallback";
    static final int TRANSACTION_onComplete = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.hdmi.IHdmiControlCallback");
    }
    
    public static IHdmiControlCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.hdmi.IHdmiControlCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IHdmiControlCallback))) {
        return (IHdmiControlCallback)localIInterface;
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
        paramParcel2.writeString("android.hardware.hdmi.IHdmiControlCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlCallback");
      onComplete(paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IHdmiControlCallback
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
        return "android.hardware.hdmi.IHdmiControlCallback";
      }
      
      public void onComplete(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.hdmi.IHdmiControlCallback");
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
