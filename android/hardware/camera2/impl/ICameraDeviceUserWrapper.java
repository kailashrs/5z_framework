package android.hardware.camera2.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.utils.SubmitInfo;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.view.Surface;

public class ICameraDeviceUserWrapper
{
  private final ICameraDeviceUser mRemoteDevice;
  
  public ICameraDeviceUserWrapper(ICameraDeviceUser paramICameraDeviceUser)
  {
    if (paramICameraDeviceUser != null)
    {
      mRemoteDevice = paramICameraDeviceUser;
      return;
    }
    throw new NullPointerException("Remote device may not be null");
  }
  
  public void beginConfigure()
    throws CameraAccessException
  {
    try
    {
      mRemoteDevice.beginConfigure();
      return;
    }
    catch (Throwable localThrowable)
    {
      CameraManager.throwAsPublicException(localThrowable);
      throw new UnsupportedOperationException("Unexpected exception", localThrowable);
    }
  }
  
  public long cancelRequest(int paramInt)
    throws CameraAccessException
  {
    try
    {
      long l = mRemoteDevice.cancelRequest(paramInt);
      return l;
    }
    catch (Throwable localThrowable)
    {
      CameraManager.throwAsPublicException(localThrowable);
      throw new UnsupportedOperationException("Unexpected exception", localThrowable);
    }
  }
  
  public CameraMetadataNative createDefaultRequest(int paramInt)
    throws CameraAccessException
  {
    try
    {
      CameraMetadataNative localCameraMetadataNative = mRemoteDevice.createDefaultRequest(paramInt);
      return localCameraMetadataNative;
    }
    catch (Throwable localThrowable)
    {
      CameraManager.throwAsPublicException(localThrowable);
      throw new UnsupportedOperationException("Unexpected exception", localThrowable);
    }
  }
  
  public int createInputStream(int paramInt1, int paramInt2, int paramInt3)
    throws CameraAccessException
  {
    try
    {
      paramInt1 = mRemoteDevice.createInputStream(paramInt1, paramInt2, paramInt3);
      return paramInt1;
    }
    catch (Throwable localThrowable)
    {
      CameraManager.throwAsPublicException(localThrowable);
      throw new UnsupportedOperationException("Unexpected exception", localThrowable);
    }
  }
  
  public int createStream(OutputConfiguration paramOutputConfiguration)
    throws CameraAccessException
  {
    try
    {
      int i = mRemoteDevice.createStream(paramOutputConfiguration);
      return i;
    }
    catch (Throwable paramOutputConfiguration)
    {
      CameraManager.throwAsPublicException(paramOutputConfiguration);
      throw new UnsupportedOperationException("Unexpected exception", paramOutputConfiguration);
    }
  }
  
  public void deleteStream(int paramInt)
    throws CameraAccessException
  {
    try
    {
      mRemoteDevice.deleteStream(paramInt);
      return;
    }
    catch (Throwable localThrowable)
    {
      CameraManager.throwAsPublicException(localThrowable);
      throw new UnsupportedOperationException("Unexpected exception", localThrowable);
    }
  }
  
  public void disconnect()
  {
    try
    {
      mRemoteDevice.disconnect();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void endConfigure(int paramInt, CameraMetadataNative paramCameraMetadataNative)
    throws CameraAccessException
  {
    try
    {
      ICameraDeviceUser localICameraDeviceUser = mRemoteDevice;
      if (paramCameraMetadataNative == null)
      {
        paramCameraMetadataNative = new android/hardware/camera2/impl/CameraMetadataNative;
        paramCameraMetadataNative.<init>();
      }
      localICameraDeviceUser.endConfigure(paramInt, paramCameraMetadataNative);
      return;
    }
    catch (Throwable paramCameraMetadataNative)
    {
      CameraManager.throwAsPublicException(paramCameraMetadataNative);
      throw new UnsupportedOperationException("Unexpected exception", paramCameraMetadataNative);
    }
  }
  
  public void finalizeOutputConfigurations(int paramInt, OutputConfiguration paramOutputConfiguration)
    throws CameraAccessException
  {
    try
    {
      mRemoteDevice.finalizeOutputConfigurations(paramInt, paramOutputConfiguration);
      return;
    }
    catch (Throwable paramOutputConfiguration)
    {
      CameraManager.throwAsPublicException(paramOutputConfiguration);
      throw new UnsupportedOperationException("Unexpected exception", paramOutputConfiguration);
    }
  }
  
  public long flush()
    throws CameraAccessException
  {
    try
    {
      long l = mRemoteDevice.flush();
      return l;
    }
    catch (Throwable localThrowable)
    {
      CameraManager.throwAsPublicException(localThrowable);
      throw new UnsupportedOperationException("Unexpected exception", localThrowable);
    }
  }
  
  public CameraMetadataNative getCameraInfo()
    throws CameraAccessException
  {
    try
    {
      CameraMetadataNative localCameraMetadataNative = mRemoteDevice.getCameraInfo();
      return localCameraMetadataNative;
    }
    catch (Throwable localThrowable)
    {
      CameraManager.throwAsPublicException(localThrowable);
      throw new UnsupportedOperationException("Unexpected exception", localThrowable);
    }
  }
  
  public Surface getInputSurface()
    throws CameraAccessException
  {
    try
    {
      Surface localSurface = mRemoteDevice.getInputSurface();
      return localSurface;
    }
    catch (Throwable localThrowable)
    {
      CameraManager.throwAsPublicException(localThrowable);
      throw new UnsupportedOperationException("Unexpected exception", localThrowable);
    }
  }
  
  public void prepare(int paramInt)
    throws CameraAccessException
  {
    try
    {
      mRemoteDevice.prepare(paramInt);
      return;
    }
    catch (Throwable localThrowable)
    {
      CameraManager.throwAsPublicException(localThrowable);
      throw new UnsupportedOperationException("Unexpected exception", localThrowable);
    }
  }
  
  public void prepare2(int paramInt1, int paramInt2)
    throws CameraAccessException
  {
    try
    {
      mRemoteDevice.prepare2(paramInt1, paramInt2);
      return;
    }
    catch (Throwable localThrowable)
    {
      CameraManager.throwAsPublicException(localThrowable);
      throw new UnsupportedOperationException("Unexpected exception", localThrowable);
    }
  }
  
  public SubmitInfo submitRequest(CaptureRequest paramCaptureRequest, boolean paramBoolean)
    throws CameraAccessException
  {
    try
    {
      paramCaptureRequest = mRemoteDevice.submitRequest(paramCaptureRequest, paramBoolean);
      return paramCaptureRequest;
    }
    catch (Throwable paramCaptureRequest)
    {
      CameraManager.throwAsPublicException(paramCaptureRequest);
      throw new UnsupportedOperationException("Unexpected exception", paramCaptureRequest);
    }
  }
  
  public SubmitInfo submitRequestList(CaptureRequest[] paramArrayOfCaptureRequest, boolean paramBoolean)
    throws CameraAccessException
  {
    try
    {
      paramArrayOfCaptureRequest = mRemoteDevice.submitRequestList(paramArrayOfCaptureRequest, paramBoolean);
      return paramArrayOfCaptureRequest;
    }
    catch (Throwable paramArrayOfCaptureRequest)
    {
      CameraManager.throwAsPublicException(paramArrayOfCaptureRequest);
      throw new UnsupportedOperationException("Unexpected exception", paramArrayOfCaptureRequest);
    }
  }
  
  public void tearDown(int paramInt)
    throws CameraAccessException
  {
    try
    {
      mRemoteDevice.tearDown(paramInt);
      return;
    }
    catch (Throwable localThrowable)
    {
      CameraManager.throwAsPublicException(localThrowable);
      throw new UnsupportedOperationException("Unexpected exception", localThrowable);
    }
  }
  
  public void unlinkToDeath(IBinder.DeathRecipient paramDeathRecipient, int paramInt)
  {
    if (mRemoteDevice.asBinder() != null) {
      mRemoteDevice.asBinder().unlinkToDeath(paramDeathRecipient, paramInt);
    }
  }
  
  public void updateOutputConfiguration(int paramInt, OutputConfiguration paramOutputConfiguration)
    throws CameraAccessException
  {
    try
    {
      mRemoteDevice.updateOutputConfiguration(paramInt, paramOutputConfiguration);
      return;
    }
    catch (Throwable paramOutputConfiguration)
    {
      CameraManager.throwAsPublicException(paramOutputConfiguration);
      throw new UnsupportedOperationException("Unexpected exception", paramOutputConfiguration);
    }
  }
  
  public void waitUntilIdle()
    throws CameraAccessException
  {
    try
    {
      mRemoteDevice.waitUntilIdle();
      return;
    }
    catch (Throwable localThrowable)
    {
      CameraManager.throwAsPublicException(localThrowable);
      throw new UnsupportedOperationException("Unexpected exception", localThrowable);
    }
  }
}
