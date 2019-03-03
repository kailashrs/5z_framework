package com.android.internal.hardware;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.SystemProperties;
import android.provider.Settings.Secure;
import android.text.TextUtils;

public class AmbientDisplayConfiguration
{
  private final Context mContext;
  
  public AmbientDisplayConfiguration(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private boolean alwaysOnDisplayAvailable()
  {
    return mContext.getResources().getBoolean(17956938);
  }
  
  private boolean alwaysOnDisplayDebuggingEnabled()
  {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (SystemProperties.getBoolean("debug.doze.aod", false))
    {
      bool2 = bool1;
      if (Build.IS_DEBUGGABLE) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  private boolean boolSetting(String paramString, int paramInt1, int paramInt2)
  {
    boolean bool;
    if (Settings.Secure.getIntForUser(mContext.getContentResolver(), paramString, paramInt2, paramInt1) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean boolSettingDefaultOff(String paramString, int paramInt)
  {
    return boolSetting(paramString, paramInt, 0);
  }
  
  private boolean boolSettingDefaultOn(String paramString, int paramInt)
  {
    return boolSetting(paramString, paramInt, 1);
  }
  
  private boolean pulseOnLongPressAvailable()
  {
    return TextUtils.isEmpty(longPressSensorType()) ^ true;
  }
  
  public boolean accessibilityInversionEnabled(int paramInt)
  {
    return boolSettingDefaultOff("accessibility_display_inversion_enabled", paramInt);
  }
  
  public boolean alwaysOnAvailable()
  {
    boolean bool;
    if (((alwaysOnDisplayDebuggingEnabled()) || (alwaysOnDisplayAvailable())) && (ambientDisplayAvailable())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean alwaysOnAvailableForUser(int paramInt)
  {
    boolean bool;
    if ((alwaysOnAvailable()) && (!accessibilityInversionEnabled(paramInt))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean alwaysOnEnabled(int paramInt)
  {
    boolean bool;
    if ((boolSettingDefaultOn("doze_always_on", paramInt)) && (alwaysOnAvailable()) && (!accessibilityInversionEnabled(paramInt))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean ambientDisplayAvailable()
  {
    return TextUtils.isEmpty(ambientDisplayComponent()) ^ true;
  }
  
  public String ambientDisplayComponent()
  {
    return mContext.getResources().getString(17039701);
  }
  
  public boolean available()
  {
    boolean bool;
    if ((!pulseOnNotificationAvailable()) && (!pulseOnPickupAvailable()) && (!pulseOnDoubleTapAvailable())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean doubleTapSensorAvailable()
  {
    return TextUtils.isEmpty(doubleTapSensorType()) ^ true;
  }
  
  public String doubleTapSensorType()
  {
    return mContext.getResources().getString(17039702);
  }
  
  public boolean dozePulsePickupSensorAvailable()
  {
    return mContext.getResources().getBoolean(17956939);
  }
  
  public boolean enabled(int paramInt)
  {
    boolean bool;
    if ((!pulseOnNotificationEnabled(paramInt)) && (!pulseOnPickupEnabled(paramInt)) && (!pulseOnDoubleTapEnabled(paramInt)) && (!pulseOnLongPressEnabled(paramInt)) && (!alwaysOnEnabled(paramInt))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public String longPressSensorType()
  {
    return mContext.getResources().getString(17039703);
  }
  
  public boolean pulseOnDoubleTapAvailable()
  {
    boolean bool;
    if ((doubleTapSensorAvailable()) && (ambientDisplayAvailable())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean pulseOnDoubleTapEnabled(int paramInt)
  {
    boolean bool;
    if ((boolSettingDefaultOn("doze_pulse_on_double_tap", paramInt)) && (pulseOnDoubleTapAvailable())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean pulseOnLongPressEnabled(int paramInt)
  {
    boolean bool;
    if ((pulseOnLongPressAvailable()) && (boolSettingDefaultOff("doze_pulse_on_long_press", paramInt))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean pulseOnNotificationAvailable()
  {
    return ambientDisplayAvailable();
  }
  
  public boolean pulseOnNotificationEnabled(int paramInt)
  {
    boolean bool;
    if ((boolSettingDefaultOn("doze_enabled", paramInt)) && (pulseOnNotificationAvailable())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean pulseOnPickupAvailable()
  {
    boolean bool;
    if ((dozePulsePickupSensorAvailable()) && (ambientDisplayAvailable())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean pulseOnPickupCanBeModified(int paramInt)
  {
    return alwaysOnEnabled(paramInt) ^ true;
  }
  
  public boolean pulseOnPickupEnabled(int paramInt)
  {
    boolean bool;
    if (((boolSettingDefaultOn("doze_pulse_on_pick_up", paramInt)) || (alwaysOnEnabled(paramInt))) && (pulseOnPickupAvailable())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
