package android.database.sqlite;

import android.os.Build;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Printer;
import java.util.ArrayList;

public final class SQLiteDebug
{
  public static final boolean DEBUG_LOG_SLOW_QUERIES = Build.IS_DEBUGGABLE;
  public static final boolean DEBUG_SQL_LOG = Log.isLoggable("SQLiteLog", 2);
  public static final boolean DEBUG_SQL_STATEMENTS = Log.isLoggable("SQLiteStatements", 2);
  public static final boolean DEBUG_SQL_TIME = Log.isLoggable("SQLiteTime", 2);
  
  private SQLiteDebug() {}
  
  public static void dump(Printer paramPrinter, String[] paramArrayOfString)
  {
    boolean bool = false;
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++) {
      if (paramArrayOfString[j].equals("-v")) {
        bool = true;
      }
    }
    SQLiteDatabase.dumpAll(paramPrinter, bool);
  }
  
  public static PagerStats getDatabaseInfo()
  {
    PagerStats localPagerStats = new PagerStats();
    nativeGetPagerStats(localPagerStats);
    dbStats = SQLiteDatabase.getDbStats();
    return localPagerStats;
  }
  
  private static native void nativeGetPagerStats(PagerStats paramPagerStats);
  
  public static final boolean shouldLogSlowQuery(long paramLong)
  {
    int i = SystemProperties.getInt("db.log.slow_query_threshold", -1);
    boolean bool;
    if ((i >= 0) && (paramLong >= i)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static class DbStats
  {
    public String cache;
    public String dbName;
    public long dbSize;
    public int lookaside;
    public long pageSize;
    
    public DbStats(String paramString, long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      dbName = paramString;
      pageSize = (paramLong2 / 1024L);
      dbSize = (paramLong1 * paramLong2 / 1024L);
      lookaside = paramInt1;
      paramString = new StringBuilder();
      paramString.append(paramInt2);
      paramString.append("/");
      paramString.append(paramInt3);
      paramString.append("/");
      paramString.append(paramInt4);
      cache = paramString.toString();
    }
  }
  
  public static class PagerStats
  {
    public ArrayList<SQLiteDebug.DbStats> dbStats;
    public int largestMemAlloc;
    public int memoryUsed;
    public int pageCacheOverflow;
    
    public PagerStats() {}
  }
}
