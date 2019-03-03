package android.webkit;

import android.annotation.SystemApi;

public class JsResult
{
  private final ResultReceiver mReceiver;
  private boolean mResult;
  
  @SystemApi
  public JsResult(ResultReceiver paramResultReceiver)
  {
    mReceiver = paramResultReceiver;
  }
  
  private final void wakeUp()
  {
    mReceiver.onJsResultComplete(this);
  }
  
  public final void cancel()
  {
    mResult = false;
    wakeUp();
  }
  
  public final void confirm()
  {
    mResult = true;
    wakeUp();
  }
  
  @SystemApi
  public final boolean getResult()
  {
    return mResult;
  }
  
  @SystemApi
  public static abstract interface ResultReceiver
  {
    public abstract void onJsResultComplete(JsResult paramJsResult);
  }
}
