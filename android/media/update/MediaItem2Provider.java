package android.media.update;

import android.media.DataSourceDesc;
import android.media.MediaItem2;
import android.media.MediaItem2.Builder;
import android.media.MediaMetadata2;
import android.os.Bundle;

public abstract interface MediaItem2Provider
{
  public abstract boolean equals_impl(Object paramObject);
  
  public abstract DataSourceDesc getDataSourceDesc_impl();
  
  public abstract int getFlags_impl();
  
  public abstract String getMediaId_impl();
  
  public abstract MediaMetadata2 getMetadata_impl();
  
  public abstract boolean isBrowsable_impl();
  
  public abstract boolean isPlayable_impl();
  
  public abstract void setMetadata_impl(MediaMetadata2 paramMediaMetadata2);
  
  public abstract Bundle toBundle_impl();
  
  public abstract String toString_impl();
  
  public static abstract interface BuilderProvider
  {
    public abstract MediaItem2 build_impl();
    
    public abstract MediaItem2.Builder setDataSourceDesc_impl(DataSourceDesc paramDataSourceDesc);
    
    public abstract MediaItem2.Builder setMediaId_impl(String paramString);
    
    public abstract MediaItem2.Builder setMetadata_impl(MediaMetadata2 paramMediaMetadata2);
  }
}
