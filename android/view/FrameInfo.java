package android.view;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

final class FrameInfo
{
  private static final int ANIMATION_START = 6;
  private static final int DRAW_START = 8;
  private static final int FLAGS = 0;
  public static final long FLAG_WINDOW_LAYOUT_CHANGED = 1L;
  private static final int HANDLE_INPUT_START = 5;
  private static final int INTENDED_VSYNC = 1;
  private static final int NEWEST_INPUT_EVENT = 4;
  private static final int OLDEST_INPUT_EVENT = 3;
  private static final int PERFORM_TRAVERSALS_START = 7;
  private static final int VSYNC = 2;
  long[] mFrameInfo = new long[9];
  
  FrameInfo() {}
  
  public void addFlags(long paramLong)
  {
    long[] arrayOfLong = mFrameInfo;
    arrayOfLong[0] |= paramLong;
  }
  
  public void markAnimationsStart()
  {
    mFrameInfo[6] = System.nanoTime();
  }
  
  public void markDrawStart()
  {
    mFrameInfo[8] = System.nanoTime();
  }
  
  public void markInputHandlingStart()
  {
    mFrameInfo[5] = System.nanoTime();
  }
  
  public void markPerformTraversalsStart()
  {
    mFrameInfo[7] = System.nanoTime();
  }
  
  public void setVsync(long paramLong1, long paramLong2)
  {
    mFrameInfo[1] = paramLong1;
    mFrameInfo[2] = paramLong2;
    mFrameInfo[3] = Long.MAX_VALUE;
    mFrameInfo[4] = 0L;
    mFrameInfo[0] = 0L;
  }
  
  public void updateInputEventTime(long paramLong1, long paramLong2)
  {
    if (paramLong2 < mFrameInfo[3]) {
      mFrameInfo[3] = paramLong2;
    }
    if (paramLong1 > mFrameInfo[4]) {
      mFrameInfo[4] = paramLong1;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FrameInfoFlags {}
}
