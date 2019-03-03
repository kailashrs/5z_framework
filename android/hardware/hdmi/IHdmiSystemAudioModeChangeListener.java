package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IHdmiSystemAudioModeChangeListener
  extends IInterface
{
  public abstract void onStatusChanged(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IHdmiSystemAudioModeChangeListener
  {
    private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiSystemAudioModeChangeListener";
    static final int TRANSACTION_onStatusChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.hdmi.IHdmiSystemAudioModeChangeListener");
    }
    
    public static IHdmiSystemAudioModeChangeListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.hdmi.IHdmiSystemAudioModeChangeListener");
      if ((localIInterface != null) && ((localIInterface instanceof IHdmiSystemAudioModeChangeListener))) {
        return (IHdmiSystemAudioModeChangeListener)localIInterface;
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
        paramParcel2.writeString("android.hardware.hdmi.IHdmiSystemAudioModeChangeListener");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiSystemAudioModeChangeListener");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onStatusChanged(bool);
      return true;
    }
    
    private static class Proxy
      implements IHdmiSystemAudioModeChangeListener
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
        return "android.hardware.hdmi.IHdmiSystemAudioModeChangeListener";
      }
      
      public void onStatusChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.hdmi.IHdmiSystemAudioModeChangeListener");
          localParcel.writeInt(paramBoolean);
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
