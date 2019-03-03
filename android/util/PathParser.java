package android.util;

import android.graphics.Path;
import dalvik.annotation.optimization.FastNative;

public class PathParser
{
  static final String LOGTAG = PathParser.class.getSimpleName();
  
  public PathParser() {}
  
  public static boolean canMorph(PathData paramPathData1, PathData paramPathData2)
  {
    return nCanMorph(mNativePathData, mNativePathData);
  }
  
  public static Path createPathFromPathData(String paramString)
  {
    if (paramString != null)
    {
      Path localPath = new Path();
      nParseStringForPath(mNativePath, paramString, paramString.length());
      return localPath;
    }
    throw new IllegalArgumentException("Path string can not be null.");
  }
  
  public static void createPathFromPathData(Path paramPath, PathData paramPathData)
  {
    nCreatePathFromPathData(mNativePath, mNativePathData);
  }
  
  public static boolean interpolatePathData(PathData paramPathData1, PathData paramPathData2, PathData paramPathData3, float paramFloat)
  {
    return nInterpolatePathData(mNativePathData, mNativePathData, mNativePathData, paramFloat);
  }
  
  @FastNative
  private static native boolean nCanMorph(long paramLong1, long paramLong2);
  
  @FastNative
  private static native long nCreateEmptyPathData();
  
  @FastNative
  private static native long nCreatePathData(long paramLong);
  
  private static native long nCreatePathDataFromString(String paramString, int paramInt);
  
  @FastNative
  private static native void nCreatePathFromPathData(long paramLong1, long paramLong2);
  
  @FastNative
  private static native void nFinalize(long paramLong);
  
  @FastNative
  private static native boolean nInterpolatePathData(long paramLong1, long paramLong2, long paramLong3, float paramFloat);
  
  private static native void nParseStringForPath(long paramLong, String paramString, int paramInt);
  
  @FastNative
  private static native void nSetPathData(long paramLong1, long paramLong2);
  
  public static class PathData
  {
    long mNativePathData = 0L;
    
    public PathData()
    {
      mNativePathData = PathParser.access$000();
    }
    
    public PathData(PathData paramPathData)
    {
      mNativePathData = PathParser.nCreatePathData(mNativePathData);
    }
    
    public PathData(String paramString)
    {
      mNativePathData = PathParser.nCreatePathDataFromString(paramString, paramString.length());
      if (mNativePathData != 0L) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid pathData: ");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    protected void finalize()
      throws Throwable
    {
      if (mNativePathData != 0L)
      {
        PathParser.nFinalize(mNativePathData);
        mNativePathData = 0L;
      }
      super.finalize();
    }
    
    public long getNativePtr()
    {
      return mNativePathData;
    }
    
    public void setPathData(PathData paramPathData)
    {
      PathParser.nSetPathData(mNativePathData, mNativePathData);
    }
  }
}
