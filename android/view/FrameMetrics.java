package android.view;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class FrameMetrics
{
  public static final int ANIMATION_DURATION = 2;
  public static final int COMMAND_ISSUE_DURATION = 6;
  public static final int DRAW_DURATION = 4;
  private static final int[] DURATIONS = { 1, 5, 5, 6, 6, 7, 7, 8, 8, 9, 10, 11, 11, 12, 12, 13, 1, 13 };
  public static final int FIRST_DRAW_FRAME = 9;
  private static final int FRAME_INFO_FLAG_FIRST_DRAW = 1;
  public static final int INPUT_HANDLING_DURATION = 1;
  public static final int INTENDED_VSYNC_TIMESTAMP = 10;
  public static final int LAYOUT_MEASURE_DURATION = 3;
  public static final int SWAP_BUFFERS_DURATION = 7;
  public static final int SYNC_DURATION = 5;
  public static final int TOTAL_DURATION = 8;
  public static final int UNKNOWN_DELAY_DURATION = 0;
  public static final int VSYNC_TIMESTAMP = 11;
  final long[] mTimingData = new long[16];
  
  FrameMetrics() {}
  
  public FrameMetrics(FrameMetrics paramFrameMetrics)
  {
    System.arraycopy(mTimingData, 0, mTimingData, 0, mTimingData.length);
  }
  
  public long getMetric(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 11))
    {
      if (mTimingData == null) {
        return -1L;
      }
      if (paramInt == 9)
      {
        long l1 = mTimingData[0];
        long l2 = 1L;
        if ((l1 & 1L) == 0L) {
          l2 = 0L;
        }
        return l2;
      }
      if (paramInt == 10) {
        return mTimingData[1];
      }
      if (paramInt == 11) {
        return mTimingData[2];
      }
      paramInt = 2 * paramInt;
      return mTimingData[DURATIONS[(paramInt + 1)]] - mTimingData[DURATIONS[paramInt]];
    }
    return -1L;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface Index
  {
    public static final int ANIMATION_START = 6;
    public static final int DRAW_START = 8;
    public static final int FLAGS = 0;
    public static final int FRAME_COMPLETED = 13;
    public static final int FRAME_STATS_COUNT = 16;
    public static final int HANDLE_INPUT_START = 5;
    public static final int INTENDED_VSYNC = 1;
    public static final int ISSUE_DRAW_COMMANDS_START = 11;
    public static final int NEWEST_INPUT_EVENT = 4;
    public static final int OLDEST_INPUT_EVENT = 3;
    public static final int PERFORM_TRAVERSALS_START = 7;
    public static final int SWAP_BUFFERS = 12;
    public static final int SYNC_QUEUED = 9;
    public static final int SYNC_START = 10;
    public static final int VSYNC = 2;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Metric {}
}
