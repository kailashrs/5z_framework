package android.widget;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.DataSourceDesc;
import android.media.MediaItem2;
import android.media.MediaMetadata2;
import android.media.SessionToken2;
import android.media.session.MediaController;
import android.media.session.PlaybackState.CustomAction;
import android.media.update.VideoView2Provider;
import android.media.update.ViewGroupHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class VideoView2
  extends ViewGroupHelper<VideoView2Provider>
{
  public static final int VIEW_TYPE_SURFACEVIEW = 1;
  public static final int VIEW_TYPE_TEXTUREVIEW = 2;
  
  public VideoView2(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public VideoView2(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public VideoView2(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public VideoView2(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(new _..Lambda.VideoView2.uEOuYyXshHhDohoRHf3tK3H7V00(paramAttributeSet, paramInt1, paramInt2), paramContext, paramAttributeSet, paramInt1, paramInt2);
    ((VideoView2Provider)mProvider).initialize(paramAttributeSet, paramInt1, paramInt2);
  }
  
  public MediaControlView2 getMediaControlView2()
  {
    return ((VideoView2Provider)mProvider).getMediaControlView2_impl();
  }
  
  public MediaController getMediaController()
  {
    return ((VideoView2Provider)mProvider).getMediaController_impl();
  }
  
  public MediaMetadata2 getMediaMetadata()
  {
    return ((VideoView2Provider)mProvider).getMediaMetadata_impl();
  }
  
  public SessionToken2 getMediaSessionToken()
  {
    return ((VideoView2Provider)mProvider).getMediaSessionToken_impl();
  }
  
  public int getViewType()
  {
    return ((VideoView2Provider)mProvider).getViewType_impl();
  }
  
  public boolean isSubtitleEnabled()
  {
    return ((VideoView2Provider)mProvider).isSubtitleEnabled_impl();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ((VideoView2Provider)mProvider).onLayout_impl(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setAudioAttributes(AudioAttributes paramAudioAttributes)
  {
    ((VideoView2Provider)mProvider).setAudioAttributes_impl(paramAudioAttributes);
  }
  
  public void setAudioFocusRequest(int paramInt)
  {
    ((VideoView2Provider)mProvider).setAudioFocusRequest_impl(paramInt);
  }
  
  public void setCustomActions(List<PlaybackState.CustomAction> paramList, Executor paramExecutor, OnCustomActionListener paramOnCustomActionListener)
  {
    ((VideoView2Provider)mProvider).setCustomActions_impl(paramList, paramExecutor, paramOnCustomActionListener);
  }
  
  public void setDataSource(DataSourceDesc paramDataSourceDesc)
  {
    ((VideoView2Provider)mProvider).setDataSource_impl(paramDataSourceDesc);
  }
  
  public void setFullScreenRequestListener(OnFullScreenRequestListener paramOnFullScreenRequestListener)
  {
    ((VideoView2Provider)mProvider).setFullScreenRequestListener_impl(paramOnFullScreenRequestListener);
  }
  
  public void setMediaControlView2(MediaControlView2 paramMediaControlView2, long paramLong)
  {
    ((VideoView2Provider)mProvider).setMediaControlView2_impl(paramMediaControlView2, paramLong);
  }
  
  public void setMediaItem(MediaItem2 paramMediaItem2)
  {
    ((VideoView2Provider)mProvider).setMediaItem_impl(paramMediaItem2);
  }
  
  public void setMediaMetadata(MediaMetadata2 paramMediaMetadata2)
  {
    ((VideoView2Provider)mProvider).setMediaMetadata_impl(paramMediaMetadata2);
  }
  
  @VisibleForTesting
  public void setOnViewTypeChangedListener(OnViewTypeChangedListener paramOnViewTypeChangedListener)
  {
    ((VideoView2Provider)mProvider).setOnViewTypeChangedListener_impl(paramOnViewTypeChangedListener);
  }
  
  public void setSpeed(float paramFloat)
  {
    ((VideoView2Provider)mProvider).setSpeed_impl(paramFloat);
  }
  
  public void setSubtitleEnabled(boolean paramBoolean)
  {
    ((VideoView2Provider)mProvider).setSubtitleEnabled_impl(paramBoolean);
  }
  
  public void setVideoPath(String paramString)
  {
    ((VideoView2Provider)mProvider).setVideoPath_impl(paramString);
  }
  
  public void setVideoUri(Uri paramUri)
  {
    ((VideoView2Provider)mProvider).setVideoUri_impl(paramUri);
  }
  
  public void setVideoUri(Uri paramUri, Map<String, String> paramMap)
  {
    ((VideoView2Provider)mProvider).setVideoUri_impl(paramUri, paramMap);
  }
  
  public void setViewType(int paramInt)
  {
    ((VideoView2Provider)mProvider).setViewType_impl(paramInt);
  }
  
  public static abstract interface OnCustomActionListener
  {
    public abstract void onCustomAction(String paramString, Bundle paramBundle);
  }
  
  public static abstract interface OnFullScreenRequestListener
  {
    public abstract void onFullScreenRequest(View paramView, boolean paramBoolean);
  }
  
  @VisibleForTesting
  public static abstract interface OnViewTypeChangedListener
  {
    public abstract void onViewTypeChanged(View paramView, int paramInt);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ViewType {}
}
