package android.database.sqlite;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.DefaultDatabaseErrorHandler;
import android.database.SQLException;
import android.os.CancellationSignal;
import android.os.Looper;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import android.util.Printer;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public final class SQLiteDatabase
  extends SQLiteClosable
{
  public static final int CONFLICT_ABORT = 2;
  public static final int CONFLICT_FAIL = 3;
  public static final int CONFLICT_IGNORE = 4;
  public static final int CONFLICT_NONE = 0;
  public static final int CONFLICT_REPLACE = 5;
  public static final int CONFLICT_ROLLBACK = 1;
  private static final String[] CONFLICT_VALUES = { "", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE " };
  public static final int CREATE_IF_NECESSARY = 268435456;
  private static final boolean DEBUG_CLOSE_IDLE_CONNECTIONS = SystemProperties.getBoolean("persist.debug.sqlite.close_idle_connections", false);
  public static final int DISABLE_COMPATIBILITY_WAL = 1073741824;
  public static final int ENABLE_WRITE_AHEAD_LOGGING = 536870912;
  private static final int EVENT_DB_CORRUPT = 75004;
  public static final int MAX_SQL_CACHE_SIZE = 100;
  public static final int NO_LOCALIZED_COLLATORS = 16;
  public static final int OPEN_READONLY = 1;
  public static final int OPEN_READWRITE = 0;
  private static final int OPEN_READ_MASK = 1;
  public static final int SQLITE_MAX_LIKE_PATTERN_LENGTH = 50000;
  private static final String TAG = "SQLiteDatabase";
  private static WeakHashMap<SQLiteDatabase, Object> sActiveDatabases = new WeakHashMap();
  private final CloseGuard mCloseGuardLocked = CloseGuard.get();
  private final SQLiteDatabaseConfiguration mConfigurationLocked;
  private SQLiteConnectionPool mConnectionPoolLocked;
  private final CursorFactory mCursorFactory;
  private final DatabaseErrorHandler mErrorHandler;
  private boolean mHasAttachedDbsLocked;
  private final Object mLock = new Object();
  private final ThreadLocal<SQLiteSession> mThreadSession = ThreadLocal.withInitial(new _..Lambda.RBWjWVyGrOTsQrLCYzJ_G8Uk25Q(this));
  
  private SQLiteDatabase(String paramString1, int paramInt1, CursorFactory paramCursorFactory, DatabaseErrorHandler paramDatabaseErrorHandler, int paramInt2, int paramInt3, long paramLong, String paramString2, String paramString3)
  {
    mCursorFactory = paramCursorFactory;
    if (paramDatabaseErrorHandler == null) {
      paramDatabaseErrorHandler = new DefaultDatabaseErrorHandler();
    }
    mErrorHandler = paramDatabaseErrorHandler;
    mConfigurationLocked = new SQLiteDatabaseConfiguration(paramString1, paramInt1);
    mConfigurationLocked.lookasideSlotSize = paramInt2;
    mConfigurationLocked.lookasideSlotCount = paramInt3;
    if (ActivityManager.isLowRamDeviceStatic())
    {
      mConfigurationLocked.lookasideSlotCount = 0;
      mConfigurationLocked.lookasideSlotSize = 0;
    }
    long l1 = Long.MAX_VALUE;
    long l2 = l1;
    if (!mConfigurationLocked.isInMemoryDb()) {
      if (paramLong >= 0L)
      {
        l2 = paramLong;
      }
      else
      {
        l2 = l1;
        if (DEBUG_CLOSE_IDLE_CONNECTIONS) {
          l2 = SQLiteGlobal.getIdleConnectionTimeout();
        }
      }
    }
    mConfigurationLocked.idleConnectionTimeoutMs = l2;
    mConfigurationLocked.journalMode = paramString2;
    mConfigurationLocked.syncMode = paramString3;
    if ((!SQLiteGlobal.isCompatibilityWalSupported()) || ((SQLiteCompatibilityWalFlags.areFlagsSet()) && (!SQLiteCompatibilityWalFlags.isCompatibilityWalSupported())))
    {
      paramString1 = mConfigurationLocked;
      openFlags |= 0x40000000;
    }
  }
  
  private void beginTransaction(SQLiteTransactionListener paramSQLiteTransactionListener, boolean paramBoolean)
  {
    acquireReference();
    try
    {
      SQLiteSession localSQLiteSession = getThreadSession();
      int i;
      if (paramBoolean) {
        i = 2;
      } else {
        i = 1;
      }
      localSQLiteSession.beginTransaction(i, paramSQLiteTransactionListener, getThreadDefaultConnectionFlags(false), null);
      return;
    }
    finally
    {
      releaseReference();
    }
  }
  
  private void collectDbStats(ArrayList<SQLiteDebug.DbStats> paramArrayList)
  {
    synchronized (mLock)
    {
      if (mConnectionPoolLocked != null) {
        mConnectionPoolLocked.collectDbStats(paramArrayList);
      }
      return;
    }
  }
  
  public static SQLiteDatabase create(CursorFactory paramCursorFactory)
  {
    return openDatabase(":memory:", paramCursorFactory, 268435456);
  }
  
  public static SQLiteDatabase createInMemory(OpenParams paramOpenParams)
  {
    return openDatabase(":memory:", paramOpenParams.toBuilder().addOpenFlags(268435456).build());
  }
  
  public static boolean deleteDatabase(File paramFile)
  {
    if (paramFile != null)
    {
      boolean bool1 = paramFile.delete();
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramFile.getPath());
      ((StringBuilder)localObject).append("-journal");
      boolean bool2 = new File(((StringBuilder)localObject).toString()).delete();
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramFile.getPath());
      ((StringBuilder)localObject).append("-shm");
      boolean bool3 = new File(((StringBuilder)localObject).toString()).delete();
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramFile.getPath());
      ((StringBuilder)localObject).append("-wal");
      bool3 = false | bool1 | bool2 | bool3 | new File(((StringBuilder)localObject).toString()).delete();
      localObject = paramFile.getParentFile();
      bool1 = bool3;
      if (localObject != null)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramFile.getName());
        localStringBuilder.append("-mj");
        paramFile = ((File)localObject).listFiles(new FileFilter()
        {
          public boolean accept(File paramAnonymousFile)
          {
            return paramAnonymousFile.getName().startsWith(SQLiteDatabase.this);
          }
        });
        bool1 = bool3;
        if (paramFile != null)
        {
          int i = paramFile.length;
          for (int j = 0;; j++)
          {
            bool1 = bool3;
            if (j >= i) {
              break;
            }
            bool3 |= paramFile[j].delete();
          }
        }
      }
      return bool1;
    }
    throw new IllegalArgumentException("file must not be null");
  }
  
  private void dispose(boolean paramBoolean)
  {
    synchronized (mLock)
    {
      if (mCloseGuardLocked != null)
      {
        if (paramBoolean) {
          mCloseGuardLocked.warnIfOpen();
        }
        mCloseGuardLocked.close();
      }
      SQLiteConnectionPool localSQLiteConnectionPool = mConnectionPoolLocked;
      mConnectionPoolLocked = null;
      if (!paramBoolean) {
        synchronized (sActiveDatabases)
        {
          sActiveDatabases.remove(this);
          if (localSQLiteConnectionPool != null) {
            localSQLiteConnectionPool.close();
          }
        }
      }
      return;
    }
  }
  
  private void dump(Printer paramPrinter, boolean paramBoolean)
  {
    synchronized (mLock)
    {
      if (mConnectionPoolLocked != null)
      {
        paramPrinter.println("");
        mConnectionPoolLocked.dump(paramPrinter, paramBoolean);
      }
      return;
    }
  }
  
  static void dumpAll(Printer paramPrinter, boolean paramBoolean)
  {
    Iterator localIterator = getActiveDatabases().iterator();
    while (localIterator.hasNext()) {
      ((SQLiteDatabase)localIterator.next()).dump(paramPrinter, paramBoolean);
    }
  }
  
  public static String findEditTable(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      int i = paramString.indexOf(' ');
      int j = paramString.indexOf(',');
      if ((i > 0) && ((i < j) || (j < 0))) {
        return paramString.substring(0, i);
      }
      if ((j > 0) && ((j < i) || (i < 0))) {
        return paramString.substring(0, j);
      }
      return paramString;
    }
    throw new IllegalStateException("Invalid tables");
  }
  
  private static ArrayList<SQLiteDatabase> getActiveDatabases()
  {
    ArrayList localArrayList = new ArrayList();
    synchronized (sActiveDatabases)
    {
      localArrayList.addAll(sActiveDatabases.keySet());
      return localArrayList;
    }
  }
  
  static ArrayList<SQLiteDebug.DbStats> getDbStats()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = getActiveDatabases().iterator();
    while (localIterator.hasNext()) {
      ((SQLiteDatabase)localIterator.next()).collectDbStats(localArrayList);
    }
    return localArrayList;
  }
  
  private static boolean isMainThread()
  {
    Looper localLooper = Looper.myLooper();
    boolean bool;
    if ((localLooper != null) && (localLooper == Looper.getMainLooper())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isReadOnlyLocked()
  {
    int i = mConfigurationLocked.openFlags;
    boolean bool = true;
    if ((i & 0x1) != 1) {
      bool = false;
    }
    return bool;
  }
  
  private void open()
  {
    try
    {
      openInner();
    }
    catch (SQLiteException localSQLiteException)
    {
      break label21;
    }
    catch (SQLiteDatabaseCorruptException localSQLiteDatabaseCorruptException)
    {
      onCorruption();
      openInner();
    }
    return;
    label21:
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Failed to open database '");
    localStringBuilder.append(getLabel());
    localStringBuilder.append("'.");
    Log.e("SQLiteDatabase", localStringBuilder.toString(), localSQLiteDatabaseCorruptException);
    close();
    throw localSQLiteDatabaseCorruptException;
  }
  
  public static SQLiteDatabase openDatabase(File paramFile, OpenParams paramOpenParams)
  {
    return openDatabase(paramFile.getPath(), paramOpenParams);
  }
  
  public static SQLiteDatabase openDatabase(String paramString, CursorFactory paramCursorFactory, int paramInt)
  {
    return openDatabase(paramString, paramCursorFactory, paramInt, null);
  }
  
  public static SQLiteDatabase openDatabase(String paramString, CursorFactory paramCursorFactory, int paramInt, DatabaseErrorHandler paramDatabaseErrorHandler)
  {
    paramString = new SQLiteDatabase(paramString, paramInt, paramCursorFactory, paramDatabaseErrorHandler, -1, -1, -1L, null, null);
    paramString.open();
    return paramString;
  }
  
  private static SQLiteDatabase openDatabase(String paramString, OpenParams paramOpenParams)
  {
    boolean bool;
    if (paramOpenParams != null) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool, "OpenParams cannot be null");
    paramString = new SQLiteDatabase(paramString, mOpenFlags, mCursorFactory, mErrorHandler, mLookasideSlotSize, mLookasideSlotCount, mIdleConnectionTimeout, mJournalMode, mSyncMode);
    paramString.open();
    return paramString;
  }
  
  private void openInner()
  {
    synchronized (mLock)
    {
      mConnectionPoolLocked = SQLiteConnectionPool.open(mConfigurationLocked);
      mCloseGuardLocked.open("close");
      synchronized (sActiveDatabases)
      {
        sActiveDatabases.put(this, null);
        return;
      }
      throw localObject2;
    }
  }
  
  public static SQLiteDatabase openOrCreateDatabase(File paramFile, CursorFactory paramCursorFactory)
  {
    return openOrCreateDatabase(paramFile.getPath(), paramCursorFactory);
  }
  
  public static SQLiteDatabase openOrCreateDatabase(String paramString, CursorFactory paramCursorFactory)
  {
    return openDatabase(paramString, paramCursorFactory, 268435456, null);
  }
  
  public static SQLiteDatabase openOrCreateDatabase(String paramString, CursorFactory paramCursorFactory, DatabaseErrorHandler paramDatabaseErrorHandler)
  {
    return openDatabase(paramString, paramCursorFactory, 268435456, paramDatabaseErrorHandler);
  }
  
  public static int releaseMemory()
  {
    return SQLiteGlobal.releaseMemory();
  }
  
  private void throwIfNotOpenLocked()
  {
    if (mConnectionPoolLocked != null) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("The database '");
    localStringBuilder.append(mConfigurationLocked.label);
    localStringBuilder.append("' is not open.");
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  private boolean yieldIfContendedHelper(boolean paramBoolean, long paramLong)
  {
    acquireReference();
    try
    {
      paramBoolean = getThreadSession().yieldTransaction(paramLong, paramBoolean, null);
      return paramBoolean;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public void addCustomFunction(String arg1, int paramInt, CustomFunction paramCustomFunction)
  {
    paramCustomFunction = new SQLiteCustomFunction(???, paramInt, paramCustomFunction);
    synchronized (mLock)
    {
      throwIfNotOpenLocked();
      mConfigurationLocked.customFunctions.add(paramCustomFunction);
      try
      {
        mConnectionPoolLocked.reconfigure(mConfigurationLocked);
        return;
      }
      catch (RuntimeException localRuntimeException)
      {
        mConfigurationLocked.customFunctions.remove(paramCustomFunction);
        throw localRuntimeException;
      }
    }
  }
  
  public void beginTransaction()
  {
    beginTransaction(null, true);
  }
  
  public void beginTransactionNonExclusive()
  {
    beginTransaction(null, false);
  }
  
  public void beginTransactionWithListener(SQLiteTransactionListener paramSQLiteTransactionListener)
  {
    beginTransaction(paramSQLiteTransactionListener, true);
  }
  
  public void beginTransactionWithListenerNonExclusive(SQLiteTransactionListener paramSQLiteTransactionListener)
  {
    beginTransaction(paramSQLiteTransactionListener, false);
  }
  
  public SQLiteStatement compileStatement(String paramString)
    throws SQLException
  {
    acquireReference();
    try
    {
      paramString = new SQLiteStatement(this, paramString, null);
      return paramString;
    }
    finally
    {
      releaseReference();
    }
  }
  
  SQLiteSession createSession()
  {
    synchronized (mLock)
    {
      throwIfNotOpenLocked();
      SQLiteConnectionPool localSQLiteConnectionPool = mConnectionPoolLocked;
      return new SQLiteSession(localSQLiteConnectionPool);
    }
  }
  
  /* Error */
  public int delete(String paramString1, String paramString2, String[] paramArrayOfString)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 207	android/database/sqlite/SQLiteDatabase:acquireReference	()V
    //   4: new 536	android/database/sqlite/SQLiteStatement
    //   7: astore 4
    //   9: new 267	java/lang/StringBuilder
    //   12: astore 5
    //   14: aload 5
    //   16: invokespecial 268	java/lang/StringBuilder:<init>	()V
    //   19: aload 5
    //   21: ldc_w 547
    //   24: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: pop
    //   28: aload 5
    //   30: aload_1
    //   31: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: pop
    //   35: aload_2
    //   36: invokestatic 358	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   39: ifne +33 -> 72
    //   42: new 267	java/lang/StringBuilder
    //   45: astore_1
    //   46: aload_1
    //   47: invokespecial 268	java/lang/StringBuilder:<init>	()V
    //   50: aload_1
    //   51: ldc_w 549
    //   54: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   57: pop
    //   58: aload_1
    //   59: aload_2
    //   60: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: pop
    //   64: aload_1
    //   65: invokevirtual 281	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   68: astore_1
    //   69: goto +6 -> 75
    //   72: ldc 102
    //   74: astore_1
    //   75: aload 5
    //   77: aload_1
    //   78: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: pop
    //   82: aload 4
    //   84: aload_0
    //   85: aload 5
    //   87: invokevirtual 281	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   90: aload_3
    //   91: invokespecial 539	android/database/sqlite/SQLiteStatement:<init>	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;[Ljava/lang/Object;)V
    //   94: aload 4
    //   96: invokevirtual 552	android/database/sqlite/SQLiteStatement:executeUpdateDelete	()I
    //   99: istore 6
    //   101: aload 4
    //   103: invokevirtual 553	android/database/sqlite/SQLiteStatement:close	()V
    //   106: aload_0
    //   107: invokevirtual 223	android/database/sqlite/SQLiteDatabase:releaseReference	()V
    //   110: iload 6
    //   112: ireturn
    //   113: astore_1
    //   114: aload 4
    //   116: invokevirtual 553	android/database/sqlite/SQLiteStatement:close	()V
    //   119: aload_1
    //   120: athrow
    //   121: astore_1
    //   122: aload_0
    //   123: invokevirtual 223	android/database/sqlite/SQLiteDatabase:releaseReference	()V
    //   126: aload_1
    //   127: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	128	0	this	SQLiteDatabase
    //   0	128	1	paramString1	String
    //   0	128	2	paramString2	String
    //   0	128	3	paramArrayOfString	String[]
    //   7	108	4	localSQLiteStatement	SQLiteStatement
    //   12	74	5	localStringBuilder	StringBuilder
    //   99	12	6	i	int
    // Exception table:
    //   from	to	target	type
    //   94	101	113	finally
    //   4	69	121	finally
    //   75	94	121	finally
    //   101	106	121	finally
    //   114	121	121	finally
  }
  
  public void disableWriteAheadLogging()
  {
    synchronized (mLock)
    {
      throwIfNotOpenLocked();
      int i = mConfigurationLocked.openFlags;
      int j = 0;
      int k;
      if ((0x20000000 & i) == 0) {
        k = 1;
      } else {
        k = 0;
      }
      if ((i & 0x40000000) != 0) {
        j = 1;
      }
      if ((k != 0) && (j != 0)) {
        return;
      }
      SQLiteDatabaseConfiguration localSQLiteDatabaseConfiguration = mConfigurationLocked;
      openFlags &= 0xDFFFFFFF;
      localSQLiteDatabaseConfiguration = mConfigurationLocked;
      openFlags = (0x40000000 | openFlags);
      try
      {
        mConnectionPoolLocked.reconfigure(mConfigurationLocked);
        return;
      }
      catch (RuntimeException localRuntimeException)
      {
        mConfigurationLocked.openFlags = i;
        throw localRuntimeException;
      }
    }
  }
  
  public boolean enableWriteAheadLogging()
  {
    synchronized (mLock)
    {
      throwIfNotOpenLocked();
      if ((mConfigurationLocked.openFlags & 0x20000000) != 0) {
        return true;
      }
      if (isReadOnlyLocked()) {
        return false;
      }
      if (mConfigurationLocked.isInMemoryDb())
      {
        Log.i("SQLiteDatabase", "can't enable WAL for memory databases.");
        return false;
      }
      if (mHasAttachedDbsLocked)
      {
        if (Log.isLoggable("SQLiteDatabase", 3))
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("this database: ");
          ((StringBuilder)localObject2).append(mConfigurationLocked.label);
          ((StringBuilder)localObject2).append(" has attached databases. can't  enable WAL.");
          Log.d("SQLiteDatabase", ((StringBuilder)localObject2).toString());
        }
        return false;
      }
      Object localObject2 = mConfigurationLocked;
      openFlags = (0x20000000 | openFlags);
      try
      {
        mConnectionPoolLocked.reconfigure(mConfigurationLocked);
        return true;
      }
      catch (RuntimeException localRuntimeException)
      {
        localObject2 = mConfigurationLocked;
        openFlags &= 0xDFFFFFFF;
        throw localRuntimeException;
      }
    }
  }
  
  public void endTransaction()
  {
    acquireReference();
    try
    {
      getThreadSession().endTransaction(null);
      return;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public void execSQL(String paramString)
    throws SQLException
  {
    executeSql(paramString, null);
  }
  
  public void execSQL(String paramString, Object[] paramArrayOfObject)
    throws SQLException
  {
    if (paramArrayOfObject != null)
    {
      executeSql(paramString, paramArrayOfObject);
      return;
    }
    throw new IllegalArgumentException("Empty bindArgs");
  }
  
  /* Error */
  public int executeSql(String paramString, Object[] paramArrayOfObject)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 207	android/database/sqlite/SQLiteDatabase:acquireReference	()V
    //   4: aload_1
    //   5: invokestatic 597	android/database/DatabaseUtils:getSqlStatementType	(Ljava/lang/String;)I
    //   8: istore_3
    //   9: iload_3
    //   10: iconst_3
    //   11: if_icmpne +58 -> 69
    //   14: iconst_0
    //   15: istore 4
    //   17: aload_0
    //   18: getfield 135	android/database/sqlite/SQLiteDatabase:mLock	Ljava/lang/Object;
    //   21: astore 5
    //   23: aload 5
    //   25: monitorenter
    //   26: aload_0
    //   27: getfield 566	android/database/sqlite/SQLiteDatabase:mHasAttachedDbsLocked	Z
    //   30: ifne +18 -> 48
    //   33: aload_0
    //   34: iconst_1
    //   35: putfield 566	android/database/sqlite/SQLiteDatabase:mHasAttachedDbsLocked	Z
    //   38: iconst_1
    //   39: istore 4
    //   41: aload_0
    //   42: getfield 227	android/database/sqlite/SQLiteDatabase:mConnectionPoolLocked	Landroid/database/sqlite/SQLiteConnectionPool;
    //   45: invokevirtual 600	android/database/sqlite/SQLiteConnectionPool:disableIdleConnectionHandler	()V
    //   48: aload 5
    //   50: monitorexit
    //   51: iload 4
    //   53: ifeq +16 -> 69
    //   56: aload_0
    //   57: invokevirtual 602	android/database/sqlite/SQLiteDatabase:disableWriteAheadLogging	()V
    //   60: goto +9 -> 69
    //   63: astore_1
    //   64: aload 5
    //   66: monitorexit
    //   67: aload_1
    //   68: athrow
    //   69: new 536	android/database/sqlite/SQLiteStatement
    //   72: astore 5
    //   74: aload 5
    //   76: aload_0
    //   77: aload_1
    //   78: aload_2
    //   79: invokespecial 539	android/database/sqlite/SQLiteStatement:<init>	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;[Ljava/lang/Object;)V
    //   82: aconst_null
    //   83: astore_1
    //   84: aload 5
    //   86: invokevirtual 552	android/database/sqlite/SQLiteStatement:executeUpdateDelete	()I
    //   89: istore 4
    //   91: aload 5
    //   93: invokevirtual 553	android/database/sqlite/SQLiteStatement:close	()V
    //   96: iload_3
    //   97: bipush 8
    //   99: if_icmpne +10 -> 109
    //   102: aload_0
    //   103: getfield 227	android/database/sqlite/SQLiteDatabase:mConnectionPoolLocked	Landroid/database/sqlite/SQLiteConnectionPool;
    //   106: invokevirtual 605	android/database/sqlite/SQLiteConnectionPool:closeAvailableNonPrimaryConnectionsAndLogExceptions	()V
    //   109: aload_0
    //   110: invokevirtual 223	android/database/sqlite/SQLiteDatabase:releaseReference	()V
    //   113: iload 4
    //   115: ireturn
    //   116: astore_2
    //   117: goto +8 -> 125
    //   120: astore_2
    //   121: aload_2
    //   122: astore_1
    //   123: aload_2
    //   124: athrow
    //   125: aload_1
    //   126: ifnull +22 -> 148
    //   129: aload 5
    //   131: invokevirtual 553	android/database/sqlite/SQLiteStatement:close	()V
    //   134: goto +19 -> 153
    //   137: astore 5
    //   139: aload_1
    //   140: aload 5
    //   142: invokevirtual 609	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   145: goto +8 -> 153
    //   148: aload 5
    //   150: invokevirtual 553	android/database/sqlite/SQLiteStatement:close	()V
    //   153: aload_2
    //   154: athrow
    //   155: astore_1
    //   156: iload_3
    //   157: bipush 8
    //   159: if_icmpne +10 -> 169
    //   162: aload_0
    //   163: getfield 227	android/database/sqlite/SQLiteDatabase:mConnectionPoolLocked	Landroid/database/sqlite/SQLiteConnectionPool;
    //   166: invokevirtual 605	android/database/sqlite/SQLiteConnectionPool:closeAvailableNonPrimaryConnectionsAndLogExceptions	()V
    //   169: aload_1
    //   170: athrow
    //   171: astore_1
    //   172: aload_0
    //   173: invokevirtual 223	android/database/sqlite/SQLiteDatabase:releaseReference	()V
    //   176: aload_1
    //   177: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	178	0	this	SQLiteDatabase
    //   0	178	1	paramString	String
    //   0	178	2	paramArrayOfObject	Object[]
    //   8	152	3	i	int
    //   15	99	4	j	int
    //   21	109	5	localObject	Object
    //   137	12	5	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   26	38	63	finally
    //   41	48	63	finally
    //   48	51	63	finally
    //   64	67	63	finally
    //   84	91	116	finally
    //   123	125	116	finally
    //   84	91	120	java/lang/Throwable
    //   129	134	137	java/lang/Throwable
    //   69	82	155	finally
    //   91	96	155	finally
    //   129	134	155	finally
    //   139	145	155	finally
    //   148	153	155	finally
    //   153	155	155	finally
    //   4	9	171	finally
    //   17	26	171	finally
    //   56	60	171	finally
    //   67	69	171	finally
    //   102	109	171	finally
    //   162	169	171	finally
    //   169	171	171	finally
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      dispose(true);
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public List<Pair<String, String>> getAttachedDbs()
  {
    ArrayList localArrayList = new ArrayList();
    synchronized (mLock)
    {
      if (mConnectionPoolLocked == null) {
        return null;
      }
      Object localObject3;
      if (!mHasAttachedDbsLocked)
      {
        localObject3 = new android/util/Pair;
        ((Pair)localObject3).<init>("main", mConfigurationLocked.path);
        localArrayList.add(localObject3);
        return localArrayList;
      }
      acquireReference();
      ??? = null;
      try
      {
        localObject3 = rawQuery("pragma database_list;", null);
        for (;;)
        {
          ??? = localObject3;
          if (!((Cursor)localObject3).moveToNext()) {
            break;
          }
          ??? = localObject3;
          Pair localPair = new android/util/Pair;
          ??? = localObject3;
          localPair.<init>(((Cursor)localObject3).getString(1), ((Cursor)localObject3).getString(2));
          ??? = localObject3;
          localArrayList.add(localPair);
        }
        if (localObject3 != null) {}
        releaseReference();
      }
      finally
      {
        try
        {
          ((Cursor)localObject3).close();
          releaseReference();
          return localArrayList;
        }
        finally
        {
          break label165;
        }
        localObject4 = finally;
        if (??? != null) {
          ???.close();
        }
      }
      label165:
      throw localObject2;
    }
  }
  
  String getLabel()
  {
    synchronized (mLock)
    {
      String str = mConfigurationLocked.label;
      return str;
    }
  }
  
  public long getMaximumSize()
  {
    long l = DatabaseUtils.longForQuery(this, "PRAGMA max_page_count;", null);
    return getPageSize() * l;
  }
  
  public long getPageSize()
  {
    return DatabaseUtils.longForQuery(this, "PRAGMA page_size;", null);
  }
  
  public final String getPath()
  {
    synchronized (mLock)
    {
      String str = mConfigurationLocked.path;
      return str;
    }
  }
  
  @Deprecated
  public Map<String, String> getSyncedTables()
  {
    return new HashMap(0);
  }
  
  int getThreadDefaultConnectionFlags(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 1;
    } else {
      i = 2;
    }
    int j = i;
    if (isMainThread()) {
      j = i | 0x4;
    }
    return j;
  }
  
  SQLiteSession getThreadSession()
  {
    return (SQLiteSession)mThreadSession.get();
  }
  
  public int getVersion()
  {
    return Long.valueOf(DatabaseUtils.longForQuery(this, "PRAGMA user_version;", null)).intValue();
  }
  
  public boolean inTransaction()
  {
    acquireReference();
    try
    {
      boolean bool = getThreadSession().hasTransaction();
      return bool;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public long insert(String paramString1, String paramString2, ContentValues paramContentValues)
  {
    try
    {
      long l = insertWithOnConflict(paramString1, paramString2, paramContentValues, 0);
      return l;
    }
    catch (SQLException paramString1)
    {
      paramString2 = new StringBuilder();
      paramString2.append("Error inserting ");
      paramString2.append(paramContentValues);
      Log.e("SQLiteDatabase", paramString2.toString(), paramString1);
    }
    return -1L;
  }
  
  public long insertOrThrow(String paramString1, String paramString2, ContentValues paramContentValues)
    throws SQLException
  {
    return insertWithOnConflict(paramString1, paramString2, paramContentValues, 0);
  }
  
  /* Error */
  public long insertWithOnConflict(String paramString1, String paramString2, ContentValues paramContentValues, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 207	android/database/sqlite/SQLiteDatabase:acquireReference	()V
    //   4: new 267	java/lang/StringBuilder
    //   7: astore 5
    //   9: aload 5
    //   11: invokespecial 268	java/lang/StringBuilder:<init>	()V
    //   14: aload 5
    //   16: ldc_w 700
    //   19: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: pop
    //   23: aload 5
    //   25: getstatic 114	android/database/sqlite/SQLiteDatabase:CONFLICT_VALUES	[Ljava/lang/String;
    //   28: iload 4
    //   30: aaload
    //   31: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: pop
    //   35: aload 5
    //   37: ldc_w 702
    //   40: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: pop
    //   44: aload 5
    //   46: aload_1
    //   47: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   50: pop
    //   51: aload 5
    //   53: bipush 40
    //   55: invokevirtual 705	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
    //   58: pop
    //   59: aconst_null
    //   60: astore_1
    //   61: aload_3
    //   62: ifnull +19 -> 81
    //   65: aload_3
    //   66: invokevirtual 709	android/content/ContentValues:isEmpty	()Z
    //   69: ifne +12 -> 81
    //   72: aload_3
    //   73: invokevirtual 712	android/content/ContentValues:size	()I
    //   76: istore 4
    //   78: goto +6 -> 84
    //   81: iconst_0
    //   82: istore 4
    //   84: iload 4
    //   86: ifle +152 -> 238
    //   89: iload 4
    //   91: anewarray 132	java/lang/Object
    //   94: astore_2
    //   95: iconst_0
    //   96: istore 6
    //   98: aload_3
    //   99: invokevirtual 713	android/content/ContentValues:keySet	()Ljava/util/Set;
    //   102: invokeinterface 716 1 0
    //   107: astore 7
    //   109: aload 7
    //   111: invokeinterface 345 1 0
    //   116: ifeq +61 -> 177
    //   119: aload 7
    //   121: invokeinterface 349 1 0
    //   126: checkcast 100	java/lang/String
    //   129: astore 8
    //   131: iload 6
    //   133: ifle +10 -> 143
    //   136: ldc_w 718
    //   139: astore_1
    //   140: goto +6 -> 146
    //   143: ldc 102
    //   145: astore_1
    //   146: aload 5
    //   148: aload_1
    //   149: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: pop
    //   153: aload 5
    //   155: aload 8
    //   157: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: pop
    //   161: aload_2
    //   162: iload 6
    //   164: aload_3
    //   165: aload 8
    //   167: invokevirtual 721	android/content/ContentValues:get	(Ljava/lang/String;)Ljava/lang/Object;
    //   170: aastore
    //   171: iinc 6 1
    //   174: goto -65 -> 109
    //   177: aload 5
    //   179: bipush 41
    //   181: invokevirtual 705	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload 5
    //   187: ldc_w 723
    //   190: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: pop
    //   194: iconst_0
    //   195: istore 6
    //   197: iload 6
    //   199: iload 4
    //   201: if_icmpge +32 -> 233
    //   204: iload 6
    //   206: ifle +10 -> 216
    //   209: ldc_w 725
    //   212: astore_1
    //   213: goto +7 -> 220
    //   216: ldc_w 727
    //   219: astore_1
    //   220: aload 5
    //   222: aload_1
    //   223: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   226: pop
    //   227: iinc 6 1
    //   230: goto -33 -> 197
    //   233: aload_2
    //   234: astore_1
    //   235: goto +35 -> 270
    //   238: new 267	java/lang/StringBuilder
    //   241: astore_3
    //   242: aload_3
    //   243: invokespecial 268	java/lang/StringBuilder:<init>	()V
    //   246: aload_3
    //   247: aload_2
    //   248: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   251: pop
    //   252: aload_3
    //   253: ldc_w 729
    //   256: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   259: pop
    //   260: aload 5
    //   262: aload_3
    //   263: invokevirtual 281	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   266: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   269: pop
    //   270: aload 5
    //   272: bipush 41
    //   274: invokevirtual 705	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
    //   277: pop
    //   278: new 536	android/database/sqlite/SQLiteStatement
    //   281: astore_2
    //   282: aload_2
    //   283: aload_0
    //   284: aload 5
    //   286: invokevirtual 281	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   289: aload_1
    //   290: invokespecial 539	android/database/sqlite/SQLiteStatement:<init>	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;[Ljava/lang/Object;)V
    //   293: aload_2
    //   294: invokevirtual 732	android/database/sqlite/SQLiteStatement:executeInsert	()J
    //   297: lstore 9
    //   299: aload_2
    //   300: invokevirtual 553	android/database/sqlite/SQLiteStatement:close	()V
    //   303: aload_0
    //   304: invokevirtual 223	android/database/sqlite/SQLiteDatabase:releaseReference	()V
    //   307: lload 9
    //   309: lreturn
    //   310: astore_1
    //   311: aload_2
    //   312: invokevirtual 553	android/database/sqlite/SQLiteStatement:close	()V
    //   315: aload_1
    //   316: athrow
    //   317: astore_1
    //   318: aload_0
    //   319: invokevirtual 223	android/database/sqlite/SQLiteDatabase:releaseReference	()V
    //   322: aload_1
    //   323: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	324	0	this	SQLiteDatabase
    //   0	324	1	paramString1	String
    //   0	324	2	paramString2	String
    //   0	324	3	paramContentValues	ContentValues
    //   0	324	4	paramInt	int
    //   7	278	5	localStringBuilder	StringBuilder
    //   96	132	6	i	int
    //   107	13	7	localIterator	Iterator
    //   129	37	8	str	String
    //   297	11	9	l	long
    // Exception table:
    //   from	to	target	type
    //   293	299	310	finally
    //   4	59	317	finally
    //   65	78	317	finally
    //   89	95	317	finally
    //   98	109	317	finally
    //   109	131	317	finally
    //   146	171	317	finally
    //   177	194	317	finally
    //   220	227	317	finally
    //   238	270	317	finally
    //   270	293	317	finally
    //   299	303	317	finally
    //   311	317	317	finally
  }
  
  /* Error */
  public boolean isDatabaseIntegrityOk()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 207	android/database/sqlite/SQLiteDatabase:acquireReference	()V
    //   4: aload_0
    //   5: invokevirtual 735	android/database/sqlite/SQLiteDatabase:getAttachedDbs	()Ljava/util/List;
    //   8: astore_1
    //   9: aload_1
    //   10: ifnull +6 -> 16
    //   13: goto +86 -> 99
    //   16: new 368	java/lang/IllegalStateException
    //   19: astore_2
    //   20: new 267	java/lang/StringBuilder
    //   23: astore_1
    //   24: aload_1
    //   25: invokespecial 268	java/lang/StringBuilder:<init>	()V
    //   28: aload_1
    //   29: ldc_w 737
    //   32: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   35: pop
    //   36: aload_1
    //   37: aload_0
    //   38: invokevirtual 738	android/database/sqlite/SQLiteDatabase:getPath	()Ljava/lang/String;
    //   41: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   44: pop
    //   45: aload_1
    //   46: ldc_w 740
    //   49: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: pop
    //   53: aload_2
    //   54: aload_1
    //   55: invokevirtual 281	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   58: invokespecial 371	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   61: aload_2
    //   62: athrow
    //   63: astore_2
    //   64: goto +281 -> 345
    //   67: astore_2
    //   68: new 336	java/util/ArrayList
    //   71: astore_1
    //   72: aload_1
    //   73: invokespecial 372	java/util/ArrayList:<init>	()V
    //   76: new 618	android/util/Pair
    //   79: astore_2
    //   80: aload_2
    //   81: ldc_w 620
    //   84: aload_0
    //   85: invokevirtual 738	android/database/sqlite/SQLiteDatabase:getPath	()Ljava/lang/String;
    //   88: invokespecial 626	android/util/Pair:<init>	(Ljava/lang/Object;Ljava/lang/Object;)V
    //   91: aload_1
    //   92: aload_2
    //   93: invokeinterface 743 2 0
    //   98: pop
    //   99: iconst_0
    //   100: istore_3
    //   101: iload_3
    //   102: aload_1
    //   103: invokeinterface 744 1 0
    //   108: if_icmpge +231 -> 339
    //   111: aload_1
    //   112: iload_3
    //   113: invokeinterface 747 2 0
    //   118: checkcast 618	android/util/Pair
    //   121: astore 4
    //   123: aconst_null
    //   124: astore 5
    //   126: aload 5
    //   128: astore_2
    //   129: new 267	java/lang/StringBuilder
    //   132: astore 6
    //   134: aload 5
    //   136: astore_2
    //   137: aload 6
    //   139: invokespecial 268	java/lang/StringBuilder:<init>	()V
    //   142: aload 5
    //   144: astore_2
    //   145: aload 6
    //   147: ldc_w 749
    //   150: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   153: pop
    //   154: aload 5
    //   156: astore_2
    //   157: aload 6
    //   159: aload 4
    //   161: getfield 752	android/util/Pair:first	Ljava/lang/Object;
    //   164: checkcast 100	java/lang/String
    //   167: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   170: pop
    //   171: aload 5
    //   173: astore_2
    //   174: aload 6
    //   176: ldc_w 754
    //   179: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   182: pop
    //   183: aload 5
    //   185: astore_2
    //   186: aload_0
    //   187: aload 6
    //   189: invokevirtual 281	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   192: invokevirtual 756	android/database/sqlite/SQLiteDatabase:compileStatement	(Ljava/lang/String;)Landroid/database/sqlite/SQLiteStatement;
    //   195: astore 5
    //   197: aload 5
    //   199: astore_2
    //   200: aload 5
    //   202: invokevirtual 759	android/database/sqlite/SQLiteStatement:simpleQueryForString	()Ljava/lang/String;
    //   205: astore 6
    //   207: aload 5
    //   209: astore_2
    //   210: aload 6
    //   212: ldc_w 761
    //   215: invokevirtual 765	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   218: ifne +94 -> 312
    //   221: aload 5
    //   223: astore_2
    //   224: new 267	java/lang/StringBuilder
    //   227: astore_1
    //   228: aload 5
    //   230: astore_2
    //   231: aload_1
    //   232: invokespecial 268	java/lang/StringBuilder:<init>	()V
    //   235: aload 5
    //   237: astore_2
    //   238: aload_1
    //   239: ldc_w 767
    //   242: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   245: pop
    //   246: aload 5
    //   248: astore_2
    //   249: aload_1
    //   250: aload 4
    //   252: getfield 770	android/util/Pair:second	Ljava/lang/Object;
    //   255: checkcast 100	java/lang/String
    //   258: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   261: pop
    //   262: aload 5
    //   264: astore_2
    //   265: aload_1
    //   266: ldc_w 772
    //   269: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   272: pop
    //   273: aload 5
    //   275: astore_2
    //   276: aload_1
    //   277: aload 6
    //   279: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   282: pop
    //   283: aload 5
    //   285: astore_2
    //   286: ldc 60
    //   288: aload_1
    //   289: invokevirtual 281	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   292: invokestatic 774	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   295: pop
    //   296: aload 5
    //   298: ifnull +8 -> 306
    //   301: aload 5
    //   303: invokevirtual 553	android/database/sqlite/SQLiteStatement:close	()V
    //   306: aload_0
    //   307: invokevirtual 223	android/database/sqlite/SQLiteDatabase:releaseReference	()V
    //   310: iconst_0
    //   311: ireturn
    //   312: aload 5
    //   314: ifnull +8 -> 322
    //   317: aload 5
    //   319: invokevirtual 553	android/database/sqlite/SQLiteStatement:close	()V
    //   322: iinc 3 1
    //   325: goto -224 -> 101
    //   328: astore_1
    //   329: aload_2
    //   330: ifnull +7 -> 337
    //   333: aload_2
    //   334: invokevirtual 553	android/database/sqlite/SQLiteStatement:close	()V
    //   337: aload_1
    //   338: athrow
    //   339: aload_0
    //   340: invokevirtual 223	android/database/sqlite/SQLiteDatabase:releaseReference	()V
    //   343: iconst_1
    //   344: ireturn
    //   345: aload_0
    //   346: invokevirtual 223	android/database/sqlite/SQLiteDatabase:releaseReference	()V
    //   349: aload_2
    //   350: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	351	0	this	SQLiteDatabase
    //   8	281	1	localObject1	Object
    //   328	10	1	localObject2	Object
    //   19	43	2	localIllegalStateException	IllegalStateException
    //   63	1	2	localObject3	Object
    //   67	1	2	localSQLiteException	SQLiteException
    //   79	271	2	localObject4	Object
    //   100	223	3	i	int
    //   121	130	4	localPair	Pair
    //   124	194	5	localSQLiteStatement	SQLiteStatement
    //   132	146	6	localObject5	Object
    // Exception table:
    //   from	to	target	type
    //   4	9	63	finally
    //   16	63	63	finally
    //   68	76	63	finally
    //   76	99	63	finally
    //   101	123	63	finally
    //   301	306	63	finally
    //   317	322	63	finally
    //   333	337	63	finally
    //   337	339	63	finally
    //   4	9	67	android/database/sqlite/SQLiteException
    //   16	63	67	android/database/sqlite/SQLiteException
    //   129	134	328	finally
    //   137	142	328	finally
    //   145	154	328	finally
    //   157	171	328	finally
    //   174	183	328	finally
    //   186	197	328	finally
    //   200	207	328	finally
    //   210	221	328	finally
    //   224	228	328	finally
    //   231	235	328	finally
    //   238	246	328	finally
    //   249	262	328	finally
    //   265	273	328	finally
    //   276	283	328	finally
    //   286	296	328	finally
  }
  
  public boolean isDbLockedByCurrentThread()
  {
    acquireReference();
    try
    {
      boolean bool = getThreadSession().hasConnection();
      return bool;
    }
    finally
    {
      releaseReference();
    }
  }
  
  @Deprecated
  public boolean isDbLockedByOtherThreads()
  {
    return false;
  }
  
  public boolean isInMemoryDatabase()
  {
    synchronized (mLock)
    {
      boolean bool = mConfigurationLocked.isInMemoryDb();
      return bool;
    }
  }
  
  public boolean isOpen()
  {
    synchronized (mLock)
    {
      boolean bool;
      if (mConnectionPoolLocked != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public boolean isReadOnly()
  {
    synchronized (mLock)
    {
      boolean bool = isReadOnlyLocked();
      return bool;
    }
  }
  
  public boolean isWriteAheadLoggingEnabled()
  {
    synchronized (mLock)
    {
      throwIfNotOpenLocked();
      boolean bool;
      if ((mConfigurationLocked.openFlags & 0x20000000) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  @Deprecated
  public void markTableSyncable(String paramString1, String paramString2) {}
  
  @Deprecated
  public void markTableSyncable(String paramString1, String paramString2, String paramString3) {}
  
  public boolean needUpgrade(int paramInt)
  {
    boolean bool;
    if (paramInt > getVersion()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void onAllReferencesReleased()
  {
    dispose(false);
  }
  
  void onCorruption()
  {
    EventLog.writeEvent(75004, getLabel());
    mErrorHandler.onCorruption(this);
  }
  
  public Cursor query(String paramString1, String[] paramArrayOfString1, String paramString2, String[] paramArrayOfString2, String paramString3, String paramString4, String paramString5)
  {
    return query(false, paramString1, paramArrayOfString1, paramString2, paramArrayOfString2, paramString3, paramString4, paramString5, null);
  }
  
  public Cursor query(String paramString1, String[] paramArrayOfString1, String paramString2, String[] paramArrayOfString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    return query(false, paramString1, paramArrayOfString1, paramString2, paramArrayOfString2, paramString3, paramString4, paramString5, paramString6);
  }
  
  public Cursor query(boolean paramBoolean, String paramString1, String[] paramArrayOfString1, String paramString2, String[] paramArrayOfString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    return queryWithFactory(null, paramBoolean, paramString1, paramArrayOfString1, paramString2, paramArrayOfString2, paramString3, paramString4, paramString5, paramString6, null);
  }
  
  public Cursor query(boolean paramBoolean, String paramString1, String[] paramArrayOfString1, String paramString2, String[] paramArrayOfString2, String paramString3, String paramString4, String paramString5, String paramString6, CancellationSignal paramCancellationSignal)
  {
    return queryWithFactory(null, paramBoolean, paramString1, paramArrayOfString1, paramString2, paramArrayOfString2, paramString3, paramString4, paramString5, paramString6, paramCancellationSignal);
  }
  
  public Cursor queryWithFactory(CursorFactory paramCursorFactory, boolean paramBoolean, String paramString1, String[] paramArrayOfString1, String paramString2, String[] paramArrayOfString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    return queryWithFactory(paramCursorFactory, paramBoolean, paramString1, paramArrayOfString1, paramString2, paramArrayOfString2, paramString3, paramString4, paramString5, paramString6, null);
  }
  
  public Cursor queryWithFactory(CursorFactory paramCursorFactory, boolean paramBoolean, String paramString1, String[] paramArrayOfString1, String paramString2, String[] paramArrayOfString2, String paramString3, String paramString4, String paramString5, String paramString6, CancellationSignal paramCancellationSignal)
  {
    acquireReference();
    try
    {
      paramArrayOfString1 = SQLiteQueryBuilder.buildQueryString(paramBoolean, paramString1, paramArrayOfString1, paramString2, paramString3, paramString4, paramString5, paramString6);
      paramCursorFactory = rawQueryWithFactory(paramCursorFactory, paramArrayOfString1, paramArrayOfString2, findEditTable(paramString1), paramCancellationSignal);
      return paramCursorFactory;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public Cursor rawQuery(String paramString, String[] paramArrayOfString)
  {
    return rawQueryWithFactory(null, paramString, paramArrayOfString, null, null);
  }
  
  public Cursor rawQuery(String paramString, String[] paramArrayOfString, CancellationSignal paramCancellationSignal)
  {
    return rawQueryWithFactory(null, paramString, paramArrayOfString, null, paramCancellationSignal);
  }
  
  public Cursor rawQueryWithFactory(CursorFactory paramCursorFactory, String paramString1, String[] paramArrayOfString, String paramString2)
  {
    return rawQueryWithFactory(paramCursorFactory, paramString1, paramArrayOfString, paramString2, null);
  }
  
  public Cursor rawQueryWithFactory(CursorFactory paramCursorFactory, String paramString1, String[] paramArrayOfString, String paramString2, CancellationSignal paramCancellationSignal)
  {
    acquireReference();
    try
    {
      SQLiteDirectCursorDriver localSQLiteDirectCursorDriver = new android/database/sqlite/SQLiteDirectCursorDriver;
      localSQLiteDirectCursorDriver.<init>(this, paramString1, paramString2, paramCancellationSignal);
      if (paramCursorFactory == null) {
        paramCursorFactory = mCursorFactory;
      }
      paramCursorFactory = localSQLiteDirectCursorDriver.query(paramCursorFactory, paramArrayOfString);
      return paramCursorFactory;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public void reopenReadWrite()
  {
    synchronized (mLock)
    {
      throwIfNotOpenLocked();
      if (!isReadOnlyLocked()) {
        return;
      }
      int i = mConfigurationLocked.openFlags;
      mConfigurationLocked.openFlags = (mConfigurationLocked.openFlags & 0xFFFFFFFE | 0x0);
      try
      {
        mConnectionPoolLocked.reconfigure(mConfigurationLocked);
        return;
      }
      catch (RuntimeException localRuntimeException)
      {
        mConfigurationLocked.openFlags = i;
        throw localRuntimeException;
      }
    }
  }
  
  public long replace(String paramString1, String paramString2, ContentValues paramContentValues)
  {
    try
    {
      long l = insertWithOnConflict(paramString1, paramString2, paramContentValues, 5);
      return l;
    }
    catch (SQLException paramString1)
    {
      paramString2 = new StringBuilder();
      paramString2.append("Error inserting ");
      paramString2.append(paramContentValues);
      Log.e("SQLiteDatabase", paramString2.toString(), paramString1);
    }
    return -1L;
  }
  
  public long replaceOrThrow(String paramString1, String paramString2, ContentValues paramContentValues)
    throws SQLException
  {
    return insertWithOnConflict(paramString1, paramString2, paramContentValues, 5);
  }
  
  public void setForeignKeyConstraintsEnabled(boolean paramBoolean)
  {
    synchronized (mLock)
    {
      throwIfNotOpenLocked();
      if (mConfigurationLocked.foreignKeyConstraintsEnabled == paramBoolean) {
        return;
      }
      mConfigurationLocked.foreignKeyConstraintsEnabled = paramBoolean;
      try
      {
        mConnectionPoolLocked.reconfigure(mConfigurationLocked);
        return;
      }
      catch (RuntimeException localRuntimeException)
      {
        mConfigurationLocked.foreignKeyConstraintsEnabled = (paramBoolean ^ true);
        throw localRuntimeException;
      }
    }
  }
  
  public void setLocale(Locale paramLocale)
  {
    if (paramLocale != null) {
      synchronized (mLock)
      {
        throwIfNotOpenLocked();
        Locale localLocale = mConfigurationLocked.locale;
        mConfigurationLocked.locale = paramLocale;
        try
        {
          mConnectionPoolLocked.reconfigure(mConfigurationLocked);
          return;
        }
        catch (RuntimeException paramLocale)
        {
          mConfigurationLocked.locale = localLocale;
          throw paramLocale;
        }
      }
    }
    throw new IllegalArgumentException("locale must not be null.");
  }
  
  @Deprecated
  public void setLockingEnabled(boolean paramBoolean) {}
  
  public void setMaxSqlCacheSize(int paramInt)
  {
    if ((paramInt <= 100) && (paramInt >= 0)) {
      synchronized (mLock)
      {
        throwIfNotOpenLocked();
        int i = mConfigurationLocked.maxSqlCacheSize;
        mConfigurationLocked.maxSqlCacheSize = paramInt;
        try
        {
          mConnectionPoolLocked.reconfigure(mConfigurationLocked);
          return;
        }
        catch (RuntimeException localRuntimeException)
        {
          mConfigurationLocked.maxSqlCacheSize = i;
          throw localRuntimeException;
        }
      }
    }
    throw new IllegalStateException("expected value between 0 and 100");
  }
  
  public long setMaximumSize(long paramLong)
  {
    long l1 = getPageSize();
    long l2 = paramLong / l1;
    long l3 = l2;
    if (paramLong % l1 != 0L) {
      l3 = l2 + 1L;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PRAGMA max_page_count = ");
    localStringBuilder.append(l3);
    return DatabaseUtils.longForQuery(this, localStringBuilder.toString(), null) * l1;
  }
  
  public void setPageSize(long paramLong)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PRAGMA page_size = ");
    localStringBuilder.append(paramLong);
    execSQL(localStringBuilder.toString());
  }
  
  public void setTransactionSuccessful()
  {
    acquireReference();
    try
    {
      getThreadSession().setTransactionSuccessful();
      return;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public void setVersion(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PRAGMA user_version = ");
    localStringBuilder.append(paramInt);
    execSQL(localStringBuilder.toString());
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SQLiteDatabase: ");
    localStringBuilder.append(getPath());
    return localStringBuilder.toString();
  }
  
  public int update(String paramString1, ContentValues paramContentValues, String paramString2, String[] paramArrayOfString)
  {
    return updateWithOnConflict(paramString1, paramContentValues, paramString2, paramArrayOfString, 0);
  }
  
  public int updateWithOnConflict(String paramString1, ContentValues paramContentValues, String paramString2, String[] paramArrayOfString, int paramInt)
  {
    if ((paramContentValues != null) && (!paramContentValues.isEmpty()))
    {
      acquireReference();
      try
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>(120);
        localStringBuilder.append("UPDATE ");
        localStringBuilder.append(CONFLICT_VALUES[paramInt]);
        localStringBuilder.append(paramString1);
        localStringBuilder.append(" SET ");
        paramInt = paramContentValues.size();
        int i;
        if (paramArrayOfString == null) {
          i = paramInt;
        } else {
          i = paramArrayOfString.length + paramInt;
        }
        Object[] arrayOfObject = new Object[i];
        int j = 0;
        Iterator localIterator = paramContentValues.keySet().iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          if (j > 0) {
            paramString1 = ",";
          } else {
            paramString1 = "";
          }
          localStringBuilder.append(paramString1);
          localStringBuilder.append(str);
          arrayOfObject[j] = paramContentValues.get(str);
          localStringBuilder.append("=?");
          j++;
        }
        if (paramArrayOfString != null) {
          for (j = paramInt; j < i; j++) {
            arrayOfObject[j] = paramArrayOfString[(j - paramInt)];
          }
        }
        if (!TextUtils.isEmpty(paramString2))
        {
          localStringBuilder.append(" WHERE ");
          localStringBuilder.append(paramString2);
        }
        paramString1 = new android/database/sqlite/SQLiteStatement;
        paramString1.<init>(this, localStringBuilder.toString(), arrayOfObject);
        try
        {
          paramInt = paramString1.executeUpdateDelete();
          return paramInt;
        }
        finally {}
        throw new IllegalArgumentException("Empty values");
      }
      finally
      {
        releaseReference();
      }
    }
  }
  
  public void validateSql(String paramString, CancellationSignal paramCancellationSignal)
  {
    getThreadSession().prepare(paramString, getThreadDefaultConnectionFlags(true), paramCancellationSignal, null);
  }
  
  @Deprecated
  public boolean yieldIfContended()
  {
    return yieldIfContendedHelper(false, -1L);
  }
  
  public boolean yieldIfContendedSafely()
  {
    return yieldIfContendedHelper(true, -1L);
  }
  
  public boolean yieldIfContendedSafely(long paramLong)
  {
    return yieldIfContendedHelper(true, paramLong);
  }
  
  public static abstract interface CursorFactory
  {
    public abstract Cursor newCursor(SQLiteDatabase paramSQLiteDatabase, SQLiteCursorDriver paramSQLiteCursorDriver, String paramString, SQLiteQuery paramSQLiteQuery);
  }
  
  public static abstract interface CustomFunction
  {
    public abstract void callback(String[] paramArrayOfString);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DatabaseOpenFlags {}
  
  public static final class OpenParams
  {
    private final SQLiteDatabase.CursorFactory mCursorFactory;
    private final DatabaseErrorHandler mErrorHandler;
    private final long mIdleConnectionTimeout;
    private final String mJournalMode;
    private final int mLookasideSlotCount;
    private final int mLookasideSlotSize;
    private final int mOpenFlags;
    private final String mSyncMode;
    
    private OpenParams(int paramInt1, SQLiteDatabase.CursorFactory paramCursorFactory, DatabaseErrorHandler paramDatabaseErrorHandler, int paramInt2, int paramInt3, long paramLong, String paramString1, String paramString2)
    {
      mOpenFlags = paramInt1;
      mCursorFactory = paramCursorFactory;
      mErrorHandler = paramDatabaseErrorHandler;
      mLookasideSlotSize = paramInt2;
      mLookasideSlotCount = paramInt3;
      mIdleConnectionTimeout = paramLong;
      mJournalMode = paramString1;
      mSyncMode = paramString2;
    }
    
    public SQLiteDatabase.CursorFactory getCursorFactory()
    {
      return mCursorFactory;
    }
    
    public DatabaseErrorHandler getErrorHandler()
    {
      return mErrorHandler;
    }
    
    public long getIdleConnectionTimeout()
    {
      return mIdleConnectionTimeout;
    }
    
    public String getJournalMode()
    {
      return mJournalMode;
    }
    
    public int getLookasideSlotCount()
    {
      return mLookasideSlotCount;
    }
    
    public int getLookasideSlotSize()
    {
      return mLookasideSlotSize;
    }
    
    public int getOpenFlags()
    {
      return mOpenFlags;
    }
    
    public String getSynchronousMode()
    {
      return mSyncMode;
    }
    
    public Builder toBuilder()
    {
      return new Builder(this);
    }
    
    public static final class Builder
    {
      private SQLiteDatabase.CursorFactory mCursorFactory;
      private DatabaseErrorHandler mErrorHandler;
      private long mIdleConnectionTimeout = -1L;
      private String mJournalMode;
      private int mLookasideSlotCount = -1;
      private int mLookasideSlotSize = -1;
      private int mOpenFlags;
      private String mSyncMode;
      
      public Builder() {}
      
      public Builder(SQLiteDatabase.OpenParams paramOpenParams)
      {
        mLookasideSlotSize = mLookasideSlotSize;
        mLookasideSlotCount = mLookasideSlotCount;
        mOpenFlags = mOpenFlags;
        mCursorFactory = mCursorFactory;
        mErrorHandler = mErrorHandler;
        mJournalMode = mJournalMode;
        mSyncMode = mSyncMode;
      }
      
      public Builder addOpenFlags(int paramInt)
      {
        mOpenFlags |= paramInt;
        return this;
      }
      
      public SQLiteDatabase.OpenParams build()
      {
        return new SQLiteDatabase.OpenParams(mOpenFlags, mCursorFactory, mErrorHandler, mLookasideSlotSize, mLookasideSlotCount, mIdleConnectionTimeout, mJournalMode, mSyncMode, null);
      }
      
      public boolean isWriteAheadLoggingEnabled()
      {
        boolean bool;
        if ((mOpenFlags & 0x20000000) != 0) {
          bool = true;
        } else {
          bool = false;
        }
        return bool;
      }
      
      public Builder removeOpenFlags(int paramInt)
      {
        mOpenFlags &= paramInt;
        return this;
      }
      
      public Builder setCursorFactory(SQLiteDatabase.CursorFactory paramCursorFactory)
      {
        mCursorFactory = paramCursorFactory;
        return this;
      }
      
      public Builder setErrorHandler(DatabaseErrorHandler paramDatabaseErrorHandler)
      {
        mErrorHandler = paramDatabaseErrorHandler;
        return this;
      }
      
      public Builder setIdleConnectionTimeout(long paramLong)
      {
        boolean bool;
        if (paramLong >= 0L) {
          bool = true;
        } else {
          bool = false;
        }
        Preconditions.checkArgument(bool, "idle connection timeout cannot be negative");
        mIdleConnectionTimeout = paramLong;
        return this;
      }
      
      public Builder setJournalMode(String paramString)
      {
        Preconditions.checkNotNull(paramString);
        mJournalMode = paramString;
        return this;
      }
      
      public Builder setLookasideConfig(int paramInt1, int paramInt2)
      {
        boolean bool1 = false;
        boolean bool2;
        if (paramInt1 >= 0) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        Preconditions.checkArgument(bool2, "lookasideSlotCount cannot be negative");
        if (paramInt2 >= 0) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        Preconditions.checkArgument(bool2, "lookasideSlotSize cannot be negative");
        if ((paramInt1 <= 0) || (paramInt2 <= 0))
        {
          bool2 = bool1;
          if (paramInt2 == 0)
          {
            bool2 = bool1;
            if (paramInt1 != 0) {}
          }
        }
        else
        {
          bool2 = true;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid configuration: ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(", ");
        localStringBuilder.append(paramInt2);
        Preconditions.checkArgument(bool2, localStringBuilder.toString());
        mLookasideSlotSize = paramInt1;
        mLookasideSlotCount = paramInt2;
        return this;
      }
      
      public Builder setOpenFlags(int paramInt)
      {
        mOpenFlags = paramInt;
        return this;
      }
      
      public Builder setSynchronousMode(String paramString)
      {
        Preconditions.checkNotNull(paramString);
        mSyncMode = paramString;
        return this;
      }
      
      public void setWriteAheadLoggingEnabled(boolean paramBoolean)
      {
        if (paramBoolean) {
          addOpenFlags(536870912);
        } else {
          removeOpenFlags(536870912);
        }
      }
    }
  }
}
