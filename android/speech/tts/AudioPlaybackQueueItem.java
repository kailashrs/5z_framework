package android.speech.tts;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.ConditionVariable;
import android.util.Log;

class AudioPlaybackQueueItem
  extends PlaybackQueueItem
{
  private static final String TAG = "TTS.AudioQueueItem";
  private final TextToSpeechService.AudioOutputParams mAudioParams;
  private final Context mContext;
  private final ConditionVariable mDone;
  private volatile boolean mFinished;
  private MediaPlayer mPlayer;
  private final Uri mUri;
  
  AudioPlaybackQueueItem(TextToSpeechService.UtteranceProgressDispatcher paramUtteranceProgressDispatcher, Object paramObject, Context paramContext, Uri paramUri, TextToSpeechService.AudioOutputParams paramAudioOutputParams)
  {
    super(paramUtteranceProgressDispatcher, paramObject);
    mContext = paramContext;
    mUri = paramUri;
    mAudioParams = paramAudioOutputParams;
    mDone = new ConditionVariable();
    mPlayer = null;
    mFinished = false;
  }
  
  private static final float clip(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat1 < paramFloat2) {
      paramFloat1 = paramFloat2;
    } else if (paramFloat1 >= paramFloat3) {
      paramFloat1 = paramFloat3;
    }
    return paramFloat1;
  }
  
  private void finish()
  {
    try
    {
      mPlayer.stop();
    }
    catch (IllegalStateException localIllegalStateException) {}
    mPlayer.release();
  }
  
  private static void setupVolume(MediaPlayer paramMediaPlayer, float paramFloat1, float paramFloat2)
  {
    float f1 = clip(paramFloat1, 0.0F, 1.0F);
    float f2 = clip(paramFloat2, -1.0F, 1.0F);
    paramFloat1 = f1;
    paramFloat2 = f1;
    float f3;
    if (f2 > 0.0F)
    {
      f1 = paramFloat1 * (1.0F - f2);
      f3 = paramFloat2;
    }
    else
    {
      f1 = paramFloat1;
      f3 = paramFloat2;
      if (f2 < 0.0F)
      {
        f3 = paramFloat2 * (1.0F + f2);
        f1 = paramFloat1;
      }
    }
    paramMediaPlayer.setVolume(f1, f3);
  }
  
  public void run()
  {
    TextToSpeechService.UtteranceProgressDispatcher localUtteranceProgressDispatcher = getDispatcher();
    localUtteranceProgressDispatcher.dispatchOnStart();
    int i = mAudioParams.mSessionId;
    Object localObject1 = mContext;
    Object localObject2 = mUri;
    AudioAttributes localAudioAttributes = mAudioParams.mAudioAttributes;
    if (i <= 0) {
      i = 0;
    }
    mPlayer = MediaPlayer.create((Context)localObject1, (Uri)localObject2, null, localAudioAttributes, i);
    if (mPlayer == null)
    {
      localUtteranceProgressDispatcher.dispatchOnError(-5);
      return;
    }
    try
    {
      localObject1 = mPlayer;
      localObject2 = new android/speech/tts/AudioPlaybackQueueItem$1;
      ((1)localObject2).<init>(this);
      ((MediaPlayer)localObject1).setOnErrorListener((MediaPlayer.OnErrorListener)localObject2);
      localObject2 = mPlayer;
      localObject1 = new android/speech/tts/AudioPlaybackQueueItem$2;
      ((2)localObject1).<init>(this);
      ((MediaPlayer)localObject2).setOnCompletionListener((MediaPlayer.OnCompletionListener)localObject1);
      setupVolume(mPlayer, mAudioParams.mVolume, mAudioParams.mPan);
      mPlayer.start();
      mDone.block();
      finish();
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      Log.w("TTS.AudioQueueItem", "MediaPlayer failed", localIllegalArgumentException);
      mDone.open();
    }
    if (mFinished) {
      localUtteranceProgressDispatcher.dispatchOnSuccess();
    } else {
      localUtteranceProgressDispatcher.dispatchOnStop();
    }
  }
  
  void stop(int paramInt)
  {
    mDone.open();
  }
}
