package android.net.captiveportal;

public final class CaptivePortalProbeResult
{
  public static final CaptivePortalProbeResult FAILED = new CaptivePortalProbeResult(599);
  public static final int FAILED_CODE = 599;
  public static final int PORTAL_CODE = 302;
  public static final CaptivePortalProbeResult SUCCESS = new CaptivePortalProbeResult(204);
  public static final int SUCCESS_CODE = 204;
  public final String detectUrl;
  private final int mHttpResponseCode;
  public final CaptivePortalProbeSpec probeSpec;
  public final String redirectUrl;
  
  public CaptivePortalProbeResult(int paramInt)
  {
    this(paramInt, null, null);
  }
  
  public CaptivePortalProbeResult(int paramInt, String paramString1, String paramString2)
  {
    this(paramInt, paramString1, paramString2, null);
  }
  
  public CaptivePortalProbeResult(int paramInt, String paramString1, String paramString2, CaptivePortalProbeSpec paramCaptivePortalProbeSpec)
  {
    mHttpResponseCode = paramInt;
    redirectUrl = paramString1;
    detectUrl = paramString2;
    probeSpec = paramCaptivePortalProbeSpec;
  }
  
  public boolean isFailed()
  {
    boolean bool;
    if ((!isSuccessful()) && (!isPortal())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPortal()
  {
    boolean bool;
    if ((!isSuccessful()) && (mHttpResponseCode >= 200) && (mHttpResponseCode <= 399)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSuccessful()
  {
    boolean bool;
    if (mHttpResponseCode == 204) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
