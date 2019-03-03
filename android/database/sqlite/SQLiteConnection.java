package android.database.sqlite;

import android.database.DatabaseUtils;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Log;
import android.util.LruCache;
import android.util.Printer;
import dalvik.system.BlockGuard;
import dalvik.system.BlockGuard.Policy;
import dalvik.system.CloseGuard;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class SQLiteConnection
  implements CancellationSignal.OnCancelListener
{
  private static final boolean DEBUG = false;
  private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  private static final String[] EMPTY_STRING_ARRAY = new String[0];
  private static final String TAG = "SQLiteConnection";
  private int mCancellationSignalAttachCount;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final SQLiteDatabaseConfiguration mConfiguration;
  private final int mConnectionId;
  private long mConnectionPtr;
  private final boolean mIsPrimaryConnection;
  private final boolean mIsReadOnlyConnection;
  private boolean mOnlyAllowReadOnlyOperations;
  private final SQLiteConnectionPool mPool;
  private final PreparedStatementCache mPreparedStatementCache;
  private PreparedStatement mPreparedStatementPool;
  private final OperationLog mRecentOperations;
  
  private SQLiteConnection(SQLiteConnectionPool paramSQLiteConnectionPool, SQLiteDatabaseConfiguration paramSQLiteDatabaseConfiguration, int paramInt, boolean paramBoolean)
  {
    mPool = paramSQLiteConnectionPool;
    mRecentOperations = new OperationLog(mPool);
    mConfiguration = new SQLiteDatabaseConfiguration(paramSQLiteDatabaseConfiguration);
    mConnectionId = paramInt;
    mIsPrimaryConnection = paramBoolean;
    paramInt = openFlags;
    paramBoolean = true;
    if ((paramInt & 0x1) == 0) {
      paramBoolean = false;
    }
    mIsReadOnlyConnection = paramBoolean;
    mPreparedStatementCache = new PreparedStatementCache(mConfiguration.maxSqlCacheSize);
    mCloseGuard.open("close");
  }
  
  private PreparedStatement acquirePreparedStatement(String paramString)
  {
    PreparedStatement localPreparedStatement1 = (PreparedStatement)mPreparedStatementCache.get(paramString);
    int i = 0;
    if (localPreparedStatement1 != null)
    {
      if (!mInUse) {
        return localPreparedStatement1;
      }
      i = 1;
    }
    long l = nativePrepareStatement(mConnectionPtr, paramString);
    PreparedStatement localPreparedStatement2 = localPreparedStatement1;
    try
    {
      int j = nativeGetParameterCount(mConnectionPtr, l);
      localPreparedStatement2 = localPreparedStatement1;
      int k = DatabaseUtils.getSqlStatementType(paramString);
      localPreparedStatement2 = localPreparedStatement1;
      localPreparedStatement1 = obtainPreparedStatement(paramString, l, j, k, nativeIsReadOnly(mConnectionPtr, l));
      if (i == 0)
      {
        localPreparedStatement2 = localPreparedStatement1;
        if (isCacheable(k))
        {
          localPreparedStatement2 = localPreparedStatement1;
          mPreparedStatementCache.put(paramString, localPreparedStatement1);
          localPreparedStatement2 = localPreparedStatement1;
          mInCache = true;
        }
      }
      mInUse = true;
      return localPreparedStatement1;
    }
    catch (RuntimeException paramString)
    {
      if ((localPreparedStatement2 == null) || (!mInCache)) {
        nativeFinalizeStatement(mConnectionPtr, l);
      }
      throw paramString;
    }
  }
  
  private void applyBlockGuardPolicy(PreparedStatement paramPreparedStatement)
  {
    if (!mConfiguration.isInMemoryDb()) {
      if (mReadOnly) {
        BlockGuard.getThreadPolicy().onReadFromDisk();
      } else {
        BlockGuard.getThreadPolicy().onWriteToDisk();
      }
    }
  }
  
  private void attachCancellationSignal(CancellationSignal paramCancellationSignal)
  {
    if (paramCancellationSignal != null)
    {
      paramCancellationSignal.throwIfCanceled();
      mCancellationSignalAttachCount += 1;
      if (mCancellationSignalAttachCount == 1)
      {
        nativeResetCancel(mConnectionPtr, true);
        paramCancellationSignal.setOnCancelListener(this);
      }
    }
  }
  
  private void bindArguments(PreparedStatement paramPreparedStatement, Object[] paramArrayOfObject)
  {
    int i = 0;
    int j;
    if (paramArrayOfObject != null) {
      j = paramArrayOfObject.length;
    } else {
      j = 0;
    }
    if (j == mNumParameters)
    {
      if (j == 0) {
        return;
      }
      long l1 = mStatementPtr;
      while (i < j)
      {
        paramPreparedStatement = paramArrayOfObject[i];
        int k = DatabaseUtils.getTypeOfObject(paramPreparedStatement);
        if (k != 4) {
          switch (k)
          {
          default: 
            if ((paramPreparedStatement instanceof Boolean))
            {
              long l2 = mConnectionPtr;
              if (((Boolean)paramPreparedStatement).booleanValue()) {}
              for (long l3 = 1L;; l3 = 0L) {
                break;
              }
              nativeBindLong(l2, l1, i + 1, l3);
            }
            else
            {
              nativeBindString(mConnectionPtr, l1, i + 1, paramPreparedStatement.toString());
            }
            break;
          case 2: 
            nativeBindDouble(mConnectionPtr, l1, i + 1, ((Number)paramPreparedStatement).doubleValue());
            break;
          case 1: 
            nativeBindLong(mConnectionPtr, l1, i + 1, ((Number)paramPreparedStatement).longValue());
            break;
          case 0: 
            nativeBindNull(mConnectionPtr, l1, i + 1);
            break;
          }
        } else {
          nativeBindBlob(mConnectionPtr, l1, i + 1, (byte[])paramPreparedStatement);
        }
        i++;
      }
      return;
    }
    paramArrayOfObject = new StringBuilder();
    paramArrayOfObject.append("Expected ");
    paramArrayOfObject.append(mNumParameters);
    paramArrayOfObject.append(" bind arguments but ");
    paramArrayOfObject.append(j);
    paramArrayOfObject.append(" were provided.");
    throw new SQLiteBindOrColumnIndexOutOfRangeException(paramArrayOfObject.toString());
  }
  
  private static String canonicalizeSyncMode(String paramString)
  {
    switch (paramString.hashCode())
    {
    default: 
      break;
    case 50: 
      if (paramString.equals("2")) {
        i = 2;
      }
      break;
    case 49: 
      if (paramString.equals("1")) {
        i = 1;
      }
      break;
    case 48: 
      if (paramString.equals("0")) {
        i = 0;
      }
      break;
    }
    int i = -1;
    switch (i)
    {
    default: 
      return paramString;
    case 2: 
      return "FULL";
    case 1: 
      return "NORMAL";
    }
    return "OFF";
  }
  
  private void detachCancellationSignal(CancellationSignal paramCancellationSignal)
  {
    if (paramCancellationSignal != null)
    {
      mCancellationSignalAttachCount -= 1;
      if (mCancellationSignalAttachCount == 0)
      {
        paramCancellationSignal.setOnCancelListener(null);
        nativeResetCancel(mConnectionPtr, false);
      }
    }
  }
  
  private void dispose(boolean paramBoolean)
  {
    if (mCloseGuard != null)
    {
      if (paramBoolean) {
        mCloseGuard.warnIfOpen();
      }
      mCloseGuard.close();
    }
    if (mConnectionPtr != 0L)
    {
      int i = mRecentOperations.beginOperation("close", null, null);
      try
      {
        mPreparedStatementCache.evictAll();
        nativeClose(mConnectionPtr);
        mConnectionPtr = 0L;
        mRecentOperations.endOperation(i);
      }
      finally
      {
        mRecentOperations.endOperation(i);
      }
    }
  }
  
  private void finalizePreparedStatement(PreparedStatement paramPreparedStatement)
  {
    nativeFinalizeStatement(mConnectionPtr, mStatementPtr);
    recyclePreparedStatement(paramPreparedStatement);
  }
  
  private SQLiteDebug.DbStats getMainDbStatsUnsafe(int paramInt, long paramLong1, long paramLong2)
  {
    String str = mConfiguration.path;
    Object localObject = str;
    if (!mIsPrimaryConnection)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(str);
      ((StringBuilder)localObject).append(" (");
      ((StringBuilder)localObject).append(mConnectionId);
      ((StringBuilder)localObject).append(")");
      localObject = ((StringBuilder)localObject).toString();
    }
    return new SQLiteDebug.DbStats((String)localObject, paramLong1, paramLong2, paramInt, mPreparedStatementCache.hitCount(), mPreparedStatementCache.missCount(), mPreparedStatementCache.size());
  }
  
  private static boolean isCacheable(int paramInt)
  {
    return (paramInt == 2) || (paramInt == 1);
  }
  
  private static native void nativeBindBlob(long paramLong1, long paramLong2, int paramInt, byte[] paramArrayOfByte);
  
  private static native void nativeBindDouble(long paramLong1, long paramLong2, int paramInt, double paramDouble);
  
  private static native void nativeBindLong(long paramLong1, long paramLong2, int paramInt, long paramLong3);
  
  private static native void nativeBindNull(long paramLong1, long paramLong2, int paramInt);
  
  private static native void nativeBindString(long paramLong1, long paramLong2, int paramInt, String paramString);
  
  private static native void nativeCancel(long paramLong);
  
  private static native void nativeClose(long paramLong);
  
  private static native void nativeExecute(long paramLong1, long paramLong2);
  
  private static native int nativeExecuteForBlobFileDescriptor(long paramLong1, long paramLong2);
  
  private static native int nativeExecuteForChangedRowCount(long paramLong1, long paramLong2);
  
  private static native long nativeExecuteForCursorWindow(long paramLong1, long paramLong2, long paramLong3, int paramInt1, int paramInt2, boolean paramBoolean);
  
  private static native long nativeExecuteForLastInsertedRowId(long paramLong1, long paramLong2);
  
  private static native long nativeExecuteForLong(long paramLong1, long paramLong2);
  
  private static native String nativeExecuteForString(long paramLong1, long paramLong2);
  
  private static native void nativeFinalizeStatement(long paramLong1, long paramLong2);
  
  private static native int nativeGetColumnCount(long paramLong1, long paramLong2);
  
  private static native String nativeGetColumnName(long paramLong1, long paramLong2, int paramInt);
  
  private static native int nativeGetDbLookaside(long paramLong);
  
  private static native int nativeGetParameterCount(long paramLong1, long paramLong2);
  
  private static native boolean nativeIsReadOnly(long paramLong1, long paramLong2);
  
  private static native long nativeOpen(String paramString1, int paramInt1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, int paramInt3);
  
  private static native long nativePrepareStatement(long paramLong, String paramString);
  
  private static native void nativeRegisterCustomFunction(long paramLong, SQLiteCustomFunction paramSQLiteCustomFunction);
  
  private static native void nativeRegisterLocalizedCollators(long paramLong, String paramString);
  
  private static native void nativeResetCancel(long paramLong, boolean paramBoolean);
  
  private static native void nativeResetStatementAndClearBindings(long paramLong1, long paramLong2);
  
  private PreparedStatement obtainPreparedStatement(String paramString, long paramLong, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    PreparedStatement localPreparedStatement = mPreparedStatementPool;
    if (localPreparedStatement != null)
    {
      mPreparedStatementPool = mPoolNext;
      mPoolNext = null;
      mInCache = false;
    }
    else
    {
      localPreparedStatement = new PreparedStatement(null);
    }
    mSql = paramString;
    mStatementPtr = paramLong;
    mNumParameters = paramInt1;
    mType = paramInt2;
    mReadOnly = paramBoolean;
    return localPreparedStatement;
  }
  
  static SQLiteConnection open(SQLiteConnectionPool paramSQLiteConnectionPool, SQLiteDatabaseConfiguration paramSQLiteDatabaseConfiguration, int paramInt, boolean paramBoolean)
  {
    paramSQLiteConnectionPool = new SQLiteConnection(paramSQLiteConnectionPool, paramSQLiteDatabaseConfiguration, paramInt, paramBoolean);
    try
    {
      paramSQLiteConnectionPool.open();
      return paramSQLiteConnectionPool;
    }
    catch (SQLiteException paramSQLiteDatabaseConfiguration)
    {
      paramSQLiteConnectionPool.dispose(false);
      throw paramSQLiteDatabaseConfiguration;
    }
  }
  
  private void open()
  {
    mConnectionPtr = nativeOpen(mConfiguration.path, mConfiguration.openFlags, mConfiguration.label, SQLiteDebug.DEBUG_SQL_STATEMENTS, SQLiteDebug.DEBUG_SQL_TIME, mConfiguration.lookasideSlotSize, mConfiguration.lookasideSlotCount);
    setPageSize();
    setForeignKeyModeFromConfiguration();
    setWalModeFromConfiguration();
    setJournalSizeLimit();
    setAutoCheckpointInterval();
    setLocaleFromConfiguration();
    int i = mConfiguration.customFunctions.size();
    for (int j = 0; j < i; j++)
    {
      SQLiteCustomFunction localSQLiteCustomFunction = (SQLiteCustomFunction)mConfiguration.customFunctions.get(j);
      nativeRegisterCustomFunction(mConnectionPtr, localSQLiteCustomFunction);
    }
  }
  
  private void recyclePreparedStatement(PreparedStatement paramPreparedStatement)
  {
    mSql = null;
    mPoolNext = mPreparedStatementPool;
    mPreparedStatementPool = paramPreparedStatement;
  }
  
  private void releasePreparedStatement(PreparedStatement paramPreparedStatement)
  {
    mInUse = false;
    if (mInCache) {
      try
      {
        nativeResetStatementAndClearBindings(mConnectionPtr, mStatementPtr);
      }
      catch (SQLiteException localSQLiteException)
      {
        mPreparedStatementCache.remove(mSql);
      }
    } else {
      finalizePreparedStatement(paramPreparedStatement);
    }
  }
  
  private void setAutoCheckpointInterval()
  {
    if ((!mConfiguration.isInMemoryDb()) && (!mIsReadOnlyConnection))
    {
      long l = SQLiteGlobal.getWALAutoCheckpoint();
      if (executeForLong("PRAGMA wal_autocheckpoint", null, null) != l)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("PRAGMA wal_autocheckpoint=");
        localStringBuilder.append(l);
        executeForLong(localStringBuilder.toString(), null, null);
      }
    }
  }
  
  private void setForeignKeyModeFromConfiguration()
  {
    if (!mIsReadOnlyConnection)
    {
      long l;
      if (mConfiguration.foreignKeyConstraintsEnabled) {
        l = 1L;
      } else {
        l = 0L;
      }
      if (executeForLong("PRAGMA foreign_keys", null, null) != l)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("PRAGMA foreign_keys=");
        localStringBuilder.append(l);
        execute(localStringBuilder.toString(), null, null);
      }
    }
  }
  
  private void setJournalMode(String paramString)
  {
    String str = executeForString("PRAGMA journal_mode", null, null);
    if (!str.equalsIgnoreCase(paramString))
    {
      try
      {
        StringBuilder localStringBuilder1 = new java/lang/StringBuilder;
        localStringBuilder1.<init>();
        localStringBuilder1.append("PRAGMA journal_mode=");
        localStringBuilder1.append(paramString);
        boolean bool = executeForString(localStringBuilder1.toString(), null, null).equalsIgnoreCase(paramString);
        if (bool) {
          return;
        }
      }
      catch (SQLiteDatabaseLockedException localSQLiteDatabaseLockedException) {}
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("Could not change the database journal mode of '");
      localStringBuilder2.append(mConfiguration.label);
      localStringBuilder2.append("' from '");
      localStringBuilder2.append(str);
      localStringBuilder2.append("' to '");
      localStringBuilder2.append(paramString);
      localStringBuilder2.append("' because the database is locked.  This usually means that there are other open connections to the database which prevents the database from enabling or disabling write-ahead logging mode.  Proceeding without changing the journal mode.");
      Log.w("SQLiteConnection", localStringBuilder2.toString());
    }
  }
  
  private void setJournalSizeLimit()
  {
    if ((!mConfiguration.isInMemoryDb()) && (!mIsReadOnlyConnection))
    {
      long l = SQLiteGlobal.getJournalSizeLimit();
      if (executeForLong("PRAGMA journal_size_limit", null, null) != l)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("PRAGMA journal_size_limit=");
        localStringBuilder.append(l);
        executeForLong(localStringBuilder.toString(), null, null);
      }
    }
  }
  
  /* Error */
  private void setLocaleFromConfiguration()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 87	android/database/sqlite/SQLiteConnection:mConfiguration	Landroid/database/sqlite/SQLiteDatabaseConfiguration;
    //   4: getfield 94	android/database/sqlite/SQLiteDatabaseConfiguration:openFlags	I
    //   7: bipush 16
    //   9: iand
    //   10: ifeq +4 -> 14
    //   13: return
    //   14: aload_0
    //   15: getfield 87	android/database/sqlite/SQLiteConnection:mConfiguration	Landroid/database/sqlite/SQLiteDatabaseConfiguration;
    //   18: getfield 524	android/database/sqlite/SQLiteDatabaseConfiguration:locale	Ljava/util/Locale;
    //   21: invokevirtual 527	java/util/Locale:toString	()Ljava/lang/String;
    //   24: astore_1
    //   25: aload_0
    //   26: getfield 135	android/database/sqlite/SQLiteConnection:mConnectionPtr	J
    //   29: aload_1
    //   30: invokestatic 529	android/database/sqlite/SQLiteConnection:nativeRegisterLocalizedCollators	(JLjava/lang/String;)V
    //   33: aload_0
    //   34: getfield 96	android/database/sqlite/SQLiteConnection:mIsReadOnlyConnection	Z
    //   37: ifeq +4 -> 41
    //   40: return
    //   41: aload_0
    //   42: ldc_w 531
    //   45: aconst_null
    //   46: aconst_null
    //   47: invokevirtual 484	android/database/sqlite/SQLiteConnection:execute	(Ljava/lang/String;[Ljava/lang/Object;Landroid/os/CancellationSignal;)V
    //   50: aload_0
    //   51: ldc_w 533
    //   54: aconst_null
    //   55: aconst_null
    //   56: invokevirtual 493	android/database/sqlite/SQLiteConnection:executeForString	(Ljava/lang/String;[Ljava/lang/Object;Landroid/os/CancellationSignal;)Ljava/lang/String;
    //   59: astore_2
    //   60: aload_2
    //   61: ifnull +12 -> 73
    //   64: aload_2
    //   65: aload_1
    //   66: invokevirtual 295	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   69: ifeq +4 -> 73
    //   72: return
    //   73: aload_0
    //   74: ldc_w 535
    //   77: aconst_null
    //   78: aconst_null
    //   79: invokevirtual 484	android/database/sqlite/SQLiteConnection:execute	(Ljava/lang/String;[Ljava/lang/Object;Landroid/os/CancellationSignal;)V
    //   82: aload_0
    //   83: ldc_w 537
    //   86: aconst_null
    //   87: aconst_null
    //   88: invokevirtual 484	android/database/sqlite/SQLiteConnection:execute	(Ljava/lang/String;[Ljava/lang/Object;Landroid/os/CancellationSignal;)V
    //   91: aload_0
    //   92: ldc_w 539
    //   95: iconst_1
    //   96: anewarray 4	java/lang/Object
    //   99: dup
    //   100: iconst_0
    //   101: aload_1
    //   102: aastore
    //   103: aconst_null
    //   104: invokevirtual 484	android/database/sqlite/SQLiteConnection:execute	(Ljava/lang/String;[Ljava/lang/Object;Landroid/os/CancellationSignal;)V
    //   107: aload_0
    //   108: ldc_w 541
    //   111: aconst_null
    //   112: aconst_null
    //   113: invokevirtual 484	android/database/sqlite/SQLiteConnection:execute	(Ljava/lang/String;[Ljava/lang/Object;Landroid/os/CancellationSignal;)V
    //   116: iconst_1
    //   117: ifeq +10 -> 127
    //   120: ldc_w 543
    //   123: astore_2
    //   124: goto +7 -> 131
    //   127: ldc_w 545
    //   130: astore_2
    //   131: aload_0
    //   132: aload_2
    //   133: aconst_null
    //   134: aconst_null
    //   135: invokevirtual 484	android/database/sqlite/SQLiteConnection:execute	(Ljava/lang/String;[Ljava/lang/Object;Landroid/os/CancellationSignal;)V
    //   138: return
    //   139: astore_3
    //   140: iconst_0
    //   141: ifeq +10 -> 151
    //   144: ldc_w 543
    //   147: astore_2
    //   148: goto +7 -> 155
    //   151: ldc_w 545
    //   154: astore_2
    //   155: aload_0
    //   156: aload_2
    //   157: aconst_null
    //   158: aconst_null
    //   159: invokevirtual 484	android/database/sqlite/SQLiteConnection:execute	(Ljava/lang/String;[Ljava/lang/Object;Landroid/os/CancellationSignal;)V
    //   162: aload_3
    //   163: athrow
    //   164: astore_3
    //   165: new 265	java/lang/StringBuilder
    //   168: dup
    //   169: invokespecial 266	java/lang/StringBuilder:<init>	()V
    //   172: astore_2
    //   173: aload_2
    //   174: ldc_w 547
    //   177: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: pop
    //   181: aload_2
    //   182: aload_0
    //   183: getfield 87	android/database/sqlite/SQLiteConnection:mConfiguration	Landroid/database/sqlite/SQLiteDatabaseConfiguration;
    //   186: getfield 403	android/database/sqlite/SQLiteDatabaseConfiguration:label	Ljava/lang/String;
    //   189: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   192: pop
    //   193: aload_2
    //   194: ldc_w 505
    //   197: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   200: pop
    //   201: aload_2
    //   202: aload_1
    //   203: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   206: pop
    //   207: aload_2
    //   208: ldc_w 549
    //   211: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   214: pop
    //   215: new 394	android/database/sqlite/SQLiteException
    //   218: dup
    //   219: aload_2
    //   220: invokevirtual 282	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   223: aload_3
    //   224: invokespecial 552	android/database/sqlite/SQLiteException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   227: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	228	0	this	SQLiteConnection
    //   24	179	1	str	String
    //   59	161	2	localObject1	Object
    //   139	24	3	localObject2	Object
    //   164	60	3	localRuntimeException	RuntimeException
    // Exception table:
    //   from	to	target	type
    //   82	116	139	finally
    //   41	60	164	java/lang/RuntimeException
    //   64	72	164	java/lang/RuntimeException
    //   73	82	164	java/lang/RuntimeException
    //   131	138	164	java/lang/RuntimeException
    //   155	164	164	java/lang/RuntimeException
  }
  
  private void setPageSize()
  {
    if ((!mConfiguration.isInMemoryDb()) && (!mIsReadOnlyConnection))
    {
      long l = SQLiteGlobal.getDefaultPageSize();
      if (executeForLong("PRAGMA page_size", null, null) != l)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("PRAGMA page_size=");
        localStringBuilder.append(l);
        execute(localStringBuilder.toString(), null, null);
      }
    }
  }
  
  private void setSyncMode(String paramString)
  {
    if (!canonicalizeSyncMode(executeForString("PRAGMA synchronous", null, null)).equalsIgnoreCase(canonicalizeSyncMode(paramString)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PRAGMA synchronous=");
      localStringBuilder.append(paramString);
      execute(localStringBuilder.toString(), null, null);
    }
  }
  
  private void setWalModeFromConfiguration()
  {
    if ((!mConfiguration.isInMemoryDb()) && (!mIsReadOnlyConnection))
    {
      int i;
      if ((mConfiguration.openFlags & 0x20000000) != 0) {
        i = 1;
      } else {
        i = 0;
      }
      boolean bool = mConfiguration.useCompatibilityWal();
      if ((i == 0) && (!bool))
      {
        String str;
        if (mConfiguration.journalMode == null) {
          str = SQLiteGlobal.getDefaultJournalMode();
        } else {
          str = mConfiguration.journalMode;
        }
        setJournalMode(str);
        if (mConfiguration.syncMode == null) {
          str = SQLiteGlobal.getDefaultSyncMode();
        } else {
          str = mConfiguration.syncMode;
        }
        setSyncMode(str);
      }
      else
      {
        setJournalMode("WAL");
        if (mConfiguration.syncMode != null) {
          setSyncMode(mConfiguration.syncMode);
        } else if ((bool) && (SQLiteCompatibilityWalFlags.areFlagsSet())) {
          setSyncMode(SQLiteCompatibilityWalFlags.getWALSyncMode());
        } else {
          setSyncMode(SQLiteGlobal.getWALSyncMode());
        }
      }
    }
  }
  
  private void throwIfStatementForbidden(PreparedStatement paramPreparedStatement)
  {
    if ((mOnlyAllowReadOnlyOperations) && (!mReadOnly)) {
      throw new SQLiteException("Cannot execute this statement because it might modify the database but the connection is read-only.");
    }
  }
  
  private static String trimSqlForDisplay(String paramString)
  {
    return paramString.replaceAll("[\\s]*\\n+[\\s]*", " ");
  }
  
  void close()
  {
    dispose(false);
  }
  
  /* Error */
  void collectDbStats(ArrayList<SQLiteDebug.DbStats> paramArrayList)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 135	android/database/sqlite/SQLiteConnection:mConnectionPtr	J
    //   4: invokestatic 615	android/database/sqlite/SQLiteConnection:nativeGetDbLookaside	(J)I
    //   7: istore_2
    //   8: lconst_0
    //   9: lstore_3
    //   10: lconst_0
    //   11: lstore 5
    //   13: aload_0
    //   14: ldc_w 617
    //   17: aconst_null
    //   18: aconst_null
    //   19: invokevirtual 468	android/database/sqlite/SQLiteConnection:executeForLong	(Ljava/lang/String;[Ljava/lang/Object;Landroid/os/CancellationSignal;)J
    //   22: lstore 7
    //   24: lload 7
    //   26: lstore_3
    //   27: aload_0
    //   28: ldc_w 619
    //   31: aconst_null
    //   32: aconst_null
    //   33: invokevirtual 468	android/database/sqlite/SQLiteConnection:executeForLong	(Ljava/lang/String;[Ljava/lang/Object;Landroid/os/CancellationSignal;)J
    //   36: lstore 9
    //   38: lload 9
    //   40: lstore 5
    //   42: lload 7
    //   44: lstore_3
    //   45: lload 5
    //   47: lstore 7
    //   49: goto +9 -> 58
    //   52: astore 11
    //   54: lload 5
    //   56: lstore 7
    //   58: aload_1
    //   59: aload_0
    //   60: iload_2
    //   61: lload_3
    //   62: lload 7
    //   64: invokespecial 621	android/database/sqlite/SQLiteConnection:getMainDbStatsUnsafe	(IJJ)Landroid/database/sqlite/SQLiteDebug$DbStats;
    //   67: invokevirtual 624	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   70: pop
    //   71: new 626	android/database/CursorWindow
    //   74: dup
    //   75: ldc_w 627
    //   78: invokespecial 628	android/database/CursorWindow:<init>	(Ljava/lang/String;)V
    //   81: astore 12
    //   83: aload_0
    //   84: ldc_w 630
    //   87: aconst_null
    //   88: aload 12
    //   90: iconst_0
    //   91: iconst_0
    //   92: iconst_0
    //   93: aconst_null
    //   94: invokevirtual 634	android/database/sqlite/SQLiteConnection:executeForCursorWindow	(Ljava/lang/String;[Ljava/lang/Object;Landroid/database/CursorWindow;IIZLandroid/os/CancellationSignal;)I
    //   97: pop
    //   98: iconst_1
    //   99: istore_2
    //   100: iload_2
    //   101: aload 12
    //   103: invokevirtual 637	android/database/CursorWindow:getNumRows	()I
    //   106: if_icmpge +332 -> 438
    //   109: aload 12
    //   111: iload_2
    //   112: iconst_1
    //   113: invokevirtual 641	android/database/CursorWindow:getString	(II)Ljava/lang/String;
    //   116: astore 11
    //   118: aload 12
    //   120: iload_2
    //   121: iconst_2
    //   122: invokevirtual 641	android/database/CursorWindow:getString	(II)Ljava/lang/String;
    //   125: astore 13
    //   127: lconst_0
    //   128: lstore_3
    //   129: lconst_0
    //   130: lstore 5
    //   132: lload_3
    //   133: lstore 7
    //   135: new 265	java/lang/StringBuilder
    //   138: astore 14
    //   140: lload_3
    //   141: lstore 7
    //   143: aload 14
    //   145: invokespecial 266	java/lang/StringBuilder:<init>	()V
    //   148: lload_3
    //   149: lstore 7
    //   151: aload 14
    //   153: ldc_w 643
    //   156: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: pop
    //   160: lload_3
    //   161: lstore 7
    //   163: aload 14
    //   165: aload 11
    //   167: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   170: pop
    //   171: lload_3
    //   172: lstore 7
    //   174: aload 14
    //   176: ldc_w 645
    //   179: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   182: pop
    //   183: lload_3
    //   184: lstore 7
    //   186: aload 14
    //   188: invokevirtual 282	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   191: astore 14
    //   193: lload_3
    //   194: lstore 7
    //   196: aload_0
    //   197: aload 14
    //   199: aconst_null
    //   200: aconst_null
    //   201: invokevirtual 468	android/database/sqlite/SQLiteConnection:executeForLong	(Ljava/lang/String;[Ljava/lang/Object;Landroid/os/CancellationSignal;)J
    //   204: lstore_3
    //   205: lload_3
    //   206: lstore 7
    //   208: new 265	java/lang/StringBuilder
    //   211: astore 14
    //   213: lload_3
    //   214: lstore 7
    //   216: aload 14
    //   218: invokespecial 266	java/lang/StringBuilder:<init>	()V
    //   221: lload_3
    //   222: lstore 7
    //   224: aload 14
    //   226: ldc_w 643
    //   229: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   232: pop
    //   233: lload_3
    //   234: lstore 7
    //   236: aload 14
    //   238: aload 11
    //   240: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: pop
    //   244: lload_3
    //   245: lstore 7
    //   247: aload 14
    //   249: ldc_w 647
    //   252: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   255: pop
    //   256: lload_3
    //   257: lstore 7
    //   259: aload 14
    //   261: invokevirtual 282	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   264: astore 14
    //   266: lload_3
    //   267: lstore 7
    //   269: aload_0
    //   270: aload 14
    //   272: aconst_null
    //   273: aconst_null
    //   274: invokevirtual 468	android/database/sqlite/SQLiteConnection:executeForLong	(Ljava/lang/String;[Ljava/lang/Object;Landroid/os/CancellationSignal;)J
    //   277: lstore 9
    //   279: lload 9
    //   281: lstore 7
    //   283: lload 7
    //   285: lstore 5
    //   287: goto +16 -> 303
    //   290: astore 14
    //   292: lload 7
    //   294: lstore_3
    //   295: goto +8 -> 303
    //   298: astore 14
    //   300: lload 7
    //   302: lstore_3
    //   303: new 265	java/lang/StringBuilder
    //   306: astore 14
    //   308: aload 14
    //   310: invokespecial 266	java/lang/StringBuilder:<init>	()V
    //   313: aload 14
    //   315: ldc_w 649
    //   318: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   321: pop
    //   322: aload 14
    //   324: aload 11
    //   326: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   329: pop
    //   330: aload 14
    //   332: invokevirtual 282	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   335: astore 14
    //   337: aload 14
    //   339: astore 11
    //   341: aload 13
    //   343: invokevirtual 652	java/lang/String:isEmpty	()Z
    //   346: ifne +45 -> 391
    //   349: new 265	java/lang/StringBuilder
    //   352: astore 11
    //   354: aload 11
    //   356: invokespecial 266	java/lang/StringBuilder:<init>	()V
    //   359: aload 11
    //   361: aload 14
    //   363: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   366: pop
    //   367: aload 11
    //   369: ldc_w 654
    //   372: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   375: pop
    //   376: aload 11
    //   378: aload 13
    //   380: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   383: pop
    //   384: aload 11
    //   386: invokevirtual 282	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   389: astore 11
    //   391: new 342	android/database/sqlite/SQLiteDebug$DbStats
    //   394: astore 14
    //   396: aload 14
    //   398: aload 11
    //   400: lload_3
    //   401: lload 5
    //   403: iconst_0
    //   404: iconst_0
    //   405: iconst_0
    //   406: iconst_0
    //   407: invokespecial 354	android/database/sqlite/SQLiteDebug$DbStats:<init>	(Ljava/lang/String;JJIIII)V
    //   410: aload_1
    //   411: aload 14
    //   413: invokevirtual 624	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   416: pop
    //   417: iinc 2 1
    //   420: goto -320 -> 100
    //   423: astore_1
    //   424: goto +7 -> 431
    //   427: astore_1
    //   428: goto +10 -> 438
    //   431: aload 12
    //   433: invokevirtual 655	android/database/CursorWindow:close	()V
    //   436: aload_1
    //   437: athrow
    //   438: aload 12
    //   440: invokevirtual 655	android/database/CursorWindow:close	()V
    //   443: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	444	0	this	SQLiteConnection
    //   0	444	1	paramArrayList	ArrayList<SQLiteDebug.DbStats>
    //   7	411	2	i	int
    //   9	392	3	l1	long
    //   11	391	5	l2	long
    //   22	279	7	l3	long
    //   36	244	9	l4	long
    //   52	1	11	localSQLiteException1	SQLiteException
    //   116	283	11	localObject1	Object
    //   81	358	12	localCursorWindow	android.database.CursorWindow
    //   125	254	13	str	String
    //   138	133	14	localObject2	Object
    //   290	1	14	localSQLiteException2	SQLiteException
    //   298	1	14	localSQLiteException3	SQLiteException
    //   306	106	14	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   13	24	52	android/database/sqlite/SQLiteException
    //   27	38	52	android/database/sqlite/SQLiteException
    //   196	205	290	android/database/sqlite/SQLiteException
    //   269	279	290	android/database/sqlite/SQLiteException
    //   135	140	298	android/database/sqlite/SQLiteException
    //   143	148	298	android/database/sqlite/SQLiteException
    //   151	160	298	android/database/sqlite/SQLiteException
    //   163	171	298	android/database/sqlite/SQLiteException
    //   174	183	298	android/database/sqlite/SQLiteException
    //   186	193	298	android/database/sqlite/SQLiteException
    //   208	213	298	android/database/sqlite/SQLiteException
    //   216	221	298	android/database/sqlite/SQLiteException
    //   224	233	298	android/database/sqlite/SQLiteException
    //   236	244	298	android/database/sqlite/SQLiteException
    //   247	256	298	android/database/sqlite/SQLiteException
    //   259	266	298	android/database/sqlite/SQLiteException
    //   83	98	423	finally
    //   100	127	423	finally
    //   135	140	423	finally
    //   143	148	423	finally
    //   151	160	423	finally
    //   163	171	423	finally
    //   174	183	423	finally
    //   186	193	423	finally
    //   196	205	423	finally
    //   208	213	423	finally
    //   216	221	423	finally
    //   224	233	423	finally
    //   236	244	423	finally
    //   247	256	423	finally
    //   259	266	423	finally
    //   269	279	423	finally
    //   303	337	423	finally
    //   341	391	423	finally
    //   391	417	423	finally
    //   83	98	427	android/database/sqlite/SQLiteException
    //   100	127	427	android/database/sqlite/SQLiteException
    //   303	337	427	android/database/sqlite/SQLiteException
    //   341	391	427	android/database/sqlite/SQLiteException
    //   391	417	427	android/database/sqlite/SQLiteException
  }
  
  void collectDbStatsUnsafe(ArrayList<SQLiteDebug.DbStats> paramArrayList)
  {
    paramArrayList.add(getMainDbStatsUnsafe(0, 0L, 0L));
  }
  
  String describeCurrentOperationUnsafe()
  {
    return mRecentOperations.describeCurrentOperation();
  }
  
  public void dump(Printer paramPrinter, boolean paramBoolean)
  {
    dumpUnsafe(paramPrinter, paramBoolean);
  }
  
  void dumpUnsafe(Printer paramPrinter, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Connection #");
    localStringBuilder.append(mConnectionId);
    localStringBuilder.append(":");
    paramPrinter.println(localStringBuilder.toString());
    if (paramBoolean)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("  connectionPtr: 0x");
      localStringBuilder.append(Long.toHexString(mConnectionPtr));
      paramPrinter.println(localStringBuilder.toString());
    }
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("  isPrimaryConnection: ");
    localStringBuilder.append(mIsPrimaryConnection);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("  onlyAllowReadOnlyOperations: ");
    localStringBuilder.append(mOnlyAllowReadOnlyOperations);
    paramPrinter.println(localStringBuilder.toString());
    mRecentOperations.dump(paramPrinter, paramBoolean);
    if (paramBoolean) {
      mPreparedStatementCache.dump(paramPrinter);
    }
  }
  
  /* Error */
  public void execute(String paramString, Object[] paramArrayOfObject, CancellationSignal paramCancellationSignal)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +119 -> 120
    //   4: aload_0
    //   5: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   8: ldc_w 697
    //   11: aload_1
    //   12: aload_2
    //   13: invokevirtual 317	android/database/sqlite/SQLiteConnection$OperationLog:beginOperation	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   16: istore 4
    //   18: aload_0
    //   19: aload_1
    //   20: invokespecial 699	android/database/sqlite/SQLiteConnection:acquirePreparedStatement	(Ljava/lang/String;)Landroid/database/sqlite/SQLiteConnection$PreparedStatement;
    //   23: astore_1
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 701	android/database/sqlite/SQLiteConnection:throwIfStatementForbidden	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   29: aload_0
    //   30: aload_1
    //   31: aload_2
    //   32: invokespecial 703	android/database/sqlite/SQLiteConnection:bindArguments	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;[Ljava/lang/Object;)V
    //   35: aload_0
    //   36: aload_1
    //   37: invokespecial 705	android/database/sqlite/SQLiteConnection:applyBlockGuardPolicy	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   40: aload_0
    //   41: aload_3
    //   42: invokespecial 707	android/database/sqlite/SQLiteConnection:attachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   45: aload_0
    //   46: getfield 135	android/database/sqlite/SQLiteConnection:mConnectionPtr	J
    //   49: aload_1
    //   50: getfield 219	android/database/sqlite/SQLiteConnection$PreparedStatement:mStatementPtr	J
    //   53: invokestatic 709	android/database/sqlite/SQLiteConnection:nativeExecute	(JJ)V
    //   56: aload_0
    //   57: aload_3
    //   58: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   61: aload_0
    //   62: aload_1
    //   63: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   66: aload_0
    //   67: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   70: iload 4
    //   72: invokevirtual 328	android/database/sqlite/SQLiteConnection$OperationLog:endOperation	(I)V
    //   75: return
    //   76: astore_2
    //   77: aload_0
    //   78: aload_3
    //   79: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   82: aload_2
    //   83: athrow
    //   84: astore_2
    //   85: aload_0
    //   86: aload_1
    //   87: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   90: aload_2
    //   91: athrow
    //   92: astore_1
    //   93: goto +16 -> 109
    //   96: astore_1
    //   97: aload_0
    //   98: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   101: iload 4
    //   103: aload_1
    //   104: invokevirtual 717	android/database/sqlite/SQLiteConnection$OperationLog:failOperation	(ILjava/lang/Exception;)V
    //   107: aload_1
    //   108: athrow
    //   109: aload_0
    //   110: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   113: iload 4
    //   115: invokevirtual 328	android/database/sqlite/SQLiteConnection$OperationLog:endOperation	(I)V
    //   118: aload_1
    //   119: athrow
    //   120: new 719	java/lang/IllegalArgumentException
    //   123: dup
    //   124: ldc_w 721
    //   127: invokespecial 722	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   130: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	131	0	this	SQLiteConnection
    //   0	131	1	paramString	String
    //   0	131	2	paramArrayOfObject	Object[]
    //   0	131	3	paramCancellationSignal	CancellationSignal
    //   16	98	4	i	int
    // Exception table:
    //   from	to	target	type
    //   45	56	76	finally
    //   24	45	84	finally
    //   56	61	84	finally
    //   77	84	84	finally
    //   18	24	92	finally
    //   61	66	92	finally
    //   85	92	92	finally
    //   97	109	92	finally
    //   18	24	96	java/lang/RuntimeException
    //   61	66	96	java/lang/RuntimeException
    //   85	92	96	java/lang/RuntimeException
  }
  
  /* Error */
  public android.os.ParcelFileDescriptor executeForBlobFileDescriptor(String paramString, Object[] paramArrayOfObject, CancellationSignal paramCancellationSignal)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +145 -> 146
    //   4: aload_0
    //   5: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   8: ldc_w 725
    //   11: aload_1
    //   12: aload_2
    //   13: invokevirtual 317	android/database/sqlite/SQLiteConnection$OperationLog:beginOperation	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   16: istore 4
    //   18: aload_0
    //   19: aload_1
    //   20: invokespecial 699	android/database/sqlite/SQLiteConnection:acquirePreparedStatement	(Ljava/lang/String;)Landroid/database/sqlite/SQLiteConnection$PreparedStatement;
    //   23: astore 5
    //   25: aload_0
    //   26: aload 5
    //   28: invokespecial 701	android/database/sqlite/SQLiteConnection:throwIfStatementForbidden	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   31: aload_0
    //   32: aload 5
    //   34: aload_2
    //   35: invokespecial 703	android/database/sqlite/SQLiteConnection:bindArguments	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;[Ljava/lang/Object;)V
    //   38: aload_0
    //   39: aload 5
    //   41: invokespecial 705	android/database/sqlite/SQLiteConnection:applyBlockGuardPolicy	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   44: aload_0
    //   45: aload_3
    //   46: invokespecial 707	android/database/sqlite/SQLiteConnection:attachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   49: aload_0
    //   50: getfield 135	android/database/sqlite/SQLiteConnection:mConnectionPtr	J
    //   53: aload 5
    //   55: getfield 219	android/database/sqlite/SQLiteConnection$PreparedStatement:mStatementPtr	J
    //   58: invokestatic 727	android/database/sqlite/SQLiteConnection:nativeExecuteForBlobFileDescriptor	(JJ)I
    //   61: istore 6
    //   63: iload 6
    //   65: iflt +12 -> 77
    //   68: iload 6
    //   70: invokestatic 733	android/os/ParcelFileDescriptor:adoptFd	(I)Landroid/os/ParcelFileDescriptor;
    //   73: astore_1
    //   74: goto +5 -> 79
    //   77: aconst_null
    //   78: astore_1
    //   79: aload_0
    //   80: aload_3
    //   81: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   84: aload_0
    //   85: aload 5
    //   87: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   90: aload_0
    //   91: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   94: iload 4
    //   96: invokevirtual 328	android/database/sqlite/SQLiteConnection$OperationLog:endOperation	(I)V
    //   99: aload_1
    //   100: areturn
    //   101: astore_1
    //   102: aload_0
    //   103: aload_3
    //   104: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   107: aload_1
    //   108: athrow
    //   109: astore_1
    //   110: aload_0
    //   111: aload 5
    //   113: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   116: aload_1
    //   117: athrow
    //   118: astore_1
    //   119: goto +16 -> 135
    //   122: astore_1
    //   123: aload_0
    //   124: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   127: iload 4
    //   129: aload_1
    //   130: invokevirtual 717	android/database/sqlite/SQLiteConnection$OperationLog:failOperation	(ILjava/lang/Exception;)V
    //   133: aload_1
    //   134: athrow
    //   135: aload_0
    //   136: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   139: iload 4
    //   141: invokevirtual 328	android/database/sqlite/SQLiteConnection$OperationLog:endOperation	(I)V
    //   144: aload_1
    //   145: athrow
    //   146: new 719	java/lang/IllegalArgumentException
    //   149: dup
    //   150: ldc_w 721
    //   153: invokespecial 722	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   156: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	157	0	this	SQLiteConnection
    //   0	157	1	paramString	String
    //   0	157	2	paramArrayOfObject	Object[]
    //   0	157	3	paramCancellationSignal	CancellationSignal
    //   16	124	4	i	int
    //   23	89	5	localPreparedStatement	PreparedStatement
    //   61	8	6	j	int
    // Exception table:
    //   from	to	target	type
    //   49	63	101	finally
    //   68	74	101	finally
    //   25	49	109	finally
    //   79	84	109	finally
    //   102	109	109	finally
    //   18	25	118	finally
    //   84	90	118	finally
    //   110	118	118	finally
    //   123	135	118	finally
    //   18	25	122	java/lang/RuntimeException
    //   84	90	122	java/lang/RuntimeException
    //   110	118	122	java/lang/RuntimeException
  }
  
  /* Error */
  public int executeForChangedRowCount(String paramString, Object[] paramArrayOfObject, CancellationSignal paramCancellationSignal)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +278 -> 279
    //   4: iconst_0
    //   5: istore 4
    //   7: iconst_0
    //   8: istore 5
    //   10: iconst_0
    //   11: istore 6
    //   13: aload_0
    //   14: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   17: ldc_w 736
    //   20: aload_1
    //   21: aload_2
    //   22: invokevirtual 317	android/database/sqlite/SQLiteConnection$OperationLog:beginOperation	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   25: istore 7
    //   27: aload_0
    //   28: aload_1
    //   29: invokespecial 699	android/database/sqlite/SQLiteConnection:acquirePreparedStatement	(Ljava/lang/String;)Landroid/database/sqlite/SQLiteConnection$PreparedStatement;
    //   32: astore_1
    //   33: iload 6
    //   35: istore 8
    //   37: aload_0
    //   38: aload_1
    //   39: invokespecial 701	android/database/sqlite/SQLiteConnection:throwIfStatementForbidden	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   42: iload 6
    //   44: istore 8
    //   46: aload_0
    //   47: aload_1
    //   48: aload_2
    //   49: invokespecial 703	android/database/sqlite/SQLiteConnection:bindArguments	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;[Ljava/lang/Object;)V
    //   52: iload 6
    //   54: istore 8
    //   56: aload_0
    //   57: aload_1
    //   58: invokespecial 705	android/database/sqlite/SQLiteConnection:applyBlockGuardPolicy	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   61: iload 6
    //   63: istore 8
    //   65: aload_0
    //   66: aload_3
    //   67: invokespecial 707	android/database/sqlite/SQLiteConnection:attachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   70: aload_0
    //   71: getfield 135	android/database/sqlite/SQLiteConnection:mConnectionPtr	J
    //   74: aload_1
    //   75: getfield 219	android/database/sqlite/SQLiteConnection$PreparedStatement:mStatementPtr	J
    //   78: invokestatic 738	android/database/sqlite/SQLiteConnection:nativeExecuteForChangedRowCount	(JJ)I
    //   81: istore 4
    //   83: iload 4
    //   85: istore 6
    //   87: iload 6
    //   89: istore 8
    //   91: aload_0
    //   92: aload_3
    //   93: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   96: iload 6
    //   98: istore 4
    //   100: iload 6
    //   102: istore 5
    //   104: aload_0
    //   105: aload_1
    //   106: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   109: aload_0
    //   110: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   113: iload 7
    //   115: invokevirtual 741	android/database/sqlite/SQLiteConnection$OperationLog:endOperationDeferLog	(I)Z
    //   118: ifeq +41 -> 159
    //   121: aload_0
    //   122: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   125: astore_1
    //   126: new 265	java/lang/StringBuilder
    //   129: dup
    //   130: invokespecial 266	java/lang/StringBuilder:<init>	()V
    //   133: astore_2
    //   134: aload_2
    //   135: ldc_w 743
    //   138: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: pop
    //   142: aload_2
    //   143: iload 6
    //   145: invokevirtual 275	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   148: pop
    //   149: aload_1
    //   150: iload 7
    //   152: aload_2
    //   153: invokevirtual 282	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   156: invokevirtual 747	android/database/sqlite/SQLiteConnection$OperationLog:logOperation	(ILjava/lang/String;)V
    //   159: iload 6
    //   161: ireturn
    //   162: astore_2
    //   163: iload 6
    //   165: istore 8
    //   167: aload_0
    //   168: aload_3
    //   169: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   172: iload 6
    //   174: istore 8
    //   176: aload_2
    //   177: athrow
    //   178: astore_2
    //   179: iload 8
    //   181: istore 4
    //   183: iload 8
    //   185: istore 5
    //   187: aload_0
    //   188: aload_1
    //   189: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   192: iload 8
    //   194: istore 4
    //   196: iload 8
    //   198: istore 5
    //   200: aload_2
    //   201: athrow
    //   202: astore_1
    //   203: goto +24 -> 227
    //   206: astore_1
    //   207: iload 5
    //   209: istore 4
    //   211: aload_0
    //   212: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   215: iload 7
    //   217: aload_1
    //   218: invokevirtual 717	android/database/sqlite/SQLiteConnection$OperationLog:failOperation	(ILjava/lang/Exception;)V
    //   221: iload 5
    //   223: istore 4
    //   225: aload_1
    //   226: athrow
    //   227: aload_0
    //   228: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   231: iload 7
    //   233: invokevirtual 741	android/database/sqlite/SQLiteConnection$OperationLog:endOperationDeferLog	(I)Z
    //   236: ifeq +41 -> 277
    //   239: aload_0
    //   240: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   243: astore_3
    //   244: new 265	java/lang/StringBuilder
    //   247: dup
    //   248: invokespecial 266	java/lang/StringBuilder:<init>	()V
    //   251: astore_2
    //   252: aload_2
    //   253: ldc_w 743
    //   256: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   259: pop
    //   260: aload_2
    //   261: iload 4
    //   263: invokevirtual 275	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   266: pop
    //   267: aload_3
    //   268: iload 7
    //   270: aload_2
    //   271: invokevirtual 282	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   274: invokevirtual 747	android/database/sqlite/SQLiteConnection$OperationLog:logOperation	(ILjava/lang/String;)V
    //   277: aload_1
    //   278: athrow
    //   279: new 719	java/lang/IllegalArgumentException
    //   282: dup
    //   283: ldc_w 721
    //   286: invokespecial 722	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   289: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	290	0	this	SQLiteConnection
    //   0	290	1	paramString	String
    //   0	290	2	paramArrayOfObject	Object[]
    //   0	290	3	paramCancellationSignal	CancellationSignal
    //   5	257	4	i	int
    //   8	214	5	j	int
    //   11	162	6	k	int
    //   25	244	7	m	int
    //   35	162	8	n	int
    // Exception table:
    //   from	to	target	type
    //   70	83	162	finally
    //   37	42	178	finally
    //   46	52	178	finally
    //   56	61	178	finally
    //   65	70	178	finally
    //   91	96	178	finally
    //   167	172	178	finally
    //   176	178	178	finally
    //   27	33	202	finally
    //   104	109	202	finally
    //   187	192	202	finally
    //   200	202	202	finally
    //   211	221	202	finally
    //   225	227	202	finally
    //   27	33	206	java/lang/RuntimeException
    //   104	109	206	java/lang/RuntimeException
    //   187	192	206	java/lang/RuntimeException
    //   200	202	206	java/lang/RuntimeException
  }
  
  /* Error */
  public int executeForCursorWindow(String paramString, Object[] paramArrayOfObject, android.database.CursorWindow paramCursorWindow, int paramInt1, int paramInt2, boolean paramBoolean, CancellationSignal paramCancellationSignal)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +603 -> 604
    //   4: aload_3
    //   5: ifnull +588 -> 593
    //   8: aload_3
    //   9: invokevirtual 750	android/database/CursorWindow:acquireReference	()V
    //   12: iconst_m1
    //   13: istore 8
    //   15: iconst_m1
    //   16: istore 9
    //   18: iconst_m1
    //   19: istore 10
    //   21: aload_0
    //   22: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   25: ldc_w 751
    //   28: aload_1
    //   29: aload_2
    //   30: invokevirtual 317	android/database/sqlite/SQLiteConnection$OperationLog:beginOperation	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   33: istore 11
    //   35: aload_0
    //   36: aload_1
    //   37: invokespecial 699	android/database/sqlite/SQLiteConnection:acquirePreparedStatement	(Ljava/lang/String;)Landroid/database/sqlite/SQLiteConnection$PreparedStatement;
    //   40: astore 12
    //   42: aload_0
    //   43: aload 12
    //   45: invokespecial 701	android/database/sqlite/SQLiteConnection:throwIfStatementForbidden	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   48: aload_0
    //   49: aload 12
    //   51: aload_2
    //   52: invokespecial 703	android/database/sqlite/SQLiteConnection:bindArguments	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;[Ljava/lang/Object;)V
    //   55: aload_0
    //   56: aload 12
    //   58: invokespecial 705	android/database/sqlite/SQLiteConnection:applyBlockGuardPolicy	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   61: aload_0
    //   62: aload 7
    //   64: invokespecial 707	android/database/sqlite/SQLiteConnection:attachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   67: aload_0
    //   68: getfield 135	android/database/sqlite/SQLiteConnection:mConnectionPtr	J
    //   71: lstore 13
    //   73: aload 12
    //   75: getfield 219	android/database/sqlite/SQLiteConnection$PreparedStatement:mStatementPtr	J
    //   78: lstore 15
    //   80: aload_3
    //   81: getfield 754	android/database/CursorWindow:mWindowPtr	J
    //   84: lstore 17
    //   86: iload 11
    //   88: istore 19
    //   90: lload 13
    //   92: lload 15
    //   94: lload 17
    //   96: iload 4
    //   98: iload 5
    //   100: iload 6
    //   102: invokestatic 756	android/database/sqlite/SQLiteConnection:nativeExecuteForCursorWindow	(JJJIIZ)J
    //   105: lstore 17
    //   107: lload 17
    //   109: bipush 32
    //   111: lshr
    //   112: l2i
    //   113: istore 20
    //   115: lload 17
    //   117: l2i
    //   118: istore 9
    //   120: aload_3
    //   121: invokevirtual 637	android/database/CursorWindow:getNumRows	()I
    //   124: istore 5
    //   126: aload_3
    //   127: iload 20
    //   129: invokevirtual 759	android/database/CursorWindow:setStartPosition	(I)V
    //   132: aload_0
    //   133: aload 7
    //   135: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   138: aload_0
    //   139: aload 12
    //   141: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   144: aload_0
    //   145: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   148: iload 19
    //   150: invokevirtual 741	android/database/sqlite/SQLiteConnection$OperationLog:endOperationDeferLog	(I)Z
    //   153: ifeq +100 -> 253
    //   156: aload_0
    //   157: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   160: astore_2
    //   161: new 265	java/lang/StringBuilder
    //   164: astore_1
    //   165: aload_1
    //   166: invokespecial 266	java/lang/StringBuilder:<init>	()V
    //   169: aload_1
    //   170: ldc_w 761
    //   173: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: pop
    //   177: aload_1
    //   178: aload_3
    //   179: invokevirtual 764	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   182: pop
    //   183: aload_1
    //   184: ldc_w 766
    //   187: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   190: pop
    //   191: aload_1
    //   192: iload 4
    //   194: invokevirtual 275	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   197: pop
    //   198: aload_1
    //   199: ldc_w 768
    //   202: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   205: pop
    //   206: aload_1
    //   207: iload 20
    //   209: invokevirtual 275	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   212: pop
    //   213: aload_1
    //   214: ldc_w 770
    //   217: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: pop
    //   221: aload_1
    //   222: iload 5
    //   224: invokevirtual 275	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   227: pop
    //   228: aload_1
    //   229: ldc_w 772
    //   232: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   235: pop
    //   236: aload_1
    //   237: iload 9
    //   239: invokevirtual 275	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   242: pop
    //   243: aload_2
    //   244: iload 19
    //   246: aload_1
    //   247: invokevirtual 282	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   250: invokevirtual 747	android/database/sqlite/SQLiteConnection$OperationLog:logOperation	(ILjava/lang/String;)V
    //   253: aload_3
    //   254: invokevirtual 775	android/database/CursorWindow:releaseReference	()V
    //   257: iload 9
    //   259: ireturn
    //   260: astore_1
    //   261: goto +212 -> 473
    //   264: astore_1
    //   265: iload 5
    //   267: istore 10
    //   269: iload 20
    //   271: istore 5
    //   273: iload 10
    //   275: istore 20
    //   277: goto +147 -> 424
    //   280: astore_1
    //   281: iload 5
    //   283: istore 10
    //   285: iload 20
    //   287: istore 5
    //   289: iload 10
    //   291: istore 20
    //   293: goto +73 -> 366
    //   296: astore_1
    //   297: iload 20
    //   299: istore 10
    //   301: iload 5
    //   303: istore 20
    //   305: iload 10
    //   307: istore 5
    //   309: goto +36 -> 345
    //   312: astore_1
    //   313: iload 20
    //   315: istore 5
    //   317: iload 10
    //   319: istore 20
    //   321: goto +24 -> 345
    //   324: astore_1
    //   325: iload 8
    //   327: istore 5
    //   329: iload 10
    //   331: istore 20
    //   333: goto +12 -> 345
    //   336: astore_1
    //   337: iload 10
    //   339: istore 20
    //   341: iload 8
    //   343: istore 5
    //   345: aload_0
    //   346: aload 7
    //   348: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   351: aload_1
    //   352: athrow
    //   353: astore_1
    //   354: goto +12 -> 366
    //   357: astore_1
    //   358: iload 10
    //   360: istore 20
    //   362: iload 8
    //   364: istore 5
    //   366: iload 5
    //   368: istore 19
    //   370: iload 9
    //   372: istore 8
    //   374: iload 20
    //   376: istore 10
    //   378: aload_0
    //   379: aload 12
    //   381: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   384: iload 5
    //   386: istore 19
    //   388: iload 9
    //   390: istore 8
    //   392: iload 20
    //   394: istore 10
    //   396: aload_1
    //   397: athrow
    //   398: astore_1
    //   399: goto +25 -> 424
    //   402: astore_1
    //   403: iconst_m1
    //   404: istore 20
    //   406: iconst_m1
    //   407: istore 9
    //   409: iconst_m1
    //   410: istore 5
    //   412: goto +61 -> 473
    //   415: astore_1
    //   416: iload 10
    //   418: istore 20
    //   420: iload 8
    //   422: istore 5
    //   424: iload 5
    //   426: istore 19
    //   428: iload 9
    //   430: istore 8
    //   432: iload 20
    //   434: istore 10
    //   436: aload_0
    //   437: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   440: iload 11
    //   442: aload_1
    //   443: invokevirtual 717	android/database/sqlite/SQLiteConnection$OperationLog:failOperation	(ILjava/lang/Exception;)V
    //   446: iload 5
    //   448: istore 19
    //   450: iload 9
    //   452: istore 8
    //   454: iload 20
    //   456: istore 10
    //   458: aload_1
    //   459: athrow
    //   460: astore_1
    //   461: iload 19
    //   463: istore 20
    //   465: iload 8
    //   467: istore 9
    //   469: iload 10
    //   471: istore 5
    //   473: aload_0
    //   474: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   477: iload 11
    //   479: invokevirtual 741	android/database/sqlite/SQLiteConnection$OperationLog:endOperationDeferLog	(I)Z
    //   482: ifeq +102 -> 584
    //   485: aload_0
    //   486: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   489: astore 7
    //   491: new 265	java/lang/StringBuilder
    //   494: astore_2
    //   495: aload_2
    //   496: invokespecial 266	java/lang/StringBuilder:<init>	()V
    //   499: aload_2
    //   500: ldc_w 761
    //   503: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   506: pop
    //   507: aload_2
    //   508: aload_3
    //   509: invokevirtual 764	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   512: pop
    //   513: aload_2
    //   514: ldc_w 766
    //   517: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   520: pop
    //   521: aload_2
    //   522: iload 4
    //   524: invokevirtual 275	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   527: pop
    //   528: aload_2
    //   529: ldc_w 768
    //   532: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   535: pop
    //   536: aload_2
    //   537: iload 20
    //   539: invokevirtual 275	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   542: pop
    //   543: aload_2
    //   544: ldc_w 770
    //   547: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   550: pop
    //   551: aload_2
    //   552: iload 5
    //   554: invokevirtual 275	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   557: pop
    //   558: aload_2
    //   559: ldc_w 772
    //   562: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   565: pop
    //   566: aload_2
    //   567: iload 9
    //   569: invokevirtual 275	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   572: pop
    //   573: aload 7
    //   575: iload 11
    //   577: aload_2
    //   578: invokevirtual 282	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   581: invokevirtual 747	android/database/sqlite/SQLiteConnection$OperationLog:logOperation	(ILjava/lang/String;)V
    //   584: aload_1
    //   585: athrow
    //   586: astore_1
    //   587: aload_3
    //   588: invokevirtual 775	android/database/CursorWindow:releaseReference	()V
    //   591: aload_1
    //   592: athrow
    //   593: new 719	java/lang/IllegalArgumentException
    //   596: dup
    //   597: ldc_w 777
    //   600: invokespecial 722	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   603: athrow
    //   604: new 719	java/lang/IllegalArgumentException
    //   607: dup
    //   608: ldc_w 721
    //   611: invokespecial 722	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   614: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	615	0	this	SQLiteConnection
    //   0	615	1	paramString	String
    //   0	615	2	paramArrayOfObject	Object[]
    //   0	615	3	paramCursorWindow	android.database.CursorWindow
    //   0	615	4	paramInt1	int
    //   0	615	5	paramInt2	int
    //   0	615	6	paramBoolean	boolean
    //   0	615	7	paramCancellationSignal	CancellationSignal
    //   13	453	8	i	int
    //   16	552	9	j	int
    //   19	451	10	k	int
    //   33	543	11	m	int
    //   40	340	12	localPreparedStatement	PreparedStatement
    //   71	20	13	l1	long
    //   78	15	15	l2	long
    //   84	32	17	l3	long
    //   88	374	19	n	int
    //   113	425	20	i1	int
    // Exception table:
    //   from	to	target	type
    //   138	144	260	finally
    //   138	144	264	java/lang/RuntimeException
    //   132	138	280	finally
    //   126	132	296	finally
    //   120	126	312	finally
    //   90	107	324	finally
    //   67	86	336	finally
    //   345	353	353	finally
    //   42	67	357	finally
    //   378	384	398	java/lang/RuntimeException
    //   396	398	398	java/lang/RuntimeException
    //   35	42	402	finally
    //   35	42	415	java/lang/RuntimeException
    //   378	384	460	finally
    //   396	398	460	finally
    //   436	446	460	finally
    //   458	460	460	finally
    //   21	35	586	finally
    //   144	253	586	finally
    //   473	584	586	finally
    //   584	586	586	finally
  }
  
  /* Error */
  public long executeForLastInsertedRowId(String paramString, Object[] paramArrayOfObject, CancellationSignal paramCancellationSignal)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +123 -> 124
    //   4: aload_0
    //   5: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   8: ldc_w 779
    //   11: aload_1
    //   12: aload_2
    //   13: invokevirtual 317	android/database/sqlite/SQLiteConnection$OperationLog:beginOperation	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   16: istore 4
    //   18: aload_0
    //   19: aload_1
    //   20: invokespecial 699	android/database/sqlite/SQLiteConnection:acquirePreparedStatement	(Ljava/lang/String;)Landroid/database/sqlite/SQLiteConnection$PreparedStatement;
    //   23: astore_1
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 701	android/database/sqlite/SQLiteConnection:throwIfStatementForbidden	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   29: aload_0
    //   30: aload_1
    //   31: aload_2
    //   32: invokespecial 703	android/database/sqlite/SQLiteConnection:bindArguments	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;[Ljava/lang/Object;)V
    //   35: aload_0
    //   36: aload_1
    //   37: invokespecial 705	android/database/sqlite/SQLiteConnection:applyBlockGuardPolicy	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   40: aload_0
    //   41: aload_3
    //   42: invokespecial 707	android/database/sqlite/SQLiteConnection:attachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   45: aload_0
    //   46: getfield 135	android/database/sqlite/SQLiteConnection:mConnectionPtr	J
    //   49: aload_1
    //   50: getfield 219	android/database/sqlite/SQLiteConnection$PreparedStatement:mStatementPtr	J
    //   53: invokestatic 781	android/database/sqlite/SQLiteConnection:nativeExecuteForLastInsertedRowId	(JJ)J
    //   56: lstore 5
    //   58: aload_0
    //   59: aload_3
    //   60: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   63: aload_0
    //   64: aload_1
    //   65: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   68: aload_0
    //   69: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   72: iload 4
    //   74: invokevirtual 328	android/database/sqlite/SQLiteConnection$OperationLog:endOperation	(I)V
    //   77: lload 5
    //   79: lreturn
    //   80: astore_2
    //   81: aload_0
    //   82: aload_3
    //   83: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   86: aload_2
    //   87: athrow
    //   88: astore_2
    //   89: aload_0
    //   90: aload_1
    //   91: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   94: aload_2
    //   95: athrow
    //   96: astore_1
    //   97: goto +16 -> 113
    //   100: astore_1
    //   101: aload_0
    //   102: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   105: iload 4
    //   107: aload_1
    //   108: invokevirtual 717	android/database/sqlite/SQLiteConnection$OperationLog:failOperation	(ILjava/lang/Exception;)V
    //   111: aload_1
    //   112: athrow
    //   113: aload_0
    //   114: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   117: iload 4
    //   119: invokevirtual 328	android/database/sqlite/SQLiteConnection$OperationLog:endOperation	(I)V
    //   122: aload_1
    //   123: athrow
    //   124: new 719	java/lang/IllegalArgumentException
    //   127: dup
    //   128: ldc_w 721
    //   131: invokespecial 722	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   134: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	135	0	this	SQLiteConnection
    //   0	135	1	paramString	String
    //   0	135	2	paramArrayOfObject	Object[]
    //   0	135	3	paramCancellationSignal	CancellationSignal
    //   16	102	4	i	int
    //   56	22	5	l	long
    // Exception table:
    //   from	to	target	type
    //   45	58	80	finally
    //   24	45	88	finally
    //   58	63	88	finally
    //   81	88	88	finally
    //   18	24	96	finally
    //   63	68	96	finally
    //   89	96	96	finally
    //   101	113	96	finally
    //   18	24	100	java/lang/RuntimeException
    //   63	68	100	java/lang/RuntimeException
    //   89	96	100	java/lang/RuntimeException
  }
  
  /* Error */
  public long executeForLong(String paramString, Object[] paramArrayOfObject, CancellationSignal paramCancellationSignal)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +123 -> 124
    //   4: aload_0
    //   5: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   8: ldc_w 782
    //   11: aload_1
    //   12: aload_2
    //   13: invokevirtual 317	android/database/sqlite/SQLiteConnection$OperationLog:beginOperation	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   16: istore 4
    //   18: aload_0
    //   19: aload_1
    //   20: invokespecial 699	android/database/sqlite/SQLiteConnection:acquirePreparedStatement	(Ljava/lang/String;)Landroid/database/sqlite/SQLiteConnection$PreparedStatement;
    //   23: astore_1
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 701	android/database/sqlite/SQLiteConnection:throwIfStatementForbidden	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   29: aload_0
    //   30: aload_1
    //   31: aload_2
    //   32: invokespecial 703	android/database/sqlite/SQLiteConnection:bindArguments	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;[Ljava/lang/Object;)V
    //   35: aload_0
    //   36: aload_1
    //   37: invokespecial 705	android/database/sqlite/SQLiteConnection:applyBlockGuardPolicy	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   40: aload_0
    //   41: aload_3
    //   42: invokespecial 707	android/database/sqlite/SQLiteConnection:attachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   45: aload_0
    //   46: getfield 135	android/database/sqlite/SQLiteConnection:mConnectionPtr	J
    //   49: aload_1
    //   50: getfield 219	android/database/sqlite/SQLiteConnection$PreparedStatement:mStatementPtr	J
    //   53: invokestatic 784	android/database/sqlite/SQLiteConnection:nativeExecuteForLong	(JJ)J
    //   56: lstore 5
    //   58: aload_0
    //   59: aload_3
    //   60: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   63: aload_0
    //   64: aload_1
    //   65: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   68: aload_0
    //   69: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   72: iload 4
    //   74: invokevirtual 328	android/database/sqlite/SQLiteConnection$OperationLog:endOperation	(I)V
    //   77: lload 5
    //   79: lreturn
    //   80: astore_2
    //   81: aload_0
    //   82: aload_3
    //   83: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   86: aload_2
    //   87: athrow
    //   88: astore_2
    //   89: aload_0
    //   90: aload_1
    //   91: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   94: aload_2
    //   95: athrow
    //   96: astore_1
    //   97: goto +16 -> 113
    //   100: astore_1
    //   101: aload_0
    //   102: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   105: iload 4
    //   107: aload_1
    //   108: invokevirtual 717	android/database/sqlite/SQLiteConnection$OperationLog:failOperation	(ILjava/lang/Exception;)V
    //   111: aload_1
    //   112: athrow
    //   113: aload_0
    //   114: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   117: iload 4
    //   119: invokevirtual 328	android/database/sqlite/SQLiteConnection$OperationLog:endOperation	(I)V
    //   122: aload_1
    //   123: athrow
    //   124: new 719	java/lang/IllegalArgumentException
    //   127: dup
    //   128: ldc_w 721
    //   131: invokespecial 722	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   134: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	135	0	this	SQLiteConnection
    //   0	135	1	paramString	String
    //   0	135	2	paramArrayOfObject	Object[]
    //   0	135	3	paramCancellationSignal	CancellationSignal
    //   16	102	4	i	int
    //   56	22	5	l	long
    // Exception table:
    //   from	to	target	type
    //   45	58	80	finally
    //   24	45	88	finally
    //   58	63	88	finally
    //   81	88	88	finally
    //   18	24	96	finally
    //   63	68	96	finally
    //   89	96	96	finally
    //   101	113	96	finally
    //   18	24	100	java/lang/RuntimeException
    //   63	68	100	java/lang/RuntimeException
    //   89	96	100	java/lang/RuntimeException
  }
  
  /* Error */
  public String executeForString(String paramString, Object[] paramArrayOfObject, CancellationSignal paramCancellationSignal)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +121 -> 122
    //   4: aload_0
    //   5: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   8: ldc_w 785
    //   11: aload_1
    //   12: aload_2
    //   13: invokevirtual 317	android/database/sqlite/SQLiteConnection$OperationLog:beginOperation	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   16: istore 4
    //   18: aload_0
    //   19: aload_1
    //   20: invokespecial 699	android/database/sqlite/SQLiteConnection:acquirePreparedStatement	(Ljava/lang/String;)Landroid/database/sqlite/SQLiteConnection$PreparedStatement;
    //   23: astore_1
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial 701	android/database/sqlite/SQLiteConnection:throwIfStatementForbidden	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   29: aload_0
    //   30: aload_1
    //   31: aload_2
    //   32: invokespecial 703	android/database/sqlite/SQLiteConnection:bindArguments	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;[Ljava/lang/Object;)V
    //   35: aload_0
    //   36: aload_1
    //   37: invokespecial 705	android/database/sqlite/SQLiteConnection:applyBlockGuardPolicy	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   40: aload_0
    //   41: aload_3
    //   42: invokespecial 707	android/database/sqlite/SQLiteConnection:attachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   45: aload_0
    //   46: getfield 135	android/database/sqlite/SQLiteConnection:mConnectionPtr	J
    //   49: aload_1
    //   50: getfield 219	android/database/sqlite/SQLiteConnection$PreparedStatement:mStatementPtr	J
    //   53: invokestatic 787	android/database/sqlite/SQLiteConnection:nativeExecuteForString	(JJ)Ljava/lang/String;
    //   56: astore_2
    //   57: aload_0
    //   58: aload_3
    //   59: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   62: aload_0
    //   63: aload_1
    //   64: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   67: aload_0
    //   68: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   71: iload 4
    //   73: invokevirtual 328	android/database/sqlite/SQLiteConnection$OperationLog:endOperation	(I)V
    //   76: aload_2
    //   77: areturn
    //   78: astore_2
    //   79: aload_0
    //   80: aload_3
    //   81: invokespecial 711	android/database/sqlite/SQLiteConnection:detachCancellationSignal	(Landroid/os/CancellationSignal;)V
    //   84: aload_2
    //   85: athrow
    //   86: astore_2
    //   87: aload_0
    //   88: aload_1
    //   89: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   92: aload_2
    //   93: athrow
    //   94: astore_1
    //   95: goto +16 -> 111
    //   98: astore_1
    //   99: aload_0
    //   100: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   103: iload 4
    //   105: aload_1
    //   106: invokevirtual 717	android/database/sqlite/SQLiteConnection$OperationLog:failOperation	(ILjava/lang/Exception;)V
    //   109: aload_1
    //   110: athrow
    //   111: aload_0
    //   112: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   115: iload 4
    //   117: invokevirtual 328	android/database/sqlite/SQLiteConnection$OperationLog:endOperation	(I)V
    //   120: aload_1
    //   121: athrow
    //   122: new 719	java/lang/IllegalArgumentException
    //   125: dup
    //   126: ldc_w 721
    //   129: invokespecial 722	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   132: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	133	0	this	SQLiteConnection
    //   0	133	1	paramString	String
    //   0	133	2	paramArrayOfObject	Object[]
    //   0	133	3	paramCancellationSignal	CancellationSignal
    //   16	100	4	i	int
    // Exception table:
    //   from	to	target	type
    //   45	57	78	finally
    //   24	45	86	finally
    //   57	62	86	finally
    //   79	86	86	finally
    //   18	24	94	finally
    //   62	67	94	finally
    //   87	94	94	finally
    //   99	111	94	finally
    //   18	24	98	java/lang/RuntimeException
    //   62	67	98	java/lang/RuntimeException
    //   87	94	98	java/lang/RuntimeException
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if ((mPool != null) && (mConnectionPtr != 0L)) {
        mPool.onConnectionLeaked();
      }
      dispose(true);
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getConnectionId()
  {
    return mConnectionId;
  }
  
  boolean isPreparedStatementInCache(String paramString)
  {
    boolean bool;
    if (mPreparedStatementCache.get(paramString) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPrimaryConnection()
  {
    return mIsPrimaryConnection;
  }
  
  public void onCancel()
  {
    nativeCancel(mConnectionPtr);
  }
  
  /* Error */
  public void prepare(String paramString, SQLiteStatementInfo paramSQLiteStatementInfo)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +163 -> 164
    //   4: aload_0
    //   5: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   8: ldc_w 807
    //   11: aload_1
    //   12: aconst_null
    //   13: invokevirtual 317	android/database/sqlite/SQLiteConnection$OperationLog:beginOperation	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)I
    //   16: istore_3
    //   17: aload_0
    //   18: aload_1
    //   19: invokespecial 699	android/database/sqlite/SQLiteConnection:acquirePreparedStatement	(Ljava/lang/String;)Landroid/database/sqlite/SQLiteConnection$PreparedStatement;
    //   22: astore_1
    //   23: aload_2
    //   24: ifnull +100 -> 124
    //   27: aload_2
    //   28: aload_1
    //   29: getfield 216	android/database/sqlite/SQLiteConnection$PreparedStatement:mNumParameters	I
    //   32: putfield 812	android/database/sqlite/SQLiteStatementInfo:numParameters	I
    //   35: aload_2
    //   36: aload_1
    //   37: getfield 180	android/database/sqlite/SQLiteConnection$PreparedStatement:mReadOnly	Z
    //   40: putfield 815	android/database/sqlite/SQLiteStatementInfo:readOnly	Z
    //   43: aload_0
    //   44: getfield 135	android/database/sqlite/SQLiteConnection:mConnectionPtr	J
    //   47: aload_1
    //   48: getfield 219	android/database/sqlite/SQLiteConnection$PreparedStatement:mStatementPtr	J
    //   51: invokestatic 817	android/database/sqlite/SQLiteConnection:nativeGetColumnCount	(JJ)I
    //   54: istore 4
    //   56: iload 4
    //   58: ifne +13 -> 71
    //   61: aload_2
    //   62: getstatic 58	android/database/sqlite/SQLiteConnection:EMPTY_STRING_ARRAY	[Ljava/lang/String;
    //   65: putfield 820	android/database/sqlite/SQLiteStatementInfo:columnNames	[Ljava/lang/String;
    //   68: goto +56 -> 124
    //   71: aload_2
    //   72: iload 4
    //   74: anewarray 56	java/lang/String
    //   77: putfield 820	android/database/sqlite/SQLiteStatementInfo:columnNames	[Ljava/lang/String;
    //   80: iconst_0
    //   81: istore 5
    //   83: iload 5
    //   85: iload 4
    //   87: if_icmpge +37 -> 124
    //   90: aload_2
    //   91: getfield 820	android/database/sqlite/SQLiteStatementInfo:columnNames	[Ljava/lang/String;
    //   94: iload 5
    //   96: aload_0
    //   97: getfield 135	android/database/sqlite/SQLiteConnection:mConnectionPtr	J
    //   100: aload_1
    //   101: getfield 219	android/database/sqlite/SQLiteConnection$PreparedStatement:mStatementPtr	J
    //   104: iload 5
    //   106: invokestatic 822	android/database/sqlite/SQLiteConnection:nativeGetColumnName	(JJI)Ljava/lang/String;
    //   109: aastore
    //   110: iinc 5 1
    //   113: goto -30 -> 83
    //   116: astore_2
    //   117: aload_0
    //   118: aload_1
    //   119: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   122: aload_2
    //   123: athrow
    //   124: aload_0
    //   125: aload_1
    //   126: invokespecial 713	android/database/sqlite/SQLiteConnection:releasePreparedStatement	(Landroid/database/sqlite/SQLiteConnection$PreparedStatement;)V
    //   129: aload_0
    //   130: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   133: iload_3
    //   134: invokevirtual 328	android/database/sqlite/SQLiteConnection$OperationLog:endOperation	(I)V
    //   137: return
    //   138: astore_1
    //   139: goto +15 -> 154
    //   142: astore_1
    //   143: aload_0
    //   144: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   147: iload_3
    //   148: aload_1
    //   149: invokevirtual 717	android/database/sqlite/SQLiteConnection$OperationLog:failOperation	(ILjava/lang/Exception;)V
    //   152: aload_1
    //   153: athrow
    //   154: aload_0
    //   155: getfield 80	android/database/sqlite/SQLiteConnection:mRecentOperations	Landroid/database/sqlite/SQLiteConnection$OperationLog;
    //   158: iload_3
    //   159: invokevirtual 328	android/database/sqlite/SQLiteConnection$OperationLog:endOperation	(I)V
    //   162: aload_1
    //   163: athrow
    //   164: new 719	java/lang/IllegalArgumentException
    //   167: dup
    //   168: ldc_w 721
    //   171: invokespecial 722	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   174: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	175	0	this	SQLiteConnection
    //   0	175	1	paramString	String
    //   0	175	2	paramSQLiteStatementInfo	SQLiteStatementInfo
    //   16	143	3	i	int
    //   54	34	4	j	int
    //   81	30	5	k	int
    // Exception table:
    //   from	to	target	type
    //   27	56	116	finally
    //   61	68	116	finally
    //   71	80	116	finally
    //   90	110	116	finally
    //   17	23	138	finally
    //   117	124	138	finally
    //   124	129	138	finally
    //   143	154	138	finally
    //   17	23	142	java/lang/RuntimeException
    //   117	124	142	java/lang/RuntimeException
    //   124	129	142	java/lang/RuntimeException
  }
  
  void reconfigure(SQLiteDatabaseConfiguration paramSQLiteDatabaseConfiguration)
  {
    int i = 0;
    mOnlyAllowReadOnlyOperations = false;
    int j = customFunctions.size();
    for (int k = 0; k < j; k++)
    {
      SQLiteCustomFunction localSQLiteCustomFunction = (SQLiteCustomFunction)customFunctions.get(k);
      if (!mConfiguration.customFunctions.contains(localSQLiteCustomFunction)) {
        nativeRegisterCustomFunction(mConnectionPtr, localSQLiteCustomFunction);
      }
    }
    if (foreignKeyConstraintsEnabled != mConfiguration.foreignKeyConstraintsEnabled) {
      k = 1;
    } else {
      k = 0;
    }
    if (((openFlags ^ mConfiguration.openFlags) & 0x60000000) != 0) {
      i = 1;
    }
    boolean bool = locale.equals(mConfiguration.locale);
    mConfiguration.updateParametersFrom(paramSQLiteDatabaseConfiguration);
    mPreparedStatementCache.resize(maxSqlCacheSize);
    if (k != 0) {
      setForeignKeyModeFromConfiguration();
    }
    if (i != 0) {
      setWalModeFromConfiguration();
    }
    if ((bool ^ true)) {
      setLocaleFromConfiguration();
    }
  }
  
  void setOnlyAllowReadOnlyOperations(boolean paramBoolean)
  {
    mOnlyAllowReadOnlyOperations = paramBoolean;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SQLiteConnection: ");
    localStringBuilder.append(mConfiguration.path);
    localStringBuilder.append(" (");
    localStringBuilder.append(mConnectionId);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  private static final class Operation
  {
    private static final int MAX_TRACE_METHOD_NAME_LEN = 256;
    public ArrayList<Object> mBindArgs;
    public int mCookie;
    public long mEndTime;
    public Exception mException;
    public boolean mFinished;
    public String mKind;
    public String mSql;
    public long mStartTime;
    public long mStartWallTime;
    
    private Operation() {}
    
    private String getStatus()
    {
      if (!mFinished) {
        return "running";
      }
      String str;
      if (mException != null) {
        str = "failed";
      } else {
        str = "succeeded";
      }
      return str;
    }
    
    private String getTraceMethodName()
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(mKind);
      ((StringBuilder)localObject).append(" ");
      ((StringBuilder)localObject).append(mSql);
      localObject = ((StringBuilder)localObject).toString();
      if (((String)localObject).length() > 256) {
        return ((String)localObject).substring(0, 256);
      }
      return localObject;
    }
    
    public void describe(StringBuilder paramStringBuilder, boolean paramBoolean)
    {
      paramStringBuilder.append(mKind);
      if (mFinished)
      {
        paramStringBuilder.append(" took ");
        paramStringBuilder.append(mEndTime - mStartTime);
        paramStringBuilder.append("ms");
      }
      else
      {
        paramStringBuilder.append(" started ");
        paramStringBuilder.append(System.currentTimeMillis() - mStartWallTime);
        paramStringBuilder.append("ms ago");
      }
      paramStringBuilder.append(" - ");
      paramStringBuilder.append(getStatus());
      if (mSql != null)
      {
        paramStringBuilder.append(", sql=\"");
        paramStringBuilder.append(SQLiteConnection.trimSqlForDisplay(mSql));
        paramStringBuilder.append("\"");
      }
      if ((paramBoolean) && (mBindArgs != null) && (mBindArgs.size() != 0))
      {
        paramStringBuilder.append(", bindArgs=[");
        int i = mBindArgs.size();
        for (int j = 0; j < i; j++)
        {
          Object localObject = mBindArgs.get(j);
          if (j != 0) {
            paramStringBuilder.append(", ");
          }
          if (localObject == null)
          {
            paramStringBuilder.append("null");
          }
          else if ((localObject instanceof byte[]))
          {
            paramStringBuilder.append("<byte[]>");
          }
          else if ((localObject instanceof String))
          {
            paramStringBuilder.append("\"");
            paramStringBuilder.append((String)localObject);
            paramStringBuilder.append("\"");
          }
          else
          {
            paramStringBuilder.append(localObject);
          }
        }
        paramStringBuilder.append("]");
      }
      if (mException != null)
      {
        paramStringBuilder.append(", exception=\"");
        paramStringBuilder.append(mException.getMessage());
        paramStringBuilder.append("\"");
      }
    }
  }
  
  private static final class OperationLog
  {
    private static final int COOKIE_GENERATION_SHIFT = 8;
    private static final int COOKIE_INDEX_MASK = 255;
    private static final int MAX_RECENT_OPERATIONS = 20;
    private int mGeneration;
    private int mIndex;
    private final SQLiteConnection.Operation[] mOperations = new SQLiteConnection.Operation[20];
    private final SQLiteConnectionPool mPool;
    
    OperationLog(SQLiteConnectionPool paramSQLiteConnectionPool)
    {
      mPool = paramSQLiteConnectionPool;
    }
    
    private boolean endOperationDeferLogLocked(int paramInt)
    {
      SQLiteConnection.Operation localOperation = getOperationLocked(paramInt);
      boolean bool1 = false;
      if (localOperation != null)
      {
        if (Trace.isTagEnabled(1048576L)) {
          Trace.asyncTraceEnd(1048576L, localOperation.getTraceMethodName(), mCookie);
        }
        mEndTime = SystemClock.uptimeMillis();
        mFinished = true;
        long l = mEndTime - mStartTime;
        mPool.onStatementExecuted(l);
        boolean bool2 = bool1;
        if (SQLiteDebug.DEBUG_LOG_SLOW_QUERIES)
        {
          bool2 = bool1;
          if (SQLiteDebug.shouldLogSlowQuery(l)) {
            bool2 = true;
          }
        }
        return bool2;
      }
      return false;
    }
    
    private SQLiteConnection.Operation getOperationLocked(int paramInt)
    {
      SQLiteConnection.Operation localOperation = mOperations[(paramInt & 0xFF)];
      if (mCookie != paramInt) {
        localOperation = null;
      }
      return localOperation;
    }
    
    private void logOperationLocked(int paramInt, String paramString)
    {
      SQLiteConnection.Operation localOperation = getOperationLocked(paramInt);
      StringBuilder localStringBuilder = new StringBuilder();
      localOperation.describe(localStringBuilder, false);
      if (paramString != null)
      {
        localStringBuilder.append(", ");
        localStringBuilder.append(paramString);
      }
      Log.d("SQLiteConnection", localStringBuilder.toString());
    }
    
    private int newOperationCookieLocked(int paramInt)
    {
      int i = mGeneration;
      mGeneration = (i + 1);
      return i << 8 | paramInt;
    }
    
    public int beginOperation(String paramString1, String paramString2, Object[] paramArrayOfObject)
    {
      synchronized (mOperations)
      {
        int i = (mIndex + 1) % 20;
        SQLiteConnection.Operation localOperation1 = mOperations[i];
        int j = 0;
        SQLiteConnection.Operation localOperation2;
        if (localOperation1 == null)
        {
          localOperation2 = new android/database/sqlite/SQLiteConnection$Operation;
          localOperation2.<init>(null);
          mOperations[i] = localOperation2;
        }
        else
        {
          mFinished = false;
          mException = null;
          localOperation2 = localOperation1;
          if (mBindArgs != null)
          {
            mBindArgs.clear();
            localOperation2 = localOperation1;
          }
        }
        mStartWallTime = System.currentTimeMillis();
        mStartTime = SystemClock.uptimeMillis();
        mKind = paramString1;
        mSql = paramString2;
        if (paramArrayOfObject != null)
        {
          if (mBindArgs == null)
          {
            paramString1 = new java/util/ArrayList;
            paramString1.<init>();
            mBindArgs = paramString1;
          }
          else
          {
            mBindArgs.clear();
          }
          while (j < paramArrayOfObject.length)
          {
            paramString1 = paramArrayOfObject[j];
            if ((paramString1 != null) && ((paramString1 instanceof byte[]))) {
              mBindArgs.add(SQLiteConnection.EMPTY_BYTE_ARRAY);
            } else {
              mBindArgs.add(paramString1);
            }
            j++;
          }
        }
        mCookie = newOperationCookieLocked(i);
        if (Trace.isTagEnabled(1048576L)) {
          Trace.asyncTraceBegin(1048576L, localOperation2.getTraceMethodName(), mCookie);
        }
        mIndex = i;
        j = mCookie;
        return j;
      }
    }
    
    public String describeCurrentOperation()
    {
      synchronized (mOperations)
      {
        Object localObject1 = mOperations[mIndex];
        if ((localObject1 != null) && (!mFinished))
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          ((SQLiteConnection.Operation)localObject1).describe(localStringBuilder, false);
          localObject1 = localStringBuilder.toString();
          return localObject1;
        }
        return null;
      }
    }
    
    public void dump(Printer paramPrinter, boolean paramBoolean)
    {
      synchronized (mOperations)
      {
        paramPrinter.println("  Most recently executed operations:");
        int i = mIndex;
        SQLiteConnection.Operation localOperation = mOperations[i];
        if (localOperation != null)
        {
          SimpleDateFormat localSimpleDateFormat = new java/text/SimpleDateFormat;
          localSimpleDateFormat.<init>("yyyy-MM-dd HH:mm:ss.SSS");
          int j = 0;
          int k;
          do
          {
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("    ");
            localStringBuilder.append(j);
            localStringBuilder.append(": [");
            Date localDate = new java/util/Date;
            localDate.<init>(mStartWallTime);
            localStringBuilder.append(localSimpleDateFormat.format(localDate));
            localStringBuilder.append("] ");
            localOperation.describe(localStringBuilder, paramBoolean);
            paramPrinter.println(localStringBuilder.toString());
            if (i > 0) {
              i--;
            } else {
              i = 19;
            }
            k = j + 1;
            localOperation = mOperations[i];
            if (localOperation == null) {
              break;
            }
            j = k;
          } while (k < 20);
        }
        else
        {
          paramPrinter.println("    <none>");
        }
        return;
      }
    }
    
    public void endOperation(int paramInt)
    {
      synchronized (mOperations)
      {
        if (endOperationDeferLogLocked(paramInt)) {
          logOperationLocked(paramInt, null);
        }
        return;
      }
    }
    
    public boolean endOperationDeferLog(int paramInt)
    {
      synchronized (mOperations)
      {
        boolean bool = endOperationDeferLogLocked(paramInt);
        return bool;
      }
    }
    
    public void failOperation(int paramInt, Exception paramException)
    {
      synchronized (mOperations)
      {
        SQLiteConnection.Operation localOperation = getOperationLocked(paramInt);
        if (localOperation != null) {
          mException = paramException;
        }
        return;
      }
    }
    
    public void logOperation(int paramInt, String paramString)
    {
      synchronized (mOperations)
      {
        logOperationLocked(paramInt, paramString);
        return;
      }
    }
  }
  
  private static final class PreparedStatement
  {
    public boolean mInCache;
    public boolean mInUse;
    public int mNumParameters;
    public PreparedStatement mPoolNext;
    public boolean mReadOnly;
    public String mSql;
    public long mStatementPtr;
    public int mType;
    
    private PreparedStatement() {}
  }
  
  private final class PreparedStatementCache
    extends LruCache<String, SQLiteConnection.PreparedStatement>
  {
    public PreparedStatementCache(int paramInt)
    {
      super();
    }
    
    public void dump(Printer paramPrinter)
    {
      paramPrinter.println("  Prepared statement cache:");
      Object localObject1 = snapshot();
      if (!((Map)localObject1).isEmpty())
      {
        int i = 0;
        localObject1 = ((Map)localObject1).entrySet().iterator();
        while (((Iterator)localObject1).hasNext())
        {
          Object localObject2 = (Map.Entry)((Iterator)localObject1).next();
          SQLiteConnection.PreparedStatement localPreparedStatement = (SQLiteConnection.PreparedStatement)((Map.Entry)localObject2).getValue();
          if (mInCache)
          {
            localObject2 = (String)((Map.Entry)localObject2).getKey();
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("    ");
            localStringBuilder.append(i);
            localStringBuilder.append(": statementPtr=0x");
            localStringBuilder.append(Long.toHexString(mStatementPtr));
            localStringBuilder.append(", numParameters=");
            localStringBuilder.append(mNumParameters);
            localStringBuilder.append(", type=");
            localStringBuilder.append(mType);
            localStringBuilder.append(", readOnly=");
            localStringBuilder.append(mReadOnly);
            localStringBuilder.append(", sql=\"");
            localStringBuilder.append(SQLiteConnection.trimSqlForDisplay((String)localObject2));
            localStringBuilder.append("\"");
            paramPrinter.println(localStringBuilder.toString());
          }
          i++;
        }
      }
      else
      {
        paramPrinter.println("    <none>");
      }
    }
    
    protected void entryRemoved(boolean paramBoolean, String paramString, SQLiteConnection.PreparedStatement paramPreparedStatement1, SQLiteConnection.PreparedStatement paramPreparedStatement2)
    {
      mInCache = false;
      if (!mInUse) {
        SQLiteConnection.this.finalizePreparedStatement(paramPreparedStatement1);
      }
    }
  }
}
