package com.android.internal.telephony.uicc.euicc.async;

import android.telephony.Rlog;

public abstract class AsyncResultCallback<Result>
{
  private static final String LOG_TAG = "AsyncResultCallback";
  
  public AsyncResultCallback() {}
  
  public void onException(Throwable paramThrowable)
  {
    Rlog.e("AsyncResultCallback", "Error in onException", paramThrowable);
  }
  
  public abstract void onResult(Result paramResult);
}
