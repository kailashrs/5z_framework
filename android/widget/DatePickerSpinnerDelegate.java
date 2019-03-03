package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.icu.util.Calendar;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.R.styleable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import libcore.icu.ICU;

class DatePickerSpinnerDelegate
  extends DatePicker.AbstractDatePickerDelegate
{
  private static final String DATE_FORMAT = "MM/dd/yyyy";
  private static final boolean DEFAULT_CALENDAR_VIEW_SHOWN = true;
  private static final boolean DEFAULT_ENABLED_STATE = true;
  private static final int DEFAULT_END_YEAR = 2100;
  private static final boolean DEFAULT_SPINNERS_SHOWN = true;
  private static final int DEFAULT_START_YEAR = 1900;
  private final CalendarView mCalendarView;
  private final java.text.DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy");
  private final NumberPicker mDaySpinner;
  private final EditText mDaySpinnerInput;
  private boolean mIsEnabled = true;
  private Calendar mMaxDate;
  private Calendar mMinDate;
  private final NumberPicker mMonthSpinner;
  private final EditText mMonthSpinnerInput;
  private int mNumberOfMonths;
  private String[] mShortMonths;
  private final LinearLayout mSpinners;
  private Calendar mTempDate;
  private final NumberPicker mYearSpinner;
  private final EditText mYearSpinnerInput;
  
  DatePickerSpinnerDelegate(DatePicker paramDatePicker, Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramDatePicker, paramContext);
    mDelegator = paramDatePicker;
    mContext = paramContext;
    setCurrentLocale(Locale.getDefault());
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DatePicker, paramInt1, paramInt2);
    boolean bool1 = localTypedArray.getBoolean(6, true);
    boolean bool2 = localTypedArray.getBoolean(7, true);
    paramInt2 = localTypedArray.getInt(1, 1900);
    int i = localTypedArray.getInt(2, 2100);
    paramDatePicker = localTypedArray.getString(4);
    paramAttributeSet = localTypedArray.getString(5);
    paramInt1 = localTypedArray.getResourceId(20, 17367151);
    localTypedArray.recycle();
    ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(paramInt1, mDelegator, true).setSaveFromParentEnabled(false);
    paramContext = new NumberPicker.OnValueChangeListener()
    {
      public void onValueChange(NumberPicker paramAnonymousNumberPicker, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        DatePickerSpinnerDelegate.this.updateInputState();
        mTempDate.setTimeInMillis(mCurrentDate.getTimeInMillis());
        if (paramAnonymousNumberPicker == mDaySpinner)
        {
          int i = mTempDate.getActualMaximum(5);
          if ((paramAnonymousInt1 == i) && (paramAnonymousInt2 == 1)) {
            mTempDate.add(5, 1);
          } else if ((paramAnonymousInt1 == 1) && (paramAnonymousInt2 == i)) {
            mTempDate.add(5, -1);
          } else {
            mTempDate.add(5, paramAnonymousInt2 - paramAnonymousInt1);
          }
        }
        else if (paramAnonymousNumberPicker == mMonthSpinner)
        {
          if ((paramAnonymousInt1 == 11) && (paramAnonymousInt2 == 0)) {
            mTempDate.add(2, 1);
          } else if ((paramAnonymousInt1 == 0) && (paramAnonymousInt2 == 11)) {
            mTempDate.add(2, -1);
          } else {
            mTempDate.add(2, paramAnonymousInt2 - paramAnonymousInt1);
          }
        }
        else
        {
          if (paramAnonymousNumberPicker != mYearSpinner) {
            break label283;
          }
          mTempDate.set(1, paramAnonymousInt2);
        }
        DatePickerSpinnerDelegate.this.setDate(mTempDate.get(1), mTempDate.get(2), mTempDate.get(5));
        DatePickerSpinnerDelegate.this.updateSpinners();
        DatePickerSpinnerDelegate.this.updateCalendarView();
        DatePickerSpinnerDelegate.this.notifyDateChanged();
        return;
        label283:
        throw new IllegalArgumentException();
      }
    };
    mSpinners = ((LinearLayout)mDelegator.findViewById(16909239));
    mCalendarView = ((CalendarView)mDelegator.findViewById(16908838));
    mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
    {
      public void onSelectedDayChange(CalendarView paramAnonymousCalendarView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        DatePickerSpinnerDelegate.this.setDate(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
        DatePickerSpinnerDelegate.this.updateSpinners();
        DatePickerSpinnerDelegate.this.notifyDateChanged();
      }
    });
    mDaySpinner = ((NumberPicker)mDelegator.findViewById(16908891));
    mDaySpinner.setFormatter(NumberPicker.getTwoDigitFormatter());
    mDaySpinner.setOnLongPressUpdateInterval(100L);
    mDaySpinner.setOnValueChangedListener(paramContext);
    mDaySpinnerInput = ((EditText)mDaySpinner.findViewById(16909191));
    mMonthSpinner = ((NumberPicker)mDelegator.findViewById(16909142));
    mMonthSpinner.setMinValue(0);
    mMonthSpinner.setMaxValue(mNumberOfMonths - 1);
    mMonthSpinner.setDisplayedValues(mShortMonths);
    mMonthSpinner.setOnLongPressUpdateInterval(200L);
    mMonthSpinner.setOnValueChangedListener(paramContext);
    mMonthSpinnerInput = ((EditText)mMonthSpinner.findViewById(16909191));
    mYearSpinner = ((NumberPicker)mDelegator.findViewById(16909582));
    mYearSpinner.setOnLongPressUpdateInterval(100L);
    mYearSpinner.setOnValueChangedListener(paramContext);
    mYearSpinnerInput = ((EditText)mYearSpinner.findViewById(16909191));
    if ((!bool1) && (!bool2))
    {
      setSpinnersShown(true);
    }
    else
    {
      setSpinnersShown(bool1);
      setCalendarViewShown(bool2);
    }
    mTempDate.clear();
    if (!TextUtils.isEmpty(paramDatePicker))
    {
      if (!parseDate(paramDatePicker, mTempDate)) {
        mTempDate.set(paramInt2, 0, 1);
      }
    }
    else {
      mTempDate.set(paramInt2, 0, 1);
    }
    setMinDate(mTempDate.getTimeInMillis());
    mTempDate.clear();
    if (!TextUtils.isEmpty(paramAttributeSet))
    {
      if (!parseDate(paramAttributeSet, mTempDate)) {
        mTempDate.set(i, 11, 31);
      }
    }
    else {
      mTempDate.set(i, 11, 31);
    }
    setMaxDate(mTempDate.getTimeInMillis());
    mCurrentDate.setTimeInMillis(System.currentTimeMillis());
    init(mCurrentDate.get(1), mCurrentDate.get(2), mCurrentDate.get(5), null);
    reorderSpinners();
    setContentDescriptions();
    if (mDelegator.getImportantForAccessibility() == 0) {
      mDelegator.setImportantForAccessibility(1);
    }
  }
  
  private Calendar getCalendarForLocale(Calendar paramCalendar, Locale paramLocale)
  {
    if (paramCalendar == null) {
      return Calendar.getInstance(paramLocale);
    }
    long l = paramCalendar.getTimeInMillis();
    paramCalendar = Calendar.getInstance(paramLocale);
    paramCalendar.setTimeInMillis(l);
    return paramCalendar;
  }
  
  private boolean isNewDate(int paramInt1, int paramInt2, int paramInt3)
  {
    Calendar localCalendar = mCurrentDate;
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (localCalendar.get(1) == paramInt1)
    {
      bool2 = bool1;
      if (mCurrentDate.get(2) == paramInt2) {
        if (mCurrentDate.get(5) != paramInt3) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
    }
    return bool2;
  }
  
  private void notifyDateChanged()
  {
    mDelegator.sendAccessibilityEvent(4);
    if (mOnDateChangedListener != null) {
      mOnDateChangedListener.onDateChanged(mDelegator, getYear(), getMonth(), getDayOfMonth());
    }
    if (mAutoFillChangeListener != null) {
      mAutoFillChangeListener.onDateChanged(mDelegator, getYear(), getMonth(), getDayOfMonth());
    }
  }
  
  private boolean parseDate(String paramString, Calendar paramCalendar)
  {
    try
    {
      paramCalendar.setTime(mDateFormat.parse(paramString));
      return true;
    }
    catch (ParseException paramString)
    {
      paramString.printStackTrace();
    }
    return false;
  }
  
  private void reorderSpinners()
  {
    mSpinners.removeAllViews();
    for (int k : ICU.getDateFormatOrder(android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMdd"))) {
      if (k != 77)
      {
        if (k != 100)
        {
          if (k == 121)
          {
            mSpinners.addView(mYearSpinner);
            setImeOptions(mYearSpinner, ???, ???);
          }
          else
          {
            throw new IllegalArgumentException(Arrays.toString(???));
          }
        }
        else
        {
          mSpinners.addView(mDaySpinner);
          setImeOptions(mDaySpinner, ???, ???);
        }
      }
      else
      {
        mSpinners.addView(mMonthSpinner);
        setImeOptions(mMonthSpinner, ???, ???);
      }
    }
  }
  
  private void setContentDescriptions()
  {
    trySetContentDescription(mDaySpinner, 16909034, 17039849);
    trySetContentDescription(mDaySpinner, 16908896, 17039845);
    trySetContentDescription(mMonthSpinner, 16909034, 17039850);
    trySetContentDescription(mMonthSpinner, 16908896, 17039846);
    trySetContentDescription(mYearSpinner, 16909034, 17039851);
    trySetContentDescription(mYearSpinner, 16908896, 17039847);
  }
  
  private void setDate(int paramInt1, int paramInt2, int paramInt3)
  {
    mCurrentDate.set(paramInt1, paramInt2, paramInt3);
    resetAutofilledValue();
    if (mCurrentDate.before(mMinDate)) {
      mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
    } else if (mCurrentDate.after(mMaxDate)) {
      mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
    }
  }
  
  private void setImeOptions(NumberPicker paramNumberPicker, int paramInt1, int paramInt2)
  {
    if (paramInt2 < paramInt1 - 1) {
      paramInt1 = 5;
    } else {
      paramInt1 = 6;
    }
    ((TextView)paramNumberPicker.findViewById(16909191)).setImeOptions(paramInt1);
  }
  
  private void trySetContentDescription(View paramView, int paramInt1, int paramInt2)
  {
    paramView = paramView.findViewById(paramInt1);
    if (paramView != null) {
      paramView.setContentDescription(mContext.getString(paramInt2));
    }
  }
  
  private void updateCalendarView()
  {
    mCalendarView.setDate(mCurrentDate.getTimeInMillis(), false, false);
  }
  
  private void updateInputState()
  {
    InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
    if (localInputMethodManager != null) {
      if (localInputMethodManager.isActive(mYearSpinnerInput))
      {
        mYearSpinnerInput.clearFocus();
        localInputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
      }
      else if (localInputMethodManager.isActive(mMonthSpinnerInput))
      {
        mMonthSpinnerInput.clearFocus();
        localInputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
      }
      else if (localInputMethodManager.isActive(mDaySpinnerInput))
      {
        mDaySpinnerInput.clearFocus();
        localInputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
      }
    }
  }
  
  private void updateSpinners()
  {
    if (mCurrentDate.equals(mMinDate))
    {
      mDaySpinner.setMinValue(mCurrentDate.get(5));
      mDaySpinner.setMaxValue(mCurrentDate.getActualMaximum(5));
      mDaySpinner.setWrapSelectorWheel(false);
      mMonthSpinner.setDisplayedValues(null);
      mMonthSpinner.setMinValue(mCurrentDate.get(2));
      mMonthSpinner.setMaxValue(mCurrentDate.getActualMaximum(2));
      mMonthSpinner.setWrapSelectorWheel(false);
    }
    else if (mCurrentDate.equals(mMaxDate))
    {
      mDaySpinner.setMinValue(mCurrentDate.getActualMinimum(5));
      mDaySpinner.setMaxValue(mCurrentDate.get(5));
      mDaySpinner.setWrapSelectorWheel(false);
      mMonthSpinner.setDisplayedValues(null);
      mMonthSpinner.setMinValue(mCurrentDate.getActualMinimum(2));
      mMonthSpinner.setMaxValue(mCurrentDate.get(2));
      mMonthSpinner.setWrapSelectorWheel(false);
    }
    else
    {
      mDaySpinner.setMinValue(1);
      mDaySpinner.setMaxValue(mCurrentDate.getActualMaximum(5));
      mDaySpinner.setWrapSelectorWheel(true);
      mMonthSpinner.setDisplayedValues(null);
      mMonthSpinner.setMinValue(0);
      mMonthSpinner.setMaxValue(11);
      mMonthSpinner.setWrapSelectorWheel(true);
    }
    String[] arrayOfString = (String[])Arrays.copyOfRange(mShortMonths, mMonthSpinner.getMinValue(), mMonthSpinner.getMaxValue() + 1);
    mMonthSpinner.setDisplayedValues(arrayOfString);
    mYearSpinner.setMinValue(mMinDate.get(1));
    mYearSpinner.setMaxValue(mMaxDate.get(1));
    mYearSpinner.setWrapSelectorWheel(false);
    mYearSpinner.setValue(mCurrentDate.get(1));
    mMonthSpinner.setValue(mCurrentDate.get(2));
    mDaySpinner.setValue(mCurrentDate.get(5));
    if (usingNumericMonths()) {
      mMonthSpinnerInput.setRawInputType(2);
    }
  }
  
  private boolean usingNumericMonths()
  {
    return Character.isDigit(mShortMonths[0].charAt(0));
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    onPopulateAccessibilityEvent(paramAccessibilityEvent);
    return true;
  }
  
  public CalendarView getCalendarView()
  {
    return mCalendarView;
  }
  
  public boolean getCalendarViewShown()
  {
    boolean bool;
    if (mCalendarView.getVisibility() == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int getDayOfMonth()
  {
    return mCurrentDate.get(5);
  }
  
  public int getFirstDayOfWeek()
  {
    return mCalendarView.getFirstDayOfWeek();
  }
  
  public Calendar getMaxDate()
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(mCalendarView.getMaxDate());
    return localCalendar;
  }
  
  public Calendar getMinDate()
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(mCalendarView.getMinDate());
    return localCalendar;
  }
  
  public int getMonth()
  {
    return mCurrentDate.get(2);
  }
  
  public boolean getSpinnersShown()
  {
    return mSpinners.isShown();
  }
  
  public int getYear()
  {
    return mCurrentDate.get(1);
  }
  
  public void init(int paramInt1, int paramInt2, int paramInt3, DatePicker.OnDateChangedListener paramOnDateChangedListener)
  {
    setDate(paramInt1, paramInt2, paramInt3);
    updateSpinners();
    updateCalendarView();
    mOnDateChangedListener = paramOnDateChangedListener;
  }
  
  public boolean isEnabled()
  {
    return mIsEnabled;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    setCurrentLocale(locale);
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable instanceof DatePicker.AbstractDatePickerDelegate.SavedState))
    {
      paramParcelable = (DatePicker.AbstractDatePickerDelegate.SavedState)paramParcelable;
      setDate(paramParcelable.getSelectedYear(), paramParcelable.getSelectedMonth(), paramParcelable.getSelectedDay());
      updateSpinners();
      updateCalendarView();
    }
  }
  
  public Parcelable onSaveInstanceState(Parcelable paramParcelable)
  {
    return new DatePicker.AbstractDatePickerDelegate.SavedState(paramParcelable, getYear(), getMonth(), getDayOfMonth(), getMinDate().getTimeInMillis(), getMaxDate().getTimeInMillis());
  }
  
  public void setCalendarViewShown(boolean paramBoolean)
  {
    CalendarView localCalendarView = mCalendarView;
    int i;
    if (paramBoolean) {
      i = 0;
    } else {
      i = 8;
    }
    localCalendarView.setVisibility(i);
  }
  
  protected void setCurrentLocale(Locale paramLocale)
  {
    super.setCurrentLocale(paramLocale);
    mTempDate = getCalendarForLocale(mTempDate, paramLocale);
    mMinDate = getCalendarForLocale(mMinDate, paramLocale);
    mMaxDate = getCalendarForLocale(mMaxDate, paramLocale);
    mCurrentDate = getCalendarForLocale(mCurrentDate, paramLocale);
    mNumberOfMonths = (mTempDate.getActualMaximum(2) + 1);
    mShortMonths = new DateFormatSymbols().getShortMonths();
    if (usingNumericMonths())
    {
      mShortMonths = new String[mNumberOfMonths];
      for (int i = 0; i < mNumberOfMonths; i++) {
        mShortMonths[i] = String.format("%d", new Object[] { Integer.valueOf(i + 1) });
      }
    }
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    mDaySpinner.setEnabled(paramBoolean);
    mMonthSpinner.setEnabled(paramBoolean);
    mYearSpinner.setEnabled(paramBoolean);
    mCalendarView.setEnabled(paramBoolean);
    mIsEnabled = paramBoolean;
  }
  
  public void setFirstDayOfWeek(int paramInt)
  {
    mCalendarView.setFirstDayOfWeek(paramInt);
  }
  
  public void setMaxDate(long paramLong)
  {
    mTempDate.setTimeInMillis(paramLong);
    if ((mTempDate.get(1) == mMaxDate.get(1)) && (mTempDate.get(6) == mMaxDate.get(6))) {
      return;
    }
    mMaxDate.setTimeInMillis(paramLong);
    mCalendarView.setMaxDate(paramLong);
    if (mCurrentDate.after(mMaxDate))
    {
      mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
      updateCalendarView();
    }
    updateSpinners();
  }
  
  public void setMinDate(long paramLong)
  {
    mTempDate.setTimeInMillis(paramLong);
    if ((mTempDate.get(1) == mMinDate.get(1)) && (mTempDate.get(6) == mMinDate.get(6))) {
      return;
    }
    mMinDate.setTimeInMillis(paramLong);
    mCalendarView.setMinDate(paramLong);
    if (mCurrentDate.before(mMinDate))
    {
      mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
      updateCalendarView();
    }
    updateSpinners();
  }
  
  public void setSpinnersShown(boolean paramBoolean)
  {
    LinearLayout localLinearLayout = mSpinners;
    int i;
    if (paramBoolean) {
      i = 0;
    } else {
      i = 8;
    }
    localLinearLayout.setVisibility(i);
  }
  
  public void updateDate(int paramInt1, int paramInt2, int paramInt3)
  {
    if (!isNewDate(paramInt1, paramInt2, paramInt3)) {
      return;
    }
    setDate(paramInt1, paramInt2, paramInt3);
    updateSpinners();
    updateCalendarView();
    notifyDateChanged();
  }
}
