package android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.icu.util.Calendar;
import android.util.AttributeSet;

class CalendarViewMaterialDelegate
  extends CalendarView.AbstractCalendarViewDelegate
{
  private final DayPickerView mDayPickerView;
  private CalendarView.OnDateChangeListener mOnDateChangeListener;
  private final DayPickerView.OnDaySelectedListener mOnDaySelectedListener = new DayPickerView.OnDaySelectedListener()
  {
    public void onDaySelected(DayPickerView paramAnonymousDayPickerView, Calendar paramAnonymousCalendar)
    {
      if (mOnDateChangeListener != null)
      {
        int i = paramAnonymousCalendar.get(1);
        int j = paramAnonymousCalendar.get(2);
        int k = paramAnonymousCalendar.get(5);
        mOnDateChangeListener.onSelectedDayChange(mDelegator, i, j, k);
      }
    }
  };
  
  public CalendarViewMaterialDelegate(CalendarView paramCalendarView, Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramCalendarView, paramContext);
    mDayPickerView = new DayPickerView(paramContext, paramAttributeSet, paramInt1, paramInt2);
    mDayPickerView.setOnDaySelectedListener(mOnDaySelectedListener);
    paramCalendarView.addView(mDayPickerView);
  }
  
  public boolean getBoundsForDate(long paramLong, Rect paramRect)
  {
    if (mDayPickerView.getBoundsForDate(paramLong, paramRect))
    {
      int[] arrayOfInt1 = new int[2];
      int[] arrayOfInt2 = new int[2];
      mDayPickerView.getLocationOnScreen(arrayOfInt1);
      mDelegator.getLocationOnScreen(arrayOfInt2);
      int i = arrayOfInt1[1] - arrayOfInt2[1];
      top += i;
      bottom += i;
      return true;
    }
    return false;
  }
  
  public long getDate()
  {
    return mDayPickerView.getDate();
  }
  
  public int getDateTextAppearance()
  {
    return mDayPickerView.getDayTextAppearance();
  }
  
  public int getFirstDayOfWeek()
  {
    return mDayPickerView.getFirstDayOfWeek();
  }
  
  public long getMaxDate()
  {
    return mDayPickerView.getMaxDate();
  }
  
  public long getMinDate()
  {
    return mDayPickerView.getMinDate();
  }
  
  public int getWeekDayTextAppearance()
  {
    return mDayPickerView.getDayOfWeekTextAppearance();
  }
  
  public void setDate(long paramLong)
  {
    mDayPickerView.setDate(paramLong, true);
  }
  
  public void setDate(long paramLong, boolean paramBoolean1, boolean paramBoolean2)
  {
    mDayPickerView.setDate(paramLong, paramBoolean1);
  }
  
  public void setDateTextAppearance(int paramInt)
  {
    mDayPickerView.setDayTextAppearance(paramInt);
  }
  
  public void setFirstDayOfWeek(int paramInt)
  {
    mDayPickerView.setFirstDayOfWeek(paramInt);
  }
  
  public void setMaxDate(long paramLong)
  {
    mDayPickerView.setMaxDate(paramLong);
  }
  
  public void setMinDate(long paramLong)
  {
    mDayPickerView.setMinDate(paramLong);
  }
  
  public void setOnDateChangeListener(CalendarView.OnDateChangeListener paramOnDateChangeListener)
  {
    mOnDateChangeListener = paramOnDateChangeListener;
  }
  
  public void setWeekDayTextAppearance(int paramInt)
  {
    mDayPickerView.setDayOfWeekTextAppearance(paramInt);
  }
}
