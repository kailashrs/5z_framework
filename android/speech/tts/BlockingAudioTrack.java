package android.speech.tts;

import android.media.AudioFormat;
import android.media.AudioFormat.Builder;
import android.media.AudioTrack;
import android.media.AudioTrack.OnPlaybackPositionUpdateListener;
import android.util.Log;

class BlockingAudioTrack
{
  private static final boolean DBG = false;
  private static final long MAX_PROGRESS_WAIT_MS = 2500L;
  private static final long MAX_SLEEP_TIME_MS = 2500L;
  private static final int MIN_AUDIO_BUFFER_SIZE = 8192;
  private static final long MIN_SLEEP_TIME_MS = 20L;
  private static final String TAG = "TTS.BlockingAudioTrack";
  private int mAudioBufferSize;
  private final int mAudioFormat;
  private final TextToSpeechService.AudioOutputParams mAudioParams;
  private AudioTrack mAudioTrack;
  private Object mAudioTrackLock = new Object();
  private final int mBytesPerFrame;
  private int mBytesWritten = 0;
  private final int mChannelCount;
  private boolean mIsShortUtterance;
  private final int mSampleRateInHz;
  private int mSessionId;
  private volatile boolean mStopped;
  
  BlockingAudioTrack(TextToSpeechService.AudioOutputParams paramAudioOutputParams, int paramInt1, int paramInt2, int paramInt3)
  {
    mAudioParams = paramAudioOutputParams;
    mSampleRateInHz = paramInt1;
    mAudioFormat = paramInt2;
    mChannelCount = paramInt3;
    mBytesPerFrame = (AudioFormat.getBytesPerSample(mAudioFormat) * mChannelCount);
    mIsShortUtterance = false;
    mAudioBufferSize = 0;
    mBytesWritten = 0;
    mAudioTrack = null;
    mStopped = false;
  }
  
  private void blockUntilCompletion(AudioTrack paramAudioTrack)
  {
    int i = mBytesWritten / mBytesPerFrame;
    int j = -1;
    long l1 = 0L;
    for (;;)
    {
      int k = paramAudioTrack.getPlaybackHeadPosition();
      if ((k < i) && (paramAudioTrack.getPlayState() == 3) && (!mStopped))
      {
        long l2 = clip((i - k) * 1000 / paramAudioTrack.getSampleRate(), 20L, 2500L);
        if (k == j)
        {
          long l3 = l1 + l2;
          l1 = l3;
          if (l3 > 2500L)
          {
            Log.w("TTS.BlockingAudioTrack", "Waited unsuccessfully for 2500ms for AudioTrack to make progress, Aborting");
            break;
          }
        }
        else
        {
          l1 = 0L;
        }
        j = k;
        try
        {
          Thread.sleep(l2);
        }
        catch (InterruptedException paramAudioTrack) {}
      }
    }
  }
  
  private void blockUntilDone(AudioTrack paramAudioTrack)
  {
    if (mBytesWritten <= 0) {
      return;
    }
    if (mIsShortUtterance) {
      blockUntilEstimatedCompletion();
    } else {
      blockUntilCompletion(paramAudioTrack);
    }
  }
  
  private void blockUntilEstimatedCompletion()
  {
    long l = mBytesWritten / mBytesPerFrame * 1000 / mSampleRateInHz;
    try
    {
      Thread.sleep(l);
    }
    catch (InterruptedException localInterruptedException) {}
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
  
  private static final long clip(long paramLong1, long paramLong2, long paramLong3)
  {
    if (paramLong1 < paramLong2) {
      paramLong1 = paramLong2;
    } else if (paramLong1 >= paramLong3) {
      paramLong1 = paramLong3;
    }
    return paramLong1;
  }
  
  private AudioTrack createStreamingAudioTrack()
  {
    int i = getChannelConfig(mChannelCount);
    int j = Math.max(8192, AudioTrack.getMinBufferSize(mSampleRateInHz, i, mAudioFormat));
    Object localObject = new AudioFormat.Builder().setChannelMask(i).setEncoding(mAudioFormat).setSampleRate(mSampleRateInHz).build();
    localObject = new AudioTrack(mAudioParams.mAudioAttributes, (AudioFormat)localObject, j, 1, mAudioParams.mSessionId);
    if (((AudioTrack)localObject).getState() != 1)
    {
      Log.w("TTS.BlockingAudioTrack", "Unable to create audio track.");
      ((AudioTrack)localObject).release();
      return null;
    }
    mAudioBufferSize = j;
    setupVolume((AudioTrack)localObject, mAudioParams.mVolume, mAudioParams.mPan);
    return localObject;
  }
  
  static int getChannelConfig(int paramInt)
  {
    if (paramInt == 1) {
      return 4;
    }
    if (paramInt == 2) {
      return 12;
    }
    return 0;
  }
  
  private static void setupVolume(AudioTrack paramAudioTrack, float paramFloat1, float paramFloat2)
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
    if (paramAudioTrack.setStereoVolume(f1, f3) != 0) {
      Log.e("TTS.BlockingAudioTrack", "Failed to set volume");
    }
  }
  
  private static int writeToAudioTrack(AudioTrack paramAudioTrack, byte[] paramArrayOfByte)
  {
    if (paramAudioTrack.getPlayState() != 3) {
      paramAudioTrack.play();
    }
    int i = 0;
    while (i < paramArrayOfByte.length)
    {
      int j = paramAudioTrack.write(paramArrayOfByte, i, paramArrayOfByte.length);
      if (j <= 0) {
        break;
      }
      i += j;
    }
    return i;
  }
  
  long getAudioLengthMs(int paramInt)
  {
    return paramInt / mBytesPerFrame * 1000 / mSampleRateInHz;
  }
  
  public boolean init()
  {
    AudioTrack localAudioTrack = createStreamingAudioTrack();
    synchronized (mAudioTrackLock)
    {
      mAudioTrack = localAudioTrack;
      return localAudioTrack != null;
    }
  }
  
  public void setNotificationMarkerPosition(int paramInt)
  {
    synchronized (mAudioTrackLock)
    {
      if (mAudioTrack != null) {
        mAudioTrack.setNotificationMarkerPosition(paramInt);
      }
      return;
    }
  }
  
  public void setPlaybackPositionUpdateListener(AudioTrack.OnPlaybackPositionUpdateListener paramOnPlaybackPositionUpdateListener)
  {
    synchronized (mAudioTrackLock)
    {
      if (mAudioTrack != null) {
        mAudioTrack.setPlaybackPositionUpdateListener(paramOnPlaybackPositionUpdateListener);
      }
      return;
    }
  }
  
  public void stop()
  {
    synchronized (mAudioTrackLock)
    {
      if (mAudioTrack != null) {
        mAudioTrack.stop();
      }
      mStopped = true;
      return;
    }
  }
  
  public void waitAndRelease()
  {
    synchronized (mAudioTrackLock)
    {
      AudioTrack localAudioTrack = mAudioTrack;
      if (localAudioTrack == null) {
        return;
      }
      if ((mBytesWritten < mAudioBufferSize) && (!mStopped))
      {
        mIsShortUtterance = true;
        localAudioTrack.stop();
      }
      if (!mStopped) {
        blockUntilDone(mAudioTrack);
      }
      synchronized (mAudioTrackLock)
      {
        mAudioTrack = null;
        localAudioTrack.release();
        return;
      }
    }
  }
  
  public int write(byte[] paramArrayOfByte)
  {
    synchronized (mAudioTrackLock)
    {
      AudioTrack localAudioTrack = mAudioTrack;
      if ((localAudioTrack != null) && (!mStopped))
      {
        int i = writeToAudioTrack(localAudioTrack, paramArrayOfByte);
        mBytesWritten += i;
        return i;
      }
      return -1;
    }
  }
}
