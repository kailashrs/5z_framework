package android.hardware.camera2;

import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.utils.SubmitInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.Surface;

public abstract interface ICameraDeviceUser
  extends IInterface
{
  public static final int CONSTRAINED_HIGH_SPEED_MODE = 1;
  public static final int NORMAL_MODE = 0;
  public static final int NO_IN_FLIGHT_REPEATING_FRAMES = -1;
  public static final int TEMPLATE_MANUAL = 6;
  public static final int TEMPLATE_PREVIEW = 1;
  public static final int TEMPLATE_RECORD = 3;
  public static final int TEMPLATE_STILL_CAPTURE = 2;
  public static final int TEMPLATE_VIDEO_SNAPSHOT = 4;
  public static final int TEMPLATE_ZERO_SHUTTER_LAG = 5;
  public static final int VENDOR_MODE_START = 32768;
  
  public abstract void beginConfigure()
    throws RemoteException;
  
  public abstract long cancelRequest(int paramInt)
    throws RemoteException;
  
  public abstract CameraMetadataNative createDefaultRequest(int paramInt)
    throws RemoteException;
  
  public abstract int createInputStream(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract int createStream(OutputConfiguration paramOutputConfiguration)
    throws RemoteException;
  
  public abstract void deleteStream(int paramInt)
    throws RemoteException;
  
  public abstract void disconnect()
    throws RemoteException;
  
  public abstract void endConfigure(int paramInt, CameraMetadataNative paramCameraMetadataNative)
    throws RemoteException;
  
  public abstract void finalizeOutputConfigurations(int paramInt, OutputConfiguration paramOutputConfiguration)
    throws RemoteException;
  
  public abstract long flush()
    throws RemoteException;
  
  public abstract CameraMetadataNative getCameraInfo()
    throws RemoteException;
  
  public abstract Surface getInputSurface()
    throws RemoteException;
  
  public abstract void prepare(int paramInt)
    throws RemoteException;
  
  public abstract void prepare2(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract SubmitInfo submitRequest(CaptureRequest paramCaptureRequest, boolean paramBoolean)
    throws RemoteException;
  
  public abstract SubmitInfo submitRequestList(CaptureRequest[] paramArrayOfCaptureRequest, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void tearDown(int paramInt)
    throws RemoteException;
  
  public abstract void updateOutputConfiguration(int paramInt, OutputConfiguration paramOutputConfiguration)
    throws RemoteException;
  
  public abstract void waitUntilIdle()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICameraDeviceUser
  {
    private static final String DESCRIPTOR = "android.hardware.camera2.ICameraDeviceUser";
    static final int TRANSACTION_beginConfigure = 5;
    static final int TRANSACTION_cancelRequest = 4;
    static final int TRANSACTION_createDefaultRequest = 11;
    static final int TRANSACTION_createInputStream = 9;
    static final int TRANSACTION_createStream = 8;
    static final int TRANSACTION_deleteStream = 7;
    static final int TRANSACTION_disconnect = 1;
    static final int TRANSACTION_endConfigure = 6;
    static final int TRANSACTION_finalizeOutputConfigurations = 19;
    static final int TRANSACTION_flush = 14;
    static final int TRANSACTION_getCameraInfo = 12;
    static final int TRANSACTION_getInputSurface = 10;
    static final int TRANSACTION_prepare = 15;
    static final int TRANSACTION_prepare2 = 17;
    static final int TRANSACTION_submitRequest = 2;
    static final int TRANSACTION_submitRequestList = 3;
    static final int TRANSACTION_tearDown = 16;
    static final int TRANSACTION_updateOutputConfiguration = 18;
    static final int TRANSACTION_waitUntilIdle = 13;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.camera2.ICameraDeviceUser");
    }
    
    public static ICameraDeviceUser asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.camera2.ICameraDeviceUser");
      if ((localIInterface != null) && ((localIInterface instanceof ICameraDeviceUser))) {
        return (ICameraDeviceUser)localIInterface;
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
        long l;
        boolean bool;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 19: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (OutputConfiguration)OutputConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject5;
          }
          finalizeOutputConfigurations(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (OutputConfiguration)OutputConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          updateOutputConfiguration(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          prepare2(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          tearDown(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          prepare(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          l = flush();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          waitUntilIdle();
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          paramParcel1 = getCameraInfo();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          paramParcel1 = createDefaultRequest(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          paramParcel1 = getInputSurface();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          paramInt1 = createInputStream(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (OutputConfiguration)OutputConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramInt1 = createStream(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          deleteStream(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CameraMetadataNative)CameraMetadataNative.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          endConfigure(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          beginConfigure();
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          l = cancelRequest(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          localObject5 = (CaptureRequest[])paramParcel1.createTypedArray(CaptureRequest.CREATOR);
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          paramParcel1 = submitRequestList((CaptureRequest[])localObject5, bool);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
          if (paramParcel1.readInt() != 0) {
            localObject5 = (CaptureRequest)CaptureRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = localObject4;
          }
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          paramParcel1 = submitRequest((CaptureRequest)localObject5, bool);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceUser");
        disconnect();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.hardware.camera2.ICameraDeviceUser");
      return true;
    }
    
    private static class Proxy
      implements ICameraDeviceUser
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
      
      public void beginConfigure()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long cancelRequest(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          localParcel1.writeInt(paramInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CameraMetadataNative createDefaultRequest(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          localParcel1.writeInt(paramInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          CameraMetadataNative localCameraMetadataNative;
          if (localParcel2.readInt() != 0) {
            localCameraMetadataNative = (CameraMetadataNative)CameraMetadataNative.CREATOR.createFromParcel(localParcel2);
          } else {
            localCameraMetadataNative = null;
          }
          return localCameraMetadataNative;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int createInputStream(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int createStream(OutputConfiguration paramOutputConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          if (paramOutputConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramOutputConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void deleteStream(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          localParcel1.writeInt(paramInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disconnect()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
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
      
      public void endConfigure(int paramInt, CameraMetadataNative paramCameraMetadataNative)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          localParcel1.writeInt(paramInt);
          if (paramCameraMetadataNative != null)
          {
            localParcel1.writeInt(1);
            paramCameraMetadataNative.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void finalizeOutputConfigurations(int paramInt, OutputConfiguration paramOutputConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          localParcel1.writeInt(paramInt);
          if (paramOutputConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramOutputConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long flush()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CameraMetadataNative getCameraInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          CameraMetadataNative localCameraMetadataNative;
          if (localParcel2.readInt() != 0) {
            localCameraMetadataNative = (CameraMetadataNative)CameraMetadataNative.CREATOR.createFromParcel(localParcel2);
          } else {
            localCameraMetadataNative = null;
          }
          return localCameraMetadataNative;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Surface getInputSurface()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Surface localSurface;
          if (localParcel2.readInt() != 0) {
            localSurface = (Surface)Surface.CREATOR.createFromParcel(localParcel2);
          } else {
            localSurface = null;
          }
          return localSurface;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.hardware.camera2.ICameraDeviceUser";
      }
      
      public void prepare(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          localParcel1.writeInt(paramInt);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void prepare2(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SubmitInfo submitRequest(CaptureRequest paramCaptureRequest, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          if (paramCaptureRequest != null)
          {
            localParcel1.writeInt(1);
            paramCaptureRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramCaptureRequest = (SubmitInfo)SubmitInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramCaptureRequest = null;
          }
          return paramCaptureRequest;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SubmitInfo submitRequestList(CaptureRequest[] paramArrayOfCaptureRequest, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          localParcel1.writeTypedArray(paramArrayOfCaptureRequest, 0);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramArrayOfCaptureRequest = (SubmitInfo)SubmitInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramArrayOfCaptureRequest = null;
          }
          return paramArrayOfCaptureRequest;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void tearDown(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          localParcel1.writeInt(paramInt);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateOutputConfiguration(int paramInt, OutputConfiguration paramOutputConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          localParcel1.writeInt(paramInt);
          if (paramOutputConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramOutputConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void waitUntilIdle()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.camera2.ICameraDeviceUser");
          mRemote.transact(13, localParcel1, localParcel2, 0);
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
