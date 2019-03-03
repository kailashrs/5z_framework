package android.server.notification;

import android.app.AppGlobals;
import android.content.pm.IPackageManager;
import android.util.Slog;

public class ZenModeUtils
{
  public static final String TAG = "ZenModeUtils";
  
  public ZenModeUtils() {}
  
  public static boolean checkSystemApp(int paramInt)
  {
    IPackageManager localIPackageManager = AppGlobals.getPackageManager();
    int i = -3;
    try
    {
      int j = localIPackageManager.checkUidSignatures(paramInt, 1000);
      paramInt = j;
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Check signatures of ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" failed, err: ");
      localStringBuilder.append(localException.getMessage());
      Slog.w("ZenModeUtils", localStringBuilder.toString());
      paramInt = i;
    }
    boolean bool;
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean checkSystemApp(String paramString)
  {
    IPackageManager localIPackageManager = AppGlobals.getPackageManager();
    int i = -3;
    try
    {
      int j = localIPackageManager.checkSignatures(paramString, "android");
      i = j;
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Check signatures of ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" failed, err: ");
      localStringBuilder.append(localException.getMessage());
      Slog.w("ZenModeUtils", localStringBuilder.toString());
    }
    boolean bool;
    if (i == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
