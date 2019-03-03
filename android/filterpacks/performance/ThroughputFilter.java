package android.filterpacks.performance;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.format.ObjectFormat;
import android.os.SystemClock;

public class ThroughputFilter
  extends Filter
{
  private long mLastTime = 0L;
  private FrameFormat mOutputFormat;
  @GenerateFieldPort(hasDefault=true, name="period")
  private int mPeriod = 5;
  private int mPeriodFrameCount = 0;
  private int mTotalFrameCount = 0;
  
  public ThroughputFilter(String paramString)
  {
    super(paramString);
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return paramFrameFormat;
  }
  
  public void open(FilterContext paramFilterContext)
  {
    mTotalFrameCount = 0;
    mPeriodFrameCount = 0;
    mLastTime = 0L;
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Object localObject = pullInput("frame");
    pushOutput("frame", (Frame)localObject);
    mTotalFrameCount += 1;
    mPeriodFrameCount += 1;
    if (mLastTime == 0L) {
      mLastTime = SystemClock.elapsedRealtime();
    }
    long l = SystemClock.elapsedRealtime();
    if (l - mLastTime >= mPeriod * 1000)
    {
      localObject = ((Frame)localObject).getFormat();
      int i = ((FrameFormat)localObject).getWidth();
      int j = ((FrameFormat)localObject).getHeight();
      localObject = new Throughput(mTotalFrameCount, mPeriodFrameCount, mPeriod, i * j);
      paramFilterContext = paramFilterContext.getFrameManager().newFrame(mOutputFormat);
      paramFilterContext.setObjectValue(localObject);
      pushOutput("throughput", paramFilterContext);
      mLastTime = l;
      mPeriodFrameCount = 0;
    }
  }
  
  public void setupPorts()
  {
    addInputPort("frame");
    mOutputFormat = ObjectFormat.fromClass(Throughput.class, 1);
    addOutputBasedOnInput("frame", "frame");
    addOutputPort("throughput", mOutputFormat);
  }
}
