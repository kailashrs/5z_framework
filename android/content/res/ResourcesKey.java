package android.content.res;

import android.text.TextUtils;
import java.util.Arrays;
import java.util.Objects;

public final class ResourcesKey
{
  public final CompatibilityInfo mCompatInfo;
  public final int mDisplayId;
  private final int mHash;
  public final String[] mLibDirs;
  public final String[] mOverlayDirs;
  public final Configuration mOverrideConfiguration;
  public final String mResDir;
  public final String[] mSplitResDirs;
  
  public ResourcesKey(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, int paramInt, Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo)
  {
    mResDir = paramString;
    mSplitResDirs = paramArrayOfString1;
    mOverlayDirs = paramArrayOfString2;
    mLibDirs = paramArrayOfString3;
    mDisplayId = paramInt;
    if (paramConfiguration != null) {
      paramString = paramConfiguration;
    } else {
      paramString = Configuration.EMPTY;
    }
    mOverrideConfiguration = new Configuration(paramString);
    if (paramCompatibilityInfo == null) {
      paramCompatibilityInfo = CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
    }
    mCompatInfo = paramCompatibilityInfo;
    mHash = (31 * (31 * (31 * (31 * (31 * (31 * (31 * 17 + Objects.hashCode(mResDir)) + Arrays.hashCode(mSplitResDirs)) + Arrays.hashCode(mOverlayDirs)) + Arrays.hashCode(mLibDirs)) + mDisplayId) + Objects.hashCode(mOverrideConfiguration)) + Objects.hashCode(mCompatInfo));
  }
  
  private static boolean anyStartsWith(String[] paramArrayOfString, String paramString)
  {
    if (paramArrayOfString != null)
    {
      int i = paramArrayOfString.length;
      for (int j = 0; j < i; j++)
      {
        String str = paramArrayOfString[j];
        if ((str != null) && (str.startsWith(paramString))) {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof ResourcesKey)) {
      return false;
    }
    paramObject = (ResourcesKey)paramObject;
    if (mHash != mHash) {
      return false;
    }
    if (!Objects.equals(mResDir, mResDir)) {
      return false;
    }
    if (!Arrays.equals(mSplitResDirs, mSplitResDirs)) {
      return false;
    }
    if (!Arrays.equals(mOverlayDirs, mOverlayDirs)) {
      return false;
    }
    if (!Arrays.equals(mLibDirs, mLibDirs)) {
      return false;
    }
    if (mDisplayId != mDisplayId) {
      return false;
    }
    if (!Objects.equals(mOverrideConfiguration, mOverrideConfiguration)) {
      return false;
    }
    return Objects.equals(mCompatInfo, mCompatInfo);
  }
  
  public boolean hasOverrideConfiguration()
  {
    return Configuration.EMPTY.equals(mOverrideConfiguration) ^ true;
  }
  
  public int hashCode()
  {
    return mHash;
  }
  
  public boolean isPathReferenced(String paramString)
  {
    String str = mResDir;
    boolean bool1 = true;
    if ((str != null) && (mResDir.startsWith(paramString))) {
      return true;
    }
    boolean bool2 = bool1;
    if (!anyStartsWith(mSplitResDirs, paramString))
    {
      bool2 = bool1;
      if (!anyStartsWith(mOverlayDirs, paramString)) {
        if (anyStartsWith(mLibDirs, paramString)) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
    }
    return bool2;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder().append("ResourcesKey{");
    localStringBuilder.append(" mHash=");
    localStringBuilder.append(Integer.toHexString(mHash));
    localStringBuilder.append(" mResDir=");
    localStringBuilder.append(mResDir);
    localStringBuilder.append(" mSplitDirs=[");
    if (mSplitResDirs != null) {
      localStringBuilder.append(TextUtils.join(",", mSplitResDirs));
    }
    localStringBuilder.append("]");
    localStringBuilder.append(" mOverlayDirs=[");
    if (mOverlayDirs != null) {
      localStringBuilder.append(TextUtils.join(",", mOverlayDirs));
    }
    localStringBuilder.append("]");
    localStringBuilder.append(" mLibDirs=[");
    if (mLibDirs != null) {
      localStringBuilder.append(TextUtils.join(",", mLibDirs));
    }
    localStringBuilder.append("]");
    localStringBuilder.append(" mDisplayId=");
    localStringBuilder.append(mDisplayId);
    localStringBuilder.append(" mOverrideConfig=");
    localStringBuilder.append(Configuration.resourceQualifierString(mOverrideConfiguration));
    localStringBuilder.append(" mCompatInfo=");
    localStringBuilder.append(mCompatInfo);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
