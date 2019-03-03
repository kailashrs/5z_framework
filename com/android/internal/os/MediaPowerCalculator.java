package com.android.internal.os;

import android.os.BatteryStats.Timer;
import android.os.BatteryStats.Uid;

public class MediaPowerCalculator
  extends PowerCalculator
{
  private static final int MS_IN_HR = 3600000;
  private final double mAudioAveragePowerMa;
  private final double mVideoAveragePowerMa;
  
  public MediaPowerCalculator(PowerProfile paramPowerProfile)
  {
    mAudioAveragePowerMa = paramPowerProfile.getAveragePower("audio");
    mVideoAveragePowerMa = paramPowerProfile.getAveragePower("video");
  }
  
  public void calculateApp(BatterySipper paramBatterySipper, BatteryStats.Uid paramUid, long paramLong1, long paramLong2, int paramInt)
  {
    BatteryStats.Timer localTimer = paramUid.getAudioTurnedOnTimer();
    if (localTimer == null)
    {
      audioTimeMs = 0L;
      audioPowerMah = 0.0D;
    }
    else
    {
      paramLong2 = localTimer.getTotalTimeLocked(paramLong1, paramInt) / 1000L;
      audioTimeMs = paramLong2;
      audioPowerMah = (paramLong2 * mAudioAveragePowerMa / 3600000.0D);
    }
    paramUid = paramUid.getVideoTurnedOnTimer();
    if (paramUid == null)
    {
      videoTimeMs = 0L;
      videoPowerMah = 0.0D;
    }
    else
    {
      paramLong1 = paramUid.getTotalTimeLocked(paramLong1, paramInt) / 1000L;
      videoTimeMs = paramLong1;
      videoPowerMah = (paramLong1 * mVideoAveragePowerMa / 3600000.0D);
    }
  }
}
