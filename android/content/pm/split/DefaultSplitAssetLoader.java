package android.content.pm.split;

import android.content.pm.PackageParser;
import android.content.pm.PackageParser.PackageLite;
import android.content.pm.PackageParser.PackageParserException;
import android.content.res.ApkAssets;
import android.content.res.AssetManager;
import android.os.Build.VERSION;
import com.android.internal.util.ArrayUtils;
import java.io.IOException;
import libcore.io.IoUtils;

public class DefaultSplitAssetLoader
  implements SplitAssetLoader
{
  private final String mBaseCodePath;
  private AssetManager mCachedAssetManager;
  private final int mFlags;
  private final String[] mSplitCodePaths;
  
  public DefaultSplitAssetLoader(PackageParser.PackageLite paramPackageLite, int paramInt)
  {
    mBaseCodePath = baseCodePath;
    mSplitCodePaths = splitCodePaths;
    mFlags = paramInt;
  }
  
  private static ApkAssets loadApkAssets(String paramString, int paramInt)
    throws PackageParser.PackageParserException
  {
    Object localObject;
    if (((paramInt & 0x1) != 0) && (!PackageParser.isApkPath(paramString)))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Invalid package file: ");
      ((StringBuilder)localObject).append(paramString);
      throw new PackageParser.PackageParserException(-100, ((StringBuilder)localObject).toString());
    }
    try
    {
      localObject = ApkAssets.loadFromPath(paramString);
      return localObject;
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Failed to load APK at path ");
      localStringBuilder.append(paramString);
      throw new PackageParser.PackageParserException(-2, localStringBuilder.toString(), localIOException);
    }
  }
  
  public void close()
    throws Exception
  {
    IoUtils.closeQuietly(mCachedAssetManager);
  }
  
  public AssetManager getBaseAssetManager()
    throws PackageParser.PackageParserException
  {
    if (mCachedAssetManager != null) {
      return mCachedAssetManager;
    }
    int i;
    if (mSplitCodePaths != null) {
      i = mSplitCodePaths.length;
    } else {
      i = 0;
    }
    ApkAssets[] arrayOfApkAssets = new ApkAssets[i + 1];
    arrayOfApkAssets[0] = loadApkAssets(mBaseCodePath, mFlags);
    if (!ArrayUtils.isEmpty(mSplitCodePaths))
    {
      localObject = mSplitCodePaths;
      int j = localObject.length;
      i = 0 + 1;
      int k = 0;
      while (k < j)
      {
        arrayOfApkAssets[i] = loadApkAssets(localObject[k], mFlags);
        k++;
        i++;
      }
    }
    Object localObject = new AssetManager();
    ((AssetManager)localObject).setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Build.VERSION.RESOURCES_SDK_INT);
    ((AssetManager)localObject).setApkAssets(arrayOfApkAssets, false);
    mCachedAssetManager = ((AssetManager)localObject);
    return mCachedAssetManager;
  }
  
  public AssetManager getSplitAssetManager(int paramInt)
    throws PackageParser.PackageParserException
  {
    return getBaseAssetManager();
  }
}
