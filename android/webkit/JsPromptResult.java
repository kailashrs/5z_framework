package android.webkit;

import android.annotation.SystemApi;

public class JsPromptResult
  extends JsResult
{
  private String mStringResult;
  
  @SystemApi
  public JsPromptResult(JsResult.ResultReceiver paramResultReceiver)
  {
    super(paramResultReceiver);
  }
  
  public void confirm(String paramString)
  {
    mStringResult = paramString;
    confirm();
  }
  
  @SystemApi
  public String getStringResult()
  {
    return mStringResult;
  }
}
