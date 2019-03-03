package android.widget;

import java.util.Calendar;

abstract interface DatePickerController
{
  public abstract Calendar getSelectedDay();
  
  public abstract void onYearSelected(int paramInt);
  
  public abstract void registerOnDateChangedListener(OnDateChangedListener paramOnDateChangedListener);
  
  public abstract void tryVibrate();
}
