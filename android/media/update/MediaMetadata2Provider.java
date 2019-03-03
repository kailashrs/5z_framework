package android.media.update;

import android.graphics.Bitmap;
import android.media.MediaMetadata2;
import android.media.MediaMetadata2.Builder;
import android.media.Rating2;
import android.os.Bundle;
import java.util.Set;

public abstract interface MediaMetadata2Provider
{
  public abstract boolean containsKey_impl(String paramString);
  
  public abstract Bitmap getBitmap_impl(String paramString);
  
  public abstract Bundle getExtras_impl();
  
  public abstract float getFloat_impl(String paramString);
  
  public abstract long getLong_impl(String paramString);
  
  public abstract String getMediaId_impl();
  
  public abstract Rating2 getRating_impl(String paramString);
  
  public abstract String getString_impl(String paramString);
  
  public abstract CharSequence getText_impl(String paramString);
  
  public abstract Set<String> keySet_impl();
  
  public abstract int size_impl();
  
  public abstract Bundle toBundle_impl();
  
  public static abstract interface BuilderProvider
  {
    public abstract MediaMetadata2 build_impl();
    
    public abstract MediaMetadata2.Builder putBitmap_impl(String paramString, Bitmap paramBitmap);
    
    public abstract MediaMetadata2.Builder putFloat_impl(String paramString, float paramFloat);
    
    public abstract MediaMetadata2.Builder putLong_impl(String paramString, long paramLong);
    
    public abstract MediaMetadata2.Builder putRating_impl(String paramString, Rating2 paramRating2);
    
    public abstract MediaMetadata2.Builder putString_impl(String paramString1, String paramString2);
    
    public abstract MediaMetadata2.Builder putText_impl(String paramString, CharSequence paramCharSequence);
    
    public abstract MediaMetadata2.Builder setExtras_impl(Bundle paramBundle);
  }
}
