package android.media.update;

import android.app.PendingIntent;
import android.media.AudioAttributes;
import android.media.MediaController2.PlaybackInfo;
import android.media.MediaItem2;
import android.media.MediaMetadata2;
import android.media.Rating2;
import android.media.SessionCommand2;
import android.media.SessionToken2;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import java.util.List;

public abstract interface MediaController2Provider
  extends TransportControlProvider
{
  public abstract void addPlaylistItem_impl(int paramInt, MediaItem2 paramMediaItem2);
  
  public abstract void adjustVolume_impl(int paramInt1, int paramInt2);
  
  public abstract void close_impl();
  
  public abstract void fastForward_impl();
  
  public abstract long getBufferedPosition_impl();
  
  public abstract MediaItem2 getCurrentMediaItem_impl();
  
  public abstract long getCurrentPosition_impl();
  
  public abstract MediaController2.PlaybackInfo getPlaybackInfo_impl();
  
  public abstract float getPlaybackSpeed_impl();
  
  public abstract int getPlayerState_impl();
  
  public abstract MediaMetadata2 getPlaylistMetadata_impl();
  
  public abstract List<MediaItem2> getPlaylist_impl();
  
  public abstract PendingIntent getSessionActivity_impl();
  
  public abstract SessionToken2 getSessionToken_impl();
  
  public abstract void initialize();
  
  public abstract boolean isConnected_impl();
  
  public abstract void playFromMediaId_impl(String paramString, Bundle paramBundle);
  
  public abstract void playFromSearch_impl(String paramString, Bundle paramBundle);
  
  public abstract void playFromUri_impl(Uri paramUri, Bundle paramBundle);
  
  public abstract void prepareFromMediaId_impl(String paramString, Bundle paramBundle);
  
  public abstract void prepareFromSearch_impl(String paramString, Bundle paramBundle);
  
  public abstract void prepareFromUri_impl(Uri paramUri, Bundle paramBundle);
  
  public abstract void removePlaylistItem_impl(MediaItem2 paramMediaItem2);
  
  public abstract void replacePlaylistItem_impl(int paramInt, MediaItem2 paramMediaItem2);
  
  public abstract void rewind_impl();
  
  public abstract void sendCustomCommand_impl(SessionCommand2 paramSessionCommand2, Bundle paramBundle, ResultReceiver paramResultReceiver);
  
  public abstract void setPlaylist_impl(List<MediaItem2> paramList, MediaMetadata2 paramMediaMetadata2);
  
  public abstract void setRating_impl(String paramString, Rating2 paramRating2);
  
  public abstract void setVolumeTo_impl(int paramInt1, int paramInt2);
  
  public abstract void updatePlaylistMetadata_impl(MediaMetadata2 paramMediaMetadata2);
  
  public static abstract interface PlaybackInfoProvider
  {
    public abstract AudioAttributes getAudioAttributes_impl();
    
    public abstract int getControlType_impl();
    
    public abstract int getCurrentVolume_impl();
    
    public abstract int getMaxVolume_impl();
    
    public abstract int getPlaybackType_impl();
  }
}
