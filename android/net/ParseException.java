package android.net;

public class ParseException
  extends RuntimeException
{
  public String response;
  
  ParseException(String paramString)
  {
    response = paramString;
  }
}
