package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IHdmiRecordListener
  extends IInterface
{
  public abstract byte[] getOneTouchRecordSource(int paramInt)
    throws RemoteException;
  
  public abstract void onClearTimerRecordingResult(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onOneTouchRecordResult(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onTimerRecordingResult(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IHdmiRecordListener
  {
    private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiRecordListener";
    static final int TRANSACTION_getOneTouchRecordSource = 1;
    static final int TRANSACTION_onClearTimerRecordingResult = 4;
    static final int TRANSACTION_onOneTouchRecordResult = 2;
    static final int TRANSACTION_onTimerRecordingResult = 3;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.hdmi.IHdmiRecordListener");
    }
    
    public static IHdmiRecordListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.hdmi.IHdmiRecordListener");
      if ((localIInterface != null) && ((localIInterface instanceof IHdmiRecordListener))) {
        return (IHdmiRecordListener)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiRecordListener");
          onClearTimerRecordingResult(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiRecordListener");
          onTimerRecordingResult(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiRecordListener");
          onOneTouchRecordResult(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiRecordListener");
        paramParcel1 = getOneTouchRecordSource(paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeByteArray(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.hardware.hdmi.IHdmiRecordListener");
      return true;
    }
    
    private static class Proxy
      implements IHdmiRecordListener
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
        return "android.hardware.hdmi.IHdmiRecordListener";
      }
      
      public byte[] getOneTouchRecordSource(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiRecordListener");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onClearTimerRecordingResult(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiRecordListener");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onOneTouchRecordResult(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiRecordListener");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void onTimerRecordingResult(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiRecordListener");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
