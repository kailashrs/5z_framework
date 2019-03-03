package android.os;

import android.app.ActivityThread;
import android.app.ContextImpl;
import android.content.Context;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class Vibrator
{
  private static final String TAG = "Vibrator";
  public static final int VIBRATION_INTENSITY_HIGH = 3;
  public static final int VIBRATION_INTENSITY_LOW = 1;
  public static final int VIBRATION_INTENSITY_MEDIUM = 2;
  public static final int VIBRATION_INTENSITY_OFF = 0;
  private final int mDefaultHapticFeedbackIntensity;
  private final int mDefaultNotificationVibrationIntensity;
  private final String mPackageName;
  
  public Vibrator()
  {
    mPackageName = ActivityThread.currentPackageName();
    ContextImpl localContextImpl = ActivityThread.currentActivityThread().getSystemContext();
    mDefaultHapticFeedbackIntensity = loadDefaultIntensity(localContextImpl, 17694767);
    mDefaultNotificationVibrationIntensity = loadDefaultIntensity(localContextImpl, 17694774);
  }
  
  protected Vibrator(Context paramContext)
  {
    mPackageName = paramContext.getOpPackageName();
    mDefaultHapticFeedbackIntensity = loadDefaultIntensity(paramContext, 17694767);
    mDefaultNotificationVibrationIntensity = loadDefaultIntensity(paramContext, 17694774);
  }
  
  private int loadDefaultIntensity(Context paramContext, int paramInt)
  {
    if (paramContext != null) {
      paramInt = paramContext.getResources().getInteger(paramInt);
    } else {
      paramInt = 2;
    }
    return paramInt;
  }
  
  public abstract void cancel();
  
  public int getDefaultHapticFeedbackIntensity()
  {
    return mDefaultHapticFeedbackIntensity;
  }
  
  public int getDefaultNotificationVibrationIntensity()
  {
    return mDefaultNotificationVibrationIntensity;
  }
  
  public abstract boolean hasAmplitudeControl();
  
  public abstract boolean hasVibrator();
  
  public abstract void vibrate(int paramInt, String paramString, VibrationEffect paramVibrationEffect, AudioAttributes paramAudioAttributes);
  
  @Deprecated
  public void vibrate(long paramLong)
  {
    vibrate(paramLong, null);
  }
  
  @Deprecated
  public void vibrate(long paramLong, AudioAttributes paramAudioAttributes)
  {
    try
    {
      vibrate(VibrationEffect.createOneShot(paramLong, -1), paramAudioAttributes);
    }
    catch (IllegalArgumentException paramAudioAttributes)
    {
      Log.e("Vibrator", "Failed to create VibrationEffect", paramAudioAttributes);
    }
  }
  
  public void vibrate(VibrationEffect paramVibrationEffect)
  {
    vibrate(paramVibrationEffect, null);
  }
  
  public void vibrate(VibrationEffect paramVibrationEffect, AudioAttributes paramAudioAttributes)
  {
    vibrate(Process.myUid(), mPackageName, paramVibrationEffect, paramAudioAttributes);
  }
  
  @Deprecated
  public void vibrate(long[] paramArrayOfLong, int paramInt)
  {
    vibrate(paramArrayOfLong, paramInt, null);
  }
  
  @Deprecated
  public void vibrate(long[] paramArrayOfLong, int paramInt, AudioAttributes paramAudioAttributes)
  {
    if ((paramInt >= -1) && (paramInt < paramArrayOfLong.length))
    {
      try
      {
        vibrate(VibrationEffect.createWaveform(paramArrayOfLong, paramInt), paramAudioAttributes);
      }
      catch (IllegalArgumentException paramArrayOfLong)
      {
        Log.e("Vibrator", "Failed to create VibrationEffect", paramArrayOfLong);
      }
      return;
    }
    paramAudioAttributes = new StringBuilder();
    paramAudioAttributes.append("vibrate called with repeat index out of bounds (pattern.length=");
    paramAudioAttributes.append(paramArrayOfLong.length);
    paramAudioAttributes.append(", index=");
    paramAudioAttributes.append(paramInt);
    paramAudioAttributes.append(")");
    Log.e("Vibrator", paramAudioAttributes.toString());
    throw new ArrayIndexOutOfBoundsException();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface VibrationIntensity {}
}
