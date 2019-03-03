package android.os;

import android.media.AudioAttributes;

public class NullVibrator
  extends Vibrator
{
  private static final NullVibrator sInstance = new NullVibrator();
  
  private NullVibrator() {}
  
  public static NullVibrator getInstance()
  {
    return sInstance;
  }
  
  public void cancel() {}
  
  public boolean hasAmplitudeControl()
  {
    return false;
  }
  
  public boolean hasVibrator()
  {
    return false;
  }
  
  public void vibrate(int paramInt, String paramString, VibrationEffect paramVibrationEffect, AudioAttributes paramAudioAttributes) {}
}
