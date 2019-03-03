package android.hardware.hdmi;

import android.annotation.SystemApi;

@SystemApi
public abstract class HdmiRecordListener
{
  public HdmiRecordListener() {}
  
  public void onClearTimerRecordingResult(int paramInt1, int paramInt2) {}
  
  public void onOneTouchRecordResult(int paramInt1, int paramInt2) {}
  
  public abstract HdmiRecordSources.RecordSource onOneTouchRecordSourceRequested(int paramInt);
  
  public void onTimerRecordingResult(int paramInt, TimerStatusData paramTimerStatusData) {}
  
  @SystemApi
  public static class TimerStatusData
  {
    private int mDurationHour;
    private int mDurationMinute;
    private int mExtraError;
    private int mMediaInfo;
    private int mNotProgrammedError;
    private boolean mOverlapped;
    private boolean mProgrammed;
    private int mProgrammedInfo;
    
    private TimerStatusData() {}
    
    private static int bcdByteToInt(byte paramByte)
    {
      return (paramByte >> 4 & 0xF) * 10 + paramByte & 0xF;
    }
    
    static TimerStatusData parseFrom(int paramInt)
    {
      TimerStatusData localTimerStatusData = new TimerStatusData();
      boolean bool1 = true;
      boolean bool2;
      if ((paramInt >> 31 & 0x1) != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mOverlapped = bool2;
      mMediaInfo = (paramInt >> 29 & 0x3);
      if ((paramInt >> 28 & 0x1) != 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      mProgrammed = bool2;
      if (mProgrammed)
      {
        mProgrammedInfo = (paramInt >> 24 & 0xF);
        mDurationHour = bcdByteToInt((byte)(paramInt >> 16 & 0xFF));
        mDurationMinute = bcdByteToInt((byte)(paramInt >> 8 & 0xFF));
      }
      else
      {
        mNotProgrammedError = (paramInt >> 24 & 0xF);
        mDurationHour = bcdByteToInt((byte)(paramInt >> 16 & 0xFF));
        mDurationMinute = bcdByteToInt((byte)(paramInt >> 8 & 0xFF));
      }
      mExtraError = (paramInt & 0xFF);
      return localTimerStatusData;
    }
    
    public int getDurationHour()
    {
      return mDurationHour;
    }
    
    public int getDurationMinute()
    {
      return mDurationMinute;
    }
    
    public int getExtraError()
    {
      return mExtraError;
    }
    
    public int getMediaInfo()
    {
      return mMediaInfo;
    }
    
    public int getNotProgammedError()
    {
      if (!isProgrammed()) {
        return mNotProgrammedError;
      }
      throw new IllegalStateException("Has no not-programmed error. Call getProgrammedInfo() instead.");
    }
    
    public int getProgrammedInfo()
    {
      if (isProgrammed()) {
        return mProgrammedInfo;
      }
      throw new IllegalStateException("No programmed info. Call getNotProgammedError() instead.");
    }
    
    public boolean isOverlapped()
    {
      return mOverlapped;
    }
    
    public boolean isProgrammed()
    {
      return mProgrammed;
    }
  }
}
