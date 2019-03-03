package android.database.sqlite;

public class SQLiteBlobTooBigException
  extends SQLiteException
{
  public SQLiteBlobTooBigException() {}
  
  public SQLiteBlobTooBigException(String paramString)
  {
    super(paramString);
  }
}
