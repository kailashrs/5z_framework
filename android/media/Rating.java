package android.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Rating
  implements Parcelable
{
  public static final Parcelable.Creator<Rating> CREATOR = new Parcelable.Creator()
  {
    public Rating createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Rating(paramAnonymousParcel.readInt(), paramAnonymousParcel.readFloat(), null);
    }
    
    public Rating[] newArray(int paramAnonymousInt)
    {
      return new Rating[paramAnonymousInt];
    }
  };
  public static final int RATING_3_STARS = 3;
  public static final int RATING_4_STARS = 4;
  public static final int RATING_5_STARS = 5;
  public static final int RATING_HEART = 1;
  public static final int RATING_NONE = 0;
  private static final float RATING_NOT_RATED = -1.0F;
  public static final int RATING_PERCENTAGE = 6;
  public static final int RATING_THUMB_UP_DOWN = 2;
  private static final String TAG = "Rating";
  private final int mRatingStyle;
  private final float mRatingValue;
  
  private Rating(int paramInt, float paramFloat)
  {
    mRatingStyle = paramInt;
    mRatingValue = paramFloat;
  }
  
  public static Rating newHeartRating(boolean paramBoolean)
  {
    float f;
    if (paramBoolean) {
      f = 1.0F;
    } else {
      f = 0.0F;
    }
    return new Rating(1, f);
  }
  
  public static Rating newPercentageRating(float paramFloat)
  {
    if ((paramFloat >= 0.0F) && (paramFloat <= 100.0F)) {
      return new Rating(6, paramFloat);
    }
    Log.e("Rating", "Invalid percentage-based rating value");
    return null;
  }
  
  public static Rating newStarRating(int paramInt, float paramFloat)
  {
    float f;
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid rating style (");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(") for a star rating");
      Log.e("Rating", localStringBuilder.toString());
      return null;
    case 5: 
      f = 5.0F;
      break;
    case 4: 
      f = 4.0F;
      break;
    case 3: 
      f = 3.0F;
    }
    if ((paramFloat >= 0.0F) && (paramFloat <= f)) {
      return new Rating(paramInt, paramFloat);
    }
    Log.e("Rating", "Trying to set out of range star-based rating");
    return null;
  }
  
  public static Rating newThumbRating(boolean paramBoolean)
  {
    float f;
    if (paramBoolean) {
      f = 1.0F;
    } else {
      f = 0.0F;
    }
    return new Rating(2, f);
  }
  
  public static Rating newUnratedRating(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    }
    return new Rating(paramInt, -1.0F);
  }
  
  public int describeContents()
  {
    return mRatingStyle;
  }
  
  public float getPercentRating()
  {
    if ((mRatingStyle == 6) && (isRated())) {
      return mRatingValue;
    }
    return -1.0F;
  }
  
  public int getRatingStyle()
  {
    return mRatingStyle;
  }
  
  public float getStarRating()
  {
    switch (mRatingStyle)
    {
    default: 
      break;
    case 3: 
    case 4: 
    case 5: 
      if (isRated()) {
        return mRatingValue;
      }
      break;
    }
    return -1.0F;
  }
  
  public boolean hasHeart()
  {
    int i = mRatingStyle;
    boolean bool = false;
    if (i != 1) {
      return false;
    }
    if (mRatingValue == 1.0F) {
      bool = true;
    }
    return bool;
  }
  
  public boolean isRated()
  {
    boolean bool;
    if (mRatingValue >= 0.0F) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isThumbUp()
  {
    int i = mRatingStyle;
    boolean bool = false;
    if (i != 2) {
      return false;
    }
    if (mRatingValue == 1.0F) {
      bool = true;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Rating:style=");
    localStringBuilder.append(mRatingStyle);
    localStringBuilder.append(" rating=");
    String str;
    if (mRatingValue < 0.0F) {
      str = "unrated";
    } else {
      str = String.valueOf(mRatingValue);
    }
    localStringBuilder.append(str);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRatingStyle);
    paramParcel.writeFloat(mRatingValue);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface StarStyle {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Style {}
}
