package android.telephony.mbms;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StreamingServiceCallback
{
  public static final int SIGNAL_STRENGTH_UNAVAILABLE = -1;
  
  public StreamingServiceCallback() {}
  
  public void onBroadcastSignalStrengthUpdated(int paramInt) {}
  
  public void onError(int paramInt, String paramString) {}
  
  public void onMediaDescriptionUpdated() {}
  
  public void onStreamMethodUpdated(int paramInt) {}
  
  public void onStreamStateUpdated(int paramInt1, int paramInt2) {}
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface StreamingServiceError {}
}
