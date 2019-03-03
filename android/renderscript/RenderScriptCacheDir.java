package android.renderscript;

import java.io.File;

public class RenderScriptCacheDir
{
  static File mCacheDir;
  
  public RenderScriptCacheDir() {}
  
  public static void setupDiskCache(File paramFile)
  {
    mCacheDir = paramFile;
  }
}
