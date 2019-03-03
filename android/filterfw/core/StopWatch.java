package android.filterfw.core;

import android.os.SystemClock;
import android.util.Log;

class StopWatch
{
  private int STOP_WATCH_LOGGING_PERIOD = 200;
  private String TAG = "MFF";
  private String mName;
  private int mNumCalls;
  private long mStartTime;
  private long mTotalTime;
  
  public StopWatch(String paramString)
  {
    mName = paramString;
    mStartTime = -1L;
    mTotalTime = 0L;
    mNumCalls = 0;
  }
  
  public void start()
  {
    if (mStartTime == -1L)
    {
      mStartTime = SystemClock.elapsedRealtime();
      return;
    }
    throw new RuntimeException("Calling start with StopWatch already running");
  }
  
  public void stop()
  {
    if (mStartTime != -1L)
    {
      long l = SystemClock.elapsedRealtime();
      mTotalTime += l - mStartTime;
      mNumCalls += 1;
      mStartTime = -1L;
      if (mNumCalls % STOP_WATCH_LOGGING_PERIOD == 0)
      {
        String str = TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("AVG ms/call ");
        localStringBuilder.append(mName);
        localStringBuilder.append(": ");
        localStringBuilder.append(String.format("%.1f", new Object[] { Float.valueOf((float)mTotalTime * 1.0F / mNumCalls) }));
        Log.i(str, localStringBuilder.toString());
        mTotalTime = 0L;
        mNumCalls = 0;
      }
      return;
    }
    throw new RuntimeException("Calling stop with StopWatch already stopped");
  }
}
