package android.content;

public class OperationApplicationException
  extends Exception
{
  private final int mNumSuccessfulYieldPoints;
  
  public OperationApplicationException()
  {
    mNumSuccessfulYieldPoints = 0;
  }
  
  public OperationApplicationException(int paramInt)
  {
    mNumSuccessfulYieldPoints = paramInt;
  }
  
  public OperationApplicationException(String paramString)
  {
    super(paramString);
    mNumSuccessfulYieldPoints = 0;
  }
  
  public OperationApplicationException(String paramString, int paramInt)
  {
    super(paramString);
    mNumSuccessfulYieldPoints = paramInt;
  }
  
  public OperationApplicationException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
    mNumSuccessfulYieldPoints = 0;
  }
  
  public OperationApplicationException(Throwable paramThrowable)
  {
    super(paramThrowable);
    mNumSuccessfulYieldPoints = 0;
  }
  
  public int getNumSuccessfulYieldPoints()
  {
    return mNumSuccessfulYieldPoints;
  }
}
