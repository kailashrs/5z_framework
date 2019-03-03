package android.hardware.hdmi;

import android.annotation.SystemApi;
import android.util.Log;

@SystemApi
public class HdmiTimerRecordSources
{
  private static final int EXTERNAL_SOURCE_SPECIFIER_EXTERNAL_PHYSICAL_ADDRESS = 5;
  private static final int EXTERNAL_SOURCE_SPECIFIER_EXTERNAL_PLUG = 4;
  public static final int RECORDING_SEQUENCE_REPEAT_FRIDAY = 32;
  private static final int RECORDING_SEQUENCE_REPEAT_MASK = 127;
  public static final int RECORDING_SEQUENCE_REPEAT_MONDAY = 2;
  public static final int RECORDING_SEQUENCE_REPEAT_ONCE_ONLY = 0;
  public static final int RECORDING_SEQUENCE_REPEAT_SATUREDAY = 64;
  public static final int RECORDING_SEQUENCE_REPEAT_SUNDAY = 1;
  public static final int RECORDING_SEQUENCE_REPEAT_THURSDAY = 16;
  public static final int RECORDING_SEQUENCE_REPEAT_TUESDAY = 4;
  public static final int RECORDING_SEQUENCE_REPEAT_WEDNESDAY = 8;
  private static final String TAG = "HdmiTimerRecordingSources";
  
  private HdmiTimerRecordSources() {}
  
  private static void checkDurationValue(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 <= 99))
    {
      if ((paramInt2 >= 0) && (paramInt2 <= 59)) {
        return;
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("minute should be in rage of [0, 59]:");
      localStringBuilder.append(paramInt2);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Hour should be in rage of [0, 99]:");
    localStringBuilder.append(paramInt1);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static void checkTimeValue(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 <= 23))
    {
      if ((paramInt2 >= 0) && (paramInt2 <= 59)) {
        return;
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Minute should be in rage of [0, 59]:");
      localStringBuilder.append(paramInt2);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Hour should be in rage of [0, 23]:");
    localStringBuilder.append(paramInt1);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  @SystemApi
  public static boolean checkTimerRecordSource(int paramInt, byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length - 7;
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = true;
    boolean bool4 = true;
    switch (paramInt)
    {
    default: 
      return false;
    case 3: 
      paramInt = paramArrayOfByte[7];
      if (paramInt == 4)
      {
        if (2 != i) {
          bool4 = false;
        }
        return bool4;
      }
      if (paramInt == 5)
      {
        if (3 == i) {
          bool4 = bool1;
        } else {
          bool4 = false;
        }
        return bool4;
      }
      return false;
    case 2: 
      if (4 == i) {
        bool4 = bool2;
      } else {
        bool4 = false;
      }
      return bool4;
    }
    if (7 == i) {
      bool4 = bool3;
    } else {
      bool4 = false;
    }
    return bool4;
  }
  
  private static void checkTimerRecordSourceInputs(TimerInfo paramTimerInfo, HdmiRecordSources.RecordSource paramRecordSource)
  {
    if (paramTimerInfo != null)
    {
      if (paramRecordSource != null) {
        return;
      }
      Log.w("HdmiTimerRecordingSources", "source should not be null.");
      throw new IllegalArgumentException("source should not be null.");
    }
    Log.w("HdmiTimerRecordingSources", "TimerInfo should not be null.");
    throw new IllegalArgumentException("TimerInfo should not be null.");
  }
  
  public static Duration durationOf(int paramInt1, int paramInt2)
  {
    checkDurationValue(paramInt1, paramInt2);
    return new Duration(paramInt1, paramInt2, null);
  }
  
  public static TimerRecordSource ofAnalogueSource(TimerInfo paramTimerInfo, HdmiRecordSources.AnalogueServiceSource paramAnalogueServiceSource)
  {
    checkTimerRecordSourceInputs(paramTimerInfo, paramAnalogueServiceSource);
    return new TimerRecordSource(paramTimerInfo, paramAnalogueServiceSource, null);
  }
  
  public static TimerRecordSource ofDigitalSource(TimerInfo paramTimerInfo, HdmiRecordSources.DigitalServiceSource paramDigitalServiceSource)
  {
    checkTimerRecordSourceInputs(paramTimerInfo, paramDigitalServiceSource);
    return new TimerRecordSource(paramTimerInfo, paramDigitalServiceSource, null);
  }
  
  public static TimerRecordSource ofExternalPhysicalAddress(TimerInfo paramTimerInfo, HdmiRecordSources.ExternalPhysicalAddress paramExternalPhysicalAddress)
  {
    checkTimerRecordSourceInputs(paramTimerInfo, paramExternalPhysicalAddress);
    return new TimerRecordSource(paramTimerInfo, new ExternalSourceDecorator(paramExternalPhysicalAddress, 5, null), null);
  }
  
  public static TimerRecordSource ofExternalPlug(TimerInfo paramTimerInfo, HdmiRecordSources.ExternalPlugData paramExternalPlugData)
  {
    checkTimerRecordSourceInputs(paramTimerInfo, paramExternalPlugData);
    return new TimerRecordSource(paramTimerInfo, new ExternalSourceDecorator(paramExternalPlugData, 4, null), null);
  }
  
  public static Time timeOf(int paramInt1, int paramInt2)
  {
    checkTimeValue(paramInt1, paramInt2);
    return new Time(paramInt1, paramInt2, null);
  }
  
  public static TimerInfo timerInfoOf(int paramInt1, int paramInt2, Time paramTime, Duration paramDuration, int paramInt3)
  {
    if ((paramInt1 >= 0) && (paramInt1 <= 31))
    {
      if ((paramInt2 >= 1) && (paramInt2 <= 12))
      {
        checkTimeValue(mHour, mMinute);
        checkDurationValue(mHour, mMinute);
        if ((paramInt3 != 0) && ((paramInt3 & 0xFFFFFF80) != 0))
        {
          paramTime = new StringBuilder();
          paramTime.append("Invalid reecording sequence value:");
          paramTime.append(paramInt3);
          throw new IllegalArgumentException(paramTime.toString());
        }
        return new TimerInfo(paramInt1, paramInt2, paramTime, paramDuration, paramInt3, null);
      }
      paramTime = new StringBuilder();
      paramTime.append("Month of year should be in range of [1, 12]:");
      paramTime.append(paramInt2);
      throw new IllegalArgumentException(paramTime.toString());
    }
    paramTime = new StringBuilder();
    paramTime.append("Day of month should be in range of [0, 31]:");
    paramTime.append(paramInt1);
    throw new IllegalArgumentException(paramTime.toString());
  }
  
  @SystemApi
  public static final class Duration
    extends HdmiTimerRecordSources.TimeUnit
  {
    private Duration(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
  }
  
  private static class ExternalSourceDecorator
    extends HdmiRecordSources.RecordSource
  {
    private final int mExternalSourceSpecifier;
    private final HdmiRecordSources.RecordSource mRecordSource;
    
    private ExternalSourceDecorator(HdmiRecordSources.RecordSource paramRecordSource, int paramInt)
    {
      super(paramRecordSource.getDataSize(false) + 1);
      mRecordSource = paramRecordSource;
      mExternalSourceSpecifier = paramInt;
    }
    
    int extraParamToByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      paramArrayOfByte[paramInt] = ((byte)(byte)mExternalSourceSpecifier);
      mRecordSource.toByteArray(false, paramArrayOfByte, paramInt + 1);
      return getDataSize(false);
    }
  }
  
  @SystemApi
  public static final class Time
    extends HdmiTimerRecordSources.TimeUnit
  {
    private Time(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
  }
  
  static class TimeUnit
  {
    final int mHour;
    final int mMinute;
    
    TimeUnit(int paramInt1, int paramInt2)
    {
      mHour = paramInt1;
      mMinute = paramInt2;
    }
    
    static byte toBcdByte(int paramInt)
    {
      return (byte)(paramInt / 10 % 10 << 4 | paramInt % 10);
    }
    
    int toByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      paramArrayOfByte[paramInt] = toBcdByte(mHour);
      paramArrayOfByte[(paramInt + 1)] = toBcdByte(mMinute);
      return 2;
    }
  }
  
  @SystemApi
  public static final class TimerInfo
  {
    private static final int BASIC_INFO_SIZE = 7;
    private static final int DAY_OF_MONTH_SIZE = 1;
    private static final int DURATION_SIZE = 2;
    private static final int MONTH_OF_YEAR_SIZE = 1;
    private static final int RECORDING_SEQUENCE_SIZE = 1;
    private static final int START_TIME_SIZE = 2;
    private final int mDayOfMonth;
    private final HdmiTimerRecordSources.Duration mDuration;
    private final int mMonthOfYear;
    private final int mRecordingSequence;
    private final HdmiTimerRecordSources.Time mStartTime;
    
    private TimerInfo(int paramInt1, int paramInt2, HdmiTimerRecordSources.Time paramTime, HdmiTimerRecordSources.Duration paramDuration, int paramInt3)
    {
      mDayOfMonth = paramInt1;
      mMonthOfYear = paramInt2;
      mStartTime = paramTime;
      mDuration = paramDuration;
      mRecordingSequence = paramInt3;
    }
    
    int getDataSize()
    {
      return 7;
    }
    
    int toByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      paramArrayOfByte[paramInt] = ((byte)(byte)mDayOfMonth);
      paramInt++;
      paramArrayOfByte[paramInt] = ((byte)(byte)mMonthOfYear);
      paramInt++;
      paramInt += mStartTime.toByteArray(paramArrayOfByte, paramInt);
      paramArrayOfByte[(paramInt + mDuration.toByteArray(paramArrayOfByte, paramInt))] = ((byte)(byte)mRecordingSequence);
      return getDataSize();
    }
  }
  
  @SystemApi
  public static final class TimerRecordSource
  {
    private final HdmiRecordSources.RecordSource mRecordSource;
    private final HdmiTimerRecordSources.TimerInfo mTimerInfo;
    
    private TimerRecordSource(HdmiTimerRecordSources.TimerInfo paramTimerInfo, HdmiRecordSources.RecordSource paramRecordSource)
    {
      mTimerInfo = paramTimerInfo;
      mRecordSource = paramRecordSource;
    }
    
    int getDataSize()
    {
      return mTimerInfo.getDataSize() + mRecordSource.getDataSize(false);
    }
    
    int toByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      int i = mTimerInfo.toByteArray(paramArrayOfByte, paramInt);
      mRecordSource.toByteArray(false, paramArrayOfByte, paramInt + i);
      return getDataSize();
    }
  }
}
