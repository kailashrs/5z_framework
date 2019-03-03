package android.media;

import android.graphics.Bitmap;
import android.media.update.ApiLoader;
import android.media.update.MediaMetadata2Provider;
import android.media.update.MediaMetadata2Provider.BuilderProvider;
import android.media.update.StaticProvider;
import android.os.Bundle;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

public final class MediaMetadata2
{
  public static final long BT_FOLDER_TYPE_ALBUMS = 2L;
  public static final long BT_FOLDER_TYPE_ARTISTS = 3L;
  public static final long BT_FOLDER_TYPE_GENRES = 4L;
  public static final long BT_FOLDER_TYPE_MIXED = 0L;
  public static final long BT_FOLDER_TYPE_PLAYLISTS = 5L;
  public static final long BT_FOLDER_TYPE_TITLES = 1L;
  public static final long BT_FOLDER_TYPE_YEARS = 6L;
  public static final String METADATA_KEY_ADVERTISEMENT = "android.media.metadata.ADVERTISEMENT";
  public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
  public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
  public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
  public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
  public static final String METADATA_KEY_ART = "android.media.metadata.ART";
  public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
  public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
  public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
  public static final String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";
  public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
  public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
  public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
  public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
  public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
  public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
  public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
  public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
  public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
  public static final String METADATA_KEY_DOWNLOAD_STATUS = "android.media.metadata.DOWNLOAD_STATUS";
  public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
  public static final String METADATA_KEY_EXTRAS = "android.media.metadata.EXTRAS";
  public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
  public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
  public static final String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";
  public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
  public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
  public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
  public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
  public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
  public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
  public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
  public static final long STATUS_DOWNLOADED = 2L;
  public static final long STATUS_DOWNLOADING = 1L;
  public static final long STATUS_NOT_DOWNLOADED = 0L;
  private final MediaMetadata2Provider mProvider;
  
  public MediaMetadata2(MediaMetadata2Provider paramMediaMetadata2Provider)
  {
    mProvider = paramMediaMetadata2Provider;
  }
  
  public static MediaMetadata2 fromBundle(Bundle paramBundle)
  {
    return ApiLoader.getProvider().fromBundle_MediaMetadata2(paramBundle);
  }
  
  public boolean containsKey(String paramString)
  {
    return mProvider.containsKey_impl(paramString);
  }
  
  public Bitmap getBitmap(String paramString)
  {
    return mProvider.getBitmap_impl(paramString);
  }
  
  public Bundle getExtras()
  {
    return mProvider.getExtras_impl();
  }
  
  public float getFloat(String paramString)
  {
    return mProvider.getFloat_impl(paramString);
  }
  
  public long getLong(String paramString)
  {
    return mProvider.getLong_impl(paramString);
  }
  
  public String getMediaId()
  {
    return mProvider.getMediaId_impl();
  }
  
  public Rating2 getRating(String paramString)
  {
    return mProvider.getRating_impl(paramString);
  }
  
  public String getString(String paramString)
  {
    return mProvider.getString_impl(paramString);
  }
  
  public CharSequence getText(String paramString)
  {
    return mProvider.getText_impl(paramString);
  }
  
  public Set<String> keySet()
  {
    return mProvider.keySet_impl();
  }
  
  public int size()
  {
    return mProvider.size_impl();
  }
  
  public Bundle toBundle()
  {
    return mProvider.toBundle_impl();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BitmapKey {}
  
  public static final class Builder
  {
    private final MediaMetadata2Provider.BuilderProvider mProvider;
    
    public Builder()
    {
      mProvider = ApiLoader.getProvider().createMediaMetadata2Builder(this);
    }
    
    public Builder(MediaMetadata2 paramMediaMetadata2)
    {
      mProvider = ApiLoader.getProvider().createMediaMetadata2Builder(this, paramMediaMetadata2);
    }
    
    public Builder(MediaMetadata2Provider.BuilderProvider paramBuilderProvider)
    {
      mProvider = paramBuilderProvider;
    }
    
    public MediaMetadata2 build()
    {
      return mProvider.build_impl();
    }
    
    public Builder putBitmap(String paramString, Bitmap paramBitmap)
    {
      return mProvider.putBitmap_impl(paramString, paramBitmap);
    }
    
    public Builder putFloat(String paramString, float paramFloat)
    {
      return mProvider.putFloat_impl(paramString, paramFloat);
    }
    
    public Builder putLong(String paramString, long paramLong)
    {
      return mProvider.putLong_impl(paramString, paramLong);
    }
    
    public Builder putRating(String paramString, Rating2 paramRating2)
    {
      return mProvider.putRating_impl(paramString, paramRating2);
    }
    
    public Builder putString(String paramString1, String paramString2)
    {
      return mProvider.putString_impl(paramString1, paramString2);
    }
    
    public Builder putText(String paramString, CharSequence paramCharSequence)
    {
      return mProvider.putText_impl(paramString, paramCharSequence);
    }
    
    public Builder setExtras(Bundle paramBundle)
    {
      return mProvider.setExtras_impl(paramBundle);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FloatKey {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LongKey {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RatingKey {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TextKey {}
}
