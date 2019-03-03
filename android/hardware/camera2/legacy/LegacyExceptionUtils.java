package android.hardware.camera2.legacy;

import android.os.ServiceSpecificException;
import android.system.OsConstants;
import android.util.AndroidException;

public class LegacyExceptionUtils
{
  public static final int ALREADY_EXISTS = -OsConstants.EEXIST;
  public static final int BAD_VALUE = -OsConstants.EINVAL;
  public static final int DEAD_OBJECT = -OsConstants.ENOSYS;
  public static final int INVALID_OPERATION = -OsConstants.EPIPE;
  public static final int NO_ERROR = 0;
  public static final int PERMISSION_DENIED = -OsConstants.EPERM;
  private static final String TAG = "LegacyExceptionUtils";
  public static final int TIMED_OUT = -OsConstants.ETIMEDOUT;
  
  private LegacyExceptionUtils()
  {
    throw new AssertionError();
  }
  
  public static int throwOnError(int paramInt)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    if (paramInt == 0) {
      return 0;
    }
    if (paramInt != BAD_VALUE)
    {
      if (paramInt >= 0) {
        return paramInt;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown error ");
      localStringBuilder.append(paramInt);
      throw new UnsupportedOperationException(localStringBuilder.toString());
    }
    throw new BufferQueueAbandonedException();
  }
  
  public static void throwOnServiceError(int paramInt)
  {
    if (paramInt >= 0) {
      return;
    }
    Object localObject;
    if (paramInt != PERMISSION_DENIED)
    {
      if (paramInt == ALREADY_EXISTS) {
        return;
      }
      if (paramInt != BAD_VALUE)
      {
        if (paramInt != DEAD_OBJECT)
        {
          if (paramInt != TIMED_OUT)
          {
            if (paramInt != -OsConstants.EACCES)
            {
              if (paramInt != -OsConstants.EBUSY)
              {
                if (paramInt != -OsConstants.EUSERS)
                {
                  if (paramInt != -OsConstants.ENODEV)
                  {
                    if (paramInt != -OsConstants.EOPNOTSUPP)
                    {
                      if (paramInt == INVALID_OPERATION)
                      {
                        paramInt = 10;
                        localObject = "Illegal state encountered in camera service.";
                      }
                      else
                      {
                        int i = 10;
                        localObject = new StringBuilder();
                        ((StringBuilder)localObject).append("Unknown camera device error ");
                        ((StringBuilder)localObject).append(paramInt);
                        localObject = ((StringBuilder)localObject).toString();
                        paramInt = i;
                      }
                    }
                    else
                    {
                      paramInt = 9;
                      localObject = "Deprecated camera HAL does not support this";
                    }
                  }
                  else
                  {
                    paramInt = 4;
                    localObject = "Camera device not available";
                  }
                }
                else
                {
                  paramInt = 8;
                  localObject = "Maximum number of cameras in use";
                }
              }
              else
              {
                paramInt = 7;
                localObject = "Camera already in use";
              }
            }
            else
            {
              paramInt = 6;
              localObject = "Camera disabled by policy";
            }
          }
          else
          {
            paramInt = 10;
            localObject = "Operation timed out in camera service";
          }
        }
        else
        {
          paramInt = 4;
          localObject = "Camera service not available";
        }
      }
      else
      {
        paramInt = 3;
        localObject = "Bad argument passed to camera service";
      }
    }
    else
    {
      paramInt = 1;
      localObject = "Lacking privileges to access camera service";
    }
    throw new ServiceSpecificException(paramInt, (String)localObject);
  }
  
  public static class BufferQueueAbandonedException
    extends AndroidException
  {
    public BufferQueueAbandonedException() {}
    
    public BufferQueueAbandonedException(Exception paramException)
    {
      super();
    }
    
    public BufferQueueAbandonedException(String paramString)
    {
      super();
    }
    
    public BufferQueueAbandonedException(String paramString, Throwable paramThrowable)
    {
      super(paramThrowable);
    }
  }
}
