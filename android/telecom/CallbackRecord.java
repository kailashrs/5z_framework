package android.telecom;

import android.os.Handler;

class CallbackRecord<T>
{
  private final T mCallback;
  private final Handler mHandler;
  
  public CallbackRecord(T paramT, Handler paramHandler)
  {
    mCallback = paramT;
    mHandler = paramHandler;
  }
  
  public T getCallback()
  {
    return mCallback;
  }
  
  public Handler getHandler()
  {
    return mHandler;
  }
}
