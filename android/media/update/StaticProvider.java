package android.media.update;

import android.app.Notification;
import android.content.Context;
import android.media.MediaBrowser2;
import android.media.MediaBrowser2.BrowserCallback;
import android.media.MediaController2;
import android.media.MediaController2.ControllerCallback;
import android.media.MediaItem2;
import android.media.MediaItem2.Builder;
import android.media.MediaLibraryService2;
import android.media.MediaLibraryService2.LibraryRoot;
import android.media.MediaLibraryService2.MediaLibrarySession;
import android.media.MediaLibraryService2.MediaLibrarySession.Builder;
import android.media.MediaLibraryService2.MediaLibrarySession.MediaLibrarySessionCallback;
import android.media.MediaMetadata2;
import android.media.MediaMetadata2.Builder;
import android.media.MediaPlaylistAgent;
import android.media.MediaSession2;
import android.media.MediaSession2.Builder;
import android.media.MediaSession2.CommandButton.Builder;
import android.media.MediaSession2.ControllerInfo;
import android.media.MediaSession2.SessionCallback;
import android.media.MediaSessionService2;
import android.media.MediaSessionService2.MediaNotification;
import android.media.Rating2;
import android.media.SessionCommand2;
import android.media.SessionCommandGroup2;
import android.media.SessionToken2;
import android.media.VolumeProvider2;
import android.os.Bundle;
import android.os.IInterface;
import android.util.AttributeSet;
import android.widget.MediaControlView2;
import android.widget.VideoView2;
import java.util.concurrent.Executor;

public abstract interface StaticProvider
{
  public abstract MediaBrowser2Provider createMediaBrowser2(Context paramContext, MediaBrowser2 paramMediaBrowser2, SessionToken2 paramSessionToken2, Executor paramExecutor, MediaBrowser2.BrowserCallback paramBrowserCallback);
  
  public abstract MediaControlView2Provider createMediaControlView2(MediaControlView2 paramMediaControlView2, ViewGroupProvider paramViewGroupProvider1, ViewGroupProvider paramViewGroupProvider2, AttributeSet paramAttributeSet, int paramInt1, int paramInt2);
  
  public abstract MediaController2Provider createMediaController2(Context paramContext, MediaController2 paramMediaController2, SessionToken2 paramSessionToken2, Executor paramExecutor, MediaController2.ControllerCallback paramControllerCallback);
  
  public abstract MediaItem2Provider.BuilderProvider createMediaItem2Builder(MediaItem2.Builder paramBuilder, int paramInt);
  
  public abstract MediaSessionService2Provider createMediaLibraryService2(MediaLibraryService2 paramMediaLibraryService2);
  
  public abstract MediaSession2Provider.BuilderBaseProvider<MediaLibraryService2.MediaLibrarySession, MediaLibraryService2.MediaLibrarySession.MediaLibrarySessionCallback> createMediaLibraryService2Builder(MediaLibraryService2 paramMediaLibraryService2, MediaLibraryService2.MediaLibrarySession.Builder paramBuilder, Executor paramExecutor, MediaLibraryService2.MediaLibrarySession.MediaLibrarySessionCallback paramMediaLibrarySessionCallback);
  
  public abstract MediaLibraryService2Provider.LibraryRootProvider createMediaLibraryService2LibraryRoot(MediaLibraryService2.LibraryRoot paramLibraryRoot, String paramString, Bundle paramBundle);
  
  public abstract MediaMetadata2Provider.BuilderProvider createMediaMetadata2Builder(MediaMetadata2.Builder paramBuilder);
  
  public abstract MediaMetadata2Provider.BuilderProvider createMediaMetadata2Builder(MediaMetadata2.Builder paramBuilder, MediaMetadata2 paramMediaMetadata2);
  
  public abstract MediaPlaylistAgentProvider createMediaPlaylistAgent(MediaPlaylistAgent paramMediaPlaylistAgent);
  
  public abstract MediaSession2Provider.BuilderBaseProvider<MediaSession2, MediaSession2.SessionCallback> createMediaSession2Builder(Context paramContext, MediaSession2.Builder paramBuilder);
  
  public abstract MediaSession2Provider.CommandProvider createMediaSession2Command(SessionCommand2 paramSessionCommand2, int paramInt, String paramString, Bundle paramBundle);
  
  public abstract MediaSession2Provider.CommandButtonProvider.BuilderProvider createMediaSession2CommandButtonBuilder(MediaSession2.CommandButton.Builder paramBuilder);
  
  public abstract MediaSession2Provider.CommandGroupProvider createMediaSession2CommandGroup(SessionCommandGroup2 paramSessionCommandGroup21, SessionCommandGroup2 paramSessionCommandGroup22);
  
  public abstract MediaSession2Provider.ControllerInfoProvider createMediaSession2ControllerInfo(Context paramContext, MediaSession2.ControllerInfo paramControllerInfo, int paramInt1, int paramInt2, String paramString, IInterface paramIInterface);
  
  public abstract MediaSessionService2Provider createMediaSessionService2(MediaSessionService2 paramMediaSessionService2);
  
  public abstract MediaSessionService2Provider.MediaNotificationProvider createMediaSessionService2MediaNotification(MediaSessionService2.MediaNotification paramMediaNotification, int paramInt, Notification paramNotification);
  
  public abstract SessionToken2Provider createSessionToken2(Context paramContext, SessionToken2 paramSessionToken2, String paramString1, String paramString2, int paramInt);
  
  public abstract VideoView2Provider createVideoView2(VideoView2 paramVideoView2, ViewGroupProvider paramViewGroupProvider1, ViewGroupProvider paramViewGroupProvider2, AttributeSet paramAttributeSet, int paramInt1, int paramInt2);
  
  public abstract VolumeProvider2Provider createVolumeProvider2(VolumeProvider2 paramVolumeProvider2, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract MediaItem2 fromBundle_MediaItem2(Bundle paramBundle);
  
  public abstract MediaMetadata2 fromBundle_MediaMetadata2(Bundle paramBundle);
  
  public abstract SessionCommand2 fromBundle_MediaSession2Command(Bundle paramBundle);
  
  public abstract SessionCommandGroup2 fromBundle_MediaSession2CommandGroup(Bundle paramBundle);
  
  public abstract Rating2 fromBundle_Rating2(Bundle paramBundle);
  
  public abstract SessionToken2 fromBundle_SessionToken2(Bundle paramBundle);
  
  public abstract Rating2 newHeartRating_Rating2(boolean paramBoolean);
  
  public abstract Rating2 newPercentageRating_Rating2(float paramFloat);
  
  public abstract Rating2 newStarRating_Rating2(int paramInt, float paramFloat);
  
  public abstract Rating2 newThumbRating_Rating2(boolean paramBoolean);
  
  public abstract Rating2 newUnratedRating_Rating2(int paramInt);
}
