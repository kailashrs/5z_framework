package android.content.pm.dex;

public class PackageOptimizationInfo
{
  private final int mCompilationFilter;
  private final int mCompilationReason;
  
  public PackageOptimizationInfo(int paramInt1, int paramInt2)
  {
    mCompilationReason = paramInt2;
    mCompilationFilter = paramInt1;
  }
  
  public static PackageOptimizationInfo createWithNoInfo()
  {
    return new PackageOptimizationInfo(-1, -1);
  }
  
  public int getCompilationFilter()
  {
    return mCompilationFilter;
  }
  
  public int getCompilationReason()
  {
    return mCompilationReason;
  }
}
