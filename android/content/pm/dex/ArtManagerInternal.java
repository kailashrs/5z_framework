package android.content.pm.dex;

import android.content.pm.ApplicationInfo;

public abstract class ArtManagerInternal
{
  public ArtManagerInternal() {}
  
  public abstract PackageOptimizationInfo getPackageOptimizationInfo(ApplicationInfo paramApplicationInfo, String paramString);
}
