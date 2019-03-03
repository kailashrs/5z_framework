package android.opengl;

public class GLException
  extends RuntimeException
{
  private final int mError;
  
  public GLException(int paramInt)
  {
    super(getErrorString(paramInt));
    mError = paramInt;
  }
  
  public GLException(int paramInt, String paramString)
  {
    super(paramString);
    mError = paramInt;
  }
  
  private static String getErrorString(int paramInt)
  {
    String str = GLU.gluErrorString(paramInt);
    Object localObject = str;
    if (str == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unknown error 0x");
      ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
      localObject = ((StringBuilder)localObject).toString();
    }
    return localObject;
  }
  
  int getError()
  {
    return mError;
  }
}
