package android.hardware.camera2;

import android.annotation.SystemApi;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.os.Handler;
import android.view.Surface;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Set;

public abstract class CameraDevice
  implements AutoCloseable
{
  @SystemApi
  public static final int SESSION_OPERATION_MODE_CONSTRAINED_HIGH_SPEED = 1;
  @SystemApi
  public static final int SESSION_OPERATION_MODE_NORMAL = 0;
  @SystemApi
  public static final int SESSION_OPERATION_MODE_VENDOR_START = 32768;
  public static final int TEMPLATE_MANUAL = 6;
  public static final int TEMPLATE_PREVIEW = 1;
  public static final int TEMPLATE_RECORD = 3;
  public static final int TEMPLATE_STILL_CAPTURE = 2;
  public static final int TEMPLATE_VIDEO_SNAPSHOT = 4;
  public static final int TEMPLATE_ZERO_SHUTTER_LAG = 5;
  
  public CameraDevice() {}
  
  public abstract void close();
  
  public abstract CaptureRequest.Builder createCaptureRequest(int paramInt)
    throws CameraAccessException;
  
  public CaptureRequest.Builder createCaptureRequest(int paramInt, Set<String> paramSet)
    throws CameraAccessException
  {
    throw new UnsupportedOperationException("Subclasses must override this method");
  }
  
  public void createCaptureSession(SessionConfiguration paramSessionConfiguration)
    throws CameraAccessException
  {
    throw new UnsupportedOperationException("No default implementation");
  }
  
  public abstract void createCaptureSession(List<Surface> paramList, CameraCaptureSession.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException;
  
  public abstract void createCaptureSessionByOutputConfigurations(List<OutputConfiguration> paramList, CameraCaptureSession.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException;
  
  public abstract void createConstrainedHighSpeedCaptureSession(List<Surface> paramList, CameraCaptureSession.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException;
  
  @SystemApi
  public abstract void createCustomCaptureSession(InputConfiguration paramInputConfiguration, List<OutputConfiguration> paramList, int paramInt, CameraCaptureSession.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException;
  
  public abstract CaptureRequest.Builder createReprocessCaptureRequest(TotalCaptureResult paramTotalCaptureResult)
    throws CameraAccessException;
  
  public abstract void createReprocessableCaptureSession(InputConfiguration paramInputConfiguration, List<Surface> paramList, CameraCaptureSession.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException;
  
  public abstract void createReprocessableCaptureSessionByConfigurations(InputConfiguration paramInputConfiguration, List<OutputConfiguration> paramList, CameraCaptureSession.StateCallback paramStateCallback, Handler paramHandler)
    throws CameraAccessException;
  
  public abstract String getId();
  
  public abstract void setVendorStreamConfigMode(int paramInt)
    throws CameraAccessException;
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RequestTemplate {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SessionOperatingMode {}
  
  public static abstract class StateCallback
  {
    public static final int ERROR_CAMERA_DEVICE = 4;
    public static final int ERROR_CAMERA_DISABLED = 3;
    public static final int ERROR_CAMERA_IN_USE = 1;
    public static final int ERROR_CAMERA_SERVICE = 5;
    public static final int ERROR_MAX_CAMERAS_IN_USE = 2;
    
    public StateCallback() {}
    
    public void onClosed(CameraDevice paramCameraDevice) {}
    
    public abstract void onDisconnected(CameraDevice paramCameraDevice);
    
    public abstract void onError(CameraDevice paramCameraDevice, int paramInt);
    
    public abstract void onOpened(CameraDevice paramCameraDevice);
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface ErrorCode {}
  }
}
