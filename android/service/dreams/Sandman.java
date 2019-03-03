package android.service.dreams;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.util.Slog;

public final class Sandman
{
  private static final ComponentName SOMNAMBULATOR_COMPONENT = new ComponentName("com.android.systemui", "com.android.systemui.Somnambulator");
  private static final String TAG = "Sandman";
  
  private Sandman() {}
  
  private static boolean isScreenSaverActivatedOnDock(Context paramContext)
  {
    boolean bool1 = paramContext.getResources().getBoolean(17956940);
    boolean bool2 = false;
    int i;
    if (bool1) {
      i = 1;
    } else {
      i = 0;
    }
    if (Settings.Secure.getIntForUser(paramContext.getContentResolver(), "screensaver_activate_on_dock", i, -2) != 0) {
      bool2 = true;
    }
    return bool2;
  }
  
  private static boolean isScreenSaverEnabled(Context paramContext)
  {
    boolean bool1 = paramContext.getResources().getBoolean(17956942);
    boolean bool2 = false;
    int i;
    if (bool1) {
      i = 1;
    } else {
      i = 0;
    }
    if (Settings.Secure.getIntForUser(paramContext.getContentResolver(), "screensaver_enabled", i, -2) != 0) {
      bool2 = true;
    }
    return bool2;
  }
  
  public static boolean shouldStartDockApp(Context paramContext, Intent paramIntent)
  {
    paramContext = paramIntent.resolveActivity(paramContext.getPackageManager());
    boolean bool;
    if ((paramContext != null) && (!paramContext.equals(SOMNAMBULATOR_COMPONENT))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static void startDream(Context paramContext, boolean paramBoolean)
  {
    try
    {
      IDreamManager localIDreamManager = IDreamManager.Stub.asInterface(ServiceManager.getService("dreams"));
      if ((localIDreamManager != null) && (!localIDreamManager.isDreaming()))
      {
        if (paramBoolean)
        {
          Slog.i("Sandman", "Activating dream while docked.");
          ((PowerManager)paramContext.getSystemService("power")).wakeUp(SystemClock.uptimeMillis(), "android.service.dreams:DREAM");
        }
        else
        {
          Slog.i("Sandman", "Activating dream by user request.");
        }
        localIDreamManager.dream();
      }
    }
    catch (RemoteException paramContext)
    {
      Slog.e("Sandman", "Could not start dream when docked.", paramContext);
    }
  }
  
  public static void startDreamByUserRequest(Context paramContext)
  {
    startDream(paramContext, false);
  }
  
  public static void startDreamWhenDockedIfAppropriate(Context paramContext)
  {
    if ((isScreenSaverEnabled(paramContext)) && (isScreenSaverActivatedOnDock(paramContext)))
    {
      startDream(paramContext, true);
      return;
    }
    Slog.i("Sandman", "Dreams currently disabled for docks.");
  }
}
