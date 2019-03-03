package android.media.update;

import android.app.PendingIntent;
import android.media.AudioFocusRequest;
import android.media.MediaItem2;
import android.media.MediaMetadata2;
import android.media.MediaPlayerBase;
import android.media.MediaPlaylistAgent;
import android.media.MediaSession2;
import android.media.MediaSession2.CommandButton;
import android.media.MediaSession2.CommandButton.Builder;
import android.media.MediaSession2.ControllerInfo;
import android.media.MediaSession2.OnDataSourceMissingHelper;
import android.media.MediaSession2.SessionCallback;
import android.media.SessionCommand2;
import android.media.SessionCommandGroup2;
import android.media.SessionToken2;
import android.media.VolumeProvider2;
import android.os.Bundle;
import android.os.ResultReceiver;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

public abstract interface MediaSession2Provider
  extends TransportControlProvider
{
  public abstract void addPlaylistItem_impl(int paramInt, MediaItem2 paramMediaItem2);
  
  public abstract void clearOnDataSourceMissingHelper_impl();
  
  public abstract void close_impl();
  
  public abstract long getBufferedPosition_impl();
  
  public abstract List<MediaSession2.ControllerInfo> getConnectedControllers_impl();
  
  public abstract MediaItem2 getCurrentPlaylistItem_impl();
  
  public abstract long getCurrentPosition_impl();
  
  public abstract int getPlayerState_impl();
  
  public abstract MediaPlayerBase getPlayer_impl();
  
  public abstract MediaPlaylistAgent getPlaylistAgent_impl();
  
  public abstract MediaMetadata2 getPlaylistMetadata_impl();
  
  public abstract List<MediaItem2> getPlaylist_impl();
  
  public abstract SessionToken2 getToken_impl();
  
  public abstract VolumeProvider2 getVolumeProvider_impl();
  
  public abstract void notifyError_impl(int paramInt, Bundle paramBundle);
  
  public abstract void removePlaylistItem_impl(MediaItem2 paramMediaItem2);
  
  public abstract void replacePlaylistItem_impl(int paramInt, MediaItem2 paramMediaItem2);
  
  public abstract void sendCustomCommand_impl(MediaSession2.ControllerInfo paramControllerInfo, SessionCommand2 paramSessionCommand2, Bundle paramBundle, ResultReceiver paramResultReceiver);
  
  public abstract void sendCustomCommand_impl(SessionCommand2 paramSessionCommand2, Bundle paramBundle);
  
  public abstract void setAllowedCommands_impl(MediaSession2.ControllerInfo paramControllerInfo, SessionCommandGroup2 paramSessionCommandGroup2);
  
  public abstract void setAudioFocusRequest_impl(AudioFocusRequest paramAudioFocusRequest);
  
  public abstract void setCustomLayout_impl(MediaSession2.ControllerInfo paramControllerInfo, List<MediaSession2.CommandButton> paramList);
  
  public abstract void setOnDataSourceMissingHelper_impl(MediaSession2.OnDataSourceMissingHelper paramOnDataSourceMissingHelper);
  
  public abstract void setPlaylist_impl(List<MediaItem2> paramList, MediaMetadata2 paramMediaMetadata2);
  
  public abstract void updatePlayer_impl(MediaPlayerBase paramMediaPlayerBase, MediaPlaylistAgent paramMediaPlaylistAgent, VolumeProvider2 paramVolumeProvider2);
  
  public abstract void updatePlaylistMetadata_impl(MediaMetadata2 paramMediaMetadata2);
  
  public static abstract interface BuilderBaseProvider<T extends MediaSession2, C extends MediaSession2.SessionCallback>
  {
    public abstract T build_impl();
    
    public abstract void setId_impl(String paramString);
    
    public abstract void setPlayer_impl(MediaPlayerBase paramMediaPlayerBase);
    
    public abstract void setPlaylistAgent_impl(MediaPlaylistAgent paramMediaPlaylistAgent);
    
    public abstract void setSessionActivity_impl(PendingIntent paramPendingIntent);
    
    public abstract void setSessionCallback_impl(Executor paramExecutor, C paramC);
    
    public abstract void setVolumeProvider_impl(VolumeProvider2 paramVolumeProvider2);
  }
  
  public static abstract interface CommandButtonProvider
  {
    public abstract SessionCommand2 getCommand_impl();
    
    public abstract String getDisplayName_impl();
    
    public abstract Bundle getExtras_impl();
    
    public abstract int getIconResId_impl();
    
    public abstract boolean isEnabled_impl();
    
    public static abstract interface BuilderProvider
    {
      public abstract MediaSession2.CommandButton build_impl();
      
      public abstract MediaSession2.CommandButton.Builder setCommand_impl(SessionCommand2 paramSessionCommand2);
      
      public abstract MediaSession2.CommandButton.Builder setDisplayName_impl(String paramString);
      
      public abstract MediaSession2.CommandButton.Builder setEnabled_impl(boolean paramBoolean);
      
      public abstract MediaSession2.CommandButton.Builder setExtras_impl(Bundle paramBundle);
      
      public abstract MediaSession2.CommandButton.Builder setIconResId_impl(int paramInt);
    }
  }
  
  public static abstract interface CommandGroupProvider
  {
    public abstract void addAllPredefinedCommands_impl();
    
    public abstract void addCommand_impl(SessionCommand2 paramSessionCommand2);
    
    public abstract Set<SessionCommand2> getCommands_impl();
    
    public abstract boolean hasCommand_impl(int paramInt);
    
    public abstract boolean hasCommand_impl(SessionCommand2 paramSessionCommand2);
    
    public abstract void removeCommand_impl(SessionCommand2 paramSessionCommand2);
    
    public abstract Bundle toBundle_impl();
  }
  
  public static abstract interface CommandProvider
  {
    public abstract boolean equals_impl(Object paramObject);
    
    public abstract int getCommandCode_impl();
    
    public abstract String getCustomCommand_impl();
    
    public abstract Bundle getExtras_impl();
    
    public abstract int hashCode_impl();
    
    public abstract Bundle toBundle_impl();
  }
  
  public static abstract interface ControllerInfoProvider
  {
    public abstract boolean equals_impl(Object paramObject);
    
    public abstract String getPackageName_impl();
    
    public abstract int getUid_impl();
    
    public abstract int hashCode_impl();
    
    public abstract boolean isTrusted_impl();
    
    public abstract String toString_impl();
  }
}
