package android.media;

import android.app.PendingIntent;
import android.media.update.ApiLoader;
import android.media.update.MediaLibraryService2Provider.LibraryRootProvider;
import android.media.update.MediaLibraryService2Provider.MediaLibrarySessionProvider;
import android.media.update.MediaSessionService2Provider;
import android.media.update.StaticProvider;
import android.os.Bundle;
import java.util.List;
import java.util.concurrent.Executor;

public abstract class MediaLibraryService2
  extends MediaSessionService2
{
  public static final String SERVICE_INTERFACE = "android.media.MediaLibraryService2";
  
  public MediaLibraryService2() {}
  
  MediaSessionService2Provider createProvider()
  {
    return ApiLoader.getProvider().createMediaLibraryService2(this);
  }
  
  public abstract MediaLibrarySession onCreateSession(String paramString);
  
  public static final class LibraryRoot
  {
    public static final String EXTRA_OFFLINE = "android.media.extra.OFFLINE";
    public static final String EXTRA_RECENT = "android.media.extra.RECENT";
    public static final String EXTRA_SUGGESTED = "android.media.extra.SUGGESTED";
    private final MediaLibraryService2Provider.LibraryRootProvider mProvider;
    
    public LibraryRoot(String paramString, Bundle paramBundle)
    {
      mProvider = ApiLoader.getProvider().createMediaLibraryService2LibraryRoot(this, paramString, paramBundle);
    }
    
    public Bundle getExtras()
    {
      return mProvider.getExtras_impl();
    }
    
    public String getRootId()
    {
      return mProvider.getRootId_impl();
    }
  }
  
  public static final class MediaLibrarySession
    extends MediaSession2
  {
    private final MediaLibraryService2Provider.MediaLibrarySessionProvider mProvider;
    
    public MediaLibrarySession(MediaLibraryService2Provider.MediaLibrarySessionProvider paramMediaLibrarySessionProvider)
    {
      super();
      mProvider = paramMediaLibrarySessionProvider;
    }
    
    public void notifyChildrenChanged(MediaSession2.ControllerInfo paramControllerInfo, String paramString, int paramInt, Bundle paramBundle)
    {
      mProvider.notifyChildrenChanged_impl(paramControllerInfo, paramString, paramInt, paramBundle);
    }
    
    public void notifyChildrenChanged(String paramString, int paramInt, Bundle paramBundle)
    {
      mProvider.notifyChildrenChanged_impl(paramString, paramInt, paramBundle);
    }
    
    public void notifySearchResultChanged(MediaSession2.ControllerInfo paramControllerInfo, String paramString, int paramInt, Bundle paramBundle)
    {
      mProvider.notifySearchResultChanged_impl(paramControllerInfo, paramString, paramInt, paramBundle);
    }
    
    public static final class Builder
      extends MediaSession2.BuilderBase<MediaLibraryService2.MediaLibrarySession, Builder, MediaLibraryService2.MediaLibrarySession.MediaLibrarySessionCallback>
    {
      public Builder(MediaLibraryService2 paramMediaLibraryService2, Executor paramExecutor, MediaLibraryService2.MediaLibrarySession.MediaLibrarySessionCallback paramMediaLibrarySessionCallback)
      {
        super();
      }
      
      public MediaLibraryService2.MediaLibrarySession build()
      {
        return (MediaLibraryService2.MediaLibrarySession)super.build();
      }
      
      public Builder setId(String paramString)
      {
        return (Builder)super.setId(paramString);
      }
      
      public Builder setPlayer(MediaPlayerBase paramMediaPlayerBase)
      {
        return (Builder)super.setPlayer(paramMediaPlayerBase);
      }
      
      public Builder setPlaylistAgent(MediaPlaylistAgent paramMediaPlaylistAgent)
      {
        return (Builder)super.setPlaylistAgent(paramMediaPlaylistAgent);
      }
      
      public Builder setSessionActivity(PendingIntent paramPendingIntent)
      {
        return (Builder)super.setSessionActivity(paramPendingIntent);
      }
      
      public Builder setSessionCallback(Executor paramExecutor, MediaLibraryService2.MediaLibrarySession.MediaLibrarySessionCallback paramMediaLibrarySessionCallback)
      {
        return (Builder)super.setSessionCallback(paramExecutor, paramMediaLibrarySessionCallback);
      }
      
      public Builder setVolumeProvider(VolumeProvider2 paramVolumeProvider2)
      {
        return (Builder)super.setVolumeProvider(paramVolumeProvider2);
      }
    }
    
    public static class MediaLibrarySessionCallback
      extends MediaSession2.SessionCallback
    {
      public MediaLibrarySessionCallback() {}
      
      public List<MediaItem2> onGetChildren(MediaLibraryService2.MediaLibrarySession paramMediaLibrarySession, MediaSession2.ControllerInfo paramControllerInfo, String paramString, int paramInt1, int paramInt2, Bundle paramBundle)
      {
        return null;
      }
      
      public MediaItem2 onGetItem(MediaLibraryService2.MediaLibrarySession paramMediaLibrarySession, MediaSession2.ControllerInfo paramControllerInfo, String paramString)
      {
        return null;
      }
      
      public MediaLibraryService2.LibraryRoot onGetLibraryRoot(MediaLibraryService2.MediaLibrarySession paramMediaLibrarySession, MediaSession2.ControllerInfo paramControllerInfo, Bundle paramBundle)
      {
        return null;
      }
      
      public List<MediaItem2> onGetSearchResult(MediaLibraryService2.MediaLibrarySession paramMediaLibrarySession, MediaSession2.ControllerInfo paramControllerInfo, String paramString, int paramInt1, int paramInt2, Bundle paramBundle)
      {
        return null;
      }
      
      public void onSearch(MediaLibraryService2.MediaLibrarySession paramMediaLibrarySession, MediaSession2.ControllerInfo paramControllerInfo, String paramString, Bundle paramBundle) {}
      
      public void onSubscribe(MediaLibraryService2.MediaLibrarySession paramMediaLibrarySession, MediaSession2.ControllerInfo paramControllerInfo, String paramString, Bundle paramBundle) {}
      
      public void onUnsubscribe(MediaLibraryService2.MediaLibrarySession paramMediaLibrarySession, MediaSession2.ControllerInfo paramControllerInfo, String paramString) {}
    }
  }
}
