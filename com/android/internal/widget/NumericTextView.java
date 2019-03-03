package com.android.internal.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.KeyEvent;
import android.widget.TextView;

public class NumericTextView
  extends TextView
{
  private static final double LOG_RADIX = Math.log(10.0D);
  private static final int RADIX = 10;
  private int mCount;
  private OnValueChangedListener mListener;
  private int mMaxCount = 2;
  private int mMaxValue = 99;
  private int mMinValue = 0;
  private int mPreviousValue;
  private boolean mShowLeadingZeroes = true;
  private int mValue;
  
  public NumericTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setHintTextColor(getTextColors().getColorForState(StateSet.get(0), 0));
    setFocusable(true);
  }
  
  private boolean handleKeyUp(int paramInt)
  {
    boolean bool1 = false;
    if (paramInt == 67)
    {
      if (mCount > 0)
      {
        mValue /= 10;
        mCount -= 1;
      }
    }
    else
    {
      if (!isKeyCodeNumeric(paramInt)) {
        break label245;
      }
      if (mCount < mMaxCount)
      {
        paramInt = numericKeyCodeToInt(paramInt);
        paramInt = mValue * 10 + paramInt;
        if (paramInt <= mMaxValue)
        {
          mValue = paramInt;
          mCount += 1;
        }
      }
    }
    Object localObject;
    if (mCount > 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("%0");
      ((StringBuilder)localObject).append(mCount);
      ((StringBuilder)localObject).append("d");
      localObject = String.format(((StringBuilder)localObject).toString(), new Object[] { Integer.valueOf(mValue) });
    }
    else
    {
      localObject = "";
    }
    setText((CharSequence)localObject);
    if (mListener != null)
    {
      boolean bool2;
      if (mValue >= mMinValue) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      if ((mCount < mMaxCount) && (mValue * 10 <= mMaxValue)) {
        break label226;
      }
      bool1 = true;
      label226:
      mListener.onValueChanged(this, mValue, bool2, bool1);
    }
    return true;
    label245:
    return false;
  }
  
  private static boolean isKeyCodeNumeric(int paramInt)
  {
    boolean bool;
    if ((paramInt != 7) && (paramInt != 8) && (paramInt != 9) && (paramInt != 10) && (paramInt != 11) && (paramInt != 12) && (paramInt != 13) && (paramInt != 14) && (paramInt != 15) && (paramInt != 16)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static int numericKeyCodeToInt(int paramInt)
  {
    return paramInt - 7;
  }
  
  private void updateDisplayedValue()
  {
    Object localObject;
    if (mShowLeadingZeroes)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("%0");
      ((StringBuilder)localObject).append(mMaxCount);
      ((StringBuilder)localObject).append("d");
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = "%d";
    }
    setText(String.format((String)localObject, new Object[] { Integer.valueOf(mValue) }));
  }
  
  private void updateMinimumWidth()
  {
    CharSequence localCharSequence = getText();
    int i = 0;
    int j = 0;
    while (j < mMaxValue)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("%0");
      localStringBuilder.append(mMaxCount);
      localStringBuilder.append("d");
      setText(String.format(localStringBuilder.toString(), new Object[] { Integer.valueOf(j) }));
      measure(0, 0);
      int k = getMeasuredWidth();
      int m = i;
      if (k > i) {
        m = k;
      }
      j++;
      i = m;
    }
    setText(localCharSequence);
    setMinWidth(i);
    setMinimumWidth(i);
  }
  
  public final OnValueChangedListener getOnDigitEnteredListener()
  {
    return mListener;
  }
  
  public final int getRangeMaximum()
  {
    return mMaxValue;
  }
  
  public final int getRangeMinimum()
  {
    return mMinValue;
  }
  
  public final boolean getShowLeadingZeroes()
  {
    return mShowLeadingZeroes;
  }
  
  public final int getValue()
  {
    return mValue;
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    if (paramBoolean)
    {
      mPreviousValue = mValue;
      mValue = 0;
      mCount = 0;
      setHint(getText());
      setText("");
    }
    else
    {
      if (mCount == 0)
      {
        mValue = mPreviousValue;
        setText(getHint());
        setHint("");
      }
      if (mValue < mMinValue) {
        mValue = mMinValue;
      }
      setValue(mValue);
      if (mListener != null) {
        mListener.onValueChanged(this, mValue, true, true);
      }
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool;
    if ((!isKeyCodeNumeric(paramInt)) && (paramInt != 67) && (!super.onKeyDown(paramInt, paramKeyEvent))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean onKeyMultiple(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    boolean bool;
    if ((!isKeyCodeNumeric(paramInt1)) && (paramInt1 != 67) && (!super.onKeyMultiple(paramInt1, paramInt2, paramKeyEvent))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool;
    if ((!handleKeyUp(paramInt)) && (!super.onKeyUp(paramInt, paramKeyEvent))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public final void setOnDigitEnteredListener(OnValueChangedListener paramOnValueChangedListener)
  {
    mListener = paramOnValueChangedListener;
  }
  
  public final void setRange(int paramInt1, int paramInt2)
  {
    if (mMinValue != paramInt1) {
      mMinValue = paramInt1;
    }
    if (mMaxValue != paramInt2)
    {
      mMaxValue = paramInt2;
      mMaxCount = (1 + (int)(Math.log(paramInt2) / LOG_RADIX));
      updateMinimumWidth();
      updateDisplayedValue();
    }
  }
  
  public final void setShowLeadingZeroes(boolean paramBoolean)
  {
    if (mShowLeadingZeroes != paramBoolean)
    {
      mShowLeadingZeroes = paramBoolean;
      updateDisplayedValue();
    }
  }
  
  public final void setValue(int paramInt)
  {
    if (mValue != paramInt)
    {
      mValue = paramInt;
      updateDisplayedValue();
    }
  }
  
  public static abstract interface OnValueChangedListener
  {
    public abstract void onValueChanged(NumericTextView paramNumericTextView, int paramInt, boolean paramBoolean1, boolean paramBoolean2);
  }
}
