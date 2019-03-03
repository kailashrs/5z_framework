package com.android.internal.telephony.uicc.euicc.async;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public abstract class AsyncMessageInvocation<Request, Response>
  implements Handler.Callback
{
  public AsyncMessageInvocation() {}
  
  public boolean handleMessage(Message paramMessage)
  {
    AsyncResult localAsyncResult = (AsyncResult)obj;
    paramMessage = (AsyncResultCallback)userObj;
    try
    {
      paramMessage.onResult(parseResult(localAsyncResult));
    }
    catch (Throwable localThrowable)
    {
      paramMessage.onException(localThrowable);
    }
    return true;
  }
  
  public final void invoke(Request paramRequest, AsyncResultCallback<Response> paramAsyncResultCallback, Handler paramHandler)
  {
    sendRequestMessage(paramRequest, new Handler(paramHandler.getLooper(), this).obtainMessage(0, paramAsyncResultCallback));
  }
  
  protected abstract Response parseResult(AsyncResult paramAsyncResult)
    throws Throwable;
  
  protected abstract void sendRequestMessage(Request paramRequest, Message paramMessage);
}
