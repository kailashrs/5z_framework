package com.android.internal.util;

import android.content.res.Resources;
import android.os.Build;
import android.os.SystemProperties;

public class ScreenShapeHelper
{
  public ScreenShapeHelper() {}
  
  public static int getWindowOutsetBottomPx(Resources paramResources)
  {
    if (Build.IS_EMULATOR) {
      return SystemProperties.getInt("ro.emu.win_outset_bottom_px", 0);
    }
    return paramResources.getInteger(17694939);
  }
}
