package android.os;

public class AsyncResult
{
  public Throwable exception;
  public Object result;
  public Object userObj;
  
  public AsyncResult(Object paramObject1, Object paramObject2, Throwable paramThrowable)
  {
    userObj = paramObject1;
    result = paramObject2;
    exception = paramThrowable;
  }
  
  public static AsyncResult forMessage(Message paramMessage)
  {
    AsyncResult localAsyncResult = new AsyncResult(obj, null, null);
    obj = localAsyncResult;
    return localAsyncResult;
  }
  
  public static AsyncResult forMessage(Message paramMessage, Object paramObject, Throwable paramThrowable)
  {
    paramObject = new AsyncResult(obj, paramObject, paramThrowable);
    obj = paramObject;
    return paramObject;
  }
}
