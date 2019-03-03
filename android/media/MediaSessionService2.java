package android.media;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.update.ApiLoader;
import android.media.update.MediaSessionService2Provider;
import android.media.update.MediaSessionService2Provider.MediaNotificationProvider;
import android.media.update.StaticProvider;
import android.os.IBinder;

public abstract class MediaSessionService2
  extends Service
{
  public static final String SERVICE_INTERFACE = "android.media.MediaSessionService2";
  public static final String SERVICE_META_DATA = "android.media.session";
  private final MediaSessionService2Provider mProvider = createProvider();
  
  public MediaSessionService2() {}
  
  MediaSessionService2Provider createProvider()
  {
    return ApiLoader.getProvider().createMediaSessionService2(this);
  }
  
  public final MediaSession2 getSession()
  {
    return mProvider.getSession_impl();
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return mProvider.onBind_impl(paramIntent);
  }
  
  public void onCreate()
  {
    super.onCreate();
    mProvider.onCreate_impl();
  }
  
  public abstract MediaSession2 onCreateSession(String paramString);
  
  public MediaNotification onUpdateNotification()
  {
    return mProvider.onUpdateNotification_impl();
  }
  
  public static class MediaNotification
  {
    private final MediaSessionService2Provider.MediaNotificationProvider mProvider;
    
    public MediaNotification(int paramInt, Notification paramNotification)
    {
      mProvider = ApiLoader.getProvider().createMediaSessionService2MediaNotification(this, paramInt, paramNotification);
    }
    
    public Notification getNotification()
    {
      return mProvider.getNotification_impl();
    }
    
    public int getNotificationId()
    {
      return mProvider.getNotificationId_impl();
    }
  }
}
