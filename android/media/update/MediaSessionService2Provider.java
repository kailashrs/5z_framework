package android.media.update;

import android.app.Notification;
import android.content.Intent;
import android.media.MediaSession2;
import android.media.MediaSessionService2.MediaNotification;
import android.os.IBinder;

public abstract interface MediaSessionService2Provider
{
  public abstract MediaSession2 getSession_impl();
  
  public abstract IBinder onBind_impl(Intent paramIntent);
  
  public abstract void onCreate_impl();
  
  public abstract MediaSessionService2.MediaNotification onUpdateNotification_impl();
  
  public static abstract interface MediaNotificationProvider
  {
    public abstract int getNotificationId_impl();
    
    public abstract Notification getNotification_impl();
  }
}
