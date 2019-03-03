package android.hardware.camera2.params;

import android.hardware.camera2.CameraCaptureSession.StateCallback;
import android.hardware.camera2.CaptureRequest;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public final class SessionConfiguration
{
  public static final int SESSION_HIGH_SPEED = 1;
  public static final int SESSION_REGULAR = 0;
  public static final int SESSION_VENDOR_START = 32768;
  private Executor mExecutor = null;
  private InputConfiguration mInputConfig = null;
  private List<OutputConfiguration> mOutputConfigurations;
  private CaptureRequest mSessionParameters = null;
  private int mSessionType;
  private CameraCaptureSession.StateCallback mStateCallback;
  
  public SessionConfiguration(int paramInt, List<OutputConfiguration> paramList, Executor paramExecutor, CameraCaptureSession.StateCallback paramStateCallback)
  {
    mSessionType = paramInt;
    mOutputConfigurations = Collections.unmodifiableList(new ArrayList(paramList));
    mStateCallback = paramStateCallback;
    mExecutor = paramExecutor;
  }
  
  public Executor getExecutor()
  {
    return mExecutor;
  }
  
  public InputConfiguration getInputConfiguration()
  {
    return mInputConfig;
  }
  
  public List<OutputConfiguration> getOutputConfigurations()
  {
    return mOutputConfigurations;
  }
  
  public CaptureRequest getSessionParameters()
  {
    return mSessionParameters;
  }
  
  public int getSessionType()
  {
    return mSessionType;
  }
  
  public CameraCaptureSession.StateCallback getStateCallback()
  {
    return mStateCallback;
  }
  
  public void setInputConfiguration(InputConfiguration paramInputConfiguration)
  {
    if (mSessionType != 1)
    {
      mInputConfig = paramInputConfiguration;
      return;
    }
    throw new UnsupportedOperationException("Method not supported for high speed session types");
  }
  
  public void setSessionParameters(CaptureRequest paramCaptureRequest)
  {
    mSessionParameters = paramCaptureRequest;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SessionMode {}
}
