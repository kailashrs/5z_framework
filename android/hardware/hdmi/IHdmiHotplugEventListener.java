package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IHdmiHotplugEventListener
  extends IInterface
{
  public abstract void onReceived(HdmiHotplugEvent paramHdmiHotplugEvent)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IHdmiHotplugEventListener
  {
    private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiHotplugEventListener";
    static final int TRANSACTION_onReceived = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.hdmi.IHdmiHotplugEventListener");
    }
    
    public static IHdmiHotplugEventListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.hdmi.IHdmiHotplugEventListener");
      if ((localIInterface != null) && ((localIInterface instanceof IHdmiHotplugEventListener))) {
        return (IHdmiHotplugEventListener)localIInterface;
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
        paramParcel2.writeString("android.hardware.hdmi.IHdmiHotplugEventListener");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiHotplugEventListener");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (HdmiHotplugEvent)HdmiHotplugEvent.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onReceived(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IHdmiHotplugEventListener
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
        return "android.hardware.hdmi.IHdmiHotplugEventListener";
      }
      
      public void onReceived(HdmiHotplugEvent paramHdmiHotplugEvent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.hdmi.IHdmiHotplugEventListener");
          if (paramHdmiHotplugEvent != null)
          {
            localParcel.writeInt(1);
            paramHdmiHotplugEvent.writeToParcel(localParcel, 0);
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
    }
  }
}
