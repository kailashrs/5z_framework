package org.codeaurora.ims.utils;

import android.content.Context;
import android.provider.Settings.Global;

public class QtiCallUtils
{
  private QtiCallUtils() {}
  
  public static boolean isCsRetryEnabledByUser(Context paramContext)
  {
    paramContext = paramContext.getContentResolver();
    boolean bool = true;
    if (Settings.Global.getInt(paramContext, "qti.settings.cs_retry", 1) != 1) {
      bool = false;
    }
    return bool;
  }
}
