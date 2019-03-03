package android.media;

import android.os.IBinder;
import android.util.Log;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.Iterator;
import java.util.List;

public class MediaHTTPService
  extends IMediaHTTPService.Stub
{
  private static final String TAG = "MediaHTTPService";
  private Boolean mCookieStoreInitialized = new Boolean(false);
  private List<HttpCookie> mCookies;
  
  public MediaHTTPService(List<HttpCookie> paramList)
  {
    mCookies = paramList;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("MediaHTTPService(");
    localStringBuilder.append(this);
    localStringBuilder.append("): Cookies: ");
    localStringBuilder.append(paramList);
    Log.v("MediaHTTPService", localStringBuilder.toString());
  }
  
  static IBinder createHttpServiceBinderIfNecessary(String paramString)
  {
    return createHttpServiceBinderIfNecessary(paramString, null);
  }
  
  static IBinder createHttpServiceBinderIfNecessary(String paramString, List<HttpCookie> paramList)
  {
    if ((!paramString.startsWith("http://")) && (!paramString.startsWith("https://")))
    {
      if (paramString.startsWith("widevine://")) {
        Log.d("MediaHTTPService", "Widevine classic is no longer supported");
      }
      return null;
    }
    return new MediaHTTPService(paramList).asBinder();
  }
  
  public IMediaHTTPConnection makeHTTPConnection()
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
          Log.v("MediaHTTPService", ((StringBuilder)localObject3).toString());
        }
        else
        {
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("makeHTTPConnection: CookieHandler (");
          ((StringBuilder)localObject3).append(localObject1);
          ((StringBuilder)localObject3).append(") exists.");
          Log.v("MediaHTTPService", ((StringBuilder)localObject3).toString());
        }
        if (mCookies != null) {
          if ((localObject1 instanceof CookieManager))
          {
            localObject3 = ((CookieManager)localObject1).getCookieStore();
            Iterator localIterator = mCookies.iterator();
            while (localIterator.hasNext())
            {
              Object localObject4 = (HttpCookie)localIterator.next();
              try
              {
                ((CookieStore)localObject3).add(null, (HttpCookie)localObject4);
              }
              catch (Exception localException)
              {
                localObject4 = new java/lang/StringBuilder;
                ((StringBuilder)localObject4).<init>();
                ((StringBuilder)localObject4).append("makeHTTPConnection: CookieStore.add");
                ((StringBuilder)localObject4).append(localException);
                Log.v("MediaHTTPService", ((StringBuilder)localObject4).toString());
              }
            }
          }
          else
          {
            Log.w("MediaHTTPService", "makeHTTPConnection: The installed CookieHandler is not a CookieManager. Can’t add the provided cookies to the cookie store.");
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
        Log.v("MediaHTTPService", ((StringBuilder)localObject3).toString());
      }
      return new MediaHTTPConnection();
    }
  }
}
