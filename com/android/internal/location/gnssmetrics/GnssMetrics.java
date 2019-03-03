package com.android.internal.location.gnssmetrics;

import android.os.SystemClock;
import android.os.connectivity.GpsBatteryStats;
import android.util.Base64;
import android.util.Log;
import android.util.TimeUtils;
import com.android.framework.protobuf.nano.MessageNano;
import com.android.internal.app.IBatteryStats;
import com.android.internal.location.nano.GnssLogsProto.GnssLog;
import com.android.internal.location.nano.GnssLogsProto.PowerMetrics;
import java.util.Arrays;

public class GnssMetrics
{
  private static final int DEFAULT_TIME_BETWEEN_FIXES_MILLISECS = 1000;
  public static final int GPS_SIGNAL_QUALITY_GOOD = 1;
  public static final int GPS_SIGNAL_QUALITY_POOR = 0;
  public static final int NUM_GPS_SIGNAL_QUALITY_LEVELS = 2;
  private static final String TAG = GnssMetrics.class.getSimpleName();
  private Statistics locationFailureStatistics;
  private String logStartInElapsedRealTime;
  private GnssPowerMetrics mGnssPowerMetrics;
  private Statistics positionAccuracyMeterStatistics;
  private Statistics timeToFirstFixSecStatistics;
  private Statistics topFourAverageCn0Statistics;
  
  public GnssMetrics(IBatteryStats paramIBatteryStats)
  {
    mGnssPowerMetrics = new GnssPowerMetrics(paramIBatteryStats);
    locationFailureStatistics = new Statistics(null);
    timeToFirstFixSecStatistics = new Statistics(null);
    positionAccuracyMeterStatistics = new Statistics(null);
    topFourAverageCn0Statistics = new Statistics(null);
    reset();
  }
  
  private void reset()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    TimeUtils.formatDuration(SystemClock.elapsedRealtimeNanos() / 1000000L, localStringBuilder);
    logStartInElapsedRealTime = localStringBuilder.toString();
    locationFailureStatistics.reset();
    timeToFirstFixSecStatistics.reset();
    positionAccuracyMeterStatistics.reset();
    topFourAverageCn0Statistics.reset();
  }
  
  public String dumpGnssMetricsAsProtoString()
  {
    Object localObject = new GnssLogsProto.GnssLog();
    if (locationFailureStatistics.getCount() > 0)
    {
      numLocationReportProcessed = locationFailureStatistics.getCount();
      percentageLocationFailure = ((int)(100.0D * locationFailureStatistics.getMean()));
    }
    if (timeToFirstFixSecStatistics.getCount() > 0)
    {
      numTimeToFirstFixProcessed = timeToFirstFixSecStatistics.getCount();
      meanTimeToFirstFixSecs = ((int)timeToFirstFixSecStatistics.getMean());
      standardDeviationTimeToFirstFixSecs = ((int)timeToFirstFixSecStatistics.getStandardDeviation());
    }
    if (positionAccuracyMeterStatistics.getCount() > 0)
    {
      numPositionAccuracyProcessed = positionAccuracyMeterStatistics.getCount();
      meanPositionAccuracyMeters = ((int)positionAccuracyMeterStatistics.getMean());
      standardDeviationPositionAccuracyMeters = ((int)positionAccuracyMeterStatistics.getStandardDeviation());
    }
    if (topFourAverageCn0Statistics.getCount() > 0)
    {
      numTopFourAverageCn0Processed = topFourAverageCn0Statistics.getCount();
      meanTopFourAverageCn0DbHz = topFourAverageCn0Statistics.getMean();
      standardDeviationTopFourAverageCn0DbHz = topFourAverageCn0Statistics.getStandardDeviation();
    }
    powerMetrics = mGnssPowerMetrics.buildProto();
    localObject = Base64.encodeToString(GnssLogsProto.GnssLog.toByteArray((MessageNano)localObject), 0);
    reset();
    return localObject;
  }
  
  public String dumpGnssMetricsAsText()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("GNSS_KPI_START");
    localStringBuilder1.append('\n');
    localStringBuilder1.append("  KPI logging start time: ");
    localStringBuilder1.append(logStartInElapsedRealTime);
    localStringBuilder1.append("\n");
    localStringBuilder1.append("  KPI logging end time: ");
    TimeUtils.formatDuration(SystemClock.elapsedRealtimeNanos() / 1000000L, localStringBuilder1);
    localStringBuilder1.append("\n");
    localStringBuilder1.append("  Number of location reports: ");
    localStringBuilder1.append(locationFailureStatistics.getCount());
    localStringBuilder1.append("\n");
    if (locationFailureStatistics.getCount() > 0)
    {
      localStringBuilder1.append("  Percentage location failure: ");
      localStringBuilder1.append(100.0D * locationFailureStatistics.getMean());
      localStringBuilder1.append("\n");
    }
    localStringBuilder1.append("  Number of TTFF reports: ");
    localStringBuilder1.append(timeToFirstFixSecStatistics.getCount());
    localStringBuilder1.append("\n");
    if (timeToFirstFixSecStatistics.getCount() > 0)
    {
      localStringBuilder1.append("  TTFF mean (sec): ");
      localStringBuilder1.append(timeToFirstFixSecStatistics.getMean());
      localStringBuilder1.append("\n");
      localStringBuilder1.append("  TTFF standard deviation (sec): ");
      localStringBuilder1.append(timeToFirstFixSecStatistics.getStandardDeviation());
      localStringBuilder1.append("\n");
    }
    localStringBuilder1.append("  Number of position accuracy reports: ");
    localStringBuilder1.append(positionAccuracyMeterStatistics.getCount());
    localStringBuilder1.append("\n");
    if (positionAccuracyMeterStatistics.getCount() > 0)
    {
      localStringBuilder1.append("  Position accuracy mean (m): ");
      localStringBuilder1.append(positionAccuracyMeterStatistics.getMean());
      localStringBuilder1.append("\n");
      localStringBuilder1.append("  Position accuracy standard deviation (m): ");
      localStringBuilder1.append(positionAccuracyMeterStatistics.getStandardDeviation());
      localStringBuilder1.append("\n");
    }
    localStringBuilder1.append("  Number of CN0 reports: ");
    localStringBuilder1.append(topFourAverageCn0Statistics.getCount());
    localStringBuilder1.append("\n");
    if (topFourAverageCn0Statistics.getCount() > 0)
    {
      localStringBuilder1.append("  Top 4 Avg CN0 mean (dB-Hz): ");
      localStringBuilder1.append(topFourAverageCn0Statistics.getMean());
      localStringBuilder1.append("\n");
      localStringBuilder1.append("  Top 4 Avg CN0 standard deviation (dB-Hz): ");
      localStringBuilder1.append(topFourAverageCn0Statistics.getStandardDeviation());
      localStringBuilder1.append("\n");
    }
    localStringBuilder1.append("GNSS_KPI_END");
    localStringBuilder1.append("\n");
    GpsBatteryStats localGpsBatteryStats = mGnssPowerMetrics.getGpsBatteryStats();
    if (localGpsBatteryStats != null)
    {
      localStringBuilder1.append("Power Metrics");
      localStringBuilder1.append("\n");
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("  Time on battery (min): ");
      ((StringBuilder)localObject).append(localGpsBatteryStats.getLoggingDurationMs() / 60000.0D);
      localStringBuilder1.append(((StringBuilder)localObject).toString());
      localStringBuilder1.append("\n");
      localObject = localGpsBatteryStats.getTimeInGpsSignalQualityLevel();
      if ((localObject != null) && (localObject.length == 2))
      {
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("  Amount of time (while on battery) Top 4 Avg CN0 > ");
        localStringBuilder2.append(Double.toString(20.0D));
        localStringBuilder2.append(" dB-Hz (min): ");
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder1.append(localObject[1] / 60000.0D);
        localStringBuilder1.append("\n");
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("  Amount of time (while on battery) Top 4 Avg CN0 <= ");
        localStringBuilder2.append(Double.toString(20.0D));
        localStringBuilder2.append(" dB-Hz (min): ");
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder1.append(localObject[0] / 60000.0D);
        localStringBuilder1.append("\n");
      }
      localStringBuilder1.append("  Energy consumed while on battery (mAh): ");
      localStringBuilder1.append(localGpsBatteryStats.getEnergyConsumedMaMs() / 3600000.0D);
      localStringBuilder1.append("\n");
    }
    return localStringBuilder1.toString();
  }
  
  public void logCn0(float[] paramArrayOfFloat, int paramInt)
  {
    if ((paramInt != 0) && (paramArrayOfFloat != null) && (paramArrayOfFloat.length != 0) && (paramArrayOfFloat.length >= paramInt))
    {
      paramArrayOfFloat = Arrays.copyOf(paramArrayOfFloat, paramInt);
      Arrays.sort(paramArrayOfFloat);
      mGnssPowerMetrics.reportSignalQuality(paramArrayOfFloat, paramInt);
      if (paramInt < 4) {
        return;
      }
      if (paramArrayOfFloat[(paramInt - 4)] > 0.0D)
      {
        double d = 0.0D;
        for (int i = paramInt - 4; i < paramInt; i++) {
          d += paramArrayOfFloat[i];
        }
        d /= 4.0D;
        topFourAverageCn0Statistics.addItem(d);
      }
      return;
    }
    if (paramInt == 0) {
      mGnssPowerMetrics.reportSignalQuality(null, 0);
    }
  }
  
  public void logMissedReports(int paramInt1, int paramInt2)
  {
    paramInt2 = paramInt2 / Math.max(1000, paramInt1) - 1;
    if (paramInt2 > 0) {
      for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
        locationFailureStatistics.addItem(1.0D);
      }
    }
  }
  
  public void logPositionAccuracyMeters(float paramFloat)
  {
    positionAccuracyMeterStatistics.addItem(paramFloat);
  }
  
  public void logReceivedLocationStatus(boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      locationFailureStatistics.addItem(1.0D);
      return;
    }
    locationFailureStatistics.addItem(0.0D);
  }
  
  public void logTimeToFirstFixMilliSecs(int paramInt)
  {
    timeToFirstFixSecStatistics.addItem(paramInt / 1000);
  }
  
  private class GnssPowerMetrics
  {
    public static final double POOR_TOP_FOUR_AVG_CN0_THRESHOLD_DB_HZ = 20.0D;
    private static final double REPORTING_THRESHOLD_DB_HZ = 1.0D;
    private final IBatteryStats mBatteryStats;
    private double mLastAverageCn0;
    
    public GnssPowerMetrics(IBatteryStats paramIBatteryStats)
    {
      mBatteryStats = paramIBatteryStats;
      mLastAverageCn0 = -100.0D;
    }
    
    private int getSignalLevel(double paramDouble)
    {
      if (paramDouble > 20.0D) {
        return 1;
      }
      return 0;
    }
    
    public GnssLogsProto.PowerMetrics buildProto()
    {
      GnssLogsProto.PowerMetrics localPowerMetrics = new GnssLogsProto.PowerMetrics();
      Object localObject = mGnssPowerMetrics.getGpsBatteryStats();
      if (localObject != null)
      {
        loggingDurationMs = ((GpsBatteryStats)localObject).getLoggingDurationMs();
        energyConsumedMah = (((GpsBatteryStats)localObject).getEnergyConsumedMaMs() / 3600000.0D);
        localObject = ((GpsBatteryStats)localObject).getTimeInGpsSignalQualityLevel();
        timeInSignalQualityLevelMs = new long[localObject.length];
        for (int i = 0; i < localObject.length; i++) {
          timeInSignalQualityLevelMs[i] = localObject[i];
        }
      }
      return localPowerMetrics;
    }
    
    public GpsBatteryStats getGpsBatteryStats()
    {
      try
      {
        GpsBatteryStats localGpsBatteryStats = mBatteryStats.getGpsBatteryStats();
        return localGpsBatteryStats;
      }
      catch (Exception localException)
      {
        Log.w(GnssMetrics.TAG, "Exception", localException);
      }
      return null;
    }
    
    public void reportSignalQuality(float[] paramArrayOfFloat, int paramInt)
    {
      double d1 = 0.0D;
      double d2 = d1;
      if (paramInt > 0)
      {
        for (int i = Math.max(0, paramInt - 4); i < paramInt; i++) {
          d1 += paramArrayOfFloat[i];
        }
        d2 = d1 / Math.min(paramInt, 4);
      }
      if (Math.abs(d2 - mLastAverageCn0) < 1.0D) {
        return;
      }
      try
      {
        mBatteryStats.noteGpsSignalQuality(getSignalLevel(d2));
        mLastAverageCn0 = d2;
      }
      catch (Exception paramArrayOfFloat)
      {
        Log.w(GnssMetrics.TAG, "Exception", paramArrayOfFloat);
      }
    }
  }
  
  private class Statistics
  {
    private int count;
    private double sum;
    private double sumSquare;
    
    private Statistics() {}
    
    public void addItem(double paramDouble)
    {
      count += 1;
      sum += paramDouble;
      sumSquare += paramDouble * paramDouble;
    }
    
    public int getCount()
    {
      return count;
    }
    
    public double getMean()
    {
      return sum / count;
    }
    
    public double getStandardDeviation()
    {
      double d1 = sum / count;
      d1 *= d1;
      double d2 = sumSquare / count;
      if (d2 > d1) {
        return Math.sqrt(d2 - d1);
      }
      return 0.0D;
    }
    
    public void reset()
    {
      count = 0;
      sum = 0.0D;
      sumSquare = 0.0D;
    }
  }
}
