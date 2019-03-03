package android.hardware.camera2;

import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CaptureResultExtras;
import android.hardware.camera2.impl.PhysicalCaptureResultInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ICameraDeviceCallbacks
  extends IInterface
{
  public static final int ERROR_CAMERA_BUFFER = 5;
  public static final int ERROR_CAMERA_DEVICE = 1;
  public static final int ERROR_CAMERA_DISABLED = 6;
  public static final int ERROR_CAMERA_DISCONNECTED = 0;
  public static final int ERROR_CAMERA_INVALID_ERROR = -1;
  public static final int ERROR_CAMERA_REQUEST = 3;
  public static final int ERROR_CAMERA_RESULT = 4;
  public static final int ERROR_CAMERA_SERVICE = 2;
  
  public abstract void onCaptureStarted(CaptureResultExtras paramCaptureResultExtras, long paramLong)
    throws RemoteException;
  
  public abstract void onDeviceError(int paramInt, CaptureResultExtras paramCaptureResultExtras)
    throws RemoteException;
  
  public abstract void onDeviceIdle()
    throws RemoteException;
  
  public abstract void onPrepared(int paramInt)
    throws RemoteException;
  
  public abstract void onRepeatingRequestError(long paramLong, int paramInt)
    throws RemoteException;
  
  public abstract void onRequestQueueEmpty()
    throws RemoteException;
  
  public abstract void onResultReceived(CameraMetadataNative paramCameraMetadataNative, CaptureResultExtras paramCaptureResultExtras, PhysicalCaptureResultInfo[] paramArrayOfPhysicalCaptureResultInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICameraDeviceCallbacks
  {
    private static final String DESCRIPTOR = "android.hardware.camera2.ICameraDeviceCallbacks";
    static final int TRANSACTION_onCaptureStarted = 3;
    static final int TRANSACTION_onDeviceError = 1;
    static final int TRANSACTION_onDeviceIdle = 2;
    static final int TRANSACTION_onPrepared = 5;
    static final int TRANSACTION_onRepeatingRequestError = 6;
    static final int TRANSACTION_onRequestQueueEmpty = 7;
    static final int TRANSACTION_onResultReceived = 4;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.camera2.ICameraDeviceCallbacks");
    }
    
    public static ICameraDeviceCallbacks asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.camera2.ICameraDeviceCallbacks");
      if ((localIInterface != null) && ((localIInterface instanceof ICameraDeviceCallbacks))) {
        return (ICameraDeviceCallbacks)localIInterface;
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
        CaptureResultExtras localCaptureResultExtras = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 7: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceCallbacks");
          onRequestQueueEmpty();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceCallbacks");
          onRepeatingRequestError(paramParcel1.readLong(), paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceCallbacks");
          onPrepared(paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceCallbacks");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (CameraMetadataNative)CameraMetadataNative.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localCaptureResultExtras = (CaptureResultExtras)CaptureResultExtras.CREATOR.createFromParcel(paramParcel1);
          }
          onResultReceived(paramParcel2, localCaptureResultExtras, (PhysicalCaptureResultInfo[])paramParcel1.createTypedArray(PhysicalCaptureResultInfo.CREATOR));
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceCallbacks");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (CaptureResultExtras)CaptureResultExtras.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject1;
          }
          onCaptureStarted(paramParcel2, paramParcel1.readLong());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceCallbacks");
          onDeviceIdle();
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.camera2.ICameraDeviceCallbacks");
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (CaptureResultExtras)CaptureResultExtras.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject2;
        }
        onDeviceError(paramInt1, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.hardware.camera2.ICameraDeviceCallbacks");
      return true;
    }
    
    private static class Proxy
      implements ICameraDeviceCallbacks
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
        return "android.hardware.camera2.ICameraDeviceCallbacks";
      }
      
      public void onCaptureStarted(CaptureResultExtras paramCaptureResultExtras, long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.camera2.ICameraDeviceCallbacks");
          if (paramCaptureResultExtras != null)
          {
            localParcel.writeInt(1);
            paramCaptureResultExtras.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeLong(paramLong);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDeviceError(int paramInt, CaptureResultExtras paramCaptureResultExtras)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.camera2.ICameraDeviceCallbacks");
          localParcel.writeInt(paramInt);
          if (paramCaptureResultExtras != null)
          {
            localParcel.writeInt(1);
            paramCaptureResultExtras.writeToParcel(localParcel, 0);
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
      
      public void onDeviceIdle()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.camera2.ICameraDeviceCallbacks");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPrepared(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.camera2.ICameraDeviceCallbacks");
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRepeatingRequestError(long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.camera2.ICameraDeviceCallbacks");
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRequestQueueEmpty()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.camera2.ICameraDeviceCallbacks");
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onResultReceived(CameraMetadataNative paramCameraMetadataNative, CaptureResultExtras paramCaptureResultExtras, PhysicalCaptureResultInfo[] paramArrayOfPhysicalCaptureResultInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.camera2.ICameraDeviceCallbacks");
          if (paramCameraMetadataNative != null)
          {
            localParcel.writeInt(1);
            paramCameraMetadataNative.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramCaptureResultExtras != null)
          {
            localParcel.writeInt(1);
            paramCaptureResultExtras.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeTypedArray(paramArrayOfPhysicalCaptureResultInfo, 0);
          mRemote.transact(4, localParcel, null, 1);
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
