package android.filterpacks.performance;

public class Throughput
{
  private final int mPeriodFrames;
  private final int mPeriodTime;
  private final int mPixels;
  private final int mTotalFrames;
  
  public Throughput(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mTotalFrames = paramInt1;
    mPeriodFrames = paramInt2;
    mPeriodTime = paramInt3;
    mPixels = paramInt4;
  }
  
  public float getFramesPerSecond()
  {
    return mPeriodFrames / mPeriodTime;
  }
  
  public float getNanosPerPixel()
  {
    return (float)(mPeriodTime / mPeriodFrames * 1000000.0D / mPixels);
  }
  
  public int getPeriodFrameCount()
  {
    return mPeriodFrames;
  }
  
  public int getPeriodTime()
  {
    return mPeriodTime;
  }
  
  public int getTotalFrameCount()
  {
    return mTotalFrames;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getFramesPerSecond());
    localStringBuilder.append(" FPS");
    return localStringBuilder.toString();
  }
}
