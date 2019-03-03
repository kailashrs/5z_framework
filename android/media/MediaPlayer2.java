package android.media;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Handler;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;

public abstract class MediaPlayer2
  extends MediaPlayerBase
  implements SubtitleController.Listener, AudioRouting
{
  public static final boolean APPLY_METADATA_FILTER = true;
  public static final boolean BYPASS_METADATA_FILTER = false;
  public static final int CALL_COMPLETED_ATTACH_AUX_EFFECT = 1;
  public static final int CALL_COMPLETED_DESELECT_TRACK = 2;
  public static final int CALL_COMPLETED_LOOP_CURRENT = 3;
  public static final int CALL_COMPLETED_NOTIFY_WHEN_COMMAND_LABEL_REACHED = 1003;
  public static final int CALL_COMPLETED_PAUSE = 4;
  public static final int CALL_COMPLETED_PLAY = 5;
  public static final int CALL_COMPLETED_PREPARE = 6;
  public static final int CALL_COMPLETED_RELEASE_DRM = 12;
  public static final int CALL_COMPLETED_RESTORE_DRM_KEYS = 13;
  public static final int CALL_COMPLETED_SEEK_TO = 14;
  public static final int CALL_COMPLETED_SELECT_TRACK = 15;
  public static final int CALL_COMPLETED_SET_AUDIO_ATTRIBUTES = 16;
  public static final int CALL_COMPLETED_SET_AUDIO_SESSION_ID = 17;
  public static final int CALL_COMPLETED_SET_AUX_EFFECT_SEND_LEVEL = 18;
  public static final int CALL_COMPLETED_SET_BUFFERING_PARAMS = 1001;
  public static final int CALL_COMPLETED_SET_DATA_SOURCE = 19;
  public static final int CALL_COMPLETED_SET_NEXT_DATA_SOURCE = 22;
  public static final int CALL_COMPLETED_SET_NEXT_DATA_SOURCES = 23;
  public static final int CALL_COMPLETED_SET_PLAYBACK_PARAMS = 24;
  public static final int CALL_COMPLETED_SET_PLAYBACK_SPEED = 25;
  public static final int CALL_COMPLETED_SET_PLAYER_VOLUME = 26;
  public static final int CALL_COMPLETED_SET_SURFACE = 27;
  public static final int CALL_COMPLETED_SET_SYNC_PARAMS = 28;
  public static final int CALL_COMPLETED_SET_VIDEO_SCALING_MODE = 1002;
  public static final int CALL_COMPLETED_SKIP_TO_NEXT = 29;
  public static final int CALL_STATUS_BAD_VALUE = 2;
  public static final int CALL_STATUS_ERROR_IO = 4;
  public static final int CALL_STATUS_ERROR_UNKNOWN = Integer.MIN_VALUE;
  public static final int CALL_STATUS_INVALID_OPERATION = 1;
  public static final int CALL_STATUS_NO_DRM_SCHEME = 5;
  public static final int CALL_STATUS_NO_ERROR = 0;
  public static final int CALL_STATUS_PERMISSION_DENIED = 3;
  public static final int MEDIAPLAYER2_STATE_ERROR = 5;
  public static final int MEDIAPLAYER2_STATE_IDLE = 1;
  public static final int MEDIAPLAYER2_STATE_PAUSED = 3;
  public static final int MEDIAPLAYER2_STATE_PLAYING = 4;
  public static final int MEDIAPLAYER2_STATE_PREPARED = 2;
  public static final int MEDIA_ERROR_IO = -1004;
  public static final int MEDIA_ERROR_MALFORMED = -1007;
  public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
  public static final int MEDIA_ERROR_SYSTEM = Integer.MIN_VALUE;
  public static final int MEDIA_ERROR_TIMED_OUT = -110;
  public static final int MEDIA_ERROR_UNKNOWN = 1;
  public static final int MEDIA_ERROR_UNSUPPORTED = -1010;
  public static final int MEDIA_INFO_AUDIO_NOT_PLAYING = 804;
  public static final int MEDIA_INFO_AUDIO_RENDERING_START = 4;
  public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;
  public static final int MEDIA_INFO_BUFFERING_END = 702;
  public static final int MEDIA_INFO_BUFFERING_START = 701;
  public static final int MEDIA_INFO_BUFFERING_UPDATE = 704;
  public static final int MEDIA_INFO_EXTERNAL_METADATA_UPDATE = 803;
  public static final int MEDIA_INFO_METADATA_UPDATE = 802;
  public static final int MEDIA_INFO_NETWORK_BANDWIDTH = 703;
  public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
  public static final int MEDIA_INFO_PLAYBACK_COMPLETE = 5;
  public static final int MEDIA_INFO_PLAYLIST_END = 6;
  public static final int MEDIA_INFO_PREPARED = 100;
  public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;
  public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;
  public static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
  public static final int MEDIA_INFO_UNKNOWN = 1;
  public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;
  public static final int MEDIA_INFO_VIDEO_NOT_PLAYING = 805;
  public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
  public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;
  public static final String MEDIA_MIMETYPE_TEXT_CEA_608 = "text/cea-608";
  public static final String MEDIA_MIMETYPE_TEXT_CEA_708 = "text/cea-708";
  public static final String MEDIA_MIMETYPE_TEXT_SUBRIP = "application/x-subrip";
  public static final String MEDIA_MIMETYPE_TEXT_VTT = "text/vtt";
  public static final boolean METADATA_ALL = false;
  public static final boolean METADATA_UPDATE_ONLY = true;
  public static final int PLAYBACK_RATE_AUDIO_MODE_DEFAULT = 0;
  public static final int PLAYBACK_RATE_AUDIO_MODE_RESAMPLE = 2;
  public static final int PLAYBACK_RATE_AUDIO_MODE_STRETCH = 1;
  public static final int PREPARE_DRM_STATUS_PREPARATION_ERROR = 3;
  public static final int PREPARE_DRM_STATUS_PROVISIONING_NETWORK_ERROR = 1;
  public static final int PREPARE_DRM_STATUS_PROVISIONING_SERVER_ERROR = 2;
  public static final int PREPARE_DRM_STATUS_SUCCESS = 0;
  public static final int SEEK_CLOSEST = 3;
  public static final int SEEK_CLOSEST_SYNC = 2;
  public static final int SEEK_NEXT_SYNC = 1;
  public static final int SEEK_PREVIOUS_SYNC = 0;
  public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;
  public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;
  
  public MediaPlayer2() {}
  
  public static final MediaPlayer2 create()
  {
    return new MediaPlayer2Impl();
  }
  
  private static final String[] decodeMediaPlayer2Uri(String paramString)
  {
    Object localObject = Uri.parse(paramString);
    boolean bool = "mediaplayer2".equals(((Uri)localObject).getScheme());
    int i = 0;
    if (!bool) {
      return new String[] { paramString };
    }
    List localList1 = ((Uri)localObject).getQueryParameters("uri");
    if (localList1.isEmpty()) {
      return new String[] { paramString };
    }
    paramString = ((Uri)localObject).getQueryParameters("key");
    List localList2 = ((Uri)localObject).getQueryParameters("value");
    if (paramString.size() != localList2.size()) {
      return new String[] { (String)localList1.get(0) };
    }
    localObject = new ArrayList();
    ((List)localObject).add((String)localList1.get(0));
    while (i < paramString.size())
    {
      ((List)localObject).add((String)paramString.get(i));
      ((List)localObject).add((String)localList2.get(i));
      i++;
    }
    return (String[])((List)localObject).toArray(new String[((List)localObject).size()]);
  }
  
  private static final String encodeMediaPlayer2Uri(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    Uri.Builder localBuilder = new Uri.Builder();
    localBuilder.scheme("mediaplayer2").path("/").appendQueryParameter("uri", paramString);
    if ((paramArrayOfString1 != null) && (paramArrayOfString2 != null) && (paramArrayOfString1.length == paramArrayOfString2.length))
    {
      for (int i = 0; i < paramArrayOfString1.length; i++) {
        localBuilder.appendQueryParameter("key", paramArrayOfString1[i]).appendQueryParameter("value", paramArrayOfString2[i]);
      }
      return localBuilder.build().toString();
    }
    return localBuilder.build().toString();
  }
  
  public abstract void addOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener paramOnRoutingChangedListener, Handler paramHandler);
  
  public void addSubtitleSource(InputStream paramInputStream, MediaFormat paramMediaFormat) {}
  
  public void addTimedTextSource(Context paramContext, Uri paramUri, String paramString)
    throws IOException
  {}
  
  public abstract void addTimedTextSource(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2, String paramString);
  
  public void addTimedTextSource(FileDescriptor paramFileDescriptor, String paramString) {}
  
  public void addTimedTextSource(String paramString1, String paramString2)
    throws IOException
  {}
  
  public abstract void attachAuxEffect(int paramInt);
  
  public abstract void clearDrmEventCallback();
  
  public abstract void clearMediaPlayer2EventCallback();
  
  public abstract void clearPendingCommands();
  
  public abstract void close();
  
  public abstract void deselectTrack(int paramInt);
  
  public PlaybackParams easyPlaybackParams(float paramFloat, int paramInt)
  {
    return new PlaybackParams();
  }
  
  public abstract AudioAttributes getAudioAttributes();
  
  public abstract int getAudioSessionId();
  
  public abstract long getBufferedPosition();
  
  public BufferingParams getBufferingParams()
  {
    return new BufferingParams.Builder().build();
  }
  
  public abstract int getBufferingState();
  
  public abstract DataSourceDesc getCurrentDataSource();
  
  public abstract long getCurrentPosition();
  
  public abstract DrmInfo getDrmInfo();
  
  public abstract MediaDrm.KeyRequest getDrmKeyRequest(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, String paramString, int paramInt, Map<String, String> paramMap)
    throws MediaPlayer2.NoDrmSchemeException;
  
  public abstract String getDrmPropertyString(String paramString)
    throws MediaPlayer2.NoDrmSchemeException;
  
  public abstract long getDuration();
  
  public float getMaxPlayerVolume()
  {
    return 1.0F;
  }
  
  public abstract int getMediaPlayer2State();
  
  public MediaTimeProvider getMediaTimeProvider()
  {
    return null;
  }
  
  public Metadata getMetadata(boolean paramBoolean1, boolean paramBoolean2)
  {
    return null;
  }
  
  public abstract PersistableBundle getMetrics();
  
  public abstract PlaybackParams getPlaybackParams();
  
  public float getPlaybackSpeed()
  {
    return 1.0F;
  }
  
  public abstract int getPlayerState();
  
  public abstract float getPlayerVolume();
  
  public abstract AudioDeviceInfo getPreferredDevice();
  
  public abstract AudioDeviceInfo getRoutedDevice();
  
  public abstract int getSelectedTrack(int paramInt);
  
  public abstract SyncParams getSyncParams();
  
  public abstract MediaTimestamp getTimestamp();
  
  public abstract List<TrackInfo> getTrackInfo();
  
  public abstract int getVideoHeight();
  
  public abstract int getVideoWidth();
  
  public void invoke(Parcel paramParcel1, Parcel paramParcel2) {}
  
  public boolean isLooping()
  {
    return false;
  }
  
  public abstract boolean isPlaying();
  
  public boolean isReversePlaybackSupported()
  {
    return false;
  }
  
  public abstract void loopCurrent(boolean paramBoolean);
  
  public Parcel newRequest()
  {
    return null;
  }
  
  public void notifyAt(long paramLong) {}
  
  public void notifyWhenCommandLabelReached(Object paramObject) {}
  
  public void onSubtitleTrackSelected(SubtitleTrack paramSubtitleTrack) {}
  
  public abstract void pause();
  
  public abstract void play();
  
  public abstract void prepare();
  
  public abstract void prepareDrm(UUID paramUUID)
    throws UnsupportedSchemeException, ResourceBusyException, MediaPlayer2.ProvisioningNetworkErrorException, MediaPlayer2.ProvisioningServerErrorException;
  
  public abstract byte[] provideDrmKeyResponse(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws MediaPlayer2.NoDrmSchemeException, DeniedByServerException;
  
  public abstract void registerPlayerEventCallback(Executor paramExecutor, MediaPlayerBase.PlayerEventCallback paramPlayerEventCallback);
  
  public abstract void releaseDrm()
    throws MediaPlayer2.NoDrmSchemeException;
  
  public abstract void removeOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener paramOnRoutingChangedListener);
  
  public abstract void reset();
  
  public abstract void restoreDrmKeys(byte[] paramArrayOfByte)
    throws MediaPlayer2.NoDrmSchemeException;
  
  public void seekTo(long paramLong)
  {
    seekTo(paramLong, 0);
  }
  
  public abstract void seekTo(long paramLong, int paramInt);
  
  public abstract void selectTrack(int paramInt);
  
  public abstract void setAudioAttributes(AudioAttributes paramAudioAttributes);
  
  public abstract void setAudioSessionId(int paramInt);
  
  public abstract void setAuxEffectSendLevel(float paramFloat);
  
  public void setBufferingParams(BufferingParams paramBufferingParams) {}
  
  public abstract void setDataSource(DataSourceDesc paramDataSourceDesc);
  
  public abstract void setDisplay(SurfaceHolder paramSurfaceHolder);
  
  public abstract void setDrmEventCallback(Executor paramExecutor, DrmEventCallback paramDrmEventCallback);
  
  public abstract void setDrmPropertyString(String paramString1, String paramString2)
    throws MediaPlayer2.NoDrmSchemeException;
  
  public abstract void setMediaPlayer2EventCallback(Executor paramExecutor, MediaPlayer2EventCallback paramMediaPlayer2EventCallback);
  
  public int setMetadataFilter(Set<Integer> paramSet1, Set<Integer> paramSet2)
  {
    return 0;
  }
  
  public abstract void setNextDataSource(DataSourceDesc paramDataSourceDesc);
  
  public abstract void setNextDataSources(List<DataSourceDesc> paramList);
  
  public abstract void setOnDrmConfigHelper(OnDrmConfigHelper paramOnDrmConfigHelper);
  
  public void setOnSubtitleDataListener(OnSubtitleDataListener paramOnSubtitleDataListener) {}
  
  public abstract void setPlaybackParams(PlaybackParams paramPlaybackParams);
  
  public abstract void setPlaybackSpeed(float paramFloat);
  
  public abstract void setPlayerVolume(float paramFloat);
  
  public abstract boolean setPreferredDevice(AudioDeviceInfo paramAudioDeviceInfo);
  
  public abstract void setScreenOnWhilePlaying(boolean paramBoolean);
  
  public void setSubtitleAnchor(SubtitleController paramSubtitleController, SubtitleController.Anchor paramAnchor) {}
  
  public abstract void setSurface(Surface paramSurface);
  
  public abstract void setSyncParams(SyncParams paramSyncParams);
  
  public void setVideoScalingMode(int paramInt) {}
  
  public abstract void setWakeMode(Context paramContext, int paramInt);
  
  public abstract void skipToNext();
  
  public void stop() {}
  
  public abstract void unregisterPlayerEventCallback(MediaPlayerBase.PlayerEventCallback paramPlayerEventCallback);
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CallCompleted {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CallStatus {}
  
  public static abstract class DrmEventCallback
  {
    public DrmEventCallback() {}
    
    public void onDrmInfo(MediaPlayer2 paramMediaPlayer2, DataSourceDesc paramDataSourceDesc, MediaPlayer2.DrmInfo paramDrmInfo) {}
    
    public void onDrmPrepared(MediaPlayer2 paramMediaPlayer2, DataSourceDesc paramDataSourceDesc, int paramInt) {}
  }
  
  public static abstract class DrmInfo
  {
    public DrmInfo() {}
    
    public abstract Map<UUID, byte[]> getPssh();
    
    public abstract List<UUID> getSupportedSchemes();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MediaError {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MediaInfo {}
  
  public static abstract class MediaPlayer2EventCallback
  {
    public MediaPlayer2EventCallback() {}
    
    public void onCallCompleted(MediaPlayer2 paramMediaPlayer2, DataSourceDesc paramDataSourceDesc, int paramInt1, int paramInt2) {}
    
    public void onCommandLabelReached(MediaPlayer2 paramMediaPlayer2, Object paramObject) {}
    
    public void onError(MediaPlayer2 paramMediaPlayer2, DataSourceDesc paramDataSourceDesc, int paramInt1, int paramInt2) {}
    
    public void onInfo(MediaPlayer2 paramMediaPlayer2, DataSourceDesc paramDataSourceDesc, int paramInt1, int paramInt2) {}
    
    public void onMediaTimeChanged(MediaPlayer2 paramMediaPlayer2, DataSourceDesc paramDataSourceDesc, MediaTimestamp paramMediaTimestamp) {}
    
    public void onTimedMetaDataAvailable(MediaPlayer2 paramMediaPlayer2, DataSourceDesc paramDataSourceDesc, TimedMetaData paramTimedMetaData) {}
    
    public void onTimedText(MediaPlayer2 paramMediaPlayer2, DataSourceDesc paramDataSourceDesc, TimedText paramTimedText) {}
    
    public void onVideoSizeChanged(MediaPlayer2 paramMediaPlayer2, DataSourceDesc paramDataSourceDesc, int paramInt1, int paramInt2) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MediaPlayer2State {}
  
  public static final class MetricsConstants
  {
    public static final String CODEC_AUDIO = "android.media.mediaplayer.audio.codec";
    public static final String CODEC_VIDEO = "android.media.mediaplayer.video.codec";
    public static final String DURATION = "android.media.mediaplayer.durationMs";
    public static final String ERRORS = "android.media.mediaplayer.err";
    public static final String ERROR_CODE = "android.media.mediaplayer.errcode";
    public static final String FRAMES = "android.media.mediaplayer.frames";
    public static final String FRAMES_DROPPED = "android.media.mediaplayer.dropped";
    public static final String HEIGHT = "android.media.mediaplayer.height";
    public static final String MIME_TYPE_AUDIO = "android.media.mediaplayer.audio.mime";
    public static final String MIME_TYPE_VIDEO = "android.media.mediaplayer.video.mime";
    public static final String PLAYING = "android.media.mediaplayer.playingMs";
    public static final String WIDTH = "android.media.mediaplayer.width";
    
    private MetricsConstants() {}
  }
  
  public static abstract class NoDrmSchemeException
    extends MediaDrmException
  {
    protected NoDrmSchemeException(String paramString)
    {
      super();
    }
  }
  
  public static abstract interface OnDrmConfigHelper
  {
    public abstract void onDrmConfig(MediaPlayer2 paramMediaPlayer2, DataSourceDesc paramDataSourceDesc);
  }
  
  public static abstract interface OnSubtitleDataListener
  {
    public abstract void onSubtitleData(MediaPlayer2 paramMediaPlayer2, SubtitleData paramSubtitleData);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PlaybackRateAudioMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PrepareDrmStatusCode {}
  
  public static abstract class ProvisioningNetworkErrorException
    extends MediaDrmException
  {
    protected ProvisioningNetworkErrorException(String paramString)
    {
      super();
    }
  }
  
  public static abstract class ProvisioningServerErrorException
    extends MediaDrmException
  {
    protected ProvisioningServerErrorException(String paramString)
    {
      super();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SeekMode {}
  
  public static abstract class TrackInfo
  {
    public static final int MEDIA_TRACK_TYPE_AUDIO = 2;
    public static final int MEDIA_TRACK_TYPE_METADATA = 5;
    public static final int MEDIA_TRACK_TYPE_SUBTITLE = 4;
    public static final int MEDIA_TRACK_TYPE_TIMEDTEXT = 3;
    public static final int MEDIA_TRACK_TYPE_UNKNOWN = 0;
    public static final int MEDIA_TRACK_TYPE_VIDEO = 1;
    
    public TrackInfo() {}
    
    public abstract MediaFormat getFormat();
    
    public abstract String getLanguage();
    
    public abstract int getTrackType();
    
    public abstract String toString();
  }
}
