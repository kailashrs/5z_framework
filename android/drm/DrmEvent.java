package android.drm;

import java.util.HashMap;

public class DrmEvent
{
  public static final String DRM_INFO_OBJECT = "drm_info_object";
  public static final String DRM_INFO_STATUS_OBJECT = "drm_info_status_object";
  public static final int TYPE_ALL_RIGHTS_REMOVED = 1001;
  public static final int TYPE_DRM_INFO_PROCESSED = 1002;
  private HashMap<String, Object> mAttributes = new HashMap();
  private String mMessage = "";
  private final int mType;
  private final int mUniqueId;
  
  protected DrmEvent(int paramInt1, int paramInt2, String paramString)
  {
    mUniqueId = paramInt1;
    mType = paramInt2;
    if (paramString != null) {
      mMessage = paramString;
    }
  }
  
  protected DrmEvent(int paramInt1, int paramInt2, String paramString, HashMap<String, Object> paramHashMap)
  {
    mUniqueId = paramInt1;
    mType = paramInt2;
    if (paramString != null) {
      mMessage = paramString;
    }
    if (paramHashMap != null) {
      mAttributes = paramHashMap;
    }
  }
  
  public Object getAttribute(String paramString)
  {
    return mAttributes.get(paramString);
  }
  
  public String getMessage()
  {
    return mMessage;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public int getUniqueId()
  {
    return mUniqueId;
  }
}
