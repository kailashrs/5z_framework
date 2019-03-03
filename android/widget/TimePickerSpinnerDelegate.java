package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.R.styleable;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import libcore.icu.LocaleData;

class TimePickerSpinnerDelegate
  extends TimePicker.AbstractTimePickerDelegate
{
  private static final boolean DEFAULT_ENABLED_STATE = true;
  private static final int HOURS_IN_HALF_DAY = 12;
  private final Button mAmPmButton;
  private final NumberPicker mAmPmSpinner;
  private final EditText mAmPmSpinnerInput;
  private final String[] mAmPmStrings;
  private final TextView mDivider;
  private char mHourFormat;
  private final NumberPicker mHourSpinner;
  private final EditText mHourSpinnerInput;
  private boolean mHourWithTwoDigit;
  private boolean mIs24HourView;
  private boolean mIsAm;
  private boolean mIsEnabled = true;
  private final NumberPicker mMinuteSpinner;
  private final EditText mMinuteSpinnerInput;
  private final Calendar mTempCalendar;
  
  public TimePickerSpinnerDelegate(TimePicker paramTimePicker, Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramTimePicker, paramContext);
    paramAttributeSet = mContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TimePicker, paramInt1, paramInt2);
    paramInt1 = paramAttributeSet.getResourceId(13, 17367343);
    paramAttributeSet.recycle();
    LayoutInflater.from(mContext).inflate(paramInt1, mDelegator, true).setSaveFromParentEnabled(false);
    mHourSpinner = ((NumberPicker)paramTimePicker.findViewById(16909013));
    mHourSpinner.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
    {
      public void onValueChange(NumberPicker paramAnonymousNumberPicker, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        TimePickerSpinnerDelegate.this.updateInputState();
        if ((!is24Hour()) && (((paramAnonymousInt1 == 11) && (paramAnonymousInt2 == 12)) || ((paramAnonymousInt1 == 12) && (paramAnonymousInt2 == 11))))
        {
          TimePickerSpinnerDelegate.access$102(TimePickerSpinnerDelegate.this, mIsAm ^ true);
          TimePickerSpinnerDelegate.this.updateAmPmControl();
        }
        TimePickerSpinnerDelegate.this.onTimeChanged();
      }
    });
    mHourSpinnerInput = ((EditText)mHourSpinner.findViewById(16909191));
    mHourSpinnerInput.setImeOptions(5);
    mDivider = ((TextView)mDelegator.findViewById(16908909));
    if (mDivider != null) {
      setDividerText();
    }
    mMinuteSpinner = ((NumberPicker)mDelegator.findViewById(16909130));
    mMinuteSpinner.setMinValue(0);
    mMinuteSpinner.setMaxValue(59);
    mMinuteSpinner.setOnLongPressUpdateInterval(100L);
    mMinuteSpinner.setFormatter(NumberPicker.getTwoDigitFormatter());
    mMinuteSpinner.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
    {
      public void onValueChange(NumberPicker paramAnonymousNumberPicker, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        TimePickerSpinnerDelegate.this.updateInputState();
        int i = mMinuteSpinner.getMinValue();
        int j = mMinuteSpinner.getMaxValue();
        if ((paramAnonymousInt1 == j) && (paramAnonymousInt2 == i))
        {
          paramAnonymousInt1 = mHourSpinner.getValue() + 1;
          if ((!is24Hour()) && (paramAnonymousInt1 == 12))
          {
            TimePickerSpinnerDelegate.access$102(TimePickerSpinnerDelegate.this, mIsAm ^ true);
            TimePickerSpinnerDelegate.this.updateAmPmControl();
          }
          mHourSpinner.setValue(paramAnonymousInt1);
        }
        else if ((paramAnonymousInt1 == i) && (paramAnonymousInt2 == j))
        {
          paramAnonymousInt1 = mHourSpinner.getValue() - 1;
          if ((!is24Hour()) && (paramAnonymousInt1 == 11))
          {
            TimePickerSpinnerDelegate.access$102(TimePickerSpinnerDelegate.this, mIsAm ^ true);
            TimePickerSpinnerDelegate.this.updateAmPmControl();
          }
          mHourSpinner.setValue(paramAnonymousInt1);
        }
        TimePickerSpinnerDelegate.this.onTimeChanged();
      }
    });
    mMinuteSpinnerInput = ((EditText)mMinuteSpinner.findViewById(16909191));
    mMinuteSpinnerInput.setImeOptions(5);
    mAmPmStrings = getAmPmStrings(paramContext);
    paramContext = mDelegator.findViewById(16908737);
    if ((paramContext instanceof Button))
    {
      mAmPmSpinner = null;
      mAmPmSpinnerInput = null;
      mAmPmButton = ((Button)paramContext);
      mAmPmButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          paramAnonymousView.requestFocus();
          TimePickerSpinnerDelegate.access$102(TimePickerSpinnerDelegate.this, mIsAm ^ true);
          TimePickerSpinnerDelegate.this.updateAmPmControl();
          TimePickerSpinnerDelegate.this.onTimeChanged();
        }
      });
    }
    else
    {
      mAmPmButton = null;
      mAmPmSpinner = ((NumberPicker)paramContext);
      mAmPmSpinner.setMinValue(0);
      mAmPmSpinner.setMaxValue(1);
      mAmPmSpinner.setDisplayedValues(mAmPmStrings);
      mAmPmSpinner.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
      {
        public void onValueChange(NumberPicker paramAnonymousNumberPicker, int paramAnonymousInt1, int paramAnonymousInt2)
        {
          TimePickerSpinnerDelegate.this.updateInputState();
          paramAnonymousNumberPicker.requestFocus();
          TimePickerSpinnerDelegate.access$102(TimePickerSpinnerDelegate.this, mIsAm ^ true);
          TimePickerSpinnerDelegate.this.updateAmPmControl();
          TimePickerSpinnerDelegate.this.onTimeChanged();
        }
      });
      mAmPmSpinnerInput = ((EditText)mAmPmSpinner.findViewById(16909191));
      mAmPmSpinnerInput.setImeOptions(6);
    }
    if (isAmPmAtStart())
    {
      paramTimePicker = (ViewGroup)paramTimePicker.findViewById(16909470);
      paramTimePicker.removeView(paramContext);
      paramTimePicker.addView(paramContext, 0);
      paramTimePicker = (ViewGroup.MarginLayoutParams)paramContext.getLayoutParams();
      paramInt1 = paramTimePicker.getMarginStart();
      paramInt2 = paramTimePicker.getMarginEnd();
      if (paramInt1 != paramInt2)
      {
        paramTimePicker.setMarginStart(paramInt2);
        paramTimePicker.setMarginEnd(paramInt1);
      }
    }
    getHourFormatData();
    updateHourControl();
    updateMinuteControl();
    updateAmPmControl();
    mTempCalendar = Calendar.getInstance(mLocale);
    setHour(mTempCalendar.get(11));
    setMinute(mTempCalendar.get(12));
    if (!isEnabled()) {
      setEnabled(false);
    }
    setContentDescriptions();
    if (mDelegator.getImportantForAccessibility() == 0) {
      mDelegator.setImportantForAccessibility(1);
    }
  }
  
  public static String[] getAmPmStrings(Context paramContext)
  {
    Object localObject = LocaleData.get(getResourcesgetConfigurationlocale);
    if (amPm[0].length() > 4) {
      paramContext = narrowAm;
    } else {
      paramContext = amPm[0];
    }
    if (amPm[1].length() > 4) {
      localObject = narrowPm;
    } else {
      localObject = amPm[1];
    }
    return new String[] { paramContext, localObject };
  }
  
  private void getHourFormatData()
  {
    Locale localLocale = mLocale;
    if (mIs24HourView) {
      str = "Hm";
    } else {
      str = "hm";
    }
    String str = DateFormat.getBestDateTimePattern(localLocale, str);
    int i = str.length();
    int j = 0;
    mHourWithTwoDigit = false;
    while (j < i)
    {
      int k = str.charAt(j);
      if ((k != 72) && (k != 104) && (k != 75) && (k != 107))
      {
        j++;
      }
      else
      {
        mHourFormat = ((char)k);
        if ((j + 1 < i) && (k == str.charAt(j + 1))) {
          mHourWithTwoDigit = true;
        }
      }
    }
  }
  
  private boolean isAmPmAtStart()
  {
    return DateFormat.getBestDateTimePattern(mLocale, "hm").startsWith("a");
  }
  
  private void onTimeChanged()
  {
    mDelegator.sendAccessibilityEvent(4);
    if (mOnTimeChangedListener != null) {
      mOnTimeChangedListener.onTimeChanged(mDelegator, getHour(), getMinute());
    }
    if (mAutoFillChangeListener != null) {
      mAutoFillChangeListener.onTimeChanged(mDelegator, getHour(), getMinute());
    }
  }
  
  private void setContentDescriptions()
  {
    trySetContentDescription(mMinuteSpinner, 16909034, 17041118);
    trySetContentDescription(mMinuteSpinner, 16908896, 17041112);
    trySetContentDescription(mHourSpinner, 16909034, 17041117);
    trySetContentDescription(mHourSpinner, 16908896, 17041111);
    if (mAmPmSpinner != null)
    {
      trySetContentDescription(mAmPmSpinner, 16909034, 17041119);
      trySetContentDescription(mAmPmSpinner, 16908896, 17041113);
    }
  }
  
  private void setCurrentHour(int paramInt, boolean paramBoolean)
  {
    if (paramInt == getHour()) {
      return;
    }
    resetAutofilledValue();
    int i = paramInt;
    if (!is24Hour())
    {
      if (paramInt >= 12)
      {
        mIsAm = false;
        i = paramInt;
        if (paramInt > 12) {
          i = paramInt - 12;
        }
      }
      else
      {
        mIsAm = true;
        i = paramInt;
        if (paramInt == 0) {
          i = 12;
        }
      }
      updateAmPmControl();
    }
    mHourSpinner.setValue(i);
    if (paramBoolean) {
      onTimeChanged();
    }
  }
  
  private void setCurrentMinute(int paramInt, boolean paramBoolean)
  {
    if (paramInt == getMinute()) {
      return;
    }
    resetAutofilledValue();
    mMinuteSpinner.setValue(paramInt);
    if (paramBoolean) {
      onTimeChanged();
    }
  }
  
  private void setDividerText()
  {
    if (mIs24HourView) {
      str = "Hm";
    } else {
      str = "hm";
    }
    String str = DateFormat.getBestDateTimePattern(mLocale, str);
    int i = str.lastIndexOf('H');
    int j = i;
    if (i == -1) {
      j = str.lastIndexOf('h');
    }
    if (j == -1) {
      str = ":";
    }
    for (;;)
    {
      break;
      i = str.indexOf('m', j + 1);
      if (i == -1) {
        str = Character.toString(str.charAt(j + 1));
      } else {
        str = str.substring(j + 1, i);
      }
    }
    mDivider.setText(str);
  }
  
  private void trySetContentDescription(View paramView, int paramInt1, int paramInt2)
  {
    paramView = paramView.findViewById(paramInt1);
    if (paramView != null) {
      paramView.setContentDescription(mContext.getString(paramInt2));
    }
  }
  
  private void updateAmPmControl()
  {
    if (is24Hour())
    {
      if (mAmPmSpinner != null) {
        mAmPmSpinner.setVisibility(8);
      } else {
        mAmPmButton.setVisibility(8);
      }
    }
    else
    {
      int i = mIsAm ^ true;
      if (mAmPmSpinner != null)
      {
        mAmPmSpinner.setValue(i);
        mAmPmSpinner.setVisibility(0);
      }
      else
      {
        mAmPmButton.setText(mAmPmStrings[i]);
        mAmPmButton.setVisibility(0);
      }
    }
    mDelegator.sendAccessibilityEvent(4);
  }
  
  private void updateHourControl()
  {
    if (is24Hour())
    {
      if (mHourFormat == 'k')
      {
        mHourSpinner.setMinValue(1);
        mHourSpinner.setMaxValue(24);
      }
      else
      {
        mHourSpinner.setMinValue(0);
        mHourSpinner.setMaxValue(23);
      }
    }
    else if (mHourFormat == 'K')
    {
      mHourSpinner.setMinValue(0);
      mHourSpinner.setMaxValue(11);
    }
    else
    {
      mHourSpinner.setMinValue(1);
      mHourSpinner.setMaxValue(12);
    }
    NumberPicker localNumberPicker = mHourSpinner;
    NumberPicker.Formatter localFormatter;
    if (mHourWithTwoDigit) {
      localFormatter = NumberPicker.getTwoDigitFormatter();
    } else {
      localFormatter = null;
    }
    localNumberPicker.setFormatter(localFormatter);
  }
  
  private void updateInputState()
  {
    InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
    if (localInputMethodManager != null) {
      if (localInputMethodManager.isActive(mHourSpinnerInput))
      {
        mHourSpinnerInput.clearFocus();
        localInputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
      }
      else if (localInputMethodManager.isActive(mMinuteSpinnerInput))
      {
        mMinuteSpinnerInput.clearFocus();
        localInputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
      }
      else if (localInputMethodManager.isActive(mAmPmSpinnerInput))
      {
        mAmPmSpinnerInput.clearFocus();
        localInputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
      }
    }
  }
  
  private void updateMinuteControl()
  {
    if (is24Hour()) {
      mMinuteSpinnerInput.setImeOptions(6);
    } else {
      mMinuteSpinnerInput.setImeOptions(5);
    }
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    onPopulateAccessibilityEvent(paramAccessibilityEvent);
    return true;
  }
  
  public View getAmView()
  {
    return mAmPmSpinnerInput;
  }
  
  public int getBaseline()
  {
    return mHourSpinner.getBaseline();
  }
  
  public int getHour()
  {
    int i = mHourSpinner.getValue();
    if (is24Hour()) {
      return i;
    }
    if (mIsAm) {
      return i % 12;
    }
    return i % 12 + 12;
  }
  
  public View getHourView()
  {
    return mHourSpinnerInput;
  }
  
  public int getMinute()
  {
    return mMinuteSpinner.getValue();
  }
  
  public View getMinuteView()
  {
    return mMinuteSpinnerInput;
  }
  
  public View getPmView()
  {
    return mAmPmSpinnerInput;
  }
  
  public boolean is24Hour()
  {
    return mIs24HourView;
  }
  
  public boolean isEnabled()
  {
    return mIsEnabled;
  }
  
  public void onPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    int i;
    if (mIs24HourView) {
      i = 0x1 | 0x80;
    } else {
      i = 0x1 | 0x40;
    }
    mTempCalendar.set(11, getHour());
    mTempCalendar.set(12, getMinute());
    String str = DateUtils.formatDateTime(mContext, mTempCalendar.getTimeInMillis(), i);
    paramAccessibilityEvent.getText().add(str);
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable instanceof TimePicker.AbstractTimePickerDelegate.SavedState))
    {
      paramParcelable = (TimePicker.AbstractTimePickerDelegate.SavedState)paramParcelable;
      setHour(paramParcelable.getHour());
      setMinute(paramParcelable.getMinute());
    }
  }
  
  public Parcelable onSaveInstanceState(Parcelable paramParcelable)
  {
    return new TimePicker.AbstractTimePickerDelegate.SavedState(paramParcelable, getHour(), getMinute(), is24Hour());
  }
  
  public void setDate(int paramInt1, int paramInt2)
  {
    setCurrentHour(paramInt1, false);
    setCurrentMinute(paramInt2, false);
    onTimeChanged();
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    mMinuteSpinner.setEnabled(paramBoolean);
    if (mDivider != null) {
      mDivider.setEnabled(paramBoolean);
    }
    mHourSpinner.setEnabled(paramBoolean);
    if (mAmPmSpinner != null) {
      mAmPmSpinner.setEnabled(paramBoolean);
    } else {
      mAmPmButton.setEnabled(paramBoolean);
    }
    mIsEnabled = paramBoolean;
  }
  
  public void setHour(int paramInt)
  {
    setCurrentHour(paramInt, true);
  }
  
  public void setIs24Hour(boolean paramBoolean)
  {
    if (mIs24HourView == paramBoolean) {
      return;
    }
    int i = getHour();
    mIs24HourView = paramBoolean;
    getHourFormatData();
    updateHourControl();
    setCurrentHour(i, false);
    updateMinuteControl();
    updateAmPmControl();
  }
  
  public void setMinute(int paramInt)
  {
    setCurrentMinute(paramInt, true);
  }
  
  public boolean validateInput()
  {
    return true;
  }
}
