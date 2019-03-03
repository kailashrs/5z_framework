package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IHdmiDeviceEventListener
  extends IInterface
{
  public abstract void onStatusChanged(HdmiDeviceInfo paramHdmiDeviceInfo, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IHdmiDeviceEventListener
  {
    private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiDeviceEventListener";
    static final int TRANSACTION_onStatusChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.hdmi.IHdmiDeviceEventListener");
    }
    
    public static IHdmiDeviceEventListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.hdmi.IHdmiDeviceEventListener");
      if ((localIInterface != null) && ((localIInterface instanceof IHdmiDeviceEventListener))) {
        return (IHdmiDeviceEventListener)localIInterface;
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
        paramParcel2.writeString("android.hardware.hdmi.IHdmiDeviceEventListener");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiDeviceEventListener");
      if (paramParcel1.readInt() != 0) {
        paramParcel2 = (HdmiDeviceInfo)HdmiDeviceInfo.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      onStatusChanged(paramParcel2, paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IHdmiDeviceEventListener
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
        return "android.hardware.hdmi.IHdmiDeviceEventListener";
      }
      
      public void onStatusChanged(HdmiDeviceInfo paramHdmiDeviceInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.hdmi.IHdmiDeviceEventListener");
          if (paramHdmiDeviceInfo != null)
          {
            localParcel.writeInt(1);
            paramHdmiDeviceInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
