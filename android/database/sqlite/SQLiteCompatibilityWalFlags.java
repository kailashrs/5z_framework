package android.database.sqlite;

import android.app.ActivityThread;
import android.app.Application;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.util.KeyValueListParser;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;

public class SQLiteCompatibilityWalFlags
{
  private static final String TAG = "SQLiteCompatibilityWalFlags";
  private static volatile boolean sCallingGlobalSettings;
  private static volatile boolean sCompatibilityWalSupported;
  private static volatile boolean sFlagsSet;
  private static volatile boolean sInitialized;
  private static volatile String sWALSyncMode;
  
  public SQLiteCompatibilityWalFlags() {}
  
  @VisibleForTesting
  public static boolean areFlagsSet()
  {
    initIfNeeded();
    return sFlagsSet;
  }
  
  @VisibleForTesting
  public static String getWALSyncMode()
  {
    initIfNeeded();
    return sWALSyncMode;
  }
  
  @VisibleForTesting
  public static void init(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      sInitialized = true;
      return;
    }
    KeyValueListParser localKeyValueListParser = new KeyValueListParser(',');
    try
    {
      localKeyValueListParser.setString(paramString);
      sCompatibilityWalSupported = localKeyValueListParser.getBoolean("compatibility_wal_supported", SQLiteGlobal.isCompatibilityWalSupported());
      sWALSyncMode = localKeyValueListParser.getString("wal_syncmode", SQLiteGlobal.getWALSyncMode());
      paramString = new StringBuilder();
      paramString.append("Read compatibility WAL flags: compatibility_wal_supported=");
      paramString.append(sCompatibilityWalSupported);
      paramString.append(", wal_syncmode=");
      paramString.append(sWALSyncMode);
      Log.i("SQLiteCompatibilityWalFlags", paramString.toString());
      sFlagsSet = true;
      sInitialized = true;
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Setting has invalid format: ");
      localStringBuilder.append(paramString);
      Log.e("SQLiteCompatibilityWalFlags", localStringBuilder.toString(), localIllegalArgumentException);
      sInitialized = true;
    }
  }
  
  private static void initIfNeeded()
  {
    if ((!sInitialized) && (!sCallingGlobalSettings))
    {
      Object localObject1 = ActivityThread.currentActivityThread();
      if (localObject1 == null) {
        localObject1 = null;
      } else {
        localObject1 = ((ActivityThread)localObject1).getApplication();
      }
      Object localObject3 = null;
      if (localObject1 == null)
      {
        Log.w("SQLiteCompatibilityWalFlags", "Cannot read global setting sqlite_compatibility_wal_flags - Application state not available");
        localObject1 = localObject3;
      }
      try
      {
        sCallingGlobalSettings = true;
        localObject1 = Settings.Global.getString(((Application)localObject1).getContentResolver(), "sqlite_compatibility_wal_flags");
        sCallingGlobalSettings = false;
        init((String)localObject1);
        return;
      }
      finally
      {
        sCallingGlobalSettings = false;
      }
    }
  }
  
  @VisibleForTesting
  public static boolean isCompatibilityWalSupported()
  {
    initIfNeeded();
    return sCompatibilityWalSupported;
  }
  
  @VisibleForTesting
  public static void reset()
  {
    sInitialized = false;
    sFlagsSet = false;
    sCompatibilityWalSupported = false;
    sWALSyncMode = null;
  }
}
