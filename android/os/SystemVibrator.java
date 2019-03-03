package android.os;

import android.content.Context;
import android.media.AudioAttributes;
import android.util.Log;

public class SystemVibrator
  extends Vibrator
{
  private static final String TAG = "Vibrator";
  private final IVibratorService mService = IVibratorService.Stub.asInterface(ServiceManager.getService("vibrator"));
  private final Binder mToken = new Binder();
  
  public SystemVibrator() {}
  
  public SystemVibrator(Context paramContext)
  {
    super(paramContext);
  }
  
  private static int usageForAttributes(AudioAttributes paramAudioAttributes)
  {
    int i;
    if (paramAudioAttributes != null) {
      i = paramAudioAttributes.getUsage();
    } else {
      i = 0;
    }
    return i;
  }
  
  public void cancel()
  {
    if (mService == null) {
      return;
    }
    try
    {
      mService.cancelVibrate(mToken);
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("Vibrator", "Failed to cancel vibration.", localRemoteException);
    }
  }
  
  public boolean hasAmplitudeControl()
  {
    if (mService == null)
    {
      Log.w("Vibrator", "Failed to check amplitude control; no vibrator service.");
      return false;
    }
    try
    {
      boolean bool = mService.hasAmplitudeControl();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean hasVibrator()
  {
    if (mService == null)
    {
      Log.w("Vibrator", "Failed to vibrate; no vibrator service.");
      return false;
    }
    try
    {
      boolean bool = mService.hasVibrator();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public void vibrate(int paramInt, String paramString, VibrationEffect paramVibrationEffect, AudioAttributes paramAudioAttributes)
  {
    if (mService == null)
    {
      Log.w("Vibrator", "Failed to vibrate; no vibrator service.");
      return;
    }
    try
    {
      mService.vibrate(paramInt, paramString, paramVibrationEffect, usageForAttributes(paramAudioAttributes), mToken);
    }
    catch (RemoteException paramString)
    {
      Log.w("Vibrator", "Failed to vibrate.", paramString);
    }
  }
}
