package android.security;

public class KeyStoreException
  extends Exception
{
  private final int mErrorCode;
  
  public KeyStoreException(int paramInt, String paramString)
  {
    super(paramString);
    mErrorCode = paramInt;
  }
  
  public int getErrorCode()
  {
    return mErrorCode;
  }
}
