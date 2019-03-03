package com.android.internal.view;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.Settings.System;
import android.util.Log;
import android.view.IWindowManager;
import android.view.WindowManagerGlobal;

public final class RotationPolicy
{
  private static final int CURRENT_ROTATION = -1;
  public static final int NATURAL_ROTATION = 0;
  private static final String TAG = "RotationPolicy";
  
  private RotationPolicy() {}
  
  private static boolean areAllRotationsAllowed(Context paramContext)
  {
    return paramContext.getResources().getBoolean(17956871);
  }
  
  public static int getRotationLockOrientation(Context paramContext)
  {
    if (!areAllRotationsAllowed(paramContext))
    {
      paramContext = new Point();
      IWindowManager localIWindowManager = WindowManagerGlobal.getWindowManagerService();
      try
      {
        localIWindowManager.getInitialDisplaySize(0, paramContext);
        int i = x;
        int j = y;
        if (i < j) {
          j = 1;
        } else {
          j = 2;
        }
        return j;
      }
      catch (RemoteException paramContext)
      {
        Log.w("RotationPolicy", "Unable to get the display size");
      }
    }
    return 0;
  }
  
  public static boolean isRotationLockToggleVisible(Context paramContext)
  {
    boolean bool1 = isRotationSupported(paramContext);
    boolean bool2 = false;
    boolean bool3 = bool2;
    if (bool1)
    {
      bool3 = bool2;
      if (Settings.System.getIntForUser(paramContext.getContentResolver(), "hide_rotation_lock_toggle_for_accessibility", 0, -2) == 0) {
        bool3 = true;
      }
    }
    return bool3;
  }
  
  public static boolean isRotationLocked(Context paramContext)
  {
    paramContext = paramContext.getContentResolver();
    boolean bool = false;
    if (Settings.System.getIntForUser(paramContext, "accelerometer_rotation", 0, -2) == 0) {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isRotationSupported(Context paramContext)
  {
    PackageManager localPackageManager = paramContext.getPackageManager();
    boolean bool;
    if ((localPackageManager.hasSystemFeature("android.hardware.sensor.accelerometer")) && (localPackageManager.hasSystemFeature("android.hardware.screen.portrait")) && (localPackageManager.hasSystemFeature("android.hardware.screen.landscape")) && (paramContext.getResources().getBoolean(17957041))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static void registerRotationPolicyListener(Context paramContext, RotationPolicyListener paramRotationPolicyListener)
  {
    registerRotationPolicyListener(paramContext, paramRotationPolicyListener, UserHandle.getCallingUserId());
  }
  
  public static void registerRotationPolicyListener(Context paramContext, RotationPolicyListener paramRotationPolicyListener, int paramInt)
  {
    paramContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("accelerometer_rotation"), false, mObserver, paramInt);
    paramContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("hide_rotation_lock_toggle_for_accessibility"), false, mObserver, paramInt);
  }
  
  public static void setRotationLock(Context paramContext, boolean paramBoolean)
  {
    int i;
    if (areAllRotationsAllowed(paramContext)) {
      i = -1;
    } else {
      i = 0;
    }
    setRotationLockAtAngle(paramContext, paramBoolean, i);
  }
  
  private static void setRotationLock(boolean paramBoolean, final int paramInt)
  {
    AsyncTask.execute(new Runnable()
    {
      public void run()
      {
        try
        {
          IWindowManager localIWindowManager = WindowManagerGlobal.getWindowManagerService();
          if (val$enabled) {
            localIWindowManager.freezeRotation(paramInt);
          } else {
            localIWindowManager.thawRotation();
          }
        }
        catch (RemoteException localRemoteException)
        {
          Log.w("RotationPolicy", "Unable to save auto-rotate setting");
        }
      }
    });
  }
  
  public static void setRotationLockAtAngle(Context paramContext, boolean paramBoolean, int paramInt)
  {
    Settings.System.putIntForUser(paramContext.getContentResolver(), "hide_rotation_lock_toggle_for_accessibility", 0, -2);
    setRotationLock(paramBoolean, paramInt);
  }
  
  public static void setRotationLockForAccessibility(Context paramContext, boolean paramBoolean)
  {
    Settings.System.putIntForUser(paramContext.getContentResolver(), "hide_rotation_lock_toggle_for_accessibility", paramBoolean, -2);
    setRotationLock(paramBoolean, 0);
  }
  
  public static void unregisterRotationPolicyListener(Context paramContext, RotationPolicyListener paramRotationPolicyListener)
  {
    paramContext.getContentResolver().unregisterContentObserver(mObserver);
  }
  
  public static abstract class RotationPolicyListener
  {
    final ContentObserver mObserver = new ContentObserver(new Handler())
    {
      public void onChange(boolean paramAnonymousBoolean, Uri paramAnonymousUri)
      {
        onChange();
      }
    };
    
    public RotationPolicyListener() {}
    
    public abstract void onChange();
  }
}
