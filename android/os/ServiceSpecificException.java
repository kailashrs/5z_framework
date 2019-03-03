package android.os;

public class ServiceSpecificException
  extends RuntimeException
{
  public final int errorCode;
  
  public ServiceSpecificException(int paramInt)
  {
    errorCode = paramInt;
  }
  
  public ServiceSpecificException(int paramInt, String paramString)
  {
    super(paramString);
    errorCode = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.toString());
    localStringBuilder.append(" (code ");
    localStringBuilder.append(errorCode);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
}
