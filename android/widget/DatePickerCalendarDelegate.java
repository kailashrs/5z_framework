package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.icu.text.DateFormat;
import android.icu.text.DisplayContext;
import android.icu.util.Calendar;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.R.styleable;
import java.util.Locale;

class DatePickerCalendarDelegate
  extends DatePicker.AbstractDatePickerDelegate
{
  private static final int ANIMATION_DURATION = 300;
  private static final int[] ATTRS_DISABLED_ALPHA = { 16842803 };
  private static final int[] ATTRS_TEXT_COLOR = { 16842904 };
  private static final int DEFAULT_END_YEAR = 2100;
  private static final int DEFAULT_START_YEAR = 1900;
  private static final int UNINITIALIZED = -1;
  private static final int USE_LOCALE = 0;
  private static final int VIEW_MONTH_DAY = 0;
  private static final int VIEW_YEAR = 1;
  private ViewAnimator mAnimator;
  private ViewGroup mContainer;
  private int mCurrentView = -1;
  private DayPickerView mDayPickerView;
  private int mFirstDayOfWeek = 0;
  private TextView mHeaderMonthDay;
  private TextView mHeaderYear;
  private final Calendar mMaxDate;
  private final Calendar mMinDate;
  private DateFormat mMonthDayFormat;
  private final DayPickerView.OnDaySelectedListener mOnDaySelectedListener = new DayPickerView.OnDaySelectedListener()
  {
    public void onDaySelected(DayPickerView paramAnonymousDayPickerView, Calendar paramAnonymousCalendar)
    {
      mCurrentDate.setTimeInMillis(paramAnonymousCalendar.getTimeInMillis());
      DatePickerCalendarDelegate.this.onDateChanged(true, true);
    }
  };
  private final View.OnClickListener mOnHeaderClickListener = new _..Lambda.DatePickerCalendarDelegate.GuCiuXPsIV2EU6oKGRXrsGY_DHM(this);
  private final YearPickerView.OnYearSelectedListener mOnYearSelectedListener = new YearPickerView.OnYearSelectedListener()
  {
    public void onYearChanged(YearPickerView paramAnonymousYearPickerView, int paramAnonymousInt)
    {
      int i = mCurrentDate.get(5);
      int j = DatePickerCalendarDelegate.getDaysInMonth(mCurrentDate.get(2), paramAnonymousInt);
      if (i > j) {
        mCurrentDate.set(5, j);
      }
      mCurrentDate.set(1, paramAnonymousInt);
      DatePickerCalendarDelegate.this.onDateChanged(true, true);
      DatePickerCalendarDelegate.this.setCurrentView(0);
      mHeaderYear.requestFocus();
    }
  };
  private String mSelectDay;
  private String mSelectYear;
  private final Calendar mTempDate;
  private DateFormat mYearFormat;
  private YearPickerView mYearPickerView;
  
  public DatePickerCalendarDelegate(DatePicker paramDatePicker, Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramDatePicker, paramContext);
    paramDatePicker = mCurrentLocale;
    mCurrentDate = Calendar.getInstance(paramDatePicker);
    mTempDate = Calendar.getInstance(paramDatePicker);
    mMinDate = Calendar.getInstance(paramDatePicker);
    mMaxDate = Calendar.getInstance(paramDatePicker);
    mMinDate.set(1900, 0, 1);
    mMaxDate.set(2100, 11, 31);
    Resources localResources = mDelegator.getResources();
    TypedArray localTypedArray = mContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DatePicker, paramInt1, paramInt2);
    mContainer = ((ViewGroup)((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(localTypedArray.getResourceId(19, 17367153), mDelegator, false));
    mContainer.setSaveFromParentEnabled(false);
    mDelegator.addView(mContainer);
    paramAttributeSet = (ViewGroup)mContainer.findViewById(16908886);
    mHeaderYear = ((TextView)paramAttributeSet.findViewById(16908888));
    mHeaderYear.setOnClickListener(mOnHeaderClickListener);
    mHeaderMonthDay = ((TextView)paramAttributeSet.findViewById(16908887));
    mHeaderMonthDay.setOnClickListener(mOnHeaderClickListener);
    paramDatePicker = null;
    paramInt1 = localTypedArray.getResourceId(10, 0);
    if (paramInt1 != 0)
    {
      paramContext = mContext.obtainStyledAttributes(null, ATTRS_TEXT_COLOR, 0, paramInt1);
      paramDatePicker = applyLegacyColorFixes(paramContext.getColorStateList(0));
      paramContext.recycle();
    }
    paramContext = paramDatePicker;
    if (paramDatePicker == null) {
      paramContext = localTypedArray.getColorStateList(18);
    }
    if (paramContext != null)
    {
      mHeaderYear.setTextColor(paramContext);
      mHeaderMonthDay.setTextColor(paramContext);
    }
    if (localTypedArray.hasValueOrEmpty(0)) {
      paramAttributeSet.setBackground(localTypedArray.getDrawable(0));
    }
    localTypedArray.recycle();
    mAnimator = ((ViewAnimator)mContainer.findViewById(16908742));
    mDayPickerView = ((DayPickerView)mAnimator.findViewById(16908885));
    mDayPickerView.setFirstDayOfWeek(mFirstDayOfWeek);
    mDayPickerView.setMinDate(mMinDate.getTimeInMillis());
    mDayPickerView.setMaxDate(mMaxDate.getTimeInMillis());
    mDayPickerView.setDate(mCurrentDate.getTimeInMillis());
    mDayPickerView.setOnDaySelectedListener(mOnDaySelectedListener);
    mYearPickerView = ((YearPickerView)mAnimator.findViewById(16908889));
    mYearPickerView.setRange(mMinDate, mMaxDate);
    mYearPickerView.setYear(mCurrentDate.get(1));
    mYearPickerView.setOnYearSelectedListener(mOnYearSelectedListener);
    mSelectDay = localResources.getString(17040960);
    mSelectYear = localResources.getString(17040966);
    onLocaleChanged(mCurrentLocale);
    setCurrentView(0);
  }
  
  private ColorStateList applyLegacyColorFixes(ColorStateList paramColorStateList)
  {
    if ((paramColorStateList != null) && (!paramColorStateList.hasState(16843518)))
    {
      int i;
      int j;
      if (paramColorStateList.hasState(16842913))
      {
        i = paramColorStateList.getColorForState(StateSet.get(10), 0);
        j = paramColorStateList.getColorForState(StateSet.get(8), 0);
      }
      else
      {
        i = paramColorStateList.getDefaultColor();
        j = multiplyAlphaComponent(i, mContext.obtainStyledAttributes(ATTRS_DISABLED_ALPHA).getFloat(0, 0.3F));
      }
      if ((i != 0) && (j != 0))
      {
        paramColorStateList = new int[0];
        return new ColorStateList(new int[][] { { 16843518 }, paramColorStateList }, new int[] { i, j });
      }
      return null;
    }
    return paramColorStateList;
  }
  
  public static int getDaysInMonth(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      throw new IllegalArgumentException("Invalid Month");
    case 3: 
    case 5: 
    case 8: 
    case 10: 
      return 30;
    case 1: 
      if (paramInt2 % 4 == 0) {
        paramInt1 = 29;
      } else {
        paramInt1 = 28;
      }
      return paramInt1;
    }
    return 31;
  }
  
  private int multiplyAlphaComponent(int paramInt, float paramFloat)
  {
    return (int)((paramInt >> 24 & 0xFF) * paramFloat + 0.5F) << 24 | 0xFFFFFF & paramInt;
  }
  
  private void onCurrentDateChanged(boolean paramBoolean)
  {
    if (mHeaderYear == null) {
      return;
    }
    String str = mYearFormat.format(mCurrentDate.getTime());
    mHeaderYear.setText(str);
    str = mMonthDayFormat.format(mCurrentDate.getTime());
    mHeaderMonthDay.setText(str);
    if (paramBoolean) {
      mAnimator.announceForAccessibility(getFormattedCurrentDate());
    }
  }
  
  private void onDateChanged(boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = mCurrentDate.get(1);
    if ((paramBoolean2) && ((mOnDateChangedListener != null) || (mAutoFillChangeListener != null)))
    {
      int j = mCurrentDate.get(2);
      int k = mCurrentDate.get(5);
      if (mOnDateChangedListener != null) {
        mOnDateChangedListener.onDateChanged(mDelegator, i, j, k);
      }
      if (mAutoFillChangeListener != null) {
        mAutoFillChangeListener.onDateChanged(mDelegator, i, j, k);
      }
    }
    mDayPickerView.setDate(mCurrentDate.getTimeInMillis());
    mYearPickerView.setYear(i);
    onCurrentDateChanged(paramBoolean1);
    if (paramBoolean1) {
      tryVibrate();
    }
  }
  
  private void setCurrentView(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 1: 
      int i = mCurrentDate.get(1);
      mYearPickerView.setYear(i);
      mYearPickerView.post(new _..Lambda.DatePickerCalendarDelegate._6rynvAYPe1gU9xVgvSm4VMsr2M(this));
      if (mCurrentView != paramInt)
      {
        mHeaderMonthDay.setActivated(false);
        mHeaderYear.setActivated(true);
        mAnimator.setDisplayedChild(1);
        mCurrentView = paramInt;
      }
      mAnimator.announceForAccessibility(mSelectYear);
      break;
    case 0: 
      mDayPickerView.setDate(mCurrentDate.getTimeInMillis());
      if (mCurrentView != paramInt)
      {
        mHeaderMonthDay.setActivated(true);
        mHeaderYear.setActivated(false);
        mAnimator.setDisplayedChild(0);
        mCurrentView = paramInt;
      }
      mAnimator.announceForAccessibility(mSelectDay);
    }
  }
  
  private void setDate(int paramInt1, int paramInt2, int paramInt3)
  {
    mCurrentDate.set(1, paramInt1);
    mCurrentDate.set(2, paramInt2);
    mCurrentDate.set(5, paramInt3);
    resetAutofilledValue();
  }
  
  private void tryVibrate()
  {
    mDelegator.performHapticFeedback(5);
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    onPopulateAccessibilityEvent(paramAccessibilityEvent);
    return true;
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return DatePicker.class.getName();
  }
  
  public CalendarView getCalendarView()
  {
    throw new UnsupportedOperationException("Not supported by calendar-mode DatePicker");
  }
  
  public boolean getCalendarViewShown()
  {
    return false;
  }
  
  public int getDayOfMonth()
  {
    return mCurrentDate.get(5);
  }
  
  public int getFirstDayOfWeek()
  {
    if (mFirstDayOfWeek != 0) {
      return mFirstDayOfWeek;
    }
    return mCurrentDate.getFirstDayOfWeek();
  }
  
  public Calendar getMaxDate()
  {
    return mMaxDate;
  }
  
  public Calendar getMinDate()
  {
    return mMinDate;
  }
  
  public int getMonth()
  {
    return mCurrentDate.get(2);
  }
  
  public boolean getSpinnersShown()
  {
    return false;
  }
  
  public int getYear()
  {
    return mCurrentDate.get(1);
  }
  
  public void init(int paramInt1, int paramInt2, int paramInt3, DatePicker.OnDateChangedListener paramOnDateChangedListener)
  {
    setDate(paramInt1, paramInt2, paramInt3);
    onDateChanged(false, false);
    mOnDateChangedListener = paramOnDateChangedListener;
  }
  
  public boolean isEnabled()
  {
    return mContainer.isEnabled();
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    setCurrentLocale(locale);
  }
  
  protected void onLocaleChanged(Locale paramLocale)
  {
    if (mHeaderYear == null) {
      return;
    }
    mMonthDayFormat = DateFormat.getInstanceForSkeleton("EMMMd", paramLocale);
    mMonthDayFormat.setContext(DisplayContext.CAPITALIZATION_FOR_STANDALONE);
    mYearFormat = DateFormat.getInstanceForSkeleton("y", paramLocale);
    onCurrentDateChanged(false);
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable instanceof DatePicker.AbstractDatePickerDelegate.SavedState))
    {
      paramParcelable = (DatePicker.AbstractDatePickerDelegate.SavedState)paramParcelable;
      mCurrentDate.set(paramParcelable.getSelectedYear(), paramParcelable.getSelectedMonth(), paramParcelable.getSelectedDay());
      mMinDate.setTimeInMillis(paramParcelable.getMinDate());
      mMaxDate.setTimeInMillis(paramParcelable.getMaxDate());
      onCurrentDateChanged(false);
      int i = paramParcelable.getCurrentView();
      setCurrentView(i);
      int j = paramParcelable.getListPosition();
      if (j != -1) {
        if (i == 0)
        {
          mDayPickerView.setPosition(j);
        }
        else if (i == 1)
        {
          i = paramParcelable.getListPositionOffset();
          mYearPickerView.setSelectionFromTop(j, i);
        }
      }
    }
  }
  
  public Parcelable onSaveInstanceState(Parcelable paramParcelable)
  {
    int i = mCurrentDate.get(1);
    int j = mCurrentDate.get(2);
    int k = mCurrentDate.get(5);
    if (mCurrentView == 0) {}
    int n;
    for (int m = mDayPickerView.getMostVisiblePosition();; m = -1)
    {
      n = -1;
      break;
      if (mCurrentView == 1)
      {
        m = mYearPickerView.getFirstVisiblePosition();
        n = mYearPickerView.getFirstPositionOffset();
        break;
      }
    }
    return new DatePicker.AbstractDatePickerDelegate.SavedState(paramParcelable, i, j, k, mMinDate.getTimeInMillis(), mMaxDate.getTimeInMillis(), mCurrentView, m, n);
  }
  
  public void setCalendarViewShown(boolean paramBoolean) {}
  
  public void setEnabled(boolean paramBoolean)
  {
    mContainer.setEnabled(paramBoolean);
    mDayPickerView.setEnabled(paramBoolean);
    mYearPickerView.setEnabled(paramBoolean);
    mHeaderYear.setEnabled(paramBoolean);
    mHeaderMonthDay.setEnabled(paramBoolean);
  }
  
  public void setFirstDayOfWeek(int paramInt)
  {
    mFirstDayOfWeek = paramInt;
    mDayPickerView.setFirstDayOfWeek(paramInt);
  }
  
  public void setMaxDate(long paramLong)
  {
    mTempDate.setTimeInMillis(paramLong);
    if ((mTempDate.get(1) == mMaxDate.get(1)) && (mTempDate.get(6) == mMaxDate.get(6))) {
      return;
    }
    if (mCurrentDate.after(mTempDate))
    {
      mCurrentDate.setTimeInMillis(paramLong);
      onDateChanged(false, true);
    }
    mMaxDate.setTimeInMillis(paramLong);
    mDayPickerView.setMaxDate(paramLong);
    mYearPickerView.setRange(mMinDate, mMaxDate);
  }
  
  public void setMinDate(long paramLong)
  {
    mTempDate.setTimeInMillis(paramLong);
    if ((mTempDate.get(1) == mMinDate.get(1)) && (mTempDate.get(6) == mMinDate.get(6))) {
      return;
    }
    if (mCurrentDate.before(mTempDate))
    {
      mCurrentDate.setTimeInMillis(paramLong);
      onDateChanged(false, true);
    }
    mMinDate.setTimeInMillis(paramLong);
    mDayPickerView.setMinDate(paramLong);
    mYearPickerView.setRange(mMinDate, mMaxDate);
  }
  
  public void setSpinnersShown(boolean paramBoolean) {}
  
  public void updateDate(int paramInt1, int paramInt2, int paramInt3)
  {
    setDate(paramInt1, paramInt2, paramInt3);
    onDateChanged(false, true);
  }
}
