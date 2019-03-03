package android.content;

import android.app.AppOpsManager;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Process;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PermissionChecker
{
  public static final int PERMISSION_DENIED = -1;
  public static final int PERMISSION_DENIED_APP_OP = -2;
  public static final int PERMISSION_GRANTED = 0;
  
  private PermissionChecker() {}
  
  public static int checkCallingOrSelfPermission(Context paramContext, String paramString)
  {
    String str;
    if (Binder.getCallingPid() == Process.myPid()) {
      str = paramContext.getPackageName();
    } else {
      str = null;
    }
    return checkPermission(paramContext, paramString, Binder.getCallingPid(), Binder.getCallingUid(), str);
  }
  
  public static int checkCallingPermission(Context paramContext, String paramString1, String paramString2)
  {
    if (Binder.getCallingPid() == Process.myPid()) {
      return -1;
    }
    return checkPermission(paramContext, paramString1, Binder.getCallingPid(), Binder.getCallingUid(), paramString2);
  }
  
  public static int checkPermission(Context paramContext, String paramString1, int paramInt1, int paramInt2, String paramString2)
  {
    if (paramContext.checkPermission(paramString1, paramInt1, paramInt2) == -1) {
      return -1;
    }
    AppOpsManager localAppOpsManager = (AppOpsManager)paramContext.getSystemService(AppOpsManager.class);
    String str = AppOpsManager.permissionToOp(paramString1);
    if (str == null) {
      return 0;
    }
    paramString1 = paramString2;
    if (paramString2 == null)
    {
      paramContext = paramContext.getPackageManager().getPackagesForUid(paramInt2);
      if ((paramContext != null) && (paramContext.length > 0)) {
        paramString1 = paramContext[0];
      } else {
        return -1;
      }
    }
    if (localAppOpsManager.noteProxyOpNoThrow(str, paramString1) != 0) {
      return -2;
    }
    return 0;
  }
  
  public static int checkSelfPermission(Context paramContext, String paramString)
  {
    return checkPermission(paramContext, paramString, Process.myPid(), Process.myUid(), paramContext.getPackageName());
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PermissionResult {}
}
