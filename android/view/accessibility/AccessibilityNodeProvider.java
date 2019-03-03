package android.view.accessibility;

import android.os.Bundle;
import java.util.List;

public abstract class AccessibilityNodeProvider
{
  public static final int HOST_VIEW_ID = -1;
  
  public AccessibilityNodeProvider() {}
  
  public void addExtraDataToAccessibilityNodeInfo(int paramInt, AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString, Bundle paramBundle) {}
  
  public AccessibilityNodeInfo createAccessibilityNodeInfo(int paramInt)
  {
    return null;
  }
  
  public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String paramString, int paramInt)
  {
    return null;
  }
  
  public AccessibilityNodeInfo findFocus(int paramInt)
  {
    return null;
  }
  
  public boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle)
  {
    return false;
  }
}
