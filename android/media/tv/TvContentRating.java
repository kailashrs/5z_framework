package android.media.tv;

import android.text.TextUtils;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class TvContentRating
{
  private static final String DELIMITER = "/";
  public static final TvContentRating UNRATED = new TvContentRating("null", "null", "null", null);
  private final String mDomain;
  private final int mHashCode;
  private final String mRating;
  private final String mRatingSystem;
  private final String[] mSubRatings;
  
  private TvContentRating(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString)
  {
    mDomain = paramString1;
    mRatingSystem = paramString2;
    mRating = paramString3;
    if ((paramArrayOfString != null) && (paramArrayOfString.length != 0))
    {
      Arrays.sort(paramArrayOfString);
      mSubRatings = paramArrayOfString;
    }
    else
    {
      mSubRatings = null;
    }
    mHashCode = (31 * Objects.hash(new Object[] { mDomain, mRating }) + Arrays.hashCode(mSubRatings));
  }
  
  public static TvContentRating createRating(String paramString1, String paramString2, String paramString3, String... paramVarArgs)
  {
    if (!TextUtils.isEmpty(paramString1))
    {
      if (!TextUtils.isEmpty(paramString2))
      {
        if (!TextUtils.isEmpty(paramString3)) {
          return new TvContentRating(paramString1, paramString2, paramString3, paramVarArgs);
        }
        throw new IllegalArgumentException("rating cannot be empty");
      }
      throw new IllegalArgumentException("ratingSystem cannot be empty");
    }
    throw new IllegalArgumentException("domain cannot be empty");
  }
  
  public static TvContentRating unflattenFromString(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      Object localObject = paramString.split("/");
      if (localObject.length >= 3)
      {
        if (localObject.length > 3)
        {
          paramString = new String[localObject.length - 3];
          System.arraycopy(localObject, 3, paramString, 0, paramString.length);
          return new TvContentRating(localObject[0], localObject[1], localObject[2], paramString);
        }
        return new TvContentRating(localObject[0], localObject[1], localObject[2], null);
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Invalid rating string: ");
      ((StringBuilder)localObject).append(paramString);
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    throw new IllegalArgumentException("ratingString cannot be empty");
  }
  
  public final boolean contains(TvContentRating paramTvContentRating)
  {
    Preconditions.checkNotNull(paramTvContentRating);
    if (!paramTvContentRating.getMainRating().equals(mRating)) {
      return false;
    }
    if ((paramTvContentRating.getDomain().equals(mDomain)) && (paramTvContentRating.getRatingSystem().equals(mRatingSystem)) && (paramTvContentRating.getMainRating().equals(mRating)))
    {
      List localList = getSubRatings();
      paramTvContentRating = paramTvContentRating.getSubRatings();
      if ((localList == null) && (paramTvContentRating == null)) {
        return true;
      }
      if ((localList == null) && (paramTvContentRating != null)) {
        return false;
      }
      if ((localList != null) && (paramTvContentRating == null)) {
        return true;
      }
      return localList.containsAll(paramTvContentRating);
    }
    return false;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof TvContentRating)) {
      return false;
    }
    paramObject = (TvContentRating)paramObject;
    if (mHashCode != mHashCode) {
      return false;
    }
    if (!TextUtils.equals(mDomain, mDomain)) {
      return false;
    }
    if (!TextUtils.equals(mRatingSystem, mRatingSystem)) {
      return false;
    }
    if (!TextUtils.equals(mRating, mRating)) {
      return false;
    }
    return Arrays.equals(mSubRatings, mSubRatings);
  }
  
  public String flattenToString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mDomain);
    localStringBuilder.append("/");
    localStringBuilder.append(mRatingSystem);
    localStringBuilder.append("/");
    localStringBuilder.append(mRating);
    if (mSubRatings != null) {
      for (String str : mSubRatings)
      {
        localStringBuilder.append("/");
        localStringBuilder.append(str);
      }
    }
    return localStringBuilder.toString();
  }
  
  public String getDomain()
  {
    return mDomain;
  }
  
  public String getMainRating()
  {
    return mRating;
  }
  
  public String getRatingSystem()
  {
    return mRatingSystem;
  }
  
  public List<String> getSubRatings()
  {
    if (mSubRatings == null) {
      return null;
    }
    return Collections.unmodifiableList(Arrays.asList(mSubRatings));
  }
  
  public int hashCode()
  {
    return mHashCode;
  }
}
