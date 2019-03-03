package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.R.styleable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CalendarView
  extends FrameLayout
{
  private static final String DATE_FORMAT = "MM/dd/yyyy";
  private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
  private static final String LOG_TAG = "CalendarView";
  private static final int MODE_HOLO = 0;
  private static final int MODE_MATERIAL = 1;
  private final CalendarViewDelegate mDelegate;
  
  public CalendarView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public CalendarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843613);
  }
  
  public CalendarView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public CalendarView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CalendarView, paramInt1, paramInt2);
    int i = localTypedArray.getInt(13, 0);
    localTypedArray.recycle();
    switch (i)
    {
    default: 
      throw new IllegalArgumentException("invalid calendarViewMode attribute");
    case 1: 
      mDelegate = new CalendarViewMaterialDelegate(this, paramContext, paramAttributeSet, paramInt1, paramInt2);
      break;
    case 0: 
      mDelegate = new CalendarViewLegacyDelegate(this, paramContext, paramAttributeSet, paramInt1, paramInt2);
    }
  }
  
  public static boolean parseDate(String paramString, Calendar paramCalendar)
  {
    if ((paramString != null) && (!paramString.isEmpty())) {
      try
      {
        paramCalendar.setTime(DATE_FORMATTER.parse(paramString));
        return true;
      }
      catch (ParseException paramCalendar)
      {
        paramCalendar = new StringBuilder();
        paramCalendar.append("Date: ");
        paramCalendar.append(paramString);
        paramCalendar.append(" not in format: ");
        paramCalendar.append("MM/dd/yyyy");
        Log.w("CalendarView", paramCalendar.toString());
        return false;
      }
    }
    return false;
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return CalendarView.class.getName();
  }
  
  public boolean getBoundsForDate(long paramLong, Rect paramRect)
  {
    return mDelegate.getBoundsForDate(paramLong, paramRect);
  }
  
  public long getDate()
  {
    return mDelegate.getDate();
  }
  
  public int getDateTextAppearance()
  {
    return mDelegate.getDateTextAppearance();
  }
  
  public int getFirstDayOfWeek()
  {
    return mDelegate.getFirstDayOfWeek();
  }
  
  @Deprecated
  public int getFocusedMonthDateColor()
  {
    return mDelegate.getFocusedMonthDateColor();
  }
  
  public long getMaxDate()
  {
    return mDelegate.getMaxDate();
  }
  
  public long getMinDate()
  {
    return mDelegate.getMinDate();
  }
  
  @Deprecated
  public Drawable getSelectedDateVerticalBar()
  {
    return mDelegate.getSelectedDateVerticalBar();
  }
  
  @Deprecated
  public int getSelectedWeekBackgroundColor()
  {
    return mDelegate.getSelectedWeekBackgroundColor();
  }
  
  @Deprecated
  public boolean getShowWeekNumber()
  {
    return mDelegate.getShowWeekNumber();
  }
  
  @Deprecated
  public int getShownWeekCount()
  {
    return mDelegate.getShownWeekCount();
  }
  
  @Deprecated
  public int getUnfocusedMonthDateColor()
  {
    return mDelegate.getUnfocusedMonthDateColor();
  }
  
  public int getWeekDayTextAppearance()
  {
    return mDelegate.getWeekDayTextAppearance();
  }
  
  @Deprecated
  public int getWeekNumberColor()
  {
    return mDelegate.getWeekNumberColor();
  }
  
  @Deprecated
  public int getWeekSeparatorLineColor()
  {
    return mDelegate.getWeekSeparatorLineColor();
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    mDelegate.onConfigurationChanged(paramConfiguration);
  }
  
  public void setDate(long paramLong)
  {
    mDelegate.setDate(paramLong);
  }
  
  public void setDate(long paramLong, boolean paramBoolean1, boolean paramBoolean2)
  {
    mDelegate.setDate(paramLong, paramBoolean1, paramBoolean2);
  }
  
  public void setDateTextAppearance(int paramInt)
  {
    mDelegate.setDateTextAppearance(paramInt);
  }
  
  public void setFirstDayOfWeek(int paramInt)
  {
    mDelegate.setFirstDayOfWeek(paramInt);
  }
  
  @Deprecated
  public void setFocusedMonthDateColor(int paramInt)
  {
    mDelegate.setFocusedMonthDateColor(paramInt);
  }
  
  public void setMaxDate(long paramLong)
  {
    mDelegate.setMaxDate(paramLong);
  }
  
  public void setMinDate(long paramLong)
  {
    mDelegate.setMinDate(paramLong);
  }
  
  public void setOnDateChangeListener(OnDateChangeListener paramOnDateChangeListener)
  {
    mDelegate.setOnDateChangeListener(paramOnDateChangeListener);
  }
  
  @Deprecated
  public void setSelectedDateVerticalBar(int paramInt)
  {
    mDelegate.setSelectedDateVerticalBar(paramInt);
  }
  
  @Deprecated
  public void setSelectedDateVerticalBar(Drawable paramDrawable)
  {
    mDelegate.setSelectedDateVerticalBar(paramDrawable);
  }
  
  @Deprecated
  public void setSelectedWeekBackgroundColor(int paramInt)
  {
    mDelegate.setSelectedWeekBackgroundColor(paramInt);
  }
  
  @Deprecated
  public void setShowWeekNumber(boolean paramBoolean)
  {
    mDelegate.setShowWeekNumber(paramBoolean);
  }
  
  @Deprecated
  public void setShownWeekCount(int paramInt)
  {
    mDelegate.setShownWeekCount(paramInt);
  }
  
  @Deprecated
  public void setUnfocusedMonthDateColor(int paramInt)
  {
    mDelegate.setUnfocusedMonthDateColor(paramInt);
  }
  
  public void setWeekDayTextAppearance(int paramInt)
  {
    mDelegate.setWeekDayTextAppearance(paramInt);
  }
  
  @Deprecated
  public void setWeekNumberColor(int paramInt)
  {
    mDelegate.setWeekNumberColor(paramInt);
  }
  
  @Deprecated
  public void setWeekSeparatorLineColor(int paramInt)
  {
    mDelegate.setWeekSeparatorLineColor(paramInt);
  }
  
  static abstract class AbstractCalendarViewDelegate
    implements CalendarView.CalendarViewDelegate
  {
    protected static final String DEFAULT_MAX_DATE = "01/01/2100";
    protected static final String DEFAULT_MIN_DATE = "01/01/1900";
    protected Context mContext;
    protected Locale mCurrentLocale;
    protected CalendarView mDelegator;
    
    AbstractCalendarViewDelegate(CalendarView paramCalendarView, Context paramContext)
    {
      mDelegator = paramCalendarView;
      mContext = paramContext;
      setCurrentLocale(Locale.getDefault());
    }
    
    public int getFocusedMonthDateColor()
    {
      return 0;
    }
    
    public Drawable getSelectedDateVerticalBar()
    {
      return null;
    }
    
    public int getSelectedWeekBackgroundColor()
    {
      return 0;
    }
    
    public boolean getShowWeekNumber()
    {
      return false;
    }
    
    public int getShownWeekCount()
    {
      return 0;
    }
    
    public int getUnfocusedMonthDateColor()
    {
      return 0;
    }
    
    public int getWeekNumberColor()
    {
      return 0;
    }
    
    public int getWeekSeparatorLineColor()
    {
      return 0;
    }
    
    public void onConfigurationChanged(Configuration paramConfiguration) {}
    
    protected void setCurrentLocale(Locale paramLocale)
    {
      if (paramLocale.equals(mCurrentLocale)) {
        return;
      }
      mCurrentLocale = paramLocale;
    }
    
    public void setFocusedMonthDateColor(int paramInt) {}
    
    public void setSelectedDateVerticalBar(int paramInt) {}
    
    public void setSelectedDateVerticalBar(Drawable paramDrawable) {}
    
    public void setSelectedWeekBackgroundColor(int paramInt) {}
    
    public void setShowWeekNumber(boolean paramBoolean) {}
    
    public void setShownWeekCount(int paramInt) {}
    
    public void setUnfocusedMonthDateColor(int paramInt) {}
    
    public void setWeekNumberColor(int paramInt) {}
    
    public void setWeekSeparatorLineColor(int paramInt) {}
  }
  
  private static abstract interface CalendarViewDelegate
  {
    public abstract boolean getBoundsForDate(long paramLong, Rect paramRect);
    
    public abstract long getDate();
    
    public abstract int getDateTextAppearance();
    
    public abstract int getFirstDayOfWeek();
    
    public abstract int getFocusedMonthDateColor();
    
    public abstract long getMaxDate();
    
    public abstract long getMinDate();
    
    public abstract Drawable getSelectedDateVerticalBar();
    
    public abstract int getSelectedWeekBackgroundColor();
    
    public abstract boolean getShowWeekNumber();
    
    public abstract int getShownWeekCount();
    
    public abstract int getUnfocusedMonthDateColor();
    
    public abstract int getWeekDayTextAppearance();
    
    public abstract int getWeekNumberColor();
    
    public abstract int getWeekSeparatorLineColor();
    
    public abstract void onConfigurationChanged(Configuration paramConfiguration);
    
    public abstract void setDate(long paramLong);
    
    public abstract void setDate(long paramLong, boolean paramBoolean1, boolean paramBoolean2);
    
    public abstract void setDateTextAppearance(int paramInt);
    
    public abstract void setFirstDayOfWeek(int paramInt);
    
    public abstract void setFocusedMonthDateColor(int paramInt);
    
    public abstract void setMaxDate(long paramLong);
    
    public abstract void setMinDate(long paramLong);
    
    public abstract void setOnDateChangeListener(CalendarView.OnDateChangeListener paramOnDateChangeListener);
    
    public abstract void setSelectedDateVerticalBar(int paramInt);
    
    public abstract void setSelectedDateVerticalBar(Drawable paramDrawable);
    
    public abstract void setSelectedWeekBackgroundColor(int paramInt);
    
    public abstract void setShowWeekNumber(boolean paramBoolean);
    
    public abstract void setShownWeekCount(int paramInt);
    
    public abstract void setUnfocusedMonthDateColor(int paramInt);
    
    public abstract void setWeekDayTextAppearance(int paramInt);
    
    public abstract void setWeekNumberColor(int paramInt);
    
    public abstract void setWeekSeparatorLineColor(int paramInt);
  }
  
  public static abstract interface OnDateChangeListener
  {
    public abstract void onSelectedDayChange(CalendarView paramCalendarView, int paramInt1, int paramInt2, int paramInt3);
  }
}
