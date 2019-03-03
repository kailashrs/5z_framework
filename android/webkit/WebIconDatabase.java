package android.webkit;

import android.annotation.SystemApi;
import android.content.ContentResolver;
import android.graphics.Bitmap;

@Deprecated
public abstract class WebIconDatabase
{
  public WebIconDatabase() {}
  
  public static WebIconDatabase getInstance()
  {
    return WebViewFactory.getProvider().getWebIconDatabase();
  }
  
  @SystemApi
  public abstract void bulkRequestIconForPageUrl(ContentResolver paramContentResolver, String paramString, IconListener paramIconListener);
  
  public abstract void close();
  
  public abstract void open(String paramString);
  
  public abstract void releaseIconForPageUrl(String paramString);
  
  public abstract void removeAllIcons();
  
  public abstract void requestIconForPageUrl(String paramString, IconListener paramIconListener);
  
  public abstract void retainIconForPageUrl(String paramString);
  
  @Deprecated
  public static abstract interface IconListener
  {
    public abstract void onReceivedIcon(String paramString, Bitmap paramBitmap);
  }
}
