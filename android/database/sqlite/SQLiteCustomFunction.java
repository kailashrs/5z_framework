package android.database.sqlite;

public final class SQLiteCustomFunction
{
  public final SQLiteDatabase.CustomFunction callback;
  public final String name;
  public final int numArgs;
  
  public SQLiteCustomFunction(String paramString, int paramInt, SQLiteDatabase.CustomFunction paramCustomFunction)
  {
    if (paramString != null)
    {
      name = paramString;
      numArgs = paramInt;
      callback = paramCustomFunction;
      return;
    }
    throw new IllegalArgumentException("name must not be null.");
  }
  
  private void dispatchCallback(String[] paramArrayOfString)
  {
    callback.callback(paramArrayOfString);
  }
}
