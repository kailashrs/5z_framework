package android.database;

public class StaleDataException
  extends RuntimeException
{
  public StaleDataException() {}
  
  public StaleDataException(String paramString)
  {
    super(paramString);
  }
}
