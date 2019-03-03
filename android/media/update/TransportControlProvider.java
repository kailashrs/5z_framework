package android.media.update;

import android.media.MediaItem2;

public abstract interface TransportControlProvider
{
  public abstract int getRepeatMode_impl();
  
  public abstract int getShuffleMode_impl();
  
  public abstract void pause_impl();
  
  public abstract void play_impl();
  
  public abstract void prepare_impl();
  
  public abstract void seekTo_impl(long paramLong);
  
  public abstract void setRepeatMode_impl(int paramInt);
  
  public abstract void setShuffleMode_impl(int paramInt);
  
  public abstract void skipToNextItem_impl();
  
  public abstract void skipToPlaylistItem_impl(MediaItem2 paramMediaItem2);
  
  public abstract void skipToPreviousItem_impl();
  
  public abstract void stop_impl();
}
