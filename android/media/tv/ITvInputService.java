package android.media.tv;

import android.hardware.hdmi.HdmiDeviceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.InputChannel;

public abstract interface ITvInputService
  extends IInterface
{
  public abstract void createRecordingSession(ITvInputSessionCallback paramITvInputSessionCallback, String paramString)
    throws RemoteException;
  
  public abstract void createSession(InputChannel paramInputChannel, ITvInputSessionCallback paramITvInputSessionCallback, String paramString)
    throws RemoteException;
  
  public abstract void notifyHardwareAdded(TvInputHardwareInfo paramTvInputHardwareInfo)
    throws RemoteException;
  
  public abstract void notifyHardwareRemoved(TvInputHardwareInfo paramTvInputHardwareInfo)
    throws RemoteException;
  
  public abstract void notifyHdmiDeviceAdded(HdmiDeviceInfo paramHdmiDeviceInfo)
    throws RemoteException;
  
  public abstract void notifyHdmiDeviceRemoved(HdmiDeviceInfo paramHdmiDeviceInfo)
    throws RemoteException;
  
  public abstract void registerCallback(ITvInputServiceCallback paramITvInputServiceCallback)
    throws RemoteException;
  
  public abstract void unregisterCallback(ITvInputServiceCallback paramITvInputServiceCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITvInputService
  {
    private static final String DESCRIPTOR = "android.media.tv.ITvInputService";
    static final int TRANSACTION_createRecordingSession = 4;
    static final int TRANSACTION_createSession = 3;
    static final int TRANSACTION_notifyHardwareAdded = 5;
    static final int TRANSACTION_notifyHardwareRemoved = 6;
    static final int TRANSACTION_notifyHdmiDeviceAdded = 7;
    static final int TRANSACTION_notifyHdmiDeviceRemoved = 8;
    static final int TRANSACTION_registerCallback = 1;
    static final int TRANSACTION_unregisterCallback = 2;
    
    public Stub()
    {
      attachInterface(this, "android.media.tv.ITvInputService");
    }
    
    public static ITvInputService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.tv.ITvInputService");
      if ((localIInterface != null) && ((localIInterface instanceof ITvInputService))) {
        return (ITvInputService)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 8: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (HdmiDeviceInfo)HdmiDeviceInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          notifyHdmiDeviceRemoved(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (HdmiDeviceInfo)HdmiDeviceInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          notifyHdmiDeviceAdded(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (TvInputHardwareInfo)TvInputHardwareInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          notifyHardwareRemoved(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (TvInputHardwareInfo)TvInputHardwareInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          notifyHardwareAdded(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputService");
          createRecordingSession(ITvInputSessionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (InputChannel)InputChannel.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject4;
          }
          createSession(paramParcel2, ITvInputSessionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputService");
          unregisterCallback(ITvInputServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.media.tv.ITvInputService");
        registerCallback(ITvInputServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.media.tv.ITvInputService");
      return true;
    }
    
    private static class Proxy
      implements ITvInputService
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
      
      public void createRecordingSession(ITvInputSessionCallback paramITvInputSessionCallback, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputService");
          if (paramITvInputSessionCallback != null) {
            paramITvInputSessionCallback = paramITvInputSessionCallback.asBinder();
          } else {
            paramITvInputSessionCallback = null;
          }
          localParcel.writeStrongBinder(paramITvInputSessionCallback);
          localParcel.writeString(paramString);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void createSession(InputChannel paramInputChannel, ITvInputSessionCallback paramITvInputSessionCallback, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputService");
          if (paramInputChannel != null)
          {
            localParcel.writeInt(1);
            paramInputChannel.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramITvInputSessionCallback != null) {
            paramInputChannel = paramITvInputSessionCallback.asBinder();
          } else {
            paramInputChannel = null;
          }
          localParcel.writeStrongBinder(paramInputChannel);
          localParcel.writeString(paramString);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.tv.ITvInputService";
      }
      
      public void notifyHardwareAdded(TvInputHardwareInfo paramTvInputHardwareInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputService");
          if (paramTvInputHardwareInfo != null)
          {
            localParcel.writeInt(1);
            paramTvInputHardwareInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void notifyHardwareRemoved(TvInputHardwareInfo paramTvInputHardwareInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputService");
          if (paramTvInputHardwareInfo != null)
          {
            localParcel.writeInt(1);
            paramTvInputHardwareInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void notifyHdmiDeviceAdded(HdmiDeviceInfo paramHdmiDeviceInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputService");
          if (paramHdmiDeviceInfo != null)
          {
            localParcel.writeInt(1);
            paramHdmiDeviceInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void notifyHdmiDeviceRemoved(HdmiDeviceInfo paramHdmiDeviceInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputService");
          if (paramHdmiDeviceInfo != null)
          {
            localParcel.writeInt(1);
            paramHdmiDeviceInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registerCallback(ITvInputServiceCallback paramITvInputServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputService");
          if (paramITvInputServiceCallback != null) {
            paramITvInputServiceCallback = paramITvInputServiceCallback.asBinder();
          } else {
            paramITvInputServiceCallback = null;
          }
          localParcel.writeStrongBinder(paramITvInputServiceCallback);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unregisterCallback(ITvInputServiceCallback paramITvInputServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputService");
          if (paramITvInputServiceCallback != null) {
            paramITvInputServiceCallback = paramITvInputServiceCallback.asBinder();
          } else {
            paramITvInputServiceCallback = null;
          }
          localParcel.writeStrongBinder(paramITvInputServiceCallback);
          mRemote.transact(2, localParcel, null, 1);
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
