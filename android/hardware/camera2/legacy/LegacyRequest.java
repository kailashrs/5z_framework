package android.hardware.camera2.legacy;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.util.Size;
import com.android.internal.util.Preconditions;

public class LegacyRequest
{
  public final CaptureRequest captureRequest;
  public final CameraCharacteristics characteristics;
  public final Camera.Parameters parameters;
  public final Size previewSize;
  
  public LegacyRequest(CameraCharacteristics paramCameraCharacteristics, CaptureRequest paramCaptureRequest, Size paramSize, Camera.Parameters paramParameters)
  {
    characteristics = ((CameraCharacteristics)Preconditions.checkNotNull(paramCameraCharacteristics, "characteristics must not be null"));
    captureRequest = ((CaptureRequest)Preconditions.checkNotNull(paramCaptureRequest, "captureRequest must not be null"));
    previewSize = ((Size)Preconditions.checkNotNull(paramSize, "previewSize must not be null"));
    Preconditions.checkNotNull(paramParameters, "parameters must not be null");
    parameters = Camera.getParametersCopy(paramParameters);
  }
  
  public void setParameters(Camera.Parameters paramParameters)
  {
    Preconditions.checkNotNull(paramParameters, "parameters must not be null");
    parameters.copyFrom(paramParameters);
  }
}
