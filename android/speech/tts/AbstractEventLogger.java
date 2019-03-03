package android.speech.tts;

import android.os.SystemClock;

abstract class AbstractEventLogger
{
  protected final int mCallerPid;
  protected final int mCallerUid;
  private volatile long mEngineCompleteTime = -1L;
  private volatile long mEngineStartTime = -1L;
  private boolean mLogWritten = false;
  protected long mPlaybackStartTime = -1L;
  protected final long mReceivedTime;
  private volatile long mRequestProcessingStartTime = -1L;
  protected final String mServiceApp;
  
  AbstractEventLogger(int paramInt1, int paramInt2, String paramString)
  {
    mCallerUid = paramInt1;
    mCallerPid = paramInt2;
    mServiceApp = paramString;
    mReceivedTime = SystemClock.elapsedRealtime();
  }
  
  protected abstract void logFailure(int paramInt);
  
  protected abstract void logSuccess(long paramLong1, long paramLong2, long paramLong3);
  
  public void onAudioDataWritten()
  {
    if (mPlaybackStartTime == -1L) {
      mPlaybackStartTime = SystemClock.elapsedRealtime();
    }
  }
  
  public void onCompleted(int paramInt)
  {
    if (mLogWritten) {
      return;
    }
    mLogWritten = true;
    SystemClock.elapsedRealtime();
    if ((paramInt == 0) && (mPlaybackStartTime != -1L) && (mEngineCompleteTime != -1L))
    {
      logSuccess(mPlaybackStartTime - mReceivedTime, mEngineStartTime - mRequestProcessingStartTime, mEngineCompleteTime - mRequestProcessingStartTime);
      return;
    }
    logFailure(paramInt);
  }
  
  public void onEngineComplete()
  {
    mEngineCompleteTime = SystemClock.elapsedRealtime();
  }
  
  public void onEngineDataReceived()
  {
    if (mEngineStartTime == -1L) {
      mEngineStartTime = SystemClock.elapsedRealtime();
    }
  }
  
  public void onRequestProcessingStart()
  {
    mRequestProcessingStartTime = SystemClock.elapsedRealtime();
  }
}
