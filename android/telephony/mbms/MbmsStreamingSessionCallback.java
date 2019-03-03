package android.telephony.mbms;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class MbmsStreamingSessionCallback
{
  public MbmsStreamingSessionCallback() {}
  
  public void onError(int paramInt, String paramString) {}
  
  public void onMiddlewareReady() {}
  
  public void onStreamingServicesUpdated(List<StreamingServiceInfo> paramList) {}
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface StreamingError {}
}
