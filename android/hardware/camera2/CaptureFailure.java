package android.hardware.camera2;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CaptureFailure
{
  public static final int REASON_ERROR = 0;
  public static final int REASON_FLUSHED = 1;
  private final boolean mDropped;
  private final long mFrameNumber;
  private final int mReason;
  private final CaptureRequest mRequest;
  private final int mSequenceId;
  
  public CaptureFailure(CaptureRequest paramCaptureRequest, int paramInt1, boolean paramBoolean, int paramInt2, long paramLong)
  {
    mRequest = paramCaptureRequest;
    mReason = paramInt1;
    mDropped = paramBoolean;
    mSequenceId = paramInt2;
    mFrameNumber = paramLong;
  }
  
  public long getFrameNumber()
  {
    return mFrameNumber;
  }
  
  public int getReason()
  {
    return mReason;
  }
  
  public CaptureRequest getRequest()
  {
    return mRequest;
  }
  
  public int getSequenceId()
  {
    return mSequenceId;
  }
  
  public boolean wasImageCaptured()
  {
    return mDropped ^ true;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FailureReason {}
}
