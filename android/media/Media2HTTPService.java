package android.media;

import android.util.Log;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.Iterator;
import java.util.List;

public class Media2HTTPService
{
  private static final String TAG = "Media2HTTPService";
  private Boolean mCookieStoreInitialized = new Boolean(false);
  private List<HttpCookie> mCookies;
  
  public Media2HTTPService(List<HttpCookie> paramList)
  {
    mCookies = paramList;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Media2HTTPService(");
    localStringBuilder.append(this);
    localStringBuilder.append("): Cookies: ");
    localStringBuilder.append(paramList);
    Log.v("Media2HTTPService", localStringBuilder.toString());
  }
  
  static Media2HTTPService createHTTPService(String paramString)
  {
    return createHTTPService(paramString, null);
  }
  
  static Media2HTTPService createHTTPService(String paramString, List<HttpCookie> paramList)
  {
    if ((!paramString.startsWith("http://")) && (!paramString.startsWith("https://")))
    {
      if (paramString.startsWith("widevine://")) {
        Log.d("Media2HTTPService", "Widevine classic is no longer supported");
      }
      return null;
    }
    return new Media2HTTPService(paramList);
  }
  
  public Media2HTTPConnection makeHTTPConnection()
  {
    synchronized (mCookieStoreInitialized)
    {
      if (!mCookieStoreInitialized.booleanValue())
      {
        Object localObject1 = CookieHandler.getDefault();
        if (localObject1 == null)
        {
          localObject1 = new java/net/CookieManager;
          ((CookieManager)localObject1).<init>();
          CookieHandler.setDefault((CookieHandler)localObject1);
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("makeHTTPConnection: CookieManager created: ");
          ((StringBuilder)localObject3).append(localObject1);
          Log.v("Media2HTTPService", ((StringBuilder)localObject3).toString());
        }
        else
        {
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("makeHTTPConnection: CookieHandler (");
          ((StringBuilder)localObject3).append(localObject1);
          ((StringBuilder)localObject3).append(") exists.");
          Log.v("Media2HTTPService", ((StringBuilder)localObject3).toString());
        }
        if (mCookies != null) {
          if ((localObject1 instanceof CookieManager))
          {
            localObject3 = ((CookieManager)localObject1).getCookieStore();
            Iterator localIterator = mCookies.iterator();
            while (localIterator.hasNext())
            {
              HttpCookie localHttpCookie = (HttpCookie)localIterator.next();
              try
              {
                ((CookieStore)localObject3).add(null, localHttpCookie);
              }
              catch (Exception localException)
              {
                StringBuilder localStringBuilder = new java/lang/StringBuilder;
                localStringBuilder.<init>();
                localStringBuilder.append("makeHTTPConnection: CookieStore.add");
                localStringBuilder.append(localException);
                Log.v("Media2HTTPService", localStringBuilder.toString());
              }
            }
          }
          else
          {
            Log.w("Media2HTTPService", "makeHTTPConnection: The installed CookieHandler is not a CookieManager. Can’t add the provided cookies to the cookie store.");
          }
        }
        mCookieStoreInitialized = Boolean.valueOf(true);
        Object localObject3 = new java/lang/StringBuilder;
        ((StringBuilder)localObject3).<init>();
        ((StringBuilder)localObject3).append("makeHTTPConnection(");
        ((StringBuilder)localObject3).append(this);
        ((StringBuilder)localObject3).append("): cookieHandler: ");
        ((StringBuilder)localObject3).append(localObject1);
        ((StringBuilder)localObject3).append(" Cookies: ");
        ((StringBuilder)localObject3).append(mCookies);
        Log.v("Media2HTTPService", ((StringBuilder)localObject3).toString());
      }
      return new Media2HTTPConnection();
    }
  }
}
