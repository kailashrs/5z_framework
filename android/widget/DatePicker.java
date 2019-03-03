package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.icu.util.Calendar;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View.BaseSavedState;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.autofill.AutofillValue;
import com.android.internal.R.styleable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Locale;

public class DatePicker
  extends FrameLayout
{
  private static final String LOG_TAG = DatePicker.class.getSimpleName();
  public static final int MODE_CALENDAR = 2;
  public static final int MODE_SPINNER = 1;
  private final DatePickerDelegate mDelegate;
  private final int mMode;
  
  public DatePicker(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public DatePicker(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843612);
  }
  
  public DatePicker(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public DatePicker(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    if (getImportantForAutofill() == 0) {
      setImportantForAutofill(1);
    }
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DatePicker, paramInt1, paramInt2);
    boolean bool = localTypedArray.getBoolean(17, false);
    int i = localTypedArray.getInt(16, 1);
    int j = localTypedArray.getInt(3, 0);
    localTypedArray.recycle();
    if ((i == 2) && (bool)) {
      mMode = paramContext.getResources().getInteger(17694942);
    } else {
      mMode = i;
    }
    if (mMode != 2) {
      mDelegate = createSpinnerUIDelegate(paramContext, paramAttributeSet, paramInt1, paramInt2);
    } else {
      mDelegate = createCalendarUIDelegate(paramContext, paramAttributeSet, paramInt1, paramInt2);
    }
    if (j != 0) {
      setFirstDayOfWeek(j);
    }
    mDelegate.setAutoFillChangeListener(new _..Lambda.DatePicker.AnJPL5BrPXPJa_Oc_WUAB_HJq84(this, paramContext));
  }
  
  private DatePickerDelegate createCalendarUIDelegate(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    return new DatePickerCalendarDelegate(this, paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  private DatePickerDelegate createSpinnerUIDelegate(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    return new DatePickerSpinnerDelegate(this, paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  public void autofill(AutofillValue paramAutofillValue)
  {
    if (!isEnabled()) {
      return;
    }
    mDelegate.autofill(paramAutofillValue);
  }
  
  public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    return mDelegate.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent);
  }
  
  public void dispatchProvideAutofillStructure(ViewStructure paramViewStructure, int paramInt)
  {
    paramViewStructure.setAutofillId(getAutofillId());
    onProvideAutofillStructure(paramViewStructure, paramInt);
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchThawSelfOnly(paramSparseArray);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return DatePicker.class.getName();
  }
  
  public int getAutofillType()
  {
    int i;
    if (isEnabled()) {
      i = 4;
    } else {
      i = 0;
    }
    return i;
  }
  
  public AutofillValue getAutofillValue()
  {
    AutofillValue localAutofillValue;
    if (isEnabled()) {
      localAutofillValue = mDelegate.getAutofillValue();
    } else {
      localAutofillValue = null;
    }
    return localAutofillValue;
  }
  
  @Deprecated
  public CalendarView getCalendarView()
  {
    return mDelegate.getCalendarView();
  }
  
  @Deprecated
  public boolean getCalendarViewShown()
  {
    return mDelegate.getCalendarViewShown();
  }
  
  public int getDayOfMonth()
  {
    return mDelegate.getDayOfMonth();
  }
  
  public int getFirstDayOfWeek()
  {
    return mDelegate.getFirstDayOfWeek();
  }
  
  public long getMaxDate()
  {
    return mDelegate.getMaxDate().getTimeInMillis();
  }
  
  public long getMinDate()
  {
    return mDelegate.getMinDate().getTimeInMillis();
  }
  
  public int getMode()
  {
    return mMode;
  }
  
  public int getMonth()
  {
    return mDelegate.getMonth();
  }
  
  @Deprecated
  public boolean getSpinnersShown()
  {
    return mDelegate.getSpinnersShown();
  }
  
  public int getYear()
  {
    return mDelegate.getYear();
  }
  
  public void init(int paramInt1, int paramInt2, int paramInt3, OnDateChangedListener paramOnDateChangedListener)
  {
    mDelegate.init(paramInt1, paramInt2, paramInt3, paramOnDateChangedListener);
  }
  
  public boolean isEnabled()
  {
    return mDelegate.isEnabled();
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    mDelegate.onConfigurationChanged(paramConfiguration);
  }
  
  public void onPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onPopulateAccessibilityEventInternal(paramAccessibilityEvent);
    mDelegate.onPopulateAccessibilityEvent(paramAccessibilityEvent);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (View.BaseSavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    mDelegate.onRestoreInstanceState(paramParcelable);
  }
  
  protected Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    return mDelegate.onSaveInstanceState(localParcelable);
  }
  
  @Deprecated
  public void setCalendarViewShown(boolean paramBoolean)
  {
    mDelegate.setCalendarViewShown(paramBoolean);
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    if (mDelegate.isEnabled() == paramBoolean) {
      return;
    }
    super.setEnabled(paramBoolean);
    mDelegate.setEnabled(paramBoolean);
  }
  
  public void setFirstDayOfWeek(int paramInt)
  {
    if ((paramInt >= 1) && (paramInt <= 7))
    {
      mDelegate.setFirstDayOfWeek(paramInt);
      return;
    }
    throw new IllegalArgumentException("firstDayOfWeek must be between 1 and 7");
  }
  
  public void setMaxDate(long paramLong)
  {
    mDelegate.setMaxDate(paramLong);
  }
  
  public void setMinDate(long paramLong)
  {
    mDelegate.setMinDate(paramLong);
  }
  
  public void setOnDateChangedListener(OnDateChangedListener paramOnDateChangedListener)
  {
    mDelegate.setOnDateChangedListener(paramOnDateChangedListener);
  }
  
  @Deprecated
  public void setSpinnersShown(boolean paramBoolean)
  {
    mDelegate.setSpinnersShown(paramBoolean);
  }
  
  public void setValidationCallback(ValidationCallback paramValidationCallback)
  {
    mDelegate.setValidationCallback(paramValidationCallback);
  }
  
  public void updateDate(int paramInt1, int paramInt2, int paramInt3)
  {
    mDelegate.updateDate(paramInt1, paramInt2, paramInt3);
  }
  
  static abstract class AbstractDatePickerDelegate
    implements DatePicker.DatePickerDelegate
  {
    protected DatePicker.OnDateChangedListener mAutoFillChangeListener;
    private long mAutofilledValue;
    protected Context mContext;
    protected Calendar mCurrentDate;
    protected Locale mCurrentLocale;
    protected DatePicker mDelegator;
    protected DatePicker.OnDateChangedListener mOnDateChangedListener;
    protected DatePicker.ValidationCallback mValidationCallback;
    
    public AbstractDatePickerDelegate(DatePicker paramDatePicker, Context paramContext)
    {
      mDelegator = paramDatePicker;
      mContext = paramContext;
      setCurrentLocale(Locale.getDefault());
    }
    
    public final void autofill(AutofillValue paramAutofillValue)
    {
      if ((paramAutofillValue != null) && (paramAutofillValue.isDate()))
      {
        long l = paramAutofillValue.getDateValue();
        paramAutofillValue = Calendar.getInstance(mCurrentLocale);
        paramAutofillValue.setTimeInMillis(l);
        updateDate(paramAutofillValue.get(1), paramAutofillValue.get(2), paramAutofillValue.get(5));
        mAutofilledValue = l;
        return;
      }
      String str = DatePicker.LOG_TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramAutofillValue);
      localStringBuilder.append(" could not be autofilled into ");
      localStringBuilder.append(this);
      Log.w(str, localStringBuilder.toString());
    }
    
    public final AutofillValue getAutofillValue()
    {
      long l;
      if (mAutofilledValue != 0L) {
        l = mAutofilledValue;
      } else {
        l = mCurrentDate.getTimeInMillis();
      }
      return AutofillValue.forDate(l);
    }
    
    protected String getFormattedCurrentDate()
    {
      return DateUtils.formatDateTime(mContext, mCurrentDate.getTimeInMillis(), 22);
    }
    
    protected void onLocaleChanged(Locale paramLocale) {}
    
    public void onPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
    {
      paramAccessibilityEvent.getText().add(getFormattedCurrentDate());
    }
    
    protected void onValidationChanged(boolean paramBoolean)
    {
      if (mValidationCallback != null) {
        mValidationCallback.onValidationChanged(paramBoolean);
      }
    }
    
    protected void resetAutofilledValue()
    {
      mAutofilledValue = 0L;
    }
    
    public void setAutoFillChangeListener(DatePicker.OnDateChangedListener paramOnDateChangedListener)
    {
      mAutoFillChangeListener = paramOnDateChangedListener;
    }
    
    protected void setCurrentLocale(Locale paramLocale)
    {
      if (!paramLocale.equals(mCurrentLocale))
      {
        mCurrentLocale = paramLocale;
        onLocaleChanged(paramLocale);
      }
    }
    
    public void setOnDateChangedListener(DatePicker.OnDateChangedListener paramOnDateChangedListener)
    {
      mOnDateChangedListener = paramOnDateChangedListener;
    }
    
    public void setValidationCallback(DatePicker.ValidationCallback paramValidationCallback)
    {
      mValidationCallback = paramValidationCallback;
    }
    
    static class SavedState
      extends View.BaseSavedState
    {
      public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
      {
        public DatePicker.AbstractDatePickerDelegate.SavedState createFromParcel(Parcel paramAnonymousParcel)
        {
          return new DatePicker.AbstractDatePickerDelegate.SavedState(paramAnonymousParcel, null);
        }
        
        public DatePicker.AbstractDatePickerDelegate.SavedState[] newArray(int paramAnonymousInt)
        {
          return new DatePicker.AbstractDatePickerDelegate.SavedState[paramAnonymousInt];
        }
      };
      private final int mCurrentView;
      private final int mListPosition;
      private final int mListPositionOffset;
      private final long mMaxDate;
      private final long mMinDate;
      private final int mSelectedDay;
      private final int mSelectedMonth;
      private final int mSelectedYear;
      
      private SavedState(Parcel paramParcel)
      {
        super();
        mSelectedYear = paramParcel.readInt();
        mSelectedMonth = paramParcel.readInt();
        mSelectedDay = paramParcel.readInt();
        mMinDate = paramParcel.readLong();
        mMaxDate = paramParcel.readLong();
        mCurrentView = paramParcel.readInt();
        mListPosition = paramParcel.readInt();
        mListPositionOffset = paramParcel.readInt();
      }
      
      public SavedState(Parcelable paramParcelable, int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2)
      {
        this(paramParcelable, paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, 0, 0, 0);
      }
      
      public SavedState(Parcelable paramParcelable, int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, int paramInt4, int paramInt5, int paramInt6)
      {
        super();
        mSelectedYear = paramInt1;
        mSelectedMonth = paramInt2;
        mSelectedDay = paramInt3;
        mMinDate = paramLong1;
        mMaxDate = paramLong2;
        mCurrentView = paramInt4;
        mListPosition = paramInt5;
        mListPositionOffset = paramInt6;
      }
      
      public int getCurrentView()
      {
        return mCurrentView;
      }
      
      public int getListPosition()
      {
        return mListPosition;
      }
      
      public int getListPositionOffset()
      {
        return mListPositionOffset;
      }
      
      public long getMaxDate()
      {
        return mMaxDate;
      }
      
      public long getMinDate()
      {
        return mMinDate;
      }
      
      public int getSelectedDay()
      {
        return mSelectedDay;
      }
      
      public int getSelectedMonth()
      {
        return mSelectedMonth;
      }
      
      public int getSelectedYear()
      {
        return mSelectedYear;
      }
      
      public void writeToParcel(Parcel paramParcel, int paramInt)
      {
        super.writeToParcel(paramParcel, paramInt);
        paramParcel.writeInt(mSelectedYear);
        paramParcel.writeInt(mSelectedMonth);
        paramParcel.writeInt(mSelectedDay);
        paramParcel.writeLong(mMinDate);
        paramParcel.writeLong(mMaxDate);
        paramParcel.writeInt(mCurrentView);
        paramParcel.writeInt(mListPosition);
        paramParcel.writeInt(mListPositionOffset);
      }
    }
  }
  
  static abstract interface DatePickerDelegate
  {
    public abstract void autofill(AutofillValue paramAutofillValue);
    
    public abstract boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent);
    
    public abstract AutofillValue getAutofillValue();
    
    public abstract CalendarView getCalendarView();
    
    public abstract boolean getCalendarViewShown();
    
    public abstract int getDayOfMonth();
    
    public abstract int getFirstDayOfWeek();
    
    public abstract Calendar getMaxDate();
    
    public abstract Calendar getMinDate();
    
    public abstract int getMonth();
    
    public abstract boolean getSpinnersShown();
    
    public abstract int getYear();
    
    public abstract void init(int paramInt1, int paramInt2, int paramInt3, DatePicker.OnDateChangedListener paramOnDateChangedListener);
    
    public abstract boolean isEnabled();
    
    public abstract void onConfigurationChanged(Configuration paramConfiguration);
    
    public abstract void onPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent);
    
    public abstract void onRestoreInstanceState(Parcelable paramParcelable);
    
    public abstract Parcelable onSaveInstanceState(Parcelable paramParcelable);
    
    public abstract void setAutoFillChangeListener(DatePicker.OnDateChangedListener paramOnDateChangedListener);
    
    public abstract void setCalendarViewShown(boolean paramBoolean);
    
    public abstract void setEnabled(boolean paramBoolean);
    
    public abstract void setFirstDayOfWeek(int paramInt);
    
    public abstract void setMaxDate(long paramLong);
    
    public abstract void setMinDate(long paramLong);
    
    public abstract void setOnDateChangedListener(DatePicker.OnDateChangedListener paramOnDateChangedListener);
    
    public abstract void setSpinnersShown(boolean paramBoolean);
    
    public abstract void setValidationCallback(DatePicker.ValidationCallback paramValidationCallback);
    
    public abstract void updateDate(int paramInt1, int paramInt2, int paramInt3);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DatePickerMode {}
  
  public static abstract interface OnDateChangedListener
  {
    public abstract void onDateChanged(DatePicker paramDatePicker, int paramInt1, int paramInt2, int paramInt3);
  }
  
  public static abstract interface ValidationCallback
  {
    public abstract void onValidationChanged(boolean paramBoolean);
  }
}
