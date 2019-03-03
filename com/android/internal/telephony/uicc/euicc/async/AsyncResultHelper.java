package com.android.internal.telephony.uicc.euicc.async;

import android.os.Handler;

public final class AsyncResultHelper
{
  private AsyncResultHelper() {}
  
  public static <T> void returnResult(final T paramT, AsyncResultCallback<T> paramAsyncResultCallback, Handler paramHandler)
  {
    if (paramHandler == null) {
      paramAsyncResultCallback.onResult(paramT);
    } else {
      paramHandler.post(new Runnable()
      {
        public void run()
        {
          onResult(paramT);
        }
      });
    }
  }
  
  public static void throwException(final Throwable paramThrowable, AsyncResultCallback<?> paramAsyncResultCallback, Handler paramHandler)
  {
    if (paramHandler == null) {
      paramAsyncResultCallback.onException(paramThrowable);
    } else {
      paramHandler.post(new Runnable()
      {
        public void run()
        {
          onException(paramThrowable);
        }
      });
    }
  }
}
