package android.database.sqlite;

import android.database.Cursor;
import android.os.CancellationSignal;

public final class SQLiteDirectCursorDriver
  implements SQLiteCursorDriver
{
  private final CancellationSignal mCancellationSignal;
  private final SQLiteDatabase mDatabase;
  private final String mEditTable;
  private SQLiteQuery mQuery;
  private final String mSql;
  
  public SQLiteDirectCursorDriver(SQLiteDatabase paramSQLiteDatabase, String paramString1, String paramString2, CancellationSignal paramCancellationSignal)
  {
    mDatabase = paramSQLiteDatabase;
    mEditTable = paramString2;
    mSql = paramString1;
    mCancellationSignal = paramCancellationSignal;
  }
  
  public void cursorClosed() {}
  
  public void cursorDeactivated() {}
  
  public void cursorRequeried(Cursor paramCursor) {}
  
  public Cursor query(SQLiteDatabase.CursorFactory paramCursorFactory, String[] paramArrayOfString)
  {
    SQLiteQuery localSQLiteQuery = new SQLiteQuery(mDatabase, mSql, mCancellationSignal);
    try
    {
      localSQLiteQuery.bindAllArgsAsStrings(paramArrayOfString);
      if (paramCursorFactory == null) {
        paramCursorFactory = new SQLiteCursor(this, mEditTable, localSQLiteQuery);
      } else {
        paramCursorFactory = paramCursorFactory.newCursor(mDatabase, this, mEditTable, localSQLiteQuery);
      }
      mQuery = localSQLiteQuery;
      return paramCursorFactory;
    }
    catch (RuntimeException paramCursorFactory)
    {
      localSQLiteQuery.close();
      throw paramCursorFactory;
    }
  }
  
  public void setBindArguments(String[] paramArrayOfString)
  {
    mQuery.bindAllArgsAsStrings(paramArrayOfString);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SQLiteDirectCursorDriver: ");
    localStringBuilder.append(mSql);
    return localStringBuilder.toString();
  }
}
