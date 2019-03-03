package android.util;

import java.util.Calendar;

public class MonthDisplayHelper
{
  private Calendar mCalendar;
  private int mNumDaysInMonth;
  private int mNumDaysInPrevMonth;
  private int mOffset;
  private final int mWeekStartDay;
  
  public MonthDisplayHelper(int paramInt1, int paramInt2)
  {
    this(paramInt1, paramInt2, 1);
  }
  
  public MonthDisplayHelper(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt3 >= 1) && (paramInt3 <= 7))
    {
      mWeekStartDay = paramInt3;
      mCalendar = Calendar.getInstance();
      mCalendar.set(1, paramInt1);
      mCalendar.set(2, paramInt2);
      mCalendar.set(5, 1);
      mCalendar.set(11, 0);
      mCalendar.set(12, 0);
      mCalendar.set(13, 0);
      mCalendar.getTimeInMillis();
      recalculate();
      return;
    }
    throw new IllegalArgumentException();
  }
  
  private void recalculate()
  {
    mNumDaysInMonth = mCalendar.getActualMaximum(5);
    mCalendar.add(2, -1);
    mNumDaysInPrevMonth = mCalendar.getActualMaximum(5);
    mCalendar.add(2, 1);
    int i = getFirstDayOfMonth() - mWeekStartDay;
    int j = i;
    if (i < 0) {
      j = i + 7;
    }
    mOffset = j;
  }
  
  public int getColumnOf(int paramInt)
  {
    return (mOffset + paramInt - 1) % 7;
  }
  
  public int getDayAt(int paramInt1, int paramInt2)
  {
    if ((paramInt1 == 0) && (paramInt2 < mOffset)) {
      return mNumDaysInPrevMonth + paramInt2 - mOffset + 1;
    }
    paramInt1 = 7 * paramInt1 + paramInt2 - mOffset + 1;
    if (paramInt1 > mNumDaysInMonth) {
      paramInt1 -= mNumDaysInMonth;
    }
    return paramInt1;
  }
  
  public int[] getDigitsForRow(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 5))
    {
      localObject = new int[7];
      for (int i = 0; i < 7; i++) {
        localObject[i] = getDayAt(paramInt, i);
      }
      return localObject;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("row ");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(" out of range (0-5)");
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public int getFirstDayOfMonth()
  {
    return mCalendar.get(7);
  }
  
  public int getMonth()
  {
    return mCalendar.get(2);
  }
  
  public int getNumberOfDaysInMonth()
  {
    return mNumDaysInMonth;
  }
  
  public int getOffset()
  {
    return mOffset;
  }
  
  public int getRowOf(int paramInt)
  {
    return (mOffset + paramInt - 1) / 7;
  }
  
  public int getWeekStartDay()
  {
    return mWeekStartDay;
  }
  
  public int getYear()
  {
    return mCalendar.get(1);
  }
  
  public boolean isWithinCurrentMonth(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 <= 5) && (paramInt2 <= 6))
    {
      if ((paramInt1 == 0) && (paramInt2 < mOffset)) {
        return false;
      }
      return 7 * paramInt1 + paramInt2 - mOffset + 1 <= mNumDaysInMonth;
    }
    return false;
  }
  
  public void nextMonth()
  {
    mCalendar.add(2, 1);
    recalculate();
  }
  
  public void previousMonth()
  {
    mCalendar.add(2, -1);
    recalculate();
  }
}
