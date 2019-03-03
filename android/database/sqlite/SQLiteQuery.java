package android.database.sqlite;

import android.os.CancellationSignal;

public final class SQLiteQuery
  extends SQLiteProgram
{
  private static final String TAG = "SQLiteQuery";
  private final CancellationSignal mCancellationSignal;
  
  SQLiteQuery(SQLiteDatabase paramSQLiteDatabase, String paramString, CancellationSignal paramCancellationSignal)
  {
    super(paramSQLiteDatabase, paramString, null, paramCancellationSignal);
    mCancellationSignal = paramCancellationSignal;
  }
  
  /* Error */
  int fillWindow(android.database.CursorWindow paramCursorWindow, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 28	android/database/sqlite/SQLiteQuery:acquireReference	()V
    //   4: aload_1
    //   5: invokevirtual 31	android/database/CursorWindow:acquireReference	()V
    //   8: aload_0
    //   9: invokevirtual 35	android/database/sqlite/SQLiteQuery:getSession	()Landroid/database/sqlite/SQLiteSession;
    //   12: aload_0
    //   13: invokevirtual 39	android/database/sqlite/SQLiteQuery:getSql	()Ljava/lang/String;
    //   16: aload_0
    //   17: invokevirtual 43	android/database/sqlite/SQLiteQuery:getBindArgs	()[Ljava/lang/Object;
    //   20: aload_1
    //   21: iload_2
    //   22: iload_3
    //   23: iload 4
    //   25: aload_0
    //   26: invokevirtual 47	android/database/sqlite/SQLiteQuery:getConnectionFlags	()I
    //   29: aload_0
    //   30: getfield 17	android/database/sqlite/SQLiteQuery:mCancellationSignal	Landroid/os/CancellationSignal;
    //   33: invokevirtual 53	android/database/sqlite/SQLiteSession:executeForCursorWindow	(Ljava/lang/String;[Ljava/lang/Object;Landroid/database/CursorWindow;IIZILandroid/os/CancellationSignal;)I
    //   36: istore_2
    //   37: aload_1
    //   38: invokevirtual 56	android/database/CursorWindow:releaseReference	()V
    //   41: aload_0
    //   42: invokevirtual 57	android/database/sqlite/SQLiteQuery:releaseReference	()V
    //   45: iload_2
    //   46: ireturn
    //   47: astore 5
    //   49: goto +75 -> 124
    //   52: astore 6
    //   54: new 59	java/lang/StringBuilder
    //   57: astore 5
    //   59: aload 5
    //   61: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   64: aload 5
    //   66: ldc 63
    //   68: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: pop
    //   72: aload 5
    //   74: aload 6
    //   76: invokevirtual 70	android/database/sqlite/SQLiteException:getMessage	()Ljava/lang/String;
    //   79: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   82: pop
    //   83: aload 5
    //   85: ldc 72
    //   87: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: pop
    //   91: aload 5
    //   93: aload_0
    //   94: invokevirtual 39	android/database/sqlite/SQLiteQuery:getSql	()Ljava/lang/String;
    //   97: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: ldc 8
    //   103: aload 5
    //   105: invokevirtual 75	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   108: invokestatic 81	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   111: pop
    //   112: aload 6
    //   114: athrow
    //   115: astore 5
    //   117: aload_0
    //   118: invokevirtual 84	android/database/sqlite/SQLiteQuery:onCorruption	()V
    //   121: aload 5
    //   123: athrow
    //   124: aload_1
    //   125: invokevirtual 56	android/database/CursorWindow:releaseReference	()V
    //   128: aload 5
    //   130: athrow
    //   131: astore_1
    //   132: aload_0
    //   133: invokevirtual 57	android/database/sqlite/SQLiteQuery:releaseReference	()V
    //   136: aload_1
    //   137: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	138	0	this	SQLiteQuery
    //   0	138	1	paramCursorWindow	android.database.CursorWindow
    //   0	138	2	paramInt1	int
    //   0	138	3	paramInt2	int
    //   0	138	4	paramBoolean	boolean
    //   47	1	5	localObject	Object
    //   57	47	5	localStringBuilder	StringBuilder
    //   115	14	5	localSQLiteDatabaseCorruptException	SQLiteDatabaseCorruptException
    //   52	61	6	localSQLiteException	SQLiteException
    // Exception table:
    //   from	to	target	type
    //   8	37	47	finally
    //   54	115	47	finally
    //   117	124	47	finally
    //   8	37	52	android/database/sqlite/SQLiteException
    //   8	37	115	android/database/sqlite/SQLiteDatabaseCorruptException
    //   4	8	131	finally
    //   37	41	131	finally
    //   124	131	131	finally
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SQLiteQuery: ");
    localStringBuilder.append(getSql());
    return localStringBuilder.toString();
  }
}
