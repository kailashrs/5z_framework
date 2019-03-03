package android.drm;

import java.util.HashMap;

public class DrmErrorEvent
  extends DrmEvent
{
  public static final int TYPE_ACQUIRE_DRM_INFO_FAILED = 2008;
  public static final int TYPE_NOT_SUPPORTED = 2003;
  public static final int TYPE_NO_INTERNET_CONNECTION = 2005;
  public static final int TYPE_OUT_OF_MEMORY = 2004;
  public static final int TYPE_PROCESS_DRM_INFO_FAILED = 2006;
  public static final int TYPE_REMOVE_ALL_RIGHTS_FAILED = 2007;
  public static final int TYPE_RIGHTS_NOT_INSTALLED = 2001;
  public static final int TYPE_RIGHTS_RENEWAL_NOT_ALLOWED = 2002;
  
  public DrmErrorEvent(int paramInt1, int paramInt2, String paramString)
  {
    super(paramInt1, paramInt2, paramString);
    checkTypeValidity(paramInt2);
  }
  
  public DrmErrorEvent(int paramInt1, int paramInt2, String paramString, HashMap<String, Object> paramHashMap)
  {
    super(paramInt1, paramInt2, paramString, paramHashMap);
    checkTypeValidity(paramInt2);
  }
  
  private void checkTypeValidity(int paramInt)
  {
    if ((paramInt >= 2001) && (paramInt <= 2008)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unsupported type: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
}
