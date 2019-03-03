package android.media;

import android.app.PendingIntent;
import android.content.Context;
import android.media.update.ApiLoader;
import android.media.update.MediaController2Provider;
import android.media.update.MediaController2Provider.PlaybackInfoProvider;
import android.media.update.StaticProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import java.util.List;
import java.util.concurrent.Executor;

public class MediaController2
  implements AutoCloseable
{
  private final MediaController2Provider mProvider = createProvider(paramContext, paramSessionToken2, paramExecutor, paramControllerCallback);
  
  public MediaController2(Context paramContext, SessionToken2 paramSessionToken2, Executor paramExecutor, ControllerCallback paramControllerCallback)
  {
    mProvider.initialize();
  }
  
  public void addPlaylistItem(int paramInt, MediaItem2 paramMediaItem2)
  {
    mProvider.addPlaylistItem_impl(paramInt, paramMediaItem2);
  }
  
  public void adjustVolume(int paramInt1, int paramInt2)
  {
    mProvider.adjustVolume_impl(paramInt1, paramInt2);
  }
  
  public void close()
  {
    mProvider.close_impl();
  }
  
  MediaController2Provider createProvider(Context paramContext, SessionToken2 paramSessionToken2, Executor paramExecutor, ControllerCallback paramControllerCallback)
  {
    return ApiLoader.getProvider().createMediaController2(paramContext, this, paramSessionToken2, paramExecutor, paramControllerCallback);
  }
  
  public void fastForward()
  {
    mProvider.fastForward_impl();
  }
  
  public long getBufferedPosition()
  {
    return mProvider.getBufferedPosition_impl();
  }
  
  public int getBufferingState()
  {
    return 0;
  }
  
  public MediaItem2 getCurrentMediaItem()
  {
    return mProvider.getCurrentMediaItem_impl();
  }
  
  public long getCurrentPosition()
  {
    return mProvider.getCurrentPosition_impl();
  }
  
  public PlaybackInfo getPlaybackInfo()
  {
    return mProvider.getPlaybackInfo_impl();
  }
  
  public float getPlaybackSpeed()
  {
    return mProvider.getPlaybackSpeed_impl();
  }
  
  public int getPlayerState()
  {
    return mProvider.getPlayerState_impl();
  }
  
  public List<MediaItem2> getPlaylist()
  {
    return mProvider.getPlaylist_impl();
  }
  
  public MediaMetadata2 getPlaylistMetadata()
  {
    return mProvider.getPlaylistMetadata_impl();
  }
  
  public MediaController2Provider getProvider()
  {
    return mProvider;
  }
  
  public int getRepeatMode()
  {
    return mProvider.getRepeatMode_impl();
  }
  
  public PendingIntent getSessionActivity()
  {
    return mProvider.getSessionActivity_impl();
  }
  
  public SessionToken2 getSessionToken()
  {
    return mProvider.getSessionToken_impl();
  }
  
  public int getShuffleMode()
  {
    return mProvider.getShuffleMode_impl();
  }
  
  public boolean isConnected()
  {
    return mProvider.isConnected_impl();
  }
  
  public void pause()
  {
    mProvider.pause_impl();
  }
  
  public void play()
  {
    mProvider.play_impl();
  }
  
  public void playFromMediaId(String paramString, Bundle paramBundle)
  {
    mProvider.playFromMediaId_impl(paramString, paramBundle);
  }
  
  public void playFromSearch(String paramString, Bundle paramBundle)
  {
    mProvider.playFromSearch_impl(paramString, paramBundle);
  }
  
  public void playFromUri(Uri paramUri, Bundle paramBundle)
  {
    mProvider.playFromUri_impl(paramUri, paramBundle);
  }
  
  public void prepare()
  {
    mProvider.prepare_impl();
  }
  
  public void prepareFromMediaId(String paramString, Bundle paramBundle)
  {
    mProvider.prepareFromMediaId_impl(paramString, paramBundle);
  }
  
  public void prepareFromSearch(String paramString, Bundle paramBundle)
  {
    mProvider.prepareFromSearch_impl(paramString, paramBundle);
  }
  
  public void prepareFromUri(Uri paramUri, Bundle paramBundle)
  {
    mProvider.prepareFromUri_impl(paramUri, paramBundle);
  }
  
  public void removePlaylistItem(MediaItem2 paramMediaItem2)
  {
    mProvider.removePlaylistItem_impl(paramMediaItem2);
  }
  
  public void replacePlaylistItem(int paramInt, MediaItem2 paramMediaItem2)
  {
    mProvider.replacePlaylistItem_impl(paramInt, paramMediaItem2);
  }
  
  public void rewind()
  {
    mProvider.rewind_impl();
  }
  
  public void seekTo(long paramLong)
  {
    mProvider.seekTo_impl(paramLong);
  }
  
  public void sendCustomCommand(SessionCommand2 paramSessionCommand2, Bundle paramBundle, ResultReceiver paramResultReceiver)
  {
    mProvider.sendCustomCommand_impl(paramSessionCommand2, paramBundle, paramResultReceiver);
  }
  
  public void setPlaybackSpeed(float paramFloat) {}
  
  public void setPlaylist(List<MediaItem2> paramList, MediaMetadata2 paramMediaMetadata2)
  {
    mProvider.setPlaylist_impl(paramList, paramMediaMetadata2);
  }
  
  public void setRating(String paramString, Rating2 paramRating2)
  {
    mProvider.setRating_impl(paramString, paramRating2);
  }
  
  public void setRepeatMode(int paramInt)
  {
    mProvider.setRepeatMode_impl(paramInt);
  }
  
  public void setShuffleMode(int paramInt)
  {
    mProvider.setShuffleMode_impl(paramInt);
  }
  
  public void setVolumeTo(int paramInt1, int paramInt2)
  {
    mProvider.setVolumeTo_impl(paramInt1, paramInt2);
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
  
  public void updatePlaylistMetadata(MediaMetadata2 paramMediaMetadata2)
  {
    mProvider.updatePlaylistMetadata_impl(paramMediaMetadata2);
  }
  
  public static abstract class ControllerCallback
  {
    public ControllerCallback() {}
    
    public void onAllowedCommandsChanged(MediaController2 paramMediaController2, SessionCommandGroup2 paramSessionCommandGroup2) {}
    
    public void onBufferingStateChanged(MediaController2 paramMediaController2, MediaItem2 paramMediaItem2, int paramInt) {}
    
    public void onConnected(MediaController2 paramMediaController2, SessionCommandGroup2 paramSessionCommandGroup2) {}
    
    public void onCurrentMediaItemChanged(MediaController2 paramMediaController2, MediaItem2 paramMediaItem2) {}
    
    public void onCustomCommand(MediaController2 paramMediaController2, SessionCommand2 paramSessionCommand2, Bundle paramBundle, ResultReceiver paramResultReceiver) {}
    
    public void onCustomLayoutChanged(MediaController2 paramMediaController2, List<MediaSession2.CommandButton> paramList) {}
    
    public void onDisconnected(MediaController2 paramMediaController2) {}
    
    public void onError(MediaController2 paramMediaController2, int paramInt, Bundle paramBundle) {}
    
    public void onPlaybackInfoChanged(MediaController2 paramMediaController2, MediaController2.PlaybackInfo paramPlaybackInfo) {}
    
    public void onPlaybackSpeedChanged(MediaController2 paramMediaController2, float paramFloat) {}
    
    public void onPlayerStateChanged(MediaController2 paramMediaController2, int paramInt) {}
    
    public void onPlaylistChanged(MediaController2 paramMediaController2, List<MediaItem2> paramList, MediaMetadata2 paramMediaMetadata2) {}
    
    public void onPlaylistMetadataChanged(MediaController2 paramMediaController2, MediaMetadata2 paramMediaMetadata2) {}
    
    public void onRepeatModeChanged(MediaController2 paramMediaController2, int paramInt) {}
    
    public void onSeekCompleted(MediaController2 paramMediaController2, long paramLong) {}
    
    public void onShuffleModeChanged(MediaController2 paramMediaController2, int paramInt) {}
  }
  
  public static final class PlaybackInfo
  {
    public static final int PLAYBACK_TYPE_LOCAL = 1;
    public static final int PLAYBACK_TYPE_REMOTE = 2;
    private final MediaController2Provider.PlaybackInfoProvider mProvider;
    
    public PlaybackInfo(MediaController2Provider.PlaybackInfoProvider paramPlaybackInfoProvider)
    {
      mProvider = paramPlaybackInfoProvider;
    }
    
    public AudioAttributes getAudioAttributes()
    {
      return mProvider.getAudioAttributes_impl();
    }
    
    public int getControlType()
    {
      return mProvider.getControlType_impl();
    }
    
    public int getCurrentVolume()
    {
      return mProvider.getCurrentVolume_impl();
    }
    
    public int getMaxVolume()
    {
      return mProvider.getMaxVolume_impl();
    }
    
    public int getPlaybackType()
    {
      return mProvider.getPlaybackType_impl();
    }
    
    public MediaController2Provider.PlaybackInfoProvider getProvider()
    {
      return mProvider;
    }
  }
}
