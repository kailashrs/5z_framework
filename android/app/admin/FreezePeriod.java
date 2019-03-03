package android.app.admin;

import android.util.Log;
import android.util.Pair;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FreezePeriod
{
  static final int DAYS_IN_YEAR = 365;
  private static final int DUMMY_YEAR = 2001;
  private static final String TAG = "FreezePeriod";
  private final MonthDay mEnd;
  private final int mEndDay;
  private final MonthDay mStart;
  private final int mStartDay;
  
  private FreezePeriod(int paramInt1, int paramInt2)
  {
    mStartDay = paramInt1;
    mStart = dayOfYearToMonthDay(paramInt1);
    mEndDay = paramInt2;
    mEnd = dayOfYearToMonthDay(paramInt2);
  }
  
  public FreezePeriod(MonthDay paramMonthDay1, MonthDay paramMonthDay2)
  {
    mStart = paramMonthDay1;
    mStartDay = mStart.atYear(2001).getDayOfYear();
    mEnd = paramMonthDay2;
    mEndDay = mEnd.atYear(2001).getDayOfYear();
  }
  
  static List<FreezePeriod> canonicalizePeriods(List<FreezePeriod> paramList)
  {
    boolean[] arrayOfBoolean = new boolean['ŭ'];
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      FreezePeriod localFreezePeriod = (FreezePeriod)paramList.next();
      for (i = mStartDay; i <= localFreezePeriod.getEffectiveEndDay(); i++) {
        arrayOfBoolean[((i - 1) % 365)] = true;
      }
    }
    paramList = new ArrayList();
    int i = 0;
    for (;;)
    {
      int j = i;
      if (j >= 365) {
        break;
      }
      if (arrayOfBoolean[j] == 0)
      {
        i = j + 1;
      }
      else
      {
        for (i = j; (i < 365) && (arrayOfBoolean[i] != 0); i++) {}
        paramList.add(new FreezePeriod(j + 1, i));
      }
    }
    i = paramList.size() - 1;
    if ((i > 0) && (getmEndDay == 365) && (get0mStartDay == 1))
    {
      paramList.set(i, new FreezePeriod(getmStartDay, get0mEndDay));
      paramList.remove(0);
    }
    return paramList;
  }
  
  private static int dayOfYearDisregardLeapYear(LocalDate paramLocalDate)
  {
    return paramLocalDate.withYear(2001).getDayOfYear();
  }
  
  private static MonthDay dayOfYearToMonthDay(int paramInt)
  {
    LocalDate localLocalDate = LocalDate.ofYearDay(2001, paramInt);
    return MonthDay.of(localLocalDate.getMonth(), localLocalDate.getDayOfMonth());
  }
  
  public static int distanceWithoutLeapYear(LocalDate paramLocalDate1, LocalDate paramLocalDate2)
  {
    return dayOfYearDisregardLeapYear(paramLocalDate1) - dayOfYearDisregardLeapYear(paramLocalDate2) + 365 * (paramLocalDate1.getYear() - paramLocalDate2.getYear());
  }
  
  static void validateAgainstPreviousFreezePeriod(List<FreezePeriod> paramList, LocalDate paramLocalDate1, LocalDate paramLocalDate2, LocalDate paramLocalDate3)
  {
    if ((paramList.size() != 0) && (paramLocalDate1 != null) && (paramLocalDate2 != null))
    {
      if ((paramLocalDate1.isAfter(paramLocalDate3)) || (paramLocalDate2.isAfter(paramLocalDate3)))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Previous period (");
        ((StringBuilder)localObject).append(paramLocalDate1);
        ((StringBuilder)localObject).append(",");
        ((StringBuilder)localObject).append(paramLocalDate2);
        ((StringBuilder)localObject).append(") is after current date ");
        ((StringBuilder)localObject).append(paramLocalDate3);
        Log.w("FreezePeriod", ((StringBuilder)localObject).toString());
      }
      paramList = canonicalizePeriods(paramList);
      Object localObject = (FreezePeriod)paramList.get(0);
      Iterator localIterator = paramList.iterator();
      for (;;)
      {
        paramList = (List<FreezePeriod>)localObject;
        if (!localIterator.hasNext()) {
          break;
        }
        paramList = (FreezePeriod)localIterator.next();
        if ((paramList.contains(paramLocalDate3)) || (mStartDay > dayOfYearDisregardLeapYear(paramLocalDate3))) {
          break;
        }
      }
      localObject = paramList.toCurrentOrFutureRealDates(paramLocalDate3);
      paramList = (List<FreezePeriod>)localObject;
      if (paramLocalDate3.isAfter((ChronoLocalDate)first)) {
        paramList = new Pair(paramLocalDate3, (LocalDate)second);
      }
      if (!((LocalDate)first).isAfter((ChronoLocalDate)second))
      {
        paramLocalDate3 = new StringBuilder();
        paramLocalDate3.append("Prev: ");
        paramLocalDate3.append(paramLocalDate1);
        paramLocalDate3.append(",");
        paramLocalDate3.append(paramLocalDate2);
        paramLocalDate3.append("; cur: ");
        paramLocalDate3.append(first);
        paramLocalDate3.append(",");
        paramLocalDate3.append(second);
        paramLocalDate3 = paramLocalDate3.toString();
        long l = distanceWithoutLeapYear((LocalDate)first, paramLocalDate2) - 1;
        if (l > 0L)
        {
          if (l < 60L)
          {
            paramList = new StringBuilder();
            paramList.append("Previous freeze period too close to new period: ");
            paramList.append(l);
            paramList.append(", ");
            paramList.append(paramLocalDate3);
            throw SystemUpdatePolicy.ValidationFailedException.combinedPeriodTooClose(paramList.toString());
          }
        }
        else
        {
          l = distanceWithoutLeapYear((LocalDate)second, paramLocalDate1) + 1;
          if (l > 90L) {
            break label412;
          }
        }
        return;
        label412:
        paramList = new StringBuilder();
        paramList.append("Combined freeze period exceeds maximum days: ");
        paramList.append(l);
        paramList.append(", ");
        paramList.append(paramLocalDate3);
        throw SystemUpdatePolicy.ValidationFailedException.combinedPeriodTooLong(paramList.toString());
      }
      paramLocalDate1 = new StringBuilder();
      paramLocalDate1.append("Current freeze dates inverted: ");
      paramLocalDate1.append(first);
      paramLocalDate1.append("-");
      paramLocalDate1.append(second);
      throw new IllegalStateException(paramLocalDate1.toString());
    }
  }
  
  static void validatePeriods(List<FreezePeriod> paramList)
  {
    Object localObject = canonicalizePeriods(paramList);
    if (((List)localObject).size() == paramList.size())
    {
      int i = 0;
      while (i < ((List)localObject).size())
      {
        FreezePeriod localFreezePeriod = (FreezePeriod)((List)localObject).get(i);
        if (localFreezePeriod.getLength() <= 90)
        {
          if (i > 0) {
            paramList = (FreezePeriod)((List)localObject).get(i - 1);
          } else {
            paramList = (FreezePeriod)((List)localObject).get(((List)localObject).size() - 1);
          }
          if (paramList != localFreezePeriod)
          {
            int j;
            if ((i == 0) && (!paramList.isWrapped())) {
              j = mStartDay + (365 - mEndDay) - 1;
            } else {
              j = mStartDay - mEndDay - 1;
            }
            if (j < 60)
            {
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("Freeze periods ");
              ((StringBuilder)localObject).append(paramList);
              ((StringBuilder)localObject).append(" and ");
              ((StringBuilder)localObject).append(localFreezePeriod);
              ((StringBuilder)localObject).append(" are too close together: ");
              ((StringBuilder)localObject).append(j);
              ((StringBuilder)localObject).append(" days apart");
              throw SystemUpdatePolicy.ValidationFailedException.freezePeriodTooClose(((StringBuilder)localObject).toString());
            }
          }
          i++;
        }
        else
        {
          paramList = new StringBuilder();
          paramList.append("Freeze period ");
          paramList.append(localFreezePeriod);
          paramList.append(" is too long: ");
          paramList.append(localFreezePeriod.getLength());
          paramList.append(" days");
          throw SystemUpdatePolicy.ValidationFailedException.freezePeriodTooLong(paramList.toString());
        }
      }
      return;
    }
    throw SystemUpdatePolicy.ValidationFailedException.duplicateOrOverlapPeriods();
  }
  
  boolean after(LocalDate paramLocalDate)
  {
    boolean bool;
    if (mStartDay > dayOfYearDisregardLeapYear(paramLocalDate)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean contains(LocalDate paramLocalDate)
  {
    int i = dayOfYearDisregardLeapYear(paramLocalDate);
    boolean bool1 = isWrapped();
    boolean bool2 = false;
    boolean bool3 = false;
    if (!bool1)
    {
      bool2 = bool3;
      if (mStartDay <= i)
      {
        bool2 = bool3;
        if (i <= mEndDay) {
          bool2 = true;
        }
      }
      return bool2;
    }
    if ((mStartDay > i) && (i > mEndDay)) {
      break label75;
    }
    bool2 = true;
    label75:
    return bool2;
  }
  
  int getEffectiveEndDay()
  {
    if (!isWrapped()) {
      return mEndDay;
    }
    return mEndDay + 365;
  }
  
  public MonthDay getEnd()
  {
    return mEnd;
  }
  
  int getLength()
  {
    return getEffectiveEndDay() - mStartDay + 1;
  }
  
  public MonthDay getStart()
  {
    return mStart;
  }
  
  boolean isWrapped()
  {
    boolean bool;
    if (mEndDay < mStartDay) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  Pair<LocalDate, LocalDate> toCurrentOrFutureRealDates(LocalDate paramLocalDate)
  {
    int i = dayOfYearDisregardLeapYear(paramLocalDate);
    int j;
    if (contains(paramLocalDate)) {
      if (mStartDay <= i)
      {
        i = 0;
        j = isWrapped();
      }
      else
      {
        i = -1;
        j = 0;
      }
    }
    for (;;)
    {
      break;
      if (mStartDay > i)
      {
        i = 0;
        j = isWrapped();
      }
      else
      {
        i = 1;
        j = 1;
      }
    }
    return new Pair(LocalDate.ofYearDay(2001, mStartDay).withYear(paramLocalDate.getYear() + i), LocalDate.ofYearDay(2001, mEndDay).withYear(paramLocalDate.getYear() + j));
  }
  
  public String toString()
  {
    DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(LocalDate.ofYearDay(2001, mStartDay).format(localDateTimeFormatter));
    localStringBuilder.append(" - ");
    localStringBuilder.append(LocalDate.ofYearDay(2001, mEndDay).format(localDateTimeFormatter));
    return localStringBuilder.toString();
  }
}
