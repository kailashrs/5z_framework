package android.content.pm.split;

import android.content.pm.PackageParser.PackageParserException;
import android.content.res.AssetManager;

public abstract interface SplitAssetLoader
  extends AutoCloseable
{
  public abstract AssetManager getBaseAssetManager()
    throws PackageParser.PackageParserException;
  
  public abstract AssetManager getSplitAssetManager(int paramInt)
    throws PackageParser.PackageParserException;
}
