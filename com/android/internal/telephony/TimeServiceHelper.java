package com.android.internal.telephony;

import android.app.AlarmManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;

public class TimeServiceHelper
{
  private static final String TIMEZONE_PROPERTY = "persist.sys.timezone";
  private final Context mContext;
  private final ContentResolver mCr;
  private Listener mListener;
  
  public TimeServiceHelper(Context paramContext)
  {
    mContext = paramContext;
    mCr = paramContext.getContentResolver();
  }
  
  static boolean isTimeZoneSettingInitializedStatic()
  {
    String str = SystemProperties.get("persist.sys.timezone");
    boolean bool;
    if ((str != null) && (str.length() > 0) && (!str.equals("GMT"))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  static void setDeviceTimeZoneStatic(Context paramContext, String paramString)
  {
    ((AlarmManager)paramContext.getSystemService("alarm")).setTimeZone(paramString);
    Intent localIntent = new Intent("android.intent.action.NETWORK_SET_TIMEZONE");
    localIntent.addFlags(536870912);
    localIntent.putExtra("time-zone", paramString);
    paramContext.sendStickyBroadcastAsUser(localIntent, UserHandle.ALL);
  }
  
  public long currentTimeMillis()
  {
    return System.currentTimeMillis();
  }
  
  public long elapsedRealtime()
  {
    return SystemClock.elapsedRealtime();
  }
  
  public boolean isTimeDetectionEnabled()
  {
    boolean bool = true;
    try
    {
      int i = Settings.Global.getInt(mCr, "auto_time");
      if (i <= 0) {
        bool = false;
      }
      return bool;
    }
    catch (Settings.SettingNotFoundException localSettingNotFoundException) {}
    return true;
  }
  
  public boolean isTimeZoneDetectionEnabled()
  {
    boolean bool = true;
    try
    {
      int i = Settings.Global.getInt(mCr, "auto_time_zone");
      if (i <= 0) {
        bool = false;
      }
      return bool;
    }
    catch (Settings.SettingNotFoundException localSettingNotFoundException) {}
    return true;
  }
  
  public boolean isTimeZoneSettingInitialized()
  {
    return isTimeZoneSettingInitializedStatic();
  }
  
  public void setDeviceTime(long paramLong)
  {
    SystemClock.setCurrentTimeMillis(paramLong);
    Intent localIntent = new Intent("android.intent.action.NETWORK_SET_TIME");
    localIntent.addFlags(536870912);
    localIntent.putExtra("time", paramLong);
    mContext.sendStickyBroadcastAsUser(localIntent, UserHandle.ALL);
  }
  
  public void setDeviceTimeZone(String paramString)
  {
    setDeviceTimeZoneStatic(mContext, paramString);
  }
  
  public void setListener(final Listener paramListener)
  {
    if (paramListener != null)
    {
      if (mListener == null)
      {
        mListener = paramListener;
        mCr.registerContentObserver(Settings.Global.getUriFor("auto_time"), true, new ContentObserver(new Handler())
        {
          public void onChange(boolean paramAnonymousBoolean)
          {
            paramListener.onTimeDetectionChange(isTimeDetectionEnabled());
          }
        });
        mCr.registerContentObserver(Settings.Global.getUriFor("auto_time_zone"), true, new ContentObserver(new Handler())
        {
          public void onChange(boolean paramAnonymousBoolean)
          {
            paramListener.onTimeZoneDetectionChange(isTimeZoneDetectionEnabled());
          }
        });
        return;
      }
      throw new IllegalStateException("listener already set");
    }
    throw new NullPointerException("listener==null");
  }
  
  public static abstract interface Listener
  {
    public abstract void onTimeDetectionChange(boolean paramBoolean);
    
    public abstract void onTimeZoneDetectionChange(boolean paramBoolean);
  }
}
