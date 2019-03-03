package android.database.sqlite;

import android.content.res.Resources;
import android.os.StatFs;
import android.os.SystemProperties;

public final class SQLiteGlobal
{
  private static final String TAG = "SQLiteGlobal";
  private static int sDefaultPageSize;
  private static final Object sLock = new Object();
  
  private SQLiteGlobal() {}
  
  public static String getDefaultJournalMode()
  {
    return SystemProperties.get("debug.sqlite.journalmode", Resources.getSystem().getString(17039861));
  }
  
  public static int getDefaultPageSize()
  {
    synchronized (sLock)
    {
      if (sDefaultPageSize == 0)
      {
        StatFs localStatFs = new android/os/StatFs;
        localStatFs.<init>("/data");
        sDefaultPageSize = localStatFs.getBlockSize();
      }
      int i = SystemProperties.getInt("debug.sqlite.pagesize", sDefaultPageSize);
      return i;
    }
  }
  
  public static String getDefaultSyncMode()
  {
    return SystemProperties.get("debug.sqlite.syncmode", Resources.getSystem().getString(17039862));
  }
  
  public static int getIdleConnectionTimeout()
  {
    return SystemProperties.getInt("debug.sqlite.idle_connection_timeout", Resources.getSystem().getInteger(17694945));
  }
  
  public static int getJournalSizeLimit()
  {
    return SystemProperties.getInt("debug.sqlite.journalsizelimit", Resources.getSystem().getInteger(17694946));
  }
  
  public static int getWALAutoCheckpoint()
  {
    return Math.max(1, SystemProperties.getInt("debug.sqlite.wal.autocheckpoint", Resources.getSystem().getInteger(17694947)));
  }
  
  public static int getWALConnectionPoolSize()
  {
    return Math.max(2, SystemProperties.getInt("debug.sqlite.wal.poolsize", Resources.getSystem().getInteger(17694944)));
  }
  
  public static String getWALSyncMode()
  {
    return SystemProperties.get("debug.sqlite.wal.syncmode", Resources.getSystem().getString(17039863));
  }
  
  public static boolean isCompatibilityWalSupported()
  {
    return SystemProperties.getBoolean("debug.sqlite.compatibility_wal_supported", Resources.getSystem().getBoolean(17957104));
  }
  
  private static native int nativeReleaseMemory();
  
  public static int releaseMemory()
  {
    return nativeReleaseMemory();
  }
}
