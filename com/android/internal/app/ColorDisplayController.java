package com.android.internal.app;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.util.Slog;
import com.android.internal.logging.MetricsLogger;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

public final class ColorDisplayController
{
  public static final int AUTO_MODE_CUSTOM = 1;
  public static final int AUTO_MODE_DISABLED = 0;
  public static final int AUTO_MODE_TWILIGHT = 2;
  public static final int COLOR_MODE_AUTOMATIC = 3;
  public static final int COLOR_MODE_BOOSTED = 1;
  public static final int COLOR_MODE_NATURAL = 0;
  public static final int COLOR_MODE_SATURATED = 2;
  private static final boolean DEBUG = false;
  private static final String TAG = "ColorDisplayController";
  private Callback mCallback;
  private final ContentObserver mContentObserver;
  private final Context mContext;
  private MetricsLogger mMetricsLogger;
  private final int mUserId;
  
  public ColorDisplayController(Context paramContext)
  {
    this(paramContext, ActivityManager.getCurrentUser());
  }
  
  public ColorDisplayController(Context paramContext, int paramInt)
  {
    mContext = paramContext.getApplicationContext();
    mUserId = paramInt;
    mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper()))
    {
      public void onChange(boolean paramAnonymousBoolean, Uri paramAnonymousUri)
      {
        super.onChange(paramAnonymousBoolean, paramAnonymousUri);
        if (paramAnonymousUri == null) {
          paramAnonymousUri = null;
        } else {
          paramAnonymousUri = paramAnonymousUri.getLastPathSegment();
        }
        if (paramAnonymousUri != null) {
          ColorDisplayController.this.onSettingChanged(paramAnonymousUri);
        }
      }
    };
  }
  
  private int getCurrentColorModeFromSystemProperties()
  {
    int i = 0;
    int j = SystemProperties.getInt("persist.sys.sf.native_mode", 0);
    if (j == 0)
    {
      if (!"1.0".equals(SystemProperties.get("persist.sys.sf.color_saturation"))) {
        i = 1;
      }
      return i;
    }
    if (j == 1) {
      return 2;
    }
    if (j == 2) {
      return 3;
    }
    return -1;
  }
  
  private MetricsLogger getMetricsLogger()
  {
    if (mMetricsLogger == null) {
      mMetricsLogger = new MetricsLogger();
    }
    return mMetricsLogger;
  }
  
  public static boolean isAvailable(Context paramContext)
  {
    return paramContext.getResources().getBoolean(17956999);
  }
  
  private boolean isColorModeAvailable(int paramInt)
  {
    int[] arrayOfInt = mContext.getResources().getIntArray(17235989);
    if (arrayOfInt != null)
    {
      int i = arrayOfInt.length;
      for (int j = 0; j < i; j++) {
        if (arrayOfInt[j] == paramInt) {
          return true;
        }
      }
    }
    return false;
  }
  
  private void onSettingChanged(String paramString)
  {
    if (mCallback != null)
    {
      int i = -1;
      switch (paramString.hashCode())
      {
      default: 
        break;
      case 1578271348: 
        if (paramString.equals("night_display_custom_start_time")) {
          i = 2;
        }
        break;
      case 1561688220: 
        if (paramString.equals("display_color_mode")) {
          i = 5;
        }
        break;
      case 800115245: 
        if (paramString.equals("night_display_activated")) {
          i = 0;
        }
        break;
      case -551230169: 
        if (paramString.equals("accessibility_display_inversion_enabled")) {
          i = 6;
        }
        break;
      case -686921934: 
        if (paramString.equals("accessibility_display_daltonizer_enabled")) {
          i = 7;
        }
        break;
      case -969458956: 
        if (paramString.equals("night_display_color_temperature")) {
          i = 4;
        }
        break;
      case -1761668069: 
        if (paramString.equals("night_display_custom_end_time")) {
          i = 3;
        }
        break;
      case -2038150513: 
        if (paramString.equals("night_display_auto_mode")) {
          i = 1;
        }
        break;
      }
      switch (i)
      {
      default: 
        break;
      case 6: 
      case 7: 
        mCallback.onAccessibilityTransformChanged(getAccessibilityTransformActivated());
        break;
      case 5: 
        mCallback.onDisplayColorModeChanged(getColorMode());
        break;
      case 4: 
        mCallback.onColorTemperatureChanged(getColorTemperature());
        break;
      case 3: 
        mCallback.onCustomEndTimeChanged(getCustomEndTime());
        break;
      case 2: 
        mCallback.onCustomStartTimeChanged(getCustomStartTime());
        break;
      case 1: 
        mCallback.onAutoModeChanged(getAutoMode());
        break;
      case 0: 
        mCallback.onActivated(isActivated());
      }
    }
  }
  
  public boolean getAccessibilityTransformActivated()
  {
    ContentResolver localContentResolver = mContext.getContentResolver();
    int i = Settings.Secure.getIntForUser(localContentResolver, "accessibility_display_inversion_enabled", 0, mUserId);
    boolean bool = true;
    if ((i != 1) && (Settings.Secure.getIntForUser(localContentResolver, "accessibility_display_daltonizer_enabled", 0, mUserId) != 1)) {
      bool = false;
    }
    return bool;
  }
  
  public int getAutoMode()
  {
    int i = Settings.Secure.getIntForUser(mContext.getContentResolver(), "night_display_auto_mode", -1, mUserId);
    int j = i;
    if (i == -1) {
      j = mContext.getResources().getInteger(17694768);
    }
    i = j;
    if (j != 0)
    {
      i = j;
      if (j != 1)
      {
        i = j;
        if (j != 2)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Invalid autoMode: ");
          localStringBuilder.append(j);
          Slog.e("ColorDisplayController", localStringBuilder.toString());
          i = 0;
        }
      }
    }
    return i;
  }
  
  public int getAutoModeRaw()
  {
    return Settings.Secure.getIntForUser(mContext.getContentResolver(), "night_display_auto_mode", -1, mUserId);
  }
  
  public int getColorMode()
  {
    if (getAccessibilityTransformActivated())
    {
      if (isColorModeAvailable(2)) {
        return 2;
      }
      if (isColorModeAvailable(3)) {
        return 3;
      }
    }
    int i = Settings.System.getIntForUser(mContext.getContentResolver(), "display_color_mode", -1, mUserId);
    int j = i;
    if (i == -1) {
      j = getCurrentColorModeFromSystemProperties();
    }
    i = j;
    if (!isColorModeAvailable(j)) {
      if ((j == 1) && (isColorModeAvailable(0))) {
        i = 0;
      } else if ((j == 2) && (isColorModeAvailable(3))) {
        i = 3;
      } else if ((j == 3) && (isColorModeAvailable(2))) {
        i = 2;
      } else {
        i = -1;
      }
    }
    return i;
  }
  
  public int getColorTemperature()
  {
    int i = Settings.Secure.getIntForUser(mContext.getContentResolver(), "night_display_color_temperature", -1, mUserId);
    int j = i;
    if (i == -1) {
      j = getDefaultColorTemperature();
    }
    i = getMinimumColorTemperature();
    int k = getMaximumColorTemperature();
    if (j >= i)
    {
      i = j;
      if (j > k) {
        i = k;
      }
    }
    return i;
  }
  
  public LocalTime getCustomEndTime()
  {
    int i = Settings.Secure.getIntForUser(mContext.getContentResolver(), "night_display_custom_end_time", -1, mUserId);
    int j = i;
    if (i == -1) {
      j = mContext.getResources().getInteger(17694769);
    }
    return LocalTime.ofSecondOfDay(j / 1000);
  }
  
  public LocalTime getCustomStartTime()
  {
    int i = Settings.Secure.getIntForUser(mContext.getContentResolver(), "night_display_custom_start_time", -1, mUserId);
    int j = i;
    if (i == -1) {
      j = mContext.getResources().getInteger(17694770);
    }
    return LocalTime.ofSecondOfDay(j / 1000);
  }
  
  public int getDefaultColorTemperature()
  {
    return mContext.getResources().getInteger(17694839);
  }
  
  public LocalDateTime getLastActivatedTime()
  {
    Object localObject = Settings.Secure.getStringForUser(mContext.getContentResolver(), "night_display_last_activated_time", mUserId);
    if (localObject != null) {
      try
      {
        LocalDateTime localLocalDateTime = LocalDateTime.parse((CharSequence)localObject);
        return localLocalDateTime;
      }
      catch (DateTimeParseException localDateTimeParseException)
      {
        try
        {
          localObject = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong((String)localObject)), ZoneId.systemDefault());
          return localObject;
        }
        catch (DateTimeException|NumberFormatException localDateTimeException) {}
      }
    }
    return null;
  }
  
  public int getMaximumColorTemperature()
  {
    return mContext.getResources().getInteger(17694840);
  }
  
  public int getMinimumColorTemperature()
  {
    return mContext.getResources().getInteger(17694841);
  }
  
  public boolean isActivated()
  {
    return false;
  }
  
  public boolean setActivated(boolean paramBoolean)
  {
    if (isActivated() != paramBoolean) {
      Settings.Secure.putStringForUser(mContext.getContentResolver(), "night_display_last_activated_time", LocalDateTime.now().toString(), mUserId);
    }
    return Settings.Secure.putIntForUser(mContext.getContentResolver(), "night_display_activated", 0, mUserId);
  }
  
  public boolean setAutoMode(int paramInt)
  {
    if ((paramInt != 0) && (paramInt != 1) && (paramInt != 2))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid autoMode: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    if (getAutoMode() != paramInt)
    {
      Settings.Secure.putStringForUser(mContext.getContentResolver(), "night_display_last_activated_time", null, mUserId);
      getMetricsLogger().write(new LogMaker(1309).setType(4).setSubtype(paramInt));
    }
    return Settings.Secure.putIntForUser(mContext.getContentResolver(), "night_display_auto_mode", paramInt, mUserId);
  }
  
  public void setColorMode(int paramInt)
  {
    if (isColorModeAvailable(paramInt))
    {
      Settings.System.putIntForUser(mContext.getContentResolver(), "display_color_mode", paramInt, mUserId);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid colorMode: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public boolean setColorTemperature(int paramInt)
  {
    return Settings.Secure.putIntForUser(mContext.getContentResolver(), "night_display_color_temperature", paramInt, mUserId);
  }
  
  public boolean setCustomEndTime(LocalTime paramLocalTime)
  {
    if (paramLocalTime != null)
    {
      getMetricsLogger().write(new LogMaker(1310).setType(4).setSubtype(1));
      return Settings.Secure.putIntForUser(mContext.getContentResolver(), "night_display_custom_end_time", paramLocalTime.toSecondOfDay() * 1000, mUserId);
    }
    throw new IllegalArgumentException("endTime cannot be null");
  }
  
  public boolean setCustomStartTime(LocalTime paramLocalTime)
  {
    if (paramLocalTime != null)
    {
      getMetricsLogger().write(new LogMaker(1310).setType(4).setSubtype(0));
      return Settings.Secure.putIntForUser(mContext.getContentResolver(), "night_display_custom_start_time", paramLocalTime.toSecondOfDay() * 1000, mUserId);
    }
    throw new IllegalArgumentException("startTime cannot be null");
  }
  
  public void setListener(Callback paramCallback)
  {
    Callback localCallback = mCallback;
    if (localCallback != paramCallback)
    {
      mCallback = paramCallback;
      if (paramCallback == null)
      {
        mContext.getContentResolver().unregisterContentObserver(mContentObserver);
      }
      else if (localCallback == null)
      {
        paramCallback = mContext.getContentResolver();
        paramCallback.registerContentObserver(Settings.Secure.getUriFor("night_display_activated"), false, mContentObserver, mUserId);
        paramCallback.registerContentObserver(Settings.Secure.getUriFor("night_display_auto_mode"), false, mContentObserver, mUserId);
        paramCallback.registerContentObserver(Settings.Secure.getUriFor("night_display_custom_start_time"), false, mContentObserver, mUserId);
        paramCallback.registerContentObserver(Settings.Secure.getUriFor("night_display_custom_end_time"), false, mContentObserver, mUserId);
        paramCallback.registerContentObserver(Settings.Secure.getUriFor("night_display_color_temperature"), false, mContentObserver, mUserId);
        paramCallback.registerContentObserver(Settings.System.getUriFor("display_color_mode"), false, mContentObserver, mUserId);
        paramCallback.registerContentObserver(Settings.Secure.getUriFor("accessibility_display_inversion_enabled"), false, mContentObserver, mUserId);
        paramCallback.registerContentObserver(Settings.Secure.getUriFor("accessibility_display_daltonizer_enabled"), false, mContentObserver, mUserId);
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AutoMode {}
  
  public static abstract interface Callback
  {
    public void onAccessibilityTransformChanged(boolean paramBoolean) {}
    
    public void onActivated(boolean paramBoolean) {}
    
    public void onAutoModeChanged(int paramInt) {}
    
    public void onColorTemperatureChanged(int paramInt) {}
    
    public void onCustomEndTimeChanged(LocalTime paramLocalTime) {}
    
    public void onCustomStartTimeChanged(LocalTime paramLocalTime) {}
    
    public void onDisplayColorModeChanged(int paramInt) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ColorMode {}
}
