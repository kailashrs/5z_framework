package android.database.sqlite;

public class SQLiteReadOnlyDatabaseException
  extends SQLiteException
{
  public SQLiteReadOnlyDatabaseException() {}
  
  public SQLiteReadOnlyDatabaseException(String paramString)
  {
    super(paramString);
  }
}
