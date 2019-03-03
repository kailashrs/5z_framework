package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IHdmiInputChangeListener
  extends IInterface
{
  public abstract void onChanged(HdmiDeviceInfo paramHdmiDeviceInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IHdmiInputChangeListener
  {
    private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiInputChangeListener";
    static final int TRANSACTION_onChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.hdmi.IHdmiInputChangeListener");
    }
    
    public static IHdmiInputChangeListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.hdmi.IHdmiInputChangeListener");
      if ((localIInterface != null) && ((localIInterface instanceof IHdmiInputChangeListener))) {
        return (IHdmiInputChangeListener)localIInterface;
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
        paramParcel2.writeString("android.hardware.hdmi.IHdmiInputChangeListener");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiInputChangeListener");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (HdmiDeviceInfo)HdmiDeviceInfo.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onChanged(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IHdmiInputChangeListener
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
        return "android.hardware.hdmi.IHdmiInputChangeListener";
      }
      
      public void onChanged(HdmiDeviceInfo paramHdmiDeviceInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.hdmi.IHdmiInputChangeListener");
          if (paramHdmiDeviceInfo != null)
          {
            localParcel.writeInt(1);
            paramHdmiDeviceInfo.writeToParcel(localParcel, 0);
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
