package android.view;

public abstract class FrameStats
{
  public static final long UNDEFINED_TIME_NANO = -1L;
  protected long[] mFramesPresentedTimeNano;
  protected long mRefreshPeriodNano;
  
  public FrameStats() {}
  
  public final long getEndTimeNano()
  {
    if (getFrameCount() <= 0) {
      return -1L;
    }
    return mFramesPresentedTimeNano[(mFramesPresentedTimeNano.length - 1)];
  }
  
  public final int getFrameCount()
  {
    int i;
    if (mFramesPresentedTimeNano != null) {
      i = mFramesPresentedTimeNano.length;
    } else {
      i = 0;
    }
    return i;
  }
  
  public final long getFramePresentedTimeNano(int paramInt)
  {
    if (mFramesPresentedTimeNano != null) {
      return mFramesPresentedTimeNano[paramInt];
    }
    throw new IndexOutOfBoundsException();
  }
  
  public final long getRefreshPeriodNano()
  {
    return mRefreshPeriodNano;
  }
  
  public final long getStartTimeNano()
  {
    if (getFrameCount() <= 0) {
      return -1L;
    }
    return mFramesPresentedTimeNano[0];
  }
}
