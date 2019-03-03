package android.media;

import android.media.update.ApiLoader;
import android.media.update.Rating2Provider;
import android.media.update.StaticProvider;
import android.os.Bundle;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Rating2
{
  public static final int RATING_3_STARS = 3;
  public static final int RATING_4_STARS = 4;
  public static final int RATING_5_STARS = 5;
  public static final int RATING_HEART = 1;
  public static final int RATING_NONE = 0;
  public static final int RATING_PERCENTAGE = 6;
  public static final int RATING_THUMB_UP_DOWN = 2;
  private final Rating2Provider mProvider;
  
  public Rating2(Rating2Provider paramRating2Provider)
  {
    mProvider = paramRating2Provider;
  }
  
  public static Rating2 fromBundle(Bundle paramBundle)
  {
    return ApiLoader.getProvider().fromBundle_Rating2(paramBundle);
  }
  
  public static Rating2 newHeartRating(boolean paramBoolean)
  {
    return ApiLoader.getProvider().newHeartRating_Rating2(paramBoolean);
  }
  
  public static Rating2 newPercentageRating(float paramFloat)
  {
    return ApiLoader.getProvider().newPercentageRating_Rating2(paramFloat);
  }
  
  public static Rating2 newStarRating(int paramInt, float paramFloat)
  {
    return ApiLoader.getProvider().newStarRating_Rating2(paramInt, paramFloat);
  }
  
  public static Rating2 newThumbRating(boolean paramBoolean)
  {
    return ApiLoader.getProvider().newThumbRating_Rating2(paramBoolean);
  }
  
  public static Rating2 newUnratedRating(int paramInt)
  {
    return ApiLoader.getProvider().newUnratedRating_Rating2(paramInt);
  }
  
  public boolean equals(Object paramObject)
  {
    return mProvider.equals_impl(paramObject);
  }
  
  public float getPercentRating()
  {
    return mProvider.getPercentRating_impl();
  }
  
  public Rating2Provider getProvider()
  {
    return mProvider;
  }
  
  public int getRatingStyle()
  {
    return mProvider.getRatingStyle_impl();
  }
  
  public float getStarRating()
  {
    return mProvider.getStarRating_impl();
  }
  
  public boolean hasHeart()
  {
    return mProvider.hasHeart_impl();
  }
  
  public int hashCode()
  {
    return mProvider.hashCode_impl();
  }
  
  public boolean isRated()
  {
    return mProvider.isRated_impl();
  }
  
  public boolean isThumbUp()
  {
    return mProvider.isThumbUp_impl();
  }
  
  public Bundle toBundle()
  {
    return mProvider.toBundle_impl();
  }
  
  public String toString()
  {
    return mProvider.toString_impl();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface StarStyle {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Style {}
}
