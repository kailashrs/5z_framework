package android.telephony.mbms;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class MbmsDownloadSessionCallback
{
  public MbmsDownloadSessionCallback() {}
  
  public void onError(int paramInt, String paramString) {}
  
  public void onFileServicesUpdated(List<FileServiceInfo> paramList) {}
  
  public void onMiddlewareReady() {}
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface DownloadError {}
}
