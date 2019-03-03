package android.database.sqlite;

public final class SQLiteStatement
  extends SQLiteProgram
{
  SQLiteStatement(SQLiteDatabase paramSQLiteDatabase, String paramString, Object[] paramArrayOfObject)
  {
    super(paramSQLiteDatabase, paramString, paramArrayOfObject, null);
  }
  
  /* Error */
  public void execute()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 17	android/database/sqlite/SQLiteStatement:acquireReference	()V
    //   4: aload_0
    //   5: invokevirtual 21	android/database/sqlite/SQLiteStatement:getSession	()Landroid/database/sqlite/SQLiteSession;
    //   8: aload_0
    //   9: invokevirtual 25	android/database/sqlite/SQLiteStatement:getSql	()Ljava/lang/String;
    //   12: aload_0
    //   13: invokevirtual 29	android/database/sqlite/SQLiteStatement:getBindArgs	()[Ljava/lang/Object;
    //   16: aload_0
    //   17: invokevirtual 33	android/database/sqlite/SQLiteStatement:getConnectionFlags	()I
    //   20: aconst_null
    //   21: invokevirtual 38	android/database/sqlite/SQLiteSession:execute	(Ljava/lang/String;[Ljava/lang/Object;ILandroid/os/CancellationSignal;)V
    //   24: aload_0
    //   25: invokevirtual 41	android/database/sqlite/SQLiteStatement:releaseReference	()V
    //   28: return
    //   29: astore_1
    //   30: goto +10 -> 40
    //   33: astore_1
    //   34: aload_0
    //   35: invokevirtual 44	android/database/sqlite/SQLiteStatement:onCorruption	()V
    //   38: aload_1
    //   39: athrow
    //   40: aload_0
    //   41: invokevirtual 41	android/database/sqlite/SQLiteStatement:releaseReference	()V
    //   44: aload_1
    //   45: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	46	0	this	SQLiteStatement
    //   29	1	1	localObject	Object
    //   33	12	1	localSQLiteDatabaseCorruptException	SQLiteDatabaseCorruptException
    // Exception table:
    //   from	to	target	type
    //   4	24	29	finally
    //   34	40	29	finally
    //   4	24	33	android/database/sqlite/SQLiteDatabaseCorruptException
  }
  
  /* Error */
  public long executeInsert()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 17	android/database/sqlite/SQLiteStatement:acquireReference	()V
    //   4: aload_0
    //   5: invokevirtual 21	android/database/sqlite/SQLiteStatement:getSession	()Landroid/database/sqlite/SQLiteSession;
    //   8: aload_0
    //   9: invokevirtual 25	android/database/sqlite/SQLiteStatement:getSql	()Ljava/lang/String;
    //   12: aload_0
    //   13: invokevirtual 29	android/database/sqlite/SQLiteStatement:getBindArgs	()[Ljava/lang/Object;
    //   16: aload_0
    //   17: invokevirtual 33	android/database/sqlite/SQLiteStatement:getConnectionFlags	()I
    //   20: aconst_null
    //   21: invokevirtual 50	android/database/sqlite/SQLiteSession:executeForLastInsertedRowId	(Ljava/lang/String;[Ljava/lang/Object;ILandroid/os/CancellationSignal;)J
    //   24: lstore_1
    //   25: aload_0
    //   26: invokevirtual 41	android/database/sqlite/SQLiteStatement:releaseReference	()V
    //   29: lload_1
    //   30: lreturn
    //   31: astore_3
    //   32: goto +10 -> 42
    //   35: astore_3
    //   36: aload_0
    //   37: invokevirtual 44	android/database/sqlite/SQLiteStatement:onCorruption	()V
    //   40: aload_3
    //   41: athrow
    //   42: aload_0
    //   43: invokevirtual 41	android/database/sqlite/SQLiteStatement:releaseReference	()V
    //   46: aload_3
    //   47: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	48	0	this	SQLiteStatement
    //   24	6	1	l	long
    //   31	1	3	localObject	Object
    //   35	12	3	localSQLiteDatabaseCorruptException	SQLiteDatabaseCorruptException
    // Exception table:
    //   from	to	target	type
    //   4	25	31	finally
    //   36	42	31	finally
    //   4	25	35	android/database/sqlite/SQLiteDatabaseCorruptException
  }
  
  /* Error */
  public int executeUpdateDelete()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 17	android/database/sqlite/SQLiteStatement:acquireReference	()V
    //   4: aload_0
    //   5: invokevirtual 21	android/database/sqlite/SQLiteStatement:getSession	()Landroid/database/sqlite/SQLiteSession;
    //   8: aload_0
    //   9: invokevirtual 25	android/database/sqlite/SQLiteStatement:getSql	()Ljava/lang/String;
    //   12: aload_0
    //   13: invokevirtual 29	android/database/sqlite/SQLiteStatement:getBindArgs	()[Ljava/lang/Object;
    //   16: aload_0
    //   17: invokevirtual 33	android/database/sqlite/SQLiteStatement:getConnectionFlags	()I
    //   20: aconst_null
    //   21: invokevirtual 55	android/database/sqlite/SQLiteSession:executeForChangedRowCount	(Ljava/lang/String;[Ljava/lang/Object;ILandroid/os/CancellationSignal;)I
    //   24: istore_1
    //   25: aload_0
    //   26: invokevirtual 41	android/database/sqlite/SQLiteStatement:releaseReference	()V
    //   29: iload_1
    //   30: ireturn
    //   31: astore_2
    //   32: goto +10 -> 42
    //   35: astore_2
    //   36: aload_0
    //   37: invokevirtual 44	android/database/sqlite/SQLiteStatement:onCorruption	()V
    //   40: aload_2
    //   41: athrow
    //   42: aload_0
    //   43: invokevirtual 41	android/database/sqlite/SQLiteStatement:releaseReference	()V
    //   46: aload_2
    //   47: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	48	0	this	SQLiteStatement
    //   24	6	1	i	int
    //   31	1	2	localObject	Object
    //   35	12	2	localSQLiteDatabaseCorruptException	SQLiteDatabaseCorruptException
    // Exception table:
    //   from	to	target	type
    //   4	25	31	finally
    //   36	42	31	finally
    //   4	25	35	android/database/sqlite/SQLiteDatabaseCorruptException
  }
  
  /* Error */
  public android.os.ParcelFileDescriptor simpleQueryForBlobFileDescriptor()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 17	android/database/sqlite/SQLiteStatement:acquireReference	()V
    //   4: aload_0
    //   5: invokevirtual 21	android/database/sqlite/SQLiteStatement:getSession	()Landroid/database/sqlite/SQLiteSession;
    //   8: aload_0
    //   9: invokevirtual 25	android/database/sqlite/SQLiteStatement:getSql	()Ljava/lang/String;
    //   12: aload_0
    //   13: invokevirtual 29	android/database/sqlite/SQLiteStatement:getBindArgs	()[Ljava/lang/Object;
    //   16: aload_0
    //   17: invokevirtual 33	android/database/sqlite/SQLiteStatement:getConnectionFlags	()I
    //   20: aconst_null
    //   21: invokevirtual 61	android/database/sqlite/SQLiteSession:executeForBlobFileDescriptor	(Ljava/lang/String;[Ljava/lang/Object;ILandroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;
    //   24: astore_1
    //   25: aload_0
    //   26: invokevirtual 41	android/database/sqlite/SQLiteStatement:releaseReference	()V
    //   29: aload_1
    //   30: areturn
    //   31: astore_1
    //   32: goto +10 -> 42
    //   35: astore_1
    //   36: aload_0
    //   37: invokevirtual 44	android/database/sqlite/SQLiteStatement:onCorruption	()V
    //   40: aload_1
    //   41: athrow
    //   42: aload_0
    //   43: invokevirtual 41	android/database/sqlite/SQLiteStatement:releaseReference	()V
    //   46: aload_1
    //   47: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	48	0	this	SQLiteStatement
    //   24	6	1	localParcelFileDescriptor	android.os.ParcelFileDescriptor
    //   31	1	1	localObject	Object
    //   35	12	1	localSQLiteDatabaseCorruptException	SQLiteDatabaseCorruptException
    // Exception table:
    //   from	to	target	type
    //   4	25	31	finally
    //   36	42	31	finally
    //   4	25	35	android/database/sqlite/SQLiteDatabaseCorruptException
  }
  
  /* Error */
  public long simpleQueryForLong()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 17	android/database/sqlite/SQLiteStatement:acquireReference	()V
    //   4: aload_0
    //   5: invokevirtual 21	android/database/sqlite/SQLiteStatement:getSession	()Landroid/database/sqlite/SQLiteSession;
    //   8: aload_0
    //   9: invokevirtual 25	android/database/sqlite/SQLiteStatement:getSql	()Ljava/lang/String;
    //   12: aload_0
    //   13: invokevirtual 29	android/database/sqlite/SQLiteStatement:getBindArgs	()[Ljava/lang/Object;
    //   16: aload_0
    //   17: invokevirtual 33	android/database/sqlite/SQLiteStatement:getConnectionFlags	()I
    //   20: aconst_null
    //   21: invokevirtual 65	android/database/sqlite/SQLiteSession:executeForLong	(Ljava/lang/String;[Ljava/lang/Object;ILandroid/os/CancellationSignal;)J
    //   24: lstore_1
    //   25: aload_0
    //   26: invokevirtual 41	android/database/sqlite/SQLiteStatement:releaseReference	()V
    //   29: lload_1
    //   30: lreturn
    //   31: astore_3
    //   32: goto +10 -> 42
    //   35: astore_3
    //   36: aload_0
    //   37: invokevirtual 44	android/database/sqlite/SQLiteStatement:onCorruption	()V
    //   40: aload_3
    //   41: athrow
    //   42: aload_0
    //   43: invokevirtual 41	android/database/sqlite/SQLiteStatement:releaseReference	()V
    //   46: aload_3
    //   47: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	48	0	this	SQLiteStatement
    //   24	6	1	l	long
    //   31	1	3	localObject	Object
    //   35	12	3	localSQLiteDatabaseCorruptException	SQLiteDatabaseCorruptException
    // Exception table:
    //   from	to	target	type
    //   4	25	31	finally
    //   36	42	31	finally
    //   4	25	35	android/database/sqlite/SQLiteDatabaseCorruptException
  }
  
  /* Error */
  public String simpleQueryForString()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 17	android/database/sqlite/SQLiteStatement:acquireReference	()V
    //   4: aload_0
    //   5: invokevirtual 21	android/database/sqlite/SQLiteStatement:getSession	()Landroid/database/sqlite/SQLiteSession;
    //   8: aload_0
    //   9: invokevirtual 25	android/database/sqlite/SQLiteStatement:getSql	()Ljava/lang/String;
    //   12: aload_0
    //   13: invokevirtual 29	android/database/sqlite/SQLiteStatement:getBindArgs	()[Ljava/lang/Object;
    //   16: aload_0
    //   17: invokevirtual 33	android/database/sqlite/SQLiteStatement:getConnectionFlags	()I
    //   20: aconst_null
    //   21: invokevirtual 70	android/database/sqlite/SQLiteSession:executeForString	(Ljava/lang/String;[Ljava/lang/Object;ILandroid/os/CancellationSignal;)Ljava/lang/String;
    //   24: astore_1
    //   25: aload_0
    //   26: invokevirtual 41	android/database/sqlite/SQLiteStatement:releaseReference	()V
    //   29: aload_1
    //   30: areturn
    //   31: astore_1
    //   32: goto +10 -> 42
    //   35: astore_1
    //   36: aload_0
    //   37: invokevirtual 44	android/database/sqlite/SQLiteStatement:onCorruption	()V
    //   40: aload_1
    //   41: athrow
    //   42: aload_0
    //   43: invokevirtual 41	android/database/sqlite/SQLiteStatement:releaseReference	()V
    //   46: aload_1
    //   47: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	48	0	this	SQLiteStatement
    //   24	6	1	str	String
    //   31	1	1	localObject	Object
    //   35	12	1	localSQLiteDatabaseCorruptException	SQLiteDatabaseCorruptException
    // Exception table:
    //   from	to	target	type
    //   4	25	31	finally
    //   36	42	31	finally
    //   4	25	35	android/database/sqlite/SQLiteDatabaseCorruptException
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SQLiteProgram: ");
    localStringBuilder.append(getSql());
    return localStringBuilder.toString();
  }
}
