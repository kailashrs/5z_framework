package android.service.notification;

import android.util.ArraySet;
import android.util.Log;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class ScheduleCalendar
{
  public static final boolean DEBUG = Log.isLoggable("ConditionProviders", 3);
  public static final String TAG = "ScheduleCalendar";
  private final Calendar mCalendar = Calendar.getInstance();
  private final ArraySet<Integer> mDays = new ArraySet();
  private ZenModeConfig.ScheduleInfo mSchedule;
  
  public ScheduleCalendar() {}
  
  private long addDays(long paramLong, int paramInt)
  {
    mCalendar.setTimeInMillis(paramLong);
    mCalendar.add(5, paramInt);
    return mCalendar.getTimeInMillis();
  }
  
  private int getDayOfWeek(long paramLong)
  {
    mCalendar.setTimeInMillis(paramLong);
    return mCalendar.get(7);
  }
  
  private long getNextTime(long paramLong, int paramInt1, int paramInt2)
  {
    long l = getTime(paramLong, paramInt1, paramInt2);
    if (l <= paramLong) {
      paramLong = addDays(l, 1);
    } else {
      paramLong = l;
    }
    return paramLong;
  }
  
  private long getTime(long paramLong, int paramInt1, int paramInt2)
  {
    mCalendar.setTimeInMillis(paramLong);
    mCalendar.set(11, paramInt1);
    mCalendar.set(12, paramInt2);
    mCalendar.set(13, 0);
    mCalendar.set(14, 0);
    return mCalendar.getTimeInMillis();
  }
  
  private boolean isInSchedule(int paramInt, long paramLong1, long paramLong2, long paramLong3)
  {
    int i = getDayOfWeek(paramLong1);
    boolean bool = true;
    paramLong2 = addDays(paramLong2, paramInt);
    paramLong3 = addDays(paramLong3, paramInt);
    if ((!mDays.contains(Integer.valueOf((i - 1 + paramInt % 7 + 7) % 7 + 1))) || (paramLong1 < paramLong2) || (paramLong1 >= paramLong3)) {
      bool = false;
    }
    return bool;
  }
  
  private void updateDays()
  {
    mDays.clear();
    if ((mSchedule != null) && (mSchedule.days != null)) {
      for (int i = 0; i < mSchedule.days.length; i++) {
        mDays.add(Integer.valueOf(mSchedule.days[i]));
      }
    }
  }
  
  public boolean exitAtAlarm()
  {
    return mSchedule.exitAtAlarm;
  }
  
  public long getNextChangeTime(long paramLong)
  {
    if (mSchedule == null) {
      return 0L;
    }
    return Math.min(getNextTime(paramLong, mSchedule.startHour, mSchedule.startMinute), getNextTime(paramLong, mSchedule.endHour, mSchedule.endMinute));
  }
  
  public boolean isAlarmInSchedule(long paramLong1, long paramLong2)
  {
    ZenModeConfig.ScheduleInfo localScheduleInfo = mSchedule;
    boolean bool = false;
    if ((localScheduleInfo != null) && (mDays.size() != 0))
    {
      long l1 = getTime(paramLong1, mSchedule.startHour, mSchedule.startMinute);
      long l2 = getTime(paramLong1, mSchedule.endHour, mSchedule.endMinute);
      long l3 = l2;
      if (l2 <= l1) {
        l3 = addDays(l2, 1);
      }
      if (((isInSchedule(-1, paramLong1, l1, l3)) && (isInSchedule(-1, paramLong2, l1, l3))) || ((isInSchedule(0, paramLong1, l1, l3)) && (isInSchedule(0, paramLong2, l1, l3)))) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  public boolean isInSchedule(long paramLong)
  {
    ZenModeConfig.ScheduleInfo localScheduleInfo = mSchedule;
    boolean bool = false;
    if ((localScheduleInfo != null) && (mDays.size() != 0))
    {
      long l1 = getTime(paramLong, mSchedule.startHour, mSchedule.startMinute);
      long l2 = getTime(paramLong, mSchedule.endHour, mSchedule.endMinute);
      long l3 = l2;
      if (l2 <= l1) {
        l3 = addDays(l2, 1);
      }
      if ((!isInSchedule(-1, paramLong, l1, l3)) && (!isInSchedule(0, paramLong, l1, l3))) {
        break label123;
      }
      bool = true;
      label123:
      return bool;
    }
    return false;
  }
  
  public void maybeSetNextAlarm(long paramLong1, long paramLong2)
  {
    if ((mSchedule != null) && (mSchedule.exitAtAlarm))
    {
      if (paramLong2 == 0L) {
        mSchedule.nextAlarm = 0L;
      }
      if (paramLong2 > paramLong1)
      {
        if (mSchedule.nextAlarm == 0L) {
          mSchedule.nextAlarm = paramLong2;
        } else {
          mSchedule.nextAlarm = Math.min(mSchedule.nextAlarm, paramLong2);
        }
      }
      else if (mSchedule.nextAlarm < paramLong1)
      {
        if (DEBUG)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("All alarms are in the past ");
          localStringBuilder.append(mSchedule.nextAlarm);
          Log.d("ScheduleCalendar", localStringBuilder.toString());
        }
        mSchedule.nextAlarm = 0L;
      }
    }
  }
  
  public void setSchedule(ZenModeConfig.ScheduleInfo paramScheduleInfo)
  {
    if (Objects.equals(mSchedule, paramScheduleInfo)) {
      return;
    }
    mSchedule = paramScheduleInfo;
    updateDays();
  }
  
  public void setTimeZone(TimeZone paramTimeZone)
  {
    mCalendar.setTimeZone(paramTimeZone);
  }
  
  public boolean shouldExitForAlarm(long paramLong)
  {
    ZenModeConfig.ScheduleInfo localScheduleInfo = mSchedule;
    boolean bool1 = false;
    if (localScheduleInfo == null) {
      return false;
    }
    boolean bool2 = bool1;
    if (mSchedule.exitAtAlarm)
    {
      bool2 = bool1;
      if (mSchedule.nextAlarm != 0L)
      {
        bool2 = bool1;
        if (paramLong >= mSchedule.nextAlarm)
        {
          bool2 = bool1;
          if (isAlarmInSchedule(mSchedule.nextAlarm, paramLong)) {
            bool2 = true;
          }
        }
      }
    }
    return bool2;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ScheduleCalendar[mDays=");
    localStringBuilder.append(mDays);
    localStringBuilder.append(", mSchedule=");
    localStringBuilder.append(mSchedule);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}
