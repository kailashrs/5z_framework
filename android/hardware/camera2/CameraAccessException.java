package android.hardware.camera2;

import android.util.AndroidException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CameraAccessException
  extends AndroidException
{
  public static final int CAMERA_DEPRECATED_HAL = 1000;
  public static final int CAMERA_DISABLED = 1;
  public static final int CAMERA_DISCONNECTED = 2;
  public static final int CAMERA_ERROR = 3;
  public static final int CAMERA_IN_USE = 4;
  public static final int MAX_CAMERAS_IN_USE = 5;
  private static final long serialVersionUID = 5630338637471475675L;
  private final int mReason;
  
  public CameraAccessException(int paramInt)
  {
    super(getDefaultMessage(paramInt));
    mReason = paramInt;
  }
  
  public CameraAccessException(int paramInt, String paramString)
  {
    super(getCombinedMessage(paramInt, paramString));
    mReason = paramInt;
  }
  
  public CameraAccessException(int paramInt, String paramString, Throwable paramThrowable)
  {
    super(getCombinedMessage(paramInt, paramString), paramThrowable);
    mReason = paramInt;
  }
  
  public CameraAccessException(int paramInt, Throwable paramThrowable)
  {
    super(getDefaultMessage(paramInt), paramThrowable);
    mReason = paramInt;
  }
  
  private static String getCombinedMessage(int paramInt, String paramString)
  {
    return String.format("%s (%d): %s", new Object[] { getProblemString(paramInt), Integer.valueOf(paramInt), paramString });
  }
  
  public static String getDefaultMessage(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 5: 
      return "The system-wide limit for number of open cameras has been reached, and more camera devices cannot be opened until previous instances are closed.";
    case 4: 
      return "The camera device is in use already";
    case 3: 
      return "The camera device is currently in the error state; no further calls to it will succeed.";
    case 2: 
      return "The camera device is removable and has been disconnected from the Android device, or the camera service has shut down the connection due to a higher-priority access request for the camera device.";
    }
    return "The camera is disabled due to a device policy, and cannot be opened.";
  }
  
  private static String getProblemString(int paramInt)
  {
    String str;
    if (paramInt != 1000) {
      switch (paramInt)
      {
      default: 
        str = "<UNKNOWN ERROR>";
        break;
      case 5: 
        str = "MAX_CAMERAS_IN_USE";
        break;
      case 4: 
        str = "CAMERA_IN_USE";
        break;
      case 3: 
        str = "CAMERA_ERROR";
        break;
      case 2: 
        str = "CAMERA_DISCONNECTED";
        break;
      case 1: 
        str = "CAMERA_DISABLED";
        break;
      }
    } else {
      str = "CAMERA_DEPRECATED_HAL";
    }
    return str;
  }
  
  public final int getReason()
  {
    return mReason;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AccessError {}
}
