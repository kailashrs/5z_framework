package android.media.update;

import android.media.DataSourceDesc;
import android.media.MediaItem2;
import android.media.MediaMetadata2;
import android.media.MediaPlaylistAgent.PlaylistEventCallback;
import java.util.List;
import java.util.concurrent.Executor;

public abstract interface MediaPlaylistAgentProvider
{
  public abstract void addPlaylistItem_impl(int paramInt, MediaItem2 paramMediaItem2);
  
  public abstract MediaItem2 getMediaItem_impl(DataSourceDesc paramDataSourceDesc);
  
  public abstract MediaMetadata2 getPlaylistMetadata_impl();
  
  public abstract List<MediaItem2> getPlaylist_impl();
  
  public abstract int getRepeatMode_impl();
  
  public abstract int getShuffleMode_impl();
  
  public abstract void notifyPlaylistChanged_impl();
  
  public abstract void notifyPlaylistMetadataChanged_impl();
  
  public abstract void notifyRepeatModeChanged_impl();
  
  public abstract void notifyShuffleModeChanged_impl();
  
  public abstract void registerPlaylistEventCallback_impl(Executor paramExecutor, MediaPlaylistAgent.PlaylistEventCallback paramPlaylistEventCallback);
  
  public abstract void removePlaylistItem_impl(MediaItem2 paramMediaItem2);
  
  public abstract void replacePlaylistItem_impl(int paramInt, MediaItem2 paramMediaItem2);
  
  public abstract void setPlaylist_impl(List<MediaItem2> paramList, MediaMetadata2 paramMediaMetadata2);
  
  public abstract void setRepeatMode_impl(int paramInt);
  
  public abstract void setShuffleMode_impl(int paramInt);
  
  public abstract void skipToNextItem_impl();
  
  public abstract void skipToPlaylistItem_impl(MediaItem2 paramMediaItem2);
  
  public abstract void skipToPreviousItem_impl();
  
  public abstract void unregisterPlaylistEventCallback_impl(MediaPlaylistAgent.PlaylistEventCallback paramPlaylistEventCallback);
  
  public abstract void updatePlaylistMetadata_impl(MediaMetadata2 paramMediaMetadata2);
}
