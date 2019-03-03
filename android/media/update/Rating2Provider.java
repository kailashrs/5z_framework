package android.media.update;

import android.os.Bundle;

public abstract interface Rating2Provider
{
  public abstract boolean equals_impl(Object paramObject);
  
  public abstract float getPercentRating_impl();
  
  public abstract int getRatingStyle_impl();
  
  public abstract float getStarRating_impl();
  
  public abstract boolean hasHeart_impl();
  
  public abstract int hashCode_impl();
  
  public abstract boolean isRated_impl();
  
  public abstract boolean isThumbUp_impl();
  
  public abstract Bundle toBundle_impl();
  
  public abstract String toString_impl();
}
