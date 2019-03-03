package android.app;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.IStatusBarService.Stub;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StatusBarManager
{
  public static final int CAMERA_LAUNCH_SOURCE_LIFT_TRIGGER = 2;
  public static final int CAMERA_LAUNCH_SOURCE_POWER_DOUBLE_TAP = 1;
  public static final int CAMERA_LAUNCH_SOURCE_WIGGLE = 0;
  public static final int DISABLE2_GLOBAL_ACTIONS = 8;
  public static final int DISABLE2_MASK = 31;
  public static final int DISABLE2_NONE = 0;
  public static final int DISABLE2_NOTIFICATION_SHADE = 4;
  public static final int DISABLE2_QUICK_SETTINGS = 1;
  public static final int DISABLE2_ROTATE_SUGGESTIONS = 16;
  public static final int DISABLE2_SYSTEM_ICONS = 2;
  public static final int DISABLE_BACK = 4194304;
  public static final int DISABLE_CLOCK = 8388608;
  public static final int DISABLE_EXPAND = 65536;
  public static final int DISABLE_HOME = 2097152;
  public static final int DISABLE_MASK = 67043328;
  @Deprecated
  public static final int DISABLE_NAVIGATION = 18874368;
  public static final int DISABLE_NONE = 0;
  public static final int DISABLE_NOTIFICATION_ALERTS = 262144;
  public static final int DISABLE_NOTIFICATION_ICONS = 131072;
  @Deprecated
  public static final int DISABLE_NOTIFICATION_TICKER = 524288;
  public static final int DISABLE_RECENT = 16777216;
  public static final int DISABLE_SEARCH = 33554432;
  public static final int DISABLE_SYSTEM_INFO = 1048576;
  public static final int NAVIGATION_HINT_BACK_ALT = 1;
  public static final int NAVIGATION_HINT_IME_SHOWN = 2;
  public static final int WINDOW_NAVIGATION_BAR = 2;
  public static final int WINDOW_STATE_HIDDEN = 2;
  public static final int WINDOW_STATE_HIDING = 1;
  public static final int WINDOW_STATE_SHOWING = 0;
  public static final int WINDOW_STATUS_BAR = 1;
  private Context mContext;
  private IStatusBarService mService;
  private IBinder mToken = new Binder();
  
  StatusBarManager(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private IStatusBarService getService()
  {
    try
    {
      if (mService == null)
      {
        mService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        if (mService == null) {
          Slog.w("StatusBarManager", "warning: no STATUS_BAR_SERVICE");
        }
      }
      IStatusBarService localIStatusBarService = mService;
      return localIStatusBarService;
    }
    finally {}
  }
  
  public static String windowStateToString(int paramInt)
  {
    if (paramInt == 1) {
      return "WINDOW_STATE_HIDING";
    }
    if (paramInt == 2) {
      return "WINDOW_STATE_HIDDEN";
    }
    if (paramInt == 0) {
      return "WINDOW_STATE_SHOWING";
    }
    return "WINDOW_STATE_UNKNOWN";
  }
  
  public void collapsePanels()
  {
    try
    {
      IStatusBarService localIStatusBarService = getService();
      if (localIStatusBarService != null) {
        localIStatusBarService.collapsePanels();
      }
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void disable(int paramInt)
  {
    try
    {
      IStatusBarService localIStatusBarService = getService();
      if (localIStatusBarService != null) {
        localIStatusBarService.disable(paramInt, mToken, mContext.getPackageName());
      }
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void disable2(int paramInt)
  {
    try
    {
      IStatusBarService localIStatusBarService = getService();
      if (localIStatusBarService != null) {
        localIStatusBarService.disable2(paramInt, mToken, mContext.getPackageName());
      }
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void expandNotificationsPanel()
  {
    try
    {
      IStatusBarService localIStatusBarService = getService();
      if (localIStatusBarService != null) {
        localIStatusBarService.expandNotificationsPanel();
      }
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void expandSettingsPanel()
  {
    expandSettingsPanel(null);
  }
  
  public void expandSettingsPanel(String paramString)
  {
    try
    {
      IStatusBarService localIStatusBarService = getService();
      if (localIStatusBarService != null) {
        localIStatusBarService.expandSettingsPanel(paramString);
      }
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void removeIcon(String paramString)
  {
    try
    {
      IStatusBarService localIStatusBarService = getService();
      if (localIStatusBarService != null) {
        localIStatusBarService.removeIcon(paramString);
      }
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setIcon(String paramString1, int paramInt1, int paramInt2, String paramString2)
  {
    try
    {
      IStatusBarService localIStatusBarService = getService();
      if (localIStatusBarService != null) {
        localIStatusBarService.setIcon(paramString1, mContext.getPackageName(), paramInt1, paramInt2, paramString2);
      }
      return;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void setIconVisibility(String paramString, boolean paramBoolean)
  {
    try
    {
      IStatusBarService localIStatusBarService = getService();
      if (localIStatusBarService != null) {
        localIStatusBarService.setIconVisibility(paramString, paramBoolean);
      }
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Disable2Flags {}
}
