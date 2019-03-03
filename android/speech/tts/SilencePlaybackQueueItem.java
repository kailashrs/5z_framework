package android.speech.tts;

import android.os.ConditionVariable;

class SilencePlaybackQueueItem
  extends PlaybackQueueItem
{
  private final ConditionVariable mCondVar = new ConditionVariable();
  private final long mSilenceDurationMs;
  
  SilencePlaybackQueueItem(TextToSpeechService.UtteranceProgressDispatcher paramUtteranceProgressDispatcher, Object paramObject, long paramLong)
  {
    super(paramUtteranceProgressDispatcher, paramObject);
    mSilenceDurationMs = paramLong;
  }
  
  public void run()
  {
    getDispatcher().dispatchOnStart();
    boolean bool = false;
    if (mSilenceDurationMs > 0L) {
      bool = mCondVar.block(mSilenceDurationMs);
    }
    if (bool) {
      getDispatcher().dispatchOnStop();
    } else {
      getDispatcher().dispatchOnSuccess();
    }
  }
  
  void stop(int paramInt)
  {
    mCondVar.open();
  }
}
