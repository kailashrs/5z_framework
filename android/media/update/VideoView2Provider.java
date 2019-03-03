package android.media.update;

import android.media.AudioAttributes;
import android.media.DataSourceDesc;
import android.media.MediaItem2;
import android.media.MediaMetadata2;
import android.media.SessionToken2;
import android.media.session.MediaController;
import android.media.session.PlaybackState.CustomAction;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.MediaControlView2;
import android.widget.VideoView2.OnCustomActionListener;
import android.widget.VideoView2.OnFullScreenRequestListener;
import android.widget.VideoView2.OnViewTypeChangedListener;
import com.android.internal.annotations.VisibleForTesting;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public abstract interface VideoView2Provider
  extends ViewGroupProvider
{
  public abstract MediaControlView2 getMediaControlView2_impl();
  
  public abstract MediaController getMediaController_impl();
  
  public abstract MediaMetadata2 getMediaMetadata_impl();
  
  public abstract SessionToken2 getMediaSessionToken_impl();
  
  public abstract int getViewType_impl();
  
  public abstract void initialize(AttributeSet paramAttributeSet, int paramInt1, int paramInt2);
  
  public abstract boolean isSubtitleEnabled_impl();
  
  public abstract void setAudioAttributes_impl(AudioAttributes paramAudioAttributes);
  
  public abstract void setAudioFocusRequest_impl(int paramInt);
  
  public abstract void setCustomActions_impl(List<PlaybackState.CustomAction> paramList, Executor paramExecutor, VideoView2.OnCustomActionListener paramOnCustomActionListener);
  
  public abstract void setDataSource_impl(DataSourceDesc paramDataSourceDesc);
  
  public abstract void setFullScreenRequestListener_impl(VideoView2.OnFullScreenRequestListener paramOnFullScreenRequestListener);
  
  public abstract void setMediaControlView2_impl(MediaControlView2 paramMediaControlView2, long paramLong);
  
  public abstract void setMediaItem_impl(MediaItem2 paramMediaItem2);
  
  public abstract void setMediaMetadata_impl(MediaMetadata2 paramMediaMetadata2);
  
  @VisibleForTesting
  public abstract void setOnViewTypeChangedListener_impl(VideoView2.OnViewTypeChangedListener paramOnViewTypeChangedListener);
  
  public abstract void setSpeed_impl(float paramFloat);
  
  public abstract void setSubtitleEnabled_impl(boolean paramBoolean);
  
  public abstract void setVideoPath_impl(String paramString);
  
  public abstract void setVideoUri_impl(Uri paramUri);
  
  public abstract void setVideoUri_impl(Uri paramUri, Map<String, String> paramMap);
  
  public abstract void setViewType_impl(int paramInt);
}
