package android.media;

import android.media.update.ApiLoader;
import android.media.update.MediaPlaylistAgentProvider;
import android.media.update.StaticProvider;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.Executor;

public abstract class MediaPlaylistAgent
{
  public static final int REPEAT_MODE_ALL = 2;
  public static final int REPEAT_MODE_GROUP = 3;
  public static final int REPEAT_MODE_NONE = 0;
  public static final int REPEAT_MODE_ONE = 1;
  public static final int SHUFFLE_MODE_ALL = 1;
  public static final int SHUFFLE_MODE_GROUP = 2;
  public static final int SHUFFLE_MODE_NONE = 0;
  private final MediaPlaylistAgentProvider mProvider = ApiLoader.getProvider().createMediaPlaylistAgent(this);
  
  public MediaPlaylistAgent() {}
  
  public void addPlaylistItem(int paramInt, MediaItem2 paramMediaItem2)
  {
    mProvider.addPlaylistItem_impl(paramInt, paramMediaItem2);
  }
  
  public MediaItem2 getMediaItem(DataSourceDesc paramDataSourceDesc)
  {
    return mProvider.getMediaItem_impl(paramDataSourceDesc);
  }
  
  public List<MediaItem2> getPlaylist()
  {
    return mProvider.getPlaylist_impl();
  }
  
  public MediaMetadata2 getPlaylistMetadata()
  {
    return mProvider.getPlaylistMetadata_impl();
  }
  
  public int getRepeatMode()
  {
    return mProvider.getRepeatMode_impl();
  }
  
  public int getShuffleMode()
  {
    return mProvider.getShuffleMode_impl();
  }
  
  public final void notifyPlaylistChanged()
  {
    mProvider.notifyPlaylistChanged_impl();
  }
  
  public final void notifyPlaylistMetadataChanged()
  {
    mProvider.notifyPlaylistMetadataChanged_impl();
  }
  
  public final void notifyRepeatModeChanged()
  {
    mProvider.notifyRepeatModeChanged_impl();
  }
  
  public final void notifyShuffleModeChanged()
  {
    mProvider.notifyShuffleModeChanged_impl();
  }
  
  public final void registerPlaylistEventCallback(Executor paramExecutor, PlaylistEventCallback paramPlaylistEventCallback)
  {
    mProvider.registerPlaylistEventCallback_impl(paramExecutor, paramPlaylistEventCallback);
  }
  
  public void removePlaylistItem(MediaItem2 paramMediaItem2)
  {
    mProvider.removePlaylistItem_impl(paramMediaItem2);
  }
  
  public void replacePlaylistItem(int paramInt, MediaItem2 paramMediaItem2)
  {
    mProvider.replacePlaylistItem_impl(paramInt, paramMediaItem2);
  }
  
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
  
  public final void unregisterPlaylistEventCallback(PlaylistEventCallback paramPlaylistEventCallback)
  {
    mProvider.unregisterPlaylistEventCallback_impl(paramPlaylistEventCallback);
  }
  
  public void updatePlaylistMetadata(MediaMetadata2 paramMediaMetadata2)
  {
    mProvider.updatePlaylistMetadata_impl(paramMediaMetadata2);
  }
  
  public static abstract class PlaylistEventCallback
  {
    public PlaylistEventCallback() {}
    
    public void onPlaylistChanged(MediaPlaylistAgent paramMediaPlaylistAgent, List<MediaItem2> paramList, MediaMetadata2 paramMediaMetadata2) {}
    
    public void onPlaylistMetadataChanged(MediaPlaylistAgent paramMediaPlaylistAgent, MediaMetadata2 paramMediaMetadata2) {}
    
    public void onRepeatModeChanged(MediaPlaylistAgent paramMediaPlaylistAgent, int paramInt) {}
    
    public void onShuffleModeChanged(MediaPlaylistAgent paramMediaPlaylistAgent, int paramInt) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RepeatMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ShuffleMode {}
}
