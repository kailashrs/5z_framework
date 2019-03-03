package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.icu.text.DecimalFormatSymbols;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.style.TtsSpan.VerbatimBuilder;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.R.styleable;
import com.android.internal.widget.NumericTextView;
import com.android.internal.widget.NumericTextView.OnValueChangedListener;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

class TimePickerClockDelegate
  extends TimePicker.AbstractTimePickerDelegate
{
  private static final int AM = 0;
  private static final int[] ATTRS_DISABLED_ALPHA = { 16842803 };
  private static final int[] ATTRS_TEXT_COLOR = { 16842904 };
  private static final long DELAY_COMMIT_MILLIS = 2000L;
  private static final int FROM_EXTERNAL_API = 0;
  private static final int FROM_INPUT_PICKER = 2;
  private static final int FROM_RADIAL_PICKER = 1;
  private static final int HOURS_IN_HALF_DAY = 12;
  private static final int HOUR_INDEX = 0;
  private static final int MINUTE_INDEX = 1;
  private static final int PM = 1;
  private boolean mAllowAutoAdvance;
  private final RadioButton mAmLabel;
  private final View mAmPmLayout;
  private final View.OnClickListener mClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      int i = paramAnonymousView.getId();
      if (i != 16908738)
      {
        if (i != 16909014)
        {
          if (i != 16909131)
          {
            if (i != 16909248) {
              return;
            }
            TimePickerClockDelegate.this.setAmOrPm(1);
          }
          else
          {
            TimePickerClockDelegate.this.setCurrentItemShowing(1, true, true);
          }
        }
        else {
          TimePickerClockDelegate.this.setCurrentItemShowing(0, true, true);
        }
      }
      else {
        TimePickerClockDelegate.this.setAmOrPm(0);
      }
      TimePickerClockDelegate.this.tryVibrate();
    }
  };
  private final Runnable mCommitHour = new Runnable()
  {
    public void run()
    {
      setHour(mHourView.getValue());
    }
  };
  private final Runnable mCommitMinute = new Runnable()
  {
    public void run()
    {
      setMinute(mMinuteView.getValue());
    }
  };
  private int mCurrentHour;
  private int mCurrentMinute;
  private final NumericTextView.OnValueChangedListener mDigitEnteredListener = new NumericTextView.OnValueChangedListener()
  {
    public void onValueChanged(NumericTextView paramAnonymousNumericTextView, int paramAnonymousInt, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2)
    {
      Runnable localRunnable;
      NumericTextView localNumericTextView;
      if (paramAnonymousNumericTextView == mHourView)
      {
        localRunnable = mCommitHour;
        if (paramAnonymousNumericTextView.isFocused()) {
          localNumericTextView = mMinuteView;
        } else {
          localNumericTextView = null;
        }
      }
      else
      {
        if (paramAnonymousNumericTextView != mMinuteView) {
          return;
        }
        localRunnable = mCommitMinute;
        localNumericTextView = null;
      }
      paramAnonymousNumericTextView.removeCallbacks(localRunnable);
      if (paramAnonymousBoolean1) {
        if (paramAnonymousBoolean2)
        {
          localRunnable.run();
          if (localNumericTextView != null) {
            localNumericTextView.requestFocus();
          }
        }
        else
        {
          paramAnonymousNumericTextView.postDelayed(localRunnable, 2000L);
        }
      }
      return;
    }
  };
  private final View.OnFocusChangeListener mFocusListener = new View.OnFocusChangeListener()
  {
    public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
    {
      if (paramAnonymousBoolean)
      {
        int i = paramAnonymousView.getId();
        if (i != 16908738)
        {
          if (i != 16909014)
          {
            if (i != 16909131)
            {
              if (i != 16909248) {
                return;
              }
              TimePickerClockDelegate.this.setAmOrPm(1);
            }
            else
            {
              TimePickerClockDelegate.this.setCurrentItemShowing(1, true, true);
            }
          }
          else {
            TimePickerClockDelegate.this.setCurrentItemShowing(0, true, true);
          }
        }
        else {
          TimePickerClockDelegate.this.setAmOrPm(0);
        }
        TimePickerClockDelegate.this.tryVibrate();
      }
    }
  };
  private boolean mHourFormatShowLeadingZero;
  private boolean mHourFormatStartsAtZero;
  private final NumericTextView mHourView;
  private boolean mIs24Hour;
  private boolean mIsAmPmAtLeft = false;
  private boolean mIsAmPmAtTop = false;
  private boolean mIsEnabled = true;
  private boolean mLastAnnouncedIsHour;
  private CharSequence mLastAnnouncedText;
  private final NumericTextView mMinuteView;
  private final RadialTimePickerView.OnValueSelectedListener mOnValueSelectedListener = new RadialTimePickerView.OnValueSelectedListener()
  {
    public void onValueSelected(int paramAnonymousInt1, int paramAnonymousInt2, boolean paramAnonymousBoolean)
    {
      int i = 0;
      int j = 0;
      int k = 0;
      switch (paramAnonymousInt1)
      {
      default: 
        k = j;
        break;
      case 1: 
        if (getMinute() != paramAnonymousInt2) {
          k = 1;
        }
        TimePickerClockDelegate.this.setMinuteInternal(paramAnonymousInt2, 1, true);
        break;
      case 0: 
        paramAnonymousInt1 = i;
        if (getHour() != paramAnonymousInt2) {
          paramAnonymousInt1 = 1;
        }
        if ((mAllowAutoAdvance) && (paramAnonymousBoolean)) {
          i = 1;
        } else {
          i = 0;
        }
        Object localObject = TimePickerClockDelegate.this;
        if (i == 0) {
          paramAnonymousBoolean = true;
        } else {
          paramAnonymousBoolean = false;
        }
        ((TimePickerClockDelegate)localObject).setHourInternal(paramAnonymousInt2, 1, paramAnonymousBoolean, true);
        k = paramAnonymousInt1;
        if (i != 0)
        {
          TimePickerClockDelegate.this.setCurrentItemShowing(1, true, false);
          paramAnonymousInt2 = TimePickerClockDelegate.this.getLocalizedHour(paramAnonymousInt2);
          TimePicker localTimePicker = mDelegator;
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append(paramAnonymousInt2);
          ((StringBuilder)localObject).append(". ");
          ((StringBuilder)localObject).append(mSelectMinutes);
          localTimePicker.announceForAccessibility(((StringBuilder)localObject).toString());
          k = paramAnonymousInt1;
        }
        break;
      }
      if ((mOnTimeChangedListener != null) && (k != 0)) {
        mOnTimeChangedListener.onTimeChanged(mDelegator, getHour(), getMinute());
      }
    }
  };
  private final TextInputTimePickerView.OnValueTypedListener mOnValueTypedListener = new TextInputTimePickerView.OnValueTypedListener()
  {
    public void onValueChanged(int paramAnonymousInt1, int paramAnonymousInt2)
    {
      switch (paramAnonymousInt1)
      {
      default: 
        break;
      case 2: 
        TimePickerClockDelegate.this.setAmOrPm(paramAnonymousInt2);
        break;
      case 1: 
        TimePickerClockDelegate.this.setMinuteInternal(paramAnonymousInt2, 2, true);
        break;
      case 0: 
        TimePickerClockDelegate.this.setHourInternal(paramAnonymousInt2, 2, false, true);
      }
    }
  };
  private final RadioButton mPmLabel;
  private boolean mRadialPickerModeEnabled = true;
  private final View mRadialTimePickerHeader;
  private final ImageButton mRadialTimePickerModeButton;
  private final String mRadialTimePickerModeEnabledDescription;
  private final RadialTimePickerView mRadialTimePickerView;
  private final String mSelectHours;
  private final String mSelectMinutes;
  private final TextView mSeparatorView;
  private final Calendar mTempCalendar;
  private final View mTextInputPickerHeader;
  private final String mTextInputPickerModeEnabledDescription;
  private final TextInputTimePickerView mTextInputPickerView;
  
  public TimePickerClockDelegate(TimePicker paramTimePicker, Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramTimePicker, paramContext);
    TypedArray localTypedArray = mContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TimePicker, paramInt1, paramInt2);
    Object localObject1 = (LayoutInflater)mContext.getSystemService("layout_inflater");
    Object localObject2 = mContext.getResources();
    mSelectHours = ((Resources)localObject2).getString(17040961);
    mSelectMinutes = ((Resources)localObject2).getString(17040965);
    localObject1 = ((LayoutInflater)localObject1).inflate(localTypedArray.getResourceId(12, 17367345), paramTimePicker);
    ((View)localObject1).setSaveFromParentEnabled(false);
    mRadialTimePickerHeader = ((View)localObject1).findViewById(16909473);
    mRadialTimePickerHeader.setOnTouchListener(new NearestTouchDelegate(null));
    mHourView = ((NumericTextView)((View)localObject1).findViewById(16909014));
    mHourView.setOnClickListener(mClickListener);
    mHourView.setOnFocusChangeListener(mFocusListener);
    mHourView.setOnDigitEnteredListener(mDigitEnteredListener);
    mHourView.setAccessibilityDelegate(new ClickActionDelegate(paramContext, 17040961));
    mSeparatorView = ((TextView)((View)localObject1).findViewById(16909342));
    mMinuteView = ((NumericTextView)((View)localObject1).findViewById(16909131));
    mMinuteView.setOnClickListener(mClickListener);
    mMinuteView.setOnFocusChangeListener(mFocusListener);
    mMinuteView.setOnDigitEnteredListener(mDigitEnteredListener);
    mMinuteView.setAccessibilityDelegate(new ClickActionDelegate(paramContext, 17040965));
    mMinuteView.setRange(0, 59);
    mAmPmLayout = ((View)localObject1).findViewById(16908740);
    mAmPmLayout.setOnTouchListener(new NearestTouchDelegate(null));
    paramTimePicker = TimePicker.getAmPmStrings(paramContext);
    mAmLabel = ((RadioButton)mAmPmLayout.findViewById(16908738));
    mAmLabel.setText(obtainVerbatim(paramTimePicker[0]));
    mAmLabel.setOnClickListener(mClickListener);
    ensureMinimumTextWidth(mAmLabel);
    mPmLabel = ((RadioButton)mAmPmLayout.findViewById(16909248));
    mPmLabel.setText(obtainVerbatim(paramTimePicker[1]));
    mPmLabel.setOnClickListener(mClickListener);
    ensureMinimumTextWidth(mPmLabel);
    paramTimePicker = null;
    int i = localTypedArray.getResourceId(1, 0);
    if (i != 0)
    {
      localObject2 = mContext.obtainStyledAttributes(null, ATTRS_TEXT_COLOR, 0, i);
      paramTimePicker = applyLegacyColorFixes(((TypedArray)localObject2).getColorStateList(0));
      ((TypedArray)localObject2).recycle();
    }
    localObject2 = paramTimePicker;
    if (paramTimePicker == null) {
      localObject2 = localTypedArray.getColorStateList(11);
    }
    mTextInputPickerHeader = ((View)localObject1).findViewById(16909042);
    if (localObject2 != null)
    {
      mHourView.setTextColor((ColorStateList)localObject2);
      mSeparatorView.setTextColor((ColorStateList)localObject2);
      mMinuteView.setTextColor((ColorStateList)localObject2);
      mAmLabel.setTextColor((ColorStateList)localObject2);
      mPmLabel.setTextColor((ColorStateList)localObject2);
    }
    if (localTypedArray.hasValueOrEmpty(0))
    {
      mRadialTimePickerHeader.setBackground(localTypedArray.getDrawable(0));
      mTextInputPickerHeader.setBackground(localTypedArray.getDrawable(0));
    }
    localTypedArray.recycle();
    mRadialTimePickerView = ((RadialTimePickerView)((View)localObject1).findViewById(16909273));
    mRadialTimePickerView.applyAttributes(paramAttributeSet, paramInt1, paramInt2);
    mRadialTimePickerView.setOnValueSelectedListener(mOnValueSelectedListener);
    mTextInputPickerView = ((TextInputTimePickerView)((View)localObject1).findViewById(16909045));
    mTextInputPickerView.setListener(mOnValueTypedListener);
    mRadialTimePickerModeButton = ((ImageButton)((View)localObject1).findViewById(16909489));
    mRadialTimePickerModeButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        TimePickerClockDelegate.this.toggleRadialPickerMode();
      }
    });
    mRadialTimePickerModeEnabledDescription = paramContext.getResources().getString(17041124);
    mTextInputPickerModeEnabledDescription = paramContext.getResources().getString(17041125);
    mAllowAutoAdvance = true;
    updateHourFormat();
    mTempCalendar = Calendar.getInstance(mLocale);
    initialize(mTempCalendar.get(11), mTempCalendar.get(12), mIs24Hour, 0);
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
      if ((i != 0) && (j != 0)) {
        return new ColorStateList(new int[][] { { 16843518 }, new int[0] }, new int[] { i, j });
      }
      return null;
    }
    return paramColorStateList;
  }
  
  private static void ensureMinimumTextWidth(TextView paramTextView)
  {
    paramTextView.measure(0, 0);
    int i = paramTextView.getMeasuredWidth();
    paramTextView.setMinWidth(i);
    paramTextView.setMinimumWidth(i);
  }
  
  private int getCurrentItemShowing()
  {
    return mRadialTimePickerView.getCurrentItemShowing();
  }
  
  private static String getHourMinSeparatorFromPattern(String paramString)
  {
    int i = 0;
    for (int j = 0; j < paramString.length(); j++)
    {
      int k = paramString.charAt(j);
      if (k != 32) {
        if (k != 39)
        {
          if ((k != 72) && (k != 75) && (k != 104) && (k != 107))
          {
            if (i != 0) {
              return Character.toString(paramString.charAt(j));
            }
          }
          else {
            i = 1;
          }
        }
        else if (i != 0)
        {
          paramString = new SpannableStringBuilder(paramString.substring(j));
          return paramString.subSequence(0, DateFormat.appendQuotedText(paramString, 0)).toString();
        }
      }
    }
    return ":";
  }
  
  private int getLocalizedHour(int paramInt)
  {
    int i = paramInt;
    if (!mIs24Hour) {
      i = paramInt % 12;
    }
    paramInt = i;
    if (!mHourFormatStartsAtZero)
    {
      paramInt = i;
      if (i == 0) {
        if (mIs24Hour) {
          paramInt = 24;
        } else {
          paramInt = 12;
        }
      }
    }
    return paramInt;
  }
  
  private void initialize(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    mCurrentHour = paramInt1;
    mCurrentMinute = paramInt2;
    mIs24Hour = paramBoolean;
    updateUI(paramInt3);
  }
  
  private static int lastIndexOfAny(String paramString, char[] paramArrayOfChar)
  {
    int i = paramArrayOfChar.length;
    if (i > 0) {
      for (int j = paramString.length() - 1; j >= 0; j--)
      {
        int k = paramString.charAt(j);
        for (int m = 0; m < i; m++) {
          if (k == paramArrayOfChar[m]) {
            return j;
          }
        }
      }
    }
    return -1;
  }
  
  private int multiplyAlphaComponent(int paramInt, float paramFloat)
  {
    return (int)((paramInt >> 24 & 0xFF) * paramFloat + 0.5F) << 24 | 0xFFFFFF & paramInt;
  }
  
  static final CharSequence obtainVerbatim(String paramString)
  {
    return new SpannableStringBuilder().append(paramString, new TtsSpan.VerbatimBuilder(paramString).build(), 0);
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
  
  private void setAmOrPm(int paramInt)
  {
    updateAmPmLabelStates(paramInt);
    if (mRadialTimePickerView.setAmOrPm(paramInt))
    {
      mCurrentHour = getHour();
      updateTextInputPicker();
      if (mOnTimeChangedListener != null) {
        mOnTimeChangedListener.onTimeChanged(mDelegator, getHour(), getMinute());
      }
    }
  }
  
  private void setAmPmStart(boolean paramBoolean)
  {
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)mAmPmLayout.getLayoutParams();
    if ((localLayoutParams.getRule(1) == 0) && (localLayoutParams.getRule(0) == 0))
    {
      if ((localLayoutParams.getRule(3) != 0) || (localLayoutParams.getRule(2) != 0))
      {
        if (mIsAmPmAtTop == paramBoolean) {
          return;
        }
        if (paramBoolean)
        {
          i = localLayoutParams.getRule(3);
          localLayoutParams.removeRule(3);
          localLayoutParams.addRule(2, i);
        }
        else
        {
          i = localLayoutParams.getRule(2);
          localLayoutParams.removeRule(2);
          localLayoutParams.addRule(3, i);
        }
        View localView = mRadialTimePickerHeader.findViewById(i);
        int j = localView.getPaddingTop();
        int i = localView.getPaddingBottom();
        localView.setPadding(localView.getPaddingLeft(), i, localView.getPaddingRight(), j);
        mIsAmPmAtTop = paramBoolean;
      }
    }
    else
    {
      if (TextUtils.getLayoutDirectionFromLocale(mLocale) != 0) {
        paramBoolean ^= true;
      }
      if (mIsAmPmAtLeft == paramBoolean) {
        return;
      }
      if (paramBoolean)
      {
        localLayoutParams.removeRule(1);
        localLayoutParams.addRule(0, mHourView.getId());
      }
      else
      {
        localLayoutParams.removeRule(0);
        localLayoutParams.addRule(1, mMinuteView.getId());
      }
      mIsAmPmAtLeft = paramBoolean;
    }
    mAmPmLayout.setLayoutParams(localLayoutParams);
  }
  
  private void setCurrentItemShowing(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    mRadialTimePickerView.setCurrentItemShowing(paramInt, paramBoolean1);
    if (paramInt == 0)
    {
      if (paramBoolean2) {
        mDelegator.announceForAccessibility(mSelectHours);
      }
    }
    else if (paramBoolean2) {
      mDelegator.announceForAccessibility(mSelectMinutes);
    }
    NumericTextView localNumericTextView = mHourView;
    paramBoolean2 = false;
    if (paramInt == 0) {
      paramBoolean1 = true;
    } else {
      paramBoolean1 = false;
    }
    localNumericTextView.setActivated(paramBoolean1);
    localNumericTextView = mMinuteView;
    paramBoolean1 = paramBoolean2;
    if (paramInt == 1) {
      paramBoolean1 = true;
    }
    localNumericTextView.setActivated(paramBoolean1);
  }
  
  private void setHourInternal(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (mCurrentHour == paramInt1) {
      return;
    }
    resetAutofilledValue();
    mCurrentHour = paramInt1;
    updateHeaderHour(paramInt1, paramBoolean1);
    updateHeaderAmPm();
    int i = 1;
    if (paramInt2 != 1)
    {
      mRadialTimePickerView.setCurrentHour(paramInt1);
      RadialTimePickerView localRadialTimePickerView = mRadialTimePickerView;
      if (paramInt1 < 12) {
        i = 0;
      }
      localRadialTimePickerView.setAmOrPm(i);
    }
    if (paramInt2 != 2) {
      updateTextInputPicker();
    }
    mDelegator.invalidate();
    if (paramBoolean2) {
      onTimeChanged();
    }
  }
  
  private void setMinuteInternal(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (mCurrentMinute == paramInt1) {
      return;
    }
    resetAutofilledValue();
    mCurrentMinute = paramInt1;
    updateHeaderMinute(paramInt1, true);
    if (paramInt2 != 1) {
      mRadialTimePickerView.setCurrentMinute(paramInt1);
    }
    if (paramInt2 != 2) {
      updateTextInputPicker();
    }
    mDelegator.invalidate();
    if (paramBoolean) {
      onTimeChanged();
    }
  }
  
  private void toggleRadialPickerMode()
  {
    if (mRadialPickerModeEnabled)
    {
      mRadialTimePickerView.setVisibility(8);
      mRadialTimePickerHeader.setVisibility(8);
      mTextInputPickerHeader.setVisibility(0);
      mTextInputPickerView.setVisibility(0);
      mRadialTimePickerModeButton.setImageResource(17302057);
      mRadialTimePickerModeButton.setContentDescription(mRadialTimePickerModeEnabledDescription);
      mRadialPickerModeEnabled = false;
    }
    else
    {
      mRadialTimePickerView.setVisibility(0);
      mRadialTimePickerHeader.setVisibility(0);
      mTextInputPickerHeader.setVisibility(8);
      mTextInputPickerView.setVisibility(8);
      mRadialTimePickerModeButton.setImageResource(17302134);
      mRadialTimePickerModeButton.setContentDescription(mTextInputPickerModeEnabledDescription);
      updateTextInputPicker();
      InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
      if (localInputMethodManager != null) {
        localInputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
      }
      mRadialPickerModeEnabled = true;
    }
  }
  
  private void tryAnnounceForAccessibility(CharSequence paramCharSequence, boolean paramBoolean)
  {
    if ((mLastAnnouncedIsHour != paramBoolean) || (!paramCharSequence.equals(mLastAnnouncedText)))
    {
      mDelegator.announceForAccessibility(paramCharSequence);
      mLastAnnouncedText = paramCharSequence;
      mLastAnnouncedIsHour = paramBoolean;
    }
  }
  
  private void tryVibrate()
  {
    mDelegator.performHapticFeedback(4);
  }
  
  private void updateAmPmLabelStates(int paramInt)
  {
    boolean bool1 = false;
    if (paramInt == 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mAmLabel.setActivated(bool2);
    mAmLabel.setChecked(bool2);
    boolean bool2 = bool1;
    if (paramInt == 1) {
      bool2 = true;
    }
    mPmLabel.setActivated(bool2);
    mPmLabel.setChecked(bool2);
  }
  
  private void updateHeaderAmPm()
  {
    if (mIs24Hour)
    {
      mAmPmLayout.setVisibility(8);
    }
    else
    {
      setAmPmStart(DateFormat.getBestDateTimePattern(mLocale, "hm").startsWith("a"));
      int i;
      if (mCurrentHour < 12) {
        i = 0;
      } else {
        i = 1;
      }
      updateAmPmLabelStates(i);
    }
  }
  
  private void updateHeaderHour(int paramInt, boolean paramBoolean)
  {
    paramInt = getLocalizedHour(paramInt);
    mHourView.setValue(paramInt);
    if (paramBoolean) {
      tryAnnounceForAccessibility(mHourView.getText(), true);
    }
  }
  
  private void updateHeaderMinute(int paramInt, boolean paramBoolean)
  {
    mMinuteView.setValue(paramInt);
    if (paramBoolean) {
      tryAnnounceForAccessibility(mMinuteView.getText(), false);
    }
  }
  
  private void updateHeaderSeparator()
  {
    Locale localLocale = mLocale;
    if (mIs24Hour) {
      str = "Hm";
    } else {
      str = "hm";
    }
    String str = getHourMinSeparatorFromPattern(DateFormat.getBestDateTimePattern(localLocale, str));
    mSeparatorView.setText(str);
    mTextInputPickerView.updateSeparator(str);
  }
  
  private void updateHourFormat()
  {
    Locale localLocale = mLocale;
    if (mIs24Hour) {
      localObject = "Hm";
    } else {
      localObject = "hm";
    }
    Object localObject = DateFormat.getBestDateTimePattern(localLocale, (String)localObject);
    int i = ((String)localObject).length();
    boolean bool1 = false;
    int j = 0;
    int k = 0;
    int i3;
    for (int m = 0;; m++)
    {
      bool2 = bool1;
      i2 = j;
      if (m >= i) {
        break label153;
      }
      i3 = ((String)localObject).charAt(m);
      if ((i3 == 72) || (i3 == 104) || (i3 == 75) || (i3 == 107)) {
        break;
      }
    }
    j = i3;
    boolean bool2 = bool1;
    int i2 = j;
    if (m + 1 < i)
    {
      bool2 = bool1;
      i2 = j;
      if (i3 == ((String)localObject).charAt(m + 1))
      {
        bool2 = true;
        i2 = j;
      }
    }
    label153:
    mHourFormatShowLeadingZero = bool2;
    if ((i2 != 75) && (i2 != 72)) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mHourFormatStartsAtZero = bool2;
    int n = true ^ mHourFormatStartsAtZero;
    if (mIs24Hour) {
      i2 = 23;
    } else {
      i2 = 11;
    }
    mHourView.setRange(n, i2 + n);
    mHourView.setShowLeadingZeroes(mHourFormatShowLeadingZero);
    localObject = DecimalFormatSymbols.getInstance(mLocale).getDigitStrings();
    int i1 = 0;
    for (i2 = k; i2 < 10; i2++) {
      i1 = Math.max(i1, localObject[i2].length());
    }
    mTextInputPickerView.setHourFormat(i1 * 2);
  }
  
  private void updateRadialPicker(int paramInt)
  {
    mRadialTimePickerView.initialize(mCurrentHour, mCurrentMinute, mIs24Hour);
    setCurrentItemShowing(paramInt, false, true);
  }
  
  private void updateTextInputPicker()
  {
    TextInputTimePickerView localTextInputTimePickerView = mTextInputPickerView;
    int i = getLocalizedHour(mCurrentHour);
    int j = mCurrentMinute;
    int k;
    if (mCurrentHour < 12) {
      k = 0;
    } else {
      k = 1;
    }
    localTextInputTimePickerView.updateTextInputValues(i, j, k, mIs24Hour, mHourFormatStartsAtZero);
  }
  
  private void updateUI(int paramInt)
  {
    updateHeaderAmPm();
    updateHeaderHour(mCurrentHour, false);
    updateHeaderSeparator();
    updateHeaderMinute(mCurrentMinute, false);
    updateRadialPicker(paramInt);
    updateTextInputPicker();
    mDelegator.invalidate();
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    onPopulateAccessibilityEvent(paramAccessibilityEvent);
    return true;
  }
  
  public View getAmView()
  {
    return mAmLabel;
  }
  
  public int getBaseline()
  {
    return -1;
  }
  
  public int getHour()
  {
    int i = mRadialTimePickerView.getCurrentHour();
    if (mIs24Hour) {
      return i;
    }
    if (mRadialTimePickerView.getAmOrPm() == 1) {
      return i % 12 + 12;
    }
    return i % 12;
  }
  
  public View getHourView()
  {
    return mHourView;
  }
  
  public int getMinute()
  {
    return mRadialTimePickerView.getCurrentMinute();
  }
  
  public View getMinuteView()
  {
    return mMinuteView;
  }
  
  public View getPmView()
  {
    return mPmLabel;
  }
  
  public boolean is24Hour()
  {
    return mIs24Hour;
  }
  
  public boolean isEnabled()
  {
    return mIsEnabled;
  }
  
  public void onPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    int i;
    if (mIs24Hour) {
      i = 0x1 | 0x80;
    } else {
      i = 0x1 | 0x40;
    }
    mTempCalendar.set(11, getHour());
    mTempCalendar.set(12, getMinute());
    String str1 = DateUtils.formatDateTime(mContext, mTempCalendar.getTimeInMillis(), i);
    String str2;
    if (mRadialTimePickerView.getCurrentItemShowing() == 0) {
      str2 = mSelectHours;
    } else {
      str2 = mSelectMinutes;
    }
    paramAccessibilityEvent = paramAccessibilityEvent.getText();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(str1);
    localStringBuilder.append(" ");
    localStringBuilder.append(str2);
    paramAccessibilityEvent.add(localStringBuilder.toString());
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable instanceof TimePicker.AbstractTimePickerDelegate.SavedState))
    {
      paramParcelable = (TimePicker.AbstractTimePickerDelegate.SavedState)paramParcelable;
      initialize(paramParcelable.getHour(), paramParcelable.getMinute(), paramParcelable.is24HourMode(), paramParcelable.getCurrentItemShowing());
      mRadialTimePickerView.invalidate();
    }
  }
  
  public Parcelable onSaveInstanceState(Parcelable paramParcelable)
  {
    return new TimePicker.AbstractTimePickerDelegate.SavedState(paramParcelable, getHour(), getMinute(), is24Hour(), getCurrentItemShowing());
  }
  
  public void setDate(int paramInt1, int paramInt2)
  {
    setHourInternal(paramInt1, 0, true, false);
    setMinuteInternal(paramInt2, 0, false);
    onTimeChanged();
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    mHourView.setEnabled(paramBoolean);
    mMinuteView.setEnabled(paramBoolean);
    mAmLabel.setEnabled(paramBoolean);
    mPmLabel.setEnabled(paramBoolean);
    mRadialTimePickerView.setEnabled(paramBoolean);
    mIsEnabled = paramBoolean;
  }
  
  public void setHour(int paramInt)
  {
    setHourInternal(paramInt, 0, true, true);
  }
  
  public void setIs24Hour(boolean paramBoolean)
  {
    if (mIs24Hour != paramBoolean)
    {
      mIs24Hour = paramBoolean;
      mCurrentHour = getHour();
      updateHourFormat();
      updateUI(mRadialTimePickerView.getCurrentItemShowing());
    }
  }
  
  public void setMinute(int paramInt)
  {
    setMinuteInternal(paramInt, 0, true);
  }
  
  public boolean validateInput()
  {
    return mTextInputPickerView.validateInput();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface ChangeSource {}
  
  private static class ClickActionDelegate
    extends View.AccessibilityDelegate
  {
    private final AccessibilityNodeInfo.AccessibilityAction mClickAction;
    
    public ClickActionDelegate(Context paramContext, int paramInt)
    {
      mClickAction = new AccessibilityNodeInfo.AccessibilityAction(16, paramContext.getString(paramInt));
    }
    
    public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfo paramAccessibilityNodeInfo)
    {
      super.onInitializeAccessibilityNodeInfo(paramView, paramAccessibilityNodeInfo);
      paramAccessibilityNodeInfo.addAction(mClickAction);
    }
  }
  
  private static class NearestTouchDelegate
    implements View.OnTouchListener
  {
    private View mInitialTouchTarget;
    
    private NearestTouchDelegate() {}
    
    private View findNearestChild(ViewGroup paramViewGroup, int paramInt1, int paramInt2)
    {
      Object localObject = null;
      int i = Integer.MAX_VALUE;
      int j = 0;
      int k = paramViewGroup.getChildCount();
      while (j < k)
      {
        View localView = paramViewGroup.getChildAt(j);
        int m = paramInt1 - (localView.getLeft() + localView.getWidth() / 2);
        int n = paramInt2 - (localView.getTop() + localView.getHeight() / 2);
        n = m * m + n * n;
        m = i;
        if (i > n)
        {
          localObject = localView;
          m = n;
        }
        j++;
        i = m;
      }
      return localObject;
    }
    
    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      int i = paramMotionEvent.getActionMasked();
      if (i == 0) {
        if ((paramView instanceof ViewGroup)) {
          mInitialTouchTarget = findNearestChild((ViewGroup)paramView, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
        } else {
          mInitialTouchTarget = null;
        }
      }
      View localView = mInitialTouchTarget;
      if (localView == null) {
        return false;
      }
      float f1 = paramView.getScrollX() - localView.getLeft();
      float f2 = paramView.getScrollY() - localView.getTop();
      paramMotionEvent.offsetLocation(f1, f2);
      boolean bool = localView.dispatchTouchEvent(paramMotionEvent);
      paramMotionEvent.offsetLocation(-f1, -f2);
      if ((i == 1) || (i == 3)) {
        mInitialTouchTarget = null;
      }
      return bool;
    }
  }
}
