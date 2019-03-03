package android.media;

import android.media.update.ApiLoader;
import android.media.update.MediaItem2Provider;
import android.media.update.MediaItem2Provider.BuilderProvider;
import android.media.update.StaticProvider;
import android.os.Bundle;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MediaItem2
{
  public static final int FLAG_BROWSABLE = 1;
  public static final int FLAG_PLAYABLE = 2;
  private final MediaItem2Provider mProvider;
  
  public MediaItem2(MediaItem2Provider paramMediaItem2Provider)
  {
    mProvider = paramMediaItem2Provider;
  }
  
  public static MediaItem2 fromBundle(Bundle paramBundle)
  {
    return ApiLoader.getProvider().fromBundle_MediaItem2(paramBundle);
  }
  
  public boolean equals(Object paramObject)
  {
    return mProvider.equals_impl(paramObject);
  }
  
  public DataSourceDesc getDataSourceDesc()
  {
    return mProvider.getDataSourceDesc_impl();
  }
  
  public int getFlags()
  {
    return mProvider.getFlags_impl();
  }
  
  public String getMediaId()
  {
    return mProvider.getMediaId_impl();
  }
  
  public MediaMetadata2 getMetadata()
  {
    return mProvider.getMetadata_impl();
  }
  
  public MediaItem2Provider getProvider()
  {
    return mProvider;
  }
  
  public boolean isBrowsable()
  {
    return mProvider.isBrowsable_impl();
  }
  
  public boolean isPlayable()
  {
    return mProvider.isPlayable_impl();
  }
  
  public void setMetadata(MediaMetadata2 paramMediaMetadata2)
  {
    mProvider.setMetadata_impl(paramMediaMetadata2);
  }
  
  public Bundle toBundle()
  {
    return mProvider.toBundle_impl();
  }
  
  public String toString()
  {
    return mProvider.toString_impl();
  }
  
  public static final class Builder
  {
    private final MediaItem2Provider.BuilderProvider mProvider;
    
    public Builder(int paramInt)
    {
      mProvider = ApiLoader.getProvider().createMediaItem2Builder(this, paramInt);
    }
    
    public MediaItem2 build()
    {
      return mProvider.build_impl();
    }
    
    public Builder setDataSourceDesc(DataSourceDesc paramDataSourceDesc)
    {
      return mProvider.setDataSourceDesc_impl(paramDataSourceDesc);
    }
    
    public Builder setMediaId(String paramString)
    {
      return mProvider.setMediaId_impl(paramString);
    }
    
    public Builder setMetadata(MediaMetadata2 paramMediaMetadata2)
    {
      return mProvider.setMetadata_impl(paramMediaMetadata2);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Flags {}
}
