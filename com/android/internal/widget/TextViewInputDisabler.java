package com.android.internal.widget;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.TextView;

public class TextViewInputDisabler
{
  private InputFilter[] mDefaultFilters;
  private InputFilter[] mNoInputFilters = { new InputFilter()
  {
    public CharSequence filter(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, Spanned paramAnonymousSpanned, int paramAnonymousInt3, int paramAnonymousInt4)
    {
      return "";
    }
  } };
  private TextView mTextView;
  
  public TextViewInputDisabler(TextView paramTextView)
  {
    mTextView = paramTextView;
    mDefaultFilters = mTextView.getFilters();
  }
  
  public void setInputEnabled(boolean paramBoolean)
  {
    TextView localTextView = mTextView;
    InputFilter[] arrayOfInputFilter;
    if (paramBoolean) {
      arrayOfInputFilter = mDefaultFilters;
    } else {
      arrayOfInputFilter = mNoInputFilters;
    }
    localTextView.setFilters(arrayOfInputFilter);
  }
}
