package android.os;

import java.lang.ref.WeakReference;

public class Registrant
{
  WeakReference refH;
  Object userObj;
  int what;
  
  public Registrant(Handler paramHandler, int paramInt, Object paramObject)
  {
    refH = new WeakReference(paramHandler);
    what = paramInt;
    userObj = paramObject;
  }
  
  public void clear()
  {
    refH = null;
    userObj = null;
  }
  
  public Handler getHandler()
  {
    if (refH == null) {
      return null;
    }
    return (Handler)refH.get();
  }
  
  void internalNotifyRegistrant(Object paramObject, Throwable paramThrowable)
  {
    Handler localHandler = getHandler();
    if (localHandler == null)
    {
      clear();
    }
    else
    {
      Message localMessage = Message.obtain();
      what = what;
      obj = new AsyncResult(userObj, paramObject, paramThrowable);
      localHandler.sendMessage(localMessage);
    }
  }
  
  public Message messageForRegistrant()
  {
    Object localObject = getHandler();
    if (localObject == null)
    {
      clear();
      return null;
    }
    localObject = ((Handler)localObject).obtainMessage();
    what = what;
    obj = userObj;
    return localObject;
  }
  
  public void notifyException(Throwable paramThrowable)
  {
    internalNotifyRegistrant(null, paramThrowable);
  }
  
  public void notifyRegistrant()
  {
    internalNotifyRegistrant(null, null);
  }
  
  public void notifyRegistrant(AsyncResult paramAsyncResult)
  {
    internalNotifyRegistrant(result, exception);
  }
  
  public void notifyResult(Object paramObject)
  {
    internalNotifyRegistrant(paramObject, null);
  }
}
