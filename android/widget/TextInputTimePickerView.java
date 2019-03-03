package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.View;

public class TextInputTimePickerView
  extends RelativeLayout
{
  private static final int AM = 0;
  public static final int AMPM = 2;
  public static final int HOURS = 0;
  public static final int MINUTES = 1;
  private static final int PM = 1;
  private final Spinner mAmPmSpinner;
  private final TextView mErrorLabel;
  private boolean mErrorShowing;
  private final EditText mHourEditText;
  private boolean mHourFormatStartsAtZero;
  private final TextView mHourLabel;
  private final TextView mInputSeparatorView;
  private boolean mIs24Hour;
  private OnValueTypedListener mListener;
  private final EditText mMinuteEditText;
  private final TextView mMinuteLabel;
  
  public TextInputTimePickerView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public TextInputTimePickerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public TextInputTimePickerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public TextInputTimePickerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    inflate(paramContext, 17367346, this);
    mHourEditText = ((EditText)findViewById(16909043));
    mMinuteEditText = ((EditText)findViewById(16909044));
    mInputSeparatorView = ((TextView)findViewById(16909046));
    mErrorLabel = ((TextView)findViewById(16909072));
    mHourLabel = ((TextView)findViewById(16909073));
    mMinuteLabel = ((TextView)findViewById(16909074));
    mHourEditText.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramAnonymousEditable)
      {
        TextInputTimePickerView.this.parseAndSetHourInternal(paramAnonymousEditable.toString());
      }
      
      public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
      
      public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
    });
    mMinuteEditText.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramAnonymousEditable)
      {
        TextInputTimePickerView.this.parseAndSetMinuteInternal(paramAnonymousEditable.toString());
      }
      
      public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
      
      public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
    });
    mAmPmSpinner = ((Spinner)findViewById(16908739));
    paramAttributeSet = TimePicker.getAmPmStrings(paramContext);
    paramContext = new ArrayAdapter(paramContext, 17367049);
    paramContext.add(TimePickerClockDelegate.obtainVerbatim(paramAttributeSet[0]));
    paramContext.add(TimePickerClockDelegate.obtainVerbatim(paramAttributeSet[1]));
    mAmPmSpinner.setAdapter(paramContext);
    mAmPmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        if (paramAnonymousInt == 0) {
          mListener.onValueChanged(2, 0);
        } else {
          mListener.onValueChanged(2, 1);
        }
      }
      
      public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {}
    });
  }
  
  private int getHourOfDayFromLocalizedHour(int paramInt)
  {
    int i = paramInt;
    int j;
    if (mIs24Hour)
    {
      j = i;
      if (!mHourFormatStartsAtZero)
      {
        j = i;
        if (paramInt == 24) {
          j = 0;
        }
      }
    }
    else
    {
      int k = i;
      if (!mHourFormatStartsAtZero)
      {
        k = i;
        if (paramInt == 12) {
          k = 0;
        }
      }
      j = k;
      if (mAmPmSpinner.getSelectedItemPosition() == 1) {
        j = k + 12;
      }
    }
    return j;
  }
  
  private boolean isValidLocalizedHour(int paramInt)
  {
    boolean bool1 = mHourFormatStartsAtZero;
    boolean bool2 = true;
    int i = bool1 ^ true;
    int j;
    if (mIs24Hour) {
      j = 23;
    } else {
      j = 11;
    }
    if ((paramInt < i) || (paramInt > j + i)) {
      bool2 = false;
    }
    return bool2;
  }
  
  private boolean parseAndSetHourInternal(String paramString)
  {
    try
    {
      int i = Integer.parseInt(paramString);
      if (!isValidLocalizedHour(i))
      {
        int j = mHourFormatStartsAtZero ^ true;
        int k;
        if (mIs24Hour) {
          k = 23;
        } else {
          k = 11 + j;
        }
        mListener.onValueChanged(0, getHourOfDayFromLocalizedHour(MathUtils.constrain(i, j, k)));
        return false;
      }
      mListener.onValueChanged(0, getHourOfDayFromLocalizedHour(i));
      return true;
    }
    catch (NumberFormatException paramString) {}
    return false;
  }
  
  private boolean parseAndSetMinuteInternal(String paramString)
  {
    try
    {
      int i = Integer.parseInt(paramString);
      if ((i >= 0) && (i <= 59))
      {
        mListener.onValueChanged(1, i);
        return true;
      }
      mListener.onValueChanged(1, MathUtils.constrain(i, 0, 59));
      return false;
    }
    catch (NumberFormatException paramString) {}
    return false;
  }
  
  private void setError(boolean paramBoolean)
  {
    mErrorShowing = paramBoolean;
    TextView localTextView = mErrorLabel;
    int i = 4;
    int j;
    if (paramBoolean) {
      j = 0;
    } else {
      j = 4;
    }
    localTextView.setVisibility(j);
    localTextView = mHourLabel;
    if (paramBoolean) {
      j = 4;
    } else {
      j = 0;
    }
    localTextView.setVisibility(j);
    localTextView = mMinuteLabel;
    if (paramBoolean) {
      j = i;
    } else {
      j = 0;
    }
    localTextView.setVisibility(j);
  }
  
  void setHourFormat(int paramInt)
  {
    mHourEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(paramInt) });
    mMinuteEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(paramInt) });
    LocaleList localLocaleList = mContext.getResources().getConfiguration().getLocales();
    mHourEditText.setImeHintLocales(localLocaleList);
    mMinuteEditText.setImeHintLocales(localLocaleList);
  }
  
  void setListener(OnValueTypedListener paramOnValueTypedListener)
  {
    mListener = paramOnValueTypedListener;
  }
  
  void updateSeparator(String paramString)
  {
    mInputSeparatorView.setText(paramString);
  }
  
  void updateTextInputValues(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2)
  {
    mIs24Hour = paramBoolean1;
    mHourFormatStartsAtZero = paramBoolean2;
    Spinner localSpinner = mAmPmSpinner;
    int i;
    if (paramBoolean1) {
      i = 4;
    } else {
      i = 0;
    }
    localSpinner.setVisibility(i);
    if (paramInt3 == 0) {
      mAmPmSpinner.setSelection(0);
    } else {
      mAmPmSpinner.setSelection(1);
    }
    mHourEditText.setText(String.format("%d", new Object[] { Integer.valueOf(paramInt1) }));
    mMinuteEditText.setText(String.format("%02d", new Object[] { Integer.valueOf(paramInt2) }));
    if (mErrorShowing) {
      validateInput();
    }
  }
  
  boolean validateInput()
  {
    boolean bool1 = parseAndSetHourInternal(mHourEditText.getText().toString());
    boolean bool2 = false;
    if ((bool1) && (parseAndSetMinuteInternal(mMinuteEditText.getText().toString()))) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    if (!bool1) {
      bool2 = true;
    }
    setError(bool2);
    return bool1;
  }
  
  static abstract interface OnValueTypedListener
  {
    public abstract void onValueChanged(int paramInt1, int paramInt2);
  }
}
