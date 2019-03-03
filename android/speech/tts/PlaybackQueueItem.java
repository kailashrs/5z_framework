package android.speech.tts;

abstract class PlaybackQueueItem
  implements Runnable
{
  private final Object mCallerIdentity;
  private final TextToSpeechService.UtteranceProgressDispatcher mDispatcher;
  
  PlaybackQueueItem(TextToSpeechService.UtteranceProgressDispatcher paramUtteranceProgressDispatcher, Object paramObject)
  {
    mDispatcher = paramUtteranceProgressDispatcher;
    mCallerIdentity = paramObject;
  }
  
  Object getCallerIdentity()
  {
    return mCallerIdentity;
  }
  
  protected TextToSpeechService.UtteranceProgressDispatcher getDispatcher()
  {
    return mDispatcher;
  }
  
  public abstract void run();
  
  abstract void stop(int paramInt);
}
