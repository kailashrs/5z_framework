package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.icu.util.Calendar;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.autofill.AutofillValue;
import com.android.internal.R.styleable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
import libcore.icu.LocaleData;

public class TimePicker
  extends FrameLayout
{
  private static final String LOG_TAG = TimePicker.class.getSimpleName();
  public static final int MODE_CLOCK = 2;
  public static final int MODE_SPINNER = 1;
  private final TimePickerDelegate mDelegate;
  private final int mMode;
  
  public TimePicker(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public TimePicker(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843933);
  }
  
  public TimePicker(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public TimePicker(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    if (getImportantForAutofill() == 0) {
      setImportantForAutofill(1);
    }
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TimePicker, paramInt1, paramInt2);
    boolean bool = localTypedArray.getBoolean(10, false);
    int i = localTypedArray.getInt(8, 1);
    localTypedArray.recycle();
    if ((i == 2) && (bool)) {
      mMode = paramContext.getResources().getInteger(17694980);
    } else {
      mMode = i;
    }
    if (mMode != 2) {
      mDelegate = new TimePickerSpinnerDelegate(this, paramContext, paramAttributeSet, paramInt1, paramInt2);
    } else {
      mDelegate = new TimePickerClockDelegate(this, paramContext, paramAttributeSet, paramInt1, paramInt2);
    }
    mDelegate.setAutoFillChangeListener(new _..Lambda.TimePicker.2FhAB9WgnLgn4zn4f9rRT7DNfjw(this, paramContext));
  }
  
  static String[] getAmPmStrings(Context paramContext)
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
  
  public CharSequence getAccessibilityClassName()
  {
    return TimePicker.class.getName();
  }
  
  public View getAmView()
  {
    return mDelegate.getAmView();
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
  
  public int getBaseline()
  {
    return mDelegate.getBaseline();
  }
  
  @Deprecated
  public Integer getCurrentHour()
  {
    return Integer.valueOf(getHour());
  }
  
  @Deprecated
  public Integer getCurrentMinute()
  {
    return Integer.valueOf(getMinute());
  }
  
  public int getHour()
  {
    return mDelegate.getHour();
  }
  
  public View getHourView()
  {
    return mDelegate.getHourView();
  }
  
  public int getMinute()
  {
    return mDelegate.getMinute();
  }
  
  public View getMinuteView()
  {
    return mDelegate.getMinuteView();
  }
  
  public int getMode()
  {
    return mMode;
  }
  
  public View getPmView()
  {
    return mDelegate.getPmView();
  }
  
  public boolean is24HourView()
  {
    return mDelegate.is24Hour();
  }
  
  public boolean isEnabled()
  {
    return mDelegate.isEnabled();
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
  public void setCurrentHour(Integer paramInteger)
  {
    setHour(paramInteger.intValue());
  }
  
  @Deprecated
  public void setCurrentMinute(Integer paramInteger)
  {
    setMinute(paramInteger.intValue());
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    mDelegate.setEnabled(paramBoolean);
  }
  
  public void setHour(int paramInt)
  {
    mDelegate.setHour(MathUtils.constrain(paramInt, 0, 23));
  }
  
  public void setIs24HourView(Boolean paramBoolean)
  {
    if (paramBoolean == null) {
      return;
    }
    mDelegate.setIs24Hour(paramBoolean.booleanValue());
  }
  
  public void setMinute(int paramInt)
  {
    mDelegate.setMinute(MathUtils.constrain(paramInt, 0, 59));
  }
  
  public void setOnTimeChangedListener(OnTimeChangedListener paramOnTimeChangedListener)
  {
    mDelegate.setOnTimeChangedListener(paramOnTimeChangedListener);
  }
  
  public boolean validateInput()
  {
    return mDelegate.validateInput();
  }
  
  static abstract class AbstractTimePickerDelegate
    implements TimePicker.TimePickerDelegate
  {
    protected TimePicker.OnTimeChangedListener mAutoFillChangeListener;
    private long mAutofilledValue;
    protected final Context mContext;
    protected final TimePicker mDelegator;
    protected final Locale mLocale;
    protected TimePicker.OnTimeChangedListener mOnTimeChangedListener;
    
    public AbstractTimePickerDelegate(TimePicker paramTimePicker, Context paramContext)
    {
      mDelegator = paramTimePicker;
      mContext = paramContext;
      mLocale = getResourcesgetConfigurationlocale;
    }
    
    public final void autofill(AutofillValue paramAutofillValue)
    {
      if ((paramAutofillValue != null) && (paramAutofillValue.isDate()))
      {
        long l = paramAutofillValue.getDateValue();
        paramAutofillValue = Calendar.getInstance(mLocale);
        paramAutofillValue.setTimeInMillis(l);
        setDate(paramAutofillValue.get(11), paramAutofillValue.get(12));
        mAutofilledValue = l;
        return;
      }
      String str = TimePicker.LOG_TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramAutofillValue);
      localStringBuilder.append(" could not be autofilled into ");
      localStringBuilder.append(this);
      Log.w(str, localStringBuilder.toString());
    }
    
    public final AutofillValue getAutofillValue()
    {
      if (mAutofilledValue != 0L) {
        return AutofillValue.forDate(mAutofilledValue);
      }
      Calendar localCalendar = Calendar.getInstance(mLocale);
      localCalendar.set(11, getHour());
      localCalendar.set(12, getMinute());
      return AutofillValue.forDate(localCalendar.getTimeInMillis());
    }
    
    protected void resetAutofilledValue()
    {
      mAutofilledValue = 0L;
    }
    
    public void setAutoFillChangeListener(TimePicker.OnTimeChangedListener paramOnTimeChangedListener)
    {
      mAutoFillChangeListener = paramOnTimeChangedListener;
    }
    
    public void setOnTimeChangedListener(TimePicker.OnTimeChangedListener paramOnTimeChangedListener)
    {
      mOnTimeChangedListener = paramOnTimeChangedListener;
    }
    
    protected static class SavedState
      extends View.BaseSavedState
    {
      public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
      {
        public TimePicker.AbstractTimePickerDelegate.SavedState createFromParcel(Parcel paramAnonymousParcel)
        {
          return new TimePicker.AbstractTimePickerDelegate.SavedState(paramAnonymousParcel, null);
        }
        
        public TimePicker.AbstractTimePickerDelegate.SavedState[] newArray(int paramAnonymousInt)
        {
          return new TimePicker.AbstractTimePickerDelegate.SavedState[paramAnonymousInt];
        }
      };
      private final int mCurrentItemShowing;
      private final int mHour;
      private final boolean mIs24HourMode;
      private final int mMinute;
      
      private SavedState(Parcel paramParcel)
      {
        super();
        mHour = paramParcel.readInt();
        mMinute = paramParcel.readInt();
        int i = paramParcel.readInt();
        boolean bool = true;
        if (i != 1) {
          bool = false;
        }
        mIs24HourMode = bool;
        mCurrentItemShowing = paramParcel.readInt();
      }
      
      public SavedState(Parcelable paramParcelable, int paramInt1, int paramInt2, boolean paramBoolean)
      {
        this(paramParcelable, paramInt1, paramInt2, paramBoolean, 0);
      }
      
      public SavedState(Parcelable paramParcelable, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
      {
        super();
        mHour = paramInt1;
        mMinute = paramInt2;
        mIs24HourMode = paramBoolean;
        mCurrentItemShowing = paramInt3;
      }
      
      public int getCurrentItemShowing()
      {
        return mCurrentItemShowing;
      }
      
      public int getHour()
      {
        return mHour;
      }
      
      public int getMinute()
      {
        return mMinute;
      }
      
      public boolean is24HourMode()
      {
        return mIs24HourMode;
      }
      
      public void writeToParcel(Parcel paramParcel, int paramInt)
      {
        super.writeToParcel(paramParcel, paramInt);
        paramParcel.writeInt(mHour);
        paramParcel.writeInt(mMinute);
        paramParcel.writeInt(mIs24HourMode);
        paramParcel.writeInt(mCurrentItemShowing);
      }
    }
  }
  
  public static abstract interface OnTimeChangedListener
  {
    public abstract void onTimeChanged(TimePicker paramTimePicker, int paramInt1, int paramInt2);
  }
  
  static abstract interface TimePickerDelegate
  {
    public abstract void autofill(AutofillValue paramAutofillValue);
    
    public abstract boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent);
    
    public abstract View getAmView();
    
    public abstract AutofillValue getAutofillValue();
    
    public abstract int getBaseline();
    
    public abstract int getHour();
    
    public abstract View getHourView();
    
    public abstract int getMinute();
    
    public abstract View getMinuteView();
    
    public abstract View getPmView();
    
    public abstract boolean is24Hour();
    
    public abstract boolean isEnabled();
    
    public abstract void onPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent);
    
    public abstract void onRestoreInstanceState(Parcelable paramParcelable);
    
    public abstract Parcelable onSaveInstanceState(Parcelable paramParcelable);
    
    public abstract void setAutoFillChangeListener(TimePicker.OnTimeChangedListener paramOnTimeChangedListener);
    
    public abstract void setDate(int paramInt1, int paramInt2);
    
    public abstract void setEnabled(boolean paramBoolean);
    
    public abstract void setHour(int paramInt);
    
    public abstract void setIs24Hour(boolean paramBoolean);
    
    public abstract void setMinute(int paramInt);
    
    public abstract void setOnTimeChangedListener(TimePicker.OnTimeChangedListener paramOnTimeChangedListener);
    
    public abstract boolean validateInput();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TimePickerMode {}
}
