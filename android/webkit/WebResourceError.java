package android.webkit;

import android.annotation.SystemApi;

public abstract class WebResourceError
{
  @SystemApi
  public WebResourceError() {}
  
  public abstract CharSequence getDescription();
  
  public abstract int getErrorCode();
}
