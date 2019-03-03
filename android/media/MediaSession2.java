package android.media;

import android.app.PendingIntent;
import android.content.Context;
import android.media.update.ApiLoader;
import android.media.update.MediaSession2Provider;
import android.media.update.MediaSession2Provider.BuilderBaseProvider;
import android.media.update.MediaSession2Provider.CommandButtonProvider;
import android.media.update.MediaSession2Provider.CommandButtonProvider.BuilderProvider;
import android.media.update.MediaSession2Provider.ControllerInfoProvider;
import android.media.update.ProviderCreator;
import android.media.update.StaticProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import android.os.ResultReceiver;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.Executor;

public class MediaSession2
  implements AutoCloseable
{
  public static final int ERROR_CODE_ACTION_ABORTED = 10;
  public static final int ERROR_CODE_APP_ERROR = 1;
  public static final int ERROR_CODE_AUTHENTICATION_EXPIRED = 3;
  public static final int ERROR_CODE_CONCURRENT_STREAM_LIMIT = 5;
  public static final int ERROR_CODE_CONTENT_ALREADY_PLAYING = 8;
  public static final int ERROR_CODE_END_OF_QUEUE = 11;
  public static final int ERROR_CODE_NOT_AVAILABLE_IN_REGION = 7;
  public static final int ERROR_CODE_NOT_SUPPORTED = 2;
  public static final int ERROR_CODE_PARENTAL_CONTROL_RESTRICTED = 6;
  public static final int ERROR_CODE_PREMIUM_ACCOUNT_REQUIRED = 4;
  public static final int ERROR_CODE_SETUP_REQUIRED = 12;
  public static final int ERROR_CODE_SKIP_LIMIT_REACHED = 9;
  public static final int ERROR_CODE_UNKNOWN_ERROR = 0;
  private final MediaSession2Provider mProvider;
  
  public MediaSession2(MediaSession2Provider paramMediaSession2Provider)
  {
    mProvider = paramMediaSession2Provider;
  }
  
  public void addPlaylistItem(int paramInt, MediaItem2 paramMediaItem2)
  {
    mProvider.addPlaylistItem_impl(paramInt, paramMediaItem2);
  }
  
  public void clearOnDataSourceMissingHelper()
  {
    mProvider.clearOnDataSourceMissingHelper_impl();
  }
  
  public void close()
  {
    mProvider.close_impl();
  }
  
  public long getBufferedPosition()
  {
    return mProvider.getBufferedPosition_impl();
  }
  
  public int getBufferingState()
  {
    return 0;
  }
  
  public List<ControllerInfo> getConnectedControllers()
  {
    return mProvider.getConnectedControllers_impl();
  }
  
  public MediaItem2 getCurrentMediaItem()
  {
    return mProvider.getCurrentPlaylistItem_impl();
  }
  
  public long getCurrentPosition()
  {
    return mProvider.getCurrentPosition_impl();
  }
  
  public float getPlaybackSpeed()
  {
    return -1.0F;
  }
  
  public MediaPlayerBase getPlayer()
  {
    return mProvider.getPlayer_impl();
  }
  
  public int getPlayerState()
  {
    return mProvider.getPlayerState_impl();
  }
  
  public List<MediaItem2> getPlaylist()
  {
    return mProvider.getPlaylist_impl();
  }
  
  public MediaPlaylistAgent getPlaylistAgent()
  {
    return mProvider.getPlaylistAgent_impl();
  }
  
  public MediaMetadata2 getPlaylistMetadata()
  {
    return mProvider.getPlaylistMetadata_impl();
  }
  
  public MediaSession2Provider getProvider()
  {
    return mProvider;
  }
  
  public int getRepeatMode()
  {
    return mProvider.getRepeatMode_impl();
  }
  
  public int getShuffleMode()
  {
    return mProvider.getShuffleMode_impl();
  }
  
  public SessionToken2 getToken()
  {
    return mProvider.getToken_impl();
  }
  
  public VolumeProvider2 getVolumeProvider()
  {
    return mProvider.getVolumeProvider_impl();
  }
  
  public void notifyError(int paramInt, Bundle paramBundle)
  {
    mProvider.notifyError_impl(paramInt, paramBundle);
  }
  
  public void pause()
  {
    mProvider.pause_impl();
  }
  
  public void play()
  {
    mProvider.play_impl();
  }
  
  public void prepare()
  {
    mProvider.prepare_impl();
  }
  
  public void removePlaylistItem(MediaItem2 paramMediaItem2)
  {
    mProvider.removePlaylistItem_impl(paramMediaItem2);
  }
  
  public void replacePlaylistItem(int paramInt, MediaItem2 paramMediaItem2)
  {
    mProvider.replacePlaylistItem_impl(paramInt, paramMediaItem2);
  }
  
  public void seekTo(long paramLong)
  {
    mProvider.seekTo_impl(paramLong);
  }
  
  public void sendCustomCommand(ControllerInfo paramControllerInfo, SessionCommand2 paramSessionCommand2, Bundle paramBundle, ResultReceiver paramResultReceiver)
  {
    mProvider.sendCustomCommand_impl(paramControllerInfo, paramSessionCommand2, paramBundle, paramResultReceiver);
  }
  
  public void sendCustomCommand(SessionCommand2 paramSessionCommand2, Bundle paramBundle)
  {
    mProvider.sendCustomCommand_impl(paramSessionCommand2, paramBundle);
  }
  
  public void setAllowedCommands(ControllerInfo paramControllerInfo, SessionCommandGroup2 paramSessionCommandGroup2)
  {
    mProvider.setAllowedCommands_impl(paramControllerInfo, paramSessionCommandGroup2);
  }
  
  public void setAudioFocusRequest(AudioFocusRequest paramAudioFocusRequest) {}
  
  public void setCustomLayout(ControllerInfo paramControllerInfo, List<CommandButton> paramList)
  {
    mProvider.setCustomLayout_impl(paramControllerInfo, paramList);
  }
  
  public void setOnDataSourceMissingHelper(OnDataSourceMissingHelper paramOnDataSourceMissingHelper)
  {
    mProvider.setOnDataSourceMissingHelper_impl(paramOnDataSourceMissingHelper);
  }
  
  public void setPlaybackSpeed(float paramFloat) {}
  
  public void setPlaylist(List<MediaItem2> paramList, MediaMetadata2 paramMediaMetadata2)
  {
    mProvider.setPlaylist_impl(paramList, paramMediaMetadata2);
  }
  
  public void setRepeatMode(int paramInt)
  {
    mProvider.setRepeatMode_impl(paramInt);
  }
  
  public void setShuffleMode(int paramInt)
  {
    mProvider.setShuffleMode_impl(paramInt);
  }
  
  public void skipBackward() {}
  
  public void skipForward() {}
  
  public void skipToNextItem()
  {
    mProvider.skipToNextItem_impl();
  }
  
  public void skipToPlaylistItem(MediaItem2 paramMediaItem2)
  {
    mProvider.skipToPlaylistItem_impl(paramMediaItem2);
  }
  
  public void skipToPreviousItem()
  {
    mProvider.skipToPreviousItem_impl();
  }
  
  public void stop()
  {
    mProvider.stop_impl();
  }
  
  public void updatePlayer(MediaPlayerBase paramMediaPlayerBase, MediaPlaylistAgent paramMediaPlaylistAgent, VolumeProvider2 paramVolumeProvider2)
  {
    mProvider.updatePlayer_impl(paramMediaPlayerBase, paramMediaPlaylistAgent, paramVolumeProvider2);
  }
  
  public void updatePlaylistMetadata(MediaMetadata2 paramMediaMetadata2)
  {
    mProvider.updatePlaylistMetadata_impl(paramMediaMetadata2);
  }
  
  public static final class Builder
    extends MediaSession2.BuilderBase<MediaSession2, Builder, MediaSession2.SessionCallback>
  {
    public Builder(Context paramContext)
    {
      super();
    }
    
    public MediaSession2 build()
    {
      return super.build();
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
    
    public Builder setSessionCallback(Executor paramExecutor, MediaSession2.SessionCallback paramSessionCallback)
    {
      return (Builder)super.setSessionCallback(paramExecutor, paramSessionCallback);
    }
    
    public Builder setVolumeProvider(VolumeProvider2 paramVolumeProvider2)
    {
      return (Builder)super.setVolumeProvider(paramVolumeProvider2);
    }
  }
  
  static abstract class BuilderBase<T extends MediaSession2, U extends BuilderBase<T, U, C>, C extends MediaSession2.SessionCallback>
  {
    private final MediaSession2Provider.BuilderBaseProvider<T, C> mProvider;
    
    BuilderBase(ProviderCreator<BuilderBase<T, U, C>, MediaSession2Provider.BuilderBaseProvider<T, C>> paramProviderCreator)
    {
      mProvider = ((MediaSession2Provider.BuilderBaseProvider)paramProviderCreator.createProvider(this));
    }
    
    T build()
    {
      return mProvider.build_impl();
    }
    
    U setId(String paramString)
    {
      mProvider.setId_impl(paramString);
      return this;
    }
    
    U setPlayer(MediaPlayerBase paramMediaPlayerBase)
    {
      mProvider.setPlayer_impl(paramMediaPlayerBase);
      return this;
    }
    
    U setPlaylistAgent(MediaPlaylistAgent paramMediaPlaylistAgent)
    {
      mProvider.setPlaylistAgent_impl(paramMediaPlaylistAgent);
      return this;
    }
    
    U setSessionActivity(PendingIntent paramPendingIntent)
    {
      mProvider.setSessionActivity_impl(paramPendingIntent);
      return this;
    }
    
    U setSessionCallback(Executor paramExecutor, C paramC)
    {
      mProvider.setSessionCallback_impl(paramExecutor, paramC);
      return this;
    }
    
    U setVolumeProvider(VolumeProvider2 paramVolumeProvider2)
    {
      mProvider.setVolumeProvider_impl(paramVolumeProvider2);
      return this;
    }
  }
  
  public static final class CommandButton
  {
    private final MediaSession2Provider.CommandButtonProvider mProvider;
    
    public CommandButton(MediaSession2Provider.CommandButtonProvider paramCommandButtonProvider)
    {
      mProvider = paramCommandButtonProvider;
    }
    
    public SessionCommand2 getCommand()
    {
      return mProvider.getCommand_impl();
    }
    
    public String getDisplayName()
    {
      return mProvider.getDisplayName_impl();
    }
    
    public Bundle getExtras()
    {
      return mProvider.getExtras_impl();
    }
    
    public int getIconResId()
    {
      return mProvider.getIconResId_impl();
    }
    
    public MediaSession2Provider.CommandButtonProvider getProvider()
    {
      return mProvider;
    }
    
    public boolean isEnabled()
    {
      return mProvider.isEnabled_impl();
    }
    
    public static final class Builder
    {
      private final MediaSession2Provider.CommandButtonProvider.BuilderProvider mProvider = ApiLoader.getProvider().createMediaSession2CommandButtonBuilder(this);
      
      public Builder() {}
      
      public MediaSession2.CommandButton build()
      {
        return mProvider.build_impl();
      }
      
      public Builder setCommand(SessionCommand2 paramSessionCommand2)
      {
        return mProvider.setCommand_impl(paramSessionCommand2);
      }
      
      public Builder setDisplayName(String paramString)
      {
        return mProvider.setDisplayName_impl(paramString);
      }
      
      public Builder setEnabled(boolean paramBoolean)
      {
        return mProvider.setEnabled_impl(paramBoolean);
      }
      
      public Builder setExtras(Bundle paramBundle)
      {
        return mProvider.setExtras_impl(paramBundle);
      }
      
      public Builder setIconResId(int paramInt)
      {
        return mProvider.setIconResId_impl(paramInt);
      }
    }
  }
  
  public static final class ControllerInfo
  {
    private final MediaSession2Provider.ControllerInfoProvider mProvider;
    
    public ControllerInfo(Context paramContext, int paramInt1, int paramInt2, String paramString, IInterface paramIInterface)
    {
      mProvider = ApiLoader.getProvider().createMediaSession2ControllerInfo(paramContext, this, paramInt1, paramInt2, paramString, paramIInterface);
    }
    
    public boolean equals(Object paramObject)
    {
      return mProvider.equals_impl(paramObject);
    }
    
    public String getPackageName()
    {
      return mProvider.getPackageName_impl();
    }
    
    public MediaSession2Provider.ControllerInfoProvider getProvider()
    {
      return mProvider;
    }
    
    public int getUid()
    {
      return mProvider.getUid_impl();
    }
    
    public int hashCode()
    {
      return mProvider.hashCode_impl();
    }
    
    public boolean isTrusted()
    {
      return mProvider.isTrusted_impl();
    }
    
    public String toString()
    {
      return mProvider.toString_impl();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ErrorCode {}
  
  public static abstract interface OnDataSourceMissingHelper
  {
    public abstract DataSourceDesc onDataSourceMissing(MediaSession2 paramMediaSession2, MediaItem2 paramMediaItem2);
  }
  
  public static abstract class SessionCallback
  {
    public SessionCallback() {}
    
    public void onBufferingStateChanged(MediaSession2 paramMediaSession2, MediaPlayerBase paramMediaPlayerBase, MediaItem2 paramMediaItem2, int paramInt) {}
    
    public boolean onCommandRequest(MediaSession2 paramMediaSession2, MediaSession2.ControllerInfo paramControllerInfo, SessionCommand2 paramSessionCommand2)
    {
      return true;
    }
    
    public SessionCommandGroup2 onConnect(MediaSession2 paramMediaSession2, MediaSession2.ControllerInfo paramControllerInfo)
    {
      paramMediaSession2 = new SessionCommandGroup2();
      paramMediaSession2.addAllPredefinedCommands();
      return paramMediaSession2;
    }
    
    public void onCurrentMediaItemChanged(MediaSession2 paramMediaSession2, MediaPlayerBase paramMediaPlayerBase, MediaItem2 paramMediaItem2) {}
    
    public void onCustomCommand(MediaSession2 paramMediaSession2, MediaSession2.ControllerInfo paramControllerInfo, SessionCommand2 paramSessionCommand2, Bundle paramBundle, ResultReceiver paramResultReceiver) {}
    
    public void onDisconnected(MediaSession2 paramMediaSession2, MediaSession2.ControllerInfo paramControllerInfo) {}
    
    public void onFastForward(MediaSession2 paramMediaSession2) {}
    
    public void onMediaPrepared(MediaSession2 paramMediaSession2, MediaPlayerBase paramMediaPlayerBase, MediaItem2 paramMediaItem2) {}
    
    public void onPlayFromMediaId(MediaSession2 paramMediaSession2, MediaSession2.ControllerInfo paramControllerInfo, String paramString, Bundle paramBundle) {}
    
    public void onPlayFromSearch(MediaSession2 paramMediaSession2, MediaSession2.ControllerInfo paramControllerInfo, String paramString, Bundle paramBundle) {}
    
    public void onPlayFromUri(MediaSession2 paramMediaSession2, MediaSession2.ControllerInfo paramControllerInfo, Uri paramUri, Bundle paramBundle) {}
    
    public void onPlaybackSpeedChanged(MediaSession2 paramMediaSession2, MediaPlayerBase paramMediaPlayerBase, float paramFloat) {}
    
    public void onPlayerStateChanged(MediaSession2 paramMediaSession2, MediaPlayerBase paramMediaPlayerBase, int paramInt) {}
    
    public void onPlaylistChanged(MediaSession2 paramMediaSession2, MediaPlaylistAgent paramMediaPlaylistAgent, List<MediaItem2> paramList, MediaMetadata2 paramMediaMetadata2) {}
    
    public void onPlaylistMetadataChanged(MediaSession2 paramMediaSession2, MediaPlaylistAgent paramMediaPlaylistAgent, MediaMetadata2 paramMediaMetadata2) {}
    
    public void onPrepareFromMediaId(MediaSession2 paramMediaSession2, MediaSession2.ControllerInfo paramControllerInfo, String paramString, Bundle paramBundle) {}
    
    public void onPrepareFromSearch(MediaSession2 paramMediaSession2, MediaSession2.ControllerInfo paramControllerInfo, String paramString, Bundle paramBundle) {}
    
    public void onPrepareFromUri(MediaSession2 paramMediaSession2, MediaSession2.ControllerInfo paramControllerInfo, Uri paramUri, Bundle paramBundle) {}
    
    public void onRepeatModeChanged(MediaSession2 paramMediaSession2, MediaPlaylistAgent paramMediaPlaylistAgent, int paramInt) {}
    
    public void onRewind(MediaSession2 paramMediaSession2) {}
    
    public void onSeekCompleted(MediaSession2 paramMediaSession2, MediaPlayerBase paramMediaPlayerBase, long paramLong) {}
    
    public void onSetRating(MediaSession2 paramMediaSession2, MediaSession2.ControllerInfo paramControllerInfo, String paramString, Rating2 paramRating2) {}
    
    public void onShuffleModeChanged(MediaSession2 paramMediaSession2, MediaPlaylistAgent paramMediaPlaylistAgent, int paramInt) {}
  }
}
