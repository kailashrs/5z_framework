package android.hardware;

import android.hardware.camera2.ICameraDeviceCallbacks;
import android.hardware.camera2.ICameraDeviceCallbacks.Stub;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.ICameraDeviceUser.Stub;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.params.VendorTagDescriptor;
import android.hardware.camera2.params.VendorTagDescriptorCache;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ICameraService
  extends IInterface
{
  public static final int API_VERSION_1 = 1;
  public static final int API_VERSION_2 = 2;
  public static final int CAMERA_HAL_API_VERSION_UNSPECIFIED = -1;
  public static final int CAMERA_TYPE_ALL = 1;
  public static final int CAMERA_TYPE_BACKWARD_COMPATIBLE = 0;
  public static final int ERROR_ALREADY_EXISTS = 2;
  public static final int ERROR_CAMERA_IN_USE = 7;
  public static final int ERROR_DEPRECATED_HAL = 9;
  public static final int ERROR_DISABLED = 6;
  public static final int ERROR_DISCONNECTED = 4;
  public static final int ERROR_ILLEGAL_ARGUMENT = 3;
  public static final int ERROR_INVALID_OPERATION = 10;
  public static final int ERROR_MAX_CAMERAS_IN_USE = 8;
  public static final int ERROR_PERMISSION_DENIED = 1;
  public static final int ERROR_TIMED_OUT = 5;
  public static final int EVENT_NONE = 0;
  public static final int EVENT_USER_SWITCHED = 1;
  public static final int USE_CALLING_PID = -1;
  public static final int USE_CALLING_UID = -1;
  
  public abstract CameraStatus[] addListener(ICameraServiceListener paramICameraServiceListener)
    throws RemoteException;
  
  public abstract ICamera connect(ICameraClient paramICameraClient, int paramInt1, String paramString, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract ICameraDeviceUser connectDevice(ICameraDeviceCallbacks paramICameraDeviceCallbacks, String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract ICamera connectLegacy(ICameraClient paramICameraClient, int paramInt1, int paramInt2, String paramString, int paramInt3)
    throws RemoteException;
  
  public abstract CameraMetadataNative getCameraCharacteristics(String paramString)
    throws RemoteException;
  
  public abstract CameraInfo getCameraInfo(int paramInt)
    throws RemoteException;
  
  public abstract VendorTagDescriptorCache getCameraVendorTagCache()
    throws RemoteException;
  
  public abstract VendorTagDescriptor getCameraVendorTagDescriptor()
    throws RemoteException;
  
  public abstract String getLegacyParameters(int paramInt)
    throws RemoteException;
  
  public abstract int getNumberOfCameras(int paramInt)
    throws RemoteException;
  
  public abstract void notifySystemEvent(int paramInt, int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract void removeListener(ICameraServiceListener paramICameraServiceListener)
    throws RemoteException;
  
  public abstract void setTorchMode(String paramString, boolean paramBoolean, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean supportsCameraApi(String paramString, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICameraService
  {
    private static final String DESCRIPTOR = "android.hardware.ICameraService";
    static final int TRANSACTION_addListener = 6;
    static final int TRANSACTION_connect = 3;
    static final int TRANSACTION_connectDevice = 4;
    static final int TRANSACTION_connectLegacy = 5;
    static final int TRANSACTION_getCameraCharacteristics = 8;
    static final int TRANSACTION_getCameraInfo = 2;
    static final int TRANSACTION_getCameraVendorTagCache = 10;
    static final int TRANSACTION_getCameraVendorTagDescriptor = 9;
    static final int TRANSACTION_getLegacyParameters = 11;
    static final int TRANSACTION_getNumberOfCameras = 1;
    static final int TRANSACTION_notifySystemEvent = 14;
    static final int TRANSACTION_removeListener = 7;
    static final int TRANSACTION_setTorchMode = 13;
    static final int TRANSACTION_supportsCameraApi = 12;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.ICameraService");
    }
    
    public static ICameraService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.ICameraService");
      if ((localIInterface != null) && ((localIInterface instanceof ICameraService))) {
        return (ICameraService)localIInterface;
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
        ICamera localICamera = null;
        Object localObject1 = null;
        Object localObject2 = null;
        boolean bool = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 14: 
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          notifySystemEvent(paramParcel1.readInt(), paramParcel1.createIntArray());
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          setTorchMode((String)localObject2, bool, paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          paramInt1 = supportsCameraApi(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          paramParcel1 = getLegacyParameters(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          paramParcel1 = getCameraVendorTagCache();
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
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          paramParcel1 = getCameraVendorTagDescriptor();
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
        case 8: 
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          paramParcel1 = getCameraCharacteristics(paramParcel1.readString());
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
        case 7: 
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          removeListener(ICameraServiceListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          paramParcel1 = addListener(ICameraServiceListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          localICamera = connectLegacy(ICameraClient.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject2;
          if (localICamera != null) {
            paramParcel1 = localICamera.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          localObject2 = connectDevice(ICameraDeviceCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = localICamera;
          if (localObject2 != null) {
            paramParcel1 = ((ICameraDeviceUser)localObject2).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          localObject2 = connect(ICameraClient.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = localObject1;
          if (localObject2 != null) {
            paramParcel1 = ((ICamera)localObject2).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.ICameraService");
          paramParcel1 = getCameraInfo(paramParcel1.readInt());
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
        paramParcel1.enforceInterface("android.hardware.ICameraService");
        paramInt1 = getNumberOfCameras(paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.hardware.ICameraService");
      return true;
    }
    
    private static class Proxy
      implements ICameraService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public CameraStatus[] addListener(ICameraServiceListener paramICameraServiceListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          if (paramICameraServiceListener != null) {
            paramICameraServiceListener = paramICameraServiceListener.asBinder();
          } else {
            paramICameraServiceListener = null;
          }
          localParcel1.writeStrongBinder(paramICameraServiceListener);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramICameraServiceListener = (CameraStatus[])localParcel2.createTypedArray(CameraStatus.CREATOR);
          return paramICameraServiceListener;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public ICamera connect(ICameraClient paramICameraClient, int paramInt1, String paramString, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          if (paramICameraClient != null) {
            paramICameraClient = paramICameraClient.asBinder();
          } else {
            paramICameraClient = null;
          }
          localParcel1.writeStrongBinder(paramICameraClient);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramICameraClient = ICamera.Stub.asInterface(localParcel2.readStrongBinder());
          return paramICameraClient;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ICameraDeviceUser connectDevice(ICameraDeviceCallbacks paramICameraDeviceCallbacks, String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          if (paramICameraDeviceCallbacks != null) {
            paramICameraDeviceCallbacks = paramICameraDeviceCallbacks.asBinder();
          } else {
            paramICameraDeviceCallbacks = null;
          }
          localParcel1.writeStrongBinder(paramICameraDeviceCallbacks);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramICameraDeviceCallbacks = ICameraDeviceUser.Stub.asInterface(localParcel2.readStrongBinder());
          return paramICameraDeviceCallbacks;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ICamera connectLegacy(ICameraClient paramICameraClient, int paramInt1, int paramInt2, String paramString, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          if (paramICameraClient != null) {
            paramICameraClient = paramICameraClient.asBinder();
          } else {
            paramICameraClient = null;
          }
          localParcel1.writeStrongBinder(paramICameraClient);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramICameraClient = ICamera.Stub.asInterface(localParcel2.readStrongBinder());
          return paramICameraClient;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CameraMetadataNative getCameraCharacteristics(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          localParcel1.writeString(paramString);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (CameraMetadataNative)CameraMetadataNative.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CameraInfo getCameraInfo(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          CameraInfo localCameraInfo;
          if (localParcel2.readInt() != 0) {
            localCameraInfo = (CameraInfo)CameraInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localCameraInfo = null;
          }
          return localCameraInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public VendorTagDescriptorCache getCameraVendorTagCache()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          VendorTagDescriptorCache localVendorTagDescriptorCache;
          if (localParcel2.readInt() != 0) {
            localVendorTagDescriptorCache = (VendorTagDescriptorCache)VendorTagDescriptorCache.CREATOR.createFromParcel(localParcel2);
          } else {
            localVendorTagDescriptorCache = null;
          }
          return localVendorTagDescriptorCache;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public VendorTagDescriptor getCameraVendorTagDescriptor()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          VendorTagDescriptor localVendorTagDescriptor;
          if (localParcel2.readInt() != 0) {
            localVendorTagDescriptor = (VendorTagDescriptor)VendorTagDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            localVendorTagDescriptor = null;
          }
          return localVendorTagDescriptor;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.hardware.ICameraService";
      }
      
      public String getLegacyParameters(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getNumberOfCameras(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifySystemEvent(int paramInt, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.ICameraService");
          localParcel.writeInt(paramInt);
          localParcel.writeIntArray(paramArrayOfInt);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeListener(ICameraServiceListener paramICameraServiceListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          if (paramICameraServiceListener != null) {
            paramICameraServiceListener = paramICameraServiceListener.asBinder();
          } else {
            paramICameraServiceListener = null;
          }
          localParcel1.writeStrongBinder(paramICameraServiceListener);
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
      
      public void setTorchMode(String paramString, boolean paramBoolean, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public boolean supportsCameraApi(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ICameraService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
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
