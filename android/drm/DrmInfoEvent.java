package android.drm;

import java.util.HashMap;

public class DrmInfoEvent
  extends DrmEvent
{
  public static final int TYPE_ACCOUNT_ALREADY_REGISTERED = 5;
  public static final int TYPE_ALREADY_REGISTERED_BY_ANOTHER_ACCOUNT = 1;
  public static final int TYPE_REMOVE_RIGHTS = 2;
  public static final int TYPE_RIGHTS_INSTALLED = 3;
  public static final int TYPE_RIGHTS_REMOVED = 6;
  public static final int TYPE_WAIT_FOR_RIGHTS = 4;
  
  public DrmInfoEvent(int paramInt1, int paramInt2, String paramString)
  {
    super(paramInt1, paramInt2, paramString);
    checkTypeValidity(paramInt2);
  }
  
  public DrmInfoEvent(int paramInt1, int paramInt2, String paramString, HashMap<String, Object> paramHashMap)
  {
    super(paramInt1, paramInt2, paramString, paramHashMap);
    checkTypeValidity(paramInt2);
  }
  
  private void checkTypeValidity(int paramInt)
  {
    if (((paramInt < 1) || (paramInt > 6)) && (paramInt != 1001) && (paramInt != 1002))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported type: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
}
