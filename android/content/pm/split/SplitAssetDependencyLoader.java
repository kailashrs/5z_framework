package android.content.pm.split;

import android.content.pm.PackageParser;
import android.content.pm.PackageParser.PackageLite;
import android.content.pm.PackageParser.PackageParserException;
import android.content.res.ApkAssets;
import android.content.res.AssetManager;
import android.os.Build.VERSION;
import android.util.SparseArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import libcore.io.IoUtils;

public class SplitAssetDependencyLoader
  extends SplitDependencyLoader<PackageParser.PackageParserException>
  implements SplitAssetLoader
{
  private final AssetManager[] mCachedAssetManagers;
  private final ApkAssets[][] mCachedSplitApks;
  private final int mFlags;
  private final String[] mSplitPaths;
  
  public SplitAssetDependencyLoader(PackageParser.PackageLite paramPackageLite, SparseArray<int[]> paramSparseArray, int paramInt)
  {
    super(paramSparseArray);
    mSplitPaths = new String[splitCodePaths.length + 1];
    mSplitPaths[0] = baseCodePath;
    System.arraycopy(splitCodePaths, 0, mSplitPaths, 1, splitCodePaths.length);
    mFlags = paramInt;
    mCachedSplitApks = new ApkAssets[mSplitPaths.length][];
    mCachedAssetManagers = new AssetManager[mSplitPaths.length];
  }
  
  private static AssetManager createAssetManagerWithAssets(ApkAssets[] paramArrayOfApkAssets)
  {
    AssetManager localAssetManager = new AssetManager();
    localAssetManager.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Build.VERSION.RESOURCES_SDK_INT);
    localAssetManager.setApkAssets(paramArrayOfApkAssets, false);
    return localAssetManager;
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
    AssetManager[] arrayOfAssetManager = mCachedAssetManagers;
    int i = arrayOfAssetManager.length;
    for (int j = 0; j < i; j++) {
      IoUtils.closeQuietly(arrayOfAssetManager[j]);
    }
  }
  
  protected void constructSplit(int paramInt1, int[] paramArrayOfInt, int paramInt2)
    throws PackageParser.PackageParserException
  {
    ArrayList localArrayList = new ArrayList();
    if (paramInt2 >= 0) {
      Collections.addAll(localArrayList, mCachedSplitApks[paramInt2]);
    }
    localArrayList.add(loadApkAssets(mSplitPaths[paramInt1], mFlags));
    int i = paramArrayOfInt.length;
    for (paramInt2 = 0; paramInt2 < i; paramInt2++)
    {
      int j = paramArrayOfInt[paramInt2];
      localArrayList.add(loadApkAssets(mSplitPaths[j], mFlags));
    }
    mCachedSplitApks[paramInt1] = ((ApkAssets[])localArrayList.toArray(new ApkAssets[localArrayList.size()]));
    mCachedAssetManagers[paramInt1] = createAssetManagerWithAssets(mCachedSplitApks[paramInt1]);
  }
  
  public AssetManager getBaseAssetManager()
    throws PackageParser.PackageParserException
  {
    loadDependenciesForSplit(0);
    return mCachedAssetManagers[0];
  }
  
  public AssetManager getSplitAssetManager(int paramInt)
    throws PackageParser.PackageParserException
  {
    loadDependenciesForSplit(paramInt + 1);
    return mCachedAssetManagers[(paramInt + 1)];
  }
  
  protected boolean isSplitCached(int paramInt)
  {
    boolean bool;
    if (mCachedAssetManagers[paramInt] != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
