package android.telephony;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.UserInfo;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import java.util.Iterator;
import java.util.List;

public final class LocationAccessPolicy
{
  private static final String LOG_TAG = LocationAccessPolicy.class.getSimpleName();
  
  public LocationAccessPolicy() {}
  
  public static boolean canAccessCellLocation(Context paramContext, String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
    throws SecurityException
  {
    Trace.beginSection("TelephonyLohcationCheck");
    boolean bool = true;
    if (paramInt1 == 1001)
    {
      Trace.endSection();
      return true;
    }
    if (paramBoolean)
    {
      try
      {
        paramContext.enforcePermission("android.permission.ACCESS_COARSE_LOCATION", paramInt2, paramInt1, "canAccessCellLocation");
      }
      finally
      {
        break label155;
      }
    }
    else
    {
      paramInt2 = paramContext.checkPermission("android.permission.ACCESS_COARSE_LOCATION", paramInt2, paramInt1);
      if (paramInt2 == -1)
      {
        Trace.endSection();
        return false;
      }
    }
    paramInt2 = AppOpsManager.permissionToOpCode("android.permission.ACCESS_COARSE_LOCATION");
    if (paramInt2 != -1)
    {
      paramInt2 = ((AppOpsManager)paramContext.getSystemService(AppOpsManager.class)).noteOpNoThrow(paramInt2, paramInt1, paramString);
      if (paramInt2 != 0)
      {
        Trace.endSection();
        return false;
      }
    }
    paramBoolean = isLocationModeEnabled(paramContext, UserHandle.getUserId(paramInt1));
    if (!paramBoolean)
    {
      Trace.endSection();
      return false;
    }
    if (!isCurrentProfile(paramContext, paramInt1))
    {
      paramBoolean = checkInteractAcrossUsersFull(paramContext);
      if (!paramBoolean)
      {
        paramBoolean = false;
        break label149;
      }
    }
    paramBoolean = bool;
    label149:
    Trace.endSection();
    return paramBoolean;
    label155:
    Trace.endSection();
    throw paramContext;
  }
  
  private static boolean checkInteractAcrossUsersFull(Context paramContext)
  {
    boolean bool;
    if (paramContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isCurrentProfile(Context paramContext, int paramInt)
  {
    long l = Binder.clearCallingIdentity();
    try
    {
      int i = ActivityManager.getCurrentUser();
      paramInt = UserHandle.getUserId(paramInt);
      if (paramInt == i) {
        return true;
      }
      paramContext = ((UserManager)paramContext.getSystemService(UserManager.class)).getProfiles(i).iterator();
      while (paramContext.hasNext())
      {
        i = nextid;
        if (i == paramInt) {
          return true;
        }
      }
      return false;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  private static boolean isLocationModeEnabled(Context paramContext, int paramInt)
  {
    paramContext = (LocationManager)paramContext.getSystemService(LocationManager.class);
    if (paramContext == null)
    {
      Log.w(LOG_TAG, "Couldn't get location manager, denying location access");
      return false;
    }
    return paramContext.isLocationEnabledForUser(UserHandle.of(paramInt));
  }
}
