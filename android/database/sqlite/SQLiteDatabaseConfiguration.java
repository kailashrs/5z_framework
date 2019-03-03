package android.database.sqlite;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SQLiteDatabaseConfiguration
{
  private static final Pattern EMAIL_IN_DB_PATTERN = Pattern.compile("[\\w\\.\\-]+@[\\w\\.\\-]+");
  public static final String MEMORY_DB_PATH = ":memory:";
  public final ArrayList<SQLiteCustomFunction> customFunctions = new ArrayList();
  public boolean foreignKeyConstraintsEnabled;
  public long idleConnectionTimeoutMs = Long.MAX_VALUE;
  public String journalMode;
  public final String label;
  public Locale locale;
  public int lookasideSlotCount = -1;
  public int lookasideSlotSize = -1;
  public int maxSqlCacheSize;
  public int openFlags;
  public final String path;
  public String syncMode;
  
  public SQLiteDatabaseConfiguration(SQLiteDatabaseConfiguration paramSQLiteDatabaseConfiguration)
  {
    if (paramSQLiteDatabaseConfiguration != null)
    {
      path = path;
      label = label;
      updateParametersFrom(paramSQLiteDatabaseConfiguration);
      return;
    }
    throw new IllegalArgumentException("other must not be null.");
  }
  
  public SQLiteDatabaseConfiguration(String paramString, int paramInt)
  {
    if (paramString != null)
    {
      path = paramString;
      label = stripPathForLogs(paramString);
      openFlags = paramInt;
      maxSqlCacheSize = 25;
      locale = Locale.getDefault();
      return;
    }
    throw new IllegalArgumentException("path must not be null.");
  }
  
  private static String stripPathForLogs(String paramString)
  {
    if (paramString.indexOf('@') == -1) {
      return paramString;
    }
    return EMAIL_IN_DB_PATTERN.matcher(paramString).replaceAll("XX@YY");
  }
  
  public boolean isInMemoryDb()
  {
    return path.equalsIgnoreCase(":memory:");
  }
  
  boolean isLookasideConfigSet()
  {
    boolean bool;
    if ((lookasideSlotCount >= 0) && (lookasideSlotSize >= 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void updateParametersFrom(SQLiteDatabaseConfiguration paramSQLiteDatabaseConfiguration)
  {
    if (paramSQLiteDatabaseConfiguration != null)
    {
      if (path.equals(path))
      {
        openFlags = openFlags;
        maxSqlCacheSize = maxSqlCacheSize;
        locale = locale;
        foreignKeyConstraintsEnabled = foreignKeyConstraintsEnabled;
        customFunctions.clear();
        customFunctions.addAll(customFunctions);
        lookasideSlotSize = lookasideSlotSize;
        lookasideSlotCount = lookasideSlotCount;
        idleConnectionTimeoutMs = idleConnectionTimeoutMs;
        journalMode = journalMode;
        syncMode = syncMode;
        return;
      }
      throw new IllegalArgumentException("other configuration must refer to the same database.");
    }
    throw new IllegalArgumentException("other must not be null.");
  }
  
  boolean useCompatibilityWal()
  {
    boolean bool;
    if ((journalMode == null) && (syncMode == null) && ((openFlags & 0x40000000) == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
