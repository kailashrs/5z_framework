package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IHdmiMhlVendorCommandListener
  extends IInterface
{
  public abstract void onReceived(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IHdmiMhlVendorCommandListener
  {
    private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiMhlVendorCommandListener";
    static final int TRANSACTION_onReceived = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.hdmi.IHdmiMhlVendorCommandListener");
    }
    
    public static IHdmiMhlVendorCommandListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.hdmi.IHdmiMhlVendorCommandListener");
      if ((localIInterface != null) && ((localIInterface instanceof IHdmiMhlVendorCommandListener))) {
        return (IHdmiMhlVendorCommandListener)localIInterface;
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
        paramParcel2.writeString("android.hardware.hdmi.IHdmiMhlVendorCommandListener");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiMhlVendorCommandListener");
      onReceived(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray());
      return true;
    }
    
    private static class Proxy
      implements IHdmiMhlVendorCommandListener
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
        return "android.hardware.hdmi.IHdmiMhlVendorCommandListener";
      }
      
      public void onReceived(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.hdmi.IHdmiMhlVendorCommandListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeByteArray(paramArrayOfByte);
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
