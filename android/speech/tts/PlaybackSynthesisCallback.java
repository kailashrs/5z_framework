package android.speech.tts;

import android.util.Log;

class PlaybackSynthesisCallback
  extends AbstractSynthesisCallback
{
  private static final boolean DBG = false;
  private static final int MIN_AUDIO_BUFFER_SIZE = 8192;
  private static final String TAG = "PlaybackSynthesisRequest";
  private final TextToSpeechService.AudioOutputParams mAudioParams;
  private final AudioPlaybackHandler mAudioTrackHandler;
  private final Object mCallerIdentity;
  private final TextToSpeechService.UtteranceProgressDispatcher mDispatcher;
  private volatile boolean mDone = false;
  private SynthesisPlaybackQueueItem mItem = null;
  private final AbstractEventLogger mLogger;
  private final Object mStateLock = new Object();
  protected int mStatusCode;
  
  PlaybackSynthesisCallback(TextToSpeechService.AudioOutputParams paramAudioOutputParams, AudioPlaybackHandler paramAudioPlaybackHandler, TextToSpeechService.UtteranceProgressDispatcher paramUtteranceProgressDispatcher, Object paramObject, AbstractEventLogger paramAbstractEventLogger, boolean paramBoolean)
  {
    super(paramBoolean);
    mAudioParams = paramAudioOutputParams;
    mAudioTrackHandler = paramAudioPlaybackHandler;
    mDispatcher = paramUtteranceProgressDispatcher;
    mCallerIdentity = paramObject;
    mLogger = paramAbstractEventLogger;
    mStatusCode = 0;
  }
  
  public int audioAvailable(byte[] arg1, int paramInt1, int paramInt2)
  {
    if ((paramInt2 <= getMaxBufferSize()) && (paramInt2 > 0)) {
      synchronized (mStateLock)
      {
        if (mItem == null)
        {
          mStatusCode = -5;
          return -1;
        }
        if (mStatusCode != 0) {
          return -1;
        }
        if (mStatusCode == -2)
        {
          paramInt1 = errorCodeOnStop();
          return paramInt1;
        }
        SynthesisPlaybackQueueItem localSynthesisPlaybackQueueItem = mItem;
        ??? = new byte[paramInt2];
        System.arraycopy(???, paramInt1, (byte[])???, 0, paramInt2);
        mDispatcher.dispatchOnAudioAvailable((byte[])???);
        try
        {
          localSynthesisPlaybackQueueItem.put((byte[])???);
          mLogger.onEngineDataReceived();
          return 0;
        }
        catch (InterruptedException ???)
        {
          synchronized (mStateLock)
          {
            mStatusCode = -5;
            return -1;
          }
        }
      }
    }
    ??? = new StringBuilder();
    ???.append("buffer is too large or of zero length (");
    ???.append(paramInt2);
    ???.append(" bytes)");
    throw new IllegalArgumentException(???.toString());
  }
  
  public int done()
  {
    synchronized (mStateLock)
    {
      if (mDone)
      {
        Log.w("PlaybackSynthesisRequest", "Duplicate call to done()");
        return -1;
      }
      if (mStatusCode == -2)
      {
        i = errorCodeOnStop();
        return i;
      }
      mDone = true;
      if (mItem == null)
      {
        Log.w("PlaybackSynthesisRequest", "done() was called before start() call");
        if (mStatusCode == 0) {
          mDispatcher.dispatchOnSuccess();
        } else {
          mDispatcher.dispatchOnError(mStatusCode);
        }
        mLogger.onEngineComplete();
        return -1;
      }
      SynthesisPlaybackQueueItem localSynthesisPlaybackQueueItem = mItem;
      int i = mStatusCode;
      if (i == 0) {
        localSynthesisPlaybackQueueItem.done();
      } else {
        localSynthesisPlaybackQueueItem.stop(i);
      }
      mLogger.onEngineComplete();
      return 0;
    }
  }
  
  public void error()
  {
    error(-3);
  }
  
  public void error(int paramInt)
  {
    synchronized (mStateLock)
    {
      if (mDone) {
        return;
      }
      mStatusCode = paramInt;
      return;
    }
  }
  
  public int getMaxBufferSize()
  {
    return 8192;
  }
  
  public boolean hasFinished()
  {
    synchronized (mStateLock)
    {
      boolean bool = mDone;
      return bool;
    }
  }
  
  public boolean hasStarted()
  {
    synchronized (mStateLock)
    {
      boolean bool;
      if (mItem != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public void rangeStart(int paramInt1, int paramInt2, int paramInt3)
  {
    if (mItem == null)
    {
      Log.e("PlaybackSynthesisRequest", "mItem is null");
      return;
    }
    mItem.rangeStart(paramInt1, paramInt2, paramInt3);
  }
  
  public int start(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt2 != 3) && (paramInt2 != 2) && (paramInt2 != 4))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Audio format encoding ");
      ((StringBuilder)localObject1).append(paramInt2);
      ((StringBuilder)localObject1).append(" not supported. Please use one of AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT or AudioFormat.ENCODING_PCM_FLOAT");
      Log.w("PlaybackSynthesisRequest", ((StringBuilder)localObject1).toString());
    }
    mDispatcher.dispatchOnBeginSynthesis(paramInt1, paramInt2, paramInt3);
    int i = BlockingAudioTrack.getChannelConfig(paramInt3);
    Object localObject1 = mStateLock;
    if (i == 0) {
      try
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Unsupported number of channels :");
        localStringBuilder.append(paramInt3);
        Log.e("PlaybackSynthesisRequest", localStringBuilder.toString());
        mStatusCode = -5;
        return -1;
      }
      finally
      {
        break label242;
      }
    }
    if (mStatusCode == -2)
    {
      paramInt1 = errorCodeOnStop();
      return paramInt1;
    }
    if (mStatusCode != 0) {
      return -1;
    }
    if (mItem != null)
    {
      Log.e("PlaybackSynthesisRequest", "Start called twice");
      return -1;
    }
    SynthesisPlaybackQueueItem localSynthesisPlaybackQueueItem = new android/speech/tts/SynthesisPlaybackQueueItem;
    localSynthesisPlaybackQueueItem.<init>(mAudioParams, paramInt1, paramInt2, paramInt3, mDispatcher, mCallerIdentity, mLogger);
    mAudioTrackHandler.enqueue(localSynthesisPlaybackQueueItem);
    mItem = localSynthesisPlaybackQueueItem;
    return 0;
    label242:
    throw localSynthesisPlaybackQueueItem;
  }
  
  void stop()
  {
    synchronized (mStateLock)
    {
      if (mDone) {
        return;
      }
      if (mStatusCode == -2)
      {
        Log.w("PlaybackSynthesisRequest", "stop() called twice");
        return;
      }
      SynthesisPlaybackQueueItem localSynthesisPlaybackQueueItem = mItem;
      mStatusCode = -2;
      if (localSynthesisPlaybackQueueItem != null)
      {
        localSynthesisPlaybackQueueItem.stop(-2);
      }
      else
      {
        mLogger.onCompleted(-2);
        mDispatcher.dispatchOnStop();
      }
      return;
    }
  }
}
