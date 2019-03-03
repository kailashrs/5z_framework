package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IHdmiVendorCommandListener
  extends IInterface
{
  public abstract void onControlStateChanged(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void onReceived(int paramInt1, int paramInt2, byte[] paramArrayOfByte, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IHdmiVendorCommandListener
  {
    private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiVendorCommandListener";
    static final int TRANSACTION_onControlStateChanged = 2;
    static final int TRANSACTION_onReceived = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.hdmi.IHdmiVendorCommandListener");
    }
    
    public static IHdmiVendorCommandListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.hdmi.IHdmiVendorCommandListener");
      if ((localIInterface != null) && ((localIInterface instanceof IHdmiVendorCommandListener))) {
        return (IHdmiVendorCommandListener)localIInterface;
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
      if (paramInt1 != 1598968902)
      {
        boolean bool1 = false;
        boolean bool2 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiVendorCommandListener");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onControlStateChanged(bool2, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiVendorCommandListener");
        paramInt1 = paramParcel1.readInt();
        paramInt2 = paramParcel1.readInt();
        byte[] arrayOfByte = paramParcel1.createByteArray();
        bool2 = bool1;
        if (paramParcel1.readInt() != 0) {
          bool2 = true;
        }
        onReceived(paramInt1, paramInt2, arrayOfByte, bool2);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.hardware.hdmi.IHdmiVendorCommandListener");
      return true;
    }
    
    private static class Proxy
      implements IHdmiVendorCommandListener
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
        return "android.hardware.hdmi.IHdmiVendorCommandListener";
      }
      
      public void onControlStateChanged(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiVendorCommandListener");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onReceived(int paramInt1, int paramInt2, byte[] paramArrayOfByte, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiVendorCommandListener");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramBoolean);
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
