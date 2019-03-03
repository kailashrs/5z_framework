package android.net.wifi.rtt;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public abstract class RangingResultCallback
{
  public static final int STATUS_CODE_FAIL = 1;
  public static final int STATUS_CODE_FAIL_RTT_NOT_AVAILABLE = 2;
  
  public RangingResultCallback() {}
  
  public abstract void onRangingFailure(int paramInt);
  
  public abstract void onRangingResults(List<RangingResult> paramList);
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RangingOperationStatus {}
}
