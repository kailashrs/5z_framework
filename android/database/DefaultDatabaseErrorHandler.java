package android.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.File;

public final class DefaultDatabaseErrorHandler
  implements DatabaseErrorHandler
{
  private static final String TAG = "DefaultDatabaseErrorHandler";
  
  public DefaultDatabaseErrorHandler() {}
  
  private void deleteDatabaseFile(String paramString)
  {
    if ((!paramString.equalsIgnoreCase(":memory:")) && (paramString.trim().length() != 0))
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("deleting the database file: ");
      ((StringBuilder)localObject).append(paramString);
      Log.e("DefaultDatabaseErrorHandler", ((StringBuilder)localObject).toString());
      try
      {
        localObject = new java/io/File;
        ((File)localObject).<init>(paramString);
        SQLiteDatabase.deleteDatabase((File)localObject);
      }
      catch (Exception localException)
      {
        paramString = new StringBuilder();
        paramString.append("delete failed: ");
        paramString.append(localException.getMessage());
        Log.w("DefaultDatabaseErrorHandler", paramString.toString());
      }
      return;
    }
  }
  
  /* Error */
  public void onCorruption(SQLiteDatabase paramSQLiteDatabase)
  {
    // Byte code:
    //   0: new 37	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 38	java/lang/StringBuilder:<init>	()V
    //   7: astore_2
    //   8: aload_2
    //   9: ldc 77
    //   11: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   14: pop
    //   15: aload_2
    //   16: aload_1
    //   17: invokevirtual 80	android/database/sqlite/SQLiteDatabase:getPath	()Ljava/lang/String;
    //   20: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   23: pop
    //   24: ldc 10
    //   26: aload_2
    //   27: invokevirtual 47	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   30: invokestatic 53	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   33: pop
    //   34: aload_1
    //   35: invokevirtual 84	android/database/sqlite/SQLiteDatabase:isOpen	()Z
    //   38: ifne +12 -> 50
    //   41: aload_0
    //   42: aload_1
    //   43: invokevirtual 80	android/database/sqlite/SQLiteDatabase:getPath	()Ljava/lang/String;
    //   46: invokespecial 86	android/database/DefaultDatabaseErrorHandler:deleteDatabaseFile	(Ljava/lang/String;)V
    //   49: return
    //   50: aconst_null
    //   51: astore_2
    //   52: aconst_null
    //   53: astore_3
    //   54: aload_1
    //   55: invokevirtual 90	android/database/sqlite/SQLiteDatabase:getAttachedDbs	()Ljava/util/List;
    //   58: astore 4
    //   60: aload 4
    //   62: astore_2
    //   63: goto +8 -> 71
    //   66: astore_2
    //   67: goto +13 -> 80
    //   70: astore_3
    //   71: aload_2
    //   72: astore_3
    //   73: aload_1
    //   74: invokevirtual 93	android/database/sqlite/SQLiteDatabase:close	()V
    //   77: goto +56 -> 133
    //   80: aload_3
    //   81: ifnull +41 -> 122
    //   84: aload_3
    //   85: invokeinterface 99 1 0
    //   90: astore_1
    //   91: aload_1
    //   92: invokeinterface 104 1 0
    //   97: ifeq +33 -> 130
    //   100: aload_0
    //   101: aload_1
    //   102: invokeinterface 108 1 0
    //   107: checkcast 110	android/util/Pair
    //   110: getfield 114	android/util/Pair:second	Ljava/lang/Object;
    //   113: checkcast 23	java/lang/String
    //   116: invokespecial 86	android/database/DefaultDatabaseErrorHandler:deleteDatabaseFile	(Ljava/lang/String;)V
    //   119: goto -28 -> 91
    //   122: aload_0
    //   123: aload_1
    //   124: invokevirtual 80	android/database/sqlite/SQLiteDatabase:getPath	()Ljava/lang/String;
    //   127: invokespecial 86	android/database/DefaultDatabaseErrorHandler:deleteDatabaseFile	(Ljava/lang/String;)V
    //   130: aload_2
    //   131: athrow
    //   132: astore_3
    //   133: aload_2
    //   134: ifnull +41 -> 175
    //   137: aload_2
    //   138: invokeinterface 99 1 0
    //   143: astore_1
    //   144: aload_1
    //   145: invokeinterface 104 1 0
    //   150: ifeq +33 -> 183
    //   153: aload_0
    //   154: aload_1
    //   155: invokeinterface 108 1 0
    //   160: checkcast 110	android/util/Pair
    //   163: getfield 114	android/util/Pair:second	Ljava/lang/Object;
    //   166: checkcast 23	java/lang/String
    //   169: invokespecial 86	android/database/DefaultDatabaseErrorHandler:deleteDatabaseFile	(Ljava/lang/String;)V
    //   172: goto -28 -> 144
    //   175: aload_0
    //   176: aload_1
    //   177: invokevirtual 80	android/database/sqlite/SQLiteDatabase:getPath	()Ljava/lang/String;
    //   180: invokespecial 86	android/database/DefaultDatabaseErrorHandler:deleteDatabaseFile	(Ljava/lang/String;)V
    //   183: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	184	0	this	DefaultDatabaseErrorHandler
    //   0	184	1	paramSQLiteDatabase	SQLiteDatabase
    //   7	56	2	localObject1	Object
    //   66	72	2	localObject2	Object
    //   53	1	3	localObject3	Object
    //   70	1	3	localSQLiteException1	android.database.sqlite.SQLiteException
    //   72	13	3	localObject4	Object
    //   132	1	3	localSQLiteException2	android.database.sqlite.SQLiteException
    //   58	3	4	localList	java.util.List
    // Exception table:
    //   from	to	target	type
    //   54	60	66	finally
    //   73	77	66	finally
    //   54	60	70	android/database/sqlite/SQLiteException
    //   73	77	132	android/database/sqlite/SQLiteException
  }
}
