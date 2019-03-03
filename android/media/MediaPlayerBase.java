package android.media;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.Executor;

public abstract class MediaPlayerBase
  implements AutoCloseable
{
  public static final int BUFFERING_STATE_BUFFERING_AND_PLAYABLE = 1;
  public static final int BUFFERING_STATE_BUFFERING_AND_STARVED = 2;
  public static final int BUFFERING_STATE_BUFFERING_COMPLETE = 3;
  public static final int BUFFERING_STATE_UNKNOWN = 0;
  public static final int PLAYER_STATE_ERROR = 3;
  public static final int PLAYER_STATE_IDLE = 0;
  public static final int PLAYER_STATE_PAUSED = 1;
  public static final int PLAYER_STATE_PLAYING = 2;
  public static final long UNKNOWN_TIME = -1L;
  
  public MediaPlayerBase() {}
  
  public abstract AudioAttributes getAudioAttributes();
  
  public long getBufferedPosition()
  {
    return -1L;
  }
  
  public abstract int getBufferingState();
  
  public abstract DataSourceDesc getCurrentDataSource();
  
  public long getCurrentPosition()
  {
    return -1L;
  }
  
  public long getDuration()
  {
    return -1L;
  }
  
  public float getMaxPlayerVolume()
  {
    return 1.0F;
  }
  
  public float getPlaybackSpeed()
  {
    return 1.0F;
  }
  
  public abstract int getPlayerState();
  
  public abstract float getPlayerVolume();
  
  public boolean isReversePlaybackSupported()
  {
    return false;
  }
  
  public abstract void loopCurrent(boolean paramBoolean);
  
  public abstract void pause();
  
  public abstract void play();
  
  public abstract void prepare();
  
  public abstract void registerPlayerEventCallback(Executor paramExecutor, PlayerEventCallback paramPlayerEventCallback);
  
  public abstract void reset();
  
  public abstract void seekTo(long paramLong);
  
  public abstract void setAudioAttributes(AudioAttributes paramAudioAttributes);
  
  public abstract void setDataSource(DataSourceDesc paramDataSourceDesc);
  
  public abstract void setNextDataSource(DataSourceDesc paramDataSourceDesc);
  
  public abstract void setNextDataSources(List<DataSourceDesc> paramList);
  
  public abstract void setPlaybackSpeed(float paramFloat);
  
  public abstract void setPlayerVolume(float paramFloat);
  
  public abstract void skipToNext();
  
  public abstract void unregisterPlayerEventCallback(PlayerEventCallback paramPlayerEventCallback);
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BuffState {}
  
  public static abstract class PlayerEventCallback
  {
    public PlayerEventCallback() {}
    
    public void onBufferingStateChanged(MediaPlayerBase paramMediaPlayerBase, DataSourceDesc paramDataSourceDesc, int paramInt) {}
    
    public void onCurrentDataSourceChanged(MediaPlayerBase paramMediaPlayerBase, DataSourceDesc paramDataSourceDesc) {}
    
    public void onMediaPrepared(MediaPlayerBase paramMediaPlayerBase, DataSourceDesc paramDataSourceDesc) {}
    
    public void onPlaybackSpeedChanged(MediaPlayerBase paramMediaPlayerBase, float paramFloat) {}
    
    public void onPlayerStateChanged(MediaPlayerBase paramMediaPlayerBase, int paramInt) {}
    
    public void onSeekCompleted(MediaPlayerBase paramMediaPlayerBase, long paramLong) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PlayerState {}
}
